package org.grails.formbuilder

import javax.servlet.http.HttpServletResponse

import org.codehaus.groovy.grails.commons.ApplicationHolder;
import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH;

import com.macrobit.grails.plugins.attachmentable.domains.Attachment;
import com.macrobit.grails.plugins.attachmentable.util.AttachmentableUtil;

import grails.converters.JSON;

class PreviewController {
	def springSecurityService
	def sqlDomainClassService
	def grailsApplication
	def imageExts = CH.config.attachment.image.ext
	def item = {
		def itemImageURLs = []
		def params_id = params.id
		if(params_id && params_id.indexOf("_")!=-1){
			def ids = params_id.split("_")
			try{
				def purchaseForm = Form.get(ids[1])
				def paypalField = purchaseForm.fieldsList.find{it.type == "Paypal"}
				def pFS = JSON.parse(paypalField.settings)
				if(pFS && pFS.iimgf){
					def itemForm = Form.get(pFS.itemForm)
					if(!itemForm){
						throw new Exception("Item form not found with id: "+pFS.itemForm)
					}
					def itemId = ids[0]
					def itemImgField = itemForm.fieldsList.find{it.name == pFS.iimgf && it.type == 'FileUpload'}
					if(itemImgField){
						def itemAttachments = itemForm.getDomainAttachments(itemId.toLong())
						itemAttachments.findAll{imageExts.contains("${it.ext}")}
						itemAttachments.each{
							itemImageURLs << "${createLink(action:'img',id:params_id,params:[ati:it.id])}"
						}
					}
				}
			}catch(Exception w){
				log.error("Item Preview Error: Error gettings item to be purchased or purchase form. ids are "+params_id+". Error is: "+w)
			}
		}
		return [itemImageURLs:itemImageURLs]
	}
	
	def img = {
		def ati = params.ati
		def params_id = params.id
		if(params_id && params_id.indexOf("_")!=-1){
			def ids = params_id.split("_")
			try{
				def purchaseForm = Form.get(ids[1])
				def paypalField = purchaseForm.fieldsList.find{it.type == "Paypal"}
				def pFS = JSON.parse(paypalField.settings)
				if(pFS && pFS.iimgf){
					def itemForm = Form.get(pFS.itemForm)
					if(!itemForm){
						throw new Exception("Item form not found with id: "+pFS.itemForm)
					}
					def itemId = ids[0]
					def itemImgField = itemForm.fieldsList.find{it.name == pFS.iimgf && it.type == 'FileUpload'}
					if(itemImgField){
						def itemAttachments = itemForm.getDomainAttachments(itemId.toLong())
						itemAttachments.findAll{imageExts.contains("${it.ext}")}
						def attachment = itemAttachments.find{"${it.id}"==ati}
						if(attachment){
							File file = AttachmentableUtil.getFile(CH.config, attachment)
							if (file.exists()) {
								String filename = attachment.filename
								['Content-disposition': "${params.containsKey('inline') ? 'inline' : 'attachment'};filename=\"$filename\"",
									'Cache-Control': 'private',
									'Pragma': ''].each {k, v ->
									response.setHeader(k, v)
								}
								response.contentType = attachment.contentType
								try{
									file.withInputStream{fis->
										response.outputStream << fis
									}
								}catch(Exception e){}
								return
							}
						}
					}
				}
			}catch(Exception w){
				log.error("Item Preview Img Error: Error gettings item to be purchased or purchase form. ids are "+params_id+". Error is: "+w)
			}
		}
		response.status = HttpServletResponse.SC_NOT_FOUND
	}
	
