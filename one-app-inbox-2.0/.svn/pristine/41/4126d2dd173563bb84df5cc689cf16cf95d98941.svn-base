/* Copyright 2010 Mihai Cazacu
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package com.macrobit.grails.plugins.attachmentable.controllers

import org.codehaus.groovy.grails.commons.ConfigurationHolder as CH
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;

import com.macrobit.grails.plugins.attachmentable.domains.Attachment
import com.macrobit.grails.plugins.attachmentable.domains.AttachmentLink
import com.macrobit.grails.plugins.attachmentable.util.AttachmentableUtil
import com.oneapp.cloud.core.Role;
import com.oneapp.cloud.core.User;

import grails.orm.PagedResultList
import javax.servlet.http.HttpServletResponse

class AttachmentableController {

    def attachmentableService
	def springSecurityService

    // download

    def download = {
        Attachment attachment = Attachment.get(params.id.toLong())
		def allowedDownload = false
		try{
			if(params.apiKey){
				session['user']=User.findByApiKey(params.apiKey)
			}else{
			    session['user'] =springSecurityService.currentUser
			}
		}catch(Exception ex){
		}
		if(attachment){ 
			if(attachment.posterId == session['user'].id){
				allowedDownload=true
			}else if(attachment.lnk.referenceClass=="org.grails.formbuilder.Form"){
				def form = attachment.lnk.reference
				if(form){
					if(form.createdBy.id == session['user'].id){
						allowedDownload=true
					}else{
						def currentUserAuthorities = session['user'].authorities
						def reference
						if(currentUserAuthorities*.authority.contains("ROLE_USER")||currentUserAuthorities*.authority.contains("ROLE_TRIAL_USER")){
							reference = getClass().classLoader.loadClass("com.oneapp.cloud.core.ActivityFeed").withCriteria(){
								'config'{
									eq "className",(form.name+"."+form.name)
								}
								eq "shareId",attachment.domainInstanceId
								or{
									'createdBy'{
										eq "id",session['user'].id
									}
									eq "visibility","Company"
									'sharedGroups'{
										'user'{
											eq "id",session['user'].id
										}
									}
									'sharedUsers'{
										eq "id",session['user'].id
									}
									'sharedRoles'{
										eq "id",currentUserAuthorities*.id.get(0)
									}
									if(session['user'].department){
										'sharedDepts'{
											eq "id",session['user'].department.id
										}
									}
								}
								projections{
									distinct("id")
								}
							}
						}else if(session['user'].userTenantId==form.tenantId){
								reference=true
						}
						if(reference){
							allowedDownload=true
						}
					}
				}
			}else if(attachment.lnk.referenceClass=="com.oneapp.cloud.core.ActivityFeed"){
				def reference = getClass().classLoader.loadClass(attachment.lnk.referenceClass).withCriteria(){
					eq 'id',attachment.lnk.referenceId
					or{
						'createdBy'{
							eq "id",session['user'].id
						}
						eq "visibility","Company"
						'sharedGroups'{
							'user'{
								eq "id",session['user'].id
							}
						}
						'sharedUsers'{
							eq "id",session['user'].id
						}
						'sharedRoles'{
							eq "id",session['user'].authorities*.id.get(0)
						}
						if(session['user'].department){
							'sharedDepts'{
								eq "id",session['user'].department.id
							}
						}
					}
					projections{
						distinct("id")
					}
				}
				if(reference){
					allowedDownload=true
				}
			}
		}
        if (allowedDownload) {
            File file = AttachmentableUtil.getFile(CH.config, attachment)

            if (file.exists()) {
                String filename = attachment.filename /*.replaceAll(/\s/, '_')*/

                ['Content-disposition': "${params.containsKey('inline') ? 'inline' : 'attachment'};filename=\"$filename\"",
                    'Cache-Control': 'private',
                    'Pragma': ''].each {k, v ->
                    response.setHeader(k, v)
                }

                if (params.containsKey('withContentType')) {
                    response.contentType = attachment.contentType                    
                } else {
                    response.contentType = 'application/octet-stream'
                }
				try{
	                file.withInputStream{fis->
	                    response.outputStream << fis
	                }
				}catch(Exception e){}

                // response.contentLength = file.length()
                // response.outputStream << file.readBytes()
                // response.outputStream.flush()
                // response.outputStream.close()
                return
            }
        }

        response.status = HttpServletResponse.SC_NOT_FOUND
    }

    def renderImg = {
		Attachment attachment
		attachment = Attachment.get(params.id.toLong())
		def allowedDownload = false
		if(attachment){
			if(attachment.posterId == session['user'].id){
				allowedDownload=true
			}else if(attachment.lnk.referenceClass=="org.grails.formbuilder.Form"){
				def form = attachment.lnk.reference
				if(form){
					if(form.createdBy.id == session['user'].id){
						allowedDownload=true
					}else{
						def currentUserAuthorities = session['user'].authorities
						def reference
						if(currentUserAuthorities*.authority.contains("ROLE_USER")||currentUserAuthorities*.authority.contains("ROLE_TRIAL_USER")){
							reference = getClass().classLoader.loadClass("com.oneapp.cloud.core.ActivityFeed").withCriteria(){
								'config'{
									eq "className",(form.name+"."+form.name)
								}
								eq "shareId",attachment.domainInstanceId
								or{
									'createdBy'{
										eq "id",session['user'].id
									}
									eq "visibility","Company"
									'sharedGroups'{
										'user'{
											eq "id",session['user'].id
										}
									}
									'sharedUsers'{
										eq "id",session['user'].id
									}
									'sharedRoles'{
										eq "id",currentUserAuthorities*.id.get(0)
									}
									if(session['user'].department){
										'sharedDepts'{
											eq "id",session['user'].department.id
										}
									}
								}
								projections{
									distinct("id")
								}
							}
						}else if(session['user'].userTenantId==form.tenantId){
								reference=true
						}
						if(reference){
							allowedDownload=true
						}
					}
				}
			}else if(attachment.lnk.referenceClass=="com.oneapp.cloud.core.ActivityFeed"){
				def reference = getClass().classLoader.loadClass(attachment.lnk.referenceClass).withCriteria(){
					eq 'id',attachment.lnk.referenceId
					or{
						'createdBy'{
							eq "id",session['user'].id
						}
						eq "visibility","Company"
						'sharedGroups'{
							'user'{
								eq "id",session['user'].id
							}
						}
						'sharedUsers'{
							eq "id",session['user'].id
						}
						'sharedRoles'{
							eq "id",session['user'].authorities*.id.get(0)
						}
						if(session['user'].department){
							'sharedDepts'{
								eq "id",session['user'].department.id
							}
						}
					}
					projections{
						distinct("id")
					}
				}
				if(reference){
					allowedDownload=true
				}
			}
		}
        if (allowedDownload) {
            File file = AttachmentableUtil.getFile(CH.config, attachment)

            if (file.exists()) {
                String filename = attachment.filename /*.replaceAll(/\s/, '_')*/

                ['Content-disposition': "${params.containsKey('inline') ? 'inline' : 'attachment'};filename=\"$filename\"",
                    'Cache-Control': 'private',
                    'Pragma': ''].each {k, v ->
                    response.setHeader(k, v)
                }

                if (params.containsKey('withContentType')) {
                    response.contentType = attachment.contentType                    
                } else {
                    response.contentType = 'application/octet-stream'
                }
				try{
	                file?.withInputStream{fis->
	                    response.outputStream << fis
	                }
				}catch(Exception e){}

                // response.contentLength = file.length()
                // response.outputStream << file.readBytes()
                // response.outputStream.flush()
                // response.outputStream.close()
                return
            }
        }

        response.status = HttpServletResponse.SC_NOT_FOUND
    }

    def show = {
        // Default show action is to display the attachment inline in the browser.
        if (!params.containsKey('inline')) {
            params.inline = ''
        }
        if (!params.containsKey('withContentType')) {
            params.withContentType = ''
        }
        forward(action:'download', params:params)
    }

    // upload

    def upload = {
        AttachmentLink lnk = new AttachmentLink(params.attachmentLink)

        attachUploadedFilesTo(lnk.reference)

        render 'success'
    }

    def uploadInfo = {
        uploadStatus()
    }

    // delete

    def delete = {
		def result = false
		Attachment attachment = Attachment.get(params.id.toLong())
		def allowedDelete = false
		def attRef = attachment?.lnk?.reference
		if(attachment){
			if(attachment.posterId == session['user'].id){
				allowedDelete=true
			}else if(attRef?.tenantId == session['user'].userTenantId && SpringSecurityUtils.ifAnyGranted(Role.ROLE_ADMIN)){
				allowedDelete=true
			}else if(attachment.lnk.referenceClass=="org.grails.formbuilder.Form"){
				def form = attRef
				if(form){
					if(form.createdBy.id == session['user'].id){
						allowedDelete=true
					}
				}
			}else if(attachment.lnk.referenceClass=="com.oneapp.cloud.core.ActivityFeed"){
				def reference = getClass().classLoader.loadClass(attachment.lnk.referenceClass).withCriteria(){
					eq 'id',attachment.lnk.referenceId
					'createdBy'{
						eq "id",session['user'].id
					}
				}
				if(reference){
					allowedDelete=true
				}
			}else if(SpringSecurityUtils.ifAnyGranted(Role.ROLE_SUPER_ADMIN)){
				allowedDelete=true
			}
		}
		if (allowedDelete) {
			result = attachmentableService.removeAttachment(attachment)
		}else{
			flash.message = "attachment.delete.notAllowed"
			flash.defaultMessage = "Sorry! You can not delete this attachment"
		}
		
		if(result){
			flash.message = "attachment.deleted"
			flash.defaultMessage = "Attachment deleted successfully"
		}

        if (params.returnPageURI) {
            redirect url: params.returnPageURI
        } else {
            render (result > 0 ? 'success' : 'failed')
        }
    }

}
