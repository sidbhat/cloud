
<%@ page import="com.oneapp.cloud.core.BlockedIp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="blockedIp.create" default="Create BlockedIp" /></title>
    </head>
   <body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <h2><g:message code="default.create.label" args="['Blocked IP']"/></h2>
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
            <g:form action="save" method="post" >
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
                        <span class="button"><g:submitButton name="create" class="button button-green"
                                                             value="${message(code: 'default.button.create.label', default: 'Create')}"/></span>
                    </div>
            </g:form>
         </div>
        </section>
    </div>
</section>
</body>
</html>
