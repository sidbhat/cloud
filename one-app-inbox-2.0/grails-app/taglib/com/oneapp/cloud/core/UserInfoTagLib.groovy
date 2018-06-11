package com.oneapp.cloud.core

import org.grails.formbuilder.*;
import grails.converters.JSON

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.codehaus.groovy.grails.web.pages.FastStringWriter

class UserInfoTagLib {
	static namespace = 'user'
	def springSecurityService
	
	def userFirstLastName = {attrs->
		def user = springSecurityService.currentUser
		def updatedUser = User.findByUsername(user?.username)
		out << '<a href="javascript:;">'+g.message(code:"menu.welcome", 'default':"Welcome") +'&nbsp;'+(updatedUser.shortName?:(updatedUser.firstName+' '+(updatedUser.lastName?:"")))+'<span class="arrow-down"></span></a>'
	}
	
	def userProfileInfo = {attrs->
		def user = User.findByUsername(springSecurityService.currentUser.username)
		def role = user?.authorities
			out << '<li style="text-align:center;">'
				out << '<a href="'+createLink(controller:'userProfile', action:'edit')+'" >'
				  out << '<div class="profileImageContainer">'
				  	if (session.pictureURL) {
						  out << '<img src="'+session.pictureURL+'" style="border: 1px solid #DDD;" width="90" /><br/>'
				   }else{
				  		  out << '<img src="'+resource(dir:'images',file:'user_32.png')+'" class="profileImage"/><br/>'
				   }
				   out << '</div>'
				   out << '<span style="color:#333;font-size:11px;font-weight:bold;">'+(user.shortName?:(user.firstName+' '+(user.lastName?:"")))+'</span>'
				 out << '</a>'
			out << '</li>'
	}
	
	def userMobileProfileInfo = {attrs->
		def user = User.findByUsername(springSecurityService.currentUser.username)
		def role = user?.authorities
		out << '<div class="userImage">'
		  if ( session.pictureURL ) {
			  out << '<img src="'+session.pictureURL+'" width="50px" height="50px"/><br/>'
		  }else{
				out << '<img src="'+resource(dir:'images',file:'user_32.png')+'" style="margin:10px;"/><br/>'
	   }
		out << '</div>'
		out << '<div style="position: relative;top: 15px;left:15px;">'
		out <<  ''+(user.shortName?:(user.firstName+' '+(user.lastName?:"")))
		out << '</div>'
	}
	
	def userDispList = {attrs->
		def userList
		def user = springSecurityService.currentUser
		def ur = UserRole.findAllByRole(Role.findByAuthority(Role.ROLE_SUPER_ADMIN))
		if (user?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)) {
			userList = User.createCriteria().list(){
				ne 'userTenantId',0
			}
		} else if (user?.authorities?.authority.contains(Role.ROLE_TRIAL_USER)) {
			userList = [user]
		} else if(user?.authorities?.authority.contains(Role.ROLE_ADMIN)) {
			userList = User.createCriteria().list(){
				eq 'userTenantId',user?.userTenantId
				if(ur){
					not{
						'in' "id",ur.user.id
					}
				}
			}
		} else {
			def allUserList = new ArrayList()
			userList = User.createCriteria().list(){
				eq 'userTenantId',user?.userTenantId
				eq 'enabled',true
				if(ur){
					not{
						'in' "id",ur.user.id
					}
				}
			}
		}
	   userList.each{
		   if(it.id != user.id){
		  	out << " <li><a href='javascript:void(0)' style='padding:0;'><span id='onlineStatus${it.id}' name='onlineStatus' style='margin-top:10px;padding-right:5px;float:left;width:5px;'></span>"
			  def up= UserProfile.findByUser(it)
			  def pictureURL=""
			  if(up?.attachments){
				 pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/attachmentable/renderImg/${up.attachments[0].id}"
			   } else if(user?.pictureURL && user.pictureURL?.length() > 0){
				  pictureURL = user.pictureURL
			   }
			   if (pictureURL) {
					out << '<img src="'+pictureURL+'" width="32" height="32"/>'
				}else{
					out << '<img src="'+resource(dir:'images',file:'user_32.png')+'"/>'
				}
			  out << "<span style='vertical-align:super;'>&nbsp;${it.firstName}</span></a></li>"
		   }
	   }
	}
}
