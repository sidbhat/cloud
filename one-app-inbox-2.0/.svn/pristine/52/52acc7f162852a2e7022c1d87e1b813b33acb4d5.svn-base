
<%@ page import="com.oneapp.cloud.core.ActivityFeedConfig" %>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="activityFeedConfig.list" default="ActivityFeedConfig List" /></title>
    </head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="activityFeedConfig.list" default="ActivityFeedConfig List" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                   <tr>
                        
                   	    <g:sortableColumn property="id" title="Id" titleKey="activityFeedConfig.id" />
                        
                   	    <g:sortableColumn property="configName" title="Config Name" titleKey="activityFeedConfig.configName" />
                        
                   	    <g:sortableColumn property="url" title="Url" titleKey="activityFeedConfig.url" />
                        
                   	    <th><g:message code="activityFeedConfig.createdBy" default="Created By" /></th>
                   	    
                   	    <g:sortableColumn property="shareType" title="Share Type" titleKey="activityFeedConfig.shareType" />
                        
                   	    <g:sortableColumn property="shareLink" title="Share Link" titleKey="activityFeedConfig.shareLink" />
                        
   				    </tr>
                    </thead>
                    <tbody>
                   <g:each in="${activityFeedConfigInstanceList}" status="i" var="activityFeedConfigInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${activityFeedConfigInstance.id}">${fieldValue(bean: activityFeedConfigInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: activityFeedConfigInstance, field: "configName")}</td>
                        
                            <td>${fieldValue(bean: activityFeedConfigInstance, field: "url")}</td>
                        
                            <td>${fieldValue(bean: activityFeedConfigInstance, field: "createdBy")}</td>
                        
                            <td>${fieldValue(bean: activityFeedConfigInstance, field: "shareType")}</td>
                        
                            <td>${fieldValue(bean: activityFeedConfigInstance, field: "shareLink")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <div class="paginateButtons">
                     <g:paginate total="${activityFeedConfigInstanceTotal}" />
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
