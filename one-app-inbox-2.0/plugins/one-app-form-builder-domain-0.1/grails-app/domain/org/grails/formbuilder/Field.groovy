

package org.grails.formbuilder
import java.io.Serializable;

import grails.converters.JSON
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class Field implements Serializable{
	String name
	String type
	String settings
	Integer sequence
	FieldStatus status
	Date dateCreated
	Date lastUpdated

	static transients = ['status']

	static constraints = {
		name nullable:false
		type nullable:false
		settings nullable:false
		sequence nullable:false
		dateCreated nullable:true
		lastUpdated nullable:true
	}

	static mapping = { settings type:'text' }

	static belongsTo = [form:Form]

	String toString(){
		def returnval
		try{
			returnval= JSON.parse(settings).en.label
		}catch (Exception e) {
			returnval= name
		}
		return returnval
	}
}

enum FieldStatus {
	U, // UPDATED
	D // DELETED
}
