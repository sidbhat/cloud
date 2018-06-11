<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
<%--<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">
--%>
<html>
	<head>
		<link rel="apple-touch-icon" href="${request.getContextPath()+'/images/favicon.ico'}" />
		<title><g:layoutTitle default="Form Viewer"/></title>
		<link rel="shortcut icon" href="${request.getContextPath()+'/images/favicon.ico'}" type="image/x-icon"/>
		<link rel="stylesheet" type="text/css" media="screen" href="${resource(dir: 'css', file: 'jquery-formbuilder.min-0.1.css')}"/>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'utility.js')}"></script>
		<g:javascript library="jquery/jquery-1.4.2.min" plugin="jquery"/>
		<uf:resources type="css" style="default"/>
		<langs:resources/>
		<icep:bridge contextPath="${request.getContextPath()}"/>
		<script type="text/javascript" src="${resource(dir: 'js', file: 'formula.js')}"></script>
		<script type="text/javascript" src="${request.getContextPath()}/js/rulesImpl.js"></script>
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
				showOverLayPopup()
				<%if(params.controller != 'embed'){%>
				getAcitvityFeedView(feedFilterTag)
				<%}%>
				try{
					setDateTypes()
				}catch(e){}
				$(".ctrlHolder:not(.ppHolder)").click(function(){
					$(".ctrlHolder").removeClass("ctrlHolderSelected");
					$(this).addClass("ctrlHolderSelected");
					$(this).fadeIn("slow");
				});
			});
			var _gaq = _gaq || [];
			_gaq.push(['_setAccount', 'UA-25353534-1']);
			_gaq.push(['_setDomainName', '.oneappcloud.com']);
			_gaq.push(['_trackPageview']);
	
			(function() {
				var ga = document.createElement('script'); ga.type = 'text/javascript'; ga.async = true;
				ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';
				var s = document.getElementsByTagName('script')[0]; s.parentNode.insertBefore(ga, s);
			})();

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
	            	        showOverLayPopup();
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
		<script type="text/javascript" src="${request.getContextPath()+'/js/application.js'}"></script>
	    <style type="text/css">
			.apple_overlay{display:none;background-image:url(/form-builder/images/overlay/white.png);width:640px;padding:35px;font-size:11px}.apple_overlay .close{background-image:url(/form-builder/images/close-icon.png);position:absolute;right:14px;top:14px;cursor:pointer;height:30px;width:30px}.apple_overlay.black{background-image:url(/form-builder/images/overlay/transparent.png);color:#fff}
			.contentWrap{border:0px}
		    
			
	    	.buttons{
	    		font: 12px/100% 'Lucida Grande', 'Lucida Sans Unicode', 'Helvetica Neue', Helvetica, Arial, Verdana, sans-serif
	    	}
	    	.ui-autocomplete-loading{
	    		background: white url("/form-builder/images/ajax-loader.gif") no-repeat right !important;
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
		<g:if test="${params.controller == 'embed' }">
				.buttons{
					color:inherit;
					background:none;
				}
		</g:if>
		<g:elseif test="${params.controller == 'formViewer' }">
			.formBody{
				/*width:640px;*/
	    		margin:43px auto;
	    		
	    	}
	    	.formBodyCover{
				-moz-box-shadow: 1px 0px 8px 2px #aaa;
				-webkit-box-shadow:  1px 0px 8px 2px #aaa;
				box-shadow: 1px 0px 8px 2px #aaa;
				position: absolute;
				width:700px;
			}
			#wrapper > header nav > ul ul{
				list-style:none;
			}
			#wrapper > section {
				padding-top:0px;
			}
			nav ul {
			    list-style: none outside none;
			    vertical-align: baseline;
			}
			.createFormInstance {
			    height: 6px;
			    left: 5px;
			    position: relative;
			    top: 0;
			    width: 4px;
			}
			a:link, a:visited, a:hover {
			    color: #666666;
			    font-weight: bold;
			    text-decoration: none;
			}
			.clearfix table{
			   border:none;
			   margin-bottom: 0px;
			}
			.clearfix td{
			   padding:0px;
			   vertical-align: inherit;
			   font: inherit;
			}
		</g:elseif>
		<g:else>
			.formBody{
				/*width: 640px;*/
	    		margin:5px auto;
	    		
	    	}
			.formBodyCover{
				-moz-box-shadow: 1px 0px 8px 2px #aaa;
				-webkit-box-shadow:  1px 0px 8px 2px #aaa;
				box-shadow: 1px 0px 8px 2px #aaa;
			}
			
		</g:else>
		
		</style>
		<g:layoutHead/>
	</head>
<body>
	<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;left:0;top:0;z-index: 10000;background-color: #fff;opacity:0.5;">
	  <img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
	</div>
	<g:if test="${params.controller == 'formViewer' }">
		<div id="wrapper">
		    <g:render template="/layouts/header"/>
		    <section>
		        <div class="container_8 clearfix">
		            <g:render template="/layouts/sidebar"/>
		           <div align="center" style="width:100%;">
						<div class="formBody" style="text-align:left;width:640px;">
							<div class="formBodyCover">
								<div></div>
								<g:layoutBody/>
							</div>
						</div>
					</div>
		        </div>
		        <div id="push"></div>
		    </section>
		</div>
	</g:if>
	<g:else>
		<div align="center" style="width:100%;">
			<div class="formBody" style="text-align:left;">
				<div class="formBodyCover">
					<div></div>
					<g:layoutBody/>
				</div>
				<g:if test="${params.controller != 'embed' }">
					<div id="legal" font="13px/1.231 arial,helvetica,clean,sans-serif" style="margin-top:5px;"><p  style="color :#676767 ;vertical-align: baseline; background: transparent; font:13px/1.231 arial,helvetica,clean,sans-serif !important;">Powered by <a href="http://sidbhat1976.blogspot.com/" target="_blank" style="color: #666 !important;
				 	text-decoration:underline !important;font:13px/1.231 arial,helvetica,clean,sans-serif !important;"><b>Form Builder</b></a></p></div>
				 </g:if>
			</div>
		</div>
	</g:else>
<!--div id="footer">Created With Form Builder </div-->
</body>
</html>
