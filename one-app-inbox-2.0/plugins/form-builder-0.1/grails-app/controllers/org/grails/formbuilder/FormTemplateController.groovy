package org.grails.formbuilder
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import freemarker.template.Template
import grails.converters.JSON;

import com.oneapp.cloud.core.*

class FormTemplateController {

	def index = { redirect(action: "list", params: params) }

	// the delete, save and update actions only accept POST requests
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def domainClassService
	def springSecurityService
	def formTemplateService
	def sessionFactory
	def sqlDomainClassService
	def freemarkerConfig
	def list = {
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		params.offset = params.offset?:0
		if ( !params.sort ){
			params.sort = 'dateCreated'
			params.order = 'desc'
		}
		def currentUser = springSecurityService.currentUser
		def formTemplateInstanceList = FormTemplate.createCriteria().list(sort:params.sort,order:params.order,max:params.max,offset:params.offset){
			or{
				eq 'global',true
				form{
					eq 'tenantId',currentUser.userTenantId.toInteger()
					if(currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
						eq 'createdBy.id',currentUser.id
					}
				}
			}
		}

		[formTemplateInstanceList: formTemplateInstanceList, formTemplateInstanceTotal: formTemplateInstanceList.totalCount]
	}

	def create = {
		def formTemplateInstance = new FormTemplate()
		formTemplateInstance.properties = params
		def formList
		def currentUser = springSecurityService.currentUser
		formList = Form.createCriteria().list(){
			eq 'tenantId',currentUser.userTenantId.toInteger()
			ne 'formCat','S'
			if(currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
				eq 'createdBy.id',currentUser.id
			}
		}
		render(view: "create", model: [formTemplateInstance: formTemplateInstance, formList: formList])
	}

