 
package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
/** Item Table for Invoices/Retainers **/
class BaseObjectItem implements Serializable{
	Integer parentId
	String type
	String description
	int quantity
	BigDecimal price
	Date dateCreated
    Date lastUpdated
	
	static mapping = { sort dateCreated:"desc" } 
	static constraints =
    {
    	parentId blank:false,nullable:false
    	type blank:false,nullable:false
	}
}
