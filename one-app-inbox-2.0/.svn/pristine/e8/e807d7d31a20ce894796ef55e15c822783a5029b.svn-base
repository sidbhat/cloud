
<%@ page import="com.oneapp.cloud.core.Plan" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="plan.list" default="Plan List" /></title>
    </head>
    <body>
       <section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="plan.list" default="Plan List" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                        <tr>
                        
	                   	    <g:sortableColumn property="id" title="Id" titleKey="plan.id" />
	                        
	                   	    <g:sortableColumn property="planName" title="Plan Name" titleKey="plan.planName" />
	                        
	                   	    <g:sortableColumn property="description" title="Description" titleKey="plan.description" />
	                        
	                   	    <g:sortableColumn property="maxStorage" title="Max Storage" titleKey="plan.maxStorage" />
	                        
	                   	    <g:sortableColumn property="amount" title="Amount" titleKey="plan.amount" />
	                        
	                   	    <g:sortableColumn property="maxUsers" title="Max Users" titleKey="plan.maxUsers" />
                        
                        </tr>
                    </thead>
                    <tbody>
                    <g:each in="${planInstanceList}" status="i" var="planInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${planInstance.id}">${fieldValue(bean: planInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: planInstance, field: "planName")}</td>
                        
                            <td>${fieldValue(bean: planInstance, field: "description")}</td>
                        
                            <td>${fieldValue(bean: planInstance, field: "maxStorage")}</td>
                        
                            <td>${fieldValue(bean: planInstance, field: "amount")}</td>
                        
                            <td>${fieldValue(bean: planInstance, field: "maxUsers")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${planInstanceTotal}" />
            </div>
	        <div class="grid_6">
               <g:link class="button button-gray" action="create"><span
                           class="add"></span><g:message code="default.button.create.label" default="Create" /></g:link>
            </div>
            
        </section>
    </div>
</section>
    </body>
</html>
