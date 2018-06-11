
<%@ page import="com.oneapp.cloud.core.ApplicationConf" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="mobile">
        <g:set var="entityName" value="${message(code: 'applicationConf.label', default: 'ApplicationConf')}" />
        <title><g:message code="default.show.label" args="[entityName]" /></title>
    </head>
    <body>
		<div data-role="header" data-position="fixed">
			<h1><g:message code="default.show.label" args="[entityName]" /></h1>
			<div data-role="navbar">
				<ul>
					<li><a data-icon="home" href="${createLink(uri: '/')}"><g:message code="default.home.label"/></a></li>
					<li><g:link data-icon="grid" action="list"><g:message code="default.list.label" args="[entityName]" /></g:link></li>
				</ul>
			</div>
		</div>
		<div data-role="content">
			<g:if test="${flash.message}">
			<div class="message">${flash.message}</div>
			</g:if>
			<dl>
			
				<dt><g:message code="applicationConf.id.label" default="Id" /></dt>
				
					<dd><g:fieldValue bean="${applicationConfInstance}" field="id"/></dd>
				
			
				<dt><g:message code="applicationConf.sendEmailDefaultFrom.label" default="Send Email Default From" /></dt>
				
					<dd><g:fieldValue bean="${applicationConfInstance}" field="sendEmailDefaultFrom"/></dd>
				
			
				<dt><g:message code="applicationConf.asynEmailJobInterval.label" default="Asyn Email Job Interval" /></dt>
				
					<dd><g:fieldValue bean="${applicationConfInstance}" field="asynEmailJobInterval"/></dd>
				
			
				<dt><g:message code="applicationConf.rulesJobInterval.label" default="Rules Job Interval" /></dt>
				
					<dd><g:fieldValue bean="${applicationConfInstance}" field="rulesJobInterval"/></dd>
				
			
				<dt><g:message code="applicationConf.logInfo.label" default="Log Info" /></dt>
				
					<dd><g:formatBoolean boolean="${applicationConfInstance?.logInfo}" /></dd>
				
			
			</dl>
			<g:form>
				<g:hiddenField name="id" value="${applicationConfInstance?.id}" />
				<g:actionSubmit data-icon="delete" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" />
			</g:form>
		</div>
		<div data-role="footer">
		</div>
    </body>
</html>