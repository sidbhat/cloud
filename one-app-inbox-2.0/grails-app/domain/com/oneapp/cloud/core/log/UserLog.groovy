 
package com.oneapp.cloud.core.log

import java.io.Serializable;

import com.oneapp.cloud.core.User
/*** Capture last login and uri details for a user in this table  ***/
class UserLog implements Serializable{

    String lastURI
    User user
    Date lastLogin
   
    static constraints = {
        user blank: false, nullable: false
        lastLogin blank:true, nullable:true
        lastURI(size: 2..256, nullable: true)
    }


}