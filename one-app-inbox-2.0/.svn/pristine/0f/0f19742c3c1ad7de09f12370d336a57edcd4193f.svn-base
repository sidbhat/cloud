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
<%@ page import="com.oneapp.cloud.core.social.*" %>
<%@ page import="com.oneapp.cloud.core.*" %>
<html>
<head>
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'button.css')}"  />
</head>

<body>

<table>
<tr class="prop">
    <td valign="top" class="name">
        <label for="username"><g:message code="user.username" default="Username"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
        <g:textField name="username" readonly="true" value="${fieldValue(bean: userInstance, field: 'username')}"/>

    </td>
</tr>


<tr class="prop">
    <td valign="top" class="name">
        <label for="firstName"><g:message code="user.firstName" default="First Name"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'firstName', 'errors')}">
        <g:textField name="firstName" value="${fieldValue(bean: userInstance, field: 'firstName')}"/>

    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="lastName"><g:message code="user.lastName" default="Last Name"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'lastName', 'errors')}">
        <g:textField name="lastName" value="${fieldValue(bean: userInstance, field: 'lastName')}"/>

    </td>
</tr>
<tr class="prop">
    <td valign="top" class="name">
        <label for="department"><g:message code="user.department" default="Department"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'department', 'errors')}">
        <g:textField name="department" value="${fieldValue(bean: userInstance, field: 'department')}"/>

    </td>
</tr>
<tr class="prop">
    <td valign="top" class="name">
        <label for="groups"><g:message code="user.groups" default="Groups"/>:</label>
    </td>
    <td valign="top">
        <% def u = GroupDetails.list() 
       
          u.each {  if ( it.user.contains(userInstance)  ){%>
				<input type="button" class="button1 small green" value="${it.groupName}"/>
		 <%}
		 }%>
    </td>
</tr>
</table>

</body>
</html>
