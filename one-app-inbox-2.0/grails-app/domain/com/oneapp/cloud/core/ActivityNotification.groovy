

package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class ActivityNotification implements Serializable{

		User actionBy
		String userFeedState
		ActivityFeed actionOnFeed
		Date actionTime
		
		
	    static constraints = {
			 userFeedState nullable:false, blank:false
			 actionBy nullable:false, blank:false
			 actionOnFeed nullable:false, blank:false
			 actionTime nullable:false, blank:false
		}
}