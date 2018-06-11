
<%@ page import="com.oneapp.cloud.core.ActivityFeed" %>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="activityFeed.list" default="ActivityFeed List" /></title>
    </head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="activityFeed.list" default="ActivityFeed List" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                   <tr>
                        
                   	    <g:sortableColumn property="id" title="Id" titleKey="activityFeed.id" />
                        
                   	    <th><g:message code="activityFeed.config" default="Config" /></th>
                   	    
                   	    <g:sortableColumn property="activityContent" title="Activity Content" titleKey="activityFeed.activityContent" />
                        
                   	    <g:sortableColumn property="shareId" title="Share Id" titleKey="activityFeed.shareId" />
                        
                   	    <th><g:message code="activityFeed.createdBy" default="Created By" /></th>
                   	    
                   	    <g:sortableColumn property="lastUpdated" title="Last Updated" titleKey="activityFeed.lastUpdated" />
                        
   				    </tr>
                    </thead>
                    <tbody>
                   <g:each in="${activityFeedInstanceList}" status="i" var="activityFeedInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${activityFeedInstance.id}">${fieldValue(bean: activityFeedInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: activityFeedInstance, field: "config")}</td>
                        
                            <td>${fieldValue(bean: activityFeedInstance, field: "activityContent")}</td>
                        
                            <td>${fieldValue(bean: activityFeedInstance, field: "shareId")}</td>
                        
                            <td>${fieldValue(bean: activityFeedInstance, field: "createdBy")}</td>
                        
                            <td><g:formatDate date="${activityFeedInstance.lastUpdated}" /></td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <div class="paginateButtons">
                     <g:paginate total="${activityFeedInstanceTotal}" />
                </div>
		        <div class="grid_6">
                  <g:link class="button button-gray" action="create"><span
                            class="add"></span><g:message code="default.button.create.label" default="Create" /></g:link>
                </div>

            </div>
        </section>
    </div>
</section>

</body>
</html>
