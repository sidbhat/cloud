
<%@ page import="org.grails.formbuilder.FormAdmin" %>
<%@ page import="grails.converters.JSON" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
  	<title><g:message code="default.formAdmin.create.label" args="[formAdminInstance?.form]" default="${formAdminInstance?.form} - Settings"/></title>
  	<script>
  		var statusFields = ${statusFields as JSON};
  		function changeBlockUserEditingField(statusDD){
  	  		var $blockUserEditing = $('#blockUserEditing')
  			$('option',$blockUserEditing).remove();
  			var statusField = null
  			var $statusDD = $(statusDD)
  			if($statusDD.val()){
				$(".statusFieldClass").show()
			}else{
				$(".statusFieldClass").hide()
			}
  			for(var i=0;i<statusFields.length;i++){
  	  			if(statusFields[i].id == $statusDD.val()){
  	  				statusField = statusFields[i];
  	  				break;
  	  	  		}
  	  		}
  	  		if(statusField != null){
  	  	  		for(var i = 0; i<statusField.values.length; i++){
  	  	  	  		$option = $('<option></option>');
  	  	  	  		$option.text(statusField.values[i])
  	  	  	  			.val(statusField.values[i]);
  	  				$blockUserEditing.append($option);
  	  	  		}
  	  	  	}
  	  	}
		function shpf(dd){
			if(dd.value == 'Password')$('#formPasswordTR').show();else $('#formPasswordTR').hide()
			if(dd.value == 'Login')$('#publishedWithTR').show();else $('#publishedWithTR').hide()
		}
		function showHideRedirect(){
			  if ($("#redirectCheck").attr('checked')) {
				  $("#openForEdit").attr('checked',false)
				  $("#redirectUrlTr").show()
			  }else{
				  $("#redirectUrlTr").hide()
				  $("#openForEdit").attr('checked',true)
			  }
		}
		function openSubscriptionList(){
			loadScreenBlock()
			window.location = "${createLink(controller:'form',action:'edit', id:formAdminInstance?.form?.id )}"
		}
  	</script>
  	<style>
    	.defaultSettings fieldset{
    		margin-top:10px;
    		padding:0 0 0 20px;
    	}
    	.form input[type="checkbox"] {
			margin-left:4px;
		}
		.defaultSettings input, .defaultSettings textarea, .defaultSettings select {
			margin-bottom: 10px;
		}
		.defaultSettings .action {
			margin-left: 185px;
		}
    </style>
</head>

