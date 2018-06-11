<%@page import="grails.converters.JSON"%>
<%@ page import="org.grails.formbuilder.FormAdmin" %>
<%@ page import="grails.converters.JSON" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="default.formAdmin.edit.label" args="[formAdminInstance?.form]" default="${formAdminInstance?.form} - Settings"/></title>
    <script type="text/javascript" src="${request.getContextPath()}/js/spCond.js"></script>
    <script type="text/javascript" src="${request.getContextPath()}/js/rulesUI.js"></script>
    <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/rulesUI.css'}"/>
    <script type="text/javascript">
    	var statusFields = ${statusFields as JSON};
    	var savedStatusFieldId = "${formAdminInstance?.statusField?.id }";
    	var bue = ${formAdminInstance?.blockUserEditing as JSON};
		function usrCh(statusDD){
	  		var a = $('#blockUserEditing')
			$('option',a).remove();
			var statusField = null
			var $d = $(statusDD)
			if($d.val()){
				$(".statusFieldClass").show()
			}else{
				$(".statusFieldClass").hide()
			}
			for(var i=0;i<statusFields.length;i++){
	  			if(statusFields[i].id == $d.val()){
	  				statusField = statusFields[i];
	  				break;
	  	  		}
	  		}
	  		if(statusField != null){
	  	  		for(var i = 0; i<statusField.values.length; i++){
	  	  	  		$option = $('<option></option>');
	  	  	  		$option.text(statusField.values[i])
	  	  	  			.val(statusField.values[i]);
	  				a.append($option);
	  				if(savedStatusFieldId == statusField.id){
	  					a.val(bue);
		  			}
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
		$(document).ready(function(){
			<%
			def initialIndex = (params.un?1:(webHookDetailsView?3:0))
			%>
			$(".mainTabs").tabs(".panes section",{initialIndex:${initialIndex}});
			$(".ruleTabs").tabs(".panes",{initialIndex:0});
			//showHideRedirect();
		});
        function getDriectURL(url){
        	showPopup(url)
        }
        function getEmbededCode(formId){
            var serverURL = '${grailsApplication.config.grails.serverURL}';
            showPopup('<div id="oneappcloud'+formId+'"></div>\n<scr'+'ipt type="text/javascript" src="'+serverURL+'/embed/jsx/'+formId+'?height=380"></scr'+'ipt>');
        }
        function showPopup(str){
        	$(".popupMessage textarea").val(str);
        	$(".popupMessage").show("slow");
        }
        function confirmMessage(message){
            var confirmation = confirm(message)
            if(confirmation == true){
            	loadScreenBlock();
                return true;
            }else{
                return false;
            }
        }
        function showFormCreateDD(event){
            
        }
	 function facebook(url, title, desc, img) {
	window.open( "http://www.facebook.com/sharer.php?s=100&p[title]="+encodeURI(title)+"&p[summary]=" +desc+ "&p[url]="+encodeURI(url)+"&p[images][0]="+encodeURI(img), "facebook", "status=1, height=400, width=550, resizable=0, toolbar=0");
	facebook.focus();
	}

	function linkedIn(url, title, desc, source) {
	window.open( "http://www.linkedin.com/shareArticle?mini=true&url=" + encodeURI(url) +"&title=" + encodeURI(title) + "&summary=" + encodeURI(desc) + "&source=" + encodeURI(source), "linkedIn", "status=1, height=400, width=550, resizable=0, toolbar=0");
	linkedIn.focus();
	}

	function tweet(url, text) {
	window.open( "https://twitter.com/intent/tweet?text=" + encodeURI(text) + "&url=" + encodeURI(url), "tweet", "status=1, height=400, width=550, resizable=0, toolbar=0");
	tweet.focus();
	}

	function plusone(url) {
	window.open( "https://plusone.google.com/_/+1/confirm?hl=en&url=" + encodeURI(url), "plusone", "status=1, height=400, width=550, resizable=0, toolbar=0");
	plusone.focus();
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
					<h2><g:message code="default.formAdmin.edit.label" args="[formAdminInstance?.form]" default="${formAdminInstance?.form} - Settings"/></h2>
					<div class="message" id="successMessageHere" ${flash.message?'':'style="display:none;"'}>
					<g:if test="${flash.message}">
						<g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" />
					</g:if>
					</div>
					<g:hasErrors bean="${formAdminInstance}">
						<div class="errors">
						    <g:renderErrors bean="${formAdminInstance}" as="list" />
						</div>
					</g:hasErrors>
					<g:hasErrors bean="${webHookDetails}">
						<div class="errors">
						    <g:renderErrors bean="${webHookDetails}" as="list" />
						</div>
					</g:hasErrors>
				</header>
				<section class="container_6 clearfix">
					<div class="message success closeable popupMessage" style="padding: 5px 15px; position: absolute;   margin: auto; width: auto; top: 230px; font-weight: bold; z-index: 100001; display: none;">
						<span class="message-close" style="display:block;"></span>
			         	<p>
			         	Copy text:<br>
			           	<textarea style="width:739px;height:75px;"></textarea>
			         	</p>
					</div>
					<div class="form grid_6">
						<div class="tabbed-pane">
							<ul class="tabs mainTabs">
							    <li><a href="#">Default</a></li>
							    <li><a href="#">Notifications</a></li>
							    <li><a href="#">Rules</a></li>
							    <li><a href="#">Integration</a></li>
							</ul>
							
							<%--Default tab--%>
							<div class="panes clearfix defaultSettings">
								<section>
									<g:form method="post" >
										<g:hiddenField name="id" value="${formAdminInstance?.id}" />
										<g:hiddenField name="version" value="${formAdminInstance?.version}" />
										<fieldset>
										<legend>Basic</legend>
										<table>
											<tbody>
												<tr class="prop">
													<td valign="top" class="name">
														<label for="form"><g:message code="formAdmin.edit.form" default="Edit Form" />:</label>
													</td>
													<td valign="top" class="value labelValue ${hasErrors(bean: formAdminInstance, field: 'form', 'errors')}" style="padding-top: 6px;">
														<g:link controller="form" style="text-decoration:underline;" action="edit" id="${formAdminInstance?.form?.id}" onmouseover="this.title='Edit Form';this.removeAttribute('onmouseover')">${formAdminInstance?.form}</g:link>
													</td>
												</tr>
<%--												<g:if test="${'Approval'.equalsIgnoreCase(formAdminInstance.formType)}">--%>
												<tr class="prop"><td colspan="2"><table><tr>
													<td valign="top" class="name">
														<label for="statusField.id"><g:message code="formAdmin.statusField" default="Select status field" />:</label>
													</td>
													<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'statusField', 'errors')}">
														<g:select name="statusField.id" from="${statusFields }" optionKey="id" optionValue="label" value="${formAdminInstance?.statusField?.id }" noSelection="['':'Select Status Field']" onchange="usrCh(this);"></g:select>
													</td>
												</tr>
												<tr class="statusFieldClass" style="${formAdminInstance?.statusField?.id ?'':'display:none;'}">
													<td valign="top" class="name">
														<label for="blockUserEditing"><g:message code="formAdmin.blockUserEditing" default="User changes not allowed for" />:</label>
													</td>
													<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'statusField', 'errors')}">
														<g:select name="blockUserEditing" from="${(formAdminInstance?.statusField?.id)?statusFields.find{it.id == formAdminInstance.statusField.id}.values:[] }" value="${formAdminInstance?.blockUserEditing }"  multiple="true"></g:select>
													</td></tr></table></td>
												</tr>
												<tr class="statusFieldClass" style="${formAdminInstance?.statusField?.id ?'':'display:none;'}">
													<td valign="top" class="name" >
														<label for="showStatusToUser"><g:message code="formAdmin.showStatusToUser" default="Show status dropdown to users" />:</label>
													</td>
													<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'showStatusToUser', 'errors')}">
														<g:checkBox name="showStatusToUser" value="${formAdminInstance?.showStatusToUser}" ></g:checkBox>
													</td>
												</tr>
