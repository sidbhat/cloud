<!DOCTYPE html>
<html lang="en">
<head>
      <meta name="layout" content="loginMain" />
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

 <div class="container_8feed">
	<div class="message info ac">
         <h3><g:message code="menu.welcome.message" default="Welcome to Form Builder"/></h3>

         <p><g:message code="menu.welcome.description" default="Form Builder lets your collect, analyze and collaborate on any kind of information."/> </p>
     </div>
     <div style="width:100%;">
     		<div style="width:35%;float:left;margin-top:45px;" >
     			<div class="login-box main-content" style="left:0;margin:0px 10px 0px !important;top:11px;position: relative !important;box-shadow:0 1px 5px 0 #4A4A4A;">
				    <header>
				    	<h2>Form Builder Login</h2>
				    	<div class="clearfix" style="position:absolute;right:10px;top:15px;">
							<ul class="action-buttons clearfix fr">
				                <li>
				               		<a href="${grailsApplication.config.grails.serverURL}/documentation/login_help.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
				                       class="help"></span></a>	                
				                </li> 
				           	</ul>
				           </div>
				    </header>
				    <section style="padding:15px 30px;">
				
				        <g:if test='${flash.message}'>
				            <div class='error'>${flash.message}</div>
				        </g:if>
				
				
				        <div class="message info">Enter your username and password</div>
				
				        <form id="form" action="${postUrl}" method="post" class="clearfix noLoader">
				            <p>
				                <input type="text" name='j_username' id='username' class="full" value="" name="username"
				                       required="required" autocorrect="off" autocapitalize="off" placeholder="Username"/>
				            </p>
				            <p>
				                <input type="password" name='j_password' id='password' class="full" value="" name="password"
				                       required="required" placeholder="Password"/>
				            </p>
				            <p class="clearfix">
				           	 	<button class="button button-green fl" type="submit">Login</button>
				                <span style="margin:0 25px;vertical-align: sub;">
				                    <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
				                           <g:if test='${hasCookie}'>checked='checked'</g:if>/>
				                    <label class="choice" for="remember">Remember me</label>
				                </span>
				            </p>
				            <p>&nbsp;</p>
				            <p style="margin-bottom:5px;"><g:link controller="register" action="forgotPassword" style="color:#CC3300 !important;font-weight:normal;">Forgot Password?</g:link> | <g:link controller="register" action="changePassword" style="color:#CC3300 !important;font-weight:normal;">Reset Your Password</g:link></p>
				        </form>
				        
				        <p style="font-weight:bold">Sign in using</p>
				        <p style="border-bottom:1px solid #ddd;padding-bottom:5px;padding-top:5px;"><g:link controller="google" action="googleOauth"><img src="${request.getContextPath()}/images/google_app.png" style="vertical-align:middle;margin-top:2px;margin-right:10px;"/></g:link> | <g:link controller="google" action="googleOauth" ><img src="${request.getContextPath()}/images/gmail_logo.gif" style="vertical-align:middle;margin-left:10px;" height="12px"/></g:link></p>
				        <p style="padding-bottom:5px;padding-top:5px;">Don't have an account?  &nbsp;<a href="${grailsApplication.config.grails.serverURL}/register/registerUser.gsp" style="color:#CC3300 !important;font-weight:normal;">Register Now</a></p>
				
				      <!-- end-->
				
				
				    </section>
				</div>
     		</div>
     		<div style="width:60%;float:right;margin-top:-45px;" >
					<div class="slider-wrapper theme-default" style="margin-left:-10px;width:100%;">
				           <div id="slider" class="nivoSlider">
				          	   <img src="../images/screenshots/activity_feed2.jpg" data-thumb="../images/screenshots/activity_feed2.jpg" alt="" data-transition="slideInLeft" title="#activityFeed"/>
				               <img src="../images/screenshots/survey.jpg" data-thumb="../images/screenshots/survey.jpg" alt="" data-transition="slideInLeft" title="#surveyForm"/>
				               <img src="../images/screenshots/formbuilder2.png" data-thumb="../images/screenshots/formbuilder2.png" alt="" data-transition="slideInLeft" title="#formBuilder" />
				               <img src="../images/screenshots/ruleset3.png" data-thumb="../images/screenshots/ruleset3.png" alt="" data-transition="slideInLeft" title="#ruleSet" />
				               <img src="../images/screenshots/reports2.jpg" data-thumb="../images/screenshots/reports2.jpg" alt="" data-transition="slideInLeft" title="#reports" />
				           </div>
				           <div id="activityFeed" class="nivo-html-caption">
				      			 <b>Be more productive with actionable inbox </b>			               
				    		   <div>Bring your emails, spreadsheets and business data together for better team collaboration 
				     	  </div> 
				         	</div>
				           <div id="surveyForm" class="nivo-html-caption">
				               <b>Survey, contact or lead generation forms</b>
				               <div>Generate more leads, capture contact information or collect feedback</div> 
				           </div>
				           <div id="formBuilder" class="nivo-html-caption">
				               <b>Data capture forms</b>
				               <div>Create your business processes like Expense, Leave, Time and many more </div> 
				           </div>
				           <div id="ruleSet" class="nivo-html-caption">
				               <b>Subscription Rules</b>
				               <div> Automate your business process workflow by creating subscription rules </div>
				           </div>
				           <div id="reports" class="nivo-html-caption">
				               <b>Reports</b>
				               <div> Generate reports and charts on real time data captured through forms</div>
				           </div>
				      </div>
     		</div>
     </div>

<script type="text/javascript">
	$(window).load(function() {
	    $('#slider').nivoSlider({
	    		pauseTime: 10000,
		    });
	});
</script>
</div>
</body>
</html>




