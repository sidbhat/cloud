
/* Copyright 2010 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.dynamicdomain
import java.text.DateFormat
import java.text.SimpleDateFormat;
import java.util.List;

import org.apache.poi.hssf.usermodel.HSSFCell;
import org.apache.poi.hssf.usermodel.HSSFDateUtil;
import org.apache.poi.hssf.usermodel.HSSFRow;
import org.apache.poi.hssf.usermodel.HSSFSheet;
import org.apache.poi.hssf.usermodel.HSSFWorkbook;
import org.apache.poi.poifs.filesystem.POIFSFileSystem;
import org.apache.poi.ss.usermodel.Cell
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.grails.formbuilder.*
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.oneapp.cloud.core.ActivityFeed;
import com.oneapp.cloud.core.ActivityFeedConfig;
import com.oneapp.cloud.core.ApplicationConf;
import com.oneapp.cloud.core.Client;
import com.oneapp.cloud.core.Role;
import com.oneapp.cloud.core.User;
import com.oneapp.cloud.core.UserAppAccessDetail;


import grails.converters.JSON
import org.grails.paypal.Payment;
  /**
 * Dynamic Controller for Dynamic Domain Class.
 * 
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 0.1
 */
class DdcController {
	
	def formTemplateService
	def domainClassService
	def clientService
	def sqlDomainClassService
	def mailChimpService
	//static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	static final List DOMAIN_CLASS_SYSTEM_FIELDS = ["id", "version", "dateCreated", "lastUpdated"]
	
	//below field is used to show all the entries in role is as follows
	static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS
	
