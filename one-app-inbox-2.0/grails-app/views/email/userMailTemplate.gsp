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

<%@ page contentType="text/html" %>
<%@ page import="com.oneapp.cloud.core.*" %>
<%@ page import="org.grails.formbuilder.*" %>
<%@ page import="grails.converters.JSON" %>
<!DOCTYPE html PUBLIC "-//W3C//DTD XHTML 1.0 Transitional//EN" "http://www.w3.org/TR/xhtml1/DTD/xhtml1-transitional.dtd">
<html>
<head>
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title>*|MC:SUBJECT|*</title>
</head>

<body leftmargin="0" marginwidth="0" topmargin="0" marginheight="0" offset="0">
	<div>Hello ${userFirstName},</div>
	<br/>
	<div>Since yesterday you have </div>
	<g:if test="${activityFeedList}">
		<div><b>[${activityFeedList.size()}] Feeds</b></div>
		<g:set var="groupedList" value="${activityFeedList?.groupBy{it.createdBy}}"/>
		<g:each in="${groupedList.keySet()}" var="keyObj">
			<div>${keyObj} - ${groupedList.getAt(keyObj).size() } new</div>
		</g:each>
		<br/>
	</g:if>
	<g:set var="formGroupedList" value="${activityFeedList?.groupBy{it.config.className}}"/>
	<g:each in="${formGroupedList.keySet()}" var="formlKeyObj">
		<g:if test="${'Approval'.equalsIgnoreCase(formGroupedList.getAt(formlKeyObj)[0].config.shareType) }">
			<%
			def ruleActivityDomainClass = DomainClass.findByName(formlKeyObj)
			def ruleActivityFormObj = Form.findByDomainClass(ruleActivityDomainClass)
			def ruleActivityFieldInstance = JSON.parse(ruleActivityFormObj.settings)
			def formName = ruleActivityFieldInstance.en.name
		 	%>
			<div><b>[${approvalCount}] Approvals</b></div>
				<div>${formName} - ${formGroupedList.getAt(formlKeyObj).size() } new</div>
		</g:if>
		<g:elseif test="${'Survey'.equalsIgnoreCase(formGroupedList.getAt(formlKeyObj)[0].config.shareType) ||  'Poll'.equalsIgnoreCase(formGroupedList.getAt(formlKeyObj)[0].config.shareType)}">
			<%
			def ruleActivityDomainClass = DomainClass.findByName(formlKeyObj)
			def ruleActivityFormObj = Form.findByDomainClass(ruleActivityDomainClass)
			def ruleActivityFieldInstance = JSON.parse(ruleActivityFormObj.settings)
			def formName = ruleActivityFieldInstance.en.name
		 	%>
			<br/><div><b>[${surveypollCount}] Surveys and Polls</b></div>
				<div>${formName} - ${formGroupedList.getAt(formlKeyObj).size() } new</div>
		</g:elseif>
		<g:elseif test="${'Survey'.equalsIgnoreCase(formGroupedList.getAt(formlKeyObj)[0].config.shareType) ||  'Poll'.equalsIgnoreCase(formGroupedList.getAt(formlKeyObj)[0].config.shareType)}">
			<%
			def ruleActivityDomainClass = DomainClass.findByName(formlKeyObj)
			def ruleActivityFormObj = Form.findByDomainClass(ruleActivityDomainClass)
			def ruleActivityFieldInstance = JSON.parse(ruleActivityFormObj.settings)
			def formName = ruleActivityFieldInstance.en.name
		 	%>
			<br/><div><b>[${surveypollCount}] Surveys and Polls</b></div>
				<div>${formName} - ${formGroupedList.getAt(formlKeyObj).size() } new</div>
		</g:elseif>
	</g:each>
	<g:if test="${taskList}">
		<br/>
		<div><b>[${taskList.size()}] Tasks</b></div>
		<g:each in="${taskList}" var="tasks" >
			<div>Reference Id - ${tasks.referenceId} Due Date : ${g.formatDate(format:'MM/dd/yyyy',date:tasks?.dueDate)}</div>
		</g:each>
	</g:if>
	<g:if test="${recentTask}">
		<br/>
		<div><b>[${recentTask.size()}] Recent Tasks</b></div>
		<g:each in="${recentTask}" var="recentTask" >
			<div>Reference Id - ${recentTask.referenceId} Due Date : ${g.formatDate(format:'MM/dd/yyyy',date:recentTask?.dueDate)}</div>
		</g:each>
	</g:if>
	<br/>
	<g:if test="${bdayUsers}">
		<div><b>[${bdayUsers.size()}] Birthdays</b></div>
		<g:each in="${bdayUsers}">
			${it[0]} ${it[1] }
		</g:each>
	</g:if>
	
	<div>&nbsp;</div>
	<div>
		Click <a target="_blank" href="${grailsApplication.config.grails.serverURL}">here</a> to see your Form Builder Inbox.
	</div>
	<br/>
	<div>Thank You</div>
	<div>Your Form Builder Team!</div>
</body>
</html>