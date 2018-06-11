package org.grails.formbuilder

import java.io.Serializable;

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class UniqueFormEntry implements Serializable{
	
	Long formId
	Long instanceId
	String uniqueId
	
	static constraints = {
		formId nullable:false, blank: false
		instanceId nullable:false, blank:false
		uniqueId nullable:false, blank:false
	}
}