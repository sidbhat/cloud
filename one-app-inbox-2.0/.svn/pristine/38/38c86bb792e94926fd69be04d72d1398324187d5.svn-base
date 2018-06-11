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
<%@ page import="com.oneapp.cloud.time.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="userProfile.edit" default="Edit UserProfile"/></title>
   
</head>
<%
	def com.oneapp.cloud.core.Client client = com.oneapp.cloud.core.Client.get(session?.user?.userTenantId)
%>
<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
<script type="text/javascript">
    $(document).ready(function(){
    	$('.attachmentImg').load(setImagesWidth);
    });
    var setImagesWidth = function(){
    	$('.attachmentImg').each(function (){
    		var w = this.clientWidth/88;
    		var h = this.clientHeight/100;
    		if(w >= h){
    			this.style.height=100+"px";
    			this.style.width="auto";
    			this.style.marginLeft = -(this.clientWidth - 88)/2 +"px";
    			this.style.marginTop = "0";
    		}else{
    			this.style.width=88+"px";
    			this.style.height="auto";
    			this.style.marginTop = -(this.clientHeight - 100)/2 +"px";
    			this.style.marginLeft = "0";
    		}
    	});  
    }
    </script>
    <ul class="action-buttons clearfix fr">
        <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                class="help"></span></a></li>
    </ul>

    <h2>
        <g:message code="userProfile.edit" default="Edit UserProfile"/></h2>
    <g:if test="${flash.message}">
        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                        default="${flash.defaultMessage}"/></div>
    </g:if>
    <g:hasErrors bean="${userProfileInstance}">
        <div class="errors">
            <g:renderErrors bean="${userProfileInstance}" as="list"/>
        </div>
    </g:hasErrors>
</header>
<section class="container_6 clearfix">
<div class="form grid_6">
<g:uploadForm method="post">
<g:hiddenField name="id" value="${userProfileInstance?.id}"/>
<g:hiddenField name="version" value="${userProfileInstance?.version}"/>
<div class="tabbed-pane">
<ul class="tabs">
    <li><a href="#">Basic</a></li>
<%--    <li><a href="#">Communication</a></li>--%>
   
</ul>

<!-- tab "panes" -->
<div class="panes clearfix">

