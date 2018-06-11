

package com.oneapp.cloud.core.log

import grails.converters.JSON
import org.codehaus.groovy.grails.web.errors.GrailsWrappedRuntimeException
import org.codehaus.groovy.grails.commons.ConfigurationHolder

class AppLogController {

    def index = { redirect(action: "list", params: params) }
	def exportService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 15, 100)
        if ( params.sort == null ) {
        	params.sort = 'dateCreated'
        	params.order = 'desc'
        }
      
        [appLogInstanceList: AppLog.list(params), appLogInstanceTotal: AppLog.count()]
    }

   def export = {
        response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
        response.setHeader("Content-disposition", "attachment; filename=form-builder-log-${new Date()}.${params.extension}")
        List fields = ["user", "ip", "uri", "msg", "deviceType", "dateCreated"]
        Map labels = ["user":"User","ip": "IP Address", "uri":"URI","msg": "Message","deviceType": "User Agent", "dateCreated": "Date"]
		
		params.sort = 'dateCreated'
        params.order = 'desc'
        params.max = 1000 // export max 1000 rows only
        list = AppLog.list(params)
        exportService.export(params.format, response.outputStream, list, fields, labels, [:], [:])
    }

    def list_user = {

        render AppLog.executeQuery("select distinct a.user,a.dateCreated from AppLog as a order by a.dateCreated desc") as JSON

    }

	def edit = {
		
		def appLogInstance = AppLog.get(params.id)
		if (!appLogInstance) {
            flash.message = "log.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Log not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [appLogInstance: appLogInstance]
		}
	
	}
   
    def error = {

        AppLog alog = new AppLog()

        // alog.user = "ANONYMOUS"//User.findByUsername(session.SPRING_SECURITY_CONTEXT?.authentication?.principal?.username)
        def exception = request.getAttribute('exception')
        if (exception instanceof GrailsWrappedRuntimeException) {
            //alog.msg =
            println "exception $exception.className, line $exception.lineNumber has throw $exception.cause"
        }

        //alog.save(failOnError:true)

        render(view: "/error")


    }


}