	def firstImg = {
		def params_id = params.id
		if(params_id && params_id.indexOf("_")!=-1){
			def ids = params_id.split("_")
			try{
				def purchaseForm = Form.get(ids[1])
				def paypalField = purchaseForm.fieldsList.find{it.type == "Paypal"}
				def pFS = JSON.parse(paypalField.settings)
				if(pFS && pFS.iimgf){
					def itemForm = Form.get(pFS.itemForm)
					if(!itemForm){
						throw new Exception("Item form not found with id: "+pFS.itemForm)
					}
					def itemId = ids[0]
					def itemImgField = itemForm.fieldsList.find{it.name == pFS.iimgf && it.type == 'FileUpload'}
					if(itemImgField){
						def itemAttachments = itemForm.getDomainAttachments(itemId.toLong())
						itemAttachments.findAll{imageExts.contains("${it.ext}")}
						def attachment = itemAttachments.size()>0?itemAttachments.get(0):null
						if(attachment){
							File file = AttachmentableUtil.getFile(CH.config, attachment)
							if (file.exists()) {
								String filename = attachment.filename
								['Content-disposition': "${params.containsKey('inline') ? 'inline' : 'attachment'};filename=\"$filename\"",
									'Cache-Control': 'private',
									'Pragma': ''].each {k, v ->
									response.setHeader(k, v)
								}
								response.contentType = attachment.contentType
								try{
									file.withInputStream{fis->
										response.outputStream << fis
									}
								}catch(Exception e){}
								return
							}
						}
					}
				}
				File file = ApplicationHolder.application.parentContext.getResource("/images/previewImg.png").file
				if (file.exists()) {
					String filename = "previewImg.png"
					['Content-disposition': "inline;filename=\"$filename\"",
						'Cache-Control': 'private',
						'Pragma': ''].each {k, v ->
						response.setHeader(k, v)
					}
					response.contentType = "image/png"
					try{
						file.withInputStream{fis->
							response.outputStream << fis
						}
					}catch(Exception e){}
					return
				}
			}catch(Exception w){
				log.error("Item Preview Img Error: Error gettings item to be purchased or purchase form. ids are "+params_id+". Error is: "+w)
			}
		}
		response.status = HttpServletResponse.SC_NOT_FOUND
	}
	
	def formImagePath = {
		def params_id = params.id
		def attachment = Attachment.get(params_id.toLong())
		if((attachment.lnk.referenceClass == "org.grails.formbuilder.Form" && attachment.domainInstanceId==null)||attachment.lnk.referenceClass=="org.grails.formbuilder.FormTemplate"){
			File file = AttachmentableUtil.getFile(CH.config, attachment)
			if (file.exists()) {
				String filename = attachment.filename
				['Content-disposition': "${params.containsKey('inline') ? 'inline' : 'attachment'};filename=\"$filename\"",
					'Cache-Control': 'private',
					'Pragma': ''].each {k, v ->
					response.setHeader(k, v)
				}
				response.contentType = attachment.contentType
				try{
					file.withInputStream{fis->
						response.outputStream << fis
					}
				}catch(Exception e){}
				return
			}
		}
	}
	def imagesUrl={
		def params_id = params.id
		def currentUser=springSecurityService?.currentUser
		if(!currentUser && params.y){
			def apiKey = params.y
			currentUser = com.oneapp.cloud.core.User.findByApiKey(apiKey);
		}
		if(currentUser){
			def attachment = Attachment.get(params_id.toLong())
			def posterUser =com.oneapp.cloud.core.User.get(attachment.posterId)
			def allowuser =false
			if(!currentUser.authorities*.authority?.contains(com.oneapp.cloud.core.Role.ROLE_TRIAL_USER))
			  allowuser=allowuser?:(attachment.lnk.referenceClass=="com.oneapp.cloud.core.UserProfile" && posterUser.userTenantId==currentUser.userTenantId)
		    else
		      allowuser=allowuser?:(attachment.lnk.referenceClass=="com.oneapp.cloud.core.UserProfile" && posterUser.id==currentUser.id)
			if(!allowuser && attachment.lnk.referenceClass=="com.oneapp.cloud.core.ActivityFeed"){
				def reference = getClass().classLoader.loadClass(attachment.lnk.referenceClass).withCriteria(){
					eq 'id',attachment.lnk.referenceId
					or{
						'createdBy'{
							eq "id",currentUser.id
						} 
						eq "visibility","Company"
						'sharedGroups'{
							'user'{
								eq "id",currentUser.id
							}
						}
						'sharedUsers'{
							eq "id",currentUser.id
						}
						'sharedRoles'{
							eq "id",currentUser.authorities*.id.get(0)
						}
						if(currentUser.department){
							'sharedDepts'{
								eq "id",currentUser.department.id
							}
						}
					}
					projections{
						distinct("id")
					}
				}
				if(reference){
					allowuser=true
				}
			}
			if(allowuser){
				File file = AttachmentableUtil.getFile(CH.config, attachment)
				if (file.exists()) {
					String filename = attachment.filename
					['Content-disposition': "${params.containsKey('inline') ? 'inline' : 'attachment'};filename=\"$filename\"",
						'Cache-Control': 'private',
						'Pragma': ''].each {k, v ->
						response.setHeader(k, v)
					}
					response.contentType = attachment.contentType
					try{
						file.withInputStream{fis->
							response.outputStream << fis
						}
					}catch(Exception e){}
					return
				}
			}
		}
	}
		
}
