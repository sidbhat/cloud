

<%@ page import="com.oneapp.cloud.core.EmailSettings" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'emailSettings.label', default: 'Email Settings')}" />
        <title><g:message code="default.edit.label" args="[entityName]" /></title>
        <script>
        function populateField(){
        	var emailAccount = $("#emailAddress").val();
        	if(emailAccount == "Gmail"){
            	$("#popServerURL").val('imap.gmail.com')
            	$("#secureCheck").show()
            }else if(emailAccount == "Yahoo"){
            	$("#popServerURL").val('imap.mail.yahoo.com')
            	$("#secureCheck").show()
            }else{
            	$("#popServerURL").val('')
            	$("#secureCheck").hide()
            }
        }
        </script>
    </head>
    <body>
        <section class="main-section grid_7">
		    <div class="main-content">
		        <header>
		            <h2><g:message code="default.edit.label" args="[entityName]" /></h2>
		            <g:if test="${flash.message}">
		                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
		                                                default="${flash.defaultMessage}"/></div>
		            </g:if>
		            <g:hasErrors bean="${emailSettingsInstance}">
		                <div class="errors">
		                    <g:renderErrors bean="${emailSettingsInstance}" as="list"/>
		                </div>
		            </g:hasErrors>
		        </header>
		        <section class="container_6 clearfix">
		            <div class="form grid_6">
		                 <g:form method="post" >
		                	<g:hiddenField name="id" value="${emailSettingsInstance?.id}" />
               				<g:hiddenField name="version" value="${emailSettingsInstance?.version}" />
		                    <table>
		                        <tbody>
		                        
		                            <tr class="prop">
		                                <td valign="top" class="name">
		                                    <label for="emailAddress"><g:message code="emailSettings.emailAddress.label" default="Email Account" /></label>
		                                </td>
		                                <td valign="top" class="value ${hasErrors(bean: emailSettingsInstance, field: 'emailAddress', 'errors')}">
		                                    <g:select name="emailAddress" id="emailAddress" from="['Gmail', 'Yahoo', 'Other']" value="${emailSettingsInstance?.emailAddress}" noSelection="['': '--Select Account--']" onchange="javascript:populateField()"/>
		                                </td>
		                            </tr>
		                        
		                            <tr class="prop">
		                                <td valign="top" class="name">
		                                  <label for="username"><g:message code="emailSettings.username.label" default="Username" /></label>
		                                </td>
		                                <td valign="top" class="value ${hasErrors(bean: emailSettingsInstance, field: 'username', 'errors')}">
		                                    <g:textField name="username" value="${emailSettingsInstance?.username}" />
		                                </td>
		                            </tr>
		                        
		              
		                        
		                            <tr class="prop">
		                                <td valign="top" class="name">
		                                  <label for="password"><g:message code="emailSettings.password.label" default="Password" /></label>
		                                </td>
		                                <td valign="top" class="value ${hasErrors(bean: emailSettingsInstance, field: 'password', 'errors')}">
		                                    <g:passwordField name="password" value="${emailSettingsInstance?.password}" />
		                                </td>
		                            </tr>
		                        	<tr class="prop">
		                                <td valign="top" class="name">
		                                    <label for="host"><g:message code="emailSettings.popServerURL.label" default="Host" /></label>
		                                </td>
		                                <td valign="top" class="value ${hasErrors(bean: emailSettingsInstance, field: 'popServerURL', 'errors')}">
		                                    <g:textField name="popServerURL" id="popServerURL" value="${emailSettingsInstance?.popServerURL}" placeholder="Enter account's host" />
		                                </td>
		                            </tr>
		                             <tr class="prop" id="secureCheck">
		                                <td valign="top" class="name">
		                                    <label for="secureConnection"><g:message code="emailSettings.popServerURL.label" default="Connect via https" /></label>
		                                </td>
		                                <td valign="top" class="value ${hasErrors(bean: emailSettingsInstance, field: 'popServerURL', 'errors')}">
		                                    <g:checkBox checked="${emailSettingsInstance?.secureConnection}" name="secureConnection" id="secureConnection" value="${emailSettingsInstance?.secureConnection}" placeholder="Enter account's host" />
		                                </td>
		                            </tr>
		                        </tbody>
		                    </table>
		
		                    <div class="action" style="margin-left:100px;">
			                    <span class="button" style="padding:4px 0px;"><g:actionSubmit class="button button-green" style="width: 140px" action="update" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="showSpinner()"/></span>
			                    <span class="button" style="padding:4px 0px;"><g:actionSubmit class="button button-red" style="width: 140px" action="delete" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" /></span>
			                    <span class="button" style="padding:4px 0px;"><input type="button" class="button button-gray" style="width: 140px"  value="${message(code: 'report.forms', default: 'Email Analyzer')}" onclick="navigateToEmailAnalyzer()" /></span>
			                </div>
		                </g:form>
		            </div>
		        </section>
		    </div>
		</section>
		<script>
			$("#emailAddress").val('${emailSettingsInstance?.emailAddress}')
			
			function navigateToEmailAnalyzer(){
					window.location = "${request.getContextPath()}/report/search?id=${emailSettingsInstance?.id}&daysPrior=7"
				}
		</script>
    </body>
   
</html>
