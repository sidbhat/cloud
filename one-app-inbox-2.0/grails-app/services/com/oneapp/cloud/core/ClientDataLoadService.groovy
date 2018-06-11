/******************************************************************************
 * [ NIKKISHI CONFIDENTIAL ]                                                  *
 *                                                                            *
 *    Copyright (c) 2011.  Nikkishi LLC                                       *
 *    All Rights Reserved.                                                    *
 *                                                                            *
 *   NOTICE:  All information contained herein is, and remains                *
 *   the property of Nikkishi LLC and its suppliers,                          *
 *   if any.  The intellectual and technical concepts contained               *
 *   herein are proprietary to Nikkishi LLC and its                           *
 *   suppliers and may be covered by U.S. and Foreign Patents,                *
 *   patents in process, and are protected by trade secret or copyright law.  *
 *   Dissemination of this information or reproduction of this material       *
 *   is strictly forbidden unless prior written permission is obtained        *
 *   from Nikkishi LLC.                                                       *
 ***************************************************************************** */

package com.oneapp.cloud.core

import com.oneapp.cloud.core.currency.CurrencyMap
import com.oneapp.cloud.core.currency.ExchangeRate
import com.oneapp.cloud.time.Task

import grails.converters.JSON;
import grails.plugin.multitenant.core.util.TenantUtils
import org.grails.plugins.excelimport.AbstractExcelImporter
import org.grails.plugins.excelimport.ExcelImportUtils
import org.grails.formbuilder.*
class ClientDataLoadService {

    static transactional = false
    def calendarService
    def calendarOneAppService
    def springSecurityService
	def domainClassService
	def formTemplateService
	def sqlDomainClassService
    
    def loadWithExcel(String fileName) {
        ExcelImporter importer = new ExcelImporter(fileName);
        def userMapList = importer.getUsers();
        return userMapList
    }

