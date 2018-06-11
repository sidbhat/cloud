package org.grails.formbuilder

import javax.mail.Session;
import java.text.SimpleDateFormat;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.web.binding.DataBindingUtils;
import org.codehaus.groovy.grails.web.pages.FastStringWriter;
import org.codehaus.groovy.reflection.ReflectionUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.oneapp.cloud.core.Client;
import com.oneapp.cloud.core.Role;
import com.oneapp.cloud.core.User;
import com.oneapp.cloud.core.UserAppAccessDetail;

import org.grails.formbuilder.FormUtils;
import org.grails.paypal.Payment;
import freemarker.template.Template
import grails.converters.JSON
import grails.converters.XML
import grails.plugin.multitenant.core.util.TenantUtils;
import groovy.sql.Sql;
import javaQuery.j2ee.GeoLocation
import javaQuery.importClass.javaQueryBundle
class PFController {
	
	def formViewerTemplateService
	def springSecurityService
	def freemarkerConfig
	def domainClassService
	def clientService
	def sqlDomainClassService
	def dataSource
	def mailChimpService
	def mailService
	//below field is used to show all the entries in role is as follows
	static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS
	
	//below field is used to show all the fields while creating or editing form entry
	//TODO remove this from here and use this directly in the GSPs
	static final def ROLES_TO_SHOW_ALL_FIELDS = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_FIELDS
	static final SimpleDateFormat dateFormatter = new SimpleDateFormat(ConfigurationHolder.config.format.date)
	private static final String EMAIL_PATTERN = '^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$';

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
	
