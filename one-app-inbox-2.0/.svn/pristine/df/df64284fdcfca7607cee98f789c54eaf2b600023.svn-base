package com.oneapp.cloud.core

import org.grails.formbuilder.*;

class ActionTagLib {
	
	def springSecurityService
	def domainClassService
	def sqlDomainClassService
	
	
	def oneAppStar = { attrs, body ->
		def feedId = attrs.feedId;
		def rateVal = attrs.starAvgValue;
		out << getStarList(rateVal,feedId);		
	}
	
	def getStarList(rateVal,feedId){
		
		StringBuilder sb = new StringBuilder();
		def starOn = resource(dir:'images',file:'star-on.png')
		def starOff = resource(dir:'images',file:'star-off.png')
		sb << """ <span class="half" style=" cursor: pointer; width: 100px;vertical-align:top;"> """
		for(int i=0;i<=3;i++){
			sb << """
					<a onclick="ajaxCall('${request.getContextPath()}/activityFeed/addRating/${feedId}?rating=${i}',this);" herf="javascript:;">
					 <img alt="1" 
					"""
			if(i<=rateVal)
				sb << """ src="${starOn}" height="16px" width="16px"/></a> """
			else
				sb << """ src="${starOff}" height="16px" width="16px"/></a> """
		}
		sb << """  </span> """
		sb.toString()
		
	}
	
	/*def formTagCount = { attrs, body ->
		def allConfigTypes = com.oneapp.cloud.core.ActivityFeedConfig.list()
		def approvalsCount = 0
		def surveysCount = 0
		def pollsCount = 0
		def user = springSecurityService?.currentUser
		def currentUserRoles = user.authorities
		allConfigTypes.each {
			def feedForConfig
			def domainClass = DomainClass.findByName(it?.configName)
			def formInstanceClass = grailsApplication.getDomainClass(it?.configName)
			if(!formInstanceClass){
				domainClassService.reloadUpdatedDomainClasses()
				formInstanceClass = grailsApplication.getDomainClass(it?.configName)
			}
			if(domainClass){
				if("approval".equalsIgnoreCase(it.shareType)){
					feedForConfig = com.oneapp.cloud.core.ActivityFeed.findAllByConfig(it)
					feedForConfig.eachWithIndex(){ ap,i->
						def formInstance = formInstanceClass?.clazz?.createCriteria().get{
							eq 'id',ap.shareId
						  }
						if(formInstance)
						{
							if(ap.visibility ==  com.oneapp.cloud.core.ActivityFeed.COMPANY)
							{
								approvalsCount++
							}
							else{
								def groupUserList =  ap.sharedGroups.find{g -> (g.user.find{u -> u.id == user.id} != null)}
								if (groupUserList != null || it.createdBy.id == user.id)
									 approvalsCount++
								 else if(ap.sharedUsers*.id.contains(user.id) || ap.createdBy.id == user.id)
										approvalsCount++
								else if(ap.sharedRoles.retainAll(currentUserRoles) || ap.createdBy.id == user.id)
										approvalsCount++
								else if(ap.sharedDepts*.id.contains(user.department?.id) || ap.createdBy.id == user.id)
										approvalsCount++
							}
						}
					}
				}else if("survey".equalsIgnoreCase(it.shareType)){
					feedForConfig = com.oneapp.cloud.core.ActivityFeed.findAllByConfig(it)
					feedForConfig.eachWithIndex(){su,j->
						def formInstance = formInstanceClass?.clazz?.createCriteria().get{
							eq 'id',su.shareId
						  }
						if(formInstance)
						{
							if(su.visibility ==  com.oneapp.cloud.core.ActivityFeed.COMPANY)
							{
								surveysCount++
							}
							else{
								def groupUserList =  su.sharedGroups.find{g -> (g.user.find{u -> u.id == user.id} != null)}
								if (groupUserList != null || it.createdBy.id == user.id)
									 surveysCount++
								 else if(su.sharedUsers*.id.contains(user.id) || su.createdBy.id == user.id)
										surveysCount++
								else if(su.sharedRoles.retainAll(currentUserRoles) || su.createdBy.id == user.id)
										surveysCount++
								else if(su.sharedDepts*.id.contains(user.department?.id) || su.createdBy.id == user.id)
										surveysCount++
							}
						}
					}
				}else if("poll".equalsIgnoreCase(it.shareType)){
					feedForConfig = com.oneapp.cloud.core.ActivityFeed.findAllByConfig(it)
					feedForConfig.eachWithIndex(){po,k->
						def formInstance = formInstanceClass?.clazz?.createCriteria().get{
							eq 'id',po.shareId
						  }
						if(formInstance)
						{
							if(po.visibility ==  com.oneapp.cloud.core.ActivityFeed.COMPANY)
							{
								pollsCount++
							}
							else{
								def groupUserList =  po.sharedGroups.find{g -> (g.user.find{u -> u.id == user.id} != null)}
								if (groupUserList != null || po.createdBy.id == user.id)
									 pollsCount++
								 else if(po.sharedUsers*.id.contains(user.id) || po.createdBy.id == user.id)
										pollsCount++
								else if(po.sharedRoles.retainAll(currentUserRoles) || po.createdBy.id == user.id)
										pollsCount++
								else if(po.sharedDepts*.id.contains(user.department?.id) || po.createdBy.id == user.id)
										pollsCount++
							}
						}
					}
				}
			}
		}
		out << '<li>'
			out << '<a class="nav-icon icon-star" href="'+createLink(controller:'activityFeed', action:'index', id:'approval')+'">Approval<span>'+approvalsCount+'</span></a>'
		out << '</li>'
		out << '<li>'
			out << '<a class="nav-icon icon-star" href="'+createLink(controller:'activityFeed', action:'index', id:'survey')+'">Survey<span>'+surveysCount+'</span></a>'
		out << '</li>'
		out << '<li>'
			out << '<a class="nav-icon icon-star" href="'+createLink(controller:'activityFeed', action:'index', id:'poll')+'">Poll<span>'+pollsCount+'</span></a>'
		out << '</li>'
	
	}*/
	
