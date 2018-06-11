<%@page import="org.grails.formbuilder.UniqueFormEntry"%>
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
			<g:link data-icon="plus" class="ui-btn-right" rel="external" action="create" params="[formId:params.formId]"><g:message code="default.new.label" args="['']" default="New" /></g:link>
		</div>
		<div data-role="content">
			<ul data-role="listview" data-split-icon="gear" data-filter="true" data-inset="true">
				<g:each in="${domainInstanceList}" status="i" var="domainInstance">
				<g:set var="uniqueId" value="" />
					<%try{
						uniqueId = UniqueFormEntry.findByFormIdAndInstanceId(form?.id,domainInstance?.id)
						valueOfField = uniqueId?.uniqueId
					}catch(Exception e){
					} %>
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
											props << [name:field.name,naturalName:settings.en.label,display:!settings.hideFromUser,sortable:true,fieldType:field.type,settings:settings]
										}
									}
									props << [name:'created_by_id',naturalName:'Created By']
									props << [name:'updated_by_id',naturalName:'Updated By']
									props << [name:'date_created',naturalName:'Date Created']
									props << [name:'last_updated',naturalName:'Last Updated']
									def total = 0
									%>
									 <tr>
										 <td width="40%" style="vertical-align:top;"><div> Unique Id</div></td>
										 <td>&nbsp;</td>
										 <td width="50%"><div style="font-weight:normal;">${valueOfField}</div></td>
									 </tr> 
									<%
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
														<g:link action="edit"  id="${itsDatabaseValue}" params="[formId:params.formId]"  data-ajax="false">${itsDatabaseValue}</g:link>
				                       				 <% } else if (j < 9) {
				                                   			  if (itsDatabaseValue?.class?.name == 'java.sql.Timestamp' ) { %>
				                            					${sdf.format(itsDatabaseValue)}
				                       				 <%    
																if(p.settings?.timeFormat){
																	def timeFormatted = new SimpleDateFormat(p.settings.timeFormat).format(itsDatabaseValue)
																	println(timeFormatted)
																}
														   }else if(p.name == 'created_by_id' || p.name == 'updated_by_id'){%>
				                            					${itsDatabaseValue?User.read(itsDatabaseValue)?.toString():' '}
				                        			<%     } else if(p.fieldType == 'AddressField' ){
													if(itsDatabaseValue){
														def mapValue = grails.converters.JSON.parse(itsDatabaseValue)
														itsDatabaseValue=mapValue."line1"?mapValue."line1"+"; ":""
														itsDatabaseValue+=mapValue."line2"?mapValue."line2"+"; ":""
														itsDatabaseValue+=mapValue."city"?mapValue."city"+"; ":""
														itsDatabaseValue+=mapValue."state"?mapValue."state"+"; ":""
														itsDatabaseValue+=mapValue."zip"?mapValue."zip"+"":""
														itsDatabaseValue+=mapValue."country"?mapValue."country"+"; ":""
													}
													
													%>
													${itsDatabaseValue}
													<% }else if(p.fieldType == 'NameTypeField'){
														def mapValue
														if(itsDatabaseValue){
															mapValue = grails.converters.JSON.parse(itsDatabaseValue)
															itsDatabaseValue=""
													    boolean	pre=(p.settings?.showPrefix)
														boolean mid=(p.settings?.showMiddleName)
														if(pre)
															itsDatabaseValue+= mapValue?."pre"?mapValue."pre"+" ":""
														itsDatabaseValue+=  mapValue?."fn"?mapValue."fn"+" ":""
														if(mid)
															itsDatabaseValue+= mapValue?."mn"?mapValue."mn"+" ":""
														itsDatabaseValue+= mapValue?."ln"?mapValue."ln":""
														}else{
														itsDatabaseValue=""
														}
														%>
														${itsDatabaseValue}${pre}${mid}
													<% } else if(p.fieldType == 'SingleLineNumber' ){
														def field
														fields.each{f->
															if(f.name==p.name){
																field=f
																}
															}
														def settings = JSON.parse(field.settings)
														int decimalPlaces=2
														try{
															 decimalPlaces=(settings?.decimalPlaces && settings?.decimalPlaces?.toInteger()!=2)?settings?.decimalPlaces?.toInteger():2
														}catch (Exception e) {}
														def value=(itsDatabaseValue)?(itsDatabaseValue.encodeAsHTML()):""
														if(decimalPlaces<2)
														try{
															def val=(itsDatabaseValue)?new BigDecimal(itsDatabaseValue):""
															  value =(new BigDecimal(val, java.math.MathContext.DECIMAL64).setScale(decimalPlaces, BigDecimal.ROUND_HALF_UP))
														}catch(Exception e){}
														boolean currencyType=(settings?.currencyType && settings?.currencyType!='')?true:false
														String currency=currencyType?grailsApplication.config?.formBuilder.currencies[settings.currencyType]:''
														if(currencyType && value){
														itsDatabaseValue = "${currency} "+value
													}else{
													itsDatabaseValue = value
													}
													%>
													${itsDatabaseValue}
													<%
													}else { %>
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