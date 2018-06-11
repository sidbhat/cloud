
package com.oneapp.cloud.core

import  com.oneapp.cloud.security.*

import grails.converters.JSON;
import grails.orm.HibernateCriteriaBuilder;
import grails.plugins.springsecurity.Secured
import groovy.sql.Sql;

import com.oneapp.cloud.core.social.*
import org.grails.formbuilder.*
import org.grails.taggable.*
import org.hibernate.criterion.CriteriaSpecification;

@grails.plugins.springsecurity.Secured("ROLE_USER")
class DashboardController {
	
	def springSecurityService
	def socialFeedService
	def sessionFactory
	def domainClassService
	def inboxReaderService
	def sqlDomainClassService
	def dataSource
	
	def index = {
		if(!request['isMobile']){
			return [filterTag:params.filterTag]
		}
			//def groupList = GroupDetails.list()
			def user1 = springSecurityService.currentUser
			//def userList = User.findAllByUserTenantIdAndEnabled(user1?.userTenantId,true)
			def currentUserRoles = user1.authorities*.id.get(0)
			//def roleList = Role.list()
			//def deptList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT) 
			ActivityFeedController activityFeed = new ActivityFeedController()
			params.order = 'desc'
			//UserProfile up = UserProfile.findByUser(user1)
	        def max = 10
//	        if ( up?.numOfRows )
//	        	max = up?.numOfRows 
	        params.max = Math.min(params.max ? params.max.toInteger() : max , 100)
	      	def allForm
			if (params.viewName != null)
	       		 session["feed.view"] = params.viewName
			if ( session["feed.view"] == null || session["feed.view"]  == "company_feed" ) {
				def activityFeedList
				def activityFeedListCnt
				def totalCompFeeds
				params.sort = 'lastUpdated'
				if ( params.offset == null )
					params.offset = 0
				else
					params.offset = Integer.parseInt(params.offset)
					
				
				if ( params.id == null ){
						activityFeedList = ActivityFeed.createCriteria().listDistinct(){
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
					}
					// Code to check if a user does not belong to a group the company activity is not shown to the user
					totalCompFeeds = activityFeedList.size()
					def maxSize = (params.offset+params.max.toInteger())>activityFeedList.size() ? activityFeedList.size() : (params.offset+params.max.toInteger());
					activityFeedList = activityFeedList?.sort{it.lastUpdated}.reverse()
					activityFeedList = activityFeedList.subList (params.offset,maxSize)
				}else{
					if ( params.filter == null ){
					
						def cnt=0
						def newList = new ArrayList()
						def tag
						def tagRef
						if(params.id == "recentTask"){
							activityFeedList = activityFeed.findRecentTaskList(user1)
						}else{
							tag = Tag.findByName(params.id)
							if(tag){
								tagRef = TagLink?.findAllByTag(tag)?.tagRef
								activityFeedList = ActivityFeed.findAllByIdInList(tagRef)
						   }else
							   activityFeedList = newList
						}
						activityFeedListCnt = activityFeedList
						activityFeedList.each{
							if(it.visibility == ActivityFeed.COMPANY)
							{
								newList?.add(it)
							}
							else{
								def groupUserList =  it.sharedGroups.find{g -> (g.user.find{u -> u.id == user1.id} != null)}
								if (groupUserList != null || it.createdBy.id == user1.id)
									 newList?.add(it)
								 else if(it.sharedUsers*.id.contains(user1.id) || it.createdBy.id == user1.id)
										newList?.add(it)
								else if(it.sharedRoles*.id.contains(currentUserRoles) || it.createdBy.id == user1.id)
										newList?.add(it)
								else if(it.sharedDepts*.id.contains(user1.department?.id) || it.createdBy.id == user1.id)
										newList?.add(it)
								else
									 cnt++
							}
						}
						
						activityFeedList = newList
						def maxSize = (params.offset+params.max.toInteger())>activityFeedList.size() ? activityFeedList.size() : (params.offset+params.max.toInteger());
						activityFeedList = activityFeedList?.sort{it.lastUpdated}.reverse()
						activityFeedList = activityFeedList.subList (params.offset,maxSize)
						totalCompFeeds = activityFeedListCnt.size() - cnt
				    }else{
						activityFeedList=[ActivityFeed.get(params.id)]
						def maxSize = (params.offset+params.max.toInteger())>activityFeedListCnt.size() ? activityFeedListCnt.size() : (params.offset+params.max.toInteger());
						activityFeedListCnt=activityFeedList?.size()
						activityFeedList = activityFeedList?.sort{it.lastUpdated}.reverse()
						activityFeedList = activityFeedList.subList (params.offset,maxSize)
						totalCompFeeds=activityFeedListCnt
					}
				}
				session["feed.view"] = "company_feed"
				render view:'index', model: [viewName : "Activity Feed",totalCompFeeds:totalCompFeeds,activityFeedList:activityFeedList,tagFilter:params.id,allForms:allForm]   
			}else{
			
				def activityFeedList
				def totalFeeds
				params.sort = 'feedDate'
				if ( params.id == null ){
					activityFeedList = ActivityFeed.findAllByUser(springSecurityService.currentUser.username,params)
		    	 	totalFeeds = ActivityFeed.findAllByUser(springSecurityService.currentUser.username).size()
		    	}else{
		    		if ( params.filter == null ){
						activityFeedList = ActivityFeed.findAllByUserAndSource(springSecurityService.currentUser.username,params.id,params)
		    		 	totalFeeds = ActivityFeed.findAllByUserAndSource(springSecurityService.currentUser.username, params.id).size()
		    		}else{
						activityFeedList = [ActivityFeed.get(params.id)]
		    		 	totalFeeds = activityFeedList?.size()
		    		}
		    
		    	}
		    	session["feed.view"] = "activity_feed"
		    	render view:'index', model: [viewName :"Social Feed",userList:userList,activityFeedList:activityFeedList,totalFeeds:totalFeeds,groupList:groupList,allForms:allForm,roleList:roleList,deptList:deptList]   

			}
	}
	
