package com.oneapp.cloud.core

class BlockedIp {
    String ipAdress
	String username
	Long formId
	String reason 
    static constraints = {
		username nullable: true,blank: true
		formId nullable: true,blank: true
    }
}
