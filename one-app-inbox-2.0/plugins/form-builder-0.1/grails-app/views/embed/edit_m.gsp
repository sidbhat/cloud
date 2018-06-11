<%@page import="org.grails.formbuilder.UniqueFormEntry"%>
<%@ page import="org.grails.formbuilder.Field" %>
<%@ page import="org.grails.formbuilder.Form" %>
<%@ page import="grails.converters.JSON" %>
<%@ page import="java.text.SimpleDateFormat" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="mobile" />
        <title><g:message code="default.new.edit.label" args="[formName]" /></title>
         <style type="text/css">
         .pay_now_button{
			background-image:url(/form-builder/images/Pay_Now.png);
			background-size: 120px;
			background-position: center;
			overflow: hidden;
			width: 120px;
			height: 28px;
		}
		.TimeControlHolder .ui-select{
			width:33%;display:inline-block
		}
		.TimeControlHolder .ui-select .ui-btn-inner{
			padding-left:7%;padding-right:7%;text-align:left
		}
		.TimeControlHolder .ui-select .ui-btn-icon-right .ui-icon{
			right:8px
		}
</style>
    </head>
    <body>
    	<link rel="stylesheet" media="screen" href="${request.getContextPath()}/css/jquery-ui-1.8.20.custom.css" />
        <script type="text/javascript" src="${request.getContextPath() }/js/jquery/jquery-ui-1.8.20.custom.min.js"></script>
        <script type="text/javascript" src="${resource(dir: 'js', file: 'lookup.mobile.js')}"></script>
        <script type="text/javascript">
			function setLookUp(fieldName,fieldId,formId,mapMasterForm){
				$('input[name="'+fieldName+'"]').each(function(){
					$(this).autocomplete({
						source: "${createLink(controller:'PF',action:'lookUp')}?field="+fieldId+"&formId="+formId,
						minLength: 2,
						select: function( event, ui ) {
							setMappedFields(ui.item.wholeObj,fieldName,mapMasterForm);
						},
						focus: function( event, ui ) {
							$(this).blur();
							$(this).focus();
							setMappedFields(ui.item.wholeObj,fieldName,mapMasterForm);
						}
					});
				});
			}
			function setHiddenCheckBoxValue(shownEle, hiddenEleId){
				if(shownEle.checked){
					$('#'+hiddenEleId).val(shownEle.value);
				}else{
					$('#'+hiddenEleId).val('');
				}
			}
		</script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/pageBreak.js'}"></script>
		<script type="text/javascript" src="${request.getContextPath()+'/js/asecmob.js'}"></script>
	<% def groupName %>
		<div data-role="header" data-position="fixed">
			<g:if test="${formInstance.formCat == 'S'}">
				<a data-icon="back" class="ui-btn-left" href="${request.getContextPath()}/embed/edit/${params.pfii}?formId=${params.pfid}" >Back</a>
			</g:if>
			<h1><g:message code="default.new.edit.label" args="[formName]" /></h1>
		</div>
		
		<div data-role="content">
		<g:if test="${domainInstance.errors }">
					<div id="closeable" class="message success closeable" style="padding:0px 5px 5px 15px;position: absolute;right:0px;left:0;margin:auto;width:170px;top:50px;font-weight:bold;display: none;z-index:1000;font-size:14px;">
			             <p style="text-align:center;">
				            <g:each in="${domainInstance.errors }" var="error">
				            	<g:if test="${error.name != 'version'}">
				                	<g:set var="fieldWithSettings" value="${fields[error.name] }" />
				                	<g:if test="${fieldWithSettings }">
					               	   <g:set var="fieldName" value="${JSON.parse(fieldWithSettings.settings)?.en?.label}" />
					               	    <li><g:message code="${error.code}" args="${[fieldName,formName] }" default="${error.defaultMessage }"/></li>
				                	</g:if>
				                </g:if>
				                <g:else>
									<li><g:message code="${error.code}" args="${['version',formName] }" default="${error.defaultMessage }" /></li>
								</g:else>
			                </g:each>
						 </p>
				         </div>
			          <script>
			          	$(document).ready(function(){
				          	setTimeout("showMessage()",500);
				          	});
			             	
			             </script>
					</g:if>	
			<g:if test="${isEditable}">
			<g:uploadForm action="update" ${ multiPart ? ' enctype="multipart/form-data"' : '' } >
				<g:hiddenField name="id" value="${domainInstance?.id}" />
				<g:hiddenField name="version" value="${domainInstance?.version}" />
				<div class="formControls">
				<g:set var="showFields" value="${false }" />
				<sec:ifAnyGranted roles="${rtsaf }">
					<g:set var="showFields" value="${true }" />
				</sec:ifAnyGranted>
				<g:set var="dateFormat" value="${grailsApplication.config.format.date}" />
				<g:set var="sdf" value="${new SimpleDateFormat(dateFormat?:'MM/dd/yyyy')}" />
				<g:set var="masterFormFieldMap" value="${[:]}" /><g:set var="display" value="${true }" />
				<g:set var="fieldsList" value="${formInstance.fieldsList as List}" /><g:set var="fieldsList" value="${fieldsList.sort { it.sequence}}" />
				<g:set var="pageBreakFields" value="${fieldsList.findAll{it.type == 'PageBreak'}}" /><g:set var="pageBreakCount" value="${1}" />
				<g:if test="${formInstance?.formCat !='S' }">
				<g:set var="uniqueId" value="" />
					<%try{
						uniqueId = UniqueFormEntry.findByFormIdAndInstanceId(formInstance?.id,domainInstance?.id)
						valueOfField = uniqueId?.uniqueId
					}catch(Exception e){
					} %>
				<div data-role="fieldcontain" style="padding:0.4em 15px;border:none;">
				<label for="UniqueId" class="ui-input-text">Unique Id</label><input type="text"  value="${valueOfField}"  disabled="disabled" readonly="readonly" />
				</div></g:if><g:each in="${fieldsList}" var="p" status="i">
					<g:if test="${p.type == 'PageBreak'}">
						<div data-role="fieldcontain" style="padding:0.4em 15px;border:none;"><div 
                               	class="backButton" style="width:49%;display:inline-block;text-align:right;vertical-align:top;"
								><input type="${pageBreakCount<=1?'hidden':'button'}" name="${p.name }" class="button button-gray" value="Back" style="width:100%;float:none;" />
								</div><div 
							class="nextButton" style="width:49%;display:inline-block;"><input 
								type="button" name="${p.name }" class="button button-gray" value="Next" style="width:100%;float:none;" /> Page ${pageBreakCount++} of ${pageBreakFields.size()+1}</div></div></div><div style="display:none;width:100%;" class="formControls">
							</g:if> 
                       <g:elseif test="${display || showFields || p.type == 'FormulaField'}">
                            <div data-role="fieldcontain" id="${p.name}ControlHolder" style="padding:0.4em 15px;border:none;">
                                    <label for="${p.name}" class="ui-input-text">
                                    <g:if test="${fields.get(p.name) != null}">
                                		<g:set var="field" value="${fields.get(p.name)}" />
                                		<g:set var="settings" value="${JSON.parse(field.settings)}" />
                                		<g:if test="${settings.mapMasterForm && settings.mapMasterField }">
                                			<g:if test="${!masterFormFieldMap.getAt(settings.mapMasterForm) }">
                                				<%masterFormFieldMap."${settings.mapMasterForm}" = []%>
                                			</g:if>
                                			<%masterFormFieldMap."${settings.mapMasterForm}" << [thisFormField:p.name,thisFormFieldType:field.type,mappedField:settings.mapMasterField]%>
                                		</g:if>
                                		<g:set var="fieldName" value="${settings.en.label}" /><g:set var="val" value="${settings.en.value }" />
                                		<g:set var="required" value="${settings.required}" /><g:set var="help" value="${settings.en.description }" />
                                		<g:if test="${field.type == 'CheckBox' || field.type == 'dropdown' || field.type == 'GroupButton' || field.type == 'FormulaField' || field.type == 'LikeDislikeButton' }">
                                			<g:set var="fieldValues" value="${val}" />
                                		</g:if><g:if test="${field?.type == 'Paypal'}" ><g:set var="fieldName" value="" /></g:if>
                                		<g:if test="${p.type!='PlainTextHref' }">
                                    	<label for="${p.name}">
                                    	<g:if test="${help}"><a 
                                		href="#" title="${help}"
                                		>${fieldName?.encodeAsHTML()}</a></g:if><g:else
                                	>${fieldName?.encodeAsHTML()}</g:else><g:if 
                                	test="${required }"><em>*</em></g:if></label></g:if></g:if><g:else
                                ><g:if test="${p.type!='PlainTextHref' }"><label for="${p.name}">${p.naturalName}</label></g:if></g:else>
                                	</label>
	                             <g:if test="${field?.type == 'SingleLineText' }"
	                             	><g:textField name="${p.name}" value="${domainInstance.getAt(p.name)}"
	                             /></g:if>
	                             <g:elseif test="${field?.type == 'Phone' }"
	                             	><g:textField name="${p.name}" value="${domainInstance.getAt(p.name)}"
	                             /></g:elseif>
	                             <g:elseif test="${field?.type == 'Email' }"
	                             	><g:textField name="${p.name}" value="${domainInstance.getAt(p.name)}"
	                             /></g:elseif>
	                             <g:elseif test="${field?.type == 'LookUp' }"><g:textField 
                                		name="${p.name}" value="${domainInstance.getAt(p.name)?:''}" 
                                		/><div style="border: 1px solid #ccc;display: inline-block;cursor: pointer;border-radius: 12px;background: url(/form-builder/images/icons/magnifier.png) center no-repeat;width: 24px;height: 24px;margin:-13px 2px;" id="${p.name}search"></div
                                		><script type="text/javascript">
	                                		$(document).ready(function(){
												$( "#${p.name}" ).autocomplete({source:"${createLink(controller:'PF',action:'lookUp',params:[field:field.id,formId:form.id])}",minLength:2,select:function(event,ui){setMappedFields(ui.item.wholeObj,"${p.name}",'${settings.mapMasterForm}')},focus:function(event,ui){$(this).blur();$(this).focus();setMappedFields(ui.item.wholeObj,"${p.name}",'${settings.mapMasterForm}')}});
												$( "#${p.name}search" ).click(function(){$("#${p.name}").autocomplete("option","minLength",0).autocomplete("search",$("#${p.name}").val()).autocomplete("option","minLength",2).focus();});
											});
										</script
									></g:elseif><g:elseif test="${field?.type == 'MultiLineText'}"><g:textArea 
										name="${p.name}" value="${domainInstance.getAt(p.name)?:''}" style="height:${(settings.fieldSize?settings.fieldSize.toInteger():2)*1*65+'px'};resize: none;" 
									/></g:elseif><g:elseif test="${field?.type == 'FileUpload'}"><input type="file" name="${p.name}_file" />
									 <%
									def attachments = (domainInstance && domainInstance.id?(formInstance.getDomainAttachments(domainInstance.id)):null)
									def itsAttachments = attachments?.findAll{it.inputName == field.name+'_file'}
									String attachmentText=""
									itsAttachments?.each{attachment->
											attachmentText += ('<br/><label class="ui-input-text" ></label ><a href="#">'+attachment.name.encodeAsHTML()+'</a><br/>')
									}
									 %>
									 ${attachmentText}
									</g:elseif><g:elseif test="${field?.type == 'Paypal'}"><form:drawPaypalMobField
										settings="${settings}" field="${field}" formId="${params.formId}" domainInstance="${domainInstance}"
									/></g:elseif><g:elseif test="${field?.type == 'SingleLineDate'}">
									 <input type="date" name="${p.name}" value="${domainInstance.getAt(p.name)?(sdf.format(domainInstance.getAt(p.name))):''}" itsType="SingleLineDate"  data-role="datebox" data-options='{"mode": "flipbox","dateFormat": "MM/DD/YYYY"}'
									  /><g:if test="${settings.timeFormat}"
									  			></div><div data-role="fieldcontain" id="${p.name}TimeControlHolder" style="padding:0.4em 15px;border:none;" class="TimeControlHolder"
									  				><%
													  def hoursValue = ""
													  def minutesValue = ""
													  def meridianValue = ""
													  try{
														  minutesValue = domainInstance."${p.name}"?new SimpleDateFormat("mm").format(domainInstance."${p.name}"):'00'
														  if(settings.timeFormat == 'HH:mm'){
															  hoursValue = domainInstance."${p.name}"?new SimpleDateFormat("HH").format(domainInstance."${p.name}"):'00'
														  }else if(settings.timeFormat == 'hh:mm a'){
															  hoursValue = domainInstance."${p.name}"?new SimpleDateFormat("hh").format(domainInstance."${p.name}"):'12'
															  meridianValue = domainInstance."${p.name}"?new SimpleDateFormat("a").format(domainInstance."${p.name}"):'AM'
														  }
													  }catch(Exception e){System.out.println(e)}
									  				%>
									  				<select name="${p.name}Hours" id="${p.name}Hours"><g:each in="${(settings.timeFormat=='HH:mm'?(0..23):(1..12)) }" var="hour"><option value="${hour<10?('0'+hour):hour }" ${hoursValue == (hour<10?('0'+hour):(hour+''))?'selected="selected"':''}>${hour<10?('0'+hour):hour }</option></g:each></select
									  				><select name="${p.name}Minutes" id="${p.name}Minutes"><g:each in="${0..59}" var="minutes"><option value="${minutes<10?('0'+minutes):minutes }" ${(minutesValue+'') == (minutes<10?('0'+minutes):(minutes+''))?'selected="selected"':''}>${minutes<10?('0'+minutes):minutes }</option></g:each></select
									  				><g:if test="${settings.timeFormat=='hh:mm a' }"><select name="${p.name}Meridian" id="${p.name}Meridian"><g:each in="${['AM','PM']}" var="meridian"><option value="${meridian}" ${meridianValue == meridian?'selected="selected"':''}>${meridian}</option></g:each></select></g:if
									  		></g:if
									  ></g:elseif><g:elseif test="${field?.type == 'SingleLineNumber'}"
									  	> <% 
								  		boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false;
										String currency=currencyType?grailsApplication.config?.formBuilder.currencies[settings.currencyType]:''
										int decimalPlaces=2
										try{
											 decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
										}catch (Exception e) {}
										def value=domainInstance.getAt(p.name)!=null?domainInstance.getAt(p.name):''
										if(decimalPlaces<2)
										try{
											def val=domainInstance.getAt(p.name)!=null?new BigDecimal(domainInstance.getAt(p.name)):''
											 value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
										}catch(Exception e){}
										 %><span style="position:absolute;padding:9px 0 0 3px;">${currency?:''}</span><input type="number" name="${p.name}" value="${value}" itsType="SingleLineNumber" style="padding-left:.8em;padding-right:0;"
									/></g:elseif><g:elseif test="${field?.type == 'FormulaField'}">
									<input ${!settings.isEditable?'type="hidden"':'type="text"'} name="${p.name}" value="${settings.en.newResultType == 'NumberResult'?domainInstance.getAt(p.name):(domainInstance.getAt(p.name)?(sdf.format(domainInstance.getAt(p.name))):'')}" itsType="FormulaField" />
	                                       	<g:if test="${!settings.isEditable}">
										   		<input type="text" name="${p.name}show" value="${settings.en.newResultType == 'NumberResult'?domainInstance.getAt(p.name):(domainInstance.getAt(p.name)?(sdf.format(domainInstance.getAt(p.name))):'')}" itsType="FormulaField" disabled="disabled"/>
											</g:if>
											   <script>
											   		$(document).bind('pagecreate',function(){
														var fieldValues = ${fieldValues as grails.converters.JSON};
														var fieldEventAdded = new Array();
														for(var i =0;i<fieldValues.length; i++){
															if(fieldValues[i].indexOf('field')>-1 && $.inArray(fieldValues[i],fieldEventAdded) == -1){
																fieldEventAdded[fieldEventAdded.length] = fieldValues[i]
																$('input[name="'+fieldValues[i]+'"]').change(function(event){changeFormulaValue("${p.name}",fieldValues);});
															}
														}
													});
											   </script>
									</g:elseif>
									<g:elseif test="${field?.type == 'dropdown'}"><g:select 
										name="${p.name}" from='${fieldValues}' noSelection="['': '']"  value="${domainInstance.getAt(p.name)}"
									/></g:elseif><g:elseif test="${field?.type == 'CheckBox'}"
										><%fieldValueList=new ArrayList<String>()
											if(domainInstance.getAt(p.name)){
												fieldValueList=JSON.parse(domainInstance.getAt(p.name).decodeHTML())
											}
										%><fieldset data-role="controlgroup">
										  		 <g:each in="${fieldValues}" status="idx" var="fieldValue">
												   	<input type="hidden"   name="${p.name}" id="${p.name}${idx}" ${fieldValueList.contains(fieldValue)?'value="'+fieldValue+'"':''} />
											   		<input type="checkbox" name="${p.name}${idx}${idx}" id="${p.name}${idx}${idx}" class="custom" ${fieldValueList.contains(fieldValue)?'checked="checked"':''} value="${fieldValue}" onchange="setHiddenCheckBoxValue(this,'${p.name}${idx}')"/> 
													<label for="${p.name}${idx}${idx}">${fieldValue}</label>
										   		 </g:each>
										</fieldset></g:elseif><g:elseif test="${field?.type == 'GroupButton'}"
										><g:set var="fieldValueDomain" value="${domainInstance.getAt(p.name)}" 
										/><fieldset data-role="controlgroup" data-mini="true">
									   		<g:each in="${fieldValues}" status="idx" var="fieldValue">
                                       			<g:radio name="${p.name}" id="${p.name}${idx}" checked="${fieldValueDomain == fieldValue}" value="${fieldValue}"/>
												 <label for="${p.name}${idx}"> ${fieldValue}</label>
									   		</g:each>
										</fieldset>
									</g:elseif><g:elseif test="${field?.type == 'AddressField'}"
										><%
										def mapValue=[:]
										try{
											mapValue = JSON.parse(domainInstance.getAt(p.name))
										}catch(Exception e){}
									%><div class="addressControl"><label for="${p.name}_city" class="ui-input-text"> Address Line 1</label><input type="text"  value="${mapValue?.'line1'}" name="${p.name}_line1"  id="${p.name}_line1" class="addressInput"
											><label for="${p.name}_line2" class="ui-input-text"> Address Line 2</label><input type="text"  value="${mapValue?.'line2'}"name="${p.name}_line2" id="${p.name}_line2" class="addressInput"
											><label for="${p.name}_city" class="ui-input-text">City</label ><input type="text" value="${mapValue?.'city'}" name="${p.name}_city" id="${p.name}_city" class="addressInput"
											><label for="${p.name}_state" class="ui-input-text">State</label><input type="text"  value="${mapValue?.'state'}" name="${p.name}_state" id="${p.name}_state" class="addressInput"
				    						><label for="${p.name}_zip" class="ui-input-text">Zip/Postal code</label><input type="text" value="${mapValue?.'zip'}" name="${p.name}_zip" id="${p.name}_zip" class="addressInput"
				    						><label for="${p.name}_Country" class="ui-input-text">Country</label><input type="text"  value="${mapValue?.'country'}" name="${p.name}_country" id="${p.name}_country" class="addressInput"
			    						></div
		    						></g:elseif>
		    						<g:elseif test="${field?.type == 'NameTypeField'}"
										><%
										boolean pre=settings?.showPrefix
										boolean mid=settings?.showMiddleName
										def mapValue=[:]
										try{
											mapValue = JSON.parse(domainInstance.getAt(p.name))
										}catch(Exception e){}
									%><div class="nameControl"
											><g:if test="${pre}"><label for="${p.name}pre" class="ui-input-text">Prefix</label><input type="text"  value="${mapValue?.'pre'}"name="${p.name}pre" id="${p.name}pre" class="nameInput"
											></g:if><label for="${p.name}fn" class="ui-input-text">First Name</label><input type="text"  value="${mapValue?.'fn'}"name="${p.name}fn" id="${p.name}fn" class="nameInput"
											><g:if test="${mid}"><label for="${p.name}mn" class="ui-input-text">Middle Name</label ><input type="text" value="${mapValue?.'mn'}" name="${p.name}mn" id="${p.name}mn" class="nameInput"
											></g:if><label for="${p.name}ln" class="ui-input-text">Last Name</label><input type="text"  value="${mapValue?.'ln'}" name="${p.name}ln" id="${p.name}ln" class="nameInput" 
			    						></div
		    						></g:elseif
		    						><g:elseif test="${field?.type == 'LikeDislikeButton'}"
										><g:set var="fieldValueDomain" value="${domainInstance.getAt(p.name)}" 
										/><g:set var="countOfLike" value="${domainInstance[p.name+'Like']}" 
										/><g:set var="countOfDislike" value="${domainInstance[p.name+'Dislike']}" 
										/><g:if test="${settings.likeAndVote == true}"><div class="voteOption" style="${settings.likeAndVote?"":"display:none;"}"><br/>
													<div class="button1 small green" id="${p.name}Vote" style="padding: 12px 20px;margin:0px 0 12px 10px;"> +${countOfLike} Votes</div><br/><br/> 
													<div data-role="button" data-inline="true" style="float:left;font-size:11px;margin:0px 0 12px 10px;" onclick="setFeildValue${p.name}('Like','Vote')" >Vote It!</div> 
												</div></g:if><g:else><div class="likeDislikeOption" style="margin-top:10px;"><g:each in="${fieldValues}" status="idx" var="fieldValue"><g:if test = "${idx == 0}"><div 
									   			style="width:100%;position:relative;float:left;"
									   				><div class="thumbStyle ${fieldValueDomain == "Like"?"thumbUpSel":"thumbUp"}" id="${p.name}thumbsUp" onclick="setFeildValue${p.name}('Like','LikeDislike')" style="margin:0px 0 12px 10px;">&nbsp;</div>${fieldValue} (${countOfLike} Likes)</div></g:if>
									   		<g:else><div 
									   			style="width:100%;position:relative;float:left;"
									   				><div class="thumbStyle ${fieldValueDomain == "Dislike"?"thumbDownSel":"thumbDown"}" id="${p.name}thumbsDown" onclick="setFeildValue${p.name}('Dislike','LikeDislike')" style="margin:0px 0 12px 10px;">&nbsp;</div>${fieldValue} (${countOfDislike} Dislikes)</div></g:else></g:each></div>
									   		</g:else>
									   		<input type="hidden" name="${p.name}" id="${p.name}" value="${fieldValueDomain}"> 
									   		<script>
								  				function setFeildValue${p.name}(selectVal,selectOption){
												  		var fieldVal = $("#${p.name}").val()
												  		if(selectOption == 'LikeDislike'){
									  						if(selectVal == 'Like'){
									  							$("#${p.name}thumbsUp").attr('class', 'thumbStyle thumbUpSel')
																$("#${p.name}thumbsDown").attr('class', 'thumbStyle thumbDown')
									  						}else{
															    $("#${p.name}thumbsUp").attr('class', 'thumbStyle thumbUp')
																$("#${p.name}thumbsDown").attr('class', 'thumbStyle thumbDownSel')
															}
												  		}else{
															if(fieldVal == 'Like' || fieldVal == 'Dislike'){
																alert("You have already voted on this question")
															}else{
																var newVoteCount = ${countOfLike}+1
																$("#${p.name}Vote").html("+"+newVoteCount+" Votes")
															}
														}
								  						$("#${p.name}").val(selectVal)
								  					}
								  			</script>
									</g:elseif><g:elseif test="${field?.type == 'PlainTextHref'}"
										>&nbsp;<a href="http://${fieldValue?fieldValue:settings.en.value }"  target="_"> ${fieldName}</a
									></g:elseif><g:elseif test="${field?.type == 'LinkVideo'}"
										><g:if test="${settings.en.urlOrEmbed}">${settings.en.embedHTML}</g:if
										><g:else><object width="425" height="350" >  
  											<param name="movie" value="" id="video" />
  											<param name="wmode" value="transparent" />  
  											 &nbsp;<embed src="${fieldValue?fieldValue.replace('@','/'):settings."en".value.replace('@','/') }"  
        										   type="application/x-shockwave-flash"  
         										   width="300" height="200" id="vId"/> 
											</object
										></g:else
									></g:elseif><g:elseif test="${field?.type == 'ImageUpload'}"
										> &nbsp;<%
											def imgURL = settings.en.value
											def imgUploadAttachments
											try{
												imgUploadAttachments = formInstance.getAttachments(field.name)
												if(settings.en.uploadImage && imgUploadAttachments){
													imgURL = grailsApplication.config.grails.serverURL+'/preview/formImagePath/'+imgUploadAttachments[0].id
												}
											}catch(Exception e){}
											%><img id="${p.name}" src="${imgURL }" width="100%" 
											/><g:if test="${settings.en.clickable }"
												><script>$(document).ready(function(){$('#${p.name}').click(function(){OpenNewWindow('${settings.en.clickableURL?.indexOf('http://')!=0 && settings.en.clickableURL?.indexOf('https://')!=0?('http://'+settings.en.clickableURL):settings.en.clickableURL}')}).css('cursor','pointer')})</script
											></g:if>
                                        </g:elseif><g:elseif test="${field?.type == 'ScaleRating'}">
                                        			<g:if test="${settings.moodRate}">
                                        				<g:set var="moodScaleMap" value="['VerySatisfied':100,'Satisfied':75,'Neutral':50,'Dissatisfied':25]" />
												  		<g:set var="fieldScaleValue" value="${domainInstance.getAt(p.name)}" />
														<input type="hidden" name="${field.name}"value="${domainInstance.getAt(p.name)}"/>
														<input type="range" name="${field.name}slider" value="${fieldScaleValue?moodScaleMap[fieldScaleValue]:75}" min="25" max="100" onchange="setRatingMobile(this.value,'${field.name}',this);" style="display:none;"/>
                                        			</g:if>
                                        			<g:else>
                                        			<fieldset data-role="controlgroup" data-mini="true" style="${settings.moodRate?"display:none;":""}">
															 <g:radio name="${field.name}" id="scale-1" value="VerySatisfied" checked='${domainInstance.getAt(p.name) == "VerySatisfied"?true:false}'/>
														   	 <label for="scale-1">Very Satisfied</label>
															
															 <g:radio name="${field.name}" id="scale-2" value="Satisfied" checked='${domainInstance.getAt(p.name)== "Satisfied"?true:false}'/>
															 <label for="scale-2">Satisfied</label>
															 
															 <g:radio name="${field.name}" id="scale-3" value="Neutral" checked='${domainInstance.getAt(p.name)== "Neutral"?true:false}'/>
															 <label for="scale-3">Neutral</label>
															 
															 <g:radio name="${field.name}" id="scale-4" value="Dissatisfied" checked='${domainInstance.getAt(p.name)== "Dissatisfied"?true:false}'/>
															 <label for="scale-4">Dissatisfied</label>
											  		</fieldset>
                                        			</g:else
                                        ></g:elseif><g:elseif test="${field.type == 'Likert'}"
                    	><g:if test="${settings.en.rows}"
							><g:each in="${settings.en.rows}" var="r" status="rIdx"
								><%
								def rowValue = ""
								try{
									rowValue = domainInstance.getAt(p.name)[rIdx]
								}catch(Exception e){}
								%><fieldset data-role="controlgroup" data-mini="true">
									<legend>${r.encodeAsHTML()}</legend>
									<g:if test="${settings.en.columns}"
										><g:each in="${settings.en.columns}" var="c" status="cIdx"
											><input type="radio" name="${field?.name}__${rIdx}_" id="${field?.name}__${rIdx}_-${cIdx}" value="${cIdx}" ${(""+rowValue) == (""+cIdx)?'checked="checked"':''}/><label for="${field?.name}__${rIdx}_-${cIdx}">${c.encodeAsHTML()}</label
										></g:each
									></g:if><g:else
										><input type="radio" name="${field?.name}__${rIdx}_" id="${field?.name}__${rIdx}_-0" value="0" ${(""+rowValue) == "0"?'checked="checked"':''}/><label for="${field?.name}__${rIdx}_-0">${c.encodeAsHTML()}</label
									></g:else
								></fieldset
						  	></g:each
						></g:if><g:else
							><fieldset data-role="controlgroup" data-mini="true">
								<legend>&nbsp;</legend
								><%
								rowValue = ""
								try{
									rowValue = domainInstance.getAt(p.name)[0]
								}catch(Exception e){}
								%><g:if test="${settings.en.columns}"
									><g:each in="${settings.en.columns}" var="c" status="cIdx"
										><input type="radio" name="${field?.name}__0_" id="${field?.name}__0_-${cIdx}" value="${cIdx}" ${(""+rowValue) == (""+cIdx)?'checked="checked"':''}/><label for="${field?.name}__0_-${cIdx}">${c.encodeAsHTML()}</label
									></g:each
								></g:if><g:else
									><input type="radio" name="${field?.name}__0_" id="${field?.name}__0_-0" value="0" ${(""+rowValue) == "0"?'checked="checked"':''}/><label for="${field?.name}__0_-0">${c.encodeAsHTML()}</label
								></g:else
							></fieldset
						></g:else
												></g:elseif><g:elseif test="${field?.type == 'PlainText'}"
										>${fieldValue?(fieldValue):(settings.en.text)}</g:elseif
									><g:elseif test="${field?.type == 'SubForm'}"
										><form:drawSubFormMobile settings="${settings}" isEditable="${isEditable}" showFields="${showFields}" domainInstance="${domainInstance}" field="${field}" pfid="${formInstance.id}" style="width:520px;" 
									/></g:elseif></div></g:elseif>
							</g:each>
                      
                        <g:if test="${pageBreakFields}">
		                     <div data-role="fieldcontain" style="padding:0.4em 15px;border:none;"><div 
								class="backButton" style="width:49%;display:inline-block;text-align:right;vertical-align:top;"
								><g:if test="${pageBreakCount>1}">
									<input type="button" name="last" class="button button-gray" value="Back" style="width:100%;float:none;" />
								</g:if></div><div 
								class="nextButton" style="width:49%;display:inline-block;padding-top: 8px;"><input type="hidden" name="last" class="button button-gray" value="Next" /> Page ${pageBreakCount++} of ${pageBreakFields.size()+1}</div></div>
			            </g:if>
	          		<g:hiddenField name="formId" value="${params.formId}" />
	           		<g:hiddenField name="pfid" value="${params.pfid}" />
                	<g:hiddenField name="pffn" value="${params.pffn}" />
                	<g:hiddenField name="pfii" value="${params.pfii}" />
                	<script>
						var masterFormFieldMap = ${masterFormFieldMap as grails.converters.JSON}
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
						});
	                </script>
	                <div>&nbsp;</div>
                	<div>&nbsp;</div>
					<g:if test="${!fieldsList.findAll{it.type.contains('Paypal')}}">
						<g:submitButton name="update" params="[formId:params.formId]" data-icon="check" value="${message(code: 'default.button.update.label', default: 'Update')}" />
					</g:if>
				</div>
			</g:uploadForm>
			</g:if>
		</div>
		<div data-role="footer">
		</div>
		

    </body>
</html>
