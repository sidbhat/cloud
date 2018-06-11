package com.oneapp.cloud.core

import org.grails.formbuilder.Form;

import grails.plugin.multitenant.core.util.TenantUtils;
import com.macrobit.grails.plugins.attachmentable.domains.Attachment;

// Client management service
class ClientService {

    static transactional = false

	// Gets the number of active users for a client
	Long getTotalUsers(Long clientId){
		if ( clientId != null )
			return User.findAllByUserTenantIdAndEnabled(clientId,true).size()
	}
	
	// Returns the total attachment disk space used by the client
	double getTotalAttachmentSize( Long clientId) {
	
		if ( clientId != null ){
			double bd = 0.0
			TenantUtils.doWithTenant(clientId.toInteger()) {
			    def activityFeedIds = ActivityFeed.withCriteria(){
						projections{
							property("id")
						}
					}
				def formDomainName = Form.withCriteria(){
					eq "tenantId",clientId.toInteger()
					projections{
						property("id")
					}
				}
				if(formDomainName){
					def attachments = Attachment.withCriteria(){
						lnk{
							or{
								and{
									eq "referenceClass","org.grails.formbuilder.Form"
									'in' "referenceId",formDomainName
								}
								if(activityFeedIds){
									and{
										eq "referenceClass","com.oneapp.cloud.core.ActivityFeed"
										'in' "referenceId",activityFeedIds
									}
								}
							}
						}
						projections{
							sum("length");
						}
					}
					if(attachments && attachments[0])
						bd += attachments?attachments[0]:0
				}
			}
		 return bd   
		    
		}
	}
	
	double getTotalAttachmentSizeForTrialUser( User user) {
		
			if ( user != null ){
				double bd = 0.0
				TenantUtils.doWithTenant(user.userTenantId) {
					def activityFeedIds = ActivityFeed.withCriteria(){
						eq "createdBy.id",user.id
						projections{
							property("id")
						}
					}
					def formDomainName = Form.withCriteria(){
						eq "createdBy.id",user.id
						projections{
							property("id")
						}
					}
					if(formDomainName){
						def attachments = Attachment.withCriteria(){
							lnk{
								or{
									and{
										eq "referenceClass","org.grails.formbuilder.Form"
										'in' "referenceId",formDomainName
									}
									if(activityFeedIds){
										and{
											eq "referenceClass","com.oneapp.cloud.core.ActivityFeed"
											'in' "referenceId",activityFeedIds
										}
									}
								}
							}
							projections{
								sum("length");
							}
						}
						if(attachments && attachments[0])
							bd += attachments?attachments[0]:0
					}
				}
			 return bd
				
			}
		}
	
	// Returns the total number of forms for a client
	Long getTotalForms(Long clientId){
	
	
	}
	
	// Returns the total number of subscriptions for a client
	Long getTotalSubscriptions (Long clientId){
		if ( clientId != null )
			RuleSet.findAllByTenantId( clientId ).size()
	
	}
	
	// Returns the total number of rules for a client
	Long getTotalRules (Long clientId){
		if ( clientId != null )
			Rule.findAllByTenantId( clientId ).size()
	
	}
	
	// Returns the total number of feeds for a client
	Long getTotalFeeds(Long clientId){
		if ( clientId != null )
			 ActivityFeed.findAllByTenantId(clientId).size()
	}
	
	// Returns the total database size for a client
	BigDecimal getTotalDBSize(Long clientId) {
		
	}
	
	
}