package org.grails.formbuilder

import java.io.Serializable;

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class FormHistory implements Serializable{
	
	Long formId
	Long instanceId
	String data
	String action
	
	static constraints = {
		formId nullable:false, blank: false
		instanceId nullable:false, blank:false
		data nullable:true, blank:true
		action nullable:false, blank:false
	}
	
	static mapping = {
		data type:'text'
	}
}