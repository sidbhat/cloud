package com.oneapp.cloud.core

import org.grails.prettytime.*
class FeedCommentTagLib {
	
	static namespace = 'feedComment'
	def springSecurityService
	
	def renderComment = {attrs->
		def prettytime = new PrettyTimeTagLib()
		def commentsInstance = attrs.commentsInstance
		def activityFeed = attrs.activityFeed
		def u = commentsInstance.poster
		def user = springSecurityService.currentUser
		def displayName = u.shortName?:(u.firstName+" "+(u.lastName?:""))   
		def displayComment = attrs.displayComment
		if(displayComment == "false")
			out << '<div class="ActivityCommentP" id="commentDiv'+commentsInstance.id+'" style="display:none;" name="commentDiv'+activityFeed.id+'">'
		else
			out << '<div class="ActivityCommentP" id="commentDiv'+commentsInstance.id+'" >'
		//Adding delete comment code
		if (user?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| user?.authorities?.authority.contains(Role.ROLE_ADMIN)|| ((user?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)|| user?.authorities?.authority.contains(Role.ROLE_USER)||user?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&& (user?.username.equalsIgnoreCase(activityFeed.createdBy.username)||commentsInstance.posterId == user.id))) 
		{
			out << '<form name="deletecomment'+commentsInstance.id+'"  method="post" style="position:absolute;right:10px;">'
			out <<	'<input type="button" class="gray-button deleteCommentButton" name="deleteFormSubmitBtn" value="x" onclick="submitDeleteCommentForm'+activityFeed.id+'('+commentsInstance.id+');return false; "/>'
			out <<	'<input type="hidden" name="id" value="'+activityFeed.id+'" />'
			out <<	'<input type="hidden" name="comment_id" value="'+commentsInstance.id+'" /></form>'
		}
		//Adding comment poster's image
		def sessionUserProfile = UserProfile.findByUser(u)
		def pictureURL=""
		if(sessionUserProfile?.attachments){
		   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
		} else if(u?.pictureURL && u.pictureURL?.length() > 0){
		   pictureURL = u.pictureURL        
		}
		if (pictureURL) {
			out << '<img src="'+pictureURL+'" width="32" height="32"/>'
		}else{
			out << '<img src="'+resource(dir:'images',file:'user_32.png')+'"/>'
		}
		
		//Adding poster's name and comment
			out << '<p><pre style="font-family: Helvetica;fon-size:11px;line-height:16px;">'
				out << '<div style="width: 85%;color:#000;font-size:12px;padding-left:43px;margin-top:5px;white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;word-wrap: break-word;"><b>'+displayName+'</b> : '+commentsInstance.body+'</div>'
			out << '</pre></p>'
			out << '<p>'
			
			//Adding comment time
				out << '<span>'+prettytime.display(date:commentsInstance.dateCreated)
				out << '</span>'
				
			out << '</p>'
		out<< '</div>'
	}

	def mobileRenderComment = {attrs->
		def prettytime = new PrettyTimeTagLib()
		def commentsInstance = attrs.commentsInstance
		def activityFeed = attrs.activityFeed
		def u = commentsInstance.poster
		def user = springSecurityService.currentUser
		def displayName = u.shortName?:(u.firstName+" "+(u.lastName?:""))
		def displayComment = attrs.displayComment
		if(displayComment == "false")
			out << '<div class="ActivityCommentP" id="commentDiv'+commentsInstance.id+'" style="display:none;" name="commentDiv'+activityFeed.id+'">'
		else
			out << '<div class="ActivityCommentP" id="commentDiv'+commentsInstance.id+'" >'
		//Adding delete comment code  <g:if test="${isAdmin || ( (!isAdmin) && ( user?.username.equalsIgnoreCase(activityFeed.createdBy?.username) || attachment.poster.id == user.id ) ) }">
																		
		if (user?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| user?.authorities?.authority.contains(Role.ROLE_ADMIN)|| ((user?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)|| user?.authorities?.authority.contains(Role.ROLE_USER)||user?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&& (user?.username.equalsIgnoreCase(activityFeed.createdBy.username)||commentsInstance.posterId == user.id))) 
		{
			out << '<form name="deletecomment'+commentsInstance.id+'"  method="post" style="position:absolute;right:10px;">'
				out <<	'<input type="hidden" name="id" value="'+activityFeed.id+'" />'
				out <<	'<input type="hidden" name="comment_id" value="'+commentsInstance.id+'" />'
				out <<  '<a data-role="button" data-icon="delete" data-iconpos="notext" href="javascript:;" onclick="submitDeleteCommentForm('+commentsInstance.id+');return false; " title="Delete" data-theme="c" class="ui-btn ui-btn-icon-notext ui-btn-corner-all ui-shadow ui-btn-up-c">'
					out <<	'<span class="ui-btn-inner ui-btn-corner-all">'
						out << '<span class="ui-btn-text">Delete</span>'
						out << '<span class="ui-icon ui-icon-delete ui-icon-shadow"></span>'
					out << '</span>'
				out<< '</a>'
			out << '</form>'
		}
		//Adding comment poster's image
        def sessionUserProfile = UserProfile.findByUser(u)
		def pictureURL=""
		if(sessionUserProfile?.attachments){
		   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/preview/imagesUrl/${sessionUserProfile.attachments[0].id}"
		} else if(u?.pictureURL && u.pictureURL?.length() > 0){
		   pictureURL = u.pictureURL
		}
		if (pictureURL) {
			out << '<img src="'+pictureURL+'" width="32" height="32"/>'
		}else{
			out << '<img src="'+resource(dir:'images',file:'user_32.png')+'"/>'
		}
		
		//Adding poster's name and comment
			out << '<div style="margin-top:0px;"><pre style="font-family: Helvetica;">'
				out << '<div style="color:#000;font-size:11px;padding-left:6px;margin-top:2px;white-space: pre;white-space: pre-wrap;white-space: pre-line;white-space: -pre-wrap;white-space: -o-pre-wrap;white-space: -moz-pre-wrap;white-space: -hp-pre-wrap;word-wrap: break-word;width:250px;"><b>'+displayName+' : </b>'+commentsInstance.body.encodeAsHTML()+'</div>'
			out << '</pre></div>'
			out << '<p style="width:100%;">'
			
			//Adding comment time
				out << '<span style="margin-left:40px;">'+prettytime.display(date:commentsInstance.dateCreated)
				out << '</span>'
				
			out << '</p>'
		out<< '</div>'
	}
	
}
