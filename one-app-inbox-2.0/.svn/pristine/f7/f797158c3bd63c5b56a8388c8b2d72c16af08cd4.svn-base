
<%@ page import="com.oneapp.cloud.core.Plan" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="plan.edit" default="Edit Plan" /></title>
    </head>
    <body>
        <section class="main-section grid_7">
			<div class="main-content">
				<header>
				    <h2><g:message code="plan.create" default="Create Plan" /></h2>
			   			<g:if test="${flash.message}">
			            	<div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
			            </g:if>
			            <g:hasErrors bean="${ruleInstance}">
				            <div class="errors">
				                <g:renderErrors bean="${ruleInstance}" as="list" />
				            </div>
			            </g:hasErrors>
				</header>
		        <section class="container_6 clearfix">
					<div class="form grid_6">
		            <g:form method="post" >
		               <section>
			                <g:hiddenField name="id" value="${planInstance?.id}" />
			                <g:hiddenField name="version" value="${planInstance?.version}" />
			                <div class="dialog">
			                    <table>
			                        <tbody>
			                        
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="planName"><g:message code="plan.planName" default="Plan Name" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: planInstance, field: 'planName', 'errors')}">
			                                    <g:textField name="planName" value="${fieldValue(bean: planInstance, field: 'planName')}" />
			
			                                </td>
			                            </tr>
			                        
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="description"><g:message code="plan.description" default="Description" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: planInstance, field: 'description', 'errors')}">
			                                    <g:textField name="description" value="${fieldValue(bean: planInstance, field: 'description')}" />
			
			                                </td>
			                            </tr>
			                        
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="maxStorage"><g:message code="plan.maxStorage" default="Max Storage" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: planInstance, field: 'maxStorage', 'errors')}">
			                                    <g:textField name="maxStorage" value="${fieldValue(bean: planInstance, field: 'maxStorage')}" />
			
			                                </td>
			                            </tr>
			                        
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="amount"><g:message code="plan.amount" default="Amount" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: planInstance, field: 'amount', 'errors')}">
			                                    <g:textField name="amount" value="${fieldValue(bean: planInstance, field: 'amount')}" />
			
			                                </td>
			                            </tr>
			                        
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="maxUsers"><g:message code="plan.maxUsers" default="Max Users" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: planInstance, field: 'maxUsers', 'errors')}">
			                                    <g:textField name="maxUsers" value="${fieldValue(bean: planInstance, field: 'maxUsers')}" />
			
			                                </td>
			                            </tr>
			                        
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="form"><g:message code="plan.form" default="Form" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: planInstance, field: 'form', 'errors')}">
			                                    <g:textField name="form" value="${fieldValue(bean: planInstance, field: 'form')}" />
			
			                                </td>
			                            </tr>
			                        
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="maxEmailAccount"><g:message code="plan.maxEmailAccount" default="Max Email Account" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: planInstance, field: 'maxEmailAccount', 'errors')}">
			                                    <g:textField name="maxEmailAccount" value="${fieldValue(bean: planInstance, field: 'maxEmailAccount')}" />
			
			                                </td>
			                            </tr>
			                        
			                        </tbody>
			                    </table>
			                </div>
			                <div class="action">
						       <g:actionSubmit class="button button-green" action="update" style="width: 120px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
						       <g:actionSubmit class="button button-red" action="delete" style="width: 120px" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
						    </div>
					    </section>
		            </g:form> 
		          </div>
		      </section>
			</div>
		</section>
    </body>
</html>
