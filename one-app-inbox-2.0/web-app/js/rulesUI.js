function parseDate(dateStr){
	var dateObj = null;
	if(dateStr){
		var dateSplit = dateStr.split("/");
		if(dateSplit.length == 3){
			var mm = dateSplit[0]*1-1;
			var dd = dateSplit[1];
			var yyyy = dateSplit[2];
			dateObj = new Date(yyyy,mm,dd);
		}
	}
	return dateObj;
}
//String Type Handlers
function isStringEqualsTo(value1,value2){ if(value1 == value2) return true; else return false;}
function isStringNotEqualsTo(value1,value2){ return !isStringEqualsTo(value1,value2);}
function doesStartWith(value,startsWith){ if(value.toLowerCase().indexOf(startsWith.toLowerCase())==0) return true; else return false;}
function doesEndWith(value,endsWith){ if(value.toLowerCase().indexOf(endsWith.toLowerCase())==(value.length - endsWith.length)) return true; else return false;}
function doesContain(value,contains){ if(value.toLowerCase().indexOf(contains.toLowerCase())>-1) return true; else return false;}
function doesNotContain(value,notContains){ if(value.toLowerCase().indexOf(notContains.toLowerCase())==-1) return true; else return false;}

//Date Type Handlers
function isDateEqualsTo(value1,value2){try{var dateValue1 = parseDate(value1);var dateValue2 = parseDate(value2); if(dateValue1 - dateValue2 == 0) return true; else return false;}catch(e){return false;}}
function isDateNotEqualsTo(value1,value2){ return !isDateEqualsTo(value1,value2);}
function isDateLessThan(value1,value2){try{var dateValue1 = parseDate(value1);var dateValue2 = parseDate(value2); if(dateValue1 - dateValue2 < 0) return true; else return false;}catch(e){return false;}}
function isDateLessThanEqualTo(value1,value2){ return isDateLessThan(value1,value2) || isDateEqualsTo(value1,value2);}
function isDateGreaterThan(value1,value2){ return !isDateLessThan(value1,value2);}
function isDateGreaterThanEqualTo(value1,value2){ return isDateGreaterThan(value1,value2) || isDateEqualsTo(value1,value2);}
//function isDateBetween(value1,value2){ return (isDateLessThanEqualTo(value1,value2.split("@##@")[0]) && isDateGreaterThanEqualTo(value1,value2.split("@##@")[1]))
//	|| (isDateGreaterThanEqualTo(value1,value2.split("@##@")[0]) && isDateLessThanEqualTo(value1,value2.split("@##@")[1]));}


//Double Type Handlers
function isDoubleEqualsTo(value1,value2){ if(eval(value1) == eval(value2)) return true; else return false;}
function isDoubleNotEqualsTo(value1,value2){ return !isDoubleEqualsTo(value1,value2);}
function isDoubleLessThan(value1,value2){ if(eval(value1) < eval(value2)) return true; else return false;}
function isDoubleLessThanEqualTo(value1,value2){ return isDoubleLessThan(value1,value2) || isDoubleEqualsTo(value1,value2);}
function isDoubleGreaterThan(value1,value2){ return !isDoubleLessThanEqualTo(value1,value2);}
function isDoubleGreaterThanEqualTo(value1,value2){ return isDoubleGreaterThan(value1,value2) || isDoubleEqualsTo(value1,value2);}
//function isDoubleBetween(value1,value2){ return (isDoubleLessThanEqualTo(value1,value2.split("@##@")[0]) && isDoubleGreaterThanEqualTo(value1,value2.split("@##@")[1]))
//	|| (isDoubleGreaterThanEqualTo(value1,value2.split("@##@")[0]) && isDoubleLessThanEqualTo(value1,value2.split("@##@")[1]));}

//tom: Type Operator Map
var tom = {
	NumberResult:"doubleType",DateResult:"dateType",SingleLineDate:"dateType",SingleLineText:"stringType",SingleLineNumber:"doubleType",MultiLineText:"stringType",dropdown:"strictType",GroupButton:"radioType",CheckBox:"checkType",LookUp:"stringType"
};

