package com.oneapp.cloud.core.log

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant;

@MultiTenant
class Log implements Serializable{
	
	static mapping = {
		table 'log'
		version false
		id generator:'identity',column:'id'
	}
	
	String description
	String errorLevel
	String errorType
	Date logTime

    static constraints = {
		description nullable:true, blank:true
		errorLevel nullable:true, blank:true
		errorType nullable:true, blank:true
		logTime nullable:true
    }
}
