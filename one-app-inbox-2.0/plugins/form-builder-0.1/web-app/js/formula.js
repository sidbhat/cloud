var listOfFormulaArray = new Array()
var listOfFormulaEleName = new Array()
function changeFormulaValue(formulaEleNameL,formulaArrayL){
	setTimeout("setValue("+listOfFormulaArray.length+")",100);
	listOfFormulaArray[listOfFormulaArray.length] = formulaArrayL
	listOfFormulaEleName[listOfFormulaEleName.length] = formulaEleNameL
}
function setValue(){
	var formulaArray = listOfFormulaArray[0];
	listOfFormulaArray.splice(0,1)
	var formulaEleName = listOfFormulaEleName[0];
	listOfFormulaEleName.splice(0,1)

	var tempFormulaArray = new Array();
	var dateFieldCount = 0;
	var resultInNumber = true;
	var multiplyNumbersBy = 1;
	for(var i = 0; i<formulaArray.length; i++){
		if(formulaArray[i].indexOf('field')>-1){
			var $ele = $('input[name="'+formulaArray[i]+'"]');
			if($ele.val() == '')
    			return;
			if($ele.attr('itsType') == 'SingleLineDate'){
				dateFieldCount++;
				multiplyNumbersBy = 86400000;
				resultInNumber = !resultInNumber;
    		}
    	}
    }
	for(var i = 0; i<formulaArray.length; i++){
    	if(formulaArray[i].indexOf('field')>-1){
        	var $ele = $('input[name="'+formulaArray[i]+'"]');
        	var fieldName = $ele.attr('name');
        	if($ele.attr('itsType') == 'SingleLineDate'){
            	var valueArr = $ele.val().split('/');
            	var MM = valueArr[0] - 1;
            	var dd = valueArr[1];
            	var yyyy = valueArr[2];
            	var valueDate = new Date(yyyy,MM,dd);
            	tempFormulaArray[i] = valueDate.getTime();
            }else if($ele.attr('itsType') == 'SingleLineNumber'){
            	tempFormulaArray[i] = multiplyNumbersBy*$ele.val();
            }else{
            	tempFormulaArray[i] = $ele.val();
            }
    	}else if(!isNaN(formulaArray[i])){
    		tempFormulaArray[i] = multiplyNumbersBy*formulaArray[i];
        }else{
        	tempFormulaArray[i] = formulaArray[i];
        }
	}
	var expression = tempFormulaArray.join(' ');
	var $formulaField = $('input[name="'+formulaEleName+'"]');
	var $formulaFieldShow = $('input[name="'+formulaEleName+'show"]');
	try{
        var value = eval(expression);
        if(dateFieldCount>0){
        	if(resultInNumber){
        		$formulaField.attr('itsType','SingleLineNumber');
            	value = value/multiplyNumbersBy;
            }else{
            	$formulaField.attr('itsType','SingleLineDate');
                value = new Date(value);
                value = (value.getMonth()+1 > 9?"":"0")+(value.getMonth()+1)+"/"+(value.getDate()+1 > 9?"":"0")+value.getDate()+"/"+value.getFullYear();
            }
        }
        if(resultInNumber){
        	if(value > Math.floor(value)){
	    		value = value.toFixed(2);
	    	}
        }
        if(value && ((""+value).indexOf('NaN')==-1)){
        	$formulaField.val(value);
        	$formulaFieldShow.val(value);
        }else{
        	$formulaField.val("");
        	$formulaFieldShow.val("");
        }
    }catch(e){
        alert(e)
    	$formulaField.val("");
        $formulaFieldShow.val("");
    }
    $formulaField.trigger('change');
}