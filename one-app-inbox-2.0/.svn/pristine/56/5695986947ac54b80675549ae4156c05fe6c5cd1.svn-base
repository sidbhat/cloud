function onOffSpCond(check){
	if(check.checked){
		$('#spCondDiv').slideDown("slow");
	} else {
		$('#spCondDiv').slideUp("slow");
	}
}
function setSequence(){
	$('.spCond .notiSeq').each(function(index){
		$(this).val(index)
	});
}
function notiDel(img){
	var $img = $(img);
	var $tr = $img.parent().parent();
	var savedCond=true
	var $notiId = $('.notiId',$tr)
	if($notiId.val()=='' || $notiId.val()=='null')savedCond=false;
	if($tr.parent().children().length == 1 && !savedCond)return;
	if(confirm('Are you sure?')){
		if(!savedCond)$tr.remove();
		else{
			$img.hide();
			$(".notiUndoDel",$img.parent()).show();
			$(".notiStat",$tr).val('D');
		}
	}
}
function notiUndoDel(img){
	var $img = $(img);
	var $tr = $img.parent().parent();
	$img.hide();
	$(".notiDel",$img.parent()).show();
	$(".notiStat",$tr).val('');
}
function notiAdd(addImg){
	var p = getNotiRow(addImg);
	if(p!=null){
		var $clonep=$(p.cloneNode(true));
		notiCount++;
		$('.notiId',$clonep).attr('name','conditions['+notiCount+'].id').attr('id','conditions['+notiCount+'].id').val('');
		$('.notiSeq',$clonep).attr('name','conditions['+notiCount+'].sequence').attr('id','conditions['+notiCount+'].sequence').val('');
		$('.notiStat',$clonep).attr('name','conditions['+notiCount+'].status').attr('id','conditions['+notiCount+'].status').val('');
		$('.notiFieldId',$clonep).attr('name','conditions['+notiCount+'].fieldId').attr('id','conditions['+notiCount+'].fieldId').val('');
		$('.notiOp',$clonep).attr('name','conditions['+notiCount+'].op').attr('id','conditions['+notiCount+'].op').val('');
		$('.notiVal',$clonep).attr('name','conditions['+notiCount+'].val').attr('id','conditions['+notiCount+'].val').val('');
		$('.notiCondOp',$clonep).attr('name','conditions['+notiCount+'].condOp').attr('id','conditions['+notiCount+'].condOp').val('');
		$('.notiDel',$clonep).show();
		$('.notiUndoDel',$clonep).hide();
		$clonep.insertAfter($(p));
	}
}
function validateNoti(updtNotiB){
	var returnVal=true
	var $updtNotiB = $(updtNotiB);
	try{
		setSequence()
	}catch(e){}
	//Add validations for the form here.
//	var usersSelected = $('#formSubmissionTo').val()
//	if(usersSelected==null || usersSelected.length==0){
//		if(!confirm("No user selected. Do you want to update?"))
//			returnVal=false;
//	}
	var formSubmissionToExternal = document.getElementById('formSubmissionToExternal').value;
	var formSubmissionAction = document.getElementById('formSubmissionAction').value;
	formSubmissionToExternal = jQuery.trim(formSubmissionToExternal);
	var invalidEmIds = ''
	if(returnVal && formSubmissionToExternal!='' && formSubmissionAction=='Email'){
		var formSubmissionToExternalEmails = formSubmissionToExternal.split(',');
		for(var i=0;i<formSubmissionToExternalEmails.length;i++){
			var emId = formSubmissionToExternalEmails[i];
			emId = jQuery.trim(emId)
			if(emId!=''){
				if(!isValidEmailAddress(emId)){
					if(invalidEmIds!=''){
						invalidEmIds+=', ';
					}
					invalidEmIds += emId;
					returnVal = false;
				}
			}
		}
	}
	if(!returnVal && invalidEmIds!=''){
		alert('Invalid Email Ids: '+invalidEmIds);
	}
	if(returnVal && !(document.getElementById('specialCondition').checked)){
		$(".spCond .notiStat").each(function(){
			var $notiStat = $(this);
			$notiStat.val('D')
			var notiId = $('.notiId',$notiStat.parent()).val()
			if( notiId==null || notiId=='' ){
				$notiStat.parent().parent().remove();
			}
		});
	}
	return returnVal;
}
function isValidEmailAddress(emailAddress) {
    var pattern = new RegExp(/^((([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+(\.([a-z]|\d|[!#\$%&'\*\+\-\/=\?\^_`{\|}~]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])+)*)|((\x22)((((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(([\x01-\x08\x0b\x0c\x0e-\x1f\x7f]|\x21|[\x23-\x5b]|[\x5d-\x7e]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(\\([\x01-\x09\x0b\x0c\x0d-\x7f]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF]))))*(((\x20|\x09)*(\x0d\x0a))?(\x20|\x09)+)?(\x22)))@((([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|\d|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.)+(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])|(([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])([a-z]|\d|-|\.|_|~|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])*([a-z]|[\u00A0-\uD7FF\uF900-\uFDCF\uFDF0-\uFFEF])))\.?$/i);
    return pattern.test(emailAddress);
};
function notiU(ele){
	var p = getNotiRow(ele);
	if(p!=null){
		$(p).prev().insertAfter($(p));
	}
}
function notiD(ele){
	var p = getNotiRow(ele);
	if(p!=null){
		$(p).next().insertBefore($(p));
	}
}
function getNotiRow(ele){
	var p = ele.parentNode;
	while(p!=null && p.tagName.toLowerCase()!="tr")p=p.parentNode;
	return p
}