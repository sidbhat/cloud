function setMappedFields(domainInstance,thisFieldName,masterForm){
	var fieldMap = masterFormFieldMap[masterForm];
	for(var i=0;i<fieldMap.length;i++){
		var value = "";
		if( $.isArray(fieldMap[i].mappedField) ){
			for(var j = 0; j< fieldMap[i].mappedField.length; j++){
				var tempValue = domainInstance[fieldMap[i].mappedField[j]];
				var IS_JSON1 = true;
				if(tempValue == null || typeof tempValue != 'object'){
					IS_JSON1 = false;
				}
				debugger;
				if(IS_JSON1){
					if(typeof tempValue.line1 != 'undefined'){
						tempValue=tempValue.line1+" "+tempValue.line2+" "+tempValue.city+" "+tempValue.state+" "+tempValue.zip+" "+tempValue.country
					}else if(typeof tempValue.fn != 'undefined'){
						tempValue=tempValue.pre+" "+tempValue.fn+" "+tempValue.mn+" "+tempValue.ln;
					}
				}
				if(tempValue==null){
					tempValue = ''
				}
				value += tempValue;
			}
		}else{
			value = domainInstance[fieldMap[i].mappedField];
		}
		$('input[name="'+fieldMap[i].thisFormField+'"]').val(value);
		var IS_JSON = true;
		if(value == null || typeof value != 'object'){
			IS_JSON = false;
		}
		if(fieldMap[i].thisFormFieldType=="AddressField"){
			if(value != null && IS_JSON){
				if(typeof value.line1 != 'undefined'){
					$.each(value, function(k, v){
						$("#"+fieldMap[i].thisFormField+"_"+k).val(v);
						});
				}else{
					var values=""
					$.each(value, function(k, v){
							if(v && v!=""){
							values+=v+" "
							}
						});
					$("#"+fieldMap[i].thisFormField+"_line1").val(values);
					blankFields(fieldMap[i].thisFormFieldType,fieldMap[i].thisFormField)
				}
            }else if(value != null && !IS_JSON){
				$("#"+fieldMap[i].thisFormField+"_line1").val(value);
				blankFields(fieldMap[i].thisFormFieldType,fieldMap[i].thisFormField)
			}else {
				$("#"+fieldMap[i].thisFormField+"_line1").val('');
				blankFields(fieldMap[i].thisFormFieldType,fieldMap[i].thisFormField)
			}
		}else if(fieldMap[i].thisFormFieldType=="NameTypeField"){
		if(value != null && IS_JSON){
				if(typeof value.fn != 'undefined'){
						$.each(value, function(k, v){
							$("#"+fieldMap[i].thisFormField+k).val(v);
						});
				}else{
					var values=""
					$.each(value, function(k, v){
						values+=v
						});
					$("#"+fieldMap[i].thisFormField+"fn").val(values);
					blankFields(fieldMap[i].thisFormFieldType,fieldMap[i].thisFormField)
				}
            }else if(value != null && !IS_JSON){
				$("#"+fieldMap[i].thisFormField+"fn").val(value);
				blankFields(fieldMap[i].thisFormFieldType,fieldMap[i].thisFormField)
			}else {
				$("#"+fieldMap[i].thisFormField+"fn").val('');
				blankFields(fieldMap[i].thisFormFieldType,fieldMap[i].thisFormField)
			}
		}else{
			if(value != null && IS_JSON){
				var v=""
				if(typeof value.line1 != 'undefined'){
					v=value.line1+" "+value.line2+" "+value.city+" "+value.state+" "+value.country+" "+value.zip
				}else if(typeof value.fn != 'undefined'){
					v=value.pre+" "+value.fn+" "+value.mn+" "+value.ln;
				}
				$("#"+fieldMap[i].thisFormField).val(v);
			}else if(value != null && !IS_JSON){
					$("#"+fieldMap[i].thisFormField).val(value);
			}else{
				$("#"+fieldMap[i].thisFormField).val(value);
			}
		}
		$('input[name="'+fieldMap[i].thisFormField+'show"]').val(value);
	}
}
function blankFields(fieldsType,thisFormField){
	if(fieldsType=="AddressField"){
		$("#"+thisFormField+"_line2").val('');$("#"+thisFormField+"_city").val('');$("#"+thisFormField+"_state").val('');$("#"+thisFormField+"_zip").val('');$("#"+thisFormField+"_country").val('');
	}else if (fieldsType=="NameTypeField"){
		$("#"+thisFormField+"pre").val('');$("#"+thisFormField+"mn").val('');$("#"+thisFormField+"ln").val('');
	}
}