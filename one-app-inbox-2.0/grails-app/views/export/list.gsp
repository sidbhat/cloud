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

<%@ page import="com.oneapp.cloud.time.*" %>
<%@ page import="com.oneapp.cloud.expense.*" %>
<%@ page import="com.oneapp.cloud.core.*" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="export.list" default="Export Data"/></title>
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
                <g:message code="export.list" default="Export List"/></h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table>
                    <thead>
                    <tr>

                        <g:sortableColumn property="type" title="Type" titleKey="export.type"/>
                        <g:sortableColumn property="format" title="Format" titleKey="export.format"/>

                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${exportInstanceList}" status="i" var="exportInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                            <td><g:link action="show"
                                        id="${taskInstance.id}">${fieldValue(bean: taskInstance, field: "id")}</g:link></td>
                            <td>${fieldValue(bean: taskInstance, field: "dueDate")}</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <div class="paginateButtons">
                    <g:paginate total="${taskInstanceTotal}"/>
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
