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
<%@ page import="com.oneapp.cloud.core.cloud.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="user.edit" default="Edit User"/></title>
    <script type="text/javascript">
        function facebookLogin() {
            FB.getLoginStatus(function(response) {
                if (response.session) {
                    // logged in and connected user, someone you know
                    window.location = "${createLink(controller:'FB', action:'profile')}";
                }
            });
        }
    </script>
</head>

<body>
<section class="main-section grid_7">

<div class="main-content">
<header>
    <h3><g:message code="user.edit" default="Edit User"/>
    </h3>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${userInstance}">
        <div class="errors">
            <g:renderErrors bean="${userInstance}" as="list"/>
        </div>
    </g:hasErrors>
</header>
<section class="container_6 clearfix">
<div class="form grid_6">
<g:form method="post" enctype="multipart/form-data">
<g:hiddenField name="id" value="${userInstance?.id}"/>
<g:hiddenField name="version" value="${userInstance?.version}"/>


<div class="tabbed-pane">
            <ul class="tabs">
                <li><a href="#">Basic </a></li>
<%--                <li><a href="#">Communication </a></li>--%>
                 <li><a href="#">Admin </a></li>
            </ul>

            <!-- tab "panes" -->
            <div class="panes clearfix">
                <section><table>
<tbody>
<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
    <tr class="prop">
        <td valign="top" class="name"><label for="userTenantId"><g:message code="user.client"
                                                                           default="Client"/>:</label></td>

        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'userTenantId', 'errors')}">
            <g:select id="userTenantId" name="userTenantId" value="${userInstance.userTenantId}"
                      from="${Client.list()}"
                      optionKey="id"
                      optionValue="name" noSelection="['':'-Select client-']"/>
        </td>
    </tr>
</sec:ifAnyGranted><%--
 <tr class="prop">
                            <td valign="top" class="name">
                                <label for="picture"><g:message code="user.picture" default="Picture"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'picture', 'errors')}">
                                <input type="file" id="picture" name="picture"/>
                                <% if ( userInstance?.picture != null && userInstance.picture.size() != 0) {%>
   							  	<img class="image" height="30" width="30"
                             	src="${createLink(controller: 'user', action: 'image', id: userInstance.id)}"/>
                  				 <%}%>
                            </td>
                        </tr>
--%><tr class="prop">
                            <td valign="top" class="name">
                                <label for="pictureURL"><g:message code="user.pictureURL" default="Picture URL"/>:</label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'pictureURL', 'errors')}">
                               <g:textField name="pictureURL" value="${fieldValue(bean: userInstance, field: 'pictureURL')}"/>
   							 <% if ( userInstance.pictureURL != null && userInstance.pictureURL.length() != 0  ) {%>
   							 <img class="image" height="30" width="30"
                             	src="${userInstance?.pictureURL}"/>
                             <%}%>
                   
                            </td>
</tr>
<tr class="prop">
    <td valign="top" class="name">
        <label for="username"><g:message code="user.username" default="Username"/>:</label>
    </td>
    <sec:ifAnyGranted roles="ROLE_ADMIN">
	    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
	        <g:textField name="username" value="${fieldValue(bean: userInstance, field: 'username')}"/>
	
	    </td>
    </sec:ifAnyGranted>
   	<sec:ifNotGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
   		 <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
	        <g:textField name="username" readonly="true" value="${fieldValue(bean: userInstance, field: 'username')}"/>
	
	    </td>
   	</sec:ifNotGranted>
</tr>


<tr class="prop">
    <td valign="top" class="name">
        <label for="firstName"><g:message code="user.firstName" default="First Name"/>: <em>*</em></label>
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
            <label for="mobilePhone"><g:message code="user.mobilePhone" default="Phone"/>:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'mobilePhone', 'errors')}">
            <g:textField name="mobilePhone" value="${fieldValue(bean: userInstance, field: 'mobilePhone')}"/>

        </td>
    </tr>
