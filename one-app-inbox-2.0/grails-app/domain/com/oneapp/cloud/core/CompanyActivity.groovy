package com.oneapp.cloud.core

import java.util.Date;
import java.io.Serializable;
import org.grails.comments.Commentable;
import org.grails.taggable.*
import org.grails.rateable.*
import grails.plugin.multitenant.core.groovy.compiler.MultiTenant
import com.macrobit.grails.plugins.attachmentable.core.Attachmentable

@MultiTenant
class CompanyActivity implements Attachmentable,Commentable,Taggable,Rateable, Serializable{

	public static final String COMPANY="COMPANY"
	public static final String GROUP="GROUP"
	public static final String USER="USER"
	
    String username
	String activityContent
	String visibility
	Date   activityDate
	Long   shareId
	String shareType
	String shareLink
	String shareActionRelLink  //Relative link to the action to be performed on 
	String shareActionAbsLink  //Absolute link to the action to be performed on 
	
	String url
	byte[] media   //document, video, picture
	String mediaType
	String mediaName
	
	Date dateUpdated  //date/time when comment/tag or activity was updated
	Date dateCreated
	User createdBy
	static auditable = [ignore: ['version', 'client', 'dateUpdated']]
    
	static constraints = {
	 activityContent type:'text', blank:false, nullable:false,size:0..512
	 visibility(inList:[COMPANY, GROUP, USER] )
	 shareId nullable:true, blank:true
	 url nullable:true, blank:true
	 createdBy nullable:false, blank:false
	 mediaType nullable:true, blank:true
	 shareType nullable:true, blank:true
	 shareLink nullable:true, blank:true
	 shareActionRelLink nullable:true, blank:true
	 shareActionAbsLink nullable:true, blank:true
	 mediaName nullable:true, blank:true
	 dateUpdated nullable:true, blank:true
	 dateCreated nullable:true, blank:true
	 media nullable:true, blank:true,size: 0..2000000
    }
    static mapping = {
        cache true
    }
    
    def onAddComment = { comment ->
        dateUpdated = new Date()
    }
    def beforeInsert = {
        dateUpdated = new Date()
        dateCreated = new Date()
        if (activityContent == null)
        	activityContent=""
    }

    def beforeUpdate = {
        dateUpdated = new Date()
        if (activityContent == null)
        	activityContent=""
    }

	
}
