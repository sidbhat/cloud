 
package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class BaseObject implements Serializable{

    BigDecimal amount
    Account account
    BigDecimal taxPercentage
    BigDecimal taxPercentage2
    BigDecimal taxAmount
    BigDecimal taxAmount2
    Date issueDate
    Date dateCreated
    Date lastUpdated
    //static mapping = { sort dateCreated:"desc" } 
    def static reportable = [
            amount: 'Amount: ',
            columns: ['amount', 'account', 'taxAmount']
    ]
	static hasMany = [ items: BaseObjectItem] 
    static constraints =
    {
    	items blank:true,nullable:true
    	account blank:true,nullable:true
        dueAmount blank: true, nullable: true, scale: 2
        amount blank: true, nullable: true, scale: 2
        taxPercentage blank: true, nullable: true, scale: 2
        taxPercentage2 blank: true, nullable: true, scale: 2
        taxAmount blank: true, nullable: true, scale: 2
        taxAmount2 blank: true, nullable: true, scale: 2
        issueDate blank: true, nullable: true
        startPeriod blank: true, nullable: true
        endPeriod blank: true, nullable: true
        project blank: true, nullable: true
       // format blank: true, nullable: true
    }

    String toString() {
        id
    }


}


