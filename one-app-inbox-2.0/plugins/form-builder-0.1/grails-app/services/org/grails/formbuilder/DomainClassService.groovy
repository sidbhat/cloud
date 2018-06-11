package org.grails.formbuilder

import grails.util.GrailsNameUtils
import org.codehaus.groovy.grails.web.pages.FastStringWriter

import com.macrobit.grails.plugins.attachmentable.core.Attachmentable;
import com.macrobit.grails.plugins.attachmentable.domains.Attachment;
import com.macrobit.grails.plugins.attachmentable.domains.AttachmentLink;

import grails.converters.JSON
import java.util.regex.Pattern
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class DomainClassService {
  def grailsApplication
  def dynamicDomainService
  static transactional = false
  public static Set formsLoaded = []
  def sessionFactory
  def attachmentableService
  
  public static boolean fetchingEmail = false

  String getName(Form form) {
    return "${form.name}.${GrailsNameUtils.getClassNameRepresentation(form.name)}"
  }
  public String getToCamel(String string) {
    String result = "";
    for (int i = 0; i < string.length(); i++) {

      if (i == 0) {
        result += string[i].toLowerCase()
      } else {
        result += string[i]
      }
    }
    return result
  }
  String getSource(Form form) {
    def out = new FastStringWriter()
    def widget, defaultConstraints
    Object settings
    def constraintsStrings = new FastStringWriter()
    
	def mappings = new FastStringWriter()
	String hasManys
	
	out << "package ${form.name}\n" //"package ${form.name}\n"
    out << "class ${GrailsNameUtils.getClassNameRepresentation(form.name)} implements com.macrobit.grails.plugins.attachmentable.core.Attachmentable {\n"
    
    int fieldCnt=0
    
    form.fieldsList.each { field ->
		try{
			fieldCnt++
			if(field?.status != FieldStatus.D){
			  def subFormClass
		      settings = JSON.parse(field.settings)
		      if (settings._persistable) {
		        String name = "" + field.name
				if(field.type == 'FormulaField'){
					def itsType = grailsApplication.config.formBuilder."${settings.en.newResultType}".type
					def itsDBType = grailsApplication.config.formBuilder."${settings.en.newResultType}".dbType
					out << """${itsType} ${field.name}\n"""
					if(settings.en.oldResultType && settings.en.oldResultType != settings.en.newResultType){
						try{
							def session = sessionFactory.getCurrentSession()
							def connection = session.connection()
							def state = connection.createStatement()
							state.addBatch("update ${form.name} set ${field.name} = null")
							state.addBatch("alter table ${form.name} modify ${field.name} ${itsDBType}")
							state.executeBatch()
							
						}catch (Exception je){
						}
					}
					settings.en.oldResultType = settings.en.newResultType
					field.settings = settings.toString()
				}else if(field.type == 'SubForm'){
					if(settings.subForm != settings.oldSubForm){
						if(settings.oldSubForm){
							Form oldSubForm = Form.read(settings.oldSubForm)
							try{
								def session = sessionFactory.getCurrentSession()
								def connection = session.connection()
								def state = connection.createStatement()
								if(oldSubForm){
									def subDAttachments = oldSubForm.attachments
									if(subDAttachments){
										List<Long> subDomainIdList = new ArrayList<Long>()
										org.apache.commons.dbcp.DelegatingResultSet subDomainList = state.executeQuery("select ${field.name}_long from ${form.name}_${field.name}")
										while(subDomainList.next()){
											subDomainIdList << subDomainList.getBigDecimal("id").toLong()
										}
										def attachmentInstanceList = subDAttachments.findAll{subDomainIdList.contains(it.domainInstanceId)}
										if(attachmentInstanceList){
											attachmentInstanceList?.each{attachment->
												attachmentableService.removeAttachment(attachment)
											}
											state = session.connection().createStatement()
										}
									}
									state.addBatch("delete from ${oldSubForm.name} where id in (select ${field.name}_long from ${form.name}_${field.name})")
//									state.addBatch("delete from attachment where lnk_id in (select id from attachment_link where reference_class = '${oldSubForm.domainClass.name}' and reference_id in (select ${field.name}_long from ${form.name}_${field.name}))")
//									state.addBatch("delete from attachment_link where reference_class = '${oldSubForm.domainClass.name}' and reference_id in (select ${field.name}_long from ${form.name}_${field.name})")
								}
								state.addBatch("delete from ${form.name}_${field.name}")
								state.executeBatch()
								
							}catch (Exception je){
								println "DomainClassService-getSource: "+je
							}
						}
						settings.oldSubForm = settings.subForm
						field.settings = settings.toString()
					}
					subFormClass = "Long"
//					if(settings.subForm){
//						try{
//							Form subForm = Form.read(settings.subForm)
//							subFormClass = subForm.domainClass.name
//						}catch(Exception e){
//						}
//					}
					out << """${grailsApplication.config.formBuilder."${field.type}".type}<${subFormClass}> ${field.name} = new ArrayList<${subFormClass}>()\n"""
					if(hasManys)
						hasManys += ", "
					else
						hasManys = ""
					hasManys += "${field.name}:${subFormClass}"
				}else{
		        	out << """${grailsApplication.config.formBuilder."${field.type}".type} ${field.name}\n"""
				}
		        widget = grailsApplication.classLoader.loadClass("${FormBuilderConstants.WIDGET_PACKAGE}.${field.type}").newInstance()
		
		        defaultConstraints = grailsApplication.config.formBuilder."${field.type}".defaultConstraints
		        constraintsStrings << widget.getConstraints(field, settings)
		
				
		        defaultConstraints?.each { k, v ->
		          constraintsStrings << ",${k}:${v}"
		        }
		        constraintsStrings << FormBuilderConstants.NEW_LINE
		        
		        // Set the mapping as text for handling multiline control
		        if ( field.type == 'MultiLineText') {
		        	mappings << " ${field.name} type: 'text' "
		        	mappings << FormBuilderConstants.NEW_LINE
		        }
		        
		      }
		    }else{
				try{
					def session = sessionFactory.getCurrentSession()
					def connection = session.connection()
					def state = connection.createStatement()
					if(field.type == 'SubForm'){
						settings = JSON.parse(field.settings)
						if(settings.oldSubForm){
							Form oldSubForm = Form.read(settings.oldSubForm)
							if(oldSubForm){
								def subDAttachments = AttachmentLink.findAllByReferenceClassAndReferenceId("org.grails.formbuilder.Form",oldSubForm.id)?.attachments
								//state = session.connection().createStatement()
								if(subDAttachments){
									List<Long> subDomainIdList = new ArrayList<Long>()
									org.apache.commons.dbcp.DelegatingResultSet subDomainList = state.executeQuery("select ${field.name}_long from ${form.name}_${field.name}")
									while(subDomainList.next()){
										subDomainIdList << subDomainList.getBigDecimal("id").toLong()
									}
									def attachmentInstanceList = subDAttachments.findAll{subDomainIdList.contains(it.domainInstanceId)}
									if(attachmentInstanceList){
										attachmentInstanceList?.each{attachment->
											attachmentableService.removeAttachment(attachment)
										}
										state = session.connection().createStatement()
									}
								}
								state.addBatch("delete from ${oldSubForm.name} where id in (select ${field.name}_long from ${form.name}_${field.name})")
//								state.addBatch("delete from attachment where lnk_id in (select id from attachment_link where reference_class = '${oldSubForm.domainClass.name}' and reference_id in (select ${field.name}_long from ${form.name}_${field.name}))")
//								state.addBatch("delete from attachment_link where reference_class = '${oldSubForm.domainClass.name}' and reference_id in (select ${field.name}_long from ${form.name}_${field.name})")
							}
						}
						state.addBatch("drop table ${form.name}_${field.name}")
					}else{
						log.info("Drop column with query: alter table ${form.name} drop column ${field.name}")
						state.addBatch("alter table ${form.name} drop column ${field.name}")
					}
					state.executeBatch()
					if(field.type=='FileUpload'){
						def attachments = form.getAttachments([field.name+'_file'])
						attachments?.each{attachment->
							attachmentableService.removeAttachment(attachment)
						}
					}
				}catch (Exception je){
				}
			}
		}catch(Exception e){
			println "FormId: "+form.id
			println "===========>>>>>>>>>Error for field: "+field+",Count is: "+fieldCnt+" Exception is: "+e
			log.error("===========>>>>>>>>>Error for field: "+field+",Count is: "+fieldCnt+" Exception is: "+e)
		}
    }
	def fieldsToBeDeleted = form.fieldsList.findAll{ it?.status == FieldStatus.D}
	fieldsToBeDeleted.each{
		form.fieldsList.remove(it)
		it?.delete()
	}
	
	
   	out << "\n com.oneapp.cloud.core.User createdBy \n 	 com.oneapp.cloud.core.User updatedBy \n Date dateCreated\nDate lastUpdated\nstatic constraints = {\n"
    out << constraintsStrings.toString() 
    out <<  " createdBy nullable:true, blank:true \n updatedBy nullable:true, blank:true \n"
    out << "}\n" // end constraints
    out << "static mapping = { \n"
    out << mappings
	out << "}\nstatic hasMany = [${hasManys?:''}]\n"
    out << "}" // end class
  }
  
  String getSourceMetaClass(){
  	//out << "package ${form.name}\n" //"package ${form.name}\n"
    //out << "class ${GrailsNameUtils.getClassNameRepresentation(form.name)} {\n"
    
    //out << "Date dateCreated\nDate lastUpdated\n"
    //out << "}" // end class
  
  }

  DomainClass getDomainClass(Form form) {
 //println"=================+"+ getSource(form)
	  def domainClass = form?.domainClass
	  if(!domainClass){
		  domainClass = new DomainClass()
	  }
	  domainClass.name = getName(form)
	  domainClass.source = getSource(form)
	  domainClass.updated = true
	  
	  return domainClass
  }
  
  def registerAndLoadDomainClass(String domainName){
//	  def formWorkWithSql = grailsApplication.config.form.workWithSQL
//	  if(formWorkWithSql){
//		  DomainClass dc= DomainClass.findByName(domainName)
//		  if(dc){
//			  registerDomainClass(dc.source)
//			  formsLoaded.add(dc.id)
//			  dc.updated = false
//			  if(dc.isDirty())
//			  	dc.save(flush:true)
//		  }
//	  }
  }

  def registerDomainClass(String source) {
//    //println "domain class source: \n${source}"
//    source.trim().split("package").each {
//      if (it) {
//        dynamicDomainService.registerDomainClass "package$it"
//      }
//    }
//    dynamicDomainService.updateSessionFactory grailsApplication.mainContext
  }

 def reloadUpdatedDomainClasses() {
//    log.debug "reloadDomainClasses() executing..."
//	while(DomainClassService.fetchingEmail == true){
//		Thread.currentThread().sleep(2000)
//	}
//	DomainClassService.fetchingEmail = true
//	def formsUpdated = Form.createCriteria().list(){
//		domainClass{
//			eq 'updated',true
//		}
//	}
//	def updatedDomainClasses = formsUpdated.sort{a,b->b.formCat.compareTo(a.formCat)}*.domainClass
//	if(updatedDomainClasses){
////		updatedDomainClasses*.discard()
//		updatedDomainClasses.each{updatedDomainClass->
//			def source = updatedDomainClass.source.trim()
//			if(source)
//				dynamicDomainService.registerDomainClass source
//			updatedDomainClass.updated = false
//			updatedDomainClass.save(flush:true)
//			formsLoaded.add(updatedDomainClass.id)
//		}
//		dynamicDomainService.updateSessionFactory grailsApplication.mainContext
//	}
//	DomainClassService.fetchingEmail = false
//    log.debug "reloadDomainClasses() executed"
  }

  def setDefaultValue(def domainInstance, def constrainedProperty) {
    def constraintNames = getConstraintNames(constrainedProperty)
    def value
    //println "setting defaul values"
    constraintNames.eachWithIndex { constraintName, i ->
      println "$i) $constraintName $constrainedProperty"
      switch (constraintName) {
        case "nullable":
        case "blank":
          // required
          if (!constrainedProperty.isNullable() && !constrainedProperty.isBlank()) {
            if (constrainedProperty.propertyType == String) {
              value = FormBuilderConstants.DEFAULT_STRING
            } else if (propertyType == Date) {
              value = FormBuilderConstants.DEFAULT_DATE
            } else if (propertyType.superclass == Number) {
              value = FormBuilderConstants.DEFAULT_NUMBER
            }
          } else { // nullable:true
            return
          }
          break
        case "creditCard":
          if (constrainedProperty.isCreditCard()) {
            value = FormBuilderConstants.DEFAULT_CREDIT_CARD_NO
          }
          break
        case "email":
          if (constrainedProperty.isEmail()) {
            value = FormBuilderConstants.DEFAULT_EMAIL
          }
          break
        case "url":
          if (constrainedProperty.isUrl()) {
            value = FormBuilderConstants.DEFAULT_URL
          }
          break
        case "inList":
          value = constrainedProperty.inList[0]
          break
        case "matches":
          break
        case "max":
          break
        case "maxSize":
          break
        case "min":
          value = constrainedProperty.min
          break
        case "minSize":
          if (constrainedProperty.propertyType == String) {
            value = FormBuilderConstants.DEFAULT_STRING * constrainedProperty.minSize
          }
          break
        case "notEqual":
          break
        case "range":
          value = constrainedProperty.range.from
          break
        case "size":
          if (constrainedProperty.propertyType == String) {
            value = FormBuilderConstants.DEFAULT_STRING * constrainedProperty.size.from
          }
          break
        case "unique":
          if (constrainedProperty.propertyType == String) {
            value = UUID.randomUUID()
          }
          break
        case "validator":
          break
        default:
          println "custom constraint: ${constraintName}"
      }
    }
    domainInstance."${constrainedProperty.propertyName}" = value
    //println "DomainClassService.setDefaultValue: ${constrainedProperty.propertyName} = ${value}"
  }

  private List getConstraintNames(def constrainedProperty) {
    def constraintNames = constrainedProperty.appliedConstraints.collect { return it.name }
    if (constraintNames.contains("blank") && constraintNames.contains("nullable")) {
      constraintNames.remove("nullable") // blank constraint take precedence
    }
    return constraintNames
  }
}