	def activityFeedView = {
	
		//def groupList = GroupDetails.list()
		def user1 = springSecurityService.currentUser
		//def userList = User.findAllByUserTenantIdAndEnabled(user?.userTenantId,true)
		def currentUserRoles = user1.authorities*.id.get(0)
		//def roleList = Role.list()
		//def deptList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT) 
		def currentUserobj = User.get(user1.id)
		params.order = 'desc'
//		UserProfile up = UserProfile.findByUser(user)
		ActivityFeedController activityFeed = new ActivityFeedController()
        def max = 10
//        if ( up?.numOfRows )
//        	max = up?.numOfRows 
        params.max = Math.min(params.max ? params.max.toInteger() : max , 100)
      	def allForm
		if (params.viewName != null)
       		 session["feed.view"] = params.viewName
		if ( session["feed.view"] == null || session["feed.view"]  == "company_feed" ) {
			if(request.xhr){
				def activityFeedList
				def activityFeedListCnt
				def totalCompFeeds
				params.sort = 'lastUpdated'
				if ( params.offset == null )
					params.offset = 0
				else
					params.offset = Integer.parseInt(params.offset)
					
				if ( params.id == null ){
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
						projections{
							distinct("id")
							order("lastUpdated","desc")
						}
					 }
					// Code to check if a user does not belong to a group the company activity is not shown to the user
					totalCompFeeds = activityIdList.size()
					if(activityIdList){
						def maxSize = (params.offset+params.max.toInteger())>activityIdList.size() ? activityIdList.size() : (params.offset+params.max.toInteger());
	//					activityFeedList = activityFeedList?.sort{it.lastUpdated}.reverse()
						params.offset = (maxSize>params.offset)?params.offset:0
						activityIdList = activityIdList.subList (params.offset,maxSize)
						activityFeedList = ActivityFeed.createCriteria().list(){
							"in" "id",activityIdList
							order("lastUpdated","desc")
						}
					}
				}else{
					if ( params.filter == null ){
						
						def cnt=0
						def newList = new ArrayList()
						def tag
						def tagRef
						if(params.id == "recentTask"){
							activityFeedList = activityFeed.findRecentTaskList(user1)
						}else{
							tag = Tag.findByName(params.id)
							if(tag){
								tagRef = TagLink?.findAllByTag(tag)?.tagRef
								activityFeedList = ActivityFeed.findAllByIdInList(tagRef)
						   }else
							   activityFeedList = newList
						}
						
						
						activityFeedListCnt = activityFeedList
						
						activityFeedList.each{
							if(it.visibility == ActivityFeed.COMPANY)
							{
								newList?.add(it)
							}
							else{
								def groupUserList =  it.sharedGroups.find{g -> (g.user.find{u -> u.id == user1.id} != null)}
								if (groupUserList != null || it.createdBy.id == user1.id)
									 newList?.add(it)
								 else if(it.sharedUsers*.id.contains(user1.id) || it.createdBy.id == user1.id)
										newList?.add(it)
								else if(it.sharedRoles*.id.contains(currentUserRoles) || it.createdBy.id == user1.id)
										newList?.add(it)
								else if(it.sharedDepts*.id.contains(user1.department?.id) || it.createdBy.id == user1.id)
										newList?.add(it)
								else
									 cnt++
							}
						}
						
						activityFeedList = newList
						def maxSize = (params.offset+params.max.toInteger())>activityFeedList.size() ? activityFeedList.size() : (params.offset+params.max.toInteger());
						activityFeedList = activityFeedList?.sort{it.lastUpdated}.reverse()
						activityFeedList = activityFeedList.subList (params.offset,maxSize)
						totalCompFeeds = activityFeedListCnt.size() - cnt
				    }else{
						activityFeedList=[ActivityFeed.get(params.id)]
						def maxSize = (params.offset+params.max.toInteger())>activityFeedListCnt.size() ? activityFeedListCnt.size() : (params.offset+params.max.toInteger());
						activityFeedListCnt=activityFeedList?.size()
						activityFeedList = activityFeedList?.sort{it.lastUpdated}.reverse()
						activityFeedList = activityFeedList.subList (params.offset,maxSize)
						totalCompFeeds=activityFeedListCnt
					}
				
				}
				def taskCount  = activityFeed.findTaskCount(user1)
				def recentTaskCount  = activityFeed.findRecentTaskCount(user1)
				def activityFeedViewContent = [feedList:[],total:totalCompFeeds,pagination:params.offset,taskCount:taskCount[0],recentTaskCount:recentTaskCount[0]]
				activityFeedList.each{af->
					activityFeedViewContent.feedList << [html:"${g.render(template:'/activityFeed/activityFeed', model:[activityFeed:af,user:currentUserobj])}",id:af.id]
				}
				render activityFeedViewContent as JSON
				return
			}else{
				redirect(controller:'dashboard',action:'index')
			}
		}else{
		
			def activityFeedList
			def totalFeeds
			params.sort = 'feedDate'
			if ( params.id == null ){
				activityFeedList = ActivityFeed.findAllByUser(springSecurityService.currentUser.username,params)
	    	 	totalFeeds = ActivityFeed.findAllByUser(springSecurityService.currentUser.username).size()
	    	}else{
	    		if ( params.filter == null ){
					activityFeedList = ActivityFeed.findAllByUserAndSource(springSecurityService.currentUser.username,params.id,params)
	    		 	totalFeeds = ActivityFeed.findAllByUserAndSource(springSecurityService.currentUser.username, params.id).size()
	    		}else{
					activityFeedList = [ActivityFeed.get(params.id)]
	    		 	totalFeeds = activityFeedList?.size()
	    		}
	    
	    	}
	    	render view:'index', model: [viewName :"Social Feed"]   

		}
	}
	
	def changeStatusDevice = {
			def status = params.status
			def activityId = params.activityId
			def success = false
			try{
				def activityFeedInstance = ActivityFeed.get(activityId)
				def strConfigName = activityFeedInstance.config.configName
				strConfigName= strConfigName.substring(0,strConfigName.indexOf("."))
				
				//def domainClass = DomainClass.findByName(activityFeedInstance.config.configName)
				def formObj = Form.findByName(strConfigName)
				def fieldName = FormAdmin.findByForm(formObj).statusField
				def domainInstance = sqlDomainClassService.get(activityFeedInstance.shareId, formObj)
				/*def formInstanceClass = grailsApplication.getDomainClass(activityFeedInstance.config.configName)
				if(!formInstanceClass || domainClass.updated){
					domainClassService.reloadUpdatedDomainClasses()
					formInstanceClass = grailsApplication.getDomainClass(it?.config?.configName)
				}
				def formInstance = formInstanceClass?.clazz?.createCriteria().get{
					eq 'id',activityFeedInstance.shareId
				  }*/
				domainInstance."${fieldName.name}" = status
				domainInstance.updatedBy = springSecurityService.currentUser
				if (!domainInstance.errors && sqlDomainClassService.update(domainInstance,formObj)) {
					def fieldInstance = JSON.parse(fieldName.settings)
					def fieldValueList = fieldInstance.en.value
					fieldValueList.each {
						activityFeedInstance.removeTag(it)
					}
					activityFeedInstance.addTag(status)
					flash.message = "Form status updated to ${status}"
					flash.defaultMessage = flash.message
					redirect(controller:'dashboard', action:'index')
				}
			}catch(Exception e){
				flash.message = "Error in updating status"
				flash.defaultMessage = flash.message
				redirect(controller:'dashboard', action:'index')
			}
			
		}
	
	
	
	def activityFeedDetails = {
		
		def activityFeed =ActivityFeed.findById(params.id);
		render template:'activityFeedDetails', model: [activityFeed:activityFeed]
	}
	
	def addcomment = {
		def commentid="commentText"+params['activity_id']
		def activityFeed =ActivityFeed.findById(params['activity_id']);
		def user=springSecurityService.currentUser;
		
		if(params[commentid]==null || ((String)(params[commentid])).trim()==""){
			render "<span style='color:#f00'>Please enter a comment<span>"
			return
		}
		activityFeed.addComment(user,params[commentid]);
		activityFeed.save(flush: true)
		render "Comment added."
		
		//redirect(action:index)
	}
	
	def formBuilderResponse = {
		def formResponseId = params.id;
		return [formResponseId:formResponseId]
	}
	
	def mobileShare = {
		def groupList = GroupDetails.list()
		def user = springSecurityService.currentUser
		def userList = User.findAllByUserTenantId(user?.userTenantId)
		def roleList = Role.list()
		def deptList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT) 
		render template:'/dashboard/mobileShare', model: [userList:userList,groupList:groupList,roleList:roleList,deptList:deptList]
	}
	
	def fetchEmail = {
		if(request.xhr){
			def ruleSet = RuleSet.findAllByStatus(RuleSet.ACTIVE,[sort:'_order', order:'asc'])
			def emailRule = false
			def data
			ruleSet.each{
				if ( it._action == "Fetch Email" ){
					def rules = Rule.findAllByRuleSetAndStatus(it,  "Active",[sort:'_order', order:'asc'])
					if(rules){
						emailRule = true
						try{
							inboxReaderService.fetchEmailForAccount(rules,it)
						}catch(Exception ex){
							data = [message:"Error in fetching email"]
							render data as JSON
							//redirect(controller:'dashboard', action:'index')
						}
					}
				}
			}
			if(!emailRule){
				data = [message:"No rules defined to fetch email"]
				render data as JSON
				//redirect(controller:'dashboard', action:'index')
			}else{
				data = [message:"Please refresh to view fetched emails"]
				render data as JSON
				//redirect(controller:'dashboard', action:'index')
			}
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
	def afterInterceptor = { model, modelAndView ->
		if (request['isMobile'] && modelAndView != null ) {
			modelAndView.viewName = modelAndView.viewName + "_m"
	}
  }

}
