

import com.oneapp.cloud.core.Role;
import com.oneapp.cloud.core.appenders.MyJDBCAppender;

import grails.plugins.springsecurity.SecurityConfigType

// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts

/*def CATALINA_HOME = "CATALINA_HOME"
def CONFIG_FILE_NAME = "config.properties"
if(!grails.config.locations || !(grails.config.locations instanceof List)) {
   grails.config.locations = []
}
 
if(System.getenv(CATALINA_HOME)) {
    def fullPath = System.getenv(CATALINA_HOME) + File.separator + "webapps" + File.separator + "${appName}" + File.separator + "WEB-INF" + File.separator + "classes" + File.separator + CONFIG_FILE_NAME
    println("Config file path: " +fullPath)
    grails.config.locations << "file:" + fullPath
 } else {
    grails.config.locations << "classpath:" + fullPath
 }
*/
grails.config.locations = [ "classpath:app-config.properties"]
grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [html: ['text/html', 'application/xhtml+xml'],
        xml: ['text/xml', 'application/xml'],
        text: 'text/plain',
        js: 'text/javascript',
        rss: 'application/rss+xml',
        atom: 'application/atom+xml',
        css: 'text/css',
        csv: 'text/csv',
        pdf: 'application/pdf',
        rtf: 'application/rtf',
        excel: 'application/vnd.ms-excel',
        ods: 'application/vnd.oasis.opendocument.spreadsheet',
        all: '*/*',
        json: ['application/json', 'text/json'],
        form: 'application/x-www-form-urlencoded',
        multipartForm: 'multipart/form-data'
]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000
grails.attachmentable.maxInMemorySize = 1024
grails.attachmentable.maxUploadSize = 1024000000
grails.attachmentable.uploadDir = "/oneapp/attachments"
grails.attachmentable.poster.evaluator = { session.user }

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// set per-environment serverURL stem for creating absolute links
recaptch.publicKey='6LcpLdoSAAAAAB52vbpKnBedzvdl2pBNNRQshzyq'
recaptch.privateKey='6LcpLdoSAAAAAAkHvCyEQzjhF2t1qXceTNKpQnm8'
environments {
  
    development {
        grails.serverURL = "http://localhost:8080/form-builder"
        rootPath = "/oneapp/upload/"
		return_to_path = "http://localhost:8080/form-builder/grails/openid/openid.dispatch"
		home_path = "/dashboard/index"
		home_path_m = "/home/index"
		realm = "http://localhost:8080"
		consumerKey = "440630415532.apps.googleusercontent.com"
		consumerSecret = "p3mnXMr3iS1_e1s3hIYZ6quj"
		oauthClientID ="171976933749-h0f7elctp4esnjc4f5di99f77lruq8fk.apps.googleusercontent.com"
		oauthClientSecret = "r6moDoQXZIlVKkInyV-e0314"
		oauthRedirectURI = "http://localhost:8080/form-builder/google/oauth2callback"
		 
    }
   
}
oauthApplicationScopes = "https://www.googleapis.com/auth/userinfo.email https://www.googleapis.com/auth/userinfo.profile"
grails.paypal.server = "https://www.paypal.com/cgi-bin/webscr"
grails.paypal.testServer = "https://www.sandbox.paypal.com/cgi-bin/webscr"
grails.paypal.login = "generic@gmail.com"

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    appenders {
		appender name:"dataBaseAppender", new MyJDBCAppender()
		console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    }
   info dataBaseAppender:['grails.app']
	error 	dataBaseAppender:['org.codehaus.groovy.grails.web.servlet',  //  controllers
			'org.codehaus.groovy.grails.web.pages', //  GSP
			'org.codehaus.groovy.grails.web.sitemesh', //  layouts
			'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
			'org.codehaus.groovy.grails.web.mapping', // URL mapping
			'org.codehaus.groovy.grails.commons', // core / classloading
			'org.codehaus.groovy.grails.plugins', // plugins
			'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
			'org.springframework',
			'org.hibernate',
			'net.sf.ehcache.hibernate',
			'grails.app',
			'grails.app.services',
			'grails.app.tablib']
	
    warn 'org.mortbay.log'
}


tenant {
    mode = "multiTenant" // "singleTenant" OR "multiTenant"
    datasourceResolver.type = "db"
    resolver.type = "springSecurity"
    resolver.request.dns.type = "db"
    request.dns.type = "db"
}
//grails.gorm.default.mapping = {
//    'tenantId' column: 'tenant_id', index: 'tenant_id_idx'
//}
grails.views.javascript.library = "jquery"
//jquery.version = '1.4.2.7'

