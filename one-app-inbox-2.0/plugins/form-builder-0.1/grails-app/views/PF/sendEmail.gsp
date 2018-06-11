<%@ page import="grails.converters.JSON" %>
<%@ page import="org.grails.formbuilder.FormBuilderConstants" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <link rel="stylesheet" media="screen" href="${request.getContextPath()}/css/forms_min.css"/>
        <script src="${request.getContextPath()}/js/jquery.tools.min.js"></script>
        <style>
	        body,td {
				font: 13px/1.231 arial,helvetica,clean,sans-serif;
			}
        	.panes section{
        		display:block;
        	}
        	.email-label{
        		text-align:right;
        		/*font-weight:bold;*/
        		width:16%;
        	}
        	#to-field{
        		width:84%;
        	}
        	.tabs li a{
        		font-size:100%;
        	}
        	#mail-fields-layout{
        		width:100%;
        	}
        	div.errors {
				background: #FFF3F3;
				border: 1px solid red;
				color: #C00;
				margin: 10px 0 5px 0;
				padding: 5px 0 5px 0;
			}
        	div.errors li {
				background: url(../images/skin/exclamation.png) 8px 0% no-repeat;
				line-height: 16px;
				padding-left: 30px;
				list-style: none;
			}
			.message {
				background: #F3F8FC url(../images/skin/information.png) 8px 50% no-repeat;
				border: 1px solid #B2D1FF;
				color: #006DBA;
				margin: 10px 0 5px 0;
				padding: 5px 5px 5px 30px;
			}
			.buttons {
				background: white url(../images/skin/shadow.jpg) bottom repeat-x;
				color: #666;
				font-size: 10px;
				overflow: hidden;
				border: 0;
				padding: 5px 1em;
				margin: 0;
			}
			.button-blue {
				background: #0095CD;
				background: -webkit-gradient(linear,left top,left bottom,from(#00ADEE),to(#0078A5));
				background: -moz-linear-gradient(top,#00ADEE,#0078A5);
				-pie-background: linear-gradient(top,#00adee,#0078a5);
				border: 1px solid #034462;
				color: white;
				text-shadow: 0 1px 1px rgba(0, 0, 0, 0.25);
				width: 100px !important;
				height: 36px;
			}
			.button {
				-moz-border-radius: 4px;
				-webkit-border-radius: 4px;
				-khtml-border-radius: 4px;
				border-radius: 4px;
				-moz-box-shadow: inset 1px 1px 0 rgba(255,255,255,0.3);
				-webkit-box-shadow: inset 1px 1px 0 rgba(255, 255, 255, 0.3);
				box-shadow: inset 1px 1px 0 rgba(255, 255, 255, 0.3);
				cursor: pointer;
				display: inline-block;
				font: 12px/100% 'Lucida Grande','Lucida Sans Unicode','Helvetica Neue',Helvetica,Arial,Verdana,sans-serif;
				padding: 4px 10px;
				outline: none!important;
				text-align: center;
				text-decoration: none;
				position: relative;
				-moz-box-sizing: border-box!important;
				line-height: 16px;
			}
        </style>
        <script>
        	function loadScreenBlock(){
        		document.getElementById("spinner").style.display = "block";//$("#spinner").show();
            }
	        $(document).ready(function(){
	            $(".tabs").tabs(".panes section",{initialIndex:0});
	        });
        </script>
    </head>
    <body style="background-color:white;">
    	<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;z-index: 10000;background-color: #fff;opacity:0.5;">
		  <img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
		</div>
		<section class="container_6 clearfix">
        	<div style="margin: 0; width: auto;"  class="tabbed-pane">
            <ul style="padding: 0;" class="tabs">
                <li><a href="#">Send Email </a></li>
            </ul>
            <div style="margin-bottom: 5px;" class="panes clearfix">
            	<section>
            		<g:if test="${!noForm}">
            			<g:if test="${emailToInvalidList }">
            				<div class="errors">
            					<li><g:message code="email.embed.form.invalidEmailTo" args="[emailToInvalidList as JSON]" default="Invalid Email-Id(s): ${emailToInvalidList as JSON}"/></li>
            				</div>
            			</g:if>
            			<g:if test="${errorMessage }">
            				<div class="errors">
            					<li>${errorMessage }</li>
            				</div>
            			</g:if>
            			<g:if test="${showMessage }">
            				<div class="message">${showMessage }</div>
            			</g:if>
	            		<g:form action="sendEmail" onsubmit="loadScreenBlock()">
	            			<g:hiddenField name="formId" value="${params.formId}"/>
							<table id="mail-fields-layout">
								<tbody>
									<tr>
										<td valign="top" class="email-label">To:</td>
										<td><g:textArea id="to-field" name="emailTo" style="width:100%;" value="${params.emailTo}"></g:textArea></td>
									</tr>
									<tr>
										<td class="email-label">From:</td>
										<td><input id="subject-input" name=emailFrom type="text"
											value="${params.emailFrom?:emailFrom}" style="text-align: left;width:100%;" dir="ltr"></td>
									</tr>
									<tr>
										<td class="email-label">Subject:</td>
										<td><input id="subject-input" name="emailSubject" type="text"
											value="${params.emailSubject?:formName }" style="text-align: left;width:100%;" dir="ltr"></td>
									</tr>
									<tr>
										<td valign="top" class="email-label">Body:</td>
										<td><g:textArea id="body-field" name="emailBody" rows="5" style="width:100%;" value="${params.emailBody }"></g:textArea></td>
									</tr>
									<tr>
										<td></td>
										<td><g:checkBox name="includeForm" id="include-form-checkbox" checked="${params.includeForm?true:false}"
											/> <label for="include-form-checkbox">Include
												form in the email</label></td>
									</tr>
									<tr>
										<td></td>
										<td><g:message code="email.form.embed.note" args="[]" default="Note: Form will be embeded at the end of email body"/></td>
									</tr>
									<tr>
										<td></td>
										<td><input class="button button-blue" type="submit" value="Send"/></td>
									</tr>
								</tbody>
							</table>
						</g:form>
					</g:if>
					<g:else>
						${noForm}
					</g:else>
				</section>
	        </div>
	        </div>
        </section>
    </body>
</html>
