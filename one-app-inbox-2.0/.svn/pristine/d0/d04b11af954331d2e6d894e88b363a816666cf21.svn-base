
<%@page import="grails.plugin.multitenant.core.util.TenantUtils"%>
<%@ page import="org.grails.formbuilder.FormTemplate" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
  	<title><g:message code="formTemplate.create" default="Create FormTemplate" /></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2>
       <g:message code="formTemplate.create" default="Create Form Template" /></h2>
   <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${formTemplateInstance}">
            <div class="errors">
                <g:renderErrors bean="${formTemplateInstance}" as="list" />
            </div>
            </g:hasErrors>
</header>
<section class="container_6 clearfix">
<div class="form grid_6">
<g:uploadForm method="post" >
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
                                    <label for="name"><g:message code="formTemplate.name" default="Name"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: formTemplateInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" maxlength="150"
                                                 value="${fieldValue(bean: formTemplateInstance, field: 'name')}"/>

                                </td>
                            </tr>
					     <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="category"><g:message code="formTemplate.category" default="Category"/>:</label>
                                </td>
                                <td valign="top"
                                    class="value ${hasErrors(bean: formTemplateInstance, field: 'category', 'errors')}">
                                    <g:textField name="category" maxlength="150"
                                                 value="${fieldValue(bean: formTemplateInstance, field: 'category')}"/>

                                </td>
                            </tr>
                            
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="form"><g:message code="formTemplate.form" default="Form" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: formTemplateInstance, field: 'form', 'errors')}">
                                    <g:select name="form.id" from="${formList}" optionKey="id" value="${formTemplateInstance?.form?.id}"  />

                                </td>
                            </tr>
						<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
							<tr class="prop">
								<td valign="top" class="name">
									<label for="global"><g:message code="formTemplate.global" default="Global" />:</label>
								</td>
								<td valign="top" class="value ${hasErrors(bean: formTemplateInstance, field: 'global', 'errors')}">
									<g:checkBox name="global" value="${formTemplateInstance?.global}" />
								</td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name">
									<label for="formTeplateImg"><g:message code="userProfile.formTeplateImg" default="Image" /></label>
								</td>
								<td valign="top" class="value ">
									<input type="file" id="formTeplateImg" name="formTeplateImg" />
								</td>
							</tr>
						</sec:ifAnyGranted>
      					
     
        </tbody>
    </table>
    <div class="action">
     	 <g:actionSubmit class="button button-green" action="save" style="width: 120px" value="${message(code: 'default.button.create.label', default: 'Create')}" />
     	 <g:actionSubmit class="button button-blue" action="list" style="width: 120px" value="${message(code: 'default.button.list.label', default: 'List')}" />
    </div>
</section>

</div>


  </section>
</div>
</g:uploadForm>
</div>
</section>
</body>
</html>

