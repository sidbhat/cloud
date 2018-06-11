 
package com.oneapp.cloud.core

import java.io.Serializable;

class Role implements Serializable{

    String authority
    String description

    static final String ROLE_SUPER_ADMIN = "ROLE_SUPER_ADMIN"
    static final String ROLE_ADMIN = "ROLE_ADMIN"
    static final String ROLE_HR_MANAGER = "ROLE_HR_MANAGER"
	static final String ROLE_TRIAL_USER = "ROLE_TRIAL_USER"
    static final String ROLE_USER = "ROLE_USER"
	
	static mapping = {
        cache true
    }

    static constraints = {
        authority blank: false
    }

    String toString() {
        description
    }

}
