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

<%@ page import="com.oneapp.cloud.core.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}"/>
    <title><g:message code="default.create.label" args="[entityName]"/></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <ul class="action-buttons clearfix fr">
        <li><a href="documentation/index.html" class="button button-gray no-text help" rel="#overlay">Help<span
                class="help"></span></a></li>
    </ul>

    <h2><g:message code="default.create.label" args="[entityName]"/></h2>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${accountInstance}">
        <div class="errors">
            <g:renderErrors bean="${accountInstance}" as="list"/>
        </div>
    </g:hasErrors>
</header>
<section class="container_6 clearfix">
    <div class="form grid_6">
        <g:form action="save" method="post">

            <div class="tabbed-pane">
                <ul class="tabs">
                    <li><a href="#">Basic</a></li>
                    <li><a href="#">Ship To</a></li>
                    <li><a href="#">Bill To</a></li>
                </ul>

                <!-- tab "panes" -->
                <div class="panes clearfix">
                    <section><table>
                        <tbody>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="name"><g:message code="account.name" default="Name"/><em>*</em>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: accountInstance, field: 'name', 'errors')}">
                                <g:textField name="name" maxlength="25"
                                             value="${fieldValue(bean: accountInstance, field: 'name')}"/>

                            </td>
                        </tr>
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="name"><g:message code="account.type" default="Account Type"/><em>*</em>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: accountInstance, field: 'accountType', 'errors')}">
                        		 <g:select name="accountType" from="${['Prospect','Active', 'Inactive']}"
                                  value="${accountInstance?.accountType}" noSelection="['null': '']"/>
							 </td>
                        </tr>
                        
                 
                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="owner"><g:link action="list" controller="user"><g:message code="account.owner" default="Owner"/></g:link>:</label>
                            </td>
            <td valign="top" class="value ${hasErrors(bean: accountInstance, field: 'user', 'errors')}">
                       <g:select name="user"
                          from="${User.findAllByUserTenantId(User.findByUsername(session.SPRING_SECURITY_CONTEXT.authentication?.principal?.username).userTenantId)}"
                          value="${accountInstance?.owner?.id}"
                          noSelection="['null': '']"
                          optionKey="id"/>

            </td>
                        </tr>

                        <tr class="prop">
                            <td valign="top" class="name">
                                <label for="contact"><g:link action="list" controller="contacts"><g:message code="account.contact" default="Contact"/></g:link>:</label>
                            </td>
                            <td valign="top"
                                class="value ${hasErrors(bean: accountInstance, field: 'contact', 'errors')}">
                                <g:select name="contact.id"
                                          from="${ContactDetails.list()}"
                                          optionKey="id" value="${accountInstance?.contact?.id}"
                                          noSelection="['null': '']"/>

                            </td>
                        </tr>

                        </tbody>
                    </table>
                    </section>

                    <section>
                        <table>
                            <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="shipTo.address1"><g:message code="address.address1"
                                                                            default="Address1"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'shipTo.address1', 'errors')}">
                                    <g:textField name="shipTo.address1" maxlength="125"
                                                 value="${fieldValue(bean: accountInstance, field: 'shipTo.address1')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="address2"><g:message code="address.address2"
                                                                     default="Address2"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'shipTo.address2', 'errors')}">
                                    <g:textField name="shipTo.address2" maxlength="125"
                                                 value="${fieldValue(bean: accountInstance, field: 'shipTo.address2')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city"><g:message code="address.city" default="City"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'shipTo.city', 'errors')}">
                                    <g:textField name="shipTo.city" maxlength="150"
                                                 value="${fieldValue(bean: accountInstance, field: 'shipTo.city')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state"><g:message code="address.state" default="State"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'shipTo.state', 'errors')}">
                                    <g:textField name="shipTo.state" maxlength="50"
                                                 value="${fieldValue(bean: accountInstance, field: 'shipTo.state')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country"><g:message code="address.country" default="Country"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'shipTo.country', 'errors')}">
                                    <g:textField name="shipTo.country" maxlength="50"
                                                 value="${fieldValue(bean: accountInstance, field: 'shipTo.country')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="zipCode"><g:message code="address.zipCode" default="Zip Code"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'shipTo.zipCode', 'shipTo.errors')}">
                                    <g:textField name="shipTo.zipCode"
                                                 value="${fieldValue(bean: accountInstance, field: 'shipTo.zipCode')}"/>

                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </section>
                    <section>
                        <table>
                            <tbody>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="billTo.address1"><g:message code="address.address1"
                                                                            default="Address1"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'billTo.address1', 'errors')}">
                                    <g:textField name="billTo.address1" maxlength="125"
                                                 value="${fieldValue(bean: accountInstance, field: 'billTo.address1')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="address2"><g:message code="address.address2"
                                                                     default="Address2"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'billTo.address2', 'errors')}">
                                    <g:textField name="billTo.address2" maxlength="125"
                                                 value="${fieldValue(bean: accountInstance, field: 'billTo.address2')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="city"><g:message code="address.city" default="City"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'billTo.city', 'errors')}">
                                    <g:textField name="billTo.city" maxlength="150"
                                                 value="${fieldValue(bean: accountInstance, field: 'billTo.city')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state"><g:message code="address.state" default="State"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'billTo.state', 'errors')}">
                                    <g:textField name="billTo.state" maxlength="50"
                                                 value="${fieldValue(bean: accountInstance, field: 'billTo.state')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="country"><g:message code="address.country" default="Country"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'billTo.country', 'errors')}">
                                    <g:textField name="billTo.country" maxlength="50"
                                                 value="${fieldValue(bean: accountInstance, field: 'billTo.country')}"/>

                                </td>
                            </tr>

                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="zipCode"><g:message code="address.zipCode" default="Zip Code"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: accountInstance, field: 'billTo.zipCode', 'billTo.errors')}">
                                    <g:textField name="billTo.zipCode"
                                                 value="${fieldValue(bean: accountInstance, field: 'billTo.zipCode')}"/>

                                </td>
                            </tr>
                            </tbody>
                        </table>
                    </section>
                </div>
            </div>

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