//Operators
var operators = {
	allTypes:['SingleLineDate','SingleLineText','SingleLineNumber','MultiLineText','dropdown','GroupButton','CheckBox','FormulaField','LookUp'],
	strictTypes:['dropdown','GroupButton'],
	stringType : {
		allOperators : ['eqt','neq','stw','ewt','con','nco'],
		eqt : {code : 'eqt', handler: new Function("value1","value2","return isStringEqualsTo(value1,value2);"), text : 'Equals to'},
		neq : {code : 'neq', handler: new Function("value1","value2","return isStringNotEqualsTo(value1,value2);"), text : 'Not equals to'},
		stw : {code : 'stw', handler: new Function("value","startsWith","return doesStartWith(value,startsWith);"), text : 'Starts with'},
		ewt : {code : 'ewt', handler: new Function("value","endsWith","return doesEndWith(value,endsWith);"), text : 'Ends with'},
		con : {code : 'con', handler: new Function("value","contains","return doesContain(value,contains);"), text : 'Contains'},
		nco : {code : 'nco', handler: new Function("value","notContains","return doesNotContain(value,notContains);"), text : 'Does not contain'}
	},
	dateType : {
		allOperators : ['eqt','neq','ltn','gtn','lte','gte'],//,'btw'],
		eqt : {code : 'eqt', handler: new Function("value1","value2","return isDateEqualsTo(value1,value2);"), text : 'Equals to'},
		neq : {code : 'neq', handler: new Function("value1","value2","return isDateNotEqualsTo(value1,value2);"), text : 'Not equals to'},
		ltn : {code : 'ltn', handler: new Function("value1","value2","return isDateLessThan(value1,value2);"), text : 'Less than'},
		gtn : {code : 'gtn', handler: new Function("value1","value2","return isDateGreaterThan(value1,value2);"), text : 'Greater than'},
		lte : {code : 'lte', handler: new Function("value1","value2","return isDateLessThanEqualTo(value1,value2);"), text : 'Less than equal to'},
		gte : {code : 'gte', handler: new Function("value1","value2","return isDateGreaterThanEqualTo(value1,value2);"), text : 'Greater than equal to'}
		//,
		//btw : {code : 'btw', handler: new Function("value1","value2","return isDateBetween(value1,value2);"), text : 'Between'}
	},
	doubleType : {
		allOperators : ['eqt','neq','ltn','gtn','lte','gte'],//,'btw'],
		eqt : {code : 'eqt', handler: new Function("value1","value2","return isDoubleEqualsTo(value1,value2);"), text : 'Equals to'},
		neq : {code : 'neq', handler: new Function("value1","value2","return isDoubleNotEqualsTo(value1,value2);"), text : 'Not equals to'},
		ltn : {code : 'ltn', handler: new Function("value1","value2","return isDoubleLessThan(value1,value2);"), text : 'Less than'},
		gtn : {code : 'gtn', handler: new Function("value1","value2","return isDoubleGreaterThan(value1,value2);"), text : 'Greater than'},
		lte : {code : 'lte', handler: new Function("value1","value2","return isDoubleLessThanEqualTo(value1,value2);"), text : 'Less than equal to'},
		gte : {code : 'gte', handler: new Function("value1","value2","return isDoubleGreaterThanEqualTo(value1,value2);"), text : 'Greater than equal to'}
		//,
		//btw : {code : 'btw', handler: new Function("value1","value2","return isDoubleBetween(value1,value2);"), text : 'Between'}
	},
	checkType : {
		allOperators : ['ick','nck'],
		ick : {code : 'ick', handler: null, text : 'Is'},
		nck : {code : 'nck', handler: null, text : 'Is Not'}
	},
	radioType : {
		allOperators : ['ick','nck'],
		ick : {code : 'ick', handler: null, text : 'Is'},
		nck : {code : 'nck', handler: null, text : 'Is Not'}
	},
	strictType : {
		allOperators : ['eqt','neq'],
		eqt : {code : 'eqt', handler: new Function("value1","value2","return isStringEqualsTo(value1,value2);"), text : 'Is'},
		neq : {code : 'neq', handler: new Function("value1","value2","return isStringNotEqualsTo(value1,value2);"), text : 'Is Not'}
	}
};

