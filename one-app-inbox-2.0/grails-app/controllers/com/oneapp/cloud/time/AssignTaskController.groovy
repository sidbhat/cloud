
package com.oneapp.cloud.time

import grails.converters.JSON

class AssignTaskController {
  def emailService
  def mailRetrieveService
  def mailService
  def index = {
    def list = []
    def milestoneList = MileStone.list(sort: "dueDate", order: "asc")
    def taskList = Task.list(sort: "dueDate", order: "asc")
       
    milestoneList.each {MileStone ->
      MileStone.task.each{mt->
      taskList.remove(mt)
      }
    }
    render view: 'assignTask', model: [milestoneList: milestoneList, taskList: taskList]
  }
  def saveAssignTask = {
    String milestoneAndTask = params.data
    String milestone = milestoneAndTask.split('@')[0]
    String task = milestoneAndTask.split('@')[1]
    String milestoneId = milestone.split('-')[1]
    String taskId = task.split('-')[1]
    MileStone milestoneInstance = MileStone.get(Long.parseLong(milestoneId))
    Task taskInstance = Task.get(Long.parseLong(taskId))

    milestoneInstance.addToTask(taskInstance)
    render([status: "OK"] as JSON)
  }

  def removeTask = {

    String milestoneAndTask = params.data
    String MileStone = milestoneAndTask.split('@')[0]
    String task = milestoneAndTask.split('@')[1]
    String milestoneId = MileStone.split('-')[1]
    String taskId = task.split('-')[1]
    MileStone milestoneInstance = MileStone.get(Long.parseLong(milestoneId))
    Task taskInstance = Task.get(Long.parseLong(taskId))

    milestoneInstance.removeFromTask(taskInstance)
    render([status: "OK"] as JSON)
  }


}
