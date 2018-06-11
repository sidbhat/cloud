

package com.oneapp.cloud.core

import grails.plugin.multitenant.core.util.TenantUtils
import org.grails.formbuilder.*;
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
//import com.oneapp.cloud.core.social.Subscribe

class UserController {

	def clientService
	def domainClassService
	
    def index = { 
		params.view = "list"
		redirect(action: "list", params: params) 
		}
    def mailService
    def calendarOneAppService

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

   def springSecurityService
   def getSessionUser() {
    return springSecurityService.currentUser
   }
    @grails.plugins.springsecurity.Secured("ROLE_USER") 
 	def image = {
        def userInstance = User.get(params.id)
        response.setContentType("image/jpeg")
        response.outputStream << userInstance.picture
    }
    
    def list = {

       UserProfile up = UserProfile.findByUser(getSessionUser())
        def max = 10
        if ( up?.numOfRows )
        	max = up?.numOfRows 
        params.max = Math.min(params.max ? params.max.toInteger() : max , 100)
		params.offset = params.offset?:0
      

        // Sort and order is only available in list_a view
        if (!params.sort)
            params.sort = 'dateCreated'
       // else
       //     params.view = "list_a"
        if (!params.order)
        	params.order = 'desc'
      //  else
       //     params.view = "list_a"

		
        def user = getSessionUser()
		def userInstanceTotal
		def userList
		def isRoleUser
		def ur = UserRole.findAllByRole(Role.findByAuthority(Role.ROLE_SUPER_ADMIN))
        if (user?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)) {
			userList = User.createCriteria().list(sort:params.sort,offset:params.offset,max:params.max,order:params.order){
				ne 'userTenantId',0
			}
        } else if (user?.authorities?.authority.contains(Role.ROLE_TRIAL_USER)) {
			userList = [user]
			userInstanceTotal = 1
		} else if(user?.authorities?.authority.contains(Role.ROLE_ADMIN)) {
			userList = User.createCriteria().list(sort:params.sort,offset:params.offset,max:params.max,order:params.order){
				eq 'userTenantId',user?.userTenantId
				if(ur){
					not{
						'in' "id",ur.user.id
					}
				}
			}
		} else {
			def allUserList = new ArrayList()
			userList = User.createCriteria().list(sort:params.sort,offset:params.offset,max:params.max,order:params.order){
				eq 'userTenantId',user?.userTenantId
				eq 'enabled',true
				if(ur){
					not{
						'in' "id",ur.user.id
					}
				}
			}
		}
		if(user?.authorities?.authority.contains(Role.ROLE_USER)){
			isRoleUser = true
		}
            
