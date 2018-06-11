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
	<icep:bridge contextPath="${request.getContextPath()}"/>
    <link rel="shortcut icon" href="${resource(dir: 'images', file: 'favicon.ico')}" type="image/x-icon"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'grid_min.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'style_min.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'messages.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'forms_min.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'tables_min.css')}"/>
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'chat.css')}"/>
    <link rel="stylesheet" href="${resource(dir: 'css', file: 'main_min.css')}"/>
	<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'button.css')}"  />
	<link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'dropdownstyle.min.css')}"/>
	<link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'token-input-facebook.css')}" />
    <link rel="stylesheet" type="text/css" href="${resource(dir: 'css', file: 'osx.css')}" />
    <link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'custom.css')}"/>
    <gui:resources components="autoComplete" css="fonts-min.css,autocomplete.css"/>
	<link rel="stylesheet" media="screen" href="${resource(dir: 'css', file: 'DasMain-min.css')}"/>
    <script src="${resource(dir: 'js', file: 'jquery.tools.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'global-min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'share.min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'dropdownscript-min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.simplemodal.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'osx-min.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'chat.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.tokeninput.min.js')}" ></script>
 	<script type="text/javascript" src="${resource(dir: 'js', file: 'scrollpagination.js')}" ></script>

    <script type="text/javascript">
        //define your itemSelect handler function:
        var pageOffset = 0
        var maxFeedsize = 0
        var feedFilterTag = "${filterTag}"
        var requestUrlForAjax = "${request.getContextPath()}/"
       	
        $(document).ready(function(){
        	$(".attUploadForm").each(function(){
            	var $thisForm = $(this)
            	this.onsubmit = function(){
            		var allow = false;
            		$thisForm.find("input[type='file']").each(function(){
                		if($(this).val()!='')
                    		allow = true;
                	})
                	if(!allow){
                		$(".success p").html("${message(code:'feed.no.attachment','default':'Please select file to upload')}")
                		showMessage();
                    }else{
                        loadScreenBlock();
                    }
                	return allow;
                }
            });
        	getAcitvityFeedView(feedFilterTag)
        });
        
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
        
        function getAcitvityFeedView(filterTag){
        	feedFilterTag = filterTag
        	xhr = $.ajax({
    	        type : "POST",
    	        url : "${request.getContextPath()}/dashboard/activityFeedView/"+filterTag,
    	        success: function(data) {
        	        $("#loadWait").hide();
        	        if (typeof $totalFeedCount == 'undefined'){
      	        		$totalFeedCount = $("#totalFeedCount")
      	        		$totalTaskCount = $("#totalTaskCount")
      	        		$recentTaskCount = $("#recentTaskCount")
      	        		$aFVC = $("#activityFeedViewContainer")
      	  	        }
        	        $totalFeedCount.html(data.total);
        	        $totalTaskCount.html(data.taskCount);
        	        $recentTaskCount.html(data.recentTaskCount)
        	        maxFeedsize = data.total
    	        	if(data.feedList.length != 0){
    	        		$aFVC.html('');
            	        for(var i=0; i<data.feedList.length; i++){
            	        	$aFVC.append(data.feedList[i].html)
            	        }
            	        if(filterTag != ""){
	            	        $(".success p").html("Feed filtered for "+filterTag)
							showMessage();
            	        }
            	        pageOffset = data.pagination+10;
            	        showShareOptionPopup();
            	        showHideSetting();
            	        showOverLayPopup();
            	        scrollPaginationControl()
            	    }else if(data.feedList.length == 0 && filterTag != ""){
	            	        $(".success p").html(" No feed for "+filterTag)
							showMessage();
            	        
                	}else if(data.feedList.length == 0){
                		$("#nofeedMessage").show();
                    }
        	        $(".showToolTip").mouseover(function(){
						$(this).attr('title',$(this).attr("attrt").replace(/<(?:.|\n)*?>/gm, ' '));
	            	});
    	        },
    	        error : function() {
    	        }
    		});
    	}

        function scrollPaginationControl(){
        	$('#activityFeedViewContainer').scrollPagination({
        		'contentPage': "${request.getContextPath()}/dashboard/activityFeedView/"+feedFilterTag, // the url you are fetching the results
        		'contentData': {}, // these are the variables you can pass to the request, for example: children().size() to know which page you are
        		'scrollTarget': $(window), // who gonna scroll? in this example, the full window
        		'heightOffset': 5, // it gonna request when scroll is 10 pixels before the page ends
        		'beforeLoad': function(){ // before load function, you can display a preloader div
        			$('#loading').fadeIn();	
        		},
        		'afterLoad': function(elementsLoaded){ // after loading content, you can use this function to animate your new elements
        			 $('#loading').fadeOut();
        			 var i = 0;
        			 $(elementsLoaded).fadeInWithDelay();
        			
        		}
        	});
        	
        	// code for fade in element by element
        	$.fn.fadeInWithDelay = function(){
        		var delay = 0;
        		return this.each(function(){
        			$(this).delay(delay).animate({opacity:1}, 200);
        			delay += 100;
        		});
        	};
        		   
        }

        function setCountAndFeed(){
  		  $.ajax({ 
  	  		url: "${grailsApplication.config.grails.serverURL}/alert/alert",
  	          type: "GET",
  	          dataType: "json",
  	          success: function(data) {
  	        	if (typeof $totalFeedCount == 'undefined'){
  	        		$totalFeedCount = $("#totalFeedCount")
  	        		$totalTaskCount = $("#totalTaskCount")
  	        		$recentTaskCount = $("#recentTaskCount")
  	        		$aFVC = $("#activityFeedViewContainer")
  	  	        }
  	        	$totalFeedCount.html(data.total);
	  	        	$totalTaskCount.html(data.taskCount);
	    	        $recentTaskCount.html(data.recentTaskCount)
  					for(var i=0; i<data.feedList.length; i++){
  						if($("#activityFeed"+data.feedList[i].id).length==0){
  							$aFVC.prepend(data.feedList[i].html)
  						}
  						var newCommentCount = "<img style='vertical-align: bottom; padding-right: 3px;' src='${request.getContextPath()}/images/comment_icon.png'>comments ("+data.feedList[i].totalFeedComents+")"
	  	  				$("#commentLinkCount"+data.feedList[i].id).html(newCommentCount)
	  	  				$("#nofeedMessage").hide();
  	  	       		}
  					for(var j=0; j<data.commentList.length; j++){
  		        		if($("#commentDiv"+data.commentList[j].commentId).length==0){
  			        		var commentMsg = "<tr><td>"+data.commentList[j].html+"</td></tr>"
  		        			$("#commentTable"+data.commentList[j].feedId).append(commentMsg)
  		        		}
  			        }
  					showShareOptionPopup();
  	     	        showHideSetting();
  	     	        showOverLayPopup();
  	              
  	          },error: function(){
  				}
  		  });
  	}

       
         
      
    </script>
 
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
		setTimeout("dashBoardMessageFade()",10000)
	}
	function dashBoardMessageFade(){
	  $('#dashBoardMessageId').fadeOut('slow')
	 }

