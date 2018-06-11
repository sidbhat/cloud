package com.oneapp.cloud.core

import org.codehaus.groovy.grails.commons.ConfigurationHolder;

import grails.plugin.multitenant.core.util.TenantUtils
import org.grails.formbuilder.*
import org.grails.paypal.Payment;
import org.grails.paypal.PaymentItem;

import java.util.*;
import java.text.*;
import grails.converters.JSON;

class RuleService {

  static transactional = false
  def grailsApplication
  def springSecurityService
  def domainClassService
  def sessionFactory
  def utilService
  def inboxReaderService
  def sqlDomainClassService
	def runRules() {
	
	// Look up active rule sets for rules to run
	def ruleSet = RuleSet.findAllByStatus(RuleSet.ACTIVE,[sort:'_order', order:'asc'])
	boolean ruleSetCondition = false 
	boolean ruleCondition = false
	
	//def dc = grailsApplication.getDomainClass(obj?.class?.name)
	//def domainInstance = dc.clazz.get(obj?.id)
	//println "domain is ${dc} and instance is ${domainInstance}"
	try{
	ruleSet.each{
			if ( it._action == "Fetch Email" ){
				//Do nothing
			}else{
			    def rules = Rule.findAllByRuleSetAndStatus(it,  "Active",[sort:'_order', order:'asc'])
				ruleCondition = false
	            boolean exit=false
	       		def session = sessionFactory.currentSession 
	       		def query =  "select id from "
	       		def tableName
				def whereClause = " where "
	       		def subQuery =""
	       		def currentForm
				def allQuery = false
	            // Run all rules and form the select query
	            rules.eachWithIndex{ rule,i->
					try{
	    		        Form formInstance = Form.get(Long.parseLong(rule?.className))
						if(!formInstance){
							log.info("Form deleted and rule exists with form id: "+rule?.className)
						}else{
		    				if ( i == 0 )
		    					tableName =  formInstance?.name?.toLowerCase()
							currentForm = formInstance
		    				Field field = Field.get(rule?.attributeName)
		    				if("*".equals(rule.value))
								allQuery = true
							else{
								boolean isFormulaNumberType = false
								boolean isFormulaDateType = false
								if(field.type.equalsIgnoreCase('FormulaField')){
									try{
										def settings = JSON.parse(field.settings)
										if(settings.en.newResultType == 'DateResult'){
											isFormulaDateType = true
										}else if(settings.en.newResultType == 'DateResult'){
											isFormulaNumberType = true
										}
									}catch(Exception e){
										log.error e
									}
								}
								if(field.type.equalsIgnoreCase('SingleLineNumber') || isFormulaNumberType){
									def ruleValue = Integer.parseInt(rule.value)
									subQuery = subQuery + " ${field.name} ${rule.operator} ${ruleValue} "
								}else if(field.type.equalsIgnoreCase('SingleLineDate') || isFormulaDateType){
									SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
									if(rule.value.indexOf("@now") > -1){
										Calendar cal = Calendar.getInstance();
										cal.setTime(dateFormat.parse(dateFormat.format( new Date())));
										def incdecVal
										def customRuleValue
										def strDateValue
										if(rule.value.indexOf("+") > -1){
											incdecVal = rule.value.substring(rule.value.indexOf("+")+1).replaceAll(" ","")
											customRuleValue = Integer.parseInt(incdecVal)
											cal.add( Calendar.DATE, customRuleValue)
											strDateValue = calendarToString(cal)
										}else if(rule.value.indexOf("-") > -1){
											incdecVal = rule.value.substring(rule.value.indexOf("-")+1).replaceAll(" ","")
											customRuleValue = Integer.parseInt(incdecVal)
											def date = cal.add( Calendar.DATE, -customRuleValue)
											strDateValue = calendarToString(cal)
										}
										subQuery = subQuery + " ${field.name} ${rule.operator} '${strDateValue}' "
									}else{
										SimpleDateFormat sdf = new SimpleDateFormat('MM/dd/yyyy')
										def value = rule.value
										def dateValue = null
										try{
											dateValue = sdf.parse(rule.value)
											value = dateFormat.format(dateValue)
										}catch(Exception e){
											log.error e
										}
										subQuery = subQuery + " ${field.name} ${rule.operator} '${value}' "
									}
									def ruleValue = Integer.parseInt(rule.value)
									
								}else{
									subQuery = subQuery + " ${field.name} ${rule.operator} '${rule.value}' "
								}
			    				if ( i+1 < rules.size() ){
			    					subQuery = subQuery + " ${rule._condition?.toLowerCase()} "
			    				}
							}
						}
					}catch(Exception ex){
						log.error ex
					}
	            }// end of loop for rules
				def q
				def list
				try{
				
					if(tableName){
						if(allQuery)
							q = query + tableName
						else
							q = query + tableName + whereClause + subQuery
			           	//println  "ran query ${q}"
			            list = session.createSQLQuery( q ).list()
			            //println  "ran query ${q} and result was ${list}"
			            if ( list.size() > 0 )
			            		ruleCondition = true
					}
				}catch(Exception ex){
					log.error ex
				}
	            			
	           // println "Ran rule ${rule.id} ${rule._order} ${rule.className} ${rule.operator} ${rule.value}  and condition was ${ruleCondition}"
	           	
	            // Check for condition and perform actions
	            //println "rule condition was ${ruleCondition}"
	            if ( ruleCondition )
	            	performActions(it,currentForm,list*.toLong())
			}
	 	}//end of loop for rule set
	 }catch(Exception){
	 
	 }finally {
	 	sessionFactory.currentSession.flush() 
		sessionFactory.currentSession.clear()
	 }
	
	}
	
