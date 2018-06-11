<%@ page import="grails.converters.JSON" %>
<%@ page import="org.grails.formbuilder.FormBuilderConstants" %>
<%@ page import="org.springframework.web.servlet.support.RequestContextUtils" %>
<g:set var="locale" value="${RequestContextUtils.getLocale(request)}" />
<% String language =  'en' %>

<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <g:set var="entityName" value="${message(code: 'form.label', default: 'Form')}" />
        <title><g:message code="default.list.label" args="[entityName]" /> (${formInstanceTotal})</title>
         <link rel="stylesheet" href="${resource(dir: 'css/streamlined', file: 'DasMain.css')}"/>
        <style type="text/css">
        .list {
          width: 740px; 
          margin-left: auto; 
          margin-right: auto;
        }  
        .col1 {
         
          font-size: 16px;
			    font-weight: bold;
			    line-height: 17px;
        }
        .col1 a {
          text-decoration: none;
          color: #666;
          padding: 0 3px 0 3px
        }
        </style>
        <script>
        function getDriectURL(url){
        	showPopup(url)
        }
        function getEmbededCode(formId){
            var serverURL = '${grailsApplication.config.grails.serverURL}';
            showPopup('<div id="oneappcloud'+formId+'"></div>\n<scr'+'ipt type="text/javascript" src="'+serverURL+'/embed/jsx/'+formId+'?height=380"></scr'+'ipt>');
        }
        function showPopup(str){
        	$(".popupMessage textarea").val(str);
        	$(".popupMessage").show("slow");
        }
        function confirmMessage(message){
            var confirmation = confirm(message)
            if(confirmation == true){
            	loadScreenBlock();
                return true;
            }else{
                return false;
            }
        }
        function showFormCreateDD(event){
            
        }
        $(document).ready(function(){
            <%
            def initialIndex = (formCat=='N'?0:1)
            %>
            $(".tabs").tabs(".panes section",{initialIndex:${initialIndex}})
        });
 function facebook(url, title, desc, img) {
window.open( "http://www.facebook.com/sharer.php?s=100&p[title]="+encodeURI(title)+"&p[summary]=" +desc+ "&p[url]="+encodeURI(url)+"&p[images][0]="+encodeURI(img), "facebook", "status=1, height=400, width=550, resizable=0, toolbar=0");
facebook.focus();
}

function linkedIn(url, title, desc, source) {
window.open( "http://www.linkedin.com/shareArticle?mini=true&url=" + encodeURI(url) +"&title=" + encodeURI(title) + "&summary=" + encodeURI(desc) + "&source=" + encodeURI(source), "linkedIn", "status=1, height=400, width=550, resizable=0, toolbar=0");
linkedIn.focus();
}

function tweet(url, text) {
window.open( "https://twitter.com/intent/tweet?text=" + encodeURI(text) + "&url=" + encodeURI(url), "tweet", "status=1, height=400, width=550, resizable=0, toolbar=0");
tweet.focus();
}