<section>

    <table>
        <tbody>
         <ul class="action-buttons clearfix fr">
                               
        <tr class="prop">
            <td valign="top" class="name">
                <label for="user.username"><g:message code="userProfile.user.username" default="Username"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.username', 'errors')}">
                <g:textField name="user.username" readonly="true"
                             value="${fieldValue(bean: userProfileInstance, field: 'user.username')}"/>
            </td>
        </tr>
          <tr class="prop">
            <td valign="top" class="name">
                <label for="user.shortName"><g:message code="userProfile.user.shortName" default="Nick Name"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.shortName', 'errors')}">
                <g:textField name="user.shortName"
                             value="${fieldValue(bean: userProfileInstance, field: 'user.shortName')}"/>
            </td>
        </tr>
         <tr class="prop">
            <td valign="top" class="name">
                <label for="currentStatus"><g:message code="userProfile.user.currentStatus" default="Status"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.currentStatus', 'errors')}">
                <g:textField name="currentStatus" 
                             value="${fieldValue(bean: userProfileInstance, field: 'currentStatus')}"/>
            </td>
        </tr>
          <tr class="prop">
            <td valign="top" class="name">
                <label for="user.pictureURL"><g:message code="userProfile.user.pictureURL" default="Picture URL"/>:</label>
            </td>
         
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.pictureURL', 'errors')}">
                   <% if ( userProfileInstance.user.pictureURL != null && userProfileInstance.user.pictureURL.length() != 0 ) {%>
           				<img src="${userProfileInstance.user.pictureURL}" width="30" height="30"/>
           			<%}%>
                <g:textField name="user.pictureURL"
                             value="${fieldValue(bean: userProfileInstance, field: 'user.pictureURL')}"/>
            </td>
        </tr>
        
        <tr class="prop">
            <td valign="top" class="name">
                <label for="user.firstName"><g:message code="user.firstName" default="First Name"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.firstName', 'errors')}">
                <g:textField name="user.firstName"
                             value="${fieldValue(bean: userProfileInstance, field: 'user.firstName')}"/>

            </td>
        </tr>

        <tr class="prop">
            <td valign="top" class="name">
                <label for="user.lastName"><g:message code="user.lastName" default="Last Name"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.lastName', 'errors')}">
                <g:textField name="user.lastName"
                             value="${fieldValue(bean: userProfileInstance, field: 'user.lastName')}"/>

            </td>
        </tr>
       
        <tr class="prop">
            <td valign="top" class="name">
                <label for="user.title"><g:message code="user.title" default="Title"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.title', 'errors')}">
                <g:textField name="user.title" value="${fieldValue(bean: userProfileInstance, field: 'user.title')}"/>

            </td>
        </tr>
        <tr class="prop">
            <td valign="top" class="name">
                <label for="dob"><g:message code="user.dob" default="Birthday (mm/dd/yyyy)"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.dob', 'errors')}">
                <input type="date" name="user.dob" id="dob" value="${userProfileInstance?.user?.dob?.format('MM/dd/yyyy')}" />
               	 <script>
	                                		$(document).ready(function(){
		                                		$("#dob").val("${userProfileInstance?.user?.dob?.format('MM/dd/yyyy')}")
		                                		
		                                	});
	                                	</script>
            </td>
        </tr>
            <tr class="prop">
 					 <td valign="top" class="name">
       			         <label for="emailSubscribed"><g:message code="userProfileInstance.emailSubscribed" default="Daily Email Summary?"/>:</label>
       			     </td>
         			 <td valign="top" class="name ${hasErrors(bean: userProfileInstance, field: 'emailSubscribed', 'errors')}">
                		<g:checkBox name="emailSubscribed" value="${userProfileInstance?.emailSubscribed}"/>
					</td>
      	  </tr>
        <%--
        
		 <tr class="prop">
            <td valign="top" class="name">
                <label for="user.lang"><g:message code="user.lang" default="Language (en,fr,it,de,hi)"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'defaultLanguage', 'errors')}">
                <input type="text" name="defaultLanguage" value="${userProfileInstance?.defaultLanguage}"/>
            </td>
        </tr>
        
		
		<tr class="prop">
            <td valign="top" class="name">
                <label for="user.time"><g:message code="user.time" default="Timezone"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'defaultTimezone', 'errors')}">
                <input type="text" name="defaultTimezone" value="${userProfileInstance?.defaultTimezone}"/>
            </td>
        </tr>--%>
         <tr class="prop">
            <td valign="top" class="name">
                <label for="user.background"><g:message code="user.background" default="Bg. Color (Ex. #FFEEDD)"/>:</label>
            </td>
            <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'background', 'errors')}">
                <input type="text" name="background" value="${userProfileInstance?.background}"/>
            </td>
        </tr>
     <g:set var="attachments" value="${userProfileInstance.attachments}"></g:set>
								 <g:set var="attachmentsCount" value="${0}"></g:set>
								<br>
								<g:each in="${attachments.sort{if(grailsApplication.config.attachment.image.ext.contains(it.ext?.toLowerCase())){return -1}else{return 0}}}" var="attachment">
									 <% attachmentsCount++%>
									 <g:if test="${grailsApplication.config.attachment.image.ext.contains(attachment.ext?.toLowerCase())}">
										<tr class="prop">
										 <td valign="top" class="name">
										   <label for="profilePicture"><g:message code="userProfile.profilePicture" default="Profile Picture" /></label></span>
										 </td> <td valign="top" class="value"><div style="position relative;border: 1px solid #CCCCCC;display: inline-block;margin: 2px;padding: 5px;width: 90px; left: 180px;margin-left: 12px;">
										<attachments:icon attachment="${attachment}"/>
									</g:if>
									<g:else>
								    	<br/>
										<attachments:icon attachment="${attachment}"/>
										<attachments:downloadLink attachment="${attachment}"/>
										${attachment.niceLength}
									</g:else>
										<attachments:deleteLink attachment="${attachment}" label="${'[Delete]'}" returnPageURI="${createLink(action: 'edit')}"/>
									<g:if test="${grailsApplication.config.attachment.image.ext.contains(attachment.ext?.toLowerCase())}">
										</div>
										 </td></tr>
									</g:if>
									</g:each>
								<g:if test="${attachmentsCount<1}">
									<tr>
									     <td valign="top" class="name"> <label for="profilePicture"><g:message code="userProfile.profilePicture" default="Profile Picture" /></label></td> 
	                                     <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'background', 'errors')}"><input type="file" id="profilePicture" name="profilePicture" /></td>
	                                
                            	</g:if>
        </tbody>
    </table>
