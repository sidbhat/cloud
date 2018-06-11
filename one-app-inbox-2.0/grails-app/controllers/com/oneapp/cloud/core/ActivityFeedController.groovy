

package com.oneapp.cloud.core
import grails.converters.JSON
import grails.converters.XML
import grails.util.*

import org.apache.poi.hssf.record.formula.functions.T
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.grails.formbuilder.*
import grails.plugin.multitenant.core.util.TenantUtils
import java.text.SimpleDateFormat

class ActivityFeedController {

    def index = {
        redirect(controller:'dashboard', action:'index', id:params.id)	
    }

    // the delete, save and update actions only accept POST requests
    def springSecurityService
	def activityFeedService
	def attachmentableService
	def inboxReaderService
	def utilService
	def sessionFactory
	def feedComment = new FeedCommentTagLib()
	def clientService
	def sqlDomainClassService
	static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS
	
	def activityFeedDetails = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def activityFeed = ActivityFeed.get(params.id)
		render template:'/activityFeed/activityFeedDetails', model: [activityFeed:activityFeed]
	}
	
	def feedStatus = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def activityFeed = ActivityFeed.get(params.id)
		render template:'/activityFeed/feedStatus', model: [activityFeed:activityFeed]
	}
	
	
    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        try{
        
        if ( params.sort == null ) {
        	params.sort = "lastUpdated"
        	params.order = "desc"
		}
        def activityFeedInstanceList = ActivityFeed.list(params)
        def total = ActivityFeed.count()
       
         			
        def response =  [activityFeedInstanceList:activityFeedInstanceList,  activityFeedInstanceTotal: total]
          withFormat {
                html {  response  }
                json {  render(status: 200, contentType: 'application/json', text: response as JSON) }
                xml {   render(status: 200, contentType: 'text/xml', text:  response as XML) }
             }
        }catch ( Exception ex ){
       		 withFormat {
                 html {  
                    flash.message = "activity.list.error"
            		flash.defaultMessage = "Activity Feed List Exception ${ex?.message}"
                 	return ([activityFeedInstanceList:null,activityFeedInstanceTotal:0])  
                 }
                 json { render(status: 400, contentType: 'application/json', text: [ex?.message] as JSON)  }
                 xml { render(status: 400, contentType: 'text/xml', text: [ex?.message] as XML) }
             }
        }
    }

    def create = {
        def activityFeedInstance = new ActivityFeed()
        activityFeedInstance.properties = params
        return [activityFeedInstance: activityFeedInstance]
    }

    def save = {
        def activityFeedInstance
        
        try{
        
        activityFeedInstance = new ActivityFeed(params)
        session.user = springSecurityService.currentUser
        activityFeedInstance.createdBy = session.user
      
        if (!activityFeedInstance.hasErrors() && activityFeedInstance.save()) {
            flash.message = "activityFeed.created"
            flash.args = [activityFeedInstance.id]
            flash.defaultMessage = "ActivityFeed ${activityFeedInstance.id} created"
    		
    		if ( params.attachment )
    			attachUploadedFilesTo(activityFeedInstance)
			
    	    withFormat {
                 html {  redirect(action: "edit", id: activityFeedInstance.id)  }
                 json { render(status: 200, contentType: 'application/json', text: activityFeedInstance as JSON) }
                 xml { render(status: 200, contentType: 'text/xml', text: activityFeedInstance as XML) }
            }
    		
        }
        else {
         render(view: "create", model: [activityFeedInstance: activityFeedInstance]) 
         	 withFormat {
                html {  render(view: "create", model: [activityFeedInstance: activityFeedInstance])  }
                json { render(status: 400, contentType: 'application/json', text: activityFeedInstance.errors as JSON) }
                xml { render(status: 400, contentType: 'text/xml', text: activityFeedInstance.errors as XML) }
             }
        }
        }catch ( Exception ex ) {
        	println "ActivityFeedController-save: "+ex
            withFormat {
                 html {  
                    flash.message = "activityFeed.error"
            		flash.defaultMessage = "ActivityFeed Exception ${ex?.message}"
                 	render(view: "create", model: [activityFeedInstance: activityFeedInstance])  
                 }
                 json { render(status: 400, contentType: 'application/json', text: [ex?.message] as JSON)  }
                 xml { render(status: 400, contentType: 'text/xml', text: [ex?.message] as XML) }
             }
        }
    }

    def show = {
        def activityFeedInstance = ActivityFeed.get(params.id)
        if (!activityFeedInstance) {
            flash.message = "activityFeed.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ActivityFeed not found with id ${params.id}"
            
            withFormat {
                html { render(action: "list") }
                json { render(status: 400, contentType: 'application/json', text: flash as JSON) }
                xml { render(status: 400, contentType: 'text/xml', text: flash as XML) }
             }
    		
        }
        else {
       		 withFormat {
                 html { return [activityFeedInstance: activityFeedInstance]  }
                 json { render(status: 200, contentType: 'application/json', text: activityFeedInstance  as JSON) }
               	 xml  { render(status: 200, contentType: 'text/xml', text: activityFeedInstance as XML) }
            }
            
        }
    }

    def edit = {
        def activityFeed = ActivityFeed.get(params.id)
        if (!activityFeed) {
            flash.message = "activityFeed.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ActivityFeed not found with id ${params.id}"
            redirect(controller:"dashboard",action: "index")
           
        }
        else {
        	withFormat {
                 html {  return [activityFeed: activityFeed]  }
                 json { render(status: 200, contentType: 'application/json', text: activityFeed  as JSON) }
               	 xml  { render(status: 200, contentType: 'text/xml', text: activityFeed as XML) }
            }
           
        }
    }

    def update = {
        def activityFeedInstance = ActivityFeed.get(params.id)
       try{
       if (activityFeedInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (activityFeedInstance.version > version) {
                    
                    activityFeedInstance.errors.rejectValue("version", "activityFeed.optimistic.locking.failure", "Another user has updated this ActivityFeed while you were editing")
                    render(view: "edit", model: [activityFeedInstance: activityFeedInstance])
                    return
                }
            }
            activityFeedInstance.properties = params
            if ( params.attachment )
            	attachUploadedFilesTo(activityFeedInstance)
            
            if (!activityFeedInstance.hasErrors() && activityFeedInstance.save()) {
                
                flash.message = "activityFeed.updated"
                flash.args = [params.id]
                flash.defaultMessage = "ActivityFeed ${params.id} updated"
                
                 withFormat {
                 html { render(action: "show", id: activityFeedInstance.id)  }
                 json { render(status: 200, contentType: 'application/json', text: activityFeedInstance as JSON) }
               	 xml { render(status: 200, contentType: 'text/xml', text: activityFeedInstance as XML) }
                }
                
            }
            else {
                 withFormat {
                 html { render(view: "edit", model: [activityFeedInstance: activityFeedInstance])  }
                 json { render(status: 400, contentType: 'application/json', text: activityFeedInstance?.errors as JSON) }
               	 xml { render(status: 400, contentType: 'text/xml', text: activityFeedInstance?.errors as XML) }
                }
                
            }
        }
        else {
            flash.message = "activityFeed.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ActivityFeed not found with id ${params.id}"
            withFormat {
                 html { redirect(action: "edit", id: params.id)  }
                 json { render(status: 400, contentType: 'application/json', text: activityFeedInstance?.errors as JSON) }
               	 xml { render(status: 400, contentType: 'text/xml', text: activityFeedInstance?.errors as XML) }
                }
            
        }
        }catch ( Exception ex ) {
            withFormat {
                 html {  
                    flash.message = "activityFeed.error"
            		flash.defaultMessage = "ActivityFeed Exception ${ex?.message}"
                 	render(view: "edit", model: [activityFeedInstance: activityFeedInstance])  
                 }
                 json { render(status: 400, contentType: 'application/json', text: [ex?.message] as JSON)  }
                 xml { render(status: 400, contentType: 'text/xml', text: [ex?.message] as XML) }
             }
        }
    }

    def delete = {
        def activityFeedInstance = ActivityFeed.get(params.id)
        if (activityFeedInstance) {
            try {
                activityFeedInstance.delete()
                flash.message = "activityFeed.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "ActivityFeed ${params.id} deleted"
               
                 //withFormat {
                // html {  
					 redirect(action: "index")  
				//	}
                 //json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
               	// xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
                //}
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "activityFeed.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "ActivityFeed ${params.id} could not be deleted"
                redirect(action: "index", id: params.id)
            }
        }
        else {
            flash.message = "activityFeed.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ActivityFeed not found with id ${params.id}"
            redirect(action: "index")
        }
    }
    
	def deleteFeed = {
		def activityFeedInstance = ActivityFeed.get(params.id)
		if (activityFeedInstance) {
			try {
				activityFeedInstance.delete()
				withFormat {
					 html { render "Success"}
					 json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
						xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
				}
				render "Success"
			}
			catch (org.springframework.dao.DataIntegrityViolationException e) {
				renderError(e)
			}
		}
		else {
			withFormat {
				 html { render(view: "index")}
				 json { render(status: 400, contentType: 'application/json', text: flash as JSON) }
					xml { render(status: 400, contentType: 'text/xml', text: flash as XML) }
			}
		}
	}
	
	def addRemoveTag = {
		
		def activityFeedInstance = ActivityFeed.get(params.id)
		
		if (activityFeedInstance){
		
		if ( activityFeedInstance.tags.contains(params.tag) ){
			activityFeedInstance.removeTag(params.tag)
			flash.message="Tag removed"
		}else{
			activityFeedInstance.addTag(params.tag)
			flash.message="Tag added"
		}	
			flash.defaultMessage = flash.message
			
			 withFormat {
                 html {  redirect(controller:'activityFeed', action:'list') }
                 json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
               	 xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
            }
		}else {
            flash.message = "activityFeed.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ActivityFeed not found with id ${params.id}"
            redirect(action: "list")
       }
	}
	
	def addSubscription = {
	
		def activityFeedInstance = ActivityFeed.get(params.id)
		if (activityFeedInstance ) {
			Subscription follow = new Subscription (followId:activityFeedInstance.id, follower:springSecurityService.currentUser,followClass:ActivityFeed.class.getName(), state:Subscription.ACTIVE)
			follow.createdBy = springSecurityService.currentUser
			follow.save()
			flash.message = "follower.added"
            flash.args = [params.follow_id]
            flash.defaultMessage = "Activity feed Subscription active"
            
             withFormat {
                 html { redirect(action: "show", id :params.id) }
                 json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
               	 xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
            }
		}
	
	}
	def deleteSubscription = {
		if ( params.follow_id && params.follow_class ){
			def followInstance = Subscription.findWhere(followId:params.follow_id, follower:springSecurityService.currentUser, followClass:params.follow_class)
			if (followInstance ) {
				followInstance.state = Subscription.INACTIVE
				follow.save()
				flash.message = "follower.deleted"
                flash.args = [params.follow_id]
                flash.defaultMessage = "Activity feed Subscription inactive"
                
                withFormat {
                 html { redirect(action: "list") }
                 json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
               	 xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
            	}
			}
		}else {
				flash.message = "follower.notfound"
                flash.args = [params.follow_id]
                flash.defaultMessage = "Activity feed Subscription not found with id ${params.follow_id}"
                withFormat {
                 html { redirect(action: "list")}
                 json { render(status: 400, contentType: 'application/json', text: flash as JSON) }
               	 xml { render(status: 400, contentType: 'text/xml', text: flash as XML) }
            }
		}
	
	}
	def addRemoveRating = {
		
		def activityFeedInstance = ActivityFeed.get(params.id)
		if (activityFeedInstance ) {
		
		Double rating=-1
		try{
			rating = new Double(params.rating)
		}catch (Exception e)
		{
			flash.message = "exception"
            flash.args = [params.id]
            flash.defaultMessage = "ActivityFeed error ${e}"
            withFormat {
                 html {redirect(action: "list")}
                 json { render(status: 400, contentType: 'application/json', text: flash as JSON) }
               	 xml { render(status: 400, contentType: 'text/xml', text: flash as XML) }
            }
		}
		if (rating != -1){
			activityFeedInstance.rate(springSecurityService.currentUser,rating)
		}	
			flash.message="Rating added/removed"
			flash.defaultMessage = flash.message
			
			withFormat {
                 html { redirect(controller:'activityFeed', action:'list') }
                 json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
               	 xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
            }
		}else {
            flash.message = "activityFeed.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ActivityFeed not found with id ${params.id}"
           withFormat {
                 html { redirect(action: "list")}
                 json { render(status: 400, contentType: 'application/json', text: flash as JSON) }
               	 xml { render(status: 400, contentType: 'text/xml', text: flash as XML) }
            }
        }
	}
	
	def updateAndGetData = {
		if(request.xhr){
			def totalCompFeeds
			def newActivityFeeds
			def user1 = User.get(springSecurityService?.currentUser.id)
			def lastActivity = user1.lastActivity
			if(!lastActivity)
				lastActivity = new Date()
			SimpleDateFormat dateFormat = new SimpleDateFormat( "yyyy-MM-dd" );
			Calendar cal = Calendar.getInstance();
			cal.setTime(dateFormat.parse(dateFormat.format( lastActivity)));
			cal.add( Calendar.DATE, -1)
			def maxDuration = cal.getTime()
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
					distinct("id")
				}
			}
			
			def activityNotification
			def jsonResult = []
			if(totalCompFeeds){
				activityNotification = ActivityNotification.createCriteria().list(max:5,sort:'id',order:"desc"){
					actionOnFeed{
						'in' "id",totalCompFeeds
					}
					 gt('actionTime', maxDuration)
					 ne('actionBy.id',user1.id)
				}
				int c = 0
				activityNotification.each{notificationInst ->
					if(c<5){
						def infoMessageTime = "${prettytime.display(date:notificationInst.actionTime)}"
						jsonResult<<[message:notificationInst.userFeedState,dateCreated:infoMessageTime,activityFeedId:notificationInst.actionOnFeed.id]
					}
				}
			}
			lastActivity = new Date()
			user1.lastActivity = lastActivity
			user1.lastViewed = lastActivity
			user1.save()
			render jsonResult as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
	def saveChatMessage = {
		if(request.xhr){
			def date = new Date();
			if(params.message != null)
			{
				params.messageTime = date;
				params.recvd = false
				
				def fromUser = User.get(springSecurityService.currentUser.id)
				params.fromUser = fromUser
				def toUser = User.findByUsername(params.to)
				params.toUser = toUser
				def messageInstance = new Message(params)
				if(messageInstance.save(flush:true,validate:false))
				{
					def data=
					[
						status:true,
						username:springSecurityService.currentUser.username
						
					]
					render data as JSON
				}else{
					def data=
					[
						status:false,
						hasValidation:true,
						validationField:''
					]
					render data as JSON
				}
				
			}else{
				def data=
				[
					status:false,
					hasValidation:true,
					
				]
				render data as JSON
			}
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	 }
	
	def startSession = {
		if(request.xhr){
			def chatWitUser = User.findByUsername(params.userId)
			def toUserId = springSecurityService.currentUser.id
			def currentDate = new Date();
			def messageInstance = null;
			messageInstance = Message.withCriteria(){
				or{
					and{
						eq 'toUser.id', chatWitUser.id
						eq 'fromUser.id', toUserId
					}
					and{
						eq 'toUser.id',toUserId
						eq 'fromUser.id', chatWitUser.id
					}
				}
				between('messageTime',currentDate-1,currentDate)
				eq 'recvd', true
				
			}
			def fromList = [firstName:[],userName:[],messageTime:[]];
			def sendMessage = true
			for(int i=0; i<messageInstance.size; i++)
			{
				def fromId = messageInstance[i].fromUser
				fromList.firstName << fromId.firstName
				fromList.userName << fromId.username
				fromList.messageTime << "${g.formatDate(date:messageInstance[i].messageTime, format:'hh:mm a', locale: request.locale)}"
			}
			 def data=
			 [
				 status:true,
				 items:messageInstance,
				 from:fromList,
				 chatboxtitle:chatWitUser.firstName
			 ]
			 render data as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
	def addComment = {

		if(request.xhr){
			def activityFeedInstance = ActivityFeed.get(params.id);
			def user=springSecurityService.currentUser;
			try{
			if (activityFeedInstance ) {
			String comment = params."commentText${params.id}"
			comment = utilService.convertURLToLink(comment.encodeAsHTML(),true)
			def commentsInstance = activityFeedInstance.addComment(user,comment)
			activityFeedInstance.save(flush: true)
			def infoMessage = ""+(springSecurityService.currentUser.firstName?:springSecurityService.currentUser.username)+" commented on feed"
			def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeedInstance, actionTime:new Date())
			activityNotification.save(flush:true)
				withFormat {
	                 //html { redirect(controller:'activityFeed', action:'list')}
					 html { render "Comment added."}
	                 json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
	               	 xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
	            }
				try{
					if (request['isMobile'] ) {
						render "${feedComment.mobileRenderComment(commentsInstance:commentsInstance, activityFeed:activityFeedInstance)}"
					}else{
						render "${feedComment.renderComment(commentsInstance:commentsInstance, activityFeed:activityFeedInstance)}"
					}
				}catch(Exception e){
					log.error "ActivityFeedController-addComment while rendering comment"+e
					throw e
				}
			} else {
	            withFormat {
	                 //html { redirect(action: "list")   }
					 html { render "Comment Not added" }
	                 json { render(status: 400, contentType: 'application/json', text: flash as JSON) }
	               	 xml { render(status: 400, contentType: 'text/xml', text: flash as XML) }
	            }
	        }
	        }catch (Exception ex)
			{
				withFormat {
	                 html {  
	                 }
	                 json { render(status: 400, contentType: 'application/json', text: [ex?.message] as JSON)  }
	                 xml { render(status: 400, contentType: 'text/xml', text: [ex?.message] as XML) }
	             }
			}
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
	
	def deleteComment={
		
			def activityFeedInstance = ActivityFeed.get(params.id);
			def user=springSecurityService.currentUser;
			try{
			if (activityFeedInstance ) {
			Long commentId=-1
			try{
				commentId = Long.parseLong(params.comment_id)
			}catch (Exception e)
			{
				withFormat {
					 html {redirect(action: "list")}
					 json { render(status: 400, contentType: 'application/json', text: [ex?.message] as JSON) }
						xml { render(status: 400, contentType: 'text/xml', text: [ex?.message] as XML) }
				}
			}
			activityFeedInstance.removeComment(commentId);
			activityFeedInstance.save(flush: true)
				withFormat {
					 html { render "Success"}
					 json { render(status: 200, contentType: 'application/json', text: flash as JSON) }
						xml { render(status: 200, contentType: 'text/xml', text: flash as XML) }
				}
				render "Success"
			
			} else {
				withFormat {
					 html { render(view: "list")}
					 json { render(status: 400, contentType: 'application/json', text: flash as JSON) }
						xml { render(status: 400, contentType: 'text/xml', text: flash as XML) }
				}
			}
			}catch (Exception ex)
			{
				renderError(ex)
			}
		}
	
	def getAttachmentCount = {
		def activityFeedInstance = ActivityFeed.get(params.id);
		def attachments = activityFeedInstance.attachments
		withFormat {
                 html { render "${obj}" }
                 json { render(status: 200, contentType: 'application/json', text: [obj] as JSON) }
               	 xml { render(status: 200, contentType: 'text/xml', text: [obj] as XML) }
            }
	
	
	}
	
	def renderSuccess(def obj){
			withFormat {
                 html { render "${obj}" }
                 json { render(status: 200, contentType: 'application/json', text: [obj] as JSON) }
               	 xml { render(status: 200, contentType: 'text/xml', text: [obj] as XML) }
            }
            
	}
	def renderError(def ex){
			withFormat {
                 html {  
                    flash.message = "activityFeed.error"
            		flash.defaultMessage = "ActivityFeed Exception ${ex?.message}"
                 	redirect(action: "list")  
                 }
                 json { render(status: 400, contentType: 'application/json', text: [ex?.message] as JSON)  }
                 xml { render(status: 400, contentType: 'text/xml', text: [ex?.message] as XML) }
             }
	
	}
	
	def feedShareUnShare = {
		def feedId = params.id
		def activityFeed = ActivityFeed.get(Long.parseLong(feedId))
		def sharedTo = JSON.parse(params.shareUnshareWith);
		try{
			activityFeed.sharedGroups = []
			activityFeed.sharedUsers = []
			activityFeed.sharedDepts = []
			activityFeed.sharedRoles = []
			def companyShare = false
			
			sharedTo.eachWithIndex{share,i->
				if ( share.visibility && ActivityFeed.GROUP.equalsIgnoreCase(share.visibility) )
					activityFeed.addToSharedGroups( com.oneapp.cloud.core.GroupDetails.get(share.sharedWith))
				else if ( share.visibility && ActivityFeed.USER.equalsIgnoreCase(share.visibility) )
					activityFeed.addToSharedUsers( com.oneapp.cloud.core.User.get(share.sharedWith))
				else if ( share.visibility != null && ActivityFeed.ROLE.equalsIgnoreCase(share.visibility) )
					activityFeed.addToSharedRoles( com.oneapp.cloud.core.Role.get(share.sharedWith))
				else if ( share.visibility != null && ActivityFeed.DEPARTMENT.equalsIgnoreCase(share.visibility) )
					activityFeed.addToSharedDepts( com.oneapp.cloud.core.DropDown.get(share.sharedWith))
				else if ( share.visibility != null && ActivityFeed.COMPANY.equalsIgnoreCase(share.visibility) )
					companyShare = true 
			}
			if(companyShare)
				activityFeed.visibility = ActivityFeed.COMPANY
			else
				activityFeed.visibility = null
			activityFeed.lastUpdated = new Date()
			if (!activityFeed.hasErrors() && activityFeed.save()){
				flash.message = "Feed Changes Saved"
			}else{
				flash.message = "Error occured while creating feed"
			}
			flash.defaultMessage = flash.message
			redirect(controller:'dashboard', action:'index')
		}catch(Exception ex){
			log.error("ActivityFeedController-feedShareUnShare"+ex)
			flash.message = ex.message
			flash.defaultMessage = flash.message
			redirect(controller:'dashboard', action:'index')
		}
	}
	
	def updateFeedAsTask = {
		try{
			def activityFeedInstance = ActivityFeed.get(params.activityFeedId);
			def dueDateString = params.dueDate
			def referenceId = params.referenceId 
			SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy");
			Date dueDate = dateFormat.parse(dueDateString);
			activityFeedInstance.isTask = true;
			activityFeedInstance.dueDate = dueDate;
			activityFeedInstance.referenceId = referenceId
			activityFeedInstance.lastUpdated = new Date()
			activityFeedInstance.save(flush: true)
			activityFeedInstance.addTag("Task")
			def infoMessage = ""+(springSecurityService.currentUser.firstName?:springSecurityService.currentUser.username)+" added a task"
			def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeedInstance, actionTime:new Date())
			activityNotification.save(flush:true)
			def currentUserobj = User.get(springSecurityService.currentUser.id)
			def feedMsg = "${g.render(template:'/activityFeed/activityFeed', model:[activityFeed:activityFeedInstance,user:currentUserobj])}"
			render feedMsg
		}catch(Exception ex){
		    render "Error"
		}
	}
	def multiFeedShare = {
		//todo validation checks. if params are null send error message back
		def source = params['source']
		String companyFeed = params['postactivity']
		companyFeed = companyFeed.encodeAsHTML()
		def shareid = params['share']
		def activityFeed = new ActivityFeed()
		def activityFeedConfig
		def uploadedFile = params['file']
		def shareType = params.shareType
		def className = params.className
		def sharedTo = JSON.parse(params.sharedWith);
		def dateCreated = new Date()
		// Set the configuration type for the feed
		try{
			if ( className ){
			  activityFeedConfig =  ActivityFeedConfig.findByClassName(className)
			  //A new configuration type is created if it doesn't exist
			  if(!activityFeedConfig){
				  def userInstance = User.get(Long.valueOf(springSecurityService.currentUser.id))
				  activityFeedConfig = new ActivityFeedConfig(createdBy: userInstance, shareType: shareType, configName: className,className:className, dateCreated: dateCreated, lastUpdated: dateCreated)
				  activityFeedConfig.save()
			  }
			}else{
				activityFeedConfig =  ActivityFeedConfig.findByConfigName("content")
				if ( !activityFeedConfig ) {
					activityFeedConfig = new ActivityFeedConfig(configName:"content", createdBy: springSecurityService.currentUser)
					activityFeedConfig.save(flush:true)
				}
			}
		   if(uploadedFile && uploadedFile.size > 0)
		   {
			   def uploadFileSize = (uploadedFile.size/1024)
			   if(uploadFileSize > 10240)
				   throw new Exception("File size cannot be more then 10MB")
			   Client myClient = Client.get(springSecurityService.currentUser.userTenantId)
			   if ( (uploadedFile?.size + clientService.getTotalAttachmentSize(myClient.id))/(1024*1024) > myClient.maxAttachmentSize )
				   throw new Exception("Sorry, can't upload attachment. You have reached your attachment file size limit. Please contact your system administrator")
				   
		   }
		   if(shareid){
			   activityFeed.shareId = Long.parseLong(shareid) // object instance shared
		   }
		   
		   activityFeed.config = activityFeedConfig
		   // Check for visibility
		   sharedTo.eachWithIndex{share,i->
		   if ( share.visibility && ActivityFeed.GROUP.equalsIgnoreCase(share.visibility) )
			   activityFeed.addToSharedGroups( com.oneapp.cloud.core.GroupDetails.get(share.sharedWith))
		   else if ( share.visibility && ActivityFeed.USER.equalsIgnoreCase(share.visibility) )
			   activityFeed.addToSharedUsers( com.oneapp.cloud.core.User.get(share.sharedWith))
		   else if ( share.visibility != null && ActivityFeed.ROLE.equalsIgnoreCase(share.visibility) )
			   activityFeed.addToSharedRoles( com.oneapp.cloud.core.Role.get(share.sharedWith))
		   else if ( share.visibility != null && ActivityFeed.DEPARTMENT.equalsIgnoreCase(share.visibility) )
			   activityFeed.addToSharedDepts( com.oneapp.cloud.core.DropDown.get(share.sharedWith))
		   else if ( share.visibility != null && ActivityFeed.COMPANY.equalsIgnoreCase(share.visibility) )
			    activityFeed.visibility = ActivityFeed.COMPANY
		   }
		   //For entries in ActivityFeed
		   
		   
		   activityFeed.feedState = ActivityFeed.ACTIVE
			   
		   if (activityFeed?.config?.shareType == "TASK"){
			   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/${activityFeed.config.shareType.toLowerCase()}/edit/${activityFeed.shareId}"
			   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
		   }else if (activityFeed?.config?.shareType == "Approval"){
			   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/ddc/edit?id=${activityFeed.shareId}&dc=${activityFeed.config.configName}"
			   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
		   }else if (activityFeed?.config?.shareType == "Survey"){
			   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/ddc/edit?id=${activityFeed.shareId}&dc=${activityFeed.config.configName}"
			   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
		   }else if (activityFeed?.config?.shareType == "Poll"){
			   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/ddc/edit?id=${activityFeed.shareId}&dc=${activityFeed.config.configName}"
			   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
		   }else
			   activityFeed.activityContent = utilService.convertURLToLink(companyFeed,true)
		   
			
				activityFeed.createdBy = springSecurityService.currentUser
			   activityFeed.dateCreated = dateCreated
			   activityFeed.lastUpdated = dateCreated
			   if ( activityFeed.config == null )
				   activityFeed.config = ActivityFeedConfig.get(1)
				if (!activityFeed.hasErrors() && activityFeed.save()){
					flash.message = "Feed Shared"
					 def infoMessage = ""
					 if(activityFeed.config.configName == "content"){
						 infoMessage = ""+(activityFeed.createdBy.firstName?:activityFeed.createdBy.username)+" shared a feed with you"
					 }else{
						 def feedClassName = activityFeed.config.className
						 feedClassName= feedClassName.substring(0,feedClassName.indexOf("."))
						 def form  = Form.findByName(feedClassName)
						 def formName = JSON.parse(form.settings)."en".name
						 if(!formName)
							 formName = form.name
						 infoMessage = "Form entry of "+formName+" shared with you"
					 }
					 def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeed, actionTime:activityFeed.dateCreated)
					 activityNotification.save(flush:true)
				}else{
					flash.message = "Error occured while creating feed"
				}
				if(uploadedFile && uploadedFile.size > 0)
					attachUploadedFilesTo(activityFeed)
		   // Add tag based on the share type
		   if (activityFeed?.config?.shareType){
			 def formObj = Form.read(params.formId?.toLong())
			def fieldInstance = JSON.parse(formObj?.settings)
			   activityFeed.addTag(fieldInstance.en.name.toLowerCase())
		   }
		   
		   def infoMessage = ""
		   if(activityFeed.config.configName == "content"){
			   infoMessage = ""+(activityFeed.createdBy.firstName?:activityFeed.createdBy.username)+" shared a feed with you"
		   }else{
			   def feedClassName = activityFeed.config.className
			   feedClassName= feedClassName.substring(0,feedClassName.indexOf("."))
			   def form  = Form.findByName(className)
			   def formName = JSON.parse(form.settings)."en".name
			   if(!formName)
				   formName = form.name
			   infoMessage = "Form entry of "+formName+" shared with you"
		   }
		   flash.defaultMessage = flash.message
		   if ( activityFeed?.config?.shareType == "TASK")
			   redirect(controller:"${activityFeedConfig?.shareType?.toLowerCase()}", action:'list')
		   else if ( activityFeed?.config?.shareType == "Approval" || activityFeed?.config?.shareType == "Survey" || activityFeed?.config?.shareType == "Poll")
			   redirect(controller:"ddc", action:'list', id:activityFeed?.shareId ,params:[dc:activityFeed.config.configName])
		   else
			   redirect(controller:'dashboard', action:'index')
		   return
		}catch(Exception ex){
			log.error("ActivityFeedController-multiFeedShare:"+ex)
			flash.message = ex.message
			flash.defaultMessage = flash.message
			redirect(controller:'dashboard', action:'index')
		}
	}
	
def ajaxcreate = {
				 //todo validation checks. if params are null send error message back
				 def source = params['source']
				 def ajax = params['ajax']
				 String companyFeed = params['postactivity']
				 companyFeed = companyFeed.encodeAsHTML()
				 def visibility = params['visibility']
				 def shareid = params['share']
				 def activityFeed = new ActivityFeed()
				 def activityFeedConfig
				 def uploadedFile = params['file']
				 def shareType = params.shareType
				 def className = params.className
				 // Set the configuration type for the feed
				 try{
					 if ( className ){
					   activityFeedConfig =  ActivityFeedConfig.findByClassName(className)
					   
					   //A new configuration type is created if it doesn't exist
					   if(!activityFeedConfig){
						   def userInstance = User.get(Long.valueOf(springSecurityService.currentUser.id))
						   def dateCreated = new Date()
						   
						   activityFeedConfig = new ActivityFeedConfig(createdBy: userInstance, shareType: shareType, configName: className,className:className, dateCreated: dateCreated, lastUpdated: dateCreated)
						   activityFeedConfig.save()
					   }
					 }else{
					 	activityFeedConfig =  ActivityFeedConfig.findByConfigName("content")
					 	if ( !activityFeedConfig ) {
					 		activityFeedConfig = new ActivityFeedConfig(configName:"content", createdBy: springSecurityService.currentUser)
					 		activityFeedConfig.save(flush:true)
					 	}
					 }
					if(uploadedFile && uploadedFile.size > 0)
					{
						def uploadFileSize = (uploadedFile.size/1024)
						if(uploadFileSize > 10240)
							throw new Exception("File size cannot be more then 10MB")
						Client myClient = Client.get(springSecurityService.currentUser.userTenantId) 
						if ( (uploadedFile?.size + clientService.getTotalAttachmentSize(myClient.id))/(1024*1024) > myClient.maxAttachmentSize )
							throw new Exception("Sorry, can't upload attachment. You have reached your attachment file size limit. Please contact your system administrator")
							
					}
					if(shareid){
						activityFeed.shareId = Long.parseLong(shareid) // object instance shared
					}
					
					activityFeed.config = activityFeedConfig
					// Check for visibility 
					if ( visibility && ActivityFeed.GROUP.equalsIgnoreCase(visibility) )
						activityFeed.addToSharedGroups( com.oneapp.cloud.core.GroupDetails.get(params.sharedWith))
					else if ( visibility && ActivityFeed.USER.equalsIgnoreCase(visibility) )
						activityFeed.addToSharedUsers( com.oneapp.cloud.core.User.get(params.sharedWith))
					else if ( visibility != null && ActivityFeed.ROLE.equalsIgnoreCase(visibility) )
						activityFeed.addToSharedRoles( com.oneapp.cloud.core.Role.get(params.sharedWith))
					else if ( visibility != null && ActivityFeed.DEPARTMENT.equalsIgnoreCase(visibility) )
						activityFeed.addToSharedDepts( com.oneapp.cloud.core.DropDown.get(params.sharedWith))
					//For entries in ActivityFeed
					if(visibility)
						activityFeed.visibility = ActivityFeed.getAt(visibility.toUpperCase())
					
					activityFeed.feedState = ActivityFeed.ACTIVE
						
					if (activityFeed?.config?.shareType == "TASK"){
						def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/${activityFeed.config.shareType.toLowerCase()}/edit/${activityFeed.shareId}"
						activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
					}else if (activityFeed?.config?.shareType == "Approval" || activityFeed?.config?.shareType == "Survey" || activityFeed?.config?.shareType == "Poll" || activityFeed?.config?.shareType == "Master"){
						def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/ddc/edit?id=${activityFeed.shareId}&dc=${activityFeed.config.configName}"
						activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
					}else
						activityFeed.activityContent = utilService.convertURLToLink(companyFeed,true)
					
					 
					 	activityFeed.createdBy = springSecurityService.currentUser
						activityFeed.dateCreated = new Date()
						activityFeed.lastUpdated = new Date()
						if ( activityFeed.config == null ) 
							activityFeed.config = ActivityFeedConfig.get(1)
						 if (!activityFeed.hasErrors() && activityFeed.save()){
							 flash.message = "Feed Shared"
							 def infoMessage = ""
							 if(activityFeed.config.configName == "content"){
								 infoMessage = ""+(activityFeed.createdBy.firstName?:activityFeed.createdBy.username)+" shared a feed with you"
							 }else{
								 def feedClassName = activityFeed.config.className
								 feedClassName= feedClassName.substring(0,feedClassName.indexOf("."))
								 def form  = Form.findByName(feedClassName)
								 def formName = JSON.parse(form.settings)."en".name
								 if(!formName)
									 formName = form.name
								 infoMessage = "Form entry of "+formName+" shared with you"
							 }
							 def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeed, actionTime:activityFeed.dateCreated)
							 activityNotification.save(flush:true)
						 }else{
							 flash.message = "Error occured while creating feed"
						 }
						 if(uploadedFile && uploadedFile.size > 0)
						 	attachUploadedFilesTo(activityFeed)
					// Add tag based on the share type	
					if (activityFeed?.config?.shareType){
						def formObj = Form.read(params.formId?.toLong())
						def formInstance = JSON.parse(formObj?.settings)
						def formName = formInstance.en.name
						if(formName == null || formName == '')
							formName = formObj.name
						activityFeed.addTag(formName.toLowerCase())
					}
					
					flash.defaultMessage = flash.message
					if ( activityFeed?.config?.shareType == "TASK")
						redirect(controller:"${activityFeedConfig?.shareType?.toLowerCase()}", action:'list')
					else if ( activityFeed?.config?.shareType == "Approval" || activityFeed?.config?.shareType == "Survey" || activityFeed?.config?.shareType == "Poll" || activityFeed?.config?.shareType == "Master")
						redirect(controller:"ddc", action:'list', id:activityFeed?.shareId ,params:[formId:params.formId])
					else
						redirect(controller:'dashboard', action:'index')
					return
				 }catch(Exception ex){
				 	log.error("ActivityFeedController-ajaxcreate:"+ex)
				 	flash.message = ex.message
					 flash.defaultMessage = flash.message
					 redirect(controller:'dashboard', action:'index')
				 }
}

	def uploadFileAttach = {
		def redirectPage = params['redirectPage']
		try{
			def uploadedFile = params['file']
			if(uploadedFile)
			{
				def uploadFileSize = (uploadedFile.size/1024)
				if(uploadFileSize > 10240)
					throw new Exception("File size cannot be more then 10MB")
				if(uploadFileSize == 0)
					throw new Exception("Please select a file to upload")
				Client myClient = Client.get(springSecurityService.currentUser.userTenantId) 
				if ( (uploadedFile?.size + clientService.getTotalAttachmentSize(myClient.id))/(1024*1024) > myClient.maxAttachmentSize )
					throw new Exception("Sorry, can't upload attachment. You have reached your attachment file size limit. Please contact your system administrator")
			}
			def activityFeed = ActivityFeed.get(params.id)
			attachUploadedFilesTo(activityFeed)
			def infoMessage = ""+(springSecurityService.currentUser.firstName?:springSecurityService.currentUser.username)+" uploaded new file"
			def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeed, actionTime:new Date())
			activityNotification.save(flush:true)
			flash.message = "File uploaded successfully"
		}catch(Exception ex){
			flash.message = ex.message
		}
		flash.defaultMessage = flash.message
		if(redirectPage)
			redirect(controller:'activityFeed', action:'edit',id:params.id)
		else
			redirect(controller:'dashboard', action:'index')
	}
	
	def addTag = {
		
		def activityFeedInstance = ActivityFeed.get(params.id)
		
		if ( activityFeedInstance.tags.contains(params.tag) )
			activityFeedInstance.removeTag(params.tag)
		else
			activityFeedInstance.addTag(params.tag)
			
			flash.message="Tag added/removed"
			flash.defaultMessage = flash.message
			redirect(controller:'dashboard', action:'index')
	}
	
	def addRating = {
		
		def activityFeedInstance = ActivityFeed.get(params.id)
		Double rating=-1
		try{
			if ( params.rating )
			 {
				 if ( params.rating == "0" )
				  rating = new Double(0)
				 else if ( params.rating  == "1" )
				  rating = new Double(1)
				 else if ( params.rating == "2" )
				  rating = new Double(2)
				 else if ( params.rating == "3" )
				  rating = new Double(3)
			 }
		
		}catch(Exception e){
			log.error "ActivityFeedController-addRating:"+e
		}
		// Update the rating with the new user rating. One user can have only one rating
	//	if ( companyActivityInstance.userRating(session.user) && rating != -1 ){
			//companyActivityInstance.deleteRating(session.user)
	//		companyActivityInstance.rate(session.user,rating)
		//}else
		if (rating != -1){
			activityFeedInstance.rate(session.user,rating)
		}
			activityFeedInstance.save(flush :true)
			def infoMessage = ""+(springSecurityService.currentUser.firstName?:springSecurityService.currentUser.username)+" rated the feed"
			def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeedInstance, actionTime:new Date())
			activityNotification.save(flush:true)
				StringBuilder sb = new StringBuilder();
				if(!request['isMobile']){
					sb << """<span style="font-weight:normal;font-size:11px;color:#666;vertical-align:top;">"""
					sb << """${new ActionTagLib().oneAppStar(feedId:activityFeedInstance.id,starAvgValue:activityFeedInstance.averageRating){}}"""
					sb << """<span style="font-weight: normal; font-size: 11px; color: rgb(102, 102, 102); vertical-align: top; position: relative; top: 2px;">(${activityFeedInstance?.totalRatings} votes)</span>"""
					sb << """</span>"""
				}else{
					sb << """${new ActionTagLib().oneAppStar(feedId:activityFeedInstance.id,starAvgValue:activityFeedInstance.averageRating){}}"""
				}

			render sb.toString();
	}
	
	
	def getAttachFromEmail = {
		File file
		def fis
		try{
			def accountId = params.emailAccount.toLong()
			def messageNumber = params.msgNo.toLong()
			file = inboxReaderService.downloadAttach(accountId,messageNumber)
			fis = new FileInputStream(file)
			def filename = file.name
	
			['Content-disposition': "attachment;filename=\"$filename\"",
				'Cache-Control': 'private',
				'Pragma': ''].each {k, v ->
				response.setHeader(k, v)
			}
	
			response.contentType = 'application/octet-stream'
			response.outputStream << fis
			response.outputStream.flush()
		}catch(Exception e){
			log.error "ActivityFeedController-getAttachFromEmail:"+e
		}finally{
			if(fis)
				fis.close()
			if(file && file.exists())
				file.delete();
		}
	}
	
	def shareOption = {
		if(request.xhr){
			def groupList = GroupDetails.list()
			def user = springSecurityService.currentUser
			def currentUserRoles = user?.authorities
			def userList
			if(currentUserRoles*.authority.contains(Role.ROLE_TRIAL_USER)){
				userList = [user]
			}else{
				userList = User.findAllByUserTenantIdAndEnabled(user?.userTenantId,true)
			}
			
			def roleList = Role.findAllByAuthorityNotEqual(Role.ROLE_SUPER_ADMIN)
			def deptList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
			def shareList = new ArrayList()
			def counter = 1
			userList.each{
				shareList << [id:it.id,name : it.firstName+" ("+it.username+") ",type : ActivityFeed.USER]
				counter++;
			}
			
			if(!currentUserRoles*.authority.contains(Role.ROLE_TRIAL_USER)){
				groupList.each{
					shareList << [id:it.id,name : it.groupName,type : ActivityFeed.GROUP]
					counter++;
				}
				roleList.each{
					if(user.userTenantId == 1){
						shareList << [id:it.id,name : it.description,type : ActivityFeed.ROLE]
						counter++;
					}else{
						if(it.authority != Role.ROLE_TRIAL_USER){
							shareList << [id:it.id,name : it.description,type : ActivityFeed.ROLE]
							counter++;
						}
					}
				}
				deptList.each{
					shareList << [id:it.id,name : it.name,type : ActivityFeed.DEPARTMENT]
					counter++;
				}
			}
			
			render shareList as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}
	
	
	def feedShareDetails = {
		if(request.xhr){
			def feedId = params.activityFeedId
			def activityFeed = ActivityFeed.get(Long.parseLong(feedId))
			def groupList = GroupDetails.list()
			def user = springSecurityService.currentUser
			def currentUserRoles = user?.authorities
			def userList
			if(currentUserRoles*.authority.contains(Role.ROLE_TRIAL_USER)){
				userList = [user]
			}else{
				userList = User.findAllByUserTenantIdAndEnabled(user?.userTenantId,true)
			}
			def roleList = Role.list()
			def deptList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
			def feedShareDetailList = new ArrayList()
			def shareList = new ArrayList()
			def counter = 1
			userList.each{
				shareList << [id:it.id,name : it.firstName+" ("+it.username+") ",type : ActivityFeed.USER]
				counter++;
			}
			if(!currentUserRoles*.authority.contains(Role.ROLE_TRIAL_USER)){
				groupList.each{
					shareList << [id:it.id,name : it.groupName,type : ActivityFeed.GROUP]
					counter++;
				}
				roleList.each{
					if(user.userTenantId == 1){
						if(it.authority != Role.ROLE_SUPER_ADMIN){
							shareList << [id:it.id,name : it.description,type : ActivityFeed.ROLE]
							counter++;
						}
						
					}else{
						if(it.authority != Role.ROLE_SUPER_ADMIN && it.authority != Role.ROLE_TRIAL_USER){
							shareList << [id:it.id,name : it.description,type : ActivityFeed.ROLE]
							counter++;
						}
					}
					
				}
				deptList.each{
					shareList << [id:it.id,name : it.name,type : ActivityFeed.DEPARTMENT]
					counter++;
				}
			}
			def feedSharedList= new ArrayList()
			activityFeed.sharedGroups.each{
				feedSharedList << [id:it.id,name : it.groupName,type : ActivityFeed.GROUP]
			}
			activityFeed.sharedUsers.each{
				feedSharedList << [id:it.id,name : it.username.substring(0,it.username.indexOf("@")),type : ActivityFeed.USER]
			}
			activityFeed.sharedDepts.each{
				feedSharedList << [id:it.id,name : it.name,type : ActivityFeed.DEPARTMENT]
			}
			activityFeed.sharedRoles.each{
				if(it.authority != Role.ROLE_SUPER_ADMIN){
					feedSharedList << [id:it.id,name : it.description,type : ActivityFeed.ROLE]
				}
			}
			
			feedShareDetailList << shareList;
			feedShareDetailList << feedSharedList
			render feedShareDetailList as JSON
		}else{
			redirect(controller:'dashboard',action:'index')
		}
	}

	def afterInterceptor = { model, modelAndView ->
		if (request['isMobile'] && modelAndView != null ) {
			modelAndView.viewName = modelAndView.viewName + "_m"
		}
	}
 def accountDeleteRequest={
	 	
	 def superUserList=UserRole.findAllByRole(Role.findByAuthority(Role.ROLE_SUPER_ADMIN)).user
	 def user =superUserList[0]
	 def currentUser = springSecurityService.currentUser
	 Client client= Client.get(currentUser.userTenantId)
	 if(currentUser && client){
			 TenantUtils.doWithTenant((int) user.userTenantId){
				 def activityFeed = new ActivityFeed()
				 def activityFeedConfig= new ActivityFeedConfig();
				 activityFeedConfig =  ActivityFeedConfig.findByConfigName("content")
					 if ( !activityFeedConfig ) {
						 activityFeedConfig = new ActivityFeedConfig(configName:"content", createdBy: user)
						 activityFeedConfig.save(flush:true)
					 }
					activityFeed.feedState = ActivityFeed.ACTIVE
					//Request for account deletion for oneappcloud.com by admin@yourdomain.com
					def mess = "Request for account deletion for ${client.name} by ${currentUser.username}"
					activityFeed.config = activityFeedConfig
					activityFeed.activityContent=mess
					activityFeed.createdBy = user
					activityFeed.dateCreated = new Date()
					activityFeed.lastUpdated = new Date()
					activityFeed.addToSharedRoles( com.oneapp.cloud.core.Role.findByAuthority(Role.ROLE_SUPER_ADMIN))
					
					activityFeed.save(flush:true)
					flash.message = "Request for account deletion sent"
					flash.defaultMessage = flash.message
			 }
		 }else{
		  flash.message = "Error occured while sending request"
		  flash.defaultMessage = flash.message
		}
		 redirect(controller:'dropDown', action:'clientUsage')
	 }
 
 def  leftPanelFormList={
	 if(request.xhr){
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
		 
		 def responcesList = []
		 def showOwnCreated = true
		 if(!SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)){
			 showOwnCreated = false
		 }
		 groupedMap.each{k,v->
			 responcesList.addAll(sqlDomainClassService.responseCountGroup(v.form,showOwnCreated))
		 }
		render responcesList as JSON
	}else{
		redirect(controller:'dashboard',action:'index')
	}
  }
 def leftPanelTagList= {
	 StringBuilder sb = new StringBuilder()
	 sb << """${new ActionTagLib().tagList()}""";
	 render sb.toString()
 }
 
 def leftPanelMobileTagList= {
	 StringBuilder sb = new StringBuilder()
	 sb <<"""<li data-role="list-divider" onclick="moblieTagList('/form-builder/activityFeed/leftPanelMobileTagList')" role="heading" class="ui-li ui-li-divider ui-btn ui-bar-b ui-corner-top ui-btn-up-undefined">Tags</li>"""
	 sb << """${new ActionTagLib().mobileTagList()}""";
	 render sb.toString()
 }
 
 def findTaskCount = { user1->
		def currentUserRoles = user1.authorities*.id.get(0)
	 	def taskCount = ActivityFeed.createCriteria().listDistinct(){
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
			 eq "isTask", true
			 projections{
				countDistinct("id")
			}
		 }
		return taskCount
 }
 
 def findRecentTaskCount = { user1->
	  def currentUserRoles = user1.authorities*.id.get(0)
	  def recentTaskCount = ActivityFeed.withCriteria(){
		  def now = new Date()
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
		  eq "isTask", true
		  between('dueDate', now-1, now+1)
		  projections{
			  countDistinct("id")
		  }
	  }
	  return recentTaskCount
   }
 
 def changeStatus = {
	 def status = params.status
	 def activityId = params.id
	 def feedType = params.feedType
	 def success = false
	 try{
		 def activityFeedInstance = ActivityFeed.get(activityId)
		 if(feedType == 'approval'){
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
			 }
			 def infoMessage = ""+(springSecurityService.currentUser.firstName?:springSecurityService.currentUser.username)+" updated the status of Form"
			 def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeedInstance, actionTime:new Date())
			 activityNotification.save(flush:true)
		 }else{
			 activityFeedInstance.taskStatus = (status=="complete"?true:false)
			 activityFeedInstance.lastUpdated = new Date()
			 activityFeedInstance.save(flush: true)
			 def infoMessage = ""+(springSecurityService.currentUser.firstName?:springSecurityService.currentUser.username)+" updated the status of Task"
			 def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeedInstance, actionTime:new Date())
			 activityNotification.save(flush:true)
		 }
		 def currentUserobj = User.get(springSecurityService.currentUser.id)
		 def feedMsg = "${g.render(template:'/activityFeed/activityFeed', model:[activityFeed:activityFeedInstance,user:currentUserobj])}"
		 render feedMsg
	 }catch(Exception e){
		 render "Error"
	 }
	 
 }
 
 def findRecentTaskList = { user1->
	 def currentUserRoles = user1.authorities*.id.get(0)
	  def recentTaskList = ActivityFeed.createCriteria().listDistinct(){
		  def now = new Date()
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
		  eq "isTask", true
		  between('dueDate', now-1, now+1)
	  }
	  return recentTaskList
   }
 
 def findTaskList = { user1->
	 def currentUserRoles = user1.authorities*.id.get(0)
	  def recentTaskList = ActivityFeed.createCriteria().listDistinct(){
		  def now = new Date()
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
		  eq "isTask", true
		 between( 'lastUpdated' , now-5, now)
	  }
	  return recentTaskList
   }

	def changeUserChatStatus = {
		try{
			def onlineStatus = params.status
			def userOnlineStatus = false
			if(onlineStatus == 'online'){
				userOnlineStatus = true
			}
			def currentUser = springSecurityService.currentUser
			def userProfile = UserProfile.findByUser(currentUser)
			if (!userProfile) {
				userProfile = new UserProfile()
				userProfile.emailSubscribed=true
				userProfile.user = currentUser
			}
			userProfile.isOnline = userOnlineStatus
			userProfile.save(flush:true)
			render "Success"
		}catch(Exception ex){
			render "Error"+ex.message
		}
		
	}
 
}