 
package com.oneapp.cloud.core

import java.io.Serializable;

class Person implements Serializable{

    // Communication details
    String firstName
    String lastName
    String officePhone
    String mobilePhone
    String faxNumber
    Address homeAddress
    Address officeAddress
    Date dob
    String email
    
    // Profile URL for social integration //
    String linkedInProfile
    String facebookProfile
    String twitterProfile
    String socialNwName
    static searchable = true
    static embedded = ['homeAddress', 'officeAddress']
	static mapping = {
        tablePerHierarchy false
    }
    static constraints =
    {
        firstName blank: false, nullable: false, size: 1..30
        lastName blank: true, nullable: true, size: 1..30
        officePhone blank: true, nullable: true, size: 1..20
        mobilePhone blank: true, nullable: true, size: 1..20
        faxNumber blank: true, nullable: true, size: 1..20
        homeAddress blank: true, nullable: true
        officeAddress blank: true, nullable: true
        dob blank: true, nullable: true
        email blank: true, nullable: true, email:true
        linkedInProfile blank: true, nullable: true
        facebookProfile blank: true, nullable: true
        twitterProfile blank: true, nullable: true
		socialNwName blank: true, nullable: true
  
    }

    String toString() {
        firstName + "  " + lastName
    }

}
class Address {

    String address1
    String address2
    String city
    String state
    String zipCode
    String country

    static constraints = {
        address1(maxSize: 125)
        address2(maxSize: 125)
        city(maxSize: 150)
        state(maxSize: 50)
        zipCode(maxSize: 150)
        country(maxSize: 50)
    }

    String toString() {
        (address1 ? address1 : "") + "\n " +
                (address2 ? address2 : "") + "\n " +
                (city ? city + ", " : "") +
                (state ? state + ", " : "") +
                (zipCode ? zipCode + ", " : "") +
                (country ? country : "")
    }

}