

package com.oneapp.cloud.core

import com.google.appengine.repackaged.com.google.common.base.Flag.Boolean;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

import grails.converters.JSON;
import grails.plugin.multitenant.core.util.TenantUtils

class RegisterController extends grails.plugins.springsecurity.ui.RegisterController {

	def clientDataLoadService

	def changePassword={
		def userNameInstance = session.user?.username
		[userNameInstance:userNameInstance]
	}
	def registerUser={
	}
	
	def userEmailVerificationApiV1={
		UserVerification uv 
		if(params.id)
			uv=  UserVerification.findById(params.id)     
		def mgs= [error:"Verification link is invalide/expired."]
		if(uv){
			def temp=(32*60*1000)+uv.dateCreated.time 
			mgs=[error:"Verification link is invalide"]
				if(temp>= new Date().time){
					User userV=User.findByApiKey(uv.apiKey)
					userV.accountLocked=false
					userV.save();
					uv.delete();
					mgs=[message:"User email verified"]
				}
			}
		render mgs as JSON 
	}

	def userContactDetail = {
		def username = springSecurityService.currentUser.username
		def companyName = username.substring((username.indexOf("@")+1), username.size())
		[username:username,companyName:companyName, phoneNumber:'']
	}

	def updateDetails = {
		def view = "/register/userContactDetail"
		boolean isValidNumber = false
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		def phoneNumber = params.mobilePhone
		def username = springSecurityService.currentUser.username
		def companyName = username.substring((username.indexOf("@")+1), username.size())
		try{
			if(phoneNumber){
				PhoneNumber usNumberProto = phoneUtil.parse(phoneNumber, "ZZ")
				isValidNumber = phoneUtil.isValidNumber(usNumberProto)
			}
		}catch(Exception ex){
		}
		if(isValidNumber){
			def userInstance = springSecurityService.currentUser
			userInstance.mobilePhone = phoneNumber
			userInstance.save(flush:true)
			flash.defaultMessage = "Phone Number Updated"
			flash.message = flash.defaultMessage
			render(view: '/home/welcome', model: [username: username, companyName: companyName, phoneNumber:phoneNumber])
		}else{
			if(phoneNumber){
				flash.defaultMessage = "Enter a valid mobile number!"
				flash.message = flash.defaultMessage
			}else{
				flash.defaultMessage = "Mobile number cannot be empty!"
				flash.message = flash.defaultMessage
			}
			render(view: view,model: [username: username, companyName: companyName, phoneNumber:phoneNumber])
		}
		
	}

	def changeUserPassword = {
		def username
		if(params.j_username) {
			username = params.j_username
		}else{
			username = session.user?.username
		}
		def userInstance = User.findByUsername(username)
		def view = "/register/changePassword"

		if (userInstance) {
			try {
				// Check to see if the user password is correct
				if (springSecurityService.encodePassword(params.old_password) != userInstance.password) {
					flash.message = "user.password.changed"
					flash.args = [params.j_username]
					flash.defaultMessage = "Incorrect old password"
				}
				// Check if password and password confirmation matches
				else if (params.password != params.password2) {
					flash.message = "user.password.changed"
					flash.args = [params.j_username]
					flash.defaultMessage = "New passwords does not match"
				}
				// Save the new password
				else {
					userInstance.password = springSecurityService.encodePassword(params.password)
					userInstance.save()
					flash.message = "user.password.changed"
					flash.args = [params.j_username]
					flash.defaultMessage = "Password reset"
				}
				render(view: view, id: params.id, model: [userNameInstance: username])
			}
			catch (Exception e) {
				log.error e
				flash.message = "password.not.set"
				flash.args = [params.j_username]
				flash.defaultMessage = "Password not reset"
				render(view: view, id: params.id, model: [userNameInstance: username])
			}
		}
		else {
			flash.message = "user.not.found"
			flash.args = [params.j_username]
			flash.defaultMessage = "User not found with name ${params.j_username}"
			render(view: view, model: [userNameInstance: username])
		}


	}

	def forgotPassword = {

	}

	def email = {
		def userInstance
		def resetPassword = params.resetPassword
		def view = "/register/forgotPassword"
		// Check if coming from forgotPassword page
		try{
			if (params.j_username != null) {
				userInstance = User.findByUsername(params.j_username)
				if (userInstance == null) {
					flash.message = "User not found."
					flash.defaultMessage =  flash.message
					redirect(controller:"register",action: "forgotPassword")
				}
			}
			else
				userInstance = User.get(params.id)

			// Generate a random password for the user
			def password = randomPass()
			userInstance.password = getEncodedRandomPassword(password)

			// Save the userInstance and send password in email
			if (!userInstance.hasErrors() && userInstance.save()) {
				// Send password in email to the user
				//def body   = "Please login with the following userid: ${userInstance.username} password: ${password}"
				// sendMessage "seda:mailQueue", [body:body, email: userInstance.username]

				// sendMail(userInstance.username, password)
				TenantUtils.doWithTenant(userInstance.userTenantId) {
					AsynchronousEmailStorage async = new AsynchronousEmailStorage()
					async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
					async.emailTO = "${userInstance.username}"
					if(resetPassword){
						async.emailSubject="Password Reset"
						async.emailData="Hello ${userInstance.firstName},\n\n Your password was reset. \n Please login to ${grailsApplication.config.grails.serverURL} using following credentials-\n Username: ${userInstance.username}\n Password: ${password}\n\nThank you,\nYour Form Builder Team"
						async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
						async.save(flush:true)
					}else{
						async.emailSubject="Your login information"
						async.emailData="Hello ${userInstance.firstName},\n\n Please login to ${grailsApplication.config.grails.serverURL} using following credentials-\n Username: ${userInstance.username}\n Password: ${password}\n\nThank you,\nYour Form Builder Team"
						async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
						async.save(flush:true)
						welcomeEmail(userInstance)
					}

				}
				flash.message = "Email sent"
				flash.args = [params.id]
				flash.defaultMessage = "Login information sent to user."

				// If coming from forgot password call view auth
				if (params.j_username != null)
					redirect(controller:"register",action: "forgotPassword")
				else
					redirect(controller:"user",action: "edit", id: userInstance.id)
			}
			else {
				if (params.j_username == null)
					render(view: "/user/edit", model: [userInstance: userInstance])
			}
		}catch(Exception ex){
		}

	}

