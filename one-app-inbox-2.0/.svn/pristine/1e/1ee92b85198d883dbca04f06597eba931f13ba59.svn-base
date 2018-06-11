/******************************************************************************
 * [ NIKKISHI CONFIDENTIAL ]                                                  *
 *                                                                            *
 *    Copyright (c) 2011.  Nikkishi LLC                                       *
 *    All Rights Reserved.                                                    *
 *                                                                            *
 *   NOTICE:  All information contained herein is, and remains                *
 *   the property of Nikkishi LLC and its suppliers,                          *
 *   if any.  The intellectual and technical concepts contained               *
 *   herein are proprietary to Nikkishi LLC and its                           *
 *   suppliers and may be covered by U.S. and Foreign Patents,                *
 *   patents in process, and are protected by trade secret or copyright law.  *
 *   Dissemination of this information or reproduction of this material       *
 *   is strictly forbidden unless prior written permission is obtained        *
 *   from Nikkishi LLC.                                                       *
 ***************************************************************************** */

package com.oneapp.cloud.core.log

import java.io.Serializable;

import com.oneapp.cloud.core.*
/*** Capture site tracking details ***/
class Tracker implements Serializable{
  
    Date dateCreated
    String controller
    String recordID
    String formID
    String action
    String params
    String userAgent
    User user
    String ip
    String clientID
    String clientName
    String loadTime
   
    static constraints = {
        user blank: true, nullable: true
        loadTime blank: true, nullable: true
        controller blank: true, nullable: true
        action blank: true, nullable: true
        userAgent blank: true, nullable: true
        recordID blank: true, nullable: true
        ip blank: true, nullable: true
        clientID blank: true, nullable: true
        clientName blank: true, nullable: true
        dateCreated blank:true, nullable:true
        formID blank:true, nullable:true
        params blank:true, nullable:true
    }
	
	static mapping = {
		params type:'text'
	}
	
	static namedQueries = {
      
       filterFeedsByLastLogin { user, lastLogin ->
           def now = new Date()
           gt 'dateCreated', now - lastLogin
           eq 'controller', 'activityFeed'
           eq 'user', user
       }
       
       filterByControllerAndActionAndId { controller,action,recordID, clientID ->
           eq 'controller', controller
           eq 'action', action
           eq 'recordID', recordID
           eq 'clientID', clientID
       }
       
       filterByControllerAndId { controller,recordID, clientID ->
           eq 'controller', controller
           eq 'recordID', recordID
           eq 'clientID', clientID
       }
       
       filterByController { controller,clientID ->
           eq 'controller', controller
           eq 'clientID', clientID
            
       }
       
       filterByClient { clientID ->
           eq 'clientID', clientID
       }

 	  filterByFormID { formID,clientID ->
           eq 'clientID', clientID
           eq 'formID', formID
       }
      
  	 }

}