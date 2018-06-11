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
<html>
	<head>
		<title><g:layoutTitle default="Welcome to Form Builder"></g:layoutTitle></title>
		<link rel="apple-touch-icon"
			href="${resource(dir: 'images', file: 'favicon.ico')}" />
		<META HTTP-EQUIV="EXPIRES" CONTENT="Mon, 22 Jul 2013 11:12:01 GMT">
		<link rel="shortcut icon"
			href="${resource(dir: 'images', file: 'favicon.ico')}"
			type="image/x-icon" />
		<link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'grid_min.css')}"/>
	    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'style_min.css')}"/>
	    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'messages.css')}"/>
	    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'forms_min.css')}"/>
	    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'tables_min.css')}"/>
	    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main_min.css')}"/>
	     <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'chat.css')}"/>
		<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'button.css')}"  />
		<link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'dropdownstyle.min.css')}"/>
	    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'custom.css')}"/>
		<link rel="stylesheet" media="screen"
			href="${resource(dir: 'plugins/jquery-ui-1.8.6/jquery-ui/themes/smoothness', file: 'jquery-ui-1.8.6.custom.min.css')}" />
		<script src="${resource(dir: 'js', file: 'jquery.tools.min.js')}"></script>
		<script type="text/javascript"
			src="${request.getContextPath()+'/js/jquery.tools.configuration.js'}"></script>
		<g:javascript library="application" />
		<g:layoutHead />
	</head>
	<body>
		<g:layoutBody />
	</body>
</html>