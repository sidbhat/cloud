package org.grails.formbuilder

import org.apache.commons.collections.list.LazyList
import org.apache.commons.collections.FactoryUtils
import org.codehaus.groovy.grails.plugins.freemarker.FreeMarkerTemplate
import org.codehaus.groovy.grails.commons.ApplicationHolder as AH
import com.macrobit.grails.plugins.attachmentable.core.Attachmentable
import com.oneapp.cloud.core.*
import grails.converters.JSON
import java.io.Serializable;
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class Form implements Attachmentable, Serializable{
	
	def sessionFactory
	
	String name
	String description
	String settings
	DomainClass domainClass
	Integer numberOfColumnInList
	Integer numberOfRowPerPage
	Integer persistableFieldsCount
	Date dateCreated
	Date lastUpdated
	User createdBy
	Integer tenantId
	
	String formCat
	
	static constraints = {
		name blank: false
		description nullable:true, blank: true
		settings nullable:false, blank: false
		domainClass nullable:false, unique: true
		numberOfColumnInList nullable:true, blank: true
		numberOfRowPerPage nullable:true, blank: true
		persistableFieldsCount nullable:false, min: 1
		fieldsList nullable:false, minSize: 1
		createdBy nullable:true, blank: true
		tenantId nullable:false, blank: false
		
		formCat nullable:true, blank: true
	}
	
	List fieldsList = new ArrayList()
	static hasMany = [ fieldsList:Field ]
	
	static mapping = { 
		fieldsList cascade:"all-delete-orphan", sort: "sequence", lazy: false 
		settings type:'text'
		domainClass cascade:"all"
		cache true
	}
	
	// From: http://omarello.com/2010/08/grails-one-to-many-dynamic-forms/
	def getFields() {
		return LazyList.decorate(fieldsList,
		FactoryUtils.instantiateFactory(Field.class))
	}
	
	String toString(){
	   return JSON.parse(settings).en.name
	}
	
	def beforeDelete(){
		
		// Delete Form templates if any
		
		withNewSession{
			removeAttachments()
		}
      	try{
			def session = sessionFactory.getCurrentSession()
	        def connection = session.connection()
	        def state = connection.createStatement()
	        state.addBatch("drop table ${this.name.toLowerCase()}")
			state.executeBatch()
        }catch (Exception je){
        	log.error je
        }
       
        	
      
	}
    
  
}
