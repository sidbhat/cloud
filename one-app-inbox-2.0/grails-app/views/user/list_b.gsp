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
<!DOCTYPE html><html lang="en">

<head>

<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" >
<meta http-equiv="X-UA-Compatible" content="IE=Edge">
<meta name="layout" content="main" />
<!-- vPanel specific css/js -->

<link href="http://fonts.googleapis.com/css?family=Yanone+Kaffeesatz:extralight,light,regular,bold" media="screen" rel="stylesheet" type="text/css" >
<link href="http://fonts.googleapis.com/css?family=PT+Serif+Caption" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/reset.css" media="screen" rel="stylesheet" type="text/css">
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/grid.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/style.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/elfinder.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/jquery.ui.datatables.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/jquery.slidernav.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/jquery.fullcalendar.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/ui/default-ui/ui.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/ui/default-ui/portlet.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/ui/default-ui/jquery.ui.uniform.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/ui/default-ui/colors/jquery.ui.colors.default.css" media="screen" rel="stylesheet" type="text/css" class="uicolor" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/forms.css" media="screen" rel="stylesheet" type="text/css" >
<link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/prettify.css" media="screen" rel="stylesheet" type="text/css" >
<!--[if lt IE 8]> <link href="${grailsApplication.config.grails.serverURL}/css/vpanel/css/ie.css" media="screen" rel="stylesheet" type="text/css" ><![endif]-->
<!--[if lt IE 9]> <script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/html5.js"></script><![endif]-->
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.min.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.selectors.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.easing.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.cookie.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/hoverIntent.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.tools.min.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/overlay.apple.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.ui.min.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.uniform.min.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.dataTables.min.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.slidernav.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.fullcalendar.min.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/jquery.isotope.min.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/superfish.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/supersubs.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/elfinder.full.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/prettify/prettify.js"></script>
<script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/global.js"></script>
<!--[if lt IE 9]> <script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/selectivizr.js"></script><![endif]-->
<!--[if lt IE 8]> <script type="text/javascript" src="${grailsApplication.config.grails.serverURL}/css/vpanel/js/ie.js"></script><![endif]-->
<!-- End of vPanel specific css/js -->


<title>Contacts Directory</title>
<script type="text/javascript">


function load_content (  content ) {
var node = document.getElementById( 'show' );
node.innerHTML = content;
}


$(document).ready(function(){
    $('#slider').slidernav();

    $('.isotope-contacts').isotope({
        layoutMode: 'cellsByRow',
        cellsByRow : {
            columnWidth : 286
        },
        getSortData : {
            fullname : function ( $elem ) {
                return $elem.find('.fullname').text();
            },
            name : function ( $elem ) {
                return $elem.find('.name').text();
            },
            bday : function ( $elem ) {
                return $elem.find('.bday').text();
            }
        },
        filter: $('input[name=index]:checked').val()
    });

    $('#isotope-contacts-filter input[name="index"]').change(function(){
        var base = this;
        setTimeout(function(){$('.isotope-contacts').isotope({ filter: $(base).val() });},500);
    });
    $('#isotope-contacts-filter input[name="sort"]').change(function(){
        var base = this;
        setTimeout(function(){$('.isotope-contacts').isotope({ sortBy: $(base).val() });},500);
    });

});
</script>

<!-- LOADING SCRIPT -->

<script>

$(window).load(function(){

    $("#loading").fadeOut(function(){

        $(this).remove();

        $('body').removeAttr('style');

    });

});

</script>



<style type = "text/css">

    #container {position: absolute; top:50%; left:50%;}

    #content {width:800px; text-align:center; margin-left: -400px; height:50px; margin-top:-25px; line-height: 50px;}

    #content {font-family: "Helvetica", "Arial", sans-serif; font-size: 18px; color: black; text-shadow: 0px 1px 0px white; }

    #loadinggraphic {margin-right: 0.2em; margin-bottom:-2px;}

    #loading {background-color: #eeeeee; overflow:hidden; width:100%; height:100%; position: absolute; top: 0; left: 0; z-index: 9999;}

</style>

<!-- LOADING SCRIPT END -->



</head>

<%@ page import="com.oneapp.cloud.core.*" %>
<%@ page import="com.oneapp.cloud.core.social.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>

    <title><g:message code="contact.list" default="Contacts Directory"/></title>
</head>

<body>
<!-- Main Section -->

<section class="main-section grid_7">
<div class="main-content">
<section class="container_7 clearfix">
<div class="grid_6">
<div class="portlet">
<header>
    <h2>Address Book - Contacts (${totalContacts})</h2>
</header>

