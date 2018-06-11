package org.grails.formbuilder

import java.io.Serializable;

import com.oneapp.cloud.core.*;

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class WebHookDetails implements Serializable{
	
	String url
	String handShakeKey
	Boolean includeFieldAndForm
	
	static constraints = {
		url nullable:false, blank: false, url: true
		handShakeKey nullable:true, blank:true
		includeFieldAndForm nullable:true
	}
}