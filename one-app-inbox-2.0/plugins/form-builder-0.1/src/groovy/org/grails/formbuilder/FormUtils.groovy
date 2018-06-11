
package org.grails.formbuilder

import grails.converters.JSON;
import groovy.sql.Sql;

import java.text.SimpleDateFormat;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;

import com.oneapp.cloud.core.ActivityFeed;
import com.oneapp.cloud.core.Role;
import com.oneapp.cloud.core.User;

/**
*
* @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
*
* @since 0.1
*/
class FormUtils {
	public static boolean isFormAccessible(Form form,def currentUser,def action,def domainInstanceId = null){
		boolean accessible = false
		try{
			def isAdmin = SpringSecurityUtils.ifAnyGranted(Role.ROLE_HR_MANAGER)
			if(isAdmin){
				accessible = true
			}else if(form){
				def formAdmin = FormAdmin.findByForm(form)
				if(formAdmin?.formLogin == 'Login'){
					if(formAdmin.publishedWith && formAdmin.publishedWith*.id.contains(currentUser.id)){
						accessible = true
					}
				}else if(formAdmin?.formLogin == 'Public' || formAdmin?.formLogin == 'Password'){
					if(formAdmin.openForEdit && (action == 'edit' || action == 'update')){
						accessible = true
					}else if(formAdmin.formLogin!='NoOne' && (action == 'create' || action == 'save')){
						accessible = true
					}
				}
				if(!accessible && currentUser && currentUser.userTenantId!=0 && (action == 'edit' || action == 'update')){
					def reference = ActivityFeed.withCriteria(){
						'config'{
							eq "className",(form.name+"."+form.name)
						}
						eq "shareId",domainInstanceId
						or{
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
							'createdBy'{
								eq "id",currentUser.id
							}
						}
					}
					if(reference){
						accessible = true
					}
				}
			}
		}catch(Exception e){
			println "error in FormUtils: "+e
		}
		return accessible
	}
	
	public static def getLookUpResults(def params, def currentUser, def dataSource, SimpleDateFormat dateFormatter){
		def sql
		def status = 'fail'
		def listResult = []
		def statusMessage = 'Could not access the data'
		try{
			sql = new Sql(dataSource)
			Form form = Form.read(params.formId)
			Field field = Field.read(params.field)
			def fieldsList = form.fieldsList
			if(fieldsList.find{it.id == field.id}){
				def settings = JSON.parse(field.settings)
				if(settings.mapMasterForm && settings.mapMasterField){
					if(settings.mapMasterForm=='user'){
						if(currentUser){
							def allUsers = User.createCriteria().list(){
								or{
									settings.mapMasterField?.each{userField->
										'ilike'("${userField}","%${params.term}%")
									}
								}
								eq 'userTenantId',currentUser.userTenantId
							}
							
							def usersWithUserRole = allUsers.findAll{it.authorities*.authority?.contains(Role.ROLE_USER)}
							if(usersWithUserRole){
								listResult = usersWithUserRole.collect{user->
									def label = ''
									settings.mapMasterField?.each{userField->
										label += ((user[userField]?:'')+' ')
									}
									def wholeObj = [username:user.username,firstName:user.firstName,lastName:user.lastName]
									[value:user.username,label:label,wholeObj:wholeObj]
								}
							}
						}
					}else{
						Form masterForm = Form.read(settings.mapMasterForm)
						def masterFields = masterForm?.fieldsList?.findAll{settings.mapMasterField.contains(it.name)}
						if(masterFields){
							def whereClause = ""
							def domainInstanceList
							if(params.term){
								masterFields.each{
									if(whereClause != "")
										whereClause += " or"
									whereClause += (" "+it.name+" like \'%"+params.term+"%\'")
								}
								domainInstanceList = sql.rows("select * from ${masterForm.name.toLowerCase()} where "+whereClause+" order by "+masterFields.get(0).name)
							}else{
								domainInstanceList = sql.rows("select * from ${masterForm.name.toLowerCase()} order by "+masterFields.get(0).name)
								if(domainInstanceList)
									domainInstanceList = domainInstanceList.subList(0,domainInstanceList.size()>29?30:(domainInstanceList.size()))
							}
							listResult = domainInstanceList.collect{domainInstance ->
								def description = ""
								masterFields.each{
									if(description != "")   
										 description += " "
										if(it.type=="AddressField" && domainInstance."${it.name}"){
											def mapValue = grails.converters.JSON.parse(domainInstance."${it.name}")
											description+=mapValue."line1"?mapValue."line1"+" ;":""
											description+=mapValue."line2"?mapValue."line2"+" ;":""
											description+=mapValue."city"?mapValue."city"+" ;":""
											description+=mapValue."state"?mapValue."state"+" ;":""
											description+=mapValue."zip"?mapValue."zip"+" ;":""
											description+=mapValue."country"?mapValue."country"+" ;":""
										}else if(it.type=="NameTypeField" && domainInstance."${it.name}"){
											def mapValue = grails.converters.JSON.parse(domainInstance."${it.name}")
											description+=mapValue."pre"?mapValue."pre"+" ":""
											description+=mapValue."fn"?mapValue."fn"+" ":""
											description+=mapValue."mn"?mapValue."mn"+" ":""
											description+=mapValue."ln"?mapValue."ln"+" ":""
										}else{
											description += domainInstance."${it.name}"
											}
								}
								def wholeObj = [:]
								wholeObj."domid"=domainInstance."id"
								masterForm.fieldsList.each{f->
									try{
										def fieldValue = domainInstance.getAt(f.name)
										if(fieldValue?.class?.name == "java.sql.Timestamp"){
											wholeObj."${f.name}" = dateFormatter.format(fieldValue)
										}else{
											if(f.type =="AddressField" || f.type=="NameTypeField"){
												if(fieldValue)
												wholeObj."${f.name}" = JSON.parse(fieldValue)
											}else{
													wholeObj."${f.name}" = fieldValue
											}
										}
									}catch(Exception e){}
								}
								[value:description,label:description,wholeObj:wholeObj]
							}
						}
					}
				}
			}
		}catch(Exception e){
			statusMessage = 'Sorry an error occurred. Please try again'
		}
		return listResult
	}
}