<form id="isotope-contacts-filter" class="ui-widget-header ui-state-default clearfix">
    <span class="buttonset" id="isotope-contacts-index">
        <input type="radio" name="index" id="index-a" value=".index-a" checked="checked"><label for="index-a">A</label>
        <input type="radio" name="index" id="index-b" value=".index-b"><label for="index-b">B</label>
        <input type="radio" name="index" id="index-c" value=".index-c"><label for="index-c">C</label>
        <input type="radio" name="index" id="index-d" value=".index-d"><label for="index-d">D</label>
        <input type="radio" name="index" id="index-e" value=".index-e"><label for="index-e">E</label>
        <input type="radio" name="index" id="index-f" value=".index-f"><label for="index-f">F</label>
        <input type="radio" name="index" id="index-g" value=".index-g"><label for="index-g">G</label>
        <input type="radio" name="index" id="index-h" value=".index-h"><label for="index-h">H</label>
        <input type="radio" name="index" id="index-i" value=".index-i"><label for="index-i">I</label>
        <input type="radio" name="index" id="index-j" value=".index-j"><label for="index-j">J</label>
        <input type="radio" name="index" id="index-k" value=".index-k"><label for="index-k">K</label>
        <input type="radio" name="index" id="index-l" value=".index-l"><label for="index-l">L</label>
        <input type="radio" name="index" id="index-m" value=".index-m"><label for="index-m">M</label>
        <input type="radio" name="index" id="index-n" value=".index-n"><label for="index-n">N</label>
        <input type="radio" name="index" id="index-o" value=".index-o"><label for="index-o">O</label>
        <input type="radio" name="index" id="index-p" value=".index-p"><label for="index-p">P</label>
        <input type="radio" name="index" id="index-q" value=".index-q"><label for="index-q">Q</label>
        <input type="radio" name="index" id="index-r" value=".index-r"><label for="index-r">R</label>
        <input type="radio" name="index" id="index-s" value=".index-s"><label for="index-s">S</label>
        <input type="radio" name="index" id="index-t" value=".index-t"><label for="index-t">T</label>
        <input type="radio" name="index" id="index-u" value=".index-u"><label for="index-u">U</label>
        <input type="radio" name="index" id="index-v" value=".index-v"><label for="index-v">V</label>
        <input type="radio" name="index" id="index-w" value=".index-w"><label for="index-w">W</label>
        <input type="radio" name="index" id="index-x" value=".index-x"><label for="index-x">X</label>
        <input type="radio" name="index" id="index-y" value=".index-y"><label for="index-y">Y</label>
        <input type="radio" name="index" id="index-z" value=".index-z"><label for="index-z">Z</label>
        <input type="radio" name="index" id="index-all" value="*"><label for="index-all">All</label>
    </span>
    <hr/>
    <span class="buttonset" id="isotope-contacts-sort">
       <input type="radio" name="sort" id="sort-firstname" value="fullname" checked="checked"><label
            for="sort-firstname">First Name</label>
        <input type="radio" name="sort" id="sort-lastname" value="name"><label for="sort-lastname">Last Name</label>
        <input type="radio" name="sort" id="sort-bday" value="bday"><label for="sort-bday">Birthday</label>
    </span>
   <p><g:link action="create" controller="contacts" class="button button-gray"><span
                        class="add"></span></g:link>
                        
    <g:link action="updateContacts" controller="linkedinConnect" class="button button-gray"  style="width: 80px"> Linkedin</g:link> 
     <g:link action="updateContacts" controller="twitterConnect" class="button button-gray"  style="width: 80px"> Twitter</g:link>
      <g:link action="updateContacts" controller="facebookConnect" class="button button-gray"  style="width: 80px"> Facebook</g:link>
    </p>
    
    <p><a href="/form-builder/account/searchResult.gsp" class="button button-gray"  style="width: 80px">${message(code: 'search', 'default': 'Search')}</a>
    </p>
</form>
<section>
<%	def index1, index2; %>
<ul class="isotope-contacts clearfix">
 <g:each in="${contactList}" status="i" var="contactInstance">
<% 
	index1=''
	index2=''
	if ( contactInstance?.firstName )
		index1 = contactInstance?.firstName?.charAt(0).toLowerCase()
	else
		index1 = contactInstance?.contactName?.charAt(0).toLowerCase() 
	
	if ( contactInstance?.lastName )
		index2 = contactInstance?.lastName?.charAt(0).toLowerCase()
	
%>                              
<li class="button-blue ui-corner-all index-${index1}" data-id="66f6d7ff16d37e3bde9643358c98cfe${i}">
    <div class="photo">
    <% if (contactInstance.facebookPictureURL) {%>
    	<img src='${fieldValue(bean: contactInstance, field: "facebookPictureURL")}'/>
    <%}else if (contactInstance.linkedinPictureURL) {%>
       <img src='${fieldValue(bean: contactInstance, field: "linkedinPictureURL")}'/>
     <%}else if (contactInstance.twitterPictureURL) {%>
       <img src='${fieldValue(bean: contactInstance, field: "twitterPictureURL")}'/>
    <%}%>
    <div></div></div>

    <div class="fullname"> 	
		
		<g:link action="edit" controller="contacts" id="${contactInstance.id}">
					<font color="white">${fieldValue(bean: contactInstance, field: "contactName")}</font>
	   </g:link>
    </div>
    <% if ( contactInstance.firstName && contactInstance.lastName ) {%>
     <div class="name"> 	
		${fieldValue(bean: contactInstance, field: "lastName")} ${fieldValue(bean: contactInstance, field: "firstName")}
	 </div>
	 <%}%>
	<% if ( contactInstance?.title ) { %>
      <div class="email"> ${fieldValue(bean: contactInstance, field: "title")}
 	<%}%>
	<% if ( contactInstance?.companyName ) { %>
      - ${fieldValue(bean: contactInstance, field: "companyName")}</div>
 	<%}%>
	<% if ( contactInstance?.city ) { %>
    <div class="city"> City: ${fieldValue(bean: contactInstance, field: "city")}</div>
 	<%}%>
	<% if ( contactInstance?.email ) { %>
    <div class="email"> Email: ${fieldValue(bean: contactInstance, field: "email")}</div>
 	<%}%>
 	<% if ( contactInstance?.mobile ) { %>
      <div class="email"> Phone: ${fieldValue(bean: contactInstance, field: "mobile")}</div>
 	<%}%>
	<% if ( contactInstance?.source ) { %>
      <div class="email"> Source: ${fieldValue(bean: contactInstance, field: "source")}</div>
 	<%}%>
 	<% if ( contactInstance?.headlines ) { %>
      <div class="email">${fieldValue(bean: contactInstance, field: "headlines")}</div>
 	<%}%>
</li>

</g:each>


</ul>
</section>


</div>
</div>

</section>
</div>

</section>

<!-- Main Section End -->



</div>

</section>

</div>

</body>

</html>