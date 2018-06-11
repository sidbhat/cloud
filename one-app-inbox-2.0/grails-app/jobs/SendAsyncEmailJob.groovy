import com.oneapp.cloud.core.*

class SendAsyncEmailJob {
     static triggers = {
        simple name:'simpleTriggerAsyncEmail', startDelay:60*1000*2

    }
	 def group = "SendAsyncEmailJobGroup"
      def sendAsyncEmailService
    def execute() {
    
    	// println org.codehaus.groovy.grails.commons.ConfigurationHolder.config.oneapp.mail.send
   		 if ( org.codehaus.groovy.grails.commons.ConfigurationHolder.config.oneapp.mail.send )
 		  sendAsyncEmailService.sendEmail()
    }
}
