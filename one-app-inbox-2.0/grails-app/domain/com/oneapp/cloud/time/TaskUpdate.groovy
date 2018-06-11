 
package com.oneapp.cloud.time

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class TaskUpdate implements Serializable{

  String from
  String subject
  String content
  Integer taskId
  String taskName
  Date date
  
  static constraints = {
    from nullable: true
    subject nullable: true
    date nullable: true
	taskName nullable:true, blank:true
	taskId nullable:false, blank:false
	content type:'text',size:0..10000000


  }
  
  static mapping = {
      table 'task_updates'
      from column:'from_'
  }
}
