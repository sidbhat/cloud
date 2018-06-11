<!DOCTYPE html>
<head>
    <title><g:message code="app.title" default="Form Builder"/></title>
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
    <link rel="shortcut icon" href="${request.getContextPath()+'/images/favicon.ico'}" type="image/x-icon"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css/streamlined', file: 'reset.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css/streamlined', file: 'grid.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css/streamlined', file: 'style.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css/streamlined', file: 'messages.css')}"/>
<%--    <link rel="stylesheet" media="screen" href="${resource(dir: 'css/streamlined', file: 'forms.css')}"/>--%>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css/streamlined', file: 'tables.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'css/streamlined', file: 'main.css')}"/>
   
 <!-- jquerytools -->
    <script src="${resource(dir: 'js/streamlined', file: 'jquery.tools.min.js')}"></script>
    <!-- highcharts -->
<%--    <script src="${resource(dir: 'js', file: 'highcharts.js')}"></script>--%>
<%--    <script type="text/javascript" src="${resource(dir: 'js/streamlined', file: 'global.js')}"></script>--%>
<%--    <script type="text/javascript" src="${resource(dir: 'js/streamlined', file: 'jquery.tools.min.js')}"></script>--%>
<%--    <script type="text/javascript" src="${resource(dir: 'js/streamlined', file: 'jquery.tables.js')}"></script>--%>
<%--    <script type="text/javascript" src="${resource(dir: 'js/streamlined', file: 'global.js')}"></script>--%>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css/streamlined', file: 'custom.css')}"/>
 	<link rel="stylesheet" href="http://code.jquery.com/ui/1.9.2/themes/base/jquery-ui.css" />
    <script src="http://code.jquery.com/jquery-1.8.3.js"></script>
    <script src="http://code.jquery.com/ui/1.9.2/jquery-ui.js"></script>
    <script type="text/javascript">
	    $(function() {
	    	$("#tabs").tabs()
	    		.addClass( "ui-tabs-vertical ui-helper-clearfix" );
	        $( "#tabs li" ).removeClass( "ui-corner-top" ).addClass( "ui-corner-left" );
	    });
    </script>
   
    <g:javascript library="application"/>
 <%--<script type="text/javascript">
    $(document).ready(function(){
    	$('.attachmentImg').load(setImagesWidth);
    });
    var setImagesWidth = function(){
    	$('.attachmentImg').each(function (){
    			this.removeAttribute("style");
    			this.style.height=200+"px";
    			this.style.width=178+"px";
    			$(this).parent().css('width', '178px');
    			$(this).parent().css('height', '200px');
    	});  
    }
    </script>
  --%><style type="text/css">
  .grid-view li .avatar {
	  height: 285px;
	  width: 178px;
  }
  .grid-view li .avatar img{
	  height: 200px;
	  width: 178px;
  }
  .grid-view li .avatar img {
    -moz-border-radius: 4px;
    -webkit-border-radius: 4px;
    -khtml-border-radius: 4px;
    border-radius: 4px;
    -moz-box-shadow: 0 0 3px #777;
    -moz-box-shadow: 0 0 3px rgba(0,0,0,0.8);
    -webkit-box-shadow: 0 0 3px #777;
    -webkit-box-shadow: 0 0 3px rgba(0,0,0,0.8);
    -khtml-box-shadow: 0 0 3px #777;
    -khtml-box-shadow: 0 0 3px rgba(0,0,0,0.8);
    box-shadow: 0 0 3px #777;
    box-shadow: 0 0 3px rgba(0,0,0,0.8);
}
.grid-view li{
border: none;
}
.grid-view li .avatar div.formtampCopy{
 height: 85px;
 width: 178px;
 background: gray;
 -moz-border-radius: 4px;
    -webkit-border-radius: 4px;
    -khtml-border-radius: 4px;
    border-radius: 4px;
    -moz-box-shadow: 0 0 3px #777;
    -moz-box-shadow: 0 0 3px rgba(0,0,0,0.8);
    -webkit-box-shadow: 0 0 3px #777;
    -webkit-box-shadow: 0 0 3px rgba(0,0,0,0.8);
    -khtml-box-shadow: 0 0 3px #777;
    -khtml-box-shadow: 0 0 3px rgba(0,0,0,0.8);
    box-shadow: 0 0 3px #777;
    box-shadow: 0 0 3px rgba(0,0,0,0.8);
    font-size: small;
    font-family: sans-serif;
}
    .ui-tabs-vertical { width: 70em;margin:auto; }
    .ui-tabs-vertical .ui-tabs-nav { padding: .2em .1em .2em .2em; float: left; width: 12em; }
    .ui-tabs-vertical .ui-tabs-nav li { clear: left; width: 100%; border-bottom-width: 1px !important; border-right-width: 0 !important; margin: 0 -1px .2em 0;overflow:hidden; }
    .ui-tabs-vertical .ui-tabs-nav li a { display:block; }
    .ui-tabs-vertical .ui-tabs-nav li.ui-tabs-active { padding-bottom: 0; padding-right: .1em; border-right-width: 1px; border-right-width: 1px; }
    .ui-tabs-vertical .ui-tabs-panel { padding: 1em; float: right; width: 55em;}
  </style>
 </head>


