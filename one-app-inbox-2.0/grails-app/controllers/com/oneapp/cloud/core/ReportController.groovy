

package com.oneapp.cloud.core

import java.text.SimpleDateFormat;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.grails.formbuilder.DomainClass
import org.grails.formbuilder.Field;
import org.grails.formbuilder.Form;
import org.grails.formbuilder.FormAdmin;

import grails.converters.JSON

import com.oneapp.cloud.core.log.*

class ReportController {
	
	def springSecurityService
	def domainClassService
	def inboxReaderService
	def sqlDomainClassService
	
	static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS
	
    def index = {
		def userRoles = springSecurityService.currentUser.authorities*.authority
		if(SpringSecurityUtils.ifAnyGranted(Role.ROLE_HR_MANAGER)){
			redirect(action: "formReport", params: params)
		}else{
		 	redirect(action: "emailReport", params: params) 
		}
    }
	
	def colorArray = ['#4572A7','#828AE2','#AA4643','#E7876B','#EAD874','#82B2E2','#89A54E','#E68AE2','','','','','','','','','','','','','','','','','','','','']
    
    def data(cat,fromDate, toDate) {
    		def v = new ArrayList()
			cat.each {
				it ->
				def name = it
				
				v << ActivityFeed.findAllByCreatedByAndDateCreatedBetween(User.findByUsername(name),fromDate, toDate)?.size()
			}
			return v 
		
	}
	
	def emailData(list) {
		def maxCount = 20
		def v = list.sort{ a, b -> b.value.emailCount <=> a.value.emailCount }.collect {it ->
		if ( maxCount-- > 0 ){
			Sender o = (Sender)it.value
			return o.emailCount
		}
		}
		v.removeAll([null])
		return v as JSON
	
}
	
	def getCategories(list) {
			def index
			def cat = new ArrayList()
			list.each {it ->
					if ( !cat?.contains( it.createdBy?.username ) )
						cat << it.createdBy?.username 
			}
			return cat 
	
	}
	
	
	def getEmailCategories(list) {
		def index
		def maxCount = 20
		def v = list.sort{ a, b -> b.value.emailCount <=> a.value.emailCount }.collect {it ->
		
			if ( maxCount-- > 0 ){
			Sender o = (Sender)it.value
			if ( (index = o.email?.indexOf("<")) != -1 ){
			 if ( index > 25 ) // first 25 characters in the name
				 index = 25
			 o.email?.substring(0,index)
			}
			else
			   o.email
			}
		}
		v.removeAll([null])
		return v as JSON

}
	
	def sender = {
		
		try{
			def fromDate = params.fromDate
			def toDate = params.toDate
			
			SimpleDateFormat sdf = new SimpleDateFormat(grailsApplication.config.format.date)
			
			// Default search is for a week
			if ( (fromDate == null || fromDate?.length() == 0 ) && (toDate == null || toDate?.length() == 0) ){
				fromDate = new Date() - 7
				toDate = new Date()
			}else{
				fromDate = sdf.parse(params.fromDate)
				toDate = sdf.parse(params.toDate)
				toDate.setHours(23)
				toDate.setMinutes(59)
			}
			
			//@todo Do all date validations here.
			//if ( fromDate > toDate ){
			//	flash.message = "From Date can't be greater than To Date. Please modify your search criteria."
			//	redirect(view: "/reports/reportsLine")
			//}
			
			
			if ( fromDate && toDate ) {
				def list  
				/*  Check user role. If user is SUPER_ADMIN he can see all feeds across all clients.
				 *  If he is CLIENT_ADMIN he can see all feeds across his client only.
				 */
				list = ActivityFeed.findAllByDateCreatedBetween(fromDate,toDate)
			
				def cat = getCategories(list)
				if ( list == null ){
				  flash.message = "No results returned. Please modify your search criteria."
				  redirect(view: "/reports/reportsLine")
				 }else 
					render(view: "/reports/reportsLine", model: [fromDate:sdf.format(fromDate),toDate:sdf.format(toDate),fullData:list,categories: "['Emails']", data: data(cat,fromDate,toDate) as JSON, dataLabel: cat as JSON, title: "Feeds By Sender", viewName: "Feeds By Sender"])
			}else 
				render(view: "/reports/reportsLine")
		}catch(Exception e){
			log.error e
			flash.message = "Some Problem occured"
			redirect(action: "sender")
		}
	
	}
	
