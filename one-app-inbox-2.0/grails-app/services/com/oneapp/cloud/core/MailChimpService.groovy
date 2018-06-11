package com.oneapp.cloud.core
import com.google.appengine.repackaged.com.google.common.base.Flag.Integer;
import com.nwire.mailchimp.IMailChimpServices
import com.nwire.mailchimp.MailChimpServiceFactory
import com.nwire.mailchimp.MailChimpUtils
import grails.converters.JSON;
class MailChimpService{
	private versionServiceMap = [:]
	private IMailChimpServices	mcServices	= null;


	def getMailChimpService(def apiKey){
		def index=apiKey.indexOf("-")
		String ver=index!=-1?apiKey.substring(index+1,apiKey.length()):null
		String url=ver?("http://"+ver+".api.mailchimp.com/1.2/"):"http://.api.mailchimp.com/1.2/"
		if(!versionServiceMap[ver]){
			mcServices = MailChimpServiceFactory.getMailChimpServices(url);
			versionServiceMap[ver]=mcServices
		}
		versionServiceMap[ver]
	}


	def initialize( String apiKey ){
		mcServices=getMailChimpService(apiKey)
		final String ping = mcServices.ping(apiKey);
		IMailChimpServices.PING_SUCCESS.equals(ping)?true:false
	}


	def getLists(def apiKey) {
		def lists
		if(initialize(apiKey)){
			try{
				lists= mcServices?.lists(apiKey);
			}catch (Exception e) {
				log.error "error in get mailchimp lists"
			}
		}else{
			println "MailChimpService-getLists: "
		}
		return lists
	}


	def getListsFields(def apiKey,def listId){
		def lists
		try{
			mcServices=getMailChimpService(apiKey)
			lists= mcServices?.listMergeVars(apiKey,listId);
		}catch (Exception e) {
			log.error "error in get mail chimp fields"
		}
		return lists
	}
	def getlistInterestGroupings(def apiKey,def listId){
		def lists
		try{
			mcServices=getMailChimpService(apiKey)
			lists= mcServices?.listInterestGroupings(apiKey,listId);
		}catch (Exception e) {
			println "no chimp groups"
		}
		return lists
	}

	def addOrUpdateDataToMailChimpList(def apiKey,def listId,def email,
	def mergeData,def emailType=IMailChimpServices.EMAIL_TYPE_HTML,
	def optin=true,def update=false,
	def replaceInterests=true,welcomeEmail=false){
		try{
			mcServices=getMailChimpService(apiKey)
			def result=mcServices.listSubscribe( apiKey, listId,email, mergeData, emailType,optin,update,replaceInterests,welcomeEmail);
		}catch (Exception e) {
			log.error "error"+e
		}
		/*(boolean) listSubscribe((string) apikey, (string) id, (string) email_address, (array)
		 merge_vars [ , (string) email_type, (boolean) double_optin, (boolean) update_existing,
		 (boolean) replace_interests, (boolean) send_welcome ] )
		 Imp: Set Update =true in case of update
		 */
	}

	def removeDataFromMailChimpList(def apiKey,def listId,
	def mergeData,def deleteMember=false,def sendGoodBye=true,def sendNotifications=true){
		mcServices=getMailChimpService(apiKey)
		def result=mcServices.listUnsubscribe( apiKey, listId,mergeData?.email,deleteMember,sendGoodBye,sendNotifications);
		/*(boolean) listUnsubscribe((string) apikey, (string) id, (string) email_address [ , (boolean) delete_member, 
		 (boolean) send_goodbye, (boolean) send_notify ] )*/
	}
	/*def getDataFromMailChimpList(def apiKey,def listId,def status=true){
	 mcServices=getMailChimpService(apiKey)
	 def dataStatus=status?IMailChimpServices.STATUS_SUBSCRIBED:IMailChimpServices.STATUS_UNSUBSCRIBED
	 def dataFromMailChimp=mcServices.listMembers(apiKey, listId,dataStatus)
	 }*/
	def  saveMailchimp(def formAdmin,def form,def domainIstance){
	 	def mailChimpDetails=formAdmin.mailChimpDetails
		boolean result=false
		if(mailChimpDetails.sendChoice=="all" || sendChoice(mailChimpDetails.sendChoice,domainIstance)){
			def fieldMappings=JSON.parse(mailChimpDetails.fieldMappings)
			def listId=JSON.parse(mailChimpDetails.listId).key
			def mergeData=[:]
			def requiredFields=fieldMappings.required
			def notrequiredFields=fieldMappings.notRequired
			def groupFields=fieldMappings.groups
			try{
				result=requiredFields?true:false
				requiredFields.each{k,v->
					if(!result ||domainIstance."${v}"==null||domainIstance."${v}"==""){
						return false;
					}else{
						mergeData.put(k, domainIstance."${v}")
					}
				}

				if(notrequiredFields){
					try{ // this is just to avoide JSON NULL object Exception
						notrequiredFields?.each{k,v->
							mergeData.put(k, domainIstance."${v}")
						}
					}catch (Exception e) {
					}
				}
	 			if(groupFields){
					def groupMapList=[]
					try{
						groupFields?.each{k,v->
							//"Grouping 5101_xxx":"field1344871982993_My Check 1"
							def index1=v.indexOf("_")
							String field=index1!=-1?v.substring(0,index1):null
							String val=index1!=-1?v.substring(index1+1,v.length()):null
							def r=(field && val && domainIstance."${field}".contains(val))?true:false
							if(r){
								def index=k.indexOf("_")
								String grp=index!=-1?k.substring(0,index):null
								String grpvalue=index!=-1?k.substring(index+1,k.length()):null
								def groupMap=groupMapList.find{it?.name==grp}
								if(!groupMap){
									groupMap=[:]
									groupMapList<<groupMap
									groupMap.name=grp
									if(!groupMap.groups)
										groupMap.groups=[]
								}
								groupMap.groups<<grpvalue
							}
						}
					}catch(Exception e){}
					if(groupMapList){
						groupMapList.each{
							it.groups = it.groups.join(",")
						}
						//					groupMapList.each{
						mergeData.put("GROUPINGS", groupMapList)
						//					}
					}
				}
				result= addOrUpdateDataToMailChimpList(mailChimpDetails.apikey,listId,mergeData.EMAIL,mergeData,IMailChimpServices.EMAIL_TYPE_HTML,mailChimpDetails.optinemail,false,true,false)
			}catch (Exception e) {
				log.error  "error"+e
				return false;
			}
		}
		return result
	}
	def sendChoice(String sendChoices,def domainIstance){
		boolean result= false
		def index=sendChoices.indexOf("_")
		String field=index!=-1?sendChoices.substring(0,index):null
		String val=index!=-1?sendChoices.substring(index+1,sendChoices.length()):null
		result=(field && val && domainIstance."${field}".contains(val))?true:false
	}
}