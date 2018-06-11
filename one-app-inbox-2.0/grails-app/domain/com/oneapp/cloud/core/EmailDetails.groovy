package com.oneapp.cloud.core


import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant;

@MultiTenant
class EmailDetails implements Serializable{

   String emailFrom
   String emailTo
   String subject
   String content
   Date messageTime
   Double msgSize
   String attachmentName
   Long messageNumber
   Long ruleAccount
   User createdBy
   
   
   static constraints = {
        emailFrom nullable: true, blank: true
        emailTo nullable: true, blank: true
        subject nullable: true, blank: true,size:0..1024
        content nullable: true, blank: true
        messageTime nullable: true, blank: true
        msgSize nullable: true, blank: true
		attachmentName nullable: true, blank: true
		messageNumber nullable: true, blank: true
		createdBy nullable:true,blank: true
    }
   
   static mapping = {
	   content type:'text'
	   emailTo type:'text'
	   
   }

}