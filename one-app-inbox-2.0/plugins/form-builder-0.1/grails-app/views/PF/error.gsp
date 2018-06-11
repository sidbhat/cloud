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
<html><head>
<meta name="layout" content="mainError"/>
<meta content="text/html; charset=utf-8" http-equiv="Content-Type">
<title>Form Builder Forms</title>
</head>
<body>
<noscript>
&lt;center&gt;
&lt;font size="5"&gt;&lt;b&gt;Javascript is not enabled in your browser.&lt;/b&gt;&lt;/font&gt;&lt;font size="4"&gt;
&lt;br&gt;This form will not function properly without Javascript enabled.&lt;br&gt;Please enable Javascript and reload the form.&lt;/font&gt;
&lt;/center&gt;
</noscript>
<g:if test="${redirectURL}">
	<script>
		function redirectToURL(){
			window.location = "${redirectURL}";
		}
		setTimeout("redirectToURL()",1000);
	</script>
</g:if>
<form action="" method="post" id="form">
<div style="border-radius:5px;overflow:hidden;-moz-box-shadow: 1px 0px 8px 2px #aaa;-webkit-box-shadow:  1px 0px 8px 2px #aaa;box-shadow: 1px 0px 8px 2px #aaa;" id="formcontent">
<div id="sizedcontent">
<div style="background-color:#CCC;padding:5px 40px;" >
<table width="100%">
<colgroup><col width="100%">
</colgroup><tbody><tr>
<td style="font-size:14px !important;"><h2 style="font-size:1.5em !important;">${exception?.message}</h2></td>
</tr>
</tbody></table>
</div>
<div style="padding:20px 40px;">
<div style="display:block;" ><span><span style="text-align:left;font-size:14px;" ><span xml:space="preserve" style="color:#666" >${exception?.detailMessage}</span></span></span><div id="confirmmsg"></div>

<div style="clear:both;font-size:0;min-height: 270px;"></div>
</div>
</div>
</div>
</div>
<div id="legal"><p>Powered by <a href="http://sidbhat1976.blogspot.com/" target="_blank" style="text-decoration:underline !important;">Form Builder</a></p></div>
</form>
</body></html>