package com.oneapp.cloud.core
import java.io.Serializable;

import com.oneapp.cloud.security.*
import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class GroupDetails implements Serializable{

	String  groupName
	String  groupDescription
	Date    createdOn
	String url
    DropDown groupType
    byte[] image
 	boolean personal
 	String groupSource
 	String groupId
    List<User> user
    User createdBy
    
    static auditable = [ignore: ['version']]
 	String toString() {
        groupName
    }
 	
 	def beforeInsert = {
        createdOn = new Date()
    }
    
    static constraints = {
        groupName nullable: false, blank: false, size:2..54
        groupDescription nullable: false, blank: false, size:2..108
        url nullable: true, blank: true
        image nullable: true, blank: true
        contacts nullable: true, blank: true
        personal nullable: true, blank: true
        user nullable: true, blank: true
        groupType nullable: false, blank: false
        createdOn nullable: true, blank: true
        groupSource nullable: true, blank: true
        groupId nullable: true, blank: true
        createdBy nullable: true, blank: true
   	}
	static hasMany = [ contacts : ContactDetails, user:User ]

	
	
}
