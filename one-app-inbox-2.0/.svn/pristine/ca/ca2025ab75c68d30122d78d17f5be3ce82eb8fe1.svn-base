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

<%@ page import="com.oneapp.cloud.core.DropDown" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="dropDown.edit" default="Edit DropDown"/></title>
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
            <g:message code="dropDown.edit" default="Edit DropDown"/></h2>
        <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                            default="${flash.defaultMessage}"/></div>
        </g:if>
        <g:hasErrors bean="${dropDownInstance}">
            <div class="errors">
                <g:renderErrors bean="${dropDownInstance}" as="list"/>
            </div>
        </g:hasErrors>
    </header>
<section class="container_6 clearfix">
<div class="form grid_6">
    <g:form method="post">
        <g:hiddenField name="id" value="${dropDownInstance?.id}"/>
        <g:hiddenField name="version" value="${dropDownInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="type"><g:message code="dropDown.type" default="Type"/>:</label>
                    </td>
                    <td valign="top"
                        class="value ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}"><g:textField
                            name="name" readonly="true" value="${fieldValue(bean: dropDownInstance, field: 'type')}"/>
                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="dropDown.name" default="Name"/><em>*</em>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: dropDownInstance, field: 'name', 'errors')}">
                        <g:textField name="name" readonly="true"
                                     value="${fieldValue(bean: dropDownInstance, field: 'name')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="description"><g:message code="dropDown.description"
                                                            default="Description"/><em>*</em>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: dropDownInstance, field: 'description', 'errors')}">
                        <g:textField name="description"
                                     value="${fieldValue(bean: dropDownInstance, field: 'description')}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="action">
            <g:actionSubmit class="button button-green" action="update" style="width: 140px"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            <!--g:actionSubmit class="button button-red" action="delete" style="width: 140px"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/-->
        </div>

        </section>
        </div>
    </g:form>
</div>
</section>
</body>
</html>