		if (params.view == "list"){
			[userInstanceList: userList, userInstanceTotal: userInstanceTotal?:userList?.totalCount,isRoleUser:isRoleUser]
		}else{
			render(view: "list_a", model: [userInstanceList: userList, userInstanceTotal: userInstanceTotal?:userList?.totalCount])
		}

    }

    // Mass email action
    def m_email = {

        // This will (re)generate a password and email it to the user
        def user
        int total = 0
        params.each { p ->
            if (p['key'].toString().startsWith('r_')) {
                def size = p['key'].size() - 1
                user = p['key'].toString()[2..size]
             
                if (user != null) {
                    def userInstance = User.get(user)
                    if (userInstance) {
                        // Generate a random password for the user
                        def password = randomPass()
                        userInstance.password = springSecurityService.encodePassword(password)
                        //sendMail(userInstance.username, password)
                        // sending mail asynchronously now
                	AsynchronousEmailStorage async = new AsynchronousEmailStorage()
                	async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
                	async.emailTO = "${userInstance.username}"
                	async.emailSubject="Password Reset"
                	async.emailData="Hello ${userInstance.firstName},\n\n Your password was reset. \n Please login to ${grailsApplication.config.grails.serverURL} using following credentials-\n Username: ${userInstance.username} \n Password: ${password} \n\nThank you,\nYour Form Builder Team"
                	async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
                	async.save(flush:true)
                        total++
                        userInstance.save()
                    }
                }

            }

        }
        flash.message = "Email sent to ${total} user(s)."
        flash.args = [params.id]
        flash.defaultMessage = "Login information sent to ${total} user(s)."

        redirect(action: "list")
    }
    def copy = {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "user.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "User not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            def newuserInstance = new User()
            newuserInstance = userInstance
            render(view: "create", model: [userInstance: newuserInstance])
        }
    }

   /* def follow = {
        if (params.id) {
            def subscribe = new Subscribe()
            subscribe.user = User.get(getSessionUser().id) // Current user
            subscribe.subscriptionId = params.id // Add the id of the user to follow
            subscribe.subscriptionType = DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.SUBSCRIPTION_TYPE, "USER")
            subscribe.frequency = "NOW"
            // Check for duplicates
            Subscribe d = Subscribe.findByUserAndSubscriptionId(subscribe.user, params.id)
            if (d == null) {
                subscribe.save()
                flash.message = "User already followed..."
                flash.args = [params.id]
                flash.defaultMessage = "User already followed..."
            } else {
                flash.message = "User followed..."
                flash.args = [params.id]
                flash.defaultMessage = "User followed..."
            }
            redirect(action: "list")
        }

    }
    
    

    def unfollow = {
        if (params.id) {
            def user = User.get(getSessionUser().id)
            def followedUser = params.id
            if (user) {
                def s = Subscribe.findByUserAndSubscriptionId(user, followedUser)
                if (s)
                s.delete()
                flash.message = "User unfollowed..."
                flash.args = [params.id]
                flash.defaultMessage = "User unfollowed..."
                redirect(action: "list")
            }
        }
    }*/
	@grails.plugins.springsecurity.Secured("ROLE_USER")
    def create = {
		def clientId = params.clientId
		def reportsToList
		def departmentList
		def ur = UserRole.findByUser(springSecurityService.currentUser)
		def userInstance = new User()
		if(clientId){
			TenantUtils.doWithTenant(clientId.toInteger()) {
				reportsToList = User.findAllByUserTenantIdAndEnabled(clientId,true)
				departmentList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
			}
		}else{
			userInstance.properties = params
			if(ur.role.authority == Role.ROLE_TRIAL_USER){
				reportsToList = User.findAllByUsername(springSecurityService.currentUser.username)
			}else{
				reportsToList = User.findAllByUserTenantIdAndEnabled(springSecurityService.currentUser.userTenantId,true)
			}
			departmentList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
		}
       
        return [userInstance: userInstance,reportsToList:reportsToList,departmentList:departmentList,clientId:clientId]
    }

	
    

	@grails.plugins.springsecurity.Secured("ROLE_USER")
    def save = {
        // Save the username in the email field
        params.email = params.username
        def userInstance = new User(params)
        def session_user = getSessionUser()
		if (!session_user?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)){
			userInstance.userTenantId = session_user.userTenantId
		}
		def clientInstance = Client.get(userInstance.userTenantId)
		def totalExistingUsers = clientService.getTotalUsers(userInstance.userTenantId)
		        // Add the client id if the user is not super admin
		        // Only super admins can choose what tenant a user belongs to
		if(totalExistingUsers < clientInstance.maxUsers){
		        
		        // Capitalize first and last name
		        if (userInstance.firstName)
		        userInstance.firstName = userInstance.firstName.replaceAll(/\w+/, { it[0].toUpperCase() + ((it.size() > 1) ? it[1..-1] : '') })
		        if (userInstance.lastName)
		        userInstance.lastName = userInstance.lastName.replaceAll(/\w+/, { it[0].toUpperCase() + ((it.size() > 1) ? it[1..-1] : '') })
		
				userInstance.password = 'password'
		        if (!userInstance.hasErrors() && userInstance.save(flush:true)) {
		            flash.message = "user.created"
		            flash.args = [userInstance.username]
		            flash.defaultMessage = "User ${userInstance.username} created"
		
		            // create user role mapping
		            UserRole ur = new UserRole(user: userInstance, role: Role.findById(params.authority.id))
		            ur.save(flush:true)
		            //update  calendar
		            if (userInstance.dob != null) {
		                calendarOneAppService?.updateCalendar(userInstance.id, 'User')
		            }
					try{
						TenantUtils.doWithTenant(userInstance.userTenantId) {
							def allUsers = User.findAllByUserTenantId(userInstance.userTenantId)
							def cAdmins = allUsers.findAll{it.authorities*.authority.contains(Role.ROLE_ADMIN)}
							if(cAdmins.size()==1){
								def defaultCreatedRuleSets = RuleSet.executeQuery("from RuleSet where createdBy = null and tenantId = "+userInstance.userTenantId)
								defaultCreatedRuleSets.each{
									it.createdBy = userInstance
									it.save(flush:true)
								}
							}
						}
					}catch(Exception e){
						log.error e
					}
		            redirect(action: "edit", id: userInstance.id)
		        }
		        else {
		            render(view: "create", model: [userInstance: userInstance])
		        }
			}else{
				flash.message = "client.maxUser.limit "
				flash.args = [userInstance.username]
				flash.defaultMessage = "You have reached the maximum user creation Limit.  Click <a href='${request.getContextPath()}/dropDown/clientUsage'>here</a> to upgrade your account "
				redirect(action:"list")
			}
    }
	@grails.plugins.springsecurity.Secured("ROLE_USER")
    def show = {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "user.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "User not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            // Get the client for the user
            def client = Client.findById(userInstance.userTenantId)
            return [userInstance: userInstance, client: client]
        }
    }
