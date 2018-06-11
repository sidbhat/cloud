<style>
.FieldDiv{
	border:1px solid gray;
	border-bottom:0;
	margin:0 10px;
	padding:5px;
}
.FieldDiv:first-child{
	margin-top:10px;
	border-top-left-radius:5px;
	border-top-right-radius:5px;
}
 .FieldDiv:last-child{
 	margin-bottom:10px;
	border-bottom-left-radius:5px;
	border-bottom-right-radius:5px;
	border-bottom:1px solid gray;
 }
label{display:inline-block;width:200px;}
label.checkbox{display:block;width:auto;}
td{padding:0;}
.partialResponseDiv{width:48%;display:inline-block}
.RequiredField label em:after{
	content:"*";
	color:red;
}
</style>
<g:if test="${errorMessage }">
	${errorMessage }
</g:if>
<g:else>
	<g:if test="${res }">
		<div>
			<div class="partialResponseDiv">
				<div style="height:480px;overflow:auto;border:2px inset;">
					<g:uploadForm action="entrySave" target="entrySaveResponse">
						<g:hiddenField name="pretty" value="true"/>
						<div class="FieldDiv">
							<div><label for="apiKey">API-Key</label><input type="text" name="apiKey" id="apiKey" placeholder="Enter API-Key" value="${params.apiKey }"/></div>
							<div><label for="formId">Form Id</label><input type="text" name="formId" id="formId" placeholder="Enter Form Id" value="${params.formId }"/></div>
							<div><label for="entryId">Entry Id</label><input type="text" name="entryId" id="entryId" placeholder="Enter Entry Id (for update)" value="${params.entryId }"/></div>
							<g:if test="${params.parentFormId }">
								<div><label for="entryId">Parent Form Id</label><input type="text" name="parentFormId" placeholder="Parent Form Id (Only for Sub Forms)" title="Parent Form Id (Only for Sub Forms)" value="${params.parentFormId }"/></div>
								<div><label for="entryId">Parent Form Entry Id</label><input type="text" name="parentFormEntryId" placeholder="Parent Form Entry Id (Only for Sub Forms)" title="Parent Form Entry Id (Only for Sub Forms)" value="${params.parentFormEntryId }"/></div>
								<div><label for="entryId">Parent Form Field Name</label><input type="text" name="parentFormFieldName" placeholder="Parent Form Field Name (Only for Sub Forms)" title="Parent Form Field Name (Only for Sub Forms)" value="${params.parentFormFieldName }"/></div>
							</g:if>
							<div><label for="respType">Response Type</label><g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value" value="${params.respType }"/></div>
						</div>
						<g:each in="${res }" var="field">
							<g:if test="${!nonPersistableFieldsList.contains(field.type)}">
								<g:set var="fieldName" value="${field.Name }"/>
								<g:set var="fieldTitle" value="${field.Title }"/>
								<div class="FieldDiv ${field.IsRequired?'RequiredField':'' }">
									<g:if test="${field.type == 'CheckBox' }">
										<label>${fieldTitle }<em></em></label>
										<g:each in="${field.SubFields }" var="SubField">
											<label class="checkbox"><input type="checkbox" name="${fieldName }" value="${SubField.Score }" />${SubField.Label }</label>
										</g:each>
									</g:if>
									<g:elseif test="${field.type == 'Likert' }">
										<label>${fieldTitle }<em></em></label>
										<table cellspacing="0" cellpadding="0">
											<g:each in="${field.SubFields }" var="SubField">
												<tr>
													<td><label for="${SubField.Name }">${SubField.Label }</label></td>
													<td><g:select name="${SubField.Name }" from="${field.Choices }" optionKey="Score" optionValue="Label" noSelection="${[Score:'',Label:'']}"/></td>
												</tr>
											</g:each>
										</table>
									</g:elseif>
									<g:elseif test="${field.type == 'GroupButton' || field.type == 'LikeDislikeButton' || field.type == 'ScaleRating' || field.type == 'dropdown' || field.type == 'dropdown'}">
										<label for="${field.Name }">${field.Title }<em></em></label><g:select name="${field.Name }" from="${field.Choices }" optionKey="Score" optionValue="Label" noSelection="${[Score:'',Label:'']}"/>
									</g:elseif>
									<g:elseif test="${field.type == 'FileUpload' }">
										<label for="${field.Name }">${field.Title }<em></em></label><input type="file" name="${field.Name }"/>
									</g:elseif>
									<g:elseif test="${field.type == 'MultiLineText' }">
										<label for="${field.Name }">${field.Title }<em></em></label><g:textArea name="${field.Name }"></g:textArea>
									</g:elseif>
									<g:elseif test="${field.type == 'SubForm' }">
										
									</g:elseif>
									<g:else>
										<g:if test="${field.SubFields }">
											<label>${fieldTitle }<em></em></label>
											<table>
												<tbody>
													<g:each in="${field.SubFields }" var="SubField">
														<tr>
															<td><label for="${SubField.Name }">${SubField.Label }</label></td>
															<td><g:textField name="${SubField.Name }" /></td>
														</tr>
													</g:each>
												</tbody>
											</table>
										</g:if>
										<g:else>
											<label for="${field.Name }">${field.Title }<em></em></label><g:textField name="${field.Name }" />
										</g:else>
									</g:else>
								</div>
							</g:if>
						</g:each>
						<div class="FieldDiv">
							<input type="submit" value="Submit"/>
						</div>
					</g:uploadForm>
				</div>
			</div>
			<div class="partialResponseDiv" style="float:right">
				<iframe id="entrySaveResponse" width="100%" height="480px;"></iframe>
			</div>
		</div>
	</g:if>
</g:else>