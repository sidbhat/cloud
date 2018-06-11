
<%@ page import="com.oneapp.cloud.core.BillingHistory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="billingHistory.create" default="Create BillingHistory" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
            <span class="menuButton"><g:link class="list" action="list"><g:message code="billingHistory.list" default="BillingHistory List" /></g:link></span>
        </div>
        <div class="body">
            <h1><g:message code="billingHistory.create" default="Create BillingHistory" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${billingHistoryInstance}">
            <div class="errors">
                <g:renderErrors bean="${billingHistoryInstance}" as="list" />
            </div>
            </g:hasErrors>
            <g:form action="save" method="post" >
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="client"><g:message code="billingHistory.client" default="Client" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: billingHistoryInstance, field: 'client', 'errors')}">
                                    <g:select name="client.id" from="${com.oneapp.cloud.core.Client.list()}" optionKey="id" value="${billingHistoryInstance?.client?.id}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="description"><g:message code="billingHistory.description" default="Description" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: billingHistoryInstance, field: 'description', 'errors')}">
                                    <g:textField name="description" value="${fieldValue(bean: billingHistoryInstance, field: 'description')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="amount"><g:message code="billingHistory.amount" default="Amount" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: billingHistoryInstance, field: 'amount', 'errors')}">
                                    <g:textField name="amount" value="${fieldValue(bean: billingHistoryInstance, field: 'amount')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="currency"><g:message code="billingHistory.currency" default="Currency" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: billingHistoryInstance, field: 'currency', 'errors')}">
                                    <g:currencySelect name="currency" value="${billingHistoryInstance?.currency}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="billDate"><g:message code="billingHistory.billDate" default="Bill Date" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: billingHistoryInstance, field: 'billDate', 'errors')}">
                                    <g:datePicker name="billDate" value="${billingHistoryInstance?.billDate}"  />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="transactionId"><g:message code="billingHistory.transactionId" default="Transaction Id" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: billingHistoryInstance, field: 'transactionId', 'errors')}">
                                    <g:textField name="transactionId" value="${fieldValue(bean: billingHistoryInstance, field: 'transactionId')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="month"><g:message code="billingHistory.month" default="Month" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: billingHistoryInstance, field: 'month', 'errors')}">
                                    <g:textField name="month" value="${fieldValue(bean: billingHistoryInstance, field: 'month')}" />

                                </td>
                            </tr>
                        
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:submitButton name="create" class="save" value="${message(code: 'create', 'default': 'Create')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
