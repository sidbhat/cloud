 
package com.oneapp.cloud.core

import java.io.Serializable;
import java.util.Currency;

class BillingHistory implements Serializable{

    Client client
	String description
    BigDecimal amount
	Currency currency = Currency.getInstance("USD")
	Date billDate
	String transactionId
	String paypalTransactionId
	


    static constraints = {
        client nullable: false
		description blank: true, nullable: true
        amount nullable: false
		currency nullable: false
		billDate nullable: false
		transactionId nullable:false
		paypalTransactionId nullable:true
    }

    

}
