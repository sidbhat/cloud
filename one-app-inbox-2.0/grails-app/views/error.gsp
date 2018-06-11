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
<%@ page import="org.springframework.web.context.request.RequestContextHolder"%>
<%@ page import="com.oneapp.cloud.core.log.AppLog"%>  
<%
	 if (request['isMobile'] ){
%>
<html>
	<head>
		<title>Form Builder Exception</title>
		<meta name="layout" content="mobile">
	</head>

	<body>
		<div data-role="header" data-position="inline">
			<a data-icon="home" class="ui-btn-left" href="${createLink(uri: '/home/index')}"><g:message code="default.home.label"/></a>
			<h1>Error </h1>
		</div>
		
		<div class="content-primary">
			<nav>
				<div data-role="content" >
					<ul data-role="listview" data-inset="true" >
						<li data-icon="false">
							<p>
							   <g:if test="${exception}">
									<div class="snippet">
										<h3>Sorry an error occurred!</h3> <br/>
										<span>${exception?.message}</span>
									</div>
								</g:if>
							</p>
							<div class="message warning">
								<h3>Email Administrator</h3>
								<p>
									<a href="mailto:admin@yourdomain.com?subject=Form Builder Error&body=${exception?.message?.encodeAsHTML()}">Click here to email</a>
								</p>
							</div>
						</li>
					</ul>
				</div>
			</nav>
		</div>
	</body>
</html>
					
<%
}else{
%>
<html>
<head>
    <title>Form Builder Exception</title>
	<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'favicon.ico')}" />
	<icep:bridge contextPath="${request.getContextPath()}"/>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'fullcalendar.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'reset.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'grid.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'style.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'messages.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'forms.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'tables.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main.css')}"/>
	<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'button.css')}"  />
	<link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'tables.css')}"/>
	<link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'dropdownstyle.css')}"/>
   
    <!--[if lt IE 8]>
<![endif]-->

    <!-- jquerytools -->
    <script src="${resource(dir: 'js', file: 'jquery.tools.min.js')}"></script>
    <!-- highcharts -->
    <script src="${resource(dir: 'js', file: 'highcharts.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'global.js')}"></script>

    <!--[if lt IE 9]>
<script type="text/javascript" src="${resource(dir: 'js', file: 'html5.js')}"></script>
<script type="text/javascript" src="${resource(dir: 'js', file: 'PIE.js')}"></script>
<script type="text/javascript" src="${resource(dir: 'js', file: 'IE9.js')}"></script>
<script type="text/javascript" src="${resource(dir: 'js', file: 'ie.js')}"></script>
<![endif]-->

    <script type="text/javascript" src="${resource(dir: 'js', file: 'fullcalendar.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'gcal.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.tools.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.cookie.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.tables.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.flot.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'global.js')}"></script>
     <script type="text/javascript" src="${resource(dir: 'js', file: 'share.js')}"></script>
      <script type="text/javascript" src="${resource(dir: 'js', file: 'dropdownscript.js')}"></script>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'custom.css')}"/>
 	
    <gui:resources components="autoComplete"/>

    <script type="text/javascript">
        //define your itemSelect handler function:
        var itemSelectHandler2 = function(aArgs) {
          //  alert(aArgs);
            var oMyAcInstance = aArgs[0]; // your AutoComplete instance
            var elListItem = aArgs[1]; // the <li> element selected in the suggestion
            // container
            var oData = aArgs[2]; // object literal of data for the result
           // alert(oData);
        };

        //subscribe your handler to the event, assuming
        //you have an AutoComplete instance myAC:
        try{
        myAC.itemSelectEvent.subscribe(itemSelectHandler2);
        }catch(e){}
        
        
    </script>
    <g:javascript library="application"/>
 
    <script type="text/javascript">

  var _gaq = _gaq || [];
  _gaq.push(['_setAccount', 'UA-25353534-1']);
  _gaq.push(['_setDomainName', '.oneappcloud.com']);
  _gaq.push(['_trackPageview']);

  (function() {
    var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
    ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
    var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
  })();



</script>
<style type="text/css">
	.createFormInstance{
		position:relative;
		width:4px;
		height:6px;
		top:4px;
	}
</style>
</head>
<body>
<div id="wrapper">
<g:render template="/layouts/header"/>
<section class="main-section grid_7" style="margin-left:250px;width:800px;">
<div class="main-content">
	<div class="message error">
		<h3>Sorry an error occurred!</h3> <br/>
		${exception?.message}
	<p>
		<!--strong>Error ${request.'javax.servlet.error.status_code'}:</strong> ${request.'javax.servlet.error.message'.encodeAsHTML()}<br/>
		<strong>URI:</strong> ${request.'javax.servlet.error.request_uri'}<br/-->
		<g:if test="${exception}">
			
				<%--
				/*try{
					   def sessionId = RequestContextHolder.getRequestAttributes()?.getSessionId()
					def ip = RequestContextHolder.getRequestAttributes()?.getRequest()?.getRemoteAddr()
					AppLog.withNewSession {
						def alog = new com.oneapp.cloud.core.log.AppLog()
						  alog.ip = ip
						alog.uri = request.forwardURI
						alog.user = session["user"]
						alog.msgType="E"
						alog.msg = exception?.message?.encodeAsHTML()
						 alog.deviceType = request.getHeader("User-Agent")
						alog.save()
						//log.error "Error Message: "+exception?.message+"\nCaused by: "+exception?.cause?.message+"\nClass: "+exception?.className+"\nAt Line: ["+exception?.lineNumber+"]"
					}
				}catch (Exception ex){
					System.out.println(ex)
					//log.error "Error while saving app log in error.gsp ${ex}"
				}*/
	
	

			<strong>Exception Message:</strong> ${exception?.message?.encodeAsHTML()} <br/>
			<strong>Caused by:</strong> ${exception?.cause?.message?.encodeAsHTML()} <br/>
			<strong>Class:</strong> ${exception?.className} <br/>
			<strong>At Line:</strong> [${exception?.lineNumber}] <br/>
			<strong>Code Snippet:</strong><br/>
--%>
			<div class="snippet">
				<g:each var="cs" in="${exception?.codeSnippet}">
				  <!--  ${cs?.encodeAsHTML()}-->
				  An error occurred with the operation performed. <br/>
				</g:each>
			</div>
		</g:if>
	</p>
	</div>

	<div class="message warning">

		<h3>Email Administrator</h3>

		<p><a href="mailto:admin@yourdomain.com?subject=Form Builder Error&body=${exception?.message?.encodeAsHTML()}">Click here to email</a>
		</p>
	</div>
</div>
</section>
</div>


</body>
</html>
<%}%>