function showfieldRules(){
	showFieldRules();
}
function showFieldRules(){
	$("#noFieldRule").hide();
	$("#fieldRulesWithSave").slideDown();
}
function showpageRules(){
	showPageRules();
}
function showPageRules(){
	$("#noPageRule").hide();
	$("#pageRulesWithSave").slideDown();
}
function showNofieldRule(){
	$("#noFieldRule").slideDown();
	$("#fieldRulesWithSave").hide();
}
function showNopageRule(){
	$("#noPageRule").slideDown();
	$("#pageRulesWithSave").hide();
}
var addFieldRule = function(clickedEle,fieldRule,forRuleType){
	var fieldRuleEle = $('<tr class="fieldRule">\
							<td class="ruleCountTd"><input type="button" class="ruleCount button button-gray"/></td>\
							<td>\
							<div class="ruleConditions"></div>\
							<div>\
								'+(forRuleType=='field'?'<select class="sOrH"><option value="s">Show</option><option value="h">Hide</option></select>':'<div class="skipBlock">Skip to: </div>')+'\
								<div><select class="SHField"></select>'+(forRuleType=='page'?'<div class="skipBlock">On Submitting:</div><select class="submittedPage"></select><div style="float:left;padding-top:8px"><div class="skipBlock">Error Message To Show:</div><input class="errorMsg"/></div>':'')+'</div>\
							</div>'+(forRuleType=='page'?'':'')+'\
							</td>\
							<td class="fieldRuleButtonTd"><input type="button" class="addFieldRule button button-green" value="Add"><input type="button" class="removeFieldRule button button-red" value="Del"></td>\
						</tr>');
	var ruleConditions = $('.ruleConditions',fieldRuleEle);
	if(fieldRule!=null){
		if(fieldRule.conditions!=null){
			for(var i=0;i<fieldRule.conditions.length;i++){
				addRuleCondition(fieldRule.conditions[i],ruleConditions,null);
			}
		}
		if(forRuleType == 'field'){
			$('.sOrH',fieldRuleEle).val(fieldRule.act);
		}
	}else{
		addRuleCondition(null,ruleConditions,null);
	}
	var SHField = $('.SHField',fieldRuleEle);
	var submittedPage
	if(forRuleType=='field'){
		var fieldTypeCountMap = {}
		for(var i=0;i<fieldsWithSetAll.length;i++){
			var ft = fieldsWithSetAll[i].type
			if(ft != 'PageBreak'){
				if(fieldTypeCountMap[ft]){
					fieldTypeCountMap[ft]++;
				}else{
					fieldTypeCountMap[ft]=1
				}
				var fieldSHFieldO = $('<option></option>');
				var label = fieldsWithSetAll[i].label?fieldsWithSetAll[i].label:(ft+'-'+fieldTypeCountMap[ft]);
				if(label.length>30){
					label = label.substring(0,28)+'...';
				}
				fieldSHFieldO.text(label);
				fieldSHFieldO.val(fieldsWithSetAll[i].name);
				
				//Adding field Option to Show Hide Page drop down
				SHField.append(fieldSHFieldO);
			}
		}
	}else{
		submittedPage = $('.submittedPage',fieldRuleEle);
		var pageNumber = 1;
		for(var i=0;i<fieldsWithSetAll.length;i++){
			var ft = fieldsWithSetAll[i].type
			if(ft == 'PageBreak'){
				var fieldSHFieldO = $('<option></option>');
				fieldSHFieldO.text("Page "+pageNumber++);
				fieldSHFieldO.val(fieldsWithSetAll[i].name);
				
				//Adding field Option to Show Hide Page drop down
				SHField.append(fieldSHFieldO);
			}
		}
		var fieldSHFieldO = $('<option></option>');
		fieldSHFieldO.text("Page "+pageNumber);
		fieldSHFieldO.val("last");
		
		//Adding field Option to Show Hide Page drop down
		SHField.append(fieldSHFieldO);
		SHField.change(function(){
			submittedPage.children().remove();
			var $this = $(this);
			var foundOpt = false;
			$('option',$this).each(function(){
				var thisOpt = $(this);
				if(!foundOpt){
					submittedPage.append(thisOpt.clone());
					if(thisOpt.attr("value") == $this.val()){
						foundOpt = true
					}
				}
			});
		}).trigger('change');
	}
	if(fieldRule){
		SHField.val(fieldRule.actOn);
		SHField.trigger('change');
		if(submittedPage){
			submittedPage.val(fieldRule.submittedPage)
		}
		if(fieldRule.errorMsg){
			$('.errorMsg',fieldRuleEle).val(fieldRule.errorMsg)
		}
	}
	var addFieldRuleButton = $('.addFieldRule',fieldRuleEle);
	var removeFieldRuleButton = $('.removeFieldRule',fieldRuleEle);
	addFieldRuleButton.click(function(){addFieldRule($(this),null,forRuleType)});
	removeFieldRuleButton.click(function(){removeFieldRule($(this),forRuleType)});
	
	//Append after clicked row if any. Clicking on Add Field Rule gives clickedEle null
	if(clickedEle){
		while(clickedEle && !clickedEle.hasClass("fieldRule")){
			clickedEle = clickedEle.parent();
		}
		clickedEle.after(fieldRuleEle);
	}else{
		$('.'+forRuleType+'RuleTable .'+forRuleType+'RuleTbody',$('#'+forRuleType+'Rules')).append(fieldRuleEle);
	}
	updateRuleCount(forRuleType+"RuleTable");
}
var addRuleCondition = function(rc,appendToEle,clickedEle){
	//rcE Rule Condition Element
	var rcE = $('<div class="ruleCondition"><table>\
					<tr><td><input type="hidden" class="fType" value=""/><select class="rFieldQ"></select></td>\
					<td><select class="rOp"></select></td>\
					<td><input type="text" class="rVal"/></td>\
					<td><input type="button" class="button button-gray addRuleCond" value="+"/><input type="button" class="button button-gray remRuleCond" value="-"/></td></tr>\
					<tr><td colspan="2"><div class="rCOpD"><select class="rCOp"><option value="or">Or</option><option value="and">And</option></select></div></td></tr></table>\
				</div>');
	var fieldQ = $('select.rFieldQ',rcE);
	fieldQ.change(repopulateOperatorDD);
	//rop Rule Operation Drop down
	var rop = $('select.rOp',rcE);
	
	var addRuleConditionButton = $('.addRuleCond',rcE);
	var remRuleConditionButton = $('.remRuleCond',rcE);
	addRuleConditionButton.click(function(){addRuleCondition(null,null,$(this))});
	remRuleConditionButton.click(remRuleCondition);
	var firstFieldType
	var fieldTypeCountMap = {}
	for(var i=0;i<fieldsWithSetAll.length;i++){
		var ft = fieldsWithSetAll[i].type
		if(ft=='FormulaField'){
			ft = fieldsWithSetAll[i].ffType;
		}
		if($.inArray(ft, operators.allTypes) > -1){
			if(!firstFieldType){
				firstFieldType = ft;
			}
			if(fieldTypeCountMap[ft]){
				fieldTypeCountMap[ft]++;
			}else{
				fieldTypeCountMap[ft]=1
			}
			if(ft=='CheckBox'){
				var fieldQOGroup = $('<optgroup label="Checkbox"></optgroup>');
				for(var j=0;j<fieldsWithSetAll[i].val.length;j++){
					fieldQO = $('<option></option>');
					var label = fieldsWithSetAll[i].val[j]?fieldsWithSetAll[i].val[j]:('Option '+j);
					if(label.length>30){
						label = label.substring(0,28)+'...';
					}
					fieldQO.text(label);
					fieldQO.val(fieldsWithSetAll[i].name+"@__@"+j);
					
					//Adding field's checkbox Option to fieldQuestions drop down's group
					fieldQOGroup.append(fieldQO);
				}
				
				//Adding field Option to fieldQuestions drop down
				fieldQ.append(fieldQOGroup);
			}else{//non CheckBox Implementation
				fieldQO = $('<option></option>');
				var label = fieldsWithSetAll[i].label?fieldsWithSetAll[i].label:(ft+'-'+fieldTypeCountMap[ft]);
				if(label.length>30){
					label = label.substring(0,28)+'...';
				}
				fieldQO.text(label);
				fieldQO.val(fieldsWithSetAll[i].name);
				
				//Adding field Option to fieldQuestions drop down
				fieldQ.append(fieldQO);
			}
		}
	}
	if(rc){
		//TODO Change here if Rule already exists 
		populateOperatorDD(firstFieldType,rop);
		fieldQ.val(rc.field);
		fieldQ.trigger('change');
		rop.val(rc.op);
		$('.rCOp',rcE).val(rc.cop)
		var rValElement = $('.rVal',rcE);
		if(rValElement && rValElement.attr('rType') != 'checkType'){
			rValElement.val(rc.val);
		}
	}else{
		populateOperatorDD(firstFieldType,rop);
	}
	
	if(clickedEle){
		while(clickedEle && !clickedEle.hasClass("ruleCondition")){
			clickedEle = clickedEle.parent();
		}
		if(clickedEle){
			clickedEle.after(rcE);
			appendToEle = null;
		}
	}
	

	if(appendToEle){
		appendToEle.append(rcE)
	}
	fieldQ.trigger('change');
}
function repopulateOperatorDD(){
	var thisEle = $(this);
	var thisVal = thisEle.val();
	var ft
	var selectedField = fieldsWithSetAll[0];
	if(thisVal.indexOf("@__@")>-1){//Case for checkbox
		var thisValFieldName = thisVal.split("@__@")[0];
		for(var i=0;i<fieldsWithSetAll.length;i++){
			if(fieldsWithSetAll[i].name==thisValFieldName){
				ft = fieldsWithSetAll[i].type
				selectedField = fieldsWithSetAll[i];
				break;
			}
		}
	}else{
		for(var i=0;i<fieldsWithSetAll.length;i++){
			if(fieldsWithSetAll[i].name==thisVal){
				ft = fieldsWithSetAll[i].type
				selectedField = fieldsWithSetAll[i];
				break;
			}
		}
	}
	var rop = thisEle;
	while(rop && !rop.hasClass("ruleCondition")){
		rop = rop.parent();
	}
	var rval
	var rValNew;
	if(rop){
		rval = $(".rVal",rop);
//		Check the selected fieldQuestion and add the input box or drop down based on it type
		if($.inArray(ft, operators.strictTypes) > -1){
			rValNew = $('<select class="rVal" rType="'+tom[ft]+'"></select>');
			myrValNew=rval.val();
			for(var j=0;j<selectedField.val.length;j++){
				var rValOpt = $('<option></option');
				var label = selectedField.val[j];
				if(label.length>30){
					label = label.substring(0,28)+'...';
				}
				rValOpt.text(label);
				if(tom[ft]=='strictType'){
					rValOpt.val(selectedField.val[j]);
				}else{
					rValOpt.val(j);
				}
				rValNew.append(rValOpt);
			}
			rval.after(rValNew);
			rval.remove();
			rValNew.val(myrValNew)
		}else if(ft=='CheckBox'){
			if(rval.attr("rType")!=tom[ft]){
				rValNew = $('<div class="rVal" rType="'+tom[ft]+'">Checked</div>');
				rval.after(rValNew);
				rval.remove();
			}else{
				rValNew=rval;
			}
		}else{
			if(rval.attr("rType")!=tom[ft]){
				rValNew = $('<input type="text" class="rVal" rType="'+tom[ft]+'"/>');
				rval.after(rValNew);
				rval.remove();
			}else{
				rValNew=rval;
			}
		}
		$('.fType',rop).val(tom[ft]);
		rop = $(".rOp",rop);
		var myval=rop.val();
		populateOperatorDD(ft,rop);
		rop.val(myval)
	}
}
function populateOperatorDD(ft,rop){
	rop.children().remove();
	//start------>>>>>>find operators applicable and add to the operators drop down
	var operatorTypeTextMap = operators[tom[ft]];
	var allOperatorForField = operatorTypeTextMap.allOperators;
	for(var j=0;j<allOperatorForField.length;j++){
		var thisOperator = allOperatorForField[j];
		if(operatorTypeTextMap[thisOperator]){
			var fieldOperatorOption = $('<option></option>');
			fieldOperatorOption.text(operatorTypeTextMap[thisOperator].text);
			fieldOperatorOption.val(operatorTypeTextMap[thisOperator].code);
			rop.append(fieldOperatorOption);
		}
	}
	//end-------->>>>>>find operators applicable
}
var removeFieldRule = function(thisFieldRule,forRuleType){
	if($("."+forRuleType+"RuleTable .fieldRule").size()==1){
		var confirmVal = confirm("Are you sure you want to delete all the "+forRuleType+" rules?");
		if(!confirmVal){
			return;
		}
	}
	while(thisFieldRule && !thisFieldRule.hasClass("fieldRule")){
		thisFieldRule = thisFieldRule.parent();
	}
	thisFieldRule.remove();
	if($("."+forRuleType+"RuleTable .fieldRule").size()==0){
		$('#'+forRuleType+'RuleFormButton').trigger('click');
		window['showNo'+forRuleType+'Rule']();
	}
	updateRuleCount(forRuleType+"RuleTable");
}
var remRuleCondition = function(){
	var thisFieldRuleC = $(this);
	while(thisFieldRuleC && !thisFieldRuleC.hasClass("ruleCondition")){
		thisFieldRuleC = thisFieldRuleC.parent();
	}
	thisFieldRuleC.remove();
}
function updateRuleCount(forRuleType){
	$('.'+forRuleType+' .ruleCount').each(function(index){
		$(this).val(index+1);
	})
}

