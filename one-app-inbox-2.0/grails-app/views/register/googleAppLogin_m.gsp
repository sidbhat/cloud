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
    	<a data-icon="home" class="ui-btn-left" href="${createLink(uri: '/login/auth')}"><g:message code="default.home.label"/></a>
			<h1><img src="${request.getContextPath()}/images/google_app.png" style="vertical-align:middle;margin-top:2px;"/></h1>
		</div>
		<div data-role="content">
				<div data-role="fieldcontain">
					<label for="hd" style="width:100%;" >Enter your Google Apps Domain Name</label>
					<input type="text" name='hd' id='hd' value="" 
                       required="required" autocorrect="off" autocapitalize="off" placeholder="E.g. COMPANYNAME.COM" style="background: #fff;width:95%"/></br>
				</div>
				<a href="#" id="changePageAnchor" rel=external style="text-decoration:none;">
				 		<button class="button button-green fr loginButton" type="button" onclick="oneSelectedDomain();">Submit</button>
				 		</a>
				 		 	
				
			<div id='loginMessage' style='color: red; margin-top: 10px;'></div>
		</div>
		<script>
			function oneSelectedDomain(){
				$("#changePageAnchor").attr("href","${request.getContextPath()}/openid/openid?hd="+$("#hd").val());
			}
		</script>

</body>
</html>




