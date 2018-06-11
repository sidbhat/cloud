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

<%@ page import="com.oneapp.cloud.core.GroupDetails" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'groups.label', default: 'Groups')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <h2><g:message code="default.create.label" args="[entityName]"/></h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
            <g:hasErrors bean="${groupsInstance}">
                <div class="errors">
                    <g:renderErrors bean="${groupsInstance}" as="list"/>
                </div>
            </g:hasErrors>
        </header>
        <section class="container_6 clearfix">
            <div class="form grid_6">
                <g:form action="save" method="post" enctype="multipart/form-data">
                    <table>
                        <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="name"><g:message code="groups.groupName" default="Name"/>: <em>*</em></label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: groupsInstance, field: 'groupName', 'errors')}">
                                <g:textField name="groupName" value="${fieldValue(bean: groupsInstance, field: 'groupName')}"/>

                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="description"><g:message code="groups.description"
                                                                    default="Description"/>: <em>*</em></label>
                            </td>
                            <td valign="top"
                                class="value ${hasErrors(bean: groupsInstance, field: 'groupDescription', 'errors')}">
                                <g:textField name="groupDescription"
                                             value="${fieldValue(bean: groupsInstance, field: 'groupDescription')}"/>

                            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="url"><g:message code="groups.url" default="Url"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: groupsInstance, field: 'url', 'errors')}">
                                <g:textField name="url" value="${fieldValue(bean: groupsInstance, field: 'url')}"/>

                            </td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="groupType"><g:message code="groups.groupType"
                                                                  default="Group Type"/>:</label>
                            </td>
                            <td valign="top"
                                class="value ${hasErrors(bean: groupsInstance, field: 'groupType', 'errors')}">
                                <g:select name="groupType.id"
                                          from="${com.oneapp.cloud.core.DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.GROUP_TYPE)}"
                                          optionKey="id" value="${groupsInstance?.groupType?.id}"/>

                            </td>
                        </tr>
                        <%--



                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="image"><g:message code="groups.image" default="Image"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: groupsInstance, field: 'image', 'errors')}">
                                <input type="file" id="image" name="image"/>

                            </td>
                        </tr>

                        --%>
                        </tbody>
                    </table>

                    <div class="action">
                        <span class="button"><g:submitButton name="create" class="button button-green"
                                                             value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
                    </div>
                </g:form>
            </div>
        </section>
    </div>
</section>
</body>
</html>
