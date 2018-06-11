
<%@ page import="com.oneapp.cloud.core.ActivityFeedConfig" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
  	<title><g:message code="activityFeedConfig.create" default="Create ActivityFeedConfig" /></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2>
       <g:message code="activityFeedConfig.edit" default="Create ActivityFeedConfig" /></h2>
   <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${activityFeedConfigInstance}">
            <div class="errors">
                <g:renderErrors bean="${activityFeedConfigInstance}" as="list" />
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
                                    <label for="configName"><g:message code="activityFeedConfig.configName" default="Config Name" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedConfigInstance, field: 'configName', 'errors')}">
                                    <g:textField name="configName" maxlength="24" value="${fieldValue(bean: activityFeedConfigInstance, field: 'configName')}" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="url"><g:message code="activityFeedConfig.url" default="Url" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedConfigInstance, field: 'url', 'errors')}">
                                   <a href="#" title="Enter complete url feed "> <g:textField name="url" value="${fieldValue(bean: activityFeedConfigInstance, field: 'url')}" /></a>

                                </td>
                            </tr>
                               <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="shareLink"><g:message code="activityFeedConfig.shareLink" default="Share Link" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedConfigInstance, field: 'shareLink', 'errors')}">
                                    <a href="#" title="Enter relative url feed "><g:textField name="shareLink" value="${fieldValue(bean: activityFeedConfigInstance, field: 'shareLink')}" /></a>

                                </td>
                            </tr>
                                                     
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="createdBy"><g:message code="activityFeedConfig.createdBy" default="Created By" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedConfigInstance, field: 'createdBy', 'errors')}">
                                    <g:select name="createdBy.id" from="${com.oneapp.cloud.core.User.list()}" optionKey="id" value="${activityFeedConfigInstance?.createdBy?.id}" noSelection="['null': '']" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="shareType"><g:message code="activityFeedConfig.shareType" default="Share Type" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: activityFeedConfigInstance, field: 'shareType', 'errors')}">
                                    <g:textField name="shareType" value="${fieldValue(bean: activityFeedConfigInstance, field: 'shareType')}" />

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

