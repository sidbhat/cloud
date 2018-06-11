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
    <title><g:message code="appLog.edit" default="App Log"/></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
    <header>
        <h2>
            <g:message code="appLog.edit" default="App Log"/></h2>
        <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                            default="${flash.defaultMessage}"/></div>
        </g:if>
        <g:hasErrors bean="${appLogInstance}">
            <div class="errors">
                <g:renderErrors bean="${appLogInstance}" as="list"/>
            </div>
        </g:hasErrors>
    </header>
<section class="container_6 clearfix">
<div class="form grid_6">
    <g:form method="post">
        <g:hiddenField name="id" value="${appLogInstance?.id}"/>
        <g:hiddenField name="version" value="${appLogInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="user"><g:message code="appLog.user" default="User"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: appLogInstance, field: 'user', 'errors')}">
                        <g:textField name="user.id" value="${appLogInstance?.user?.username}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="ip"><g:message code="appLog.ip" default="Ip"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: appLogInstance, field: 'ip', 'errors')}">
                        <g:textField name="ip" value="${fieldValue(bean: appLogInstance, field: 'ip')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="uri"><g:message code="appLog.uri" default="Uri"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: appLogInstance, field: 'uri', 'errors')}">
                        <g:textField name="uri" value="${fieldValue(bean: appLogInstance, field: 'uri')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="lastUpdated"><g:message code="appLog.lastUpdated" default="Last Updated"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: appLogInstance, field: 'lastUpdated', 'errors')}">
                        <input type="date" name="lastUpdated" value="${appLogInstance?.lastUpdated}"/>

                    </td>
                </tr>
                   <tr class="prop">
                    <td valign="top" class="name">
                        <label for="msg"><g:message code="appLog.message" default="Message"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: appLogInstance, field: 'msg', 'errors')}">
                        <g:textArea name="msg" value="${appLogInstance?.msg}"/>

                    </td>
                </tr>

         <tr class="prop">
                    <td valign="top" class="name">
                        <label for="deviceType"><g:message code="appLog.deviceType" default="Device Type"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: appLogInstance, field: 'deviceType', 'errors')}">
                        <input type="text" name="deviceType" value="${appLogInstance?.deviceType}"/>

                    </td>
                </tr>


                </tbody>
            </table>
        </div>
		     <div class="action">
                   <span class="button"> <g:actionSubmit controller="appLog" action="list" 
                            class="button button-green" value="${message(code: 'back', 'default': 'Back')}"/></span>
                </div>

        </section>
     

        </section>
        </div>
    </g:form>
</div>
</section>
</body>
</html>