	//below field is used to show all the fields while creating or editing form entry
	//TODO remove this from here and use this directly in the GSPs
	static final def ROLES_TO_SHOW_ALL_FIELDS = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_FIELDS
	def springSecurityService
	
//	def index = {
//		redirect(action: "list", params: params)
//	}
//	
//	def list = {
//		Form form
//		try{
//			form = Form.read(params.formId)
//		}catch(Exception e){}
//		if(form){
//			
//			FormAdmin fa
//			if(form){
//				fa = FormAdmin.findByForm(form)
//			}
//			if(fa){
//				def showOwnCreated = SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)
//				
//				params.max = Math.min(params.max ? params.int('max') : 10, 100)
//				def sortCol = params.sort?:'id'
//				def order = params.order?:'desc'
//				
//				def listTotalMap = sqlDomainClassService.list(form,showOwnCreated,[max:params.max,offset: params.offset?:0,sort:sortCol,order:order])
//				def formType = fa.formType
//				def formName = JSON.parse(form.settings)."en".name
//				
//				[domainInstanceList: listTotalMap.instanceList,
//				 domainInstanceTotal: listTotalMap.totalCount,
//				 formId:params.formId,
//				 formType: formType,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,form:form, formName:formName, formAdmin:fa]
//			}else{
//				flash.message = "form.notPublished"
//				flash.args = []
//				flash.defaultMessage = "Form not published."
//				redirect(controller:'form',action:'list')
//			}
//		}else{
//			flash.message = "form.notSelected"
//			flash.args = []
//			flash.defaultMessage = "No form Selected"
//			redirect(controller:'form',action:'list')
//		}
//	}
//	
//	def copy = {
//			
//			// Get the domain class details and field labels
//			def formName
//			def fields = new HashMap()
//			Form form  = Form.get(params.formId)
//			def domainInstance = form?sqlDomainClassService.get(params.id?.toLong(), form):null
//			if ( form && domainInstance ){
//				
//				if(form && form.tenantId == springSecurityService.currentUser.userTenantId){
//					formName = JSON.parse(form.settings)."en".name
//					form.fieldsList?.each { field ->
//						fields.put( field.name,field)
//					}
//					
//					render(view:'create',model: [formId:params.formId, form:form, formName:formName,fields:fields,domainInstance: domainInstance,
//					multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS]) // multiPart:true if form have upload component
//				}else{
//					flash.message = message(code:'form.not.accessible','default':'Form not accessible')
//					flash.defaultMessage = flash.message
//					redirect(controller:'dashboard')
//				}
//			}else{
//				flash.message = message(code:'domainClass.not.found','default':'Form not found')
//				flash.defaultMessage = flash.message
//				redirect(controller:'dashboard')
//			}
//		
//	}
//	
//	def create = {
//		try{
//			Form form  = Form.read( params.formId)
//			def formForFormAdminCheck
//			if(form){
//				def domainInstance = sqlDomainClassService.populate(params,form)
//				domainInstance.errors = []
//				def formName
//				def fields = new HashMap()
//				if(form.formCat == 'S'){//current form is subform
//					if(params.pfid){
//						formForFormAdminCheck = Form.read(params.pfid)
//					}else{
//						//TODO throw error that sub form must be entered through parent form only.
//					}
//				}else{
//					formForFormAdminCheck = form
//				}
//				if(form && form.tenantId == springSecurityService.currentUser.userTenantId){
//					if(!FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'create')){
//						flash.message = message(code:'form.not.accessible','default':'Form not accessible')
//						flash.defaultMessage = flash.message
//						redirect(controller:'dashboard')
//						return
//					}
//					formName = form.toString()
//					form.fieldsList?.each {Field field ->
//					    fields.put( field.name,field)
//					}
//					return [formId:params.formId, form:form, formName:formName,fields:fields,domainInstance: domainInstance, 
//					multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS] // multiPart:true if form have upload component
//				}else{
//					flash.message = message(code:'form.not.accessible','default':'Form not accessible')
//					flash.defaultMessage = flash.message
//					redirect(controller:'dashboard')
//				}
//			}else{
//				flash.message = message(code:'domainClass.not.found','default':'Form not found')
//				flash.defaultMessage = flash.message
//				redirect(controller:'dashboard')
//			}
//		}catch(Exception e){
//			flash.message = message(code:'some.problem','default':'Some problem occured')
//			flash.defaultMessage = flash.message
//			redirect(controller:'dashboard')
//		}
//	}
//	
//	def save = {
//		def domainInstance
//		try{
//			Form form  = Form.read( params.formId )
//			if(form){
//				domainInstance = sqlDomainClassService.populate(params, form)
//				def parentDomainInstance
//				def formForFormAdminCheck
//				if(form.formCat == 'S'){//current form is subform
//					if(params.pfid && params.pfii && params.pffn){
//						formForFormAdminCheck = Form.read(params.pfid)
//						def parentField = ((Form)formForFormAdminCheck).fieldsList.find{it.name == params.pffn}
//						if(parentField && parentField.type == 'SubForm'){
//							//parentDomainInstance = sqlDomainClassService.get(params.pfii,formForFormAdminCheck)
//							parentDomainInstance = true
////							if(!parentDomainInstance){
////								throw new Exception(message(code:'parent.formInstance.not.found',args:[],'default':"Sub form must be entered through parent form only, here the instance not found."))
////							}
//						}else{
//							throw new Exception(message(code:'parent.formField.not.found',args:[],'default':"Sub form must be entered through parent form only, here the field is not found."))
//						}
//					}else{
//						throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
//					}
//				}else{
//					formForFormAdminCheck = form
//				}
//				if(!FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'save')){
//					flash.message = message(code:'form.not.accessible','default':'Form not accessible')
//					flash.defaultMessage = flash.message
//					redirect(controller:'dashboard')
//					return
//				}
//				def fields = new HashMap()
//				def formName
//				formName = JSON.parse(form.settings)."en".name
//				def fieldsList = form.fieldsList
//				def itemsBought
//				def fieldNameSetting_MapList = []
//				fieldsList?.each { field ->
//						 def settings = grails.converters.JSON.parse(field.settings)
//		            	 def fieldName = settings."en".label
//						 fields.put( field.name,field)
//		                 if ( field.type == "PlainText" ) {
//		                 	def fieldVal = params[field.name]
//		                 	if ( fieldVal )
//		                 		domainInstance."${fieldVal}"= "true"
//		                 }else if ( field.type == "CheckBox" ) {
//		                 	def fieldVal = params.list(field.name).findAll{it}
//							def v
//							if ( fieldVal ){
//								v = fieldVal as List
//		                 		domainInstance."${field.name}"= (v as JSON).toString()
//							}else
//								domainInstance."${field.name}"= ""
//		                 }else if(field.type == "FileUpload"){
//						 	def keyValueMap = [:]
//							 keyValueMap."${field.name}" = settings
//						 	fieldNameSetting_MapList.add(keyValueMap)
//						 }else if(field.type == "Paypal"){
//						 itemsBought = domainInstance."${field.name}_bought"
//						 }
//		        }
//				
//				def totalFileSize = 0
//		        		
//		        if (request instanceof DefaultMultipartHttpServletRequest) {
//		            request.multipartFiles.each {k, v ->
//		                if (k) {
//							List<MultipartFile> files = new ArrayList()
//							def fieldName = k.replace('_file','')
//		                    if (v instanceof List) {
//		                        v.each {MultipartFile file ->
//									if(file && file.size>0){
//										files << file
//									}
//		                        }
//		                    } else {
//								MultipartFile file = v
//								if(file && file.size>0){
//									files << file
//								}
//		                    }
//							files.each{MultipartFile file->
//								def fileSize = file.size/1024
//								def field = fieldNameSetting_MapList.find{it."${fieldName}"}
//								if(field){
//									def settings = field.getAt(fieldName)
//									def maxSize = settings.maxSize
//									if(settings.unit == "MB"){
//										maxSize = settings.maxSize*1024
//									}
//									if(fileSize > maxSize){
//										domainInstance.errors.add([name:fieldName, code:"default.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for property ${settings.en.label} can not be greater than ${settings.maxSize} ${settings.unit}"])
//										render(view: "create", model: [formName:formName, form:form, domainInstance: domainInstance, fields:fields, formId:params.formId, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
//										return
//									}
//									totalFileSize += fileSize
//								}else{
//									domainInstance.errors.add([name:"version", code:"default.form.changed", args:[formName] as Object[], defaultMessage:"Someone might have changed the form while you were entering data. Please try again."])
//									render(view: "create", model: [formName:formName, form:form, domainInstance: domainInstance, fields:fields, formId:params.formId, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
//									return
//								}
//							}
//		                }
//		            }
//		        }
//				
//				//This code might not be required in case of SQL implementation of forms
////				if (domainInstance.hasErrors() || !domainInstance.validate()) {
////					render(view: "create", model: [formName:formName, form:form, domainInstance: domainInstance, fields:fields, domainClass: domainClass, formId:params.formId, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
////					return;
////				}
//				if(totalFileSize>0){
//						Client myClient = Client.get(springSecurityService.currentUser.userTenantId)
//						def earlyDataLength = clientService.getTotalAttachmentSize( myClient.id)
//						if( ( totalFileSize*1024 + earlyDataLength )/(1024*1024) > myClient.maxAttachmentSize){
//							domainInstance.errors.add([name:"version", code:"default.client.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for your client exceeded. Please contact administrator"])
//							render(view: "create", model: [formName:formName, form:form, domainInstance: domainInstance, fields:fields, formId:params.formId, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
//							return
//						}
//					}
//				
//		        
//		        domainInstance.createdBy = springSecurityService.currentUser
//				if (!domainInstance.errors && sqlDomainClassService.save(domainInstance,form)) {
//					
//					if(parentDomainInstance){
//						sqlDomainClassService.addSubFormInstance(domainInstance.id,params.pfii,params.pffn,formForFormAdminCheck)
//					}
//					def resultAttachment = attachUploadedFilesTo(form,domainInstance.id)
//					if(parentDomainInstance){
//						WebHookAdaptor.sendData(FormAdmin.findByForm(formForFormAdminCheck),domainInstance,params,resultAttachment?.uploadedFiles,"Save",form)
//					}else{
//						def formAdmin = FormAdmin.findByForm(form)
//						WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Save")
//						if(formAdmin.mailChimpDetails && formAdmin.mailChimpDetails!="")
//							mailChimpService.saveMailchimp(formAdmin, form, domainInstance)
//					}
//					
//					def pfc = new PFController()
//					pfc.saveUserDetails(params.location,request,form.domainClass.name,UserAppAccessDetail.CREATE,springSecurityService.currentUser,springSecurityService.currentUser.userTenantId)
//					if(itemsBought){
//						def formInstance=form
//						def paymentField = domainInstance.paymentField
//						def paymentFieldSettings = domainInstance.paymentFieldSettings
//						Payment payment = domainInstance.payment
//						payment.formId = formInstance.id
//						payment.instanceId = domainInstance.id
//						payment.buyerId = domainInstance.createdBy.id
//						payment.currency = Currency.getInstance(paymentFieldSettings.curr)
//						if(paymentFieldSettings.itemForm){
//							itemsBought.each{item->
//								payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
//									if(paymentItem.itemNumber == "${item.id}"){
//										paymentItem.amount = item[paymentFieldSettings.iaf]
//										def itemName = item[paymentFieldSettings.inf]?:"Item ${itemIdx}"
//										if(itemName.length()>126){
//											itemName = itemName.substring(0,123) + "..."
//										}
//										paymentItem.itemName = itemName
//										paymentItem.itemNumber = item.id+"_"+paymentFieldSettings.itemForm
//									}
//								}
//							}
//						}else{
//							payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
//								paymentItem.amount = domainInstance[paymentFieldSettings.iaf]
//								paymentItem.itemNumber = domainInstance.id+"_"+formInstance.id
//							}
//						}
//						if (payment?.validate()) {
//							request.payment = payment
//							payment.save(flush: true)
//							def config = grailsApplication.config.grails.paypal
//							def server
//							if(paymentFieldSettings.test){
//								server = config.testServer
//							}else{
//								server = config.server
//							}
//							def baseUrl = grailsApplication.config.grails.serverURL
//							def login = paymentFieldSettings.emid
//							if (!server || !login) throw new IllegalStateException("Paypal misconfigured! You need to specify the Paypal server URL and/or account email. Refer to documentation.")
//				
//							def commonParams = [buyerId: payment.buyerId, transactionId: payment.transactionId,returnAction: 'edit',returnController: 'ddc',cancelAction: 'edit',cancelController:'ddc']
//							def notifyURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'notify', params: commonParams).encodeAsURL()
//							def successURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'success', params: commonParams).encodeAsURL()
//							def cancelURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'cancel', params: commonParams).encodeAsURL()
//				
//							def url = new StringBuffer("$server?")
//							url << "business=$login&"
//							if(paymentFieldSettings.itemForm){
//								url << "cmd=_cart&upload=1&"
//								payment.paymentItems.eachWithIndex {paymentItem, i ->
//									def itemId = i + 1
//									url << "item_name_${itemId}=${paymentItem.itemName}&"
//									url << "item_number_${itemId}=${paymentItem.itemNumber}&"
//									url << "quantity_${itemId}=${paymentItem.quantity}&"
//									url << "amount_${itemId}=${paymentItem.amount}&"
//									if (payment.discountCartAmount == 0 && paymentItem.discountAmount > 0) {
//										url << "discount_amount_${itemId}=${paymentItem.discountAmount}&"
//									}
//								}
//							}else{
//								url << "cmd=_xclick&"
//								payment.paymentItems.eachWithIndex {paymentItem, i ->
//									url << "item_name=${paymentItem.itemName}&"
//									url << "amount=${paymentItem.amount}&"
//								}
//							}
//							
//							url << "currency_code=${payment.currency}&"
//							url << "notify_url=${notifyURL}&"
//							url << "return=${successURL}&"
//							url << "cancel_return=${cancelURL}"
//				
//							log.debug "Redirection to PayPal with URL: $url"
//							println "Url here is :===========>>>>>>>>"+url
//							redirect(url: url)
//							return
//						}
//						else {
//							//Handle in case validation fails
//						}
//					}
//					
//					flash.message = 'default.created.message'
//					//"${message(code: 'default.created.message', args: [message(code: '${domainClass.propertyName}.label', default: domainClass.name), domainInstance.id])}"
//					flash.args = [formName, domainInstance.id]
//					flash.defaultMessage = "Saved Successfully"
//					if(form.formCat == 'S'){
//						redirect(action: "edit", id: params.pfii, params:[formId:params.pfid])
//					}else
//						redirect(action: "edit", id: domainInstance.id, params:[formId:params.formId])
//				} else {
//					render(view: "create", model: [formName:formName, form:form, domainInstance: domainInstance, fields:fields, formId:params.formId, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
//					return
//				}
//			}else{
//				flash.message = 'default.not.found.message'
//				flash.args = [params.formId, params.id]
//				flash.defaultMessage = "Entry not found"
//				redirect(action: "list", params:[formId:params.formId])
//			}
//		}catch(Exception ex){
//			flash.message = ex.message
//			flash.defaultMessage = flash.message
//			redirect(action: "create", model:[formId:params.formId], params:domainInstance)
//		}
//	}
//	
//	def show = {
//		try{
//			Form form
//			def dc =params.dc
//			if(dc){
//				def name=dc.substring(0,dc.indexOf("."))
//				form  = Form.findByName(name)
//				params.formId = form.id
//			}else{
//				form = Form.read(params.formId)
//			}
//			if(form){
//				def formName
//				Form formForFormAdminCheck
//				def parentDomainInstance
//				def fields = new HashMap()
//				def domainInstance= form?sqlDomainClassService.get(params.id, form):null
//				if (!domainInstance) {
//					flash.message = message(code:'domainClass.not.found','default':'Form not found')
//					flash.defaultMessage = flash.message
//					redirect(controller:'dashboard')
//				}
//				else {
//					[domainInstance: domainInstance, formId: params.formId]
//				}
//			}
//			else{
//				flash.message = message(code:'domainClass.not.found','default':'Form not found')
//				flash.defaultMessage = flash.message
//				redirect(controller:'dashboard')
//			}
//		}
//		catch (Exception e) {
//			flash.message = ex.message
//			flash.defaultMessage = flash.message
//			redirect(action: "list", params:params)
//		}
//	}
	def edit = {
		try{
			Form form
			def dc =params.dc
			if(dc){
				def name=dc.substring(0,dc.indexOf("."))
				form  = Form.findByName(name)
				params.formId = form.id
			}else{
				form = Form.read(params.formId)
			}
			if(form){
				redirect(controller:"formViewer", action: "edit", id:params.id,params:params)
			}else{
				flash.message = "Form not found"
				flash.defaultMessage = flash.message
				redirect(controller:'dashboard')
			}
			
		}catch(Exception ex){
			flash.message = ex.message
			flash.defaultMessage = flash.message
			redirect(controller:'dashboard')
		}
	}
	
//	def update = {
//		println "update+++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++++"
//		def domainInstance
//		Form form
//		try{
//			form = Form.read(params.formId)
//			def formForFormAdminCheck
//			def domainInstanceId
//			try{
//				if(form.formCat == 'S'){
//					formForFormAdminCheck = Form.read(params.pfid)
//				}else{
//					formForFormAdminCheck = form
//				}
//				domainInstanceId = params.id.toLong()
//			}catch(Exception e){}
//			if(!domainInstanceId || !FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'edit',domainInstanceId)){
//				flash.message = message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')
//				flash.defaultMessage = flash.message
//				redirect(controller:'dashboard')
//				return
//			}
//			domainInstance = form?sqlDomainClassService.get(params.id, form):null
//			if(form && domainInstance){
//				def parentDomainInstance
//				def currentDataInstance = sqlDomainClassService.populate(params, form, domainInstance)
//				if(form.formCat == 'S'){//current form is subform
//					if(params.pfid && params.pfii && params.pffn){
//						parentDomainInstance = sqlDomainClassService.get(params.pfii, formForFormAdminCheck)
//						if(!parentDomainInstance){
//							throw new Exception(message(code:'parent.formInstance.not.found',args:[],'default':"Sub form must be entered through parent form only, here the instance not found."))
//						}
//					}else{
//						throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
//					}
//				}else{
//					formForFormAdminCheck = form
//					parentDomainInstance = domainInstance
//				}
//				def formName
//				formName = JSON.parse(form.settings)."en".name
//				def fields = new HashMap()
//				if (domainInstance) { 
//					if (params.version) {
//						def version = params.version.toLong()
//						if (domainInstance.version > version) {
//							form.fieldsList?.each { field ->
//								fields.put( field.name,field)
//							}
//							domainInstance.errors.add([name:"version", code:"default.optimistic.locking.failure", args:[formName] as Object[], defaultMessage:"Another user has updated this ${formName} while you were editing"])
//							render(view: "edit", model: [formId:params.formId,formName:formName, form:form, fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS],isEditable:isFormEditable(formForFormAdminCheck,parentDomainInstance))
//							return
//						}
//					}
//					def fieldNameSetting_MapList = []
//					def createdBy = domainInstance.created_by_id
//					def dateCreated = domainInstance.date_created
//					domainInstance = currentDataInstance
//					def itemsBought
//					domainInstance.dateCreated = dateCreated
//					domainInstance.created_by_id = createdBy
//					form.fieldsList?.each { field ->
//						fields.put( field.name,field)
//						 def settings = grails.converters.JSON.parse(field.settings)
//		            	 def fieldName = settings."en".label
//		                 if ( field.type == "PlainText" ) {
//		                 	def fieldVal = params[field.name]
//		                 	if ( fieldVal )
//		                 		domainInstance."${fieldVal}"= "true"
//		                 }else if ( field.type == "CheckBox" ) {
//		                 	def fieldVal = params.list(field.name).findAll{it}
//							def v
//							if ( fieldVal ){
//								v = fieldVal as List
//		                 		domainInstance."${field.name}"= (v as JSON).toString()
//							}else
//								domainInstance."${field.name}"= null
//		                 }else if(field.type == "FileUpload"){
//						 	def keyValueMap = [:]
//							 keyValueMap."${field.name}" = settings
//						 	fieldNameSetting_MapList.add(keyValueMap)
//		                 } else if(field.type == "Paypal"){
//								 itemsBought = domainInstance."${field.name}_bought"
//						 }
//		       
//					}
//			
//					def totalFileSize = 0
//			        		
//			        if (request instanceof DefaultMultipartHttpServletRequest) {
//			            request.multipartFiles.each {k, v ->
//			                if (k) {
//								List<MultipartFile> files = new ArrayList()
//								def fieldName = k.replace('_file','')
//			                    if (v instanceof List) {
//			                        v.each {MultipartFile file ->
//										if(file && file.size>0){
//											files << file
//										}
//			                        }
//			                    } else {
//									MultipartFile file = v
//									if(file && file.size>0){
//										files << file
//									}
//			                    }
//								files.each{MultipartFile file->
//									def fileSize = file.size/1024
//									def field = fieldNameSetting_MapList.find{it."${fieldName}"}
//									if(field){
//										def settings = field.getAt(fieldName)
//										def maxSize = settings.maxSize
//										if(settings.unit == "MB"){
//											maxSize = settings.maxSize*1024
//										}
//											if(fileSize > maxSize){
//												domainInstance.errors.add([name:fieldName, code:"default.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for property ${settings.en.label} can not be greater than ${settings.maxSize} ${settings.unit}"])
//												render(view: "edit", model: [formId:params.formId, formName:formName, form:form, fields: fields, domainInstance: domainInstance, multiPart:false, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:isFormEditable(formForFormAdminCheck,parentDomainInstance)])
//												return
//											}
//										totalFileSize += fileSize
//									}else{
//										domainInstance.errors.add([name:"version", code:"default.form.changed", args:[formName] as Object[], defaultMessage:"Someone might have changed the form while you were entering data. Please try again."])
//										render(view: "edit", model: [formId:params.formId, formName:formName, form:form, fields: fields, domainInstance: domainInstance, multiPart:false, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:isFormEditable(formForFormAdminCheck,parentDomainInstance)])
//										return
//									}
//								}
//			                }
//			            }
//			        }
//					
////					if (domainInstance.hasErrors() || !domainInstance.validate()) {
////						render(view: "edit", model: [formId:params.formId,formName:formName, form:form, fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:isFormEditable(formForFormAdminCheck,parentDomainInstance)])
////						return;
////					}
//					if(totalFileSize>0){
//							Client myClient = Client.get(springSecurityService.currentUser.userTenantId)
//							def earlyDataLength = clientService.getTotalAttachmentSize( myClient.id)
//							if( ( totalFileSize*1024 + earlyDataLength )/(1024*1024) > myClient.maxAttachmentSize){
//								domainInstance.errors.add([name:"version", code:"default.client.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for your client exceeded. Please contact administrator"])
//								render(view: "edit", model: [formId:params.formId, formName:formName, form:form, fields: fields, domainInstance: domainInstance, multiPart:false, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:isFormEditable(formForFormAdminCheck,parentDomainInstance)])
//								return
//							}
//						}
//			        domainInstance.updatedBy = springSecurityService.currentUser
//		    	   		
//		        	// end special handling 
//		        
//					if (!domainInstance.errors && sqlDomainClassService.update(domainInstance,form)) {
//						def resultAttachment = attachUploadedFilesTo(form,domainInstance.id)
//						
//						if(parentDomainInstance){
//							WebHookAdaptor.sendData(FormAdmin.findByForm(formForFormAdminCheck),domainInstance,params,resultAttachment?.uploadedFiles,"Update",form)
//						}else{
//							WebHookAdaptor.sendData(FormAdmin.findByForm(form),domainInstance,params,resultAttachment?.uploadedFiles,"Update")
//						}
//						if(itemsBought){
//							def formInstance = form
//							Payment p = Payment.findByFormIdAndInstanceId("${formInstance.id}","${domainInstance.id}")
//							p.delete(flush:true)
//							def paymentField = domainInstance.paymentField
//							def paymentFieldSettings = domainInstance.paymentFieldSettings
//							Payment payment = domainInstance.payment
//							payment.formId = formInstance.id
//							payment.instanceId = domainInstance.id
//							payment.buyerId = domainInstance.updatedBy.id
//							payment.currency = Currency.getInstance(paymentFieldSettings.curr)
//							if(paymentFieldSettings.itemForm){
//								itemsBought.each{item->
//									payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
//										if(paymentItem.itemNumber == "${item.id}"){
//											paymentItem.amount = item[paymentFieldSettings.iaf]
//											def itemName = item[paymentFieldSettings.inf]?:"Item ${itemIdx}"
//											if(itemName.length()>126){
//												itemName = itemName.substring(0,123) + "..."
//											}
//											paymentItem.itemName = itemName
//											paymentItem.itemNumber = item.id+"_"+paymentFieldSettings.itemForm
//										}
//									}
//								}
//							}else{
//								payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
//									paymentItem.amount = domainInstance[paymentFieldSettings.iaf]
//									paymentItem.itemNumber = domainInstance.id+"_"+formInstance.id
//								}
//							}
//							if (payment?.validate()) {
//								request.payment = payment
//								payment.save(flush: true)
//								def config = grailsApplication.config.grails.paypal
//								def server
//								if(paymentFieldSettings.test){
//									server = config.testServer
//								}else{
//									server = config.server
//								}
//								def baseUrl = grailsApplication.config.grails.serverURL
//								def login = paymentFieldSettings.emid
//								if (!server || !login) throw new IllegalStateException("Paypal misconfigured! You need to specify the Paypal server URL and/or account email. Refer to documentation.")
//					
//								def commonParams = [buyerId: payment.buyerId, transactionId: payment.transactionId,returnAction: 'edit',returnController: 'ddc',cancelAction: 'edit',cancelController:'ddc']
//								def notifyURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'notify', params: commonParams).encodeAsURL()
//								def successURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'success', params: commonParams).encodeAsURL()
//								def cancelURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'cancel', params: commonParams).encodeAsURL()
//					
//								def url = new StringBuffer("$server?")
//								url << "business=$login&"
//								if(paymentFieldSettings.itemForm){
//									url << "cmd=_cart&upload=1&"
//									payment.paymentItems.eachWithIndex {paymentItem, i ->
//										def itemId = i + 1
//										url << "item_name_${itemId}=${paymentItem.itemName}&"
//										url << "item_number_${itemId}=${paymentItem.itemNumber}&"
//										url << "quantity_${itemId}=${paymentItem.quantity}&"
//										url << "amount_${itemId}=${paymentItem.amount}&"
//										if (payment.discountCartAmount == 0 && paymentItem.discountAmount > 0) {
//											url << "discount_amount_${itemId}=${paymentItem.discountAmount}&"
//										}
//									}
//								}else{
//									url << "cmd=_xclick&"
//									payment.paymentItems.eachWithIndex {paymentItem, i ->
//										url << "item_name=${paymentItem.itemName}&"
//										url << "amount=${paymentItem.amount}&"
//									}
//								}
//								
//								url << "currency_code=${payment.currency}&"
//								url << "notify_url=${notifyURL}&"
//								url << "return=${successURL}&"
//								url << "cancel_return=${cancelURL}"
//					
//								log.debug "Redirection to PayPal with URL: $url"
//								redirect(url: url)
//								return
//							}
//							else {
//								//Handle in case validation fails
//							}
//						}
//						flash.message = 'default.updated.message'
//						flash.args = [formName, domainInstance.id]
//						flash.defaultMessage = "Successfully updated"
//						if(form.formCat == 'S'){
//							redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
//						}else{
//							redirect(action: "edit", id: params.id, params:[formId:params.formId])
//						}
//					}
//					else {
//						render(view: "edit", model: [formId:params.formId,formName:formName, form:form, fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:isFormEditable(formForFormAdminCheck,parentDomainInstance)])
//					}
//				}
//				else {
//					flash.message = 'default.not.found.message'
//					flash.args = [formName, params.id]
//					flash.defaultMessage = "Entry not found"
//					redirect(action: "list", params:[formId:params.formId])
//				}
//			}else{
//				flash.message = 'default.not.found.message'
//				flash.args = [params.formId, params.id]
//				flash.defaultMessage = "Entry not found"
//				redirect(action: "list", params:[formId:params.formId])
//			}
//		}catch(Exception ex){
//			flash.message = ex.message
//			flash.defaultMessage = flash.message
//			if(form.formCat == 'S'){
//				redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
//			}else{
//				redirect(action: "edit", id: params.id, params:[formId:params.formId])
//			}
//		}
//	}
//	
//	def delete = {
//		def domainInstance
//		def formName
//		def isDetete= false
//		try{
//			Form form
//			try{
//				form = Form.read(params.formId?.toLong())
//			}catch(Exception e){
//				log.error "Form not found"+e
//				throw new Exception("Form not found")
//			}
//			formName = JSON.parse(form.settings)."en".name
//			def parentForm
//			if(form){
//				domainInstance = sqlDomainClassService.get(params.id, form)
//				if(form.formCat == 'S'){
//					if(params.pfid && params.pfii && params.pffn){
//						parentForm = Form.read(params.pfid?.toLong())
//						isDetete=sqlDomainClassService.deleteSubForm(parentForm.name, domainInstance.id, params.pffn, form)
//					}
//				}else{
//					isDetete=sqlDomainClassService.delete(domainInstance.id,form)
//				}
//			}else{
//				flash.message = message(code:'domainClass.not.found','default':'Form not found')
//				flash.defaultMessage = flash.message
//				redirect(controller:'dashboard')
//			}
//			if(isDetete){
//				if( parentForm ){
//					WebHookAdaptor.sendData(FormAdmin.findByForm(parentForm),null,params,null,"Delete",form)
//				}else{
//					WebHookAdaptor.sendData(FormAdmin.findByForm(form),null,params,null,"Delete")
//				}
//				def activityFeedConfig = ActivityFeedConfig.findByConfigName(form.name+"."+form.name)
//				def activityFeed = ActivityFeed.findByShareIdAndConfig(domainInstance.id,activityFeedConfig)
//				if(activityFeed)
//					activityFeed.delete(flush:true)
//				flash.message = 'default.deleted.message'
//				flash.args = [formName?:domainInstance.id, domainInstance.id]
//				flash.defaultMessage = "Successfully Deleted"
//				if(form.formCat == 'S'){
//					redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
//				}else{
//					redirect(action: "list", id: params.id, params:[formId:params.formId])
//				}
//			}else{
//				flash.message = 'default.not.deleted.message'
//				flash.args = [formName?:domainInstance.id, domainInstance.id]
//				flash.defaultMessage = "Form not Deleted"
//				if(form.formCat == 'S'){
//					redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
//				}else{
//					redirect(action: "list", id: params.id, params:[formId:params.formId])
//				}
//			}
//		}catch(Exception ex){
//			flash.message = "Error Occured"+ex.message
//			flash.defaultMessage = flash.message
//			redirect(action: "list",id: params.id, params:[formId:params.formId] )
//	  }
//	}
//
//	def multipleDelete = {
//		try{
//			def formName
//			def selectedInstance = params.list("selectCheck")
//			def selectedInstanceList = selectedInstance*.toLong()
//			Form form
//			try{
//				form = Form.read(params.formId?.toLong())
//			}catch(Exception e){
//				log.error "Form not found"+e
//				throw new Exception("Form not found")
//			}
//			formName = JSON.parse(form.settings)."en".name
//			if(form && selectedInstanceList.size>0){
//				selectedInstanceList.each{
//					def domainInstance = sqlDomainClassService.get(it, form)
//					if(domainInstance){
//						def isDetete=sqlDomainClassService.delete(domainInstance.id,form)
//						if(isDetete){
//							def activityFeedConfig = ActivityFeedConfig.findByConfigName(form.name+"."+form.name)
//							def activityFeed = ActivityFeed.findByShareIdAndConfig(domainInstance.id,activityFeedConfig)
//							if(activityFeed)
//								activityFeed.delete()
//						}else{
//							flash.message = 'default.not.deleted.message'
//							flash.args = [formName?:domainInstance.id, domainInstance.id]
//							flash.defaultMessage = "Form not Deleted"
//							redirect(action: "list", id: params.id, params:[formId:params.formId])
//							return
//						}
//					}else{
//						flash.message = message(code:'domainClass.not.found','default':'Form not found')
//						flash.defaultMessage = flash.message
//						redirect(controller:'dashboard')
//						return
//					}
//				}
//			}
//			flash.message = "${message(code: 'default.deleted.message', args: [formName, selectedInstance])}"
//			flash.defaultMessage = flash.message
//			redirect(action: "list", id: params.id, params:[formId:params.formId])
//		}catch(Exception ex){
//					flash.message = "Error Occured"+ex.message
//					flash.defaultMessage = flash.message
//					redirect(action: "list",id: params.id, params:[formId:params.formId] )
//				}
//	}
//	
//	def afterInterceptor = { model, modelAndView ->
//  		if (request['isMobile'] && modelAndView != null ) {
//  			modelAndView.viewName = modelAndView.viewName + "_m"
//  			println "DdcController-afterInterceptor: "+request['isMobile'] +modelAndView.viewName
// 	 }
//	}
//	
//	private boolean isFormEditable(Form form, Object domainInstance){
//		boolean isEditable = true;
//		if(form && domainInstance){
//			FormAdmin fa = FormAdmin.findByForm(form);
//			Field statusField = fa.statusField
//			if(statusField && fa.blockUserEditing?.contains(domainInstance."${statusField.name}")){
//				isEditable = false
//			}
//		}
//		return isEditable
//	}
	
