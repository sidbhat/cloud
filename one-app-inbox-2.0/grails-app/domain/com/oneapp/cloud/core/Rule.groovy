package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant
@MultiTenant
class Rule implements Serializable{

	String className
	String attributeName
	String operator
	String value
	String _condition
	String status
	Integer _order
	
	
	static constraints = {
	 _order  nullable: false, blank: false, unique : 'ruleSet'
	 className  nullable: false, blank: false
	 attributeName  nullable: false, blank: false
	 value  nullable: false, blank: false
	 operator  nullable: false, blank: false, inList : ["=", "<", ">", "!=", "<=", ">=", "Between"]
	 _condition nullable: true, blank: true, inList : ["AND", "OR"]
	 status nullable: false, blank: false, inList : ["Active", "Inactive"]
	}
	
	static belongsTo = [ruleSet:RuleSet]
	static defaultSort = '_order'
 	String toString(){
 	  "[$className : ${attributeName} $operator $value]"
 	}
 	
 	def beforeInsert = {
        if (_condition == null)
      	  _condition = "AND"
    }
    def beforeUpdate = {
        if (_condition == null)
      	  _condition = "AND"
    }
}
