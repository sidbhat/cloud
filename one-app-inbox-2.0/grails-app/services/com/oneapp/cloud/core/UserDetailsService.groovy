package com.oneapp.cloud.core

import java.text.DateFormat;
import java.text.SimpleDateFormat;
import java.util.Collection;

import org.apache.log4j.Logger
import org.grails.plugins.localization.*
import org.springframework.security.core.GrantedAuthority
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils
import org.springframework.security.core.authority.GrantedAuthorityImpl
import org.springframework.security.core.userdetails.UserDetails
import org.springframework.security.core.userdetails.UsernameNotFoundException
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUser;
import org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService;
import org.codehaus.groovy.grails.plugins.springsecurity.GormUserDetailsService
import grails.plugin.multitenant.core.util.TenantUtils
import org.springframework.context.ApplicationContext
import org.springframework.dao.DataAccessException;

import com.oneapp.cloud.core.User

/**
 * Default implementation of <code>GrailsUserDetailsService</code> that uses
 * domain classes to load users and roles.
 *
 * @author <a href='mailto:burt@burtbeckwith.com'>Burt Beckwith</a>
 */
class UserDetailsService extends GormUserDetailsService {

	def userNotFoundStr = "User Not Found"
	def clientNotDefined = "User Account Not Found"
	def noDataSourceFound = "No Data-Source assigned, please contact administrator"
	def licenseNotDefined = "License not proper, please contact administrator"

	/**
	 * {@inheritDoc}
	 * @see org.codehaus.groovy.grails.plugins.springsecurity.GrailsUserDetailsService#loadUserByUsername(
	 * 	java.lang.String, boolean)
	 */
        @Override
	UserDetails loadUserByUsername(String username, boolean loadRoles) throws UsernameNotFoundException,DataAccessException {		

		User.withTransaction { status ->
			def user = User.findByUsername(username)
			
                        	
                        
                        if (!user) {
				log.warn "${userNotFoundStr}"
                                print "${userNotFoundStr}"
				throw new UsernameNotFoundException(userNotFoundStr, username)
			}
			Collection<GrantedAuthority> authorities = loadAuthorities(user, username, loadRoles)
			createUserDetails user, authorities
		}
	}
	
	/**
	 * {@inheritDoc}
	 * @see org.springframework.security.core.userdetails.UserDetailsService#loadUserByUsername(
	 * 	java.lang.String)
	 */
	UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {
		loadUserByUsername username, true
	}
	
	UserDetails createUserDetails(User user, Collection<GrantedAuthority> authorities) {
		
//		def conf = SpringSecurityUtils.securityConfig
//
//		String usernamePropertyName = conf.userLookup.usernamePropertyName
//		String passwordPropertyName = conf.userLookup.passwordPropertyName
//		String enabledPropertyName = conf.userLookup.enabledPropertyName
//		String accountExpiredPropertyName = conf.userLookup.accountExpiredPropertyName
//		String accountLockedPropertyName = conf.userLookup.accountLockedPropertyName
//		String passwordExpiredPropertyName = conf.userLookup.passwordExpiredPropertyName
//
//		String username = user."$usernamePropertyName"
//		String password = user."$passwordPropertyName"
//		boolean enabled = enabledPropertyName ? user."$enabledPropertyName" : true
//		boolean accountExpired = accountExpiredPropertyName ? user."$accountExpiredPropertyName" : false
//		boolean accountLocked = accountLockedPropertyName ? user."$accountLockedPropertyName" : false
//		boolean passwordExpired = passwordExpiredPropertyName ? user."$passwordExpiredPropertyName" : false

		new GrailsUser(user.username, user.password, user.enabled, user.accountExpired, user.passwordExpired,
				user.accountLocked, authorities, user.id)
	}

		
}
