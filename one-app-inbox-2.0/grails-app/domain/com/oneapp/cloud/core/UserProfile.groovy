 
package com.oneapp.cloud.core

import java.io.Serializable;
import java.util.List;

import com.macrobit.grails.plugins.attachmentable.core.Attachmentable;
import com.oneapp.cloud.time.Task;

class UserProfile implements Attachmentable, Serializable{

    boolean emailSubscribed
    
    boolean emailTimeSubscribed
    boolean emailProjectSubscribed
    boolean emailExpenseSubscribed
    boolean reportsSubscribed
    
    boolean syncFacebookContacts
    boolean syncLinkedinContacts
    boolean syncTwitterContacts
	boolean isOnline=true
    
   // Project defaultProject
    Task task
    BigDecimal defaultHours
    User user
    String currency
    Integer numOfRows = 10

    static belongsTo = [user: User]

    BigDecimal projectBudgetAlert
    BigDecimal timeEntryAlert
    BigDecimal expenseEntryAlert
    BigDecimal invoiceAlert
    
    String defaultLanguage
    String defaultTimezone
    Date lastUpdated
    Date dateCreated
    
    String background
    String header
    
    String currentStatus
	List<User> myFriendsList
    def beforeInsert = {
        if (defaultLanguage)
     	   defaultLanguage = "en"
        if (defaultTimezone)
     	   defaultTimezone = "est"
    }
    
    

    static constraints = {
        emailTimeSubscribed nullable: true, blank: true
        emailProjectSubscribed nullable: true, blank: true
        emailExpenseSubscribed nullable: true, blank: true
        reportsSubscribed nullable: true, blank: true
       // defaultProject nullable: true, blank: true
        task nullable: true, blank: true
        defaultHours nullable: true, blank: true, scale: 2, min: 0.5, max: 24.0
        user nullable: true, blank: true, unique: true
        projectBudgetAlert nullable: true, blank: true
        timeEntryAlert nullable: true, blank: true
        expenseEntryAlert nullable: true, blank: true
        invoiceAlert nullable: true, blank: true
        currency nullable: true, blank: true
        numOfRows nullable: true, blank: true
        syncFacebookContacts nullable: true, blank: true
        syncLinkedinContacts nullable: true, blank: true
        syncTwitterContacts nullable: true, blank: true
		isOnline nullable:true, blank:true
        defaultLanguage nullable: true, blank: true
        defaultTimezone nullable: true, blank: true
        background nullable: true, blank: true
        header nullable: true, blank: true
        currentStatus nullable: true, blank: true
        emailSubscribed nullable: true, blank: true, default:true
		myFriendsList  nullable: true, blank: true
    }
}
