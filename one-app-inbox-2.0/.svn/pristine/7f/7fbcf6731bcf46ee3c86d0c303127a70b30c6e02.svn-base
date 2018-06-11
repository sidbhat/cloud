<%@ page import="com.oneapp.cloud.core.*" %>
<!doctype html>
<html>
    <head>
        <meta name="layout" content="mobile">
        <g:set var="entityName" value="${message(code: 'account.label', default: 'Account')}" />
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
			<g:hasErrors bean="${accountInstance}">
			<div class="errors" role="alert">
				<g:renderErrors bean="${accountInstance}" as="list" />
			</div>
			</g:hasErrors>
			<g:form method="post" >
			<% if ( accountInstance?.id != null) { %>
				<g:hiddenField name="id" value="${accountInstance?.id}" />
				<g:hiddenField name="version" value="${accountInstance?.version}" />
			<%}%>
			
				<div data-role="fieldcontain">
					<label for="name"><g:message code="account.name.label" default="Name" /></label>
					<g:textField name="name" maxlength="54" required="required" value="${accountInstance?.name}" />
				</div>
				
				<div data-role="fieldcontain">
					<label for="accountType"><g:message code="account.accountType.label" default="Account Type" /></label>
	
			       		 <g:select name="accountType" from="${['Prospect','Active', 'Inactive']}"
                                  value="${accountInstance?.accountType}" noSelection="['null': '']"/>
				
				</div>
			
				<div data-role="fieldcontain">
					<label for="owner"><g:message code="account.owner.label" default="Owner" /></label>
	    		<g:select name="user"
                          from="${User.findAllByUserTenantId(User.findByUsername(session.SPRING_SECURITY_CONTEXT.authentication?.principal?.username).userTenantId)}"
                          value="${accountInstance?.owner?.id}"
                          noSelection="['null': '']"
                          optionKey="id" />
				</div>
				
				<div data-role="fieldcontain">
					<label for="status"><g:message code="account.status.label" default="Status" /></label>
					<g:select name="status.id" from="${com.oneapp.cloud.core.DropDown.list()}" optionKey="id" value="${accountInstance?.status?.id}" noSelection="['null': '']" />
				</div>
			
	
			
				<div data-role="fieldcontain">
					<label for="contact"><g:message code="account.contact.label" default="Contact" /></label>
					<g:select name="contact.id" from="${com.oneapp.cloud.core.ContactDetails.list()}" optionKey="id" value="${accountInstance?.contact?.id}" noSelection="['null': '']" />
				</div>
			
				<div data-role="fieldcontain">
					<label for="shipTo"><g:message code="account.shipTo.label" default="Ship To" /></label>
					
				</div>
			
				<div data-role="fieldcontain">
					<label for="billTo"><g:message code="account.billTo.label" default="Bill To" /></label>
					
				</div>
			
		
		
			<% if ( accountInstance?.id != null) { %>
				<g:actionSubmit data-icon="check" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" />
			<%}else{%>
				<g:actionSubmit data-icon="check" action="save" value="${message(code: 'default.button.save.label', default: 'Create')}" />
			<%}%>
		</g:form>
		</div>
		<div data-role="footer">
		</div>
    </body>
</html>
