 
package com.oneapp.cloud.core.log

import java.io.Serializable;

import com.oneapp.cloud.core.User

/*** Capture all login failures in this table  ***/
class LoginFailureLog implements Serializable{

    String ip
    String uri
    String user
    Date _date
    String msg

    static constraints = {
        ip blank: true, nullable: true
        uri blank: true, nullable: true
        _date blank: true, nullable: true
        msg blank: true, nullable: true
    }



}