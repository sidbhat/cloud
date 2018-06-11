 
package com.oneapp.cloud.time

import java.io.Serializable;

import com.oneapp.cloud.core.*
import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class Task extends BaseApproval implements Serializable{
    
    String name
    String description
    DropDown taskType
    Date startDate
    Date dueDate
    User assignedTo
    
  
  static hasMany = [taskUpdate:TaskUpdate] 
	
    String toString() {
        name
    }

    static constraints = {
        name nullable: false, blank: false, size: 2..54
        description nullable: true, blank: true, size: 2..2048
        taskType nullable: false, blank: false
        startDate nullable: true, blank: true
        dueDate nullable: true, blank: true
        assignedTo nullable: true, blank: true
    }

}