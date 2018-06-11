<%@ page import="com.oneapp.cloud.core.ActivityFeed" %>
<%@ page import="com.oneapp.cloud.core.User" %>
<% 				 
   				   def total
            	   if (viewName == "Activity Feed" )
            	   	total = totalCompFeeds
            	   else 
            	    total = totalFeeds
            	%>
<!DOCTYPE html>
<html>
<head>

<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'DasMain.css')}" />
 <!-- markitup! -->

<script type="text/javascript" src="${resource(dir:'markitup',file:'jquery.markitup.js')}" />
<!-- markItUp! toolbar settings -->
<script type="text/javascript" src="${resource(dir:'markitup/sets/default',file:'set.js')}" />
<!-- markItUp! skin -->
<link rel="stylesheet" media="screen" href="${resource(dir:'markitup/skins/simple',file:'style.css')}" />
<!--  markItUp! toolbar skin -->
<link rel="stylesheet" media="screen" href="${resource(dir:'markitup/sets/default',file:'style.css')}" />

<script type="text/javascript">
$(document).ready(function(){
	//$('.markItUpTextarea').markItUp(mySettings, { root:"${resource(dir:'markitup/skins/simple')}"});
});


</script>


<style>
.markItUp {
    width: 390px;
}
</style>

<title>Activity Feed</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="mainFeed">
</head>
<body>
	<section class="main-section grid_7">
    	<div class="main-content grid_4 alpha">
			<g:render template="share"  model="[userList:userList,groupList:groupList]"/>
			<div id="nofeedMessage" style="display:none;float:left;width:100%;">
				<ul class="listing list-view" style="float:none;width:80%;margin:auto;">
					 <div class="message warning" style="text-align:center;">
	           			<h3>No feeds Available</h3>
	       			</div>
	             </ul>	
            </div>
			<ul class="listing list-view"  id="activityFeedViewContainer">
				<li style="text-align:center;background:none;" id="loadWait">
					<img src="${request.getContextPath()}/images/loadingAnimation.gif"/>
				</li>
			</ul>
			
			<ul class="listing list-view">
				<li style="text-align:center;border-top:none;display:none;background:none;" id="loading">
					<div style="background: #f4f4f4; line-height: 25px; width: 400px; margin-left: 175px;"><img src="${request.getContextPath()}/images/facebook_loading.gif"/></div>
				</li>
				<li style="text-align:center;border-top:none;display:none;background:none;" id="nomoreresults">
					<div style="background: #f4f4f4; line-height: 25px; width: 400px; margin-left: 175px;">Sorry, no more results</div>
				</li>
			</ul>
			<div id="question-modal-content">
				<div id="osx-modal-title" class="questionTitle">Share/Unshare Feed</div>
					<div class="close"><a href="javascript:;" class="simplemodal-close">x</a></div>
					<div id="osx-modal-data">
						<g:form name="shareUnshareForm" url="[ controller: 'activityFeed', action: 'feedShareUnShare']" >
							<label for="shareUnshareWit"style="margin-left:5px;color:#0066FF;font-weight:bold;">Shared With :</label>
							<input type="text" id="demo-input-populate" name="toMessage" style="box-shadow:none;width: 350px;"/>
							<input type="hidden" name="id" id="activityFeedId" value="">
							<input type="hidden" name="shareUnshareWith" id="shareUnshareWith" value=""><br/>
							<div id="feedContent" style="margin-left:5px;overflow:auto;max-width:570px;max-height:450px;"></div>
							<div style="margin-left:5px;color:#0066FF;font-weight:bold;"><span style="color:#0066FF;">Shared By :</span><span id="feedSharedBy"></span></div>
							<br/>
							<p><button class="button button-blue simplemodal-close" id="questionButton" onclick="submitShareUnshareForm()">Save Changes</button></p>
						</g:form>
					</div>
				</div>		
			</div>	
			<div id="approval-modal-content">
				<div id="osx-modal-title" class="approvalTitle">Task</div>
				<div class="close"><a href="#" class="simplemodal-close">x</a></div>
				<div id="osx-modal-data">
					<label for="question">&nbsp;</label>
					
					<table style="margin:2px 55px;">
						<tr>
							<td colspan="3">
								<span>This feed will be tagged as task with following details</span>
							</td>
						</tr>
						<tr>
							<td>
								<span>Due Date</span>
							</td>
							<td>
								:&nbsp; &nbsp;
							</td>
							<td>
								<input type="text" name="option1" id="option1" onclick="$(this).dateinput({format:'mm/dd/yyyy',trigger:false}).trigger('focus')" placeholder="mm/dd/yyyy"/>
							</td>
						</tr>
						<tr>
							<td>
								<span>Reference Id</span>
							</td>
							<td>
							:&nbsp; &nbsp;
							</td>
							<td>
								<input type="text" name="referenceId" id="referenceId" placeholder="" readonly="readonly"/>
							</td>
						</tr>
					</table>
					<p><button class="button button-blue simplemodal-close" onclick="updateFeedAsTask()" id="approvalButton">Save</button></p>
				</div>
			</div>
	</section>
		<script>
			var shareUnshareList;
			var operationFeedId = 0;
			function showShareUnsharePopUp(feedId){
				shareUnshareList = new Array()	
				$("#activityFeedId").val(feedId);
				$("#feedSharedBy").html($("#feedOwner"+feedId).html());
				//$("#feedContent").html($("#container"+feedId).html());
				//$("#feedContent tr").show()
				//$("#feedContent td").show()
				//$("#feedContent td").css("white-space","normal")
				 $.ajax({
                             type : "POST",
                             url : '${request.getContextPath()}/activityFeed/feedShareDetails?activityFeedId='+feedId,
                             success: function(data) {
                             	 $("#demo-input-populate").tokenInput( data[0],
						      { theme:"facebook",
							    propertyToSearch: "name",
						 	    resultsFormatter: function(item){ 
							 	    	return "<li><div style='display: inline-block; padding-left: 10px;'><b>" + item.type + " : </b><span class='full_name'>" + item.name + "</span></div></li>" 
						   				},
                               	 onAdd: function (item) {
                               		 	shareUnshareList[shareUnshareList.length] = {"visibility":item.type,'sharedWith':item.id}
                               		 },
                               	 onDelete: function (item) {
                                	 var deleteIndex;
                               		 	for(var i=0;i<shareUnshareList.length;i++){
                                		 		var currentObj = shareUnshareList[i]
                                		 		if(currentObj.visibility == item.type && currentObj.sharedWith == item.id)
                                		 			deleteIndex = i
                                		 	}
                               		 	shareUnshareList.splice(deleteIndex,1);
                               		 },
                               		 prePopulate:data[1],
                               	preventDuplicates: true
							});
							for(var j=0;j<data[1].length; j++){
								shareUnshareList[shareUnshareList.length] = {"visibility":data[1][j].type,'sharedWith':data[1][j].id}
							}
                             },
                             error : function() {
                                     alert("Sorry, The requested property could not be found.");
                             }
                     	});
				}
				
				function updateFeedAsTask(){
					shareUnshareList = new Array()	
					var dueDate = $("[name='option1']").val();
					var referenceId = $("#referenceId").val()
					 $.ajax({
                             type : "POST",
                             url : '${request.getContextPath()}/activityFeed/updateFeedAsTask',
                             data: {referenceId:referenceId,dueDate:dueDate,activityFeedId:operationFeedId},
                             success: function(data) {
                            	 if(data != "Error"){
                            		 $("#activityFeed"+operationFeedId).remove()
                                	 $("#activityFeedViewContainer").prepend(data)
                                	 showShareOptionPopup();
	                     	         showHideSetting();
	                     	         showOverLayPopup();
                                	 $('body').animate({scrollTop: 0},1000);
                                }
                             },
                             error : function() {
                                     alert("Sorry, The requested property could not be found.");
                             }
                     	});
				}

				function updateFeedStatus(feedType,status,feedId){
					 $.ajax({
                            type : "POST",
                            url : '${request.getContextPath()}/activityFeed/changeStatus',
                            data: {status:status,id:feedId,feedType:feedType},
                            success: function(data) {
                           	 if(data != "Error"){
                           		 $("#activityFeed"+feedId).remove()
                               	 $("#activityFeedViewContainer").prepend(data)
                               }
                           	 showShareOptionPopup();
                    	     showHideSetting();
                    	     showOverLayPopup();
                            },
                            error : function() {
                                    alert("Sorry, The requested property could not be found.");
                            }
                    	});
				}
				
				function submitShareUnshareForm(){
							$("#shareUnshareWith").val(JSON.stringify(shareUnshareList));
							if($("#shareUnshareWith").val()=="[]"||shareUnshareList.length<=0){
								$(".success p").html("Please specify at least one to share")
								showMessage();
								return;
							}else{
								loadScreenBlock();
								document.shareUnshareForm.submit();
							}
					}

				function showHideComments(activityFeedId) {
					var commentDivName = "commentDiv"+activityFeedId
						if ($("[name="+commentDivName+"]").is(':hidden')) 
							$("[name="+commentDivName+"]").show();
						else
							$("[name="+commentDivName+"]").hide();	
					}

				function deleteFeedForm(activityFeedId){
					var deleteFeedCheck = confirm("Are you sure?");
					if(deleteFeedCheck){
						var feedFormName = "deleteFeed"+activityFeedId
						var thisForm = $("[name="+feedFormName+"]")[$("[name="+feedFormName+"]").length-1];
						$.ajax({
							  type:"post",
							  url: "${request.getContextPath()}/activityFeed/deleteFeed",
							  data: ($(thisForm)).serialize(),
							  success: function(){
								$("#activityFeed"+activityFeedId).remove()
								$(".success p").html("Feed sucessfully deleted")
								if($("#activityFeedViewContainer").children().size() == 0){
									$("#nofeedMessage").show();
								}
								showMessage();
							  },
						      failure : function(){
						    	alert("Error occured")
							   }
							});
					}else{
						return;
					}
				}
				

				function setFeedId(activityFeedId,dueDate){
					operationFeedId = activityFeedId
					$("#referenceId").val("#"+activityFeedId)
					$("#option1").val(dueDate)
				}

				function attachmentCommentFile(activityFeedId){
					$("#commentFileUpload"+activityFeedId).slideToggle('slow');
					$("#attachCommentFile"+activityFeedId).hide();
				}
				function attachMoreFile(obj){
					$("<input type='file' name='file' id='attachFileToFeed' style='width: 220px;'><br />").insertBefore(obj);
				}	

				function addCommentTrTd(activityFeedId){
					$('#commentTable'+activityFeedId).append($('<tr id="newCommentTR"><td id="newComment"></td></tr>'))
				}
				
				function addCommentSuccess(activityFeedId){
					window['commentCount'+activityFeedId] = window['commentCount'+activityFeedId] + 1; 
					var newCommentCount = "<img style='vertical-align: bottom; padding-right: 3px;' src='${request.getContextPath()}/images/comment_icon.png'>comments ("+window['commentCount'+activityFeedId]+")"
					$("#commentLinkCount"+activityFeedId).html(newCommentCount);
					$('#newComment').removeAttr('id');
					$('#newCommentTR').removeAttr('id');
					$('#commentText'+activityFeedId).val('');
					$("#commentText"+activityFeedId).focus();
				}	
				
				function addErrorMessage(activityFeedId){
					$('#newCommentTR').remove();
					$('#messageShow').fadeIn('slow');
					$('#messageShow').html('Comment could not be added');
					setTimeout("$('#messageShow').fadeOut('slow',function(){$('#messageShow').hide();$('#messageShow').html('')});",2000);
				}
					
		</script>
</body>
</html>
