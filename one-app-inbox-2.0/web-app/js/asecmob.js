$(document).ready(function(){
	$(".itm select.q").change(function(){
		var $this = $(this);
		var q = $this.val()
		if(!isNaN(q)){
			var row = this
			while(row.tagName.toLowerCase() != "li"){
				row = row.parentNode;
			}
			var amt = $('.amt',row).html()
			if(!isNaN(amt)){
				$('.t',row).html((q*amt).toFixed(2));
				cT();
			}
		}
	}).trigger("change");
});
function cT(){
    $('form').each(function(){
            var gt = 0;
            $(".t",$(this)).each(function(){
                    gt += ($(this).html()*1)
            })
            if(!isNaN(gt)){
                    $(".gt",$(this)).html(gt.toFixed(2));
            }
    })
}
function updatePPA(ele){
	var gt = 0;
	gt += ($(ele).val()*1)
	if(!isNaN(gt)){
		$(".gt").html(gt.toFixed(2));
	}
}
function submitForm(ele){
	var parentNode = ele
	while(parentNode.tagName.toLowerCase() != "form"){
		parentNode = parentNode.parentNode;
	}
	$(parentNode).submit();
}