 
package com.oneapp.cloud.core.currency

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class CurrencyMap implements Serializable{

    Currency currency
    String description

    static constraints = {
        date(unique: ['currency'])
    }

    String toString() {
        return description
    }


}
