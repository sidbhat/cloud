
<%@ page import="com.oneapp.cloud.core.ActivityFeed" %>
<%@ page import="com.oneapp.cloud.core.User" %>
<%@ page import="org.grails.formbuilder.*" %>
<%@ page import="grails.converters.JSON" %>

<!DOCTYPE html>
<html>
<head>
<title>${viewName} &nbsp;(${total})</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="mobile">
</head>
<body>
	
	<% 
		def domainClass = DomainClass.findByName(activityFeed?.config?.configName)
		def formObj = Form.findByDomainClass(domainClass)
		def fieldName = FormAdmin.findByForm(formObj).statusField.name
		def fieldInstance = JSON.parse(Field.findByFormAndName(formObj,fieldName).settings)
		def activityId = attrs?.activityFeedInstance?.id
		def fieldValueList = fieldInstance.en.value
	%>
	<div class="content-primary">
		<nav>
			<div data-role="content">
				<%fieldValueList.each{ %>
					<a href="${grailsApplication.config.grails.serverURL}/dashboard/changeStatusDevice?status=${it}&activityId=${activityFeed?.id}" data-role="button" >${it}</a>
				<%}%>
				<a href="${createLink(uri: '/dashboard/index')}" data-role="button" data-theme="b" class="ui-btn-up-b"><g:message code="default.label" default="Cancel"/></a>
			</div>
		</nav>
		
	</div>
</body>