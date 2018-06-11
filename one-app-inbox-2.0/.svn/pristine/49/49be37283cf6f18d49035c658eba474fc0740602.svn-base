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
    <title><g:message code="contact.edit" default="Edit Contact"/></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h3><g:message code="contact.edit" default="Edit Contact"/></h3>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${contactInstance}">
        <div class="errors">
            <g:renderErrors bean="${contactInstance}" as="list"/>
        </div>
    </g:hasErrors>
</header>

<section class="container_6 clearfix">
<div class="form grid_6">
<g:form method="post" enctype="multipart/form-data">
<g:hiddenField name="id" value="${contactInstance?.id}"/>


<div class="tabbed-pane">
<ul class="tabs">
    <li><a href="#">Basic</a></li>
</ul>

<!-- tab "panes" -->
<div class="panes clearfix">
<section><table>
    <tbody>
	<tr class="prop">
        <td valign="top" class="name">
            <label for="headlines"><g:message code="contact.headlines" default="Headlines"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'headlines', 'errors')}">
            <g:textField name="headlines" readonly="true" value="${fieldValue(bean: contactInstance, field: 'headlines')}"/>
        </td>
    </tr>
    <% if ( contactInstance.source == null ) {%>
    <tr class="prop">
                            <td valign="top" class="name">
                                <label for="pictureURL"><g:message code="contact.pictureURL" default="Picture URL"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'pictureURL', 'errors')}">
                               <g:textField name="pictureURL" value="${fieldValue(bean: contactInstance, field: 'pictureURL')}"/>
   							  <img class="image" height="30" width="30"
                             	src="${contactInstance?.pictureURL}"/>
                   
                            </td>
   </tr>
   <%}else if ( contactInstance.source == "facebook" ) {%>
    <tr class="prop">
                            <td valign="top" class="name">
                                <label for="facebookPictureURL"><g:message code="contact.facebookPictureURL" default="Facebook Picture"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'facebookPictureURL', 'errors')}">
                          	  <img class="image" height="30" width="30"
                             	src="${contactInstance?.facebookPictureURL}"/>
                   
                            </td>
   </tr>
   <%} else if ( contactInstance.source == "linkedin" ) {%>
    <tr class="prop">
                            <td valign="top" class="name">
                                <label for="linkedinPictureURL"><g:message code="contact.linkedinPictureURL" default="Linkedin Profile"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'linkedinPictureURL', 'errors')}">
                       	 	&nbsp;&nbsp; <img class="image" height="30" width="30"
                             	src="${contactInstance?.linkedinPictureURL}"/> &nbsp;
                   			<a href="${fieldValue(bean: contactInstance, field: 'profileURL')}">Profile</a>
                            </td>
   </tr>
   <%} else if ( contactInstance.source == "twitter" ) {%>
   				 <tr class="prop">
                            <td valign="top" class="name">
                                <label for="twitterPictureURL"><g:message code="contact.twitterPictureURL" default="Twitter Picture"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'twitterPictureURL', 'errors')}">
                       	  <img class="image" height="30" width="30"
                             	src="${contactInstance?.twitterPictureURL}"/>
                   
                            </td>
  				 </tr>
  				  <tr class="prop">
                            <td valign="top" class="name">
                                <label for="currentStatus"><g:message code="contact.currentStatus" default="Status"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'currentStatus', 'errors')}">
                       	  		<g:textArea name="currentStatus" readonly="true" value="${fieldValue(bean: contactInstance, field: 'currentStatus')}"/>
                            </td>
  				 </tr>
  				  <tr class="prop">
                            <td valign="top" class="name">
                                <label for="followerCnt"><g:message code="contact.followerCnt" default="Followers"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'followerCnt', 'errors')}">
                       	  		<g:textField name="followerCnt" readonly="true" value="${fieldValue(bean: contactInstance, field: 'followerCnt')}"/>
                            </td>
  				 </tr>
  				 <tr class="prop">
                            <td valign="top" class="name">
                                <label for="friendCnt"><g:message code="contact.friendCnt" default="Friends"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'friendCnt', 'errors')}">
                       	  		<g:textField name="followerCnt" readonly="true" value="${fieldValue(bean: contactInstance, field: 'friendCnt')}"/>
                            </td>
  				 </tr>
   <%}%>
    <tr class="prop">
        <td valign="top" class="name">
            <label for="Status"><g:message code="contact.currentStatus" default="Status"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'currentStatus', 'errors')}">
            <g:textField name="currentStatus" readonly="true" value="${fieldValue(bean: contactInstance, field: 'currentStatus')}"/>
        </td>
    </tr>
   
     <tr class="prop">
        <td valign="top" class="name">
            <label for="username"><g:message code="contact.name" default="Name"/>: <em>*</em></label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'contactName', 'errors')}">
            <g:textField name="contactName" value="${fieldValue(bean: contactInstance, field: 'contactName')}"/>
        </td>
    </tr>
     <tr class="prop">
        <td valign="top" class="name">
            <label for="firstName"><g:message code="contact.firstName" default="firstName"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'firstName', 'errors')}">
            <g:textField name="firstName" value="${fieldValue(bean: contactInstance, field: 'firstName')}"/>
        </td>
    </tr>
       <tr class="prop">
        <td valign="top" class="name">
            <label for="lastName"><g:message code="contact.lastName" default="lastName"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'lastName', 'errors')}">
            <g:textField name="lastName" value="${fieldValue(bean: contactInstance, field: 'lastName')}"/>
        </td>
    </tr>
   <tr class="prop">
        <td valign="top" class="name">
            <label for="email"><g:message code="contact.email" default="Email"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'email', 'errors')}">
            <g:textField name="email" value="${fieldValue(bean: contactInstance, field: 'email')}"/>
        </td>
    </tr>
       <tr class="prop">
        <td valign="top" class="name">
            <label for="companyName"><g:message code="contact.companyName" default="Company"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'companyName', 'errors')}">
            <g:textField name="companyName" value="${fieldValue(bean: contactInstance, field: 'companyName')}"/>
        </td>
    </tr>
        <tr class="prop">
        <td valign="top" class="name">
            <label for="title"><g:message code="contact.title" default="Title"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'title', 'errors')}">
            <g:textField name="title" value="${fieldValue(bean: contactInstance, field: 'title')}"/>
        </td>
    </tr>
        <tr class="prop">
        <td valign="top" class="name">
            <label for="mobile"><g:message code="contact.mobile" default="Phone"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'mobile', 'errors')}">
            <g:textField name="mobile" value="${fieldValue(bean: contactInstance, field: 'mobile')}"/>
        </td>
    </tr>
    <tr class="prop">
        <td valign="top" class="name">
            <label for="birthday"><g:message code="contact.birthday" default="Birthday"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'birthday', 'errors')}">
            <g:textField name="birthday" value="${fieldValue(bean: contactInstance, field: 'birthday')}"/>
        </td>
    </tr>
     <tr class="prop">
        <td valign="top" class="name">
            <label for="city"><g:message code="contact.city" default="City"/>: </label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'city', 'errors')}">
            <g:textField name="city" value="${fieldValue(bean: contactInstance, field: 'city')}"/>
        </td>
    </tr>
    
	 <tr class="prop">
                            <td valign="top" class="name">
                                <label for="picture"><g:message code="contact.picture" default="Picture"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: contactInstance, field: 'picture', 'errors')}">
                                <input type="file" id="picture" name="picture"/>

                            </td>
                        </tr>
    
    </tbody>
</table>

</section>

</div>



<div class="action">
    <g:actionSubmit class="button button-green" action="update"  style="width: 110px"
                    value="${message(code: 'update', 'default': 'Update')}"/>
    <g:actionSubmit class="button button-red" action="delete"  style="width: 110px" value="${message(code: 'delete', 'default': 'Delete')}"
                    onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/>
    <g:actionSubmit class="button button-blue" action="list"  style="width: 110px" value="${message(code: 'list', 'default': 'List')}"/>
    <g:actionSubmit class="button button-orange" controller="linkedinConnect" action="sendDirectMessage"  style="width: 110px" value="${message(code: 'message', 'default': 'Contact')}"/>
</div>

</g:form>

</div>

</section>
</div>
</section>
</body>
</html>
