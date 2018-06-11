package com.oneapp.cloud.core

import java.io.Serializable;

class Message implements Serializable{
	
	String message
	User fromUser
	User toUser
	Date messageTime
	Boolean recvd = false

    static constraints = {
		message(size: 0..5000, nullable:true)
		fromUser(nullable:true)
		toUser(nullable:true)
		messageTime(nullable:true)
		recvd()
    }
}
