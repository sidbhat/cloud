package com.oneapp.cloud.core

class PlanController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [planInstanceList: Plan.list(params), planInstanceTotal: Plan.count()]
    }

    def create = {
        def planInstance = new Plan()
        planInstance.properties = params
        return [planInstance: planInstance]
    }

    def save = {
        def planInstance = new Plan(params)
        if (!planInstance.hasErrors() && planInstance.save()) {
            flash.message = "plan.created"
            flash.args = [planInstance.id]
            flash.defaultMessage = "Plan ${planInstance.id} created"
            redirect(action: "edit", id: planInstance.id)
        }
        else {
            render(view: "create", model: [planInstance: planInstance])
        }
    }

    def show = {
        def planInstance = Plan.get(params.id)
        if (!planInstance) {
            flash.message = "plan.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Plan not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [planInstance: planInstance]
        }
    }

    def edit = {
        def planInstance = Plan.get(params.id)
        if (!planInstance) {
            flash.message = "plan.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Plan not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [planInstance: planInstance]
        }
    }

    def update = {
        def planInstance = Plan.get(params.id)
        if (planInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (planInstance.version > version) {
                    
                    planInstance.errors.rejectValue("version", "plan.optimistic.locking.failure", "Another user has updated this Plan while you were editing")
                    render(view: "edit", model: [planInstance: planInstance])
                    return
                }
            }
            planInstance.properties = params
            if (!planInstance.hasErrors() && planInstance.save()) {
                flash.message = "plan.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Plan ${params.id} updated"
                redirect(action: "edit", id: planInstance.id)
            }
            else {
                render(view: "edit", model: [planInstance: planInstance])
            }
        }
        else {
            flash.message = "plan.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Plan not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def planInstance = Plan.get(params.id)
        if (planInstance) {
            try {
                planInstance.delete()
                flash.message = "plan.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Plan ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "plan.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Plan ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "plan.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Plan not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
