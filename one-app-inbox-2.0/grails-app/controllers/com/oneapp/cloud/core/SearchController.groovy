

package com.oneapp.cloud.core

import grails.converters.JSON

import org.grails.comments.Comment;
import org.grails.comments.CommentLink;
import org.grails.formbuilder.*
/***
Spotlight search controller used to query across multiple objects in the system
***/
class SearchController {

	def socialFeedService
	public static final String TASK_PREFIX = "[Task] - "
	public static final String COMP_FEED_PREFIX = "[Company Feed] - "
	public static final String USER_PREFIX = "[User] - "
	public static final String FORM_PREFIX = "[Form] - "
	public static final String ACCOUNT_PREFIX = "[Account] - "
	public static final String CONTACT_PREFIX = "[Contact] - "
	def springSecurityService
	
    def search = {
		if(request.xhr){
	        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
	
			def me = User.get(springSecurityService.currentUser.id)
	
			if ( me == null )
				return
				
	        def ur = UserRole.findByUser(me)
	        def pjsonList
	        def list
			def l
			def ujsonList,user_list
			def form_list,fjsonList
			def group_list,gjsonList
			def activityFeed_list,activityFeedjsonList
			def activityComment_list
			def comment_list,commentjsonList
			def task_list,taskjsonList
			//Users(ones in the user tenant), Tasks(ones assignedTo or created by the user) and Expenses(only ones created by the user)
			if(ur.role.authority == Role.ROLE_SUPER_ADMIN){
				user_list = User.findAll("from User u where u.enabled=true and ( lower(u.firstName) like lower('%${params.query}%') or lower(u.username) like lower('%${params.query}%') or lower(u.lastName) like lower('%${params.query}%') or lower(u.department) like lower('%${params.query}%'))", [max: params.max]);//AndUserTenantId,"1")
			}else if(ur.role.authority == Role.ROLE_TRIAL_USER){
				user_list = User.findAll("from User u where  u.userTenantId = ${me.userTenantId} and u.enabled=true and u.id = ${me.id} and ( lower(u.firstName) like lower('%${params.query}%')))", [max: params.max]);//AndUserTenantId,"1")
			}else {
				user_list = User.findAll("from User u where  u.userTenantId = ${me.userTenantId} and u.enabled=true and (lower(u.firstName) like lower('%${params.query}%') or lower(u.username) like lower('%${params.query}%') or lower(u.lastName) like lower('%${params.query}%') or lower(u.department) like lower('%${params.query}%'))", [max: params.max]);//AndUserTenantId,"1")
			}
			
			try{
	//      		form_list = Form.findAll("from Form as f where  f.tenantId = ${me.userTenantId} and ( f.formCat='M' or f.formCat='N')");
				def formAdmins = FormAdmin.createCriteria().list(){
					form{
						eq "tenantId",me.userTenantId
						or{
							eq "formCat","M"
							eq "formCat","N"
						}
					}
					eq "published",true
				}
				if(formAdmins){
					form_list = formAdmins.form
				}else{
					form_list=[]
				}
	      	}catch( Exception ex ){
	      		log.error ex	
	      	}
			  
			  group_list = GroupDetails.findAll("from GroupDetails g where g.tenantId = ${me.userTenantId} and (lower(g.groupDescription) like lower('%${params.query}%') or lower(g.groupName) like lower('%${params.query}%'))")
			  
	      	 // task_list = Task.findAll("from Task as t where  t.tenantId = ${session.user.userTenantId} and t.active=true and lower(t.name) like lower('%${params.query}%') and (t.personal=false or t.assignedTo.id = ${me.id})", [max: params.max])
	        //exp_list = Expense.findAllByDescriptionIlikeAndCreatedBy("%${params.query}%", me, params)
			//println form_list
	        ujsonList = user_list.collect {
				if(it.username != 'publicuser@yourdomain.com'){
				  [id: it.id, name: "[User] - " + it.username] 
				}
			}
			if(ujsonList.size() > 0){
				def ujsonListRemove = ujsonList.findAll{it == null}
				ujsonList.removeAll(ujsonListRemove)
			}
	       
	        fjsonList = form_list.collect {
				  def formLabel = it.toString()
				  if(ur.role.authority == Role.ROLE_TRIAL_USER){
					  if ( formLabel?.toLowerCase()?.indexOf(params.query?.toLowerCase()) != -1 && it.createdBy.id == me.id) {
						  [id: it.id, name: "[Form] - " + formLabel]
					 }
				  }else{
					  if ( formLabel?.toLowerCase()?.indexOf(params.query?.toLowerCase()) != -1) {
						   [id: it.id, name: "[Form] - " + formLabel]
					  }
				  }
	        }
			
			if(fjsonList.size() > 0){
				def fjsonListToRemove = fjsonList.findAll{it == null}
				fjsonList.removeAll(fjsonListToRemove)
			}
			
			
			gjsonList = group_list.collect {
				def groupName 
				if ( it.groupDescription == null || it.groupDescription.length() == 0)
					groupName = it.groupName
				else 
					groupName = it.groupDescription
				[id: it.id, name: "[Group] - " + it.groupDescription]
			}
	        //tjsonList = task_list.collect { [id: it.id, name: "${TASK_PREFIX}" + it.name] }
	          // Search in task notes
	       /* def notes = Task.list().each{
	        	it.comments.each{p->
	        		if ( p.body.contains(params.query.toLowerCase()) )
	        			tjsonList.add( [ id: it.id, name: "${TASK_PREFIX}" + it.name] )
	        	}
	        }
			
	        account_list = Account.findAll("from Account as o where  o.tenantId = ${session.user.userTenantId} and  lower(o.name) like lower('%${params.query}%')",[max:params.max])
			ajsonList = account_list.collect { [id: it.id, name: "${ACCOUNT_PREFIX}" + it.name] }
			
			contact_list = ContactDetails.findAll("from ContactDetails as o where  o.tenantId = ${session.user.userTenantId} and lower(o.contactName) like lower('%${params.query}%')",[max:params.max])
			cjsonList = contact_list.collect { [id: it.id, name: "${CONTACT_PREFIX}" + it.contactName] }
	       */        
			def currentUserRoles =  me.authorities*.id.get(0)
			activityFeed_list = ActivityFeed.withCriteria(){
				or{
					eq "visibility",ActivityFeed.COMPANY
					sharedGroups{
						user{
							eq "id",me.id
						}
					}
					sharedUsers{
						eq "id",me.id
					}
					sharedRoles{
						eq "id",currentUserRoles
					}
					if(me.department){
						sharedDepts{
							eq "id",me.department.id
						}
					}
					createdBy{
						eq "id",me.id
					}
				}
				like("activityContent", "%${params.query}%")
			}
			
			activityFeedjsonList = activityFeed_list.collect {
				  def feedMessage = it.activityContent
				  if(feedMessage.size() > 20){
					  feedMessage = feedMessage.substring(0,20)+".."
				  }
				  [id: it.id, name: "[Feed] - " + feedMessage]
			}
			
			task_list = ActivityFeed.findAllByReferenceId("${params.query}")
			taskjsonList = task_list.collect {
				[id: it.id, name: "[Task] - " + it.referenceId]
		  }
			
			activityComment_list = ActivityFeed.withCriteria(){
				or{
					eq "visibility",ActivityFeed.COMPANY
					sharedGroups{
						user{
							eq "id",me.id
						}
					}
					sharedUsers{
						eq "id",me.id
					}
					sharedRoles{
						eq "id",currentUserRoles
					}
					if(me.department){
						sharedDepts{
							eq "id",me.department.id
						}
					}
					createdBy{
						eq "id",me.id
					}
				}
				projections {
					property("id")
				}
			}
			try{
				if(activityComment_list){
					comment_list = CommentLink.withCriteria(){
						'in' "commentRef",activityComment_list
						comment{
							like("body","%${params.query}%")
						}
					}
				}
			}catch(Exception e){}
	        
			commentjsonList = comment_list.collect {
				  def commentMessage = it.comment.body
				  if(commentMessage.size() > 20){
					  commentMessage = commentMessage.substring(0,20)+".."
				  }
				  [id: it.commentRef, name: "[Comment] - " + commentMessage]
			}
	       l = ujsonList + fjsonList + gjsonList + activityFeedjsonList + commentjsonList + taskjsonList
	        
	        def jsonResult = [
	                result: l
	        ]
	        render jsonResult as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
    }

    def tags = {
		if(request.xhr){
			render Tag.findAllByNameIlike("${params.term}%")*.name as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
    }
}