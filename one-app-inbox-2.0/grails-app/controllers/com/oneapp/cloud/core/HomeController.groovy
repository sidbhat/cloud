

package com.oneapp.cloud.core

import java.math.MathContext;

import grails.converters.JSON;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.grails.formbuilder.Field;
import org.grails.formbuilder.Form;
import org.grails.formbuilder.FormBuilderConstants;
import org.grails.formbuilder.UniqueFormEntry;
import org.grails.paypal.Payment;
import org.grails.paypal.PaymentItem;

import com.google.gdata.util.AuthenticationException;
import com.oneappcloud.DocumentList;

class HomeController {
	
	def domainClassService
	def springSecurityService
	def exportService
	def sqlDomainClassService
	
	static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS

    def index = {  }
    def afterInterceptor = { model, modelAndView ->
  		if (request['isMobile'] && modelAndView != null ) {
  			//println "HomeController-index: "+request['isMobile'] 
  			modelAndView.viewName = modelAndView.viewName + "_m"
 	 }
		  if (request['isTablet'] && modelAndView != null ) {
			  //println "HomeController-index: "+request['isTablet']
			  modelAndView.viewName = modelAndView.viewName + "_t"
	  }
    }
	
	
	
	def exportToGoogleDocs = {
		File f
		FileOutputStream fo
		def msg = ""
		try{
	 		f = new File("one-app-form-response-${new Date().getTime()}.${params.extension}")
			fo = new FileOutputStream(f)
			exportData(fo)
			fo.flush()
			fo.close()
			DocumentList documentList = new DocumentList("form-builder")
			documentList.loginWithConsumerKeyAndSecret()
			documentList.uploadFile(f, f.getName(), springSecurityService.currentUser.username);
			flash.message = message(code:"googleDocs.doc.created",args:[f.getName()],default:"Document created for your docs on your mail id.")
			flash.args = []
			flash.defaultMessage = flash.message
			redirect(controller:params.returnController?:"ddc",action:"list",params:[formId:params.formId])
			return
		}catch(AuthenticationException e){
			msg = message(code:"googleDocs.not.authenticated",default:"Sorry, we could not authenticate you on Google Marketplace.")
		}catch(Exception e){
			msg = message(code:"googleDocs.doc.not.created",default:"Sorry, document could not be created.")
		}finally{
			if(fo){
				fo.close()
			}
			if(f){
				f.delete()
			}
		  }
		flash.message = msg
		flash.args = []
		flash.defaultMessage = flash.message
		redirect(controller:params.returnController?:"ddc",action:"list",params:[formId:params.formId])
		return
	}
	
