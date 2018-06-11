

package com.oneapp.cloud.core
import org.grails.taggable.*
import java.text.SimpleDateFormat;

import grails.converters.JSON;
import grails.plugin.multitenant.core.util.TenantUtils;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.web.pages.FastStringWriter;
import org.grails.formbuilder.Form;
import org.grails.formbuilder.FormAdmin;
import org.grails.formbuilder.FormUtils;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;

/**
 * @author Main Pal
 *
 */
class ApiV1Controller {
	
	def springSecurityService
	def sqlDomainClassService
	def clientService
	def utilService
	def dataSource
	def gcmPushService
	static final SimpleDateFormat dateFormatter = new SimpleDateFormat(ConfigurationHolder.config.format.date)
	
	//below two parameters will allow to configure number of call in a duration. To limit per day use: MM/dd/yyyy and to limit per minute use: MM/dd/yyyy HH:mm
	static final SimpleDateFormat dateFormatterWithoutTime = new SimpleDateFormat('MM/dd/yyyy HH')
	static final def apiCallLimit = 10000

	// the delete, save and update actions only accept POST requests
	static allowedMethods = [save: "POST", update: "POST", delete: "POST",deleteFeed:"POST"] 
	
	//This map is used to send user the overridden text for type like linkVideo is embedHTML
	//Documented
	static def fieldTypeOverrideMap = ['PlainText':'RichText','SingleLineDate':'Date','SingleLineText':'Text','SingleLineNumber':'Number','MultiLineText':'LongText','PlainTextHref':'Link','dropdown':'DropDown','GroupButton':'Radio','CheckBox':'Checkbox','LikeDislikeButton':'LikeDislike','AddressField':'Address','NameTypeField':'Name','FormulaField':'Formula','LinkVideo':'EmbedHTML']
	
	//TODO consider paypal type field later in these lists whether to add or not
	
	//non-persistable fields list will tell to send or not to send name, defaultValue, isRequired and hideFromUser fields
	static def nonPersistableFieldsList = ['ImageUpload','LinkVideo','PlainText','PlainTextHref','PageBreak']
	
	//resizable fields list will tell field's size should be small, medium or large
	static def resizableFieldsList = ['SingleLineDate','SingleLineText','Phone','Email','SingleLineNumber','MultiLineText','dropdown','FormulaField','LookUp']
	static def classMap = ['sClass':'small','mClass':'medium','lClass':'large']
	
	//mappable fields list will tell which fields can be mapped (related to lookup)
	static def mappableFieldsList = ['SingleLineDate','SingleLineText','Phone','Email','SingleLineNumber','MultiLineText','dropdown','AddressField','NameTypeField']
	
	//non-formattable fields list will tell which fields can not be formatted (font, fontSize, fontWeight, fontStyle, textDecoration)
	static def nonFormattableFieldsList = ['PlainText']
	
	//input-limit fields list will tell which fields can have minLength and maxLength attributes
	static def inputLimitFieldsList = ['SingleLineText','Phone','Email','SingleLineNumber','MultiLineText']
	
	def index = {
		if(params.test){
			
		}else{
			//TODO Will redirect it to API Docs.
			render ""
		}
	}
	
