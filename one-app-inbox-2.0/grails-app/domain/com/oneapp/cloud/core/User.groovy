 
package com.oneapp.cloud.core
import java.io.Serializable;
import java.util.Date;

import org.grails.formbuilder.*;
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH

class User extends Person implements Serializable{

    String username
    String shortName
    String password
    boolean enabled
    boolean accountExpired
    boolean accountLocked
    boolean passwordExpired
    Integer userTenantId
	User createdBy
    Date dateCreated
    Date lastUpdated
	Date lastLogIn
	Date lastActivity
	Date lastViewed
    
    //HR Fields
    DropDown department
    DropDown subOrg
    String title
    BigDecimal hourlyRate
    String hourlyRateCurr
    DropDown type
    User reportsTo
    byte[] picture
    String pictureURL
	String claimedId
	String apiKey

    static auditable = [ignore: ['version', 'lastUpdated', 'accountExpired', 'accountLocked', 'passwordExpired', 'enabled', 'password', 'type', 'createdBy']]

    static constraints = {
        username blank: false, nullable: false, unique: true, email: true
        userTenantId nullable: false, blank: false, min: 0
	    password blank: true, nullable: true
        pictureURL blank: true, nullable: true, url: true
        department nullable: true, blank: true
        title nullable: true, blank: true
        type nullable: true, blank: true
        reportsTo nullable: true, blank: true
        hourlyRate scale: 2, nullable: true, blank: true
        // project nullable:true, blank:true
        createdBy nullable: true, blank: true
        hourlyRateCurr nullable: true, blank: true
        subOrg nullable: true, blank: true
        shortName nullable: true, blank: true
        dateCreated nullable: true, blank: true
        lastUpdated nullable: true, blank: true
		lastLogIn nullable: true, blank: true
		lastActivity nullable: true, blank: true
		lastViewed nullable: true, blank: true
        picture nullable: true, blank: true, size: 0..1000000 // upto 1MB
		claimedId nullable: true, blank: true
		apiKey nullable: true, blank: true
    }

    void setProjectIds(List ids) {
        this.project = ids.collect { Project.get(it) }
    }

	String toString()
	{
		return username
	}
    List getProjectIds() {

        this.project.collect { it.id }
    }

    static transients = ['projectIds']

   static mapping = {
        password column: '`password`'
        reportsTo lazy: false, cascade: "all"
        project lazy: false, cascade: "all"
        userTenantId column:'user_tenant_id'
   		tablePerHierarchy false
    }
   
    Set<Role> getAuthorities() {
        UserRole.findAllByUser(this).collect { it.role } as Set
    }


    def beforeInsert = {
        if (hourlyRate == null)
        hourlyRate = 0
        enabled = true
        username = username.toLowerCase()

    }
    def beforeUpdate = {
        if (hourlyRate == null)
        hourlyRate = 0
        username = username.toLowerCase()
    }
	

}




