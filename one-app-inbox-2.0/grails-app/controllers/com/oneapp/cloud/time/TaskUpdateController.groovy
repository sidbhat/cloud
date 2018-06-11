
package com.oneapp.cloud.time


class TaskUpdateController {

    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def index = {
        redirect(action: "list", params: params)
    }

    def list = {
        params.max = Math.min(params.max ? params.int('max') : 10, 100)
        [taskUpdateInstanceList: TaskUpdate.list(params), taskUpdateInstanceTotal: TaskUpdate.count()]
    }

    def create = {
        def taskUpdateInstance = new TaskUpdate()
        taskUpdateInstance.properties = params
        return [taskUpdateInstance: taskUpdateInstance]
    }

    def save = {
        def taskUpdateInstance = new TaskUpdate(params)
        if (taskUpdateInstance.save(flush: true)) {
            flash.message = "${message(code: 'default.created.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), taskUpdateInstance.id])}"
            redirect(action: "show", id: taskUpdateInstance.id)
        }
        else {
            render(view: "create", model: [taskUpdateInstance: taskUpdateInstance])
        }
    }

    def show = {
        def taskUpdateInstance = TaskUpdate.get(params.id)
        if (!taskUpdateInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), params.id])}"
            redirect(action: "list")
        }
        else {
            [taskUpdateInstance: taskUpdateInstance]
        }
    }

    def edit = {
        def taskUpdateInstance = TaskUpdate.get(params.id)
        if (!taskUpdateInstance) {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), params.id])}"
            redirect(action: "list")
        }
        else {
            return [taskUpdateInstance: taskUpdateInstance]
        }
    }

    def update = {
        def taskUpdateInstance = TaskUpdate.get(params.id)
        if (taskUpdateInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (taskUpdateInstance.version > version) {
                    
                    taskUpdateInstance.errors.rejectValue("version", "default.optimistic.locking.failure", [message(code: 'taskUpdate.label', default: 'TaskUpdate')] as Object[], "Another user has updated this TaskUpdate while you were editing")
                    render(view: "edit", model: [taskUpdateInstance: taskUpdateInstance])
                    return
                }
            }
            taskUpdateInstance.properties = params
            if (!taskUpdateInstance.hasErrors() && taskUpdateInstance.save(flush: true)) {
                flash.message = "${message(code: 'default.updated.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), taskUpdateInstance.id])}"
                redirect(action: "show", id: taskUpdateInstance.id)
            }
            else {
                render(view: "edit", model: [taskUpdateInstance: taskUpdateInstance])
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), params.id])}"
            redirect(action: "list")
        }
    }

    def delete = {
        def taskUpdateInstance = TaskUpdate.get(params.id)
        if (taskUpdateInstance) {
            try {
                taskUpdateInstance.delete(flush: true)
                flash.message = "${message(code: 'default.deleted.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), params.id])}"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error e
                flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), params.id])}"
                redirect(action: "show", id: params.id)
            }
        }
        else {
            flash.message = "${message(code: 'default.not.found.message', args: [message(code: 'taskUpdate.label', default: 'TaskUpdate'), params.id])}"
            redirect(action: "list")
        }
    }
}
