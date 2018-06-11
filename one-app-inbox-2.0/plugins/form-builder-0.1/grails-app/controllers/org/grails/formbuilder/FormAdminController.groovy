package org.grails.formbuilder
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;

import com.oneapp.cloud.core.Role;
import com.oneapp.cloud.core.User;
import com.oneapp.cloud.core.UserRole;

import grails.converters.JSON

/***
	
	User specific report/view for collected form data

***/
class FormAdminController{
	
	// variable used in case
	def objNotFoundRedir = grailsApplication.config.formBuilder.objectNotFound.redirection
	def mailChimpService
	def springSecurityService
	
	def index = {
		redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
	}
	
	def list = {
		redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
	}
	
	def create = {
	
		def form = Form.get(params.formId)
		if(!form) {
			flash.message = "form.not.found"
			flash.args = [params.formId]
			flash.defaultMessage = "Form not found with id ${params.formId}"
			redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
			return
		}else if(form.formCat=='S'){
			flash.message = "No form admin for sub-forms"
			flash.defaultMessage =flash.message
			redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
			return
		}
		def formAdminInstance = FormAdmin.findByForm(form)
		if(formAdminInstance){
			redirect(action:"edit",id:formAdminInstance.id)
		}
		formAdminInstance = new FormAdmin()
    	bindData(formAdminInstance, params, [exclude: ['formId']])
		formAdminInstance.form = form
    	def statusFields = new ArrayList()
		def userList
		def currentUser = springSecurityService.currentUser
		if(currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
			userList = [currentUser]
		}else{
			userList = User.findAllByUserTenantIdAndEnabled(currentUser.userTenantId,true)
		}
		userList = userList.findAll{it.authorities*.authority.contains(Role.ROLE_USER)}
		setFields(formAdminInstance,statusFields)
        return [formAdminInstance: formAdminInstance, statusFields: statusFields, userList:userList]
		
	}
	
