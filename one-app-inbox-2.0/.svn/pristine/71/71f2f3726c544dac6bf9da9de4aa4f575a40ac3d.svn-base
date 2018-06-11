package com.oneapp.cloud.core
import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class ContactDetails implements Serializable{

	/** Control fields **/
	String user
	String source
	Date lastUpdated
	boolean personal
	User createdBy
	String contactDate
	
	/** Contact basic details **/
	String contactName
	String firstName
	String lastName
	String contactId
	String title
	String headlines
	String latestActivity
	String currentStatus
	String summary
	String mobile, phone, fax
	String companyName
	String department
	String picture
	
	/** External links **/
	String profileURL
	String pictureURL
	String linkedinPictureURL
	String twitterPictureURL
	String facebookPictureURL
	String email
	Date birthday
	String city
	String location
	String addDescription
	String friendCnt
	String followerCnt
	String recommendations
	
	static auditable = [ignore: ['version', 'client', 'lastUpdated']]
	
	static constraints = {
	    headlines blank:true, nullable:true
	    currentStatus blank:true, nullable:true
	    picture blank:true, nullable:true
	    summary blank:true, nullable:true
	    user blank:true, nullable:true
	    mobile blank:true, nullable:true
	    phone blank:true, nullable:true
	    fax blank:true, nullable:true
	    contactId blank:true, nullable:true
	    pictureURL blank:true, nullable:true
	    source blank:true, nullable:true
	    email blank:true, nullable:true
	    birthday blank:true, nullable:true
	    city blank:true, nullable:true
	    firstName blank:true, nullable:true
	    lastName blank:true, nullable:true
	    contactDate blank:true, nullable:true
	    linkedinPictureURL blank:true, nullable:true
	    twitterPictureURL blank:true, nullable:true
	    facebookPictureURL blank:true, nullable:true
	    location blank:true, nullable:true
	    title blank:true, nullable:true
	    department blank:true, nullable:true
	    companyName blank:true, nullable:true
	    addDescription blank:true, nullable:true, size:0..2048
    	contactName nullable: false, blank: false, size:2..54
    	personal nullable: true, blank: true
    	createdBy nullable: true, blank: true
    	friendCnt nullable: true, blank: true
    	followerCnt nullable: true, blank: true
    	recommendations nullable: true, blank: true
    	profileURL nullable: true, blank: true
    	latestActivity nullable: true, blank: true
    }
    
    String toString() {
        contactName
    }

	
}