	def logReport = {
		
		try{
			def fromDate = params.fromDate
			def toDate = params.toDate
			
			SimpleDateFormat sdf = new SimpleDateFormat(grailsApplication.config.format.date)
			
			// Default search is for a week
			if ( (fromDate == null || fromDate?.length() == 0 ) && (toDate == null || toDate?.length() == 0) ){
				fromDate = new Date() - 7
				toDate = new Date()
			}else{
				fromDate = sdf.parse(params.fromDate)
				toDate = sdf.parse(params.toDate)
				toDate.setHours(23)
				toDate.setMinutes(59)
			}
			
			//@todo Do all date validations here.
			//if ( fromDate > toDate ){
			//	flash.message = "From Date can't be greater than To Date. Please modify your search criteria."
			//	redirect(view: "/reports/reportsLine")
			//}
			
			def tenantList = params.list("tenantId");
			
			if ( fromDate && toDate && params.errh==null) {
				def list  
				/*  Check user role. If user is SUPER_ADMIN he can see all feeds across all clients.
				 *  If he is CLIENT_ADMIN he can see all feeds across his client only.
				 */
				SimpleDateFormat sdf1 = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss")
				
				if(tenantList){
					if(tenantList.size()>20)
						tenantList = tenantList.subList(0, 19)
					if(SpringSecurityUtils.ifAnyGranted(Role.ROLE_SUPER_ADMIN))
						list = Log.executeQuery("from Log where tenantId in ("+tenantList.join(",")+") and logTime between '"+sdf1.format(fromDate)+"' and '"+sdf1.format(toDate)+"' order by logTime desc")
					else{
						render(view: "/reports/logReport")
						return
					}
				}else if(SpringSecurityUtils.ifAnyGranted(Role.ROLE_SUPER_ADMIN) || SpringSecurityUtils.ifAnyGranted(Role.ROLE_HR_MANAGER)){
					tenantList = new ArrayList()
					tenantList << (""+springSecurityService.currentUser.userTenantId)
					list = Log.findAllByLogTimeBetween(fromDate,toDate)
				}else{
					render(view: "/reports/logReport")
					return
				}
			
				if ( list == null ){
				  flash.message = "No results returned. Please modify your search criteria."
				  redirect(view: "/reports/logReport")
				 }else {
				 	def clientNameList = Client.executeQuery("select c.id,c.name from Client c where c.id in ("+tenantList.join(",")+")")
					def dataLabel = new ArrayList()
					def groupedLogs = list.groupBy{it.tenantId}
					def errorLevelGroup = list.groupBy{it.errorLevel}
					def errorLevelList = errorLevelGroup.keySet()
					def yAxisDataList = new ArrayList()
					def pieData = new ArrayList()
					groupedLogs.eachWithIndex {k,v,i->
						errorLevelList.eachWithIndex{ errorLevel,j->
							if(i==0){
								yAxisDataList << [name:errorLevel,color:colorArray[j],data:new ArrayList()]
							}
							yAxisDataList.get(j).data << v.findAll{it.errorLevel == errorLevel}.size()
						}
						def clientIdNameMap = clientNameList.find{it[0] == k}
						dataLabel << clientIdNameMap[1]
					}
					
					if(errorLevelList.size() == 1){
						dataLabel.eachWithIndex{label,i->
							def nthData = new ArrayList()
							nthData << label
							nthData << yAxisDataList.get(0).data.get(i)
							pieData << nthData
						}
					}
					
					def yAxisLabels = new ArrayList()
					errorLevelList.eachWithIndex{yAxisFieldName,i->
						yAxisLabels << [labels:[style:[color:colorArray[i]]],title:[text:yAxisFieldName,style:[color:colorArray[i]]]]
					}
					//[pieData: pieData, formList:formList,xAndYAxis: xAndYAxis,yAxisLabel: yAxisLabels as JSON, fromDate:sdf.format(fromDate),toDate:sdf.format(toDate),fullData:list,categories: "['Emails']", data: yDataArrList as JSON, dataLabel: dataLabel as JSON, title: "Report on forms", viewName: "Report on forms"]
					render(view: "/reports/logReport",model:[tenantList: tenantList, clientNameList: clientNameList, pieData: pieData, yAxisLabel: yAxisLabels as JSON, fromDate:sdf.format(fromDate),toDate:sdf.format(toDate),fullData:list,categories: "['Emails']", data: yAxisDataList as JSON, dataLabel: dataLabel as JSON, title: "Report on logs", viewName: "Report on logs"])
				 }
			}else 
				render(view: "/reports/logReport")
		}catch(Exception e){
			log.error e
			flash.message = "Some Problem occured"
			redirect(action: "logReports")
		}
	
	}
	
	def logReports = {
		render(view: "/reports/logReport")
	}
	
	// Reports by feed config
	def config = {
	
	
	
	}
	
