

package com.oneapp.cloud.core

import java.io.Serializable;
import grails.plugin.multitenant.core.groovy.compiler.MultiTenant
import grails.plugin.multitenant.core.util.TenantUtils

@MultiTenant
class ActivityFeedConfig implements Serializable{

	String configName
	String shareType			//task or form
	String className			
	
	Date lastUpdated  
	Date dateCreated
	User createdBy
	
	static constraints = {
	 configName nullable:false, blank:false
	 createdBy nullable:true, blank:true
	 shareType nullable:true, blank:true
	 className nullable:true, blank:true
	 lastUpdated nullable:true, blank:true
	 dateCreated nullable:true, blank:true
	 createdBy nullable:true, blank:true
	}
	
	String toString(){
		
		configName
	}
	
	
	def beforeDelete() {
		
		try {
			TenantUtils.doWithTenant((Integer) this.tenantId) {
				ActivityFeed.withNewSession {
	                    ActivityFeed.findAllByConfig(this).each {ActivityFeed obj ->
	                        obj.delete()
	                    }
	                }
			}
		} catch (Exception e) {
			log.error e
		}
	}
}