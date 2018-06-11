package com.oneapp.cloud.core

import java.io.Serializable;
import java.util.List;

import org.grails.formbuilder.Form

class ApplicationConf implements Serializable{
	
	boolean logInfo = false
	boolean trackChanges = false
	String sendEmailDefaultFrom
	Integer asynEmailJobInterval
	Integer rulesJobInterval
	
	Long form=75
	Long formForTrial=2
    
	Integer maxFormControls=40
	
	static constraints = {
			sendEmailDefaultFrom(nullable:true, blank:true, email:true)
			asynEmailJobInterval nullable:true
			rulesJobInterval nullable:true
			
			form nullable:false,blank:false
			maxFormControls nullable:false,blank:false
			copyForms nullable: true
			copyFormsTrial nullable:true
		}
	
	List copyForms = new ArrayList()
	List copyFormsTrial = new ArrayList()
	static hasMany = [ copyForms:Form, copyFormsTrial:Form ]

	}
