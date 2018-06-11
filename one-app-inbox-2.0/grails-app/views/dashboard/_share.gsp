						
<g:form name="postForm"
										url="[ controller: 'activityFeed', action: 'multiFeedShare']"
										update="aresult" enctype="multipart/form-data">
										<div class="PostForm">
											&nbsp;
											
											
											 <!--div class="yui-skin-sam yui-ac">
                    <input type="text" style="display:none" id="myAC1_id" name="myAC1_id" hidden="true"/>
					<textarea class="yui-ac-input" id="postactivity" name="postactivity"
												style="height: 80px; width: 330px;"></textarea>

                    <!--input type="text" class="yui-ac-input" id="myAC1" name="myAC1" value=""--/>

                    <div class="yui-ac-container" id="cgui_04dcd93a3c175ff76e685481b088001e">
                    </div>
                </div-->
					
<!--											
<fckeditor:config CustomConfigurationsPath="${resource(dir:'js',file:'myconfig.js')}"/>	
											<fckeditor:editor
    name="postactivity"
    width="400"
    height="300"
    toolbar="ed-limited"
  >

											</fckeditor:editor>
-->

					<div id="messageTable" style="margin:0px 18px;">
									<div class="clearfix" style="position:absolute;right:10px;top:12px;">
										<ul class="action-buttons clearfix fr">
											<li style="width:95px;font-weight: normal;">
						                		<img style="margin: auto; position: absolute; top: 0pt; bottom: 0pt; left: 75px; right: 0pt;display:none;" id="loaderImg" src="${request.getContextPath()}/images/ajax-loader.gif">	                
							                </li>
							                <li style="width:95px;font-weight: normal;">
						                		<g:remoteLink style="font-weight: normal;" controller="dashboard" action="fetchEmail"  before="lockButton();"  onSuccess="unLockButton(data);" onFailure="unLockButton(data);"  name="fetchEmailBtn"  class="button button-gray">Fetch Email</g:remoteLink>	                
							                </li>
							                <li>
						                		<a href="${grailsApplication.config.grails.serverURL}/documentation/inbox_help.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
						                        class="help"></span></a>	                
							                </li> 
						            	</ul>
						            </div>
									   <table style="margin-left:30px;">
											<tr>
												<td style="color:#333333;margin-left:-5px;"><a href="javascript:;" id="postFeedIcon" class="postIcon icon-note" style="font-size:13px;">Write Post</a><a href="javascript:;" id="linkOpenattach"  class="postAreaMargin postIcon icon-attach" style="font-size:13px;">Share File</a></td>
											</tr>
										</table>
										<table style="margin-left:30px;display: none;" id="tableContentTextArea">
											<tr>
												<td style="color:#333333;" >&nbsp;</td>
											</tr>	
											<tr >												
												<td style="color:#333333;"><label> To: </label><input type="text" id="demo-input-facebook-theme" name="toMessage" style="box-shadow:none;width: 660px;"/></td>
											</tr>
											<tr>
												<td style="color:#333333;"  >&nbsp;</td>
											</tr>										
											<tr>					
												<td  >													
													<textarea class="markItUpTextarea" id="postactivity" name="postactivity"
												style="height: 75px; width: 660px;margin-top:0px;" placeholder="Share something..." ></textarea>
													<input type="file" name="file" id="attachFile" style="display:none;margin-left:0px;" class="postAreaMargin"/>
													
													<script>
													var shareList = new Array()
													$(document).ready(function(){
														 $.ajax({
								                                type : "POST",
								                                url : '${request.getContextPath()}/activityFeed/shareOption',
								                                success: function(data) {
								                                	 $("#demo-input-facebook-theme").tokenInput( data,
																	      { theme:"facebook",
																		    propertyToSearch: "name",
																	 	    resultsFormatter: function(item){ 
																		 	    	return "<li><div style='display: inline-block; padding-left: 10px;'><b>" + item.type + " : </b><span class='full_name'>" + item.name + "</span></div></li>" 
																	   				},
										                                	 onAdd: function (item) {
										                                		 	shareList[shareList.length] = {"visibility":item.type,'sharedWith':item.id}
										                                		 },
										                                	 onDelete: function (item) {
											                                	 var deleteIndex;
										                                		 	for(var i=0;i<shareList.length;i++){
											                                		 		var currentObj = shareList[i]
											                                		 		if(currentObj.visibility == item.type && currentObj.sharedWith == item.id)
											                                		 			deleteIndex = i
											                                		 	}
										                                		 	shareList.splice(deleteIndex,1);
										                                		 },
										                                	preventDuplicates: true
																		});
								                                },
								                                error : function() {
								                                        alert("Sorry, The requested property could not be found.");
								                                }
								                        	});
														});
													
														$("#linkOpenattach").click(function () {
															if ($("#tableContentTextArea").is(':hidden')){
																$("#postFeedIcon").trigger('click');
																$("#attachFile").css("display","inline");
															}else{
																if(!$("#attachFile").is(':hidden')){//shown attachment 
																	$("#postFeedIcon").trigger('click');
																}else{
																	$("#attachFile").css("display","inline");
																}
															}
														});
														
														$("#postFeedIcon").click(function () {
															$(".token-input-list-facebook").width("666px");
															$('[name="shareToTd"]').slideToggle("slow");
															$("#tableContentTextArea").slideToggle("slow");
															$("#attachFile").hide();
														});

														function lockButton(){
																$("#loaderImg").show();
																$("[name='fetchEmailBtn']").hide();
															}

														function unLockButton(data){
																$(".success p").html(data.message)
																showMessage();
																$("#loaderImg").hide();
																$("[name='fetchEmailBtn']").show();
															}

														function submitShareForm(){
															document.postForm.source.value = "company";
															$("#sharedWith").val(JSON.stringify(shareList));
															document.postForm.postactivity.value = htmlEncode(document.postForm.postactivity.value)
															formSubmitForFeed();
														}
															
													</script>
														<div class="PostShare postAreaMargin" style="width:120px;">
															<input type="button" class="button button-gray" style="width: 100px" value="${message(code: 'share.label', default: 'Share')}"
                   												 onclick="javascript:submitShareForm();"/>
															 <div id="aresult"></div>
														</div>
														<div id="functionIcon"></div>
														<div>&nbsp;</div>
													<%--<button class="fr button button-gray">Add Post</button>											
												--%></td>
											</tr>
											
											
											
									   </table>
									   <table>
											<tr>
												<td style="vertical-align:middle;">
													
												</td>
											</tr>
									   </table>
									   
									   
									   
									 </div>

											<input type="hidden" name="source" value="">
											<input type="hidden" name="shareType" id="shareType" value="">
											<input type="hidden" name="sharedWith" id="sharedWith" value="">
											&nbsp;
											&nbsp;
											
										</div>
									</g:form>
						
