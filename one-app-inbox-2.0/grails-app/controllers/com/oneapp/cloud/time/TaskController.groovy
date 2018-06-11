 
package com.oneapp.cloud.time

import com.oneapp.cloud.core.AsynchronousEmailStorage
import com.oneapp.cloud.core.User
import com.oneapp.cloud.core.UserProfile
import grails.converters.JSON
import org.grails.comments.Comment

class TaskController {

  def index = { redirect(action: "list", params: params) }
  def calendarOneAppService
  def checkRoleService
  // the delete, save and update actions only accept POST requests
  static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
  def springSecurityService

  def getSessionUser() {
    return springSecurityService.currentUser
  }


  def publishTaskForEmail(Long id) {
    def taskInstance = Task.get(id)

    AsynchronousEmailStorage asynchronousEmailStorage = new AsynchronousEmailStorage()
    asynchronousEmailStorage.emailFrom = "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
    asynchronousEmailStorage.emailTO = taskInstance.assignedTo.username
    asynchronousEmailStorage.emailData = """ Hello ${taskInstance.assignedTo.username},
Your project manager has assigned task -${taskInstance?.name}
You can reply to this email with your comments. Delete the original text and only send your comments.
Thanks
Project team
""".toString()
    asynchronousEmailStorage.emailSubject = "${taskInstance.id} # ${taskInstance.name}  has been assigned to you"
    asynchronousEmailStorage.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
    if (!asynchronousEmailStorage.save(flush: true)) {

    } else
      println "TaskController-publishTaskForEmail: " + asynchronousEmailStorage

  }

  def emailNotes(Long id, comments) {
    def taskInstance = Task.get(id)
    createEmail(taskInstance, taskInstance?.createdBy?.username, comments)
    createEmail(taskInstance, taskInstance?.assignedTo?.username, comments)
  }

  def deleteComment = {
    Comment comment = Comment.get(Long.parseLong('' + params.commentId))
    Task task=Task.get(Long.parseLong(''+params?.taskId))
    try {
     task.removeComment(comment)
     render(template: 'comments', model: [taskInstance:task])
    } catch (Exception e) {
		log.error e
      e.printStackTrace()
    }

  }

  def createEmail(taskInstance, emailTo, comments) {
    AsynchronousEmailStorage asynchronousEmailStorage = new AsynchronousEmailStorage()
    asynchronousEmailStorage.emailFrom = "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
    asynchronousEmailStorage.emailTO = emailTo
    asynchronousEmailStorage.emailData = """${comments}"""
    asynchronousEmailStorage.emailSubject = "[Notification : ${taskInstance.name}]"
    asynchronousEmailStorage.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
    if (!asynchronousEmailStorage.save(flush: true)) {
      println "TaskController-createEmail: " + asynchronousEmailStorage
    } else
      println "TaskController-createEmail: " + asynchronousEmailStorage
  }


  def list = {

    UserProfile up = UserProfile.findByUser(getSessionUser())
    def max = 10
    if (up?.numOfRows)
      max = up?.numOfRows
    params.max = Math.min(params.max ? params.max.toInteger() : max, 100)

    if (!params.sort) {
      params.sort = 'dueDate'
      params.order = 'desc'
    }

    def viewName
    def list
    def taskInstanceTotal
    def me = getSessionUser()
    def status = com.oneapp.cloud.core.DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.STATUS, 'CLOSE')

    if (params.view != null)
      session["task.view"] = params.view

    if (session["task.view"] == null || session["task.view"] == "my_open") {
      viewName = message(code: "task.view.my_open")
      session["task.view"] = "my_open"
      list = Task.findAll("from Task as t where t.active=true and t.assignedTo.id=${me.id} and t.status.id <> ${status.id}  order by dueDate desc")
      taskInstanceTotal = Task.findAll("from Task as t where t.active=true and t.assignedTo.id=${me.id} and t.status.id <> ${status.id}  order by dueDate desc").size()
    }
    else if (session["task.view"] == "project_open") {
      viewName = message(code: "task.view.project_open")
      session["task.view"] = "project_open"
      list = Task.findAll("from Task as t where t.active=true and t.project is not null and t.status.id <> ${status.id} and personal=false")
      taskInstanceTotal = Task.findAll("from Task as t where t.active=true and t.project is not null and t.status.id <> ${status.id} and t.personal=false  order by dueDate desc").size()
    }
    else if (session["task.view"] == "project_all") {
      viewName = message(code: "task.view.project_all")
      session["task.view"] = "project_all"
      list = Task.findAllByActiveAndProjectIsNotNull(true, params)
      taskInstanceTotal = list.size()
    }
    else if (session["task.view"] == "my_all") {
      viewName = message(code: "task.view.my_all")
      session["task.view"] = "my_all"
      list = Task.findAllByActiveAndAssignedTo(true, me, params)
      taskInstanceTotal = Task.findAllByActiveAndAssignedTo(true, me).size()
    }
    else if (session["task.view"] == "all_open") {
      viewName = message(code: "task.view.all_open")
      session["task.view"] = "all_open"
      list = Task.findAll("from Task as t where t.active=true and t.status.id <> ${status.id} and ( t.personal=false or t.assignedTo.id = ${me.id}) order by dueDate desc")
      taskInstanceTotal = Task.findAll("from Task as t where t.active=true and t.status.id <> ${status.id} and ( t.personal=false or t.assignedTo.id = ${me.id})  order by dueDate desc").size()
    }
    else if (session["task.view"] == "all") {
      viewName = message(code: "task.view.all")
      session["task.view"] = "all"
      list = Task.findAllByPersonalOrAssignedTo(false, me, params)
      taskInstanceTotal = Task.findAllByPersonalOrAssignedTo(false, me).size()
    }