<%--												</g:if>--%>
												
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
										<legend>Who can create?</legend>
										<table>
											<tbody>
												<tr class="prop">
													<td valign="top" class="name">
														<label for="formLogin"><g:message code="formAdmin.formLogin" default="Allow" />:</label>
													</td>
													<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formLogin', 'errors')}">
														<g:select name="formLogin" from="${[[id:'NoOne',label:'No one (form closed)'],[id:'Login',label:'System users'],[id:'Public',label:'Anyone'],[id:'Password',label:'Anyone with a password']]}" optionKey="id" optionValue="label" value="${formAdminInstance.formLogin}" valueMessagePrefix="formAdmin.formLogin" onchange="shpf(this)" />
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
												 <tr class="prop" id="publishedWithTR" style="${formAdminInstance?.formLogin == 'Login'?'':'display:none;'}">
													<td valign="top" class="name">
														<label for="publishedWith"><g:message code="formAdmin.publishedWith" default="Published With" />:</label>
													</td>
													<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'publishedWith', 'errors')}">
														<g:select name="publishedWith" from="${onlyUsers}" optionKey="id" optionValue="username" multiple="${true }" value="${formAdminInstance?.publishedWith?.id}" />
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
														<g:checkBox name="openForEdit" value="${formAdminInstance?.openForEdit }" style="margin-top:8px;"></g:checkBox>
													</td>
												</tr>
					                           
											</table>
											</fieldset>
											<fieldset>
											<legend><g:message code="formAdmin.afterFormSubmision" default="After form submission" /></legend>
											<table>
												<tr class="prop">
					                                <td valign="top" class="name">
					                                    <label for="redirectCheck"><g:message code="formAdmin.redirectCheck" default="Redirect to URL" />:</label>
					                                </td>
					                                <td valign="top" class="name ${hasErrors(bean: formAdminInstance, field: 'redirectUrl', 'errors')}">
					                                    <g:checkBox name="redirectCheck" onchange="showHideRedirect()" value="${formAdminInstance?.redirectUrl || params.redirectCheck?true:false}"/>
					                                </td>
					                            </tr>
	<%--									                            <tr class="prop" style="${formAdminInstance.formType && 'approval'.equalsIgnoreCase(formAdminInstance.formType)?'display:none;':(formAdminInstance?.redirectUrl || params.redirectCheck?'':'display:none;')}" id="redirectUrlTr">--%>
					                            <tr class="prop" style="${formAdminInstance?.redirectUrl || params.redirectCheck?'':'display:none;'}" id="redirectUrlTr">
					                                <td valign="top" class="name">
					                                    <label for="redirectUrl"><g:message code="formAdmin.redirectUrl" default="Redirect URL" /> <em>*</em>:</label>
					                                </td>
					                                <td valign="top" class="name ${hasErrors(bean: formAdminInstance, field: 'redirectUrl', 'errors')}">
					                                    <g:textField name="redirectUrl" maxlength="150" value="${fieldValue(bean: formAdminInstance, field: 'redirectUrl')}"/>
					                                </td>
					                            </tr> 
												<tr class="prop" id="formSubmitMessageTr">
													<td valign="top" class="name">
														<label for="formSubmitMessage"><g:message code="formAdmin.formSubmitMessage" default="Form Submit Message"/>:</label>
													</td>
													<td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formSubmitMessage', 'errors')}">
														<g:textField name="formSubmitMessage" maxlength="150" value=""/>
														<g:if test="${formAdminInstance?.formSubmitMessage }">
															<script>
																$(document).ready(function(){
																	$("#formSubmitMessage").val('${formAdminInstance?.formSubmitMessage?.encodeAsJavaScript()}');
																})
															</script>
														</g:if>
													</td>
												</tr>
											</table>
											</fieldset>
											<table>
												
<%--												<g:else>--%>
													
						                        	
<%--												</g:else>--%>
					      					 	
					                            
												<tr>
													<td colspan="2">
														<table>
<%--															<g:if test="${!'Approval'.equalsIgnoreCase(formAdminInstance.formType)}">--%>
																
															
<%--															<g:if test="${!'Approval'.equalsIgnoreCase(formAdminInstance.formType)}">--%>
																
<%--																<tr class="prop napf" style="${formAdminInstance.formType && 'approval'.equalsIgnoreCase(formAdminInstance.formType)?'display:none;':''}">--%>
																
