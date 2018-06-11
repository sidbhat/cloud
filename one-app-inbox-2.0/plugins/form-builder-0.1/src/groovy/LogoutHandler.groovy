

import org.springframework.security.web.authentication.logout.*
import org.springframework.security.core.*
import com.oneapp.cloud.core.*

class LogoutHandler implements org.springframework.security.web.authentication.logout.LogoutHandler {

	def springSecurityService
	
	public void logout(javax.servlet.http.HttpServletRequest request, javax.servlet.http.HttpServletResponse response, Authentication authentication) 
	{
		
	
	}

}