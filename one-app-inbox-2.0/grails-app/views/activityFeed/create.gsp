
<%@ page import="com.oneapp.cloud.core.ActivityFeed" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
  	<title><g:message code="activityFeed.create" default="Create ActivityFeed" /></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2>
       <g:message code="activityFeed.edit" default="Create ActivityFeed" /></h2>
   <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${activityFeedInstance}">
            <div class="errors">
                <g:renderErrors bean="${activityFeedInstance}" as="list" />
            </div>
            </g:hasErrors>
</header>
<section class="container_6 clearfix">
<div class="form grid_6">
<g:form method="post" >
<div class="tabbed-pane">
<ul class="tabs">
    <li><a href="#">Details</a></li>
</ul>

<!-- tab "panes" -->
<div class="panes clearfix">

<section>

    <table>
        <tbody>
         <ul class="action-buttons clearfix fr">
                                   
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="config"><g:message code="activityFeed.config" default="Config" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'config', 'errors')}">
                                    <g:select name="config.id" from="${com.oneapp.cloud.core.ActivityFeedConfig.list()}" optionKey="id" value="${activityFeedInstance?.config?.id}"  />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="activityContent"><g:message code="activityFeed.activityContent" default="Activity Content" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'activityContent', 'errors')}">
                                    <g:textArea name="activityContent" style="resize: vertical;" rows="5" cols="40" value="${fieldValue(bean: activityFeedInstance, field: 'activityContent')}" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="shareId"><g:message code="activityFeed.shareId" default="Share Id" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'shareId', 'errors')}">
                                    <g:textField name="shareId" value="${fieldValue(bean: activityFeedInstance, field: 'shareId')}" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createdBy"><g:message code="activityFeed.createdBy" default="Created By" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'createdBy', 'errors')}">
                                    <g:select name="createdBy.id" from="${com.oneapp.cloud.core.User.list()}" optionKey="id" value="${activityFeedInstance?.createdBy?.id}"  />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="lastUpdated"><g:message code="activityFeed.lastUpdated" default="Last Updated" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'lastUpdated', 'errors')}">
                                    <input type="date" name="lastUpdated" value="${activityFeedInstance?.lastUpdated}" noSelection="['': '']" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="dateCreated"><g:message code="activityFeed.dateCreated" default="Date Created" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'dateCreated', 'errors')}">
                                    <input type="date" name="dateCreated" value="${activityFeedInstance?.dateCreated}" noSelection="['': '']" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="visibility"><g:message code="activityFeed.visibility" default="Visibility" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'visibility', 'errors')}">
                                    <g:select name="visibility" from="${activityFeedInstance.constraints.visibility.inList}" value="${activityFeedInstance.visibility}" valueMessagePrefix="activityFeed.visibility"  />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="feedState"><g:message code="activityFeed.feedState" default="Feed State" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'feedState', 'errors')}">
                                    <g:select name="feedState" from="${activityFeedInstance.constraints.feedState.inList}" value="${activityFeedInstance.feedState}" valueMessagePrefix="activityFeed.feedState"  />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sharedUsers"><g:message code="activityFeed.sharedUsers" default="Shared Users" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'sharedUsers', 'errors')}">
                                    

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="sharedGroups"><g:message code="activityFeed.sharedGroups" default="Shared Groups" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'sharedGroups', 'errors')}">
                                    

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="tenantId"><g:message code="activityFeed.tenantId" default="Tenant Id" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedInstance, field: 'tenantId', 'errors')}">
                                    <g:textField name="tenantId" value="${fieldValue(bean: activityFeedInstance, field: 'tenantId')}" />

                                </td>
                            </tr>
                                             
      					    <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="attachment"><g:message code="activityFeed.attachment" default="Upload" />:</label>
                                </td>
                                <td valign="top" >
                                     <input type="file" name="attachment" id="attachment">Upload</input>
                         

                                </td>
                            </tr>
                        
     
        </tbody>
    </table>
    <div class="action">
     	 <g:actionSubmit class="button button-green" action="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
    </div>
</section>

</div>


  </section>
</div>
</g:form>
</div>
</section>
</body>
</html>

