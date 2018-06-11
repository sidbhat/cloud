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

<%@ page import="com.oneapp.cloud.core.Account" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="account.list" default="Account List"/></title>
</head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <h2>
                <g:message code="account.list" default="Account List"/> &nbsp;(${accountInstanceTotal})</h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <section class="with-table">
                    <table class="datatable tablesort selectable full">
                        <thead>
                        <tr>

                            <g:sortableColumn property="name" title="Name" titleKey="account.name"/>

                            <th><g:message code="account.owner" default="Owner"/></th>

                            <th><g:message code="account.contact" default="Contact"/></th>

                        </tr>
                        </thead>
                        <tbody>
                        <g:each in="${accountInstanceList}" status="i" var="accountInstance">
                            <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                                <td><g:link action="edit"
                                            id="${accountInstance.id}">${fieldValue(bean: accountInstance, field: "name")}</g:link></td>

                                <td>${fieldValue(bean: accountInstance, field: "owner")}</td>

                                <td>${fieldValue(bean: accountInstance, field: "contact")}</td>

                            </tr>
                        </g:each>
                        </tbody>
                    </table>
                </section>

                <div class="paginateButtons">
                    <g:paginate total="${accountInstanceTotal}"/>
                </div>

                <div class="grid_6">
                    <g:link action="create" class="button button-gray"><span
                            class="add"></span>${message(code: 'create', 'default': 'Create')}</g:link>
                </div>

            </div>
        </section>
    </div>
</section>

</body>
</html>
