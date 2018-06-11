package com.oneapp.cloud.core

import org.codehaus.groovy.grails.commons.GrailsApplication

class CheckRoleService {

  static transactional = false
  def springSecurityService
  def grailsApplication

  // Check if user is allowed update/delete operation
  boolean isAuthenticatedForUDOperation(object) {

    def sec = grailsApplication.mainContext.getBean('grails.plugins.springsecurity.SecurityTagLib')
  	// super admin can access everything  
    if (sec.ifAnyGranted(roles: "ROLE_SUPER_ADMIN") {'body'}) {
      return true
    } else if (sec.ifAnyGranted(roles: "ROLE_ADMIN") {'body'}) {
      // Check if client admin belongs to the same tenant
       def sessionUser = springSecurityService.getPrincipal()?.username?.toString()?.toLowerCase()
       def user =  User.findByUsername( object.createdBy.username )?.toString()?.toLowerCase()
       return ( sessionUser?.userTenantId == user?.userTenantId )
    } else if (sec.ifAnyGranted(roles: "ROLE_USER") {'body'}) {
      // Check if the person is the one the same as the created by	
      def creatorUserName = object.createdBy?.username?.toString()?.toLowerCase()
      def sessionUserName = springSecurityService.getPrincipal()?.username?.toString()?.toLowerCase()
      return (creatorUserName?.equals(sessionUserName))
    }
    return false
  }

}