	def publicSearch = {
	
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
					if('Login'!=fa.formLogin && !FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'create')){
						render(view:"error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
						return
					}
					if('Login'.equalsIgnoreCase(fa.formLogin)){
						if(formForFormAdminCheck.formCat == 'S'){
							redirect(controller:'formViewer',action:'create',params:['formId':params.formId,pfii:params.pfii,pfid:params.pfid,pffn:params.pffn])
						}else{
							redirect(controller:'formViewer',action:'create',params:['formId':params.formId])
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
			log.error "PFController-aForm: "+e+", For params.formId"+params.formId
			render(view:"error",model:[exception:['message':'Some problem occurred',detailMessage:e.message]])
		}
	}
	
	private showRequiredForm(String goToAction){
		this.invokeMethod(goToAction+"Form", null)
	}
	
	def create = {
		def formIds = params.list('formId')
		params.formId = formIds?formIds[0]:null
		aForm("create")
	}
	
	def preview = {
		Form formInstance
		try{
		    formInstance = Form.read(params.formId)
			if(!formInstance){
				render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
			}
			FormAdmin formAdmin = FormAdmin.findByForm(formInstance)
			if(formAdmin){
				redirect(action:'a', id:"oneapp"+new Date().time, params:[formId:params.formId])
			}else{
			    flash.message = "Form settings is required to preview the form"
				flash.defaultMessage = flash.message
				redirect(controller:'formAdmin', action:'create', params:[formId:params.formId])
			}
		}catch(Exception ex){
			flash.message = "Error in form preview"
			flash.defaultMessage = flash.message
			redirect(controller:'form', action:'list')
			
		}
	}
	
	private createForm(){
		Form formInstance
		try{
			try{ 
	 			formInstance = Form.read(params.formId)
			}catch (Exception ex) {
				
			}
			if(!formInstance){
				render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
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
					//def domainInstance = sqlDomainClassService.newInstance(formInstance)
					def domainInstance = sqlDomainClassService.populate(params,formInstance,null,request)
					domainInstance.put("errors",[])
					// Get the domain class details and field labels
					def formName
					def fields = new HashMap()
					if(formInstance){
						formName = JSON.parse(formInstance.settings)."en".name
						formInstance.fieldsList?.each { field ->
							fields.put( field.name,field)
						}
						FormAdmin fa=FormAdmin.findByForm(formInstance)
						render(view:'create_m',model: [formInstance:formInstance, formName:formName,fields:fields,domainInstance: domainInstance,
						multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:fa?.openForEdit]) // multiPart:true if form have upload component
					}else{
						flash.message = message(code:'form.not.accessible','default':'Form not accessible')
						flash.defaultMessage = flash.message
						redirect(controller:'dashboard')
					}
					
			}else{
				flash.pfii = params.pfii
				flash.pfid = params.pfid
				flash.pffn = params.pffn
				def domainInstance = sqlDomainClassService.populate(params,formInstance,null,request)
				domainInstance.put("errors",[])
				
				renderView('create', [flash: flash, formInstance: formInstance,
						domainInstance: domainInstance,  multiPart: false], // multiPart:true if form have upload component
						formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance))
			}
		}catch(Exception ex){
			log.error "PF create: "+ex+", For form id: "+params.formId
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
	
	private renderViewEmail(name, model, templateText) {
		if(model.domainInstance?.errors != null){
			if(!model.domainInstance.errors){
				model.domainInstance.remove("errors")
			}
		}
		FastStringWriter out = new FastStringWriter()
		new Template(name, new StringReader(templateText), freemarkerConfig.configuration).process(model, out)
		return out.toString()
	}
	
	def sendEmail = {
		def form
		if(params.formId || session.emailEmbedFormId){
			try{
				form = Form.read(params.formId)
			}catch(Exception e){}
		}
		if(!form){
			[noForm:'Form not found']
			return
		}
		def isFromValid = true
		def errorMessage = ""
		def showMessage = ""
		def emailFrom
		if(form && params.emailTo){
			try{
				def emailToList
				def emailToInvalidList = []
				if(params.emailTo.indexOf(";")){
					emailToList = []
					params.emailTo.split(";").each{email->
						email = email.trim()
						if(email){
							if(validateEmail(email))
								emailToList << email
							else
								emailToInvalidList << email
						}
					}
					emailToList = emailToList.toArray()
				}else{
					if(validateEmail(params.emailTo))
						emailToList = [params.emailTo].toArray()
					else
						emailToInvalidList << params.emailTo
				}
				
//				def mail = new ConfigObject()
//				
//				mail.host = "smtp.gmail.com"
//				mail.port = 465
//				mail.username = "notification@yourdomain.com"
//				mail.password = "oneapp@123"
//				mail.props = ["mail.smtp.auth": "true",
//							"mail.smtp.socketFactory.port": "465",
//							"mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
//							"mail.smtp.starttls.enable": "true",
//							 "mail.debug": "false",
//							"mail.smtp.socketFactory.fallback": "false"]
				if(emailToInvalidList || !emailToList){
					render(view:'sendEmail',model:[emailToInvalidList:emailToInvalidList,emailFrom:"notification@yourdomain.com",formName:form.toString()])
					return
				}
				emailFrom = params.emailFrom
				if(emailFrom){
					emailFrom = emailFrom.trim()
					if(!validateEmail(emailFrom)){
						errorMessage = message(code:'email.form.invalid','default':'From email address is invalid')
						isFromValid = false
					}
				}else{
					errorMessage = message(code:'email.form.empty','default':'From email address is empty')
					isFromValid = false
				}
				def emailBody = params.emailBody
				if(emailBody){
					while(emailBody.indexOf("\n")>-1){
						emailBody = emailBody.replace("\n","<br>")
					}
				}
				def templateText = ""
				try{
					def formAdmin = FormAdmin.findByForm(form)
					def linkToForm = formAdmin?.shortURL?:(createLink(controller:'PF',action:'create',params:[formId:form.id],absolute:true))
					templateText = """<b>If you have trouble viewing or submitting this form, you can fill it out online:</b> ${linkToForm} <br><br>"""
				}catch(Exception e){}
				
				if(params.includeForm){
					def domainInstance = sqlDomainClassService.newInstance(form)
					boolean emailEmbedForm = true
					def createViewTemplateText = formViewerTemplateService.getCreateViewTemplate(request, flash, form, domainInstance,emailEmbedForm)
					flash.formAction = g.createLink(controller:'PF',action:'create',params:[formId:form.id],absolute:true)
					templateText += renderViewEmail('create', [flash: flash, formInstance: form,
						domainInstance: domainInstance,  multiPart: false], // multiPart:true if form have upload component
						createViewTemplateText)
				}
				if(isFromValid){
					//println "teplate text==========>>>>>>>>>>>"+templateText+"teplate text==========<<<<<<<<<<"
					mailService.sendMail
						{
							from emailFrom
							to emailToList
							subject ""+params.emailSubject
							html "<div style='width:640px;'>"+emailBody+"<br><br>"+templateText+"</div>"
						}
					showMessage = message(code:'email.embed.form.success','default':'Mail sent successfully')
					}
			}catch(Exception e){
				if(isFromValid){
					errorMessage = message(code:'email.embed.form.fail','default':'Problem sending mail')
					log.error "Problem settings email: "+e
				}
			}
		}
		if(isFromValid)
			[emailFrom:springSecurityService.currentUser.username,formName:form.toString(),showMessage:showMessage,errorMessage:errorMessage]
		else
			[emailFrom:emailFrom,formName:form.toString(),showMessage:showMessage,errorMessage:errorMessage]
	}
	def validateEmail(email){
		Matcher matcher
		Pattern pattern = Pattern.compile(EMAIL_PATTERN);
		matcher = pattern.matcher(email);
		if(!matcher.matches()){
			return false
		}
		return true
	}
	
	def save = {
		def formIds = params.list('formId')
		params.formId = formIds?formIds[0]:null
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
			FormAdmin formAdmin = FormAdmin.findByForm(formInstance)
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
			// println params
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
											render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, domainClass: null, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin?.openForEdit])
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
										render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, domainClass: null, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin?.openForEdit])
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
						render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin?.openForEdit])
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
			
						def commonParams = [buyerId: payment.buyerId, transactionId: payment.transactionId,returnAction: 'edit',returnController: 'PF',cancelAction: 'edit',cancelController:'PF']
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
				if(params.subFormid && params.subFormid !="null"  && params.subFormfn && params.subFormfn !="null"){
					redirect(action:'create',params:[formId:params.subFormid, pfii:domainInstance.id, pfid:formInstance.id, pffn:params.subFormfn])
				  return
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
				if(!formAdmin?.openForEdit){
					if(request['isMobile']){
						if(formAdmin?.redirectUrl){
							render(view:"error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message],redirectURL:formAdmin.redirectUrl])
							return
						}else{
							render(view:"error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message]])
							flash.message = null
							return
						}
					}else{
						if(formAdmin?.redirectUrl){
							render(view:"error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message],redirectURL:formAdmin.redirectUrl])
							return
						}else{
							render(view:"error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message]])
							flash.message = null
							return
						}
					}
				}else{
					if(formAdmin?.redirectUrl){
						render(view:"error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message],redirectURL:formAdmin.redirectUrl])
						return
					}else{
						redirect(action: "edit", id: domainInstance.id, params: [formId: formInstance.id])
					}
				}
			}else{
				if(request['isMobile']){
					def fields = new HashMap()
					formInstance.fieldsList?.each { field ->
						fields.put( field.name,field)
					}
					render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, domainClass: null, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
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
		def domainInstance
		def uniqueFormInstance = UniqueFormEntry.findByUniqueId(params.uniqueId)
		try{ 
			formInstance = Form.read(uniqueFormInstance.formId)
		}catch (Exception ex) {
			
		}
		if(!formInstance){
			render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
			return
		}
		FormAdmin formAdmin = FormAdmin.findByForm(formInstance)
		if(formAdmin.searchable){
			domainInstance = sqlDomainClassService.get(uniqueFormInstance.instanceId, formInstance)
			def formName = ""
			try{
				formName = JSON.parse(formInstance.settings)."en".name
			}catch(Exception e){}
			
			if (!domainInstance) {
				flash.message = "${message(code: 'default.not.found.message', args: [formName, params.id])}"
				flash.defaultMessage = flash.message
				redirect(action: "list", params: [formId: params.formId])
			}else{
				if(request['isMobile']){
					def fields = new HashMap()
					formInstance.fieldsList?.each { field ->
						fields.put( field.name,field)
					}
					render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
					return
				}else{
					//formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
					renderView('show', [flash: flash, formInstance: formInstance,
						domainInstance: domainInstance],
						formViewerTemplateService.getShowViewTemplate(request, flash, formInstance,domainInstance))
				}
			}
		}else{
			render(view:"error",model:[exception:['message':message(code:'form.not.searchable','default':"Form data is not searchable."),detailMessage:message(code:'form.not.searchable','default':"Form data is not searchable.")]])
			return
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
			}else{
				formForFormAdminCheck = formInstance
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
				def itemsBought
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
					if(itemsBought){
						Payment p = Payment.findByFormIdAndInstanceId("${formInstance.id}","${domainInstance.id}")
						p.delete(flush:true)
						def paymentField = domainInstance.paymentField
						def paymentFieldSettings = domainInstance.paymentFieldSettings
						Payment payment = domainInstance.payment
						payment.formId = formInstance.id
						payment.instanceId = domainInstance.id
						payment.buyerId = domainInstance.updatedBy.id
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
				
							def commonParams = [buyerId: payment.buyerId, transactionId: payment.transactionId,returnAction: 'edit',returnController: 'PF',cancelAction: 'edit',cancelController:'PF']
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
							redirect(url: url)
							return
						}
						else {
							//Handle in case validation fails
						}
					}
					if(params.subFormid && params.subFormid !="null"  && params.subFormfn && params.subFormfn !="null"){
						redirect(action:'create',params:[formId:params.subFormid, pfii:domainInstance.id, pfid:formInstance.id, pffn:params.subFormfn])
					  return
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
		def userLocation="Unknown"
		Form formInstance
		try{ 
			formInstance = Form.read(params.formId.replaceAll(',',''))
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
		def userAppAccessDetail = []//new UserAppAccessDetail()
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
		def userAppAccessDetail = []//new UserAppAccessDetail()
		withFormat {
			 html { render "Success"}
			 json { render(status: 200, contentType: 'application/json', text: userAppAccessDetail as JSON) }
			xml { render(status: 200, contentType: 'text/xml', text: userAppAccessDetail as XML) }
		}
	}
	
	def saveUserDetails(String userLocation,def request,def className,def action,def user,def tenantId)
		{
			try{
				request.getHeader("VIA");
				String ipAddress = request.getHeader("X-FORWARDED-FOR");
					   if (ipAddress == null) {
							ipAddress = request.getRemoteAddr();
					   }
				GeoLocation $gl = javaQueryBundle.createGeoLocation();
				$gl.MAPTargetByIP(ipAddress, "This is Demo. You can set even NULL");
				userLocation = $gl.Country
			}catch(Exception e){
				println e+" PF controller saveFormViewUser action"
			}
			
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
		def status = 'fail'
		def jsonResult = []
		def statusMessage = 'Could not access the data'
		try{
			jsonResult = FormUtils.getLookUpResults(params,springSecurityService.currentUser,dataSource,dateFormatter)
		}catch(Exception e){
			
			log.error "Error in lookup: "+e
			statusMessage = 'Sorry an error occurred. Please try again'
		}
		render jsonResult as JSON
	}
	
	def search = {
		def term = params.term
		def jsonResult = []
		def uniqueFormEntry = UniqueFormEntry.findByUniqueId(term)
		if(uniqueFormEntry){
			jsonResult << [value:'Found',label:'Click to open',formId:uniqueFormEntry.formId,instanceId:uniqueFormEntry.instanceId]
		}
		render jsonResult as JSON
	}
	
	def createFormTable = {
		def form = Form.get(params.id)
		sqlDomainClassService.createForm(form)
	}
	
	def checkUserLoggedIn = {
		def data = [loggedIn:springSecurityService.isLoggedIn()]
		render data as JSON
	}
	
	def afterInterceptor = { model, modelAndView ->
		if (request['isMobile'] && modelAndView != null && modelAndView.viewName?.indexOf("error")>-1) {
			modelAndView.viewName = modelAndView.viewName + "_m"
		}
	}
}
