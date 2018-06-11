

package com.oneapp.cloud.core
import com.oneapp.cloud.core.*
import com.oneapp.cloud.crm.*
import grails.converters.JSON

class GroupsController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def springSecurityService
  def assign = {
  
   def group = GroupDetails.get(params.id)
   def userList 
   def contactList
   
   if ( group ){
  	    userList  = User.findAllByUserTenantIdAndEnabled(springSecurityService.currentUser?.userTenantId,true)
  	    contactList = ContactDetails.list()
  	    
  		 group.user.each{ mt->
        	 userList.remove(mt)
		 }
		 group.contacts.each{ mt->
        	 contactList.remove(mt)
		 }
   	}
   render view: 'assignToGroup', model: [groupList: group, userList: userList,contactList:contactList] 
  
  }
  
  
 /* def assignToGroup = {
	
    String userAndGroup = params.data
    String group = userAndGroup.split('@')[0]
    String user = userAndGroup.split('@')[1]
    String groupId = group.split('-')[1]
    String userId = user.split('-')[1]
    GroupDetails groupInstance = GroupDetails.get(Long.parseLong(groupId))
    
    ContactDetails contactInstance
    User userInstance
    if ( params.type == "ContactDetails" ){
   	    contactInstance = ContactDetails.get(Long.parseLong(userId))
   	    groupInstance.addToContacts(contactInstance)
    }else{
    	userInstance = User.get(Long.parseLong(userId))
    	groupInstance.addToUser(userInstance)
    }
    
    render([status: "OK"] as JSON)
  }*/

  def assignToGroup = {
	def groupId = params.groupId
	def userData = params.list('groupUsers')
	GroupDetails groupInstance = GroupDetails.get(Long.parseLong(groupId))
	try{
		if(groupInstance){
			if(userData){
				groupInstance?.user=[]
				userData.each{
					User userInstance = User.get(Long.parseLong(it))
					groupInstance.addToUser(userInstance)
				}
			}
		}
		flash.message = "groups.assignment.success"
		flash.args = [groupInstance.groupName]
		flash.defaultMessage = "User successfully added to {groupInstance.groupName} Group"
		redirect(action: "index",params:params)
	}catch(Exception ex){
		flash.message = "groups.assignment.fail"
		flash.args = [groupInstance.groupName]
		flash.defaultMessage = "Error occured while adding user to {groupInstance.groupName} Group"
		redirect(action: "assign",id:params.groupId)
	}
  }
  
    def list = {
		def groupsInstanceList
		def groupsInstanceTotal
		params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
		def userRole = springSecurityService.currentUser.authorities*.authority
		groupsInstanceList = GroupDetails.list(params)
		groupsInstanceTotal = GroupDetails.count()
        [groupsInstanceList: groupsInstanceList, groupsInstanceTotal: groupsInstanceTotal]
    }

    def create = {
        def groupsInstance = new GroupDetails()
        groupsInstance.properties = params
        return [groupsInstance: groupsInstance]
    }

    def save = {
        def groupsInstance = new GroupDetails(params)
		groupsInstance.createdBy = springSecurityService.currentUser
        if (!groupsInstance.hasErrors() && groupsInstance.save()) {
        	// Assign the person creating the group
        	def userInstance = springSecurityService.currentUser
       		groupsInstance.addToUser(userInstance)
       		flash.message = "GroupDetails.created"
            flash.args = [groupsInstance.id]
            flash.defaultMessage = "Group Details ${groupsInstance.id} created"
            redirect(action: "edit", id: groupsInstance.id)
        }
        else {
            render(view: "create", model: [groupsInstance: groupsInstance])
        }
    }

   
    def edit = {
        def groupsInstance = GroupDetails.get(params.id)
        if (!groupsInstance) {
            flash.message = "GroupDetails.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Group Details not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [groupsInstance: groupsInstance]
        }
    }

    def update = {
        def groupsInstance = GroupDetails.get(params.id)
        if (groupsInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (groupsInstance.version > version) {

                    groupsInstance.errors.rejectValue("version", "GroupDetails.optimistic.locking.failure", "Another user has updated this Group Details while you were editing")
                    render(view: "edit", model: [groupsInstance: groupsInstance])
                    return
                }
            }
            groupsInstance.properties = params
            if (!groupsInstance.hasErrors() && groupsInstance.save()) {
                flash.message = "GroupDetails.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Group Details ${params.id} updated"
                redirect(action: "edit", id: groupsInstance.id)
            }
            else {
                render(view: "edit", model: [groupsInstance: groupsInstance])
            }
        }
        else {
            flash.message = "GroupDetails.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Group Details not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def groupsInstance = GroupDetails.get(params.id)
        if (groupsInstance) {
            try {
				List<ActivityFeed> afList = ActivityFeed.createCriteria().list(){
					sharedGroups{
						eq 'id',groupsInstance.id
					}
				}
				afList.each{af->
					def afGroups = af.sharedGroups
					af.sharedGroups = []
					afGroups.remove(groupsInstance)
					af.sharedGroups = afGroups
				}
                groupsInstance.delete()
                flash.message = "GroupDetails.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Group Details ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error e
                flash.message = "GroupDetails.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Group Details ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "GroupDetails.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Group Details not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
