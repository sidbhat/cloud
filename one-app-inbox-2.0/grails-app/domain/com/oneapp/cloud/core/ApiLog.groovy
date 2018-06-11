 
package com.oneapp.cloud.core

import java.io.Serializable;

class ApiLog implements Serializable{

    Date logDate
	Long userId
	Long callCount

    static mapping = {
		version: false
    }

    static constraints = {
        logDate nullable: true
        userId nullable: true
        callCount nullable: true
    }

}
