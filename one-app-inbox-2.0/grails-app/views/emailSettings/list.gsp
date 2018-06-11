
<%@ page import="com.oneapp.cloud.core.EmailSettings" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'emailSettings.label', default: 'Email Settings')}" />
        <title><g:message code="default.list.label" args="[entityName]" /></title>
    </head>
    <body>
        <section class="main-section grid_7">
		    <div class="main-content">
		        <header>
		            
		            <h2>
		                <g:message code="default.list.label" args="[entityName]" /></h2>
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
		
		                       		 <g:sortableColumn property="id" title="${message(code: 'emailSettings.id.label', default: 'Id')}" />
		                        
		                            <g:sortableColumn property="username" title="${message(code: 'emailSettings.username.label', default: 'Username')}" />
		                    	</tr>
		                    </thead>
		                    <tbody>
		                   <g:each in="${emailSettingsInstanceList}" status="i" var="emailSettingsInstance">
		                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
		                        
		                            <td><g:link action="edit" id="${emailSettingsInstance.id}">${fieldValue(bean: emailSettingsInstance, field: "id")}</g:link></td>
		                        
		                        
		                            <td style="text-align:center;"><g:link action="edit"  id="${emailSettingsInstance.id}">${fieldValue(bean: emailSettingsInstance, field: "username")}</g:link></td>
		                        
		                        
		                        
		                        </tr>
		                    </g:each>
		                    </tbody>
		                </table>
		
		                <div class="paginateButtons">
		                	<g:paginate total="${emailSettingsInstanceTotal}" />
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
