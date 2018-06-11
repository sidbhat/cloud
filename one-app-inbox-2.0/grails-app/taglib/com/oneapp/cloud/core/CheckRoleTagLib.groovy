
package com.oneapp.cloud.core

class CheckRoleTagLib {


  static namespace = 'secure'
  def springSecurityService
  def checkRoleService
  def isAuthenticatedForUDOperation = { attrs, body ->

    if (checkRoleService.isAuthenticatedForUDOperation(attrs?.object)) {
      out << body()
    }

  }
}