    def loadInitialData(Long tenantID, User loggedInUser) {
        TenantUtils.doWithTenant((Integer) tenantID) {

            def user = loggedInUser
            
            
            /*Create users
            User default_ = new User(
                    username: "admin${tenantID}@yourdomain.com",
                    password: springSecurityService.encodePassword("admin"),
                    enabled: true,
                    userTenantId: tenantID,
                    firstName: "Default",
                    lastName: "Admin",
                    hourlyRate: 200,
                    dob: new Date(),
                    title:"Default Admin",
                    hourlyRateCurr: 'USD'
            ).save(failOnError: true)
            
            new UserRole(user:default_, role: Role.findByAuthority(Role.ROLE_ADMIN)).save()
           
          
            def manager = default_


            ContactDetails per = new ContactDetails(firstName: "Amy", lastName: "Smith", contactName:"Amy Smith", email:"amy.smith@apple.com",birthday:"01/20/1967", mobile:"484-556-7899",title:"CIO",companyName:"Microsoft",createdBy:manager).save(failOnError: true)
			per = new ContactDetails(firstName: "Baine", lastName: "Rudy", contactName:"Baine Rudy", email:"baine.rudy@att.com",birthday:"11/05/1978", mobile:"773-556-4566",title:"CTO",companyName:"Mysoft", createdBy:manager).save(failOnError: true)
			per = new ContactDetails(firstName: "Ram", lastName: "Rudy", contactName:"Ram Rudy", email:"ram.rudy@att.com",birthday:"11/05/1978", mobile:"773-556-4566",title:"CEO",companyName:"Mysoft", createdBy:manager).save(failOnError: true)
		
			
		 // Create task types
            new DropDown(type: DropDownTypes.TASK_TYPE, name: "PER", description: "Personal To Do").save()
            new DropDown(type: DropDownTypes.TASK_TYPE, name: "URG", description: "Urgent").save()
            new DropDown(type: DropDownTypes.TASK_TYPE, name: "FOL", description: "Follow up").save()

     	 // Create Statuses
            new DropDown(type: DropDownTypes.STATUS, name: "OPEN", description: "Open").save()
            new DropDown(type: DropDownTypes.STATUS, name: "CLOSE", description: "Close").save()
            new DropDown(type: DropDownTypes.STATUS, name: "IN_PROCESS", description: "In Process").save()
            new DropDown(type: DropDownTypes.STATUS, name: "SUBMITTED", description: "Submitted").save()
            new DropDown(type: DropDownTypes.STATUS, name: "REJECTED", description: "Rejected").save()
            new DropDown(type: DropDownTypes.STATUS, name: "APPROVED", description: "Approved").save()
			new DropDown(type: DropDownTypes.STATUS, name: "INVOICED", description: "Invoiced").save()
			*/
			
			//=====^^^^^^^^^^^^^^^^^Start copying forms for the clients^^^^^^^^^^^^^^^^^========
			def applicationConf = ApplicationConf.get(1)
			def copyForms = applicationConf.copyForms
			copyForms?.each{Form copyForm->
				Form newForm= new Form()
				
				try{
					newForm.tenantId = tenantID.toInteger()
					 
					 // update the form name
					 def formCnt = new FormCounter().save(flush:true)
					 newForm.name = "Form${formCnt.id}"
				 
					 copyForm.fieldsList.each{Field obj ->
						 Field field = new Field()
						 field.name = obj.name+formCnt.id
						 field.type = obj.type
						def settings = obj.settings
						def jsonSettings = JSON.parse(obj.settings)
						jsonSettings.mapMasterForm = ""
						jsonSettings.mapMasterField = ""
						if(obj.type == 'SubForm'){
							if(jsonSettings.subForm){
								Form subForm = Form.read(jsonSettings.subForm)
								if(subForm){
									try{
										Form newSubForm = new Form()
										newSubForm.tenantId = tenantID.toInteger()
										def subFormCnt = new FormCounter().save(flush:true)
										newSubForm.name = "Form${subFormCnt.id}"
										subForm.fieldsList.each{Field subField->
											Field newSubField = new Field()
											newSubField.name = subField.name+subFormCnt.id
											newSubField.type = subField.type
											def subSettings = subField.settings
											def jsonSubSettings = JSON.parse(subSettings)
											jsonSubSettings.mapMasterForm = ""
											jsonSubSettings.mapMasterField = ""
											newSubField.settings = jsonSubSettings.toString()
											newSubField.sequence = subField.sequence
											newSubForm.fieldsList.add(newSubField)
										}
										newSubForm.description = subForm.description
										def jsonSubFormSettings = JSON.parse(subForm.settings)
										jsonSubFormSettings.masterForms = []
										newSubForm.settings = jsonSubFormSettings.toString()
										newSubForm.domainClass = domainClassService.getDomainClass(newSubForm)
										newSubForm.persistableFieldsCount = newSubForm.fieldsList.size()
										newSubForm.createdBy = springSecurityService?.currentUser
										newSubForm.formCat = subForm.formCat
										newSubForm.save(flush:true)
										try{
											sqlDomainClassService.createForm(newSubForm)
										}catch(Exception e){}
										jsonSettings.subForm = "${newSubForm.id}"
									}catch(Exception e){
										println "Problem creating subForm"
									}
								}else{
									jsonSettings.subForm = ""
								}
							}
						}
						field.settings = jsonSettings.toString()
						 field.sequence = obj.sequence
						 newForm.fieldsList.add(field)
						 
					 }
					 
					 newForm.description = copyForm.description
					 def jsonFormSettings = JSON.parse(copyForm.settings)
					 jsonFormSettings.masterForms = []
					 newForm.settings=jsonFormSettings.toString()
					 newForm.formCat=copyForm.formCat
					 newForm.domainClass = domainClassService.getDomainClass(newForm)
					 newForm.persistableFieldsCount = formTemplateService.getPersistableFieldsCount(newForm.fieldsList)
				 //    domainClassService.registerDomainClass newForm.domainClass.source
					 newForm.createdBy = springSecurityService?.currentUser
					 newForm.save(flush:true)
					 try{
						 sqlDomainClassService.createForm(newForm)
					 }catch(Exception e){}
					 def formAdmin = FormAdmin.findByForm(copyForm)
					 if(formAdmin){
						 def newFormAdmin = new FormAdmin()
						 newFormAdmin.properties = formAdmin.properties
						 newFormAdmin.dateCreated = new Date()
						 newFormAdmin.lastUpdated = null
						 newFormAdmin.statusField = newForm.fieldsList.find{it.name == ("${formAdmin.statusField?.name}"+(formCnt.id))}
						 newFormAdmin.blockUserEditing = []
						 formAdmin.blockUserEditing.each{
							 newFormAdmin.blockUserEditing.add(it.toString())
						 }
						 newFormAdmin.form = newForm
						 newFormAdmin.save(flush:true)
					 }
					 def rules = Rule.executeQuery("select r from Rule r where r.className = "+copyForm.id+" and r.ruleSet._action = '"+RuleSet.SUBSCRIBE+"'")
					 if(rules){
						 def rulesetRules = rules.groupBy{it.ruleSet}
						 rulesetRules.each{RuleSet k,List<Rule> v->
							 def ruleSetInstance = new RuleSet()
							 ruleSetInstance.resultClass = k.resultClass
							 ruleSetInstance.resultInstance = k.resultInstance
							 ruleSetInstance._order = k._order
							 ruleSetInstance._action = k._action
							 ruleSetInstance.createdBy = null
							 ruleSetInstance.dateCreated = new Date()
							 ruleSetInstance.lastUpdated = new Date()
							 ruleSetInstance.name = k.name
							 ruleSetInstance.status = k.status
							 if(!ruleSetInstance.hasErrors() && ruleSetInstance.save(flush:true)){
								 v.each{Rule r->
									 def ruleInstance = new Rule()
									 ruleInstance.ruleSet = ruleSetInstance
									 ruleInstance._order = r._order
									 ruleInstance.status = r.status
									 ruleInstance._condition = r._condition
									 ruleInstance.operator = r.operator
									 ruleInstance.className = newForm.id.toString()
									 def field = newForm.fieldsList.find{it.name == Field.get(r.attributeName)?.name}
									 ruleInstance.attributeName = field?.id
									 ruleInstance.value = r.value
									 ruleInstance.save(flush:true)
								 }
							 }
						 }
					 }
				}catch(Exception e){
					log.error e
				}
			}
			//=====_________________End copying forms for the clients___________________========

         // Create user type
            new DropDown(type: DropDownTypes.EMPLOYEE_TYPE, name: "INTERNAL", description: "Internal").save()
            new DropDown(type: DropDownTypes.EMPLOYEE_TYPE, name: "EXTERNAL", description: "External").save()


            new DropDown(type: DropDownTypes.UPDATE_TYPE, name: "ADDED", description: "Added").save()
            new DropDown(type: DropDownTypes.UPDATE_TYPE, name: "DELETED", description: "Deleted").save()
            new DropDown(type: DropDownTypes.UPDATE_TYPE, name: "UPDATED", description: "Updated").save()
            new DropDown(type: DropDownTypes.UPDATE_TYPE, name: "DATE", description: "Date").save()


            new DropDown(type: DropDownTypes.SHARE_TYPE, name: "ALL", description: "All").save()
            new DropDown(type: DropDownTypes.SHARE_TYPE, name: "GROUP", description: "Group").save()
            new DropDown(type: DropDownTypes.SHARE_TYPE, name: "PRIVATE", description: "Private").save()

			new DropDown(type: DropDownTypes.GROUP_TYPE, name: "COMPANY", description: "Company").save()
			
			new DropDown(type: DropDownTypes.DEPARTMENT, name: "HR", description: "HR").save()
			new DropDown(type: DropDownTypes.DEPARTMENT, name: "Sales", description: "Sales").save()
			new DropDown(type: DropDownTypes.DEPARTMENT, name: "Finance", description: "Finance").save()
			new DropDown(type: DropDownTypes.DEPARTMENT, name: "Accounting", description: "Accounting").save()
			
			new DropDown(type: DropDownTypes.SUB_ORG, name: "NA Sales", description: "NA Sales").save()
			new DropDown(type: DropDownTypes.SUB_ORG, name: "NA Field Services", description: "NA Field Services").save()
			
 			GroupDetails g = new GroupDetails (
        			groupName:"Sales ",
        			groupDescription:"Sales Group",
        			createdOn:new Date(),
        			groupType:DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.GROUP_TYPE, "COMPANY"),
        			personal:false )
      		  g.save(failOnError:true)
      		
      		g = new GroupDetails (
        			groupName:"HR ",
        			groupDescription:"Human Resources",
        			createdOn:new Date(),
        			groupType:DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.GROUP_TYPE, "COMPANY"),
        			personal:false )
      		 g.save(failOnError:true)
      		 