function addAllRules(rules,forRuleType){
	if(rules && rules.length>0){
		window['show'+forRuleType+'Rules']();
		for(var i=0;i<rules.length;i++){
			addFieldRule(null,rules[i],forRuleType);
		}
	}else{
		window['showNo'+forRuleType+'Rule']();
	}
}

function saveFieldRules(forRuleType){
	var rulesToSave = new Array();
	var rulesEles = $('#'+forRuleType+'Rules');
	$('.fieldRule',rulesEles).each(function(){
		var thisRuleEle = $(this);
		var thisRule = {};
		thisRule.act=(forRuleType=='field'?$('.sOrH',thisRuleEle).val():'k');
		thisRule.actOn=$('.SHField',thisRuleEle).val();
		if(forRuleType=='page'){
			thisRule.submittedPage=$('.submittedPage',thisRuleEle).val();
			thisRule.errorMsg=$('.errorMsg',thisRuleEle).val();
		}
		var tRCs = new Array();//tRCs means this rules conditions
		$('.ruleCondition',thisRuleEle).each(function(){
			var thisConditionEle = $(this);
			var thisCondition = {};
			thisCondition.field = $('.rFieldQ',thisConditionEle).val();
			thisCondition.op = $('.rOp',thisConditionEle).val();
			thisCondition.val = $('.rVal',thisConditionEle).val();
			thisCondition.cop = $('.rCOp',thisConditionEle).val();
			thisCondition.fieldType = $('.fType',thisConditionEle).val();
			tRCs[tRCs.length] = thisCondition;
		});
		thisRule.conditions = tRCs;
		rulesToSave[rulesToSave.length] = thisRule;
	});
	var jsonRules = JSON.stringify(rulesToSave);
	$("#"+forRuleType+"RulesData").val(jsonRules);
}
function hideScreenBlock(){
	$('#spinner').hide();
}
function updateVersion(data){
	if(data && data.status == 'OK')
		$('input[name="version"]').val(data.version);
}