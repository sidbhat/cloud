
<head>
    <title>

Form Builder</title>
    <meta name='layout' content='main'/>
    

</head>

<body class="login">
<div class="login-box main-content">
    <header><h2>Sign In with <img src="${request.getContextPath()}/images/google_app.png" style="vertical-align:middle;margin-top:2px;"/></h2></header>
    <section>
	    <div class="message info">Enter your Google Apps Domain Name</div>
	    <g:form action='openid' controller="openid" name="register" autocomplete='off'>
	
	        <g:if test="${flash.message}">
	            <div class="message"><g:message code="${flash.message}" args="${flash.args}"
	                                            default="${flash.defaultMessage}"/></div>
	        </g:if>
	        <p>
	       		<g:textField name="hd" size="36" placeholder="E.g. COMPANYNAME.COM" class="full"/>
	       	</p>
	           
	        <br/>
		  	<div class="action"> 
		        <g:submitButton name="submit" value="Sign In" class="button button-green fr" type="submit">Sign Up</g:submitButton>
		    </div>                    
	    </g:form>
	    <div>&nbsp;</div>
	</section>
</div>
</body>

   
   
  