<%--															</g:if>--%>
														</table>
													</td>
												</tr>
											</tbody>
										</table>
										<div class="action">
											<g:actionSubmit class="button button-green" action="update" style="width: 110px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
											<div class="PostShare" style="width:120px;float: left;margin-left: 15px;margin-top: 2px;">
												<ul style="width:115px;">
													<li ><a href="javascript:;" class="ShareMainBtn" style="font-weight: bold;">
														<span>&nbsp;&nbsp;&nbsp;&nbsp;</span><g:message code="default.button.share.label" default="Share"  /><span class="arrow-down"></span></a>
														<ul style="width:115px;">
															<% def formsettings=JSON.parse(formAdminInstance?.form?.settings) %>
															<li><a target="_blank" href="${request.getContextPath() }/PF/sendEmail?formId=${formAdminInstance?.form?.id}&includeForm=true"  rel="#overlay" class="no-text help"><img src="${request.getContextPath()}/images/mail_16.png"/>&nbsp;<span style="vertical-align: super;width:60px">Email</span></a></li>
															<li><a href='javascript:;' onclick="getEmbededCode('${formInstance?.id}')"><img src="${request.getContextPath()}/images/emed_16.png"/>&nbsp;<span style="vertical-align: super;width:60px">Embed</span></a></li>
															<li><a href='javascript:;' onclick="getDriectURL('${formAdminInstance.shortURL}')" ><img src="${request.getContextPath()}/images/icon_direct_link_16.png"/>&nbsp;<span style="vertical-align: super;width:62px">Direct URL</span></a></li>
															<li><a href="javascript:void(0)" onclick="linkedIn('${formAdminInstance.shortURL}', '${formAdminInstance?.form}', '${formsettings?.en?.description}', '${formAdminInstance?.form}');"><img src="${request.getContextPath()}/images/Linkdin_16.png"/>&nbsp;<span style="vertical-align: super;width:60px">LinkedIn</span></a></li>
															<li><a href="javascript:void(0)" onclick="facebook('${formAdminInstance.shortURL}', '${formAdminInstance?.form}', '${formsettings?.en?.description}', '${formAdminInstance?.form}');"><img src="${request.getContextPath()}/images/facbook_16.png"/>&nbsp;<span style="vertical-align: super;width:60px">Facebook</span></a></li>
															<li><a href="javascript:void(0)" onclick="plusone('${formAdminInstance.shortURL}'); return false;"><img src="${request.getContextPath()}/images/icon_google_plus.png"/>&nbsp;<span style="vertical-align: super;width:60px">Google+</span></a></li>
															<li><a href="javascript:void(0)" onclick="tweet('${formAdminInstance.shortURL}', '${formAdminInstance?.form}');"><img src="${request.getContextPath()}/images/Twitter_16.png"/>&nbsp;<span style="vertical-align: super;width:60px">Twitter</span></a></li>
														</ul>
													</li>
												</ul>
										   </div>
										<input type="button" class="button button-blue" style="width: 110px;margin-left: 0px;" value="${message(code: 'form.edit', 'default': 'Edit Form')}"
												onclick="javascript:openSubscriptionList();"/>
										</div>
									</g:form>
								</section>
							</div>

							<%--Notification tab--%>
							<div class="panes clearfix defaultSettings">
								<section>
									<%
										def operators = ["<",">","=","<=",">=","!="]
										def fieldsWithSetAll = formAdminInstance.form.fieldsList
										fieldsWithSetAll = fieldsWithSetAll.collect{foField->
											def foSet = JSON.parse(foField.settings)
											def formulaFieldType
											if(foField.type=='FormulaField'){
												formulaFieldType = foSet.en.oldResultType
											}
											[id:foField.id,name:foField.name,type:foField.type,val:foSet.en.value,label:foField.type=='PlainText'?(foSet.en.text?:null):(foField.type=='Paypal'?null:foSet.en.label),ffType:formulaFieldType,isEmail:(foSet.restriction=='email')]
										}
										def fieldsWithSet = fieldsWithSetAll.findAll{grailsApplication.config.fields.forRule.contains(it.type)}
									%>
									<script>
										fieldsWithSetAll = ${fieldsWithSetAll as JSON};
									</script>
									<g:form method="post" >
										<g:hiddenField name="id" value="${formAdminInstance?.id}" />
										<g:hiddenField name="version" value="${formAdminInstance?.version}" />
										<g:hiddenField name="un" value="t" />
<%--										<g:hiddenField name="formSubmissionAction" value="Email"/>--%>
										<fieldset>
										<legend>Notification</legend>
										<table>
											<tbody>
