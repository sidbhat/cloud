
<%@ page import="org.grails.formbuilder.FormTemplate" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="formTemplate.edit" default="Edit FormTemplate" /></title>
    <script type="text/javascript">
    $(document).ready(function(){
    	$('.attachmentImg').load(setImagesWidth);
    });
    var setImagesWidth = function(){
    	$('.attachmentImg').each(function (){
    			this.removeAttribute("style");
    			this.style.height=200+"px";
    			this.style.width=178+"px";
    			$(this).parent().css('width', '178px');
    			$(this).parent().css('height', '200px');
    			$(this).parent().parent().css('width', '178px');
    			$(this).parent().parent().css('height', '200px');
    	});  
    }
    </script>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
   

    <h2>
       <g:message code="formTemplate.edit" default="Edit Form Template" /></h2>
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
				<g:hiddenField name="id" value="${formTemplateInstance?.id}" />
				<g:hiddenField name="version" value="${formTemplateInstance?.version}" />
<div class="tabbed-pane">
<ul class="tabs">
    <li><a href="#">Details</a></li>
</ul>

<!-- tab "panes" -->
<div class="panes clearfix">

<section>

    <table>
        <tbody>
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
                                <td valign="top" class="name ${hasErrors(bean: formTemplateInstance, field: 'form', 'errors')}">
                                        &nbsp;&nbsp;${formTemplateInstance?.form}
                                </td>
                            </tr>
                                             
						<sec:ifAnyGranted roles="ROLE_SUPER_ADMIN">
							<tr class="prop">
								<td valign="top" class="name">
									<label for="global"><g:message code="formTemplate.global" default="Global" />:</label>
								</td>
								<td valign="top" class="value ${hasErrors(bean: formTemplateInstance, field: 'global', 'errors')}">
									<g:checkBox name="global" value="${formTemplateInstance?.global}" style="margin-top:10px;"/>
								</td>
							</tr>
							<tr class="prop">
								<td valign="top" class="name">
									<label for="formTeplateImg"><g:message code="userProfile.formTeplateImg" default="Image" /></label>
								</td>
								<td valign="top" class="value ">
								<g:set var="attachmentsCount" value="${0}"></g:set>
								<div style="position:relative;float:left;padding-left:4px;margin-bottom:5px;font-size:12px;">
											   <g:each in="${formTemplateInstance?.attachments.sort{if(grailsApplication.config.attachment.image.ext.contains(it.ext?.toLowerCase())){return -1}else{return 0}} }" var="attachment">
											   		<%attachmentsCount++ %>
											   		<g:if test="${grailsApplication.config.attachment.image.ext.contains(attachment.ext?.toLowerCase())}">
												<div style="border: 1px solid #CCCCCC;display: inline-block;margin: 2px;padding: 5px;width: 178px;height: 200px;">
												<attachments:icon attachment="${attachment}" />
											</g:if>
										
												<attachments:deleteLink attachment="${attachment}" label="${'[Delete]'}"  returnPageURI="${createLink(action: 'edit', id:formTemplateInstance.id)}"/>
											
					
											   </g:each>
										   </div>
								<g:if test="${attachmentsCount<1}">
									<input type="file" id="formTeplateImg" name="formTeplateImg" />
									</g:if>
								</td>
						  </tr>
						</sec:ifAnyGranted>
                        
     
        </tbody>
    </table>
    <div class="action">
       <g:actionSubmit class="button button-green" action="update" style="width: 120px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
       <g:actionSubmit class="button button-red" action="delete" style="width: 120px" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
 	   <input type="button" class="button button-gray" style="width: 140px" value="${message(code: 'default.button.preview.label', default: 'Preview')}"
                    onclick="javascript:openPreviewScreen();"/>
 	   
 </div>
</section>
 <script>
 function openPreviewScreen(){
		loadScreenBlock()
		window.location = "${request.getContextPath()}/PF/a?formId=${formTemplateInstance?.formId}"
	}
 </script>

</div>


  </section>
</div>
</g:uploadForm>
</div>
</section>
</body>
</html>

