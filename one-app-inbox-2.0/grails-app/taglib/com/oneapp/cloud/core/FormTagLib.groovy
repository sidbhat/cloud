package com.oneapp.cloud.core

import org.grails.formbuilder.*;
import org.grails.paypal.Payment;
import org.grails.paypal.PaymentItem;

import grails.converters.JSON

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import com.macrobit.grails.plugins.attachmentable.taglibs.AttachmentsTagLib;
import java.text.*
class FormTagLib {
	static namespace = 'form'
	def springSecurityService
	def domainClassService
	def subFormFieldTypes = ConfigurationHolder.config.subFieldTypes
	def sqlDomainClassService

	//below field is used to show all the entries in role is as follows
	static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS

	/*def reloadUpdatedDomainClasses = {attrs->
	 domainClassService.reloadUpdatedDomainClasses()
	 }*/

	def responseListCount = {attrs->
		def totalCount = 0;
		def formInstance = attrs.formInstance
		def showOwnCreated = true
		if(!SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)){
			showOwnCreated = false
		}
		totalCount = sqlDomainClassService.responseCount(formInstance,showOwnCreated)
		out << '<span class="menuButton"><a class="list" href="'+createLink(controller:FormBuilderConstants.FORM_VIEWER_CONTROLLER,action:'list',params:[formId:formInstance.id])+'" onclick="loadScreenBlock();">List ('+totalCount+')</a></span>'
	}

	def drawSubForm = {attrs->
		String format = 'MM/dd/yyyy'
 		SimpleDateFormat dateformat = new SimpleDateFormat(format)
		def settings = attrs.settings
		def isEditable = attrs.isEditable?:false
		def showFields = attrs.showFields?:false
		def domainInstance = attrs.domainInstance
		def outResult = attrs.outResult?:(new FastStringWriter())
		def id = attrs.id?:""
		def pfid = attrs.pfid?:""
		def style=attrs.style?:""
		Field field = attrs.field
		Form subFormInstance

		if(settings.subForm){
			try{
				subFormInstance = Form.read(settings.subForm)
			}catch(Exception e){}
		}
		if(subFormInstance)
		{
			if(domainInstance)
			{
				outResult << """<div class="tableContainer" ${id?("id=\"${id}\""):""} style="${style}"><table class="datatable" cellpadding="0" cellspacing="0" style="margin:0;margin-left:0px;width:100%;overflow-x:auto;border:none;"><thead><tr><th 
				 			style="text-decoration:none;font-weight:bold;">Edit</th>"""
			def fieldsList = subFormInstance.fieldsList.findAll{(subFormFieldTypes.contains(it.type))}
			for(int j =0; j<fieldsList.size(); j++)
			{
				def fieldTitle
				try{
		 			Field subField = fieldsList.get(j) 
					if(subField?.type == 'SingleLineNumber'){
						def subFieldSettings = JSON.parse(subField.settings)
						boolean currencyType=(subFieldSettings?.currencyType && subFieldSettings?.currencyType!='')?true:false;
						String currency=currencyType?" (${ConfigurationHolder.config?.formBuilder.currencies[subFieldSettings.currencyType].decodeHTML()})":''
					 	fieldTitle = subField.toString()+currency
					}else{
						fieldTitle = subField.toString()
					}
				}catch(Exception e){}
			 	outResult<< """<th style="font-weight:bold;" class="showToolTip" attrt="${fieldTitle}">${fieldTitle}</th>"""
			}
			outResult<< """</tr></thead>"""
			/*  def subFormDomainClass = grailsApplication.getDomainClass(subFormInstance.domainClass.name)
			 if(!subFormDomainClass || subFormInstance.domainClass.updated){
			 domainClassService.reloadUpdatedDomainClasses()
			 subFormDomainClass = grailsApplication.getDomainClass(subFormInstance.domainClass.name)
			 }*/
			def subFormInstanceList = domainInstance."${field.name}"
			def totalMap = [:]
			for(int j=0; j<subFormInstanceList.size();j++){
			 	def subDomainInstance = subFormInstanceList.get(j)
				if(subDomainInstance){
	 				outResult << """<tr>
							  <td><a href="${createLink(controller:'ddc',action:'edit',id:subDomainInstance.id, params:[formId:subFormInstance.id,pfii:domainInstance.id,pfid:pfid,pffn:field.name])}" ><img src="${resource(dir:'images/icons',file:'page_white_edit.png')}" alt="Edit"></a></td>"""

					for(int k=0;k<fieldsList.size();k++){
						def fieldTitle
		 			 	Field subField = fieldsList.get(k)
						try{
							fieldTitle = subField.name
							if( subDomainInstance[fieldTitle] && settings.numericSubField && settings.numericSubField.contains(fieldTitle) ){
								totalMap."${fieldTitle}" = (totalMap."${fieldTitle}"?:0) + (subDomainInstance."${fieldTitle}"?:0)
							}
						}catch(Exception e){}
						if(subField?.type == 'SingleLineNumber'){
							def subFieldSettings = JSON.parse(subField.settings)
		 					int decimalPlaces=2
							try{
								 decimalPlaces=(subFieldSettings?.decimalPlaces && subFieldSettings?.decimalPlaces?.toInteger()!=2)?subFieldSettings?.decimalPlaces?.toInteger():2
							}catch (Exception e) {}
							def value=subDomainInstance?."${fieldTitle}"!=null?subDomainInstance?."${fieldTitle}":''
							if(decimalPlaces<2)
							try{
								def val=subDomainInstance?."${fieldTitle}"!=null?new BigDecimal(subDomainInstance?."${fieldTitle}"):''
								 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
							}catch(Exception e){}
							fieldTitle=value
						}else if(subField?.type == 'AddressField'){
		 					String values=""
							def mapValue = grails.converters.JSON.parse((subDomainInstance?."${fieldTitle}").toString())
							if(mapValue){
								values+=mapValue."line1"?(mapValue."line1"+" ;"):""
								values+=mapValue."line2"?(mapValue."line2"+" ;"):""
								values+=mapValue."city"?(mapValue."city"+" ;"):""
								values+=mapValue."state"?(mapValue."state"+" ;"):""
								values+=mapValue."zip"?(mapValue."zip"+" ;"):""
								values+=mapValue."country"?(mapValue."country"+" ;"):""
							}
						fieldTitle= values
						}else if(subField?.type == 'NameTypeField'){
		 					String values=""
							 def subSettings = grails.converters.JSON.parse(subField.settings)
							 boolean pre=subSettings?.showPrefix
							 boolean mid=subSettings?.showMiddleName
							def mapValue = grails.converters.JSON.parse((subDomainInstance?."${fieldTitle}").toString())
							if(mapValue){
								if(pre)
									values+=mapValue."pre"?(mapValue."pre"+" "):""
								values+=mapValue."fn"?(mapValue."fn"+" "):""
								if(mid)
									values+=mapValue."mn"?(mapValue."mn"+" "):""
								values+=mapValue."ln"?(mapValue."ln"):""
							}
						fieldTitle= values
						}else{
						fieldTitle=subDomainInstance?."${fieldTitle}"
						}
						if(fieldTitle && fieldTitle?.class?.name == "java.sql.Timestamp"){
							fieldTitle =dateformat.format(fieldTitle)
						}
						outResult << """<td>${fieldTitle?:"&nbsp;"}</td>"""
					}
					outResult << """</tr>"""
				}
			}
			if(totalMap.keySet().size()>0){
				outResult << """<tr style="font-weight:bold;">
						   <td>Total</td>"""
				for(int k=0;k<4 && k<fieldsList.size();k++){
					def totalForThisField
					def fieldTitle
					try{
						Field subField = fieldsList.get(k)
						fieldTitle = subField.name
						if(subField?.type == 'SingleLineNumber'){
							def subFieldSettings = JSON.parse(subField.settings)
							 int decimalPlaces=2
							try{
								 decimalPlaces=(subFieldSettings?.decimalPlaces && subFieldSettings?.decimalPlaces?.toInteger()!=2)?subFieldSettings?.decimalPlaces?.toInteger():2
							}catch (Exception e) {}
							def value=totalMap."${fieldTitle}"!=null?totalMap."${fieldTitle}":''
							if(decimalPlaces<2)
							try{
								def val=totalMap."${fieldTitle}"!=null?new BigDecimal(totalMap."${fieldTitle}"):''
								 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
							}catch(Exception e){}
							totalForThisField = value
						}else{
						totalForThisField = totalMap."${fieldTitle}"
						}
					}catch(Exception e){}
					outResult << """<td>${totalForThisField?:"&nbsp;"}</td>"""
				}
				outResult << """</tr>"""
			}
			outResult << """</table></div>"""
			if(isEditable || showFields){
				outResult << """<a href="${createLink(action:"create", params:[formId:subFormInstance.id,pfii:domainInstance.id,pfid:pfid,pffn:field.name])}" style="text-decoration: none; margin-top: 20px; margin-right: 50px; float: right; width: 75px; padding-top:3px; padding-bottom:5px; border-radius:5px; text-align: center; vertical-align: middle; background-color: gray; color: white !important; line-height: inherit; font: inherit;">New</a>"""
			}
		}
	}
		out << outResult
		return outResult.toString()
}

