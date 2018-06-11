 
package com.oneapp.cloud.core

import java.io.Serializable;

import org.codehaus.groovy.grails.commons.ConfigurationHolder as Conf

import com.oneapp.cloud.time.*
import grails.plugin.multitenant.core.util.TenantUtils
import org.grails.formbuilder.*;

class Client implements Serializable{

    String name
    String domain
    String description
    byte[] logo
    String logoURL
	String consumerKey
   // Event event
    //Account account
    String externalURL
    boolean licenseCollaboration
    boolean licenseFormBuilder
	Long maxUsers=5
	Date validFrom
	Date validTo
	boolean disableEmail
	Long form=5
	Long maxAttachmentSize = 2000  //max attachment size in MB
	Long maxEmailAccount = 2
	Long maxEmailFetchCount=100
	String gcmApiKey
	
    static auditable = [ignore: ['version']]

    static hasMany = [user: User]

    /** * Delete all users and projects if a client is deleted  ***/
    /** * Time and Expenses are tied to projects so they will   ***/
    /** * be deleted when a project is deleted                  ***/
   

    static mapping = {
        user cascade: 'all-delete-orphan'
    //    project cascase: 'all-delete-orphan'
        sort 'name'
    	cache true
		gcmApiKey type:'text' 
    }

    static constraints = {
        name nullable: false, blank: false, unique: true, size: 2..54
        description nullable: true, blank: true, size: 2..1024
        domain nullable: true, blank: true
        user nullable: true, blank: true
       // project nullable: true, blank: true
        logo nullable: true, blank: true
       // event nullable: true, blank: true
       // account nullable: true, blank: true
        externalURL nullable: true, blank: true
        licenseCollaboration nullable: true, blank: true
     	maxUsers nullable: false, blank: false
   		validFrom nullable: true, blank: true
   		validTo nullable: true, blank: true
   		disableEmail nullable: true, blank: true
   		logoURL nullable: true, blank: true, url:true
   		licenseFormBuilder nullable: true, blank: true
		form nullable:false,blank:false,default:5
		maxAttachmentSize nullable:false,blank:false,default:2000
		consumerKey nullable: true,unique: true, size: 2..54
		gcmApiKey nullable:false,blank:false
		
    }
    
  
    String toString() {
        name
    }

    boolean deleteDir(File dir) {
      //  println dir.isDirectory()
        if (dir.isDirectory()) {
            String[] children = dir.list();
            for (int i = 0; i < children.length; i++) {
                boolean success = deleteDir(new File(dir, children[i]));
                if (!success) {
                    return false;
                }
            }
        }

        // The directory is now empty so delete it
        return dir.delete();
    }

}

