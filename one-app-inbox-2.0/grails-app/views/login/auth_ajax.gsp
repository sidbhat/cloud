<!DOCTYPE html>
<%@page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils"%>
<html lang="en">
<head>
	<meta name="layout" content="loginAjax" />
      <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
      	<link rel="stylesheet" media="screen" href="${request.getContextPath()}/css/forms_min.css"/>
        <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'messages.css')}"/>
        <script src="${request.getContextPath()}/js/jquery.tools.min.js"></script>
      <style>
	        body {
				font: 13px/1.231 arial,helvetica,clean,sans-serif;
				background-color:white;
				margin:5px;
			}
        	.panes section{
        		display:block;
        	}
        	.tabs li a{
        		font-size:100%;
        	}
        	.error {
				color: #C30!important;
				font-weight: normal;
				margin-bottom: 12px;
			}
			p{
				color: #676767;
				margin: 0;
			}
        </style>
      <script>
	        $(document).ready(function(){
	            $(".tabs").tabs(".panes section",{initialIndex:${showFieldValues!=null?1:0}});
	            var loginForm = $("#LoginForm");
	            var count = 0;
	            $("button",loginForm).click(function(){
		            $(".errorMessage").hide();
	            	loadScreenBlock();
		            $.ajax({
		            	url: loginForm.attr("action"),
		        		type: "POST",
		        		data: loginForm.serialize(),
		        		dataType: 'json',
		        		success: function(data){
			        		if(data.success){
			        			window.parent.userLoggedInSubmitForm();
				        	}else{
				        		$(".errorMessage").html(data.error);
				        		$(".errorMessage").show();
					        }
			        		hideScreenBlock();
			        		count = 0;
			        	},
			        	error:function(jqXHR, textStatus, errorThrown){
			        		console.log(textStatus, errorThrown);
				        	if(count>1){
				        		$(".errorMessage").html("Connection problem");
				        		$(".errorMessage").show();
				        		hideScreenBlock();
				        		count = 0;
					        }else{
					        	count++;
					        	$("button",loginForm).trigger("click");
						    }
				        }
			        });
		        });
	        });
	        function loadScreenBlock(){
        		document.getElementById("spinner").style.display = "block";//$("#spinner").show();
            }
	        function hideScreenBlock(){
        		document.getElementById("spinner").style.display = "none";//$("#spinner").show();
            }
        </script>
</head>

<body class="login">
<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;z-index: 10000;background-color: #fff;opacity:0.5;">
  <DIV style="Z-INDEX: 10000; POSITION: fixed; TEXT-ALIGN: center; BACKGROUND-COLOR: rgb(255,255,255); WIDTH: 100%; HEIGHT: 100%; VERTICAL-ALIGN: middle" id=spinner class=spinner>
