package com.oneapp.cloud.core

class BlockedIpController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [blockedIpInstanceList: BlockedIp.list(params), blockedIpInstanceTotal: BlockedIp.count()]
    }

    def create = {
        def blockedIpInstance = new BlockedIp()
        blockedIpInstance.properties = params
        return [blockedIpInstance: blockedIpInstance]
    }

    def save = {
        def blockedIpInstance = new BlockedIp(params)
        if (!blockedIpInstance.hasErrors() && blockedIpInstance.save()) {
            flash.message = "blockedIp.created"
            flash.args = [blockedIpInstance.id]
            flash.defaultMessage = "BlockedIp ${blockedIpInstance.id} created"
            redirect(action: "edit", id: blockedIpInstance.id)
        }
        else {
            render(view: "create", model: [blockedIpInstance: blockedIpInstance])
        }
    }

    def show = {
        def blockedIpInstance = BlockedIp.get(params.id)
        if (!blockedIpInstance) {
            flash.message = "blockedIp.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "BlockedIp not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [blockedIpInstance: blockedIpInstance]
        }
    }

    def edit = {
        def blockedIpInstance = BlockedIp.get(params.id)
        if (!blockedIpInstance) {
            flash.message = "blockedIp.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "BlockedIp not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [blockedIpInstance: blockedIpInstance]
        }
    }

    def update = {
        def blockedIpInstance = BlockedIp.get(params.id)
        if (blockedIpInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (blockedIpInstance.version > version) {
                    
                    blockedIpInstance.errors.rejectValue("version", "blockedIp.optimistic.locking.failure", "Another user has updated this BlockedIp while you were editing")
                    render(view: "edit", model: [blockedIpInstance: blockedIpInstance])
                    return
                }
            }
            blockedIpInstance.properties = params
            if (!blockedIpInstance.hasErrors() && blockedIpInstance.save()) {
                flash.message = "blockedIp.updated"
                flash.args = [params.id]
                flash.defaultMessage = "BlockedIp ${params.id} updated"
                redirect(action: "edit", id: blockedIpInstance.id)
            }
            else {
                render(view: "edit", model: [blockedIpInstance: blockedIpInstance])
            }
        }
        else {
            flash.message = "blockedIp.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "BlockedIp not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def blockedIpInstance = BlockedIp.get(params.id)
        if (blockedIpInstance) {
            try {
                blockedIpInstance.delete()
                flash.message = "blockedIp.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "BlockedIp ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "blockedIp.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "BlockedIp ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "blockedIp.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "BlockedIp not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
