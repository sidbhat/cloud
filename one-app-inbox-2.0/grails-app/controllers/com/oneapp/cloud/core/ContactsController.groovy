
package com.oneapp.cloud.core

import grails.plugins.springsecurity.Secured
import twitter4j.*;
import com.google.code.linkedinapi.client.*

@Secured("ROLE_USER")
class ContactsController {
	
	def springSecurityService
	def index = { redirect(action: "list", params: params) }
	def socialFeedService
	
	def list = {
	
		def contactList = null	
		def totalContacts = ContactDetails.count()	
		params.sort = 'contactName'
		params.max = Math.min( params.max ? params.max.toInteger() : 10,  100)
		render view:'/user/list_b', model: [contactList:ContactDetails.findAllByCreatedByOrPersonal(springSecurityService.currentUser, false), totalContacts:ContactDetails.findAllByCreatedByOrPersonal(springSecurityService.currentUser, false).size()]
		
	}
	
	
	def create = {
        def contactInstance = new ContactDetails()
        contactInstance.properties = params
        render ( view:"/user/contactCreate", model: [contactInstance: contactInstance])
    
	}
	def save = {
	    def contactInstance = new ContactDetails(params)
	    contactInstance.user = springSecurityService.currentUser.username
        if (!contactInstance.hasErrors() && contactInstance.save()) {
            flash.message = "Contacts.created"
            flash.args = [contactInstance.id]
            flash.defaultMessage = "Contact ${contactInstance.id} created"
            redirect(action: "edit", id: contactInstance.id)
        }
        else {
            render(view: "/user/contactCreate", model: [contactInstance: contactInstance])
        }
	
	}
	def edit = {
        def contactInstance = ContactDetails.get(params.id)
        if (!contactInstance) {
            flash.message = "ContactDetails.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ContactDetails not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            render(view: "/user/contactEdit", model: [contactInstance: contactInstance])
        }
    }
    