	// perform the actions mentioned in the rules set if the rules are met
	def performActions(def ruleSet, def currentForm, def resultList){
		try{
			def domainInstance
			def domainClass
			def userInstance
			def groupInstance
			def departmentInstance
			def userManagerInstance
			def userDeptInstance
			def roleInstance
			def currentTenantId
			def currentFormName = currentForm.name+"."+currentForm.name
			if(ruleSet?.resultClass == RuleSet.USER){
				userInstance = User.findByUsername(ruleSet?.resultInstance)
				currentTenantId = userInstance.userTenantId
			}else if(ruleSet?.resultClass == RuleSet.GROUP){
				groupInstance = GroupDetails.findByGroupName(ruleSet?.resultInstance)
				currentTenantId = groupInstance.tenantId
			}else if(ruleSet?.resultClass == RuleSet.ROLE){
				roleInstance = Role.findByDescription(ruleSet?.resultInstance)
				currentTenantId = ruleSet?.tenantId
			}else if(ruleSet?.resultClass == RuleSet.DEPARTMENT){
				departmentInstance = DropDown.findByName(ruleSet?.resultInstance)
				currentTenantId = departmentInstance.tenantId
			}else if(ruleSet?.resultClass == RuleSet.USER_MANAGER){
				currentTenantId = ruleSet?.createdBy?.userTenantId
			}else if(ruleSet?.resultClass == RuleSet.USER_DEPARTMENT){
				currentTenantId = ruleSet?.createdBy?.userTenantId
			}
			
			
		if ( ruleSet._action == "Email" ){
			//send email
			/*AsynchronousEmailStorage async = new AsynchronousEmailStorage()
            async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
            async.emailTO = "${userInstance.username}"
            async.emailSubject="Your feed information"
            async.emailData=""
            async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
            async.save(flush:true)*/
       	
		}else if ( ruleSet._action == "Subscribe" ){
			// Check to see if result should be assigned to a user, group, department, role, user_department or user_manager
			TenantUtils.doWithTenant(currentTenantId) {
				ActivityFeedConfig config = ActivityFeedConfig.findByConfigName(currentFormName)
			
				if(config){
					def afList = ActivityFeed.findAllByConfig(config) 
					def rules = Rule.findAllByRuleSet(ruleSet)
					if(rules){
						def formInstanceId = rules?.get(0)?.className
						def ruleShareType = FormAdmin.findByForm(Form.get(Long.parseLong(formInstanceId))).formType
						def ruleFormIntance = sqlDomainClassService.ruleFormInstanceList(currentForm.name,resultList)
						ruleFormIntance.each { rfi ->
							if(afList.shareId.contains(rfi.id))
							{
								def ruleCreatedUser = User.get(rfi.created_by_id)
								afList.each {af->
									if(af.shareId == rfi.id){
										if ( ruleSet?.resultClass == RuleSet.USER ) {
											def list =  af?.sharedUsers.find { it.id == userInstance.id }
											if ( list == null ) {
												af?.sharedUsers.add(userInstance)
												af?.save()
												sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
											}
										}else if ( ruleSet?.resultClass == RuleSet.GROUP ){
											def list =  af?.sharedGroups.find { it.id == groupInstance.id }
											if ( list == null ) {
												af?.sharedGroups.add(groupInstance)
												af?.save()
												sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
											}
										}else if ( ruleSet?.resultClass == RuleSet.ROLE ){
											def list =  af?.sharedRoles.find { it.id == roleInstance.id }
											if ( list == null ) {
												af?.sharedRoles.add(roleInstance)
												af?.save()
												sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
											}
										}else if ( ruleSet?.resultClass == RuleSet.DEPARTMENT ){
											def list =  af?.sharedDepts.find { it.id == departmentInstance.id }
											if ( list == null ) {
												af?.sharedDepts.add(departmentInstance)
												af?.save()
												sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
											}
										}else if ( ruleSet?.resultClass == RuleSet.USER_MANAGER ){
											userManagerInstance = rfi.createdBy.reportsTo
											def list =  af?.sharedUsers.find { it.id == userManagerInstance?.id }
											if ( list == null ) {
												af?.sharedUsers.add(userManagerInstance)
												af?.save()
												sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
											}
										}else if ( ruleSet?.resultClass == RuleSet.USER_DEPARTMENT ){
											userDeptInstance = rfi.createdBy.department
											def list =  af?.sharedDepts.find { it.id == userDeptInstance?.id }
											if ( list == null ) {
												af?.sharedDepts.add(userDeptInstance)
												af?.save()
												sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
											}
										}
									}
								}
							}else{
								def ruleActivityFeed = new ActivityFeed()
								ruleActivityFeed.config = config
								ruleActivityFeed.shareId = rfi.id
								def serverUrl = "${grailsApplication.config.grails.serverURL}/ddc/edit?id=${ruleActivityFeed.shareId}&formId=${currentForm.id}"
								def mess = "Click here to view ${ruleShareType.toLowerCase()} - "+serverUrl
								ruleActivityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>",false)
								def ruleCreatedUser = User.get(rfi.created_by_id)
								if ( ruleSet?.resultClass == RuleSet.GROUP ){
									ruleActivityFeed.addToSharedGroups( groupInstance )
								}else if ( ruleSet?.resultClass == RuleSet.USER ){
									ruleActivityFeed.addToSharedUsers( userInstance )
								}else if ( ruleSet?.resultClass == RuleSet.ROLE ){
									ruleActivityFeed.addToSharedRoles( roleInstance )
								}else if ( ruleSet?.resultClass == RuleSet.DEPARTMENT ){
									ruleActivityFeed.addToSharedDepts( departmentInstance )
								}else if ( ruleSet?.resultClass == RuleSet.USER_MANAGER ){
									userManagerInstance = ruleCreatedUser?.reportsTo
									if(userManagerInstance){
										ruleActivityFeed.addToSharedUsers( userManagerInstance )
									}
								}else if ( ruleSet?.resultClass == RuleSet.USER_DEPARTMENT ){
									userDeptInstance = ruleCreatedUser?.department
									ruleActivityFeed.addToSharedDepts( userDeptInstance )
								}
								ruleActivityFeed.createdBy = ruleCreatedUser
								ruleActivityFeed.feedState = ActivityFeed.ACTIVE
								ruleActivityFeed.dateCreated = new Date()
								ruleActivityFeed.lastUpdated = new Date()
								if (!ruleActivityFeed.hasErrors() && ruleActivityFeed.save(validate:true,flush:true)){
									def ruleActivityFieldInstance = JSON.parse(currentForm.settings)
									ruleActivityFeed.addTag(ruleActivityFieldInstance.en.name.toLowerCase())
									def infoMessage = ""
									def feedClassName = ruleActivityFeed.config.className
									feedClassName= feedClassName.substring(0,feedClassName.indexOf("."))
									def form  = Form.findByName(feedClassName)
									def formName = JSON.parse(form.settings)."en".name
									if(!formName)
										formName = form.name
									infoMessage = "Form entry of "+formName+" shared with you"
									sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
									def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:ruleActivityFeed, actionTime:ruleActivityFeed.dateCreated)
									activityNotification.save(flush:true)
								}
						}
					}
				}
			}else{
				def ruleActivityFeedConfig = new ActivityFeedConfig()
				def rules = Rule.findAllByRuleSet(ruleSet)
				if(rules){
					def formInstanceId = rules?.get(0)?.className
					def ruleShareType = FormAdmin.findByForm(Form.get(Long.parseLong(formInstanceId))).formType
					def dateCreated = new Date()
					ruleActivityFeedConfig = new ActivityFeedConfig(createdBy: ruleSet.createdBy, shareType: ruleShareType, configName: currentFormName,className:currentFormName, dateCreated: dateCreated, lastUpdated: dateCreated)
					ruleActivityFeedConfig.save(flush:true)
					def ruleFormIntance = sqlDomainClassService.ruleFormInstanceList(currentForm.name,resultList)
					ruleFormIntance.each { rfi ->
							def ruleActivityFeed = new ActivityFeed()
							ruleActivityFeed.config = ruleActivityFeedConfig
							ruleActivityFeed.shareId = rfi.id
							def serverUrl = "${grailsApplication.config.grails.serverURL}/ddc/edit?id=${ruleActivityFeed.shareId}&formId=${currentForm.id}"
							def mess = "Click here to view ${ruleShareType.toLowerCase()} - "+serverUrl
							ruleActivityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>",false)
							def ruleCreatedUser = User.get(rfi.created_by_id)
							if ( ruleSet?.resultClass == RuleSet.GROUP ){
								ruleActivityFeed.addToSharedGroups( groupInstance )
							}else if ( ruleSet?.resultClass == RuleSet.USER ){
								ruleActivityFeed.addToSharedUsers( userInstance )
							}else if ( ruleSet?.resultClass == RuleSet.ROLE ){
								ruleActivityFeed.addToSharedRoles( roleInstance )
							}else if ( ruleSet?.resultClass == RuleSet.DEPARTMENT ){
								ruleActivityFeed.addToSharedDepts( departmentInstance )
							}else if ( ruleSet?.resultClass == RuleSet.USER_MANAGER ){
								userManagerInstance = ruleCreatedUser?.reportsTo
								ruleActivityFeed.addToSharedUsers( userManagerInstance )
							}else if ( ruleSet?.resultClass == RuleSet.USER_DEPARTMENT ){
								userDeptInstance = ruleCreatedUser?.department
								ruleActivityFeed.addToSharedDepts( userDeptInstance )
							}
							ruleActivityFeed.createdBy = ruleCreatedUser
							ruleActivityFeed.feedState = ActivityFeed.ACTIVE
							ruleActivityFeed.dateCreated = new Date()
							ruleActivityFeed.lastUpdated = new Date()
							if (!ruleActivityFeed.hasErrors() && ruleActivityFeed.save(validate:true,flush:true)){
								def ruleActivityFieldInstance = JSON.parse(currentForm.settings)
								ruleActivityFeed.addTag(ruleActivityFieldInstance.en.name.toLowerCase())
								def infoMessage = ""
								def feedClassName = ruleActivityFeed.config.className
								feedClassName= feedClassName.substring(0,feedClassName.indexOf("."))
								def form  = Form.findByName(feedClassName)
								def formName = JSON.parse(form.settings)."en".name
								if(!formName)
									formName = form.name
								infoMessage = "Form entry of "+formName+" shared with you"
								sendMail(rfi,currentForm,ruleSet, ruleCreatedUser,currentTenantId)
								def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:ruleActivityFeed, actionTime:ruleActivityFeed.dateCreated)
								activityNotification.save(flush:true)
							}
						}
					}
				}
			
			}
		
		}
		}catch(Exception ex){
		
		}
	}

	def calendarToString(Calendar cal) {
		StringBuffer ret = new StringBuffer();
		  ret.append(cal.get(Calendar.YEAR));
		  ret.append("-");
		  
		  String month = null;
		  int mo = cal.get(Calendar.MONTH) + 1; /* Calendar month is zero indexed, string months are not */
		  if(mo < 10) {
			month = "0" + mo;
		  }
		  else {
			month = "" + mo;
		  }
		  ret.append(month);
		  
		  ret.append("-");
		  
		  String date = null;
		  int dt = cal.get(Calendar.DATE);
		  if(dt < 10) {
			date = "0" + dt;
		  }
		  else {
			date = "" + dt;
		  }
		  ret.append(date);
		
		
		return ret.toString();
	  }
	
   def sendMail(def domainInstance,Form form,RuleSet ruleSet, User entryCreatedUser, def currentTenantId){
		  try{
			  TenantUtils.doWithTenant(currentTenantId) {
			def uniqueId=	  UniqueFormEntry.findByFormIdAndInstanceId(form.id, domainInstance.id).uniqueId
				def formSettings = JSON.parse(form.settings)
				def formName = formSettings?.en?.name?:form.name
				def templateText = drawFormEmail( form, domainInstance)
			def action=(domainInstance.version==0?'create':'create')
				if(ruleSet?.resultClass == RuleSet.GROUP){
					def sendToGroup = GroupDetails.findByGroupName(ruleSet?.resultInstance)
					if(sendToGroup.user.size() > 0){
						sendToGroup.user.each{ usr->
							saveEmails(  entryCreatedUser, usr,action,uniqueId, formName,templateText)
							
							/*AsynchronousEmailStorage async = new AsynchronousEmailStorage()
							async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
							async.emailTO = "${us.username}"
							async.emailSubject="New Entry for ${formName}"
							async.emailData="${'Description : '+description}<br>Dear ${us?.firstName},<br><br>You have a new form submission for your form <b>${formName}</b> by: <b>${entryCreatedUser.firstName?:''} ${entryCreatedUser.lastName?:''}</b>${entryCreatedUser.userTenantId!=0?('&lt;'+entryCreatedUser.username+'&gt;'):''}<br><br>"+templateText+"<br><br>Thank you,<br>Your Form Builder Team!"
							async.isHtml=true
							async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
							async.save()*/
						}
					}
				}else if(ruleSet?.resultClass == RuleSet.USER){
					def sendToUser = User.findByUsername(ruleSet?.resultInstance)
					saveEmails(  entryCreatedUser, sendToUser,action,uniqueId, formName,templateText)
					/*AsynchronousEmailStorage async = new AsynchronousEmailStorage()
					async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
					async.emailTO = "${sendToUser.username}"
					async.emailSubject="New Entry for ${formName}"
					async.emailData="${'Description : '+description}<br>Dear ${sendToUser?.firstName},<br><br>You have a new form submission for your form <b>${formName}</b> by: <b>${entryCreatedUser.firstName?:''} ${entryCreatedUser.lastName?:''}</b>${entryCreatedUser.userTenantId!=0?('&lt;'+entryCreatedUser.username+'&gt;'):''}<br><br>"+templateText+"<br><br>Thank you,<br>Your Form Builder Team!"
					async.isHtml=true
					async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
					async.save()*/
				}else if(ruleSet?.resultClass == RuleSet.ROLE){
					def allUsers = User.findAllByUserTenantId(currentTenantId.toInteger())
					def sendToRole = Role.findByDescription(ruleSet?.resultInstance)
					def userForRole = allUsers.findAll{it.authorities*.description == ruleSet.resultInstance}
					if(userForRole.size() > 0){
						userForRole.each{ us->
							saveEmails(  entryCreatedUser, us,action,uniqueId, formName,templateText)
							/*AsynchronousEmailStorage async = new AsynchronousEmailStorage()
							async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
							async.emailTO = "${us.username}"
							async.emailSubject="New Entry for ${formName}"
							async.emailData="${'Description : '+description}<br>Dear ${us?.firstName},<br><br>You have a new form submission for your form <b>${formName}</b> by: <b>${entryCreatedUser.firstName?:''} ${entryCreatedUser.lastName?:''}</b>${entryCreatedUser.userTenantId!=0?('&lt;'+entryCreatedUser.username+'&gt;'):''}<br><br>"+templateText+"<br><br>Thank you,<br>Your Form Builder Team!"
							async.isHtml=true
							async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
							async.save()*/
						}
					}
				}else if(ruleSet?.resultClass == RuleSet.DEPARTMENT){
				    def sendToDepartment = DropDown.findByName(ruleSet?.resultInstance)
					def userForDepartment = User.createCriteria().list(){
												department{
													eq 'id',ruleDept.id
												}
											}
					if(userForDepartment.size() > 0){
						userForDepartment.each{ us->
							saveEmails(  entryCreatedUser, us,action,uniqueId, formName,templateText)
							/*AsynchronousEmailStorage async = new AsynchronousEmailStorage()
							async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
							async.emailTO = "${us.username}"
							async.emailSubject="New Entry for ${formName}"
							async.emailData="${'Description : '+description}<br>Dear ${us?.firstName},<br><br>You have a new form submission for your form <b>${formName}</b> by: <b>${entryCreatedUser.firstName?:''} ${entryCreatedUser.lastName?:''}</b>${entryCreatedUser.userTenantId!=0?('&lt;'+entryCreatedUser.username+'&gt;'):''}<br><br>"+templateText+"<br><br>Thank you,<br>Your Form Builder Team!"
							async.isHtml=true
							async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
							async.save()*/
						}
					}
				}else if (ruleSet?.resultClass == RuleSet.USER_MANAGER ){
					def userManagerInstance = entryCreatedUser?.reportsTo
					saveEmails(  entryCreatedUser, userManagerInstance,action,uniqueId, formName,templateText)
					/*AsynchronousEmailStorage async = new AsynchronousEmailStorage()
					async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
					async.emailTO = "${userManagerInstance.username}"
					async.emailSubject="New Entry for ${formName}"
					async.emailData="${'Description : '+description}<br>Dear ${userManagerInstance?.firstName},<br><br>You have a new form submission for your form <b>${formName}</b> by: <b>${entryCreatedUser.firstName?:''} ${entryCreatedUser.lastName?:''}</b>${entryCreatedUser.userTenantId!=0?('&lt;'+entryCreatedUser.username+'&gt;'):''}<br><br>"+templateText+"<br><br>Thank you,<br>Your Form Builder Team!"
					async.isHtml=true
					async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
					async.save()*/
				}else if (ruleSet?.resultClass == RuleSet.USER_DEPARTMENT ){
					def userDept = entryCreatedUser?.department
					def userForDepartment = User.createCriteria().list(){
						department{
							eq 'id',userDept.id
						}
					}
					if(userForDepartment.size() > 0){
						userForDepartment.each{ us->
							saveEmails(  entryCreatedUser, us,action,uniqueId, formName,templateText)
							/*AsynchronousEmailStorage async = new AsynchronousEmailStorage()
							async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
							async.emailTO = "${us.username}"
							async.emailSubject="New Entry for ${formName}"
							async.emailData="${'Description : '+description}<br>Dear ${us?.firstName},<br><br>You have a new form submission for your form <b>${formName}</b> by: <b>${entryCreatedUser.firstName?:''} ${entryCreatedUser.lastName?:''}</b>${entryCreatedUser.userTenantId!=0?('&lt;'+entryCreatedUser.username+'&gt;'):''}<br><br>"+templateText+"<br><br>Thank you,<br>Your Form Builder Team!"
							async.isHtml=true
							async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
							async.save()*/
						}
					}
				}
				try{
					sessionFactory.getCurrentSession().flush()
				}catch(Exception e){}
			  }
		}catch(Exception ex){
			print ex
		}
   }
   def saveEmails( def entryCreatedUser,def usrTo,def action ,def uniqueId, def formName,def templateText){
	   println "Counter"
		   AsynchronousEmailStorage async = new AsynchronousEmailStorage()
		   async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
		   async.emailTO = "${usrTo.username}"
		   async.emailSubject="${action=='create'?'New Entry':'Entry Updated'} for ${formName} ${uniqueId?('(Unique ID: '+uniqueId+')'):''}"
		   async.emailData="<span style=\"font-family: 'Helvetica Neue',Helvetica,Helvetica,Arial,sans-serif;vertical-align:top;color:#455560;font-weight:700;margin:0;font-size: 14px\">Dear ${usrTo?.firstName},<br><br>You have  ${action=='create'?'a new form submission':'an entry updated'} for your form <b>${formName}</b> by: <b>${entryCreatedUser.firstName?:''} ${entryCreatedUser.lastName?:''}</b>${entryCreatedUser.userTenantId!=0?('&lt;'+entryCreatedUser.username+'&gt;'):''}<br><br>"+templateText+"<br><br>${uniqueId?('You can search this entry with its unique-id: '+uniqueId+'<br>'+'Click <a href=\"'+ConfigurationHolder.config.grails.serverURL+'/PF/publicSearch\">here</a> to search.<br><br>'):''}Thank you,<br>Your Form Builder Team!</span>"
		   async.isHtml=true
		   async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
		   async.save(flush:true)
	   }
   def drawFormEmail(Form form,def domainInstance){
	def fields = new HashMap()
 	def display = true
	String format = 'MM/dd/yyyy'
	SimpleDateFormat dateformat = new SimpleDateFormat(format)
	def outString=new org.codehaus.groovy.grails.web.pages.FastStringWriter()
	outString<<"""<table style="border: none;">"""
	def formAdmin = FormAdmin.findByForm(form)
	def statusFieldId = null
	if('Approval'.equalsIgnoreCase(formAdmin?.formType)){
		statusFieldId = formAdmin.statusField?.id
	}
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
			outString<<"""<tr>"""
			outString<<"""<td style="padding:5px;border-bottom:1px solid #eee;"><b>${fieldName?.encodeAsHTML()}<span style="float:right;">:</span></b></td><td style="padding:5px;border-bottom:1px solid #eee;">"""
			if(isFieldCheckBox){
				outString<<"""${fieldValue?(checkValuesBoxTo(fieldValue))?.encodeAsHTML():""}"""
			}else if(isFieldDate){
				outString<<"""${fieldValue?(dateformat.format(fieldValue).toString().encodeAsHTML()):''}"""
				if(settings.timeFormat){
					outString << " "+(fieldValue?(new SimpleDateFormat(settings.timeFormat).format(fieldValue)):'')
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
				outString<<"""${fieldValue?:''}"""
			}else if("AddressField".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue.toString())
					results+=mapValue."line1"?mapValue."line1"+"; ":""
					results+=mapValue."line2"?mapValue."line2"+"; ":""
					results+=mapValue."city"?mapValue."city"+"; ":""
					results+=mapValue."state"?mapValue."state"+"; ":""
					results+=mapValue."zip"?mapValue."zip"+" ":""
					results+=mapValue."country"?mapValue."country"+" ":""
				}
				outString<<""" ${results.toString().encodeAsHTML()}"""
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
				outString<<""" ${results.toString().encodeAsHTML()}"""
			}else if("NameTypeField".equalsIgnoreCase(field.type)){
				def results=""
				if(fieldValue){
					def mapValue = grails.converters.JSON.parse(fieldValue)
					results+=mapValue."pre"?mapValue."pre"+" ":""
					results+=mapValue."fn"?mapValue."fn"+" ":""
					results+=mapValue."mn"?mapValue."mn"+" ":""
					results+=mapValue."ln"?mapValue."ln"+" ":""
					
				}
				outString<<""" ${results.toString().encodeAsHTML()}"""
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
				outString<<""" ${results.toString().encodeAsHTML()}"""
			}else{
				outString<<"""${fieldValue?(fieldValue.toString().encodeAsHTML()):''}"""
			}
			outString<<"""</td>"""
			outString<<"""</tr>"""
		}
	}
	outString<< """</table>"""
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
return outString.toString()
	   
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