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
<%@page import="grails.plugin.multitenant.core.util.TenantUtils"%>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<%@ page import="org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils" %>
<%@ page import="com.oneapp.cloud.core.Role" %>
<g:set var="locale" value="${RequestContextUtils.getLocale(request)}"/>
<% String language = 'en' %>
<head>
    <title><g:layoutTitle default="Welcome to Form Builder Form Builder"></g:layoutTitle></title>
	<link rel="apple-touch-icon" href="${request.getContextPath()+'/images/favicon.ico'}" />
	<icep:bridge contextPath="${request.getContextPath()}"/>
    <link rel="shortcut icon" href="${request.getContextPath()+'/images/favicon.ico'}" type="image/x-icon"/>
    <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/reset.css'}"/>
    <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/grid_min.css'}"/>
    <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/style_min.css'}"/>
    <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/messages.css'}"/>
    <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/forms.css'}"/>
    <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/tables_min.css'}"/>
    <link rel="stylesheet" href="${request.getContextPath()+'/css/main_min.css'}"/>
	<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/button.css'}"  />
	<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/tables.css'}"/>
	<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/dropdownstyle.min.css'}"/>
	<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/plugins/jquery-ui-1.8.6/jquery-ui/themes/smoothness/jquery-ui-1.8.6.custom.min.css'}"/>
	<g:javascript library="jquery/jquery-1.4.2.min" plugin="jquery"/>
	<script src="${request.getContextPath()+'/plugins/jquery-ui-1.8.6/jquery-ui/js/jquery-ui-1.8.6.custom.min.js'}" type="text/javascript"></script>
	<jqJson:resources/>
	<uf:resources type="css" style="default"/>
	<langs:resources/>
	<g:if test="${SpringSecurityUtils.ifAnyGranted(Role.ROLE_HR_MANAGER) }">
		<g:set var="subForms" value="${org.grails.formbuilder.Form.findAllWhere(formCat:'S',tenantId:TenantUtils.getCurrentTenant()) }" />
		<%
		masterForms = org.grails.formbuilder.Form.createCriteria().list(){
				eq "tenantId",TenantUtils.getCurrentTenant()
				not{
					eq "formCat",'S'
				}
			}
		%>
	</g:if>
	<g:elseif test="${SpringSecurityUtils.ifAnyGranted(Role.ROLE_TRIAL_USER) }">
		<g:set var="subForms" value="${org.grails.formbuilder.Form.findAllWhere(formCat:'S',tenantId:TenantUtils.getCurrentTenant(),createdBy: session['user']) }" />
		<%
	 	masterForms = org.grails.formbuilder.Form.createCriteria().list(){
					eq "tenantId",TenantUtils.getCurrentTenant()
					eq "createdBy", session['user']
					not{
						eq "formCat",'S'
					}
				}
			%>
	</g:elseif>
	<script type="text/javascript">
	<%
	def subFormFieldTypes = grailsApplication.config.subFieldTypes
	def masterFieldTypes = grailsApplication.config.masterFieldTypes
	def subFormData = subForms.collect{[id:it.id,label:it.toString(),fieldsList:it.fieldsList.findAll{(it && subFormFieldTypes.contains(it?.type))}.collect{[name:it.name,label:it.toString(),isNumeric:((it?.type == 'SingleLineNumber' || (it?.type == 'FormulaField' && grails.converters.JSON.parse(it.settings).en.newResultType == 'NumberResult') )?true:false)]}]}
	
  	 def masterFormData = masterForms.collect{[id:it.id,label:it.toString(),fieldsList:it.fieldsList.findAll{(it && masterFieldTypes.contains(it?.type))}.collect{[name:it.name,label:it.toString(),isNumeric:(it?.type == 'SingleLineNumber' || (it?.type == 'FormulaField' && grails.converters.JSON.parse(it.settings).en.newResultType == 'NumberResult')),isString:(it?.type == 'SingleLineText' || it?.type == 'MultiLineText'|| it?.type == 'AddressField'),isFileUpload:(it?.type == 'FileUpload')]}]}
	masterFormData.add(0,[id:'user',label:'Users',fieldsList:[[name:'firstName',label:'First Name',isNumeric:false,isString:true,isFileUpload:false],[name:'lastName',label:'Last Name',isNumeric:false,isString:true,isFileUpload:false],[name:'username',label:'Email',isNumeric:false,isString:true,isFileUpload:false]]])
	%>
	var subFormData = ${subFormData as grails.converters.JSON};
	var masterFormData = ${masterFormData as grails.converters.JSON};
	var maxFormControls = ${(com.oneapp.cloud.core.ApplicationConf.get(1)?.maxFormControls)?:40};
	$(document).ready(function(){
		$('.formHeading').click(function(){$('a[href$="#formSettings"]').trigger('click')})
	});
    $(function() {
      $('#container').formbuilder();
      $('div.buttons').children().button();

    });
  </script>
  <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/jHtmlArea.css'}"/>
	<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/jquery-ui-1.8.20.custom.css'}"/>
  <script type="text/javascript" src="${request.getContextPath()+'/js/jquery/jHtmlArea-0.7.0.min.js'}"></script>
  <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.formbuilder.core.js')}"></script>
  <link rel="stylesheet" type="text/css" media="screen" href="${resource(dir: 'css', file: 'jquery-formbuilder.min-0.1.css')}"/>
  <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.qtip.min.js')}"></script>
  <script type="text/javascript" src="${request.getContextPath()+'/js/formTemplateToForm.js'}"></script>
  <link rel="stylesheet" type="text/css" media="screen" href="${resource(dir: 'css', file: 'jquery.qtip.min.css')}"/>
  <link rel="stylesheet" type="text/css" media="screen" href="${resource(dir: 'css', file: 'formbuilder.css')}"/>
  <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/custom.css'}"/>
 	
    <gui:resources components="autoComplete"/>
    <script type="text/javascript">
    	$(document).ready(function(){
        	$("#overlayHelper .close").click(hideOverlay);
        	$('.message-close').click(function(){
        		$(this).parent().parent().fadeOut();
            	});
        	var exz=document.getElementById("refresheds");
            if(exz.value=="no"){exz.value="yes";}
            else{exz.value="no";location.reload();}
        });
    	
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
    <script type="text/javascript" src="${request.getContextPath()+'/js/application.js'}"></script>
    <script type="text/javascript" src="${request.getContextPath()+'/js/share.js'}"></script>
 
    <g:layoutHead/>
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

  function showMessage(){
		$('.closeable').css('display','inline-block');
		$('#dashBoardMessageId').css('display','block');
	}

  $(document).ready(function(){
		timerAction("Your session will expire in 10 minutes. Do you want to save the changes?");
		}
	);
