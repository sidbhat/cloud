$(document).ready(function(){
	$(".itm select.q").change(function(){
		var $this = $(this);
		var q = $this.val()
		if(!isNaN(q)){
			var row = $this.parent().parent()
			var amt = $('.amt',row).html()
			if(!isNaN(amt)){
				$('.t',row).html((q*amt).toFixed(2));
				cT();
			}
		}
	}).trigger("change");
});
function cT(){
	var gt = 0;
	$(".t").each(function(){
		gt += ($(this).html()*1)
	})
	if(!isNaN(gt)){
		$(".gt").html(gt.toFixed(2));
	}
}
function updatePPA(ele){
	var gt = 0;
	gt += ($(ele).val()*1)
	if(!isNaN(gt)){
		$(".gt").html(gt.toFixed(2));
	}
}