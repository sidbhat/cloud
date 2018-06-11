
package com.oneapp.cloud.core
import grails.converters.*
//import com.oneapp.cloud.workflow.*
import java.util.regex.*
class CompanyActivityController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def springSecurityService
	def utilService
	
  	def tags = {
 		 render Tag.findAllByNameIlike("${params.term}%")*.name as JSON
	}
	
	
	    
	def media = {
        def companyActivityInstance = CompanyActivity.get(params.id)
        if ( companyActivityInstance.media != null ){
       	 	response.setContentType(companyActivityInstance.mediaType)
        	response.outputStream << companyActivityInstance.media
        }
    }
    
 
	def addTag = {
		
		def companyActivityInstance = CompanyActivity.get(params.id)
		
		if ( companyActivityInstance.tags.contains(params.tag) )
			companyActivityInstance.removeTag(params.tag)
		else
			companyActivityInstance.addTag(params.tag)
			
			flash.message="Tag added/removed"
			flash.defaultMessage = flash.message
			redirect(controller:'dashboard', action:'index')
	}
	
	
	def addRating = {
		
		def companyActivityInstance = CompanyActivity.get(params.id)
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
			
		}
		// Update the rating with the new user rating. One user can have only one rating
	//	if ( companyActivityInstance.userRating(session.user) && rating != -1 ){
			//companyActivityInstance.deleteRating(session.user)
	//		companyActivityInstance.rate(session.user,rating)
		//}else 
		if (rating != -1){
			companyActivityInstance.rate(session.user,rating)
		}	
			flash.message="Rating added/removed"
			flash.defaultMessage = flash.message
			redirect(controller:'dashboard', action:'index')
	}
	
	def add = {
		
				if(session.username == null){			
					session.username = springSecurityService.currentUser.username
				}
		
				def comment = params['comment']
				def user=session.username
				def dateCreated = new Date()
				//def visibility = "company"
				
				if(comment.length()>0 ){
					def companyActivity = new CompanyActivity()
					companyActivity.username = user
					companyActivity.activityContent = comment
					companyActivity.visibility = visibility
					companyActivity.activityDate = dateCreated
					companyActivity.save(flush:true);
					
					redirect(controller:'dashboard', action:'index', params:["sourcetab":'company'])
				} else {
					redirect(controller:'dashboard', action:'index')
					
				}
			}
	 def index = {
        redirect(controller:'dashboard', action:'index', id:params.id)	
    }
    
    
    def ajaxApproveReject = {
    
    	//println params
    	
    	def comment = params['comment']
		def user= session.user
		def companyActivityInstance = CompanyActivity.get(params.share)
		def action = params['action']
		
    	if ( companyActivityInstance == null ) 
    	 	render "No id specified for workflow"
    	else {
    		if ( comment ) 
				companyActivityInstance.addComment(comment)
			if ( action )
				companyActivityInstance.wfType = action
    		companyActivityInstance.lastUpdated = new Date()
    		companyActivityInstance.approvedDate = new Date()
    		companyActivityInstance.approver = user
    		companyActivityInstance.save()
    		render "Workflow successfully ${action}"
    	}
    	
    }
	 
	 def ajaxcreate = {

		 if(session.username == null){
			 session.username = springSecurityService.currentUser.username
		 }
		 def source = params['source']
		 def ajax = params['ajax']

		 String companyFeed = params['postactivity']
		
		 def user=session.username
		 def dateCreated = new Date()
		 def visibility = params['visibility']
		 def shareid = params['share']	
		 def formResponseId = params['responseId'] 
		 if(source == "company")
		 {
			source = "company"	
			def activityFeed = new ActivityFeed()
			def activityFeedConfig = new ActivityFeedConfig()
			//For entries in ActivityFeedConfig
			def userInstance = User.get(Long.valueOf(springSecurityService.currentUser.id))			
			activityFeedConfig.createdBy = userInstance
			activityFeedConfig.shareType = params['shareType']
			activityFeedConfig.configName = params['shareType']
			activityFeedConfig.dateCreated = dateCreated
			activityFeedConfig.lastUpdated = dateCreated
			if ( visibility == ActivityFeed.COMPANY || visibility == ActivityFeed.GROUP || visibility == ActivityFeed.USER ){
				if ( shareid && params['shareLink'] ){
					activityFeed.shareId = Long.parseLong(shareid)
					activityFeedConfig.shareLink = params['shareLink']
				}
			}
			activityFeedConfig.save(flush:true,validate:false)
			
			
			//For entries in ActivityFeed
			activityFeed.visibility = visibility
			activityFeed.config = activityFeedConfig
			activityFeed.feedState = ActivityFeed.ACTIVE
			if (activityFeedConfig.shareType)
			{ 
				def mess
				if(activityFeedConfig.shareType == "TASK")
				{
					mess = "Click here to view ${companyActivity.shareType.toLowerCase()} - <a href='../${companyActivity.shareType.toLowerCase()}/edit/${companyActivity.shareLink}'>Here</a>"
					activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed)
				}
				else{
					activityFeed.activityContent = formResponseId
				}
				activityFeed.createdBy = userInstance
				activityFeed.dateCreated = dateCreated
				activityFeed.lastUpdated = dateCreated
				activityFeed.save(flush:true,validate:false)
				activityFeed.addTag(activityFeedConfig.shareType.toLowerCase())
			}else
				activityFeed.activityContent = (companyFeed)					
			/*def f 
			try{
				f =  request.getFile('file')
			}catch(Exception ex){}
			if (f != null )
			{
				if ( f.getSize() > 2000000)
				{
				  flash.message = "File size can not exceed 2MB"
				  flash.defaultMessage = flash.message
  				  redirect(controller:'dashboard', action:'index')
  				  return
				}
				activityFeed.media = request.getFile('file').getBytes()
				activityFeed.mediaType = f.getContentType()
				activityFeed.mediaName = f.getOriginalFilename()
				if (activityFeed.activityContent == null)
					activityFeed.activityContent = f.getOriginalFilename()
			}*/
			
			flash.message = "Feed shared"
 			flash.defaultMessage = flash.message
 			if ( ajax != null && ajax == "true")
 				redirect(controller:"${activityFeedConfig?.shareType?.toLowerCase()}", action:'list')
 			else
				redirect(controller:'dashboard', action:'index')
			return
		 } else if(source == "facebook"){
			 redirect(controller: "facebookConnect", action: "authorizeapp", params:["feed":companyFeed])
		 } else if(source == "twitter"){
			 redirect(controller: "twitterConnect", action: "authorizeapp", params:["feed":companyFeed])
		 } else if(source == "linkedin"){
			 redirect(controller: "linkedinConnect", action: "authorizeapp", params:["feed":companyFeed])
		 } 
	 }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        def list
        def count
        if ( params.id != null ){
        	list = CompanyActivity.findAllByTag(params.id)
        	count = CompanyActivity.countByTag(params.id)
        }else{
            params.sort = "lastUpdated"
       		params.order = "desc"
   			list =  CompanyActivity.list(params)
        	count = CompanyActivity.count()
        }
        [companyActivityInstanceList:list, companyActivityInstanceTotal: count]
    }

    def create = {
        def companyActivityInstance = new CompanyActivity()
        companyActivityInstance.properties = params
        return [companyActivityInstance: companyActivityInstance]
    }

    def save = {
        def companyActivityInstance = new CompanyActivity(params)
        if (companyActivityInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), companyActivityInstance.id])}"
            redirect(action: "show", id: companyActivityInstance.id)
        }
        else {
            render(view: "create", model: [companyActivityInstance: companyActivityInstance])
        }
    }

    def show = {
        def companyActivityInstance = CompanyActivity.get(params.id)
        if (!companyActivityInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), params.id])}"
            redirect(action: "list")
        }
        else {
            [companyActivityInstance: companyActivityInstance]
        }
    }

    def edit = {
        def companyActivityInstance = CompanyActivity.get(params.id)
        if (!companyActivityInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [companyActivityInstance: companyActivityInstance]
        }
    }

    def update = {
        def companyActivityInstance = CompanyActivity.get(params.id)
        if (companyActivityInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (companyActivityInstance.version > version) {
                    
                    companyActivityInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'companyActivity.label', default: 'CompanyActivity')] as Object[], "Another user has updated this CompanyActivity while you were editing")
                    render(view: "edit", model: [companyActivityInstance: companyActivityInstance])
                    return
                }
            }
            companyActivityInstance.properties = params
            if (!companyActivityInstance.hasErrors() && companyActivityInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), companyActivityInstance.id])}"
                redirect(action: "show", id: companyActivityInstance.id)
            }
            else {
                render(view: "edit", model: [companyActivityInstance: companyActivityInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def companyActivityInstance = CompanyActivity.get(params.id)
        if (companyActivityInstance) {
            try {
                companyActivityInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), params.id])}"
                flash.defaultMessage = flash.message
                redirect(action: "index")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), params.id])}"
                flash.defaultMessage = flash.message
                redirect(action: "index", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'companyActivity.label', default: 'CompanyActivity'), params.id])}"
            flash.defaultMessage = flash.message
            redirect(action: "index")
        }
    }
	def error = {

		}
	
	
	def companyFeedDetails = {
		params.max = Math.min(params.max ? params.int('max') : 10, 100)
		def companyActivity =CompanyActivity.findById(params.id)
		def comments = companyActivity.comments.sort{ it.dateCreated }.reverse() 
		render template:'companyFeedDetails', model: [companyActivity:companyActivity,comments:comments,companyActivityCommentsInstanceTotal:companyActivity.comments.size()]
	}
	
	def deleteComment={
	
		def commentid=params['comment_id']
		def companyActivity =CompanyActivity.findById(params['activity_id']);
		
		def user=springSecurityService.currentUser;
		companyActivity.removeComment(Long.parseLong(commentid));
		companyActivity.save(flush: true)
		render "Comment deleted."
		//redirect action:"companyFeedDetails", id:params['activity_id']
	}
	
	def addcomment = {
	//println params
		def commentid="commentText"+params['activity_id']
	//	println "adding comment for activity id"+params['activity_id']+" comment:"+params[commentid]
		def companyActivity =CompanyActivity.findById(params['activity_id']);
		def user=springSecurityService.currentUser;
		
		if(params[commentid]==null || ((String)(params[commentid])).trim()==""){
			println "empty comment hence returning"
			render "<span style='color:#f00'>Please enter a comment<span>"
			return
		}
		
		String comment = (String)params[commentid]
		companyActivity.addComment(user,(comment));
		companyActivity.save(flush: true)
		
		render "Comment added."
		
		//redirect(action:index)
	}
	
	 def afterInterceptor = { model, modelAndView ->
  		if (request['isMobile'] && modelAndView != null ) {
  			println "CompanyActivityController-afterInterceptor: "+request['isMobile'] 
  			modelAndView.viewName = modelAndView.viewName + "_m"
 	 }
	}
}
