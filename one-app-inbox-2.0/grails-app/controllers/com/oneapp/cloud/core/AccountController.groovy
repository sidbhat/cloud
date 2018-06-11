

package com.oneapp.cloud.core

class AccountController {
	def springSecurityService
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
         UserProfile up = UserProfile.findByUser(springSecurityService.currentUser)
         def max = 10
        if ( up?.numOfRows )
        	max = up?.numOfRows 
        params.max = Math.min(params.max ? params.max.toInteger() : max , 100)
      
        params.sort = 'dateCreated'
        params.order = 'desc'

        [accountInstanceList: Account.list(params), accountInstanceTotal: Account.count()]
    }

    def create = {
        def accountInstance = new Account()
        accountInstance.properties = params
        return [accountInstance: accountInstance]
    }

    def save = {
        def accountInstance = new Account(params)
       // println params
        accountInstance.name = accountInstance.name.replaceAll(/\w+/, { it[0].toUpperCase() + ((it.size() > 1) ? it[1..-1] : '') })

        if (!accountInstance.hasErrors() && accountInstance.save()) {
            flash.message = "account.created"
            flash.args = [accountInstance.id]
            flash.defaultMessage = "Account ${accountInstance.name} created"
            redirect(action: "edit", id: accountInstance.id)
        }
        else {
            render(view: "create", model: [accountInstance: accountInstance])
        }
    }

    def show = {
        def accountInstance = Account.get(params.id)
        if (!accountInstance) {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [accountInstance: accountInstance]
        }
    }

    def edit = {
        def accountInstance = Account.get(params.id)
        if (!accountInstance) {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with name ${params.id}"
            redirect(action: "list")
        }
        else {
            return [accountInstance: accountInstance]
        }
    }

    def update = {
        def accountInstance = Account.get(params.id)
        if (accountInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (accountInstance.version > version) {

                    accountInstance.errors.rejectValue("version", "account.optimistic.locking.failure", "Another user has updated this Account while you were editing")
                    render(view: "edit", model: [accountInstance: accountInstance])
                    return
                }
            }
            accountInstance.properties = params
           
            if (!accountInstance.hasErrors() && accountInstance.save()) {
                flash.message = "account.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Account ${accountInstance.name} updated"
                redirect(action: "edit", id: accountInstance.id)
            }
            else {
                render(view: "edit", model: [accountInstance: accountInstance])
            }
        }
        else {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def accountInstance = Account.get(params.id)
        if (accountInstance) {
            try {
                accountInstance.delete()
                flash.message = "account.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Account ${accountInstance.name} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "account.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Account ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "account.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Account not found with id ${params.id}"
            redirect(action: "list")
        }
    }
   def afterInterceptor = { model, modelAndView ->
  		if (request['isMobile'] && modelAndView != null ) {
  			println "AccountController-afterInterceptor: "+request['isMobile'] 
  			modelAndView.viewName = modelAndView.viewName + "_m"
 	 }
	}

}
