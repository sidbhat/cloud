
import org.springframework.security.web.authentication.session.ConcurrentSessionControlStrategy
import org.springframework.security.web.session.ConcurrentSessionFilter
import org.springframework.security.core.session.SessionRegistryImpl
import org.springframework.security.web.authentication.session.SessionAuthenticationStrategy

// Place your Spring DSL code here
beans = {

    customPropertyEditorRegistrar(CustomDateEditorRegistrar)
    //myLogoutHandler(LogoutHandler)
    //securityEventListener(SpringSecurityEventListener)
	hibernateStringEncryptor(org.jasypt.hibernate.encryptor.HibernatePBEStringEncryptor) {
		registeredName = "gormEncryptor"
		algorithm = "PBEWithMD5AndTripleDES"
		password = "s3kr1t"
		keyObtentionIterations = 1000
	}
	
	sessionRegistry(SessionRegistryImpl)
	
			sessionAuthenticationStrategy(ConcurrentSessionControlStrategy, sessionRegistry) {
					maximumSessions = -1
			}
	
			concurrentSessionFilter(ConcurrentSessionFilter){
					sessionRegistry = sessionRegistry
					expiredUrl = '/login/concurrentSession'
			}

}