</script>
	<style type="text/css">
		#paletteTabs a:link, #paletteTabs a:visited, #paletteTabs a:hover {
			font-weight: normal;
		}
		.message a:link, .message a:visited, .message a:hover {
			color: orangered;
			text-decoration: underline;
		}
		input, select, textarea {
			vertical-align: initial;
			margin: 2px;
		}
		.clear {
			display: initial;
			overflow: initial;
			visibility: initial;
			width: initial;
			height: initial;
		}
		.buttons{
			font: 12px/100% 'Lucida Grande', 'Lucida Sans Unicode', 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;
		}
		.buttons input{
			background: #F1F1F1;
			background: -webkit-gradient(linear, left top, left bottom, from(#E9E9E9), to(#D1D1D1));
			background: -moz-linear-gradient(top, #E9E9E9, #D1D1D1);
			-pie-background: linear-gradient(top, #e9e9e9, #d1d1d1);
			border: 1px solid #BBB;
			font: 12px/100% 'Lucida Grande', 'Lucida Sans Unicode', 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif;
			font-weight: normal;
			padding: 4px 10px;
			line-height: 16px;
		}
		.jHtmlArea iframe{
			border: 1px inset #ccc;
			scroll: auto;
		}
		#builderPalette .ui-button-text{
			text-align:left;
		}
		#builderPalette .ui-button-text span{
			padding-left:9px !important;
		}
		.pay_now_button{
			background-image:url(/form-builder/images/Pay_Now.png);
			background-size: 120px;
			background-position: center;
			overflow: hidden;
			width: 120px;
			height: 28px;
		}
		.itmHeader td{font-weight:bold}
		table.itmTable{border: 0;width: 100%;margin-bottom: 5px}
		.itmTable td{padding:5px;line-height: 16px;text-align:left;border-bottom:1px dotted #D5D5D5}
		.itmTable img{border:1px dotted #D5D5D5}
		.itmTable img:hover{box-shadow: 0px 0px 6px 3px #D4F5FA}
		.itmTable td:first-child{padding-left:0px}
		.itmTable td:nth-child(2){width:100%}
		
	</style>
	<form:addScript></form:addScript>
</head>


<body onload="${pageProperty(name: 'body.onload')}">
<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;left:0;top:0;z-index: 10000;background-color: #fff;opacity:0.5;">
  <img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
</div>
<div id="overlayHelper" class="apple_overlay black" style="display:none;z-index: 10001;position: fixed;margin: auto;left:0;right: 0;top: 0;bottom: 0;width:586px;height:456px;">
	<a class="close"></a>
	<iframe src="${createLink(controller:'login',action:'auth_ajax')}" style="border:0;width:100%;height:100%" scrolling="auto"></iframe>
</div>
<style type="text/css">
	<g:render template="/layouts/custom"/>
</style>

<div id="wrapper">
    <g:render template="/layouts/header"/>
    <section>
        <div>
            <g:layoutBody/>
        </div>

        <div id="push"></div>
    </section>
    <g:render template="/layouts/footer"/>
</div>
<input type="hidden" id="refresheds" value="no">

</body>

</html>
