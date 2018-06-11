
<%@ page import="com.oneapp.cloud.core.ApplicationConf" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="applicationConf.show" default="Show Application Config" /></title>
    </head>
    <body>
        <div class="nav">
            <span class="menuButton"><a class="home" href="${createLinkTo(dir: '')}"><g:message code="home" default="Home" /></a></span>
        </div>
        <div class="body">
            <h1><g:message code="applicationConf.show" default="Show ApplicationConf" /></h1>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:form>
                <div class="dialog">
                    <table>
                        <tbody>
                        
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="applicationConf.id" default="Id" />:</td>
                                
                                <td valign="top" class="value">${fieldValue(bean: applicationConfInstance, field: "id")}</td>
                                
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="applicationConf.sendEmailDefaultFrom" default="Send Email Default From" />:</td>
                                
                                <td valign="top" class="value">${fieldValue(bean: applicationConfInstance, field: "sendEmailDefaultFrom")}</td>
                                
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="applicationConf.asynEmailJobInterval" default="Asyn Email Job Interval" />:</td>
                                
                                <td valign="top" class="value">${fieldValue(bean: applicationConfInstance, field: "asynEmailJobInterval")}</td>
                                
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="applicationConf.rulesJobInterval" default="Rules Job Interval" />:</td>
                                
                                <td valign="top" class="value">${fieldValue(bean: applicationConfInstance, field: "rulesJobInterval")}</td>
                                
                            </tr>
                            
                            <tr class="prop">
                                <td valign="top" class="name"><g:message code="applicationConf.logInfo" default="Log Info" />:</td>
                                
                                <td valign="top" class="value"><g:formatBoolean boolean="${applicationConfInstance?.logInfo}" /></td>
                                
                            </tr>
                            
                        </tbody>
                    </table>
                </div>
                <div class="buttons">
                    <span class="button"><g:actionSubmit class="edit" action="edit" value="${message(code: 'edit', 'default': 'Edit')}" /></span>
                </div>
            </g:form>
        </div>
    </body>
</html>
