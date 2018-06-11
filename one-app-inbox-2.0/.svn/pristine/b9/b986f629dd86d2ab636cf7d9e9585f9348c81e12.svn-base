

<%@ page import="com.oneapp.cloud.core.ApplicationConf" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="mobile">
        <g:set var="entityName" value="${message(code: 'applicationConf.label', default: 'ApplicationConf')}" />
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
			<g:hasErrors bean="${applicationConfInstance}">
			<div class="errors" role="alert">
				<g:renderErrors bean="${applicationConfInstance}" as="list" />
			</div>
			</g:hasErrors>
			<g:form method="post" >
				<g:hiddenField name="id" value="${applicationConfInstance?.id}" />
				<g:hiddenField name="version" value="${applicationConfInstance?.version}" />
			
				<div data-role="fieldcontain">
					<label for="sendEmailDefaultFrom"><g:message code="applicationConf.sendEmailDefaultFrom.label" default="Send Email Default From" /></label>
					<g:textField name="sendEmailDefaultFrom" value="${fieldValue(bean: applicationConfInstance, field: 'sendEmailDefaultFrom')}" />

				</div>
			
				<div data-role="fieldcontain">
					<label for="asynEmailJobInterval"><g:message code="applicationConf.asynEmailJobInterval.label" default="Asyn Email Job Interval" /></label>
					<g:textField name="asynEmailJobInterval" value="${fieldValue(bean: applicationConfInstance, field: 'asynEmailJobInterval')}" />

				</div>
			
				<div data-role="fieldcontain">
					<label for="rulesJobInterval"><g:message code="applicationConf.rulesJobInterval.label" default="Rules Job Interval" /></label>
					<g:textField name="rulesJobInterval" value="${fieldValue(bean: applicationConfInstance, field: 'rulesJobInterval')}" />

				</div>
			
				<div data-role="fieldcontain">
					<label for="logInfo"><g:message code="applicationConf.logInfo.label" default="Log Info" /></label>
					<g:checkBox name="logInfo" value="${applicationConfInstance?.logInfo}" />

				</div>
			
				<g:actionSubmit data-icon="check" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
			</g:form>
		</div>
		<div data-role="footer">
		</div>
    </body>
</html>