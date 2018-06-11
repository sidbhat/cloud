package com.oneapp.cloud.core

import com.oneapp.cloud.time.Task
import com.oneapp.cloud.social.*
import com.oneapp.cloud.crm.*
import com.oneapp.cloud.core.*
//import org.codehaus.groovy.grails.plugins.orm.auditable.AuditLogEvent
import org.codehaus.groovy.grails.commons.GrailsApplication
import grails.plugin.multitenant.core.util.TenantUtils

class DailyUpdateService {
  def mailService
  def sessionFactory
  static transactional = false
  def grailsApplication
  
  def sendDailyDigestMail() {
	
    User.list().each { user ->
     if ( user.toString().equals("admin@yourdomain.com") ) //&& (oppList?.size()!= 0 && compList?.size()!= 0 && newUserList?.size()!= 0 &&  taskDueToday?.size()!= 0 && taskChangeList?.size()!= 0 && projectChangeList?.size()!= 0 ) ) 
	 { // change this
      mailService.sendMail {
		  from grailsApplication.config.grails.dailyMailer.emailFrom
        to "admin@yourdomain.com"
        subject "[Form Builder Daily Digest]"
        body(view: "/email/template", model: [user:User.findByUsername("admin@yourdomain.com")])
      }
      }
    }
  }
  
  def sendDailyReportToUser(clientId){
	  def userList = User.findAllByUserTenantId(clientId);
	  userList.each { user ->
	  
	  	  // Check to see if user profile has subscribed to email
	  	  boolean emailSubscribed=false
	  	  UserProfile up = UserProfile.findByUser(user)
	  	  if ( up == null )
	  	  	emailSubscribed = true // if no profile exists then by default email is subscribed to
	  	  else 
	  	  	emailSubscribed = up.emailSubscribed
	  	  	
	  	  if ( emailSubscribed ) {	
		  def currentUserRoles = user.authorities
		  if(!user?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN) && user?.authorities?.authority && user.enabled)
		  {
			def activityFeedList
			def bdayUsers
			def approvalCount = 0
			def surveypollCount = 0
			def subjectBody = ""
			ActivityFeedController activityFeedCont = new ActivityFeedController()
			def taskList  = activityFeedCont.findTaskList(user)
			def recentTask  = activityFeedCont.findRecentTaskList(user)
		  	TenantUtils.doWithTenant((int)user.userTenantId) {
				 activityFeedList = ActivityFeed.withCriteria()
				 {
					def now = new Date()
					between( 'dateCreated' , now-1, now)
				}
				def newList = new ArrayList()
				activityFeedList.each{
					if(it.visibility == ActivityFeed.COMPANY)
					{
						newList?.add(it)
					}
					else{
						def groupUserList =  it.sharedGroups.find{g -> (g.user.find{u -> u.id == user.id} != null)}
						if (groupUserList != null || it.createdBy.id == user.id)
							 newList?.add(it)
						 else if(it.sharedUsers*.id.contains(user.id) || it.createdBy.id == user.id)
								newList?.add(it)
						else if(it.sharedRoles.retainAll(currentUserRoles) || it.createdBy.id == user.id)
								newList?.add(it)
						else if(it.sharedDepts*.id.contains(user.department?.id) || it.createdBy.id == user.id)
								newList?.add(it)
					}
				}
				activityFeedList = newList
				activityFeedList.each{
					if(it.config.shareType == 'Approval'){
						approvalCount++;
					}else if('Poll'.equalsIgnoreCase(it?.config?.shareType) || 'Survey'.equalsIgnoreCase(it?.config?.shareType)){
						  surveypollCount++;
					}
				}
				if(user?.userTenantId == 1){
					bdayUsers = []
				}else{
					def session = sessionFactory.currentSession
					Calendar cal = Calendar.getInstance();
					int month = cal.get(Calendar.MONTH)+1
					int dayOfMonth = cal.get(Calendar.DAY_OF_MONTH)
					def q = "select person.first_name, person.last_name from person inner join user on (person.id = user.id) where person.dob like '%-"+(month<10?'0':'')+month+"-"+(dayOfMonth<10?'0':'')+dayOfMonth+"%' and user.enabled=TRUE and user.user_tenant_id ="+user.userTenantId;
					bdayUsers = session.createSQLQuery( q ).list()
				}
				if(activityFeedList){
					subjectBody += "[${activityFeedList.size()}] Feeds"
				}
				if(approvalCount > 0){
					if(subjectBody != "")
						subjectBody += ", "
					subjectBody += "[${approvalCount}] Approvals"
				}
				if(surveypollCount > 0){
					if(subjectBody != "")
						subjectBody += ", "
					subjectBody += "[${surveypollCount}] Surveys & Polls"
				}
				if(taskList.size() > 0){
					if(subjectBody != "")
						subjectBody += ", "
					subjectBody += "[${taskList.size()}] Tasks"
				}
				if(recentTask.size() > 0){
					if(subjectBody != "")
						subjectBody += ", "
					subjectBody += "[${recentTask.size()}] Recent Tasks"
				}
				if(bdayUsers){
					if(subjectBody != "")
						subjectBody += ", "
					subjectBody += "[${bdayUsers.size()}] Birthdays"
				}
				if(subjectBody != ""){
					subjectBody += " on Form Builder"
				}
				
			}//end do with tenant 
			  if(activityFeedList.size() > 0 || bdayUsers.size() > 0){
			     mailService.sendMail{
					 from grailsApplication.config.grails.dailyMailer.emailFrom
					  to user?.username
					  subject subjectBody
					  body(view:"/email/userMailTemplate", model:[userList:userList,userFirstName:user.firstName,activityFeedList:activityFeedList,approvalCount:approvalCount,surveypollCount:surveypollCount,taskList:taskList,recentTask:recentTask,bdayUsers:bdayUsers])
				 }
			  }
		   }
		  }//end of email subscribed
	  	}
  	}

}