oneapp.mail.read = true  // Read inbox for task updates
oneapp.mail.send = true  // Send emails from oneapp
oneapp.social.read = true // Read social networks
grails {
	mail {
		host = "smtp.gmail.com"
		port = 465
		username = "admin@addyourdomain.com"
		password = "addyourpassword"
		props = ["mail.smtp.auth": "true",
				"mail.smtp.socketFactory.port": "465",
				"mail.smtp.socketFactory.class": "javax.net.ssl.SSLSocketFactory",
				"mail.smtp.starttls.enable": "true",
				 "mail.debug": "false",
				"mail.smtp.socketFactory.fallback": "false"]
	}
}
grails.plugins.springsecurity.useSessionFixationPrevention = true
grails.plugins.springsecurity.roleHierarchy =
    '''
 		 ROLE_SUPER_ADMIN  > ROLE_ADMIN
         ROLE_ADMIN >        ROLE_HR_MANAGER
		 ROLE_HR_MANAGER >   ROLE_TRIAL_USER
         ROLE_TRIAL_USER >   ROLE_USER

'''
//grails.plugins.springsecurity.rejectIfNoRule = true
grails.plugins.springsecurity.securityConfigType = SecurityConfigType.InterceptUrlMap
grails.plugins.springsecurity.interceptUrlMap = [
		'/home/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/applicationConf/**': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
        '/client/**': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
        '/role/**': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
         '/monitoring/**': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
        '/account/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/task/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/mileStone/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/asset/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/companyActivity/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/activityFeed/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/appLog/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
        '/taskUpdate/**': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/tracker/**': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
        '/reports/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/contacts/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/account/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/asynchronousEmailStorage/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
        '/userProfile/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/chart/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		
		'/register/userContactDetail/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/register/updateDetails/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		
		'/user/index/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/list/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/m_email/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/copy/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/create/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/save/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/show/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/edit/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/update/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/user/delete/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		
        '/rule/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/emailSettings/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/report/index/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/data/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/sender/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/logReport/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/logReports/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/formReport/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/config/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/userReport/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/feedLineReport/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/getXandYAxis/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/emailReport/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/search/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/clientFormReport/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/report/formDefaultReport/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ruleSet/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/dashboard/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/dropDown/allowedMethod/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/copy/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/create/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/delete/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/edit/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/index/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/list/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/save/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/update/**': ['ROLE_ADMIN', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/clientUsage/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/makePayment/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/makeSubscription/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/paymentCancel/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/paymentSuccess/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/dropDown/unSubscribeRequest/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		
        '/form/index/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/form/list/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/form/create/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/form/save/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/form/show/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/form/edit/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/form/update/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/form/delete/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formAdmin/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formTemplate/list/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formTemplate/copy/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formTemplate/create/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formTemplate/save/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formTemplate/edit/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formTemplate/update/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formTemplate/delete/**': ['ROLE_TRIAL_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/formViewer/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/groups/list/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/groups/edit/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/groups/create/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/groups/assign/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/groups/save/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/groups/update/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/ddc/copy/**': ['ROLE_HR_MANAGER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ddc/index/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ddc/list/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ddc/create/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ddc/edit/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ddc/save/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ddc/update/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
        '/ddc/delete/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/attachmentable/**': ['ROLE_USER', 'IS_AUTHENTICATED_REMEMBERED'],
		'/blockedIp/**': ['ROLE_SUPER_ADMIN', 'IS_AUTHENTICATED_REMEMBERED']
		
		
		
        
]
//grails.plugins.springsecurity.secureChannel.definition = [
//     '/login/**':         'REQUIRES_SECURE_CHANNEL',
//]
// Added by the Spring Security Core plugin:
grails.plugins.springsecurity.userLookup.userDomainClassName = 'com.oneapp.cloud.core.User'
grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'com.oneapp.cloud.core.UserRole'
grails.plugins.springsecurity.authority.className = 'com.oneapp.cloud.core.Role'
grails.plugins.springsecurity.ui.register.emailFrom = 'admin@yourdomain.com'
grails.plugins.springsecurity.ui.register.emailSubject = 'Please confirm your registration'
grails.plugins.springsecurity.ui.register.defaultRoleNames = ['ROLE_USER']
grails.plugins.springsecurity.useSecurityEventListener = true
grails.plugins.springsecurity.rememberMe.persistent = true

grails.dailyMailer.emailFrom = 'notification@addyourdomain.com'

//grails.plugins.springsecurity.onInteractiveAuthenticationSuccessEvent = { e, appCtx ->
//    com.oneapp.cloud.core.log.UserLog.withTransaction{
//   	def user = com.oneapp.cloud.core.User.get(appCtx.springSecurityService.currentUser.id)
//   	com.oneapp.cloud.core.log.UserLog ul = new com.oneapp.cloud.core.log.UserLog(user: user, lastLogin:new Date())
//   	ul.save(flush:true)
//   	}
//}

//grails.plugins.springsecurity.onAbstractAuthenticationFailureEvent = { e, appCtx ->
          
      //     com.oneapp.cloud.core.log.LoginFailureLog.withTransaction{
              // def ip = rg.springframework.web.context.request.RequestContextHolder.getRequestAttributes()?.getRequest()?.getRemoteAddr()
              // def user = com.oneapp.cloud.core.User.get(appCtx.springSecurityService.currentUser.id)
   		//	   com.oneapp.cloud.core.log.LoginFailureLog ul = new com.oneapp.cloud.core.log.LoginFailureLog(_date:new Date())
   		//	   ul.save(flush:true)
   //	}
   	
             
//}

grails.plugins.springsecurity.logout.handlerNames = [
   'rememberMeServices', 'securityContextLogoutHandler'
   //, 'myLogoutHandler'
]
grails.plugins.springsecurity.rememberMe.persistentToken.domainClassName = 'com.oneapp.cloud.core.PersistentLogin'
grails.plugins.springsecurity.password.algorithm='SHA-512'



// Start of LDAP Specific Settings ###

//grails.plugins.springsecurity.ldap.context.managerDn = 'CN=xxxx,DC=com' // Use full distinguished name, use jxplorer
//grails.plugins.springsecurity.ldap.context.managerPassword = 'password'
//grails.plugins.springsecurity.ldap.context.server = 'ldap://ldap.com:389/'
//grails.plugins.springsecurity.ldap.authorities.ignorePartialResultException = true
//grails.plugins.springsecurity.ldap.search.base = 'DC=Group,DC=com'
//grails.plugins.springsecurity.ldap.search.filter='sAMAccountName={0}'
//grails.plugins.springsecurity.ldap.search.searchSubtree = true
//grails.plugins.springsecurity.ldap.auth.hideUserNotFoundExceptions= false
//grails.plugins.springsecurity.ldap.context.referral = 'follow'
//grails.plugins.springsecurity.ldap.search.derefLink = true
//grails.plugins.springsecurity.providerNames = ['ldapAuthProvider', 'anonymousAuthenticationProvider']
//grails.plugins.springsecurity.conf.ldap.authorities.retrieveGroupRoles = false
//grails.plugins.springsecurity.conf.ldap.authorities.retrieveDatabaseRoles = false
//grails.plugins.springsecurity.ldap.authorities.groupSearchBase ='DC=Group,DC=com'
//grails.plugins.springsecurity.ldap.authorities.groupSearchFilter = 'member={0}'

//role specific ldap config
//grails.plugins.springsecurity.ldap.useRememberMe = false

// Added by the Spring Security Core plugin:
//grails.plugins.springsecurity.userLookup.userDomainClassName = 'org.example.SecUser'
//grails.plugins.springsecurity.userLookup.authorityJoinClassName = 'org.example.SecUserSecRole'
//grails.plugins.springsecurity.authority.className = 'org.example.SecRole'

// End of LDAP ###

// Added by the JQuery Validation plugin:
jqueryValidation.packed = true
jqueryValidation.cdn = false  // false or "microsoft"
jqueryValidation.additionalMethods = false

// Added by the JQuery Validation UI plugin:
jqueryValidationUi {
	errorClass = 'error'
	validClass = 'valid'
	onsubmit = true
	renderErrorsOnTop = false
	
	qTip {
		packed = true
	  classes = 'ui-tooltip-red ui-tooltip-shadow ui-tooltip-rounded'  
	}
	
	/*
	  Grails constraints to JQuery Validation rules mapping for client e validation.
	  Constraint not found in the ConstraintsMap will trigger remote AJAX validation.
	*/
	StringConstraintsMap = [
		blank:'required', // inverse: blank=false, required=true
		creditCard:'creditcard',
		email:'email',
		inList:'inList',
		minSize:'minlength',
		maxSize:'maxlength',
		size:'rangelength',
		matches:'matches',
		notEqual:'notEqual',
		url:'url',
		nullable:'required',
		unique:'unique',
		validator:'validator'
	]
	
	// Long, Integer, Short, Float, Double, BigInteger, BigDecimal
	NumberConstraintsMap = [
		min:'min',
		max:'max',
		range:'range',
		notEqual:'notEqual',
		nullable:'required',
		inList:'inList',
		unique:'unique',
		validator:'validator'
	]
	
	CollectionConstraintsMap = [
		minSize:'minlength',
		maxSize:'maxlength',
		size:'rangelength',
		nullable:'required',
		validator:'validator'
	]
	
	DateConstraintsMap = [
		min:'minDate',
		max:'maxDate',
		range:'rangeDate',
		notEqual:'notEqual',
		nullable:'required',
		inList:'inList',
		unique:'unique',
		validator:'validator'
	]
	
	ObjectConstraintsMap = [
		nullable:'required',
		validator:'validator'
	]
	
	CustomConstraintsMap = [
		phone:'true', // International phone number validation
		phoneUS:'true',
		alphanumeric:'true',
		letterswithbasicpunc:'true',
    lettersonly:'true'
	]	
}

