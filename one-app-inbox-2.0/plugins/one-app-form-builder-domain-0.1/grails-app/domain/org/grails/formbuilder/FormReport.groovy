package org.grails.formbuilder

import java.io.Serializable;

import com.oneapp.cloud.core.*
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class FormReport implements Serializable{
	String viewName
	String description
	
	User createdBy
	boolean _private
	String keyFigures
	String nonKeyFigures
	String filters
	
	static constraints = {
		viewName blank: false, unique: true
		description nullable:true, blank: true
		form nullable:false, blank: false
		createdBy nullable:true,blank:true
		_private nullable:true, blank:true
		keyFigures nullable:true, blank:true
		nonKeyFigures nullable:true, blank:true
		filters nullable:true, blank:true
	}
	static belongsTo = [form:Form]

}