/******************************************************************************
 * [ NIKKISHI CONFIDENTIAL ]                                                  *
 *                                                                            *
 *    Copyright (c) 2011.  Nikkishi LLC                                       *
 *    All Rights Reserved.                                                    *
 *                                                                            *
 *   NOTICE:  All information contained herein is, and remains                *
 *   the property of Nikkishi LLC and its suppliers,                          *
 *   if any.  The intellectual and technical concepts contained               *
 *   herein are proprietary to Nikkishi LLC and its                           *
 *   suppliers and may be covered by U.S. and Foreign Patents,                *
 *   patents in process, and are protected by trade secret or copyright law.  *
 *   Dissemination of this information or reproduction of this material       *
 *   is strictly forbidden unless prior written permission is obtained        *
 *   from Nikkishi LLC.                                                       *
 ******************************************************************************/

import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import com.oneapp.cloud.core.*

class LogoutController {
	def springSecurityService
    /**
     * Index action. Redirects to the Spring security logout uri.
     */
    def index = {
        //println "Logout called ${ springSecurityService?.currentUser?.username }"
     	def o = OauthDetails.findWhere ( username:springSecurityService?.currentUser?.username, source:"twitter" )
			o?.delete(flush:true)
        o = OauthDetails.findWhere ( username:springSecurityService?.currentUser?.username, source:"facebook" )
			o?.delete(flush:true)
        o = OauthDetails.findWhere ( username:springSecurityService?.currentUser?.username, source:"linkedin" )
			o?.delete(flush:true)
        
        redirect uri: SpringSecurityUtils.securityConfig.logout.filterProcessesUrl // '/j_spring_security_logout'
    }
}