    def update = {
        def contactInstance = ContactDetails.get(params.id)
        if (contactInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (contactInstance.version > version) {

                    contactInstance.errors.rejectValue("version", "ContactDetails.optimistic.locking.failure", "Another user has updated this GroupDetails while you were editing")
                    render(view: "edit", model: [contactInstance: contactInstance])
                    return
                }
            }
            contactInstance.properties = params
            if (!contactInstance.hasErrors() && contactInstance.save()) {
                flash.message = "ContactDetails.updated"
                flash.args = [params.id]
                flash.defaultMessage = "ContactDetails ${params.id} updated"
                redirect(action: "edit", id: contactInstance.id)
            }
            else {
                 render(view: "/user/contactEdit", model: [contactInstance: contactInstance])
            }
        }
        else {
            flash.message = "ContactDetails.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ContactDetails not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def contactInstance = ContactDetails.get(params.id)
        if (contactInstance) {
            try {
                contactInstance.delete()
                flash.message = "ContactDetails.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "ContactDetails ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "ContactDetails.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "ContactDetails ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "ContactDetails.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "ContactDetails not found with id ${params.id}"
            redirect(action: "list")
        }
    }
    
    
	def group = {
		
		def contactList = ContactDetails.findAllByUser (springSecurityService.currentUser.username,[sort:"contactName"])
		render template:'addGroup', model: [contactList:contactList]		
	}
	
	def createGroup = {
		
		def groupName = params['groupname']
		def groupnameList = params['groupid'].toString().replace("[","").replace("]","")
		
		String [] arr_contact = groupnameList.split(",")
		
		def groupList = GroupDetails.findWhere (user:springSecurityService.currentUser.username, groupName:groupName)
		def contactList = null
		if(groupList == null){
			
			for(int i=0; i<arr_contact.length;i++){
				contactList = ContactDetails.findWhere(user:springSecurityService.currentUser.username, contactId:arr_contact[i].trim())
				if(contactList != null){
					def groupDetails = new GroupDetails()
					//groupDetails.user = springSecurityService.currentUser.username
					groupDetails.groupName = groupName
					groupDetails.groupContactId = arr_contact[i].trim()
					groupDetails.groupContactName = contactList.contactName
					groupDetails.source = contactList.source
					
					GroupDetails.withTransaction {
						groupDetails = groupDetails.save( flush : true )
					}
					println "Saved: " + groupDetails					
				}
			}			
		}else {
			println("this groupname already exists")
		}
		def groupDetailsList = GroupDetails.findWhere(user:springSecurityService.currentUser.username)
		render template:'viewGroup', model: [groupDetailsList:groupDetailsList]	
	}
	
	def viewGroup = {
		
		def groupList = GroupDetails.findAllByUserOrPersonal (springSecurityService.currentUser.username,false)
	}
	
	def listgroups = {
		String username=springSecurityService.currentUser.username
		def groupList = GroupDetails.findAllByUser (username)
		//println "total no of groups "+groupList.size
		
		render view:'listgroups', model: [groupList:groupList]	
	}
	
	def creategroup = {
		String username=springSecurityService.currentUser.username
		
		
		def newGroupDeatils=new GroupDetails()
		newGroupDeatils.user=username
		newGroupDeatils.groupName=params['groupName']
		newGroupDeatils.groupDescription=params['groupDescription']
		newGroupDeatils.createdOn=new Date()
		
		if (newGroupDeatils.groupName==null || newGroupDeatils.groupName==""){
			println "Group Name is empty"
			redirect (controller:"contacts",action:"listgroups")
			return
		}
		
		GroupDetails.withTransaction {
			newGroupDeatils = newGroupDeatils.save( flush : true )
		}
		
		//println "Saved: " + newGroupDeatils
		
		def groupList = GroupDetails.findAllByUser (username)
		//println "total no of groups "+groupList.size
		
		render view:'listgroups', model: [groupList:groupList]
	}
	
	def deletegroup = {
		String username=springSecurityService.currentUser.username
		
		//println "deleting group with id "+params['id']
		def groupdetail=GroupDetails.get(params['id'])
		
		groupdetail.delete(flush: true)
		
		redirect (controller:"contacts",action:"listgroups")
		
	}
	
	def groupContactDetails = {
		String username=springSecurityService.currentUser.username
		
		//println "getting deatils for group with id "+params['id']
		def groupdetail=GroupDetails.get(params['id'])
        
		render template:'groupContactDetails', model: [groupdetail:groupdetail]
		
	}
	
	def addContactToGroup = {
		String username=springSecurityService.currentUser.username
		
		//println "addContactToGroup with group id "+params['groupid']
		
		//println "addContactToGroup with contact id "+params['contactid']
		def contactdetail=ContactDetails.get(params['contactid'])
		def groupdetails=GroupDetails.get(params['groupid'])
		groupdetails.contacts.add(contactdetail);
		
		GroupDetails.withTransaction {
			groupdetails = groupdetails.save( flush : true )
		}
		
		//println "Contact added"
		
		
		redirect (controller:"contacts",action:"listgroups")
		
	}
	
	def contactDetails = {
		String username=springSecurityService.currentUser.username
		
		//println "getting deatils for contact with id "+params['id']
		def contactdetail=ContactDetails.get(params['id'])
		
		def groupList = GroupDetails.findAllByUser (username)
		
		render template:'contactDetails', model: [contactdetail:contactdetail,groupList:groupList]
		
	}
	
	def contactSendMessage = {
	
		def contactdetail=ContactDetails.get(params['to'])
		def message = params['message']
		
		if ( contactdetail != null && message != null && message.length() != 0 )
		{
			if (contactdetail.source == "twitter")
			{
				 socialFeedService.sendDirectMessage(session.username,contactdetail.contactId, message);
			}
		
		}
	
	}
	  // Note : redirection from a view will not work
    def afterInterceptor = { model, modelAndView ->
  		if (request['isMobile'] && modelAndView != null ) {
  			println "ContactsController-afterInterceptor: "+request['isMobile'] 
  			modelAndView.viewName = modelAndView.viewName + "_m"
 	 }
	}
	
}