<body style="background: #FFF;">
<div id="wrapper">
    <header>
    <div class="container_8 clearfix">
       <h1 class='grid_2'><a href='${grailsApplication.config.grails.serverURL}'><g:message code="menu.oneapp" default="Form Builder"/></a></h1>
    <nav class="grid_5">
    		
    	</nav>
    </div>
</header>
    <section>
        <div class="container_6 clearfix" style="width: 97%;">
        	<div id="tabs">
				<ul class="tabs mainTabs">
				    <li><a href="#tabs-1">Default</a></li>
				    <li><a href="#tabs-2">Notification</a></li>
				    <li><a href="#tabs-3">Rules</a></li>
				    <li><a href="#tabs-4">Integration asdf asdf adsf a fdsaas</a></li>
				</ul>
				
				<%--Default tab--%>
				<div id="tabs-1">
						hEllo1
				</div>
				<%--Default tab--%>
				<div id="tabs-2">
						hEllo2
				</div>
				<%--Default tab--%>
				<div id="tabs-3">
						hEllo3
				</div>
				<%--Default tab--%>
				<div id="tabs-4">
						hEllo4
				</div>
			</div>
          <ul  class="listing clearfix grid-view">
             <g:each in="${formTemplateInstanceList}" var="ft">
              <li class="contact clearfix current document" style="margin: 20px;padding: 0px;">
                   <div class="avatar">
                   <g:if test="${ft?.attachments[0] }">
                   <%imgURL = grailsApplication.config.grails.serverURL+'/preview/formImagePath/'+ft?.attachments[0]?.id %>
                   <img src="${imgURL}" >
                   </g:if>
                   <g:else>
                   <div style="height: 200px;width: 178px;background: #E2C3C3"> No image </div>
                   </g:else>
                  		 <div class="formtampCopy"><div style="height: 45px; padding-top: 10px;">
                  		 <div style="height:15px;overflow: hidden;padding-left: 3px;"><b>Name</b>: ${ft.name}</div>
                  		 <div style="height:15px; overflow: hidden;padding-left: 3px;"><b>Category</b>: ${ft.category}</div>
<%--                  		 <div style="height:15px; overflow: hidden;padding-left: 3px;"><b>Form Name</b>: ${ft.form}</div>--%>
                  		 </div>
                  		 <a href="${createLink(controller: 'formTemplate',action:'copyTemplate',id:ft.id)}"  style="overflow: hidden;
                   		text-overflow: ellipsis;width: 178px;color: #F3F3CE;padding: 4px 0px;" class="button button-green" target="_blank">${message(code: 'default.button.Copy.label', default: 'Copy')}</a>
                  		 </div>
                  </div>
              </li>
              </g:each>
          </ul>
        </div>
     </section>
  
</div>
  <g:render template="/layouts/footer"/>
</body>

</html>
