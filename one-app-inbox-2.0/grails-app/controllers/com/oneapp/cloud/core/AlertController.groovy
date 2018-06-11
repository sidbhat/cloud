

package com.oneapp.cloud.core

import java.sql.Timestamp;

import grails.converters.JSON
import groovy.sql.Sql;

class AlertController {
	
		def index = {  }
		def springSecurityService
		def sessionRegistry
		def dataSource
		def sessionFactory
		
		// Get total number of inbox feeds
		def alert = {
			if(springSecurityService.isLoggedIn()){
				def totalCompFeeds
				def newActivityFeeds
				def lastViewed
				User user1 = springSecurityService?.currentUser
				if(!user1.lastViewed){
					user1.lastViewed = new Date()
				}
				Calendar c = Calendar.getInstance()
				c.setTime(user1.lastViewed)
				c.add(Calendar.MINUTE,-1)
				lastViewed = c.getTime()
				def currentUserRoles = user1.authorities*.id.get(0)
				totalCompFeeds = ActivityFeed.withCriteria(){
					or{
						eq "visibility",ActivityFeed.COMPANY
						sharedGroups{
							user{
								eq "id",user1.id
							}
						}
						sharedUsers{
							eq "id",user1.id
						}
						sharedRoles{
							eq "id",currentUserRoles
						}
						if(user1.department){
							sharedDepts{
								eq "id",user1.department.id
							}
						}
						createdBy{
							eq "id",user1.id
						}
						
					}
					projections{
						countDistinct("id")
					}
				
				 }
			
				def activityIdList = ActivityFeed.withCriteria(){
					or{
						eq "visibility",ActivityFeed.COMPANY
						sharedGroups{
							user{
								eq "id",user1.id
							}
						}
						sharedUsers{
							eq "id",user1.id
						}
						sharedRoles{
							eq "id",currentUserRoles
						}
						if(user1.department){
							sharedDepts{
								eq "id",user1.department.id
							}
						}
						createdBy{
							eq "id",user1.id
						}
						
					}
					gt "lastUpdated",lastViewed
					projections{
						distinct("id")
						order("lastUpdated","desc")
					}
				 }
				ActivityFeedController activityFeedCont = new ActivityFeedController()
				def taskCount  = activityFeedCont.findTaskCount(user1)
				def recentTaskCount  = activityFeedCont.findRecentTaskCount(user1)
				def activityFeedViewContent = [feedList:[],total:totalCompFeeds[0],commentList:[],taskCount:taskCount[0],recentTaskCount:recentTaskCount[0]]
				if(activityIdList){
					newActivityFeeds = ActivityFeed.createCriteria().list(){
						"in" "id",activityIdList
						order("lastUpdated","desc")
					}
					newActivityFeeds.each{af->
						activityFeedViewContent.feedList << [html:"${g.render(template:'/activityFeed/activityFeed', model:[activityFeed:af,user:user1,nocomment:true])}",id:af.id, totalFeedComents:af.comments.size()]
						def commentList = af.comments.findAll{it.dateCreated > lastViewed}
						if(commentList){
							commentList.each{comment->
								activityFeedViewContent.commentList << [html:"${feedComment.renderComment(commentsInstance:comment, activityFeed:af)}",commentId:comment.id, feedId: af.id]
							}
						}
					}
				}
				try{
					user1.lastViewed = new Date()
					user1.save()
				}catch(Exception ex){}
				 render activityFeedViewContent as JSON
			}else{
			    def data = []
				render data as JSON
			}
		}
     	//Get logged in users
     	def loggedInUsers = {
			 if(springSecurityService.isLoggedIn()){
				 def loggedInUsers= new String()
				 User currentUser = springSecurityService?.currentUser
				 def listIdUsers = []
				 def userProfile = UserProfile.findByUser(currentUser)
				 loggedInUsers +=  "<li><ul id='onlineStatus${currentUser?.id}' name='onlineStatus' class='dropdown' style='position:absolute;font-size:70%;left:122px;margin-top:4px;'><li><a href='#' id='statusIcon'>"
				 if(userProfile?.isOnline){
					 loggedInUsers +=  "<img src='${resource(dir:'images',file:'bullet_green.png')}'/></a><ul style='padding-left: 0px;' class='sub_menu'><li class=''><a href='javascript:changeUserChatStatus(\"offline\")'>Go Offline</a></li></ul></li></ul>"
				 }else{
					 loggedInUsers +=  "<img src='${resource(dir:'images',file:'offline-status6.png')}'/></a><ul style='padding-left: 0px;' class='sub_menu'><li class=''><a href='javascript:changeUserChatStatus(\"online\")'>Go Online</a></li></ul></li></ul>"
				 }
				 if ( currentUser?.pictureURL != null && currentUser?.pictureURL.length() != 0 ) {
					 loggedInUsers += "<img src='${currentUser?.pictureURL}' width='24' height='24'/>"
				 }else{
					 loggedInUsers += "<img src='${resource(dir:'images',file:'user_32.png')}' width='24' height='24'/>"
				 }
				 loggedInUsers += "<span style='vertical-align:super;font-size:11px;margin-left:5px;color:#666;font-weight:bold;'>&nbsp;${currentUser?.firstName}</span>"
				 loggedInUsers += ""
				 if(!currentUser.authorities*.authority?.contains(Role.ROLE_TRIAL_USER)){
					 sessionRegistry?.getAllPrincipals().each{
						 def user =  User.findByUsername(it.username)
						 def sessionUserProfile = UserProfile.findByUser(user)
						 if (user.userTenantId == currentUser?.userTenantId && !listIdUsers.contains(user.id) && user.id != currentUser?.id && sessionUserProfile?.isOnline){
							 loggedInUsers +=  "<li><a href='javascript:void(0)' style='padding:0;' onclick='chatWith(\"${user.firstName}\",\"${user.username}\")'><span id='onlineStatus${user.id}' name='onlineStatus' style='margin-top:5px;padding-right:10px;float:right;width:5px;'><img src='${resource(dir:'images',file:'bullet_green.png')}'/></span>"
							 if ( user?.pictureURL != null && user?.pictureURL.length() != 0 ) {
								 loggedInUsers += "<img src='${user?.pictureURL}' width='24' height='24'/>"
							 }else{
								 loggedInUsers += "<img src='${resource(dir:'images',file:'user_32.png')}' width='24' height='24'/>"
							 }
						  loggedInUsers += "<span style='vertical-align:super;font-size:11px;margin-left:5px;'>&nbsp;${user.firstName}</span></a></li>"
						  listIdUsers << user.id
						  }
					 }
				 }
				 render loggedInUsers
			 }else{
				 def data = []
				 render data as JSON
			 }
     	}
     	
