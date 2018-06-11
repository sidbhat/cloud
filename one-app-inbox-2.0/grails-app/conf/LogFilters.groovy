

import com.oneapp.cloud.core.*
import com.oneapp.cloud.core.log.AppLog
import org.springframework.web.context.request.RequestContextHolder
import org.springframework.web.servlet.support.RequestContextUtils as RCU;

class LogFilters {

    def filters = {
        all(controller: '*', action: '*') {
            before = {
				
				
				String requestIP=request.getRemoteAddr()
				if(requestIP && BlockedIp.countByIpAdress(requestIP)>0){
					  render """<h1>This IP Address is Blocked </h1>For details please email
			administrator(<a 
				href="mailto:admin@yourdomain.com?subject=Account expired">admin@yourdomain.com</a>)"""
					return
				}else{
				
				def username
				try{
					username = session.SPRING_SECURITY_CONTEXT?.authentication?.principal?.username
					try{ 
						flash.language='en'
					}catch(Exception ey){}
				}catch(Exception e){
					session.invalidate()
					response.sendRedirect(request.getContextPath()+"/")
					return false
				}
                
                if (username) {

                   	 User user = User.findByUsername(username)
					if(!user){
						session.invalidate()
						response.sendRedirect(request.getContextPath()+"/")
						return false
					}
                   	 UserProfile up = UserProfile.findByUser(user)
                   	 if ( up && up.defaultLanguage ){
							

                   	  session.lang = up.defaultLanguage
                   	  def locale = new Locale(session.lang) 
                   	  RCU.getLocaleResolver(request)?.setLocale(request,response,locale) 
                   	 }
                   		
                   	def sessionId = RequestContextHolder.getRequestAttributes()?.getSessionId()
                    def ip = RequestContextHolder.getRequestAttributes()?.getRequest()?.getRemoteAddr()
                    def authenticateService
                    def logEventService

                    if (session["user"] == null)
                    	session["user"] = user
						def pictureURL=""
						if(up?.attachments){
						   pictureURL ="${request.getScheme()}://${request.getServerName()}:${request.getServerPort()}${request.getContextPath()}/attachmentable/renderImg/${up.attachments[0].id}"
						 } else if(user?.pictureURL && user.pictureURL?.length() > 0){
							pictureURL = user.pictureURL
						 }
						 session.pictureURL=pictureURL
					// Check for error
					//    <strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/>
        			//	${request.'javax.servlet.error.servlet_name'}<br/>
        			/*try{
        			AppLog.withSession {
                        AppLog alog = new AppLog()
                        alog.ip = ip
                        alog.uri = request.forwardURI
                        alog.user = session["user"]
                        alog.msgType="I"
                   		alog.deviceType = request.getHeader("User-Agent")
                    	alog.save()
                    }
                    }catch ( Exception ex )
                    {
						println ex
                    	//log.error ex
                    }*/
                    // Record last access uri for user
                  //  com.oneapp.cloud.core.log.UserLog ul =  com.oneapp.cloud.core.log.UserLog.findByUser(user: session["user"])
                   // ul.lastURI = request.forwardURI
   					//ul.save(flush:true)

                }
            }
             
            }
            after = {}
            afterView = {
           
            }
        }
    }
}