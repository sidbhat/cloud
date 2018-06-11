%{----------------------------------------------------------------------------
  - [ NIKKISHI CONFIDENTIAL ]                                                -
  -                                                                          -
  -    Copyright (c) 2011.  Nikkishi LLC                                     -
  -    All Rights Reserved.                                                  -
  -                                                                          -
  -   NOTICE:  All information contained herein is, and remains              -
  -   the property of Nikkishi LLC and its suppliers,                        -
  -   if any.  The intellectual and technical concepts contained             -
  -   herein are proprietary to Nikkishi LLC and its                         -
  -   suppliers and may be covered by U.S. and Foreign Patents,              -
  -   patents in process, and are protected by trade secret or copyright law.
  -   Dissemination of this information or reproduction of this material     -
  -   is strictly forbidden unless prior written permission is obtained      -
  -   from Nikkishi LLC.                                                     -
  ----------------------------------------------------------------------------}%

<%@ page import="tenant.DomainTenantMap" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="domainTenantMap.create" default="Create DomainTenantMap"/></title>
</head>

<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home"/></a>
    </span>
    <span class="menuButton"><g:link class="list" action="list"><g:message code="domainTenantMap.list"
                                                                           default="DomainTenantMap List"/></g:link></span>
</div>

<div class="body">
    <h1><g:message code="domainTenantMap.create" default="Create DomainTenantMap"/></h1>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${domainTenantMapInstance}">
        <div class="errors">
            <g:renderErrors bean="${domainTenantMapInstance}" as="list"/>
        </div>
    </g:hasErrors>
    <g:form action="save" method="post">
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="domainName"><g:message code="domainTenantMap.domainName"
                                                           default="Domain Name"/>:</label>
                    </td>
                    <td valign="top"
                        class="value ${hasErrors(bean: domainTenantMapInstance, field: 'domainName', 'errors')}">
                        <g:textField name="domainName"
                                     value="${fieldValue(bean: domainTenantMapInstance, field: 'domainName')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="mappedTenantId"><g:message code="domainTenantMap.mappedTenantId"
                                                               default="Mapped Tenant Id"/>:</label>
                    </td>
                    <td valign="top"
                        class="value ${hasErrors(bean: domainTenantMapInstance, field: 'mappedTenantId', 'errors')}">
                        <g:textField name="mappedTenantId"
                                     value="${fieldValue(bean: domainTenantMapInstance, field: 'mappedTenantId')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="domainTenantMap.name" default="Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: domainTenantMapInstance, field: 'name', 'errors')}">
                        <g:textField name="name" value="${fieldValue(bean: domainTenantMapInstance, field: 'name')}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="buttons">
            <span class="button"><g:submitButton name="create" class="save"
                                                 value="${message(code: 'create', 'default': 'Create')}"/></span>
        </div>
    </g:form>
</div>
</body>
</html>
