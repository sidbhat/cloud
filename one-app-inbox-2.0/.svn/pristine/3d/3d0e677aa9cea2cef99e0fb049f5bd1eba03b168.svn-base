package com.oneapp.cloud.core

class RuleSetController {

	def springSecurityService
    def index = { redirect(action: "list", params: params) }

    // the delete, save and update actions only accept POST requests
    static allowedMethods = [save: "POST", update: "POST", delete: "POST"]
	
	def formRuleList = {
		try{
			def formId = params.formId
			params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
			params.offset = params.offset?:0
			if ( params.sort == null){
				params.sort = '_order'
				params.order = 'asc'
			}
			def userRoles = springSecurityService.currentUser.authorities*.authority
			def ruleSetInstanceList = new ArrayList()
			def ruleSetInstanceTotal
			def ruleListForForm = Rule.withCriteria(){
				and{
					eq 'className', formId
					ruleSet{
						eq '_action',RuleSet.SUBSCRIBE
					}
				}
			}
			ruleListForForm.each{
				ruleSetInstanceList.add(it.ruleSet)
			}
			ruleSetInstanceTotal = ruleSetInstanceList.size()
			ApplicationConf appConf = ApplicationConf.read(1)
			def formsForClient = appConf?.copyForms
			def formIdsForClient = new ArrayList()
			def ruleSetIdsForClient
			if(formsForClient && formsForClient*.tenantId.contains(springSecurityService.currentUser.userTenantId)){
				def ids = formsForClient*.id*.toString()
				try{
					if(RuleSet.count()>0){
						ruleSetIdsForClient = Rule.withCriteria(){
							'in' 'className',ids
							ruleSet{
								eq '_action',RuleSet.SUBSCRIBE
								'in' 'id',ruleSetInstanceList*.id
								projections{
									property("id")
								}
							}
						}
					}
				}catch(Exception e){
					println "=======>>>> no rule set: "+e
				}
				if(!ruleSetIdsForClient || !ruleSetIdsForClient[0]){
					ruleSetIdsForClient = new ArrayList()
				}
			}
			render(view: "list", model: [ruleSetInstanceList: ruleSetInstanceList, ruleSetInstanceTotal: ruleSetInstanceTotal, formIdsForClient: formIdsForClient, ruleSetIdsForClient: ruleSetIdsForClient as List])
		}catch(Exception ex){
			flash.message = "Error : "+ex.message
			flash.defaultMessage = flash.message
			redirect(controller:"form", action: "list")
		}
	}
	
    def list = {
        params.max = Math.min(params.max ? params.max.toInteger() : 10,  100)
		params.offset = params.offset?:0
        if ( params.sort == null){
        	params.sort = '_order'
        	params.order = 'asc'
        }
		def userRoles = springSecurityService.currentUser.authorities*.authority
		def ruleSetInstanceList
		def ruleSetInstanceTotal
		ruleSetInstanceList = RuleSet.createCriteria().list(offset:params.offset,max:params.max,sort:params.sort,order:params.order){
			if(userRoles.contains(Role.ROLE_USER)){
				eq "_action","Fetch Email"
			}
			eq "createdBy.id",springSecurityService.currentUser.id
		}
		ruleSetInstanceTotal = ruleSetInstanceList.totalCount
		ApplicationConf appConf = ApplicationConf.read(1)
		def formsForClient = appConf?.copyForms
		def formIdsForClient = new ArrayList()
		def ruleSetIdsForClient
		if(formsForClient && formsForClient*.tenantId.contains(springSecurityService.currentUser.userTenantId)){
			def ids = formsForClient*.id*.toString()
			try{
				if(RuleSet.count()>0){
					ruleSetIdsForClient = Rule.withCriteria(){
						'in' 'className',ids
						ruleSet{
							eq '_action',RuleSet.SUBSCRIBE
							'in' 'id',ruleSetInstanceList*.id
							projections{
								property("id")
							}
						}
					}
				}
			}catch(Exception e){
				println "=======>>>> no rule set: "+e
			}
			if(!ruleSetIdsForClient || !ruleSetIdsForClient[0]){
				ruleSetIdsForClient = new ArrayList()
			}
		}
		[ruleSetInstanceList: ruleSetInstanceList, ruleSetInstanceTotal: ruleSetInstanceTotal, formIdsForClient: formIdsForClient, ruleSetIdsForClient: ruleSetIdsForClient as List]
    }
	
	
    def create = {
        def ruleSetInstance = new RuleSet()
        ruleSetInstance.properties = params
        ruleSetInstance.status = "Active"
        render(view: "create", model: [ruleSetInstance: ruleSetInstance])
    }

