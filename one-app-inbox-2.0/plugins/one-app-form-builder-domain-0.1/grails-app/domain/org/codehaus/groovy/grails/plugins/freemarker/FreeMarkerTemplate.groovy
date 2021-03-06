
package org.codehaus.groovy.grails.plugins.freemarker

import java.io.Serializable;

/**
*
* @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
*
* @since 0.1
*/
class FreeMarkerTemplate implements Serializable{
  String name
  String source
  Date dateCreated
  Date lastUpdated
  
  static constraints = {
	  name blank:false, unique: true, size:5..255
	  source blank:false, maxSize:10000
	  dateCreated blank:false
	  lastUpdated nullable:true
    }
  
  String toString() {
	  name
   }
}
