<%@ page import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events" %>
<%@ page import="org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator" %>
<%@ page import="org.codehaus.groovy.grails.plugins.PluginManagerHolder" %>
<%@ page import="org.grails.formbuilder.*" %>
<%@ page import="grails.converters.JSON" %>
<%@ page import="java.text.DecimalFormat" %> 
<html>
<head>

    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    %{----------------------------------------------------------------------------
  - [ NIKKISHI CONFIDENTIAL ]                                                -
  -                                                                          -
  -    Copyright (c) 2011.  Nikkishi LLC                                     -
  -    All Rights Reserved.                                                  -
  -                                                                          -
  -   NOTICE:  All information contained herein is, and remains              -
  -   the property of Nikkishi LLC and its suppliers,                        -
  -   if any.  The intellectual and technical concepts contained             -
  -   herein are proprietary to Nikkishi LLC and its                         -
  -   suppliers and may be covered by U.S. and Foreign Patents,              -
  -   patents in process, and are protected by trade secret or copyright law.
  -   Dissemination of this information or reproduction of this material     -
  -   is strictly forbidden unless prior written permission is obtained      -
  -   from Nikkishi LLC.                                                     -
  ----------------------------------------------------------------------------}%
 <script type="text/javascript">
	function twodateComp(cdate1, cdate2) {
		//$('#toDate').val()
		//alert($('#tenantId').val());
		var date1 = $('#' + cdate1).val();
		var date2 = $('#' + cdate2).val();

		//alert(date1 + "----" + date2)
		if (date1 == '' || date2 == '') {
			if (date1 == '') {
				alert("Please Enter From Date..!");
				$('#' + cdate1).select();
			} else {
				alert("Please Enter To Date..!");
				$('#' + cdate2).select();
			}
			return false;
		}
<%--		if ($('#tenantId').val()==null||$('#tenantId').val()==""){--%>
<%--			alert("Please select a Cleint from Cleint List ..!");--%>
<%--			return false;--%>
<%--		}--%>
		if ($('#formId').val()==null||$('#formId').val()==""){
			alert("Please select a form ..!");
			return false;
		}
		//if ($('#userYAxis').val()==null){
		//	alert("Please select value For Y-Axis..!");
		//	return false;
		//}
		/*var dd1 = date1.substring(3, 5);
		var mm1 = date1.substring(0, 2);
		var yy1 = date1.substring(6, date1.length);
		tmpdate1 = new Date(yy1, mm1 - 1, dd1);

		var dd2 = date2.substring(3, 5);
		var mm2 = date2.substring(0, 2);
		var yy2 = date2.substring(6, date2.length);
		tmpdate2 = new Date(yy2, mm2 - 1, dd2);*/

		if (date2 != '' & date1 != '')
			/*if (tmpdate1 > tmpdate2) {
				alert("From Date must be less then To Date(" + mm2 + "/" + dd2
						+ "/" + yy2 + ")");
				$('#' + cdate1).select();
				return false;
			} else if (tmpdate1 == tmpdate2) {
				//alert("ok");
				showSpinner();
				return true;
			} else {*/
				//alert("ok");
				showSpinner();
				return true;
			/*}*/
	}
</script>
<style>
button-orange:active{
	width:auto;
}
</style>
<g:set var="entityName" value="${message(code: 'reports.label', default: 'Report')}"/>
    <title><g:message code="default.report.label" args="[entityName]"/></title>
    <% if ( fullData != null ) { %>
   <script type="text/javascript">

        var chart;
        $(document).ready(function(){
			drawChart('bar');
		});
	
		function drawChart(chartType) {
			if(chartType=='pie'){
				drawPieChart();
				return;
			}
			var rotationValue = 0;
			if(chartType == 'line')
				rotationValue = -45
			chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container',
					type: 'column'
				},
				title: {
					text: '${title}'
				},
				subtitle: {
					text: ''
				},
				xAxis: {
					categories:${dataLabel}
				},
				yAxis: {
					min: 0,
					title: {
						text: 'Counts'
					}
				},
				legend: {
					layout: 'vertical',
					backgroundColor: '#FFFFFF',
					align: 'left',
					verticalAlign: 'top',
					x: 100,
					y: 70,
					floating: true,
					shadow: true
				},
				tooltip: {
					formatter: function() {
						return '' +
								this.x +' '+currentMonthYear+'-'+this.series.name+ ': ' + this.y + ' ';
					}
				},
				plotOptions: {
					column: {
						pointPadding: 0.2,
						borderWidth: 0
					}
				},
				series: ${data}
			});
	
	
		}
		
		var currentMonthYear = "${g.formatDate(date:currentMonthDate,format:'MMMMM, yyyy')}"
    </script>
	<%}%>
	
</head>

