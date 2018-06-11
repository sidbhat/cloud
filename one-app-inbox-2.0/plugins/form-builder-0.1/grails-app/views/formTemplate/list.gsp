
<%@ page import="org.grails.formbuilder.FormTemplate" %>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="formTemplate.list" default="FormTemplate List" /></title>
    </head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="formTemplate.list" default="FormTemplate List" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                   <tr>
                        <g:sortableColumn property="category" title="Category" titleKey="formTemplate.category" />
                   	    <g:sortableColumn property="name" title="Name" titleKey="formTemplate.name" />
                        
                   	    <th><g:message code="formTemplate.form" default="Form" /></th>
                   	    
                   	    <g:sortableColumn property="global" title="Global" titleKey="formTemplate.global" />
     
     					<th> New Form </th>      
   				    </tr>
                    </thead>
                    <tbody>
                   <g:each in="${formTemplateInstanceList}" status="i" var="formTemplateInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                        	<td>${fieldValue(bean: formTemplateInstance, field: "category")}</td>
                            <td>
                            	<g:if test="${org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils.ifAnyGranted('ROLE_SUPER_ADMIN')}">
	                            	<g:link action="edit" id="${formTemplateInstance.id}">${fieldValue(bean: formTemplateInstance, field: "name")}</g:link>
	                            </g:if>
	                            <g:else>
	                            	${fieldValue(bean: formTemplateInstance, field: "name")}
	                            </g:else>
                        	</td>
                            <td>${fieldValue(bean: formTemplateInstance, field: "form")}</td>
                        
                            <td><g:formatBoolean boolean="${formTemplateInstance.global}" /></td>
                        
                        	<td><g:link class="copy" action="copy" id="${formTemplateInstance.id}"> Create Form </g:link></td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                 <g:paginate total="${formTemplateInstanceTotal}" />
            </div>
	        <div class="grid_6">
                 <g:link class="button button-gray" action="create"><span
                           class="add"></span><g:message code="default.button.create.label" default="Create" /></g:link>
                 <g:link class="button button-gray" controller="form" action="list"><span
                           class="add"></span><g:message code="default.button.list.label" default="Form List" /></g:link>
           	</div>
        </section>
    </div>
</section>

</body>
</html>