	// Reports for forms
	def formReport = {
		
		log.info "Entering formReport"
		def formInstance
		if(params.formId){
			formInstance = Form.get(params.formId)
			
		}else{
		if(params.formInstanceId){
				params.formId=params.formInstanceId
				formInstance = Form.get(params.formId)
			}
		}
		
		def userXAndYAxis = [[name:'ipAddress',label:'IP Address'],[name:'accessType',label:'Access Mode'],[name:'action',label:'Action'],[name:'location',label:'Access Location']]
		//def formList = Form.findAllByTenantIdAndFormCat(springSecurityService.currentUser.userTenantId,'N')
		def formList=Form.createCriteria().list(){
			eq 'tenantId',springSecurityService.currentUser.userTenantId
			not{
				eq 'formCat','S'
			}
		}
		if(!formInstance && formList.size() > 0){
			formInstance = formList[0]
			params.formId = formInstance.id
		}
		if(formInstance&& !(params.xAxis && params.yAxis)){
			if(formInstance.formCat=='S'){
				flash.message = "Reports on sub-forms are not allowed"
				flash.defaultMessage=flash.message 
				redirect(controller:"dashboard", action: "index")
				return
			}
			def xAndYAxis = getXandYAxisData(formInstance)
			if(xAndYAxis.xAxis && (params.xAxis == null || params.xAxis == "")){
				params.xAxis = xAndYAxis.xAxis[0].name
				params.yAxis = xAndYAxis.xAxis[0].name
				params.sort = xAndYAxis.xAxis[0].name
				params.order = 'asc'
			}else{
				flash.message = message(code:'formReport.no.xAxis','default':"Sorry! Report can only be generated on forms having input fields")
				flash.defaultMessage=flash.message
				redirect(controller:"form", action: "list")
				return
			}
		}
/*		if(!formInstance && formList.size() > 0){
			formInstance = formList[0]
			def xAndYAxis = getXandYAxisData(formInstance)
			if(params.xAxis == null || params.xAxis == ""){
				params.xAxis = xAndYAxis.xAxis[0].name
				params.yAxis = xAndYAxis.xAxis[0].name
				params.formId = formInstance.id
				params.sort = xAndYAxis.xAxis[0].name
				params.order = 'asc'
			}	
		}
*/		try{
			def fromDate = params.fromDate
			def toDate = params.toDate
			
			SimpleDateFormat sdf = new SimpleDateFormat(grailsApplication.config.format.date)
			
			// Default search is for a week
			if ( (fromDate == null || fromDate?.length() == 0 ) && (toDate == null || toDate?.length() == 0) ){
				fromDate = new Date() - 7
				toDate = new Date()
			}else{
				fromDate = sdf.parse(params.fromDate)
				toDate = sdf.parse(params.toDate)
				toDate.setHours(23)
				toDate.setMinutes(59)
			}
			
			if(fromDate > toDate){
				def temp = fromDate
				fromDate = toDate
				toDate = temp
			}
			
			if ( fromDate && toDate && formInstance && params.xAxis && params.yAxis) {
				def splitterIndex = params.xAxis.indexOf("@_@")
				def xAxisFieldName = splitterIndex==-1?params.xAxis:(params.xAxis.substring(0,splitterIndex))
				def xAxisField = formInstance.fieldsList.find{it.name == xAxisFieldName}
	 			def xAxisFieldType = xAxisField?.type
				def likertFieldSettings = xAxisFieldType=='Likert'?JSON.parse(xAxisField.settings):null
				def likertRowIdx = -1
				try{
					
					likertRowIdx = xAxisFieldType=='Likert'?0:-1
					likertRowIdx = splitterIndex==-1?likertRowIdx:(params.xAxis.substring(splitterIndex+3,params.xAxis.length()).toInteger())
					params.xAxis = xAxisFieldType=='Likert'?(xAxisFieldName+'@_@'+likertRowIdx):params.xAxis
				}catch(Exception e){
					log.error "Likert row index number format in formReport action"
					throw e
				}
				
				if(!params.sort){
					params.sort = 'date_created'
					params.order = 'asc'
				}
				
				def list
				/*  Check user role. If user is SUPER_ADMIN he can see all feeds across all clients.
				 *  If he is CLIENT_ADMIN he can see all feeds across his client only.
				 */
				def listTotalMap = sqlDomainClassService.list(formInstance,SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS),[sort:params.sort,order:params.order])
				list = listTotalMap.instanceList
				def accessList = UserAppAccessDetail.createCriteria().list(){
					between('accessTime',fromDate,toDate)
					eq('accessedClass',formInstance.domainClass.name)
				}
				def totalViews = accessList.findAll{it.action == 'View'}.size()
				def totalCreates = accessList.findAll{it.action == 'Create'}.size()
				def accessArrList = [[name:"Access",color:colorArray[0],data:[]]]
				def yAxisAccessLabels = [[labels:[style:[color:colorArray[0]]],title:[text:"Count",style:[color:colorArray[0]]]]]
				def accessDataLabel = []
				accessList = accessList.groupBy{it.location}
				accessList.each{k,v->
					accessDataLabel << k
					accessArrList.get(0).data << v.size()
				}
				def dateIdx
				def yDataArrListViews = [[name:"Views",color:colorArray[1],data:[]],[name:"Creates",color:colorArray[6],data:[]]]
				def dataLabelViews = new ArrayList()
				def yAxisLabelsViews = [[labels:[style:[color:colorArray[1]]],title:[text:"Views",style:[color:colorArray[1]]]],[labels:[style:[color:colorArray[6]]],title:[text:"Creates",style:[color:colorArray[6]]]]]
				def viewsList = UserAppAccessDetail.createCriteria().list(){
					between('accessTime',fromDate,toDate)
					eq('accessedClass',formInstance.domainClass.name)
				}
				def dateDiff = toDate - fromDate
				if(dateDiff <= 31){
					viewsList = viewsList.collect{
						[date:sdf.format(it.accessTime),action:it.action]
					}
					for(dateIdx=fromDate;dateIdx<=toDate;dateIdx++){
						def dateStr = sdf.format(dateIdx)
						def thisDateActions = viewsList.findAll{it.date == dateStr}
						def thisDateViews = thisDateActions.findAll{it.action == "View"}
						def thisDateCreates = thisDateActions.findAll{it.action == "Create"}
						yDataArrListViews.get(0).data << (thisDateViews.size() >= thisDateCreates.size()?thisDateViews.size():thisDateCreates.size())
						yDataArrListViews.get(1).data << thisDateCreates.size()
						dataLabelViews << dateStr
					}
				}else if((dateDiff/30) < 30){//show months
					SimpleDateFormat sdfMonth = new SimpleDateFormat("MMM, yyyy")
					viewsList = viewsList.collect{
						[date:sdfMonth.format(it.accessTime),action:it.action]
					}
					Calendar cMonth = Calendar.getInstance()
					cMonth.setTime(fromDate)
					dateIdx=cMonth.getTime()
					dateIdx.date=1
					for(;dateIdx<=toDate;){
						cMonth.setTime(dateIdx)
						def dateStr = sdfMonth.format(dateIdx)
						def thisDateActions = viewsList.findAll{it.date == dateStr}
						def thisDateViews = thisDateActions.findAll{it.action == "View"}
						def thisDateCreates = thisDateActions.findAll{it.action == "Create"}
						yDataArrListViews.get(0).data << (thisDateViews.size() >= thisDateCreates.size()?thisDateViews.size():thisDateCreates.size())
						yDataArrListViews.get(1).data << thisDateCreates.size()
						dataLabelViews << dateStr
						cMonth.add(Calendar.MONTH, 1)
						dateIdx = cMonth.getTime()
					}
				}else if((dateDiff/365) < 30){//show Years
					SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy")
					viewsList = viewsList.collect{
						[date:sdfMonth.format(it.accessTime),action:it.action]
					}
					Calendar cMonth = Calendar.getInstance()
					for(dateIdx=fromDate;dateIdx<=toDate;){
						cMonth.setTime(dateIdx)
						def dateStr = sdfMonth.format(dateIdx)
						def thisDateActions = viewsList.findAll{it.date == dateStr}
						def thisDateViews = thisDateActions.findAll{it.action == "View"}
						def thisDateCreates = thisDateActions.findAll{it.action == "Create"}
						yDataArrListViews.get(0).data << (thisDateViews.size() >= thisDateCreates.size()?thisDateViews.size():thisDateCreates.size())
						yDataArrListViews.get(1).data << thisDateCreates.size()
						dataLabelViews << dateStr
						cMonth.add(Calendar.YEAR, 1)
						dateIdx = cMonth.getTime()
						if(dateIdx.year == toDate.year){
							dateIdx = toDate
						}
					}
				}else if((dateDiff/365) < 300){//show decades
					SimpleDateFormat sdfMonth = new SimpleDateFormat("yyyy")
					viewsList = viewsList.collect{
						[date:sdfMonth.format(it.accessTime),action:it.action]
					}
					Calendar cMonth = Calendar.getInstance()
					def toDateYear = sdfMonth.format(toDate)
					def firstDLC = true
					for(dateIdx=fromDate;dateIdx<=toDate;){
						cMonth.setTime(dateIdx)
						if(!firstDLC){
							cMonth.add(Calendar.YEAR, 1)
						}
						def dateStr = sdfMonth.format(cMonth.getTime())
						cMonth.setTime(dateIdx)
						cMonth.add(Calendar.YEAR, 10)
						dateIdx = cMonth.getTime()
						if(firstDLC){
							def dLC = dateIdx.year%10
							cMonth.add(Calendar.YEAR, -dLC)
							dateIdx = cMonth.getTime()
							firstDLC = false
						}
						def toDateStr = sdfMonth.format(dateIdx)
						def thisDateActions = viewsList.findAll{it.date >= dateStr && it.date <= toDateStr}
						def thisDateViews = thisDateActions.findAll{it.action == "View"}
						def thisDateCreates = thisDateActions.findAll{it.action == "Create"}
						yDataArrListViews.get(0).data << (thisDateViews.size() >= thisDateCreates.size()?thisDateViews.size():thisDateCreates.size())
						yDataArrListViews.get(1).data << thisDateCreates.size()
						if(toDateStr > toDateYear){
							toDateStr = toDateYear
						}
						dataLabelViews << dateStr+"-"+toDateStr
					}
				}else{
					//TODO handle this scenario. For the time it's not possible if date is entered using calendar. 
				}
				if ( list == null ){
					flash.message = "No data"
					redirect(view: "/reports/formReportsLine")
				 }else{
				 	 def xAndYAxis = getXandYAxisData(formInstance)
					 def yAxisLabels = new ArrayList()
					 
					 if(xAxisFieldType == 'Likert'){
						 list.each{elem->
							 def label = elem."${xAxisFieldName}"
							 def likertValues
							 try{
								 likertValues = JSON.parse(label)
								 def likertColIdx = likertValues?.size()>likertRowIdx?likertValues[likertRowIdx]:''
								 label = likertFieldSettings?.en?.columns?.getAt(likertColIdx)
							 }catch(Exception e){
							 	label = null
							 }
							 elem."${params.xAxis}" = label
						 }
					 }
					 def cat = list.groupBy{it."${params.xAxis}"}
					 def dataLabel = new ArrayList()
					 def pieData = new ArrayList()
					 def yDataArrList = new ArrayList()
					 cat.eachWithIndex{k,v,i->
						 if(i < 21)
						 {
							 params.list("yAxis").eachWithIndex{yAxisFieldName,j->
								 def total = 0
								 if(i==0){
									 def yAxisLabel = xAndYAxis?.yAxis?.find{it.name == yAxisFieldName}
									 def thisYAxis = formInstance.fieldsList.find{it.name == yAxisFieldName}
									 if(thisYAxis.type == 'SingleLineNumber'){
										 def thisYAxisSettings = grails.converters.JSON.parse(thisYAxis.settings)
										 boolean currencyType=(thisYAxisSettings?.currencyType && thisYAxisSettings?.currencyType!='')?true:false
										 String currency=currencyType?grailsApplication.config?.formBuilder.currencies[thisYAxisSettings.currencyType]:''
										 yAxisLabel.currency=" ${currency}"
									 }
									 yDataArrList << [name:yAxisLabel,color:colorArray[j],data:new ArrayList()]
								 }
								 v.each{
									 def fieidVal = it."${yAxisFieldName}"
										 if(j==0){
	 		 								 def  label= (params.xAxis=="created_by_id"?User.get(it."${params.xAxis}").username:it."${params.xAxis}"?:'None')
											 if(label && label!='None'){
												 if(xAxisFieldType == "CheckBox"){
													 def labelJSON = JSON.parse(label.decodeHTML()) as List
													 label = labelJSON.join(", ")
												 }else if(label.class.name == "java.sql.Timestamp"){
												 	label = sdf.format(label)
													 it."${params.xAxis}" = label
												 }else if(xAxisFieldType == "AddressField"){
		 											 def results=""
								 			 		 if(label){
														 def mapValue = grails.converters.JSON.parse(label)
														 results+=mapValue."line1"?mapValue."line1"+"; ":""
														 results+=mapValue."line2"?mapValue."line2"+"; ":""
														 results+=mapValue."city"?mapValue."city"+"; ":""
														 results+=mapValue."state"?mapValue."state"+"; ":""
														 results+=mapValue."zip"?mapValue."zip"+"":""
														 results+=mapValue."country"?mapValue."country"+"; ":""
													 }
												   if(!results.isEmpty()){
													   label=results
													   }else{
													   label="None"
													 }
												 }else if(xAxisFieldType == "NameTypeField"){
													 def thisXAxis = formInstance.fieldsList.find{it.name == params.xAxis}
													 def settings = grails.converters.JSON.parse(thisXAxis.settings)
			 										 def mapValue
													 if(label){
														mapValue = grails.converters.JSON.parse(label)
														label=""
													    boolean	pre=(settings?.showPrefix)
														boolean mid=(settings?.showMiddleName)
														if(pre)
															label+= mapValue?."pre"?mapValue."pre"+" ":""
														label+=  mapValue?."fn"?mapValue."fn"+" ":""
														if(mid)
															label+= mapValue?."mn"?mapValue."mn"+" ":""
														label+= mapValue?."ln"?mapValue."ln":""
														}else{
														label=""
														}
												    if(!label.isEmpty()){
													   label=label
													   }else{
													   label="None"
													   }
												 }
											 }
											 label = ""+label
											 if(!dataLabel.contains(label)){
												    dataLabel << label
											 }
										 }
										if (fieidVal instanceof java.math.BigDecimal) {
											 total += it."${yAxisFieldName}"?:0
									   } else {
											   total = total+1
									   }
								 }
								 yDataArrList.get(j).data << total
							 }
						 }
					 }
					 if(params.list("yAxis").size() == 1){
						 dataLabel.eachWithIndex{label,i->
							 def nthData = new ArrayList()
							 nthData << label
							 nthData << yDataArrList.get(0).data.get(i)
							 pieData << nthData
						 }
					 }
					 params.list("yAxis").eachWithIndex{yAxisFieldName,i->
						 def yAxisLabel = xAndYAxis?.yAxis?.find{it.name == yAxisFieldName}
						 yAxisLabels << [labels:[style:[color:colorArray[i]]],title:[text:yAxisLabel.label+" ${yAxisLabel.currency?.decodeHTML()?:''}",style:[color:colorArray[i]]]]
					 }
					 
	//				 xAndYAxis?.yAxis?.eachWithIndex{y,i->
	//					 if(i==0)
	//					 yAxisLabels << [labels:[style:[color:colorArray[i]]],title:[text:y.label,style:[color:colorArray[i]]]]
	//					 else
	//					 yAxisLabels << [labels:[style:[color:colorArray[i]]],title:[text:y.label,style:[color:colorArray[i]]],opposite: true]
	//				 }
					try{
						def tempTotalViews = 0
						yDataArrListViews?.get(0)?.data?.each{
							tempTotalViews += it
						}
						totalViews = tempTotalViews?:totalViews
					}catch(Exception excep){}
					render(view: "/reports/formReportsLine", model: [formName:formInstance.toString(),yDataArrListViews:yDataArrListViews as JSON,dataLabelViews:dataLabelViews as JSON,pieData: pieData, formList:formList,xAndYAxis: xAndYAxis,yAxisLabel: yAxisLabels as JSON, fromDate:sdf.format(fromDate),toDate:sdf.format(toDate),fullData:list,categories: "['Emails']", data: yDataArrList as JSON, dataLabel: dataLabel as JSON, title: "Report on forms", viewName: "Report on forms",userXAndYAxis:userXAndYAxis,totalCreates:totalCreates,totalViews:totalViews,accessArrList:accessArrList as JSON,accessDataLabel:accessDataLabel as JSON,yAxisAccessLabels:yAxisAccessLabels as JSON])
				 }
			}else{
				render(view: "/reports/formReportsLine",model:[formList:formList])
			}
		}catch(Exception e){
			log.error e
			def xAndYAxis = getXandYAxisData(formInstance)
			flash.message = "Some Problem occured"
			render(view: "/reports/formReportsLine", model: [formList:formList,xAndYAxis: xAndYAxis,fromDate:params.fromDate,toDate:params.toDate])
		}
	}
	
	def feedLineReport = {
        render(view: "/reports/reportsLine")
    }
    
	def getXandYAxis = {
		if(request.xhr){
			render getXandYAxisData(Form.get(params.id)) as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
	def getXandYAxisData(Form formInstance){
		def fieldsList = formInstance.fieldsList
		def xAxis = []
		def yAxis = []
		def setCountField = false
		FormAdmin formAdmin = FormAdmin.findByForm(formInstance)
		def likertCount = 1;
		fieldsList.each{
			boolean numberType = false
			if(it.type == 'FormulaField'){
				numberType = JSON.parse(it.settings).en.newResultType == 'NumberResult'
			}
			if(it.type == 'SingleLineNumber' || numberType){
				yAxis.add([name:it.name,label:it.toString()])
			}else{
				if(!grailsApplication.config.formAdmin.fields.notKeyFigures.contains(it.type) && !"FileUpload".equalsIgnoreCase(it.type) && !"SubForm".equalsIgnoreCase(it.type) && !"Paypal".equalsIgnoreCase(it.type)){
					if("Likert".equalsIgnoreCase(it.type)){
						def settings = JSON.parse(it.settings)
						if(settings?.en?.rows){
							xAxis.add([name:it.name,label:it.toString(),isLikert:true,rows:settings.en.rows])
							if(!setCountField){
								yAxis.add([name:it.name,label:"Count"])
								setCountField = true
							}
						}
					}else{
						xAxis.add([name:it.name,label:it.toString()])
						if(!setCountField){
							yAxis.add([name:it.name,label:"Count"])
							setCountField = true
						}
					}
				}
			}
		}
		if(formAdmin && formAdmin.formType == 'Approval'){
			xAxis.add([name:"created_by_id",label:"Created By"])
		}
		def data = [xAxis:xAxis,yAxis:yAxis]
		return data
	}
	
	def emailReport = {
		def user = springSecurityService.currentUser
		def emailSettingList = EmailSettings.findAllByUser(user)
		if(!emailSettingList){
			flash.message = 'emailSettings.create.breforeReport'
			flash.defaultMessage = "Please create email setting before running report on it"
			redirect(controller:'emailSettings',action:'create')
		}else{
			render(view: "/reports/emailReport" ,model:[emailSettingList:emailSettingList])
		}
	}
	
	def search = {
			def instance = [:]
			def user = springSecurityService.currentUser
			instance.email = EmailSettings.get(Long.parseLong(params.id))
			try{
				instance.daysPrior = (int)params.daysPrior.toInteger()
				if(instance.daysPrior <= 0)
					instance.daysPrior = (int)grailsApplication.config.reports.email.search.defaultDaysPrior
				else if(instance.daysPrior > 180)
					instance.daysPrior = (int)grailsApplication.config.reports.email.search.maxDaysPrior
			}catch(Exception e){
				instance.daysPrior = (int)grailsApplication.config.reports.email.search.defaultDaysPrior
			}
			def emailSettingList = EmailSettings.findAllByUser(user)
			def now = new Date()
			if ( instance ) {
				def list  = inboxReaderService.readMail ( instance )
				if ( list == null ){
				  flash.message = "emailAnalyser.invalid.credentials"
				  flash.args = [g.link(controller:'emailSettings'){message(code:"emailSetting.label",'default':'Email Settings')}] 
				  flash.message = message(code:flash.message,args:flash.args,'default':'')
				  redirect(controller:"report", action:"emailReport", id:params.id)
				 }else
					render(view: "/reports/emailReport", model: [id:instance.id,search:instance,fullData:list.sort{ a, b -> b.value.emailCount <=> a.value.emailCount },categories: "['Emails']", data: emailData(list), dataLabel: getEmailCategories(list), title: "Top 20 Senders in past ${instance.daysPrior} days", viewName: "Top 20 Senders in past ${instance.daysPrior} days",emailSettingList:emailSettingList,daysPrior:instance.daysPrior,selectedAccount:params.id])
			}
		
		}
	
	def userReport = {
		def formList = Form.findAllByTenantId(springSecurityService.currentUser.userTenantId)
		def xAndYAxis = [[name:'ipAddress',label:'IP Address'],[name:'accessMode',label:'Access Mode'],[name:'accessedClass',label:'Accessed Form'],[name:'action',label:'Action'],[name:'location',label:'Access Location']]
		render(view: "/reports/userReport", model: [xAndYAxis:xAndYAxis,formList:formList] )
	}
	
	def userActionReport = {
		log.info "Entering userActionReport"
		
		try{
			def fromDate = params.fromDate
			def toDate = params.toDate
			def xAndYAxis = [[name:'ipAddress',label:'IP Address'],[name:'accessMode',label:'Access Mode'],[name:'action',label:'Action'],[name:'location',label:'Access Location']]
			if ( fromDate && toDate && params.userXAxis && params.userYAxis) {
				SimpleDateFormat sdf = new SimpleDateFormat(grailsApplication.config.format.date)
			
				// Default search is for a week
				if ( (fromDate == null || fromDate?.length() == 0 ) && (toDate == null || toDate?.length() == 0) ){
					fromDate = new Date() - 7
					toDate = new Date()
				}else{
					fromDate = sdf.parse(params.fromDate)
					toDate = sdf.parse(params.toDate)
					toDate.setHours(23)
					toDate.setMinutes(59)
				}
				//def formList = Form.findAllByTenantIdAndFormCat(springSecurityService.currentUser.userTenantId,'N')
				def formList=Form.createCriteria().list(){
					eq 'tenantId',springSecurityService.currentUser.userTenantId
					not{
						eq 'formCat','S'
					}
				}
				def formInstance
				def formName
				if(params.userXAxis){
					formInstance = Form.get(params.userXAxis)
					if(formInstance){
						formName = JSON.parse(formInstance.settings)."en".name
					}
					
				}
				
				def list = UserAppAccessDetail.createCriteria().list(){
								between('accessTime',fromDate,toDate)
								eq('accessedClass',formInstance.domainClass.name)
							}
				if ( list == null ){
					flash.message = "No data"
					redirect(view: "/reports/userReport")
				 }else{
					 def yAxisLabels = new ArrayList()
					 def cat = list.groupBy{it."${params.userYAxis}"}
					 def yDataArrList = new ArrayList()
					 def dataLabel = new ArrayList()
					 yDataArrList << [name:"Count",color:colorArray[0],data:new ArrayList()]
					 cat.eachWithIndex{k,v,i->
							 def total = 0
							 v.each{
									 def label = k
									 if(!dataLabel.contains(label)){
										 dataLabel << label
										 total = total+1
									 }else{
									 	total = total+1
									 }
							 }
							 yDataArrList.get(0).data << total
							 yAxisLabels << [labels:[style:[color:colorArray[i]]],title:[text:k,style:[color:colorArray[i]]]]
					 }
					
				 	render(view: "/reports/userReport", model: [xAndYAxis: xAndYAxis,yAxisLabel: yAxisLabels as JSON,dataLabel: dataLabel as JSON, data: yDataArrList as JSON, fromDate:sdf.format(fromDate),toDate:sdf.format(toDate),fullData:list, title: "Report on User action for "+formName, viewName: "Report on User Action for "+formName,formList:formList])
				 }
			}else{
				render(view: "/reports/userReport", model: [xAndYAxis:xAndYAxis] )
			}
		}catch(Exception e){
			log.error e
			flash.message = "Some Problem occured"
			redirect(action: "userReport")
		}
	}
	
	def formDefaultReport = {
		log.info "Entering userActionReport"
		
		try{
			def formInstance
			def formName
			if(params.formId){
				formInstance = Form.read(params.formId)
				if(formInstance?.tenantId != session['user'].userTenantId){
					formInstance = null
				}
				formName = formInstance?.toString()
			}
			
			if(!formInstance){
				flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'form.label', default: 'Form'), params.formId])}"
				flash.defaultMessage = flash.message
				redirect(controller:'form',action:'list')
				return
			}
			def toDate = params.toDate?:""
			def fromDate = params.fromDate?:""
			SimpleDateFormat sdf = new SimpleDateFormat(grailsApplication.config.format.date)
		
			// Default search is for a week
			if ( (fromDate == null || fromDate?.length() == 0 ) && (toDate == null || toDate?.length() == 0) ){
				toDate = new Date()
				fromDate = toDate-(toDate.date - 1)
				fromDate.setHours(0)
				fromDate.setMinutes(0)
			}else{
				fromDate = sdf.parse(params.fromDate)
				toDate = sdf.parse(params.toDate)
				if(params.nextPrev == '<'){
					toDate = fromDate-1
					fromDate = toDate - (toDate.date-1)
				}else if(params.nextPrev == '>'){
					fromDate = toDate+1
					def tempDate = new Date()
					if(fromDate.month == tempDate.month){
						toDate = tempDate
					}else{
						Calendar c = Calendar.getInstance()
						c.setTime(fromDate)
						c.add(Calendar.MONTH, 1)
						toDate = c.getTime()
						toDate = toDate - 1
					}
				}
				toDate.setHours(23)
				toDate.setMinutes(59)
			}
			//def formList = Form.findAllByTenantIdAndFormCat(springSecurityService.currentUser.userTenantId,'N')
			def formList=Form.createCriteria().list(){
				eq 'tenantId',springSecurityService.currentUser.userTenantId
				not{
					eq 'formCat','S'
				}
			}
			
			def list = UserAppAccessDetail.createCriteria().list(){
				between('accessTime',fromDate,toDate)
				eq('accessedClass',formInstance.domainClass.name)
			}
			def totalViews = 0
			def totalCreates = 0
			if ( list == null ){
				flash.message = "No data"
				redirect(view: "/reports/formDefaultReport")
			 }else{
			 	 def dateIdx
				 def yDataArrList = [[name:"Views",color:colorArray[1],data:[]],[name:"Creates",color:colorArray[6],data:[]]]
				 def dataLabel = new ArrayList()
				 def yAxisLabels = [[labels:[style:[color:colorArray[1]]],title:[text:"Views",style:[color:colorArray[1]]]],[labels:[style:[color:colorArray[6]]],title:[text:"Creates",style:[color:colorArray[6]]]]]
				 list = list.collect{
					 [date:sdf.format(it.accessTime),action:it.action]
				 }
				 for(dateIdx=fromDate;dateIdx<=toDate;dateIdx++){
					 def dateStr = sdf.format(dateIdx)
					 def thisDateActions = list.findAll{it.date == dateStr}
					 def thisDateViews = thisDateActions.findAll{it.action == "View"}
					 def thisDateCreates = thisDateActions.findAll{it.action == "Create"}
					 totalViews += (thisDateViews.size() >= thisDateCreates.size()?thisDateViews.size():thisDateCreates.size())
					 totalCreates += thisDateCreates.size()
					 yDataArrList.get(0).data << (thisDateViews.size() >= thisDateCreates.size()?thisDateViews.size():thisDateCreates.size())
					 yDataArrList.get(1).data << thisDateCreates.size()
					 dataLabel << dateIdx.date
				 }
				
				 render(view: "/reports/formDefaultReport", model: [totalViews:totalViews,totalCreates:totalCreates,yAxisLabel: yAxisLabels as JSON,dataLabel: dataLabel as JSON, data: yDataArrList as JSON, fromDate:sdf.format(fromDate),toDate:sdf.format(toDate),currentMonthDate:(fromDate),fullData:list, title: "Report on User Views for "+formName, viewName: "Report on User Views for "+formName,formList:formList])
			 }
		}catch(Exception e){
			log.error e
			flash.message = "Some Problem occured"
			redirect(action: "formDefaultReport")
		}
	}
	
	def getActionCategories(list,def xAxis){
		def index
		def cat = new ArrayList()
		list.each {it ->
			if(xAxis == "accessedClass"){
				DomainClass dc= DomainClass.findByName(it."${xAxis}")
				Form form
				def formName
				if(dc){
					form  = Form.findByDomainClass(dc)
				}
				if(form){
					formName = JSON.parse(form.settings)."en".name
				}else{
					formName = it."${xAxis}"
				}
				if ( !cat?.contains( formName ) ){
					cat << formName
				}
			}else{
				if ( !cat?.contains( it."${xAxis}" ) )
					cat << it."${xAxis}"
			}
		}
		return cat
	}
	
	
	def clientFormReport = {
		log.info "Entering clientFormActionReport"
		try{
			def userRoles = springSecurityService.currentUser.authorities*.authority
			def clientList = new ArrayList()
			def selectedClientList = new ArrayList()
			if(userRoles.contains(Role.ROLE_SUPER_ADMIN)){
				clientList = Client.list()
				selectedClientList = params.list("client")
				if(selectedClientList.size() == 0)
					selectedClientList[0] = (int)springSecurityService.currentUser.userTenantId
			}else{
				clientList[0] = Client.get(springSecurityService.currentUser.userTenantId)
				selectedClientList[0] = (int)springSecurityService.currentUser.userTenantId
			}
			
			
			if ( selectedClientList.size() == 0){
				//flash.message = "No data"
				render(view: "/reports/clientFormReport", model: [clientList:clientList])
			 }else{
			 	  def dataLabel = new ArrayList()
				  def yDataArrList = new ArrayList()
				  yDataArrList << [name:"Form Count",color:colorArray[0],data:new ArrayList()]
				  selectedClientList.eachWithIndex{clientId,i->
					 def client =  Client.get(clientId.toLong())
					 dataLabel << client.name
					 def formList = Form.findAllByTenantId(clientId.toLong())
					 def total = 0
					 def drillData = [:]
					 drillData.y = formList.size()
					 drillData.color = colorArray[i]
					 def drillYAxisData = [:]
					 drillYAxisData.name = "Form's of "+client.name
					 def drillCat = []
					 def drillResponseData = []
					 formList.eachWithIndex{form,j->
						 def formName = JSON.parse(form.settings).en.name
						 drillCat << formName
						 def domainClass = grailsApplication.getDomainClass(form.domainClass.name)
						 if(!domainClass){
							 domainClassService.reloadUpdatedDomainClasses()
							 domainClass = grailsApplication.getDomainClass(form.domainClass.name)
						 }
						 def formResponse = sqlDomainClassService.responseCount(form,SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS))
						 drillResponseData << formResponse
					 }
					 drillYAxisData.categories = drillCat
					 drillYAxisData.data = drillResponseData
					 drillYAxisData.color = colorArray[i]
					 drillData.drilldown = drillYAxisData
					 yDataArrList.get(0).data << drillData
				  }
				 render(view: "/reports/clientFormReport", model: [dataLabel: dataLabel as JSON, data: yDataArrList as JSON, fullData:new ArrayList(), title: "Forms Created By Clients", viewName: "",clientList:clientList,selectedClientList:selectedClientList])
			 }
		}catch(Exception e){
			log.error e
			flash.message = "Some Problem occured"
			render(view: "/reports/clientFormReport", model: [clientList:clientList])
		}
	}