	def excelUpload = {
		try{
			def uploadedFile = params.uploadFormInstance
			if(uploadedFile && uploadedFile.size > 0)
			{
				def uploadFileSize = (uploadedFile.size/1024)
				if(uploadFileSize > 10240){
					throw new Exception("File size cannot be more then 10MB")
				}else{
					def extension = uploadedFile.fileItem.fileName.substring(uploadedFile.fileItem.fileName.indexOf("."),uploadedFile.fileItem.fileName.size())
					if(!(".xls".equalsIgnoreCase(extension))){
						throw new Exception("File format not supported")
					}
				}
				Form form = Form.read(params.formId?.toLong())
				def uploadResponse = uploadDataToForm(uploadedFile,form)
				if(!uploadResponse.errorOccured){
					flash.message = "Total Rows imported :"+uploadResponse.rowsadded+""
					flash.defaultMessage = flash.message
				}else{
					flash.message = "Total Rows imported :"+uploadResponse.rowsadded+" "+uploadResponse.errorMessage
					flash.defaultMessage = flash.message
					
				}
			}else{
				throw new Exception("No file selected to upload")
			}
			/*def dc = DomainClass.findByName(params.dc)
			def domainClass = grailsApplication.getDomainClass(params.dc)
			if(!domainClass || dc?.updated){
				domainClassService.reloadUpdatedDomainClasses()
				domainClass = grailsApplication.getDomainClass(params.dc)
			}*/
			
			redirect(controller:"formViewer", action: "list",params:[formId:params.formId])
		}catch(Exception ex){
			flash.message = "Error Occured : "+ex.message
			flash.defaultMessage = flash.message
			redirect(controller:"formViewer", action: "list",params:[formId:params.formId])
		}
	}
	
	
	def uploadDataToForm(def uploadedFile,Form form){
		def uploadResponse = [:]
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		InputStream fileIn = null;
		try{
			POIFSFileSystem fs = new POIFSFileSystem(uploadedFile.getInputStream())
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFCell cell;
			int rows; // No of rows
			def counter = 0;
			rows = sheet.getPhysicalNumberOfRows();
			def fileCaptionList = new ArrayList();
			def errorOccured = false
			def isHeaderSet = false
			def errorMessage = ""
			def rowsadded=0
			def formName
			formName = JSON.parse(form.settings)."en".name
			def fieldsForMessage = new HashMap()
			form.fieldsList?.each { field ->
			   fieldsForMessage.put( field.name,field)
			}
			while (counter <= sheet.getLastRowNum())
			{
				if(rows > 1) //data present
				{
					row = sheet.getRow(counter);
					if(row != null)
					{
						//Get First Time data
						int firstColumnIndex = row.getFirstCellNum();
						int lastColumnIndex = row.getLastCellNum();
						int totelColumnCount = lastColumnIndex -firstColumnIndex;
						if(totelColumnCount != 0)
						{
							def domainInstance = sqlDomainClassService.newInstance(form)
							if(!isHeaderSet)
							{
								for(int j = 0; j <lastColumnIndex; j++ )
								{
									cell = row.getCell(j);
									if(cell != null)
									{
										def columnCaption = getCellData(cell);
										def colType = getCellType(cell)
										if("SingleLineNumber".equalsIgnoreCase(colType))
										{
											columnCaption = ""+columnCaption
										}
										fileCaptionList.add(columnCaption);
									}else{
										String columnCaption = "";
										fileCaptionList.add(columnCaption);
									}
								}
								isHeaderSet = true
							}else{
								def fieldValue = null;
								try{
									for(int i =firstColumnIndex; i<lastColumnIndex; i++){
											cell = row.getCell(i);
											def value = null;
											if ( cell != null ) {
												fieldValue = form.fieldsList[i]
												def settings = JSON.parse(fieldValue.settings)
												def fieldName = settings."en".label
												if(!fieldName.equalsIgnoreCase("Created By") && !fieldName.equalsIgnoreCase("Date Created")){
													value = getCellData(cell);
													if(value ){
														domainInstance."${fieldValue.name}" = value
													}
												}
											}
										}
										 domainInstance.createdBy = springSecurityService.currentUser
										 domainInstance.dateCreated = new Date()
										if (!domainInstance.errors && sqlDomainClassService.save(domainInstance,form)) {
											/*domainInstance.save(flush: true)*/
											rowsadded++;
										}else{
											errorOccured = true;
											domainInstance.errors.allErrors.each {
												def fieldName = fieldsForMessage."${it.field}"
												def fieldDisplayNameSetting = JSON.parse(fieldName.settings)
												def fieldDisplayName = fieldDisplayNameSetting."en".label
												errorMessage += "<br>Row "+(counter+1)+":"+"${message(code: it.code, args: [fieldDisplayName,formName], default:it.defaultMessage)}"
											}
											
										}
								}catch( Exception ex){
									errorMessage += "<br>Row "+(counter+1)+": Column "+fieldValue+" "+ex.message
									errorOccured = true;
								}
							}
						}
					}
				}
				counter++;
			}
			uploadResponse.rowsadded = rowsadded
			uploadResponse.errorMessage = errorMessage
			uploadResponse.errorOccured = errorOccured
		}catch(Exception ex){
			print ex
		}
		return uploadResponse
	}
	