	def fields = { 	 
		 def prop
		 String value = params.id 
		 if ( value == RuleSet.USER ){
		 	prop = User.findAllByUserTenantIdAndEnabled(springSecurityService.currentUser.userTenantId,true)
		 }else if ( value == RuleSet.GROUP ){
		 	prop = GroupDetails.list()
		 }else if ( value == RuleSet.DEPARTMENT ){
		 	prop = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
		 }else  if ( value == RuleSet.ROLE ){
		 	if(springSecurityService.currentUser.userTenantId  == 1)
		 		prop = Role.findAllByAuthorityNotEqual(Role.ROLE_SUPER_ADMIN)
			else
				prop = Role.findAllByAuthorityInList([Role.ROLE_ADMIN,Role.ROLE_HR_MANAGER,Role.ROLE_USER])
		 }else  if ( value == RuleSet.USER_MANAGER ||value == RuleSet.USER_DEPARTMENT ){
		 	prop = ['Determined']   // Users manager and department is automatically determined from the user field - reportsTo and department
		 }
		  
		 def widget =  g.select(from:prop,  name: "resultInstance", noSelection: ['': 'Select One...']) 
		 render widget
	}
    def save = {
    	def ruleSetInstance = new RuleSet(params)
        ruleSetInstance._action=params._action
		try{
        if ( params._order )
       	 	ruleSetInstance._order= Integer.parseInt(params._order.replaceAll(",",""))
		}catch(Exception ex){
			ruleSetInstance.errors.rejectValue("_order", "typeMismatch.java.lang.Integer", ["Order"] as Object[],"Property Order must be a valid number")
		}
		if(ruleSetInstance._action.equalsIgnoreCase("Fetch Email")){
			ruleSetInstance.resultClass = "User"
			ruleSetInstance.resultInstance = springSecurityService.currentUser.username
		}
		ruleSetInstance.createdBy = springSecurityService.currentUser
		ruleSetInstance.dateCreated = new Date()
		ruleSetInstance.lastUpdated = new Date()
		
        if (!ruleSetInstance.hasErrors() && ruleSetInstance.save(flush:true)) {
            flash.message = "ruleSet.created"
            flash.args = [ruleSetInstance.id]
            flash.defaultMessage = "RuleSet created"
            redirect(action: "edit", id: ruleSetInstance.id)
        }
        else {
         def prop
		 if ( ruleSetInstance.resultClass == RuleSet.USER ){
		 	prop = User.findAllByUserTenantIdAndEnabled(springSecurityService.currentUser.userTenantId,true)
		 }else if ( ruleSetInstance.resultClass == RuleSet.GROUP ){
		 	prop = GroupDetails.list()
		 }else if ( ruleSetInstance.resultClass == RuleSet.DEPARTMENT ){
		 	prop = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
		 }else  if ( ruleSetInstance.resultClass == RuleSet.ROLE ){
		 	prop = Role.findAllByAuthorityNotEqual(Role.ROLE_SUPER_ADMIN)
		 }else  if ( ruleSetInstance.resultClass == RuleSet.USER_MANAGER || ruleSetInstance.resultClass == RuleSet.USER_DEPARTMENT ){
		 	prop = ['Determined']   // Users manager and department is automatically determined from the user field - reportsTo and department
		 }
        	render(view: "create", model: [ruleSetInstance: ruleSetInstance,prop:prop])
        }
    }

 
    def edit = {
        def ruleSetInstance = RuleSet.get(params.id)
		if (!ruleSetInstance) {
            flash.message = "ruleSet.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "RuleSet not found with id ${params.id}"
            redirect(action: "list")
        }
        else {
			boolean isUsedForClient = false
			ApplicationConf appConf = ApplicationConf.read(1)
			def formsForClient = appConf?.copyForms
			if(formsForClient){
				def ids = formsForClient*.id*.toString()
				def rulesList = Rule.withCriteria(){
					'in' 'className',ids
					ruleSet{
						eq 'id',ruleSetInstance.id
					}
					projections{
						rowCount()
					}
				}
				if(rulesList && rulesList[0] && rulesList[0]>0){
					isUsedForClient = true
				}
			}
			
			def prop
			if ( ruleSetInstance.resultClass == RuleSet.USER ){
				prop = User.findAllByUserTenantIdAndEnabled(springSecurityService.currentUser.userTenantId,true)
			}else if ( ruleSetInstance.resultClass == RuleSet.GROUP ){
				prop = GroupDetails.list()
			}else if ( ruleSetInstance.resultClass == RuleSet.DEPARTMENT ){
				prop = DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)
			}else  if ( ruleSetInstance.resultClass == RuleSet.ROLE ){
				prop = Role.findAllByAuthorityNotEqual(Role.ROLE_SUPER_ADMIN)
			}else  if ( ruleSetInstance.resultClass == RuleSet.USER_MANAGER || ruleSetInstance.resultClass == RuleSet.USER_DEPARTMENT ){
				prop = ['Determined']   // Users manager and department is automatically determined from the user field - reportsTo and department
			}
			
            return [ruleSetInstance: ruleSetInstance, isUsedForClient: isUsedForClient,prop:prop]
        }
    }

    def update = {
        def ruleSetInstance = RuleSet.get(params.id)
        if (ruleSetInstance) {
            if (params.version) {
                def version = params.version.toLong()
                if (ruleSetInstance.version > version) {
                    
                    ruleSetInstance.errors.rejectValue("version", "ruleSet.optimistic.locking.failure", "Another user has updated this RuleSet while you were editing")
                    render(view: "edit", model: [ruleSetInstance: ruleSetInstance])
                    return
                }
            }
            ruleSetInstance.properties = params
              ruleSetInstance._action=params._action
			  if(ruleSetInstance._action.equalsIgnoreCase("Fetch Email")){
				  ruleSetInstance.resultClass = "User"
				  ruleSetInstance.resultInstance = springSecurityService.currentUser.username
			  }
        	try{
	        if ( params._order )
	       	 	ruleSetInstance._order= Integer.parseInt(params._order.replaceAll(",",""))
			}catch(Exception ex){
				ruleSetInstance.errors.rejectValue("_order", "typeMismatch.java.lang.Integer", ["Order"] as Object[],"Property Order must be a valid number")
			}
       	 	ruleSetInstance.lastUpdated = new Date()
       	 	
      		if (!ruleSetInstance.hasErrors() && ruleSetInstance.save()) {
                flash.message = "ruleSet.updated"
                flash.args = [params.id]
                flash.defaultMessage = "RuleSet ${params.id} updated"
                render(view: "edit", model: [ruleSetInstance: ruleSetInstance])
            }
            else {
                render(view: "edit", model: [ruleSetInstance: ruleSetInstance])
            }
        }
        else {
            flash.message = "ruleSet.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "RuleSet not found with id ${params.id}"
            redirect(action: "edit", id: params.id)
        }
    }

    def delete = {
        def ruleSetInstance = RuleSet.get(params.id)
        if (ruleSetInstance) {
            try {
                ruleSetInstance.delete()
                flash.message = "ruleSet.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "RuleSet ${params.id} deleted"
                redirect(action: "list")
            }
            catch (org.springframework.dao.DataIntegrityViolationException e) {
				log.error e
                flash.message = "ruleSet.not.deleted"
                flash.args = [params.id]
                flash.defaultMessage = "RuleSet ${params.id} could not be deleted"
                redirect(action: "edit", id: params.id)
            }
        }
        else {
            flash.message = "ruleSet.not.found"
            flash.args = [params.id]
            flash.defaultMessage = "RuleSet not found with id ${params.id}"
            redirect(action: "list")
        }
    }
	
	def emailRule = {
		def sender = params.sender
		def emailAccountId = params.emailAccount
		try{
			def ruleSetInstance = new RuleSet()
			ruleSetInstance.resultClass = "User"
			ruleSetInstance.resultInstance = springSecurityService.currentUser.username
			ruleSetInstance._order = 1
			ruleSetInstance._action = "Fetch Email"
			ruleSetInstance.createdBy = springSecurityService.currentUser
			ruleSetInstance.dateCreated = new Date()
			ruleSetInstance.lastUpdated = new Date()
			ruleSetInstance.name = sender.toString()
			ruleSetInstance.status = RuleSet.ACTIVE
			if(!ruleSetInstance.hasErrors() && ruleSetInstance.save(flush:true)) {
				def ruleInstance = new Rule()
				ruleInstance.ruleSet = ruleSetInstance
				ruleInstance._order = 1
				ruleInstance.status = "Active"
				ruleInstance._condition = "AND"
				ruleInstance.operator = "="
				ruleInstance.className = emailAccountId.toString()
				ruleInstance.attributeName = "Sender"
				ruleInstance.value = sender.toString()
				ruleInstance.save(flush:true)
				flash.message = "ruleSet.created"
				flash.args = [ruleSetInstance.id]
				flash.defaultMessage = "RuleSet created"
				redirect(action: "edit", id: ruleSetInstance.id)
			}else{
				flash.message = "Error in creating rule"
				flash.defaultMessage = flash.message
				redirect(controller:"report", action: "emailReport")
			}
		}catch(Exception ex){
			flash.message = "Error in creating rule"
			flash.defaultMessage = flash.message
			redirect(controller:"report", action: "emailReport")
		}
	}
	
}
