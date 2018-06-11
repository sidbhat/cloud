
package org.grails.formbuilder

import java.io.Serializable;

/**
*
* @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
*
* @since 0.1
*/
class DomainClass implements Serializable{
	String name
	String source
	Boolean updated
	Date dateCreated
	Date lastUpdated
	def domainClassService
	
   static constraints = {
	  name blank:false, size:5..255
	  source blank:false, maxSize:40000
	  updated nullable:true
	  dateCreated nullable:true
	  lastUpdated nullable:true
    }
    static mapping = {
    	source type: 'text' 
    }
	def afterUpdate={
//		domainClassService.reloadUpdatedDomainClasses( id )
	}
}