<body>
<g:if test="${formAdminInstance.form.formCat != 'S'}">
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2><g:message code="default.formAdmin.create.label" args="[formAdminInstance?.form]" default="${formAdminInstance?.form} - Settings"/></h2>
   <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${formAdminInstance}">
            <div class="errors">
                <g:renderErrors bean="${formAdminInstance}" as="list" />
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
					<div class="panes clearfix defaultSettings">
					
						<section>
							<fieldset>
								<legend>Basic</legend>
								<table>
									<tbody>
										<tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="form"><g:message code="formAdmin.form" default="Form" />:</label>
			                                </td>
			                                <td valign="top" class="name labelValue ${hasErrors(bean: formAdminInstance, field: 'form', 'errors')}">
												<g:hiddenField name="form.id" value="${formAdminInstance?.form?.id}"/>
												<g:link controller="form" action="edit" id="${formAdminInstance?.form?.id}">${formAdminInstance?.form}</g:link>
			                                </td>
			                            </tr>
													                                 
										<tr class="prop">
											<td valign="top" class="name">
												<label for="statusField.id"><g:message code="formAdmin.statusField" default="Select status field" />:</label>
											</td>
											<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'statusField', 'errors')}">
												<g:select name="statusField.id" from="${statusFields }" optionKey="id" optionValue="label" value="${formAdminInstance?.statusField?.id }" noSelection="['':'Select Status Field']" onchange="changeBlockUserEditingField(this);"></g:select>
											</td>
										</tr>
										<tr class="prop statusFieldClass" style="${formAdminInstance?.statusField?.id ?'':'display:none;'}">
											<td valign="top" class="name">
												<label for="blockUserEditing"><g:message code="formAdmin.blockUserEditing" default="User changes not allowed for" />:</label>
											</td>
											<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'blockUserEditing', 'errors')}">
												<g:select name="blockUserEditing" from="${(formAdminInstance?.statusField?.id)?(statusFields.find{it.id == formAdminInstance?.statusField?.id}?.value):[] }" value="${formAdminInstance?.blockUserEditing }" multiple="true"></g:select>
											</td>
										</tr>
										<tr class="prop statusFieldClass" style="${formAdminInstance?.statusField?.id ?'':'display:none;'}">
											<td valign="top" class="name">
												<label for="showStatusToUser"><g:message code="formAdmin.showStatusToUser" default="Show status dropdown to users" />:</label>
											</td>
											<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'showStatusToUser', 'errors')}">
												<g:checkBox name="showStatusToUser" value="${formAdminInstance?.showStatusToUser}" ></g:checkBox>
											</td>
										</tr>
										<tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="trackChanges"><g:message code="formAdmin.trackChanges" default="Track Changes" />:</label>
			                                </td>
			                                <td valign="top" class="name ${hasErrors(bean: formAdminInstance, field: 'trackChanges', 'errors')}">
			                                    <g:checkBox name="trackChanges" value="${formAdminInstance?.trackChanges?true:false}" />
			                                </td>
			                            </tr>
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="searchable"><g:message code="formAdmin.searchable" default="Searchable" />:</label>
			                                </td>
			                                <td valign="top" class="name ${hasErrors(bean: formAdminInstance, field: 'searchable', 'errors')}">
			                                    <g:checkBox name="searchable" value="${formAdminInstance?.searchable?true:false}" />
			                                </td>
			                            </tr>
									</tbody>
								</table>
							</fieldset>
							<fieldset>
								<legend><g:message code="formAdmin.createQuestion" default="Who can create?" /></legend>
								<table>
									<tbody>
			                            <tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="formLogin"><g:message code="formAdmin.formLogin" default="Allow" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formLogin', 'errors')}">
			                                 	<g:select name="formLogin" from="${[[id:'NoOne',label:'No one (form closed)'],[id:'Login',label:'System users'],[id:'Public',label:'Anyone'],[id:'Password',label:'Anyone with a password']]}" optionKey="id" optionValue="label" value="${formAdminInstance?.formLogin?:'Public'}" valueMessagePrefix="formAdmin.formLogin" onchange="shpf(this)" />
			                                </td>
			                            </tr>
			                            <tr class="prop" id="formPasswordTR" style="${formAdminInstance?.formLogin == 'Password'?'':'display:none;'}">
			                                <td valign="top" class="name">
			                                    <label for="formPassword"><g:message code="formAdmin.formPassword" default="Password"/> <em>*</em>:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formPassword', 'errors')}">
			                                    <g:passwordField name="formPassword" maxlength="150" value="${fieldValue(bean: formAdminInstance, field: 'formPassword')}"/>
			                                </td>
			                            </tr>
			                            <tr class="prop" id="publishedWithTR" style="display:none;">
			                                <td valign="top" class="name">
			                                    <label for="publishedWith.id"><g:message code="formAdmin.publishedWith" default="Published With" />:</label>
			                                </td>
			                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'publishedWith', 'errors')}">
			                                    <g:select name="publishedWith" from="${userList}" optionKey="id" optionValue="username" multiple="true" value="${formAdminInstance?.publishedWith?:session['user'].id}" />
			                                </td>
			                            </tr>
									</tbody>
								</table>
							</fieldset>
							<fieldset>
								<legend><g:message code="formAdmin.editQuestion" default="Who can edit?" /></legend>
								<table>
									<tbody>
			                        	<tr class="prop">
											<td valign="top" class="name">
												<label for="openForEdit"><g:message code="formAdmin.statusField" default="Open for editing" />:</label>
											</td>
											<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'openForEdit', 'errors')}">
												<g:checkBox name="openForEdit" value="${formAdminInstance?.openForEdit?:true }" style="margin-top:8px;"></g:checkBox>
											</td>
										</tr>
									</tbody>
								</table>
							</fieldset>
							<fieldset>
								<legend><g:message code="formAdmin.afterFormSubmision" default="After form submission" /></legend>
								<table>
									<tbody>
										<tr class="prop">
			                                <td valign="top" class="name">
			                                    <label for="redirectCheck"><g:message code="formAdmin.redirectCheck" default="Redirect to URL" />:</label>
			                                </td>
			                                <td valign="top" class="name ${hasErrors(bean: formAdminInstance, field: 'redirectUrl', 'errors')}">
			                                    <g:checkBox name="redirectCheck" onchange="showHideRedirect()" value="${formAdminInstance?.redirectUrl || params.redirectCheck?true:false}"/>
			                                </td>
			                            </tr>
										<tr class="prop" style="${formAdminInstance?.redirectUrl || params.redirectCheck?'':'display:none;'}" id="redirectUrlTr">
			                                <td valign="top" class="name">
			                                    <label for="redirectUrl"><g:message code="formAdmin.redirectUrl" default="Redirect URL" /> <em>*</em>:</label>
			                                </td>
			                                <td valign="top" class="name ${hasErrors(bean: formAdminInstance, field: 'redirectUrl', 'errors')}">
			                                    <g:textField name="redirectUrl" maxlength="150" value="${fieldValue(bean: formAdminInstance, field: 'redirectUrl')}" placeholder="http://"/>
			                                </td>
			                            </tr>
			                        	<tr class="prop" id="formSubmitMessageTr">
			                                <td valign="top" class="name">
			                                    <label for="formSubmitMessage"><g:message code="formAdmin.formSubmitMessage" default="Form Submit Message"/>:</label>
			                                </td>
			                                <td valign="top"
			                                    class="value ${hasErrors(bean: formAdminInstance, field: 'formSubmitMessage', 'errors')}">
			                                    <g:textField name="formSubmitMessage" maxlength="150" value="${fieldValue(bean: formAdminInstance, field: 'formSubmitMessage')}"/>
			                                </td>
			                            </tr>
									</tbody>
								</table>
							</fieldset>
						    
						    <div class="action">
						     	 <g:actionSubmit class="button button-green" action="save" style="width: 120px" value="${message(code: 'default.button.create.label', default: 'Create')}" />
						     	<input type="button" class="button button-blue" style="width: 120px" value="${message(code: 'form.edit', 'default': 'Edit Form')}"
												onclick="javascript:openSubscriptionList();"/>
						    </div>
						</section>
					
					</div>
				</div>
			</g:form>
		</div>
	</section>
		</g:if>
</body>
</html>