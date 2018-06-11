import com.oneapp.cloud.core.*
import org.grails.formbuilder.*
import grails.plugin.multitenant.core.util.TenantUtils

class RulesJob {
    
    static triggers = {
        simple name:'rulesJob', startDelay:300000,repeatInterval:300000
	}
	
	def group = "RulesJobGroup"
    
    def ruleService
    def dynamicDomainService
    def grailsApplication
    
    def execute() {
    
	def clientListInstance = Client.list()
		clientListInstance.each {
		    TenantUtils.doWithTenant((int)it.id) {
		     		ruleService.runRules()
			 }//end do with tenant
		}
   		
    }// end execute

}//end of class
