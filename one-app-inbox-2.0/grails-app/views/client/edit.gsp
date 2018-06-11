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

<%@ page import="com.oneapp.cloud.core.Client" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="client.edit" default="Edit Client"/></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
    <header>
        <ul class="action-buttons clearfix fr">
            <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                    class="help"></span></a></li>
        </ul>

        <h2>
            <g:message code="client.edit" default="Edit Client"/></h2>
        <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                            default="${flash.defaultMessage}"/></div>
        </g:if>
        <g:hasErrors bean="${clientInstance}">
            <div class="errors">
                <g:renderErrors bean="${clientInstance}" as="list"/>
            </div>
        </g:hasErrors>
    </header>
<section class="container_6 clearfix">
<div class="form grid_6">
    <g:form method="post" enctype="multipart/form-data">
        <g:hiddenField name="id" value="${clientInstance?.id}"/>
        <g:hiddenField name="version" value="${clientInstance?.version}"/>
        <div class="dialog">
            <table>
                <tbody>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="name"><g:message code="client.name" default="Name"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'name', 'errors')}">
                        <g:textField name="name" value="${fieldValue(bean: clientInstance, field: 'name')}"/>

                    </td>
                </tr>

                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="description"><g:message code="client.description" default="Description"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'description', 'errors')}">
                        <g:textArea name="description" rows="5" cols="40"
                                    value="${fieldValue(bean: clientInstance, field: 'description')}"/>

                    </td>
                </tr>
                
                <%--
               
            
                <tr class="prop">
                    <td valign="top" class="name">
                        <label for="excel"><g:message code="client.excel" default="Excel Upload"/>:</label>
                    </td>
                    <td valign="top">
                        <input type="file" id="excel" name="excel"/>
                    </td>
                </tr>
                     --%><tr class="prop">
                    <td valign="top" class="name">
                        <label for="validFrom"><g:message code="client.validFrom" default="Valid From"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'validFrom', 'errors')}">
                        <input type="date" name="validFrom" id="validFrom" value="${clientInstance?.validFrom}"/>

                    </td>
                  </tr>
 				 <tr class="prop">
                    <td valign="top" class="name">
                        <label for="validTo"><g:message code="client.validFrom" default="Valid To"/>:</label>
                    </td>
                    <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'validTo', 'errors')}">
                        <input type="date" name="validTo" id="validTo" value="${clientInstance?.validTo}"/>

                    </td>
                  </tr>
                  <script type="text/javascript">
						$(document).ready(function(){
	                         		$("#validFrom").val("${clientInstance?.validFrom?.format('MM/dd/yyyy')}")
	                         		$("#validTo").val("${clientInstance?.validTo?.format('MM/dd/yyyy')}")
	                         	});
					</script>
                   <tr class="prop">
                            <td valign="top" class="name">
                                <label for="maxUsers"><g:message code="client.maxUsers" default="Max Users"/>: </label>
                            </td>
                            <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'maxUsers', 'errors')}">
                                <g:textField name="maxUsers" value="${fieldValue(bean: clientInstance, field: 'maxUsers')}"/>

                            </td>
                  </tr>
					
      	   				<tr class="prop">
 					 <td valign="top" class="name">
       			         <label for="licenseCollaboration"><g:message code="client.licenseCollaboration" default="License Collaboration"/>:</label>
       			     </td>
         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'licenseCollaboration', 'errors')}">
                		<g:checkBox name="licenseCollaboration" value="${clientInstance?.licenseCollaboration}"/>
					</td>
      	   			</tr>
      	   				<tr class="prop">
 					 <td valign="top" class="name">
       			         <label for="licenseFormBuilder"><g:message code="client.licenseFormBuilder" default="License Forms"/>:</label>
       			     </td>
         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'licenseFormBuilder', 'errors')}">
                		<g:checkBox name="licenseFormBuilder" value="${clientInstance?.licenseFormBuilder}"/>
					</td>
      	   			</tr>
      	   			<tr class="prop">
	 					 <td valign="top" class="name">
	       			         <label for="disableEmail"><g:message code="client.disableEmail" default="Disable Email"/>:</label>
	       			     </td>
	         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'disableEmail', 'errors')}">
	                		<g:checkBox name="disableEmail" value="${clientInstance?.disableEmail}"/>
						</td>
      	   			</tr>
      	   			<tr class="prop">
	 					 <td valign="top" class="name">
	       			         <label for="form"><g:message code="client.form" default="Maximum Forms Allowed"/>:</label>
	       			     </td>
	         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'form', 'errors')}">
	                		<g:textField name="form" value="${fieldValue(bean: clientInstance, field: 'form')}"/>
						</td>
      	   			</tr>
      	   			<tr class="prop">
	 					 <td valign="top" class="name">
	       			         <label for="maxAttachmentSize"><g:message code="client.maxAttachmentSize" default="Attachment Size (in MB)"/>:</label>
	       			     </td>
	         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'maxAttachmentSize', 'errors')}">
	                		<g:textField name="maxAttachmentSize" value="${fieldValue(bean: clientInstance, field: 'maxAttachmentSize')}"/>
						</td>
      	   			</tr>
      	   			<tr class="prop">
	 					 <td valign="top" class="name">
	       			         <label for="maxEmailAccount"><g:message code="client.maxEmailAccount" default="Max Email Accounts"/>:</label>
	       			     </td>
	         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'maxEmailAccount', 'errors')}">
	                		<g:textField name="maxEmailAccount" value="${fieldValue(bean: clientInstance, field: 'maxEmailAccount')}"/>
						</td>
      	   			</tr>
      	   			<tr class="prop">
	 					 <td valign="top" class="name">
	       			         <label for="maxEmailFetchCount"><g:message code="client.maxEmailFetchCount" default="Max Email Fetch Count"/>:</label>
	       			     </td>
	         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'maxEmailFetchCount', 'errors')}">
	                		<g:textField name="maxEmailFetchCount" value="${fieldValue(bean: clientInstance, field: 'maxEmailFetchCount')}"/>
						</td>
      	   			</tr>
      	   			<tr class="prop">
	 					 <td valign="top" class="name">
	       			         <label for="maxEmailFetchCount"><g:message code="client.planType" default="Plan Type"/>:</label>
	       			     </td>
	         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'plan', 'errors')}">
	                		<g:textField name="plan" readonly="readOnly" value="${clientInstance.plan?.description?:'No Plan'}"/>
						</td>
      	   			</tr>
      	   			<tr class="prop">
	 					 <td valign="top" class="name">
	       			         <label for="gcmApiKey"><g:message code="client.gcmApiKey" default="GCM Api Key"/>:</label>
	       			     </td>
	         			 <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'gcmApiKey', 'errors')}">
	                		<g:textArea rows="5" cols="10"   name="gcmApiKey" value="${fieldValue(bean: clientInstance, field: 'gcmApiKey')}" style="height: 75px;"/>
						</td>
      	   			</tr>
      	   			<tr class="prop" >
	                            <td valign="top" class="name" style="width:0.5%;">
	                                <label for="type"><g:message code="clientPlan.billingHistory" default="Billing History"/>:</label>
	                            </td>
	                            <td valign="top"
	                                class="name ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}" width="80%">
	                                <g:if test="${billingHistory.size() > 0}">
	                                	<g:each in="${billingHistory}" status="i" var="billingHistoryInstance">
		                                	<span><a href="${grailsApplication.config.grails.serverURL}/billingHistory/show/${billingHistoryInstance?.id}" style="text-decoration: underline !important;margin-left:10px;"><g:formatDate date="${billingHistoryInstance?.billDate}" /></a></span>
		                                </g:each>
	                                </g:if>
	                                <g:else>
	                                	<span><a href="javascript:;" style="margin-left:10px;">No History available</a></span>
	                                </g:else>
	                            </td>
	                            
	                        </tr>
                </tbody>
            </table>
        </div>

        <div class="action">
            <g:actionSubmit class="button button-green" action="update" style="width: 140px"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
       
       		<g:if test="${clientInstance.id != 1}">
                <g:actionSubmit class="button button-red" action="delete" style="width: 140px"
                                value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                                onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/>
            </g:if>
        </div>

        </section>
        </div>
    </g:form>
</div>
</section>
</body>
</html>