@grails.plugins.springsecurity.Secured("ROLE_USER")
    def edit = {
        def userInstance = User.get(params.id)
        if (!userInstance) {
            flash.message = "user.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "User not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
			def	reportsToList
			def ur = UserRole.findByUser(springSecurityService.currentUser)
			if(ur.role.authority == Role.ROLE_TRIAL_USER){
				reportsToList = User.findAllByUsername(springSecurityService.currentUser.username)
			}else{
				reportsToList = User.findAllByUserTenantIdAndEnabled(springSecurityService.currentUser.userTenantId,true)
			}
			def	departmentList = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
            return [userInstance: userInstance,reportsToList:reportsToList,departmentList:departmentList]
        }
    }
@grails.plugins.springsecurity.Secured("ROLE_USER")
    def update = {
        def userInstance = User.get(params.id)
        if (userInstance) {
			try{
//	            if (params.version) {
//	                def version = params.version.toLong()
//	                if (userInstance.version > version) {
//	
//	                    userInstance.errors.rejectValue("version", "user.optimistic.locking.failure", "Another user has updated this User while you were editing")
//	                    render(view: "edit", model: [userInstance: userInstance])
//	                    return
//	                }
//	            }
				def userEnabled = params.enabled
				def clientInstance = Client.get(userInstance.userTenantId)
				if(userEnabled && clientInstance && !userInstance.enabled){
					def totalExistingUsers = clientService.getTotalUsers(userInstance.userTenantId)
					if(totalExistingUsers >= clientInstance.maxUsers){
						throw new Exception("You have reached the maximum user creation Limit. Please contact your Administrator")
					}
				}
	            userInstance.reportsTo = null
	            userInstance.properties = params
					
	            if (!userInstance.hasErrors() && userInstance.save(flush:true,validate:true)) {
	                flash.message = "user.updated"
	                flash.args = [userInstance.username]
	                flash.defaultMessage = "User ${userInstance.username} updated"
	
	                // update user role mapping
	                UserRole.removeAll(userInstance)
	                UserRole ur = new UserRole(user: userInstance, role: Role.findById(params.authority))
	                ur.save(flush:true,validate:true)
						
	                //update  calendar
	                if (userInstance.dob != null) {
	                    calendarOneAppService?.updateCalendar(userInstance.id, 'User')
	                }
					try{
						TenantUtils.doWithTenant(userInstance.userTenantId) {
							def allUsers = User.findAllByUserTenantId(userInstance.userTenantId)
							def cAdmins = allUsers.findAll{it.authorities*.authority.contains(Role.ROLE_ADMIN)}
							if(cAdmins.size()==1){
								def defaultCreatedRuleSets = RuleSet.executeQuery("from RuleSet where createdBy = null and tenantId = "+userInstance.userTenantId)
								defaultCreatedRuleSets.each{
									it.createdBy = userInstance
									it.save(flush:true)
								}
							}
						}
					}catch(Exception e){
						log.error e
					}
					if(userInstance.id !=  springSecurityService.currentUser.id)
					{
						redirect(action: "edit", id: userInstance.id)
					}else
					{
						redirect(controller: "logout")
					}
	            }
	            else {
	                render(view: "edit", model: [userInstance: userInstance])
	            }
	        }catch(Exception ex){
				flash.message = "client.maxUser.limit"
				flash.args = [userInstance.username]
				flash.defaultMessage = "You have reached the maximum user creation Limit. Please contact your Administrator"
				redirect(action:"list")
	        }
        }
        else {
            flash.message = "user.not.found"
            flash.args = [userInstance.username]
            flash.defaultMessage = "User not found with username ${userInstance.username}"
            redirect(action: "edit", id: params.id)
        }
    }