<sec:ifNotGranted roles="ROLE_SUPER_ADMIN">
<%--   <tr class="prop">--%>
<%--        <td valign="top" class="name">--%>
<%--            <label for="subOrg"><g:message code="user.subOrg" default="Sub Org."/>:</label>--%>
<%--        </td>--%>
<%--        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'subOrg', 'errors')}">--%>
<%--            <g:select name="subOrg.id"--%>
<%--                      from="${com.oneapp.cloud.core.DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.SUB_ORG)}"--%>
<%--                      optionKey="id" value="${userInstance?.subOrg?.id}" noSelection="['': '']"/>--%>
<%----%>
<%--        </td>--%>
<%--    </tr>--%>
    <tr class="prop">
        <td valign="top" class="name">
            <label for="department"><g:message code="user.department" default="Department"/>:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'department', 'errors')}">
            <g:select name="department.id"
                      from="${com.oneapp.cloud.core.DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.DEPARTMENT)}"
                      optionKey="id" value="${userInstance?.department?.id}" noSelection="['': '']"/>

        </td>
    </tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="title"><g:message code="user.title" default="Title"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'title', 'errors')}">
        <g:textField name="title" value="${fieldValue(bean: userInstance, field: 'title')}"/>

    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="type"><g:message code="user.type" default="Type"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'type', 'errors')}">
        <g:select name="type.id"
                  from="${com.oneapp.cloud.core.DropDown.findAllByType(com.oneapp.cloud.core.DropDownTypes.EMPLOYEE_TYPE)}"
                  optionKey="id" value="${userInstance?.type?.id}" noSelection="['': '']"/>
    </td>
</tr>

<tr class="prop">
    <td valign="top" class="name">
        <label for="reportsTo"><g:message code="user.reportsTo" default="Manager"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'reportsTo', 'errors')}">
        <g:select name="reportsTo.id"
                  from="${reportsToList}"
                  optionKey="id"
                  value="${userInstance?.reportsTo?.id}" noSelection="['': '']"/>

    </td>
</tr>
<%--<tr class="prop">--%>
<%--    <td valign="top" class="name">--%>
<%--        <label for="hourlyRate"><g:message code="user.hourlyRate" default="Hourly Rate"/>:</label>--%>
<%--    </td>--%>
<%--    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'hourlyRate', 'errors')}">--%>
<%--        <g:textField name="hourlyRate" value="${fieldValue(bean: userInstance, field: 'hourlyRate')}"/>--%>
<%----%>
<%--    </td>--%>
<%--</tr>--%>
<%--<tr class="prop">--%>
<%--    <td valign="top" class="name">--%>
<%--        <label for="currency"><g:message code="user.hourlyRateCurr" default="Rate Currency"/>:</label>--%>
<%--    </td>--%>
<%--    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'hourlyRateCurr', 'errors')}">--%>
<%--        <g:select name="hourlyRateCurr" from="${['USD']}" value="${userInstance?.hourlyRateCurr}"/>--%>
<%--    </td>--%>
<%--</tr>--%>
<tr class="prop">
    <td valign="top" class="name">
        <label for="dob"><g:message code="user.dob" default="Birthday (mm/dd/yyyy)"/>:</label>
    </td>
    <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'dob', 'errors')}">
        <input type="date" name="dob" value="${userInstance?.dob?.format('MM/dd/yyyy')}"/>
    </td>
</tr>
</sec:ifNotGranted>
</tbody>
</table>
</section>
<%--<section>--%>
<%--    <table>--%>
<%--        <tbody>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="officePhone"><g:message code="user.officePhone" default="Office Phone"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'officePhone', 'errors')}">--%>
<%--                <g:textField name="officePhone" value="${fieldValue(bean: userInstance, field: 'officePhone')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="mobilePhone"><g:message code="user.mobilePhone" default="Mobile Phone"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'mobilePhone', 'errors')}">--%>
<%--                <g:textField name="mobilePhone" value="${fieldValue(bean: userInstance, field: 'mobilePhone')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="faxNumber"><g:message code="user.faxNumber" default="Fax Number"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'faxNumber', 'errors')}">--%>
<%--                <g:textField name="faxNumber" value="${fieldValue(bean: userInstance, field: 'faxNumber')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%----%>
<%----%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="officeAddress.address1"><g:message code="address.address1" default="Address1"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'officeAddress.address1', 'errors')}">--%>
<%--                <g:textField name="officeAddress.address1" maxlength="125"--%>
<%--                             value="${fieldValue(bean: userInstance, field: 'officeAddress.address1')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="address2"><g:message code="address.address2" default="Address2"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'officeAddress.address2', 'errors')}">--%>
<%--                <g:textField name="officeAddress.address2" maxlength="125"--%>
<%--                             value="${fieldValue(bean: userInstance, field: 'officeAddress.address2')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="city"><g:message code="address.city" default="City"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'officeAddress.city', 'errors')}">--%>
<%--                <g:textField name="officeAddress.city" maxlength="150"--%>
<%--                             value="${fieldValue(bean: userInstance, field: 'officeAddress.city')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="state"><g:message code="address.state" default="State"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'officeAddress.state', 'errors')}">--%>
<%--                <g:textField name="officeAddress.state" maxlength="50"--%>
<%--                             value="${fieldValue(bean: userInstance, field: 'officeAddress.state')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="country"><g:message code="address.country" default="Country"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'officeAddress.country', 'errors')}">--%>
<%--                <g:textField name="officeAddress.country" maxlength="50"--%>
<%--                             value="${fieldValue(bean: userInstance, field: 'officeAddress.country')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        <tr class="prop">--%>
<%--            <td valign="top" class="name">--%>
<%--                <label for="zipCode"><g:message code="address.zipCode" default="Zip Code"/>:</label>--%>
<%--            </td>--%>
<%--            <td valign="top"--%>
<%--                class="value ${hasErrors(bean: userInstance, field: 'officeAddress.zipCode', 'officeAddress.errors')}">--%>
<%--                <g:textField name="officeAddress.zipCode"--%>
<%--                             value="${fieldValue(bean: userInstance, field: 'officeAddress.zipCode')}"/>--%>
<%----%>
<%--            </td>--%>
<%--        </tr>--%>
<%----%>
<%--        </tbody>--%>
<%--    </table></section>--%>

