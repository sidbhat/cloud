<%@ page import="grails.converters.JSON" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.oneapp.cloud.core.*" %>
<%@ page import="org.grails.paypal.Payment" %>
<%@ page import="org.grails.paypal.PaymentItem" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="default.list.label" args="[formName]" /></title>
        <link rel="stylesheet" href="${resource(dir: 'css/streamlined', file: 'DasMain.css')}"/>
         <script type="text/javascript">
			function goToPage(requestParams){
   				 window.location.href="${createLink(controller:'ddc' ,action:'list' ,params:[formId:params.formId,max:''])}" + requestParams;
   			 }
	        if(navigator.appName == "Microsoft Internet Explorer"){
				$(document).ready(function(){
					$("td.expandibleTds").trigger('click');
					$("td.expandibleTds").each(function(){
						this.onclick = function(){}
					});
				});
			}
    </script>
    </head>
    <body>
     <section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="default.list.label" args="[formName]" default="$formName List" /> &nbsp;(${domainInstanceTotal}) </h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6 clearfix">
            <div class="grid_6">
            	<div>
		        	Show <g:select name="max" from="[10,25,50,100]" value="${params.max}" onchange="goToPage(this.value)"/> entries
		        </div>
                <table class="datatable tablesort selectable full">
                    <thead>
                        <tr>
                        <th name="selectField">Select</th>
                        <g:set var="showFields" value="${false }" />
				         <sec:ifAnyGranted roles="${rtsaf }">
				         	<g:set var="showFields" value="${true }" />
				         </sec:ifAnyGranted>
                        <%
							def notKeyFigures = ['SubForm']
							notKeyFigures.addAll(grailsApplication.config.formAdmin.fields.notKeyFigures)
							def dateFormat = grailsApplication.config.format.date
							def sdf = new SimpleDateFormat(dateFormat?:"MM/dd/yyyy")
							def counter=0
							boolean isApproval = false
							def maxCounter = ((isApproval = ('Approval'.equalsIgnoreCase(formType) && !showFields))?6:7)
							excludedProps = ['version']
                            def fields = form.fieldsList
							props = [[name:'id',naturalName:'Id']]
							fields.each{field->
								if(!notKeyFigures.contains(field?.type)){
									def settings = JSON.parse(field.settings)
									props << [name:field.name,naturalName:(settings.en.label?:field.name),display:!settings.hideFromUser,sortable:true,fieldType:field.type]
								}
							}
							props << [name:'created_by_id',naturalName:'Created By']
							if(!props.find{it.fieldType=='Paypal'}){
								props << [name:'updated_by_id',naturalName:'Updated By']
							}
							props << [name:'date_created',naturalName:'Date Created',sortable:true]
							if(!props.find{it.fieldType=='Paypal'}){
								props << [name:'last_updated',naturalName:'Last Updated',sortable:true]
							}
							def display = true
                            props.eachWithIndex { p, i ->
								display = (p.display!=null?p.display:true)
                                if (counter < maxCounter && (display || showFields)) {
									counter++;
                                    if("id".equalsIgnoreCase(p.name)){%>
										<th>Edit</th>
									<%}else if(!p.sortable){
										%><th>${p.naturalName}</th><%
									}else if(p.fieldType == 'Paypal'){
										%><th>Amount</th><th>Status</th><%
									}else{%>
	                            		<g:sortableColumn property="${p.name}" title="${p.naturalName?.length()>10?(p.naturalName?.substring(0,10)+'...'):(p.naturalName)}" params="[formId:params.formId]" />
	                       			<%}
								}
							}
							if(isApproval){
								def p = props.find{it.name == formAdmin?.statusField?.name}
								%>
								<g:sortableColumn property="${p.name}" title="${p.naturalName}" params="[formId:params.formId]" />
							<%}%>
                       		<th><g:message code="${showFields?'form.shareCopy':'form.share'}" default="${showFields?'Share/Copy':'Share'}"/></th>
                        </tr>
                    </thead>
                    <tbody>
                    	
                    <g:each in="${domainInstanceList}" status="i" var="domainInstanceGroovyRowResult">
                    	<%domainInstance = [:]
						domainInstance.putAll(domainInstanceGroovyRowResult) %>
                    	<g:set var="attachments" value="${form.getDomainAttachments(domainInstance.id) }" />
                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
                        	<td name="selectField"><input type="checkbox" name="selectCheckId" id="selectCheck${i}" onclick="hideButtonControls()" value="${domainInstance.id}"/></td>
                        <%  
							counter=1
							props.eachWithIndex { p, j ->
								display = (p.display!=null?p.display:true)
								itsDatabaseValue = ((Map)domainInstance).getAt(p.name)
								if (j == 0) { %>
                            <td><g:link action="edit" id="${itsDatabaseValue}" params="[formId:params.formId]"><img src="${request.getContextPath()}/images/icons/page_white_edit.png" alt="Edit"></g:link></td>
                        <%      } else if (counter < maxCounter && (display || showFields) ) {
									counter++;
                                    if (itsDatabaseValue?.class?.name == 'java.sql.Timestamp' ) { 
										%><td>${sdf.format(itsDatabaseValue)}</td><%
									}else if(p.fieldType == 'FileUpload'){
										%><td>
											<g:set var="attachmentsThisField" value="${attachments?.findAll{it.inputName == p.name+'_file'}}" />
											${attachmentsThisField?.size()}</td><%
									}else if(p.fieldType == 'Paypal'){
										Payment payment = Payment.findByFormIdAndInstanceId("${form.id}","${domainInstance.id}")
										def amount = "0"
										def status = "N/A"
										if(payment){
											def totalPayment = 0
											def currentCurr = grailsApplication.config.formBuilder.currencies[payment.currency.toString()]
											payment?.paymentItems?.each{PaymentItem paymentItem->
												totalPayment+=paymentItem.amount*paymentItem.quantity
											}
											status = payment.status
											amount = currentCurr + totalPayment
										} 
										%><td>${amount}</td><td>${status}</td><%
									}else if(p.name == 'created_by_id' || p.name == 'updated_by_id'){
										%><td id="textField${j}" class="expandibleTds" onclick="expandCollapse(this);">${itsDatabaseValue?User.read(itsDatabaseValue)?.toString():' '}</td><%
									}else{
										%><td id="textField${j}" class="expandibleTds" onclick="expandCollapse(this);">${itsDatabaseValue}</td><%
									}%>
							
						
						 <%  }   } 
							if(isApproval){
								def p = props.find{it.name == formAdmin?.statusField?.name}
								%>
								<td >
									${domainInstance.getAt(p.name)}
								</td>
							<%}%>
							<td>
								<g:render template="/dashboard/sharepopup" model="['id':domainInstance.id,'shareType':formType,'className':form.domainClass.name,'formId':params.formId]"/>
								<g:if test="${showFields }">
                                   	<g:link action="copy" id="${domainInstance.id}" params="[formId:params.formId]" class="button1 small blue">
                                   		<font color="white">Copy</font>
                                   	</g:link>
                                </g:if>
							</td>
                        </tr>
                    </g:each>
                    </tbody>
                </table>
            </div>
            <div class="paginateButtons">
                <g:paginate total="${domainInstanceTotal}" params="[formId:params.formId]" />
            </div>
             	<div class="grid_6 buttonDiv" style="width:240px;margin-top:10px;">
             		<div class="PostShare" style="width:120px;">
							<ul style="width:120px;">
								<li ><a href="#" class="ShareMainBtn" style="font-weight: bold;">
									<span class="exportIcon"></span><g:message code="export.label" default="Export"/><span class="arrow-down"></span></a>
									<ul style="width:110px;">
										<li>
											<g:link controller="home" action="exportToGoogleDocs" params="[dc:form.domainClass.name,formId:form.id,format:'excel',extension:'xls',returnController:'ddc']" onclick="loadScreenBlock();" style="width:105px;padding:6px 3px;"><g:message code="default.button.exportGoogleDocs.label" default="Google Docs" /><span class="googleDoc" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></g:link>
										</li>
										<li>
											<g:link controller="formViewer" action="exportResponseList" params="[formId:form.id,format:'pdf',extension:'pdf']" style="width:105px;padding:6px 3px;"><g:message code="default.button.exportGoogleDocs.label" default="Pdf" /><span class="pdf" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></g:link>
										</li>
										<li class="Nobdr">
											<g:link controller="formViewer" action="exportResponseList" params="[formId:form.id,format:'excel',extension:'xls']" style="width:105px;padding:6px 3px;"><g:message code="default.button.exportGoogleDocs.label" default="Excel" /><span class="excel" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></g:link>
										</li>
									</ul>
								</li>
							</ul>
						</div>
	                    <div class="PostShare" style="width:120px;">
							<ul style="width:120px;">
								<li ><a href="#" class="ShareMainBtn" style="font-weight: bold;">
									<span  class="add"></span><g:message code="default.button.create.label" default="Create"  /><span class="arrow-down"></span></a>
									<ul style="width:110px;">
										<li>
											<g:link action="create" params="[formId:params.formId]" style="width:110px;padding:6px 3px;"><g:message code="default.button.create.label" default="Create"  /><span class="subForm" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></g:link>
										</li>
										<li class="Nobdr">
											<a href="javascript:;"  onclick="showUploadForm();" style="width:110px;padding:6px 3px;"><g:message code="formInstance.data.upload" default="Upload Data"/><span class="uploadForm" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></a>
										</li>
									</ul>
								</li>
							</ul>
						</div>
	             	  	
	                </div>
	                 <g:form method="post" >
		                <div class="grid_6 deleteButtonDiv" style="width:100px;display:none;margin-top:10px;">
		                  <g:hiddenField name="formId" value = "${params.formId}"/>
		                  <g:actionSubmit class="button button-red" action="multipleDelete" style="width: 120px" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
		                </div>
		                <div id=checkBoxIdDiv>&nbsp;</div>
                	</g:form>
			<div>&nbsp;</div>
            <div>&nbsp;</div>
            <div id="uploadFormDiv" style="display:none;border:1px solid #ddd;">
	                <g:form method="post" controller="ddc" action="excelUpload" enctype="multipart/form-data" style="margin:15px;">
	                	<g:hiddenField name="formId" value="${params.formId }"/>
	                	<div style="font-weight:bold;color:#666666;"> Upload Form Data</div><br/>
	                	<label for="uploadFormInstance" style="font-weight:bold;color:#666666;">Select File :</label>
	                	<input type="file" name="uploadFormInstance"/><br/>
	                	<input class="save button button-gray" type="submit" value="Upload" style="padding:0px 10px;"/>&nbsp;<input class="button button-red" type="button" onclick="hideUploadForm()" style="padding:0px 10px;" value="Cancel"/>
	                </g:form>
                </div>
            </div>
            
        </section>
        <script>
					function expandCollapse(ele){
						if($(ele).css('white-space')=='nowrap'){
							$(ele).css('white-space','normal')
							var resultingText = ""
							var text = $(ele).html();
							var counter = 0;
							for(var i=0;i<text.length;i++){
								if(text[i]!=' ' && text[i]!='-'){
									counter++;
								}else{
									counter = 0;
								}
								if(counter==12){
									resultingText += '<span class="word-wrap"> </span>';
									counter = 0;
								}
								resultingText += text[i];
							}
							$(ele).html(resultingText);
						}else{
							$('.word-wrap',$(ele)).remove();
							$(ele).css('white-space','nowrap')
						}
					}

					function showUploadForm(){
							$("#uploadFormDiv").show('slow')
						}

					function hideUploadForm(){
						$("#uploadFormDiv").hide('slow')
					}

					function hideButtonControls(){
							var selectedCheckBoxCount = $("input:checkbox[name=selectCheckId]:checked").length;
							var selectedCheckBox = $("input:checkbox[name=selectCheckId]:checked");
							var htmlEleConetent = ""
							for(var i=0; i<selectedCheckBoxCount; i++){
								htmlEleConetent += "<input type='hidden' name='selectCheck' value='"+selectedCheckBox[i].value+"'>"
							}
							$("#checkBoxIdDiv").html(htmlEleConetent)
							if(selectedCheckBoxCount > 0){
								$(".buttonDiv").hide();
								$(".deleteButtonDiv").show();
							}else{
								$(".buttonDiv").show();
								$(".deleteButtonDiv").hide();
							}
						}

					
					
                </script>
    </div>
</section>
    </body>
</html>