<%--												<tr class="prop">--%>
<%--					                                <td valign="top" colspan="2">--%>
<%--					                                    <label for="formSubmissionAction" style="float:none;clear:none;"><g:message code="formAdmin.formSubmission" default="Send form submission" />:</label>--%>
<%--					                                </td>--%>
<%--					                            </tr>--%>
												<tr class="prop" style="${formAdminInstance?.formSubmissionAction=='Email'?'display:none;':''}">
					                                <td valign="top" class="name">
					                                    <label for="formSubmissionAction"><g:message code="formAdmin.formSubmissionAction" default="As" />:</label>
					                                </td>
					                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formSubmissionAction', 'errors')}">
														<g:select name="formSubmissionAction" from="${['Email','Feed']}" value="${formAdminInstance?.formSubmissionAction}" />
					                                </td>
					                            </tr>
					                            <tr class="prop">
					                                <td valign="top" class="name">
					                                    <label for="formSubmissionTo" style="float:none;clear:none;"><g:message code="formAdmin.formSubmissionTo" default="To users" />:</label>
					                                </td>
					                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formSubmissionTo', 'errors')}">
					                                	<g:select style="margin-bottom:2px;" name="formSubmissionTo" from="${userList}" optionKey="id" optionValue="username" multiple="${true }" value="${formAdminInstance?.formSubmissionTo?.id}" />
					                                	<table style="margin-bottom:20px;"><tr><td><label for="notiOnCreate" style="width:65px">On create</label></td><td><g:checkBox name="notiOnCreate" value="${formAdminInstance?.notiOnCreate }" style="margin:0"/></td><td class="formEmailFieldsTD"><label for="notiOnUpdate" style="width:135px">On update</label></td><td class="formEmailFieldsTD"><g:checkBox name="notiOnUpdate" value="${formAdminInstance?.notiOnUpdate }" style="margin:0"/><div style="width:100px;"></div></td></tr></table>
					                                </td>
					                            </tr>
					                            <tr class="prop formEmailFields">
					                                <td valign="top" class="name">
					                                    <label for="formSubmissionToExternal" style="float:none;clear:none;"><g:message code="formAdmin.formSubmissionToExternal" default="To external email-ids (comma separated)" />:</label>
					                                </td>
					                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formSubmissionTo', 'errors')}">
					                                	<g:textField style="margin-bottom:2px;width:236px;" name="formSubmissionToExternal" value="${formAdminInstance?.formSubmissionToExternal}" />
					                                	<table style="margin-bottom:20px;"><tr><td><label for="extNotiOnCreate" style="width:65px">On create</label></td><td><g:checkBox name="extNotiOnCreate" value="${formAdminInstance?.extNotiOnCreate }" style="margin:0"/></td><td><label for="extNotiOnUpdate" style="width:135px">On update</label></td><td><g:checkBox name="extNotiOnUpdate" value="${formAdminInstance?.extNotiOnUpdate }" style="margin:0"/><div style="width:100px;"></div></td></tr></table>
					                                </td>
					                            </tr>
					                            <tr class="prop">
					                                <td valign="top" class="name">
					                                    <label for="specialCondition" style="float:none;clear:none;"><g:message code="formAdmin.specialCondition" default="Active" />:</label>
					                                </td>
					                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'specialCondition', 'errors')}">
					                                	<g:checkBox style="margin-top: 7px;" name="specialCondition" value="${formAdminInstance?.specialCondition}" onclick="onOffSpCond(this);"/>
					                                </td>
					                            </tr>
					                            <tr>
					                            	<td colspan="2">
					                            		<div id="spCondDiv" style="${formAdminInstance?.specialCondition?'':'display:none;'}">
						                            		<style>table.spCond{margin-bottom:10px;margin-left:60px;}table.spCond tr:first-child .notiU{display:none}table.spCond tr:last-child .notiD{display:none}table.spCond td{max-width:initial;max-width:auto;}.spCond select{width:160px;margin:0}.spCond select.notiOp{width:80px}.spCond input[type=text]{width:170px;margin:0}select.notiCondOp{width:57px}.spCond img{width:20px;cursor:pointer}.notiUndoDel{display:none;}</style>
						                            		<table class="datatable spCond">
						                            			<thead>
						                            				<tr>
						                            					<th>Field</th><th>Operator</th><th>Value</th><th>Condition</th><th>Add/Rem.</th>
						                            				</tr>
						                            			</thead>
						                            			<tbody>
																	<g:if test="${formAdminInstance?.conditions }">
						                            					<g:each in="${formAdminInstance?.conditions }" status="condIdx" var="CondInst">
							                            					<tr>
								                            					<td>
								                            						<g:hiddenField class="notiId" name="conditions[${condIdx}].id" value="${CondInst?.id}"/>
								                            						<g:hiddenField class="notiSeq" name="conditions[${condIdx}].sequence" value="${CondInst?.sequence}"/>
								                            						<g:hiddenField class="notiStat" name="conditions[${condIdx}].status"/>
								                            						<g:select class="notiFieldId" name="conditions[${condIdx}].fieldId" from="${fieldsWithSet }" optionKey="id" optionValue="label" value="${CondInst?.fieldId}" />
								                            					</td>
								                            					<td><g:select class="notiOp" name="conditions[${condIdx}].op" from="${operators }" value="${CondInst?.op}" /></td>
								                            					<td><g:textField class="notiVal" name="conditions[${condIdx}].val" value="${CondInst?.val }"/></td>
								                            					<td><g:select class="notiCondOp" name="conditions[${condIdx}].condOp" from="${['AND','OR']}" value="${CondInst?.condOp}" /></td>
								                            					<td>
								                            						<img src="${resource(dir:'images',file:'add.png')}" onclick="notiAdd(this)">
								                            						<img class="notiDel" src="${resource(dir:'images',file:'images.png')}" onclick="notiDel(this)">
								                            						<img class="notiUndoDel" src="${resource(dir:'images',file:'undo.png')}" onclick="notiUndoDel(this)">
								                            						<img class="notiU" src="${resource(dir:'images',file:'up.png')}" onclick="notiU(this)">
								                            						<img class="notiD" src="${resource(dir:'images',file:'down.png')}" onclick="notiD(this)">
								                            					</td>
								                            				</tr>
							                            				</g:each>
						                            				</g:if>
						                            				<g:else>
							                            				<tr>
							                            					<td>
							                            						<g:hiddenField class="notiId" name="conditions[0].id" value=""/>
							                            						<g:hiddenField class="notiSeq" name="conditions[0].sequence"/>
							                            						<g:hiddenField class="notiStat" name="conditions[0].status"/>
							                            						<g:select class="notiFieldId" name="conditions[0].fieldId" from="${fieldsWithSet }" optionKey="id" optionValue="label" />
							                            					</td>
							                            					<td><g:select class="notiOp" name="conditions[0].op" from="${operators }" /></td>
							                            					<td><g:textField class="notiVal" name="conditions[0].val" /></td>
							                            					<td><g:select class="notiCondOp" name="conditions[0].condOp" from="${['AND','OR']}" /></td>
							                            					<td>
							                            						<img src="${resource(dir:'images',file:'add.png')}" onclick="notiAdd(this)">
							                            						<img class="notiDel" src="${resource(dir:'images',file:'images.png')}" onclick="notiDel(this)">
							                            						<img class="notiUndoDel" src="${resource(dir:'images',file:'undo.png')}" onclick="notiUndoDel(this)">
							                            						<img class="notiU" src="${resource(dir:'images',file:'up.png')}" onclick="notiU(this)">
								                            					<img class="notiD" src="${resource(dir:'images',file:'down.png')}" onclick="notiD(this)">
							                            					</td>
							                            				</tr>
						                            				</g:else>
						                            			</tbody>
						                            		</table>
						                            		<script>
						                            			var notiCount=${formAdminInstance?.conditions?.size()?:0};
						                            		</script>
					                            		</div>
					                            	</td>
					                            </tr>
					                         </tbody>
					                    </table>
					                    </fieldset>
					                    <fieldset class="formEmailFields">
										<legend>Confirmation</legend>
					                    <table>
											<tbody>
					                            <tr class="prop">
					                                <td valign="top" class="name">
					                                    <label for="formEmailField" style="float:none;clear:none;"><g:message code="formAdmin.formEmailField" default="To value entered in" />:</label>
					                                </td>
					                                <td valign="top" class="value ${hasErrors(bean: formAdminInstance, field: 'formEmailField', 'errors')}">
					                                	<g:select style="margin-bottom:2px;" name="formEmailField" from="${fieldsWithSet.findAll{it.isEmail == true}}" optionKey="id" optionValue="label" value="${formAdminInstance?.formEmailField}" multiple="${true }"/>
					                                	<table style="margin-bottom:20px;"><tr><td><label for="confOnCreate" style="width:65px">On create</label></td><td><g:checkBox name="confOnCreate" value="${formAdminInstance?.confOnCreate }" style="margin:0"/></td><td><label for="confOnUpdate" style="width:135px">On update</label></td><td><g:checkBox name="confOnUpdate" value="${formAdminInstance?.confOnUpdate }" style="margin:0"/><div style="width:100px;"></div></td></tr></table>
					                                </td>
					                            </tr>
					                         </tbody>
					                    </table>
					                    </fieldset>
										<div class="action">
											<g:actionSubmit class="button button-green" action="update" style="width: 120px" value="${message(code: 'default.button.update.label', default: 'Update')}" onclick="return validateNoti(this);"/>
											<input type="button" class="button button-blue" style="width: 120px" value="${message(code: 'form.edit', 'default': 'Edit Form')}"
												onclick="javascript:openSubscriptionList();"/>
										</div>
									</g:form>
								</section>
							</div>
							
							<%--Rules tab--%>
							<div class="panes clearfix">
								<section>
									<script>
										var addImgSource = "${resource(dir:'images',file:'add.png')}";
										var remImgSource = "${resource(dir:'images',file:'images.png')}";
									</script>
									<div class="tabbed-pane">
										<ul class="tabs ruleTabs">
										    <li><a href="#">Field Rules</a></li>
										    <li><a href="#">Page Rules</a></li>
										</ul>
										<div id="forRuleUpdate" style="display:none;"></div>
										<div class="panes clearfix allTypeRules">
											<div style="padding:10px;border-bottom:1px solid #aaa">
												<div id="noFieldRule" style="display:none">
													<h1 style="color:#333">There are no field rules.</h1>
													<div class="action">
														<div class="button button-green" onclick="addFieldRule(null,null,'field');showFieldRules();"><span class="add"></span>Create a Field Rule</div>
													</div>
												</div>
												<div id="fieldRulesWithSave" style="display:none">
													<div id="fieldRules">
														<g:formRemote name="fieldRuleForm" update="forRuleUpdate" url="[ controller: 'formAdmin', action:'updateFieldRules' ]" before="loadScreenBlock();saveFieldRules('field')" onSuccess="updateVersion(data);hideScreenBlock();" onFailure="">
															<input type="hidden" name="id" value="${formAdminInstance?.id}">
															<input type="hidden" name="fieldRulesData" id="fieldRulesData" value="">
															<input type="submit" id="fieldRuleFormButton" style="display:none;">
														</g:formRemote>
														<table class="fieldRuleTable">
															<tbody class="fieldRuleTbody">
															</tbody>
														</table>
													</div>
													<div class="action">
														<div class="button button-green" style="margin-top:5px;" onclick="$('#fieldRuleFormButton').trigger('click');">Save Field Rules</div>
													</div>
												</div>
											</div>
										</div>
										<div class="panes clearfix allTypeRules">
											<div style="padding:10px;border-bottom:1px solid #aaa">
												<div id="noPageRule" style="${!formAdminInstance?.pageRulesData?.isEmpty()?'display:none;':''}">
													<h1 style="color:#333">There are no page rules.</h1>
													<div class="action">
														<div class="button button-green" onclick="addFieldRule(null,null,'page');showPageRules();"><span class="add"></span>Create a Page Rule</div>
													</div>
												</div>
												<div id="pageRulesWithSave" style="${!formAdminInstance?.pageRulesData?.isEmpty()?'':'display:none;'}">
													<script>
														var pageRulesData = ${formAdminInstance?.pageRulesData?JSON.parse(formAdminInstance?.pageRulesData):'null'};
													</script>
													<div id="pageRules">
														<g:formRemote name="pageRuleForm" update="forRuleUpdate" url="[ controller: 'formAdmin', action:'updatePageRules' ]" before="loadScreenBlock();saveFieldRules('page')" onSuccess="updateVersion(data);hideScreenBlock()" onFailure="">
															<input type="hidden" name="id" value="${formAdminInstance?.id}">
															<input type="hidden" name="pageRulesData" id="pageRulesData">
															<input type="submit" id="pageRuleFormButton" style="display:none;">
														</g:formRemote>
														<table class="pageRuleTable">
															<tbody class="pageRuleTbody">
															</tbody>
														</table>
													</div>
													<div class="action">
														<div class="button button-green" style="margin-top:5px;" onclick="$('#pageRuleFormButton').trigger('click')">Save Page Rules</div>
													</div>
												</div>
											</div>
										</div>
									</div>
									<script>
										var fieldRulesData = ${formAdminInstance?.fieldRulesData?JSON.parse(formAdminInstance?.fieldRulesData):'null'};
										addAllRules(fieldRulesData,'field');
										var pageRulesData = ${formAdminInstance?.pageRulesData?JSON.parse(formAdminInstance?.pageRulesData):'null'};
										addAllRules(pageRulesData,'page');
									</script>
								</section>
							</div>
							
							<%--Integration tab--%>
							<div class="panes clearfix">
								<section>
									<g:if test="${!(formAdminInstance?.webHookDetails?.id)}">
										<%
											def listValues = ["Mail Chimp","Web Hook"]
										%>
										<div id="webHookDivMailChimpDD">
											<table>
												<tbody>
													<tr class="prop">
														<td valign="top" class="name">
															<label for="abc"><g:message code="formAdmin.integration.type" default="Integration Type" /> <em>*</em></label>
														</td>
														<td valign="top" class="value">
															<g:select name="abc" from="${listValues}" noSelection="['':'Select Integration Type']" value="${(webHookDetailsView)?('Web Hook'):((formAdminInstance?.mailChimpDetails)?'Mail Chimp':'')}" onchange="integrationShowHide(this)" style="float:none;"/>
														</td>
													</tr>
												</tbody>
											</table>
										</div>
									</g:if>
									<div id="webHookDiv" ${(webHookDetailsView || formAdminInstance?.webHookDetails?.id)?'':'style="display:none;"'}>
										<div>
											<g:form method="post" >
												<g:hiddenField name="id" value="${formAdminInstance?.id}" />
												<table>
													<tbody>
														<tr class="prop">
															<td valign="top" class="name">
																<label for="webHookDetails.url"><g:message code="formAdmin.webHookDetails.url" default="Your WebHook URL" /> <em>*</em></label>
															</td>
															<td valign="top" class="value ${hasErrors(bean: webHookDetails?:formAdminInstance?.webHookDetails, field: 'url', 'errors')}">
																<g:textField name="webHookDetails.url" value="${(webHookDetails?:formAdminInstance?.webHookDetails)?.url}" />
															</td>
														</tr>
														<%--<tr class="prop">
															<td valign="top" class="name">
																<label for="webHookDetails.handShakeKey"><g:message code="formAdmin.webHookDetails.handShakeKey" default="Your WebHook HandShake Key" /></label>
															</td>
															<td valign="top" class="value ${hasErrors(bean: webHookDetails?:formAdminInstance?.webHookDetails, field: 'handShakeKey', 'errors')}">
																<g:textField name="webHookDetails.handShakeKey" value="${(webHookDetails?:formAdminInstance?.webHookDetails)?.handShakeKey}" />
															</td>
														</tr>
														--%><tr class="prop">
															<td valign="top" class="name">
																<label for="webHookDetails.includeFieldAndForm"><g:message code="formAdmin.webHookDetails.includeFieldAndForm" default="Include Field and Form Structures with Entry Data" /></label>
															</td>
															<td valign="top" class="value ${hasErrors(bean: webHookDetails?:formAdminInstance?.webHookDetails, field: 'includeFieldAndForm', 'errors')}">
																<g:checkBox name="webHookDetails.includeFieldAndForm" value="${(webHookDetails?:formAdminInstance?.webHookDetails)?.includeFieldAndForm}" />
															</td>
														</tr>
													</tbody>
												</table>
												<div class="action">
													<g:if test="${(webHookDetails?:formAdminInstance?.webHookDetails)?.id }">
														<g:hiddenField name="webHookDetails.id" value="${(webHookDetails?:formAdminInstance.webHookDetails)?.id}" />
														<g:hiddenField name="webHookDetails.version" value="${(webHookDetails?:formAdminInstance.webHookDetails).version}" />
														<g:actionSubmit class="button button-green" action="saveW" style="width: 120px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
														<g:actionSubmit class="button button-red" action="deleteW" style="width: 120px" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('Are you sure?')"/>
													</g:if>
													<g:else>
														<g:actionSubmit class="button button-green" action="saveW" value="${message(code: 'default.button.create.label', default: 'Create')}" />
													</g:else>
												</div>
											</g:form>
										</div>
									</div>
									<div id="mailChimpDiv" ${formAdminInstance?.mailChimpDetails?'':'style="display:none;"'}>
										<g:formRemote name="mailChimpDetailsForm" before="if(!v())return false"
											url="[ controller: 'formAdmin', action:'saveMailChimpFieldSettings' ]" onSuccess="successMessageHere('x')" onFailure="errorMessageHere()">
											<div>
											<div id="apikeyAndList">
											 <g:hiddenField name="id" value="${formAdminInstance?.id}" />
											<table>
												<tbody>
													<tr class="prop" id="mailchimapikeytr" >
														<td valign="top" class="name">
															<label for="mailChimpDetails.apiKey"><g:message code="formAdmin.mailChimpDetails.apiKey" default="Your MailChimp API Key" /> <em>*</em></label>
														</td>
														<td valign="top" class="value">
															<g:textField id="mailChimpDetailsapiKey" name="mailChimpDetailsapiKey" autocomplete="off" placeholder="Please enter your API-Key" value="${formAdminInstance?.mailChimpDetails?.apikey}" /><input type="button" class="button button-green" value="Next" onclick="getMailChimpList('${request.getContextPath()}/formAdmin/findMailChimpListByAjax?userapikey=','#mailChimpDetailsapiKey')" style="width: 47px;height: 25px">
														<div id="apiKeyLoadingimgdiv" style=" background:#fff;opacity:0.7;display:none;width:100%;height:100%;position:absolute;top:0;left:0;"><img style="margin:auto;position:absolute;top:0;bottom:0;left:0;right:0;" id="apiKeyLoadingimg" src="${request.getContextPath()}/images/ajax-loader.gif"></div>
														</td>
													</tr>
													<tr class="prop" style="display: none;" id="mailChimpDetailsLists">
														<td valign="top" class="name" >
															<label for="mailChimpDetails.List"><g:message code="formAdmin.mailChimpDetails.List" default="Select your MailChimp List " /></label>
														</td>
														<td valign="top" class="value ">
														<%try{ %>
													  	    <g:hiddenField name="mailChimpDetailsListName" value="${JSON.parse(formAdminInstance?.mailChimpDetails?.listId)?.value}" />
															<%
															  }catch(Exception e){  %>
															  <g:hiddenField name="mailChimpDetailsListName" value="" />
															  
															 	<% }
													  		%>
															<select id="mailChimpDetailsList" name="mailChimpDetailsList" ></select>
															
														</td>
													</tr>
													<tr class="prop" style="display: none;" id="sendChoicetr">
														<td valign="top" class="name" >
															<label for="sendChoice"><g:message code="formAdmin.sendChoice.List" default="Only Send if Choice is Checked" /></label>
														</td>
														<td valign="top" class="value ">
															<select id="sendChoice" name="sendChoice"  >
															</select>
															
														</td>
													</tr>
													<tr class="prop" id="optin" style="display: none;">
														<td valign="top" class="name">
															<label for="optinemail"><g:message code="optinemail" default="Send Opt-In Email" /></label>
														</td>
														<td valign="top" class="value">
															<g:checkBox name="optinemail" value="${formAdminInstance?.mailChimpDetails?.optinemail}"/>
														</td>
													</tr>
													<tr class="prop" id="mailchimpNextbutton" style="display: none;">
														<td colspan="2"><div class="action"><input type="button" class="button button-gray" value="Back" onclick="hideAndback()" style="width:100px;"><input type="button" class="button button-gray" value="Next" onclick="hideAndNext('${request.getContextPath()}/formAdmin/findAllfieldsByAjax')" style="width:100px;"></div></td>
													</tr>
												</tbody>
											</table>
											</div>
											<div id="matchfields" style="display: none;">
											
											</div>
											<div id=buttonDiv style="display: none;">
											<table>
											<tr class="prop">
												<td><div class="action"><input type="button"class="button button-gray" value="Back" onclick="backButton()" style="width: 100px;"> <g:submitButton  name="Save" class="button button-green" style="width: 100px;"/></div></td>
											</tr>
											</table>
											</div>
											<div id="onsuccess" style="display: none;" >
										  <table>
										  	<tr class="prop"><td class="name"><label for="mailChimpDetails.apiKey.value" id="sendEnrty">Send All new entries to list</label></td>
										  		<%try{
													  def fl
													 if(formAdminInstance?.mailChimpDetails?.sendChoice!="all"){
														def fn = formAdminInstance?.mailChimpDetails?.sendChoice.substring(0,formAdminInstance?.mailChimpDetails?.sendChoice.indexOf("_"))
														
														if(fn){
															def f = formAdminInstance.form.fieldsList.find{it.name==fn}
															if(f){
																fl = "if "+JSON.parse(f.settings).en.label+
																   " ("+formAdminInstance?.mailChimpDetails?.sendChoice.substring(formAdminInstance?.mailChimpDetails?.sendChoice.indexOf("_")+1,formAdminInstance?.mailChimpDetails?.sendChoice.length())+
																    ") is checked. Send entries to list"
															}
														}
										  			}else{
														fl ="Send All new entries to list"
													 }
												%>
												<g:hiddenField name="sendChoiceType" value="${fl}"/>
										  	    <td class="value labelValue"id="listName"><g:message code="formAdmin.mailChimpDetails.list.name" default="${JSON.parse(formAdminInstance?.mailChimpDetails?.listId)?.value} "/></td>
										  		<%}catch(Exception e){%>
												  <td class="value labelValue"id="listName"><g:message code="formAdmin.mailChimpDetails.list.name" default=" "/></td>
												<%}%>
										  	</tr>
										  	<tr class="prop"><td class="name"><label for="mailChimpDetails.optinmail.value">Double Opt-In Email</label></td>
										  	   	<g:if test="${formAdminInstance?.mailChimpDetails?.optinemail}">
										  	    	<td class="name labelValue" id="optin1">Send double opt-in email.</td>
										  	    </g:if>
										  	    <g:else>
										  	    	<td class="name labelValue" id="optin2"><g:message code="formAdmin.mailChimpDetails.optin" default="Do not send a double opt-in email."/></td>
												</g:else>									  	
										  	</tr>
	
										  	<tr class="prop">
										  	    <td colspan="2"><div class="action"><input type="button" class="button button button-blue" value="Edit" onclick="editMailChimp()" style="width: 80px;"><input type="button" class="button button button-red" value="Delete" onclick="deleteMailchimp('${request.getContextPath()}/formAdmin/deleteMailChimpFieldSettings')" style="width: 80px;"></div></td>
										  	</tr>
										  </table>
									</div>
									</div>
									</g:formRemote>
									</div>
								</section>
							</div>
				
						</div>
					</div>
				</section>
			</div>
		</section>
		<script>
		$(document).ready(function(){
			if(${mailChimpDetails?true:false}){
				successMessageHere();
				$('#apikeyAndList').hide();
				$('#sendEnrty').html($('#sendChoiceType').val())
			}
			$("[name='formSubmissionAction']").trigger('change');
		 });
		function editMailChimp(){
			$('#onsuccess').hide()
			$('#apikeyAndList').show()
			hideAndback()
		}
				function openSubscriptionList(){
				loadScreenBlock()
				window.location = "${createLink(controller:'form',action:'edit', id:formAdminInstance?.form?.id )}"
			}

			$("[name='formSubmissionAction']").change(function() {
				showHideFormEmailFields();
			});

			function showHideFormEmailFields(){
				if($("[name='formSubmissionAction']").val().toLowerCase() == "feed"){
					$('.formEmailFields').hide();
					$('.formEmailFieldsTD').css('visibility','hidden');
				}else if($("[name='formSubmissionAction']").val().toLowerCase() == "email"){
					$('.formEmailFields').show();
					$('.formEmailFieldsTD').css('visibility','visible');
				}else{
					$('.formEmailFields').hide();
					$('.formEmailFieldsTD').css('visibility','hidden');
				}
			}
			function getMailChimpList(urL,value){
				var val=$(value).val()
				if(val==""||val==null){}else{
					$('option','#mailChimpDetailsList').remove();
					$('option','#sendChoice').remove();
					$('#apiKeyLoadingimgdiv').show();
					$.ajax({ 
						url: urL+val+'&id='+$("#id").val(),
				        type: "GET",
				        dataType: "json",
				        success: function(data) {
				        	var mailChimpDetailsList=$("#mailChimpDetailsList");
				        	 $.each(data.map, function(key, val) {
				        		 var id=val.id
					        	 var name=val.name
				        		 var opt = $('<option value="'+id+'">'+name+'</option>').appendTo(mailChimpDetailsList)
				        	 });
				        	 var objSendChoice=$('#sendChoice')
				        	 var opt = $('<option value="all">Send all new entries</option>').appendTo(objSendChoice)
				        		getData(data.formFields.CheckBox,objSendChoice,false)
				        	 $('#apiKeyLoadingimgdiv').hide();
				        	 $('#mailChimpDetailsLists').show()
				        	 $('#sendChoicetr').show()
				        	 $('#optin').show()
				        	 $('#mailchimpNextbutton').show()
				        	 $('#mailchimapikeytr').hide()
					        },
						error: function(){
							 $('#apiKeyLoadingimgdiv').hide();
							window.alert('Error! Please check your API Key');
						}
			        });
		        }
		       }
			function hideAndNext(urL){
				if($("#mailChimpDetailsList").val()==null){
					alert('Please select a list')
					}else{
						$("#mailChimpDetailsListName").val($("#mailChimpDetailsList option:selected").text())
						$('#spinner').show();
						$.ajax({ 
							url: urL+'?mailChimpDetailsList='+$("#mailChimpDetailsList").val()+'&userapikey='+$("#mailChimpDetailsapiKey").val()+'&id='+$("#id").val(),
					        type: "GET",
					        dataType: "json",
					        success: function(data) {
					        	$('#matchfields').children().remove();
					        	var matchFieldDiv=$('#matchfields')
					        	var table = $('<table></table>').appendTo(matchFieldDiv)
					        	var tbody=$('<tbody></tbody>').appendTo(table)
					        	 $.each(data.mailChimpFields, function(key, val) {
						         if(val!=null){
						        	var tr=$('<tr class="prop" style="height: 20px;"></tr>').appendTo(tbody)
						        	var	tdLeft=$('<td  valign="top" ></td>').appendTo(tr)
							        var label=$('<label >'+val.name+'&nbsp;&nbsp;&nbsp;</label>').appendTo(tdLeft)
							        if(val.req){
								        $('<font color="red">*</font>').appendTo(label)
								        }					        
						        	var	tdCenter=$('<td  valign="top"style="width:60px" >('+val.fieldType+')</td>').appendTo(tr)
							        var	tdRight=$('<td></td>').appendTo(tr)
							        if(val.req){
							        	var	tdRightSelect=$('<Select id="'+val.tag+'" name="m.r.'+val.tag+'" class="required" ></Select>').appendTo(tdRight)
							        	}else{
							        	var	tdRightSelect=$('<Select id="'+val.tag+'" name="m.nr.'+val.tag+'" ></Select>').appendTo(tdRight)    
							        	}	
						        	var optionblank = $('<option value=""></option>').appendTo(tdRightSelect)
							        if(val.fieldType=='text'){
							        	getData(data.formFields.text,tdRightSelect,true)
							        	getData(data.formFields.email,tdRightSelect,true)
							        	getData(data.formFields.dropdown,tdRightSelect,true)
							        	getData(data.formFields.radio,tdRightSelect,true)
							        }
							        else if(val.fieldType=='number'){
							        	getData(data.formFields.number,tdRightSelect,true)
							        }
							        else if(val.fieldType=='date'||val.fieldType=='birthday'){
							        	getData(data.formFields.date,tdRightSelect,true)
							        }
						        	else if(val.fieldType=='dropdown'){
						        		getData(data.formFields.dropdown,tdRightSelect,true)
							        }
						        	else if(val.fieldType=='email'){
						        		getData(data.formFields.email,tdRightSelect,true)
							        }
						        	else if(val.fieldType=='radio'){
						        		getData(data.formFields.radio,tdRightSelect,true)
							        }
						         }
					         	});
					        	var table1 = $('<table></table>').appendTo(matchFieldDiv)
					        	var tbody1=$('<tbody></tbody>').appendTo(table1)
					        	$.each(data.mailChimpgroups, function(key, val) {
					        		$.each(val.groups,function(kc,vc){
					        			var tr1=$('<tr class="prop" style="height: 20px;"></tr>').appendTo(tbody1)
						        		var tdLeft1=$('<td  valign="top" ></td>').appendTo(tr1)
						        		var label1=$('<label >'+val.name+'-'+vc.name+'</label>').appendTo(tdLeft1)
						        		var	tdCenter=$('<td  valign="top" style="width:60px" >&nbsp;</td>').appendTo(tr1)
						        		var tdright1=$('<td  valign="top" ></td>').appendTo(tr1)
						        		var	tdRightSelect=$('<Select name="m.group.'+val.name+'_'+vc.name+'"></Select>').appendTo(tdright1)
						        		$('<option></option>').appendTo(tdRightSelect)
						        		getData(data.formFields.checkbox,tdRightSelect,false)
						        	});
						        });
					        	$('#spinner').hide();
					        	$('#buttonDiv').show();
								$('#apikeyAndList').hide();
								$('#matchfields').show();
					        	 
						      },
						      error: function(){
						    	  $('#spinner').hide();
									window.alert('Error !');
								}
				        });
					}
			   }
			function backButton(){
				  $('#spinner').show();
					$('#matchfields').hide();
					$('#buttonDiv').hide();
					$('#apikeyAndList').show();
				 	$('#spinner').hide();
				}
			function hideAndback(){
				$('#mailchimapikeytr').show();
				$('#mailChimpDetailsLists').hide();
				$('#sendChoicetr').hide()
				$('#optin').hide();
				$('#mailchimpNextbutton').hide()
				}
			function getData(thisData,selectObj ,thisval){
				if(thisData!=null){
					$.each(thisData, function(k, v){
			        	//alert(v.label+""+v.value)
			        	if(thisval){
		        			var opt = $('<option value="'+v.id+'">'+v.label+'</option>').appendTo(selectObj)
			        	}else{
			        		$.each(v.value, function(ki,vi){
			        			var opt = $('<option value="'+v.id+'_'+vi+'">'+v.label+' ('+vi+')'+'</option>').appendTo(selectObj)
			        		});
			        	}
					});
			    }
			}
			function v(){
				var rVal = true;
				$('.required').each(function(){
					if($(this).val()=="" || $(this).val()==null){
						rVal = false;
						alert("Oops! You didn't map a required field.")
						setTimeout("hideS()",10);
						return false;
					}
				});
				return rVal;
			}
			function successMessageHere(){
				hideS();
				if(arguments.length>0){
					$("#successMessageHere").html("${message(code:'formAdmin.mailChimpDetails.saved',default:'Mail Chimp Setttings saved successfully')}")
					$("#successMessageHere").show();
				}
				$('#listName').html($('#mailChimpDetailsListName').val())
				if($("#sendChoice option:selected").val()=="all"){
					$('#sendEnrty').html("Send All new entries to list");
					}
				else{
					$('#sendEnrty').html("If "+$("#sendChoice option:selected").text()+" is checked. Send entries to list")
					}
				if(document.getElementById('optinemail').checked == true){
					$('#optin1').html("Send double opt-in email.")
					$('#optin2').html("Send double opt-in email.")
				}else{
					$('#optin1').html("Do not send a double opt-in email.")
					$('#optin2').html("Do not send a double opt-in email.")
				}
				$('#matchfields').hide();
				$('#buttonDiv').hide();
				$('#onsuccess').show()
				$("#webHookDivMailChimpDD").hide();
				$(".errors").hide();
			}
			function errorMessageHere(){
				hideS();
				backButton();
			}
			function hideS(){$('#spinner').hide();}
			function deleteMailchimp(urL){
				var conf=confirm('Are you sure ?')
				if(conf){
					$('#spinner').show();
					$.ajax({ 
						url: urL+'?id='+$("#id").val(),
				        type: "GET",
				        dataType: "json",
				        success: function(data) {
				        	$('#spinner').hide();
				        	 editMailChimp();
				        	 $('#mailChimpDetailsapiKey').val("")
					        $("#successMessageHere").html("Mailchimp settings deleted")
							$("#successMessageHere").show();
					        $("#webHookDivMailChimpDD").show();
				        },
					});
				}
			}
			function integrationShowHide(dd){
				if(dd.value == "Web Hook"){
					$("#webHookDiv").show();
					$("#mailChimpDiv").hide();
				}else if(dd.value == "Mail Chimp"){
					$("#webHookDiv").hide();
					$("#mailChimpDiv").show();
				}else{
					$("#webHookDiv").hide();
					$("#mailChimpDiv").hide();
				}
			}
		 </script>
		 </g:if>
	</body>
</html>