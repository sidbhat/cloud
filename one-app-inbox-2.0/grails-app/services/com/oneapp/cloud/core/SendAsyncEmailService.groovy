package com.oneapp.cloud.core


import com.oneapp.cloud.core.AsynchronousEmailStorage
import grails.plugin.multitenant.core.util.TenantUtils

import java.text.SimpleDateFormat;
class SendAsyncEmailService {

  static transactional = false
  def mailService
  /* This Method will send Async Mail
 * */

  def sendEmail() {
	  
	  def clients = Client.list()
	  clients.each{
	    TenantUtils.doWithTenant(it.id.toInteger()) {
	      def asyncEmailStorageList = AsynchronousEmailStorage.findAllByEmailSentStatusOrEmailSentStatus(AsynchronousEmailStorage.NOT_ATTEMPT, AsynchronousEmailStorage.FAILED)
	      AsynchronousEmailStorage.withTransaction {
	        asyncEmailStorageList.each {asyncEmailStorage ->
	          try {
	            mailService.sendMail {
	              to asyncEmailStorage.emailTO
	              from asyncEmailStorage.emailFrom
	              subject asyncEmailStorage.emailSubject
				  if(asyncEmailStorage.isHtml)
				  	html asyncEmailStorage.emailData
				  else
	              	body asyncEmailStorage.emailData?.encodeAsHTML()
	            }
	
	            asyncEmailStorage.emailSentStatus = AsynchronousEmailStorage.SENT_SUCCESSFULLY
	            asyncEmailStorage.delete(flush: true)
	
	
	          } catch (Exception e) {
			  	log.error e
	            asyncEmailStorage.emailSentStatus = AsynchronousEmailStorage.FAILED
	            asyncEmailStorage.save(flush: true)
	
	
	          }
	
	        }
	      }
	    }
	  }
  }
 def paypalClientNotFound(String emailFrom,String emailTo,String TransactionId,String sbsId){
	      println "Payment not found send EMAIL to admin Paypal T ID ${params.txn_id} sbsID ${params.subscr_id}"
		  TenantUtils.doWithTenant(1){
		  AsynchronousEmailStorage async = new AsynchronousEmailStorage(isHtml:true,
			  		emailSentStatus:AsynchronousEmailStorage.NOT_ATTEMPT,
				    emailFrom : emailFrom, 
					emailTO : emailTo)
		  async.emailSubject="IPN notification(Paypal) ${new SimpleDateFormat('MM/dd/yyyy').format(new Date())}"
		  async.emailData= """Dear Admin ,<br><br>
							   You have a IPN for payment.<br>
								Paypal Transaction Id:${TransactionId}<br>
								Subscriber Id:${sbsId}<br>
								We are not able to identify client for this payment.Please update subscription manually.<br>
								Thank you,<br>
								Your Form Builder Team!"""
		    async.save(flush:true)
	    }
  }
}
