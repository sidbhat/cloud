package com.oneapp.cloud.core

class EmailSettingsController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def springSecurityService

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [emailSettingsInstanceList: EmailSettings.findAllByUser(session.user,params), emailSettingsInstanceTotal: EmailSettings.findAllByUser(session.user).size()]
    }

    def create = {
		def emailSettinglist = EmailSettings.findAllByUser(springSecurityService.currentUser)
		def clientInstance = Client.get(springSecurityService.currentUser.userTenantId)
		if(emailSettinglist.size() <  clientInstance.maxEmailAccount){
	        def emailSettingsInstance = new EmailSettings()
	        emailSettingsInstance.properties = params
	        return [emailSettingsInstance: emailSettingsInstance]
		}else{
			flash.message = "maxlimit.email.message"
			flash.defaultMessage = "Cannot create more then ${clientInstance.maxEmailAccount} email accounts"
			redirect(action: "list")
		}
    }

    def save = {
		def emailSettingsInstance = new EmailSettings(params)
		emailSettingsInstance.user = session.user
		if (emailSettingsInstance.save(flush: true)) {
			flash.message = "emailSetting.created.message"
			flash.args = [emailSettingsInstance.username]
			flash.defaultMessage = "Email Setting ${emailSettingsInstance.username} created"
			redirect(action: "edit", id: emailSettingsInstance.id)
		}
		else {
			render(view: "create", model: [emailSettingsInstance: emailSettingsInstance])
		}
    }

    def edit = {
        def emailSettingsInstance = EmailSettings.get(params.id)
        if (!emailSettingsInstance || emailSettingsInstance.user.id != session.user.id ) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'emailSettings.label', default: 'EmailSettings'), params.id])}"
            redirect(action: "list")
        }
        else {
            [emailSettingsInstance: emailSettingsInstance]
        }
    }

    def update = {
        def emailSettingsInstance = EmailSettings.get(params.id)
        emailSettingsInstance.user = session.user
        if (emailSettingsInstance &&  emailSettingsInstance.user.id == session.user.id) {
            if (params.version) {
                def version = params.version.toLong()
                if (emailSettingsInstance.version > version) {
                    emailSettingsInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'emailSettings.label', default: 'EmailSettings')] as Object[], "Another user has updated this EmailSettings while you were editing")
                    render(view: "edit", model: [emailSettingsInstance: emailSettingsInstance])
                    return
                }
            }
            emailSettingsInstance.properties = params
            if (!emailSettingsInstance.hasErrors() && emailSettingsInstance.save(flush: true)) {
                flash.message = "emailSetting.updated.message"
				flash.args = [emailSettingsInstance.username]
				flash.defaultMessage = "Email Setting ${emailSettingsInstance.username} updated"
				redirect(action: "edit", id: emailSettingsInstance.id)
            }
            else {
                render(view: "edit", model: [emailSettingsInstance: emailSettingsInstance])
            }
        }
        else {
            flash.message = "default.not.found.message"
			flash.args = [params.id]
			flash.defaultMessage = "Email Setting not found"
            redirect(action: "list")
        }
    }

    def delete = {
        def emailSettingsInstance = EmailSettings.get(params.id)
        if (emailSettingsInstance  &&  emailSettingsInstance.user.id == session.user.id) {
            try {
				def emailSettingUserName = emailSettingsInstance.username
                emailSettingsInstance.delete(flush: true)
                flash.message = "emailSetting.deleted.message"
				flash.args = [emailSettingUserName]
				flash.defaultMessage = "Email Setting ${emailSettingUserName} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "${message(code: 'email.not.deleted.message', args: [message(code: 'emailSettings.label', default: 'EmailSettings'), params.id])}"
				flash.args = [params.id]
				flash.defaultMessage = "Error occured while deleting Email Setting"
				redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "default.not.found.message"
			flash.args = [params.id]
			flash.defaultMessage = "Email Setting not found"
			redirect(action: "list")
        }
    }
}
