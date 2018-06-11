

package com.oneapp.cloud.core

import org.codehaus.groovy.grails.commons.ConfigurationHolder as Conf
import grails.plugins.springsecurity.Secured
import org.grails.formbuilder.*

class ClientController {

    def index = { redirect(action: "list", params: params) }
    def clientDataLoadService
    def calendarOneAppService
	def clientService
	def sqlDomainClassService
	
    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]

    def springSecurityService
  	def getSessionUser() {
  	  return springSecurityService.currentUser
  	}
	
	def logo = {
        def clientInstance = Client.get(params.id)
        //response.setContentType("image/jpeg")
        response.outputStream << clientInstance.logo
    }

    def loadExcel(Long tenantId) {

        def file = request.getFile('excel')
        if (file == null || file.empty)
        return
        File dir = new File(Conf.config.rootPath + tenantId)
        dir.mkdirs()

        File destination = new File(dir.getPath() + "/" + file.getOriginalFilename())
        file.transferTo(destination)

        //println destination

        def uploadedFile = destination

        def userMapList = clientDataLoadService.loadWithExcel("" + uploadedFile)
        int success = 0
        int error = 0
        String err = ""

        if (!userMapList) {
            flash.message = "client.excel.error"
            flash.args = [params.excel]
            flash.defaultMessage = "Error loading excel ${params.file}"
        }
        else {
            userMapList.each { Map userParams ->
                def newUser = new User(userParams)
                newUser.userTenantId = tenantId
                if (!newUser.save()) {
                    error++
                    err = err + " " + newUser.errors.fieldErrors + " \n"
                } else {
                    // Create a user role mapping with the user created.
                    def newUserRole = new UserRole(user: newUser, role: Role.findByAuthority(Role.ROLE_USER))
                    newUserRole.save()
                    // Update calendar with dob information
                    if (newUser.dob != null) {
                        calendarOneAppService?.updateCalendar(newUser.id, 'User')
                    }
                    success++
                }
            }
            flash.message = "client.excel.summary"
            flash.args = [params.excel]
            flash.defaultMessage = "<b> Errors ${error} </b><br/> ${err}. <br/><b>Success ${success}</b>"

        }
    }

    def list = {
        UserProfile up = UserProfile.findByUser(getSessionUser())
        def max = 10
        if ( up?.numOfRows )
        	max = up?.numOfRows 
        params.max = Math.min(params.max ? params.max.toInteger() : max , 100)
		if (!params.sort)
		    params.sort = 'dateCreated'
        if (!params.order)
        	params.order = 'desc'        
		[clientInstanceList: Client.list(params), clientInstanceTotal: Client.count()]
    }
	
	def create = {
        def clientInstance = new Client()
        clientInstance.properties = params
        return [clientInstance: clientInstance]
    }

	@Secured(['ROLE_SUPER_ADMIN'])
    def save = {
        def clientInstance = new Client(params)
        clientInstance.name = clientInstance.name.replaceAll(/\w+/, { it[0].toUpperCase() + ((it.size() > 1) ? it[1..-1] : '') })
		UUID uuid = UUID.randomUUID()
		String key= uuid 
		clientInstance.consumerKey=key.replaceAll("-","")
        if (!clientInstance.hasErrors() && clientInstance.save(flush:true,validate:true)) {
            flash.message = "client.created"
            flash.args = [clientInstance.name]
			flash.defaultMessage = "Client ${clientInstance.name} created."
			/*This code is commented to limit the auto creation of the user for new client
            flash.defaultMessage = "Client ${clientInstance.name} created. Admin user admin${clientInstance.id}@yourdomain.com with password admin has also been created."
             */
            // Load the initial data for the client
            clientDataLoadService.loadInitialData(clientInstance.id, getSessionUser())
            loadExcel(clientInstance.id)
            redirect(action: "list", id: clientInstance.id)
        }
        else {
            render(view: "create", model: [clientInstance: clientInstance])
        }
    }

    def show = {
        def clientInstance = Client.get(params.id)
        if (!clientInstance) {
            flash.message = "client.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Client not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
            return [clientInstance: clientInstance]
        }
    }

    def edit = {
        def clientInstance = Client.get(params.id)
        if (!clientInstance) {
            flash.message = "client.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Client not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
			def billingHistory
			billingHistory = BillingHistory.createCriteria().list(max:1){
				eq "client", clientInstance
				projections{
					order "billDate","desc"
				}
			}
            return [clientInstance: clientInstance,billingHistory:billingHistory]
        }
    }
 
 
  def deleteLogo={
   def clientInstance = Client.get(params.id)
    clientInstance.logo=null
    clientInstance.save(flush:true)
    render text:"&nbsp;&nbsp;Logo deleted  "
  }
    def update = {
        def clientInstance = Client.get(params.id)
		def formCount = Form.findAllByTenantId(clientInstance.id)
		def emailCount = EmailSettings.count()
		try{
			if(formCount.size() > Integer.parseInt(params.form)){
				throw new Exception("Cannot reduce the form count as client already has ${formCount.size()} forms")
			}else if(clientInstance.maxEmailAccount != Long.parseLong(params.maxEmailAccount)){
				def userList = User.findAllByUserTenantId(clientInstance.id)
				def isEmailCountLimit = true
				userList.each{
					def emailSettingCount = EmailSettings.findAllByUser(it)
					if(emailSettingCount.size() > Integer.parseInt(params.maxEmailAccount)){
						isEmailCountLimit = false
					}
				}
				if(!isEmailCountLimit)
					throw new Exception("Cannot reduce the email accounts count as some users of current client already have more than ${params.maxEmailAccount} email accounts")
			}
	        if (clientInstance) {
	            if (params.version) {
	                def version = params.version.toLong()
	                if (clientInstance.version > version) {
	                    clientInstance.errors.rejectValue("version", "client.optimistic.locking.failure", "Another user has updated this Client while you were editing")
	                    render(view: "edit", model: [clientInstance: clientInstance])
	                    return
	                }
	            }
				params.remove("plan")
		          clientInstance.properties = params
				  if(!clientInstance?.consumerKey){
					  UUID uuid = UUID.randomUUID()
					  String key= uuid
					  clientInstance.consumerKey=key.replaceAll("-","")
				  }
	            if (!clientInstance.hasErrors() && clientInstance.save()) {
	                flash.message = "client.updated"
	                flash.args = [clientInstance.name]
	                flash.defaultMessage = "Client ${clientInstance.name} updated"
	
	                // Check for excel file
	                loadExcel(clientInstance.id)
	                redirect(action: "list", id: clientInstance.id)
	            }
	            else {
	                render(view: "edit", model: [clientInstance: clientInstance])
	            }
	        }
	        else {
	            flash.message = "client.not.found"
	            flash.args = [params.id]
	            flash.defaultMessage = "Client not found with id ${params.id}"
	            redirect(action: "edit", id: params.id)
	        }
		}catch(Exception ex){
			flash.message = ex.message
			flash.defaultMessage = flash.message
			redirect(action: "edit", id: params.id)
		}
    }
	@Secured(['ROLE_SUPER_ADMIN'])
    def delete = {
        def clientInstance = Client.get(params.id)
        if (clientInstance) {
            try {
				sqlDomainClassService.deleteUserAppAccessDetails(clientInstance.id)
                clientInstance.delete()
                flash.message = "client.deleted"
                flash.args = [clientInstance.name]
                flash.defaultMessage = "Client ${clientInstance.name} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error e
                flash.message = "client.not.deleted"
                flash.args = [clientInstance.name]
                flash.defaultMessage = "Client ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "client.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Client not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