	String randomPass() {
		UUID uuid = UUID.randomUUID()
		uuid.toString()[0..7]
	}
	def welcomeEmail(userInstance){
		AsynchronousEmailStorage asyncWelcome = new AsynchronousEmailStorage()
		asyncWelcome.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
		asyncWelcome.emailTO = "${userInstance.username}"
		asyncWelcome.emailSubject="Welcome to Form Builder "
		asyncWelcome.emailData= "${g.render(template:'/dashboard/welcomeTemplate')}"
		asyncWelcome.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
		asyncWelcome.isHtml = true
		asyncWelcome.save(flush:true,validate:true)
	}
	def register = {
		boolean showFieldValues=true;
		def view = "/register/registerUser"
		boolean isValidNumber = false
		PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
		def phoneNumber = params.j_mobile
		try{
			if(phoneNumber){
				PhoneNumber usNumberProto = phoneUtil.parse(phoneNumber, "ZZ")
				isValidNumber = phoneUtil.isValidNumber(usNumberProto)
			}
		}catch(Exception ex){
		}
		if (params.j_username == null || params.j_username?.indexOf("@") == -1 || params.j_username?.length() < 5  ) {
			flash.defaultMessage = "Enter a valid email address!"
			flash.message = flash.defaultMessage
		}else if (params.j_mobile == null || params.j_mobile.length()<10 || !isValidNumber){
			flash.defaultMessage = "Enter a valid mobile number!"
			flash.message = flash.defaultMessage
		}else if (params.j_company == null || params.j_company.length()<1){
			flash.defaultMessage = "company name is required!"
			flash.message = flash.defaultMessage
		}else if (params.j_username != null && params.j_mobile != null && params.j_company != null){
			def userInstance = User.findByUsername(params.j_username)
			if(userInstance){
				flash.defaultMessage = "User already exists!"
				flash.message = flash.defaultMessage
			}else{
				userInstance = new User()
				userInstance.username = params.j_username
				userInstance.userTenantId = 1 // Add the user to the default client
				userInstance.enabled= true
				userInstance.firstName = userInstance.username.substring(0,userInstance.username.indexOf("@"))
				userInstance.mobilePhone=phoneNumber
				def password = randomPass()
				userInstance.password = getEncodedRandomPassword(password)
				userInstance.save(flush:true)
				UserRole ur = new UserRole(user: userInstance, role:Role.findByAuthority(Role.ROLE_TRIAL_USER))

				try{
					ur.save(flush:true)
					def userProfileInstance = new UserProfile()
					userProfileInstance.user = userInstance
					userProfileInstance.save(flush:true)
					// sending mail asynchronously now
					TenantUtils.doWithTenant(1) {
						AsynchronousEmailStorage async = new AsynchronousEmailStorage()
						async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
						async.emailTO = "${userInstance.username}"
						async.emailSubject="Your login information"
						async.emailData="Hello ${userInstance.firstName},\n\n Please login to ${grailsApplication.config.grails.serverURL} using following credentials-\n Username: ${userInstance.username} \n Password: ${password}\n\nThank you,\nYour Form Builder Team"
						async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
						async.save(flush:true)
						showFieldValues=false;
						welcomeEmail(userInstance);
						clientDataLoadService.loadInitialDataForTrialUser(userInstance)
					}
				}catch ( Exception e ){
					println "RegisterController-register: "+e
					flash.message = "Email not sent ${e.getMessage()}"
					if ( ur )
						ur.delete()
					if ( userInstance )
						userInstance.delete()
					flash.defaultMessage = flash.message
				}
				flash.message = "Email sent"
				flash.args = [params.id]
				flash.defaultMessage = "Login information sent!"
			}
		}
		if(params.fromFormTemplate){
			render(view:'/login/auth_ajax', model:[showFieldValues:showFieldValues])
		}else{
			render(view: view, model:[showFieldValues:showFieldValues])
		}

	}

	String getEncodedRandomPassword(String password)
	{

		return springSecurityService.encodePassword(password)

	}

	def googleAppLogin = {

	}

	def afterInterceptor = { model, modelAndView ->
		if (request['isMobile'] && modelAndView != null ) {
			println "RegisterController-afterInterceptor: "+request['isMobile']
			modelAndView.viewName = modelAndView.viewName + "_m"
		}
	}
	def accountInfo={ }
}
