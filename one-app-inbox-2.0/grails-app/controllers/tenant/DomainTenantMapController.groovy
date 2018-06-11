 
package tenant

class DomainTenantMapController {

    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10, 100)
        [domainTenantMapInstanceList: DomainTenantMap.list(params), domainTenantMapInstanceTotal: DomainTenantMap.count()]
    }

    def create = {
        def domainTenantMapInstance = new DomainTenantMap()
        domainTenantMapInstance.properties = params
        return [domainTenantMapInstance: domainTenantMapInstance]
    }

    def save = {
        def domainTenantMapInstance = new DomainTenantMap(params)
        if (!domainTenantMapInstance.hasErrors() && domainTenantMapInstance.save()) {
            flash.message = "domainTenantMap.created"
            flash.args = [domainTenantMapInstance.id]
            flash.defaultMessage = "DomainTenantMap ${domainTenantMapInstance.id} created"
            redirect(action: "show", id: domainTenantMapInstance.id)
        }
        else {
            render(view: "create", model: [domainTenantMapInstance: domainTenantMapInstance])
        }
    }

    def show = {
        def domainTenantMapInstance = DomainTenantMap.get(params.id)
        if (!domainTenantMapInstance) {
            flash.message = "domainTenantMap.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "DomainTenantMap not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [domainTenantMapInstance: domainTenantMapInstance]
        }
    }

    def edit = {
        def domainTenantMapInstance = DomainTenantMap.get(params.id)
        if (!domainTenantMapInstance) {
            flash.message = "domainTenantMap.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "DomainTenantMap not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [domainTenantMapInstance: domainTenantMapInstance]
        }
    }

    def update = {
        def domainTenantMapInstance = DomainTenantMap.get(params.id)
        if (domainTenantMapInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (domainTenantMapInstance.version > version) {

                    domainTenantMapInstance.errors.rejectValue("version", "domainTenantMap.optimistic.locking.failure", "Another user has updated this DomainTenantMap while you were editing")
                    render(view: "edit", model: [domainTenantMapInstance: domainTenantMapInstance])
                    return
                }
            }
            domainTenantMapInstance.properties = params
            if (!domainTenantMapInstance.hasErrors() && domainTenantMapInstance.save()) {
                flash.message = "domainTenantMap.updated"
                flash.args = [params.id]
                flash.defaultMessage = "DomainTenantMap ${params.id} updated"
                redirect(action: "show", id: domainTenantMapInstance.id)
            }
            else {
                render(view: "edit", model: [domainTenantMapInstance: domainTenantMapInstance])
            }
        }
        else {
            flash.message = "domainTenantMap.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "DomainTenantMap not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def domainTenantMapInstance = DomainTenantMap.get(params.id)
        if (domainTenantMapInstance) {
            try {
                domainTenantMapInstance.delete()
                flash.message = "domainTenantMap.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "DomainTenantMap ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error e
                flash.message = "domainTenantMap.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "DomainTenantMap ${params.id} could not be deleted"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "domainTenantMap.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "DomainTenantMap not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