def drawSubFormMobile = {attrs->
	def settings = attrs.settings
	def isEditable = attrs.isEditable?:false;
	def showFields = attrs.showFields?:false
	def domainInstance = attrs.domainInstance
	def id = attrs.id?:""
	def pfid = attrs.pfid?:""
	def style=attrs.style?:""
	Field field = attrs.field
	Form subFormInstance
	def fieldsettings = grails.converters.JSON.parse(field.settings)
	def fieldName = settings."en".label
	if(settings.subForm){
		try{
			subFormInstance = Form.read(settings.subForm)
		}catch(Exception e){}
	}
	if(subFormInstance)
	{
		if(domainInstance)
		{
			out << """<ul data-role="listview" data-split-icon="gear" data-inset="true">
					 			<li data-role="list-divider"> ${fieldName} </li>"""

			def subFormInstanceList = domainInstance?."${field.name}"
			def totalMap = [:]
			for(int j=0; j<subFormInstanceList.size();j++){
				def subDomainInstance = subFormInstanceList.get(j)
				if(subDomainInstance){
					out << """<li>
										<table width="100%">
											<tr>
												<td style="text-decoration:none;font-weight:bold;vertical-align:top;" width="40%" >Id</td>
												<td style="font-weight:normal;vertical-align:top;" width="40%" ><a href="${createLink(controller:'ddc',action:'edit',id:subDomainInstance.id, params:[formId:subFormInstance.id,pfii:domainInstance?.id,pfid:pfid,pffn:field.name])}" >${subDomainInstance.id}</a></td>
									      	</tr>"""
					def fieldsList = subFormInstance.fieldsList.findAll{(subFormFieldTypes.contains(it.type))}
					for(int k=0;k<fieldsList.size();k++){
						def fieldTitle
						def fieldLabel
						Field subField 
						try{
							subField = fieldsList.get(k)
							fieldLabel = subField.toString()?:''
							fieldTitle = subField.name
							if( subDomainInstance[fieldTitle] && settings.numericSubField && settings.numericSubField.contains(fieldTitle) ){
								totalMap."${fieldTitle}" = (totalMap."${fieldTitle}"?:0) + (subDomainInstance."${fieldTitle}"?:0)
							}
						}catch(Exception e){}
						if(subField?.type == 'SingleLineNumber'){
							def subFieldSettings = JSON.parse(subField.settings)
							boolean currencyType=(subFieldSettings?.currencyType && subFieldSettings?.currencyType!='')?true:false;
							String currency=currencyType?"${ConfigurationHolder.config?.formBuilder.currencies[subFieldSettings.currencyType].decodeHTML()}":''
							  int decimalPlaces=2
							 try{
								  decimalPlaces=(subFieldSettings?.decimalPlaces && subFieldSettings?.decimalPlaces?.toInteger()!=2)?subFieldSettings?.decimalPlaces?.toInteger():2
							 }catch (Exception e) {}
 							 def value=subDomainInstance.getAt(fieldTitle)!=null?subDomainInstance.getAt(fieldTitle):''
							 if(decimalPlaces<2)
							 try{
								 def val=subDomainInstance.getAt(fieldTitle)!=null?new BigDecimal(subDomainInstance.getAt(fieldTitle)):''
								  value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
							 }catch(Exception e){}
							 value=value?currency+" "+value:value
							   out << """<tr>
											   <td style="font-weight:bold;">${fieldLabel}</td>
											   <td style="font-weight:normal;">${fieldTitle?value:"&nbsp;"}</td>
										   </tr>"""
							 
						}else if(subField?.type=="NameTypeField"){
							def thisFieldSettings = grails.converters.JSON.parse(subField.settings)
							boolean pre=thisFieldSettings?.showPrefix
							boolean mid=thisFieldSettings?.showMiddleName
							def results=""
							if(fieldTitle && subDomainInstance.getAt(fieldTitle)){
								def mapValue = grails.converters.JSON.parse(subDomainInstance.getAt(fieldTitle))
								if(pre)
									results+=mapValue."pre"?mapValue."pre"+" ":""
								results+=mapValue."fn"?mapValue."fn"+" ":""
								if(mid)
									results+=mapValue."mn"?mapValue."mn"+" ":""
								results+=mapValue."ln"?mapValue."ln"+" ":""
							}
							out << """<tr>
											   <td style="font-weight:bold;">${fieldLabel}</td>
											   <td style="font-weight:normal;">${fieldTitle?results:"&nbsp;"}</td>
										   </tr>"""
						}else if(subField?.type=="AddressField"){
	 						def results=""
							if(fieldTitle && subDomainInstance.getAt(fieldTitle)){
								def mapValue = grails.converters.JSON.parse(subDomainInstance.getAt(fieldTitle))
								results+=mapValue."line1"?mapValue."line1"+" ;":""
								results+=mapValue."line2"?mapValue."line2"+" ;":""
								results+=mapValue."city"?mapValue."city"+" ;":""
								results+=mapValue."state"?mapValue."state"+" ;":""
								results+=mapValue."zip"?mapValue."zip"+" ;":""
								results+=mapValue."country"?mapValue."country"+" ;":""
							}
							out << """<tr>
											   <td style="font-weight:bold;">${fieldLabel}</td>
											   <td style="font-weight:normal;">${fieldTitle?results:"&nbsp;"}</td>
											   </tr>"""
						}else{
						out << """<tr>
												<td style="font-weight:bold;">${fieldLabel}</td>
												<td style="font-weight:normal;">${fieldTitle?(subDomainInstance.getAt(fieldTitle)?:''):"&nbsp;"}</td>
											</tr>"""
						}
					}
					out << """<tr>
										<td style="text-decoration:none;font-weight:normal;vertical-align:top;" width="40%" colspan="2">...</td>
							      	</tr>
								 </table>
						    	</li>"""
				}
			}

			out << """</ul>"""
			if(isEditable || showFields){
				out << """<a href='javascript:saveFormOpenSubFormMobile("${settings.subForm}", "${field.name}")' class="button button-green fr loginButton" style="font-size:18px;text-align:center;${settings.subForm=="1765"?"width:150px;":"width:90px;"};color:#fff !important;">${settings.subForm=="1765"?"Issue Citation":"New"}</a>
                          
 """
			}
		}
	}
}

