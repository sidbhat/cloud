var Ajax;
if (Ajax && (Ajax != null)) {
	Ajax.Responders.register({
	  onCreate: function() {
        if($('spinner') && Ajax.activeRequestCount>0)
          Effect.Appear('spinner',{duration:0.5,queue:'end'});
	  },
	  onComplete: function() {
        if($('spinner') && Ajax.activeRequestCount==0)
          Effect.Fade('spinner',{duration:0.5,queue:'end'});
	  }
	});
}

$(document).ready(function(){
	$('form:not(.noLoader)').submit(function(){
		loadScreenBlock();
	});
	$(".LSC").click(function(){loadScreenBlock();});
});

function oneAppValidate(){
	if(typeof beforeSubmit == 'function'){
		if(!beforeSubmit()){
			setTimeout("hideScreenBlock()",50);
			return false;
		}
	}
}

function hideScreenBlock(){
	document.getElementById("spinner").style.display = "none";
}