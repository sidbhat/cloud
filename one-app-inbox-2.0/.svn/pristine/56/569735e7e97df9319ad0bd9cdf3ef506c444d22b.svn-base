package com.oneapp.cloud.core

import java.io.Serializable;
import java.util.Date;

class UserVerification implements Serializable {
	String id
	String apiKey
	Date dateCreated
	static constraints = {
		id(unique:true,blank:false)
		apiKey nullable:false
	}
	static mapping = {
		id column: 'id', generator: 'assigned'
	}
}
