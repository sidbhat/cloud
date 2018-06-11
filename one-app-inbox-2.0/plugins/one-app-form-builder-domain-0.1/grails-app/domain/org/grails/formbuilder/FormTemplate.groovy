package org.grails.formbuilder

import java.io.Serializable;

import com.macrobit.grails.plugins.attachmentable.core.Attachmentable;
import com.oneapp.cloud.core.*
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class FormTemplate implements Attachmentable, Serializable{
	String name
	String category
	boolean global=false
	Date dateCreated
	Date lastUpdated
	User createdBy
	User updatedBy
	
	 
	static constraints = {
		form nullable:false, blank: false
		name nullable:false, blank: false
		category nullable:false, blank: false
		global nullable:true, blank:true
		createdBy nullable:true, blank:true
		updatedBy nullable:true, blank:true
	}
	

	 String toString() {
        name
    }
    static belongsTo = [form:Form]
	
}