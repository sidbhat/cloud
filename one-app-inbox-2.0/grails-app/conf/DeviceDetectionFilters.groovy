import com.oneapp.cloud.core.User;



class DeviceDetectionFilters {

    def filters = {

        def IPHONE = "iPhone"
        def IPAD = "iPad"
        def ANDROID = "Android"
        def BLACKBERRY = "Blackberry"
		def springSecurityService
		
        detectMobile(controller: '*', action: '*') {
            before = {

                def mobileList = [IPHONE, ANDROID, BLACKBERRY]
                def tabletList = [IPAD]
                def userAgent = request.getHeader('user-agent')
				//println "USER AGENT ::"+userAgent
		
                // Return if the user-agent is null
                if (userAgent == null)
                    return

                // Check for mobile devices
                for (i in mobileList){
                    if (userAgent.indexOf(i) != -1){
                        request['isMobile'] = true
                        break;                        
                    }
                }

                // Check for tablets
                if(!request['isMobile']){
                   for (i in tabletList){
                      if (userAgent.indexOf(i) != -1){
						 if( !['embed','PF'].contains(params.controller))
						 		request['isTablet'] = true
						break;
                    // Everything else gets the desktop view
                      }else{
                        request['isDesktop'] = true
					  }
					}
				} 
				//println  "Mobile Reques::"+request['isMobile']
				
				def isUserLoggedIn = session.isUserLoggedIn
				def username = request.getRemoteUser()
				if(!isUserLoggedIn && username){
					session.isUserLoggedIn = true
					def user = User.findByUsername(username)
					session.lastLastLogin = user.lastLogIn
					user.lastLogIn = new Date()
					if(!user.lastActivity){
						user.lastActivity = user.lastLogIn
						user.lastViewed = user.lastLogIn
						user.save()
					}
				}
            }
            return true
        }
		
		
    }

}