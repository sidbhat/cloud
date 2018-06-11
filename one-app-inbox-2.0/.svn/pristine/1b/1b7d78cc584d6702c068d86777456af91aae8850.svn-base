
<%@ page import="com.oneapp.cloud.core.Subscription" %>
<html>
<head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="subscription.list" default="Subscription List" /></title>
    </head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="subscription.list" default="Subscription List" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    <thead>
                   <tr>
                        
                   	    <g:sortableColumn property="id" title="Id" titleKey="subscription.id" />
                        
                   	    <th><g:message code="subscription.follower" default="Subscribe To User" /></th>
                   	    
                   	    <g:sortableColumn property="followClass" title="Subscribe To " titleKey="subscription.followClass" />
                        
                   	    <g:sortableColumn property="followId" title="Subscribe To Id" titleKey="subscription.followId" />
                        
                   	    <g:sortableColumn property="followCategory" title="Subscription Category" titleKey="subscription.followCategory" />
                        
   				    </tr>
                    </thead>
                    <tbody>
                   <g:each in="${subscriptionInstanceList}" status="i" var="subscriptionInstance">
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td><g:link action="edit" id="${subscriptionInstance.id}">${fieldValue(bean: subscriptionInstance, field: "id")}</g:link></td>
                        
                            <td>${fieldValue(bean: subscriptionInstance, field: "follower")}</td>
                        
                            <td>${fieldValue(bean: subscriptionInstance, field: "followClass")}</td>
                        
                            <td>${fieldValue(bean: subscriptionInstance, field: "followId")}</td>
                        
                            <td>${fieldValue(bean: subscriptionInstance, field: "followCategory")}</td>
                        
                        </tr>
                    </g:each>
                    </tbody>
                </table>

                <div class="paginateButtons">
                     <g:paginate total="${subscriptionInstanceTotal}" />
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
