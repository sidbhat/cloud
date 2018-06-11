 
package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class AsynchronousEmailStorage implements Serializable{

  String emailFrom
  String emailTO
  String emailSubject
  String emailData
  int emailSentStatus
  boolean isHtml = false
  public static final int NOT_ATTEMPT=1
  public static final int FAILED=0
  public static final int SENT_SUCCESSFULLY=2


    static constraints = {
 //     emailSubject unique:true
      emailData (type: 'text',size:0..100000000)
    }
}
