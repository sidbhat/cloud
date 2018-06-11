
<%@ page import="com.oneapp.cloud.core.RuleSet" %>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="ruleSet.list" default="Rule Set List" /></title>
    </head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="ruleSet.list" default="Rule Set List" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
			<div class="clearfix" style="position:absolute;right:10px;top:20px;">
				<ul class="action-buttons clearfix fr">
	                <li>
                		<a href="${grailsApplication.config.grails.serverURL}/documentation/ruleset_help.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a>	                
	                </li>
            	</ul>
            </div>
        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                   <tr>
                        
                   	    <g:sortableColumn property="_order" title="Order" titleKey="ruleSet._order" />
                        
                   	    <g:sortableColumn property="name" title="Name" titleKey="ruleSet.name" />
                        
                   	    <g:sortableColumn property="resultClass" title="Send to" titleKey="ruleSet.resultClass" />
                        
                   	    <g:sortableColumn property="resultInstance" title="Send to instance" titleKey="ruleSet.resultInstance" />
                        
                   	    <g:sortableColumn property="_action" title="Action to perform" titleKey="ruleSet._action" />
                        
   				    </tr>
                    </thead>
                    <tbody>
                   <g:each in="${ruleSetInstanceList}" status="i" var="ruleSetInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>
                            	<g:link action="edit" id="${ruleSetInstance.id}">${fieldValue(bean: ruleSetInstance, field: "_order")}</g:link>
                            	<g:if test="${ruleSetIdsForClient?.contains(ruleSetInstance.id) }">
                            		<img src="${request.getContextPath()}/images/icons/lock.png" title="${message(code:'ruleSet.used.forClient','default':'For newly created clients') }" />
                            	</g:if>
							</td>
                        
                            <td><g:link action="edit" id="${ruleSetInstance.id}">${fieldValue(bean: ruleSetInstance, field: "name")}</g:link></td>
                        
                            <td>${ruleSetInstance.resultClass}</td>
                        
                            <td>${ruleSetInstance.resultInstance}</td>
                        
                            <td>${fieldValue(bean: ruleSetInstance, field: "_action")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>

			<div class="paginateButtons">
				<g:paginate total="${ruleSetInstanceTotal}" />
			</div>
			<div class="grid_6">
				<g:link class="button button-gray" action="create" onclick="loadScreenBlock()"><span
					class="add"></span><g:message code="default.button.create.label" default="Create" /></g:link>
			</div>
        </section>
    </div>
</section>

</body>
</html>