      		 g = new GroupDetails (
        			groupName:"Accounting ",
        			groupDescription:"Accounting",
        			createdOn:new Date(),
        			groupType:DropDown.findByTypeAndName(com.oneapp.cloud.core.DropDownTypes.GROUP_TYPE, "COMPANY"),
        			personal:false )
      		 g.save(failOnError:true)
        
        

        }
	
		

}
	
	def loadInitialDataForTrialUser(User trialUser){
		
		def applicationConf = ApplicationConf.get(1)
		def copyForms = applicationConf.copyFormsTrial
		copyForms?.each{Form copyForm->
			Form newForm= new Form()
			
			try{
				newForm.tenantId = trialUser.userTenantId
				 
				 // update the form name
				 def formCnt = new FormCounter().save(flush:true)
				 newForm.name = "Form${formCnt.id}"
			 
				 copyForm.fieldsList.each{Field obj ->
					 Field field = new Field()
					 field.name = obj.name+formCnt.id
					 field.type = obj.type
					def settings = obj.settings
					def jsonSettings = JSON.parse(obj.settings)
					jsonSettings.mapMasterForm = ""
					jsonSettings.mapMasterField = ""
					if(obj.type == 'SubForm'){
						if(jsonSettings.subForm){
							Form subForm = Form.read(jsonSettings.subForm)
							if(subForm){
								try{
									Form newSubForm = new Form()
									newSubForm.tenantId = trialUser.userTenantId
									def subFormCnt = new FormCounter().save(flush:true)
									newSubForm.name = "Form${subFormCnt.id}"
									subForm.fieldsList.each{Field subField->
										Field newSubField = new Field()
										newSubField.name = subField.name+subFormCnt.id
										newSubField.type = subField.type
										def subSettings = subField.settings
										def jsonSubSettings = JSON.parse(subSettings)
										jsonSubSettings.mapMasterForm = ""
										jsonSubSettings.mapMasterField = ""
										newSubField.settings = jsonSubSettings.toString()
										newSubField.sequence = subField.sequence
										newSubForm.fieldsList.add(newSubField)
									}
									newSubForm.description = subForm.description
									def jsonSubFormSettings = JSON.parse(subForm.settings)
									jsonSubFormSettings.masterForms = []
									newSubForm.settings = jsonSubFormSettings.toString()
									newSubForm.domainClass = domainClassService.getDomainClass(newSubForm)
									newSubForm.persistableFieldsCount = newSubForm.fieldsList.size()
									newSubForm.createdBy = trialUser
									newSubForm.formCat = subForm.formCat
									newSubForm.save(flush:true)
									try{
										sqlDomainClassService.createForm(newSubForm)
									}catch(Exception e){}
									jsonSettings.subForm = "${newSubForm.id}"
								}catch(Exception e){
									println "Problem creating subForm"
								}
							}else{
								jsonSettings.subForm = ""
							}
						}
					}
					field.settings = jsonSettings.toString()
					 field.sequence = obj.sequence
					 newForm.fieldsList.add(field)
					 
				 }
				 
				 newForm.description = copyForm.description
				 def jsonFormSettings = JSON.parse(copyForm.settings)
				 jsonFormSettings.masterForms = []
				 newForm.settings=jsonFormSettings.toString()
				 newForm.formCat=copyForm.formCat
				 newForm.domainClass = domainClassService.getDomainClass(newForm)
				 newForm.persistableFieldsCount = formTemplateService.getPersistableFieldsCount(newForm.fieldsList)
			 //    domainClassService.registerDomainClass newForm.domainClass.source
				 newForm.createdBy = trialUser
				 newForm.save(flush:true)
				 try{
					 sqlDomainClassService.createForm(newForm)
				 }catch(Exception e){}
				 def formAdmin = FormAdmin.findByForm(copyForm)
				 if(formAdmin){
					 def newFormAdmin = new FormAdmin()
					 newFormAdmin.properties = formAdmin.properties
					 newFormAdmin.conditions = []
					 newFormAdmin.formSubmissionTo = []
					 newFormAdmin.publishedWith = []
					 newFormAdmin.dateCreated = new Date()
					 newFormAdmin.lastUpdated = null
					 newFormAdmin.statusField = newForm.fieldsList.find{it.name == ("${formAdmin.statusField?.name}"+(formCnt.id))}
					 newFormAdmin.blockUserEditing = []
					 formAdmin.blockUserEditing.each{
						 newFormAdmin.blockUserEditing.add(it.toString())
					 }
					 newFormAdmin.form = newForm
					 newFormAdmin.save(flush:true)
				 }
			}catch(Exception e){
				log.error e
			}
		}
	}


    class ExcelImporter extends AbstractExcelImporter {
        static Map CONFIG_USER_COLUMN_MAP = [
                sheet: 'Sheet1',
                startRow: 2,
                columnMap: [
                        'B': 'username',
                        'C': 'firstName',
                        'D': 'lastName',
                ]
        ]

        static Map CONFIG_TASK_COLUMN_MAP = [
                sheet: 'Sheet2',
                startRow: 2,
                columnMap: [
                        'B': 'name',
                        'C': 'description',
                        'D': 'personal',
                ]
        ]

        static Map propertyConfigurationMap = [
                firstName: ([expectedType: ExcelImportUtils.PROPERTY_TYPE_STRING, defaultValue: null]),
                lastName: ([expectedType: ExcelImportUtils.PROPERTY_TYPE_STRING, defaultValue: null]),
                name: ([expectedType: ExcelImportUtils.PROPERTY_TYPE_STRING, defaultValue: null]),
                description: ([expectedType: ExcelImportUtils.PROPERTY_TYPE_STRING, defaultValue: null]),
                personal: ([expectedType: ExcelImportUtils.PROPERTY_TYPE_STRING, defaultValue: true])
        ]

        public ExcelImporter(fileName) {
            super(fileName)
        }

        List<Map> getUsers() {
            List userList = ExcelImportUtils.convertColumnMapConfigManyRows(workbook, CONFIG_USER_COLUMN_MAP)
        }

        List<Map> getTasks() {
            List taskList = ExcelImportUtils.convertColumnMapConfigManyRows(workbook, CONFIG_TASK_COLUMN_MAP)
        }
    }


}
