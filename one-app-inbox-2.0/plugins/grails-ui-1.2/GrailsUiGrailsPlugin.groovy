import org.springframework.beans.factory.config.MethodInvokingFactoryBean

class GrailsUiGrailsPlugin {
    def version = "1.2"
    def dependsOn = [:]
    
    def grailsVersion = "1.2 > *"

    def author = "Matthew Taylor"
    def authorEmail = "rhyolight@gmail.com"
    def title = "Grails UI"
    def description = "Provides a standard UI tag library for ajaxy widgets using YUI."

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugins/grails-ui"

    def license = "APACHE"
    def issueManagement = [ system: "JIRA", url: "http://jira.grails.org/browse/GPUI" ]
    def scm = [ url: "http://svn.codehaus.org/grails-plugins/grails-grails-ui/" ]

    def doWithSpring = {
        grailsUIConfig(MethodInvokingFactoryBean) {
            targetObject = new ConfigSlurper()
            targetMethod = 'parse'
            arguments = [application.classLoader.loadClass('GrailsUIConfig')]
        }
    }
   
    def doWithApplicationContext = { applicationContext ->
    }

    def doWithWebDescriptor = { xml ->
    }
	                                      
    def doWithDynamicMethods = { ctx ->
    }
	
    def onChange = { event ->
    }

    def onConfigChange = { event ->
    }
}
