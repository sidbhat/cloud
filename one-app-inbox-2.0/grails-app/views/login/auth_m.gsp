%{----------------------------------------------------------------------------
  - [ NIKKISHI CONFIDENTIAL ]                                                -
  -                                                                          -
  -    Copyright (c) 2011.  Nikkishi LLC                                     -
  -    All Rights Reserved.                                                  -
  -                                                                          -
  -   NOTICE:  All information contained herein is, and remains              -
  -   the property of Nikkishi LLC and its suppliers,                        -
  -   if any.  The intellectual and technical concepts contained             -
  -   herein are proprietary to Nikkishi LLC and its                         -
  -   suppliers and may be covered by U.S. and Foreign Patents,              -
  -   patents in process, and are protected by trade secret or copyright law.
  -   Dissemination of this information or reproduction of this material     -
  -   is strictly forbidden unless prior written permission is obtained      -
  -   from Nikkishi LLC.                                                     -
  ----------------------------------------------------------------------------}%

<!DOCTYPE html>
<html lang="en">
<head>
    <title>Form Builder Mobile</title>
	<meta name="layout" content="mobile"/>
</head>
<body class="login">
    	<div data-role="header" data-position="inline">
			<h1>Form Builder</h1>
		</div>
		<div data-role="content">
		  <form id="loginForm" name="loginForm" action="${postUrl}" method="post" data-ajax="false">
				<div data-role="fieldcontain">
					<label for="userName" class="ui-input-text"><g:message code="username.label" default="Username" /></label>
					<input type="text" name='j_username' id='username' value="" name="username"
                       required="required" autocorrect="off" autocapitalize="off" placeholder="Username" style="background: #fff;"/></br>
		
					<label for="password" class="ui-input-text"><g:message code="password.label" default="Password" /></label>
					 <input type="password" name='j_password' id='password'  value="" name="password"
                       required="required" placeholder="Password" style="background: #fff;"/>
				</div>
				<table>
				<tr>
					<td>	
						Sign In using  
					</td>
				 	<td>
				 		  <a href="${request.getContextPath()}/google/googleOauth" rel=external><img src="${request.getContextPath()}/images/google_app.png" style="vertical-align:middle;margin-top:2px;"/></a>
				 	</td>
				 	<td>
				 		| <a href="${request.getContextPath()}/google/googleOauth" rel=external><img src="${request.getContextPath()}/images/gmail_logo.png" style="vertical-align:middle;" height="12px" /></a>
				 	</td>
				</tr>
			</table>
			<button class="button button-green fr loginButton" type="submit">Login</button>	
		</form>
	</div>
</body>
</html>




