import grails.converters.JSON;

import org.icepush.PushContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH
import com.oneapp.cloud.core.log.*
import com.oneapp.cloud.core.*
import org.codehaus.groovy.grails.commons.*;
import org.codehaus.groovy.grails.web.servlet.GrailsApplicationAttributes
import org.springframework.context.*
import org.springframework.web.context.WebApplicationContext
import org.springframework.web.context.support.WebApplicationContextUtils

class InterceptFilters {

	Date start
	Date end
	
	def filters = {
        all(controller:'*', action:'*') {
    	    	
            before = {
            	start = new Date()    
            }
            after = {
            	try {
				def client 
				if ( session.user != null ) {
				    client = Client?.get( session?.user?.userTenantId )
					if( client && client.id!=1l && client?.validTo 
						&& client.validTo< new Date() && !["alert",'apiV1',"login","logout","register"].contains(controllerName)){
						    response.sendRedirect(request.getContextPath()+"/register/accountInfo")
					}
				 } 
            	def applicationConfInstance = ApplicationConf.get(1)
            	if ( applicationConfInstance?.trackChanges != null && !applicationConfInstance?.trackChanges )
            		return
				def cotN = controllerName
				def recI = params?.id
				if(controllerName == 'attachmentable' && params.feed != null){
					cotN = "activityFeed"
					recI = params.feed
				}
            	
				if ( cotN == "ddc" || cotN == "PF" ||
            		 cotN == "activityFeed" || cotN == "logout" || cotN == "user" ||
					 cotN == "userProfile" || cotN == "emailSettings" || cotN == "report"
            	) 
            	{
            	
            	
            	def clientID 
            	def clientName
            	def formID
            	if(client){
					clientID = client?.id
					clientName =client?.name
				}
            		
            	end = new Date()
            
            	if ( cotN == "ddc" || cotN?.indexOf("form") != -1  )
            		formID = params?.id
				//else if ( controllerName == "PF" && actionName.indexOf("save") != -1)
				def paramsMessage = [:]
				params.each{k,v->
					try{
						if(v.length()>16){
							v = v.substring(0,16)
						}
					}catch(Exception ex){}
					if(k == "password")
						paramsMessage."${k}"= ""
					else
						paramsMessage."${k}"=v?.encodeAsHTML()
				}
				paramsMessage = paramsMessage as JSON
				Tracker t = new Tracker( 
            	dateCreated: new Date(), 
            	loadTime: end-start, 
            	params:paramsMessage.toString(), 
            	controller:cotN, 
            	action:actionName,
            	userAgent: request.getHeader("User-Agent"),
            	ip:request.getRemoteAddr(),
            	user: session?.user,
            	recordID: recI,
            	formID:formID,
            	clientID : clientID,
            	clientName: clientName ).save()
            	
            	}
				if(cotN!='alert' && cotN!='login' && cotN!='PF'){
	            	push 'secondGroup'
	            	push 'loggedInUser'
					}
            	}catch ( Exception e ) {
				  e.printStackTrace()
            		println "Request here-: "+request.getRequestURL()+", Parameters here are: "+params+", User: "+session.user
            	}
    
    	}
            afterView = {
                
            }
        }
         
    }
     def push(String s) {   
      		     PushContext.getInstance(SCH.servletContext).push (s)
   			 } 
}
