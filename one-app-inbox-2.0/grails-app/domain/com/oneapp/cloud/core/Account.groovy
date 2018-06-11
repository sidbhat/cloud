 
package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant
import com.oneapp.cloud.core.*

@MultiTenant
class Account extends Base implements Serializable{

    String name
    User owner
    ContactDetails contact
    Address shipTo
    Address billTo
    String accountType
    String totalEmployees
    String industry
    String accountId
    String addDescription
    String externalURL
    String logoURL
    
    /**Social fields**/
    String followerCnt
    String likesCnt
    String latestActivity
    String headlines
    String summary
    
    static auditable = [ignore: ['version']]
    static embedded = ['shipTo', 'billTo']
    static constraints = {
        name(size: 2..54, nullable: false, blank: false)
        owner blank: true, nullable: true
        contact blank: true, nullable: true
        shipTo blank: true, nullable: true
        billTo blank: true, nullable: true
        totalEmployees blank: true, nullable: true
        industry blank: true, nullable: true
        accountId blank: true, nullable: true
        addDescription blank: true, nullable: true
        externalURL blank: true, nullable: true
        logoURL blank: true, nullable: true
        followerCnt blank: true, nullable: true
        likesCnt blank: true, nullable: true
        latestActivity blank: true, nullable: true
        headlines blank: true, nullable: true
    	summary blank: true, nullable: true
        accountType blank:false,nullable:false,inList:["Inactive", "Active", "Prospect"]
    }

    String toString() {
        name
    }
    
    static mapping= {
    	cache true
    }

}