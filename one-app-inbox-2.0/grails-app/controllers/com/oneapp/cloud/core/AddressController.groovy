

package com.oneapp.cloud.core

class AddressController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [addressInstanceList: Address.list(params), addressInstanceTotal: Address.count()]
    }

    def create = {
        def addressInstance = new Address()
        addressInstance.properties = params
        return [addressInstance: addressInstance]
    }

    def save = {
        def addressInstance = new Address(params)
        if (!addressInstance.hasErrors() && addressInstance.save()) {
            flash.message = "address.created"
            flash.args = [addressInstance.id]
            flash.defaultMessage = "Address ${addressInstance.id} created"
            redirect(action: "show", id: addressInstance.id)
        }
        else {
            render(view: "create", model: [addressInstance: addressInstance])
        }
    }

    def show = {
        def addressInstance = Address.get(params.id)
        if (!addressInstance) {
            flash.message = "address.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Address not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [addressInstance: addressInstance]
        }
    }

    def edit = {
        def addressInstance = Address.get(params.id)
        if (!addressInstance) {
            flash.message = "address.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Address not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [addressInstance: addressInstance]
        }
    }

    def update = {
        def addressInstance = Address.get(params.id)
        if (addressInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (addressInstance.version > version) {

                    addressInstance.errors.rejectValue("version", "address.optimistic.locking.failure", "Another user has updated this Address while you were editing")
                    render(view: "edit", model: [addressInstance: addressInstance])
                    return
                }
            }
            addressInstance.properties = params
            if (!addressInstance.hasErrors() && addressInstance.save()) {
                flash.message = "address.updated"
                flash.args = [params.id]
                flash.defaultMessage = "Address ${params.id} updated"
                redirect(action: "show", id: addressInstance.id)
            }
            else {
                render(view: "edit", model: [addressInstance: addressInstance])
            }
        }
        else {
            flash.message = "address.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Address not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def addressInstance = Address.get(params.id)
        if (addressInstance) {
            try {
                addressInstance.delete()
                flash.message = "address.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Address ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
                flash.message = "address.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Address ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "address.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Address not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
