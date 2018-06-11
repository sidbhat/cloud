package com.oneapp.cloud.core

import grails.plugin.multitenant.core.util.TenantUtils;

import java.awt.List;
import java.util.*;
import java.text.*;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;


class RuleController {
	
	def springSecurityService
	def fieldsForRule = ConfigurationHolder.config.fields.forRule
	
	def index = { redirect(controller:"ruleSet",action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	

    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
        [ruleInstanceList: Rule.list(params), ruleInstanceTotal: Rule.count()]
    }
    
    def fieldValue = {
    	//def field = org.grails.formbuilder.Field.get(params.id)
		//println field
		
		//if ( field.type == "dropdown" )
		//	render g.select(from:field.clazz?.toList(), id: "value", name: "value", noSelection: ['': 'Select One...']) 
		//else
		//	render g.textField (id: "value", name: "value")
    }
  
	def fields = { 	 
		 //println params.id
		def w
		def prop = []
		try{
		 org.grails.formbuilder.Form form = org.grails.formbuilder.Form.get(params.id)
		 prop = org.grails.formbuilder.Field.createCriteria().list(){
			 eq 'form.id',params.id.toLong()
			 'in' 'type',fieldsForRule
		 }
		 if(prop && prop.get(0).form.tenantId != TenantUtils.getCurrentTenant()){
			 prop = []
		 }
		}catch(Exception e){
		
		}
		w = "${g:select(optionKey: 'id', from:prop, id: "attributeName", name: "attributeName", noSelection: ['': 'Select One...'])}"
		render w
	}
    def create = {
        def ruleInstance = new Rule()
		def user = springSecurityService.currentUser
		def multipleRule = false
		ArrayList<org.grails.formbuilder.Form> formInstance = new ArrayList<org.grails.formbuilder.Form>()
		ArrayList<com.oneapp.cloud.core.EmailSettings> emailAccountList = new ArrayList<com.oneapp.cloud.core.EmailSettings>()
		try{
			if ( params.id ){
				ruleInstance.ruleSet = RuleSet.get(params.id)
				ruleInstance.properties = params
				ruleInstance.status = "Active"
				ruleInstance._condition = "AND"
				def ruleSetForRule = Rule.findAllByRuleSet(ruleInstance.ruleSet)
				if(ruleSetForRule.size() > 0)
					multipleRule = true
				if(ruleInstance.ruleSet._action.equalsIgnoreCase("Fetch Email")){
					if(ruleSetForRule){
						if(com.oneapp.cloud.core.EmailSettings.get(ruleSetForRule[0].className))
							emailAccountList.add(com.oneapp.cloud.core.EmailSettings.get(ruleSetForRule[0].className))
						else
							throw new Exception("Email Account not found")
					}else{
						emailAccountList = EmailSettings.findAllByUser(user)
					}
				}else{
					if(ruleSetForRule)
						formInstance.add(org.grails.formbuilder.Form.get(ruleSetForRule[0].className))
					else{
						def formAdminInstance
						formAdminInstance = org.grails.formbuilder.FormAdmin.withCriteria(){
							eq 'published',true
							form{
								eq 'tenantId',user.userTenantId
								not{
									eq 'formCat', 'S'
								}
							}
						}
						formInstance = formAdminInstance.form
					}
				}
			}else{
				 flash.message = "ruleset.not.provided"
				flash.defaultMessage = "Specifiy a ruleset for the rule"
			}
			render(view: "create", model: [ruleInstance: ruleInstance,formInstance: formInstance,emailAccountList:emailAccountList,multipleRule:multipleRule])
		}catch(Exception ex){
			flash.message = "Error Occured "+ex.message
			flash.defaultMessage = flash.message
			redirect(controller: "ruleSet",action: "edit", id: params.id)
		}
    }

    def save = {
		def ruleErrorInstance = new Rule()
		def user = springSecurityService.currentUser
		def multipleRule = false
		ArrayList<org.grails.formbuilder.Form> formInstance = new ArrayList<org.grails.formbuilder.Form>()
		ArrayList<com.oneapp.cloud.core.EmailSettings> emailAccountList = new ArrayList<com.oneapp.cloud.core.EmailSettings>()
		if ( params.ruleset.id ){
			ruleErrorInstance.ruleSet = RuleSet.get(params.ruleset.id)
			ruleErrorInstance.properties = params
			ruleErrorInstance.status = "Active"
			ruleErrorInstance._condition = "AND"
			def ruleSetForRule = Rule.findAllByRuleSet(ruleErrorInstance.ruleSet)
			if(ruleSetForRule.size() > 0)
				multipleRule = true
			if(ruleErrorInstance.ruleSet._action.equalsIgnoreCase("Fetch Email")){
				if(ruleSetForRule)
					emailAccountList.add(com.oneapp.cloud.core.EmailSettings.get(ruleSetForRule[0].className))
				else{
					emailAccountList = EmailSettings.findAllByUser(user)
				}
			}else{
				if(ruleSetForRule)
					formInstance.add(org.grails.formbuilder.Form.get(ruleSetForRule[0].className))
				else{
					def formAdminInstance
						formAdminInstance = org.grails.formbuilder.FormAdmin.withCriteria(){
							eq 'published',true
							form{
								eq 'tenantId',user.userTenantId
								not{
									eq 'formCat', 'S'
								}
							}
						}
						formInstance = formAdminInstance.form
				}
			}
		}
		if(params.attributeName)
		{
			if(ruleErrorInstance.ruleSet._action.equalsIgnoreCase("Fetch Email")){
				
			}else{
				if(params.value.indexOf("@now") > -1){
					
				}else{
					String format = message(code:'format.date',args:[],defaultMessage:'MM/dd/yyyy')
					SimpleDateFormat dateformat = new SimpleDateFormat(format)
					def prop = org.grails.formbuilder.Field.get(Long.parseLong(params.attributeName))
					if("SingleLineDate".equalsIgnoreCase(prop.type)){
						Date date ;
						try{
							 date = dateformat.parse(params.value);
						}catch(Exception pex){
							log.error pex
							ruleErrorInstance.errors.rejectValue("value", "rule.optimistic.locking.failure", "Date should be in ${format} format")
							 render(view: "create", model: [ruleInstance: ruleErrorInstance,formInstance: formInstance,emailAccountList:emailAccountList])
							 return
						}
					}
				}
			}
		}
		def ruleInstance = new Rule(params)
		try{
	       
	        ruleInstance.ruleSet = RuleSet.get(params.ruleset.id)
	       
	        if ( params._order )
	       	 ruleInstance._order= Integer.parseInt(params._order.replaceAll(",",""))
	        if ( params._condition)
	       	 ruleInstance._condition= params._condition
			
	        if (!ruleInstance.hasErrors() && ruleInstance.save(flush:true)) {
				if(ruleErrorInstance.ruleSet._action.equalsIgnoreCase("Fetch Email") && !multipleRule){
					flash.message = "rule.created"
					flash.args = [ruleInstance.id]
					flash.defaultMessage = "Rule ${ruleInstance.id} created to fetch email of last 7 days.You can increase the duration upto 180 days by creating new rule with AND condition on Email field [days prior]"
				}else{
					flash.message = "rule.created"
					flash.args = [ruleInstance.id]
					flash.defaultMessage = "Rule ${ruleInstance.id} created."
				}
	            redirect(controller: "ruleSet",action: "edit", id: ruleInstance.ruleSet.id)
	        }
	        else {
	            render(view: "create", model: [ruleInstance: ruleInstance,formInstance: formInstance,emailAccountList:emailAccountList,multipleRule:multipleRule])
	        }
		}catch(Exception ex){
			log.error ex
				flash.defaultMessage = ex.message;
			    render(view: "create", model: [ruleInstance: ruleInstance,formInstance: formInstance,emailAccountList:emailAccountList,multipleRule:multipleRule])
	     }
    }

 
    def edit = {
        def ruleInstance = Rule.read(params.id)
        if (!ruleInstance) {
            flash.message = "rule.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Rule not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
			try{
				def emailAccountList = new ArrayList()
	        	def form
	        	def fields
				if(ruleInstance.ruleSet._action.equalsIgnoreCase("Fetch Email")){
					if(com.oneapp.cloud.core.EmailSettings.get(ruleInstance.className))
						emailAccountList.add(com.oneapp.cloud.core.EmailSettings.get(ruleInstance.className))
					else
						throw new Exception("Email Account not found")
				}else{
		        	if ( ruleInstance?.className ) {
		        		form = org.grails.formbuilder.Form.get(Long.parseLong(ruleInstance?.className) )
						def prop = org.grails.formbuilder.Field.get(Long.parseLong(ruleInstance?.attributeName))
							if("SingleLineDate".equalsIgnoreCase(prop.type)){
								if(ruleInstance?.value.indexOf("@now") > -1){
						
								}else{
		//							 Date date ;
		//							 date = dateformat2.parse( ruleInstance?.value);
		//							 ruleInstance.value = new StringBuilder( dateformat1.format( date ) );
								}
							}
		        		fields = form?.fieldsList.findAll{fieldsForRule.contains(it.type)}
		        	}
				}
	        	
	        	return [ruleInstance: ruleInstance,form:form,fields:fields,emailAccountList:emailAccountList]
	        }catch(Exception ex){
				flash.message = "Error Occured "+ex.message
				flash.defaultMessage = flash.message
				redirect(controller: "ruleSet",action: "edit", id: ruleInstance.ruleSet.id)
	        }
        }
    }

    def update = {
		
	        def ruleInstance = Rule.get(params.id)
	        if (ruleInstance) {
	            if (params.version) {
	                def version = params.version.toLong()
	                if (ruleInstance.version > version) {
	                    ruleInstance.errors.rejectValue("version", "rule.optimistic.locking.failure", "Another user has updated this Rule while you were editing")
	                    render(view: "edit", model: [ruleInstance: ruleInstance])
	                    return
	                }
	            }
				if(params.attributeName)
				{
					if(ruleInstance.ruleSet._action.equalsIgnoreCase("Fetch Email")){
					
					}else{
						if(params.value.indexOf("@now") > -1){
						
						}else{
							def prop = org.grails.formbuilder.Field.get(Long.parseLong(params.attributeName))
							if("SingleLineDate".equalsIgnoreCase(prop.type)){
								String format = message(code:'format.date',args:[],defaultMessage:'MM/dd/yyyy')
								SimpleDateFormat dateformat = new SimpleDateFormat(format)
								Date date ;
								try{
									 date = dateformat.parse(params.value);
								}catch(Exception pex){
									log.error pex
									ruleInstance.errors.rejectValue("value", "rule.optimistic.locking.failure", "Date should be in ${format} format")
									render(view: "edit", model: [ruleInstance: ruleInstance])
									return
								}
							}
						}
					}
				}
				ruleInstance.properties = params
	            if ( params._order )
	  	        	 ruleInstance._order= Integer.parseInt(params._order.replaceAll(",",""))
	      		if ( params._condition)
	       			 ruleInstance._condition= params._condition
	            if (!ruleInstance.hasErrors() && ruleInstance.save(validate:true)) {
	                flash.message = "rule.updated"
	                flash.args = [params.id]
	                flash.defaultMessage = "Rule ${params.id} updated"
	                redirect(controller: "ruleSet",action: "edit", id: ruleInstance.ruleSet.id)
	            }
	            else {
	                render(view: "edit", model: [ruleInstance: ruleInstance])
	            }
	        }
	        else {
	            flash.message = "rule.not.found"
	            flash.args = [params.id]
	            flash.defaultMessage = "Rule not found with id ${params.id}"
	            redirect(action: "edit", id: params.id)
	        }
    }

    def delete = {
        def ruleInstance = Rule.get(params.id)
        def rs = ruleInstance?.ruleSet
        rs?.removeFromRule(ruleInstance)
        
        if (ruleInstance) {
            try {
                ruleInstance.delete()
                flash.message = "rule.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Rule ${params.id} deleted"
                redirect(controller: "ruleSet",action: "edit", id: rs.id)
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error e
                flash.message = "rule.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "Rule ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "rule.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "Rule not found with id ${params.id}"
            redirect(action: "list")
        }
    }
}
