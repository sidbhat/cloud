<%
	  if (request['isMobile']){
%>

<html>
<head>
	<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	<meta name="layout" content="mobile" />
	<g:set var="entityName" value="${message(code: '404.label', default: 'Page Not Found')}" />
	<title>404 Error</title>
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
						<h3>404 Error!</h3>
						 <p style="white-space:normal;">
							Sorry! Page not found. Click <a href="${grailsApplication.config.grails.serverURL}">here</a> to goto the main page.
						 </p>
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
	        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
	        <meta name="layout" content="main" />
	        <g:set var="entityName" value="${message(code: '404.label', default: 'Page Not Found')}" />
	        <title>404 Error</title>
	    </head>
	    <body>
	        <div class="message error">
	             <h3>404 Error!</h3>
	             <p>
	               Sorry! Page not found. Click <a href="${grailsApplication.config.grails.serverURL}">here</a> to goto the main page.
	             </p>
	         </div>
	    </body>
	</html>

<%}%>