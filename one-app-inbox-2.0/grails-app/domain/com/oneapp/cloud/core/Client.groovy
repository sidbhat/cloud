 
package com.oneapp.cloud.core

import java.io.Serializable;

import org.codehaus.groovy.grails.commons.ConfigurationHolder as Conf
import grails.plugin.multitenant.core.util.TenantUtils
import org.grails.formbuilder.*;

import com.oneapp.cloud.core.log.Tracker;

class Client extends Base implements Serializable{

    String name
    String domain
    String description
    byte[] logo
    String logoURL
	String consumerKey
   // Event event
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
	Long maxFormEntries = 100
	String gcmApiKey
	Plan plan
	boolean isRecursive = false	
	
    static auditable = [ignore: ['version']]

    static hasMany = [user: User]

    /** * Delete all users and projects if a client is deleted  ***/
    /** * Time and Expenses are tied to projects so they will   ***/
    /** * be deleted when a project is deleted                  ***/
    def beforeDelete() {
        // Common tables
       

        try {

            TenantUtils.doWithTenant((Integer) this.id) {
				
				ActivityFeedConfig.withNewSession {
					ActivityFeedConfig.findAllByTenantId(this.id).each {ActivityFeedConfig obj ->
						obj.delete()
					}
				}
				
				RuleSet.withNewSession {
					RuleSet.findAllByTenantId(this.id).each{RuleSet obj ->
						obj.delete();
					}
				}
            
                Form.withNewSession{
					Form.findAllByTenantId(this.id).each{Form obj ->
						
						def formTemplateList = FormTemplate.findAllByForm(obj)
						if ( formTemplateList ){
							formTemplateList.each { FormTemplate ft ->
								ft.delete(flush:true)
							}
						}
						// Delete FormAdmin if it exists
						def formAdminInstance = FormAdmin.findByForm(obj)
						if ( formAdminInstance ){
							formAdminInstance.delete(flush:true)
						}
						obj.delete();
					}
				}
				
                GroupDetails.withNewSession {
                    GroupDetails.findAllByTenantId(this.id).each {GroupDetails obj ->
                        obj.delete()
                    }
                }
				
                DropDown.withNewSession {
                    DropDown.findAllByTenantId(this.id).each {DropDown obj ->
                        obj.delete()
                    }
                }
				
				EmailSettings.withNewSession {
					EmailSettings.findAllByTenantId(this.id).each {EmailSettings obj ->
						obj.delete()
					}
				}
				
				EmailDetails.withNewSession {
					EmailDetails.findAllByTenantId(this.id).each {EmailDetails obj ->
						obj.delete()
					}
				}
				
				Tracker.withNewSession {
					Tracker.findAllByClientID(this.id).each{ Tracker obj ->
						obj.delete()
					}
				}
           
        }

        } catch (Exception e) {
            log.error e
        }
		
		try {
			User.withNewSession {
				User.findAllByUserTenantId(this.id).each {User obj ->

					deleteDir(new File(Conf.config.rootPath + this.id + "/")) // Excel upload folder for the client

					UserRole.findAllByUser(obj).each {UserRole o ->
						o.delete()
					}
					
					UserProfile.findAllByUser(obj).each{UserProfile up ->
						up.delete()
					}
					
					
					obj.delete()
				}
			}
		} catch (Exception e) {
			log.error e
		}


    }

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
		plan nullable:true
		isRecursive nullable:true
		maxFormEntries nullable:true
		consumerKey nullable: true,unique: true, size: 2..54
		gcmApiKey nullable: true,blank :true
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