</section>
<%--<section>
    <table><tbody>
    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.officePhone"><g:message code="user.officePhone" default="Office Phone"/>:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.officePhone', 'errors')}">
            <g:textField name="user.officePhone"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.officePhone')}"/>

        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.mobilePhone"><g:message code="user.mobilePhone" default="Mobile Phone"/>:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.mobilePhone', 'errors')}">
            <g:textField name="user.mobilePhone"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.mobilePhone')}"/>

        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.faxNumber"><g:message code="user.faxNumber" default="Fax Number"/>:</label>
        </td>
        <td valign="top" class="value ${hasErrors(bean: userProfileInstance, field: 'user.faxNumber', 'errors')}">
            <g:textField name="user.faxNumber"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.faxNumber')}"/>

        </td>
    </tr>




    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.officeAddress.address1"><g:message code="address.address1" default="Address1"/>:</label>
        </td>
        <td valign="top"
            class="value ${hasErrors(bean: userProfileInstance, field: 'user.officeAddress.address1', 'errors')}">
            <g:textField name="user.officeAddress.address1" maxlength="125"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.officeAddress.address1')}"/>

        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.address2"><g:message code="address.address2" default="Address2"/>:</label>
        </td>
        <td valign="top"
            class="value ${hasErrors(bean: userProfileInstance, field: 'user.officeAddress.address2', 'errors')}">
            <g:textField name="user.officeAddress.address2" maxlength="125"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.officeAddress.address2')}"/>

        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.city"><g:message code="address.city" default="City"/>:</label>
        </td>
        <td valign="top"
            class="value ${hasErrors(bean: userProfileInstance, field: 'user.officeAddress.city', 'errors')}">
            <g:textField name="user.officeAddress.city" maxlength="150"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.officeAddress.city')}"/>

        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.state"><g:message code="address.state" default="State"/>:</label>
        </td>
        <td valign="top"
            class="value ${hasErrors(bean: userProfileInstance, field: 'user.officeAddress.state', 'errors')}">
            <g:textField name="user.officeAddress.state" maxlength="50"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.officeAddress.state')}"/>

        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.country"><g:message code="address.country" default="Country"/>:</label>
        </td>
        <td valign="top"
            class="value ${hasErrors(bean: userProfileInstance, field: 'user.officeAddress.country', 'errors')}">
            <g:textField name="user.officeAddress.country" maxlength="50"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.officeAddress.country')}"/>

        </td>
    </tr>

    <tr class="prop">
        <td valign="top" class="name">
            <label for="user.zipCode"><g:message code="address.zipCode" default="Zip Code"/>:</label>
        </td>
        <td valign="top"
            class="value ${hasErrors(bean: userProfileInstance, field: 'user.officeAddress.zipCode', 'user.officeAddress.errors')}">
            <g:textField name="user.officeAddress.zipCode"
                         value="${fieldValue(bean: userProfileInstance, field: 'user.officeAddress.zipCode')}"/>

        </td>
    </tr>

    </tbody>
    </table>
</section>


    --%><div class="action">
        <g:actionSubmit class="button button-green" action="update"
                        value="${message(code: 'default.button.update.label', default: 'Update')}"/>
    </div>
</section>

</div>


  </section>
</div>
</g:uploadForm>
</div>
</section>
</body>
</html>