	def exportData = {OutputStream out ->
		Form form = session.formInstance ?: Form.read(params.formId)
		
	  List fields = []
	  Map labels = [:]
	  boolean hasSubForm = false
	  def dataListWithSubFormData = []
		def subFormHeaderBar = [:]
	  def filedsNotToShow = grailsApplication.config.formAdmin.fields.notKeyFigures
	  
	  def mainFormFields = form.fieldsList.findAll{!filedsNotToShow.contains(it.type)}
	  labels.uid = "Unique Id"
	  fields.add("uid")
	  mainFormFields?.each {Field field ->
		  if(field.settings.indexOf(FormBuilderConstants.PERSISTABLE) > -1){
			  def fieldSettings = JSON.parse(field.settings)
			  if(field.type != 'SubForm' || !fieldSettings.subForm){
			      if(field.type=='Paypal'){
					  fields.add("${field.name}Amount")
					  fields.add("${field.name}Status")
					  //(fieldSettings."${request.locale.baseLocale.language}")?:
					  labels."${field.name}Amount"="Amount"
					  labels."${field.name}Status"="Status"
				  }else if(field?.type == 'AddressField'){
					  fields.add("${field.name}(Address Line1)")
					  fields.add("${field.name}(Address Line2)")
					  fields.add("${field.name}(City)")
					  fields.add("${field.name}(State)") 
					  fields.add("${field.name}(Zip/Postal Code)")
					  fields.add("${field.name}(Country)")
					  def localeSpecificSettings = fieldSettings.en
					  def label = localeSpecificSettings.label
					  labels."${field.name}(Address Line1)"="${label}(Address Line1)"
					  labels."${field.name}(Address Line2)"= "${label}(Address Line2)"
					  labels."${field.name}(City)"= "${label}(City)"
					  labels."${field.name}(State)" ="${label}(State)"
					  labels."${field.name}(Country)" ="${label}(Country)"
					  labels."${field.name}(Zip/Postal Code)"= "${label}(Zip/Postal Code)"
				  }else if(field?.type == 'SingleLineNumber'){
					  boolean currencyType=(fieldSettings?.currencyType && fieldSettings?.currencyType!='')?true:false
					  String currency=currencyType?grailsApplication.config?.formBuilder.currencies[fieldSettings.currencyType]:''
					  if(currencyType){
						  fields.add("${field.name}(${(currency).decodeHTML()})")
						  def localeSpecificSettings = fieldSettings.en
						  def label = localeSpecificSettings.label
						  labels."${field.name}(${(currency).decodeHTML()})"=label+"(${(currency).decodeHTML()})"
						 }else{
						 fields.add(field.name)
						 def localeSpecificSettings = fieldSettings.en
						 def label = localeSpecificSettings.label
						 labels."${field.name}"=label
						 }
					}else if(field?.type == 'NameTypeField'){
					  boolean	pre=(fieldSettings?.showPrefix)
					  boolean mid=(fieldSettings?.showMiddleName)
					  if(pre)
						  fields.add("${field.name}(Prefix)")
					  fields.add("${field.name}(First Name)")
					  if(mid)
						  fields.add("${field.name}(Middle Name)")
					  fields.add("${field.name}(Last Name)")
					  def localeSpecificSettings = fieldSettings.en
					  def label = localeSpecificSettings.label
					  if(pre)
					  	labels."${field.name}(Prefix)"="${label}(Prefix)"
					  labels."${field.name}(First Name)"= "${label}(First Name)"
					  if(mid)
					  	labels."${field.name}(Middle Name)"= "${label}(Middle Name)"
					  labels."${field.name}(Last Name)" ="${label}(Last Name)"
				  }else{
				      fields.add(field.name)
					  def localeSpecificSettings = fieldSettings.en
					  def label = localeSpecificSettings.label
					  labels."${field.name}"=label
				  }
			  } else {
			  	Form subForm = Form.read(fieldSettings.subForm)
				if(subForm){
					def subFormName = subForm.toString()
					def subFieldsList = subForm.fieldsList.findAll{!filedsNotToShow.contains(it.type)}
					subFieldsList.each{Field subField->
						fields.add(subField.name)
						def subFieldSettings = JSON.parse(subField.settings)
						def label = subFieldSettings.en.label
						if(subField?.type == 'SingleLineNumber'){
							def subSettings = grails.converters.JSON.parse(subField.settings)
							boolean currencyType=(subFieldSettings?.currencyType && subFieldSettings?.currencyType!='')?true:false;
							String currency=currencyType?" (${ConfigurationHolder.config?.formBuilder.currencies[subFieldSettings.currencyType].decodeHTML()})":''
							label+=currency
						}
						labels."${subField.name}"=label
						subFormHeaderBar."${subField.name}"="("+subFormName+")"
					}
					hasSubForm = true
				}else{
					fields.add(field.name)
					def label = fieldSettings.en.label
					labels."${field.name}"=label
				}
			  }
		  }
	  }
	  
	  def extraFields = grailsApplication.config.formViewer?.responseList?.extraFieldsToShow
	  extraFields?.each{field->
		  fields.add(field.name)
		  def label = message(code:field.messageCode,default:field.messageDefault)
		  labels."${field.name}"=label
	  }
	  
	  def listTotalMap = sqlDomainClassService.list(form,SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS))
	  def dataList = listTotalMap.instanceList

		def tempDataList
		def extraFields2 = grailsApplication.config.formViewer.responseList.export.extraFieldsToShow
		
		if(subFormHeaderBar){
			dataListWithSubFormData.add(subFormHeaderBar)
		}
		