<section>
    <table>
        <tbody>
        <tr class="prop">
            <td valign="top" class="name">
                <label for="accountExpired"><g:message code="user.accountExpired" default="Account Expired"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'accountExpired', 'errors')}">
                <g:checkBox name="accountExpired" value="${userInstance?.accountExpired}"/>

            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">
                <label for="accountLocked"><g:message code="user.accountLocked" default="Account Locked"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'accountLocked', 'errors')}">
                <g:checkBox name="accountLocked" value="${userInstance?.accountLocked}"/>

            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">
                <label for="enabled"><g:message code="user.enabled" default="Enabled"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'enabled', 'errors')}">
                <g:checkBox name="enabled" value="${userInstance?.enabled}"/>

            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">
                <label for="passwordExpired"><g:message code="user.passwordExpired"
                                                        default="Password Expired"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'passwordExpired', 'errors')}">
                <g:checkBox name="passwordExpired" value="${userInstance?.passwordExpired}"/>

            </td>
        </tr>


        <!--- Hide the super admin role from client admins --->
        <sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
            <tr class="prop">
                <td valign="top" class="name">
                    <label for="authority"><g:message code="user.role" default="Role"/>:</label>
                </td>
                <td valign="top">
                    <g:select id="authority" name="authority"
                              from="${Role.list()}"
                              optionKey="id"
                              optionValue="description"
                              value="${UserRole.findByUser(userInstance).role.id}" noSelection="['':'-Select role-']"/>
                </td>
            </tr>
        </sec:ifAnyGranted>
        <sec:ifNotGranted roles="ROLE_SUPER_ADMIN">

            <tr class="prop">
                <td valign="top" class="name">
                    <label for="authority"><g:message code="user.role" default="Role"/>:</label></td>
                <td valign="top">
                    <g:select id="authority" name="authority"
                              from="${Role.findAllByAuthorityInList([Role.ROLE_ADMIN,Role.ROLE_HR_MANAGER,Role.ROLE_USER])}"
                              optionKey="id"
                              optionValue="description"
                              value="${UserRole.findByUser(userInstance).role.id}" noSelection="['':'-Select role-']"/>
                </td>
            </tr>
        </sec:ifNotGranted>

        </tbody>
    </table>
</section>
</div>

<sec:ifAnyGranted roles="ROLE_HR_MANAGER">
<div class="action">
    <g:actionSubmit class="button button-green" action="update"  style="width: 140px"
                    value="${message(code: 'update', 'default': 'Update')}" onclick="return validateForm()"/>
    <g:actionSubmit class="button button-red" action="delete"  style="width: 140px" value="${message(code: 'user.delete', 'default': 'Deactivate Account')}"
                    onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/>
    <input type="button" class="button button-blue" style="width: 140px" value="${message(code: 'email', 'default': 'Email Password')}"
                    onclick="javascript:submitEmailPassword();"/>
</div>
</sec:ifAnyGranted>
</g:form>

</div>
<script>
		function validateForm(){
			if($("#authority").val() == ""){
				alert("Select a role");
				return false;
			}else
				return true;
			
			}

		function submitEmailPassword(){
			window.location = "${request.getContextPath()}/register/email/${userInstance?.id}"
			}
</script>
</section>
</div>
</section>
</body>
</html>