@grails.plugins.springsecurity.Secured("ROLE_USER")
    def delete = {
        def userInstance = User.get(params.id)
        if (userInstance) {
			def username = userInstance.username
			def superUsersList = UserRole.findAllByRole(Role.findByAuthority(Role.ROLE_SUPER_ADMIN))?.user
			def superUsers = ""
			superUsersList.each{
				superUsers += it.username
			}
            try {
				User.withTransaction(){
					/*UserRole.removeAll(userInstance)
					def userProfileInstance = UserProfile.findByUser(userInstance)
					if(userProfileInstance)
						userProfileInstance.delete()*/
					
					def userProfileInstance = UserProfile.findByUser(userInstance)
					if(userProfileInstance){
						userProfileInstance.emailSubscribed = false
						userProfileInstance.save()
					}else{
						def userProfileInst = new UserProfile()
						userProfileInst.user = userInstance
						userProfileInst.emailSubscribed=false
						userProfileInst.save()
					}

					userInstance.enabled=false
					if(!userInstance.hasErrors() && userInstance.save(flush:true)){
						calendarOneAppService?.deleteCalendarEvent(userInstance.id, 'User')
						if(springSecurityService.currentUser.username.equalsIgnoreCase(userInstance.username)){
							flash.message = "user.selfdelete"
							flash.args = [superUsers]
							flash.defaultMessage = "Your account has been deactivated to activate your account contact your admin or super admin at {0}"
							redirect(controller: "logout")
						}else{
							flash.message = "user.deleted"
							flash.args = [username]
							flash.defaultMessage = "User ${username} deactivated"
							redirect(action: "list")
						}
					}
					// Delete calendar event
					
				}
	
					
			}catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "user.not.deleted"
                flash.args = [username]
                flash.defaultMessage = "User ${username} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "user.not.found"
            flash.args =[userInstance.username]
            flash.defaultMessage = "User not found with id ${params.id}"
            redirect(action: "list")
        }
    }


    

    // Generate a random password for the user
    String randomPass() {
        UUID uuid = UUID.randomUUID()
        uuid.toString()[0..7]
    }


    def sendMail(user, pass) {

        mailService.sendMail {
            from grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom
            to user
            title "Your login information"
            body "Please login with the following userid: ${user} password: ${pass}"
        }


    }

	def loginWith={
		springSecurityService.reauthenticate(params.j_username)
		redirect uri:SpringSecurityUtils.securityConfig.successHandler.defaultTargetUrl
		//render "done"
	}

}