	def save = {
		if(params['form.id']){
			def form = Form.read(params['form.id'])
			def formAdmin
			if(form) 
				formAdmin = FormAdmin.findByForm(form)
			if(formAdmin){
				flash.message = message('code':'formAdmin.already.created','default':'Form Settings already created')
				flash.defaultMessage = flash.message
				redirect(action:'edit',params:[id:formAdmin.id])
				return
			}
		}
		def formAdminInstance = new FormAdmin(params)
		
		def statusFields = new ArrayList()
		def userList = User.findAllByUserTenantIdAndEnabled(springSecurityService.currentUser.userTenantId,true)
		userList = userList.findAll{it.authorities*.authority.contains(Role.ROLE_USER)}
		try{
			setFields(formAdminInstance,statusFields)
		}catch(Exception e){
			log.error "FormAdminController-save1:"+e
			//TODO show proper message in case exception occurs
		}
	    try{
			if(formAdminInstance.statusField){
				formAdminInstance.formType = 'Approval'
			}else{
				formAdminInstance.formType = 'Survey'
			}
			if('Password'.equalsIgnoreCase(formAdminInstance.formLogin) && !formAdminInstance.formPassword){
				formAdminInstance.errors.rejectValue("formPassword", "formAdmin.formPassword.notNull", "Password Field for Password-Protected form can not be blank");
				render(view: "create", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, userList:userList])
				return
			}
			def redirectUrl
			if(params.redirectCheck){
				redirectUrl = formAdminInstance.redirectUrl
				if(redirectUrl && redirectUrl.indexOf("http") == -1){
					redirectUrl = "http://"+redirectUrl
				}
				if(!redirectUrl){
					formAdminInstance.errors.rejectValue("redirectUrl", "formAdmin.redirectUrl.notBlank", "Redirect URL should not be blank");
				}
			}
			formAdminInstance.redirectUrl = redirectUrl
			if('M'.equalsIgnoreCase(formAdminInstance.form.formCat)){
				formAdminInstance.formType = "Master"
			}else if('S'.equalsIgnoreCase(formAdminInstance.form.formCat)){
				formAdminInstance.formType = "Sub"
			}
			formAdminInstance.formSubmissionAction = "Eamil"
			formAdminInstance.formSubmissionTo = [formAdminInstance.form.createdBy]
			if (!formAdminInstance.hasErrors() && formAdminInstance.save()) {
				flash.message = "formAdmin.created"
				flash.args = [formAdminInstance.form]
				flash.defaultMessage = "Form Settings for ${formAdminInstance.form} created"
				redirect(action: "edit", id: formAdminInstance.id)
			}
			else {
				render(view: "create", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, userList:userList])
				return
			}
		}catch(Exception e){
			log.error "FormAdminController-save 2nd trycatch:"+e
			flash.message = "formAdmin.error.save"
			flash.args = []
			flash.defaultMessage = "Error saving form settings"
			render(view: "create", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, userList:userList])
			return
		}
	}
	
	def edit = {
	    def formAdminInstance = FormAdmin.get(params.id)
	    if (!formAdminInstance) {
	        flash.message = "formAdmin.not.found"
	        flash.args = [params.id]
	        flash.defaultMessage = "Form Admin not found with id ${params.id}"
	    	redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
	    } else {
		 	def mailChimpDetails=formAdminInstance?.mailChimpDetails?true:false
			def statusFields = new ArrayList()
			def userList
			def currentUser = springSecurityService.currentUser
			if(currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
				userList = [currentUser]
			}else{
				userList = User.findAllByUserTenantIdAndEnabled(currentUser.userTenantId,true)
			}
			def onlyUsers
			onlyUsers = userList.findAll{it.authorities*.authority.contains(Role.ROLE_USER)}
			try{
				setFields(formAdminInstance,statusFields)
			}catch(Exception e){
				log.error "FormAdminController-edit"+e
				//TODO show proper message in case exception occurs
			}
			return [formAdminInstance: formAdminInstance, statusFields: statusFields, webHookDetailsView:params.webHookDetailsView,onlyUsers:onlyUsers,userList:userList,mailChimpDetails:mailChimpDetails]
	    }
	}
	
	def update = {
        def formAdminInstance = FormAdmin.get(params.id)
		def userList
		def currentUser = springSecurityService.currentUser
		if(currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
			userList = [currentUser]
		}else{
			userList = User.findAllByUserTenantIdAndEnabled(currentUser.userTenantId,true)
		}
		def onlyUsers
		onlyUsers = userList.findAll{it.authorities*.authority.contains(Role.ROLE_USER)}
	    if (formAdminInstance) {
			def statusFields = new ArrayList()
			try{
				setFields(formAdminInstance,statusFields)
			}catch(Exception e){
				log.error "FormAdminController-update"+e
				//TODO show proper message in case exception occurs
			}
            if (params.version) {
                def version = params.version.toLong()
                if (formAdminInstance.version > version) {
                    formAdminInstance.errors.rejectValue("version", "formAdmin.optimistic.locking.failure", "Another user has updated this Form while you were editing")
                    render(view: "edit", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, onlyUsers:onlyUsers,userList:userList])
                    return
                }
            }
			def redirectUrl
			if(params.redirectCheck){
				redirectUrl = params.redirectUrl
				if(redirectUrl && redirectUrl.indexOf("http") == -1){
					redirectUrl = "http://"+redirectUrl
				}
				if(!redirectUrl){
					formAdminInstance.errors.rejectValue("redirectUrl", "formAdmin.redirectUrl.notBlank", "Redirect URL should not be blank");
					render(view: "edit", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, onlyUsers:onlyUsers,userList:userList])
					return
				}
			}
            if(params.un){
				formAdminInstance.formSubmissionTo = []
				redirectUrl = formAdminInstance.redirectUrl
			}else{
				formAdminInstance.publishedWith = []
			}
			def paramsMap = [:]
			paramsMap.putAll(params)
			if(!params.statusField?.id && !params.un){
				paramsMap.remove('statusField.id')
				paramsMap.remove('statusField')
			}
			
			formAdminInstance.properties = paramsMap
			formAdminInstance.redirectUrl = redirectUrl
			
			def condToBeRemd = formAdminInstance.conditions.findAll{!it || !it.val}
			formAdminInstance.conditions.removeAll(condToBeRemd)
			def condToBeDeld = formAdminInstance.conditions.findAll{it.status==NotiStatus.D || it.val.length()>255}
			condToBeDeld.each{
				formAdminInstance.conditions.remove(it)
				it.delete()
			}
			formAdminInstance.conditions = formAdminInstance.conditions.sort{it.sequence}
			formAdminInstance.conditions.each{
				if(it.val.length()>255)
					it.val = it.substring(0,255)
			}
			if( !params.un){
				if(paramsMap.statusField){
					formAdminInstance.formType = 'Approval'
				}else{
					formAdminInstance.formType = 'Survey'
					formAdminInstance.statusField = null
				}
			}
			
            if('Password'.equalsIgnoreCase(formAdminInstance.formLogin) && !formAdminInstance.formPassword){
				formAdminInstance.errors.rejectValue("formPassword", "formAdmin.formPassword.notNull", "Password Field for Password-Protected form can not be blank");
				render(view: "edit", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, onlyUsers:onlyUsers,userList:userList])
				return
			}
			def mailChimpDetails=formAdminInstance?.mailChimpDetails?true:false
			if (!formAdminInstance.hasErrors() && formAdminInstance.save(flush:true)) {
                flash.message = "formAdmin.updated"
                flash.args = [formAdminInstance.form]
                flash.defaultMessage = "Form Settings for ${formAdminInstance.form} updated"
                redirect(action: "edit", id: formAdminInstance.id,params:[mailChimpDetails:mailChimpDetails,un:params.un])
            }
            else {
                render(view: "edit", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, onlyUsers:onlyUsers,userList:userList])
				return
            }
        }
        else {
            flash.message = "formAdmin.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Form Admin not found with id ${params.id}"
            redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
        }
    }
	
	def updateFieldRules = {
		if(request.xhr){
			try{
				def formAdmin = FormAdmin.get(params.id)
				formAdmin.fieldRulesData = params.fieldRulesData
				formAdmin.save(flush:true)
				def data = ['status':'OK','version':formAdmin.version]
				render data as JSON
			}catch(Exception e){
				log.error "Problem updating Field Rules: "+e
				def data = ['status':'fail']
				render data as JSON
			}
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
	def updatePageRules = {
		if(request.xhr){
			try{
				
				def formAdmin = FormAdmin.get(params.id)
				formAdmin.pageRulesData = params.pageRulesData
				formAdmin.save(flush:true)
				def data = ['status':'OK','version':formAdmin.version]
				render data as JSON
			}catch(Exception e){
				log.error "Problem updating Page Rules: "+e
				def data = ['status':'fail']
				render data as JSON
			}
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}

	def saveW = {
        def formAdminInstance = FormAdmin.get(params.id)
		def userList
		def currentUser = springSecurityService.currentUser
		if(currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
			userList = [currentUser]
		}else{
			userList = User.findAllByUserTenantIdAndEnabled(currentUser.userTenantId,true)
		}
		def onlyUsers
		onlyUsers = userList.findAll{it.authorities*.authority.contains(Role.ROLE_USER)}
	    if (formAdminInstance) {
			def statusFields = new ArrayList()
			try{
				setFields(formAdminInstance,statusFields)
			}catch(Exception e){
				log.error "FormAdminController-saveW"+e
				//TODO show proper message in case exception occurs
			}
			def webHookDetails
			def textSavedUpdated
			if(formAdminInstance.webHookDetails){
            	webHookDetails = formAdminInstance.webHookDetails
				textSavedUpdated = "updated"
			}else{
				webHookDetails = new WebHookDetails()
				textSavedUpdated = "saved"
			}
			webHookDetails.properties = params.webHookDetails
			if (!webHookDetails.hasErrors() && webHookDetails.save(flush:true)) {
				formAdminInstance.webHookDetails = webHookDetails
				formAdminInstance.save()
				flash.message = "formAdmin.webHookDetails.${textSavedUpdated}"
                flash.args = [formAdminInstance.form]
                flash.defaultMessage = "WebHook Details for [${formAdminInstance.form}] ${textSavedUpdated}"
                redirect(action: "edit", id: formAdminInstance.id, params:[webHookDetailsView:'true'])
            }
            else {
				render(view: "edit", model: [formAdminInstance: formAdminInstance, statusFields: statusFields, webHookDetails:webHookDetails, webHookDetailsView:true, onlyUsers:onlyUsers,userList:userList])
				return
            }
        }
        else {
            flash.message = "formAdmin.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Form Admin not found with id ${params.id}"
            redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
        }
    }
	
	def deleteW = {
		try{
			def formAdminInstance = FormAdmin.get(params.id)
			def currentUser = springSecurityService.currentUser
			def allowDelete = false
			if(SpringSecurityUtils.ifAnyGranted(Role.ROLE_HR_MANAGER)){
				allowDelete = true
			}else if(currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER) && formAdminInstance?.form?.createdBy?.id == currentUser.id){
				allowDelete = true
			}
			if(allowDelete && formAdminInstance?.form?.tenantId == currentUser.userTenantId && formAdminInstance?.webHookDetails){
				def webHookDetails = formAdminInstance.webHookDetails
				formAdminInstance.webHookDetails=null
				
				if(formAdminInstance.save(flush:true)){
					webHookDetails?.delete()
					flash.message = "formAdmin.webHookDetails.deleted"
                flash.args = [formAdminInstance.form]
                flash.defaultMessage = "WebHook Details for [${formAdminInstance.form}] deleted"
					redirect(action: "edit", id: formAdminInstance.id, params:[webHookDetailsView:'true'])
				}else{
					throw new Exception("Could not delete form settings")
				}
			}else {
				flash.message = "formAdmin.not.found"
				flash.args = [params.id]
				flash.defaultMessage = "Form Settings not found with id ${params.id}"
				redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
			}
		}catch(Exception e){
			flash.message = "formAdmin.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "Form Settings not found with id ${params.id}"
			redirect(controller: objNotFoundRedir?.controller,action: objNotFoundRedir?.action)
		}
	}

	def setFields = {formAdminInstance,statusFields->
		def notKeyFigures = grailsApplication.config.formAdmin.fields.notKeyFigures
		formAdminInstance?.form?.fieldsList?.each { field ->
			if (!notKeyFigures.contains(field.type) && "dropdown".equalsIgnoreCase(field.type)) {
				def settings = JSON.parse(field.settings)
				statusFields?.add([id:field.id,label:"${settings.en.label}",values:settings.en.value])
			}
		}
	}
	def findMailChimpListByAjax={
		if(request.xhr){
			if(params.userapikey){
				def formAdminInstance=FormAdmin.get(params.id)
				
				def	formFields=[:]
				def data=mailChimpService.getLists(params.userapikey)
				def map=data.collect{d->
					 [id:d.id,name:d.name]
				}
				formAdminInstance?.form?.fieldsList.each{ field ->
					def settings = JSON.parse(field.settings)
					def type="CheckBox"
					if("CheckBox".equalsIgnoreCase(field.type)){
						if(!formFields."${type}")
							formFields."${type}" = []
						formFields."${type}" << [id:field.name,label:settings.en.label,value:settings.en.value]
					}
				}
				def result = [formFields:formFields,map:map]
				render result as JSON
			}
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	def findAllfieldsByAjax={
		if(request.xhr){
			if(params.userapikey && params.mailChimpDetailsList){
				def formAdminInstance=FormAdmin.get(params.id)
				
				def	formFields=[:]
				
				formAdminInstance?.form?.fieldsList.each{ field ->
					def settings = JSON.parse(field.settings)
					if(field.type!='SubForm'||field.type!='PageBreak'||field.type!='FileUpload'||
						   field.type!='PlainTextHref'||field.type!='PlainText'||
							field.type!='LinkVideo'||field.type!='ImageUpload'){
						def type=""
						if(field.type=='SingleLineDate'||(field.type=='FormulaField' && settings.en.newResultType)){
							type = "date"
						}else if(field.type=='SingleLineNumber'||(field.type=='FormulaField' && settings.en.newResultType)){
							type = "number"
						}else if(field.type=='SingleLineText' || field.type=='LookUp'){
							type = settings.restriction=='email'?'email':'text'
						}else if(field.type=='CheckBox'){
							type = "checkbox"
						}else if(field.type=='GroupButton'){
							type = "radio"
						}else if(field.type=='dropdown'){
							type = "dropdown"
						}
						else if(field.type=='Email'){
							type = "email"
						}
						if(!formFields."${type}")
							formFields."${type}" = []
						formFields."${type}" << [id:field.name,label:settings.en.label,value:settings.en.value]
					}
				}
				def data=mailChimpService.getListsFields(params.userapikey,params.mailChimpDetailsList)
				def mailChimpFields=data.collect{d->
					if(!(d.field_type=="radio"||d.field_type=="dropdown"||d.field_type=="birthday"||d.field_type=="zip"))
						[id:d.order,fieldType:d.field_type,name:d.name,req:d.req,tag:d.tag]
				}
				def dNObjs = mailChimpFields.findAll{it==null}
				mailChimpFields.removeAll(dNObjs)
				def groupings=mailChimpService.getlistInterestGroupings(params.userapikey,params.mailChimpDetailsList)
				def mailChimpgroups=groupings.collect{grp->
					[id:grp.id,fieldType:grp.form_field,name:grp.name,groups:grp.groups]
				}
				def result = [mailChimpFields:mailChimpFields,formFields:formFields,mailChimpgroups:mailChimpgroups]
				render result as JSON
			}
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	def saveMailChimpFieldSettings={
		def formAdminInstance=FormAdmin.get(params.id)
		if(formAdminInstance){
			def mailChimpDetails
			if(formAdminInstance.mailChimpDetails){
				mailChimpDetails=formAdminInstance.mailChimpDetails
			}else{
				mailChimpDetails= new MailChimpDetails()
			}
			mailChimpDetails.apikey=params.mailChimpDetailsapiKey
			mailChimpDetails.listId=[key:params.mailChimpDetailsList,value:params.mailChimpDetailsListName] as JSON
			mailChimpDetails.fieldMappings=[required:params.m.r,notRequired:params.m.nr,groups:params.m.group] as JSON
			mailChimpDetails.optinemail=params.optinemail?true:false
			mailChimpDetails.sendChoice=params.sendChoice
			if (!mailChimpDetails.hasErrors() && mailChimpDetails.save(flush:true)) {
				formAdminInstance.mailChimpDetails=mailChimpDetails
				formAdminInstance.save(flush:true)
			}
		}else{
			throw new Exception("Problem saving mailChimp Field mapping")
			return
		}
		render "success"
	}
	def deleteMailChimpFieldSettings={
		if(request.xhr){
			def result=[]
			def formAdminInstance=FormAdmin.get(params.id)
			if(formAdminInstance){
				def mailChimpDetails
				if(formAdminInstance.mailChimpDetails){
					mailChimpDetails=formAdminInstance.mailChimpDetails
					def setting=JSON.parse(formAdminInstance.form.settings)
					def mailChimpList=JSON.parse(mailChimpDetails.listId)
					result= [formName: setting?.en.name,chimpList:mailChimpList.value]
					mailChimpDetails.delete()
					formAdminInstance.mailChimpDetails=null;
					formAdminInstance.save()
				
				}
			}
			render result as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
}