</script>
<style type="text/css">
	.createFormInstance{
		position:relative;
		width:4px;
		height:6px;
		top:0px;
		left:5px;
	}
</style>
<script type="text/javascript" src="${resource(dir: 'js', file: 'jquery.tokeninput.js')}" ></script>
</head>


<body>
<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;z-index: 10000;background-color: #fff;opacity:0.5;">
  <img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
</div>
<div id="dashBoardMessageId" style="width:100%;margin:auto;position: absolute;display: block;text-align: center;">
	<div class="message success closeable" style="padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 15px; position: relative;   margin-top: auto; margin-right: auto; margin-bottom: auto; margin-left: auto; width: auto; top: 50px; font-weight: bold; z-index: 1001; display: none;">
		<span class="message-close" style="display:block;"></span>
             <p>
                 <g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}"/>
	            <% if ( filterTag !=null ) {%>
	                		<input type="button" class="button1 small green" value="${tagFilter}"/>
	             <%}%>
             </p>
         </div>
       </div>
 	<g:if test="${flash.message}">
 		
          <script>
             	showMessage();
             </script>
        
    </g:if>
<style type="text/css">
	<g:render template="/layouts/custom"/>
</style>

<div id="wrapper">
    <g:render template="/layouts/header"/>
    <section style="padding-top:43px;">
        <div class="container_8feed clearfix">
        	
            <g:render template="/layouts/sidebar"/>
            <g:layoutBody/>
        </div>
        <div id="push"></div>
    </section>
    <g:render template="/layouts/footer"/>
</div>
</body>

</html>