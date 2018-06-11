 
package com.oneapp.cloud.core.currency

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant

@MultiTenant
class ExchangeRate implements Serializable {
    Currency baseCurrency
    Currency toCurrency
    BigDecimal rate
    Date date = new Date()

    static constraints = {
        date(unique: ['baseCurrency', 'toCurrency'])
    }

    String toString() {
        "$date $baseCurrency to $toCurrency @ $rate"
    }
}