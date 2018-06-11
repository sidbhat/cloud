

package com.oneapp.cloud.core

import java.util.Date;

import org.grails.comments.Commentable;
import org.grails.taggable.*
import org.grails.rateable.*
import org.hibernate.criterion.CriteriaSpecification;
import java.io.Serializable;
import grails.plugin.multitenant.core.groovy.compiler.MultiTenant
import com.macrobit.grails.plugins.attachmentable.core.*
import com.macrobit.grails.plugins.attachmentable.domains.*
import grails.plugin.multitenant.core.util.TenantUtils
// Main class to replace CompanyActivity
@MultiTenant
class ActivityFeed implements Attachmentable,Commentable,Taggable,Rateable,Serializable{
	
	def attachmentableService

	static auditable = [ignore:['version','lastUpdated','createdBy','dateCreated']]
	
	Long   shareId
    List<User> sharedUsers
    List<GroupDetails> sharedGroups 
    List<DropDown> sharedDepts
	List<Role> sharedRoles
    String visibility
	
	String activityContent
	ActivityFeedConfig config

	Date lastUpdated  
	Date dateCreated
	User createdBy
    String feedState
	Boolean isTask
	Date dueDate
	String referenceId
	Boolean taskStatus
	
	public static final String ARCHIVED="Archived"
	public static final String ACTIVE="Active"
    public static final String COMPANY="Company"
	public static final String GROUP="Group"
	public static final String USER="User"
	public static final String ROLE="Role"
	public static final String DEPARTMENT="Department"
	public static final String CUSTOM="Custom"
	
	static hasMany = [ sharedUsers : User, sharedGroups : GroupDetails, sharedDepts: DropDown, sharedRoles : Role]
	
	static constraints = {
	 config nullable:true, blank:true
	 activityContent type:'text', blank:false, nullable:false,size:0..1024
	 shareId nullable:true, blank:true
	 createdBy nullable:false, blank:false
	 lastUpdated nullable:true, blank:true
	 dateCreated nullable:true, blank:true
	 visibility nullable:true, blank:true, inList:[COMPANY,USER,GROUP,CUSTOM,ROLE,DEPARTMENT]
	 feedState nullable:false, blank:false, inList:[ARCHIVED, ACTIVE]
	 sharedUsers nullable:true, blank:true
	 sharedGroups nullable:true, blank:true
     sharedDepts nullable:true, blank:true
     sharedRoles nullable:true, blank:true
	 isTask nullable:true
	 dueDate nullable:true
	 referenceId nullable:true
	 taskStatus nullable:true
	}
	
	  // Named queries
	 static namedQueries = {
     findByVisibility { visibility ->
     		and {
     			eq 'visibility', visibility
     		}
     		order('lastUpdated','desc')
      }
      
      findByState{ state ->
     		and {
     			eq 'feedState', state
     		}
     		order('lastUpdated','desc')
      }
      
      findByCreatedDateRange {  from,to ->
     		and {
     			between 'dateCreated', from,to
     		}
     		order('lastUpdated','desc')
      }
      findByConfigName { type ->
      		and {
     			between 'config.configName', type
     		}
     		order('lastUpdated','desc')
      }
	  
  	}
  	
    static mapping = {
        cache true
		autoTimestamp false
    }
    
    def onAddComment = { comment ->
        lastUpdated = new Date()
    }
   
    def onAddAttachment = {Attachment attachment -> 
        lastUpdated = new Date()
    }
	
	def beforeDelete() {
		
		try {
				withNewSession{
					this?.comments.each{com->
						this.removeComment(com.id as Long);
					}
				}
				
				withNewSession{
					removeAttachments()
				}
				
				withNewSession{
					this?.tags.each{tag->
						this.removeTag(tag)
					}
				}
				
				withNewSession{
					def ratLinkList = RatingLink.findAllByRatingRef(this.id)
					ratLinkList.each{rlt->
						rlt.delete(flush:true)
					}
				}
				
				withNewSession{
					def activityNotification = ActivityNotification.findAllByActionOnFeed(this)
					activityNotification.each{an->
						an.delete(flush:true)
					}
				}
				
		} catch (Exception e) {
			log.error e
		}
	}
		
}


