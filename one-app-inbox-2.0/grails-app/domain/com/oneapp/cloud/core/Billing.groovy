 
package com.oneapp.cloud.core

import java.io.Serializable;

class Billing implements Serializable{

    Client client
    Long referenceId
    Long amount
    Long intialAmount
	String currency
	String description


    static constraints = {
        client  nullable: false
        referenceId nullable: false
        amount  nullable: true
		intialAmount blank: true, nullable: true
		currency nullable: false
		description blank: true, nullable: true
    }

    

}