def leftPanelList = {attrs->
	List<FormAdmin> publishedForms = FormAdmin.createCriteria().list(readOnly:true, max:5){
		and{
			form{
				eq 'tenantId',springSecurityService?.currentUser?.userTenantId
				if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
					eq 'createdBy.id',springSecurityService?.currentUser.id
				}
				domainClass{}
			}
			eq 'published',true
			if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_USER)){
				or{
					eq 'formLogin','Public'
					eq 'formLogin','Password'
					publishedWith{
						eq "id",springSecurityService?.currentUser?.id
					}
				}
			}
			or{
				eq 'formType', 'Approval'
				eq 'formType', 'Survey'
				eq 'formType', 'Poll'
				eq 'formType', 'Master'
			}
		}
	}
	def groupedMap = publishedForms.groupBy { it.formType }
	def showOwnCreated = true
	if(!SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)){
		showOwnCreated = false
	}
	List formsList = []
	groupedMap.each{k,v->
		formsList.addAll(v.form)
	}
	def formWithCountList = sqlDomainClassService.responseCountGroup(formsList,showOwnCreated)
	formWithCountList.each{formWithCount->
		def rowCount = formWithCount.count
		def cn=("${params.controller}"=='formViewer'&& ("${params.formId}"=="${formWithCount.id}" || "${params.pfid}"=="${formWithCount.id}"))?"active":"notactive"
		out << '<li name="formList" class='+cn+'>'
		def rowCountShow=rowCount
		if(rowCount>100){
			rowCountShow="100+"
		}
		out << '<table  style=" border-collapse:collapse;border-spacing:0;"><tr><td><a class="icon-toggle-plus createFormInstance" href="'+createLink(controller:'formViewer',action:'create',params:[formId:formWithCount.id])+'" onclick="loadScreenBlock()"></a></td><td class="showToolTip" attrT="'+formWithCount.name+'"><a href="'+createLink(controller:'formViewer',action:'list',params:[formId:formWithCount.id])+'" style="padding:5px 0 5px 9px;width:135px;" onclick="loadScreenBlock()"><div class="formInstCount"style="padding: 0 2px;">'+rowCountShow+'</div><div class="menuFormName">'+formWithCount.name+'</div></a></td></tr></table>'
		out << '</li>'
	}
	if(publishedForms.totalCount > 5)
		out << """<span id="idformMoreMenu" class="formMoreMenu" onclick="showAllForms('${request.getContextPath()}/activityFeed/leftPanelFormList');" >More Forms</span>"""
}

def mobileFormList = {attrs->
	List<FormAdmin> publishedForms = FormAdmin.createCriteria().list(readOnly:true, max:5){
		and{
			form{
				eq 'tenantId',springSecurityService?.currentUser?.userTenantId
				if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
					eq 'createdBy.id',springSecurityService?.currentUser.id
				}
				domainClass{}
			}
			eq 'published',true
			if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_USER)){
				publishedWith{
					eq "id",springSecurityService?.currentUser?.id
				}
			}
			or{
				eq 'formType', 'Approval'
				eq 'formType', 'Survey'
				eq 'formType', 'Poll'
				eq 'formType', 'Master'
			}
		}
	}
	def groupedMap = publishedForms.groupBy { it.formType }
	def showOwnCreated = true
	if(!SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)){
		showOwnCreated = false
	}
	List formsList = []
	groupedMap.each{k,v->
		formsList.addAll(v.form)
	}
	def formWithCountList = sqlDomainClassService.responseCountGroup(formsList,showOwnCreated)
	formWithCountList.each{formWithCount->
		def rowCount = formWithCount.count
		out << '<li name="formList" >'
		def rowCountShow=rowCount
		if(rowCount>100){
			rowCountShow="100+"
		}
		out << '<a href="'+createLink(controller:'formViewer',action:'mobileList',params:[formId:formWithCount.id])+'" rel="external">'+formWithCount.name+'</a><span class="ui-li-count">'+rowCountShow+'</span><a href="'+createLink(controller:'formViewer',action:'create',params:[formId:formWithCount.id])+'" rel="external"></a>'
		out << '</li>'
	}
	if(publishedForms.totalCount > 5)
		out << """<li data-role="list-divider" class="formMoreMenu" onclick="showAllMobileFormList('${request.getContextPath()}/activityFeed/leftPanelFormList')">More Forms..</li>"""

}
def tabletFormList = {attrs->
	List<FormAdmin> publishedForms = FormAdmin.createCriteria().list(readOnly:true){
		and{
			form{
				eq 'tenantId',springSecurityService?.currentUser?.userTenantId
				if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
					eq 'createdBy.id',springSecurityService?.currentUser.id
				}
				domainClass{}
			}
			eq 'published',true
			if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_USER)){
				or{
					eq 'formLogin','Public'
					eq 'formLogin','Password'
					publishedWith{
						eq "id",springSecurityService?.currentUser?.id
					}
				}
			}
			or{
				eq 'formType', 'Approval'
				eq 'formType', 'Survey'
				eq 'formType', 'Poll'
				eq 'formType', 'Master'
			}
		}
	}
	def groupedMap = publishedForms.groupBy { it.formType }
	def showOwnCreated = true
	if(!SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)){
		showOwnCreated = false
	}
	List formsList = []
	groupedMap.each{k,v->
		formsList.addAll(v.form)
	}
	
	def formWithCountList = sqlDomainClassService.responseCountGroup(formsList,showOwnCreated)
	formWithCountList.each{formWithCount->
		def cn=("${params.controller}"=='formViewer'&& ("${params.formId}"=="${formWithCount.id}" || "${params.pfid}"=="${formWithCount.id}"))?"active":""
		def rowCount = formWithCount.count
		out << """<li id="sidebar_menu_home" class="${formWithCount.id} ${cn}">"""
		def rowCountShow=rowCount
		if(rowCount>100){
			rowCountShow="100+"
		}
		out << '<a href="'+createLink(controller:'formViewer',action:'create',params:[formId:formWithCount.id])+'" rel="external"><span class="abs toggle_arrow"></span>'+formWithCount.name+'</a>'
		//out << '<a href="'+createLink(controller:'formViewer',action:'mobileList',params:[formId:formWithCount.id])+'" rel="external">'+formWithCount.name+'</a><span class="ui-li-count">'+rowCountShow+'</span><a href="'+createLink(controller:'formViewer',action:'create',params:[formId:formWithCount.id])+'" rel="external"></a>'
		out << '</li>'
	}
	//if(publishedForms.totalCount > 5)
		//out << """<li data-role="list-divider" class="formMoreMenu" onclick="showAllMobileFormList('${request.getContextPath()}/activityFeed/leftPanelFormList')">More Forms..</li>"""

}
def formStatusDropDown = { attrs, body ->
	def domainClass = DomainClass.findByName(attrs?.activityFeedInstance?.config?.configName)
	def formObj = Form.findByDomainClass(domainClass)
	def formAdmin = FormAdmin.findByForm(formObj)
	def fieldName = formAdmin.statusField.name
	def fieldInstance = JSON.parse(Field.findByFormAndName(formObj,fieldName).settings)
	def activityId = attrs?.activityFeedInstance?.id
	def fieldValueList = fieldInstance.en.value
	if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_USER)){
		if(formAdmin.showStatusToUser)
		  out << getStatusList(fieldValueList,activityId);
		else
		  out << ""
	}else{
		out << getStatusList(fieldValueList,activityId);
	}
}

def getStatusList(fieldValueList,activityId){
	StringBuilder sb = new StringBuilder();
	def arrowImg = resource(dir:'images',file:'arrow-down.png');
	sb << """
					<ul class="dropdown">
						<li>
							<a href="#"><img src="${arrowImg}" alt="">
								Status
							</a>
							<ul class="sub_menu" style="padding-left:0px;">
			 """
	fieldValueList.each{ sb << """
								<li>
									<a onclick="updateFeedStatus('approval','${it}','${activityId}')" href="javascript:;">${it}</a>
								</li>
							  """ }
	sb << """
							</ul>
						</li>
					</ul>
			"""
	sb.toString()
}

def mobileFormStatusDropDown = { attrs, body ->
	def domainClass = DomainClass.findByName(attrs?.activityFeedInstance?.config?.configName)
	def formObj = Form.findByDomainClass(domainClass)
	def formAdmin = FormAdmin.findByForm(formObj)
	def activityId = attrs?.activityFeedInstance?.id
	if(springSecurityService?.currentUser?.authorities*.authority.contains(Role.ROLE_USER)){
		if(formAdmin.showStatusToUser)
		  out << getMobileStatusLink(activityId);
		else
		  out << ""
	}else{
		out << getStatusList(activityId);
	}
}

def getMobileStatusLink(activityId){
	StringBuilder sb = new StringBuilder();
	def arrowImg = resource(dir:'images',file:'arrow-down.png');
	sb << """
					<a href="${grailsApplication.config.grails.serverURL}/activityFeed/feedStatus/${activityId}" id="statusPopUpLink" class="button-green" style="color:#fff;" data-rel="dialog" data-transition="slidedown" >Status</a></td>
			"""
	sb.toString()
}

def taskStatusDropDown = { attrs, body ->
	def activityId = attrs?.activityFeedInstance?.id
	out << getTaskStatusList(activityId);
}

