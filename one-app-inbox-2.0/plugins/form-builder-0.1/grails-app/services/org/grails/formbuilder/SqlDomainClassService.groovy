package org.grails.formbuilder

import java.text.SimpleDateFormat;
import java.util.regex.Matcher
import java.util.regex.Pattern

import javax.mail.internet.AddressException;
import javax.mail.internet.InternetAddress

import grails.util.GrailsNameUtils

import net.tanesha.recaptcha.ReCaptchaImpl;
import net.tanesha.recaptcha.ReCaptchaResponse;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.grails.paypal.Payment;
import org.grails.paypal.PaymentItem;
import groovy.sql.Sql
import com.google.i18n.phonenumbers.NumberParseException;
import com.google.i18n.phonenumbers.PhoneNumberUtil;
import com.google.i18n.phonenumbers.PhoneNumberUtil.PhoneNumberFormat;
import com.google.i18n.phonenumbers.Phonenumber.PhoneNumber;
import com.macrobit.grails.plugins.attachmentable.core.Attachmentable;
import com.macrobit.grails.plugins.attachmentable.domains.Attachment;
import com.oneapp.cloud.core.*;
import grails.plugin.multitenant.core.util.TenantUtils

import  com.oneapp.cloud.core.User
import grails.converters.JSON
import groovy.sql.Sql;
//import org.grails.paypal.Payment
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class SqlDomainClassService {
	def grailsApplication
	def dynamicDomainService
	static transactional = false
	def sessionFactory
	def attachmentableService
	def dataSource
	def springSecurityService
	def utilService
	def notiService

	private static Pattern pattern;
	private static Matcher matcher;
	private static final String EMAIL_PATTERN = '^[_A-Za-z0-9-]+(\\.[_A-Za-z0-9-]+)*@[A-Za-z0-9]+(\\.[A-Za-z0-9]+)*(\\.[A-Za-z]{2,})$';
	private static final def INTERNATIONAL_PHONE_PATTERN = /^((\+)?[1-9]{1,2})?([-\s\.])?((\(\d{1,4}\))|\d{1,4})(([-\s\.])?[0-9]{1,12}){1,2}$/;


	def newInstance(Form form){
		def domainInstance=[:]
		Sql sql = new Sql(dataSource)
		try{
			form.fieldsList.each{Field field->
				domainInstance."${field.name}"=null
				if(field.type == 'LikeDislikeButton'){
					domainInstance."${field.name}Like" = ''
					domainInstance."${field.name}Dislike" = ''
					def likesDislikes = (Map)sql.firstRow("select count(case "+field.name+" when 'Like' then 1 else null end) as '"+field.name+"Like',count(case "+field.name+" when 'Dislike' then 1 else null end) as '"+field.name+"Dislike' from "+form.name)
					likesDislikes.each{k,v->
						domainInstance."${k}"=v
					}
				}
			}
		}catch(Exception e){
			println "SqlDomainClassService-newInstance: "+e
			return false
		}finally{
			sql.close();
		}
		return domainInstance
	}

	def get(def id, Form form){
		def domainInstance
		def domainInstanceMap = [errors:[]]
		Sql sql = new Sql(dataSource)
		try{
			def getQuery = "select * from ${form.name} where id = ?"
			domainInstance = (Map)(sql.firstRow(getQuery,["${id}".toLong()]))
			if(domainInstance){
				domainInstanceMap.putAll(domainInstance)
				domainInstance = domainInstanceMap
				form.fieldsList.each{Field field->
					if(field.type == 'LikeDislikeButton'){
						domainInstance."${field.name}Like" = ''
						domainInstance."${field.name}Dislike" = ''
						def likesDislikes = (Map)sql.firstRow("select count(case "+field.name+" when 'Like' then 1 else null end) as '"+field.name+"Like',count(case "+field.name+" when 'Dislike' then 1 else null end) as '"+field.name+"Dislike' from "+form.name)
						likesDislikes.each{k,v->
							domainInstance."${k}"=v
						}
					}else if(field.type == 'SubForm'){
						domainInstance."${field.name}" = []
						try{
							def settings = JSON.parse(field.settings)
							if(settings.subForm){
								Form subForm = Form.read(settings.subForm)
								if(subForm)
									domainInstance."${field.name}" = sql.rows("select * from "+subForm.name+" where id in (select "+field.name+"_long from "+form.name+"_"+field.name+" where "+form.name+"_id = "+domainInstance.id+")")
							}
						}catch(Exception e){
							println "SqlDomainClassService-get: in Case SubForm fieldName(${field.name})"+e
						}
					}else if(field.type == 'Likert'){
						try{
							domainInstance."${field.name}" = JSON.parse(domainInstance."${field.name}")
						}catch(Exception e){
							domainInstance."${field.name}" = []
							//println "SqlDomainClassService-get: in Case Likert fieldName(${field.name}) form ${form.name} domainInstance ${domainInstance} id ${id}"+e
						}
					}
				}
			}
		}catch(Exception e){
			println "SqlDomainClassService-get: top most tryCatch"+e
		}finally{
			sql.close();
		}
		return domainInstance
	}

	def listSubForm(def id, Form form, Form subForm,String fieldName){
		def subFormInstanceList = []
		Sql sql = new Sql(dataSource)
		try{
			subFormInstanceList = sql.rows("select * from "+subForm.name+" where id in (select "+fieldName+"_long from "+form.name+"_"+fieldName+" where "+form.name+"_id = "+id+")")
		}catch(Exception e){
			println "SqlDomainClassService-listSubForm: "+e
		}finally{
			sql.close();
		}
		return subFormInstanceList
	}

	def save(def domainInstance, Form form){
	 	Sql sql = new Sql(dataSource)
		try{
			def currentTime = new Date()
			domainInstance.dateCreated = currentTime
			domainInstance.lastUpdated = currentTime
			def valueParams = []
			def fieldNames = []
			def prepQueryQMark = []
			form.fieldsList.each{Field field->
				if( field.type != "SubForm" ){
					fieldNames << field.name
					prepQueryQMark << "?"
					if(domainInstance."${field.name}"!=null &&domainInstance."${field.name}"!=""){
						if( field.type == "Likert" || field.type == 'AddressField'||field.type == 'NameTypeField'){
							domainInstance."${field.name}" = domainInstance."${field.name}".toString()
							valueParams << domainInstance."${field.name}"
						}else{
							valueParams << domainInstance."${field.name}"
						}
					}else{
						valueParams << null
					}
				}
			}
			fieldNames << "version"
			prepQueryQMark << "?"
			valueParams << 0
			fieldNames << "created_by_id"
			prepQueryQMark << "?"
			valueParams << domainInstance.createdBy.id
			fieldNames << "date_created"
			prepQueryQMark << "?"
			valueParams << currentTime
			fieldNames << "last_updated"
			prepQueryQMark << "?"
			valueParams << currentTime
			def insertPreparedQuery = "insert into ${form.name} (${fieldNames.join(', ')}) values(${prepQueryQMark.join(', ')})"
			def resultSet = sql.executeInsert(insertPreparedQuery, valueParams)
			resultSet.each{obj ->
				domainInstance.id = obj.get(0)
				domainInstance.version = 0
			}
			FormAdmin formAdmin = FormAdmin.findByForm(form)
			def uniqueId
			if(form.formCat != 'S'){
				UUID uuid = UUID.randomUUID()
				uniqueId = uuid.toString()[0..7]
				def uniqueFormEntry = new UniqueFormEntry(formId:form.id,instanceId:domainInstance.id,uniqueId:uniqueId).save()
			}
			if(formAdmin){
				if(formAdmin.trackChanges){
					def dataMap = [:]
					SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a zzz");
					dataMap.putAll(domainInstance)
					dataMap.lastUpdated = dateFormat.format(domainInstance.lastUpdated)
					def data = dataMap as JSON
					new FormHistory(formId:form.id,instanceId:domainInstance.id,data:data.toString(),action:'create').save()
				}
				notiService.notifyFormInstance(formAdmin, domainInstance, uniqueId, "create")
			}
			   formMaxEntryMessage(form)

			return true//or false
		}catch(Exception e){
			domainInstance.errors.add([name:'version',code:'',args:[]as Object[],defaultMessage:"${e}"])
			return false
		}finally{
			sql.close();
		}
	}

	def addSubFormInstance(def sFId,def pFId,def pFFN,Form parentForm){
		Sql sql = new Sql(dataSource)
		try{
			def idx = (sql.firstRow("select count("+pFFN+"_idx) as idx from "+parentForm.name+"_"+pFFN+" where "+parentForm.name+"_id = "+pFId)).idx
			def valueParams = []
			def fieldNames = []
			def prepQueryQMark = []
			fieldNames << "${parentForm.name}_id"
			prepQueryQMark << "?"
			valueParams << "${pFId}".toLong()
			fieldNames << "${pFFN}_long"
			prepQueryQMark << "?"
			valueParams << "${sFId}".toLong()
			fieldNames << "${pFFN}_idx"
			prepQueryQMark << "?"
			valueParams << "${idx}".toInteger()
			def insertPreparedQuery = "insert into ${parentForm.name}_${pFFN} (${fieldNames.join(', ')}) values(${prepQueryQMark.join(', ')})"
			sql.executeInsert(insertPreparedQuery, valueParams)
			return true//or false
		}catch(Exception e){
			return false
		}finally{
			sql.close();
		}
	}

	def update(def domainInstance, Form form){
		Sql sql = new Sql(dataSource)
		try{
			def currentTime = new Date()
			domainInstance.lastUpdated = currentTime
			def valueParams = []
			def isUpdate = false
			def updateQuery = "update ${form.name} set "
			form.fieldsList.each{Field field->
				if( field.type != "SubForm" ){
					if(isUpdate)
						updateQuery += ","
					updateQuery += "${field.name} = ?"
					isUpdate = true
					if(domainInstance."${field.name}"!=null &&domainInstance."${field.name}"!=""){
						if( field.type == "Likert" ||  field.type == 'AddressField'||  field.type == 'NameTypeField'){
							domainInstance."${field.name}" = domainInstance."${field.name}".toString()
							valueParams << domainInstance."${field.name}"
						}else{
							valueParams << domainInstance."${field.name}"
						}
					}else{
						valueParams << null
					}
				}
			}
			if(updateQuery)
				updateQuery += ","
			updateQuery += "version=?,updated_by_id=?,last_updated=? where id = ${domainInstance.id}"
			valueParams << "${domainInstance.version}".toLong()+1
			valueParams << domainInstance.updatedBy.id
			valueParams << currentTime
			def rowsUpdated = sql.executeUpdate(updateQuery, valueParams)
			if(rowsUpdated == 1){
				domainInstance.version = "${domainInstance.version}".toLong()+1
				FormAdmin formAdmin = FormAdmin.findByForm(form)
				def uniqueId
				if(form.formCat != 'S'){
					def uniqueFormEntry = UniqueFormEntry.findByFormIdAndInstanceId(form.id,domainInstance.id)
					if(uniqueFormEntry){
						uniqueId = uniqueFormEntry.uniqueId
					}else{
						UUID uuid = UUID.randomUUID()
						uniqueId = uuid.toString()[0..7]
						uniqueFormEntry = new UniqueFormEntry(formId:form.id,instanceId:domainInstance.id,uniqueId:uniqueId).save()
					}
				}
				if(formAdmin){
					if(formAdmin.trackChanges){
						def dataMap = [:]
						SimpleDateFormat dateFormat = new SimpleDateFormat("MM/dd/yyyy hh:mm a zzz");
						dataMap.putAll(domainInstance)
						dataMap.lastUpdated = dateFormat.format(domainInstance.lastUpdated)
						def data = dataMap as JSON
						new FormHistory(formId:form.id,instanceId:domainInstance.id,data:data.toString(),action:'update').save()
					}
				
				notiService.notifyFormInstance(formAdmin, domainInstance, uniqueId, "update")
				}
				return true//or false
			}else{
				return false//or false
			}
		}catch(Exception e){
			return false
		}finally{
			sql.close();
		}
	}

	def list(Form form,def showOwnCreated = true, def criteria = null){//by default will return all the instances with no criteria
		def listTotalMap = [totalCount:0,instanceList:[]]
		def domainInstanceList = []
		Sql sql = new Sql(dataSource)
		try{
			def getQuery = "select * from "+form.name
			def criteriaUser = []
			def whereClause = ""
			if(!showOwnCreated){
				whereClause += " where created_by_id = ?"
				criteriaUser << springSecurityService.currentUser?.id
			}
			getQuery += whereClause
			if(criteriaUser){
				domainInstanceList = sql.rows(getQuery,criteriaUser)
			}else{
				domainInstanceList = sql.rows(getQuery)
			}
			if(criteria?.sort){
				if(criteria?.order == 'desc'){
					domainInstanceList.sort {a,b->
						if(a."${criteria?.sort}"<b."${criteria?.sort}") return 1
						else return 0
					}
				}else{
					domainInstanceList.sort {a,b->
						if(a."${criteria?.sort}">b."${criteria?.sort}") return 1
						else return 0
					}
				}
			}
			listTotalMap.totalCount = domainInstanceList.size()
			if(criteria?.offset!=null && criteria?.max && domainInstanceList){
				int fromIndex = "${criteria.offset}".toInteger()
				fromIndex = (listTotalMap.totalCount>fromIndex?fromIndex:0)
				int toIndex = fromIndex + criteria.max
				toIndex = (listTotalMap.totalCount>toIndex?toIndex:listTotalMap.totalCount)
				listTotalMap.instanceList = domainInstanceList.subList(fromIndex, toIndex)
			}else{
				listTotalMap.instanceList = domainInstanceList
			}
		}catch(Exception e){
			println "SqlDomainClassService-list: top most tryCatch"+e
		}finally{
			sql.close();
		}
		return listTotalMap
	}

	def responseCount(Form form,def showOwnCreated = true){
		def total = 0
		Sql sql = new Sql(dataSource)
		try{
			def result
			def getQuery = "select count(id) as total from "+form.name
			def criteriaUser = []
			def whereClause = ""
			if(!showOwnCreated){
				whereClause += " where created_by_id = ?"
				criteriaUser << springSecurityService.currentUser?.id
			}
			getQuery += whereClause
			if(criteriaUser){
				result = sql.firstRow(getQuery,criteriaUser)
			}else{
				result = sql.firstRow(getQuery)
			}
			total = result["total"]
		}catch(Exception e){
			println "SqlDomainClassService-responseCount: top most tryCatch"+e
		}finally{
			sql.close();
		}
		return total
	}

	def responseCountGroup(List<Form> formList,def showOwnCreated = true){
		def totalList = []
		Sql sql = new Sql(dataSource)
		try{
			formList.each{Form form->
				def total = 0
				try{
					def result
					def getQuery = "select count(id) as total from "+form.name
					def criteriaUser = []
					def whereClause = ""
					if(!showOwnCreated){
						whereClause += " where created_by_id = ?"
						criteriaUser << springSecurityService.currentUser?.id
					}
					getQuery += whereClause
					if(criteriaUser){
						result = sql.firstRow(getQuery,criteriaUser)
					}else{
						result = sql.firstRow(getQuery)
					}
					total = result["total"]
				}catch(Exception e){
					println "SqlDomainClassService-responseCountGroup: for form(${form?.id})"+e
				}
				totalList << [id:form.id,name:form.toString(),count:total]
			}
		}catch(Exception e){
			println "SqlDomainClassService-responseCountGroup: top most tryCatch"+e
		}finally{
			sql.close();
		}
		return totalList
	}

	def validate(def instance, Form form){
		//TO DO
		return instance
	}

	//dI is the domainInstance already fetched using get in case of update action
	def populate(def params, Form form, def dI = null, def request){
		def domainInstance = [errors:[]]
		Sql sql = new Sql(dataSource)
		try{
			form.fieldsList.each{Field field->

				String dateFormat = ConfigurationHolder.config.format.date
				SimpleDateFormat sdf = new SimpleDateFormat(dateFormat?:"MM/dd/yyyy");
				def settings = JSON.parse(field.settings)
				try{
					params[field.name] = params[field.name]?.trim()
				}catch(Exception e){}

				if( field.type == 'SingleLineNumber' || (field.type == 'FormulaField' && settings.en.newResultType == 'NumberResult') ) {
					try{
						if(params[field.name]){
							def numflag=false
							def validflag=true
							def maxRange = 99999999999999999.99
							try{
								maxRange = settings.maxRange.toBigDecimal()?:maxRange
								if(maxRange > 99999999999999999.99){
									throw new Exception()
								}
							}catch(Exception e){
								maxRange = 99999999999999999.99
							}
							def minRange = settings.minRange?.toBigDecimal()?:0
							def number=params[field.name]?.replaceAll(",","")
							def data=number.toBigDecimal()
							if(number.length()>20){
								if(number.indexOf(".")!=-1)
									number=number.substring(0,number.indexOf("."))
								numflag=(number.length()<=17)?true:false
							}else{
								numflag=true
							}
							if(maxRange > 0){
								if(data > maxRange){
									domainInstance.errors.add([name:field.name, code:"default.length.message", args:[]as Object[], defaultMessage:"Value in Field {0} must be less then ${maxRange}"])
									validflag=false
								}
							}
							if(minRange > 0){
								if(data < minRange){
									domainInstance.errors.add([name:field.name, code:"default.length.message", args:[]as Object[], defaultMessage:"Value in Field {0} must be greater then ${minRange}"])
									validflag=false
								}
							}
							domainInstance."${field.name}" = params[field.name]
							if(numflag){
								if(validflag)
									domainInstance."${field.name}" = data
							}else{
								domainInstance.errors.add([name:field.name, code:"default.length.message", args:[]as Object[], defaultMessage:"Number of digits in Field {0} must be less then 19"])
							}
						}else{
							if(settings.required){
								domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
							}
							domainInstance."${field.name}" = null
						}
					}catch(Exception e){
						domainInstance."${field.name}" = null
						domainInstance.errors.add([name:field.name, code:"typeMismatch.java.math.BigDecimal", args:[]as Object[], defaultMessage:"Field {0} must be a valid number"])
					}
				} else if( field.type == 'SingleLineDate'  || (field.type == 'FormulaField' && settings.en.newResultType == 'DateResult')) {
					try{
						if(!params[field.name]){
							if(params[field.name+"dateMM"] && params[field.name+"dateDD"] && params[field.name+"dateYYYY"]){
								if(params[field.name+"dateMM"]?.length()==1){
									params[field.name+"dateMM"]="0"+params[field.name+"dateMM"]
								}
								if(params[field.name+"dateDD"]?.length()==1){
									params[field.name+"dateDD"]="0"+params[field.name+"dateDD"]
								}
								params[field.name]=params[field.name+"dateMM"]+"/"+params[field.name+"dateDD"]+"/"+params[field.name+"dateYYYY"]
							}
						}
						if(params[field.name]){
							Date d
							if(settings.timeFormat){
								def newSDF = new SimpleDateFormat((dateFormat?:"MM/dd/yyyy ")+settings.timeFormat)
								d = newSDF.parse(params[field.name]+(params[field.name+"Hours"]?:'00')+":"+(params[field.name+"Minutes"]?:'00')+(params[field.name+"Meridian"]?(' '+params[field.name+"Meridian"]):''))
							}else{
								d = sdf.parse(params[field.name])
							}
							if(!params[field.name]?.equals(""+sdf.format(d)?.toString())){
								throw new Exception()
							}
							domainInstance."${field.name}" = d
						}else{
							if(settings.required){
								domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
							}
							domainInstance."${field.name}" = null
						}
					}catch(Exception e){
						domainInstance."${field.name}" = null
						domainInstance.errors.add([name:field.name, code:"typeMismatch.java.math.Date", args:[]as Object[], defaultMessage:"Field {0} must be a valid Date with format '${dateFormat?:'MM/dd/yyyy'}'"])
					}
				} else if( field.type == "SubForm" ){
					domainInstance."${field.name}" = dI?dI."${field.name}":[]
				}else if(field.type == 'LikeDislikeButton'){
					if(params[field.name]){
						domainInstance."${field.name}" = params[field.name]
					}else {
						if(settings.required){
							domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
						}
						domainInstance."${field.name}" = null
					}
					if(!dI){
						def likesDislikes = (Map)sql.firstRow("select count(case "+field.name+" when 'Like' then 1 else null end) as '"+field.name+"Like',count(case "+field.name+" when 'Dislike' then 1 else null end) as '"+field.name+"Dislike' from "+form.name)
						likesDislikes.each{k,v->
							domainInstance."${k}"=v
						}
					}else{
						domainInstance."${field.name}Like" = dI."${field.name}Like"
						domainInstance."${field.name}Dislike" = dI."${field.name}Dislike"
					}
				}else if(field.type == 'Likert'){
					try{
						def likertValList = []
						boolean reqFail = false
						for(def i=0;i<settings.en.rows.size();i++){
							def rowVal = params."${field.name}__${i}_"
							if(rowVal){
								likertValList << rowVal
							}else{
								likertValList << null
								reqFail=true
							}
						}
						if(settings.required && reqFail){
							domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
						}
						domainInstance."${field.name}" = likertValList
					}catch(Exception e){
					}
				}else if( field.type == 'AddressField'){
					def addressFieldMap = [:]
					try{
						addressFieldMap."line1"= (params."${field.name}_line1")?:''
						addressFieldMap."line2"= (params."${field.name}_line2")?:''
						addressFieldMap."city"= (params."${field.name}_city")?:''
						addressFieldMap."state"= (params."${field.name}_state")?:''
						addressFieldMap."zip"=(params."${field.name}_zip")?:''
						addressFieldMap."country"=(params."${field.name}_country")?:''
						if(!(params."${field.name}_line1" && params."${field.name}_city" &&
						params."${field.name}_state" && params."${field.name}_country" && params."${field.name}_zip")){
							if(settings.required){
								domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
							}
						}
	 					domainInstance."${field.name}" = addressFieldMap as JSON
						 if(domainInstance.errors){
		 					domainInstance."${field.name}"=domainInstance."${field.name}"?.toString()
							 }
					}catch (Exception e) {
						println "error in adressField"+e
						domainInstance."${field.name}" =null
					}
				}else if( field.type == 'NameTypeField'){
					def nameFieldMap = [:]
					try{
						nameFieldMap."pre"= (params."${field.name}pre")?:''
						nameFieldMap."fn"= (params."${field.name}fn")?:''
						nameFieldMap."mn"= (params."${field.name}mn")?:''
						nameFieldMap."ln"= (params."${field.name}ln")?:''
						if(!(params."${field.name}fn")){
							if(settings.required){
								domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
							}
						}
	 					domainInstance."${field.name}" = nameFieldMap as JSON
						 if(domainInstance.errors){
		 					domainInstance."${field.name}"=domainInstance."${field.name}"?.toString()
							 }
					}catch (Exception e) {
						println "error in name"+e
						domainInstance."${field.name}" =null
					}
				}else if( field.type == "MultiLineText" || field.type == 'SingleLineText' || field.type == 'Email' || field.type == 'LookUp'){
					try{
						def propertyValue = params[field.name]
						def maxRange = (field.type == "MultiLineText")?10000:256
						def minRange = 0
						try{
							maxRange = settings.maxRange?.toBigDecimal()?:maxRange
						}catch(Exception e){}
						try{
							minRange = settings.minRange?.toBigDecimal()?:0
						}catch(Exception e){}
						if(propertyValue){
							if(propertyValue.size() > 256 && (field.type == 'SingleLineText' || field.type == 'Email')){
								domainInstance.errors.add([name:field.name, code:"invalid.maxSize.message", args:[]as Object[], defaultMessage:"Field {0} exceeds the maximum size"])
							}
							if(settings.restriction == 'lettersonly'){
								if(!(propertyValue ==~ /^[A-Za-z]+$/)){
									domainInstance.errors.add([name:field.name, code:"lettersonly.invalid", args:[]as Object[], defaultMessage:"Field {0} can contain letters only"])
								}
							}else if(settings.restriction == 'alphanumeric'){
								if(!(propertyValue ==~ /^\w+$/)){
									domainInstance.errors.add([name:field.name, code:"alphanumeric.invalid", args:[]as Object[], defaultMessage:"Field {0} can contain letters or digits only"])
								}
							}else if(settings.restriction == 'letterswithbasicpunc'){
								def v = propertyValue ==~ /^[A-Za-z-.,()'\"\s]+$/
								if(!(v )){
									domainInstance.errors.add([name:field.name, code:"letterswithbasicpunc.invalid", args:[]as Object[], defaultMessage:"Field {0} can contain letters or puntuations only"])
								}
							}else if(settings.restriction == 'email'){
								pattern = Pattern.compile(EMAIL_PATTERN);
								matcher = pattern.matcher(propertyValue);
								if(!matcher.matches()){
									domainInstance.errors.add([name:field.name, code:"default.invalid.email.message", args:[]as Object[], defaultMessage:"Field {0} is not a valid e-mail address"])
								}
							}
							if(maxRange > 0 || minRange > 0){
								if(propertyValue.size() > maxRange || propertyValue.size() < minRange)
									domainInstance.errors.add([name:field.name, code:"default.length.message", args:[]as Object[], defaultMessage:"Field {0} value should be between ${minRange} and ${maxRange}"])
							}
							domainInstance."${field.name}" = params[field.name]?:""
						}else{
							if(maxRange > 0 || minRange > 0){
								if(propertyValue.size() > maxRange || propertyValue.size() < minRange)
									domainInstance.errors.add([name:field.name, code:"default.length.message", args:[]as Object[], defaultMessage:"Field [{0}] value should be between ${minRange} and ${maxRange}"])
							}
							if(!domainInstance.errors.name.contains(field.name) && settings.required){
								domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
							}
							domainInstance."${field.name}" = ""
						}
					}catch(Exception e){
						domainInstance."${field.name}" = ""
						domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
					}
				} else if( field.type == "Phone"){
					try{
						def propertyValue = ""
						propertyValue = params[field.name]
						if(propertyValue){
							def tempPropertyValue = propertyValue
							def replaceChars = ["-"," ",")","(","+","."]
							replaceChars.each{ch->
								while(tempPropertyValue.indexOf(ch)>-1){
									tempPropertyValue = tempPropertyValue.replace(ch,"")
								}
							}
							PhoneNumberUtil phoneUtil = PhoneNumberUtil.getInstance();
							boolean isValidNumber = false
							try {
								PhoneNumber usNumberProto = phoneUtil.parse(propertyValue, settings.format)
								isValidNumber = phoneUtil.isValidNumber(usNumberProto)
								if(!isValidNumber){
									domainInstance.errors.add([name:field.name, code:"default.invalid.phone.message", args:[]as Object[], defaultMessage:"Field {0} is not a valid phone number"])
								}else{
									def region = phoneUtil.getRegionCodeForNumber(usNumberProto)
									propertyValue = phoneUtil.format(usNumberProto, settings.format=="ZZ"?PhoneNumberFormat.INTERNATIONAL:PhoneNumberFormat.NATIONAL)
								}
							} catch (NumberParseException e) {
								domainInstance.errors.add([name:field.name, code:"default.invalid.phone.message", args:[]as Object[], defaultMessage:"Field {0} is not a valid phone number"])
							}
							domainInstance."${field.name}" = propertyValue?:""
						}else{
							if(!domainInstance.errors?.name?.contains(field.name) && settings.required){
								domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
							}
							domainInstance."${field.name}" = ""
						}
					}catch(Exception e){
						domainInstance."${field.name}" = ""
						domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
					}
				}else if(field.type=="dropdown"){
					 List<String> optionListList=settings."en".value
					 domainInstance."${field.name}"= optionListList.contains(params[field.name])?params[field.name]:""
					 if(!domainInstance."${field.name}" || domainInstance."${field.name}"==""){
						if(settings.required){
							domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
						}else if(settings.hideFromUser && !params.id && (!currentUser || currentUser.authorities*.authority.contains(Role.ROLE_TRIAL_USER) || currentUser.authorities*.authority.contains(Role.ROLE_USER) )){
							domainInstance."${field.name}" = settings."en".value[0]
						}
					 }
				}else if(field.type == 'Paypal'){
					try{
						if(settings.itemForm){
							Payment payment = new Payment(params[field.name])
							List<PaymentItem> itemIDsList = payment.paymentItems
							def itemIDsToBeRemoved = itemIDsList?.findAll{!(it?.itemNumber) || (it?.quantity == 0)}
							itemIDsList?.removeAll(itemIDsToBeRemoved)
							if(itemIDsList){
								def itemForm = Form.read(settings.itemForm)
								def itemsBought = []
								itemIDsList.eachWithIndex{itemId,itemIdx->
									def item = get("${itemId.itemNumber}",itemForm)
									if(item){
										if(item[settings.iqf]<0){
											domainInstance.errors.add([name:field.name, code:"paypal.item.qty.negative", args:[item[settings.inf]]as Object[], defaultMessage:"Sorry, the quantity for item [${item[settings.inf]}] can not be in negative"])
										}else if(item[settings.iqf]==0){
											domainInstance.errors.add([name:field.name, code:"paypal.item.sold.out", args:[item[settings.inf]]as Object[], defaultMessage:"Sorry, the item [${item[settings.inf]}] is sold out"])
										}else if(item[settings.iqf]<itemId.quantity){
											domainInstance.errors.add([name:field.name, code:"paypal.item.max.exceed", args:[
													item[settings.inf],
													item[settings.iqf]]
												as Object[], defaultMessage:"Sorry, the maximum quantity of item [${item[settings.inf]}] is ${(int)item[settings.iqf]}"])
										}
										item.qty = itemId.quantity
										itemsBought << item
									}else{
										domainInstance.errors.add([name:field.name, code:"paypal.item.not.found", args:[]as Object[], defaultMessage:"Item selected is not found"])
									}
								}
								domainInstance."${field.name}_bought" = itemsBought
								domainInstance.payment = payment
								domainInstance.paymentFieldSettings = settings
								domainInstance.paymentField = field
								domainInstance.itemForm = itemForm
							}else{
								domainInstance."${field.name}" = ""
								domainInstance.errors.add([name:field.name, code:"paypal.noitem.selected", args:[]as Object[], defaultMessage:"No item selected to buy"])
							}
						}else{
							def paymentAmount = 0
							try{
								if(domainInstance[settings.iaf]){
									paymentAmount = "${domainInstance[settings.iaf]}".toDouble()
								}else{
									paymentAmount = "${params[settings.iaf]}".toDouble()
								}
							}catch(Exception e){
							}
							if(paymentAmount>0){
								Payment payment = new Payment()
								def paymentItems = new PaymentItem()
								paymentItems.amount = paymentAmount
								def itemName = "${settings.inf?params[settings.inf]:'Payment'}"
								paymentItems.itemName = itemName.length()>126?(itemName.substring(0,123)+"..."):itemName
								payment.addToPaymentItems(paymentItems)
								domainInstance.payment = payment
								domainInstance.paymentFieldSettings = settings
								domainInstance.paymentField = field
								domainInstance."${field.name}_bought" = payment
							}else{
								domainInstance."${field.name}" = ""
								domainInstance.errors.add([name:field.name, code:"paypal.amount.zero.orLess", args:[]as Object[], defaultMessage:"Payment amount should be greater than zero"])
							}
						}
					}catch(Exception ex){
						println "SqlDomainClassService-createForm: in Case PayPal fieldName(${field.name})"+ex
						domainInstance."${field.name}" = ""
						domainInstance.errors.add([name:field.name, code:"paypal.validate.error", args:[]as Object[], defaultMessage:"Problem validating items"])
					}
				}else{
					domainInstance."${field.name}" = params[field.name]?:""
					try{
						if(params[field.name]){
							domainInstance."${field.name}" = params[field.name]
						}else{
							User currentUser= springSecurityService.currentUser
							if(settings.required){
								domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
							}
						}
					}catch(Exception e){
						domainInstance."${field.name}" = ""
						domainInstance.errors.add([name:field.name, code:"default.blank.message", args:[]as Object[], defaultMessage:"Field [{0}] of class [{1}] cannot be blank"])
					}
				}


			}
			domainInstance.version = params.version
			domainInstance.id = params.id?.toLong()
			def formSettings
			try{
				formSettings = JSON.parse(form.settings)
				if(request && !domainInstance.errors && formSettings?.reCaptcha && (!dI || !dI.id)){
					String remoteAddr = request.getRemoteAddr();
					ReCaptchaImpl reCaptcha = new ReCaptchaImpl();
					reCaptcha.setPrivateKey(grailsApplication.config.recaptch.privateKey);

					String challenge = params.recaptcha_challenge_field
					String uresponse = params.recaptcha_response_field
					ReCaptchaResponse reCaptchaResponse = reCaptcha.checkAnswer(remoteAddr, challenge, uresponse);

					if (!reCaptchaResponse.isValid()) {
						domainInstance.errors.add([name:'version', code:"default.reCaptcha.error", args:[]as Object[], defaultMessage:"ReCAPTCHA value incorrect! Please try again."])
					}
				}
			}catch(Exception e){
				println e
			}
		}catch(Exception ex){
		}finally{
			sql.close()
		}
		return domainInstance
	}

	def createForm(def form){
	 	def sql = new Sql(dataSource)
		int counter=0
		try{
			def createQuery = "create table "+form.name+" (id bigint(20) not null auto_increment, version bigint(20) not null default 0,"
			createQuery += "created_by_id bigint(20),updated_by_id bigint(20),date_created datetime not null,last_updated datetime not null"
			form.fieldsList.each { field ->
				createQuery += getFeildQuery(field,true,counter,form.name)
				counter++
			}
			createQuery += ",primary key (id),foreign key (created_by_id) references user(id),foreign key (updated_by_id) references user(id))"
			sql.execute(createQuery)
		}catch(Exception ex){
			println "SqlDomainClassService-createForm: top most tryCatch"+ex
		}finally{
			sql.close()
		}
	}

	def updateForm(def form){
	 	def sql = new Sql(dataSource)
		 int counter=0
		try{
			def isUpdate = false
			def updateQuery = "alter table "+form.name+" add ( "
			form.fieldsList.each { field ->
				if(field.id == null){
					if(field.type != 'SubForm'){
						if(isUpdate){
							updateQuery += ","
							isUpdate = false
						}
						updateQuery += getFeildQuery(field,false,counter,form.name)
						isUpdate = true
					}else{
						getFeildQuery(field,false,counter,form.name)
					}
				}
			 	counter++
				
			}
			updateQuery += ")"
			if(isUpdate)
				sql.execute(updateQuery)
		}catch(Exception ex){
			print ex
			log.error "Problem updating form table:"+ex
		}finally{
			sql.close()
		}
	}

	def createSubformMap(def formName,def fieldName){
		def sql = new Sql(dataSource)
		try{
			def createQuery = "create table "+formName+"_"+fieldName+" ("+formName+"_id bigint(20), "+fieldName+"_long bigint(20),  "+fieldName+"_idx int(11))"
			sql.execute(createQuery)
		}catch(Exception ex){
		}finally{
			sql.close()
		}
	}

	def getFeildQuery(def field,def isCreateQuery, int counter ,def formName){
		def queryString = ""
		if(isCreateQuery && field.type != 'SubForm')
			queryString += ","
		if(field.type == 'SingleLineDate'){
			queryString += field.name+" datetime"
		}else if(field.type == 'SingleLineNumber'){
			queryString += field.name+" decimal(19,2)"
		}else if(field.type == 'MultiLineText'){
			queryString += field.name+" longtext"
		}else if(field.type == 'AddressField'){
			queryString += field.name+" longtext"
		}else if(field.type == 'NameTypeField'){
			queryString += field.name+" longtext"
		}else if(field.type == 'Likert'){
			queryString += field.name+" longtext"
		}else if(field.type == 'FormulaField'){
			def settings = JSON.parse(field.settings)
			if(settings.en.newResultType == 'DateResult')
				queryString += field.name+" datetime"
			else
				queryString += field.name+" decimal(19,2)"
		}else if(field.type == 'FileUpload'){
			queryString += field.name+" varchar(1)"
		}else if(field.type == 'SubForm'){
			createSubformMap(formName,field.name)
		}else{
			if(counter> 70)
				queryString += field.name+" TEXT"
			else
				queryString += field.name+" varchar(255)"
		}
		return queryString
	}

	def deleteItsAttachments(def domainInstanceId,Form form){
		try{
			def itsAttachments = form.getDomainAttachments("${domainInstanceId}".toLong())
			itsAttachments?.each{attachment->
				attachmentableService.removeAttachment(attachment)
			}
		}catch(Exception e){}
	}

	def delete(def domainInstanceId,Form form){
		Sql sql = new Sql(dataSource)
		try{
			form.fieldsList.each{Field field->
				if(field.type == 'SubForm'){
					def setting = JSON.parse(field.settings)
					def subFormInstance = Form.read(setting.subForm)
					if(subFormInstance){
						def subFormName = subFormInstance.name
						def selectQuery ="select ${field.name}_long from ${form.name}"+"_"+"${field.name} where "+"${form.name}_id="+domainInstanceId.toLong()
						def subFormList=sql.rows(selectQuery)
						subFormList.each{
							deleteItsAttachments(it."${field.name}_long",subFormInstance)
							def deleteSubFormQuery="delete from ${subFormName} where id ="+it."${field.name}_long"
							sql.executeUpdate(deleteSubFormQuery)
						}
					}
					def deleteSubFormMapingQuery = "delete from ${form.name}"+"_"+"${field.name} where "+"${form.name}_id="+domainInstanceId.toLong()
					sql.executeUpdate(deleteSubFormMapingQuery)
				}
			}
			deleteItsAttachments(domainInstanceId,form)
			def deleteQuery = "delete from ${form.name} where id ="+domainInstanceId.toLong()
			def rowsdeleted=sql.executeUpdate(deleteQuery)
			if(rowsdeleted>0){
				FormAdmin formAdmin = FormAdmin.findByForm(form)
				if(formAdmin){
					//					Ask what to do on delete??????????????
					//					def uniqueId
					//					if(formAdmin.searchable){
					//						UUID uuid = UUID.randomUUID()
					//						uniqueId = uuid.toString()[0..7]
					//						def uniqueFormEntry = new UniqueFormEntry(formId:form.id,instanceId:domainInstance.id,uniqueId:uniqueId).save()
					//					}
					//					if(formAdmin.trackChanges){
					//						def data = domainInstance as JSON
					//						new FormHistory(formId:form.id,instanceId:domainInstance.id,data:data.toString(),action:'delete').save()
					//					}
				}
				return true
			}else
				return false
		}catch(Exception e){
			println "SqlDomainClassService-delete: top most tryCatch"+e
			return false
		}finally{
			sql.close();
		}
	}
	def deleteSubForm(def parentFormName,def domainInstanceId,def fieldName, Form form){
		Sql sql = new Sql(dataSource)
		try{
			deleteItsAttachments(domainInstanceId,form)
			def deleteSubFormQuery = "delete from ${parentFormName}"+"_"+"${fieldName} where "+"${fieldName}_long="+domainInstanceId.toLong()
			sql.executeUpdate(deleteSubFormQuery)
			def deleteQuery = "delete from ${form.name} where id ="+domainInstanceId.toLong()
			def rowsdeleted=sql.executeUpdate(deleteQuery)
			if(rowsdeleted>0)
				return true
			else
				return false
		}catch(Exception e){
			println "SqlDomainClassService-deleteSubForm: top most tryCatch"+e
			return false
		}finally{
			sql.close();
		}
	}

	def ruleFormInstanceList(def formName,def idList){
		Sql sql = new Sql(dataSource)
		def instanceList
		try{
			def selectQuery = new StringBuilder("select * from ${formName} where id in (${idList.collect { "${it}" }.join(',')})")
	instanceList = sql.rows(selectQuery.toString())
}catch(Exception e){
	println "SqlDomainClassService-ruleFormInstanceList: top most tryCatch"+e
}finally{
	sql.close();
}
return instanceList
}

def deleteUserAppAccessDetails(clientId){
Sql sql = new Sql(dataSource)
def rowsdeleted
try{
	def deleteQuery = "delete from user_app_access_detail where tenant_id="+clientId
	rowsdeleted=sql.executeUpdate(deleteQuery)
}catch(Exception e){
	println "SqlDomainClassService-deleteUserAppAccessDetails: top most tryCatch"+e+"Form clientId"+clientId
}finally{
	sql.close();
}
return rowsdeleted
}
def responseCountLastUpdate(List<Form> formList){
def totalList = []
Sql sql = new Sql(dataSource)
try{
	formList.each{Form form->
		def total = 0
		def recent,lastUpdatedEntryId
		try{
			def result
			def getQuery = "select count(id) as total, max(last_updated) as recentEntry from "+form.name
			def criteriaUser = []
			result = sql.firstRow(getQuery)
			total = result["total"]
			recent=result["recentEntry"]
			try{
				if(result['total']>0){
					def result2 = sql.firstRow("select id as Id from "+form.name+" where last_updated = (select max(last_updated) from "+form.name+")")
					lastUpdatedEntryId = result2["Id"]
				}
			}catch(Exception e){}
		}catch(Exception e){
			println "SqlDomainClassService-responseCountLastUpdate: for form(${form?.id})"+e
		}
		totalList << [id:form.id,recentEntry:recent,count:total,lastUpdatedEntryId:lastUpdatedEntryId]
	}
}catch(Exception e){
	println "SqlDomainClassService-responseCountLastUpdate: top most tryCatch"+e
}finally{
	sql.close();
}
return totalList
}

def upgradeClient(User user, Client client){
Sql sql = new Sql(dataSource)
try{
	def updateQuery1 = "update activity_feed set tenant_id = ${client.id} where tenant_id = ${user.userTenantId} and created_by_id = ${user.id}"
	def updateQuery2 = "update activity_feed_config set tenant_id = ${client.id} where tenant_id = ${user.userTenantId} and created_by_id = ${user.id}"
	def updateQuery3 = "update rule_set set tenant_id = ${client.id} where tenant_id = ${user.userTenantId} and created_by_id = ${user.id}"
	def updateQuery4 = "update email_details set tenant_id = ${client.id} where tenant_id = ${user.userTenantId} and created_by_id = ${user.id}"
	def updateQuery5 = "update email_settings set tenant_id = ${client.id} where tenant_id = ${user.userTenantId} and user_id = ${user.id}"
	def updateQuery6 = "update user set user_tenant_id = ${client.id} where user_tenant_id = ${user.userTenantId} and id = ${user.id}"
	def updateQuery7 = "update form set tenant_id = ${client.id} where tenant_id = ${user.userTenantId} and created_by_id = ${user.id}"
	def rowupdated1 = sql.executeUpdate(updateQuery1)
	def rowupdated2 = sql.executeUpdate(updateQuery2)
	def rowupdated3 = sql.executeUpdate(updateQuery3)
	def rowupdated4 = sql.executeUpdate(updateQuery4)
	def rowupdated5 = sql.executeUpdate(updateQuery5)
	def rowupdated6 = sql.executeUpdate(updateQuery6)
	def rowupdated7 = sql.executeUpdate(updateQuery7)
}catch(Exception e){
	return false
}finally{
	sql.close();
}

}

def formMaxEntryMessage(Form form){
		def formInstanceCount = responseCount(form)
		Client myClient = Client.get(form.tenantId)
		def maxFormEntries
		def isValueNull = false
		if(myClient.maxFormEntries){
			maxFormEntries = myClient.maxFormEntries
		}else{
			maxFormEntries = 100
			isValueNull = true
		}
		if(formInstanceCount == maxFormEntries || (formInstanceCount > maxFormEntries && isValueNull)){
			def superUserList=UserRole.findAllByRole(Role.findByAuthority(Role.ROLE_SUPER_ADMIN)).user
			def user =superUserList[0]
			def currentUser = springSecurityService.currentUser
			if(currentUser && myClient){
				TenantUtils.doWithTenant((int) user.userTenantId){
					def activityFeed = new ActivityFeed()
					def activityFeedConfig= new ActivityFeedConfig();
					activityFeedConfig =  ActivityFeedConfig.findByConfigName("content")
					if ( !activityFeedConfig ) {
						activityFeedConfig = new ActivityFeedConfig(configName:"content", createdBy: user)
						activityFeedConfig.save(flush:true)
					}
				   activityFeed.feedState = ActivityFeed.ACTIVE
				   //Request for account deletion for oneappcloud.com by admin@yourdomain.com
				   def mess = "Form entries for form ${form.toString()} of client ${myClient.name} has reached max limit ${maxFormEntries}"
				   activityFeed.config = activityFeedConfig
				   activityFeed.activityContent=mess
				   activityFeed.createdBy = user
				   activityFeed.dateCreated = new Date()
				   activityFeed.lastUpdated = new Date()
				   activityFeed.addToSharedRoles( com.oneapp.cloud.core.Role.findByAuthority(Role.ROLE_SUPER_ADMIN))
				   activityFeed.save(flush:true)
				}
				if(isValueNull){
					myClient.maxFormEntries = 100
					myClient.save(flush:true)
				}
			}
		}
	}
def deleteAllsubformData(def domainInstanceId,Form form,def subFormFieldName){
	Sql sql = new Sql(dataSource)
	try{
		form.fieldsList.each{Field field->
			if(field.type == 'SubForm'&& subFormFieldName==field.name){
				def setting = JSON.parse(field.settings)
				def subFormInstance = Form.read(setting.subForm)
				if(subFormInstance){
					def subFormName = subFormInstance.name
					def selectQuery ="select ${field.name}_long from ${form.name}"+"_"+"${field.name} where "+"${form.name}_id="+domainInstanceId.toLong()
					def subFormList=sql.rows(selectQuery)
					subFormList.each{
						deleteItsAttachments(it."${field.name}_long",subFormInstance)
						def deleteSubFormQuery="delete from ${subFormName} where id ="+it."${field.name}_long"
						sql.executeUpdate(deleteSubFormQuery)
					}
				}
				def deleteSubFormMapingQuery = "delete from ${form.name}"+"_"+"${field.name} where "+"${form.name}_id="+domainInstanceId.toLong()
				sql.executeUpdate(deleteSubFormMapingQuery)
			}
		}
		return true
	}catch(Exception e){
		println "SqlDomainClassService-delete: top most tryCatch"+e
		return false
	}finally{
		sql.close();
	}
}
def getUpdateList(Form form,def showOwnCreated = true, def criteria = null){//by default will return all the instances with no criteria
	def listTotalMap = [totalCount:0,instanceList:[]]
	def domainInstanceList = []
	Sql sql = new Sql(dataSource)
	try{
		def getQuery = "select * from "+form.name+" where last_updated>='"+criteria.lastSyncDate+
				"' And created_by_id ="+ springSecurityService.currentUser?.id
		 
		domainInstanceList = sql.rows(getQuery)
		if(criteria?.sort){
			if(criteria?.order == 'desc'){
				domainInstanceList.sort {a,b->
					if(a."${criteria?.sort}"<b."${criteria?.sort}") return 1
					else return 0
				}
			}else{
				domainInstanceList.sort {a,b->
					if(a."${criteria?.sort}">b."${criteria?.sort}") return 1
					else return 0
				}
			}
		}
		listTotalMap.totalCount = domainInstanceList.size()
		if(criteria?.offset!=null && criteria?.max && domainInstanceList){
			int fromIndex = "${criteria.offset}".toInteger()
			fromIndex = (listTotalMap.totalCount>fromIndex?fromIndex:0)
			int toIndex = fromIndex + criteria.max
			toIndex = (listTotalMap.totalCount>toIndex?toIndex:listTotalMap.totalCount)
			listTotalMap.instanceList = domainInstanceList.subList(fromIndex, toIndex)
		}else{
			listTotalMap.instanceList = domainInstanceList
		}
	}catch(Exception e){
		println "SqlDomainClassService-list: top most tryCatch"+e
	}finally{
		sql.close();
	}
	return listTotalMap
}
}