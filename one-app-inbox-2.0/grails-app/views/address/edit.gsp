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

<%@ page import="com.oneapp.cloud.core.Address" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="address.edit" default="Edit Address"/></title>
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
            <g:message code="address.edit" default="Edit Address"/></h2>
        <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                            default="${flash.defaultMessage}"/></div>
        </g:if>
        <g:hasErrors bean="${addressInstance}">
            <div class="errors">
                <g:renderErrors bean="${addressInstance}" as="list"/>
            </div>
        </g:hasErrors>
    </header>
<section class="container_6 clearfix">
<div class="form grid_6">
    <g:form method="post">
        <g:hiddenField name="id" value="${addressInstance?.id}"/>
        <g:hiddenField name="version" value="${addressInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="address1"><g:message code="address.address1" default="Address1"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'address1', 'errors')}">
                        <g:textField name="address1" maxlength="125"
                                     value="${fieldValue(bean: addressInstance, field: 'address1')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="address2"><g:message code="address.address2" default="Address2"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'address2', 'errors')}">
                        <g:textField name="address2" maxlength="125"
                                     value="${fieldValue(bean: addressInstance, field: 'address2')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="city"><g:message code="address.city" default="City"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'city', 'errors')}">
                        <g:textField name="city" maxlength="150"
                                     value="${fieldValue(bean: addressInstance, field: 'city')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="state"><g:message code="address.state" default="State"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'state', 'errors')}">
                        <g:textField name="state" maxlength="50"
                                     value="${fieldValue(bean: addressInstance, field: 'state')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="country"><g:message code="address.country" default="Country"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'country', 'errors')}">
                        <g:textField name="country" maxlength="50"
                                     value="${fieldValue(bean: addressInstance, field: 'country')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="tenantId"><g:message code="address.tenantId" default="Tenant Id"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'tenantId', 'errors')}">
                        <g:textField name="tenantId" value="${fieldValue(bean: addressInstance, field: 'tenantId')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="zipCode"><g:message code="address.zipCode" default="Zip Code"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: addressInstance, field: 'zipCode', 'errors')}">
                        <g:textField name="zipCode" value="${fieldValue(bean: addressInstance, field: 'zipCode')}"/>

                    </td>
                </tr>

                </tbody>
            </table>
        </div>

        <div class="action">
            <g:actionSubmit class="button button-green" action="update"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            <g:actionSubmit class="button button-red" action="delete"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/>
        </div>

        </section>
        </div>
    </g:form>
</div>
</section>
</body>
</html>

