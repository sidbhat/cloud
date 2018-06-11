
<%@ page import="com.oneapp.cloud.core.BlockedIp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="blockedIp.edit" default="Edit Blocked IP" /></title>
    </head>
   <body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <h2><g:message code="default.edit.label" args="['Blocked IP']"/></h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
            <g:hasErrors bean="${blockedIpInstance}">
                <div class="errors">
                    <g:renderErrors bean="${blockedIpInstance}" as="list"/>
                </div>
            </g:hasErrors>
        </header>
        <section class="container_6 clearfix">
            <div class="form grid_6">
            <g:form  method="post" >
            <g:hiddenField name="id" value="${blockedIpInstance?.id}"/>
            <g:hiddenField name="version" value="${blockedIpInstance?.version}"/>
                <div class="dialog">
                    <table>
                        <tbody>
                         <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="ipAdress"><g:message code="blockedIp.ipAdress" default="IP Adress" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: blockedIpInstance, field: 'ipAdress', 'errors')}">
                                    <input type="text"  required="required" name="ipAdress" value="${fieldValue(bean: blockedIpInstance, field: 'ipAdress')}" />

                                </td>
                            </tr>
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="username"><g:message code="blockedIp.username" default="Username" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: blockedIpInstance, field: 'username', 'errors')}">
                                    <g:textField name="username" value="${fieldValue(bean: blockedIpInstance, field: 'username')}" />

                                </td>
                            </tr>
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="formId"><g:message code="blockedIp.formId" default="Form Id" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: blockedIpInstance, field: 'formId', 'errors')}">
                                    <input type="number" id="formId" name="formId" value="${fieldValue(bean: blockedIpInstance, field: 'formId')}" />

                                </td>
                            </tr>
                        
                        
                            <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="reason"><g:message code="blockedIp.reason" default="Reason" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: blockedIpInstance, field: 'reason', 'errors')}">
                                    <g:textArea name="reason" value="${fieldValue(bean: blockedIpInstance, field: 'reason')}" />
                                </td>
                            </tr>
                        </tbody>
                    </table>
                </div>
                <div class="action">
	
            <g:actionSubmit class="button button-green" action="update" style="width: 120px"
                            value="${message(code: 'default.button.update.label', default: 'Update')}"/>
            <g:actionSubmit class="button button-red" action="delete" style="width: 120px"
                            value="${message(code: 'default.button.delete.label', default: 'Delete')}"
                            onclick="return confirm('${message(code: 'delete.confirm', 'default': 'Are you sure?')}');"/>
       		 <input type="button" class="button button-blue" style="width: 140px" value="List"
                    onclick="javascript:openBLIST();"/>

    </div>
            </g:form>
         </div>
        </section>
    </div>
</section>
<script>
	function openBLIST(){
			window.location = "${request.getContextPath()}/blockedIp/list"
		}
</script>
</body>
</html>