def getTaskStatusList(activityId){
	StringBuilder sb = new StringBuilder();
	def arrowImg = resource(dir:'images',file:'arrow-down.png');
	sb << """
					<ul class="dropdown">
						<li>
							<a href="#"><img src="${arrowImg}" alt="">
								Task
							</a>
							<ul class="sub_menu" style="padding-left:0px;">
								<li>
									<a onclick="updateFeedStatus('task','open','${activityId}')" href="javascript:;" >Open</a>
								</li>
								<li>
									<a onclick="updateFeedStatus('task','complete','${activityId}')" href="javascript:;" >Complete</a>
								</li>
							</ul>
						</li>
					</ul>
			"""
	sb.toString()
}

def drawForm = {attrs->
	def className = attrs?.className
	className= className.substring(0,className.indexOf("."))
	def activityFeedId= attrs?.activityFeedId
	def shareType= attrs?.shareType
	def shareId= attrs?.shareId
	def fields = new HashMap()
	String format = 'MM/dd/yyyy'
	SimpleDateFormat dateformat = new SimpleDateFormat(format)
	out<<"""<table class="datatable" width='650px;' style="border: none;">"""
	Form form
	def domainInstance
	if(attrs?.notiForm){
		form = attrs?.notiForm
		domainInstance = attrs?.notiDI
	}else{
		form  = Form.findByName(className)
		form = Form.read(form.id)
		domainInstance = sqlDomainClassService.get(shareId, form)
	}
	def formName = JSON.parse(form.settings)."en".name
	form.fieldsList?.each { field ->
		def settings = JSON.parse(field.settings)
		fields.put( field.name,field)
	}
	def excludeFieldType = [
		"PlainText",
		"PlainTextHref",
		"LinkVideo",
		"ImageUpload",
		"FileUpload",
		"PageBreak"
	]
	form.fieldsList.sort { a, b -> return a.sequence.compareTo(b.sequence) }
	def counter=0
	def subFormText = new FastStringWriter()
	form.fieldsList.eachWithIndex { field,i  ->
		def display = true
		if(!excludeFieldType.contains(field.type))
			display = true
		else
			display = false
		if (display ) {
			def isFieldDate = false
			def isFieldCheckBox = false
			def isFieldNumber = false
			def isSubForm = false
			ArrayList fieldValues
			def settings = grails.converters.JSON.parse(field.settings)
			def fieldName = (field.type=='Paypal'?'PayPal Payment':(settings."en".label?:''))
			if("SingleLineDate".equalsIgnoreCase(field.type) || ("FormulaField".equalsIgnoreCase(field.type) && settings.en.newResultType == 'DateResult'))
				isFieldDate = true
			else if("CheckBox".equalsIgnoreCase(field.type))
				isFieldCheckBox = true
			else if("SingleLineNumber".equalsIgnoreCase(field.type) || ("FormulaField".equalsIgnoreCase(field.type) && settings.en.newResultType == 'NumberResult'))
				isFieldNumber = true
			else if("SubForm".equalsIgnoreCase(field.type))
				isSubForm = true
			def fieldValue =  domainInstance.getAt(field.name)
		
			if((counter % 3) == 0) {
				out<<"""<tr"""
				if(counter>3){
					out<<""" style="display:none;" name="formContentTable${activityFeedId}" """
				}
				out<<""" >"""
			}
			out<<""" <td class="showToolTip" style="max-width:85px;width:100px;background: none;border:none;padding:2px;vertical-align:top;"
						   		attrt="${fieldName}"><b>${makeSubString(0,15,fieldName?.encodeAsHTML())}</b></td>"""
			if(isSubForm){
				out<<"""<td  class="showToolTip"
								style="max-width:130px;width:170px;background: none;border:none;padding:2px;vertical-align:top;" attrt="${fieldValue?.id}"name="formContentTd${activityFeedId}">"""
			}else if(isFieldDate){
				out<<"""<td  class="showToolTip"
								style="max-width:130px;width:170px;background: none;border:none;padding:2px;vertical-align:top;" attrt="${fieldValue?(dateformat.format(fieldValue).toString()):''}"name="formContentTd${activityFeedId}">"""
			}else if("Paypal".equalsIgnoreCase(field.type)){
				Payment pmt = Payment.findByFormIdAndInstanceId("${form.id}","${domainInstance.id}")
				def totalPayment = 0
				pmt?.paymentItems?.each{PaymentItem paymentItem->
					totalPayment+=(paymentItem.amount*paymentItem.quantity)
				}
				if(totalPayment>0){
					fieldValue = """Amount: ${ConfigurationHolder.config.formBuilder.currencies[pmt.currency.toString()]+totalPayment}<br>Status: ${pmt.status}"""
				}
				out<<"""<td  class="showToolTip"
								style="max-width:130px;width:170px;background: none;border:none;padding:2px;vertical-align:top;" attrt="${fieldValue?:''}"name="formContentTd${activityFeedId}">"""
			}else{
				out<<"""<td  class="showToolTip"
								style="max-width:130px;width:170px;background: none;border:none;padding:2px;vertical-align:top;" attrt="${fieldValue}"name="formContentTd${activityFeedId}">"""
			}
			if(isFieldCheckBox){
				out<<""" ${makeSubString(0,15,fieldValue?fieldValue.substring(1,fieldValue?(fieldValue.length()-1):0):"")}"""
			}else if(isFieldDate){
				out<<"""${fieldValue?(dateformat.format(fieldValue).toString().encodeAsHTML()):''}"""
			}else if(isSubForm){
	  			out<<""" <a href="javascript:;" onclick='showSubForm${activityFeedId}("${field?.name}")'>Show Sub Form</a>"""
				def outResult = new FastStringWriter()
	 	 		drawSubForm(outResult:outResult,'settings': settings, 'pfid':form.id ,'isEditable':false, 'showFields':false ,'domainInstance':domainInstance ,'field':field , 'id':"subForm${activityFeedId}${field?.name}",'style':"margin-top:2px;display:none;margin-bottom:5px;")
				subFormText<<"""${outResult}"""
			}else if("Paypal".equalsIgnoreCase(field.type)){
				out<<""" ${fieldValue?:''}"""
			}else if("AddressField".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue)
					results+=mapValue."line1"?mapValue."line1"+" ;":""
					results+=mapValue."line2"?mapValue."line2"+" ;":""
					results+=mapValue."city"?mapValue."city"+" ;":""
					results+=mapValue."state"?mapValue."state"+" ;":""
					results+=mapValue."zip"?mapValue."zip"+" ;":""
					results+=mapValue."country"?mapValue."country"+" ;":""
				}
				out<<""" ${makeSubString(0,15,results?(results.toString().encodeAsHTML()):'')}"""
			}else if("SingleLineNumber".equalsIgnoreCase(field.type)){
				boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false;
				String currency=currencyType?ConfigurationHolder.config?.formBuilder.currencies[settings.currencyType].decodeHTML():''
				int decimalPlaces=2
				try{
					 decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
				}catch (Exception e) {}
				def value=fieldValue!=null?fieldValue:''
				if(decimalPlaces<2)
				try{
					def val=fieldValue!=null?new BigDecimal(fieldValue):''
					 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
				}catch(Exception e){}
				def results=value?currency+" "+value:""
				out<<""" ${makeSubString(0,15,results?(results.toString().encodeAsHTML()):'')}"""
			}else if("NameTypeField".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue)
					results+=mapValue."pre"?mapValue."pre"+" ":""
					results+=mapValue."fn"?mapValue."fn"+" ":""
					results+=mapValue."mn"?mapValue."mn"+" ":""
					results+=mapValue."ln"?mapValue."ln"+" ":""
					
				}
				out<<""" ${makeSubString(0,15,results?(results.toString().encodeAsHTML()):'')}"""
			}else{
				out<<""" ${makeSubString(0,15,fieldValue?(fieldValue.toString().encodeAsHTML()):'')}"""
			}
			out<<""" </td>"""
			if((counter % 3)-2 == 0) {
				out<<""" </tr>"""
			}
			counter++;
		}
	}
	if(counter>6){
		out<< """<tr ><td style="background:none;"><a href="javascript:;" id="formContentMore${activityFeedId}" onclick="showHideFormData${activityFeedId}()" style="font-weight:normal;color:#3B5998;font-size:11px;">See More..</a>
                 </td></tr> """
	}
	out<< """</table>"""
	out<< subFormText
	def allAttachments = []
	try{
		allAttachments = form.getDomainAttachments(domainInstance.id)
	}catch(Exception e){}
	allAttachments?.each{attachment->
		if(grailsApplication.config.attachment.image.ext.contains(attachment.ext)){
			out<<""" <div
					style="border: 1px solid #CCCCCC; display: inline-block; margin: 2px; padding: 5px; width: 92px;">"""
			out<<attachments.icon('attachment':attachment,feed:activityFeedId)
		}else{
			out<<"""<br />"""
			out<< attachments.icon('attachment':attachment)
			out<< attachments.downloadLink('attachment':attachment,feed:activityFeedId)
			out<<"""${attachment.niceLength}"""

		}
		out<<attachments.deleteLink('attachment':attachment,'label':'[Delete]','returnPageURI':"${createLink(action: 'index')}")

		if(grailsApplication.config.attachment.image.ext.contains(attachment.ext)){
			out<<"""</div>"""
		}
	}
}