	def getCellData(Cell cell)
	{
		def cellData;
		int cellType = cell.getCellType();
		if( cellType == Cell.CELL_TYPE_STRING)
			cellData = cell.getStringCellValue();
		else if( cellType == Cell.CELL_TYPE_NUMERIC)
		{
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
					cellData = date;
			 }else
			 {
				cellData = cell.getNumericCellValue();
			 }

		}else if( cellType == Cell.CELL_TYPE_FORMULA)
		{
			def cellFormula = cell.getCachedFormulaResultType();
			if(cellFormula == Cell.CELL_TYPE_NUMERIC)
				cellData = cell.getNumericCellValue();
			else
				cellData = cell.getRichStringCellValue();
		}
		return cellData;
	}
	
	def getCellType(Cell cell){
		def cellDataType;
		int cellType = cell.getCellType();
		if( cellType == Cell.CELL_TYPE_STRING)
			cellDataType = "SingleLineText";
		else if( cellType == Cell.CELL_TYPE_NUMERIC)
		{
			if (HSSFDateUtil.isCellDateFormatted(cell)) {
				Date date = cell.getDateCellValue();
					cellDataType = "SingleLineDate";
			 }else
			 {
				cellDataType = "SingleLineNumber";
			 }

		}else if( cellType == Cell.CELL_TYPE_FORMULA)
		{
			def cellFormula = cell.getCachedFormulaResultType();
			if(cellFormula == Cell.CELL_TYPE_NUMERIC)
				cellDataType = "SingleLineNumber";
			else
				cellDataType = "SingleLineText";
		}
		return cellDataType;
	}
	
	def excelFormCreate = {
		try{
			def uploadedFile = params.uploadFormInstance
			if(uploadedFile && uploadedFile.size > 0)
			{
				def formCount = Form.count()
				def appConf = ApplicationConf.read(1)
				if(formCount < appConf.form){
					def clientInstance = Client.get(springSecurityService?.currentUser.userTenantId)
					def currentClientFormCount = Form.findAllByTenantId(springSecurityService?.currentUser.userTenantId).size()
					if(currentClientFormCount < clientInstance.form){
					}else{
						throw new Exception("You can only create ${clientInstance.form} forms.")
					}
				}else{
					throw new Exception("You have reached the maximum form creation Limit. Please contact your Administrator")
				}
				def uploadFileSize = (uploadedFile.size/1024)
				if(uploadFileSize > 10240){
					throw new Exception("File size cannot be more then 10MB")
				}else{
					def extension = uploadedFile.fileItem.fileName.substring(uploadedFile.fileItem.fileName.indexOf("."),uploadedFile.fileItem.fileName.size())
					if(!(".xls".equalsIgnoreCase(extension))){
						throw new Exception("File format not supported.Please upload .xls file")
					}
				}
				
			}
			def formInstance = new Form()
			def u = User.get( springSecurityService?.currentUser.id)
			formInstance.tenantId = u.userTenantId
			// update the form name
			def formCnt = new FormCounter().save(flush:true)
			formInstance.name = "Form${formCnt.id}"
			def formSettings = JSON.parse('{"en":{"name":"","description":"","classes":["centerAlign"],"heading":"h2","styles":{"fontFamily":"Georgia,Georgia,serif","fontSize":"14","fontStyles":[1,0,0]}},"zh_CN":{"name":"??","classes":["rightAlign"],"heading":"h2","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[1,0,0]}},"styles":{"color":"default","backgroundColor":"default"}}')
			def formSettingName = uploadedFile.fileItem.fileName.substring(0,uploadedFile.fileItem.fileName.indexOf("."))
			formSettings."en".name = formSettingName
			formInstance.settings = formSettings
			def fieldList = fieldListForForm(uploadedFile)
			//formInstance.formCat = params.formCat
			formInstance.formCat = 'N'
			formInstance.fieldsList = fieldList
			formInstance.domainClass = domainClassService.getDomainClass(formInstance)
			formInstance.persistableFieldsCount = formTemplateService.getPersistableFieldsCount(formInstance.fieldsList)
			formInstance.createdBy = springSecurityService?.currentUser
			if (formInstance.save(flush: true)) {
				try{
					sqlDomainClassService.createForm(formInstance)
					/*DomainClass dc = DomainClass.findByName( formInstance.domainClass.name)
					def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
					if(!domainClass || dc?.updated){
						domainClassService.reloadUpdatedDomainClasses()
						domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
					}*/
					def uploadResponse = uploadDataToForm(uploadedFile,formInstance)
					if(!uploadResponse.errorOccured){
						def formUploadMsg = "<br/>Total Rows imported :"+uploadResponse.rowsadded+""
						flash.message = "${message(code: 'form.created.message', args: [message(code: 'form.label', default: 'Form'), formSettingName?:formInstance.id, g.link(controller:'formAdmin',action:'create',params:[formId:formInstance.id]){message(code:'form.created.formAdminClickHere',default:'here')}])}"+formUploadMsg
						flash.defaultMessage = flash.message
						redirect(controller:"form",action: "list")
					}else{
						def formUploadMsg = "<br/>Total Rows imported :"+uploadResponse.rowsadded+" "+uploadResponse.errorMessage
						flash.message = "${message(code: 'form.created.message', args: [message(code: 'form.label', default: 'Form'), formSettingName?:formInstance.id, g.link(controller:'formAdmin',action:'create',params:[formId:formInstance.id]){message(code:'form.created.formAdminClickHere',default:'here')}])}"+formUploadMsg
						flash.defaultMessage = flash.message
						redirect(controller:"form",action: "list")
					}
				}catch(Exception ex){
					flash.message = "Error Occured "+ex.message
					flash.defaultMessage = flash.message
					redirect(controller:"form",action: "list")
				}
			}else{
				flash.message = "Error Occured"
				flash.defaultMessage = flash.message
				redirect(controller:"form", action: "list")
			}
		}catch(Exception ex){
				flash.message = "Error Occured "+ex.message
				flash.defaultMessage = flash.message
				redirect(controller:"form", action: "list")
			}
	}
	
	def fieldListForForm(def uploadedFile){
		List fieldsList = new ArrayList()
		HSSFWorkbook wb = null;
		HSSFSheet sheet = null;
		InputStream fileIn = null;
		try{
			POIFSFileSystem fs = new POIFSFileSystem(uploadedFile.getInputStream())
			wb = new HSSFWorkbook(fs);
			sheet = wb.getSheetAt(0);
			HSSFRow row;
			HSSFCell cell;
			int rows; // No of rows
			def counter = 0;
			def rowCounter = 0;
			rows = sheet.getPhysicalNumberOfRows();
			if(rows > 10000)
			{
				throw new Exception("Data to large cannot upload the file")
			}
			def fileCaptionList = new ArrayList();
			def isHeaderSet = false
			def fieldCaptionList = new ArrayList()
			int firstColumnIndex = 0;
			int lastColumnIndex = 0;
			int totelColumnCount = 0;
			while (counter <= sheet.getLastRowNum())
			{
				if(rows > 1) //data present
				{
					row = sheet.getRow(counter);
					if(row != null)
					{
						//Get First Time data
						if(rowCounter == 0){
							firstColumnIndex = row.getFirstCellNum();
							lastColumnIndex = row.getLastCellNum();
							totelColumnCount = lastColumnIndex -firstColumnIndex;
						}
						if(totelColumnCount != 0)
						{
							if(!isHeaderSet)
							{
								for(int j = 0; j <lastColumnIndex; j++ )
								{
									cell = row.getCell(j);
									def fieldLabelDetails = [:]
									if(cell != null)
									{
										def cellType = getCellType(cell)
										fieldLabelDetails.label = getCellData(cell);
										fieldLabelDetails.type = cellType
										fileCaptionList.add(fieldLabelDetails);
									}else{
										fieldLabelDetails.label = "";
										fieldLabelDetails.type = ""
										fileCaptionList.add(fieldLabelDetails);
									}
								}
								isHeaderSet = true
								rowCounter++;
							}else{
								def fieldValue = null;
								try{
									for(int i =firstColumnIndex; i<lastColumnIndex; i++){
										def fieldLabelDetails = [:]
										cell = row.getCell(i);
										def value = null;
										def dataType = null
										if ( cell != null ) {
											fieldValue = fileCaptionList[i]
											value = getCellData(cell);
											dataType = getCellType(cell)
											if(fieldValue.type != dataType && dataType != null)
												fieldValue.type = dataType
											if(rowCounter == 1){
												fieldCaptionList.add(fieldValue)
											}else {
												if(fieldCaptionList[i].label == fieldValue.label){
													if(fieldCaptionList[i].type == "SingleLineText"){
														if(value.size() > 255)
															fieldCaptionList[i].type = "MultiLineText"
													}
												}
											}
										}else if(fileCaptionList[i].label != null && cell == null && rowCounter == 1){
											fieldCaptionList.add(fileCaptionList[i])
										}
									}
									rowCounter++;
								}catch( Exception ex){
									
								}
							}
						}
					}
				}
				counter++;
			}
			fieldCaptionList.eachWithIndex{ f,i ->
				Field fieldInstance = new Field()
				def fieldSettings
				fieldInstance.name = "field"+(new Date().getTime())+i
				fieldInstance.sequence = i
				fieldInstance.type = f.type
				if(f.label == null || f.label == ""){
					throw new Exception("Header field cannot be empty")
				}else{
					if(fieldInstance.type == "SingleLineText"){
						fieldSettings = JSON.parse('{"en":{"label":"Single Line Text 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"zh_CN":{"label":"?????? 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"_persistable":true,"required":false,"hideFromUser":false,"mapMasterForm":"","mapMasterField":"","lookUp":false,"restriction":"no","styles":{"label":{"color":"default","backgroundColor":"default"},"value":{"color":"default","backgroundColor":"default"},"description":{"color":"777777","backgroundColor":"default"}}}')
						fieldSettings."en".label = f.label
					}else if(fieldInstance.type == "MultiLineText"){
						fieldSettings = JSON.parse('{"en":{"label":"Multi Line Text 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"zh_CN":{"label":"?????? 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"_persistable":true,"required":false,"hideFromUser":false,"mapMasterForm":"","mapMasterField":"","restriction":"no","styles":{"label":{"color":"default","backgroundColor":"default"},"value":{"color":"default","backgroundColor":"default"},"description":{"color":"777777","backgroundColor":"default"}}}')
						fieldSettings."en".label = f.label
					}else if(fieldInstance.type == "SingleLineDate"){
						fieldSettings = JSON.parse('{"en":{"label":"Date 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"zh_CN":{"label":"?????? 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"_persistable":true,"required":false,"hideFromUser":false,"mapMasterForm":"","mapMasterField":"","restriction":"no","styles":{"label":{"color":"default","backgroundColor":"default"},"value":{"color":"default","backgroundColor":"default"},"description":{"color":"777777","backgroundColor":"default"}}}')
						fieldSettings."en".label = f.label
					}else if(fieldInstance.type == "SingleLineNumber"){
						fieldSettings = JSON.parse('{"en":{"label":"Numeric Text 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"zh_CN":{"label":"?????? 1","value":"","description":"","styles":{"fontFamily":"default","fontSize":"default","fontStyles":[0,0,0]}},"_persistable":true,"required":false,"hideFromUser":false,"mapMasterForm":"","mapMasterField":"","restriction":"no","styles":{"label":{"color":"default","backgroundColor":"default"},"value":{"color":"default","backgroundColor":"default"},"description":{"color":"777777","backgroundColor":"default"}}}')
						fieldSettings."en".label = ""+f.label
					}
				}
				fieldInstance.settings = fieldSettings
				fieldsList.add(fieldInstance)
			}
		}catch(Exception ex){
			throw new Exception(ex.message)
		}
		return fieldsList
	}
	
	
	
	/*def getCellData1(Cell cell,def field)
	{
		def cellData;
		String fieldType = field.type;
		if( fieldType == ConfigurationHolder.config.formBuilder.fieldType.PLAIN_TEXT || 
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.SINGLE_LINE_TEXT || 
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.MULTILINE_TEXT ||
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.DROPDOWN ||
		    fieldType == ConfigurationHolder.config.formBuilder.fieldType.GROUP_BUTTON ||
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.CHECKBOX ||
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.PLAIN_TEXT_HREF ||
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.SCALE_RATING ||
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.IMAGE_UPLOAD ||
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.LINK_VIDEO ||
			fieldType == ConfigurationHolder.config.formBuilder.fieldType.FILE_UPLOAD
		){
			cellData = cell.getStringCellValue();
		}else if( fieldType == ConfigurationHolder.config.formBuilder.fieldType.SINGLE_LINE_DATE){
			  DateFormat formatter = new SimpleDateFormat("yyyy-MM-dd");
			  cellData = (Date)formatter.parse(cell.getStringCellValue());
		}else if(fieldType == ConfigurationHolder.config.formBuilder.fieldType.SINGLE_LINE_NUMBER){
			cellData = cell.getNumericCellValue()
		}else if(fieldType == ConfigurationHolder.config.formBuilder.fieldType.FORMULA_FIELD){
			def settings = JSON.parse(field.settings)
			if(settings."en".newResultType == "NumberResult"){
				cellData = cell.getNumericCellValue()
			}else if(settings."en".newResultType == "DateResult"){
				  DateFormat formatter = new SimpleDateFormat("dd-MMM-yy");
				  cellData = (Date)formatter.parse(cell.getStringCellValue());
			}
		}
		return cellData;
	}*/
}
