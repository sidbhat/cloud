
<%@ page import="com.oneapp.cloud.core.log.Tracker" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main"/>
        <title><g:message code="tracker.list" default="Tracker List" /></title>
    </head>
    <body>
   	<section class="main-section grid_7">
        <div class="main-content">
        	 <header>
	            <h2><g:message code="tracker.list" default="Tracker List" /></h2>
	            <g:if test="${flash.message}">
		            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
		         </g:if>
	        </header>
	        <section class="container_6 clearfix">
	            <div class="grid_6">
	       			<g:form>
						<table class="datatable full">
                        	<tbody>
                       			 <ul class="action-buttons clearfix fr">
                         			  <tr class="prop">
			                                <td style="vertical-align:top;">
			                                    <sec:ifAnyGranted roles="${com.oneapp.cloud.core.Role.ROLE_SUPER_ADMIN }">
			                                    	Client <font color="red" >*</font>
			                                    </sec:ifAnyGranted>
			                                    &nbsp;
			                                </td>
				                            <td>
				                            	<sec:ifAnyGranted roles="${com.oneapp.cloud.core.Role.ROLE_SUPER_ADMIN }">
				                                	<g:select style="width:200px" from="${com.oneapp.cloud.core.Client.list() }" name="tenantId" optionKey="id" optionValue="name"/>
				                                	<script>
				                                	$('#tenantId').val(${tenantId})
				                                	</script>
				                                </sec:ifAnyGranted>
				                                &nbsp;
			                                </td>
                            			</tr>
			                        	<tr class="prop">
			                                <td valign="top"  class="name">Run Again </td>
			                            	<td><g:actionSubmit class="button button-gray" action="list" controller="tracker"  name="Run Report" value="Run Report"/> </td>
			                            </tr>
                        			</ul>
                  				</tbody>
							</table>
						</g:form>
		               <table class="datatable tablesort selectable full">
		                    <thead>
		                        <tr>
		                        
		                        <g:sortableColumn property="clientName" title="Client" titleKey="tracker.clientName" />
		                      	<g:sortableColumn property="controller" title="Controller" titleKey="tracker.controller"/>
		                        <g:sortableColumn property="action" title="Action" titleKey="tracker.action" />
		                   	    <g:sortableColumn property="user" title="User" titleKey="tracker.user" />
		                   	    <g:sortableColumn property="params" title="Params" titleKey="tracker.params"/>
		                        <g:sortableColumn property="dateCreated" title="Date Created" titleKey="tracker.dateCreated" />
		                        
		                   	   
		                        </tr>
		                    </thead>
		                    <tbody>
		                    <g:each in="${trackerInstanceList}" status="i" var="trackerInstance">
		                        <tr class="${(i % 2) == 0 ? 'odd' : 'even'}">
		                        
		                        	<td>${fieldValue(bean: trackerInstance, field: "clientName")}</td>
		                      		<td>${fieldValue(bean: trackerInstance, field: "controller")}</td>
		                            <td>${fieldValue(bean: trackerInstance, field: "action")}</td>
		                            <td onclick="expandCollapse(this);">${fieldValue(bean: trackerInstance, field: "user")}</td>
		                          	<td onclick="expandCollapse(this);">${fieldValue(bean: trackerInstance, field: "params")}</td>
		                            <td onclick="expandCollapse(this);">${trackerInstance.dateCreated}</td>
		                        
		                        
		                        </tr>
		                    </g:each>
		                    </tbody>
		                </table>
	                 <div class="paginateButtons">
		                <g:paginate total="${trackerInstanceTotal}" params="['tenantId':tenantId]"/>
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
            </script>
           
        </div>
      </section> 
    </body>
</html>