def drawFormEmail = {attrs->
	def shareId= attrs?.shareId
	def fields = new HashMap()
	def display = true
	String format = 'MM/dd/yyyy'
	SimpleDateFormat dateformat = new SimpleDateFormat(format)
	out<<"""<table style="border: none;">"""
	Form form
	def domainInstance
	if(attrs?.notiForm){
		form = (Form)(attrs?.notiForm)
		domainInstance = attrs?.notiDI
	}
	def formAdmin = FormAdmin.findByForm(form)
	def statusFieldId = null
	if('Approval'.equalsIgnoreCase(formAdmin?.formType)){
		statusFieldId = formAdmin.statusField?.id
	}
	def formName = attrs?.formName
	form.fieldsList?.each { field ->
		def settings = JSON.parse(field.settings)
		fields.put( field.name,field)
	}
	def excludeFieldType = [
		"PlainText",
		"PlainTextHref",
		"LinkVideo",
		"ImageUpload",
		"FileUpload",
		"PageBreak"
	]
	form.fieldsList.sort { a, b -> return a.sequence.compareTo(b.sequence) }
	form.fieldsList.eachWithIndex { p,i  ->
		def field
		def settings
		def fieldName
		def isFieldDate = false
		def isFieldCheckBox = false
		def isFieldNumber = false
		def isSubForm = false
		ArrayList fieldValues
		if ( fields.get(p.name) != null ) {
			field = fields.get(p.name)
			settings = grails.converters.JSON.parse(field.settings)
			fieldName = (field.type=='Paypal'?'PayPal Payment':settings."en".label)
			if("SingleLineDate".equalsIgnoreCase(field.type) || ("FormulaField".equalsIgnoreCase(field.type) && settings.en.newResultType == 'DateResult'))
				isFieldDate = true
			else if("CheckBox".equalsIgnoreCase(field.type))
				isFieldCheckBox = true
			else if("SingleLineNumber".equalsIgnoreCase(field.type) || ("FormulaField".equalsIgnoreCase(field.type) && settings.en.newResultType == 'NumberResult'))
				isFieldNumber = true
			else if("SubForm".equalsIgnoreCase(field.type))
				isSubForm = true
		}
		if(!excludeFieldType.contains(p.type) && !settings.hideFromUser || p.id == statusFieldId){
			display = true
		}else{
			display = false
		}
		def fieldValue =  domainInstance.getAt(p.name)
		if (display && !isSubForm) {//subForm entries of this for are not send in mail as whenever subform is created it's entry is sent.
			out<<"""<tr>"""
			out<<"""<td style="padding:5px;border-bottom:1px solid #eee;"><b>${fieldName?.encodeAsHTML()}<span style="float:right;">:</span></b></td><td style="padding:5px;border-bottom:1px solid #eee;">"""
			if(isFieldCheckBox){
				out<<"""${fieldValue?(checkValuesBoxTo(fieldValue))?.encodeAsHTML():""}"""
			}else if(isFieldDate){
				out<<"""${fieldValue?(dateformat.format(fieldValue).toString().encodeAsHTML()):''}"""
				if(settings.timeFormat){
					out << " "+(fieldValue?(new SimpleDateFormat(settings.timeFormat).format(fieldValue)):'')
				}
			}else if("Paypal".equalsIgnoreCase(field.type)){
				Payment pmt = Payment.findByFormIdAndInstanceId("${form.id}","${domainInstance.id}")
				def totalPayment = 0
				pmt?.paymentItems?.each{PaymentItem paymentItem->
					totalPayment+=(paymentItem.amount*paymentItem.quantity)
				}
				if(totalPayment>0){
					fieldValue = """Amount: ${ConfigurationHolder.config.formBuilder.currencies[pmt.currency.toString()]+totalPayment}<br>Status: ${pmt.status}"""
				}
				out<<"""${fieldValue?:''}"""
			}else if("AddressField".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue.toString())
					results+=mapValue."line1"?mapValue."line1"+"; ":""
					results+=mapValue."line2"?mapValue."line2"+"; ":""
					results+=mapValue."city"?mapValue."city"+"; ":""
					results+=mapValue."state"?mapValue."state"+"; ":""
					results+=mapValue."zip"?mapValue."zip"+"":""
					results+=mapValue."country"?mapValue."country"+"; ":""
				}
				out<<""" ${results.toString().encodeAsHTML()}"""
			}else if("SingleLineNumber".equalsIgnoreCase(field.type)){
				boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false;
				String currency=currencyType?ConfigurationHolder.config?.formBuilder.currencies[settings.currencyType].decodeHTML():''
				int decimalPlaces=2
				try{
					 decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
				}catch (Exception e) {}
				def value=fieldValue!=null?fieldValue:''
				if(decimalPlaces<2)
				try{
					def val=fieldValue!=null?new BigDecimal(fieldValue):''
					 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
				}catch(Exception e){}
				def results=value?currency+" "+value:""
				out<<""" ${results.toString().encodeAsHTML()}"""
			}else if("NameTypeField".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue)
					results+=mapValue."pre"?mapValue."pre"+" ":""
					results+=mapValue."fn"?mapValue."fn"+" ":""
					results+=mapValue."mn"?mapValue."mn"+" ":""
					results+=mapValue."ln"?mapValue."ln"+" ":""
					
				}
				out<<""" ${results.toString().encodeAsHTML()}"""
			}else if("Likert".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue)
					mapValue?.each{mapValueObj->
						if(results!=''){
							results+=', '
						} 
						results+=mapValueObj!=null && mapValueObj.toString()!='null'?mapValueObj.toString():''
					}
				}
				out<<""" ${results.toString().encodeAsHTML()}"""
			}else{
				out<<"""${fieldValue?(fieldValue.toString().encodeAsHTML()):''}"""
			}
			out<<"""</td>"""
			out<<"""</tr>"""
		}
	}
	out<< """</table>"""
