package com.oneapp.cloud.core
import java.io.Serializable;

import com.bloomhealthco.jasypt.GormEncryptedStringType
import grails.plugin.multitenant.core.groovy.compiler.MultiTenant;

@MultiTenant
class EmailSettings implements Serializable{

	String emailAddress
	String username
	String password
	String popServerURL
	String port
	boolean secureConnection=true
	User user
	
	static constraints = {
        username blank: false, nullable: false, unique:true, email: true
        password blank: false, nullable: false
        emailAddress blank: true, nullable: true
        popServerURL blank: true, nullable: true
        port blank: true, nullable: true
        user blank: false, nullable: false
		secureConnection nullable: true, blank: true
    }
	
	String toString() {
        username
    }
	
	static mapping = {
		username type: GormEncryptedStringType
		password type: GormEncryptedStringType
	}
    
    
}