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

<%@ page import="com.oneapp.cloud.core.log.AppLog" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="appLog.list" default="App Log"/></title>
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
                <g:message code="appLog.list" default="AppLog"/> &nbsp;(${appLogInstanceTotal})</h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                    <tr>

                        <g:sortableColumn property="id" title="Id" titleKey="appLog.id"/>

                        <th><g:message code="appLog.user" default="User"/></th>

                        <g:sortableColumn property="ip" title="Ip" titleKey="appLog.ip"/>

                        <g:sortableColumn property="uri" title="Uri" titleKey="appLog.uri"/>

                   
                        <g:sortableColumn property="dateCreated" title="Date Created" titleKey="appLog.dateCreated"/>

                        <g:sortableColumn property="deviceType" title="Device Type" titleKey="appLog.deviceType"/>
                         
                        <g:sortableColumn property="deviceType" title="Msg Type" titleKey="appLog.msgType"/>

                    </tr>
                    </thead>
                    <tbody>
                    <g:each in="${appLogInstanceList}" status="i" var="appLogInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">

                            <td><g:link action="edit"
                                        id="${appLogInstance.id}">${fieldValue(bean: appLogInstance, field: "id")}</g:link></td>

                            <td>${fieldValue(bean: appLogInstance, field: "user")}</td>

                            <td>${fieldValue(bean: appLogInstance, field: "ip")}</td>

                            <td>${fieldValue(bean: appLogInstance, field: "uri")}</td>
                   
                            <td><g:formatDate date="${appLogInstance.dateCreated}" formatName="format.date"/></td>

                            <td>${fieldValue(bean: appLogInstance, field: "deviceType")}</td>
                            <td>${fieldValue(bean: appLogInstance, field: "msgType")}</td>

                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <div class="paginateButtons">
                    <g:paginate total="${appLogInstanceTotal}"/>
                </div>
                
                 <export:formats formats="['pdf','excel']" controller="appLog" action="export"/>
            </div>
        </section>
    </div>
</section>

</body>
</html>
