package com.oneapp.cloud.core

class SubscriptionController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	def springSecurityService

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [subscriptionInstanceList: Subscription.list(params), subscriptionInstanceTotal: Subscription.count()]
    }

    def create = {
        def subscriptionInstance = new Subscription()
        subscriptionInstance.properties = params
        render(view: "create", model: [subscriptionInstance: subscriptionInstance])
    }

    def save = {
        def subscriptionInstance = new Subscription(params)
        subscriptionInstance.createdBy = springSecurityService.currentUser
        if (!subscriptionInstance.hasErrors() && subscriptionInstance.save()) {
            flash.message = "subscription.created"
            flash.args = [subscriptionInstance.id]
            flash.defaultMessage = "Subscription ${subscriptionInstance.id} created"
            redirect(action: "edit", id: subscriptionInstance.id)
        }
        else {
            render(view: "create", model: [subscriptionInstance: subscriptionInstance])
        }
    }

 
    def edit = {
        def subscriptionInstance = Subscription.get(params.id)
        if (!subscriptionInstance) {
            flash.message = "subscription.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Subscription not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [subscriptionInstance: subscriptionInstance]
        }
    }

    def update = {
        def subscriptionInstance = Subscription.get(params.id)
        if (subscriptionInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (subscriptionInstance.version > version) {
                    
                    subscriptionInstance.errors.rejectValue("version", "subscription.optimistic.locking.failure", "Another user has updated this Subscription while you were editing")
                    render(view: "edit", model: [subscriptionInstance: subscriptionInstance])
                    return
                }
            }
            subscriptionInstance.properties = params
            if (!subscriptionInstance.hasErrors() && subscriptionInstance.save()) {
                flash.message = "subscription.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Subscription ${params.id} updated"
                redirect(action: "edit", id: subscriptionInstance.id)
            }
            else {
                render(view: "edit", model: [subscriptionInstance: subscriptionInstance])
            }
        }
        else {
            flash.message = "subscription.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Subscription not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def subscriptionInstance = Subscription.get(params.id)
        if (subscriptionInstance) {
            try {
                subscriptionInstance.delete()
                flash.message = "subscription.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Subscription ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error e
                flash.message = "subscription.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Subscription ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "subscription.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Subscription not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