	def forgetpasswordService = {
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		if(params.username){
			def username = params.username
			UUID uuid = UUID.randomUUID()
			def password=uuid.toString()[1..6]
			def userInstance = User.findByUsername(username);
			if(userInstance){
				if(!userInstance.accountLocked && userInstance.enabled){
					userInstance.password = springSecurityService.encodePassword(password)
					if(userInstance.save())
						TenantUtils.doWithTenant(userInstance?.userTenantId) {
							AsynchronousEmailStorage async = new AsynchronousEmailStorage()
							async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
							async.emailTO = "${userInstance.username}"
							async.emailSubject="Password Reset"
							async.emailData="Hello ${userInstance.firstName},\n\n Your password was reset. \n Please login to ${grailsApplication.config.grails.serverURL} using following credentials-\n Username: ${userInstance.username}\n Password: ${password}\n\nThank you,\nYour Form Builder Team"
							async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
							if(async.save(flush:true)){
							def successMessage=[:]
							successMessage."Body" = "Password sent to your email id"
							res<<successMessage
							}
					}
					}else{
					  errorMessage = message(code:'api.user.login.inactive',args:[],default:(userInstance.accountLocked?'Account is locked':!userInstance.enabled?'Account is not active':''))
					}
			}else{
				errorMessage = message(code:'api.user.login.fail',args:[],default:"User not found with email ${username}" )
			}
		}else{
			errorMessage = message(code:'api.user.login.noUsernameOrPassword',args:[],default:'Username  is required')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertMgsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Message:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def fblogin={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		if(params.consumerKey){
			Client client=Client.findByConsumerKey(params.consumerKey)
			if(client){
				def username = params.username
				if(username){
					def user = User.findByUsername(username);
					if(!user){
						UUID uuid = UUID.randomUUID();
						def key = uuid.toString()[4..22];
						user = new User()
						user.username=username;
						user.password="password";
						user.enabled=true;
						user.userTenantId=client.id;
						user.firstName=params.firstName?:username;
						user.lastName=params.lastName?:'';
						user.apiKey= key;
						user.facebookProfile=params.fbToken;
						user.title= "FB User";
						user.pictureURL= params.pictureURL;
						user.claimedId=params.fbId;
						user.socialNwName="FB"
						user.save()
						UserRole ur = new UserRole(user: user, role:Role.findByAuthority(Role.ROLE_USER))
						ur.save();
					}else{
						user.firstName=params.firstName?:''
						user.lastName= params.lastName?:''
						user.facebookProfile=params.fbToken
						user.pictureURL=params.pictureURL
						user.save()
					}
					def userProfileInstance = new UserProfile()
					userProfileInstance.emailSubscribed=false
					userProfileInstance.user = user
					userProfileInstance.save()
                    if(params.fbfriend){
						   def myfriends=params.fbfriend.split(",")
						   if(!userProfileInstance.myFriendsList)   
	                             userProfileInstance.myFriendsList=[]
	                        myfriends.each{
		                       def currentUserFriend=User.findByClaimedId(it)
		                        if(currentUserFriend && !userProfileInstance.myFriendsList.contains(currentUserFriend))
		                            userProfileInstance.myFriendsList<<currentUserFriend
	                       }
	                      userProfileInstance.save()
				    }
					def User = [:]
					User.Id = user.id
					User.FaceBookId=user.claimedId
					User.FirstName = user.firstName?:''
					User.LastName = user.lastName?:''
					User.Username = user.username?:''
					User.ApiKey = user.apiKey
					User.MobilePhone = user.mobilePhone?:''
					User.OfficePhone = user.officePhone?:''
					User.fbToken=user.facebookProfile
					User.Role = user.authorities?.authority?.get(0)?:''
					def pictureURL
					def attachments=UserProfile.findByUser(user)?.attachments
					if(attachments){
						pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${attachments[0].id}?y=${user.apiKey}"
					 } else if(user?.pictureURL && user.pictureURL?.length() > 0){
						pictureURL = user.pictureURL
					 }
					User.ImageURL = pictureURL?:''
					res << User
					if(params.deivceToken)
						 gcmPushService.addDevice(params.deivceToken,user)
				}else{
					errorMessage = message(code:'api.user.login.noUsernameOrPassword',args:[],default:'Some error occured')
				}
			}else{
			errorMessage = message(code:'client.not.found.consumerkey',args:[],default:"Client not found.")
			}
		}else{
				errorMessage = message(code:'api.user.login.noUsernameOrPassword',args:[],default:'Some error occured')
			}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertUsersToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Users:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def login = {
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		if(params.username && params.password){
			def username = params.username
			def password = springSecurityService.encodePassword(params.password)
			def user = User.findByUsernameAndPassword(username,password);
			def User = [:]
			if(user){ 
				boolean isTenantExpired=isUserTenantExpired(user.userTenantId.toLong()) 
				if(!user.accountLocked && user.enabled && !isTenantExpired){
					User.Id = user.id
					User.FirstName = user.firstName?:''
					User.LastName = user.lastName?:''
					User.Username = user.username?:''
					if(!user.apiKey){
						UUID uuid = UUID.randomUUID()
						user.apiKey = uuid.toString()[4..22]
						user.save()
					}
					User.ApiKey = user.apiKey
					User.MobilePhone = user.mobilePhone?:''
					User.OfficePhone = user.officePhone?:''
					User.Role = user.authorities?.authority?.get(0)?:''
					def pictureURL
					def attachments=UserProfile.findByUser(user)?.attachments
					if(attachments){
						pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${attachments[0].id}?y=${user.apiKey}"
					 } else if(user?.pictureURL && user.pictureURL?.length() > 0){
						pictureURL = user.pictureURL
					 }
					User.ImageURL = pictureURL?:''
					res << User
					if(params.dToken && params.dId)
					     gcmPushService.addDevice(params.dId,params.dToken,user)
					}else{ 
					  errorMessage = message(code:'api.user.login.inactive',args:[],default:(user.accountLocked?'Account is locked':!user.enabled?'Account is not'+
						  ' active':isTenantExpired?'Client account expired.For details please email administrator (admin@yourdomain.com)':''))
					}
			}else{
				errorMessage = message(code:'api.user.login.fail',args:[],default:'Username or password is incorrect')
			}
		}else{
			errorMessage = message(code:'api.user.login.noUsernameOrPassword',args:[],default:'Username or password not sent')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertUsersToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Users:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	
	def users = {
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		String pagging="YES" 
		int totalCount=1
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				if(allowUserAPICall(currentUser)){
					def allUsers
					if(!currentUser.authorities?.authority.contains(Role.ROLE_TRIAL_USER)){
						allUsers = User.findAllByUserTenantId(currentUser.userTenantId)
						allUsers.removeAll{it.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)}
						totalCount=allUsers.size()
						pagging=params.isPagging?:"YES"
						if("YES".equals(pagging)){
							params.max =Math.min(params.pageSize ? params.pageSize.toInteger() : 25 , 100)
							params.offset=params.pageStart ? params.pageStart.toInteger() : 0
							if(allUsers){
								def maxSize = (params.offset+params.max)>allUsers.size() ? allUsers.size() : (params.offset+params.max);
								params.offset = (maxSize>params.offset)?params.offset:0
								allUsers = allUsers.subList (params.offset,maxSize)
							  }
						}else{
							params.max=allUsers.size()
							params.offset=0
						}
					}else{
						params.max=1
						params.offset=0
						allUsers = [currentUser]
					}
					allUsers.each{u->
						if(u.username != 'publicuser@yourdomain.com')
							res << getDataPopulatedForUser(u,currentUser)
					}
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{ 
				out = convertUsersToXML(res,params.max,params.offset,Math.min((params.offset+params.max),totalCount),totalCount)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{ 
				out = [Users:res,"Max":params.max ,"pageStart": params.offset, 
					 "pageEnds" :Math.min((params.offset+params.max),totalCount),"TotalCount" : totalCount] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
def getMyFriendList={
	def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
	def responseContentType = ""
	def out = new FastStringWriter()
	def res = []
	def errorMessage
	if(params.apiKey){
		def apiKey = params.apiKey
		def currentUser = User.findByApiKey(apiKey);
		if(currentUser){
		        springSecurityService.reauthenticate(currentUser.username)
				session['user'] = currentUser
				if(allowUserAPICall(currentUser)){
					def userProfileInstance = UserProfile.findByUser(currentUser)
					if(userProfileInstance.myFriendsList)
						res.addAll(userProfileInstance.myFriendsList)
			}else{
				errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
			}
		}else{
			errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
		}
	}else{
		errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
	}
	if(xmlResponse){//For XML response
		if(errorMessage){
			out = errorMessageXML(errorMessage)
		}else{
			out = convertUsersToXML(res)
		}
		responseContentType = 'text/xml'
		if(params.pretty){
			responseContentType = 'text/html'
			out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
		}
	}else if(jsonResponse){//For JSON response
		if(errorMessage){
			out = [ErrorMessage:errorMessage] as JSON
		}else{
			out = [Users:res] as JSON
		}
		responseContentType = 'text/json'
		if(params.pretty){
			out = out.toString(true)
		}
	}
	render(status: 200, contentType: responseContentType, text: out)
}
	def updateProfile={ 
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		def successMessage=[:]
		if(params.apiKey){  
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				springSecurityService.reauthenticate(currentUser.username)
				session['user'] = currentUser
				if(allowUserAPICall(currentUser)){
					def userProfileInstance = UserProfile.findByUser(currentUser)
					if (!userProfileInstance) {
						userProfileInstance = new UserProfile()
						userProfileInstance.emailSubscribed=false
					}
					userProfileInstance.user = currentUser
					userProfileInstance.properties = params
		            if (!userProfileInstance.hasErrors() && userProfileInstance.save()) {
						if(params.profilePicture && params.profilePicture.size>0){
							boolean isImage
							def contentType=['image/jpeg','image/gif','image/png','image/bmp']
							contentType.each {v->
							   String itType=params?.profilePicture?.getContentType()
							   if(itType.toLowerCase().contains(v)){
								   isImage =true
								   return
								   }
						        }
							if(isImage){
								userProfileInstance.removeAttachments()
								attachUploadedFilesTo(userProfileInstance)
								//successMessage."Body" = "Profile Updated"
								//res<<successMessage
							}else{
						    	errorMessage = message(code:'profilePicture.not.created',args:[],default:'Please only upload image')
							}
							}
						       def User = [:]
								User.Id = currentUser.id
								User.FirstName = currentUser.firstName?:''
								User.LastName = currentUser.lastName?:''
								User.Username = currentUser.username?:''
								User.ApiKey = currentUser.apiKey
								User.MobilePhone = currentUser.mobilePhone?:''
								User.OfficePhone = currentUser.officePhone?:''
								def pictureURL
								def attachments=userProfileInstance?.attachments
								if(attachments){
									pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${attachments[0].id}?y=${currentUser.apiKey}"
								 } else if(currentUser?.pictureURL && currentUser.pictureURL?.length() > 0){
									pictureURL = currentUser.pictureURL
								 }
								User.ImageURL = pictureURL?:''
								res<<User
			            } else {
			               errorMessage= errorlists(userProfileInstance.errors)
			            }
					}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = out = convertUsersToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Users:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def getDataPopulatedForUser(def user , def cu){
		def User = [:]
		User.Id = user.id
		User.FirstName = user.firstName?:''
		User.LastName = user.lastName?:''
		User.Username = user.username?:''
		if(!user.apiKey){
			UUID uuid = UUID.randomUUID()
			user.apiKey = uuid.toString()[4..22]
			user.save()
		}
		//User.ApiKey = user.apiKey
		User.MobilePhone = user.mobilePhone?:''
		User.OfficePhone = user.officePhone?:''
		def authorityList = user.authorities?.authority
		User.Role = authorityList && authorityList.size()>0?(authorityList.get(0)?:''):''
		def pictureURL
		def attachments=UserProfile.findByUser(user)?.attachments
		if(attachments){
			pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${attachments[0].id}?y=${cu.apiKey}"
		 } else if(user?.pictureURL && user.pictureURL?.length() > 0){
			pictureURL = user.pictureURL
		 }
		User.ImageURL = pictureURL?:'' 
		return User
	}
	
	def forms = {
		def apiKey = params.apiKey
		def currentUser
		def errorMessage
		if(!apiKey){
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}else{
			currentUser = User.findByApiKey(apiKey)
			if(!currentUser){
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}
		def out = new FastStringWriter()
		def res = []
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		if(currentUser){
			if(allowUserAPICall(currentUser)){
				def currentUserAuthorities = currentUser.authorities?.authority
				def forms
				def isAdmin = currentUserAuthorities?.contains(Role.ROLE_ADMIN) || currentUserAuthorities?.contains(Role.ROLE_HR_MANAGER) || currentUserAuthorities.contains(Role.ROLE_SUPER_ADMIN)?true:false
				def isTrialUser = currentUserAuthorities?.contains(Role.ROLE_TRIAL_USER)?true:false
				def isUser = currentUserAuthorities?.contains(Role.ROLE_USER)?true:false
				def formAdminList = FormAdmin.createCriteria().list(){
					if(isUser){
						or{
							ne 'formLogin','Login'
							publishedWith{
								eq 'id',currentUser.id
							}
						}
						form{
							if(params.formId){
								eq 'id',params.formId.toLong()
							}
							eq 'tenantId',currentUser.userTenantId
						}
					}else if(isTrialUser){
						form{
							if(params.formId){
								eq 'id',params.formId.toLong()
							}
							createdBy{
								eq 'id',currentUser.id
							}
							eq 'tenantId',currentUser.userTenantId
						}
					}else if(isAdmin){
						form{
							if(params.formId){
								eq 'id',params.formId.toLong()
							}
							eq 'tenantId',currentUser.userTenantId
						}
					}
				}
				def responseList
				if(formAdminList){
					responseList = sqlDomainClassService.responseCountLastUpdate(formAdminList.form)
				}
				
				formAdminList?.each{FormAdmin formAdmin->
					def form = formAdmin.form
					def Form = [:]
					def settings = JSON.parse(form.settings)
					Form.Id = form.id
					Form.Name = settings.en.name?:''
					Form.Description = settings.en.description?:''
					Form.Access = formAdmin?.formLogin?:'';//Login (SystemUser), Public, Password
					Form.RedirectMessage = formAdmin?.formSubmitMessage?:''
					Form.RedirectURL = formAdmin?.redirectUrl?:''
					Form.StatusField = formAdmin?.statusField?.name?:''
					Form.BlockUserChangeValues = formAdmin?.blockUserEditing
					Form.ShowStatusToUser = formAdmin?.showStatusToUser?:''
					def recentCountMap = responseList.find{it.id == form.id}
					Form.ResponseCount = recentCountMap?.count
					Form.RecentEntryTime = recentCountMap?.recentEntry?:''
					Form.RecentEntryId = recentCountMap?.lastUpdatedEntryId?:''
					Form.OpenForEdit = formAdmin?.openForEdit?1:0
					Form.FieldLabelAlignment = settings.labelDisplay=="0"?'top':(settings.labelDisplay=="1"?'left':'right')
					Form.Font = settings.en?.styles?.fontFamily?:''
					Form.FontSize = settings.en?.styles?.fontSize?:''
					Form.Heading = settings.en.heading
					Form.HeadingAlignment = settings.en.classes[0]?.replace('Align','')?:''
					Form.HeadingFontWeight = settings.en?.styles?.fontStyles[0] == 1 ? 'bold' : 'normal'
					Form.HeadingFontStyle = settings.en?.styles?.fontStyles[1] == 1 ? 'italic' : 'normal'
					Form.HeadingTextDecoration = settings.en?.styles?.fontStyles[2] == 1 ? 'underline' : 'none'
					Form.EmbededCSS = settings?.en?.CSS?:''
					Form.EmbededJS = settings?.en?.js?:''
					Form.Recaptcha = settings?.reCaptcha?1:0
					Form.DateCreated = form.dateCreated?:''
					Form.LastUpdated = form.lastUpdated?:''
					res << Form
				}
			}else{
				errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
			}
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out << errorMessageXML(errorMessage)
			}else{
				out << convertFormsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Forms:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	
	def fields = {
		def apiKey = params.apiKey
		def currentUser
		def errorMessage
		if(!apiKey){
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}else{
			currentUser = User.findByApiKey(apiKey)
			if(!currentUser){
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}
		
		Form form = Form.read(params.formId)
		Form parentForm
		if(form.formCat == 'S'){
			if(params.parentFormId){
				parentForm = Form.read(params.parentFormId)
				def subFormFields = parentForm?.fieldsList?.find{it.type == 'SubForm'}
				def subForm = subFormFields?.find{JSON.parse(it.settings)?.subForm == params.formId}
				if(!subForm){
					parentForm = null
				}
			}
		}
		def out = new FastStringWriter()
		def res = []
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def pageCount = 1
		if(form && isFormAccessible(parentForm?:form,currentUser)){
			if(allowUserAPICall(currentUser)){
				form.fieldsList.each{field->
					if(field.type == 'PageBreak'){
						pageCount++
					}else{
						def Field = [:]
						def settings = JSON.parse(field.settings)
						
						if(!nonPersistableFieldsList.contains(field.type)){
							if(field.type=='FileUpload'){
								Field.Name = field.name+'_file'
								Field.MaxSize = settings.maxSize
								Field.Unit = settings.unit
							}else{
								Field.Name = field.name
							}
							Field.DefaultValue = settings.en.value?:''
							Field.IsRequired = settings.required?1:0
							Field.IsHideFromUser = settings.en.hideFromUser?1:0
						}
						Field.Title = settings.en.text?:settings.en.label?:''
						Field.Description = settings.en.description?:''
						Field.Type = fieldTypeOverrideMap[field.type]?:field.type//See declaration of fieldTypeOverrideMap
						if(params.test)
							Field.type = field.type//Only for test case
						
						if(resizableFieldsList.contains(field.type)){
							Field.fieldSize = classMap[settings.fieldSize]?:'medium'
						}
						if(mappableFieldsList.contains(field.type)){
							Field.MappedForm = settings.mapMasterForm && settings.mapMasterField?settings.mapMasterForm:''
							Field.MappedField = settings.mapMasterForm && settings.mapMasterField?settings.mapMasterField:''
						}
						if(!nonFormattableFieldsList.contains(field.type)){
							Field.Font = settings.en.styles.fontFamily?:''
							Field.FontSize = settings.en.styles.fontSize?:''
							Field.FontWeight = settings.en.styles.fontStyles[0] == 1 ? 'bold' : 'normal'
							Field.FontStyle = settings.en.styles.fontStyles[1] == 1 ? 'italic' : 'normal'
							Field.TextDecoration = settings.en.styles.fontStyles[2] == 1 ? 'underline' : 'none'
						}
						if(inputLimitFieldsList.contains(field.type)){
							Field.MinLength = settings.minRange ?:''
							Field.MaxLength = settings.maxRange ?:''
						}
						
						if(field.type=='SingleLineDate'){
							def SubFields = []
							Field.ShowCalendar = settings.showCalendar?1:0
							Field.TimeFormat = settings.timeFormat?:''
							if(!settings.showCalendar){
								def SubField = [:]
								SubField.Name = field.name+'dateMM'
								SubField.Label = 'Month'
								SubFields << SubField
								SubField = [:]
								SubField.Name = field.name+'dateDD'
								SubField.Label = 'Date'
								SubFields << SubField
								SubField = [:]
								SubField.Name = field.name+'dateYYYY'
								SubField.Label = 'Year'
								SubFields << SubField
							}
							if(settings.timeFormat){
								def SubField = [:]
								SubField.Name = field.name+'Hours'
								SubField.Label = 'Hours'
								SubFields << SubField
								SubField = [:]
								SubField.Name = field.name+'Minutes'
								SubField.Label = 'Minutes'
								SubFields << SubField
								if(settings.timeFormat == 'hh:mm a'){
									SubField = [:]
									SubField.Name = field.name+'Meridian'
									SubField.Label = 'Meridian'
									SubFields << SubField
								}
							}
							Field.SubFields = SubFields
						}else if(field.type == 'SingleLineText' || field.type == 'MultiLineText'){
							Field.Restriction = settings.restriction != 'no'?settings.restriction:''
						}else if(field.type == 'Phone'){
							Field.Country = settings.format
						}else if(field.type == 'SingleLineNumber'){
							Field.Decimal = settings.decimalPlaces?:2
							Field.Currency = settings.currencyType?:''
						}else if(field.type == 'PlainTextHref'){
							Field.Link = settings.en.value?"http://${settings.en.value}":''
						}else if(field.type=='CheckBox'){
							def SubFields = []
							Field.Layout = settings.fieldLayout?:'oneCol'
							settings.en.value?.eachWithIndex{k,counter->
								def SubField = [:]
								SubField.Label = k
								SubField.Score = k
								SubFields << SubField
							}
							Field.OtherOption = settings.otherOption?1:0
							Field.SubFields = SubFields
						}else if(field.type=='GroupButton'){
							def Choices = []
							Field.Layout = settings.fieldLayout?:'oneCol'
							settings.en.value?.eachWithIndex{k,counter->
								def Choice = [:]
								Choice.Label = k
								Choice.Score = k
								Choices << Choice
							}
							Field.OtherOption = settings.otherOption?1:0
							Field.Choices = Choices
						}else if(field.type=='LikeDislikeButton'){
							def Choices = []
							Field.Voting = settings.likeAndVote?1:0
							def Choice = [:]
							Choice.Label = 'Like'
							Choice.Score = 'Like'
							Choices << Choice
							Choice = [:]
							Choice.Label = 'Dislike'
							Choice.Score = 'Dislike'
							Choices << Choice
							Field.Choices = Choices
						}else if(field.type=='ScaleRating'){
							Field.MoodRating = settings.moodRate
							def Choices = []
							['VerySatisfied':'Very Satisfied','Satisfied':'Satisfied','Neutral':'Neutral','Dissatisfied':'Dissatisfied']?.eachWithIndex{k,v,counter->
								def Choice = [:]
								Choice.Label = v
								Choice.Score = k
								Choices << Choice
							}
							Field.Choices = Choices
						}else if(field.type == 'ImageUpload'){
							String value = ""
							if(settings.en.uploadImage){
								def attachments = form.getAttachments(field.name)
								if(attachments && attachments[0]){
									value = "${ConfigurationHolder.config.grails.serverURL}/preview/formImagePath/${attachments[0]?.id}"
								}
							}else{
								value = settings.en.value
							}
							Field.URL = value
							Field.Width = settings.en.width
							Field.Height = settings.en.height
							Field.Clickable = settings.en.clickable?1:0
							def clickableURL = ""
							if(settings.en.clickable){
								clickableURL = settings.en.clickableURL
								if(clickableURL?.indexOf("http://")!=0 && clickableURL?.indexOf("https://")!=0){
									clickableURL = "http://"+clickableURL
								}
							}
							Field.ClickableURL = clickableURL
						}else if(field.type == 'LinkVideo'){
							Field.HTML = settings.en.embedHTML?:''
						}else if(field.type=='dropdown'){
							def Choices = []
							settings.en.value?.eachWithIndex{k,counter->
								def Choice = [:]
								Choice.Label = k
								Choice.Score = k
								Choices << Choice
							}
							Field.Choices = Choices
						}else if(field.type=='AddressField'){
							def SubFields = []
							['line1':'Address Line 1','line2':'Address Line 2','city':'City','state':'State','zip':'Zip','country':'Country'].each{k,v->
								def SubField = [:]
								SubField.Name = field.name+k
								SubField.Label = v
								SubFields << SubField
							}
							Field.SubFields = SubFields
						}else if(field.type=='NameTypeField'){
							def SubFields = []
							def SubField
							if(settings?.showPrefix){
								SubField = [:]
								SubField.Name = field.name+'pre'
								SubField.Label = 'Prefix'
								SubFields << SubField
							}
							SubField = [:]
							SubField.Name = field.name+'fn'
							SubField.Label = 'First Name'
							SubFields << SubField
							if(settings?.showMiddleName){
								SubField = [:]
								SubField.Name = field.name+'mn'
								SubField.Label = 'Middle Name'
								SubFields << SubField
							}
							SubField = [:]
							SubField.Name = field.name+'ln'
							SubField.Label = 'Last Name'
							SubFields << SubField
							Field.SubFields = SubFields
						}else if(field.type=='FormulaField'){
							def Elements = []
							settings.en.value?.eachWithIndex{k,counter->
								def Element = [:]
								Element.Index = counter
								Element.Value = k
								Elements << Element
							}
							Field.AllowUserEdit = settings.isEditable
							Field.Elements = Elements
						}else if(field.type=='LookUp'){
							Field.LookUpURL = createLink('action':'lookUp',params:[formId:form.id,field:field.id,apiKey:params.apiKey,respType:params.respType],absolute:true)
						}else if(field.type=='Likert'){
							Field.SwitchRowCol = settings.en.switchRowCol?1:0
							def SubFields = []
							settings.en.rows?.eachWithIndex{k,counter->
								def SubField = [:]
								SubField.Name = field.name+"__${counter}_"
								SubField.Label = k
								SubFields << SubField
							}
							Field.SubFields = SubFields
							def Choices = []
							settings.en.columns?.eachWithIndex{k,counter->
								def Choice = [:]
								Choice.Score = counter
								Choice.Label = k
								Choices << Choice
							}
							Field.Choices = Choices
						}else if(field.type=='SubForm'){
							Field.SubFormId = settings.subForm
							Field.ShowTotalForFields = []
							settings.numericSubField?.each{
								def ShowTotalForField = [:]
								ShowTotalForField.FieldName = it
								Field.ShowTotalForFields << ShowTotalForField
							}
						}
						Field.Page = pageCount
						res << Field
					}
				}
			}else{
				errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
			}
		}
		if(params.test){
			if(errorMessage){
				[errorMessage:errorMessage]
			}else{
				[res:res,nonPersistableFieldsList:nonPersistableFieldsList]
			}
		}else{
			if(xmlResponse){//For XML response
				if(errorMessage){
					out << errorMessageXML(errorMessage)
				}else{
					out << convertFieldsToXML(res)
				}
				responseContentType = 'text/xml'
				if(params.pretty){
					responseContentType = 'text/html'
					out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
				}
			}else if(jsonResponse){//For JSON response
				if(errorMessage){
					out = [ErrorMessage:errorMessage] as JSON
				}else{
					out = [Fields:res] as JSON
				}
				responseContentType = 'text/json'
				if(params.pretty){
					out = out.toString(true)
				}
			}
			render(status: 200, contentType: responseContentType, text: out)
		}
	}
	
	def entries = {
		def apiKey = params.apiKey
		def criteria = [max:25,offset:0]
		int totalCount=0
		def currentUser
		def errorMessage
		if(!apiKey){
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}else{
			currentUser = User.findByApiKey(apiKey)
			if(!currentUser){
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}else{
			 springSecurityService.reauthenticate(currentUser.username) 
			}
		}
		
		Form form = Form.read(params.formId)
		def out = new FastStringWriter()
		def res = []
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def pageCount = 1
		criteria['max'] = Math.min(params.pageSize ? params.pageSize.toInteger() : criteria['max'] , 100)
		criteria['offset'] = params.pageStart ? params.pageStart : criteria['offset']
		params.max=criteria['max']
		params.offset=criteria['offset']
		if(form && isFormAccessible(form,currentUser)){
			if(allowUserAPICall(currentUser)){
				if(params.entryId){
					def entry = sqlDomainClassService.get(params.entryId, form)
					if(entry){
						def attachments = form.getDomainAttachments(entry.id)
						getEntryPopulated(form,entry,res,attachments)
						totalCount=1
					}
				}else if(params.lastSync){
				    criteria.lastSyncDate=params.lastSync
					def listOfEntries = sqlDomainClassService.getUpdateList(form,false,criteria)
					listOfEntries.instanceList.each{entry->
						def attachments = form.getDomainAttachments(entry.id)
						getEntryPopulated(form,entry,res,attachments)
					}
					totalCount=listOfEntries.totalCount
				}else{
					def listOfEntries = sqlDomainClassService.list(form,false,criteria)
					listOfEntries.instanceList.each{entry->
						def attachments = form.getDomainAttachments(entry.id)
						getEntryPopulated(form,entry,res,attachments)
					}
					totalCount=listOfEntries.totalCount
				}
			}else{
				errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
			}
		}
		
		if(xmlResponse){//For XML response
			if(errorMessage){
				out << errorMessageXML(errorMessage)
			}else{
				out << convertEntriesToXML(res,params.max,params.offset,Math.min((params.offset+params.max),totalCount),totalCount)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Entries:res ,"Max":params.max ,"pageStart": params.offset, 
					 "pageEnds" :Math.min((params.offset+params.max),totalCount),"TotalCount" : totalCount] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	
	def getEntryPopulated(Form form,def entry,def res,def attachments){
		def Entry = [:]
		Entry.Id = entry.id
		Entry.version = entry.version?:0
		Entry.lastUpdated =  new SimpleDateFormat("MM/dd/yyyy HH:mm").format(entry."last_updated") 
		form.fieldsList.each{field->
			if(!nonPersistableFieldsList.contains(field.type)){
				try{
					def fieldSettings = JSON.parse(field.settings)
					if(field.type == 'CheckBox'){
						def values = JSON.parse(entry."${field.name}")
						def Values = []
						values.each{value->
							Values << value
						}
						Entry."${field.name}" = Values
					}else if(field.type == 'Likert'){
						def values = entry."${field.name}"?JSON.parse(entry."${field.name}"):[]
						fieldSettings.en.rows.eachWithIndex{r,idx->
							def answeredValue = ''
							values.eachWithIndex{value,vIdx->
								if(idx == vIdx)
									answeredValue = value
							}
							Entry."${field.name}__${idx}_" = answeredValue
						}
					}else if(field.type == 'AddressField'){
						def values = entry."${field.name}"?JSON.parse(entry."${field.name}"):[:]
						values.each{k,v->
							Entry."${field.name}${k}" = v
						}
					}else if(field.type == 'NameTypeField'){
						def values = entry."${field.name}"?JSON.parse(entry."${field.name}"):[:]
						values.each{k,v->
							Entry."${field.name}${k}" = v
						}
					}else if(field.type == 'FileUpload'){
						def Files = []
						attachments?.each{attachment->
							def File = [:]
							File.name = attachment.name
							File.DownloadLink = ConfigurationHolder.config.grails.serverURL+'/attachmentable/download/'+attachment.id
							//TODO add delete link if required
							Files << File
						}
						Entry."${field.name}" = [Files:Files]
					}else if(field.type == 'SingleLineDate'){
						try{
							if(entry."${field.name}")
						        Entry."${field.name}" = new SimpleDateFormat("MM/dd/yyyy HH:mm").format(entry."${field.name}")
						    else
						         Entry."${field.name}"=""
						}catch (Exception e) {
							Entry."${field.name}"=""
						}
					}else if(field.type == 'SubForm'){
						def subFormEntries = []
						if(fieldSettings.subForm){
							def subForm = Form.read(fieldSettings.subForm)
							if(subForm){
								entry."${field.name}"?.each{subFormEntry->
									def subFormAttachments = subForm.getDomainAttachments(subFormEntry.id)
									getEntryPopulated(subForm,subFormEntry,subFormEntries,subFormAttachments)
								}
							}
						}
						Entry."${field.name}" = [SubFormEntries:subFormEntries]
					}else{
						Entry."${field.name}" = entry."${field.name}"?:''
					}
				}catch(Exception exception){
					//TODO log error here about formId, fieldName, location and exception message
					Entry."${field.name}" = ""
				}
			}
		}
		res << Entry
	}
	
	def entrySave = {
		def apiKey = params.apiKey
		def currentUser
		def errorMessage
		if(!apiKey){
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}else{
			currentUser = User.findByApiKey(apiKey)
			if(!currentUser){
				// We are not going to allow any user who is not logged in or public user.
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}
		Form form = Form.read(params.formId)
		Form parentForm
		if(form.formCat == 'S'){
			if(params.parentFormId){
				parentForm = Form.read(params.parentFormId)
				if(parentForm){
					def subFormFields = parentForm?.fieldsList?.find{it.type == 'SubForm'}
					def subForm = subFormFields?.find{JSON.parse(it.settings)?.subForm == params.formId && it.name == params.parentFormFieldName}
					def parentFormEntry = subForm && params.parentFormEntryId?sqlDomainClassService.get(params.parentFormEntryId,parentForm):null
					if(!subForm && parentFormEntry){
						parentForm = null
					}
				}
			}
		}
		def out = new FastStringWriter()
		def res = []
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		if(form && isFormAccessible(parentForm?:form,currentUser,params.entryId?'update':'save')){
			if(allowUserAPICall(currentUser)){
				def  entry =[errors:[]]
				def fieldsList = form.fieldsList
				def isUpdate = false//true will tell we have to call update and false will tell save is to be called on sqlDomainClassService
				if(params.entryId){
					entry = sqlDomainClassService.get(params.entryId, form)
					if(entry){
						if (params.version) {
							def version = params.version.replaceAll(",","").toLong()
							if (entry.version > version) {
								entry.errors.add([name:"version", code:"default.optimistic.locking.failure", args:[form.toString()] as Object[], defaultMessage:"Another user has updated this data while you were editing"])
							}
						}else{
							params.version = entry?entry.version:null
						}
						    params.id = params.entryId
							isUpdate =  true 
					}else{
					 entry =[errors:[]]
					 entry.errors.add([name:"enrtyIdNotfound", code:"default.enrtyIdNotfound", args:[form.toString()] as Object[], defaultMessage:"Another user has deleted this data while you were editing. Please check!"])
					}
				}
				if(!entry.errors)     
				     entry = sqlDomainClassService.populate(params,form,entry,null)//last object is request object and used in populate() to get captch details. If null not checked
				if(entry){
					if(isUpdate){
						entry.updatedBy = currentUser
					}else{
						entry.createdBy = currentUser
					}
					fieldsList?.each { field ->
						def settings = grails.converters.JSON.parse(field.settings)
						def fieldName = settings."en".label
						//fields.put( field.name,field)
						if ( field.type == "PlainText" ) {
							def fieldVal = params[field.name]
							if ( fieldVal )
								entry."${fieldVal}"= "true"
						}else if ( field.type == "CheckBox" ) {
							def fieldVal = params.list(field.name)
							def v
							if ( fieldVal ){
								v = fieldVal as List
								entry."${field.name}"= (v as JSON).toString()
							}else
								entry."${field.name}"= null
						}else if(field.type == "FileUpload"){
	//						def keyValueMap = [:]
	//						keyValueMap."${field.name}" = settings
	//						fieldNameSetting_MapList.add(keyValueMap)
						}else if(field.type == "Paypal"){
	//						itemsBought = domainInstance."${field.name}_bought"
						}
					}
					
					def totalFileSize = 0
					boolean breakTheAction = false
							
					if (request instanceof DefaultMultipartHttpServletRequest) {
						request.multipartFiles.each {k, v ->
							if (k) {
								List<MultipartFile> files = new ArrayList()
								def fieldName = k.replace('_file','')
								if (v instanceof List) {
									v.each {MultipartFile file ->
										if(file && file.size>0){
											files << file
										}
									}
								} else {
									MultipartFile file = v
									if(file && file.size>0){
										files << file
									}
								}
								files.each{MultipartFile file->
									if(!breakTheAction){
										def fileSize = file.size/1024
										def field = fieldsList.find{it.name == fieldName}
										if(field){
											def settings = JSON.parse(field.settings)
											def maxSize = settings.maxSize
											if(settings.unit == "MB"){
												maxSize = settings.maxSize*1024
											}
											if(fileSize > maxSize){
												entry.errors.add([name:fieldName, code:"default.fileSize.exceed", args:[] as Object[], defaultMessage:"File size for property ${settings.en.label} can not be greater than ${settings.maxSize} ${settings.unit}"])
												breakTheAction = true
												return
											}
											totalFileSize += fileSize
										}else{
											entry.errors.add([name:"version", code:"default.form.changed", args:[] as Object[], defaultMessage:"Someone might have changed the form while you were entering data. Please try again."])
											breakTheAction = true
											return
										}
									}
								}
							}
						}
					}
					if(totalFileSize>0){
						Client myClient = Client.get(form.tenantId)
						def earlyDataLength = clientService.getTotalAttachmentSize( myClient.id)
						if( ( totalFileSize*1024 + earlyDataLength )/(1024*1024) > myClient.maxAttachmentSize){
							entry.errors.add([name:"version", code:"default.client.fileSize.exceed", args:[] as Object[], defaultMessage:"File size for your client exceeded. Please contact administrator"])
						}
					}
					
					if(!entry.errors && (isUpdate?sqlDomainClassService.update(entry,form):sqlDomainClassService.save(entry,form))){
						if(parentForm && !isUpdate){
							sqlDomainClassService.addSubFormInstance(entry.id,params.parentFormEntryId,params.parentFormFieldName,parentForm)
						}
						session['user'] = session['user']?:(currentUser)
						def resultAttachment = attachUploadedFilesTo(form,entry.id)
						res = [["${isUpdate?'Updated':'Saved'}":true,'EntryId':entry.id]]
					}else{
						def errors = []
						entry.errors?.each{error->
							def Error = [:]
							Error.FieldName = error.name
							def fieldLabel
							try{
								fieldLabel = fieldsList.find{it.name == error.name}?.toString()
							}catch(Exception e){}
							Error.ErrorMessage = message(code:error.code,args:[fieldLabel?:''],default:error.defaultMessage)
							errors << Error
						}
						res = [["${isUpdate?'Updated':'Saved'}":false,'EntryId':params.entryId?:'','Errors':errors]]
					}
				}
			}else{
				errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
			}
		}
		
		if(xmlResponse){//For XML response
			if(errorMessage){
				out << errorMessageXML(errorMessage)
			}else{
				out << convertEntryToXML(res && res.size()>0?res.get(0):[])
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Entry:res?.get(0)] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	
	def lookUp = {
		def apiKey = params.apiKey
		def currentUser = null
		def errorMessage
		if(apiKey){
			currentUser = User.findByApiKey(apiKey)
		}
		
		Form form = Form.read(params.formId)
		def out = new FastStringWriter()
		def res = []
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		try{
			res = FormUtils.getLookUpResults(params,currentUser,dataSource,dateFormatter)
		}catch(Exception e){}
		
		if(xmlResponse){//For XML response
			if(errorMessage){
				out << errorMessageXML(errorMessage)
			}else{
				out << convertLookUpResultsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Results:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def registerUser={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
			if(params.consumerKey){
				Client client=Client.findByConsumerKey(params.consumerKey)
				if(client){
					def successMessage=[:]
					boolean isValidNumber = true
					PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
					def phoneNumber = params.j_mobile
					try{
						if(phoneNumber){
							isValidNumber = false
							PhoneNumber usNumberProto = phoneUtil.parse(phoneNumber, "ZZ")
							isValidNumber = phoneUtil.isValidNumber(usNumberProto)
						}
					}catch(Exception ex){
					}
					if (params.username == null || params.username?.indexOf("@") == -1 || params.username?.length() < 5  ) {
							errorMessage = message(code:'invalid.email', args:[], default:'Enter a valid email address!') 
					}else if (params.password == null || params.password.length()<1){
							 errorMessage = message(code:'password.required',args:[],default:"Password is required!")
					  }else if (!isValidNumber){
							  errorMessage = message(code:'invalid.email',args:[],default:"Enter a valid mobile number!")
					  }else {
							  def userInstance = User.findByUsername(params.username)
							if(userInstance){
								errorMessage = message(code:'already.exits',args:[],default:"User already exists!")
							}else{
								userInstance = new User()
								userInstance.username = params.username
								userInstance.userTenantId =client.id // Add the user to the default client
								userInstance.enabled= true;
								userInstance.accountLocked= true;
								UUID uuid = UUID.randomUUID()
								userInstance.apiKey=uuid.toString()[4..22]
								userInstance.firstName = userInstance.username.substring(0,userInstance.username.indexOf("@"))
								userInstance.mobilePhone=phoneNumber
								userInstance.password = springSecurityService.encodePassword(params.password)
								userInstance.save(flush:true)
								UserRole ur = new UserRole(user: userInstance, role:Role.findByAuthority(Role.ROLE_USER))
								try{
									 ur.save(flush:true)
									 def userProfileInstance = new UserProfile()
									 userProfileInstance.emailSubscribed=false
									 userProfileInstance.user = userInstance
									 userProfileInstance.save(flush:true)
									 
								  // sending mail asynchronously now
									 UserVerification uv= new  UserVerification();
									 uv.id=UUID.randomUUID()?.toString()?.replaceAll("-", "")
									 uv.apiKey=userInstance.apiKey
									 uv.save();
									TenantUtils.doWithTenant(Integer.valueOf(client.id.intValue())) {
										  AsynchronousEmailStorage async = new AsynchronousEmailStorage()
										  async.emailFrom =  "${grailsApplication.config.grails.plugins.springsecurity.ui.register.emailFrom}"
										  async.emailTO = "${userInstance.username}"
										  async.emailSubject="Please complete your signup"
										  async.emailData="Hello ${userInstance.firstName},\n\n You're one step away from completing your signup.\n Follow the link below to confirm your email address:\n ${grailsApplication.config.grails.serverURL}/register/userEmailVerificationApiV1/${uv.id} \n\nThank you "
										  async.emailSentStatus = AsynchronousEmailStorage.NOT_ATTEMPT
										  async.save(flush:true)
									 }
								}catch ( Exception e ){
								e.printStackTrace()
								  /* if ( ur )
									  ur.delete()
								   if ( userInstance )
									  userInstance.delete()*/
							  }
							 successMessage."Body" = "Verification mail send to ${params.username}. "
							 res<<successMessage
							}
					  }
					}else{
					errorMessage = message(code:'client.not.found.consumerkey',args:[],default:"Client not found.")
					}
			}else{
			 errorMessage = message(code:'consumerkey.not.found',args:[],default:"Consumerkey not found") 
			}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertFeedsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Message:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
		}
	def addComment={
			def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
			def responseContentType = ""
			def out = new FastStringWriter()
			def res = []
			def errorMessage
			if(params.apiKey){
				def apiKey = params.apiKey
				def currentUser = User.findByApiKey(apiKey);
				if(currentUser){
					springSecurityService.reauthenticate(currentUser.username)
					if(allowUserAPICall(currentUser)){
						def activityFeedInstance = ActivityFeed.get(params.id);
						try{
							if (activityFeedInstance && params."commentBody") {
								String comment = params."commentBody"
								comment = utilService.convertURLToLink(comment.encodeAsHTML(),true)
								def commentsInstance = activityFeedInstance.addComment(currentUser,comment)
								activityFeedInstance.save(flush: true)
								res<<feeds(activityFeedInstance,currentUser)
							} else {
								errorMessage = message(code:'api.comment.not.found',args:[],default:'Comment Not added')
							}
						}catch (Exception ex){
						   errorMessage = message(code:'api.comment.not.found',args:[],default:'Comment Not added. Some error occurred.')
						}
					}else{
						errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
					}
				}else{
					errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
				}
			}else{
				errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
			}
			if(xmlResponse){//For XML response
				if(errorMessage){
					out = errorMessageXML(errorMessage)
				}else{
					out = convertFeedsToXML(res)
				}
				responseContentType = 'text/xml'
				if(params.pretty){
					responseContentType = 'text/html'
					out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
				}
			}else if(jsonResponse){//For JSON response
				if(errorMessage){
					out = [ErrorMessage:errorMessage] as JSON
				}else{
					out = [Feeds:res] as JSON
				}
				responseContentType = 'text/json'
				if(params.pretty){
					out = out.toString(true)
				}
			}
			render(status: 200, contentType: responseContentType, text: out)
		}
	def addFeed={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				boolean isTenantExpired=isUserTenantExpired(currentUser.userTenantId.toLong())
				if(currentUser.accountLocked ||  !currentUser.enabled || isTenantExpired){
					errorMessage = message(code:'api.user.login.inactive',args:[],default:(currentUser.accountLocked?'Account is locked':!currentUser.enabled?'Account is not'+
						' active':isTenantExpired?'Client account expired.For details please email administrator (admin@yourdomain.com)':''))
				return
				}
				springSecurityService.reauthenticate(currentUser.username)
				if(allowUserAPICall(currentUser)){
		        def source = params['source']
		        String companyFeed = params['postactivity']
		        companyFeed = companyFeed?.encodeAsHTML()
	        	def shareid = params['share']
		        def activityFeed = new ActivityFeed()
		        def activityFeedConfig
		        def uploadedFile = params['file']
		        def shareType 
				Form form
				if(params.formId)
				   form=Form.get(params.formId)
		        def className
				if(form)
					className=form.name+"."+form.name
	        	def sharedTo = [:]
	        	def dateCreated = new Date()
					try{
						if(params.usernames){
							sharedTo."users" =new ArrayList<String>(Arrays.asList(params.usernames.split(",")));
							}
						if(params.groups){
							sharedTo."groups" =new ArrayList<String>(Arrays.asList(params.groups.split(",")));
							}
						if ( className ){
						  activityFeedConfig =  ActivityFeedConfig.findByClassName(className)
						  FormAdmin fa=FormAdmin.findByForm(form)
						  shareType=fa?.formType
						  if(!activityFeedConfig){
							   activityFeedConfig = new ActivityFeedConfig(createdBy: currentUser, shareType: shareType, configName: className,className:className, dateCreated: dateCreated, lastUpdated: dateCreated)
							  activityFeedConfig.save()
						  }
						}else{
							activityFeedConfig =  ActivityFeedConfig.findByConfigName("content")
							if ( !activityFeedConfig ) {
								activityFeedConfig = new ActivityFeedConfig(configName:"content", createdBy: springSecurityService.currentUser)
								activityFeedConfig.save(flush:true)
							}
						}
					   if(uploadedFile && uploadedFile.size > 0)
					   {
						   def uploadFileSize = (uploadedFile.size/1024)
						   if(uploadFileSize > 10240)
							   throw new Exception("File size cannot be more then 10MB")
						   Client myClient = Client.get(currentUser.userTenantId)
						   if ( (uploadedFile?.size + clientService.getTotalAttachmentSize(myClient.id))/(1024*1024) > myClient.maxAttachmentSize )
							   throw new Exception("Sorry, can't upload attachment. You have reached your attachment file size limit. Please contact your system administrator")
							   
					   }
					   if(shareid){
						   activityFeed.shareId = Long.parseLong(shareid) // object instance shared
					   }
					   
					   activityFeed.config = activityFeedConfig
					   // Check for visibility
					   sharedTo?.users?.each{userName->
							   activityFeed.addToSharedUsers( com.oneapp.cloud.core.User.findByUsername(userName))
						   } 
					   sharedTo?.groups?.each{group->
						   activityFeed.addToSharedGroups( com.oneapp.cloud.core.GroupDetails.get(group))
					   }
					   /*else if ( share.visibility != null && ActivityFeed.ROLE.equalsIgnoreCase(share.visibility) )
						   activityFeed.addToSharedRoles( com.oneapp.cloud.core.Role.get(share.sharedWith))
					   else if ( share.visibility != null && ActivityFeed.DEPARTMENT.equalsIgnoreCase(share.visibility) )
						   activityFeed.addToSharedDepts( com.oneapp.cloud.core.DropDown.get(share.sharedWith))
					   else if ( share.visibility != null && ActivityFeed.COMPANY.equalsIgnoreCase(share.visibility) )
						    activityFeed.visibility = ActivityFeed.COMPANY*/
					   //}
					   //For entries in ActivityFeed
					   
					   
					   activityFeed.feedState = ActivityFeed.ACTIVE
						   
					   if (activityFeed?.config?.shareType == "TASK"){
						   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/${activityFeed.config.shareType.toLowerCase()}/edit/${activityFeed.shareId}"
						   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
					   }else if (activityFeed?.config?.shareType == "Approval"){
						   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/ddc/edit?id=${activityFeed.shareId}&dc=${activityFeed.config.configName}"
						   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
					   }else if (activityFeed?.config?.shareType == "Survey"){
						   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/ddc/edit?id=${activityFeed.shareId}&dc=${activityFeed.config.configName}"
						   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
					   }else if (activityFeed?.config?.shareType == "Poll"){
						   def mess = "Click here to view ${activityFeed.config.shareType.toLowerCase()} - ${grailsApplication.config.grails.serverURL}/ddc/edit?id=${activityFeed.shareId}&dc=${activityFeed.config.configName}"
						   activityFeed.activityContent = utilService.convertURLToLink(mess+ "<br/>"+companyFeed,false)
					   }else
						   activityFeed.activityContent = utilService.convertURLToLink(companyFeed,true)
					   
						
							activityFeed.createdBy = currentUser
						   activityFeed.dateCreated = dateCreated
						   activityFeed.lastUpdated = dateCreated
						   if ( activityFeed.config == null )
							   activityFeed.config = ActivityFeedConfig.get(1)
							if (!activityFeed.hasErrors() && activityFeed.save()){
								res<<feeds(activityFeed, currentUser)
								 def infoMessage = ""
								 if(activityFeed.config.configName == "content"){
									 infoMessage = ""+(activityFeed.createdBy.firstName?:activityFeed.createdBy.username)+" shared a feed with you"
								 }else{
									 def feedClassName = activityFeed.config.className
									 feedClassName= feedClassName.substring(0,feedClassName.indexOf("."))
									 ///def form  = Form.findByName(feedClassName)
									 def formName = JSON.parse(form.settings)."en".name
									 if(!formName)
										 formName = form.name
									 infoMessage = "Form entry of "+formName+" shared with you"
								 }
								 def activityNotification = new ActivityNotification(actionBy:springSecurityService.currentUser,userFeedState:infoMessage,actionOnFeed:activityFeed, actionTime:activityFeed.dateCreated)
								 activityNotification.save(flush:true)
							}else{
								errorMessage = message(code:'api.ActivityFeed.multiFeedShare.exceeded',args:[],default:'Error occured while creating feed')
							}
							if(uploadedFile && uploadedFile.size > 0)
								attachUploadedFilesTo(activityFeed)
					   // Add tag based on the share type
					   if (activityFeed?.config?.shareType){
						 def formObj = Form.read(params.formId?.toLong())
						def fieldInstance = JSON.parse(formObj?.settings)
						   activityFeed.addTag(fieldInstance.en.name.toLowerCase())
					   }
					   if(params.filtertag){
						   activityFeed.addTag((params.filtertag).toLowerCase())
						  }
					   def infoMessage = ""
					   if(activityFeed.config.configName == "content"){
						   infoMessage = ""+(activityFeed.createdBy.firstName?:activityFeed.createdBy.username)+" shared a feed with you"
					   }else{
						   def feedClassName = activityFeed.config.className
						   feedClassName= feedClassName.substring(0,feedClassName.indexOf("."))
						//   def form  = Form.findByName(className)
						   def formName = JSON.parse(form.settings)."en".name
						   if(!formName)
							   formName = form.name
						   infoMessage = "Form entry of "+formName+" shared with you"
					   }
					   
					}catch(Exception ex){
					ex.printStackTrace()
						errorMessage = message(code:'api.ActivityFeed.multiFeedShare.exceeded',args:[],default:'ActivityFeedController-multiFeedShare')
					}
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertMgsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Feeds:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def deleteGroup={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		def successMessage=[:]
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				if(allowUserAPICall(currentUser)){
					springSecurityService.reauthenticate(currentUser.username) 
					def groupsInstance = GroupDetails.get(params.id)
					if (groupsInstance && groupsInstance.createdBy==currentUser) {
						try {
							List<ActivityFeed> afList = ActivityFeed.createCriteria().list(){
								sharedGroups{
									eq 'id',groupsInstance.id
								}
							}
							afList.each{af->
								def afGroups = af.sharedGroups
								af.sharedGroups = []
								afGroups.remove(groupsInstance)
								af.sharedGroups = afGroups
							}
							groupsInstance.delete()
							successMessage."Body" = "Successfully delete"
							res<<successMessage
						}
						catch (org.springframework.dao.DataIntegrityViolationException e) {
								errorMessage = message(code:'api.call.error.delete',args:[],default:'Error in delete')
							}
						}else{
							errorMessage = message(code:'api.delete.permission',args:[],default:'User do not have permmissions')
						}
					
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertMgsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Message:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def addGroup={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				boolean isTenantExpired=isUserTenantExpired(currentUser.userTenantId.toLong())
				if(currentUser.accountLocked ||  !currentUser.enabled || isTenantExpired){
					errorMessage = message(code:'api.user.login.inactive',args:[],default:(currentUser.accountLocked?'Account is locked':!currentUser.enabled?'Account is not'+
						' active':isTenantExpired?'Client account expired .For details please email administrator (admin@yourdomain.com)':''))
				}
				springSecurityService.reauthenticate(currentUser.username)
				if(allowUserAPICall(currentUser)){ 
						GroupDetails groupsInstance
						if(params.id){
						   groupsInstance= GroupDetails.get(params.id)
                           groupsInstance.properties = params
						}else
					      groupsInstance = new GroupDetails(params)
						groupsInstance.createdBy = springSecurityService.currentUser
						groupsInstance.groupType=com.oneapp.cloud.core.DropDown.findByType(com.oneapp.cloud.core.DropDownTypes.GROUP_TYPE)
						if (!groupsInstance.hasErrors() && groupsInstance.save()) {
							groupsInstance?.user=[]
							def userData =new ArrayList<String>(Arrays.asList(params.usernames.split(",")));
								
							userData.each{
								User gpuserInstance = User.findByUsername(it)
								if(gpuserInstance.userTenantId==currentUser.userTenantId)
								   groupsInstance.addToUser(gpuserInstance)
							}
							groupsInstance.save()
							res<<groups(groupsInstance, currentUser)
						}
						else {
							 errorMessage= errorlists(groupsInstance.errors)
						}
					}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertGroupToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Groups:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	
		
		
		
		}
	def getUserGroups={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				if(allowUserAPICall(currentUser)){ 
					 springSecurityService.reauthenticate(currentUser.username)
					 def groupsInstanceList =  GroupDetails.findAllByCreatedBy(currentUser)
					 groupsInstanceList.each {GroupDetails gd->
						 res<<groups(gd, currentUser)
					 }
				}else{
				errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
			    }
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertGroupToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Groups:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def getUserFeeds={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		int totalCount=0
		def errorMessage
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){         
				boolean isTenantExpired=isUserTenantExpired(currentUser.userTenantId.toLong())
				if(currentUser.accountLocked ||  !currentUser.enabled || isTenantExpired){
					errorMessage = message(code:'api.user.login.inactive',args:[],default:(currentUser.accountLocked?'Account is locked':!currentUser.enabled?'Account is not'+
						' active':isTenantExpired?'Client account expired.For details please email administrator (admin@yourdomain.com)':''))
				}    
				if(allowUserAPICall(currentUser)){
					springSecurityService.reauthenticate(currentUser.username)  
					def currentUserRoles = currentUser.authorities*.id.get(0)
					def activityIdList = []
					if(params.createdByMe){
						def temp=ActivityFeed.findAllByCreatedBy(currentUser)
						activityIdList=temp*.id
				    }else{
					    activityIdList=ActivityFeed.withCriteria(){
						   or{
							eq "visibility",ActivityFeed.COMPANY
							sharedGroups{
								user{
									eq "id",currentUser.id
								}
							}
							sharedUsers{
								eq "id",currentUser.id
							}
							sharedRoles{
								eq "id",currentUserRoles
							}
							if(currentUser.department){
								sharedDepts{
									eq "id",currentUser.department.id
								}
							}
							createdBy{
								eq "id",currentUser.id
							}
						}
						projections{
							distinct("id")
							order("lastUpdated","desc")
						}
					 }
				  }
					def activityFeedList
					if(activityIdList){
						totalCount=activityIdList.size()
						  params.max =params.pageSize
						  params.offset = params.offset
							  // above is for new values
						  params.max = Math.min(params.max ? params.max.toInteger() : 25 , 100)
						  if (params.offset==0 || params.offset == null )
							  params.offset = 0
						  else
							  params.offset = Integer.parseInt(params.offset)
							  
					       def maxSize = (params.offset+params.max.toInteger())>activityIdList.size() ? activityIdList.size() : (params.offset+params.max.toInteger());
					//					activityFeedList = activityFeedList?.sort{it.lastUpdated}.reverse()
						   params.offset = (maxSize>params.offset)?params.offset:0
						   activityIdList = activityIdList.subList (params.offset,maxSize)
						   activityFeedList=ActivityFeed.createCriteria().list(){
								"in" ("id",activityIdList)
								order("lastUpdated","desc")
						}
						    
					} 
					if(params.filtertag){
						def tag = Tag.findByName((params.filtertag).toLowerCase())
							if(tag){
							   def tagRef = TagLink?.findAllByTag(tag)?.tagRef
								activityFeedList = ActivityFeed.findAllByIdInList(tagRef)
							 }else{
								activityFeedList=[]
							}
					 }
				   activityFeedList?.each{af->
						res<<feeds(af,currentUser)
					   }
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertFeedsToXML(res,params.max,params.offset,Math.min((params.offset+params.max),totalCount),totalCount)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Feeds:res,"Max":params.max ,"pageStart": params.offset, 
					 "pageEnds" :Math.min((params.offset+params.max),totalCount),"TotalCount" : totalCount] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	
	def addRating={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				if(allowUserAPICall(currentUser)){
					springSecurityService.reauthenticate(currentUser.username)
					def activityFeedInstance = ActivityFeed.get(params.id)
					Double rating=-1
					try{
						if ( params.rating )
						 {
							 if ( params.rating == "0" )
							  rating = new Double(0)
							 else if ( params.rating  == "1" )
							  rating = new Double(1)
							 else if ( params.rating == "2" )
							  rating = new Double(2)
							 else if ( params.rating == "3" )
							  rating = new Double(3)
						 }
					
					}catch(Exception e){
						log.error "apiV1-addRating:"+e
					}
					if (rating != -1){
						activityFeedInstance.rate(currentUser,rating)
						activityFeedInstance.save(flush :true)
						res<<feeds(activityFeedInstance, currentUser)
					}else{
					errorMessage = message(code:'api.call.rating.exceeded',args:[],default:'Rating not updated')
					}
					
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertFeedsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Feeds:res] as JSON 
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	
		
	}
	def deleteAttachment={
	
	
    } 
	def getComments={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		int totalCount=0
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				boolean isTenantExpired=isUserTenantExpired(currentUser.userTenantId.toLong())
				if(currentUser.accountLocked ||  !currentUser.enabled || isTenantExpired){
					errorMessage = message(code:'api.user.login.inactive',args:[],default:(currentUser.accountLocked?'Account is locked':!currentUser.enabled?'Account is not'+
						' active':isTenantExpired?'Client account expired.For details please email administrator (admin@yourdomain.com)':''))
				}
				if(allowUserAPICall(currentUser)){
					springSecurityService.reauthenticate(currentUser.username)
					def activityFeedInstance = ActivityFeed.get(params.id)
					def commentList=activityFeedInstance?.comments?.sort{a,b-> b.dateCreated <=>a.dateCreated } 
					totalCount=commentList.size()
					if(commentList){
						  params.max = Math.min(params.pageSize ? params.pageSize.toInteger() : 25 , 100)
						  if ( params.offset == null )
							 params.offset = params.pageStart ? params.pageStart.toInteger() : 0
						  else
							params.offset = Integer.parseInt(params.offset)
						  def maxSize = (params.offset+params.max.toInteger())>commentList.size() ? commentList.size() : (params.offset+params.max.toInteger());
						  params.offset = (maxSize>params.offset)?params.offset:0
						  commentList = commentList.subList (params.offset,maxSize)
						} 
					def prettytime = new org.grails.prettytime.PrettyTimeTagLib()
					commentList?.each {  
						def comment=[:]
						comment.CommentId=it.id
						comment.CommentBody=it.body
						comment.DateCreated=it.dateCreated
						comment.DateCreatedPrettyFormat=prettytime.prettyDateMethode(it.dateCreated)
						comment.PostedBy=[getDataPopulatedForUser(User.read(it.posterId.toLong()),currentUser)]
						comment.CanDelete=currentUser?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| currentUser?.authorities?.authority.contains(Role.ROLE_ADMIN)|| ((currentUser?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)|| currentUser?.authorities?.authority.contains(Role.ROLE_USER)||currentUser?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&& (currentUser?.username.equalsIgnoreCase(af.createdBy.username)||currentUser?.username.equalsIgnoreCase(User.read(it.posterId.toLong())?.username)))
						res<<comment
					} 
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertCommentsToXML(res,params.max,params.offset,Math.min((params.offset+params.max),totalCount),totalCount)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Comments:res,"Max":params.max ,"pageStart": params.offset, 
					 "pageEnds" :Math.min((params.offset+params.max),totalCount),"TotalCount" : totalCount] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
		}
	def reShareFeed={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		def successMessage=[:]
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				if(allowUserAPICall(currentUser)){
					springSecurityService.reauthenticate(currentUser.username)
					def activityFeedInstance = ActivityFeed.get(params.id)
					if (activityFeedInstance) {
						if(currentUser?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| currentUser?.authorities?.authority.contains(Role.ROLE_ADMIN)||
							((currentUser?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)||  currentUser?.authorities?.authority.contains(Role.ROLE_USER)||
								currentUser?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&&currentUser?.username.equalsIgnoreCase(activityFeedInstance.createdBy.username))){
							try{
//								activityFeedInstance.delete()
								def sharedTo=[:]
								if(params.usernames){
									sharedTo."users" =new ArrayList<String>(Arrays.asList(params.usernames.split(",")));
									}
								activityFeedInstance.sharedUsers=[]
								sharedTo?.users?.each{userName->
									activityFeedInstance.addToSharedUsers( com.oneapp.cloud.core.User.findByUsername(userName))
								}
								activityFeedInstance.lastUpdated = new Date()
								activityFeedInstance.save(flush:true)
								successMessage."Body" = "Successfully shared"
								res<<successMessage
							}catch(Exception e){
								errorMessage = message(code:'api.call.error.delete',args:[],default:'Error in delete')
							}
						}else{
							errorMessage = message(code:'api.delete.permission',args:[],default:'User do not have permmissions')
						}
					}else{
					  errorMessage = message(code:'api.call.error.delete',args:[],default:'Error in delete')
					}
					
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertMgsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Message:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
	def deleteFeed={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		def successMessage=[:]
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				if(allowUserAPICall(currentUser)){
					springSecurityService.reauthenticate(currentUser.username)
					def activityFeedInstance = ActivityFeed.get(params.id)
					if (activityFeedInstance) {
						if(currentUser?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| currentUser?.authorities?.authority.contains(Role.ROLE_ADMIN)||
							((currentUser?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)||  currentUser?.authorities?.authority.contains(Role.ROLE_USER)||
								currentUser?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&&currentUser?.username.equalsIgnoreCase(activityFeedInstance.createdBy.username))){
							try{
								activityFeedInstance.delete()
								successMessage."Body" = "Successfully delete"
								res<<successMessage
							}catch(Exception e){
								errorMessage = message(code:'api.call.error.delete',args:[],default:'Error in delete')
							}
						}else{
							errorMessage = message(code:'api.delete.permission',args:[],default:'User do not have permmissions')
						}
					}else{
					  errorMessage = message(code:'api.call.error.delete',args:[],default:'Error in delete')
					}
					
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertMgsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Message:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	}
    def deleteComment={
		def xmlResponse = getResponseType(params.respType),jsonResponse = !xmlResponse
		def responseContentType = ""
		def out = new FastStringWriter()
		def res = []
		def errorMessage
		def successMessage=[:]
		if(params.apiKey){
			def apiKey = params.apiKey
			def currentUser = User.findByApiKey(apiKey);
			if(currentUser){
				if(allowUserAPICall(currentUser)){
					springSecurityService.reauthenticate(currentUser.username)
					def activityFeedInstance = ActivityFeed.get(params.id)
					if (activityFeedInstance) {
						if(currentUser?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| currentUser?.authorities?.authority.contains(Role.ROLE_ADMIN)||
							((currentUser?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)||  currentUser?.authorities?.authority.contains(Role.ROLE_USER)||
								currentUser?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&&currentUser?.username.equalsIgnoreCase(activityFeedInstance.createdBy.username))){
							Long commentId=-1
							 try{
				                commentId = Long.parseLong(params.commentId)
								activityFeedInstance.removeComment(commentId);
								activityFeedInstance.save(flush: true)
								successMessage."Body" = "Successfully delete"
								res<<successMessage
							}catch(Exception e){
								errorMessage = message(code:'api.call.error.delete',args:[],default:'Error in delete')
							}
						}else{
							errorMessage = message(code:'api.delete.permission',args:[],default:'User do not have permmissions')
						}
					}else{
					  errorMessage = message(code:'api.call.error.delete',args:[],default:'Error in delete')
					}
					
				}else{
					errorMessage = message(code:'api.call.limit.exceeded',args:[],default:'API call limit exceeded')
				}
			}else{
				errorMessage = message(code:'api.user.not.found',args:[],default:'User not found with the given API-Key')
			}
		}else{
			errorMessage = message(code:'api.no.api.key',args:[],default:'API-Key is required for this access')
		}
		if(xmlResponse){//For XML response
			if(errorMessage){
				out = errorMessageXML(errorMessage)
			}else{
				out = convertMgsToXML(res)
			}
			responseContentType = 'text/xml'
			if(params.pretty){
				responseContentType = 'text/html'
				out = """<?xml version="1.0" encoding="UTF-8"?><b>You are viewing your XML output in "pretty mode."</b><hr />Please note: Because pretty mode has a significant processing overhead, this markup has a intentionally unclosed and unescaped ">" character, which is intended to prevent the use of the pretty=true parameter in xml parsers. Be sure to remove this parameter in production or you parser will throw an error.<hr /><pre>${out.toString().encodeAsHTML()}</pre>"""
			}
		}else if(jsonResponse){//For JSON response
			if(errorMessage){
				out = [ErrorMessage:errorMessage] as JSON
			}else{
				out = [Message:res] as JSON
			}
			responseContentType = 'text/json'
			if(params.pretty){
				out = out.toString(true)
			}
		}
		render(status: 200, contentType: responseContentType, text: out)
	
	}

	def getResponseType(respType){
		if(respType && respType=='xml'){
			return true
		}else{
			return false
		}
	}
	
	def allowUserAPICall(def currentUser){
		def allow = false
		if(currentUser?.id){
			def now = new Date()
			def nowStrWithoutTime = dateFormatterWithoutTime.format(now)
			def today = dateFormatterWithoutTime.parse(nowStrWithoutTime)
			def apiLog = ApiLog.findByUserIdAndLogDate(currentUser.id,today)
			if(apiLog){
				if(apiLog.callCount < apiCallLimit){
					allow = true
					apiLog.callCount++
					apiLog.save(flush:true)
				}
			}else{
				allow = true
				new ApiLog(userId:currentUser.id,logDate:today,callCount:1).save(flush:true)
			}
		}
		return allow
	}
	
	//TODO put these converter methods in com.oneappcloud.api.util.Converter.groovy 
	//These are kept here 
	def convertUsersToXML(def listOfUsers,int max=1,int pageStart=0,int pageEnds=1,int totalCount=1){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Users{
			builder."Max" "${max}"
			builder."pageStart" "${pageStart}"
			builder."pageEnds" "${pageEnds}"
			builder."TotalCount" "${totalCount}"
			listOfUsers.each{f->
				builder.User{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
	}
	def convertFeedsToXML(listOfFeeds,int max=1,int pageStart=0,int pageEnds=1,int totalCount=1){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Feeds{
			builder."Max" "${max}"
			builder."pageStart" "${pageStart}"
			builder."pageEnds" "${pageEnds}"
			builder."TotalCount" "${totalCount}"
			listOfFeeds.each{f->
				builder.Feed{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
		}
	def convertCommentsToXML(listOfComments,int max=1,int pageStart=0,int pageEnds=1,int totalCount=1){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Comments{
			builder."Max" "${max}"
			builder."pageStart" "${pageStart}"
			builder."pageEnds" "${pageEnds}"
			builder."TotalCount" "${totalCount}"
			listOfComments.each{f->
				builder.Comment{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
	 }
	def convertGroupToXML(listOfGroups){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Groups{
			listOfGroups.each{f->
				builder.Group{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
		}
	def convertMgsToXML(mgs){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		mgs.each{f->
			builder.Message{
				f.each(){ key, value ->
						"${key}""${value}"
				}
			}
		}
		return s_xml
	}
	def convertLookUpResultsToXML(def listOfResults){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Results{
			listOfResults.each{f->
				builder.Result{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else if(value instanceof Map){
							builder."${key}"{
								value.each{k,v->
									"${k}""${v}"
								}
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
	}

	def convertEntriesToXML(def listOfEntries,int max=1,int pageStart=0,int pageEnds=1,int totalCount=1){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Entries{
			builder."Max" "${max}"
			builder."pageStart" "${pageStart}"
			builder."pageEnds" "${pageEnds}"
			builder."TotalCount" "${totalCount}"
			listOfEntries.each{f->
				builder.Entry{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else if(value instanceof Map){
							builder."${key}"{
								value.each{k,v->
									if(v instanceof java.util.List){
										builder."${k}"{
											subFieldsXML(v,builder,k.substring(0,k.length()-1))
										}
									}else{
										"${key}""${value}"
									}
								}
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
	}
	
	def convertEntryToXML(def entry){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Entry{
			entry?.each(){ key, value ->
				if(value instanceof java.util.List){
					builder."${key}"{
						subFieldsXML(value,builder,key.substring(0,key.length()-1))
					}
				}else{
					"${key}""${value}"
				}
			}
		}
		return s_xml
	}
	
	def convertFormsToXML(def listOfForms){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Forms{
			listOfForms.each{f->
				builder.Form{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
	}

	def convertFieldsToXML(def listOfFields){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Fields{
			listOfFields.each{f->
				builder.Field{
					f.each(){ key, value ->
						if(value instanceof java.util.List){
							builder."${key}"{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
							}
						}else{
							"${key}""${value}"
						}
					}
				}
			}
		}
		return s_xml
	}
	
	def subFieldsXML(def subFieldsList, def builder, def keyStr){
		subFieldsList?.each{f->
			if(f instanceof Map){
				builder?."${keyStr}"{
					f.each(){ key, value ->
						if(value instanceof java.util.List || value instanceof Object[]){
							builder?."${key}"{
								 try{
								subFieldsXML(value,builder,key.substring(0,key.length()-1))
								 }catch (Exception e) {
								 "${key}""${value}"
								}
							}
						}else{
							"${key}""${value}"  
						}
					}
				}
			}else if(f instanceof List){
				f.each(){ element ->
					"${keyStr}""${element}"
				}
			}else{
				println "-->> Check which field is this"
				//"${f?:''}"
			}
		}
	}
	
	def errorMessageXML(errorMessage){
		def s_xml=new StringWriter()
		def builder = new groovy.xml.MarkupBuilder(s_xml)
		builder.Error{
			"ErrorMessage""${errorMessage}"
		}
		return s_xml
	}
	
	def getAccessibleFormList(def user){
		
	}
	
	def isFormAccessible(def thisForm, User currentUser,def action = null){
		def isAccessible = false
		if(currentUser){
			def currentUserAuthorities = currentUser.authorities?.authority
			def isAdmin = currentUserAuthorities?.contains(Role.ROLE_ADMIN) || currentUserAuthorities?.contains(Role.ROLE_HR_MANAGER) || currentUserAuthorities?.contains(Role.ROLE_SUPER_ADMIN)?true:false
			def isTrialUser = currentUserAuthorities?.contains(Role.ROLE_TRIAL_USER)?true:false
			def isUser = currentUserAuthorities?(currentUserAuthorities.contains(Role.ROLE_USER)?true:false):true
			def formAdminList = FormAdmin.createCriteria().list(){
				if(isUser){
					or{
						ne 'formLogin','Login'
						publishedWith{
							eq 'id',currentUser.id
						}
					}
					form{
						eq 'id',thisForm.id
						eq 'tenantId',currentUser.userTenantId
					}
				}else if(isTrialUser){
					form{
						eq 'id',thisForm.id
						createdBy{
							eq 'id',currentUser.id
						}
						eq 'tenantId',currentUser.userTenantId
					}
				}else if(isAdmin){
					form{
						eq 'id',thisForm.id
						eq 'tenantId',currentUser.userTenantId
					}
				}
			}
			if(action == 'update'){//check for open for editing
				if(formAdminList){
					isAccessible = formAdminList.get(0)?.openForEdit
				}
			}else{
				isAccessible = formAdminList?true:false
			}
		}
		return isAccessible;
	}
	private errorlists(def errorObj){
		def listerror=[]
		try{
			def err= errorObj as JSON
			def tempErr = err.toString()
			err = JSON.parse(tempErr)
			err.errors.each{error->
				listerror << error.message
			}
		}catch (Exception e) {
	   }
	   return listerror
   }
	def groups(GroupDetails gd,User currentUser){
		def GroupDetailsMap=[:]
		 GroupDetailsMap."Id"=gd.id
		 GroupDetailsMap.Name= gd.groupName
		 GroupDetailsMap.Description= gd.groupDescription
		 GroupDetailsMap.Users=[]
		  gd?.user.each{u->
			 if(u.username != 'publicuser@yourdomain.com')
				GroupDetailsMap.Users << getDataPopulatedForUser(u,currentUser)
		 }
		 GroupDetailsMap
		}
	def feeds(ActivityFeed af,User currentUser ){
		def Feed=[:]
		Feed."Id"=af.id
		Feed."ActivityContent"=af.activityContent
		Feed."CreatedBy"=[getDataPopulatedForUser(af.createdBy,currentUser)]
		Feed."DateCreated"=af."dateCreated"
		Feed."LastUpdated"=af.lastUpdated
		Feed."FeedState"=af."feedState"
		Feed."IsTask"=af.isTask
		Feed."DueDate"=af.dueDate
		Feed."ReferenceId"=af.referenceId
		Feed."ShareId"=af.shareId
		Feed."FormContents"=[]
		if(af.shareId && af?.config?.shareType != 'Email'){
			def className=af?.config?.className
			Form form  = Form.findByName(className.substring(0,className.indexOf(".")))
			def entry = sqlDomainClassService.get(af.shareId, form)
			if(entry){
				def attachments = form.getDomainAttachments(entry.id)
				getEntryPopulated(form,entry,Feed."FormContents",attachments)
			}
		}  
		Feed."SharedWithGroups"=[]
		af.sharedGroups.each{ shareGroup ->
			Feed."SharedWithGroups"<<groups( shareGroup,currentUser)
		}
		Feed."SharedWithUsers"=[]
		af.sharedUsers.each{ sharedUsers->
			Feed."SharedWithUsers"<<getDataPopulatedForUser(sharedUsers, currentUser)
		}
		Feed."SharedWithDepartments"=[]
		af.sharedDepts.each{ sharedDepts  ->
			 Feed."SharedWithDepartments"<<[Id:sharedDepts.id,Name:sharedDepts.name]
		}
		Feed."SharedWithRoles"=[]
		af.sharedRoles.each{ sharedRoles ->
			Feed."SharedWithRoles"<<[Id:sharedRoles.id,Name: sharedRoles.description]
		} 
		Feed."taskStatus"=af."taskStatus"
		Feed."Visibility"=af."visibility"
		Feed."Ratings" =[]
		af.ratings.each{
			def rate=[:]
			rate.star=it.stars
			rate.users=[getDataPopulatedForUser(User.read(it.raterId.toLong()),currentUser)] 
			Feed."Ratings"<<rate
		}
		Feed."CanDelete"=currentUser?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| currentUser?.authorities?.authority.contains(Role.ROLE_ADMIN)|| ((currentUser?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)|| currentUser?.authorities?.authority.contains(Role.ROLE_USER)||currentUser?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&&currentUser?.username.equalsIgnoreCase(af.createdBy.username))
		def commentsList=[]
		int ci=0
		def prettytime = new org.grails.prettytime.PrettyTimeTagLib()
		 af?.comments?.sort{a,b-> b.dateCreated <=>a.dateCreated }.each {
			 if(ci==5)
			     return
			 def comment=[:]
				comment.CommentId=it.id
				comment.CommentBody=it.body
				comment.DateCreated=it.dateCreated
				comment.DateCreatedPrettyFormat=prettytime.prettyDateMethode(it.dateCreated)
				comment.PostedBy=[getDataPopulatedForUser(User.read(it.posterId.toLong()),currentUser)]
				comment.CanDelete=currentUser?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| currentUser?.authorities?.authority.contains(Role.ROLE_ADMIN)|| ((currentUser?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)|| currentUser?.authorities?.authority.contains(Role.ROLE_USER)||currentUser?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&& (currentUser?.username.equalsIgnoreCase(af.createdBy.username)||currentUser?.username.equalsIgnoreCase(User.read(it.posterId.toLong())?.username)))
				commentsList<<comment
				ci++;
			 }
		 def attachmentList=[]
		 af?.attachments.sort{if(grailsApplication.config.attachment.image.ext.contains(it.ext?.toLowerCase())){return -1}else{return 0}}.each {
			 def attachment=[:]
			 attachment.URL="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${it.id}?y=${currentUser.apiKey}"
			 attachment.CanDelete=currentUser?.authorities?.authority.contains(Role.ROLE_SUPER_ADMIN)|| currentUser?.authorities?.authority.contains(Role.ROLE_ADMIN)|| ((currentUser?.authorities?.authority.contains(Role.ROLE_HR_MANAGER)|| currentUser?.authorities?.authority.contains(Role.ROLE_USER)||currentUser?.authorities?.authority.contains(Role.ROLE_TRIAL_USER))&& (currentUser?.username.equalsIgnoreCase(af.createdBy.username)||currentUser?.username.equalsIgnoreCase(User.read(it.poster.id.toLong())?.username)))
			 /*attachment.DeleteLink=""
			 if(attachment?.CanDelete)
				 attachment.DeleteLink="${grailsApplication.config.grails.serverURL}/attachmentable/delete/${it.id}"*/
			 attachmentList<<attachment
			}
		 Feed."Comments"=commentsList
		 Feed."Attachment"=attachmentList
		return Feed
	}
def acentsApp={
    def xmlResponse = getResponseType(params.respType), jsonResponse = !xmlResponse
    def responseContentType = ""
    def out = new FastStringWriter()
    def res = []
    def errorMessage
    def successMessage = [: ]
    if (params.apiKey) {
        def apiKey = params.apiKey
        def currentUser = User.findByApiKey(apiKey);
        if (currentUser) {
			boolean isTenantExpired=isUserTenantExpired(currentUser.userTenantId.toLong())
			if(currentUser.accountLocked ||  !currentUser.enabled || isTenantExpired){ 
				errorMessage = message(code:'api.user.login.inactive',args:[],default:(currentUser.accountLocked?'Account is locked':!currentUser.enabled?'Account is not'+
					' active':isTenantExpired?'Client account expired.For details please email administrator (admin@yourdomain.com)':''))
			  }
			springSecurityService.reauthenticate(currentUser.username)
			def form = Form.read(1284)
			def timeForm = Form.read(1302)
			def friendsForm=Form.read(1303)
			def friendSubFieldName="field1384870221776";
			def emailIdFieldName="field1384870091679"
			def inTime="field1384869826500"
			def outTime="field1384869876172"
            if (params.act == "UT") {
                if (params.entryId){
                    sqlDomainClassService.deleteAllsubformData(params.entryId, form, "field1384869939853")
	                if (params.len) {
	                    def len = params.len.toLong()
	                    for (int i = 1; i <= len; i++) {
	                        def strs = params."str${i}"
	                        strs = strs.split(",")
	                        params."${inTime}" = strs[0]
	                        params."${outTime}" = strs[1]
	                        params.formId = timeForm.id
	                        params.pfii = params.entryId
	                        params.pfid = form.id
	                        params.pffn = "field1384869939853"
	                        def domainInstanceNew = sqlDomainClassService.populate(params, timeForm, null, request)
	                        domainInstanceNew.createdBy =currentUser
	                        sqlDomainClassService.save(domainInstanceNew, timeForm)
	                        sqlDomainClassService.addSubFormInstance(domainInstanceNew.id, params.pfii, params.pffn, form)
	                    }
						res<< (successMessage."Body" = "Successfully done")
						def listOfEntries = sqlDomainClassService.list(form,true,null)
						def otherThenMe=[]
						def myentry
						def friends=[]
						listOfEntries.instanceList.each{
							def entry= sqlDomainClassService.get(it.id, form)
							if(entry."created_by_id"==currentUser.id){
								if( entry && entry."${friendSubFieldName}"){
								friends =entry."${friendSubFieldName}".collect {["email":it."${emailIdFieldName}","id":it.id]}
							   }
								myentry=entry
							}else{
							if(entry."field1384869939853")
								 otherThenMe<<entry
							}
						}
						if(otherThenMe){
							 def alluser=User.findAllByUserTenantId(currentUser.userTenantId)
							 def frindList=[]
							alluser.each{au->
								if(friends*."email".contains(au.username))
								   frindList<<au.id
								}
							def prettytime = new org.grails.prettytime.PrettyTimeTagLib()
							otherThenMe.each{ nl->
								if(frindList.contains(nl."created_by_id")){
									nl."field1384869939853".each{t->
										Date d=t?."date_created"
										Date d1=new Date();
										if(d.getYear()==d1.getYear() && d.getMonth()==d1.getMonth()&& d.getDate()==d.getDate()){
											def temps=[:]
											def user=User.get(nl."created_by_id") 
											myentry."field1384869939853".each{ts->
											boolean sendNote=false
											 sendNote=betweenExclusive(Date.parse( 'hh:mm a',  ts."${inTime}" ).time, Date.parse( 'hh:mm a',  ts."${outTime}" ),
												   Date.parse( 'hh:mm a', t."${inTime}" ),  Date.parse( 'hh:mm a', t."${outTime}") )
												if(sendNote && user )
													gcmPushService.sendNotificationToUser(user, "You have a Gym Buddy time match." )
										   
											}
										}
									}
								}
							}
						}
	                }
               }
           } else if (params.act == "aNdf") {
				def friends=[]
				if (params.entryId){
					def entry= sqlDomainClassService.get(params.entryId, form)
					def listOfEntries = sqlDomainClassService.list(form,true,null)
					def frUser=User.findByUsername(params.friend)
					def fFriendList=[]
					def fentry
					 listOfEntries.instanceList.each{
						 if(fFriendList)
						    return
                         fentry= sqlDomainClassService.get(it.id, form)
						if(fentry."created_by_id"==frUser.id){
							if(fentry && fentry."${friendSubFieldName}"){
							   fFriendList =fentry."${friendSubFieldName}".collect {["email":it."${emailIdFieldName}","id":it.id]}
						     }else{
							  fFriendList=["temps"]
							}
						}
                      }
					  if(!fFriendList){
						  fentry=null
					   }
					def subFormEntries = []
					if(entry && entry."${friendSubFieldName}"){
						friends =entry."${friendSubFieldName}".collect {["email":it."${emailIdFieldName}","id":it.id]}
						}
					 if(friends && friends*."email".contains(params.friend)){ 
						 friends.each{f->
							   if( f."email"==params.friend)
								   sqlDomainClassService.deleteSubForm(form.name, f.id, friendSubFieldName , friendsForm)
						 }
						 fFriendList?.each{f->
							 if( f."email"==currentUser.username)
								 sqlDomainClassService.deleteSubForm(form.name, f.id, friendSubFieldName , friendsForm)
					     }
						 res<< (successMessage."Body" = "Successfully done")
						}else{
						    if( params.friend){
								  params. "${emailIdFieldName}" = params.friend
								  params.formId = friendsForm.id
								  params.pfii = params.entryId
								  params.pfid = form.id
								  params.pffn = "${friendSubFieldName}"
								  def domainInstanceNew = sqlDomainClassService.populate(params, friendsForm, null, request)
								  domainInstanceNew.createdBy =currentUser
								  sqlDomainClassService.save(domainInstanceNew, friendsForm)
								  sqlDomainClassService.addSubFormInstance(domainInstanceNew.id, params.pfii, params.pffn, form)
								 if(fentry){
								   params.pfii=fentry.id
								   params. "${emailIdFieldName}" = currentUser.username
								   def domainInstancefNew = sqlDomainClassService.populate(params, friendsForm, null, request)
								   domainInstancefNew.createdBy =frUser
								   sqlDomainClassService.save(domainInstancefNew, friendsForm)
								   sqlDomainClassService.addSubFormInstance(domainInstancefNew.id, params.pfii, params.pffn, form)
								 }
								 if(frUser)
								      gcmPushService.sendNotificationToUser(frUser, "${currentUser.shortName?:(currentUser.firstName+' '+(currentUser.lastName?:""))} added you on Ascend. " )
						          res<< (successMessage."Body" = "Successfully done")
						    }
				   }
				}
            } else {
			    def matchList=[],otherList=[]
				def listOfEntries = sqlDomainClassService.list(form,true,null)       
				def otherThenMe=[]
				def myentry
				def friends=[]
				listOfEntries.instanceList.each{
					def entry= sqlDomainClassService.get(it.id, form)
					if(entry."created_by_id"==currentUser.id){
						if( entry && entry."${friendSubFieldName}"){
						friends =entry."${friendSubFieldName}".collect {["email":it."${emailIdFieldName}","id":it.id]}
					   }
						myentry=entry
					}else{
					if(entry."field1384869939853")
					     otherThenMe<<entry     
					}
				}
				if(otherThenMe){ 
					 def alluser=User.findAllByUserTenantId(currentUser.userTenantId)
					 def frindList=[]
					alluser.each{au->
						if(friends*."email".contains(au.username))
						   frindList<<au.id
						}
					def prettytime = new org.grails.prettytime.PrettyTimeTagLib()
					otherThenMe.each{ nl->
						if(frindList.contains(nl."created_by_id")){
							def temps=[:]
							def user=User.get(nl."created_by_id")
							temps.FirstName = user.firstName?:''
							temps.LastName = user.lastName?:''
							temps.Username = user.username?:''
							def pictureURL
							def attachments=UserProfile.findByUser(user)?.attachments
							if(attachments){
								pictureURL ="${grailsApplication.config.grails.serverURL}/preview/imagesUrl/${attachments[0].id}?y=${currentUser.apiKey}"
							 }else if(user?.pictureURL && user.pictureURL?.length() > 0){
								pictureURL = user.pictureURL
							 }
							temps."Id"=nl."id" 
							temps.ImageURL = pictureURL?:'' 
							temps."Location"=nl."field1384329039420"?:''
							temps."Goal"=nl."field1384327813199"?:''
							temps."Today's Workout"=nl."field1384329014831"?:''
							temps."Caption"=nl."field1384329044387"?:''
							boolean match= false
							def timeList=[]
							nl."field1384869939853"?.each{t->
								timeList<<["inTime" : t."${inTime}"?:'',"outTime": t."${outTime}"?:'', "dateCreated":prettytime.display(date:t?."date_created"),dt:t?."date_created" ]
								if(!match)
									myentry."field1384869939853".each{tx-> 
									match=betweenExclusive( Date.parse( 'hh:mm a',  tx."${inTime}" ), Date.parse( 'hh:mm a',  tx."${outTime}" ),   Date.parse( 'hh:mm a',  t."${inTime}" ),  Date.parse( 'hh:mm a',  t."${outTime}" ))
								 }
							}
							temps."match"= match
							if( timeList ){
								timeList=srt(timeList,timeList?.size)
								temps."inTime" = timeList[0]."inTime"?:''
								temps."outTime"= timeList[0]."outTime"?:''
								temps."dateCreated"=timeList[0].dateCreated  
								temps."date"=timeList[0].dt
								temps."TimeList"=timeList
							    res<<temps
							}
						}
					}    
					
				}      
				int ns= res.size 
				for(int x=0;x<ns;x++){
						if(res[x]."match")
							matchList<<res[x] 
				        else
					    	otherList<<res[x]
				}
				matchList=srt(matchList, matchList.size)
				otherList=srt(otherList, otherList.size)
				res=[]
				res=matchList
				if(otherList)
				   res.addAll(otherList)
            }
        } else {
            errorMessage = message(code: 'api.user.not.found', args: [],
                default: 'User not found with the given API-Key')
        }
    } else {
        errorMessage = message(code: 'api.no.api.key', args: [],
            default: 'API-Key is required for this access')
    }
       if (errorMessage) {
            out = [ErrorMessage: errorMessage] as JSON
        } else {
            out = [outPut: res] as JSON
        }
        responseContentType = 'text/json'
        if (params.pretty) {
            out = out.toString(true)
        }
    render(status: 200, contentType: responseContentType, text: out)
}

private boolean betweenExclusive(Date myin, Date myout,  Date userin, Date userout){   
       return  (userin==myin && userin==myout) ||(userin>=myin && userin<myout) || (userout>myin && userout<=myout) ;    
   }
def srt(def a, int n) {
	for (int i = 0; i < n; i++) {
		for (int j = 1; j < (n - i); j++) {
			def t0=  Date.parse( 'hh:mm a', a[j-1]."inTime" ).time
			def t1=  Date.parse( 'hh:mm a', a[j]."inTime" ).time
			if (t0 > t1) {
				def t = a[j - 1];
				a[j - 1] = a[j];
				a[j] = t;
			}
		}
	}
	return a
  }
 private  boolean	isUserTenantExpired(long userTenantId){
	Client clientInstance= Client.get(userTenantId) ;
	return (clientInstance?.validTo && clientInstance.validTo< new Date());
   }
}