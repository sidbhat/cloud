 
 import com.oneapp.cloud.core.*
 import grails.plugin.multitenant.core.util.TenantUtils

class DailyUpdateJob {
    def dailyUpdateService
	 static triggers = {
		 cron name:'simpleTrigger', cronExpression: "0 0 9 * * ?"

    }
  
  	def execute() {
		    def clientListInstance = Client.list()
			clientListInstance.each {c->
				TenantUtils.doWithTenant(c.id.toInteger()) {
					if ( !c?.disableEmail ) // send email only if client email is not disabled
					//if(false)
						dailyUpdateService.sendDailyReportToUser(c.id)
				}//end do with tenant
			}
  	} 
}