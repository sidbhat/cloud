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
<!doctype html>
<html>
	<head>
	
	<link rel="apple-touch-icon" href="${request.getContextPath()+'/images/favicon.ico'}" />
		<title><g:layoutTitle default="tablet"/></title>
		<link rel="shortcut icon" href="${request.getContextPath()+'/images/favicon.ico'}" type="image/x-icon"/>
		<link rel="apple-touch-icon" href="${resource(dir: 'images', file: 'one_app_logo.png')}">
		<link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'slablet.css')}"/>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8">
		<meta name="apple-mobile-web-app-capable" content="yes">
		<meta name="viewport" content="width=device-width, initial-scale=1, maximum-scale=1, user-scalable=no"> 

		<script type="text/javascript" src="${resource(dir: 'js', file: 'utility.js')}"></script>
		<g:javascript library="jquery/jquery-1.4.2.min" plugin="jquery"/>
		<uf:resources type="css" style="default"/>
		<langs:resources/>
		<script type="text/javascript" src="${resource(dir: 'js/slablet', file: 'ga.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js/slablet', file: 'master.js')}"></script>
		 <script type="text/javascript" src="${resource(dir: 'js/slablet', file: 'iscroll.js')}"></script>
		<script type="text/javascript" src="${resource(dir: 'js/slablet', file: 'trustBanner.min.js')}"></script>
