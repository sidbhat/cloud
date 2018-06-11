var pPar = 0;
function x(){
	var mainPage = $('#mainpage');//for mobile views
	if(mainPage.size()>0){
		parent.postMessage($("#mainpage").innerHeight()+2+"|"+(pPar==0?'scroll':''),"*");
	}else{
		parent.postMessage($(".formBody").innerHeight()+2+"|"+(pPar==0?'scroll':''),"*");
	}
}
function share(comment)
											{
												document.postForm.postactivity.value = comment;
												//alert(document.postForm.postactivity.value);
											}
											function submitComment(id,comment)
											{
												document.f.comment.value = comment;
												document.f.id.value = id;
												document.f.submit();
											}
											function approveItem(id,form,v)
											{
												document.form.share.value = v;
												document.form.id.value = id;
												document.form.action.value="approve"
												document.form.submit();
											}
											
											function rejectItem(id,form,v)
											{
												document.form.share.value = v;
												document.form.id.value = id;
												document.form.action.value="reject"
												document.form.submit();
											}
											function replyItem(id,form,v)
											{
												document.form.share.value = v;
												document.form.id.value = id;
												document.form.action.value="reply"
												document.form.submit();
											}
											
											function submitform1(form,v)
											{
												
												document.form.source.value = "company";
												document.form.visibility.value = "GROUP";
												document.form.share.value = v;
												document.form.submit();
											}
											function submitform2(form)
											{
												document.form.source.value = "company";
												document.form.visibility.value = "COMPANY";
												document.form.submit();
											}
											function submitform3(form,v)
											{
												
												document.form.source.value = "company";
												document.form.visibility.value = "USER";
												document.form.share.value = v;
												document.form.submit();
											}
											
											
											function submitform1(v)
											{
												
												document.postForm.source.value = "company";
												document.postForm.visibility.value = "GROUP";
												document.postForm.postactivity.value = htmlEncode(document.postForm.postactivity.value)
												document.postForm.sharedWith.value = v;
												formSubmitForFeed()
											}
											function submitform2()
											{
												document.postForm.source.value = "company";
												document.postForm.visibility.value = "COMPANY";
												document.postForm.postactivity.value = htmlEncode(document.postForm.postactivity.value)
												formSubmitForFeed()
											}
											
											function htmlEncode(value){
											    /*if (value) {
											        return jQuery('<div />').text(value).html();
											    } else {
											        return '';
											    }*/
												return value
											}
											function submitform3(v)
											{
												document.postForm.source.value = "company";
												document.postForm.visibility.value = "USER";
												document.postForm.sharedWith.value = v;
												document.postForm.postactivity.value = htmlEncode(document.postForm.postactivity.value)
												formSubmitForFeed()
											}
											function submitform4(v)
											{
												
												document.postForm.source.value = "company";
												document.postForm.visibility.value = "ROLE";
												document.postForm.sharedWith.value = v;
												document.postForm.postactivity.value = htmlEncode(document.postForm.postactivity.value)
												formSubmitForFeed()
											}
											function submitform5(v)
											{
												
												document.postForm.source.value = "company";
												document.postForm.visibility.value = "DEPARTMENT";
												document.postForm.sharedWith.value = v;
												document.postForm.postactivity.value = htmlEncode(document.postForm.postactivity.value)
												formSubmitForFeed()
											}
											function submitformfb()
											{
												document.postForm.source.value = "facebook";
												document.postForm.submit();

											}
											function submitformtw()
											{
												document.postForm.source.value = "twitter";
												document.postForm.submit();

											}
											function submitformli()
											{
												document.postForm.source.value = "linkedin";
												document.postForm.submit();

											}
											
											function loadScreenBlock(){
												document.getElementById("spinner").style.display = "block";//$("#spinner").show();
												
											}
											function formSubmitForFeed(){
//												if($("#sharedWith").val()=="[]"||shareList.length<=0){
												if($("#sharedWith").val()=="[]"||shareList.length<=0){
													$(".success p").html("Please specify at least one to share")
													showMessage();
													return;
												}
												else if($("#postactivity").val()==""){
													$(".success p").html("Please write something to share")
													showMessage();
													//createPopup("Please write something to share")
													$("#postactivity").focus();
													return;
												}else{
													document.postForm.submit();
													loadScreenBlock();
												}
											}
											
											function createPopup(msg){
												var elementContent = '<div class="message success closeable"  style="padding:5px 5px 5px 15px;position: absolute;right:0px;left:0;margin:auto;width:235px;top:50px;font-weight:bold;z-index:1000;"><span class="message-close" style="display:block;"></span>'
													elementContent += '<span class="message-close"></span><span class="message-close"></span><span class="message-close" style="display:block"></span><p>'+msg+'</p></div>'
													elementContent += '<script></script>'
													$(".container_8feed").append(elementContent);
											}
											
											
											var moodScaleMap = ["Dissatisfied","Neutral","Satisfied","VerySatisfied"]


											function moodToScale(mood,fieldName){
												$('input[name="'+fieldName+'"]').each(function(){
													if(this.value == moodScaleMap[mood-1]){
														$(this).attr('checked','checked');
													}
												});
											}
											
											function setMood(scaleValue){
												var value = 75;
												if(scaleValue != null && scaleValue != "null"){
													for(var i=0;i<moodScaleMap.length;i++){
														if(moodScaleMap[i] == scaleValue){
															value = (i+1)*25;
														}
													}
												}
												return value;
											}
											
											function setRatingMobile(rate,fieldName,currentObj){
												var $parentNode = $(currentObj.parentNode)
												$('input[name="'+fieldName+'"]',$parentNode).val(moodScaleMap[Math.round(rate/25).toFixed(0)-1]);
												var $face = $('a',$parentNode);
												$face.attr('style','background-image:url(/form-builder/plugins/form-builder-0.1/images/face-'+Math.round(rate/25).toFixed(0)+'.png) !important;left:'+$face.css('left')+';');
											}
											$(document).ready(function(){ 
												try{
													setDateTypes();
												}catch(e){}
												setTimeout("setImageWidthAndHeight(1)",100);
												setTimeout("setVideoWidthAndHeight()",150)
									        	$(".showToolTip").mouseover(function(){
													$(this).attr('title',$(this).attr("attrt"));
								            	});
											});
											function setImageWidthAndHeight(imgElement){
												try{
													parent.postMessage("0|","*");
													$("body").height("auto");
													if(arguments.length==0)pPar=0;else pPar=1
													setTimeout("x()",10);
												}catch(e){}
												var setForThisImg = function(ele){
		                                        	var $imgObj=$(ele);
		                                        	$imgObj.hide();
		                                        	var userSpecifiedWidth=($imgObj.attr("userSpecifiedWidth")==""?$imgObj.width():$imgObj.attr("userSpecifiedWidth"))*1;
			                                        var userSpecifiedHeight=($imgObj.attr("userSpecifiedHeight")==""?$imgObj.height():$imgObj.attr("userSpecifiedHeight"))*1;
			                                        var $availableWidth=($imgObj.parent().innerWidth())
			                                        if($availableWidth<userSpecifiedWidth){
			                                        	var ratio= $availableWidth / userSpecifiedWidth
				                                        var $availableHeight= userSpecifiedHeight * ratio
				                                      	$imgObj.width($availableWidth+"px")
														$imgObj.height($availableHeight+"px")
													}else{
														$imgObj.width(userSpecifiedWidth+"px")
														$imgObj.height(userSpecifiedHeight+"px")
													}
													$imgObj.css("display","inline");
												}
												if(typeof imgElement != 'undefined' && imgElement != '1'){
													setForThisImg(imgElement);
												}else{
													$(".imgDisplay").each(function(){
														setForThisImg(this);
													});
												}
											}
											function setVideoWidthAndHeight(){
												$(".videoDisplay").each(function(){
									            	var $vObj=$(this);
									            	$vObj.hide();
									            	var userSpecifiedWidth=($vObj.attr("userSpecifiedWidth")==""?$vObj.width():$vObj.attr("userSpecifiedWidth"))*1;
									                var userSpecifiedHeight=($vObj.attr("userSpecifiedHeight")==""?$vObj.height():$vObj.attr("userSpecifiedHeight"))*1;
									                var $availableWidth=($("#videoId").parent().innerWidth())-10
									                if($availableWidth<userSpecifiedWidth){
									                	var ratio= $availableWidth / userSpecifiedWidth
									                    var $availableHeight= userSpecifiedHeight * ratio
									                    $vObj.width($availableWidth+"px")
														$vObj.height($availableHeight+"px")
													}else{
														$vObj.width(userSpecifiedWidth+"px")
														$vObj.height(userSpecifiedHeight+"px")
													}
									                $vObj.css("display","inline");
												});
											}
											function setDateTypes(){
												$(".date").each(function(){
													$(this).val($("#"+$(this).attr("name")+"Value").val());
													$(this).attr('autocomplete','off')
													$(this).attr('itsType',$("#"+$(this).attr("name")+"Value").attr('itsType'));
												});
											}
											function removeScriptTag(value){
												while(value.indexOf("<script")>-1){
									        		
									        		var endingScript = value.indexOf(">",value.indexOf("<script"))
									        		var replaceString = value.substring(value.indexOf("<script"),(endingScript>-1?(endingScript+1):value.length))
									        		value = value.replace(replaceString,"")
									        	}
									        	var $dummyEle = $('<div>'+value+'</div>');
									        	value =  $dummyEle.html();
									        	$dummyEle.remove();
									        	return value
											}
											function timerAction(message)
											{
												setTimeout(function() {alert(message)},1200000)
											}
function OpenNewWindow(url){window.open(url, "_blank", "")}
function saveMainFormAndOpenSubForm(subFormid,subFormfn){
	$('#spinner').show();
	$('#subFormid').val(subFormid);
	$('#subFormfn').val(subFormfn);
    $("input").removeAttr("disabled");
    $("select").removeAttr("disabled");
    $("textarea").removeAttr("disabled");
	$('.button-green').trigger('click')
	
}
function saveFormOpenSubFormMobile(subFormid,subFormfn){
	debugger;
	$('#spinner').show();
	$('#subFormid').val(subFormid);
	$('#subFormfn').val(subFormfn);
    $("input").removeAttr("disabled");
    $("select").removeAttr("disabled");
    $("textarea").removeAttr("disabled");
	$('.ui-btn-hidden').trigger('click')
	
}