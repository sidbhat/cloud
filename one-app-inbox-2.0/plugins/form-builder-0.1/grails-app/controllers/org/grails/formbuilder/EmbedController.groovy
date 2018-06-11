package org.grails.formbuilder

import javax.mail.Session;
import java.text.SimpleDateFormat;
import org.grails.paypal.Payment;
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.web.binding.DataBindingUtils;
import org.codehaus.groovy.grails.web.pages.FastStringWriter;
import org.codehaus.groovy.reflection.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.oneapp.cloud.core.Client;
import com.oneapp.cloud.core.User;
import com.oneapp.cloud.core.UserAppAccessDetail;

import freemarker.template.Template
import grails.converters.JSON
import grails.converters.XML
import grails.plugin.multitenant.core.util.TenantUtils;
import groovy.sql.Sql;

class EmbedController {
	
	def formViewerTemplateService
	def springSecurityService
	def freemarkerConfig
	def domainClassService
	def clientService
	def sqlDomainClassService
	def dataSource
	def mailChimpService
	//below field is used to show all the entries in role is as follows
	static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS
	
	//below field is used to show all the fields while creating or editing form entry
	//TODO remove this from here and use this directly in the GSPs
	static final def ROLES_TO_SHOW_ALL_FIELDS = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_FIELDS
	static final SimpleDateFormat dateFormatter = new SimpleDateFormat(ConfigurationHolder.config.format.date)

    def index = {
		redirect(action:'a')
	}
	
	//this action checks if the form is login required, public or password protected
	//if login required redirects to the loginRequiredForm controller along with the parameters
	//if open shows the form
	//if protected checks the password and gives response accordingly
	def a = {
		aForm("create")
	}
	
	def jsx = {
	}
	
