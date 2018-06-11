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

//Conditional And Or Handler
function andOrHandler(conditionalOp,value1,value2){if(conditionalOp == 'or') return (value1 || value2); else return (value1 && value2);}

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
		ick : {code : 'ick', handler: function(ele){return ele.checked==true}, text : 'Is'},
		nck : {code : 'nck', handler: function(ele){return ele.checked==false}, text : 'Is Not'}
	},
	radioType : {
		allOperators : ['ick','nck'],
		ick : {code : 'ick', handler: function(ele){return ele.checked==true}, text : 'Is'},
		nck : {code : 'nck', handler: function(ele){return ele.checked==false}, text : 'Is Not'}
	},
	strictType : {
		allOperators : ['eqt','neq'],
		eqt : {code : 'eqt', handler: new Function("value1","value2","return isStringEqualsTo(value1,value2);"), text : 'Is'},
		neq : {code : 'neq', handler: new Function("value1","value2","return isStringNotEqualsTo(value1,value2);"), text : 'Is Not'}
	}
};
var actOnHandler = {
	s: function(ele,cond){
		if(cond) ele.show(); else ele.hide();
		try{
			x();//to resize frame in case embed
		}catch(e){}
	},
	h: function(ele,cond){
		if(cond) ele.hide(); else ele.show();
		try{
			x();//to resize frame in case embed
		}catch(e){}
	}
}
function handleRuleOn(element){
	try{
		if(fieldRules){
			for(var i=0;i<fieldRules.length;i++){
				var conditionResult = false;
				conditionResult = checkCondition(fieldRules[i]);
				//Show or hide on the basis of Condition Result
				actOnHandler[fieldRules[i].act]($("#"+fieldRules[i].actOn+"ControlHolder"),conditionResult)
			}
		}
	}catch(e){console.log("error in handleRuleOn function"+e)}
}

function checkPageSkipLogic(submittedPageB,isNext){
	if(pageRules){
		$('#ruleErrors').hide();
		var conditionResult = false;
		var skipToPageName;
		try{
			for(var i=0;i<pageRules.length;i++){
				conditionResult = false;
				if(submittedPageB == pageRules[i][isNext?'submittedPage':'actOn']){
					conditionResult = checkCondition(pageRules[i]);
				}
				if(conditionResult){
					skipToPageName = pageRules[i][isNext?'actOn':'submittedPage'];
					if(pageRules[i].errorMsg && pageRules[i].errorMsg!=''){
						debugger;
						var ruleErrorsEle = $('#pageMessageRule')
						ruleErrorsEle.html('<p style="text-align:center;"></p>');
						$('p',ruleErrorsEle).text(pageRules[i].errorMsg);
						$('#pageMessageRule').show();
						$('#pageMessageRule').fadeOut(10000);
					}else{
						$('#ruleErrors').hide();
					}
				}
			}
		}catch(e){console.log("Error while checking page rules")}
		console.log("Skip to page name: "+skipToPageName)
		return skipToPageName;
	}
}
function checkCondition(fieldRule){
	var conditionResult = false;
	var itsConditions = fieldRule.conditions;
	var prevCondOp = 'or';
	var currCondOp;
	for(var j=0;j<itsConditions.length;j++){
		var con = itsConditions[j];
		currCondOp = con.cop;
		fieldType = con.fieldType;
		currentCondition = false;
		var fieldId
		try{
			if(fieldType.indexOf('checkType')>-1){//check for checkbox type elements
				fieldId = (con.field.split("@__@")[0])+(con.field.split("@__@")[1]);
				var ele = document.getElementById(fieldId);
				if(ele && ele.type=='hidden'){
					ele = document.getElementById(fieldId+(con.field.split("@__@")[1]));
				}
				currentCondition = operators[fieldType][con.op].handler(ele);
			}else{
				fieldId = con.field;
				var conVal = con.val;
				if(fieldType=='radioType'){
					var radioEle = document.getElementById(fieldId+conVal)
					if(radioEle){
						currentCondition = operators[fieldType][con.op].handler(radioEle);
					}
				}else{
					var ele = $('[name="'+fieldId+'"]');
					currentCondition = operators[fieldType][con.op].handler(ele.val(),conVal);
				}
			}
		}catch(e){console.log("Error while getting value of element with id: "+fieldId+", and fieldType: "+fieldType)}
		conditionResult = andOrHandler(prevCondOp,conditionResult,currentCondition);
		prevCondOp = currCondOp;
		if(conditionResult && currCondOp=='or'){
			break;
		}
		if(!conditionResult && currCondOp=='and'){
			break;
		}
	}
	return conditionResult;
}