<body>
	
	
	<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <g:if test="${flash.message}">

			<div class="message">${flash.message}</div>
            </g:if>
  		
  			<div class="view-switcher">
                <h2>Form Analytics <a href="#">&darr;</a></h2>
                <ul>
        		  <sec:ifAnyGranted roles="ROLE_HR_MANAGER">
        		  	<li><g:link controller="report" action="formReport"><g:message
                            code="report.forms" default= 'Report on forms'/></g:link></li>
        		     <li><g:link controller="report" action="sender"><g:message
                            code="report.sender" default= 'Feeds by Sender'/></g:link></li>
                   	 <li><g:link controller="report" action="logReport"><g:message
                           code="report.config" default= 'Report on logs'/></g:link></li>
                  </sec:ifAnyGranted>
                   <sec:ifAnyGranted roles="ROLE_ADMIN">
        		     <li><g:link controller="report" action="clientFormReport"><g:message
                           code="report.config" default= 'Reports on forms by client '/></g:link></li>
                  </sec:ifAnyGranted>
                  <li><g:link controller="report" action="emailReport"><g:message
                            code="report.forms" default= 'Email Analyzer'/></g:link></li>
                </ul>
            </div>
        </header>
        <!-- Main Section -->

        <section class="container_6 clearfix">
            
            
            
	<div class="grid_6">
	
	<g:form controller="report">
	<table class="datatable full">
                        <tbody>
                            <tr>
                                <td style="width:10%">Form: <font color="red">*</font> </td>
                                <td>
                               		<g:select style="width:200px" from="${formList }" name="formId" optionKey="id" noSelection="['':'Please Select']"  value="${params.formId }" />
                                </td>
                                <td style="width:15%;text-align:center;font-weight:bold"> Month </td>
                                <td style="width:15%;text-align:center;font-weight:bold"> Previews </td>
                                <td style="width:15%;text-align:center;font-weight:bold"> Creates </td>
                                <td style="width:15%;text-align:center;font-weight:bold"> Conversion Rate </td>
                            </tr>
                        	<tr class="prop">
                                <td valign="top" class="name">Run Again </td>
                            	<td><g:actionSubmit class="button button-gray" action="formDefaultReport" onclick="return twodateComp('fromDate','toDate');" name="Run Report" value="Run Report"/>
                            		<input type="button" class="button button-gray" style="width: 100px;"  value="Back" onclick="javascript:history.go(-1)" /> 
                            	 </td>
                            	 <td style="text-align:center;font-weight:bold;padding:0;max-width: initial;">
                            	 	<table width="100%">
                            	 		<tr>
                            	 			<td style="background:none"><input type="button" value="&lt;" class="button button-orange" style="padding-left:2px;padding-right:2px;" onclick="changeMonth(this);"></td>
                            	 			<td style="background:none"><g:formatDate date="${currentMonthDate }" format="MMMMM"/><br><g:formatDate date="${currentMonthDate }" format="yyyy"/></td>
                            	 			<td style="background:none">
                            	 				<input type="button" value="&gt;" class="button button-${currentMonthDate.month != (new Date().month)?'orange':'gray' }" style="padding-left:2px;padding-right:2px;" onclick="${currentMonthDate.month != (new Date().month)?'changeMonth(this);':'' }">
                            	 			</td>
                            	 		</tr>
                            	 	</table>
                            	 </td>
                            	 <td style="text-align:center;font-weight:bold"> ${totalViews } </td>
                                <td style="text-align:center;font-weight:bold"> ${totalCreates } </td>
                                <td style="text-align:center;font-weight:bold"> <%
										try{
											DecimalFormat df = new DecimalFormat("#.#");
											println(df.format(100/(totalViews/totalCreates)))
										}catch(Exception e){println("0")}
									%>% </td>
                            </tr>
                  </tbody>
</table>
</g:form>
<g:form name="changeMonthForm" action="formDefaultReport">
<g:hiddenField name="formId" value="${params.formId }"/>
<g:hiddenField name="toDate" value="${toDate }"/>
<g:hiddenField name="fromDate" value="${fromDate }"/>
<g:hiddenField name="nextPrev" value=""/>
</g:form>
</div>

	<%
		def summaryTotal=new ArrayList()
		def totalEntries = 0
		def totalAttachmentSize=0
	
	%>
	<br/><br/><br/>
	<div id="container" class="grid_6" ></div>

	<br/><br/><br/>
	<div id="grid" class="grid_6">
	<script>
		function changeMonth(button){
			$("#nextPrev").val(button.value);
			$("#changeMonthForm").submit();
			return false;
		}
	</script>
	<% if ( false ) { %>
		<table class="datatable selectable full">
				<thead>
				<tr>
					<th>IP Address</th>
					<th>Access Mode</th>
					<th>Accessed Form</th>
					<th>Action</th>
					<th>Access Location</th>
					<th>Access Time</th>
				</tr>
				</thead>
	                    <tbody>
		                    <g:each in="${fullData }" var="d" status="i">
		                    		<g:set var="counter" value="${0 }"/>
			                        <tr class="${(counter++ % 2) == 0 ? 'odd' : 'even'}">
			                            <td>${fieldValue(bean:d,field:'ipAddress')}</td>
			                            <td>${fieldValue(bean:d,field:'accessType')}</td>
			                            <td>
											<%
												def fieldVal = fieldValue(bean:d,field:'accessedClass')
												DomainClass dc= DomainClass.findByName(fieldVal)
												Form form
												def formName
												if(dc){
													form  = Form.findByDomainClass(dc)
												}
												if(form){
													formName = JSON.parse(form.settings)."en".name
												}else{
													formName = fieldVal
												}
											%>
											${formName}
										</td>
			                            <td>${fieldValue(bean:d,field:'action')}</td>
			                            <td>${fieldValue(bean:d,field:'location')}</td>
			                            <td>${d.accessTime}</td>
			                        </tr>
			                        <%totalEntries++ %>
		                    </g:each>
                    </tbody>
                </table>
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
								if(counter==23){
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
	
				
   				<%}%>
   			
   	 </div>

            
            
        </section>
    </div>

</section>

<!-- Main Section End -->

</div>
</section>
	 
	
	
</body>
</html>
