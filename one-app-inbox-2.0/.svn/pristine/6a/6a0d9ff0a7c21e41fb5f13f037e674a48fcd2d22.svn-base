
<%@ page import="com.oneapp.cloud.core.BlockedIp" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="blockedIp.list" default="Blocked IP List" /></title>
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
                <g:message code="blocked.list" default="Blocked IP List"/>  &nbsp;(${blockedIpInstanceTotal})</h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                    <tr>
                         
                   	    <g:sortableColumn property="ipAdress" title="Ip Adress" titleKey="blockedIp.ipAdress" />
                   	    
                   	    <g:sortableColumn property="username" title="Username" titleKey="blockedIp.username" />
                        
                   	    <g:sortableColumn property="formId" title="Form Id" titleKey="blockedIp.formId" />
                        
                   	    <g:sortableColumn property="reason" title="Reason" titleKey="blockedIp.reason" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${blockedIpInstanceList}" status="i" var="blockedIpInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${blockedIpInstance.id}">${fieldValue(bean: blockedIpInstance, field: "ipAdress")}</g:link></td>
                        
                            <td>${fieldValue(bean: blockedIpInstance, field: "username")}</td>
                        
                            <td>${fieldValue(bean: blockedIpInstance, field: "formId")}</td>
                        
                            <td>${fieldValue(bean: blockedIpInstance, field: "reason")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
               <div class="paginateButtons">
                    <g:paginate total="${blockedIpInstanceTotal}"/>
                </div>

                <div class="grid_6">
						<g:link action="create" class="button button-gray"><span
                            class="add"></span>${message(code: 'create', 'default': 'Create')}</g:link>
                </div>

            </div>
        </section>
    </div>
</section>

</body>
</html>