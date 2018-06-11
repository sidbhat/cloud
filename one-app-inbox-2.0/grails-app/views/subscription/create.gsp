
<%@ page import="com.oneapp.cloud.core.Subscription" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
  	<title><g:message code="subscription.create" default="Create Subscription" /></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2>
       <g:message code="subscription.edit" default="Create Subscription" /></h2>
   <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${subscriptionInstance}">
            <div class="errors">
                <g:renderErrors bean="${subscriptionInstance}" as="list" />
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
                                    <label for="follower"><g:message code="subscription.follower" default="Follower" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: subscriptionInstance, field: 'follower', 'errors')}">
                                    <g:select name="follower.id" from="${com.oneapp.cloud.core.User.list()}" optionKey="id" value="${subscriptionInstance?.follower?.id}"  />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="followClass"><g:message code="subscription.followClass" default="Subscribe To" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: subscriptionInstance, field: 'followClass', 'errors')}">
                                     <g:select name="followClass" from="${com.oneapp.cloud.core.ActivityFeedConfig.list()}" optionKey="id"  value="${subscriptionInstance?.followClass}" noSelection="['null': '']" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="followId"><g:message code="subscription.followId" default="Subscribe To Id" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: subscriptionInstance, field: 'followId', 'errors')}">
                                    <g:textField name="followId" value="${fieldValue(bean: subscriptionInstance, field: 'followId')}" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="followCategory"><g:message code="subscription.followCategory" default="Subscription Category" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: subscriptionInstance, field: 'followCategory', 'errors')}">
                                    <g:textField name="followCategory" value="${fieldValue(bean: subscriptionInstance, field: 'followCategory')}" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="followUser"><g:message code="subscription.followUser" default="Subscribe to User" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: subscriptionInstance, field: 'followUser', 'errors')}">
                                    <g:select name="followUser.id" from="${com.oneapp.cloud.core.User.list()}" optionKey="id" value="${subscriptionInstance?.followUser?.id}" noSelection="['null': '']" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="state"><g:message code="subscription.state" default="State" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: subscriptionInstance, field: 'state', 'errors')}">
                                    <g:select name="state" from="${subscriptionInstance.constraints.state.inList}" value="${subscriptionInstance.state}" valueMessagePrefix="subscription.state" noSelection="['': '']" />

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

