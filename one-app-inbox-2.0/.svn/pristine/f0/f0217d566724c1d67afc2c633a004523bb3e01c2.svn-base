
<%@ page import="com.oneapp.cloud.core.Rule" %>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="rule.list" default="Rule List" /></title>
    </head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="rule.list" default="Rule List" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                   <tr>
                        
                   	    <g:sortableColumn property="_order" title="Order" titleKey="rule._order" />
                        
                   	    <g:sortableColumn property="className" title="Class Name" titleKey="rule.className" />
                        
                   	    <g:sortableColumn property="attributeName" title="Attribute Name" titleKey="rule.attributeName" />
                        
                   	    <g:sortableColumn property="value" title="Value" titleKey="rule.value" />
                        
                   	    <g:sortableColumn property="operator" title="Operator" titleKey="rule.operator" />
                        
                   	    <g:sortableColumn property="_condition" title="_condition" titleKey="rule._condition" />
                        
   				    </tr>
                    </thead>
                    <tbody>
                   <g:each in="${ruleInstanceList}" status="i" var="ruleInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${ruleInstance.id}">${fieldValue(bean: ruleInstance, field: "_order")}</g:link></td>
                        
                            <td>${fieldValue(bean: ruleInstance, field: "className")}</td>
                        
                            <td>${fieldValue(bean: ruleInstance, field: "attributeName")}</td>
                        
                            <td>${fieldValue(bean: ruleInstance, field: "value")}</td>
                        
                            <td>${fieldValue(bean: ruleInstance, field: "operator")}</td>
                        
                            <td>${fieldValue(bean: ruleInstance, field: "_condition")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>

            <div class="paginateButtons">
               <g:paginate total="${ruleInstanceTotal}" />
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
