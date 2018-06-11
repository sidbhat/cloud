package com.oneapp.cloud.core

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant;
import java.io.Serializable;
@MultiTenant
class Device implements Serializable {
	 String token
	 String deviceId
	 User deviceUser
	static constraints = {
		token  nullable:false, blank:false, unique:true
	}
	static mapping = { token type:'text' }
}