<%--<script type="text/javascript" src="${resource(dir: 'js/slablet', file: 'mixpanel-2.1.min.js')}"></script>		
		--%>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'formula.js')}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/rulesImpl.js'}"></script>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/grid_min.css'}"/>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/forms_min.css'}"/>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/tables_min.css'}"/>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/DasMain-min.css'}"/>
		 <link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/style_min.css'}"/>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/button.css'}"  />
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/chat.css'}"/>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/dropdownstyle.min.css'}"/>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/css/custom.css'}"/>
		<link rel="stylesheet" media="screen" href="${request.getContextPath()+'/plugins/jquery-ui-1.8.6/jquery-ui/themes/smoothness/jquery-ui-1.8.6.custom.min.css'}"/>
	  	
		<script type="text/javascript" src="${request.getContextPath()+'/js/jquery.tools.min.js'}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/asec.js'}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/jquery.tools.configuration.js'}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/jquery.tables.js'}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/global-min.js'}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/share.js'}"></script>
		  <script type="text/javascript" src="${request.getContextPath()+'/js/chat.js'}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/pageBreak.js'}"></script>
		<script src="${request.getContextPath()+'/plugins/jquery-ui-1.8.6/jquery-ui/js/jquery-ui-1.8.6.custom.min.js'}" type="text/javascript"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/dropdownscript-min.js'}"></script>
		<link rel="stylesheet" type="text/css" media="screen" href="${resource(dir: 'css', file: 'formbuilder.css')}"/>
		  <script type="text/javascript" src="${request.getContextPath()+'/js/codehighcharts.js'}"></script>
	    <gui:resources components="autoComplete"/>
	    <script type="text/javascript">
	   var feedFilterTag = "${filterTag}"
		    var requestUrlForAjax = "${request.getContextPath()}/"
		    $(document).ready(function(){
		    	 showOverLayPopup();
				try{
					setDateTypes()
				}catch(e){}
				$(".ctrlHolder:not(.ppHolder)").click(function(){
					$(".ctrlHolder").removeClass("ctrlHolderSelected");
					$(this).addClass("ctrlHolderSelected");
					$(this).fadeIn("slow");
				});
			});
	    </script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/application.js'}"></script>
		<style type="text/css">
			.apple_overlay{display:none;background-image:url(/goombal/images/overlay/white.png);width:640px;padding:35px;font-size:11px}.apple_overlay .close{background-image:url(/form-builder/images/close-icon.png);position:absolute;right:14px;top:14px;cursor:pointer;height:30px;width:30px}.apple_overlay.black{background-image:url(/form-builder/images/overlay/transparent.png);color:#fff}
			.contentWrap{border:0px}	
	    	.buttons{
	    		font: 12px/100% 'Lucida Grande', 'Lucida Sans Unicode', 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif
	    	}
	    	.ui-autocomplete-loading{
	    		background: white url("/goombal/images/ajax-loader.gif") no-repeat right !important;
	    	}
	    	.apple_overlay{
	    		display:none;
	    	}
	    	label em{
	    		color:red;
	    	}
	    	table.datatable td {
				padding: 5px;
				vertical-align: middle;
				max-width: inherit;
				white-space: normal;
			}		
		</style>
		<%--<script type="text/javascript" src="${resource(dir: 'js/slablet', file: 'mixpanel-2.1.min.js')}"></script>
		 --%><style type="text/css">
	.headingContainer{
	display: none;
	}	 
#field1368444216316ControlHolder,#field1369141674386ControlHolder,#field1369141731064ControlHolder,#field1368603351999ControlHolder,#field1366187255663ControlHolder,#field1366187218666ControlHolder,#field1368603833249ControlHolder,
#field1367308384678ControlHolder,#field1369033553552ControlHolder,#field1366801880536ControlHolder,#field1369032630937ControlHolder,#field1366808184674ControlHolder,#field1371455983417ControlHolder,#field1371457117596ControlHolder,
#field1371455167030ControlHolder
{
display: none;
}
</style>


		 <g:layoutHead/>		
	</head>
	<body>
	<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;left:0;top:0;z-index: 10000;background-color: #fff;opacity:0.5;">
	  <img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
	</div>
			<div id="main" class="abs">
	<div class="abs header_upper chrome_light">
		<span class="float_left button" style="width: 60px;height: 18px;" id="button_navigation" >
			Navigation
		</span>
		<%--<a href="" class="float_left button" style="width: 55px;height: 18px; ">
			Back
		</a>
		--%><a href="${request.getContextPath()+'/logout/index'}" class="float_right button"  style="width: 55px;height: 18px;">
			Sign out
		</a>
		<span id="pageTitle">
		</span>
	</div>
	<div id="main_content" class="abs" style="top: 92px; bottom: 46px; overflow: auto;">
		<div id="main_content_inner">
		<ul id="sidebar_menu" >
		<g:layoutBody/>		
		</ul>
		<script type='text/javascript'>
$(function() { 
        var x = $('h2.heading').text();
        $('#pageTitle').text(x);
        var x1=$('#formId').val();
        if( $('.'+x1)!=undefined)
       		 $('.'+x1).addClass("active")
       	 else{
       		x1=$('#pfid').val();
       		$('.'+x1).addClass("active")
       	 }
});
</script>
		</div>
				
		</div>
	</div>
<div id="sidebar" class="abs" style="z-index:2;">
	<span id="nav_arrow"></span>
	<div class="abs header_upper chrome_light">
		Form Builder
	</div>	
	<div id="sidebar_content" class="abs" style="top: 92px; bottom: 46px; overflow: auto;">
	<div style="margin-left: 60px;margin-top: 20px;">
	<div >
	<g:if test='${session["user"] &&  session["user"]?.pictureURL != null && session["user"]?.pictureURL?.length() != 0 }'>
    	<img width="90" style="border: 1px solid #DDD;" src="${ session["user"]?.pictureURL}">
     </g:if>
     <br>
       </div>
<span style="color:#333;font-size:11px;font-weight:bold;margin-left:11px;">${(session["user"].shortName?:(session["user"].firstName+' '+(session["user"].lastName?:"")))}</span></div>
		<div id="sidebar_content_inner">
			<ul id="sidebar_menu" style="padding-left:0;">
			<form:tabletFormList/>
			</ul>
			
		</div>
	</div>
</div>
	</body>
</html>