<TABLE width="100%" height="100%">
<TBODY>
<TR>
<TD style="TEXT-ALIGN: center" vAlign=middle align=center>
<img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
</TD></TR></TBODY></TABLE></DIV>
</div>
	<div class="message info ac">
         <h3><g:message code="menu.welcome.message" default="Welcome to Form Builder"/></h3>

         <p><g:message code="menu.welcome.description" default="Form Builder lets your collect, analyze and collaborate on any kind of information."/> </p>
     </div>
     <div style="margin: 30px 0 0 0; width: auto;"  class="tabbed-pane">
     	<ul style="padding: 0;" class="tabs">
            <li><a href="#">Login</a></li>
            <li><a href="#">Register</a></li>
        </ul>
        <div style="" class="panes clearfix">
            <section>
            	<div>
	     			<div class="login-box main-content" style="left:0;margin:0px 10px 0px !important;padding:15px 30px;position: relative !important;box-shadow:0 1px 5px 0 #4A4A4A;border-radius:5px;">
				        <div class='error errorMessage' style="display:none;"></div>
				
				
				        <div class="message info" style="width: 92%;">Enter your username and password</div>
				
				        <form id="LoginForm" action="${request.contextPath}${org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils.securityConfig.apf.filterProcessesUrl}" method="post" class="clearfix noLoader" style="border-bottom: 1px solid #DDD;margin-bottom:5px;">
				            <p>
				                <input type="text" name='j_username' id='username' class="full" value="" name="username" style="margin:2px 0;"
				                       required="required" autocorrect="off" autocapitalize="off" placeholder="Username"/>
				            </p>
				            <p>
				                <input type="password" name='j_password' id='password' class="full" value="" name="password" style="margin:2px 0;"
				                       required="required" placeholder="Password"/>
				            </p>
				            <p class="clearfix">
				           	 	<button class="button button-green fl" type="button" style="margin:2px 0;">Login</button>
				                <span style="margin:0 25px;vertical-align: sub;">
				                    <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
				                           <g:if test='${hasCookie}'>checked='checked'</g:if>/>
				                    <label class="choice" for="remember">Remember me</label>
				                </span>
				            </p>
				            <p>&nbsp;</p>
				            <p style="margin-bottom:5px;"><g:link controller="register" action="forgotPassword" style="color:#CC3300 !important;font-weight:normal;" target="_blank">Forgot Password?</g:link> | <g:link controller="register" action="changePassword" style="color:#CC3300 !important;font-weight:normal;" target="_blank">Reset Your Password</g:link></p>
				        </form>
				        <p style="font-weight:bold">Sign in using</p>
				        <p style="padding-top:5px;"><g:link controller="register" action="googleAppLogin" target="_blank"><img src="${request.getContextPath()}/images/google_app.png" style="vertical-align:middle;margin-top:2px;margin-right:10px;"/></g:link> | <g:link controller="openid" action="openid" params="[hd:'gmail.com']" target="_blank"><img src="${request.getContextPath()}/images/gmail_logo.gif" style="vertical-align:middle;margin-left:10px;" height="12px"/></g:link></p>
					    <!-- end-->
					</div>
				</div>
			</section>
		</div>
		<div style="" class="panes clearfix">
            <section>
				<div class="login-box main-content" style="left:0;margin:0px 10px 0px !important;padding:15px 30px;position: relative !important;box-shadow:0 1px 5px 0 #4A4A4A;border-radius:5px;">
						<g:form action='register' controller="register" name="register"
							autocomplete='off'>
							<g:hiddenField name="fromFormTemplate" value="true"/>

							<g:if test="${flash.message}">
								<div class="error">
									<g:message code="${flash.message}" args="${flash.args}"
										default="${flash.defaultMessage}" />
								</div>
							</g:if>
							
							<div class="message info" style="width: 92%;">Enter your details</div>

							<table width="100%">
								<tr class="prop">
									<td valign="top" class="name" style="vertical-align: middle;"><label
										for="j_username"><g:message
												code='spring.security.ui.forgotPassword.username' /></label></td>
									<td valign="top" class="name"><g:textField
											name="j_username"
											value="${showFieldValues?params.j_username:''}" size="25"
											placeholder="E.g.myname@companyname.com" style="width: 92%;"/></td>
								</tr>
								<tr class="prop">
									<td valign="top" class="name" style="vertical-align: middle;"><label
										for="j_company"><g:message
												code='register.signup.company' /></label></td>
									<td valign="top" class="name"><g:textField
											name="j_company" size="25"
											value="${showFieldValues?params.j_company:''}"
											placeholder="E.g. COMPANYNAME.COM"  style="width: 92%;"/></td>
								</tr>
								<tr class="prop">
									<td valign="top" class="name" style="vertical-align: middle;"><label
										for="j_mobile"><g:message
												code='register.signup.mobile' /></label></td>
									<td valign="top" class="name"><g:textField name="j_mobile"
											size="25" value="${showFieldValues?params.j_mobile:''}"
											placeholder="Phone number"  style="width: 92%;"/></td>
								</tr>
							</table>
							<br />
							<div class="action">
								<g:submitButton name="submit" value="Sign Up"
									class="button button-green fr" style="margin-right:10px;"
									type="submit"></g:submitButton>
							</div>

						</g:form>
				</div>
			</section>
		</div>
	</div>
</body>
</html>