	private aForm(String goToAction){
		try{
			if(params.formId){
				Form formInstance
					try{ 
						formInstance = Form.read(params.formId)
					}catch (Exception ex) {
						
					}
					if(!formInstance){
						render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
						return
					}
				def formForFormAdminCheck
				if(formInstance.formCat == 'S'){//current form is subform
					if( params.pfii && params.pfid && params.pffn ){
						formForFormAdminCheck = Form.read(params.pfid)
					}else{
						render(view:"error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
					}
				}else{
					formForFormAdminCheck = formInstance
				}
				FormAdmin fa
				try{
					fa = FormAdmin.createCriteria().get{
						if(formInstance.formCat == 'S'){
							eq 'form.id',params.pfid.toLong()
						}else{
							eq 'form.id',params.formId.toLong()
						}
					}
				}catch(Exception e){
					render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
					return
				}
				if(fa && fa.formLogin!='NoOne'){
					if('Login'.equalsIgnoreCase(fa.formLogin)){
						if(springSecurityService.currentUser!=null){
							showRequiredForm(goToAction)
						}else{
							render(view:"error",model:[exception:['message':message(code:'form.login.required',args:[],'default':"Form requires login"),detailMessage:message(code:'form.login.required.detailMessage',args:[params.formId],'default':"This form can be viewed by logged-in users. Login ${g.link(controller:'login',target:'blank'){'here'}} and <a href='javascript:;' onclick='\$(this).attr(\"href\",window.location);'>try again</a>")]])
							return
						}
					}else if('Public'.equalsIgnoreCase(fa.formLogin)){
						showRequiredForm(goToAction)
					}else if('Password'.equalsIgnoreCase(fa.formLogin)){
						if(formForFormAdminCheck.formCat == 'S'){
							showRequiredForm(goToAction)
						}else{
							params.goToAction = goToAction
							if(params.jPass == null){
								render(view:'a')
							}else if(params.jPass == ''){
								flash.message = 'publicForm.password.blank'
								flash.args = []
								flash.defaultMessage = 'Password can not be blank'
								redirect(action:'a',params:[formId:params.formId,goToAction:params.goToAction])
							}else{
								if(fa.formPassword == params.jPass){
									showRequiredForm(goToAction)
								}else{
									flash.message = 'publicForm.password.worong'
									flash.args = []
									flash.defaultMessage = 'Wrong Password. Please enter the exact password you received.'
									redirect(action:'a',params:[formId:params.formId,goToAction:params.goToAction])
								}
							}
						}
					}else{
						render(view:"error",model:[exception:['message':message(code:'form.config.update',args:[],'default':"Configuration not updated"),detailMessage:message(code:'form.config.update.detailMessage',args:[],'default':"Form configurations are not updated. Please contact the person who shared this form to you")]])
					}
				}else{
					render(view:"error",model:[exception:['message':message(code:'form.access.dataEntryClosed',args:[],'default':"Data entry closed"),detailMessage:message(code:'form.access.dataEntryClosed.detailMessage',args:[],'default':'This form is currently closed for responses. Please contact the author of this form for further assistance.')]])
				}
			}else{
				render(view:"error",model:[exception:['message':message(code:'form.access.wrongURL',args:[],'default':"Wrong URL"),detailMessage:message(code:'form.access.wrongURL.detailMessage',args:[],'default':'URL for this form seems to be incorrect. Please contact the author of this form for further assistance.')]])
			}
		}catch(Exception e){
			log.error "EmbedController-aForm:"+e+", FormId: "+params.formId
			render(view:"error",model:[exception:['message':'Some problem occurred',detailMessage:e.message]])
		}
	}
	
	private showRequiredForm(String goToAction){
		this.invokeMethod(goToAction+"Form", null)
	}
	
	def create = {
		aForm("create")
	}
	
	private createForm(){
		Form formInstance
		Form formForFormAdminCheck
		try{
			try{ 
				formInstance = Form.read(params.formId)
			}catch (Exception ex) {
				
			}
			
			if(!formInstance){
				render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
			}
			if(formInstance.formCat == 'S'){
				formForFormAdminCheck = Form.read(params.pfid)
			}else{
				formForFormAdminCheck = formInstance
			}
			if(!FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'create')){
				render(view:"error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
				return
			}
			DomainClass dc = formInstance.domainClass
			if(request['isMobile']){
				
				/*def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
				if(!domainClass || dc?.updated){
					domainClassService.reloadUpdatedDomainClasses()
					domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
				}
				if(domainClass){*/
					 def domainInstance = sqlDomainClassService.newInstance(formInstance)
					// Get the domain class details and field labels
					def formName
					def fields = new HashMap()
					if(formInstance){
						formName = JSON.parse(formInstance.settings)."en".name
						formInstance.fieldsList?.each { field ->
							fields.put( field.name,field)
						}
						
						render(view:'create_m',model: [formInstance:formInstance, formName:formName,fields:fields,domainInstance: domainInstance,
						multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS]) // multiPart:true if form have upload component
					}else{
						flash.message = message(code:'form.not.accessible','default':'Form not accessible')
						flash.defaultMessage = flash.message
						redirect(controller:'dashboard')
					}
					
				/*}else{
					flash.message = message(code:'domainClass.not.found','default':'Form not found')
					flash.defaultMessage = flash.message
					redirect(controller:'dashboard')
				}*/
			}else{
				flash.pfii = params.pfii
				flash.pfid = params.pfid
				flash.pffn = params.pffn
				/*def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
				if(!domainClass || dc?.updated){
					domainClassService.reloadUpdatedDomainClasses()
					domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
				}*/
				 def domainInstance = sqlDomainClassService.newInstance(formInstance)
				
				//formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
				renderView('create', [flash: flash, formInstance: formInstance,
						domainInstance: domainInstance,  multiPart: false], // multiPart:true if form have upload component
						formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance))
			}
		}catch(Exception ex){
			flash.message="${message(code: 'default.error.message')}"
			flash.defaultMessage = flash.message
			redirect(action:'create',params:[formId:formInstance.id, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
		}
	}
	
	private renderView(name, model, templateText) {
		if(model.domainInstance?.errors != null){
			if(!model.domainInstance.errors){
				model.domainInstance.remove("errors")
			}
		}
		FastStringWriter out = new FastStringWriter()
		new Template(name, new StringReader(templateText), freemarkerConfig.configuration).process(model, out)
		render out.toString()
	}
	
	def save = {
		saveForm()
	}
	
	private saveForm(){
		Form formInstance
			try{
			try{ 
					formInstance = Form.read(params.formId)
				}catch (Exception ex) {
				render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
				}
				if(!formInstance){
					render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
					return
			}
			def formForFormAdminCheck
			def parentDomainInstance
			if(formInstance.formCat == 'S'){//current form is subform
				if(params.pfid && params.pfii && params.pffn){
					if(!request['isMobile']){
						flash.pfii = params.pfii
						flash.pfid = params.pfid
						flash.pffn = params.pffn
					}
					formForFormAdminCheck = Form.read(params.pfid)
					def parentField = ((Form)formForFormAdminCheck).fieldsList.find{it.name == params.pffn}
					if(parentField && parentField.type == 'SubForm'){
						parentDomainInstance = true
					}else{
						//throw new Exception(message(code:'parent.formField.not.found',args:[],'default':"Sub form must be entered through parent form only, here the field is not found."))
						render(view:"error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
					}
				}else{
					//throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
					render(view:"error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
					return
				}
			}else{
				formForFormAdminCheck = formInstance
			}
			if(!FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'save')){
				render(view:"error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
				return
			}
			def domainInstance = sqlDomainClassService.populate(params, formInstance,null,request)
			// setProperties(domainClass, domainInstance, params)
			def formName = ""
			try{
				formName = JSON.parse(formInstance.settings)."en".name
			}catch(Exception e){}
			
			if ( springSecurityService.currentUser )
				domainInstance.createdBy = springSecurityService.currentUser
			else
				domainInstance.createdBy = User.findByUsername("publicUser@yourdomain.com")
			
			//PropertySetter.setProperties(domainClass, domainInstance, params)
			def fieldsList = formInstance.fieldsList
			def itemsBought
			def fieldNameSetting_MapList = []
			fieldsList?.each { field ->
					def settings = grails.converters.JSON.parse(field.settings)
					def fieldName = settings."en".label
					//fields.put( field.name,field)
					if ( field.type == "PlainText" ) {
						def fieldVal = params[field.name]
						if ( fieldVal )
							domainInstance."${fieldVal}"= "true"
					}else if ( field.type == "CheckBox" ) {
						def fieldVal = params.list(field.name).findAll{it}
					   def v
					   if ( fieldVal ){
						   v = fieldVal as List
							domainInstance."${field.name}"= (v as JSON).toString()
					   }else
						   domainInstance."${field.name}"= null
					}else if(field.type == "FileUpload"){
					 	def keyValueMap = [:]
						 keyValueMap."${field.name}" = settings
					 	fieldNameSetting_MapList.add(keyValueMap)
					 }else if(field.type == "Paypal"){
				 	itemsBought = domainInstance."${field.name}_bought"
				 }
			}
			
			def totalFileSize = 0
			boolean breakTheAction = false
		    		
		    if (request instanceof DefaultMultipartHttpServletRequest) {
		        request.multipartFiles.each {k, v ->
		            if (k) {
						List<MultipartFile> files = new ArrayList()
						def fieldName = k.replace('_file','')
		                if (v instanceof List) {
		                    v.each {MultipartFile file ->
								if(file && file.size>0){
									files << file
								}
		                    }
		                } else {
							MultipartFile file = v
							if(file && file.size>0){
								files << file
							}
		                }
						files.each{MultipartFile file->
							if(!breakTheAction){
								def fileSize = file.size/1024
								def field = fieldNameSetting_MapList.find{it."${fieldName}"}
								if(field){
									def settings = field.getAt(fieldName)
									def maxSize = settings.maxSize
									if(settings.unit == "MB"){
										maxSize = settings.maxSize*1024
									}
									if(fileSize > maxSize){
										domainInstance.errors.add([name:fieldName, code:"default.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for property ${settings.en.label} can not be greater than ${settings.maxSize} ${settings.unit}"])
										if(request['isMobile']){
											def fields = new HashMap()
											formInstance.fieldsList?.each { f ->
												fields.put( f.name,f)
											}
											render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, domainClass: null, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
										}else{
											def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
											
											renderView('create', [flash: flash, formInstance: formInstance,
												  domainInstance: domainInstance, domainClass: null, multiPart: false], // multiPart:true if form have upload component
												  createViewTemplate)
										}
										breakTheAction = true
										return
									}
									totalFileSize += fileSize
								}else{
									domainInstance.errors.add([name:"version", code:"default.form.changed", args:[formName] as Object[], defaultMessage:"Someone might have changed the form while you were entering data. Please try again."])
									if(request['isMobile']){
										def fields = new HashMap()
										formInstance.fieldsList?.each { f ->
											fields.put( f.name,f)
										}
										render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, domainClass: null , formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
									}else{
										def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
										
										renderView('create', [flash: flash, formInstance: formInstance,
											  domainInstance: domainInstance, domainClass: null, multiPart: false], // multiPart:true if form have upload component
											  createViewTemplate)
									}
									breakTheAction = true
									return
								}
							}
						}
		            }
		        }
		    }
			
			if(breakTheAction){
				return
			}
			if(totalFileSize>0){
				Client myClient = Client.get(formInstance.tenantId)
				def earlyDataLength = clientService.getTotalAttachmentSize( myClient.id)
				if( ( totalFileSize*1024 + earlyDataLength )/(1024*1024) > myClient.maxAttachmentSize){
					domainInstance.errors.add([name:"version", code:"default.client.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for your client exceeded. Please contact administrator"])
					if(request['isMobile']){
						def fields = new HashMap()
						formInstance.fieldsList?.each { f ->
							fields.put( f.name,f)
						}
						render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
					}else{
						def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
						
						renderView('create', [flash: flash, formInstance: formInstance,
							  domainInstance: domainInstance,multiPart: false], // multiPart:true if form have upload component
							  createViewTemplate)
					}
					return
				}
			}
			//formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass, domainInstance)
			 if (!domainInstance.errors && sqlDomainClassService.save(domainInstance,formInstance)) {
					if(parentDomainInstance){
						sqlDomainClassService.addSubFormInstance(domainInstance.id,params.pfii,params.pffn,formForFormAdminCheck)
					}
				session['user'] = session['user']?:domainInstance.createdBy
				def resultAttachment = attachUploadedFilesTo(formInstance,domainInstance.id)
				def user = springSecurityService.currentUser?:null
				def formAdmin
				if(parentDomainInstance){
					formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
					WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Save",formInstance)
				}else{
					formAdmin = FormAdmin.findByForm(formInstance)
					WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Save")
					if(formAdmin.mailChimpDetails && formAdmin.mailChimpDetails!="")
						mailChimpService.saveMailchimp(formAdmin, formInstance, domainInstance)
				}
				def pfc = new PFController()
				def tenantId
				if(user){
					tenantId = springSecurityService.currentUser.userTenantId
				}else{
					tenantId = formInstance.tenantId
				}
				pfc.saveUserDetails(params.location,request,formInstance.domainClass.name,UserAppAccessDetail.CREATE,springSecurityService.currentUser?:null,tenantId)
				if(itemsBought){
					def paymentField = domainInstance.paymentField
					def paymentFieldSettings = domainInstance.paymentFieldSettings
					Payment payment = domainInstance.payment
					payment.formId = formInstance.id
					payment.instanceId = domainInstance.id
					payment.buyerId = domainInstance.createdBy.id
					payment.currency = Currency.getInstance(paymentFieldSettings.curr)
					if(paymentFieldSettings.itemForm){
						itemsBought.each{item->
							payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
								if(paymentItem.itemNumber == "${item.id}"){
									paymentItem.amount = item[paymentFieldSettings.iaf]
									def itemName = item[paymentFieldSettings.inf]?:"Item ${itemIdx}"
									if(itemName.length()>126){
										itemName = itemName.substring(0,123) + "..."
									}
									paymentItem.itemName = itemName
									paymentItem.itemNumber = item.id+"_"+paymentFieldSettings.itemForm
								}
							}
						}
					}else{
						payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
							paymentItem.amount = domainInstance[paymentFieldSettings.iaf]
							paymentItem.itemNumber = domainInstance.id+"_"+formInstance.id
						}
					}
					if (payment?.validate()) {
						request.payment = payment
						payment.save(flush: true)
						def config = grailsApplication.config.grails.paypal
						def server
						if(paymentFieldSettings.test){
							server = config.testServer
						}else{
							server = config.server
						}
						def baseUrl = grailsApplication.config.grails.serverURL
						def login = paymentFieldSettings.emid
						if (!server || !login) throw new IllegalStateException("Paypal misconfigured! You need to specify the Paypal server URL and/or account email. Refer to documentation.")
			
						def commonParams = [buyerId: payment.buyerId, transactionId: payment.transactionId,returnAction: 'edit',returnController: 'embed',cancelAction: 'edit',cancelController:'embed']
						def notifyURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'notify', params: commonParams).encodeAsURL()
						def successURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'success', params: commonParams).encodeAsURL()
						def cancelURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'cancel', params: commonParams).encodeAsURL()
			
						def url = new StringBuffer("$server?")
						url << "business=$login&"
						if(paymentFieldSettings.itemForm){
							url << "cmd=_cart&upload=1&"
							payment.paymentItems.eachWithIndex {paymentItem, i ->
								def itemId = i + 1
								url << "item_name_${itemId}=${paymentItem.itemName}&"
								url << "item_number_${itemId}=${paymentItem.itemNumber}&"
								url << "quantity_${itemId}=${paymentItem.quantity}&"
								url << "amount_${itemId}=${paymentItem.amount}&"
								if (payment.discountCartAmount == 0 && paymentItem.discountAmount > 0) {
									url << "discount_amount_${itemId}=${paymentItem.discountAmount}&"
								}
							}
						}else{
							url << "cmd=_xclick&"
							payment.paymentItems.eachWithIndex {paymentItem, i ->
								url << "item_name=${paymentItem.itemName}&"
								url << "amount=${paymentItem.amount}&"
							}
						}
						
						url << "currency_code=${payment.currency}&"
						url << "notify_url=${notifyURL}&"
						url << "return=${successURL}&"
						url << "cancel_return=${cancelURL}"
			
						log.debug "Redirection to PayPal with URL: $url"
						println "Url here is :===========>>>>>>>>"+url
						redirect(url: url)
						return
					}
					else {
						//Handle in case validation fails
					}
				}
				if(formAdmin?.formSubmitMessage){
					flash.message = "${formAdmin?.formSubmitMessage}"
					flash.defaultMessage = flash.message
				}else{
					def showMessage = "${message(code: 'default.created.message', args: [formName?:'', domainInstance.id])}"
					flash.message = showMessage
					flash.defaultMessage = flash.message
				}
				if(formInstance.formCat == 'S'){
					redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
					return
				}
	//			if(!formAdmin?.openForEdit){
	//				if(request['isMobile']){
	//					redirect(action: "edit", id: domainInstance.id, params: [formId: formInstance.id])
	//				}else{
						render(view:"error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message]])
						flash.message = null
						return
	//				}
	//			}else{
	//				redirect(action: "edit", id: domainInstance.id, params: [formId: formInstance.id])
	//			}
			}else{
				if(request['isMobile']){
					def fields = new HashMap()
					formInstance.fieldsList?.each { field ->
						fields.put( field.name,field)
					}
					render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, domainClass: null, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
				}else{
					def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
					renderView('create', [flash: flash, formInstance: formInstance,
						domainInstance: domainInstance,  multiPart: false], // multiPart:true if form have upload component
						createViewTemplate)
				}
			}
		}catch(Exception ex){
			flash.message="${message(code: 'default.error.message')}"
			flash.defaultMessage = flash.message
			redirect(action:'create',params:[formId:formInstance.id, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
		}
	}
	
	def show = {
		showForm()
	}
	
	private showForm(){
		Form formInstance
		try{ 
				formInstance = Form.read(params.formId)
			}catch (Exception ex) {
				
			}
			if(!formInstance){
				render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
		}
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.clazz.createCriteria().get{
			eq 'id',params.id.toLong()
		}
		def formName = ""
		try{
			formName = JSON.parse(formInstance.settings)."en".name
		}catch(Exception e){}
		
	    if (!domainInstance) {
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
			flash.defaultMessage = flash.message
			redirect(action: "list", params: [formId: params.formId])
		}else{
			if(request['isMobile']){
				def formAdmin = FormAdmin.findByForm(formInstance)
				def fields = new HashMap()
				formInstance.fieldsList?.each { field ->
					fields.put( field.name,field)
				}
				render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, domainClass:domainClass,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
				return
			}else{
				//formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
				renderView('show', [flash: flash, formInstance: formInstance,
					domainInstance: domainInstance, domainClass: domainClass],
					formViewerTemplateService.getShowViewTemplate(request, flash, formInstance,domainInstance))
			}
		}
	}

	def edit = {
		editForm()
	}
	
	private editForm(){
		Form formInstance
		def domainInstance
			try{
			try{ 
				formInstance = Form.read(params.formId)
			}catch (Exception ex) {
				
			}
			def formForFormAdminCheck
			if(formInstance){
				if(formInstance.formCat == 'S'){
					formForFormAdminCheck = Form.read(params.pfid)
				}else{
					formForFormAdminCheck = formInstance
				}
				def domainInstanceId
				try{
					domainInstanceId = params.id.toLong()
				}catch(Exception e){}
				if(!domainInstanceId || !FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'edit',domainInstanceId)){
					render(view:"error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
					return
				}
				domainInstance = sqlDomainClassService.get(params.id, formInstance)
			}
			if(!formInstance || !domainInstance){
				render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
			}
			def parentDomainInstance
			if(formInstance.formCat == 'S'){
				if(params.pfid && params.pfii && params.pffn){
					if(!request['isMobile']){
						flash.pfii = params.pfii
						flash.pfid = params.pfid
						flash.pffn = params.pffn
					}
					parentDomainInstance = sqlDomainClassService.get(params.pfii, formForFormAdminCheck)
					if(!formForFormAdminCheck || !parentDomainInstance){
						//throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
						render(view:"error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
					}
				 }else{
					throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
				}
			}
			def formName = ""
			try{
				formName = JSON.parse(formInstance.settings)."en".name
			}catch(Exception e){}
			
		    if (!domainInstance) {
				flash.message = "${message(code: 'default.not.found.message', args: [formName, params.id])}"
				flash.defaultMessage = flash.message
				redirect(action: "list", params: [formId: params.formId])
			}
			else {
				if(request['isMobile']){
					def fields = new HashMap()
					formInstance.fieldsList?.each { field ->
						fields.put( field.name,field)
					}
					def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
					render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
				}else{
	//				formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
					renderView('edit', [flash: flash, formInstance: formInstance,
						domainInstance: domainInstance],
						formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance))
				}
			}
		}catch(Exception ex){
			flash.message="${message(code: 'default.error.message')}"
			flash.defaultMessage = flash.message
			redirect(action:'edit',id: domainInstance.id,params:[formId:formInstance.id, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
		}
	}
	
	def update = {
		updateForm()
	}
	
	private updateForm(){
		Form formInstance
		def domainInstance
			try{
			try{ 
				formInstance = Form.read(params.formId)
			}catch (Exception ex) {
				
			}
			if(!formInstance){
				render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
			}
			def formForFormAdminCheck
			if(formInstance.formCat == 'S'){
				formForFormAdminCheck = Form.read(params.pfid)
			}else{
				formForFormAdminCheck = formInstance
			}
			def domainInstanceId
			try{
				domainInstanceId = params.id.toLong()
			}catch(Exception e){}
			if(!domainInstanceId || !FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'edit',domainInstanceId)){
				render(view:"error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
				return
			}
			def parentDomainInstance
			if(formInstance.formCat == 'S'){//current form is subform
				if(params.pfid && params.pfii && params.pffn){
					if(!request['isMobile']){
						flash.pfii = params.pfii
						flash.pfid = params.pfid
						flash.pffn = params.pffn
					}
					parentDomainInstance = sqlDomainClassService.get(params.pfii, formForFormAdminCheck)
					if(!parentDomainInstance){
						//throw new Exception(message(code:'parent.formInstance.not.found',args:[],'default':"Sub form must be entered through parent form only, here the instance not found."))
						render(view:"error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
					}
				}else{
					//throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
					render(view:"error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
					return
				}
			}
			domainInstance = sqlDomainClassService.get(params.id, formInstance)
			def currentDataInstance = sqlDomainClassService.populate(params, formInstance,domainInstance,request)
			def formName = ""
			try{
				formName = JSON.parse(formInstance.settings)."en".name
			}catch(Exception e){}
			
		    if (domainInstance) {
				if (params.version) {
					def version = params.version.replaceAll(",","").toLong()
					if (domainInstance.version > version) {
						domainInstance.errors.add([name:"version", code:"default.optimistic.locking.failure", args:[formName] as Object[], defaultMessage:"Another user has updated this ${formName} while you were editing"])
						if(request['isMobile']){
							def fields = new HashMap()
							formInstance.fieldsList?.each { field ->
								fields.put( field.name,field)
							}
							def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
							render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
						}else{
							renderView('edit', [flash: flash, formInstance: formInstance,
								domainInstance: domainInstance],
								formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance))
						}
						return
					}
				}
				domainInstance = currentDataInstance
				def fieldsList = formInstance.fieldsList
				def fieldNameSetting_MapList = []
				fieldsList?.each { field ->
						  def settings = grails.converters.JSON.parse(field.settings)
						  def fieldName = settings."en".label
						  //fields.put( field.name,field)
						  if ( field.type == "PlainText" ) {
							  def fieldVal = params[field.name]
							  if ( fieldVal )
								  domainInstance."${fieldVal}"= "true"
						  }else if ( field.type == "CheckBox" ) {
							  def fieldVal = params.list(field.name)
							 def v
							 if ( fieldVal ){
								 v = fieldVal as List
								  domainInstance."${field.name}"= (v as JSON).toString()
							 }else
								 domainInstance."${field.name}"= null
						  }else if(field.type == "FileUpload"){
						 	def keyValueMap = [:]
							 keyValueMap."${field.name}" = settings
						 	fieldNameSetting_MapList.add(keyValueMap)
						 }
				  }
			
					def totalFileSize = 0
					boolean breakTheAction = false
				    		
				    if (request instanceof DefaultMultipartHttpServletRequest) {
				        request.multipartFiles.each {k, v ->
				            if (k) {
								List<MultipartFile> files = new ArrayList()
								def fieldName = k.replace('_file','')
				                if (v instanceof List) {
				                    v.each {MultipartFile file ->
										if(file && file.size>0){
											files << file
										}
				                    }
				                } else {
									MultipartFile file = v
									if(file && file.size>0){
										files << file
									}
				                }
								files.each{MultipartFile file->
									if(!breakTheAction){
										def fileSize = file.size/1024
										def field = fieldNameSetting_MapList.find{it."${fieldName}"}
										if(field){
											def settings = field.getAt(fieldName)
											def maxSize = settings.maxSize
											if(settings.unit == "MB"){
												maxSize = settings.maxSize*1024
											}
											if(fileSize > maxSize){
												domainInstance.errors.add([name:fieldName, code:"default.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for property ${settings.en.label} can not be greater than ${settings.maxSize} ${settings.unit}"])
												if(request['isMobile']){
													def fields = new HashMap()
													formInstance.fieldsList?.each { f ->
														fields.put( f.name,f)
													}
													def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
													render(view:'edit_m', model:[formName:formName,domainInstance: domainInstance,fields:fields, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
												}else{
													def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
													renderView('edit', [flash: flash, formInstance: formInstance,
															  domainInstance: domainInstance], editViewTemplate)
												}
												breakTheAction = true
												return
											}
											totalFileSize += fileSize
										}else{
											domainInstance.errors.add([name:"version", code:"default.form.changed", args:[formName] as Object[], defaultMessage:"Someone might have changed the form while you were entering data. Please try again."])
											if(request['isMobile']){
												def fields = new HashMap()
												formInstance.fieldsList?.each { f ->
													fields.put( f.name,f)
												}
												def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
												render(view:'edit_m', model:[formName:formName,domainInstance: domainInstance,fields:fields, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
											}else{
												def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
												renderView('edit', [flash: flash, formInstance: formInstance,
														  domainInstance: domainInstance], editViewTemplate)
											}
											breakTheAction = true
											return
										}
									}
								}
				            }
				        }
				    }
					if(breakTheAction){
						return
					}
					
					
					//If the validation fails then clientService.getTotalAttachmentSize() method throws exception. Below we need to check the validations first
	//				if (domainInstance.hasErrors() || !domainInstance.validate()) {
	//					if(request['isMobile']){
	//						def fields = new HashMap()
	//						formInstance.fieldsList?.each { f ->
	//							fields.put( f.name,f)
	//						}
	//						def formAdmin = FormAdmin.findByForm(formInstance)
	//						render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, domainClass:domainClass,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
	//					}else{
	//							def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
	//						  renderView('edit', [flash: flash, formInstance: formInstance,
	//								  domainInstance: domainInstance, domainClass: domainClass], editViewTemplate)
	//					}
	//					return
	//				}
					
					if(totalFileSize>0){
						Client myClient = Client.get(formInstance.tenantId)
						def earlyDataLength = clientService.getTotalAttachmentSize( myClient.id)
						if( ( totalFileSize*1024 + earlyDataLength )/(1024*1024) > myClient.maxAttachmentSize){
							domainInstance.errors.add([name:"version", code:"default.client.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for your client exceeded. Please contact administrator"])
							if(request['isMobile']){
								def fields = new HashMap()
								formInstance.fieldsList?.each { f ->
									fields.put( f.name,f)
								}
								def formAdmin = FormAdmin.findByForm(formInstance)
								render(view:'edit_m', model:[formName:formName,domainInstance: domainInstance,fields:fields, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
							}else{
									def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
								  renderView('edit', [flash: flash, formInstance: formInstance,
										  domainInstance: domainInstance], editViewTemplate)
							}
							return
						}
					}
					if ( springSecurityService.currentUser )
						domainInstance.updatedBy = springSecurityService.currentUser
					else
						domainInstance.updatedBy = User.findByUsername("publicUser@yourdomain.com")
						
	//			formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass, domainInstance)
				if (!domainInstance.errors && sqlDomainClassService.update(domainInstance,formInstance)) {
					session['user'] = session['user']?:domainInstance.updatedBy
					def resultAttachment = attachUploadedFilesTo(formInstance,domainInstance.id)
					def formAdmin
					if(parentDomainInstance){
						formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
						WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Update",formInstance)
					}else{
						formAdmin = FormAdmin.findByForm(formInstance)
						WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Update")
					}
					def showMessage = "${message(code: 'default.form.updated.message', args: [formName?:'', domainInstance.id])}"
					flash.message = showMessage
					flash.defaultMessage = flash.message
					//flash.message = "${message(code: 'default.updated.message', args: [ formName, domainInstance.id])}"
					//flash.defaultMessage = flash.message
							
					//No need to handle for mobile view as we are redirecting it, it'll be taken care when the request is received again.
					if(formInstance.formCat == 'S'){
						redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
					}else{
			        	redirect(action: "edit", id: domainInstance.id, params: [formId: params.formId])
					}
				}else{
					if(request['isMobile']){
						def fields = new HashMap()
						formInstance.fieldsList?.each { field ->
							fields.put( field.name,field)
						}
						def formAdmin = FormAdmin.findByForm(formInstance)
						render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
					}else{
						def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
						renderView('edit', [flash: flash, formInstance: formInstance,
							domainInstance: domainInstance], editViewTemplate)
					}
				}
			}else{
				render(view:"error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
			}
		}catch(Exception ex){
			flash.message="${message(code: 'default.error.message')}"
			flash.defaultMessage = flash.message
			redirect(action:'edit',id: domainInstance.id,params:[formId:formInstance.id, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
		}
	}
	
	def delete = {
		deleteForm()
	}
	
	private deleteForm(){
		//TODO remove this redirection and make restrictions accordingly
		//This redirection is temporary so that anyone is not capable of deleting the instance for the time.
		redirect(action: "edit", id: params.id, params: [formId: params.formId])
		return
		Form formInstance
		try{ 
			formInstance = Form.read(params.formId)
		}catch (Exception ex) {
			
		}
		if(!formInstance){
			render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
			return
		}
		def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		def domainInstance = domainClass.clazz.createCriteria().get{
			eq 'id',params.id.toLong()
		}
	
		def formName = ""
		try{
			formName = JSON.parse(formInstance.settings)."en".name
		}catch(Exception e){}
		
	    if (domainInstance) {
			try {
				domainInstance.removeAttachments()
				domainInstance.delete(flush: true)
				flash.message = "${message(code: 'default.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
				flash.defaultMessage = flash.message
				redirect(action: "list", params: [formId: params.formId])
			}catch (org.springframework.dao.DataIntegrityViolationException e) {
				flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
				flash.defaultMessage = flash.message
				redirect(action: "edit", id: params.id, params: [formId: params.formId])
			}
		}else{
			flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
			flash.defaultMessage = flash.message
			redirect(action: "list", params: [formId: params.formId])
		}
	}
	
	private String getErrorsForForm(Form form,Object domainInstance){
		FastStringWriter out = new FastStringWriter()
		def formName
		def fields = new HashMap()
		formName = JSON.parse(form.settings)."en".name
		form.fieldsList?.each { field ->
			fields.put( field.name,field)
		}
		
		out << '<div class="errors">'
		domainInstance.errors.each{ error ->
			
			if(error.name != 'version')
			{
				def fieldWithSettings = fields?.get(error.name)
				if(fieldWithSettings){
					def fieldName = JSON.parse(fieldWithSettings.settings)?.en?.label
					out << '<li>'+message(code:error.code, args:[fieldName,formName], default:error.defaultMessage)+'</li>';
				}
			}else{
				out << '<li>'+message(code:error.code, args:["version",formName], default:error.defaultMessage)+'</li>';
			}
			
		}
		out << '</div>'
		return out.toString()
	}
	
	def saveFormViewUser = {
		def userLocation = params.location
		Form formInstance
		try{ 
			formInstance = Form.read(params.formId)
			}catch (Exception ex) {
			
			}
		if(!formInstance){
			render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
			return
		}
		def formName = formInstance.domainClass.name
		def action = UserAppAccessDetail.VIEW
		def user = springSecurityService.currentUser?:null
		def tenantId
		if(user){
			tenantId = springSecurityService.currentUser.userTenantId
		}else{
			tenantId = formInstance.tenantId
		}
		saveUserDetails(userLocation,request,formName,action,user,tenantId)
		def userAppAccessDetail = new UserAppAccessDetail()
		withFormat {
			 html { render "Success"}
			 json { render(status: 200, contentType: 'application/json', text: userAppAccessDetail as JSON) }
			xml { render(status: 200, contentType: 'text/xml', text: userAppAccessDetail as XML) }
		}
	}
	
	def saveUserData = {
		def userLocation = params.location
		def formName =  params.formName
		def action = UserAppAccessDetail.VIEW
		def user = springSecurityService.currentUser?:null
		Form form  = Form.createCriteria().get{
			domainClass{
				eq 'name',formName
			}
		}
		def tenantId
		if(user){
			tenantId = springSecurityService.currentUser.userTenantId
		}else{
			tenantId = form.tenantId
		}
		saveUserDetails(userLocation,request,formName,action,user,tenantId)
		def userAppAccessDetail = new UserAppAccessDetail()
		withFormat {
			 html { render "Success"}
			 json { render(status: 200, contentType: 'application/json', text: userAppAccessDetail as JSON) }
			xml { render(status: 200, contentType: 'text/xml', text: userAppAccessDetail as XML) }
		}
	}
	
	def saveUserDetails(String userLocation,def request,def className,def action,def user,def tenantId)
		{
			TenantUtils.doWithTenant(tenantId) {
				String userAgent = request.getHeader("user-agent")
				def userAgentData =  userAgentDetails(userAgent)
				def userAppAccessDetail = new UserAppAccessDetail()
				userAppAccessDetail.ipAddress = request.getRemoteAddr()
				userAppAccessDetail.accessType = userAgentData.browseType
				userAppAccessDetail.accessMode = userAgentData.browseMode
				userAppAccessDetail.accessedClass = className
				userAppAccessDetail.action = action
				userAppAccessDetail.location = userLocation
				userAppAccessDetail.accessTime = new Date()
				userAppAccessDetail.user = user
				userAppAccessDetail.save(flush:true,validate:false)
			}
	}
	
	def userAgentDetails(String userAgentInfo)
	{
		
		def userAgent = [:]
		if (userAgentInfo.indexOf("iPhone") != -1)
		{
			userAgent.browseType = UserAppAccessDetail.DEVICE ;
			userAgent.browseMode = UserAppAccessDetail.IPHONE
		}
		else if (userAgentInfo.indexOf("iPad") != -1)
		{
			userAgent.browseType = UserAppAccessDetail.DEVICE;
			userAgent.browseMode = UserAppAccessDetail.IPAD;
		}
		else if(userAgentInfo.indexOf("Android") != -1 && userAgentInfo.indexOf("Mozilla") != -1)
		{
			userAgent.browseType = UserAppAccessDetail.DEVICE;
			userAgent.browseMode = UserAppAccessDetail.ANDROID;
		}
		else if(userAgentInfo.toLowerCase().indexOf("msie")!=-1)
		{
			userAgent.browseType = UserAppAccessDetail.BROWSER;
			userAgent.browseMode = UserAppAccessDetail.IE;
		}
		else if(userAgentInfo.toLowerCase().indexOf("firefox")!=-1)
		{
			userAgent.browseType = UserAppAccessDetail.BROWSER;
			userAgent.browseMode = UserAppAccessDetail.MOZILLA;
		}
		else if(userAgentInfo.toLowerCase().indexOf("safari")!=-1 && userAgentInfo.toLowerCase().indexOf("chrome")==-1 && userAgentInfo.toLowerCase().indexOf("playbook")==-1)
		{
			userAgent.browseType = UserAppAccessDetail.BROWSER;
			userAgent.browseMode = UserAppAccessDetail.SAFARI;
		}
		else if(userAgentInfo.toLowerCase().indexOf("chrome")!=-1 && userAgentInfo.toLowerCase().indexOf("safari")!=-1)
		{
			userAgent.browseType = UserAppAccessDetail.BROWSER;
			userAgent.browseMode = UserAppAccessDetail.CHROME;
		}
		else if(userAgentInfo.indexOf("macintosh") != -1 )
		{
			if(userAgentInfo.indexOf("chrome") != -1 && userAgentInfo.indexOf("safari") != -1)
			{
					userAgent.browseType = UserAppAccessDetail.BROWSER;
					userAgent.browseMode = UserAppAccessDetail.MACCHROME;
			}
			else{
					userAgent.browseType = UserAppAccessDetail.BROWSER;
					userAgent.browseMode = UserAppAccessDetail.MACSAFARI;
				}
			}
		return userAgent;

	}
	
	def lookUp = {
		def sql
		def status = 'fail'
		def jsonResult = []
		def statusMessage = 'Could not access the data'
		try{
			sql = new Sql(dataSource)
			Form form = Form.read(params.formId)
			Field field = Field.read(params.field)
			def fieldsList = form.fieldsList
			if(fieldsList.find{it.id == field.id}){
				def settings = JSON.parse(field.settings)
				if(settings.mapMasterForm && settings.mapMasterField){
					Form masterForm = Form.read(settings.mapMasterForm)
					def masterFields = masterForm?.fieldsList?.findAll{settings.mapMasterField.contains(it.name)}
					if(masterFields){
						def whereClause = ""
						masterFields.each{
							if(whereClause != "")
								whereClause += " or"
							whereClause += (" "+it.name+" like \'%"+params.term+"%\'")
						}
						def domainInstanceList = sql.rows("select * from "+masterForm.name+" where "+whereClause)
						jsonResult = domainInstanceList.collect{domainInstance ->
							def description = ""
							masterFields.each{
								if(description != "")
									description += " "
								description += domainInstance."${it.name}"
							}
							def wholeObj = [:]
							masterForm.fieldsList.each{f->
								try{
									def fieldValue = domainInstance.getAt(f.name)
									if(fieldValue?.class?.name == "java.sql.Timestamp"){
										wholeObj."${f.name}" = dateFormatter.format(fieldValue)
									}else{
										wholeObj."${f.name}" = fieldValue
									}
								}catch(Exception e){}
							}
							[value:description,label:description,wholeObj:wholeObj]
						}
					}
				}
			}
		}catch(Exception e){
			log.error "Error in lookup: "+e
			statusMessage = 'Sorry an error occurred. Please try again'
		}
		render jsonResult as JSON
	}
	
	def afterInterceptor = { model, modelAndView ->
		if (request['isMobile'] && modelAndView != null && modelAndView.viewName?.indexOf("error")>-1) {
			modelAndView.viewName = modelAndView.viewName + "_m"
		}
	}
}