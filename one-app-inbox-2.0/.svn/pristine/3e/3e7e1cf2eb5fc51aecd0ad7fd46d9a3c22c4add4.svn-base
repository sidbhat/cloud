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
    <title><g:message code="domainTenantMap.list" default="DomainTenantMap List"/></title>
</head>

<body>
<div class="nav">
    <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home"/></a>
    </span>
    <span class="menuButton"><g:link class="create" action="create"><g:message code="domainTenantMap.new"
                                                                               default="New DomainTenantMap"/></g:link></span>
</div>

<div class="body">
    <h1><g:message code="domainTenantMap.list" default="DomainTenantMap List"/></h1>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <div class="list">
        <table>
            <thead>
            <tr>

                <g:sortableColumn property="id" title="Id" titleKey="domainTenantMap.id"/>

                <g:sortableColumn property="domainName" title="Domain Name" titleKey="domainTenantMap.domainName"/>

                <g:sortableColumn property="mappedTenantId" title="Mapped Tenant Id"
                                  titleKey="domainTenantMap.mappedTenantId"/>

                <g:sortableColumn property="name" title="Name" titleKey="domainTenantMap.name"/>

            </tr>
            </thead>
            <tbody>
            <g:each in="${domainTenantMapInstanceList}" status="i" var="domainTenantMapInstance">
                <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                    <td><g:link action="show"
                                id="${domainTenantMapInstance.id}">${fieldValue(bean: domainTenantMapInstance, field: "id")}</g:link></td>

                    <td>${fieldValue(bean: domainTenantMapInstance, field: "domainName")}</td>

                    <td>${fieldValue(bean: domainTenantMapInstance, field: "mappedTenantId")}</td>

                    <td>${fieldValue(bean: domainTenantMapInstance, field: "name")}</td>

                </tr>
            </g:each>
            </tbody>
        </table>
    </div>

    <div class="paginateButtons">
        <g:paginate total="${domainTenantMapInstanceTotal}"/>
    </div>
</div>
</body>
</html>
