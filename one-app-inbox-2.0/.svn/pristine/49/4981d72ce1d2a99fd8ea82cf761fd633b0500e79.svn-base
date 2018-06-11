package com.oneapp.cloud.core

class BillingHistoryController {
	def springSecurityService
	def index = {
		redirect(controller:"dropDown" ,action: "clientUsage")
		return
	}

	// the delete, save and update actions only accept POST requests
	static allowedMethods = [save: "POST", update: "POST", delete: "POST"]


	def show = {
		def billingHistoryInstance = BillingHistory.get(params.id)
		if (!billingHistoryInstance) {
			flash.message = "billingHistory.not.found"
			flash.args = [params.id]
			flash.defaultMessage = "BillingHistory not found with id ${params.id}"
			redirect(controller:"dropDown" ,action: "clientUsage")
			return
		}
		else {
			if(billingHistoryInstance.client.id==Client.get(springSecurityService.currentUser.userTenantId).id){
				return [billingHistoryInstance: billingHistoryInstance]
			}else{
				redirect(controller:"dropDown" ,action: "clientUsage")
				return
			}
		}
	}
}
