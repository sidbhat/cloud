<html>
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
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

<g:set var="entityName" value="${message(code: 'dashboard.label', default: 'Dashboard')}"/>
    <title><g:message code="default.dashboard.label" args="[entityName]"/></title>
</head>

<body>
<div class="container_8 clearfix">
    <!-- Main Section -->

    <section class="main-section grid_7">
        <header>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
          
        </header>

        <section class="container_6 clearfix">
            <div class="grid_6">
                <div class="message info ac">
                    <h3><g:message code="menu.welcome.message" default="Welcome to Form Builder"/></h3>

                    <p><g:message code="menu.welcome.description" default="Form Builder simplifies the way you collaborate within your company."/> </p>
                </div>
            </div>

            <hgroup class="grid_6 ac">
                <h2>Here are some key features... </h2>
            </hgroup>
			<br/><br/><br/>
			<style>.container_6 .grid_2{width:300px;margin-top: 10px}
			.container_6 .grid_2 img{
				border-radius: 4px;
				box-shadow: 1px 1px 10px 0px;
				margin-bottom: 3px;
			}
			.container_6 .grid_2 a:hover img{
				box-shadow: 1px 1px 10px 1px;
			}
			.container_6 .grid_2 .textDiv{
				border-radius: 5px;
				box-shadow: 1px 1px 10px -1px;
				height:61px;
				padding-top: 5px
			}
			</style>
            <figure class="grid_2 ac">
				<g:link controller="form" action="list"><img src="${request.getContextPath()}/images/wel_form.jpg" width="300" height="168"/></g:link>
				<div class="textDiv">
    	            <h3>Create Forms to capture data</h3>
	                <p> Create your approval forms, surveys and more </p>
				</div>
            </figure>
            <figure class="grid_2 ac" style="float:right">
            	<g:link controller="dashboard" action="index"><img src="${request.getContextPath()}/images/wel_inbox.jpg" width="300" height="168"/></g:link>
            	<div class="textDiv">
	            	<h3>Collaborate within your Organization</h3>
	                <p> Collaborate within your company. Share information and act quickly on your feeds </p>
            	</div>
            </figure>
            <div style="text-align:center;width:100%;float:left">
	            <figure class="grid_2 ac" style="left: 0;position: absolute;right: 0;margin: 10px auto;">
	            	<g:link controller="report" action="index"><img src="${request.getContextPath()}/images/wel_report.jpg" width="300" height="168"/></g:link>
	            	<div class="textDiv">
		                <h3>Generate Reports</h3>
		                <p> Analyze your inbox, form data, its views and creates </p>
	                </div>
	            </figure>
	        </div>
          
        </section>
</div>

</section>

<!-- Main Section End -->
<!-- Testing commit (Main Pal) -->
</div>
</section>
</body>
</html>