		dataList.each{dataInstance->
			tempDataList = []
			tempDataList << [:]
			def uniqueFormId = UniqueFormEntry.findByFormIdAndInstanceId(params.formId.toLong(),"${dataInstance.id}".toLong())
			def attachments = form.getDomainAttachments(dataInstance.id)
			dataInstance.created_by_id = User.get(dataInstance.created_by_id)?.toString()
			//dataInstance.dateCreated = dataInstance.date_created
			tempDataList.get(0).uid = uniqueFormId?.uniqueId?:dataInstance.id
			mainFormFields.each{Field field->
				if(field.type == 'FileUpload'){
					def thisFieldAttachments = attachments.find{it.inputName == field.name+'_file'}//find is used just to check if it has atleast one attachment
					if(thisFieldAttachments){
						tempDataList.get(0)."${field.name}" = "Y"
					}else{
						tempDataList.get(0)."${field.name}" = "N"
					}
				}else if(field?.type == 'AddressField'){
					def mapValue
					if(dataInstance."${field.name}"){
						mapValue = grails.converters.JSON.parse(dataInstance."${field.name}")
					}
					tempDataList.get(0)."${field.name}(Address Line1)"=mapValue?."line1"?mapValue."line1":""
					tempDataList.get(0)."${field.name}(Address Line2)"= mapValue?."line2"?mapValue."line2":""
					tempDataList.get(0)."${field.name}(City)"= mapValue?."city"?mapValue."city":""
					tempDataList.get(0)."${field.name}(State)" =mapValue?."state"?mapValue."state":""
					tempDataList.get(0)."${field.name}(Zip/Postal Code)"= mapValue?."zip"?mapValue."zip":""
					tempDataList.get(0)."${field.name}(Country)" =mapValue?."country"?mapValue."country":""
					
					//newDatarow.add results
				}else if(field?.type == 'NameTypeField'){
					def mapValue
					def settings = grails.converters.JSON.parse(field.settings)
					if(dataInstance."${field.name}"){
						mapValue = grails.converters.JSON.parse(dataInstance."${field.name}")
					}
				    boolean	pre=(settings?.showPrefix)
					boolean mid=(settings?.showMiddleName)
					if(pre)
						tempDataList.get(0)."${field.name}(Prefix)"= mapValue?."pre"?mapValue."pre":""
					tempDataList.get(0)."${field.name}(First Name)"= mapValue?."fn"?mapValue."fn":""
					if(mid)
						tempDataList.get(0)."${field.name}(Middle Name)"= mapValue?."mn"?mapValue."mn":""
					tempDataList.get(0)."${field.name}(Last Name)"= mapValue?."ln"?mapValue."ln":""
				}else if(field?.type == 'SingleLineNumber'){  
					def settings = grails.converters.JSON.parse(field.settings)
					int decimalPlaces=2
					  try{
						   decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
					  }catch (Exception e) {}
					  def value=(dataInstance."${field.name}")?(dataInstance."${field.name}".encodeAsHTML()):""
					  if(decimalPlaces<2)
					  try{
						  def val=(dataInstance."${field.name}")?new BigDecimal(dataInstance."${field.name}"):""
							value =(new BigDecimal(val, MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
					  }catch(Exception e){}
					  boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false
					  String currency=currencyType?grailsApplication.config?.formBuilder.currencies[settings.currencyType]:''
					  if(currencyType){
					  tempDataList.get(0)."${field.name}(${(currency).decodeHTML()})" = value
					  }else{
					  tempDataList.get(0)."${field.name}" = value
					  }
				  }else if(field.type == 'Paypal'){
					Payment payment = Payment.findByFormIdAndInstanceId("${form.id}","${dataInstance.id}")
					def amount = "0"
					def status = "N/A"
					if(payment){
						def totalPayment = 0
						def currentCurr = grailsApplication.config.formBuilder.currencies[payment.currency.toString()]
						payment?.paymentItems?.each{PaymentItem paymentItem->
							totalPayment+=paymentItem.amount*paymentItem.quantity
						}
						status = payment.status
						amount = currentCurr.decodeHTML() + totalPayment
						}
					tempDataList.get(0)."${field.name}Amount" = amount
					tempDataList.get(0)."${field.name}Status" = status
				}else if(field.type == 'SubForm'){
					def fieldSettings = JSON.parse(field.settings)
					if(fieldSettings.subForm){
						Form subForm = Form.read(fieldSettings.subForm)
						if(subForm){
								def subFieldsList = subForm.fieldsList.findAll{!filedsNotToShow.contains(it.type)}
								def subDataInstanceIdList = sqlDomainClassService.listSubForm(dataInstance.id, form, subForm, field.name)
								subDataInstanceIdList.eachWithIndex{subDataInstance,i->
										if(tempDataList.size()-1<i){
											tempDataList << [:]
										}
										def subAttachements = subForm.getDomainAttachments(subDataInstance.id)
										subFieldsList.each{Field subField->
											if(subField.type == 'FileUpload'){
												def thisSubFieldAttachments = subAttachements.find{it.inputName == subField.name+'_file'}//find is used just to check if it has atleast one attachment
												if(thisSubFieldAttachments){
													tempDataList.get(i)."${subField.name}" = "Y"
												}else{
													tempDataList.get(i)."${subField.name}" = "N"
												}
											}else if(subField?.type == 'AddressField'){
												String values=""
												def mapValue = grails.converters.JSON.parse((subDataInstance."${subField.name}").toString())
												if(mapValue){
													values+=mapValue."line1"?(mapValue."line1"+" ;"):""
													values+=mapValue."line2"?(mapValue."line2"+" ;"):""
													values+=mapValue."city"?(mapValue."city"+" ;"):""
													values+=mapValue."state"?(mapValue."state"+" ;"):""
													values+=mapValue."zip"?(mapValue."zip"+" ;"):""
													values+=mapValue."country"?(mapValue."country"+" ;"):""
												}
												tempDataList.get(i)."${subField.name}"=values
											}else if(subField?.type == 'NameTypeField'){
												String values=""
												def mapValue = grails.converters.JSON.parse((subDataInstance."${subField.name}").toString())
												def subSettings = grails.converters.JSON.parse(subField.settings) 
												boolean pre=subSettings?.showPrefix
												boolean mid=subSettings?.showMiddleName
												if(mapValue){
													if(pre)
														values+=mapValue."pre"?(mapValue."pre"+" "):""
													values+=mapValue."fn"?(mapValue."fn"+" "):""
													if(mid)
														values+=mapValue."mn"?(mapValue."mn"+" "):""
													values+=mapValue."ln"?(mapValue."ln"):""
												}
												tempDataList.get(i)."${subField.name}"=values
											}else if(subField?.type == 'SingleLineNumber'){
												def subSettings = grails.converters.JSON.parse(subField.settings)
												int decimalPlaces=2
												try{
													 decimalPlaces=(subSettings?.decimalPlaces && subSettings?.decimalPlaces?.toInteger()!=2)?subSettings?.decimalPlaces?.toInteger():2
												}catch (Exception e) {}
												def value=subDataInstance."${subField.name}"!=null?subDataInstance."${subField.name}":''
												if(decimalPlaces<2)
												try{
													def val=subDataInstance."${subField.name}"!=null?new BigDecimal(subDataInstance."${subField.name}"):''
													 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
												}catch(Exception e){}
												tempDataList.get(i)."${subField.name}"=value 
											}else{
												tempDataList.get(i)."${subField.name}" = subDataInstance."${subField.name}"
											}
										}
								}
						}else{
							tempDataList.get(0)."${field.name}" = ""
						}
					}else{
						tempDataList.get(0)."${field.name}" = ""
					}
				}else{
					tempDataList.get(0)."${field.name}" = dataInstance."${field.name}"
				}
			}
			extraFields2?.each{field->
				tempDataList.get(0)."${field.name}" = dataInstance."${field.name}"
			}
			
			dataListWithSubFormData.addAll(tempDataList)
			if(subFormHeaderBar){
				dataListWithSubFormData.add([:])
			}
		}
		
		exportService.export(params.format, out, dataListWithSubFormData, fields, labels, [:], [:])
	}
 
	def welcome = {}
}