     	// Get total number of feeds changed since last user login
     	def updates = {
			 if(springSecurityService.isLoggedIn()){
				 def totalCompFeedList
				 def totalCompanyFeed = 0
				 def user1 = User.get(springSecurityService?.currentUser.id)
				 def lastActivity = user1.lastActivity
				 if(!lastActivity)
					 lastActivity = new Date()
				 def currentUserRoles = user1.authorities*.id.get(0)
				 totalCompFeedList = ActivityFeed.withCriteria(){
					 or{
						 eq "visibility",ActivityFeed.COMPANY
						 sharedGroups{
							 user{
								 eq "id",user1.id
							 }
						 }
						 sharedUsers{
							 eq "id",user1.id
						 }
						 sharedRoles{
							 eq "id",currentUserRoles
						 }
						 if(user1.department){
							 sharedDepts{
								 eq "id",user1.department.id
							 }
						 }
						 createdBy{
							 eq "id",user1.id
						 }
					 }
					 projections{
						 distinct("id")
					 }
				 }
				 if(totalCompFeedList){
					 def activityNotification = ActivityNotification.createCriteria().listDistinct(){
						 actionOnFeed{
							 'in' "id",totalCompFeedList
						 }
						  gt('actionTime', lastActivity)
						  ne('actionBy.id',user1.id)
					 }
					 totalCompanyFeed = activityNotification.size()
				 }
				 render "${totalCompanyFeed}"
			 }else{
				 def data = []
				 render data as JSON
			 }
     	}
		 
		
		 
		 def getChatMessage = {
			 if(springSecurityService.isLoggedIn()){
				 try{
					 def toUser = springSecurityService.currentUser
					 def messageInstance = Message.findAllByRecvdAndToUser(false,toUser)
					 def fromList = [firstName:[],userName:[]];
					 def sendMessage = true
					 for(int i=0; i<messageInstance.size; i++)
					 {
						 def fromId = messageInstance[i].fromUser
						 fromList.firstName << fromId.firstName
						 fromList.userName << fromId.username
						 try{
							 def messageId = Message.get( messageInstance[i].id)
							 if(messageId.version > messageInstance[i].version){
								 sendMessage = false
							 }else{
								 messageId.recvd = true
								 messageId.save(flush:true)
							 }
						 }catch(Exception ex){}
					 }
					 if(sendMessage){
						 def data=
						 [
							 status:true,
							 items:messageInstance,
							 from:fromList
						 ]
						 render data as JSON
					 }
				 }finally{
					 def session = sessionFactory.currentSession
					 session.flush()
					 session.clear()
				 }
			 }else{
				 def data = []
				 render data as JSON
			 }
		 }
    
}