	def inboxFeedCount = { attrs, body ->
		def totalCompFeeds
		def user1 = springSecurityService?.currentUser
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
		def rowCountShow="0"
		if(totalCompFeeds && totalCompFeeds[0])
		rowCountShow=totalCompFeeds[0]
		if(totalCompFeeds[0]>100){
			rowCountShow="100+"
		}
		out << '<li id="feedLink">'
			out << '<a class="nav-icon icon-house" style="width:135px;" onclick="loadScreenBlock()" href="'+createLink(controller:'dashboard', action:'index')+'">Inbox<span class="formInstCount"style="padding: 0 2px;">'+rowCountShow+'</span></a>'
		out << '</li>'
	}
	
	def tagList = { attrs, body ->
		def activityFeedList
		def activityFeedListCnt
		def totalCompFeeds
		def user = springSecurityService?.currentUser
		def currentUserRoles = user.authorities*.id.get(0)
		activityFeedList = ActivityFeed.list()
		//activityFeedList=ActivityFeed.executeQuery("select a from ActivityFeed as a where a.tenantId = ${session.user.userTenantId} and a.visibility='COMPANY' or (a.visibility='USER' and (a.shareId=${session.user.id} or a.createdBy='${session.user.id}') ) or (a.visibility='GROUP' or a.createdBy='${session.user.id}'  and exists ( from GroupDetails as g where g.id = a.shareId or (a.createdBy='${session.user.id}') ) ) or (a.visibility='Role' or a.createdBy='${session.user.id}' and exists (from Role as r where r.id = '${currentUserRoleId}' or (a.createdBy='${session.user.id}' )))")
		//activityFeedListCnt=ActivityFeed.executeQuery("select a from ActivityFeed as a where a.tenantId = ${session.user.userTenantId} and a.visibility='COMPANY' or (a.visibility='USER' and (a.shareId=${session.user.id} or a.createdBy='${session.user.id}') ) or (a.visibility='GROUP' or a.createdBy='${session.user.id}'  and exists ( from GroupDetails as g where g.id = a.shareId or (a.createdBy='${session.user.id}') ) ) or (a.visibility='Role' or a.createdBy='${session.user.id}' and exists (from Role as r where r.id = '${currentUserRoleId}' or (a.createdBy='${session.user.id}' )))")
		//def activityFeedSharedUserList = activityFeedList.get(0).sharedUsers
		// Code to check if a user does not belong to a group the company activity is not shown to the user
		def cnt=0
		def newList = new ArrayList()
		activityFeedListCnt = activityFeedList
		
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
				else if(it.sharedRoles*.id.contains(currentUserRoles) || it.createdBy.id == user.id)
						newList?.add(it)
				else if(it.sharedDepts*.id.contains(user.department?.id) || it.createdBy.id == user.id)
						newList?.add(it)
				else if(it.createdBy.id == user.id)
						newList?.add(it)
				else
					 cnt++
			}
		}
		def tagList = new ArrayList()
		def counter = 0;
	    activityFeedList = newList
		activityFeedList.each{ af->
			af?.tags.each{ t->
				if(!tagList.contains(t)){
					tagList.add(t)
					def tagDesc = t.length()<20?t:t.substring(0,19)+'..'
					out << '<li name="tagList" '+(counter<5?'':'style="display:none;"')+'>'
						out << '<a href="'+createLink(controller:'dashboard', action:'index', params:[filterTag:t])+'"  class="nav-icon icon-tag" onclick="loadScreenBlock()">'+tagDesc+'</a>'
					out << '</li>'
					counter++;
				}
				
			}
	    }
		
		if(counter > 5)
			out << '<span class="tagMoreMenu" onclick="showAllTags()">More Tags</span>'
		out << '<script>'
		out << 'function showAllTags(){$("[name=\'tagList\']").show();$(".tagMoreMenu").hide();}'
		out << '</script>'
		
	}
	
	def mobileTagList = { attrs, body ->
		def activityFeedList
		def activityFeedListCnt
		def totalCompFeeds
		def user = springSecurityService?.currentUser
		def currentUserRoles = user.authorities*.id.get(0)
		activityFeedList = ActivityFeed.list()
		//activityFeedList=ActivityFeed.executeQuery("select a from ActivityFeed as a where a.tenantId = ${session.user.userTenantId} and a.visibility='COMPANY' or (a.visibility='USER' and (a.shareId=${session.user.id} or a.createdBy='${session.user.id}') ) or (a.visibility='GROUP' or a.createdBy='${session.user.id}'  and exists ( from GroupDetails as g where g.id = a.shareId or (a.createdBy='${session.user.id}') ) ) or (a.visibility='Role' or a.createdBy='${session.user.id}' and exists (from Role as r where r.id = '${currentUserRoleId}' or (a.createdBy='${session.user.id}' )))")
		//activityFeedListCnt=ActivityFeed.executeQuery("select a from ActivityFeed as a where a.tenantId = ${session.user.userTenantId} and a.visibility='COMPANY' or (a.visibility='USER' and (a.shareId=${session.user.id} or a.createdBy='${session.user.id}') ) or (a.visibility='GROUP' or a.createdBy='${session.user.id}'  and exists ( from GroupDetails as g where g.id = a.shareId or (a.createdBy='${session.user.id}') ) ) or (a.visibility='Role' or a.createdBy='${session.user.id}' and exists (from Role as r where r.id = '${currentUserRoleId}' or (a.createdBy='${session.user.id}' )))")
		//def activityFeedSharedUserList = activityFeedList.get(0).sharedUsers
		// Code to check if a user does not belong to a group the company activity is not shown to the user
		def cnt=0
		def newList = new ArrayList()
		activityFeedListCnt = activityFeedList
		
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
				else if(it.sharedRoles*.id.contains(currentUserRoles) || it.createdBy.id == user.id)
						newList?.add(it)
				else if(it.sharedDepts*.id.contains(user.department?.id) || it.createdBy.id == user.id)
						newList?.add(it)
				else if(it.createdBy.id == user.id)
						newList?.add(it)
				else
					 cnt++
			}
		}
		def tagList = new ArrayList()
		activityFeedList = newList
		StringBuilder sb= new StringBuilder()
		def flag=true
		activityFeedList.each{ af->
				af?.tags.each{ t->
					if(!tagList.contains(t)){
						tagList.add(t)
						def tagDesc = t.length()<20?t:t.substring(0,19)+'..' 
						if(flag){
							flag=false
							sb << '<li name="tagList" data-theme="c" class="ui-btn ui-btn-icon-right ui-li ui-corner-bottom ui-btn-hover-c"><div class="ui-btn-inner ui-li ui-corner-bottom"><div class="ui-btn-text">'
							sb << '<a class="ui-link-inherit" href="'+createLink(controller:'activityFeed', action:'index', params:[id:t])+'">'+tagDesc+'</a>'
							sb << '</div><span class="ui-icon ui-icon-arrow-r"></span></div></li></li>'
						}else{
							out << '<li name="tagList" data-theme="c" class="ui-btn ui-btn-icon-right ui-li ui-btn-hover-c"><div class="ui-btn-inner ui-li "><div class="ui-btn-text">'
							out << '<a class="ui-link-inherit" href="'+createLink(controller:'activityFeed', action:'index', params:[id:t])+'">'+tagDesc+'</a>'
							out << '</div><span class="ui-icon ui-icon-arrow-r"></span></div></li></li>'
						}
					}
				}
			}
		out<<sb.toString();
	}
	
	def mobileInboxFeedCount = { attrs, body ->
		def activityFeedList
		def activityFeedListCnt
		def totalCompFeeds
		def user = springSecurityService?.currentUser
		def currentUserRoles = user.authorities*.id.get(0)
		activityFeedList = ActivityFeed.list()
		//activityFeedList=ActivityFeed.executeQuery("select a from ActivityFeed as a where a.tenantId = ${session.user.userTenantId} and a.visibility='COMPANY' or (a.visibility='USER' and (a.shareId=${session.user.id} or a.createdBy='${session.user.id}') ) or (a.visibility='GROUP' or a.createdBy='${session.user.id}'  and exists ( from GroupDetails as g where g.id = a.shareId or (a.createdBy='${session.user.id}') ) ) or (a.visibility='Role' or a.createdBy='${session.user.id}' and exists (from Role as r where r.id = '${currentUserRoleId}' or (a.createdBy='${session.user.id}' )))")
		//activityFeedListCnt=ActivityFeed.executeQuery("select a from ActivityFeed as a where a.tenantId = ${session.user.userTenantId} and a.visibility='COMPANY' or (a.visibility='USER' and (a.shareId=${session.user.id} or a.createdBy='${session.user.id}') ) or (a.visibility='GROUP' or a.createdBy='${session.user.id}'  and exists ( from GroupDetails as g where g.id = a.shareId or (a.createdBy='${session.user.id}') ) ) or (a.visibility='Role' or a.createdBy='${session.user.id}' and exists (from Role as r where r.id = '${currentUserRoleId}' or (a.createdBy='${session.user.id}' )))")
		//def activityFeedSharedUserList = activityFeedList.get(0).sharedUsers
		// Code to check if a user does not belong to a group the company activity is not shown to the user
		def cnt=0
		def newList = new ArrayList()
		activityFeedListCnt = activityFeedList
		
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
				else if(it.sharedRoles*.id.contains(currentUserRoles) || it.createdBy.id == user.id)
						newList?.add(it)
				else if(it.sharedDepts*.id.contains(user.department?.id) || it.createdBy.id == user.id)
						newList?.add(it)
				else if(it.createdBy.id == user.id)
						newList?.add(it)
				else
					 cnt++
			}
		}
		
		activityFeedList = newList
		totalCompFeeds = activityFeedListCnt.size() - cnt
		def rowCountShow="0"
		rowCountShow=totalCompFeeds
		if(totalCompFeeds>100){
			rowCountShow="100+"
		}
		out << '<li id="feedLink">'
			out << '<a class="nav-icon icon-house" href="'+createLink(controller:'dashboard', action:'index')+'">Inbox</a><span class="ui-li-count">'+rowCountShow+'</span>'
		out << '</li>'
	}
	
	def ifNotFormExist = {attrs, body ->
		def config = attrs?.config
		def instanceId = attrs?.shareId
		def formName 
		def form 
		def formInstance
		if(!config?.configName.equalsIgnoreCase("content") && !config?.shareType.equalsIgnoreCase("Email")){
			formName = config?.configName?.substring(0,config?.configName?.indexOf("."))
			form = Form.findByName(formName)
		}
		if(form){
			formInstance = sqlDomainClassService.get(instanceId, form)
		}
		if((!form && !config?.configName.equalsIgnoreCase("content") && !config?.shareType.equalsIgnoreCase("Email")) || (!formInstance && !config?.configName.equalsIgnoreCase("content") && !config?.shareType.equalsIgnoreCase("Email"))){
			out << body()
		}
		
	}
	
	def ifFormExist = {attrs, body ->
		def config = attrs?.config
		def instanceId = attrs?.shareId
		def formName 
		def form 
		def formInstance
		if(!config?.configName.equalsIgnoreCase("content") && !config?.shareType.equalsIgnoreCase("Email")){
			formName = config?.configName?.substring(0,config?.configName?.indexOf("."))
			form = Form.findByName(formName)
		}
		if(form){
			formInstance = sqlDomainClassService.get(instanceId, form)
		}
		if((!form && !config?.configName.equalsIgnoreCase("content") && !config?.shareType.equalsIgnoreCase("Email")) || (!formInstance && !config?.configName.equalsIgnoreCase("content") && !config?.shareType.equalsIgnoreCase("Email"))){
			//Do nothing
		}else{
			out << body()
		}
		
	}
	
}