/*
    def projectTypeCategories() {

        //def cat =  "
        //Project.findAllByActiveBillableType(true,DropDown.BILLABLE)*.projectName
    }

    def invoiceCategories() {

        def l = Invoice.findAllByDueDateLessThan(new Date(), [max: max])*.project.projectName as JSON
        //println l
        return l
    }

    def invoiceData() {
        HashMap jsonMap = new HashMap()
        jsonMap.name = "Due Amount"
        jsonMap.data = Invoice.findAllByDueDateLessThan(new Date(), [max: max])*.dueAmount
        //println jsonMap
        return [jsonMap] as JSON
    }

    def projectCategories() {
        return Project.findAllByActive(true, [max: max])*.projectName as JSON
    }
 	def accountCategories() {
        return Account.findAllByActive(true, [max: max])*.name as JSON
    }
    
    
    def oppData() {
        HashMap jsonMap = new HashMap()
        jsonMap.name = "Deal Value"
        jsonMap.data = Opportunity.list([max: max])*.dealValue
        //println jsonMap
        return [jsonMap] as JSON
    }

    def projectData() {
		// Get all projects budgeted by hour
		com.oneapp.cloud.core.DropDown budgetBy = com.oneapp.cloud.core.DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.PROJECT_BUDGET_BY, 'PROJECT_HOUR')
		
        def one = Project.findAllByActiveAndBudgetBy(true,budgetBy,[max: max])
        def two = Project.findAllByActiveAndBudgetBy(true,budgetBy, [max: max])*.calculateTotalTimes()

        HashMap jsonMap = new HashMap()
        jsonMap.name = "Budget"
        jsonMap.data = one.collect {p ->
            return p.budget
        }

        HashMap jsonMap2 = new HashMap()
        jsonMap2.name = "Booked"
        jsonMap2.data = one.collect {p ->
            return p.calculateTotalTimes()
        }

        def result = [jsonMap, jsonMap2] as JSON
        //println result
        return result
    }

	def pieData() {
	                    
    	def billable = com.oneapp.cloud.core.DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.PROJECT_BILLABLE_TYPE,"BILLABLE")
        def nbillable = com.oneapp.cloud.core.DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.PROJECT_BILLABLE_TYPE,"NON_BILLABLE")
        
        def one = Project.findAllByActiveAndBillableType(true,billable, [max: max])
        def two = Project.findAllByActiveAndBillableType(true,nbillable, [max: max])

		def total = one.size()+two.size()
		
		Double percentOne, percentTwo
		try{
			 percentOne = (one.size()/total)*100
			 percentTwo = (two.size()/total)*100
		}catch( Exception ex ){
			//println ex
		}
        HashMap jsonMap = new HashMap()
        jsonMap.name = "Billable - ${one.size()}"
        jsonMap.y =  percentOne.round(2)
        

        HashMap jsonMap2 = new HashMap()
        jsonMap2.name = "Non Billable - ${two.size()}"
        jsonMap2.y = percentTwo.round(2)
        

        def result = [jsonMap, jsonMap2] as JSON
       // println result
        return result
                        
	}
	
	def lineData() {
		HashMap jsonMap = new HashMap()
        jsonMap.name = "Leave Request"
        def now = new Date()
        
        params.sort = "dateCreated"
        params.order = "desc"
        AppLog feed = AppLog.findAllByDateCreatedBetween(now,now-12,params)
       
        jsonMap.data =  [7.0, 10.0, 9.0, 14.0, 18.0, 21.0, 25.0, 26.0]
        
        HashMap jsonMap2 = new HashMap()
        jsonMap2.name = "Poll"
        jsonMap2.data =  [7.0, 6.0, 49.0, 44.0, 58.0, 29.0, 18.0, 13.0, 9.0]
        
        HashMap jsonMap3 = new HashMap()
        jsonMap3.name = "General"
        jsonMap3.data =  [17.0, 26.0, 49.0, 54.0, 68.0, 29, 28.0, 33.0, 19.0]
        
        
		return [jsonMap,jsonMap2,jsonMap3] as JSON			
	}
	
	def feedLineReport = {
        render(view: "/reports/reportsLine", model: [data: lineData(), dataLabel: "Tokyo", title: "Company Feed By Type", viewName: "Company Feed By Type"])
    }
    
	def oppPieReport = {
        render(view: "/reports/reportsPie", model: [categories: accountCategories(), data: pieData(), dataLabel: "USD", title: "Billable/Non Billable Projects", viewName: "Billable/Non Billable"])
    }
    
	def oppReport = {
        render(view: "/reports/reports", model: [categories: accountCategories(), data: oppData(), dataLabel: "USD", title: "Opps. By Customer", viewName: "Opportunities"])
    }

    def projectReport = {
        render(view: "/reports/reports", model: [categories: projectCategories(), data: projectData(), dataLabel: "hours", title: "Budget v/s Actual", viewName: "Project"])
    }

    def invoiceReport = {
        render(view: "/reports/reports", model: [categories: invoiceCategories(), data: invoiceData(), dataLabel: "USD", title: "Invoices Past Due", viewName: "Invoice"])
    }

    def projectReportPie = {
        render(view: "/reports/reportsPie", model: [categories: projectCategories(), data: projectData(), dataLabel: "hours", title: "Billable v/s Non-Billable", viewName: "Project"])
    }*/
}