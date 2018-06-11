

<%@ page import="com.oneapp.cloud.core.BillingHistory" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="mobile">
        <g:set var="entityName" value="${message(code: 'billingHistory.label', default: 'BillingHistory')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
    </head>
    <body>
		<div data-role="header" data-position="fixed">
			<h1><g:message code="default.edit.label" args="[entityName]" /></h1>
			<div data-role="navbar">
				<ul>
					<li><a data-icon="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
					<li><g:link data-icon="grid" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				</ul>
			</div>
		</div>
		<div data-role="content">
			<g:if test="${flash.message}">
			<div class="message" role="alert">${flash.message}</div>
			</g:if>
			<g:hasErrors bean="${billingHistoryInstance}">
			<div class="errors" role="alert">
				<g:renderErrors bean="${billingHistoryInstance}" as="list" />
			</div>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${billingHistoryInstance?.id}" />
				<g:hiddenField name="version" value="${billingHistoryInstance?.version}" />
			
				<div data-role="fieldcontain">
					<label for="client"><g:message code="billingHistory.client.label" default="Client" /></label>
					<g:select name="client.id" from="${com.oneapp.cloud.core.Client.list()}" optionKey="id" value="${billingHistoryInstance?.client?.id}"  />

				</div>
			
				<div data-role="fieldcontain">
					<label for="description"><g:message code="billingHistory.description.label" default="Description" /></label>
					<g:textField name="description" value="${fieldValue(bean: billingHistoryInstance, field: 'description')}" />

				</div>
			
				<div data-role="fieldcontain">
					<label for="amount"><g:message code="billingHistory.amount.label" default="Amount" /></label>
					<g:textField name="amount" value="${fieldValue(bean: billingHistoryInstance, field: 'amount')}" />

				</div>
			
				<div data-role="fieldcontain">
					<label for="currency"><g:message code="billingHistory.currency.label" default="Currency" /></label>
					<g:currencySelect name="currency" value="${billingHistoryInstance?.currency}"  />

				</div>
			
				<div data-role="fieldcontain">
					<label for="billDate"><g:message code="billingHistory.billDate.label" default="Bill Date" /></label>
					<g:datePicker name="billDate" value="${billingHistoryInstance?.billDate}"  />

				</div>
			
				<div data-role="fieldcontain">
					<label for="transactionId"><g:message code="billingHistory.transactionId.label" default="Transaction Id" /></label>
					<g:textField name="transactionId" value="${fieldValue(bean: billingHistoryInstance, field: 'transactionId')}" />

				</div>
			
				<div data-role="fieldcontain">
					<label for="month"><g:message code="billingHistory.month.label" default="Month" /></label>
					<g:textField name="month" value="${fieldValue(bean: billingHistoryInstance, field: 'month')}" />

				</div>
			
				<g:actionSubmit data-icon="check" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
			</g:form>
		</div>
		<div data-role="footer">
		</div>
    </body>
</html>