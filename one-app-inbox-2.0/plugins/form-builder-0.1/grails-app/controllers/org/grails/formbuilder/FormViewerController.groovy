package org.grails.formbuilder

import java.math.MathContext;
import java.text.SimpleDateFormat;


import org.apache.poi.hssf.record.formula.functions.T
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import grails.converters.JSON
import freemarker.template.Template
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.codehaus.groovy.grails.commons.ConfigurationHolder
import org.grails.formbuilder.Form;
import org.grails.formbuilder.UniqueFormEntry;
import org.grails.paypal.Payment;
import org.grails.paypal.PaymentItem;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.multipart.support.DefaultMultipartHttpServletRequest;

import com.oneapp.cloud.core.ActivityFeed;
import com.oneapp.cloud.core.ActivityFeedConfig;
import com.oneapp.cloud.core.Client;
import com.oneapp.cloud.core.User;
import com.oneapp.cloud.core.Role;
import com.oneapp.cloud.core.UserAppAccessDetail;

import groovy.sql.Sql
/**
 * Form Viewer Controller for Dynamic Domain Class.
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class FormViewerController {

  static allowedMethods = [save: "POST", update: "POST", delete: "GET"]
  def freemarkerConfig
  def formViewerTemplateService
  def domainClassService
  def springSecurityService
  def exportService
  def clientService
  def sqlDomainClassService
  def dataSource
  def mailChimpService
  //below field is used to show all the entries in role is as follows
  static final def ROLES_TO_SHOW_ALL_RECORDS  = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS
  
  //below field is used to show all the fields while creating or editing form entry
  //TODO remove this from here and use this directly in the GSPs
  static final def ROLES_TO_SHOW_ALL_FIELDS = ConfigurationHolder.config.ROLES_TO_SHOW_ALL_FIELDS

  def index = {
    render(view:"/PF/error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
	return
  }
	
  def list = {
	  try{
		  Sql sql = new Sql(dataSource)
		  Form form = session.formInstance ?: Form.read(params.formId)
		  FormAdmin formAdmin = FormAdmin.findByForm(form)
		  def sortCol = params.sort?:'id'
		  def order = params.order?:'desc'
		  if(!form){
			  redirect(controller:'form',action:'list')
			  return
		  }
		  User currentUser=springSecurityService.currentUser
		  /**
		   * Show only on tenant form and
		   * for trail user show on created form
		   * */
		  if((form.tenantId != currentUser.userTenantId)||
			  (currentUser?.authorities*.authority.contains(Role.ROLE_TRIAL_USER) && form.createdBy!=currentUser)){
			  render(view:"/PF/error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
			  return
		  }
//		  def domainClass = grailsApplication.getDomainClass(form.domainClass.name)
//		  if(!domainClass || form.domainClass.updated){
//			  domainClassService.reloadUpdatedDomainClasses()
//			  domainClass = grailsApplication.getDomainClass(form.domainClass.name)
//		  }
		  def domainInstance = sqlDomainClassService.newInstance(form)
//		  formViewerTemplateService.handleDomainClassSourceUpdated(form, domainClass)
		  def filters = []
		  def formFields = form.fieldsList
		  def config = ConfigurationHolder.config
		  def fieldsToRender = formFields?.findAll { field ->
			field.type != 'SubForm' && !config.formAdmin.fields.notKeyFigures.contains(field.type)
		  }
		  fieldsToRender.findAll { field ->
			config.formBuilder."${field.type}".type == 'String'
		  }.each { field ->
			filters << "${field.name} like ?"
		  }
		  fieldsToRender = fieldsToRender.collect { it.name }
		  fieldsToRender.add(0, 'id')
		  grailsApplication.config.formViewer?.responseList?.extraFieldsToShow?.each{field->
			  fieldsToRender.add(field.name)
			  if(field.type=="String")
				  filters << "${field.name} like ?"
		  }
		  def filter = filters.join(" OR ")
	  
		  def dataToRender = [:]
		  dataToRender.sEcho = params.sEcho
		  dataToRender.aaData = []                // Array of domains.
		  dataToRender.aoColumns = []
		  def totalRecordResult = sql.firstRow("select count(id) from "+form.name.toLowerCase())
		  dataToRender.iTotalRecords = totalRecordResult.getAt("count(id)")
		  dataToRender.iTotalDisplayRecords = dataToRender.iTotalRecords
	  
		  def queryWithOrder = new StringBuilder("from ${form.name.toLowerCase()}")
		  def showOwnCreated = SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)
		  if(!showOwnCreated){
			  queryWithOrder.append(" where created_by_id = '"+springSecurityService.currentUser?.id+"'")
			  if (params.sSearch) {
				  queryWithOrder.append(" and (${filter})")
				}
		  }else{
		  if (params.sSearch) {
			  queryWithOrder.append(" where (${filter})")
			}
		  }
		  
		  String query = queryWithOrder.toString()
		  queryWithOrder.append(" order by id desc")
		  def dataRows
		  String selectQuery = "select ${fieldsToRender.collect { "${it}" }.join(',')} ${queryWithOrder.toString()}"
		  if (params.sSearch) {
			 
			dataToRender.iTotalDisplayRecords = sql.rows("select count(id) ${query}", "%${params.sSearch}%")
			dataRows = sql.rows(selectQuery,"%${params.sSearch}%")
			int fromIndex = params.iDisplayStart.toInteger() * params.iDisplayLength.toInteger()
			fromIndex = (dataToRender.iTotalDisplayRecords>fromIndex?fromIndex:0)
			int toIndex = fromIndex + params.iDisplayLength.toInteger()
			toIndex = (dataToRender.iTotalDisplayRecords>toIndex?toIndex:dataToRender.iTotalDisplayRecords)
			dataRows = dataRows.subList(fromIndex, toIndex)
		  } else {
			dataRows = sql.rows(selectQuery)
		  }
		  
		  def showResult = {
		  
		  
		  }
	  
		  def dateFormatter = new SimpleDateFormat(grailsApplication.config.format.date)
		  def fieldDateFormatters = [:]
		  dataRows?.each { dataRow ->
			def newDatarow = []
			def counter = 0
			def itsAttachments = []
			def itsId
			dataRow.each{
				def fieldName = fieldsToRender.getAt(counter)
				def valueOfField = it.getValue()
				if(counter==0){
					itsAttachments = form.getDomainAttachments("${valueOfField}".toLong())
					itsId = valueOfField
				}
				if(valueOfField?.getClass()?.getName() == "java.sql.Timestamp"){
					def thisField = formFields.find{it.name == fieldName}
					def timeFormatted = ""
					if(thisField){
						try{
							def thisFieldSettings = JSON.parse(thisField.settings)
							if(thisFieldSettings.timeFormat){
								fieldDateFormatters[fieldName] = fieldDateFormatters[fieldName]?:(new SimpleDateFormat(thisFieldSettings.timeFormat))
								timeFormatted = " "+fieldDateFormatters[fieldName].format(valueOfField)
							}
						}catch(Exception e){}
					}
					newDatarow.add ((dateFormatter.format( valueOfField)?:"")+timeFormatted)
				}else if(fieldName == "created_by_id"){
					def user = User.get(valueOfField)
					newDatarow.add user?.username?user?.username?.encodeAsHTML():""
				}else{
					def thisField = formFields.find{it.name == fieldName}
					if(thisField?.type == 'FileUpload'){
						def thisFieldAtt = itsAttachments?.findAll{it.inputName == (fieldName+'_file')}
						valueOfField = thisFieldAtt?thisFieldAtt.size():"0"
						newDatarow.add valueOfField?valueOfField.encodeAsHTML():""
					}else if(thisField?.type == 'AddressField'){
						def mapValue
						if(valueOfField){
							mapValue = grails.converters.JSON.parse(valueOfField)
						}
							newDatarow.add mapValue?."line1"?mapValue."line1":""
							newDatarow.add mapValue?."line2"?mapValue."line2":""
							newDatarow.add mapValue?."city"?mapValue."city":""
							newDatarow.add mapValue?."state"?mapValue."state":""
							newDatarow.add mapValue?."zip"?mapValue."zip":""
							newDatarow.add mapValue?."country"?mapValue."country":""
						
						//newDatarow.add results
					}else if(thisField?.type == 'NameTypeField'){
						def mapValue
						def settings = grails.converters.JSON.parse(thisField.settings)
						if(valueOfField){
							mapValue = grails.converters.JSON.parse(valueOfField)
						}
						    boolean	pre=(settings?.showPrefix)
							boolean mid=(settings?.showMiddleName)
							if(pre)
								newDatarow.add mapValue?."pre"?mapValue."pre":""
							newDatarow.add mapValue?."fn"?mapValue."fn":""
							if(mid)
								newDatarow.add mapValue?."mn"?mapValue."mn":""
							newDatarow.add mapValue?."ln"?mapValue."ln":""
					}else if(thisField?.type == 'Likert'){
						def mapValue
						def resultVal = ""
						if(valueOfField){ 
							mapValue = grails.converters.JSON.parse(valueOfField)
							mapValue?.each{mapValObj->
								if(resultVal!=''){
									resultVal += ', '
								}
								resultVal+=mapValObj!=null && mapValObj.toString()!='null'?mapValObj.toString():''
							}
						}
						    
							newDatarow.add resultVal
					}else if(thisField?.type == 'Paypal'){
						Payment p = Payment.findByFormIdAndInstanceId("${form.id}","${itsId}")
						def amount = "0"
						def status = "N/A"
						if(p){
							def totalPayment = 0
							def currentCurr = config.formBuilder.currencies[p.currency.toString()]
							p?.paymentItems?.each{PaymentItem paymentItem->
								totalPayment+=paymentItem.amount*paymentItem.quantity
							}
							status = p.status
							amount = currentCurr + totalPayment
						}
						newDatarow.add amount?:""
						newDatarow.add status?:""
					}else if(thisField?.type == 'SingleLineNumber'){  
						def settings = grails.converters.JSON.parse(thisField.settings)
						int decimalPlaces=2
						  try{
							   decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
						  }catch (Exception e) {
						}
						  def value=valueOfField?valueOfField.encodeAsHTML():""
						  if(decimalPlaces<2)
						  try{
							  def val=(valueOfField)?new BigDecimal(valueOfField):""
								value =(new BigDecimal(val, MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
						  }catch(Exception e){
						  }
						  newDatarow.add value
					  }else{
						if(counter == 0){
							newDatarow.add valueOfField?valueOfField.encodeAsHTML():""
							def uniqueId
							try{
								uniqueId = UniqueFormEntry.findByFormIdAndInstanceId(form.id,"${valueOfField}".toLong())
								valueOfField = uniqueId.uniqueId
							}catch(Exception e){
							}
							newDatarow.add valueOfField?valueOfField.encodeAsHTML():""
						}else{
							newDatarow.add valueOfField?valueOfField.encodeAsHTML():""
						}
					}
				}
				counter++
				if(counter == dataRow.size()){
					newDatarow.add("<a href='${request.getContextPath()}/formViewer/instanceHistoryList/${itsId}?formId=${form.id}'>History</a>")
				}
			}
			dataToRender.aaData << newDatarow
		  }
		  try{
			  def fieldsTitle = []
			  fieldsTitle.add(["sTitle":"Id."])
			  fieldsTitle.add(["sTitle":"Unique Id"])
			  formFields?.each { field ->
				  def settings = grails.converters.JSON.parse(field.settings)
				  if (field.type != 'SubForm' && !config.formAdmin.fields.notKeyFigures.contains(field.type)) {
					if(field.type == 'Paypal'){
						fieldsTitle.add(["sTitle":'Amount',"sClass":'alignRight'])
						fieldsTitle.add(["sTitle":'Status'])
					}else if(field.type == 'AddressField'){
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Address Line1)'])
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Address Line2)'])
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(City)'])
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(State)'])
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Zip/Postal Code)'])
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Country)'])
					}else if(field.type == 'NameTypeField'){
						boolean	pre=(settings?.showPrefix)
						boolean mid=(settings?.showMiddleName)
						if(pre)
							fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Prefix)'])
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(First Name)'])
						if(mid)
							fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Middle Name)'])
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Last Name)'])
					}else if(field?.type == 'SingleLineNumber'){
					  boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false
					  String currency=currencyType?config.formBuilder.currencies[settings.currencyType]:''
					  fieldsTitle.add(["sTitle":(settings."en".label?.encodeAsHTML()+(currencyType?"(${currency})":""))])
					}else{
						fieldsTitle.add(["sTitle":settings."en".label?.encodeAsHTML()])
					}
				  }
			  }
			  grailsApplication.config.formViewer?.responseList?.extraFieldsToShow?.each{field->
				  fieldsTitle.add(["sTitle":message(code:field.messageCode,default:field.messageDefault)])
			  }
			  fieldsTitle.add(["sTitle":"History"])
			  dataToRender.aoColumns = fieldsTitle
		  }catch(Exception e){}
		  if (request['isTablet'])
		  render (view:"/form/iPadList",params:params,model:[dataToRender:dataToRender,formName:form?.toString(),searchable:formAdmin.searchable?formAdmin.searchable:false])
	    else
		  render (view:"/form/resultList",params:params,model:[dataToRender:dataToRender,formName:form?.toString(),searchable:formAdmin.searchable?formAdmin.searchable:false])
	  }catch(Exception e){
	  	log.error e
		flash.message = "Some problem occured"
		flash.defaultMessage = flash.message
	  	redirect(controller:'form',action:'list')
	  }
  }
  
  def mobileList = {
	  		Form form
	  		try{
	  			form = Form.read(params.formId)
	  		}catch(Exception e){}
	  		if(form){
	  
	  			FormAdmin fa
	  			if(form){
	  				fa = FormAdmin.findByForm(form)
	  			}
	  			if(fa){
	  				def showOwnCreated = SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS)
	  
	  				params.max = Math.min(params.max ? params.int('max') : 10, 100)
	  				def sortCol = params.sort?:'id'
	  				def order = params.order?:'desc'
	  
	  				def listTotalMap = sqlDomainClassService.list(form,showOwnCreated,[max:params.max,offset: params.offset?:0,sort:sortCol,order:order])
	  				def formType = fa.formType
	  				def formName = JSON.parse(form.settings)."en".name
	  
	  				[domainInstanceList: listTotalMap.instanceList,
	  				 domainInstanceTotal: listTotalMap.totalCount,
	  				 formId:params.formId,
	  				 formType: formType,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,form:form, formName:formName, formAdmin:fa]
	  			}else{
	  				flash.message = "form.notPublished"
	  				flash.args = []
	  				flash.defaultMessage = "Form not published."
	  				redirect(controller:'form',action:'list')
	  			}
	  		}else{
	  			flash.message = "form.notSelected"
	  			flash.args = []
	  			flash.defaultMessage = "No form Selected"
	  			redirect(controller:'form',action:'list')
	  		}
	  	}
  
  def instanceHistoryList = {
		try{
		  Form form = Form.read(params.formId)
		  FormAdmin formAdmin = FormAdmin.findByForm(form)
		  def formHistoryItems = FormHistory.findAllByFormIdAndInstanceId(params.formId.toLong(),params.id.toLong())
		  def uniqueFormId = UniqueFormEntry.findByFormIdAndInstanceId(params.formId.toLong(),params.id.toLong())
		  def dataToRender = [:]
		  dataToRender.aaData = []                // Array of domains.
		  dataToRender.aoColumns = []
		  dataToRender.iTotalRecords = formHistoryItems.size()
		  dataToRender.iTotalDisplayRecords = dataToRender.iTotalRecords
		  def counter = 0
		  SimpleDateFormat dateFormat = new SimpleDateFormat("yyyy-MM-dd");
		  SimpleDateFormat dateFormatForField = new SimpleDateFormat("MM/dd/yyyy");
		  dataToRender.aoColumns.add(["sTitle":"Datetime"])
		  dataToRender.aoColumns.add(["sTitle":"Action"])
		  dataToRender.aoColumns.add(["sTitle":"User"])
		  formHistoryItems.each{ formHistory ->
			  def historydata = JSON.parse(formHistory.data)
			  def newDatarow = []
			  def isValueSet = false
			  form.fieldsList.eachWithIndex{field, i ->
				  try{
					  if(field.type!='SubForm' && field.type!='ImageUpload' && field.type!='PlainTextHref' && field.type!='PlainText'){
						  def settings = grails.converters.JSON.parse(field.settings)
						  def fieldValue = historydata[field.name]
						  if(!isValueSet){
							  newDatarow.add(historydata.lastUpdated)
							  newDatarow.add formHistory.action
							  if(formHistory.action == "create")
							  	newDatarow.add historydata.createdBy.username
							  else
							  	newDatarow.add historydata.updatedBy.username
							  isValueSet = true
						  }
						  if(counter == 0){
							  if(field.type == 'AddressField'){
								  dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Address Line1)'])
								  dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Address Line2)'])
								  dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(City)'])
								  dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(State)'])
								  dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Zip/Postal Code)'])
								  dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Country)'])
							  }else if(field.type == 'NameTypeField'){
								    boolean	pre=(settings?.showPrefix)
									boolean mid=(settings?.showMiddleName)
									if(pre)
										dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Prefix)'])
									dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(First Name)'])
									if(mid)
										dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Middle Name)'])
									dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()+'(Last Name)'])
							  }else{
								  dataToRender.aoColumns.add(["sTitle":settings."en".label?.encodeAsHTML()])
							  }
						  }
						  if(field.type == 'SingleLineDate' || (field.type == 'FormulaField' && settings.en.newResultType == 'DateResult')){
							  try{
								  Date cdate = dateFormat.parse(fieldValue);
								  fieldValue = dateFormatForField.format(cdate)
								  newDatarow.add fieldValue
								}catch(Exception e){ }
							  }else if(field.type == 'AddressField'){
								  def mapValue
								  if(fieldValue){
									  mapValue = grails.converters.JSON.parse(fieldValue.toString())
								  }
								  newDatarow.add mapValue?."line1"?mapValue."line1":""
								  newDatarow.add mapValue?."line2"?mapValue."line2":""
								  newDatarow.add mapValue?."city"?mapValue."city":""
								  newDatarow.add mapValue?."state"?mapValue."state":""
								  newDatarow.add mapValue?."zip"?mapValue."zip":""
								  newDatarow.add mapValue?."country"?mapValue."country":""
							  }else if(field?.type == 'NameTypeField'){
									def mapValue
									if(fieldValue){
										mapValue = grails.converters.JSON.parse(fieldValue.toString())
									}
								    boolean	pre=(settings?.showPrefix)
									boolean mid=(settings?.showMiddleName)
									if(pre)
										newDatarow.add mapValue?."pre"?mapValue."pre":""
									newDatarow.add mapValue?."fn"?mapValue."fn":""
									if(mid)
										newDatarow.add mapValue?."mn"?mapValue."mn":""
									newDatarow.add mapValue?."ln"?mapValue."ln":""
							  }else{
							  	newDatarow.add fieldValue
							  }
						  }
					  }catch(Exception e){
							  print e.message
					  }
				  }
			  dataToRender.aaData << newDatarow
			  counter++;
		  }
		  if(request['isTablet'])
		  render (view:"/form/iPadhistoryList",params:params,model:[dataToRender:dataToRender,formName:form?.toString(),uniqueFormId:uniqueFormId?.uniqueId?:params.id])
		  else
		  render (view:"/form/historyList",params:params,model:[dataToRender:dataToRender,formName:form?.toString(),uniqueFormId:uniqueFormId?.uniqueId?:params.id])
	  }catch(Exception e){
		  log.error e
		flash.message = "Some problem occured"
		flash.defaultMessage = flash.message
		  redirect(controller:'form',action:'list')
	  }
  }

  private renderView(name, model, templateText) {
	if(model.domainInstance?.errors != null){
		if(!model.domainInstance.errors){
			model.domainInstance.remove("errors")
		}
	}
    FastStringWriter out = new FastStringWriter()
    new Template(name, new StringReader(templateText),
            freemarkerConfig.configuration).process(model, out)
    render out.toString()
  }

  private outPut(name, model, templateText, fromId) {
    FastStringWriter out = new FastStringWriter()
    new Template(name, new StringReader(templateText),
            freemarkerConfig.configuration).process(model, out)
    String sb = out.toString()
    String first = sb.split("<head>")[0]
    String last = sb.split("<head>")[1]
    def aa = """
    <link rel="shortcut icon" href="favicon1.ico" type="image/x-icon" />
        <link rel="stylesheet" type="text/css" media="screen" href="formbuilder.css" />
        <script type="text/javascript" src="utility.js"></script>
        <script type="text/javascript" src="jquery-1.4.2.js"></script>


        <link href="jquery-ui-1.8.6.custom.css" type="text/css" rel="stylesheet" media="screen, projection" id="jquery-ui-theme" /><script src="/form-builder/plugins/jquery-ui-1.8.6/jquery-ui/js/jquery-ui-1.8.6.custom.min.js" type="text/javascript" ></script>
        <link rel="stylesheet" type="text/css" media="screen" href="uni-form.css" />
<link rel="stylesheet" type="text/css" media="screen" href="default.uni-form.css" />
<!--[if lte ie 7]>
      <style type="text/css" media="screen">
        /* Move these to your IE6/7 specific stylesheet if possible */
        .uniForm, .uniForm fieldset, .uniForm .ctrlHolder, .uniForm .formHint, .uniForm .buttonHolder, .uniForm .ctrlHolder ul{ zoom:1; }
      </style>
    <![endif]-->

		<link rel='stylesheet' href="langSelector.css" />

        <style type="text/css">
					#footer {
						clear: both;
						text-align: center;
						margin-top: 5px;
					}

					/* Plain Text field */
					.uniForm .ctrlHolder .PlainText { padding-top: 0; padding-bottom: 0 }
					.uniForm .topAlign { padding-top: 0; padding-bottom: 2em; }
					.uniForm .bottomAlign { padding-top: 2em; padding-bottom: 0; }
					div.rightAlign { text-align: right; }
					div.centerAlign { text-align: center; }
        </style>
    """

    String text = first + "<head>" + aa + last
    text = text.replaceAll("<form action=\"", "<form action=\"${ConfigurationHolder.config.grails.server.URL}")

    //File dir = new File("form/${fromId}")
    // dir.mkdir()

    File file = new File("index${fromId}.html")
    file.write(text)
    file.createNewFile()

    def ant = new AntBuilder()
    ant.zip(destfile: "${fromId}.zip",
            basedir: "form",
            includes: "*.css, *.js,index${fromId}.html"
    )
    file = new File("${fromId}.zip")
    response.contentType = "application/octet-stream"
    response.setHeader("Content-disposition", "attachment;filename=${file.getName()}")
    response.outputStream << file.newInputStream()
    //response.flush()
  }
  
  def exportResponseList = {
	  Form form = session.formInstance ?: Form.read(params.formId)
	  
	  List fields = []
	  Map labels = [:]
	  boolean hasSubForm = false
	  def dataListWithSubFormData = []
		def subFormHeaderBar = [:]
	  def filedsNotToShow = grailsApplication.config.formAdmin.fields.notKeyFigures
	  
	  def mainFormFields = form.fieldsList.findAll{!filedsNotToShow.contains(it.type)}
	  labels.uid = "Unique Id"
	  fields.add("uid")
	  mainFormFields?.each {Field field ->
		  if(field.settings.indexOf(FormBuilderConstants.PERSISTABLE) > -1){
			  def fieldSettings = JSON.parse(field.settings)
			  if(field.type != 'SubForm' || !fieldSettings.subForm){
				  if(field.type=='Paypal'){
					  fields.add("${field.name}Amount")
					  fields.add("${field.name}Status")
					  //(fieldSettings."${request.locale.baseLocale.language}")?:
					  labels."${field.name}Amount"="Amount"
					  labels."${field.name}Status"="Status"
				  }else if(field?.type == 'AddressField'){
					  fields.add("${field.name}(Address Line1)")
					  fields.add("${field.name}(Address Line2)")
					  fields.add("${field.name}(City)")
					  fields.add("${field.name}(State)") 
					  fields.add("${field.name}(Zip/Postal Code)")
					  fields.add("${field.name}(Country)")
					  def localeSpecificSettings = fieldSettings.en
					  def label = localeSpecificSettings.label
					  labels."${field.name}(Address Line1)"="${label}(Address Line1)"
					  labels."${field.name}(Address Line2)"= "${label}(Address Line2)"
					  labels."${field.name}(City)"= "${label}(City)"
					  labels."${field.name}(State)" ="${label}(State)"
					  labels."${field.name}(Zip/Postal Code)"= "${label}(Zip/Postal Code)"
					  labels."${field.name}(Country)" ="${label}(Country)"
				  }else if(field?.type == 'NameTypeField'){
					  boolean	pre=(fieldSettings?.showPrefix)
					  boolean mid=(fieldSettings?.showMiddleName)
					  if(pre)
						  fields.add("${field.name}(Prefix)")
					  fields.add("${field.name}(First Name)")
					  if(mid)
						  fields.add("${field.name}(Middle Name)")
					  fields.add("${field.name}(Last Name)")
					  def localeSpecificSettings = fieldSettings.en
					  def label = localeSpecificSettings.label
					  if(pre)
					  	labels."${field.name}(Prefix)"="${label}(Prefix)"
					  labels."${field.name}(First Name)"= "${label}(First Name)"
					  if(mid)
					  	labels."${field.name}(Middle Name)"= "${label}(Middle Name)"
					  labels."${field.name}(Last Name)" ="${label}(Last Name)"
				  }else if(field?.type == 'SingleLineNumber'){
					  boolean currencyType=(fieldSettings?.currencyType && fieldSettings?.currencyType!='')?true:false
					  String currency=currencyType?grailsApplication.config?.formBuilder.currencies[fieldSettings.currencyType]:''
					  if(currencyType){
						  fields.add("${field.name}(${(currency).decodeHTML()})")
						  def localeSpecificSettings = fieldSettings.en
						  def label = localeSpecificSettings.label
						  labels."${field.name}(${(currency).decodeHTML()})"=label+"(${(currency).decodeHTML()})"
						 }else{
						 fields.add(field.name)
						 //(fieldSettings."${request.locale.baseLocale.language}")?:
						 def localeSpecificSettings = fieldSettings.en
						 def label = localeSpecificSettings.label
						 labels."${field.name}"=label
						 }
					}else{
				      fields.add(field.name)
					  //(fieldSettings."${request.locale.baseLocale.language}")?:
					  def localeSpecificSettings = fieldSettings.en
					  def label = localeSpecificSettings.label
					  labels."${field.name}"=label
				  }
			  } else {
			  	Form subForm = Form.read(fieldSettings.subForm)
				if(subForm){
					def subFormName = subForm.toString()
					def subFieldsList = subForm.fieldsList.findAll{!filedsNotToShow.contains(it.type)}
					subFieldsList.each{Field subField->
						fields.add(subField.name)
						def subFieldSettings = JSON.parse(subField.settings)
						def label = subFieldSettings.en.label
						if(subField?.type == 'SingleLineNumber'){
							def subSettings = grails.converters.JSON.parse(subField.settings)
							boolean currencyType=(subFieldSettings?.currencyType && subFieldSettings?.currencyType!='')?true:false;
							String currency=currencyType?" (${ConfigurationHolder.config?.formBuilder.currencies[subFieldSettings.currencyType].decodeHTML()})":''
							label+=currency
						}
						labels."${subField.name}"=label
						subFormHeaderBar."${subField.name}"="("+subFormName+")"
					}
					hasSubForm = true
				}else{
					fields.add(field.name)
					def label = fieldSettings.en.label
					labels."${field.name}"=label
				}
			  }
		  }
	  }
	  
	  def extraFields = grailsApplication.config.formViewer?.responseList?.extraFieldsToShow
	  extraFields?.each{field->
		  fields.add(field.name)
		  def label = message(code:field.messageCode,default:field.messageDefault)
		  labels."${field.name}"=label
	  }
	  
	  def listTotalMap = sqlDomainClassService.list(form,SpringSecurityUtils.ifAnyGranted(ROLES_TO_SHOW_ALL_RECORDS))
	  def dataList = listTotalMap.instanceList

		def tempDataList
		def extraFields2 = grailsApplication.config.formViewer.responseList.export.extraFieldsToShow
		
		if(subFormHeaderBar){
			dataListWithSubFormData.add(subFormHeaderBar)
		}
		
		dataList.each{dataInstance->
			tempDataList = []
			tempDataList << [:]
			def uniqueFormId = UniqueFormEntry.findByFormIdAndInstanceId(params.formId.toLong(),"${dataInstance.id}".toLong())
			def attachments = form.getDomainAttachments(dataInstance.id)
			dataInstance.created_by_id = User.get(dataInstance.created_by_id).toString()
			//dataInstance.date_created = dataInstance.date_created
			tempDataList.get(0).uid = uniqueFormId?.uniqueId?:(dataInstance.id)
			mainFormFields.each{Field field->
				if(field.type == 'FileUpload'){
					def thisFieldAttachments = attachments.find{it.inputName == field.name+'_file'}//find is used just to check if it has atleast one attachment
					if(thisFieldAttachments){
						tempDataList.get(0)."${field.name}" = "Y"
					}else{
						tempDataList.get(0)."${field.name}" = "N"
					}
				}else if(field?.type == 'AddressField'){
					def mapValue
					if(dataInstance."${field.name}"){
						mapValue = grails.converters.JSON.parse(dataInstance."${field.name}")
					}
					tempDataList.get(0)."${field.name}(Address Line1)"=mapValue?."line1"?mapValue."line1":""
					tempDataList.get(0)."${field.name}(Address Line2)"= mapValue?."line2"?mapValue."line2":""
					tempDataList.get(0)."${field.name}(City)"= mapValue?."city"?mapValue."city":""
					tempDataList.get(0)."${field.name}(State)" =mapValue?."state"?mapValue."state":""
					tempDataList.get(0)."${field.name}(Zip/Postal Code)"= mapValue?."zip"?mapValue."zip":""
					tempDataList.get(0)."${field.name}(Country)" =mapValue?."country"?mapValue."country":""
					
					//newDatarow.add results
				}else if(field?.type == 'NameTypeField'){
						def mapValue
						def settings = grails.converters.JSON.parse(field.settings)
						if(dataInstance."${field.name}"){
							mapValue = grails.converters.JSON.parse(dataInstance."${field.name}")
						}
					    boolean	pre=(settings?.showPrefix)
						boolean mid=(settings?.showMiddleName)
						if(pre)
							tempDataList.get(0)."${field.name}(Prefix)"= mapValue?."pre"?mapValue."pre":""
						tempDataList.get(0)."${field.name}(First Name)"= mapValue?."fn"?mapValue."fn":""
						if(mid)
							tempDataList.get(0)."${field.name}(Middle Name)"= mapValue?."mn"?mapValue."mn":""
						tempDataList.get(0)."${field.name}(Last Name)"= mapValue?."ln"?mapValue."ln":""
					}else if(field.type == 'Paypal'){
					Payment payment = Payment.findByFormIdAndInstanceId("${form.id}","${dataInstance.id}")
					def amount = "0"
					def status = "N/A"
					if(payment){ 
						def totalPayment = 0
						def currentCurr = grailsApplication.config.formBuilder.currencies[payment.currency.toString()]
						payment?.paymentItems?.each{PaymentItem paymentItem->
							totalPayment+=paymentItem.amount*paymentItem.quantity
						}
						status = payment.status
						amount = currentCurr.decodeHTML() + totalPayment
						}
					tempDataList.get(0)."${field.name}Amount" = amount
					tempDataList.get(0)."${field.name}Status" = status
				}else if(field?.type == 'SingleLineNumber'){  
					def settings = grails.converters.JSON.parse(field.settings)
					int decimalPlaces=2
					  try{
						   decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
					  }catch (Exception e) {}
					  def value=(dataInstance."${field.name}")?(dataInstance."${field.name}".encodeAsHTML()):""
					  if(decimalPlaces<2)
					  try{
						  def val=(dataInstance."${field.name}")?new BigDecimal(dataInstance."${field.name}"):""
							value =(new BigDecimal(val, MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
					  }catch(Exception e){}
					  boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false
					  String currency=currencyType?grailsApplication.config?.formBuilder.currencies[settings.currencyType]:''
					  if(currencyType){
					  tempDataList.get(0)."${field.name}(${(currency).decodeHTML()})" = value
					  }else{
					  tempDataList.get(0)."${field.name}" = value
					  }
				  }else if(field.type == 'SubForm'){
					def fieldSettings = JSON.parse(field.settings)
					if(fieldSettings.subForm){
						Form subForm = Form.read(fieldSettings.subForm)
						if(subForm){
								def subFieldsList = subForm.fieldsList.findAll{!filedsNotToShow.contains(it.type)}
								def subDataInstanceIdList = sqlDomainClassService.listSubForm(dataInstance.id, form, subForm, field.name)
								subDataInstanceIdList.eachWithIndex{subDataInstance,i->
										if(tempDataList.size()-1<i){
											tempDataList << [:]
										}
										def subAttachements = subForm.getDomainAttachments(subDataInstance.id)
										subFieldsList.each{Field subField->
											if(subField.type == 'FileUpload'){
												def thisSubFieldAttachments = subAttachements.find{it.inputName == subField.name+'_file'}//find is used just to check if it has atleast one attachment
												if(thisSubFieldAttachments){
													tempDataList.get(i)."${subField.name}" = "Y"
												}else{
													tempDataList.get(i)."${subField.name}" = "N"
												}
											}else if(subField?.type == 'AddressField'){
												String values=""
												def mapValue = grails.converters.JSON.parse((subDataInstance."${subField.name}").toString())
												if(mapValue){
													values+=mapValue."line1"?(mapValue."line1"+" ;"):""
													values+=mapValue."line2"?(mapValue."line2"+" ;"):""
													values+=mapValue."city"?(mapValue."city"+" ;"):""
													values+=mapValue."state"?(mapValue."state"+" ;"):""
													values+=mapValue."zip"?(mapValue."zip"+" ;"):""
													values+=mapValue."country"?(mapValue."country"+" ;"):""
												}
												tempDataList.get(i)."${subField.name}"=values
											}else if(subField?.type == 'NameTypeField'){
												String values=""
												def mapValue = grails.converters.JSON.parse((subDataInstance."${subField.name}").toString())
												def subSettings = grails.converters.JSON.parse(subField.settings) 
												boolean pre=subSettings?.showPrefix
												boolean mid=subSettings?.showMiddleName
												if(mapValue){
													if(pre)
														values+=mapValue."pre"?(mapValue."pre"+" "):""
													values+=mapValue."fn"?(mapValue."fn"+" "):""
													if(mid)
														values+=mapValue."mn"?(mapValue."mn"+" "):""
													values+=mapValue."ln"?(mapValue."ln"):""
												}
												tempDataList.get(i)."${subField.name}"=values
											}else if(subField?.type == 'SingleLineNumber'){
												def subSettings = grails.converters.JSON.parse(subField.settings)
												int decimalPlaces=2
												try{
													 decimalPlaces=(subSettings?.decimalPlaces && subSettings?.decimalPlaces?.toInteger()!=2)?subSettings?.decimalPlaces?.toInteger():2
												}catch (Exception e) {}
												def value=subDataInstance."${subField.name}"!=null?subDataInstance."${subField.name}":''
												if(decimalPlaces<2)
												try{
													def val=subDataInstance."${subField.name}"!=null?new BigDecimal(subDataInstance."${subField.name}"):''
													 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
												}catch(Exception e){}
												tempDataList.get(i)."${subField.name}"=value
											}else{
												tempDataList.get(i)."${subField.name}" = subDataInstance."${subField.name}"
											}
										}
								}
						}else{
							tempDataList.get(0)."${field.name}" = ""
						}
					}else{
						tempDataList.get(0)."${field.name}" = ""
					}
				}else{
					tempDataList.get(0)."${field.name}" = dataInstance."${field.name}"
				}
			}
			extraFields2?.each{field->
				tempDataList.get(0)."${field.name}" = dataInstance."${field.name}"
			}
			
			dataListWithSubFormData.addAll(tempDataList)
			if(subFormHeaderBar){
				dataListWithSubFormData.add([:])
			}
		}
	  
	  response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
	  response.setHeader("Content-disposition", "attachment; filename=one-app-form-response-${new Date()}.${params.extension}")
	  
	  exportService.export(params.format, response.outputStream, dataListWithSubFormData, fields, labels, [:], [:])
	  
  }

  def create = {
	  
	  try{
	    Form formInstance
		try{ 
			formInstance = Form.read(params.formId)
		}catch (Exception ex) {
			
		}
		if(!formInstance){
			//render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found")]])
			render(view:"/PF/error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
			return
		}
		def formForFormAdminCheck
		if(formInstance?.formCat == 'S'){//current form is subform
			if(params.pfid && params.pfii && params.pffn){
				if(!request['isMobile']){
					flash.pfii = params.pfii
					flash.pfid = params.pfid
					flash.pffn = params.pffn
				}
				formForFormAdminCheck = Form.read(params.pfid)
			}else{
				//TODO throw error that sub form must be entered through parent form only.
			}
		}else{
			formForFormAdminCheck = formInstance
		}
		if(!FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'create')){
			render(view:"/PF/error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
			return
		}
		FormAdmin fa
		if(formInstance && formForFormAdminCheck){
			fa = FormAdmin.findByForm(formForFormAdminCheck)
		}
		if(!fa || fa.formLogin=='NoOne'){
			//render(view:"error",model:[exception:['message':message(code:'form.access.dataEntryClosed',args:[],'default':"Data entry closed")]])
			render(view:"/PF/error",model:[exception:['message':message(code:'form.access.dataEntryClosed',args:[],'default':"Data entry closed"),detailMessage:message(code:'form.access.dataEntryClosed.detailMessage',args:[],'default':'This form is currently closed for responses. Please contact the author of this form for further assistance.')]])
			return
		}
		
//		if("Approval".equalsIgnoreCase(fa.formType)){
//			//render(view:"error",model:[exception:['message':message(code:'form.approval.publicAccess.denied',args:[],'default':"Access Denied")]])
//			render(view:"/PF/error",model:[exception:['message':message(code:'form.approval.publicAccess.denied',args:[],'default':"Access Denied"),detailMessage:message(code:'form.approval.publicAccess.denied.detailMessage',args:[],'default':"This form is not accessible. Please contact the author of this form for further assistance.")]])
//			return
//		}
		/*def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		if(!domainClass || formInstance.domainClass.updated){
			domainClassService.reloadUpdatedDomainClasses()
			//formInstance = Form.read(params.formId)
			domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
		}
		
	    def domainInstance = domainClass.newInstance()*/
		def domainInstance = sqlDomainClassService.populate(params,formInstance,null,request)
		domainInstance.put("errors",[])
	    
		if(request['isMobile']){
			def formName = JSON.parse(formInstance.settings)."en".name
			def fields = new HashMap()
			formInstance.fieldsList?.each { field ->
				fields.put( field.name,field)
			}
			
			render(view:'create_m',model: [formInstance:formInstance, formName:formName,fields:fields,domainInstance: domainInstance,
			multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS]) // multiPart:true if form have upload component
		}else{
//			formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
			def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance)
		    renderView('create', [flash: flash, formInstance: formInstance,
		            domainInstance: domainInstance, multiPart: false], // multiPart:true if form have upload component
		            createViewTemplate)
		}
	 }catch(Exception ex){
	 	println "FormViewer create: "+ex+", For formid: "+params.formId
			flash.message="${message(code: 'default.error.message')}"
			flash.defaultMessage = flash.message
			redirect(action:'create',params:[formId:params.formId, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
		}
  }
//  def export = {
//    Form formInstance = Form.read(params.formId)
//    def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
//	if(!domainClass || formInstance.domainClass.updated){
//		domainClassService.reloadUpdatedDomainClasses()
//		domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
//	}
//    def domainInstance = domainClass.newInstance()
////    formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
//    outPut('create', [flash: flash, formInstance: formInstance,
//            domainInstance: domainInstance, domainClass: domainClass, multiPart: false], // multiPart:true if form have upload component
//            formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance), params.formId)
//  }

  def save = {
    Form formInstance = Form.read(params.formId)
	if(!formInstance){
		redirect(controller:"dashboard", action:"index")
	}else{
		try{
			def formForFormAdminCheck
			def parentDomainInstance
			if(formInstance.formCat == 'S'){//current form is subform
				if(params.pfid && params.pfii && params.pffn){
					if(!request['isMobile']){
						flash.pfii = params.pfii
						flash.pfid = params.pfid
						flash.pffn = params.pffn
					}
					formForFormAdminCheck = Form.read(params.pfid)
					def parentField = ((Form)formForFormAdminCheck).fieldsList.find{it.name == params.pffn}
					if(parentField && parentField.type == 'SubForm'){
						parentDomainInstance = true
		//				if(!parentDomainInstance){
		//					throw new Exception(message(code:'parent.formInstance.not.found',args:[],'default':"Sub form must be entered through parent form only, here the instance not found."))
		//				}
					}else{
						//throw new Exception(message(code:'parent.formField.not.found',args:[],'default':"Sub form must be entered through parent form only, here the field is not found."))
						render(view:"/PF/error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
					}
				}else{
					//throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
					render(view:"/PF/error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
					return
				}
			}else{
				formForFormAdminCheck = formInstance
			}
			if(!FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'save')){
				render(view:"/PF/error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
				return
			}
			def domainInstance = sqlDomainClassService.populate(params, formInstance,null,request)
			def formName = ""
			try{
				formName = JSON.parse(formInstance.settings)."en".name
			}catch(Exception e){}
			
		    if ( springSecurityService.currentUser )
		     domainInstance.createdBy = springSecurityService.currentUser
		     
		    //PropertySetter.setProperties(domainClass, domainInstance, params)
			def fieldsList = formInstance.fieldsList
			def itemsBought
			def fieldNameSetting_MapList = []
			fieldsList?.each { field ->
					def settings = grails.converters.JSON.parse(field.settings)
					def fieldName = settings."en".label
					//fields.put( field.name,field)
					if ( field.type == "PlainText" ) {
						def fieldVal = params[field.name]
						if ( fieldVal )
							domainInstance."${fieldVal}"= "true"
					}else if ( field.type == "CheckBox" ) {
						def fieldVal = params.list(field.name)
					   def v
					   if ( fieldVal ){
						   v = fieldVal as List
							domainInstance."${field.name}"= (v as JSON).toString()
					   }else
						   domainInstance."${field.name}"= null
					}else if(field.type == "FileUpload"){
					 	def keyValueMap = [:]
						 keyValueMap."${field.name}" = settings
					 	fieldNameSetting_MapList.add(keyValueMap)
					 }else if(field.type == "Paypal"){
					 	itemsBought = domainInstance."${field.name}_bought"
					 }
			}
				
			def totalFileSize = 0
			boolean breakTheAction = false
		    		
		    if (request instanceof DefaultMultipartHttpServletRequest) {
		        request.multipartFiles.each {k, v ->
		            if (k) {
						List<MultipartFile> files = new ArrayList()
						def fieldName = k.replace('_file','')
		                if (v instanceof List) {
		                    v.each {MultipartFile file ->
								if(file && file.size>0){
									files << file
								}
		                    }
		                } else {
							MultipartFile file = v
							if(file && file.size>0){
								files << file
							}
		                }
						files.each{MultipartFile file->
							if(!breakTheAction){
								def fileSize = file.size/1024
								def field = fieldNameSetting_MapList.find{it."${fieldName}"}
								if(field){
									def settings = field.getAt(fieldName)
									def maxSize = settings.maxSize
									if(settings.unit == "MB"){
										maxSize = settings.maxSize*1024
									}
									if(fileSize > maxSize){
										domainInstance.errors.add([name:fieldName, code:"default.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for property ${settings.en.label} can not be greater than ${settings.maxSize} ${settings.unit}"])
										if(request['isMobile']){
											def fields = new HashMap()
											formInstance.fieldsList?.each { f ->
												fields.put( f.name,f)
											}
											render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
										}else{
											def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
											
											renderView('create', [flash: flash, formInstance: formInstance,
												  domainInstance: domainInstance, multiPart: false], // multiPart:true if form have upload component
												  createViewTemplate)
										}
										breakTheAction = true
										return
									}
									totalFileSize += fileSize
								}else{
									domainInstance.errors.add([name:"version", code:"default.form.changed", args:[formName] as Object[], defaultMessage:"Someone might have changed the form while you were entering data. Please try again."])
									if(request['isMobile']){
										def fields = new HashMap()
										formInstance.fieldsList?.each { f ->
											fields.put( f.name,f)
										}
										render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields,  formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
									}else{
										def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
										
										renderView('create', [flash: flash, formInstance: formInstance,
											  domainInstance: domainInstance,  multiPart: false], // multiPart:true if form have upload component
											  createViewTemplate)
									}
									breakTheAction = true
									return
								}
							}
						}
		            }
		        }
		    }
			
			if(breakTheAction){
				return
			}
			if(totalFileSize>0){
				Client myClient = Client.get(springSecurityService.currentUser.userTenantId)
				def earlyDataLength = clientService.getTotalAttachmentSize( myClient.id)
				if( ( totalFileSize*1024 + earlyDataLength )/(1024*1024) > myClient.maxAttachmentSize){
					domainInstance.errors.add([name:"version", code:"default.client.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for your client exceeded. Please contact administrator"])
					if(request['isMobile']){
						def fields = new HashMap()
						formInstance.fieldsList?.each { f ->
							fields.put( f.name,f)
						}
						render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields,  formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
					}else{
						def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
						
						renderView('create', [flash: flash, formInstance: formInstance,
							  domainInstance: domainInstance, multiPart: false], // multiPart:true if form have upload component
							  createViewTemplate)
					}
					return
				}
			}
		//    formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass, domainInstance)
		   if (!domainInstance.errors && sqlDomainClassService.save(domainInstance,formInstance)) {
			   	if(parentDomainInstance){
					sqlDomainClassService.addSubFormInstance(domainInstance.id,params.pfii,params.pffn,formForFormAdminCheck)
				}
			   def resultAttachment = attachUploadedFilesTo(formInstance,domainInstance.id)
			   def user = springSecurityService.currentUser?:null
			   def formAdmin
			   if(parentDomainInstance){
				   formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
				   WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Save",formInstance)
			   }else{
			   		formAdmin = FormAdmin.findByForm(formInstance)
					WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Save")
					if(formAdmin.mailChimpDetails && formAdmin.mailChimpDetails!="")
				  		mailChimpService.saveMailchimp(formAdmin, formInstance, domainInstance)
			   }
			  def pfc = new PFController()
			  def tenantId
			  if(user){
				tenantId = springSecurityService.currentUser.userTenantId
			  }else{
				tenantId = formInstance.tenantId
			  }
			  pfc.saveUserDetails(params.location,request,formInstance.domainClass.name,UserAppAccessDetail.CREATE,springSecurityService.currentUser?:null,tenantId)
			  if(itemsBought){
				  def paymentField = domainInstance.paymentField
				  def paymentFieldSettings = domainInstance.paymentFieldSettings
				  Payment payment = domainInstance.payment
				  payment.formId = formInstance.id
				  payment.instanceId = domainInstance.id
				  payment.buyerId = domainInstance.createdBy.id
				  payment.currency = Currency.getInstance(paymentFieldSettings.curr)
				  if(paymentFieldSettings.itemForm){
					  itemsBought.each{item->
						  payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
							  if(paymentItem.itemNumber == "${item.id}"){
								  paymentItem.amount = item[paymentFieldSettings.iaf]
								  def itemName = item[paymentFieldSettings.inf]?:"Item ${itemIdx}"
								  if(itemName.length()>126){
									  itemName = itemName.substring(0,123) + "..."
								  }
								  paymentItem.itemName = itemName
								  paymentItem.itemNumber = item.id+"_"+paymentFieldSettings.itemForm
							  }
						  }
					  }
				  }else{
					  payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
						  paymentItem.amount = domainInstance[paymentFieldSettings.iaf]
						  paymentItem.itemNumber = domainInstance.id+"_"+formInstance.id
					  }
				  }
				  if (payment?.validate()) {
					  request.payment = payment
					  payment.save(flush: true)
					  def config = grailsApplication.config.grails.paypal
					  def server
					  if(paymentFieldSettings.test){
						  server = config.testServer
					  }else{
						  server = config.server
					  }
					  def baseUrl = grailsApplication.config.grails.serverURL
					  def login = paymentFieldSettings.emid
					  if (!server || !login) throw new IllegalStateException("Paypal misconfigured! You need to specify the Paypal server URL and/or account email. Refer to documentation.")
		  
					  def commonParams = [buyerId: payment.buyerId, transactionId: payment.transactionId,returnAction: 'edit',returnController: 'formViewer',cancelAction: 'edit',cancelController:'formViewer']
					  def notifyURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'notify', params: commonParams).encodeAsURL()
					  def successURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'success', params: commonParams).encodeAsURL()
					  def cancelURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'cancel', params: commonParams).encodeAsURL()
		  
					  def url = new StringBuffer("$server?")
					  url << "business=$login&"
					  if(paymentFieldSettings.itemForm){
						  url << "cmd=_cart&upload=1&"
						  payment.paymentItems.eachWithIndex {paymentItem, i ->
							  def itemId = i + 1
							  url << "item_name_${itemId}=${paymentItem.itemName}&"
							  url << "item_number_${itemId}=${paymentItem.itemNumber}&"
							  url << "quantity_${itemId}=${paymentItem.quantity}&"
							  url << "amount_${itemId}=${paymentItem.amount}&"
							  if (payment.discountCartAmount == 0 && paymentItem.discountAmount > 0) {
								  url << "discount_amount_${itemId}=${paymentItem.discountAmount}&"
							  }
						  }
					  }else{
						  url << "cmd=_xclick&"
						  payment.paymentItems.eachWithIndex {paymentItem, i ->
							  url << "item_name=${paymentItem.itemName}&"
							  url << "amount=${paymentItem.amount}&"
						  }
					  }
					  
					  url << "currency_code=${payment.currency}&"
					  url << "notify_url=${notifyURL}&"
					  url << "return=${successURL}&"
					  url << "cancel_return=${cancelURL}"
		  
					  log.debug "Redirection to PayPal with URL: $url"
					  redirect(url: url)
					  return
				  }
				  else {
					  //Handle in case validation fails
				  }
			  }
				 if(params.subFormid && params.subFormid !="null"  && params.subFormfn && params.subFormfn !="null"){
					   redirect(action:'create',params:[formId:params.subFormid, pfii:domainInstance.id, pfid:formInstance.id, pffn:params.subFormfn])
					 return
				 }
		      	if(formAdmin?.formSubmitMessage){
					flash.message = "${formAdmin?.formSubmitMessage}"
					flash.defaultMessage = flash.message
				}else{
					def showMessage = "${message(code: 'default.created.message', args: [formName?:'', domainInstance.id])}"
					flash.message = showMessage
					flash.defaultMessage = flash.message
				}
				if(formInstance.formCat == 'S'){
					redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
					return
				}
				if(!formAdmin?.openForEdit){
					if(request['isMobile']){
						redirect(action: "edit", id: domainInstance.id, params: [formId: formInstance.id])
					}else{
						if(formAdmin?.redirectUrl){
							render(view:"/PF/error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message],redirectURL:formAdmin.redirectUrl])
							return
						}else{
							render(view:"/PF/error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message]])
							flash.message = null
							return
						}
					}
				}else{
					if(formAdmin?.redirectUrl){
						render(view:"/PF/error",model:[exception:['message':(formName?:"Form Saved"),detailMessage:flash.message],redirectURL:formAdmin.redirectUrl])
						return
					}else{
						redirect(action: "edit", id: domainInstance.id, params: [formId: formInstance.id])
					}
				}
		    }
		    else {
				if(request['isMobile']){
					def fields = new HashMap()
					formInstance.fieldsList?.each { f ->
						fields.put( f.name,f)
					}
					render(view: "create_m", model: [formName:formName, domainInstance: domainInstance, fields:fields, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS])
				}else{
					def createViewTemplate = formViewerTemplateService.getCreateViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
					
					renderView('create', [flash: flash, formInstance: formInstance,
			              domainInstance: domainInstance, multiPart: false], // multiPart:true if form have upload component
			              createViewTemplate)
				}
		    }
		}catch(Exception ex){
			flash.message="${message(code: 'default.error.message')}"
			flash.defaultMessage = flash.message
			redirect(action:'create',params:[formId:formInstance.id, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
		}
	 }
  }

  def show = {
	  Form formInstance
		try{ 
			formInstance = Form.read(params.formId)
			}catch (Exception ex) {
			
				}
			if(!formInstance){
				//render(view:"error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found")]])
				render(view:"/PF/error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
			}
    def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
	if(!domainClass || formInstance.domainClass.updated){
		domainClassService.reloadUpdatedDomainClasses()
		domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
	}
	def domainInstance
	try{
	    domainInstance = domainClass.clazz.createCriteria().get{
			eq 'id',params.id.toLong()
		}
	}catch(Exception e){
		redirect(controller:'form',action:'list')
		return
	}
    def formName = ""
	try{
		formName = JSON.parse(formInstance.settings)."en".name
	}catch(Exception e){}
	
    if (!domainInstance) {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
	  flash.defaultMessage = flash.message
      redirect(action: "list", params: [formId: params.formId])
    }
    else {
		if(request['isMobile']){
			def formAdmin = FormAdmin.findByForm(formInstance)
			def fields = new HashMap()
			formInstance.fieldsList?.each { f ->
				fields.put( f.name,f)
			}
			render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, domainClass:domainClass,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
			return
		}else{
//			formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
			renderView('show', [flash: flash, formInstance: formInstance,
			    domainInstance: domainInstance, domainClass: domainClass],
			    formViewerTemplateService.getShowViewTemplate(request, flash, formInstance,domainInstance))
		}
    }
  }

  def edit = {
    Form formInstance
	def domainInstance
	try{
		try{ 
			formInstance = Form.read(params.formId)
		}catch (Exception ex) {
			
		}
		def formForFormAdminCheck
		if(formInstance){
			if(formInstance.formCat == 'S'){
				formForFormAdminCheck = Form.read(params.pfid)
			}else{
				formForFormAdminCheck = formInstance
			}
			def domainInstanceId
			try{
				domainInstanceId = params.id.toLong()
			}catch(Exception e){}
			if(!domainInstanceId || !FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'edit',domainInstanceId)){
				render(view:"/PF/error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
				return
			}
			domainInstance = sqlDomainClassService.get(params.id, formInstance)
		}
		if(!formInstance || !domainInstance){
			render(view:"/PF/error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
			return
		}
		def parentDomainInstance
		if(formInstance.formCat == 'S'){
			if(params.pfid && params.pfii && params.pffn){
				if(!request['isMobile']){
					flash.pfii = params.pfii
					flash.pfid = params.pfid
					flash.pffn = params.pffn
				}
				parentDomainInstance = sqlDomainClassService.get(params.pfii, formForFormAdminCheck)
				if(!formForFormAdminCheck || !parentDomainInstance){
					throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
				}
			}else{
				//throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
				render(view:"/PF/error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
				return
			}
		}
	    def formName = ""
		try{
			formName = JSON.parse(formInstance.settings)."en".name
		}catch(Exception e){}
		
	    if (!domainInstance) {
	      flash.message = "${message(code: 'default.not.found.message', args: [formName, params.id])}"
		  flash.defaultMessage = flash.message
	      redirect(action: "list", params: [formId: params.formId])
	    }
	    else {
			if(request['isMobile']){
				def fields = new HashMap()
				formInstance.fieldsList?.each { f ->
					fields.put( f.name,f)
				}
				def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
				render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
			}else{
	//			formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass)
				renderView('edit', [flash: flash, formInstance: formInstance,
					domainInstance: domainInstance],
					formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance))
			}
	    }
	}catch(Exception ex){
		flash.message="${message(code: 'default.error.message')}"
		flash.defaultMessage = flash.message
		redirect(action:'edit',id: domainInstance.id,params:[formId:formInstance.id, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
	}
  }

  def update = { 
	    Form formInstance
		def domainInstance
		try{
			try{ 
				formInstance = Form.read(params.formId)
			}catch (Exception ex) {
				
			}
			if(!formInstance){
				render(view:"/PF/error",model:[exception:['message':message(code:'form.not.found',args:[params.formId],'default':"Form not found"),detailMessage:message(code:'form.not.found.detailMessage',args:[params.formId],'default':"Form not found.")]])
				return
			}
			def formForFormAdminCheck
			if(formInstance.formCat == 'S'){
				formForFormAdminCheck = Form.read(params.pfid)
			}else{
				formForFormAdminCheck = formInstance
			}
			def domainInstanceId
			try{
				domainInstanceId = params.id.toLong()
			}catch(Exception e){}
			if(!domainInstanceId || !FormUtils.isFormAccessible(formForFormAdminCheck, session['user'], 'edit',domainInstanceId)){
				render(view:"/PF/error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
				return
			}
			def parentDomainInstance
			if(formInstance.formCat == 'S'){//current form is subform
				if(params.pfid && params.pfii && params.pffn){
					if(!request['isMobile']){
						flash.pfii = params.pfii
						flash.pfid = params.pfid
						flash.pffn = params.pffn
					}
					formForFormAdminCheck = Form.read(params.pfid?.toLong())
					parentDomainInstance = sqlDomainClassService.get(params.pfii, formForFormAdminCheck)
					if(!parentDomainInstance){
						//throw new Exception(message(code:'parent.formInstance.not.found',args:[],'default':"Sub form must be entered through parent form only, here the instance not found."))
						render(view:"/PF/error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
					}
				}else{
					//throw new Exception(message(code:'parent.form.not.found',args:[],'default':"Sub form must be entered through parent form only."))
					render(view:"/PF/error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
					return
				}
			}else{
				formForFormAdminCheck = formInstance
			}
		domainInstance = sqlDomainClassService.get(params.id, formInstance)
		def currentDataInstance = sqlDomainClassService.populate(params, formInstance,domainInstance,request)
		def formName = ""
		try{
			formName = JSON.parse(formInstance.settings)."en".name
		}catch(Exception e){}
		
	    if (domainInstance) {
	      if (params.version) {
	        def version = params.version.replaceAll(",","").toLong()
	        if (domainInstance.version > version) {
				domainInstance.errors.add([name:"version", code:"default.optimistic.locking.failure", args:[formName] as Object[], defaultMessage:"Another user has updated this ${formName} while you were editing"])
				if(request['isMobile']){
					def fields = new HashMap()
					formInstance.fieldsList?.each { f ->
						fields.put( f.name,f)
					}
					def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
					render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, domainClass:null,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
				}else{
			          renderView('edit', [flash: flash, formInstance: formInstance,
			                  domainInstance: domainInstance],
			                  formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance)))
				}
	          return
	        }
	      }
		  	domainInstance = currentDataInstance
			def itemsBought
			def fieldsList = formInstance.fieldsList
			def fieldNameSetting_MapList = []
			fieldsList?.each { field ->
					def settings = grails.converters.JSON.parse(field.settings)
					def fieldName = settings."en".label
					//fields.put( field.name,field)
					if ( field.type == "PlainText" ) {
						def fieldVal = params[field.name]
						if ( fieldVal )
							domainInstance."${fieldVal}"= "true"
					}else if ( field.type == "CheckBox" ) {
						def fieldVal = params.list(field.name)
					   def v
					   if ( fieldVal ){
						   v = fieldVal as List
							domainInstance."${field.name}"= (v as JSON).toString()
					   }else
						   domainInstance."${field.name}"= null
					}else if(field.type == "FileUpload"){
					 	def keyValueMap = [:]
						 keyValueMap."${field.name}" = settings
					 	fieldNameSetting_MapList.add(keyValueMap)
					 }else if(field.type == "Paypal"){
					 	itemsBought = domainInstance."${field.name}_bought"
					 }
			}
			
			def totalFileSize = 0
			boolean breakTheAction = false
		    		
		    if (request instanceof DefaultMultipartHttpServletRequest) {
		        request.multipartFiles.each {k, v ->
		            if (k) {
						List<MultipartFile> files = new ArrayList()
						def fieldName = k.replace('_file','')
		                if (v instanceof List) {
		                    v.each {MultipartFile file ->
								if(file && file.size>0){
									files << file
								}
		                    }
		                } else {
							MultipartFile file = v
							if(file && file.size>0){
								files << file
							}
		                }
						files.each{MultipartFile file->
							if(!breakTheAction){
								def fileSize = file.size/1024
								def field = fieldNameSetting_MapList.find{it."${fieldName}"}
								if(field){
									def settings = field.getAt(fieldName)
									def maxSize = settings.maxSize
									if(settings.unit == "MB"){
										maxSize = settings.maxSize*1024
									}
									if(fileSize > maxSize){
										domainInstance.errors.add([name:fieldName, code:"default.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for property ${settings.en.label} can not be greater than ${settings.maxSize} ${settings.unit}"])
										if(request['isMobile']){
											def fields = new HashMap()
											formInstance.fieldsList?.each { f ->
												fields.put( f.name,f)
											}
											def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
											render(view:'edit_m', model:[formName:formName,domainInstance: domainInstance,fields:fields,formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
										}else{
											def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
											renderView('edit', [flash: flash, formInstance: formInstance,
													  domainInstance: domainInstance], editViewTemplate)
										}
										breakTheAction = true
										return
									}
									totalFileSize += fileSize
								}else{
									domainInstance.errors.add([name:"version", code:"default.form.changed", args:[formName] as Object[], defaultMessage:"Someone might have changed the form while you were entering data. Please try again."])
									if(request['isMobile']){
										def fields = new HashMap()
										formInstance.fieldsList?.each { f ->
											fields.put( f.name,f)
										}
										def formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
										render(view:'edit_m', model:[formName:formName,domainInstance: domainInstance,fields:fields, domainClass:null, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
									}else{
										def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
										renderView('edit', [flash: flash, formInstance: formInstance,
												  domainInstance: domainInstance, domainClass: null], editViewTemplate)
									}
									breakTheAction = true
									return
								}
							}
						}
		            }
		        }
		    }
			if(breakTheAction){
				return
			}
			
			//If the validation fails then clientService.getTotalAttachmentSize() method throws exception. Below we need to check the validations first
	//		if (domainInstance.hasErrors() || !domainInstance.validate()) {
	//			if(request['isMobile']){
	//				def fields = new HashMap()
	//				formInstance.fieldsList?.each { f ->
	//					fields.put( f.name,f)
	//				}
	//				def formAdmin = FormAdmin.findByForm(formInstance)
	//				render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance, domainClass:domainClass,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
	//			}else{
	//					def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
	//				  renderView('edit', [flash: flash, formInstance: formInstance,
	//						  domainInstance: domainInstance, domainClass: domainClass], editViewTemplate)
	//			}
	//			return
	//		}
			
			if(totalFileSize>0){
				Client myClient = Client.get(springSecurityService.currentUser.userTenantId)
				def earlyDataLength = clientService.getTotalAttachmentSize( myClient.id)
				if( ( totalFileSize*1024 + earlyDataLength )/(1024*1024) > myClient.maxAttachmentSize){
					domainInstance.errors.add([name:"version", code:"default.client.fileSize.exceed", args:[formName] as Object[], defaultMessage:"File size for your client exceeded. Please contact administrator"])
					if(request['isMobile']){
						def fields = new HashMap()
						formInstance.fieldsList?.each { f ->
							fields.put( f.name,f)
						}
						def formAdmin = FormAdmin.findByForm(formInstance)
						render(view:'edit_m', model:[formName:formName,domainInstance: domainInstance,fields:fields, domainClass:null, formInstance:formInstance, multiPart:false,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
					}else{
							def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
						  renderView('edit', [flash: flash, formInstance: formInstance,
								  domainInstance: domainInstance, domainClass: null], editViewTemplate)
					}
					return
				}
			}
	//      formViewerTemplateService.handleDomainClassSourceUpdated(formInstance, domainClass, domainInstance)
			if ( springSecurityService.currentUser )
				domainInstance.updatedBy = springSecurityService.currentUser
			
	      if (!domainInstance.errors && sqlDomainClassService.update(domainInstance,formInstance)) {
			  def resultAttachment = attachUploadedFilesTo(formInstance,domainInstance.id)
			  def user = springSecurityService.currentUser?:null
			  def formAdmin
			  if(parentDomainInstance){
				  formAdmin = FormAdmin.findByForm(formForFormAdminCheck)
				  WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Update",formInstance)
			  }else{
			  		formAdmin = FormAdmin.findByForm(formInstance)
					WebHookAdaptor.sendData(formAdmin,domainInstance,params,resultAttachment?.uploadedFiles,"Update")
			  }
			  if(itemsBought){
				  Payment p = Payment.findByFormIdAndInstanceId("${formInstance.id}","${domainInstance.id}")
				  p?.delete(flush:true)
				  def paymentField = domainInstance.paymentField
				  def paymentFieldSettings = domainInstance.paymentFieldSettings
				  Payment payment = domainInstance.payment
				  payment.formId = formInstance.id
				  payment.instanceId = domainInstance.id
				  payment.buyerId = domainInstance.updatedBy.id
				  payment.currency = Currency.getInstance(paymentFieldSettings.curr)
				  if(paymentFieldSettings.itemForm){
					  itemsBought.each{item->
						  payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
							  if(paymentItem.itemNumber == "${item.id}"){
								  paymentItem.amount = item[paymentFieldSettings.iaf]
								  def itemName = item[paymentFieldSettings.inf]?:"Item ${itemIdx}"
								  if(itemName.length()>126){
									  itemName = itemName.substring(0,123) + "..."
								  }
								  paymentItem.itemName = itemName
								  paymentItem.itemNumber = item.id+"_"+paymentFieldSettings.itemForm
							  }
						  }
					  }
				  }else{
					  payment.paymentItems.eachWithIndex{paymentItem,itemIdx->
						  paymentItem.amount = domainInstance[paymentFieldSettings.iaf]
						  paymentItem.itemNumber = domainInstance.id+"_"+formInstance.id
					  }
				  }
				  if (payment?.validate()) {
					  request.payment = payment
					  payment.save(flush: true)
					  def config = grailsApplication.config.grails.paypal
					  def server
					  if(paymentFieldSettings.test){
						  server = config.testServer
					  }else{
						  server = config.server
					  }
					  def baseUrl = grailsApplication.config.grails.serverURL
					  def login = paymentFieldSettings.emid
					  if (!server || !login) {
						  render(view:"/PF/error",model:[exception:['message':message(code:'form.paypal.misconfig',args:[],'default':"PayPal misconfigured"),detailMessage:message(code:'form.paypal.misconfig.detailMessage',args:[],'default':"Paypal misconfigured! You need to specify the Paypal account email. Refer to documentation.")]])
						  return
					  }
		  
					  def commonParams = [buyerId: payment.buyerId, transactionId: payment.transactionId,returnAction: 'edit',returnController: 'formViewer',cancelAction: 'edit',cancelController:'formViewer']
					  def notifyURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'notify', params: commonParams).encodeAsURL()
					  def successURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'success', params: commonParams).encodeAsURL()
					  def cancelURL = g.createLink(absolute: baseUrl==null, base: baseUrl, controller: 'paypal', action: 'cancel', params: commonParams).encodeAsURL()
		  
					  def url = new StringBuffer("$server?")
					  url << "business=$login&"
					  if(paymentFieldSettings.itemForm){
						  url << "cmd=_cart&upload=1&"
						  payment.paymentItems.eachWithIndex {paymentItem, i ->
							  def itemId = i + 1
							  url << "item_name_${itemId}=${paymentItem.itemName}&"
							  url << "item_number_${itemId}=${paymentItem.itemNumber}&"
							  url << "quantity_${itemId}=${paymentItem.quantity}&"
							  url << "amount_${itemId}=${paymentItem.amount}&"
							  if (payment.discountCartAmount == 0 && paymentItem.discountAmount > 0) {
								  url << "discount_amount_${itemId}=${paymentItem.discountAmount}&"
							  }
						  }
					  }else{
						  url << "cmd=_xclick&"
						  payment.paymentItems.eachWithIndex {paymentItem, i ->
							  url << "item_name=${paymentItem.itemName}&"
							  url << "amount=${paymentItem.amount}&"
						  }
					  }
					  
					  url << "currency_code=${payment.currency}&"
					  url << "notify_url=${notifyURL}&"
					  url << "return=${successURL}&"
					  url << "cancel_return=${cancelURL}"
		  
					  log.debug "Redirection to PayPal with URL: $url"
					  redirect(url: url)
					  return
				  }
				  else {
					  //Handle in case validation fails
				  }
			  }
			  if(params.subFormid && params.subFormid !="null"  && params.subFormfn && params.subFormfn !="null"){
				  redirect(action:'create',params:[formId:params.subFormid, pfii:domainInstance.id, pfid:formInstance.id, pffn:params.subFormfn])
				return
			}
			def showMessage = "${message(code: 'default.form.updated.message', args: [formName?:'', domainInstance.id])}"
			flash.message = showMessage
			flash.defaultMessage = flash.message
			//No need to handle for mobile view as we are redirecting it, it'll be taken care when the request is received again.
			if(formInstance.formCat == 'S'){
				redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
			}else{
	        	redirect(action: "edit", id: domainInstance.id, params: [formId: params.formId])
			}
	      }
	      else {
			  if(request['isMobile']){
				  def fields = new HashMap()
				  formInstance.fieldsList?.each { f ->
					  fields.put( f.name,f)
				  }
				  def formAdmin = FormAdmin.findByForm(formInstance)
				  render(view:'edit_m', model:[formInstance:formInstance,formName:formName,fields:fields,domainInstance: domainInstance,rtsaf:ROLES_TO_SHOW_ALL_FIELDS,isEditable:formAdmin.openForEdit])
			  }else{
			  		def editViewTemplate = formViewerTemplateService.getEditViewTemplate(request, flash, formInstance, domainInstance).replace('@FIELD_ERRORS',getErrorsForForm(formInstance, domainInstance))
					renderView('edit', [flash: flash, formInstance: formInstance,
			                domainInstance: domainInstance], editViewTemplate)
			  }
	      }
	    }
	    else {
	      render(view:"/PF/error",model:[exception:['message':message(code:'parent.form.not.found',args:[],'default':"No parent found"),detailMessage:message(code:'parent.form.not.found.detailMessage',args:[],'default':"Sub form must be entered through parent form only.")]])
						return
	    }
	}catch(Exception ex){
		flash.message="${message(code: 'default.error.message')}"
		flash.defaultMessage = flash.message
		redirect(action:'edit',id: domainInstance.id,params:[formId:formInstance.id, pfii:params.pfii, pfid:params.pfid, pffn:params.pffn])
	}
  }

  /*def delete = {
    Form formInstance = Form.read(params.formId)
    def domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
	if(!domainClass || formInstance.domainClass.updated){
		domainClassService.reloadUpdatedDomainClasses()
		domainClass = grailsApplication.getDomainClass(formInstance.domainClass.name)
	}
    def domainInstance = domainClass.clazz.createCriteria().get{
		eq 'id',params.id.toLong()
	}

    def formName = ""
	try{
		formName = JSON.parse(formInstance.settings)."en".name
	}catch(Exception e){}
	
    if (domainInstance) {
      try {
		domainInstance.removeAttachments()
        domainInstance.delete(flush: true)
        flash.message = "${message(code: 'default.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
		flash.defaultMessage = flash.message
        redirect(action: "list", params: [formId: params.formId])
      }
      catch (org.springframework.dao.DataIntegrityViolationException e) {
        flash.message = "${message(code: 'default.not.deleted.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
		flash.defaultMessage = flash.message
        redirect(action: "edit", id: params.id, params: [formId: params.formId])
      }
    }
    else {
      flash.message = "${message(code: 'default.not.found.message', args: [message(code: '${domainClass.propertyName}.label', default: formName?:domainClass.name), params.id])}"
	  flash.defaultMessage = flash.message
      redirect(action: "list", params: [formId: params.formId])
    }
  }*/
	
	private String getErrorsForForm(Form form,Object domainInstance){
		FastStringWriter out = new FastStringWriter()
		def formName
		def fields = new HashMap()
		formName = JSON.parse(form.settings)."en".name
		form.fieldsList?.each { field ->
			fields.put( field.name,field)
		}
		out << '<div class="errors">'
		domainInstance.errors.each{ error ->
			
			if(error.name != 'version')
			{
				def fieldWithSettings = fields?.get(error.name)
				if(fieldWithSettings){
					if(fieldWithSettings.type=="Paypal"){
						out << '<li>'+message(code:error.code, args:error.args as List, default:error.defaultMessage)+'</li>';
					}else{
						def fieldName = JSON.parse(fieldWithSettings.settings)?.en?.label
						out << '<li>'+message(code:error.code, args:[fieldName,formName], default:error.defaultMessage)+'</li>';
					}
				}
			}else{
				out << '<li>'+message(code:error.code, args:["version",formName], default:error.defaultMessage)+'</li>';
			}
			
		}
		out << '</div>'
		return out.toString()
	}
	
	def afterInterceptor = { model, modelAndView ->
		if (request['isMobile'] && modelAndView != null && modelAndView.viewName?.indexOf("error")>-1) {
			modelAndView.viewName = modelAndView.viewName + "_m"
		}
	}
		def delete = {
			def domainInstance   
		 	def formName
			def isDetete= false
			try{
				Form form
				try{
					form = Form.read(params.formId?.toLong())
				}catch(Exception e){
					log.error "Form not found"+e
					throw new Exception("Form not found")
				}
				formName = JSON.parse(form.settings)."en".name
				def parentForm
				if(form){
					boolean accessible
					domainInstance = sqlDomainClassService.get(params.id, form)
					if(session['user'].authorities*.authority.contains(Role.ROLE_TRIAL_USER)){
						accessible=form.createdBy.id==session['user'].id       
					  }else{
					    accessible= domainInstance?.created_by_id==session['user'].id
					  }
					  if(!accessible)
						if(form.formCat != 'S' && (!domainInstance || !FormUtils.isFormAccessible(form, session['user'], 'delete',domainInstance.id))){
							render(view:"/PF/error",model:[exception:['message':message(code:'form.not.accessible','default':'Form not accessible'),detailMessage:message(code:'form.not.accessible.detailMessage','default':'Form is currently not accessible.')]])
							return
						}
					if(form.formCat == 'S'){
						if(params.pfid && params.pfii && params.pffn){
							parentForm = Form.read(params.pfid?.toLong())
							isDetete=sqlDomainClassService.deleteSubForm(parentForm.name, domainInstance.id, params.pffn, form)
						}
					}else{
						isDetete=sqlDomainClassService.delete(domainInstance.id,form)
					}
				}else{
					flash.message = message(code:'domainClass.not.found','default':'Form not found')
					flash.defaultMessage = flash.message
					redirect(controller:'dashboard')
				}
				if(isDetete){
					if( parentForm ){
						WebHookAdaptor.sendData(FormAdmin.findByForm(parentForm),null,params,null,"Delete",form)
					}else{
						WebHookAdaptor.sendData(FormAdmin.findByForm(form),null,params,null,"Delete")
					}
					def activityFeedConfig = ActivityFeedConfig.findByConfigName(form.name+"."+form.name)
					def activityFeed = ActivityFeed.findByShareIdAndConfig(domainInstance.id,activityFeedConfig)
					if(activityFeed)
						activityFeed.delete(flush:true)
					flash.message =form.formCat == 'S'? "${message(code: 'default.deleted.message', args: [formName?:domainInstance.id, domainInstance.id])}":'default.deleted.message';
					flash.args = [formName?:domainInstance.id, domainInstance.id]
					flash.defaultMessage = "Successfully Deleted"
					if(form.formCat == 'S'){
						redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
					}else{
						redirect(action: "list", id: params.id, params:[formId:params.formId])
					}
				}else{
					flash.message = 'default.not.deleted.message'
					flash.args = [formName?:domainInstance.id, domainInstance.id]
					flash.defaultMessage = "Form not Deleted"
					if(form.formCat == 'S'){
						redirect(action: "edit", id: params.pfii, params: [formId: params.pfid])
					}else{
						redirect(action: "list", id: params.id, params:[formId:params.formId])
					}
				}
			}catch(Exception ex){
				flash.message = "Error Occured"+ex.message
				flash.defaultMessage = flash.message
				redirect(action: "list",id: params.id, params:[formId:params.formId] )
		  }
		}
}
