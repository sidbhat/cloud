

package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant
import com.oneapp.unibox.auth.*

@MultiTenant
class ActivityFeedUserState implements Serializable{

		User user
		String userFeedState
		ActivityFeed feed
		
		public static final String ARCHIVED="ARCHIVED"
		public static final String READ="READ"
		public static final String UNREAD="UNREAD"
		
	    static constraints = {
			 userFeedState nullable:false, blank:false, inList:[ARCHIVED, READ,UNREAD]
			 user nullable:false, blank:false
			 feed nullable:false, blank:false
		}
}