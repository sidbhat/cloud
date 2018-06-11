package com.oneapp.cloud.core

import java.io.Serializable;

import grails.plugin.multitenant.core.groovy.compiler.MultiTenant
@MultiTenant
class RuleSet implements Serializable{

	Integer _order
	String name
	String _action
	String resultClass
	String resultInstance
	String status
	User createdBy
	User updatedBy
	Date lastUpdated
	Date dateCreated
	
	public static final String USER="User"
	public static final String GROUP="Group"
	public static final String DEPARTMENT="Department"
	public static final String ROLE="Role"
	public static final String USER_MANAGER="User's Manager"
	public static final String USER_DEPARTMENT="User's Department"
	
	public static final String EMAIL="Email"
	public static final String SUBSCRIBE="Subscribe"
	public static final String FETCH_EMAIL="Fetch Email"
	
	public static final String ACTIVE="Active"
	public static final String INACTIVE="Inactive"
	
	static constraints = {
		_order  nullable: false, blank: false, unique : 'subscription'
		name  nullable: false, blank: false
		subscription  nullable: true, blank: true
		resultClass  nullable: false, blank: false,inList : [USER,GROUP,DEPARTMENT,ROLE,USER_MANAGER,USER_DEPARTMENT]
		resultInstance  nullable: false, blank: false
		_action nullable: false, blank: false, inList : [EMAIL,SUBSCRIBE,FETCH_EMAIL]
		status nullable: false, blank: false, inList : [ACTIVE,INACTIVE]
		createdBy nullable: true, blank: true
		updatedBy nullable: true, blank: true
		lastUpdated nullable: true, blank: true
		dateCreated nullable: true, blank: true
	
	}
	static defaultSort = '_order'
	static hasMany   = [rule: Rule]
	static belongsTo = [subscription:Subscription]
	static mapping = {
		rule lazy: false, cascade: "all,delete-orphan"
	}
	String toString(){
		"$name"
	}
	
}