//	<br><br>
//			You can view the form entry <a href="${grailsApplication.config.grails.serverURL}/ddc/edit/${domainInstance.id}?formId=${form.id}">here</a>."""

	//		form.fieldsList.eachWithIndex { p,i  ->
	//			def attachments =form.getDomainAttachments(domainInstance.id)
	//			attachments=attachments.findAll{it.inputName == (p.name+'_file')}
	//			attachments.each{attachment->
	//				AttachmentsTagLib thisattachments= new AttachmentsTagLib()
	//				if(grailsApplication.config.attachment.image.ext.contains(attachment.ext)){
	//					out<<""" <div
	//						style="border: 1px solid #CCCCCC; display: inline-block; margin: 2px; padding: 5px; width: 92px;">"""
	//					out<<thisattachments.icon('attachment':attachment)
	//					out<<"""<br/>"""
	//				}else{
	//					out<< thisattachments.icon('attachment':attachment)
	//					out<< thisattachments.downloadLink('attachment':attachment)
	//					out<<"""${attachment.niceLength}"""
	//				}
	//
	//				if(grailsApplication.config.attachment.image.ext.contains(attachment.ext)){
	//					out<<"""</div>"""
	//				}
	//			}
	//		}
}
def drawMobileForm = {attrs->
	def className = attrs?.className
	className= className.substring(0,className.indexOf("."))
	def activityFeedId= attrs?.activityFeedId
	def shareType= attrs?.shareType
	def shareId= attrs?.shareId
	def fields = new HashMap()
	def display = true
	String format = 'MM/dd/yyyy'
	SimpleDateFormat dateformat = new SimpleDateFormat(format)
	out<<"""<table class="datatable" width='270px' style="border:none;">"""
	Form form  = Form.findByName(className)
	def domainInstance = sqlDomainClassService.get(shareId, form)
	def formName = JSON.parse(form.settings)."en".name
	form.fieldsList?.each { field ->
		def settings = JSON.parse(field.settings)
		fields.put( field.name,field)
	}
	FormAdmin formAdmin  = FormAdmin.findByForm(form)
	def excludeFieldType = [
		"PlainText",
		"PlainTextHref",
		"LinkVideo",
		"ImageUpload",
		"FileUpload",
		"PageBreak"
	]
	form.fieldsList.sort { a, b -> return a.sequence.compareTo(b.sequence) }
	def counter=0
	form.fieldsList.eachWithIndex { p,i  ->
		if(!excludeFieldType.contains(p.type))
			display = true
		else
			display = false
		def field
		def settings
		def fieldName
		def isFieldDate = false
		def isFieldCheckBox = false
		def isFieldNumber = false
		def isSubForm = false
		ArrayList fieldValues
		if ( fields.get(p.name) != null ) {
			field = fields.get(p.name)
			settings = grails.converters.JSON.parse(field.settings)
			fieldName = settings."en".label
			if("SingleLineDate".equalsIgnoreCase(field.type) || ("FormulaField".equalsIgnoreCase(field.type) && settings.en.newResultType == 'DateResult'))
				isFieldDate = true
			else if("CheckBox".equalsIgnoreCase(field.type))
				isFieldCheckBox = true
			else if("SingleLineNumber".equalsIgnoreCase(field.type) || ("FormulaField".equalsIgnoreCase(field.type) && settings.en.newResultType == 'NumberResult'))
				isFieldNumber = true
			else if("SubForm".equalsIgnoreCase(field.type))
				isSubForm = true
		}
		def fieldValue =  domainInstance.getAt(p.name)
		if (display ) {
			out<<"""<tr"""
			if(counter>2 && (formAdmin?.statusField?.id != field.id)){
				out<<""" style="display:none;" name="formContentTable${activityFeedId}" """
			}
			out<<""" >"""
			out<<""" <td style="max-width:100px;width:70px;background: none;border:none;padding:2px;vertical-align:top;white-space:nowrap; overflow:hidden;text-overflow:ellipsis;font-size:12px;" ><b>${fieldName}
						   				</b></td><td style="max-width:100px;width:50px;background: none;border:none;padding:2px;vertical-align:top;font-weight:normal;white-space:nowrap;overflow:hidden;text-overflow:ellipsis;font-size:12px;" name="formContentTd${activityFeedId}">"""
			if(isFieldCheckBox){
				out<<""" ${fieldValue?fieldValue.substring(1,fieldValue?(fieldValue.length()-1):0):""}"""
			}else if(isFieldDate){
				out<<"""${fieldValue?(dateformat.format(fieldValue).toString().encodeAsHTML()):''}"""
			}else if("AddressField".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue)
					results+=mapValue."line1"?mapValue."line1"+"; ":""
					results+=mapValue."line2"?mapValue."line2"+"; ":""
					results+=mapValue."city"?mapValue."city"+"; ":""
					results+=mapValue."state"?mapValue."state"+"; ":""
					results+=mapValue."zip"?mapValue."zip"+"; ":""
					results+=mapValue."country"?mapValue."country"+"; ":""
				}
				out<<""" ${results}"""
			}else if("NameTypeField".equalsIgnoreCase(field.type)){
				def thisFieldSettings = grails.converters.JSON.parse(field.settings)
				boolean pre=thisFieldSettings?.showPrefix
				boolean mid=thisFieldSettings?.showMiddleName
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue)
					if(pre)
						results+=mapValue."pre"?mapValue."pre"+" ":""
					results+=mapValue."fn"?mapValue."fn"+" ":""
					if(mid)
						results+=mapValue."mn"?mapValue."mn"+" ":""
					results+=mapValue."ln"?mapValue."ln"+" ":""
				}
				out<<""" ${results}"""
			}else if ("SingleLineNumber".equalsIgnoreCase(field.type)){
			boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false;
				String currency=currencyType?ConfigurationHolder.config?.formBuilder.currencies[settings.currencyType].decodeHTML():''
				int decimalPlaces=2
				try{
					 decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
				}catch (Exception e) {}
				def value=fieldValue!=null?fieldValue:''
				if(decimalPlaces<2)
				try{
					def val=fieldValue!=null?new BigDecimal(fieldValue):''
					 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
				}catch(Exception e){}
				def results=value?currency+" "+value:""
				out<<results
				}else if(isSubForm){
				out<<""" <a href="javascript:;" onclick='showSubForm${activityFeedId}("${p?.name}")'>Show Sub Form</a>"""
			}else{
				out<<""" ${fieldValue?(fieldValue.toString().encodeAsHTML()):''}"""
			}
			out<<""" </td> </tr>"""
			counter++;
		}
	}
	if(counter>5){
		out<< """<tr ><td style="background:none;"><a href="javascript:;" id="formContentMore${activityFeedId}" onclick="showHideMore${activityFeedId}()" style="font-weight:normal;color:#3B5998;font-size:11px;">See More..</a>
				 </td></tr> """
	}
	out<< """</table>"""

	form.fieldsList.eachWithIndex { p,i  ->
		if(excludeFieldType.contains(p.type))
			display = false
		def field
		def settings
		def fieldName
		def isSubForm = false
		ArrayList fieldValues
		if ( fields.get(p.name) != null ) {
			field = fields.get(p.name)
			settings = grails.converters.JSON.parse(field.settings)
			fieldName = settings."en".label
			if("SubForm".equalsIgnoreCase(field.type))
				isSubForm = true
		}
		def fieldValue =  domainInstance.getAt(p.name)
		if(isSubForm){
			Form subFormInstance
			def subFormFieldTypes = grailsApplication.config.subFieldTypes
			if(settings.subForm){
				try{
					subFormInstance = Form.read(settings.subForm)
				}catch(Exception e){}
			}
			if(subFormInstance)
			{
				if(domainInstance)
				{
					out<< """<ul data-role="listview" data-split-icon="gear" data-inset="true" style="display:none;" id="subForm${activityFeedId}${p?.name}">
													  	<li data-role="list-divider"> ${fieldName} </li>"""
					def subFormInstanceList = domainInstance."${field.name}"
					def totalMap = [:]
					for(int j=0; j<subFormInstanceList.size();j++){
						def subDomainInstance = subFormInstanceList.get(j)
						if(subDomainInstance){
							out << """<li><table width="100%"><tr><td 
									 		style="text-decoration:none;font-weight:bold;vertical-align:top;" width="40%" >Id</td>
													<td style="font-weight:normal;vertical-align:top;" width="40%" ><a href="${createLink(controller:'ddc',action:'edit',id:"${subFormInstanceList.get(j).id}" ,params:[formId:subFormInstance.id,pfii:domainInstance.id,pfid:form.id,pffn:field.name])}" >${subFormInstanceList.get(j).id}</a></td></tr>"""
						def fieldsList = subFormInstance.fieldsList.findAll{(subFormFieldTypes.contains(it.type))}
						for(int k=0;k<fieldsList.size();k++){
							def fieldTitle
							def fieldTitleDisp
							Field subField 
							if( fieldsList.size() == k ){
								fieldTitle = ""
							}else{
								try{
									 subField = fieldsList.get(k)
									fieldTitleDisp = subField.toString()
									fieldTitle = subField.name
								}catch(Exception e){}
							}
							if(subField?.type == 'SingleLineNumber'){
								def subFieldSettings = JSON.parse(subField.settings)
								boolean currencyType=(subFieldSettings?.currencyType && subFieldSettings?.currencyType!='')?true:false;
								String currency=currencyType?"${ConfigurationHolder.config?.formBuilder.currencies[subFieldSettings.currencyType].decodeHTML()}":''
								  int decimalPlaces=2
								 try{
									  decimalPlaces=(subFieldSettings?.decimalPlaces && subFieldSettings?.decimalPlaces?.toInteger()!=2)?subFieldSettings?.decimalPlaces?.toInteger():2
								 }catch (Exception e) {}
								  def value=subDomainInstance.getAt(fieldTitle)!=null?subDomainInstance.getAt(fieldTitle):''
								 if(decimalPlaces<2)
								 try{
									 def val=subDomainInstance.getAt(fieldTitle)!=null?new BigDecimal(subDomainInstance.getAt(fieldTitle)):''
									  value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
								 }catch(Exception e){}
								 value=value?currency+" "+value:value
								 out << """<tr><td style="font-weight:bold;">${fieldTitleDisp}</td>
									 <td style="font-weight:normal;">${fieldTitle?value:"&nbsp;"}</td>
									  </tr>"""
							}else if(subField?.type=="NameTypeField"){
								def thisFieldSettings = grails.converters.JSON.parse(subField.settings)
								boolean pre=thisFieldSettings?.showPrefix
								boolean mid=thisFieldSettings?.showMiddleName
								def results=""
								if(fieldTitle && subDomainInstance.getAt(fieldTitle)){
									def mapValue = grails.converters.JSON.parse(subDomainInstance.getAt(fieldTitle))
									if(pre)
										results+=mapValue."pre"?mapValue."pre"+" ":""
									results+=mapValue."fn"?mapValue."fn"+" ":""
									if(mid)
										results+=mapValue."mn"?mapValue."mn"+" ":""
									results+=mapValue."ln"?mapValue."ln"+" ":""
								}
								out << """<tr>
											   <td style="font-weight:bold;">${fieldTitleDisp}</td>
											   <td style="font-weight:normal;">${fieldTitle?results:"&nbsp;"}</td>
										   </tr>"""
						}else if(subField?.type=="AddressField"){
	 						def results=""
							if(fieldTitle && subDomainInstance.getAt(fieldTitle)){
								def mapValue = grails.converters.JSON.parse(subDomainInstance.getAt(fieldTitle))
								results+=mapValue."line1"?mapValue."line1"+" ;":""
								results+=mapValue."line2"?mapValue."line2"+" ;":""
								results+=mapValue."city"?mapValue."city"+" ;":""
								results+=mapValue."state"?mapValue."state"+" ;":""
								results+=mapValue."zip"?mapValue."zip"+" ;":""
								results+=mapValue."country"?mapValue."country"+" ;":""
							}
							out << """<tr>
											   <td style="font-weight:bold;">${fieldTitleDisp}</td>
											   <td style="font-weight:normal;">${fieldTitle?results:"&nbsp;"}</td>
											   </tr>"""
						}else{
							out << """<tr><td style="font-weight:bold;">${fieldTitleDisp}</td>
								<td style="font-weight:normal;">${fieldTitle?(subDomainInstance.getAt(fieldTitle)?:''):"&nbsp;"}</td>
								 </tr>"""
							}
						}
						out << """<tr><td style="text-decoration:none;font-weight:normal;vertical-align:top;" width="40%" colspan="2">...</td>
												</tr></table></li>"""
					}
				}
				out << """</ul>"""
			}
		}
	}
}
}
def drawPaypalField={attrs->
	def settings=attrs?.settings
	def domainInstance=attrs?.domainInstance
	def form=Form.get(attrs?.formId)
	def field=attrs?.field
	def itemForm
	def trs = ""
	def totalAndPaymentStatus = ""
	def payNowButton = ""
	try{
		if(settings.itemForm){
			itemForm = Form.get(settings.itemForm)
		}
	}catch(Exception e){
		log.error("PayPal Configuration error: Item Form not found with id: "+settings.itemForm)
		trs = "<tr><td colspan='5'>PayPal Configuration error: Items configured not found</td></tr>"
	}
	Payment p
	if(domainInstance?.id){
		p = Payment.findByFormIdAndInstanceId("${form.id}","${domainInstance.id}")
	}
	if(p?.status == Payment.COMPLETE){
		def totalPayment = 0
		def currentCurr = ""
		if(p)
			currentCurr = ConfigurationHolder.config.formBuilder.currencies[p.currency.toString()]
		p?.paymentItems?.each{PaymentItem paymentItem->
			def itemDesc = ""
			if(itemForm){
				try{
					if("${itemForm.id}" != paymentItem.itemNumber.split('_')[1]){
						log.error "Error : Item form of Payment Control in ["+form.toString()+"] form is changed."
						itemForm = paymentItem.itemNumber.split('_')[1]?(Form.read(paymentItem.itemNumber.split('_')[1])):form
					}
					def item = sqlDomainClassService.get(paymentItem.itemNumber.split('_')[0],itemForm)
					itemDesc = item[settings.idescf]
				}catch(Exception e){
					log.error "Exception in Form TagLib-drawPaypalField if itemForm exists:"+e
				}
			
			trs += """<tr class="itm"><td><a
									href="${ConfigurationHolder.config.grails.serverURL}/preview/item/${paymentItem.itemNumber.split('_')[0]+'_'+form.id}" class="no-text help" rel="#overlay"><img
									src="${ConfigurationHolder.config.grails.serverURL}/preview/firstImg/${paymentItem.itemNumber.split('_')[0]+'_'+form.id}" style="width:80px;"
									></a></td><td><b>${paymentItem.itemName}</b><br>${itemDesc?:''}</td><td
									class="num">${currentCurr} <div class="amt">${paymentItem.amount}</div></td><td 
									class="num">${paymentItem.quantity}</td
									><td class="num">${currentCurr} <div class="t">${paymentItem.amount*paymentItem.quantity}</div></td></tr>"""
			}
			totalPayment+=paymentItem.amount*paymentItem.quantity
		}
		if(totalPayment>0){
			totalAndPaymentStatus = """<b>Total amount: ${currentCurr+totalPayment}</b><br><b>Payment status: ${p.status}</b>"""
		}
	}else{
		def paymentStatus = ""
		if(p){
			paymentStatus = """<br><b>Payment status: ${p.status}</b>"""
		}
		if(itemForm){
			def allInstances = sqlDomainClassService.list(itemForm)?.instanceList
			if(p){
				domainInstance."${field.name}_bought" = []
				p?.paymentItems?.each{PaymentItem paymentItem->
					def item = allInstances.find{"${it.id}" == paymentItem.itemNumber.split('_')[0]}
					if(item){
						item.qty = paymentItem.quantity
						domainInstance."${field.name}_bought" << item
					}
				}
				paymentStatus = """<br><b>Payment status: ${p.status}</b>"""
			}
			def itemsBought = domainInstance."${field.name}_bought"?:[]
			def currentCurr = ConfigurationHolder.config.formBuilder.currencies[settings.curr]
			allInstances?.eachWithIndex{item,iidx->
				if(settings.iqf){
					def options = ""
					def itemBought = itemsBought?.find{it.id == item.id}
					for(int qidx=1;qidx<=item[settings.iqf];qidx++){
						options += """<option value="${qidx}" ${itemBought?.qty == qidx?'selected="selected"':''}>${qidx}</option>"""
					}
					trs += """<tr class="itm"
						><td
							><a href="${ConfigurationHolder.config.grails.serverURL}/preview/item/${item.id+'_'+form.id}" class="no-text help" rel="#overlay"
								><img src="${ConfigurationHolder.config.grails.serverURL}/preview/firstImg/${item.id+'_'+form.id}" style="width:80px;"
							></a
						></td
						><td><b>${settings.inf?item[settings.inf]:''}</b></br>${settings.idescf?item[settings.idescf]:''}</td
						><td class="num">${currentCurr} <div class="amt">${settings.iaf?item[settings.iaf]:''}</div></td
						><td>${options?"<input type='hidden' class='itemNumber' name='${field.name}.paymentItems[${iidx}].itemNumber' value='${item.id}'><select style='width:50px' name='${field.name}.paymentItems[${iidx}].quantity' class='q'><option value='0'>0</option>${options}</select>":'Sold Out'}</td
						><td class="num">${currentCurr} <div class="t">0.00</div></td
					></tr>"""
				}
			}
			if(!trs){
				trs += """<tr>
					<td colspan="5">No items available to buy</td>
				</tr>"""
			}
		}
		totalAndPaymentStatus = """<b>Total Payable Amount: ${ConfigurationHolder.config.formBuilder.currencies[settings.curr]}<span class='gt'>0</span> </b>${paymentStatus}"""
		payNowButton = """<div class="pay_now_button" onclick="submitForm(this);" style="cursor: pointer;"></div>"""
	}
	out<< """<div>"""
	if(itemForm){
		out<< """<table class="itmTable"><tbody><tr class="itmHeader"><td>Preview</td><td>Product Name &amp; Description</td><td>Price</td><td>Quantity</td><td>Amount</td></tr>${trs}</tbody></table>"""
		}
	out<< """</div>${totalAndPaymentStatus}<div style="border:0;padding:5px 0 0 0;margin:0;background:none;">${payNowButton}<br><script id="#${field.name}scr">\$(document).ready(function(){\$("input[name='${settings.iaf}']").change(function(){updatePPA(this);}).trigger("change")});</script>"""
}
def drawPaypalMobField={attrs->
	def settings=attrs?.settings
	def domainInstance=attrs?.domainInstance
	def form=Form.get(attrs?.formId)
	def field=attrs?.field
	def itemForm
	def lis = ""
	def totalAndPaymentStatus = ""
	def payNowButton = ""
	try{
		if(settings.itemForm){
			itemForm = Form.get(settings.itemForm)
		}
	}catch(Exception e){
		log.error("PayPal Configuration error: Item Form not found with id: "+settings.itemForm)
		lis = "<li><p class='ui-li-desc'>PayPal Configuration error: Items configured not found</p></li>"
	}
	Payment p
	if(domainInstance?.id){
		p = Payment.findByFormIdAndInstanceId("${form.id}","${domainInstance.id}")
	}
	if(p?.status == Payment.COMPLETE){
		def totalPayment = 0
		def currentCurr = ""
		if(p)
			currentCurr = ConfigurationHolder.config.formBuilder.currencies[p.currency.toString()]
		p?.paymentItems?.each{PaymentItem paymentItem->
			def itemDesc = ""
			if(itemForm){
				try{
					if("${itemForm.id}" != paymentItem.itemNumber.split('_')[1]){
						log.error "Error : Item form of Payment Control in ["+form.toString()+"] form is changed."
						itemForm = paymentItem.itemNumber.split('_')[1]?(Form.read(paymentItem.itemNumber.split('_')[1])):form
					}
					def item = sqlDomainClassService.get(paymentItem.itemNumber.split('_')[0],itemForm)
					itemDesc = item[settings.idescf]
				}catch(Exception e){
					log.error "Exception in Form TagLib-drawPaypalMobField if itemForm exists:"+e
				}
			
			lis += """<li><imgsrc="${ConfigurationHolder.config.grails.serverURL}/preview/firstImg/${paymentItem.itemNumber.split('_')[0]+'_'+form.id}" style="width:80px;">
							<h3 class="ui-li-heading">${paymentItem.itemName}</h3>
							<p class="ui-li-desc">Description: ${itemDesc?:''}</p><p
									class="ui-li-desc" >Price: ${currentCurr} <span class="amt">${paymentItem.amount}</span></p><p 
									class="ui-li-desc">Quantity: ${paymentItem.quantity}</p
									><p class="ui-li-desc"><strong>Amount: ${currentCurr} <span class="t">${paymentItem.amount*paymentItem.quantity}</span></strong></p></li>"""
			}
			totalPayment+=paymentItem.amount*paymentItem.quantity
		}
		if(totalPayment>0){
			totalAndPaymentStatus = """<b>Total amount: ${currentCurr+totalPayment}</b><br><b>Payment status: ${p.status}</b>"""
		}
	}else{
		def paymentStatus = ""
		if(p){
			paymentStatus = """<br><b>Payment status: ${p.status}</b>"""
		}
		if(itemForm){
			def allInstances = sqlDomainClassService.list(itemForm)?.instanceList
			if(p){
				domainInstance."${field.name}_bought" = []
				p?.paymentItems?.each{PaymentItem paymentItem->
					def item = allInstances.find{"${it.id}" == paymentItem.itemNumber.split('_')[0]}
					if(item){
						item.qty = paymentItem.quantity
						domainInstance."${field.name}_bought" << item
					}
				}
				paymentStatus = """<br><b>Payment status: ${p.status}</b>"""
			}
			def itemsBought = domainInstance."${field.name}_bought"?:[]
			def currentCurr = ConfigurationHolder.config.formBuilder.currencies[settings.curr]
			allInstances?.eachWithIndex{item,iidx->
				if(settings.iqf){
					def options = ""
					def itemBought = itemsBought?.find{it.id == item.id}
					for(int qidx=1;qidx<=item[settings.iqf];qidx++){
						options += """<option value="${qidx}" ${itemBought?.qty == qidx?'selected="selected"':''}>${qidx}</option>"""
					}
					lis += """<li class="itm" style="padding-right:15px"><img src="${ConfigurationHolder.config.grails.serverURL}/preview/firstImg/${item.id+'_'+form.id}" style="width:80px;"
							>
						<h3 class="ui-li-heading" >${settings.inf?item[settings.inf]:''}</h3>
							<p  class="ui-li-desc">Description:	${settings.idescf?item[settings.idescf]:''}</p
						><p  class="ui-li-desc" >Price: ${currentCurr} <span class="amt" style="display:inline;">${settings.iaf?item[settings.iaf]:''}</span></p
						><p  class="ui-li-desc">Quantity: ${options?"<input type='hidden' class='itemNumber' name='${field.name}.paymentItems[${iidx}].itemNumber' value='${item.id}'><select name='${field.name}.paymentItems[${iidx}].quantity' class='q'><option value='0'>0</option>${options}</select>":'Sold Out'}</p
						><p class="ui-li-desc"><strong>Amount: ${currentCurr} <span class="t">0.00</span></strong></p
					></li>"""
				}
			}
			if(!lis){
				lis += """<li>
					<p  class="ui-li-desc">No items available to buy</p>
				</li>"""
			}
		}
		totalAndPaymentStatus = """<b>Total Payable Amount: ${ConfigurationHolder.config.formBuilder.currencies[settings.curr]}<span class='gt'>0</span> </b>${paymentStatus}"""
		payNowButton = """<div class="pay_now_button" onclick="submitForm(this);" style="cursor: pointer;"></div>"""
	}
	out<< """<div>"""
	if(itemForm){
	out<< """<ul 
		data-role="listview" data-inset="true">
			${lis}</ul>"""
			}
	out<< """${totalAndPaymentStatus}<div style="border:0;padding:5px 0 0 0;margin:0;background:none;">${payNowButton}<br></div><script id="#${field.name}scr">\$(document).ready(function(){\$("input[name='${settings.iaf}']").change(function(){updatePPA(this);}).trigger("change")});</script>"""
}
	def addScript = {attrs->
		def addPaypalBlockScript = false
		try{
			def currentUser = User.get(springSecurityService.currentUser.id)
			def currentUserAuthorities = currentUser.authorities*.authority
			if(!currentUserAuthorities?.contains(Role.ROLE_SUPER_ADMIN)){
				if(currentUserAuthorities?.contains(Role.ROLE_TRIAL_USER)){
					addPaypalBlockScript = true
				}else{
					def client = Client.get(currentUser.userTenantId)
					if(!client?.plan){
						addPaypalBlockScript = true
					}
				}
			}
		}catch(Exception e){
			addPaypalBlockScript = true
		}
		out << """<script id="ppbScript">ppb = ${addPaypalBlockScript};ppbMsg='${message(code:'paypal.block.nonPaidUser',args:[g.link(controller:'dropDown',action:'clientUsage'){message(code:'paypal.block.nonPaidUser.linkText','default':'here')}],'default':'Sorry! PayPal control is for paid users only.').encodeAsJavaScript()}';
					ppOne = '${message(code:'paypal.only.one.control','default':'Form can contain only one PayPal control').encodeAsJavaScript()}'
				</script>"""
	}
private String makeSubString(int init=0,int last=0, String str){
	if(str && str.length()>last){
		str=str.substring(init,last)+".."
		}
	return str
	}
private  String checkValuesBoxTo (def submitList){
	def userSubmitList
	StringBuffer result = new StringBuffer("");
   try{
	userSubmitList=submitList?JSON.parse(submitList):"";
   }catch (Exception e) {
   }
   if(userSubmitList)
	 for (int iSub=0;iSub< userSubmitList.size();iSub++) 
		   result.append((iSub==0?'':', ')+userSubmitList[iSub]) ;
   return result?.toString();
}
}
