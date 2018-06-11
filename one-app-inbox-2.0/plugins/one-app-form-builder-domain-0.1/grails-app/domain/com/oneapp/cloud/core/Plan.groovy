 
package com.oneapp.cloud.core

import java.io.Serializable;

class Plan implements Serializable{

    String planName
	String description
	Long maxStorage
    Double amount
	Long maxUsers=5
	Long form=5
	Long maxEmailAccount = 2
	Long maxFormEntries = 100

    static constraints = {
        planName  nullable: false
		description blank: true, nullable: true
		maxStorage nullable: false
        amount  nullable: false
		maxUsers nullable: false
		form nullable:false
		maxEmailAccount nullable:false
		maxFormEntries nullable:false
    }

}