function plusone(url) {
window.open( "https://plusone.google.com/_/+1/confirm?hl=en&url=" + encodeURI(url), "plusone", "status=1, height=400, width=550, resizable=0, toolbar=0");
plusone.focus();
}
  
        </script>
    </head>
    <body>
	<section class="main-section grid_7">
    <div class="main-content">
    	<header>
            <h2>
            	<g:if test="${formCat=='N'}">
                	<g:message code="form.list" default="Form List"/>
                </g:if>
				<g:if test="${formCat=='M' }">
                	<g:message code="masterForm.list" default="Master Form List"/>
                </g:if>
                <g:if test="${formCat=='S' }">
                	<g:message code="subForm.list" default="Sub Form List"/>
                </g:if>
                &nbsp;(${formInstanceTotal})</h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
            
            <div class="clearfix" style="position:absolute;right:10px;top:20px;">
				<ul class="action-buttons clearfix fr">
	                <li>
                		<a href="${grailsApplication.config.grails.serverURL}/documentation/formbuilder_help.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a>	                
	                </li>
            	</ul>
            </div>
        </header>
        <section class="container_6 clearfix">
        	<div class="message success closeable popupMessage" style="padding: 5px 15px; position: absolute;   margin: auto; width: auto; top: 70px; font-weight: bold; z-index: 100001; display: none;">
				<span class="message-close" style="display:block;"></span>
		         	<p>
		         	Copy text:<br>
		           	<textarea style="width:739px;height:75px;"></textarea>
		         	</p>
			</div>
			<div style="margin: 0; width: auto;"  class="tabbed-pane">
            <ul style="padding: 0;" class="tabs">
                <li><a href="#" onclick="${(formCat!='N'?('window.location = \''+createLink(action:'list',params:[formCat:'N'])+'\';loadScreenBlock();'):'return false;')}">Forms </a></li>
                <li><a href="#" onclick="${(formCat!='S'?('window.location = \''+createLink(action:'list',params:[formCat:'S'])+'\';loadScreenBlock();'):'return false;')}">Sub-Forms </a></li>
            </ul>
            <div style="margin-bottom: 5px;" class="panes clearfix">
            <%
			for(int i=0;i<(formCat=='N'?0:1);i++){%>
				<section></section>
			<%}
			%>
            <section><table class="datatable tablesort selectable full">
	                    <thead>
	                        <tr>
	                        <g:if test="${formCat!='S' }">
                        		<th>Preview</th>
                        	</g:if>
                        	<g:sortableColumn property="settings" title="Form Name"/>
	                           <g:if test="${formCat!='S' }">
	                           <th>Settings</th>
	                           <th>Share</th>
	                            <th><nav class="nav-icon icon-charts" style="background-repeat: no-repeat;">&nbsp;&nbsp;Reports</nav></th>
	                            <th >Responses</th>
	                            <th>Most Recent Entry</th>
	                           </g:if>
	                        </tr>
	                    </thead>
	                    <tbody> 
	                    <g:each in="${formInstanceList}" status="i" var="formInstance">
	                    <g:set var="formAdmin" value="${formAdminList?.find{it.form.id == formInstance.id} }"/>
	                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
	                        	<g:if test="${formCat!='S' }">
		                        	<td>
		                        		<g:link style="text-decoration:underline;" class="LSC" controller="PF" action="preview" params="['formId':formInstance.id]"><img src="${request.getContextPath()}/images/icons/control_play.png" alt="Preview"></g:link>
		                        		<g:if test="${formIdsForClient?.contains(formInstance.id) && formIdsForTrial?.contains(formInstance.id)}">
			                            	<img src="${request.getContextPath()}/images/icons/lock.png" title="${message(code:'ruleSet.used.forClientAndTrial','default':'For newly created clients and trial users') }" />
		                                </g:if>
		                                <g:elseif test="${formIdsForClient?.contains(formInstance.id)}">
			                            	<img src="${request.getContextPath()}/images/icons/lock.png" title="${message(code:'ruleSet.used.forClient','default':'For newly created clients') }" />
		                                </g:elseif>
		                                <g:elseif test="${formIdsForTrial?.contains(formInstance.id)}">
			                            	<img src="${request.getContextPath()}/images/icons/lock.png" title="${message(code:'ruleSet.used.forTrial','default':'For newly created trial users') }" />
		                                </g:elseif>
		                        	</td>
	                        	</g:if>
	                           <td><g:link style="text-decoration:underline;" class="LSC" action="edit" id="${formInstance.id}">${formInstance}</g:link></td>
	                           <g:if test="${formCat!='S' }">
	                           <td align="center"> 
	                           			<span class="menuButton"><g:link style="text-decoration:underline;" class="LSC" controller="formAdmin" action="create" params="['formId':formInstance.id]" >
	                                       Settings</g:link></span>
	                           </td>
	                           <td>
	                           		<g:if test="${formAdmin}">
	                           			<%
										   def setting=JSON.parse(formInstance.settings)
										   
										  %>
	                           			<ul class="dropdown" style="position:absolute;font-size: 70%;">
											<li id="shareDropDownLi" >
												<a href='#'><img src="${request.getContextPath()+'/images/arrow-down.png'}" alt=''>Share</a>
												<ul class='sub_menu' style='padding-left:0px;width:80px;'>
													<li><a target="_blank" href="${request.getContextPath() }/PF/sendEmail?formId=${formInstance?.id}&includeForm=true" rel="#overlay" class="no-text help"><img src="${request.getContextPath()}/images/mail_16.png"/>&nbsp;<span style="vertical-align: super;">Email</span></a></li>
													<li><a href='javascript:;' onclick="getEmbededCode('${formInstance?.id}')"><img src="${request.getContextPath()}/images/emed_16.png"/>&nbsp;<span style="vertical-align: super;">Embed</span></a></li>
													<li><a href='javascript:;' onclick="getDriectURL('${formAdmin.shortURL}')"><img src="${request.getContextPath()}/images/icon_direct_link_16.png"/>&nbsp;<span style="vertical-align: super;">Direct URL</span></a></li>
													<li><a href="javascript:void(0)" onclick="linkedIn('${formAdmin.shortURL}', '${formInstance}', '${setting.en.description}', '${formInstance}');"><img src="${request.getContextPath()}/images/Linkdin_16.png"/>&nbsp;<span style="vertical-align: super;">LinkedIn</span></a></li>
													<li><a href="javascript:void(0)" onclick="facebook('${formAdmin.shortURL}', '${formInstance}', '${setting.en.description}', '${formInstance}');"><img src="${request.getContextPath()}/images/facbook_16.png"/>&nbsp;<span style="vertical-align: super;">Facebook</span></a></li>
													<li><a href="javascript:void(0)" onclick="plusone('${formAdmin.shortURL}'); return false;"><img src="${request.getContextPath()}/images/icon_google_plus.png"/>&nbsp;<span style="vertical-align: super;">Google+</span></a></li>
													<li><a href="javascript:void(0)" onclick="tweet('${formAdmin.shortURL}', '${formInstance}');"><img src="${request.getContextPath()}/images/Twitter_16.png"/>&nbsp;<span style="vertical-align: super;">Twitter</span></a></li>
												</ul>
											</li>
										</ul>
										<div style="width:53px;height:19px;"></div>
	<%--                           			<a target="_blank" href="mailto:?subject=[Form Builder Forms] - Please complete this form&body=Hello, %0A%0A Please complete the form provided at %0A%0A ${formAdmin.shortURL}%0A%0A${'Password'.equalsIgnoreCase(formAdmin.formLogin)?('Password to access this form is: '+formAdmin.formPassword+'%0A%0A'):''}Thank you,%0A${currentUser?.firstName+' '+currentUser?.lastName }">Email</a>--%>
	                            </g:if>
	                           </td>
	                      
	                    	 <td align="center"><g:link class="LSC" style="text-decoration:underline;" controller="report" action="formReport" params="[formId:formInstance.id]"> Reports</g:link></td>
	                    		<td>
	                    			<g:set var="responseInstance" value="${responseList?.find{it.id == formInstance.id}}"/>
	                    			<g:if test="${formAdmin && responseInstance}">
		                            	<span class="menuButton"><a class="list LSC" style="text-decoration:underline;" href="/form-builder/formViewer/list?formId=${responseInstance.id}">List (${responseInstance.count})</a></span>
	                                </g:if><g:else>
										<span class="menuButton">No Response</span>
									</g:else>
	                            </td>
	                            <td> 
									<g:if test="${responseInstance?.recentEntry}">
										<i><font color="#666"><prettytime:display date="${responseInstance?.recentEntry }"></prettytime:display></font></i>
									</g:if>
									<g:else>
										<i><font color="#666">No Response</font></i>
									</g:else>
	                           </td>
	                            </g:if>
	                        </tr>
	                    </g:each>
	                    </tbody>
	                </table>
	            <div class="paginateButtons">
                  <g:paginate total="${formInstanceTotal}" params="[formCat:formCat]"/>
                </div>
	        </section>
	        <%
			for(int i=0;i<(formCat=='N'?0:1);i++){%>
				<section></section>
			<%}
			%>
	        </div>
	        </div>
                <div class="grid_6 buttonDiv" style="width:240px;">
                	<div class="PostShare" style="width:120px;">
						<g:link class="button button-gray LSC" controller="formTemplate"><span 
                            class="add"></span>${message(code: 'template', 'default': 'Templates')}</g:link>
                     </div>
                    <div class="PostShare" style="width:120px;">
						<ul style="width:115px;">
							<li ><a href="javascript:;" class="ShareMainBtn" style="font-weight: bold;">
								<span  class="add"></span><g:message code="default.button.create.label" default="Create"  /><span class="arrow-down"></span></a>
								<ul style="width:115px;">
									<li>
										<g:link class="LSC" action="create" style="width:110px;"><g:message code="default.button.label" default="Form"  /><span class="mainForm" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></g:link>
									</li>
									<li>
										<a class="LSC" href="javascript:;" onclick="document.formCatForm.formCat.value='S';document.formCatForm.submit();" style="width:110px;"><g:message code="form.create.masterForm" default="Sub Form"/><span class="subForm" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></a>
									</li>
									<li class="Nobdr">
										<a href="javascript:;" onclick="showUploadForm()" style="width:110px;"><g:message code="form.upload" default="Upload Form"/><span class="uploadForm" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></a>
									</li>
								</ul>
							</li>
						</ul>
					</div>
                    <g:form name="formCatForm" action="create">
                    	<g:hiddenField name="formCat" value="M"/>
                    </g:form>
                </div>
                <div>&nbsp;</div>
                <div>&nbsp;</div>
                <div>&nbsp;</div>
               <div id="uploadFormDiv" style="display:none;border:1px solid #ddd;">
	                <g:form method="post" controller="ddc" action="excelFormCreate" enctype="multipart/form-data" style="margin:15px;">
	                <div class="preview-pane grid_3 omega" style="float:right;">
       						 <div class="content" style="padding:0;">
			                	<div class="message info">
					                <h3><g:message code="help.main.helpful.tips"/></h3>
					                <img src="${request.getContextPath()}/images/lightbulb_32.png" class="fl"/>
					                <p>Upload the excel with version less than 2007(.xls extension)</p>
					            </div>
					           </div>
					         </div>
	                	<div style="font-weight:bold;color:#666666;"> Upload File to create Form</div><br/>
	                	<label for="uploadFormInstance" style="font-weight:bold;color:#666666;">Select File :</label>
	                	<input type="file" name="uploadFormInstance" id="uploadFormInstance"/><br/>
	                	<input class="save button button-gray" type="submit" value="Upload" style="padding:0px 10px;" onclick="if($('#uploadFormInstance').val()=='')return false;"/>&nbsp;<input class="button button-red" type="button" onclick="hideUploadForm()" style="padding:0px 10px;" value="Cancel"/>
	                </g:form>
                </div>
                
                 <script type="text/javascript">
	                function showUploadForm(){
						$("#uploadFormDiv").show('slow')
					}

					function hideUploadForm(){
						$("#uploadFormDiv").hide('slow')
					}
                </script>
        </section>
    </div>
    
</section>
    </body>
</html>
