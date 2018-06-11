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
<head>
    <title><g:layoutTitle default="Welcome to Form Builder"></g:layoutTitle></title>
	<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'favicon.ico')}" />
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'grid_min.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'style_min.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'messages.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'forms_min.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'default.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'nivo-slider.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main_min.css')}"/>
	<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'button.css')}"  />
	<link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'custom.css')}"/>
	<link rel="stylesheet" media="screen" href="${resource(dir:'plugins/yui-2.8.2/js/yui/fonts',file:'fonts-min.css')}"  />
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery-1.7.1.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.nivo.slider-min.js')}"></script>
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

</head>


<body onload="${pageProperty(name: 'body.onload')}">


<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;z-index: 10000;background-color: #fff;opacity:0.5;">
  <img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
</div>
<div id="wrapper">
    <g:render template="/layouts/header"/>
    <section>
            <g:layoutBody/>
    </section>
 </div>

</body>

</html>