
<%@ page import="com.oneapp.cloud.core.ApplicationConf" %>
<%@ page import="grails.plugin.multitenant.core.util.TenantUtils" %>
<%@ page import="org.grails.formbuilder.Form" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="applicationConf.edit" default="Edit Application Config" /></title>
    </head>
    <body>
    
	<section class="main-section grid_7">
		<div class="main-content">
			<header>
				<h2><g:message code="applicationConf.edit" default="Edit Application Config" /></h2>
	            <g:if test="${flash.message}">
	            	<div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
	            </g:if>
	            <g:hasErrors bean="${applicationConfInstance}">
	            <div class="errors">
	                <g:renderErrors bean="${applicationConfInstance}" as="list" />
	            </div>
	            </g:hasErrors>
	        </header>
				<section class="container_6 clearfix">
					<div class="form grid_6">

			            <g:form method="post" >
			                <g:hiddenField name="version" value="${applicationConfInstance?.version}" />
			                <div class="tabbed-pane">
								<ul class="tabs">
								    <li><a href="#">Details</a></li>
								</ul>
								
								<!-- tab "panes" -->
								<div class="panes clearfix">
								<section>
				                    <table>
				                        <tbody>
				                        
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="sendEmailDefaultFrom"><g:message code="applicationConf.sendEmailDefaultFrom" default="Send Email Default From" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'sendEmailDefaultFrom', 'errors')}">
				                                    <g:textField name="sendEmailDefaultFrom" value="${fieldValue(bean: applicationConfInstance, field: 'sendEmailDefaultFrom')}" />
				
				                                </td>
				                            </tr>
				                        
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="asynEmailJobInterval"><g:message code="applicationConf.asynEmailJobInterval" default="Asyn Email Job Interval" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'asynEmailJobInterval', 'errors')}">
				                                    <g:textField name="asynEmailJobInterval" value="${fieldValue(bean: applicationConfInstance, field: 'asynEmailJobInterval')}" />
				
				                                </td>
				                            </tr>
				                        
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="rulesJobInterval"><g:message code="applicationConf.rulesJobInterval" default="Rules Job Interval (in ms)" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'rulesJobInterval', 'errors')}">
				                                    <g:textField name="rulesJobInterval" value="${fieldValue(bean: applicationConfInstance, field: 'rulesJobInterval')}" />
				
				                                </td>
				                            </tr>
				                        
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="logInfo"><g:message code="applicationConf.logInfo" default="Log Info" />:</label>
				                                </td>
				                                <td valign="top" class="name ${hasErrors(bean: applicationConfInstance, field: 'logInfo', 'errors')}">
				                                    <g:checkBox name="logInfo" value="${applicationConfInstance?.logInfo}" />
				
				                                </td>
				                            </tr>
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="trackChanges"><g:message code="applicationConf.trackChanges" default="Track Changes" />:</label>
				                                </td>
				                                <td valign="top" class="name ${hasErrors(bean: applicationConfInstance, field: 'trackChanges', 'errors')}">
				                                    <g:checkBox name="trackChanges" value="${applicationConfInstance?.trackChanges}" />
				
				                                </td>
				                            </tr>
				                        	<tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="form"><g:message code="applicationConf.form" default="Max Forms (Def. 75)" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'form', 'errors')}">
				                                    <g:textField name="form" value="${fieldValue(bean: applicationConfInstance, field: 'form')}" />
				
				                                </td>
				                            </tr>
				                            
				                        	<tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="formForTrial"><g:message code="applicationConf.formForTrial" default="Max Forms For Trial (Def. 2)" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'formForTrial', 'errors')}">
				                                    <g:textField name="formForTrial" value="${fieldValue(bean: applicationConfInstance, field: 'formForTrial')}" />
				
				                                </td>
				                            </tr>
				                             <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="maxFormControls"><g:message code="applicationConf.maxFormControls" default="Max Widgets (Def. 40)" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'maxFormControls', 'errors')}">
				                                    <g:textField name="maxFormControls" value="${fieldValue(bean: applicationConfInstance, field: 'maxFormControls')}" />
									            </td>
				                            </tr>
				                        
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="copyForms"><g:message code="applicationConf.copyForms" default="Forms for client" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'copyForms', 'errors')}">
				                                    <g:select name="copyForms" from="${Form.findAllByTenantIdAndFormCatNotEqual(TenantUtils.getCurrentTenant(),'S') }" optionKey="id" multiple="${true }" value="${applicationConfInstance.copyForms*.id}"/>
				
				                                </td>
				                            </tr>
				                        
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="copyFormsTrial"><g:message code="applicationConf.copyFormsTrial" default="Forms for trial user" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'copyFormsTrial', 'errors')}">
				                                    <g:select name="copyFormsTrial" from="${Form.findAllByTenantIdAndFormCatNotEqual(TenantUtils.getCurrentTenant(),'S') }" optionKey="id" multiple="${true }" value="${applicationConfInstance.copyFormsTrial*.id}"/>
				
				                                </td>
				                            </tr><%--
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="loadedForms"><g:message code="applicationConf.form" default="Loaded Forms" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'form', 'errors')}">
				                                	<g:textField name="loadedForms" disabled="true" value="${formsLoaded}" />
				                                </td>
				                            </tr>
				                            --%><tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="activeSession"><g:message code="applicationConf.form" default="Active Sessions" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'form', 'errors')}">
				                                	<g:textField name="activeSession" disabled="true" value="${activeSession}" />
				                                </td>
				                            </tr>
				                            <tr class="prop">
				                                <td valign="top" class="name">
				                                    <label for="permGen"><g:message code="applicationConf.form" default="Perm Gen Memory" />:</label>
				                                </td>
				                                <td valign="top" class="value ${hasErrors(bean: applicationConfInstance, field: 'form', 'errors')}">
				                                	<g:textField name="permGen" disabled="true" value="${prettysize.display(size:permGenMem[4]?.getUsage()?.used, abbr:'true', format:'#.##')}" />
				                                </td>
				                            </tr>
				                        </tbody>
				                    </table>
									<div class="action">
										<g:actionSubmit class="button button-green" action="update" style="width: 120px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
									</div>
								</section>
								</div>
			                </div>
			            </g:form>
					</div>
				</section>
        </div>
	</section>
    </body>
</html>
