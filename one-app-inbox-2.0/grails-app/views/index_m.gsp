<html>
<head>
    <title>Form Builder Inbox </title>
    <meta name="layout" content="mobile"/>
</head>

<body>

		<div data-role="header" data-position="inline">
			<h1>Form Builder Inbox </h1>
			<g:link controller="logout" class="ui-btn-right" data-icon="check" data-theme="b"><g:message code="menu.logout" default="Logout"/></g:link>
		</div>
		<div class="content-primary">
			<nav>
				
		<div data-role="content">
			<g:if test="${flash.message}">
				<div class="message">${flash.message}</div>
			</g:if>
			<ul data-role="listview" data-split-icon="plus" data-inset="true">
					<li data-role="list-divider">Inbox</li>
					<li>
						<g:link action="list" controller="feeds"><g:message code="menu.feeds" default="Feeds"/></g:link>
					</li>
					
					<li data-role="list-divider">Forms</li>
					<li>
						<g:link action="list" controller="formViewer"><g:message code="menu.feed" default="Forms"/></g:link>
					</li>
				
		</div>
		<div data-role="footer">
		</div>
		
			</nav>
			</div>

</body>
</html>
