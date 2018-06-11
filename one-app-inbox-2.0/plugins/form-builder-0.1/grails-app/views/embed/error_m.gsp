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
<html>
	<head>
		<title>Form Builder Forms</title>
		<meta name="layout" content="mobile">
	</head>

	<body>
		<div data-role="header" data-position="inline">
			<h1 style="margin-left: auto;margin-right: auto;">${exception?.message}</h1>
		</div>
		
		<div class="content-primary">
			<nav>
				<div data-role="content" >
					<ul data-role="listview" data-inset="true" >
						<li data-icon="false">
							<p>
								<g:if test="${exception}">
									<div class="snippet">
										<span>${exception?.detailMessage}</span>
									</div>
								</g:if>
							</p>
						</li>
					</ul>
				</div>
			</nav>
		</div>
		<div data-role="footer" data-position="inline" style="font-size: 14px !important;position:fixed;bottom:0px;padding:10px;font-weight:normal;">
		<p style="margin:0px;">Powered by <a href="http://sidbhat1976.blogspot.com/" target="_blank" style="color:#eee;">Form Builder</a></p>
		</div>
	</body>
</html>