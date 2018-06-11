$(document).ready(function(){setNextBack();});

function validatePage($parentFieldSet){
	return true;
}

function setNextBack(){
	$('.formControls').each(function(){
		var $parentFieldSet = $(this);
		$('.backButton input',$parentFieldSet).click(function(){
			var skipped = false;
			try{
				var skipBackToName = checkPageSkipLogic($(this).attr('name'),false);
				if(skipBackToName){
					var skipBackToPageFound = false;
					var prevFieldSet = $parentFieldSet;
					var prevFieldSetSaved = $parentFieldSet;
					while(prevFieldSet.length>0 && !skipBackToPageFound){
						skipBackToPageFound = $('.backButton input[name="'+skipBackToName+'"]',prevFieldSet).size()>0
						prevFieldSetSaved = prevFieldSet;
						prevFieldSet = prevFieldSet.prev();
					}
					if(skipBackToPageFound){
						$parentFieldSet.hide();
						prevFieldSetSaved.show();
						skipped = true;
					}
				}
			}catch(e){skipped = false;alert("error in skipping logic:->>> "+e)}
			if(!skipped){
				$parentFieldSet.prev().show();
				$parentFieldSet.hide();
				setImageWidthAndHeight();
			}
		});
		$('.nextButton input',$parentFieldSet).click(function(){
			if(validatePage($parentFieldSet)){
				var skipped = false;
				try{
					var skipToName = checkPageSkipLogic($(this).attr('name'),true);
					if(skipToName){
						var skipToPageFound = false;
						var nextFieldSet = $parentFieldSet;
						var nextFieldSetSaved = $parentFieldSet;
						while(nextFieldSet.length>0 && !skipToPageFound){
							skipToPageFound = $('.nextButton input[name="'+skipToName+'"]',nextFieldSet).size()>0
							nextFieldSetSaved = nextFieldSet;
							nextFieldSet = nextFieldSet.next();
						}
						if(skipToPageFound){
							$parentFieldSet.hide();
							nextFieldSetSaved.show();
							skipped = true;
						}
					}
				}catch(e){skipped = false;alert("error in skipping logic:->>> "+e)}
				if(!skipped){
					$parentFieldSet.next().show();
					$parentFieldSet.hide();
					setImageWidthAndHeight();
				}
			}
		});
	});
}