function checkUserLoggedIn(ele){
	$.ajax({ 
		url: '/form-builder/PF/checkUserLoggedIn',
		type: "POST",
		dataType: "json",
		success: function(data){
			if(data.loggedIn){
				userLoggedInSubmitForm();
			} else {
				showOverlay();
			}
		},
		error: function(){
			document.getElementById("spinner").style.display = "none";
			alert("Connection error");
		}
	});
	loadScreenBlock();
	var target = $(event.target);
	if(!target.hasClass('formPreviewButton') && !target.hasClass('formSettingsButton')){
		$('#extraParameter').val('');
	}
	return false;
}
function showOverlay(){
	loadScreenBlock();
	$("#overlayHelper").show();
	$('body').css('overflow','hidden');
}
function userLoggedInSubmitForm(){
	hideOverlay();
	document.getElementById("spinner").style.display = "none";
	var submitButton = $('input[type="submit"][name="_action_save"]');
	submitButton.unbind("click");
	submitButton.click(submitFormUsingButton);
	submitButton.trigger('click');
}
function hideOverlay(){
	document.getElementById("spinner").style.display = "none";
	$("#overlayHelper").hide();
	var iframe = $("#overlayHelper iframe");
	iframe.attr("src",iframe.attr("src"));
	$('body').css('overflow','auto');
}