	def copy = {
		def formCount
		def formsAllowed
		def appConf = ApplicationConf.read(1)
		def currentUser = springSecurityService.currentUser
		def isTrialUser = currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)
		if(isTrialUser){
			formCount = Form.countByCreatedBy(currentUser)
			formsAllowed = appConf.formForTrial?:2
		}else{
			formCount = Form.count()
			formsAllowed = appConf.form
		}
		def formCat = params.formCat?:"N"
		def u = User.get( currentUser.id)
		if(formCount < formsAllowed){
			def clientInstance = Client.get(currentUser.userTenantId)
			def currentClientFormCount = Form.findAllByTenantId(u.userTenantId).size()
			if(currentClientFormCount < clientInstance.form){
				def formTemplateInstance = FormTemplate.get(params.id)
				if (!formTemplateInstance || (!formTemplateInstance?.global && formTemplateInstance?.form?.tenantId!=currentUser?.userTenantId && formTemplateInstance?.form?.createdBy?.id != currentUser.id)) {
					flash.message = "formTemplate.not.found"
					flash.args = [params.id]
					flash.defaultMessage = "FormTemplate not found with id ${params.id}"
					redirect(action: "list")
				}else {
					Form copyForm = Form.get(formTemplateInstance.form.id)
					Form newForm= new Form()

					try{
						newForm.tenantId = u.userTenantId

						// update the form name
						def formCnt = new FormCounter().save(flush:true)
						newForm.name = "Form${formCnt.id}"

						copyForm.fieldsList.each{Field obj ->
							if(!isTrialUser || (obj.type != 'Paypal')){
								Field field = new Field()
								field.name = obj.name+formCnt.id
								field.type = obj.type
								def settings = obj.settings
								def jsonSettings = JSON.parse(obj.settings)
								jsonSettings.mapMasterForm = ""
								jsonSettings.mapMasterField = ""
								if(obj.type == 'SubForm'){
									if(jsonSettings.subForm){
										Form subForm = Form.read(jsonSettings.subForm)
										if(subForm){
											try{
												Form newSubForm = new Form()
												newSubForm.tenantId = u.userTenantId
												def subFormCnt = new FormCounter().save(flush:true)
												newSubForm.name = "Form${subFormCnt.id}"
												subForm.fieldsList.each{Field subField->
													Field newSubField = new Field()
													newSubField.name = subField.name+subFormCnt.id
													newSubField.type = subField.type
													def subSettings = subField.settings
													def jsonSubSettings = JSON.parse(subSettings)
													jsonSubSettings.mapMasterForm = ""
													jsonSubSettings.mapMasterField = ""
													newSubField.settings = jsonSubSettings.toString()
													newSubField.sequence = subField.sequence
													newSubForm.fieldsList.add(newSubField)
												}
												newSubForm.description = subForm.description
												def jsonSubFormSettings = JSON.parse(subForm.settings)
												jsonSubFormSettings.masterForms = []
												newSubForm.settings = jsonSubFormSettings.toString()
												newSubForm.domainClass = domainClassService.getDomainClass(newSubForm)
												newSubForm.persistableFieldsCount = newSubForm.fieldsList.size()
												newSubForm.createdBy = currentUser
												newSubForm.formCat = subForm.formCat
												newSubForm.save(flush:true)
												sqlDomainClassService.createForm(newSubForm)
												jsonSettings.oldSubForm=""
												jsonSettings.subForm = "${newSubForm.id}"
											}catch(Exception e){
												log.error "FormTemplateController-copy: Problem creating subform, from subform(${subForm?.id}, mainForm(${copyForm?.id}))"+e
												println "FormTemplateController-copy: Problem creating subform, from subform(${subForm?.id}, mainForm(${copyForm?.id}))"+e
											}
										}else{
											jsonSettings.oldSubForm=""
											jsonSettings.subForm = ""
										}
									}
								}
								field.settings = jsonSettings.toString()
								field.sequence = obj.sequence
								newForm.fieldsList.add(field)
							}
						}

						newForm.description = copyForm.description
						def jsonFormSettings = JSON.parse(copyForm.settings)
						jsonFormSettings.masterForms = []
						newForm.settings=jsonFormSettings.toString()
						newForm.formCat = copyForm.formCat
						newForm.domainClass = domainClassService.getDomainClass(newForm)
						newForm.persistableFieldsCount = formTemplateService.getPersistableFieldsCount(newForm.fieldsList)
						//    domainClassService.registerDomainClass newForm.domainClass.source
						newForm.createdBy = currentUser
						newForm.save(flush:true)
						sqlDomainClassService.createForm(newForm)
						flash.message = "${message(code: 'form.template.create', args: [message(code: 'form.label', default: 'Form'), newForm?:newForm.id, g.link(controller:'formAdmin',action:'create',params:[formId:newForm.id]){message(code:'form.created.formAdminClickHere',default:'here')}])}"
						sessionFactory.currentSession.flush()
						sessionFactory.currentSession.clear()
						redirect(controller:"form",action: "edit",id: newForm.id)
					}catch ( Exception ex ) {

						// Delete the domain class if exists
						try{

							def session = sessionFactory.getCurrentSession()
							def connection = session.connection()
							def state = connection.createStatement()
							state.addBatch("drop table ${newForm.name}")
							state.executeBatch()

						}catch(Exception e){
							println "FormTemplateController-copy while destroying the table in case of exception: "+e
							//ignore
						}finally{
							sessionFactory.currentSession.flush()
							sessionFactory.currentSession.clear()
						}
						flash.message = "formTemplate.exception"
						flash.args = [formTemplateInstance.id]
						flash.defaultMessage = "Form Template ${formTemplateInstance.id} cannot be copied. Exception $ex"
						redirect(action: "list")

					}

				}
			}else{
				flash.message ="${message(code: 'client.form.maxlimit',args:[clientInstance.form,g.link(controller:'dropDown',action:'clientUsage'){message(code:'form.creation.maxlimit.linkText','default':'here')}],default:'You can only create ${clientInstance.form} forms.')}"
				flash.defaultMessage = flash.message
				redirect(action: "list")
			}
		}else{
			flash.message ="${message(code: 'form.creation.maxlimit',args:[message(code:'form.creation.maxlimit.linkText','default':'here')], default:'You have reached the maximum form creation Limit. Please contact your Administrator')}"
			flash.defaultMessage = flash.message
			redirect(action: "list")

		}
	}

	def save = {
		def formTemplateInstance = new FormTemplate(params)
		def currentUser = springSecurityService.currentUser
		def isTrialUser = currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)
		//Check if trial user he can create templates using his own forms
		if(!isTrialUser || formTemplateInstance?.form?.createdBy?.id == currentUser.id){
			formTemplateInstance.createdBy = currentUser
			if (!formTemplateInstance.hasErrors() && formTemplateInstance.save()) {
				if(params.formTeplateImg && params.formTeplateImg.size>0){
					def contentType=[
						'image/jpeg',
						'image/gif',
						'image/png',
						'image/bmp'
					]
					if(contentType.contains(params?.formTeplateImg?.getContentType())){
						attachUploadedFilesTo(formTemplateInstance)
						flash.message = "formTemplate.created"
						flash.args = [formTemplateInstance.id]
						flash.defaultMessage = "FormTemplate ${formTemplateInstance.id} created"
					}else{
						flash.message = "formTeplateImg.not.created"
						flash.defaultMessage = "<span style='color: red;'>Please upload image only</span>"
					}
					redirect(action: "edit", id: formTemplateInstance.id)
					return
				}
				flash.message = "formTemplate.created"
				flash.args = [formTemplateInstance.id]
				flash.defaultMessage = "FormTemplate ${formTemplateInstance.id} created"
				redirect(action: "edit", id: formTemplateInstance.id)
			} else {
				def formList
				formList = Form.createCriteria().list(){
					eq 'tenantId',currentUser.userTenantId.toInteger()
					ne 'formCat','S'
					if(isTrialUser){
						eq 'createdBy.id',currentUser.id
					}
				}
				render(view: "create", model: [formTemplateInstance: formTemplateInstance, formList: formList])
			}
		}else{
			flash.message = "formTeplate.not.accessible"
			flash.defaultMessage = "Sorry, this template is not accessible to you."
			redirect(action: "list")
		}
	}


	def edit = {
		def formTemplateInstance = FormTemplate.get(params.id)
		if (!formTemplateInstance) {
			flash.message = "formTemplate.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "FormTemplate not found with id ${params.id}"
			redirect(action: "list")
		}
		else {
			return [formTemplateInstance: formTemplateInstance]
		}
	}

	def update = {
		def formTemplateInstance = FormTemplate.get(params.id)
		if (formTemplateInstance) {
			if (params.version) {
				def version = params.version.toLong()
				if (formTemplateInstance.version > version) {

					formTemplateInstance.errors.rejectValue("version", "formTemplate.optimistic.locking.failure", "Another user has updated this FormTemplate while you were editing")
					render(view: "edit", model: [formTemplateInstance: formTemplateInstance])
					return
				}
			}
			formTemplateInstance.properties = params
			formTemplateInstance.updatedBy = springSecurityService.currentUser
			if (!formTemplateInstance.hasErrors() && formTemplateInstance.save()) {
				if(params.formTeplateImg && params.formTeplateImg.size>0){
					def contentType=[
						'image/jpeg',
						'image/gif',
						'image/png',
						'image/bmp'
					]
					def a=params?.formTeplateImg
					if(contentType.contains(params?.formTeplateImg?.getContentType())){
						attachUploadedFilesTo(formTemplateInstance)
						flash.message = "formTemplate.updated"
						flash.args = [params.id]
						flash.defaultMessage = "FormTemplate ${params.id} updated"

					}else{
						flash.message = "formTeplateImg.not.created"
						flash.defaultMessage = "<span style='color: red;'>Please only upload image</span>"
					}
					redirect(action: "edit", id: formTemplateInstance.id)
					return
				}

				redirect(action: "edit", id: formTemplateInstance.id)
			}
			else {
				render(view: "edit", model: [formTemplateInstance: formTemplateInstance])
			}
		}
		else {
			flash.message = "formTemplate.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "FormTemplate not found with id ${params.id}"
			redirect(action: "edit", id: params.id)
		}
	}

	def delete = {
		def formTemplateInstance = FormTemplate.get(params.id)
		if (formTemplateInstance) {
			try {
				formTemplateInstance.delete()
				flash.message = "formTemplate.deleted"
				flash.args = [params.id]
				flash.defaultMessage = "FormTemplate ${params.id} deleted"
				redirect(action: "list")
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "formTemplate.not.deleted"
				flash.args = [params.id]
				flash.defaultMessage = "FormTemplate ${params.id} could not be deleted"
				redirect(action: "edit", id: params.id)
			}
		}
		else {
			flash.message = "formTemplate.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "FormTemplate not found with id ${params.id}"
			redirect(action: "list")
		}
	}
	def globalFormTemplates={
		params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		params.offset = params.offset?:0
		if ( !params.sort ){
			params.sort = 'dateCreated'
			params.order = 'desc'
		}
		def formTemplateInstanceList = FormTemplate.createCriteria().list(sort:params.sort,order:params.order,max:params.max,offset:params.offset){
			or{ eq 'global',true /*form{
				 eq 'tenantId',springSecurityService.currentUser.userTenantId.toInteger()
				 }*/ }
		}

		[formTemplateInstanceList: formTemplateInstanceList, formTemplateInstanceTotal: formTemplateInstanceList.totalCount]

	}
	def copyTemplate = {
		try{
			def formTemplateInstance = FormTemplate.get(params.id)
			if (!formTemplateInstance) {
				flash.message = "formTemplate.not.found"
				flash.args = [params.id]
				flash.defaultMessage = "FormTemplate not found with id ${params.id}"
				redirect(action: "list")
			}else {
				Form copyForm = Form.get(formTemplateInstance.form.id)
				Form newForm= new Form()

				try{

					// update the form name
					def formCnt = new FormCounter().save(flush:true)
					newForm.name = "Form${formCnt.id}"

					copyForm.fieldsList.each{Field obj ->
						if(obj.type != 'SubForm' && obj.type != 'Paypal'){
							Field field = new Field()
							field.name = obj.name+formCnt.id
							field.type = obj.type
							def settings = obj.settings
							def jsonSettings = JSON.parse(obj.settings)
							jsonSettings.mapMasterForm = ""
							jsonSettings.mapMasterField = ""
							/*if(obj.type == 'SubForm'){
							 if(jsonSettings.subForm){
							 Form subForm = Form.read(jsonSettings.subForm)
							 if(subForm){
							 try{
							 Form newSubForm = new Form()
							 newSubForm.tenantId = u.userTenantId
							 def subFormCnt = new FormCounter().save(flush:true)
							 newSubForm.name = "Form${subFormCnt.id}"
							 subForm.fieldsList.each{Field subField->
							 Field newSubField = new Field()
							 newSubField.name = subField.name+subFormCnt.id
							 newSubField.type = subField.type
							 def subSettings = subField.settings
							 def jsonSubSettings = JSON.parse(subSettings)
							 jsonSubSettings.mapMasterForm = ""
							 jsonSubSettings.mapMasterField = ""
							 newSubField.settings = jsonSubSettings.toString()
							 newSubField.sequence = subField.sequence
							 newSubForm.fieldsList.add(newSubField)
							 }
							 newSubForm.description = subForm.description
							 def jsonSubFormSettings = JSON.parse(subForm.settings)
							 jsonSubFormSettings.masterForms = []
							 newSubForm.settings = jsonSubFormSettings.toString()
							 newSubForm.domainClass = domainClassService.getDomainClass(newSubForm)
							 newSubForm.persistableFieldsCount = newSubForm.fieldsList.size()
							 newSubForm.createdBy = springSecurityService?.currentUser
							 newSubForm.formCat = subForm.formCat
							 newSubForm.save(flush:true)
							 jsonSettings.subForm = "${newSubForm.id}"
							 }catch(Exception e){
							 println "Problem creating subForm"
							 }
							 }else{
							 jsonSettings.subForm = ""
							 }
							 }
							 }*/
							field.settings = jsonSettings.toString()
							field.sequence = obj.sequence
							newForm.fieldsList.add(field)
						}
					}

					newForm.description = copyForm.description
					def jsonFormSettings = JSON.parse(copyForm.settings)
					jsonFormSettings.masterForms = []
					newForm.settings=jsonFormSettings.toString()
					newForm.formCat = copyForm.formCat
					//newForm.domainClass = domainClassService.getDomainClass(newForm)
					//newForm.persistableFieldsCount = newForm.fieldsList?.size()
					//    domainClassService.registerDomainClass newForm.domainClass.source
					renderView("create", newForm,formTemplateService.getCreateViewTemplate(request, flash, newForm))
				}catch ( Exception ex ) {
					println "FromTemplateController-copyTemplate: "+ex
					flash.message = "formTemplate.exception"
					flash.args = [formTemplateInstance.id]
					flash.defaultMessage = "Form Template ${formTemplateInstance.id} cannot be copied. Exception $ex"
					redirect(action: "globalFormTemplates")
				}
			}
		}catch(Exception e){
			redirect(controller:'home')
		}
	}
	private renderView(name, formInstance, templateText) {
		if (flash.formCounter == null)
			flash.formCounter = ""
		FastStringWriter out = new FastStringWriter()
		new Template(name,
				new StringReader(templateText),
				freemarkerConfig.configuration).process([formInstance: formInstance, flash: flash], out)
		render out.toString()
	}
}
