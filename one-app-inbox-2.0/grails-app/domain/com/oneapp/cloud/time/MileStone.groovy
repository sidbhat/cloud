 
package com.oneapp.cloud.time

//import com.oneapp.cloud.core.Project
import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class MileStone extends com.oneapp.cloud.core.Base implements Serializable{
    String description
    Date startDate
    Date dueDate
   // Project project
    

    static hasMany = [task: Task]
  //  static belongsTo = [project: Project]
	static auditable = [ignore: ['version', 'lastUpdated']]
    static constraints = {
        description nullable: true, blank: true, size:0..256
        startDate nullable: true, blank: true
        dueDate nullable: true, blank: true
       // project nullable: false, blank: false

    }

    String toString() {
        return description
    }

}