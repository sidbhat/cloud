<%@ page import="grails.converters.JSON" %>
<%@ page import="java.text.SimpleDateFormat" %>
<%@ page import="com.oneapp.cloud.core.*" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="mobile" />
        <title><g:message code="default.list.label" args="[formName]" /></title>
    </head>
    <body>
   		<div data-role="header" data-position="fixed">
			<h1><g:message code="default.list.label" args="[formName]" default="$formName List" /></h1>
			<a data-icon="home" class="ui-btn-left" href="${createLink(uri: '/home/index')}"><g:message code="default.home.label"/></a></li>
			<g:link data-icon="plus" class="ui-btn-right" action="create" params="[formId:params.formId]"><g:message code="default.new.label" args="['']" default="New" /></g:link>
		</div>
		<div data-role="content">
			<ul data-role="listview" data-split-icon="gear" data-filter="true" data-inset="true">
				<g:each in="${domainInstanceList}" status="i" var="domainInstance">
					<li>
						<table width="100%">
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
											props << [name:field.name,naturalName:settings.en.label,display:!settings.hideFromUser,sortable:true,fieldType:field.type]
										}
									}
									props << [name:'created_by_id',naturalName:'Created By']
									props << [name:'updated_by_id',naturalName:'Updated By']
									props << [name:'date_created',naturalName:'Date Created']
									props << [name:'last_updated',naturalName:'Last Updated']
									def total = 0
		                            props.eachWithIndex { p, j ->
										itsDatabaseValue = domainInstance.getAt(p.name)
		                                	if (j < 9) {%>
												<tr <% if(j>3){%>style="display:none" name="hiddenListTr${i}" <%}%>>
													<td width="40%" style="vertical-align:top;">
														<div>
														
															${p.naturalName}
														
													</div>
												 </td>
												 <td>&nbsp;</td>
									   		 <%}%>
											<td width="50%">
												<div style="font-weight:normal;">
											 		<%
				                               		 if (j == 0) { %>
														<g:link action="edit"  id="${itsDatabaseValue}" params="[formId:params.formId]">${itsDatabaseValue}</g:link>
				                       				 <% } else if (j < 9) {
				                                   			  if (itsDatabaseValue?.class?.name == 'java.sql.Timestamp' ) { %>
				                            					${sdf.format(itsDatabaseValue)}
				                       				 <%    }else if(p.name == 'created_by_id' || p.name == 'updated_by_id'){%>
				                            					${itsDatabaseValue?User.read(itsDatabaseValue)?.toString():' '}
				                        			<%     } else { %>
				                           						${itsDatabaseValue}
				                        			<%      } } %>
												</div>
											</td>
										</tr> 
										<%  total++;}  %>
								<%if(total > 3){ %>
									<tr>
										<td colspan="2"><a href="javascript:;" id="showMoreTableData${i}" style="font-size:normal;color:#3B5998;text-decoration:none;">Show More..</a>
									</tr>
								<%}%>
								<script>
									$("#showMoreTableData${i}").click(function(){
										if ($('[name="hiddenListTr${i}"]').is(':hidden')){
											$('[name="hiddenListTr${i}"]').show();
											$("#showMoreTableData${i}").html('Show Less..')
										}else{
											$('[name="hiddenListTr${i}"]').hide();
											$("#showMoreTableData${i}").html('Show More..')
										}
									});
								</script>
                       		</table>
                       </li>
                  </g:each>
             </ul>    
            <fieldset class="paginateButtons">
                <g:paginate total="${domainInstanceTotal}" params="[formId:params.formId]" />
            </fieldset>
         </div>
    </body>
</html>