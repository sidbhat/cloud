
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