    [taskInstanceList: list, taskInstanceTotal: taskInstanceTotal, viewName: viewName]
  }

  def create = {
    def taskInstance = new Task()
    def user = getSessionUser()
    taskInstance.assignedTo = user
    taskInstance.properties = params
    return [taskInstance: taskInstance]
  }

  def copy = {
    def taskInstance = Task.get(params.id)

    if (!taskInstance) {
      flash.message = "task.not.found"
      flash.args = [params.id]
      flash.defaultMessage = "Task not found with id ${params.id}"
      redirect(action: "list")
    }
    else {
      def newtaskInstance = new Task()
      newtaskInstance = taskInstance
      render(view: "create", model: [taskInstance: newtaskInstance])
    }
  }

  def save = {
    def taskInstance = new Task(params)
    taskInstance.createdBy = User.get(getSessionUser().id)
    taskInstance.name = taskInstance.name.replaceAll(/\w+/, { it[0].toUpperCase() + ((it.size() > 1) ? it[1..-1] : '') })

    if (!taskInstance.hasErrors() && taskInstance.save()) {
      flash.message = "task.created"

  
      if (taskInstance.status == null) {
        def status = com.oneapp.cloud.core.DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.STATUS, 'OPEN')
        taskInstance.status = status
      }
      
     // if ( taskInstance.assignedTo)
      //	publishTaskForEmail(taskInstance.id)
      flash.args = [taskInstance.name]
      flash.defaultMessage = "Task ${taskInstance.name} created"
      redirect(action: "list", id: taskInstance.id)
    }
    else {
      render(view: "create", model: [taskInstance: taskInstance])
    }
  }

  def show = {
    def taskInstance = Task.get(params.id)
    if (!taskInstance) {
      flash.message = "task.not.found"
      flash.args = [params.id]
      flash.defaultMessage = "Task not found with id ${params.id}"
      redirect(action: "list")
    }
    else {
      return [taskInstance: taskInstance]
    }
  }

  def addNotes = {
  
    def map = [:]
    def task = Task.get(Long.parseLong('' + params?.taskId))
    if (task) {
      task.addComment(getSessionUser(), params?.comment)
     // emailNotes(task.id, params?.comment)
    }
    render(template: 'comments', model: [taskInstance: task])
  }

  def edit = {
    def taskInstance = Task.get(params.id)
    if (!taskInstance) {
      flash.message = "task.not.found"
      flash.args = [params.id]
      flash.defaultMessage = "Task not found with id ${params.id}"
      redirect(action: "list")
    }
    else {
      return [taskInstance: taskInstance]
    }
  }

  def update = {
    def taskInstance = Task.get(params.id)
    if (taskInstance) {
      if (checkRoleService.isAuthenticatedForUDOperation(taskInstance)) {
        def u = User.get(getSessionUser().id)

        if (params.version) {
          def version = params.version.toLong()
          if (taskInstance.version > version) {

            taskInstance.errors.rejectValue("version", "task.optimistic.locking.failure", "Another user has updated this Task while you were editing")
            render(view: "edit", model: [taskInstance: taskInstance])
            return
          }
        }
        taskInstance.properties = params;
        taskInstance.updatedBy = u

   
        if (!taskInstance.hasErrors() && taskInstance.save()) {
          flash.message = "task.updated"
          flash.args = [taskInstance.name]
          flash.defaultMessage = "Task ${taskInstance.name} updated"
          redirect(action: "list", id: taskInstance.id)
        }
        else {
          render(view: "edit", model: [taskInstance: taskInstance])
        }
      } else {
        flash.message = "not.Authenticated"
        flash.args = [params.id]
        flash.defaultMessage = "You are not authorized to update Task with id ${params.id}"
        redirect(action: "edit", id: params.id)
      }
    }
    else {
      flash.message = "task.not.found"
      flash.args = [params.id]
      flash.defaultMessage = "Task not found with id ${params.id}"
      redirect(action: "edit", id: params.id)

    }
  }

  def delete = {
    def taskInstance = Task.get(params.id)
    if (taskInstance) {
      if (checkRoleService.isAuthenticatedForUDOperation(taskInstance)) {
        try {
          taskInstance.delete()

          flash.message = "task.deleted"
          flash.args = [taskInstance.name]
          flash.defaultMessage = "Task ${taskInstance.name} deleted"
          redirect(action: "list")
        }
        catch (org.springframework.dao.DataIntegrityViolationException e) {
			log.error e
          flash.message = "task.not.deleted"
          flash.args = [taskInstance.name]
          flash.defaultMessage = "Task ${taskInstance.name} could not be deleted"
          redirect(action: "edit", id: params.id)
        }
      } else {
        flash.message = "not.Authenticated"
        flash.args = [params.id]
        flash.defaultMessage = "You are not authorized to delete Task with id ${params.id}"
        redirect(action: "edit", id: params.id)
      }
    }
    else {
      flash.message = "task.not.found"
      flash.args = [params.id]
      flash.defaultMessage = "Task not found with id ${params.id}"
      redirect(action: "list")
    }
  }
}

