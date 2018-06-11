<%@ page import="org.grails.formbuilder.Field" %>
<%@ page import="org.grails.formbuilder.Form" %>
<%@ page import="grails.converters.JSON" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="default.new.edit.label" args="[formName]" /></title>
        <script type="text/javascript" src="${resource(dir: 'js', file: 'formula.js')}"></script>
        <script type="text/javascript" src="${request.getContextPath()}/js/rulesImpl.js"></script>
        <script language="JavaScript" src="http://j.maxmind.com/app/geoip.js"></script>
        <script type="text/javascript">
			function setMappedFields(domainInstance,thisFieldName,masterForm){var fieldMap = masterFormFieldMap[masterForm];for(var i=0;i<fieldMap.length;i++){var value = "";if( $.isArray(fieldMap[i].mappedField) ){for(var j = 0; j< fieldMap[i].mappedField.length; j++){value += domainInstance[fieldMap[i].mappedField[j]];}}else{value = domainInstance[fieldMap[i].mappedField];}$('input[name="'+fieldMap[i].thisFormField+'"]').val(value);$("#"+fieldMap[i].thisFormField).val(value);$('input[name="'+fieldMap[i].thisFormField+'show"]').val(value);}}
			function submitForm(ele){
				var parentNode = ele
				while(parentNode.tagName.toLowerCase() != "form"){
					parentNode = parentNode.parentNode;
				}
				parentNode.action = parentNode.action.replace("index","update");
				$(parentNode).submit();
			}
	     </script>
        <script type="text/javascript" src="${request.getContextPath()+'/js/pageBreak.js'}"></script>
    </head>
	<body>
		<section class="main-section grid_7">
		<div class="main-content">
			<header>
				<h2><g:message code="default.new.edit.label" args="[formName]" /></h2>
				<g:if test="${flash.message}">
		            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
				</g:if>
				<g:if test="${domainInstance.errors }">
					<div class="errors">
						<g:each in="${domainInstance.errors }" var="error">
							<g:if test="${error.name != 'version'}">
								<g:set var="fieldWithSettings" value="${fields[error.name] }" />
								<g:if test="${fieldWithSettings }">
									<g:set var="fieldName" value="${JSON.parse(fieldWithSettings.settings)?.en?.label}" />
									<li><g:message code="${error.code}" args="${[fieldName,formName] }" default="${error.defaultMessage }" /></li>
								</g:if>
							</g:if>
							<g:else>
								<li><g:message code="${error.code}" args="${['version',formName] }" default="${error.defaultMessage }" /></li>
							</g:else>
						</g:each>
					</div>
				</g:if>
				<div id="ruleErrors" class="errors" style="display:none;"></div>
				<div class="clearfix" style="position: absolute; right: 10px; top: 20px;"><ul class="action-buttons clearfix fr">
						<li><a href="${grailsApplication.config.grails.serverURL}/documentation/formInstance_help.gsp" class="button button-gray no-text help" rel="#overlay">Help<span class="help"></span></a></li>
				</ul></div>
			</header>
			<section class="container_6 clearfix">
				<div class="form grid_6">
					<g:set var="attachments" value="${form.getDomainAttachments(domainInstance.id) }"></g:set>
					<g:form useToken="true" enctype="multipart/form-data">
						<g:hiddenField name="id" value="${domainInstance?.id}" />
						<g:hiddenField name="version" value="${domainInstance?.version}" />
						<div class="tabbed-pane">
							<ul class="tabs">
								<li><a href="#">Details</a></li>
							</ul>
							<div class="panes clearfix">
								<section>
									<table class="formControls ddcView">
										<tbody>
											<g:set var="showFields" value="${false }" />
											<sec:ifAnyGranted roles="${rtsaf }">
												<g:set var="showFields" value="${true }" />
											</sec:ifAnyGranted>
											<g:if test="${grailsApplication.config.form.workWithSQL}">
											</g:if>
											<g:else>
											</g:else>
											<g:set var="dateFormat" value="${grailsApplication.config.format.date}" />
											<g:set var="sdf" value="${new SimpleDateFormat(dateFormat?:'MM/dd/yyyy')}" />
											<g:set var="masterFormFieldMap" value="${[:]}" />
											<g:set var="fieldsList" value="${form.fieldsList as List}" /><g:set var="fieldsList" value="${fieldsList.sort { it.sequence}}" />
											<g:set var="pageBreakFields" value="${fieldsList.findAll{it.type == 'PageBreak'}}" /><g:set var="pageBreakCount" value="${1}" />
											<g:each in="${fieldsList}" var="p" status="i">
												<g:set var="display" value="${true }" />
												<g:set var="settings" value="${JSON.parse(p.settings)}" />
                                    			<g:if test="${settings.hideFromUser==true}"><g:set var="display" value="${false }" /></g:if>
												<g:if test="${p.type == 'PageBreak'}">
													<tr><td colspan="2"><div class="action"><div class="backButton" style="display:inline-block;float:left;" 
															><input type="button" name="${p.name }" class="button button-gray" value="Back" style="width:90px;${pageBreakCount<=1?'display:none;':''}" 
														/></div><div class="nextButton" style="display:inline-block;float:left;"><input type="button" name="${p.name }" class="button button-gray" value="Next" style="width:90px;" />
														</div></div><div class="action"><div><span style="float:right;line-height: 35px;">Page ${pageBreakCount++} of ${pageBreakFields.size()+1}</span></div></div></td></tr></tbody></table><table style="display:none;width:100%;" class="formControls"><tbody>
												</g:if>
                                    			<g:elseif test="${display || showFields || p.type == 'FormulaField'}">
                                    			
                            <tr class="prop" id="${p.name}ControlHolder" ${(!display && p.type == 'FormulaField' && !showFields)?'style="display:none;"':''}>
                            	<g:set var="fieldsNeedingPadding" value="${['PlainText','PlainTextHref','ScaleRating','LinkVideo','ImageUpload']}" />
                                	<g:set var="fieldsNeedingPaddingTop" value="${['PlainTextHref','ScaleRating','ImageUpload']}" />
                                	<g:if test="${fields.get(p.name) != null}">
                                		<g:set var="field" value="${fields.get(p.name)}" />
                                		<g:if test="${settings.mapMasterForm && settings.mapMasterField }">
                                			<g:if test="${!masterFormFieldMap.getAt(settings.mapMasterForm) }">
                                				<%masterFormFieldMap."${settings.mapMasterForm}" = []%>
                                			</g:if>
                                			<%masterFormFieldMap."${settings.mapMasterForm}" << [thisFormField:p.name,mappedField:settings.mapMasterField]%>
                                		</g:if>
                                		<g:set var="fieldName" value="${settings.en.label}" /><g:set var="val" value="${settings.en.value }" />
                                		<g:set var="required" value="${settings.required}" /><g:set var="help" value="${settings.en.description }" />
                                		<g:if test="${field.type == 'CheckBox' || field.type == 'dropdown' || field.type == 'GroupButton' || field.type == 'FormulaField' || field.type == 'LikeDislikeButton' }">
                                			<g:set var="fieldValues" value="${val}" />
                                		</g:if><g:if test="${field?.type == 'Paypal'}" ><g:set var="fieldName" value="" /></g:if>
                                <td valign="top" class="name"><label 
                                	for="${p.name}"
                                	><g:if test="${help}"><a 
                                		href="#" title="${help}"
                                		>${fieldName?.encodeAsHTML()}</a></g:if><g:else
                                	>${fieldName?.encodeAsHTML()}</g:else><g:if 
                                	test="${required }"><em>*</em></g:if></label></g:if><g:else
                                ><label for="${p.name}">${p.naturalName}</label></g:else></td>
                                <td valign="top" class="value ${fieldsNeedingPadding.contains(field?.type)?'labelValue'+(fieldsNeedingPaddingTop.contains(field?.type)?'Link':''):''} ${(domainInstance?.errors && domainInstance.errors*.name*.toString().contains(p.name))?'errors':''}" 
                                	><g:if test="${field?.type == 'SingleLineText' }"><g:textField 
                                		name="${p.name}" value="${domainInstance.getAt(p.name)?:''}" 
                                	/></g:if><g:elseif test="${field?.type == 'LookUp' }"><g:textField 
                                		name="${p.name}" value="${domainInstance.getAt(p.name)?:''}" 
                                		/><div class="LookUpButton" id="${p.name}search"></div
                                		><script 
                                			type="text/javascript">
												$(document).ready(function(){
													$( "#${p.name}" ).autocomplete({source: "${createLink(controller:'PF',action:'lookUp',params:[field:field.id,formId:form.id])}",minLength: 2,select: function( event, ui ) {setMappedFields(ui.item.wholeObj,"${p.name}",${settings.mapMasterForm?:'[]'});}});
													$( "#${p.name}search" ).click(function(){$("#${p.name}").autocomplete("option","minLength",0).autocomplete("search",$("#${p.name}").val()).autocomplete("option","minLength",2).trigger("focus");});
												});
										</script
									></g:elseif><g:elseif test="${field?.type == 'MultiLineText'}"><g:textArea 
										name="${p.name}" value="${domainInstance.getAt(p.name)?:''}" style="resize: none;height:${(settings.fieldSize?settings.fieldSize.toInteger():2)*1*65+'px'};" 
									/></g:elseif><g:elseif test="${field?.type == 'FileUpload'}"><input 
										type="file" name="${p.name}_file" 
											/><div style="position:relative;float:left;padding-left:4px;margin-bottom:5px;font-size:12px;">
											   <g:each in="${attachments.findAll{it.inputName == (p.name+'_file')} }" var="attachment"
													>[ <attachments:downloadLink attachment="${attachment }"></attachments:downloadLink
													> ] - <g:if test="${params.pfid && params.pfii && params.pffn}"
														>[ <attachments:deleteLink attachment="${attachment }" returnPageURI="${createLink(controller:'ddc',action:'edit',id:params.id,params:[formId:form.id,pfid:params.pfid,pfii:params.pfii,pffn:params.pffn])}">Delete</attachments:deleteLink> ]
												  </g:if><g:else
												  	>[ <attachments:deleteLink attachment="${attachment }" returnPageURI="${createLink(controller:'ddc',action:'edit',id:params.id,params:[formId:form.id])}">Delete</attachments:deleteLink> ]
												  </g:else>
											  </g:each>
										   </div>
									</g:elseif><g:elseif test="${field?.type == 'Paypal'}"><form:drawPaypalField 
										settings="${settings}" field="${field}" formId="${params.formId}" domainInstance="${domainInstance}"
									/></g:elseif><g:elseif test="${field?.type == 'SingleLineDate'}"><g:if test="${settings.showCalendar}"><div id="singleDateField"><input 
										type="hidden" id="${p.name}Value" disabled="disabled" itsType="SingleLineDate" value="${domainInstance.getAt(p.name)?(sdf.format(domainInstance.getAt(p.name))):''}" 
										/><input type="date" name="${p.name}" itsType="SingleLineDate"
									    /></div></g:if><g:else><div id="boxDateField"><input type="text" style="width:30px !important;" id="${p.name}dateMM" onchange="setFieldValue${p.name}()"
									    /><input type="text" style="width:30px !important;" id="${p.name}dateDD" onchange="setFieldValue${p.name}()"
									    /><input type="text" style="width:60px !important;" id="${p.name}dateYYYY" onchange="setFieldValue${p.name}()"
									    /><input type="hidden" name="${p.name}" id="${p.name}" itsType="SingleLineDate"
									    /></div><script>
										var dateValue = "${domainInstance.getAt(p.name)?(sdf.format(domainInstance.getAt(p.name))):val}"
										try{
											var elements = dateValue.split("/");
											$("#${p.name}dateMM").val(elements[0])
	                                        $("#${p.name}dateDD").val(elements[1])
	                                        $("#${p.name}dateYYYY").val(elements[2])
										}catch(err){
										}

										function setFieldValue${p.name}(){
											try{
												var dateMM = $("#${p.name}dateMM").val()
		                                        var dateDD = $("#${p.name}dateDD").val()
		                                        var dateYY = $("#${p.name}dateYYYY").val()
												var date = ""
												if(dateMM && dateDD && dateYY){
													date = (dateMM.length==1?"0":"")+dateMM+"/"+(dateDD.length==1?"0":"")+dateDD+"/"+dateYY
												}
												$("#${p.name}").val(date).trigger("change")
											}catch(err){
												alert(err)
											}
										}
									    </script
									    ></g:else></g:elseif><g:elseif test="${field?.type == 'SingleLineNumber'}"><input 
										type="text" name="${p.name}" value="${domainInstance.getAt(p.name)!=null?domainInstance.getAt(p.name):''}" itsType="SingleLineNumber" 
									/></g:elseif><g:elseif test="${field?.type == 'FormulaField'}"><input 
										type="${!settings.isEditable?'hidden':'text'}" name="${p.name}" value="${settings.en.newResultType == 'NumberResult'?domainInstance.getAt(p.name):(domainInstance.getAt(p.name)?(sdf.format(domainInstance.getAt(p.name))):'')}" itsType="FormulaField" 
										/><g:if test="${!settings.isEditable}"><input 
											type="text" name="${p.name}show" value="${settings.en.newResultType == 'NumberResult'?domainInstance.getAt(p.name):(domainInstance.getAt(p.name)?(sdf.format(domainInstance.getAt(p.name))):'')}" itsType="FormulaField" disabled="disabled"
										/></g:if><script>
											$(document).ready(function(){var fieldValues = ${fieldValues as grails.converters.JSON};var fieldEventAdded = new Array();for(var i =0;i<fieldValues.length; i++){if(fieldValues[i].indexOf('field')>-1 && $.inArray(fieldValues[i],fieldEventAdded) == -1){fieldEventAdded[fieldEventAdded.length] = fieldValues[i];$('input[name="'+fieldValues[i]+'"]').change(function(event){changeFormulaValue("${p.name}",fieldValues);});}}});
										</script
									></g:elseif><g:elseif test="${field?.type == 'dropdown'}"><g:select 
										name="${p.name}" from='${fieldValues}' noSelection="['': '']"  value="${domainInstance.getAt(p.name)}"
									/></g:elseif><g:elseif test="${field?.type == 'CheckBox'}"
										><%fieldValueList=new ArrayList<String>()
											if(domainInstance.getAt(p.name)){
												fieldValueList=JSON.parse(domainInstance.getAt(p.name).decodeHTML())
											}
										%><g:each in="${fieldValues}" status="idx" var="fieldValue"
											><div style="width:100%;position:relative;float:left;"
												><input type="checkbox" name="${p.name}" id="${p.name}${idx}" ${fieldValueList.contains(fieldValue)?'checked="checked"':''} value="${fieldValue}"
											/>${fieldValue}</div
										></g:each
									><g:if test="${settings.otherOption}"><div style="margin-left:10px;"><%def otherVal = fieldValueList.find{!fieldValues.contains(it)}
											%><input type="text" name="${p.name}" value="${otherVal?:''}" 
											/></div
										></g:if></g:elseif><g:elseif test="${field?.type == 'GroupButton'}"
										><g:set var="fieldValueDomain" value="${domainInstance.getAt(p.name)}" 
										/><g:each in="${fieldValues}" status="idx" var="fieldValue"
											><div style="width:100%;position:relative;float:left;"
												><g:radio name="${p.name}" id="${p.name}${idx}" checked="${fieldValueDomain == fieldValue}" value="${fieldValue}"
											/>${fieldValue}</div
										></g:each
									><g:if test="${settings.otherOption}"><%def otherVal = (!fieldValues.contains(fieldValueDomain)?fieldValueDomain:'') %><div style="margin-left:10px;"><input type="radio" name="${p.name}" id="${p.name}other" style="display:none;"
			                              /><input type="text" id="${p.name}othertext" value="${otherVal?:''}" /></div
			                              ><script>
			                               $("#${p.name}other").val("${otherVal?.encodeAsJavaScript()}")${otherVal!=''?'.attr("checked","checked")':''}
			              		  		   $("#${p.name}othertext").keyup(function(){
			                                    $("#${p.name}other").val($(this).val()).trigger('click');
			                               });
			                              </script></g:if></g:elseif><g:elseif test="${field?.type == 'LikeDislikeButton'}"
										><g:set var="fieldValueDomain" value="${domainInstance.getAt(p.name)}" 
										/><g:set var="countOfLike" value="${domainInstance[p.name+'Like']}" 
										/><g:set var="countOfDislike" value="${domainInstance[p.name+'Dislike']}" 
										/><g:if test="${settings.likeAndVote == true}"
											><div class="voteOption" style="${settings.likeAndVote?"":"display:none;"}" 
												><br
												/><div class="button1 small green" id="${p.name}Vote" style="padding: 12px 12px;margin:0px 0 12px 10px;"> +${countOfLike} Votes</div><br/><br
												/><div class="button button-gray" onclick="setFeildValue${p.name}('Like','Vote')" style="margin:0px 0 12px 10px;">Vote It!</div
											></div
										></g:if
										><g:else
											><div class="likeDislikeOption"
												><g:each in="${fieldValues}" status="idx" var="fieldValue"
													><g:if test = "${idx == 0}"
														><div style="width:100%;position:relative;float:left;"
															><div class="thumbStyle ${fieldValueDomain == "Like"?"thumbUpSel":"thumbUp"}" id="${p.name}thumbsUp" onclick="setFeildValue${p.name}('Like','LikeDislike')" style="margin:0px 0 12px 10px;">&nbsp;</div><span id="${p.name}thumbsUpCount">${fieldValue} (${countOfLike} Likes)</span
														></div
													></g:if
													><g:else
														><div style="width:100%;position:relative;float:left;"
															><div class="thumbStyle ${fieldValueDomain == "Dislike"?"thumbDownSel":"thumbDown"}" id="${p.name}thumbsDown" onclick="setFeildValue${p.name}('Dislike','LikeDislike')" style="margin:0px 0 12px 10px;">&nbsp;</div><span id="${p.name}thumbsDownCount">${fieldValue} (${countOfDislike} Dislikes)</span
														></div
													></g:else
												></g:each
											></div
										></g:else
										><input type="hidden" name="${p.name}" id="${p.name}" value="${fieldValueDomain}"
										><script>
												function setFeildValue${p.name}(selectVal,selectOption){var fieldVal = $("#${p.name}").val();if(selectOption == 'LikeDislike'){if(selectVal == 'Like'){$("#${p.name}thumbsUp").attr('class', 'thumbStyle thumbUpSel');$("#${p.name}thumbsDown").attr('class', 'thumbStyle thumbDown');}else{$("#${p.name}thumbsUp").attr('class', 'thumbStyle thumbUp');$("#${p.name}thumbsDown").attr('class', 'thumbStyle thumbDownSel');}}else{if(fieldVal == 'Like' || fieldVal == 'Dislike'){alert("You have already voted on this question");}else{var newVoteCount = ${countOfLike}+1;$("#${p.name}Vote").html("+"+newVoteCount+" Votes");}}$("#${p.name}").val(selectVal);}
										</script
									></g:elseif><g:elseif test="${field?.type == 'PlainTextHref'}"
										>&nbsp;<a href="http://${fieldValue?fieldValue:settings.en.value }"  target="_"> ${fieldName}</a
									></g:elseif><g:elseif test="${field?.type == 'LinkVideo'}"
										><div id="videoId" 
											><g:if test="${settings.en.urlOrEmbed}">${settings.en.embedHTML}</g:if
											><g:else
												><embed src="${fieldValue?fieldValue:settings.en.value }"
												type="application/x-shockwave-flash"  class="videoDisplay" userSpecifiedWidth="${settings.en.width}" userSpecifiedHeight="${settings.en.height}" 
											 	id="vId" style="border:1px solid #ccc;display: none;"
											/></g:else
										></div
									></g:elseif><g:elseif test="${field?.type == 'ImageUpload'}"
										><div
											><%
											def imgURL = settings.en.value
											def imgUploadAttachments
											try{
												imgUploadAttachments = form.getAttachments(field.name)
												if(settings.en.uploadImage && imgUploadAttachments){
													imgURL = grailsApplication.config.grails.serverURL+'/preview/formImagePath/'+imgUploadAttachments[0].id
												}
											}catch(Exception e){}
											%><img style="display: none;" class="imgDisplay" src="${imgURL }" id="${p.name}" userSpecifiedWidth="${settings.en.width}" userSpecifiedHeight="${settings.en.height}" 
											/><g:if test="${settings.en.clickable }"
												><script>$(document).ready(function(){$('#${p.name}').click(function(){OpenNewWindow('${settings.en.clickableURL?.indexOf('http://')!=0 && settings.en.clickableURL?.indexOf('https://')!=0?('http://'+settings.en.clickableURL):settings.en.clickableURL}')}).css('cursor','pointer')})</script
											></g:if 
										></div
									></g:elseif><g:elseif test="${field?.type == 'ScaleRating'}"
				><table class="datatable" style='width:380px;table-layout:fixed;${settings.moodRate?"display:none;":""}'>
					<thead align="center"><tr>
						<th style="width:100px;max-width:initial;text-overflow:initial;">Very Satisfied</th><th style="width:initial;max-width:initial;">Satisfied</th><th style="width:initial;max-width:initial;">Neutral</th><th style="width:initial;max-width:initial;">Dissatisfied</th>
					</tr></thead>
					<tr>
						<td style="width:100px;max-width:initial;padding-left: 6%;"><g:radio name="${field.name}" value="VerySatisfied" checked='${domainInstance.getAt(p.name) == "VerySatisfied"?true:false}'/></td>
						<td style="width:initial;max-width:initial;padding-left: 6%;"><g:radio name="${field.name}" value="Satisfied" checked='${domainInstance.getAt(p.name)== "Satisfied"?true:false}'/></td>
						<td style="width:initial;max-width:initial;padding-left: 6%;"><g:radio name="${field.name}" value="Neutral" checked='${domainInstance.getAt(p.name)== "Neutral"?true:false}'/></td>
						<td style="width:initial;max-width:initial;padding-left: 6%;"><g:radio name="${field.name}" value="Dissatisfied" checked='${domainInstance.getAt(p.name)== "DisSatisfied"?true:false}'/></td>
					</tr>
				</table><div class="slider" id="${field.name}" style="width:67%;${settings.moodRate?"":"display:none;"}"></div><script>
					$(".slider[id='${field.name}']").slider({value:setMood("${domainInstance.getAt(p.name)}"), min: 25, max: 100,create: function(event,ui){$('a',$(this)).attr('style', 'background-image: url(${grailsApplication.config.grails.serverURL}/plugins/form-builder-0.1/images/face-'+Math.round(setMood("${domainInstance.getAt(p.name)}")/25).toFixed(0)+'.png) !important;').css('left',setMood("${domainInstance.getAt(p.name)}")+'%');},slide: function( event, ui ) {moodToScale(Math.round(ui.value/25).toFixed(0),'${field.name}');$('a',$(this)).attr('style', 'background-image: url(${grailsApplication.config.grails.serverURL}/plugins/form-builder-0.1/images/face-'+Math.round(ui.value/25).toFixed(0)+'.png) !important;');}});
				</script
									></g:elseif><g:elseif test="${field.type == 'Likert'}"
				><%
				def switchRowCol = settings.en.switchRowCol
				def columns = settings.en.columns
				def rows = settings.en.rows
				def columnsToPrint = switchRowCol?rows:columns
				def rowsToPrint = switchRowCol?columns:rows
				%><table class="lTable"
					><thead
						><tr><td>&nbsp;</td
							><g:if test="${columnsToPrint}"
								><g:each in="${columnsToPrint}" var="c"
									><td>${c.encodeAsHTML()}</td
								></g:each
							></g:if><g:else
								><td>&nbsp;</td
							></g:else
						></tr
					></thead
					><tbody
						><g:if test="${rowsToPrint}"
							><g:each in="${rowsToPrint}" var="r" status="rIdx"
								><%
								def rIdxIdx = switchRowCol?0:rIdx
								def cIdxIdx = switchRowCol?rIdx:0
								def rowValue = ""
								try{
									rowValue = domainInstance.getAt(p.name)[rIdxIdx]
								}catch(Exception e){}
								%><tr
									><td>${r.encodeAsHTML()}</td
									><g:if test="${columnsToPrint}"
										><g:each in="${columnsToPrint}" var="c" status="cIdx"
											><%
											rIdxIdx = switchRowCol?cIdx:rIdx
											cIdxIdx = switchRowCol?rIdx:cIdx
											try{
												rowValue = switchRowCol?(domainInstance.getAt(p.name)[rIdxIdx]):rowValue
											}catch(Exception e){}
											%><td><input type="radio" name="${field?.name}__${rIdxIdx}_" value="${cIdxIdx}" ${(""+rowValue) == (""+cIdxIdx)?'checked="checked"':''}/></td
										></g:each
									></g:if><g:else
										><td><input type="radio" name="${field?.name}__${rIdxIdx}_" value="${cIdxIdx}" ${(""+rowValue) == (""+cIdxIdx)?'checked="checked"':''}/></td
									></g:else
								></tr
							></g:each
						></g:if><g:else
							><tr
								><td
									><%
									def rowValue = ""
									try{
										rowValue = domainInstance.getAt(p.name)[0]
									}catch(Exception e){}
									%><input type="radio" name="${field?.name}__0_" value="0" ${(""+rowValue) == "0"?'checked="checked"':''}
								/></td></tr
						></g:else></tbody
				></table
									></g:elseif><g:elseif test="${field?.type == 'PlainText'}"
										>${fieldValue?(fieldValue):(settings.en.text)}</g:elseif
									><g:elseif test="${field?.type == 'SubForm'}"
										><form:drawSubForm settings="${settings}" isEditable="${isEditable}" showFields="${showFields}" domainInstance="${domainInstance}" field="${field}" pfid="${form.id}" style="width:520px;" 
									/></g:elseif
								></td>
							</tr>

												</g:elseif>
											</g:each>
										<tr><td class="name">&nbsp;</td><td class="value">&nbsp;</td></tr>
										<tr>
											<td colspan=2>
								                <g:hiddenField name="formId" value="${params.formId}" />
								                <g:hiddenField name="pfid" value="${params.pfid}" />
								                <g:hiddenField name="pffn" value="${params.pffn}" />
								                <g:hiddenField name="pfii" value="${params.pfii}" />
								                <g:hiddenField name="location" value="" />
								                <script>
													var masterFormFieldMap = ${masterFormFieldMap as grails.converters.JSON}
								                </script>
								                <div class="action">
								                	 <g:if test="${pageBreakFields}">
									                     <div 
															class="backButton" style="display:inline-block;float:left;"
																><g:if test="${pageBreakCount>1}">
																	<input type="button" name="last" class="button button-gray" value="Back" style="width:90px;" />
																</g:if></div>
														<div class="nextButton" style="display:none"><input type="button" name="last" class="button button-gray" value="Next" style="width:90px;" /></div>
										            </g:if>
										             <g:if test="${!fieldsList.findAll{it.type.contains('Paypal')}}">
													<g:if test="${isEditable || showFields }">
														<g:actionSubmit class="button button-green" action="update" params="[formId:params.formId]" style="width: 90px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
														<g:actionSubmit class="button button-red" action="delete" params="[formId:params.formId]" style="width: 90px" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
													</g:if>
													<g:if test="${form.formCat == 'S'}">
												       	<input type="button" class="button button-blue" style="width: 90px" value="${message(code: 'default.button.cancel.label', default: 'Cancel')}"
												            onclick="javascript:backToParentForm('${params.pfii}','${params.pfid}');"/>
												    </g:if>
												    <g:else>
											     		<input type="button" class="button button-blue" style="width: 90px" value="${message(code: 'default.button.list.label', default: 'List')}"
											               onclick="javascript:openFormInstanceList();"/>
											        </g:else>
											        </g:if>
											        </div>
											        <div class="action">
											        <g:if test="${pageBreakFields}">
									                    <span style="float:right;line-height: 35px;">Page ${pageBreakCount++} of ${pageBreakFields.size()+1}</span>
										            </g:if>
											    </div>
							            	</td>
							            </tr>
						               </tbody>
						           </table>
						      
								</section>
								</div>
						</div>
					</g:form>
				</div>
			</section>
		</div>

		</section>
		<script>
			function openFormInstanceList(){
				loadScreenBlock()
				window.location = "${request.getContextPath()}/ddc/list?formId=${params.formId}"
			}
			function backToParentForm(id,formId){
				loadScreenBlock()
				window.location = "${request.getContextPath()}/ddc/edit/"+id+"?formId="+formId
			}
		
			$(document).ready(function() {
				<%
				def fieldRules
				def pageRules
				try{
					def formAdmin = org.grails.formbuilder.FormAdmin.findByForm(form)
					fieldRules = formAdmin?.fieldRulesData
					pageRules = formAdmin?.pageRulesData
				}catch(Exception eRule){
				}
				%>
				fieldRules = ${fieldRules?JSON.parse(fieldRules):'null'};
				pageRules = ${pageRules?JSON.parse(pageRules):'null'};
				handleRuleOn();
				$('input').change(function(){
					setTimeout("handleRuleOn()",50);
				}).keyup(function(){
					setTimeout("handleRuleOn()",50);
				});
				$('textarea').keyup(function(){
					console.log('coming here')
					setTimeout("handleRuleOn()",50);
				});
				$('select').change(function(){
					setTimeout("handleRuleOn()",50);
				});
					var location = geoplugin_countryName();
					$("#location").val(location)
					$.ajax({
						  type:"post",
						  url: "${request.getContextPath()}/PF/saveUserData?location="+location+"&formName=${form.domainClass.name}",
						  success: function(){
							  //on successfully entry
						  },
					      failure : function(){
					    	  //on entry failure
						   }
						});
				});
		
		</script>
	</body>
</html>