uniForm.validation = false
jqueryFormBuilder.minified = false

grails.plugins.freemarkertags = [
   autoImport: false,
   asSharedVariables: true
 ]

formBuilder {
	reloadUpdatedDomainClassesInMs = 6000000
	SingleLineText {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
	LookUp {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
	SubForm {
		type = 'List'
		defaultConstraints = []
	}
	Paypal {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
	Likert {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
	PageBreak {
		type = 'String'
		defaultConstraints = []
	}
	FormulaField {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
	FileUpload {
		type = 'String'
		defaultConstraints = [maxSize: 1]
	}
	SingleLineNumber{
		type = 'BigDecimal'
		defaultConstraints = [maxSize: 255]
	}
	SingleLineDate {
		type = 'Date'
		defaultConstraints = [maxSize: 255]
	}
   
	NumberResult{
		type = 'BigDecimal'
		dbType = 'decimal(19,2)'
		defaultConstraints = [maxSize: 255]
	}
	DateResult{
		type = 'Date'
		dbType = 'datetime'
		defaultConstraints = [maxSize: 255]
	}
   
	SingleLineTextDate {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}

  PlainText {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
   LinkVideo {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
   ScaleRating {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
	dropdown {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
	
	MultiLineText {
		type = 'String'
		defaultConstraints = [maxSize: 10000]
	}
  ImageUpload {
		type = 'String'
		defaultConstraints = [maxSize: 255]


	}
  CheckBox {
		type = 'String'
		defaultConstraints = [maxSize: 255]


	}
  GroupButton {
		type = 'String'
		defaultConstraints = [maxSize: 255]


	}
  LikeDislikeButton {
	  type = 'String'
	  defaultConstraints = [maxSize: 255]


  }
  PlainTextHref {
		type = 'String'
		defaultConstraints = [maxSize: 255]
	}
}

formViewer.responseList.extraFieldsToShow=[
	[messageCode:'formViewer.response.createdBy',messageDefault:'Created By',name:'created_by_id',type:'String'],
	[messageCode:'formViewer.response.dateCreated',messageDefault:'Date Created',name:'date_created',type:'Date']
]
formViewer.responseList.export.extraFieldsToShow=[
	[messageCode:'formViewer.response.createdBy',messageDefault:'Created By',name:'created_by_id',type:'String'],
	[messageCode:'formViewer.response.dateCreated',messageDefault:'Date Created',name:'date_created',type:'Date']
]
formViewer.responseList.dateFormat="MM/dd/yyyy hh:mm a"

formBuilder.objectNotFound.redirection.action = "list"
formBuilder.objectNotFound.redirection.controller = "form"
subFieldTypes = ["SingleLineText","Email","SingleLineDate", "SingleLineNumber", "dropdown", "FormulaField", "LookUp","MultiLineText","GroupButton","CheckBox","ScaleRating","AddressField","NameTypeField"]
fields.forRule = ["SingleLineText","Email","SingleLineDate", "SingleLineNumber", "dropdown", "FormulaField", "LookUp","MultiLineText","GroupButton","ScaleRating"]
masterFieldTypes = ["SingleLineText","Email","SingleLineDate", "SingleLineNumber", "dropdown", "FormulaField", "LookUp", "MultiLineText", "FileUpload","AddressField","NameTypeField"]
formAdmin.fields.notKeyFigures=['PlainText', 'PlainTextHref', 'ImageUpload', 'LinkVideo','PageBreak']
formViewer.fields.defaultValueFor=['SingleLineNumber', 'SingleLineText', 'MultiLineText', 'SingleLineNumber']
form.workWithSQL = true
attachment.image.ext=["gif","png","jpg","jpeg","bmp","tif"]
format.date = 'MM/dd/yyyy'

ROLES_TO_SHOW_ALL_RECORDS=Role.ROLE_TRIAL_USER
ROLES_TO_SHOW_ALL_FIELDS=Role.ROLE_TRIAL_USER

ruleSet.intimationSendTo.onChangeValues = ['User','Department','Group']

reports.email.search.defaultDaysPrior=7
reports.email.search.maxDaysPrior=180

formViewerBackground = [none:[bodyBGColor:''],theme1:[bodyBGColor:'EEE'],theme2:[bodyBGColor:'EEE'],theme3:[bodyBGColor:'022446'],theme4:[bodyBGColor:'EEE']]
formBuilder.currencies=[
						USD:"&#36;",
						GBP:"&pound;",
						JPY:"&yen;",
						EUR:"&euro;"
					]

examples = ['US':'(201) 555-0123','GB':'0121 234 5678','AU':'(02) 1234 5678','CA':'(204) 234-5678','ZZ':'+1 201-555-0123']
