<%@ page import="org.codehaus.groovy.grails.orm.hibernate.support.ClosureEventTriggeringInterceptor as Events" %>
<%@ page import="org.codehaus.groovy.grails.scaffolding.DomainClassPropertyComparator" %>
<%@ page import="org.codehaus.groovy.grails.plugins.PluginManagerHolder" %>
<%@ page import="org.grails.formbuilder.*" %>
<%@ page import="grails.converters.JSON" %> 
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
		if ($('#userXAxis').val()==null||$('#userXAxis').val()==""){
			alert("Please select value For X-Axis ..!");
			return false;
		}if ($('#userYAxis').val()==null){
			alert("Please select value For Y-Axis..!");
			return false;
		}
		var dd1 = date1.substring(3, 5);
		var mm1 = date1.substring(0, 2);
		var yy1 = date1.substring(6, date1.length);
		tmpdate1 = new Date(yy1, mm1 - 1, dd1);

		var dd2 = date2.substring(3, 5);
		var mm2 = date2.substring(0, 2);
		var yy2 = date2.substring(6, date2.length);
		tmpdate2 = new Date(yy2, mm2 - 1, dd2);

		if (date2 != '' & date1 != '')
			if (tmpdate1 > tmpdate2) {
				alert("From Date must be less then To Date(" + mm2 + "/" + dd2
						+ "/" + yy2 + ")");
				$('#' + cdate1).select();
				return false;
			} else if (tmpdate1 == tmpdate2) {
				//alert("ok");
				showSpinner();
				return true;
			} else {
				//alert("ok");
				showSpinner();
				return true;
			}
	}
	$(document).ready(function(){
		$("#fromDate").val("${fromDate}")
		$("#toDate").val("${toDate}")
	});
</script>
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
			chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container',
					defaultSeriesType: chartType
				},
				title: {
					text: '${title}'
				},
				subtitle: {
					text: ''
				},
				xAxis: {"categories":${dataLabel}
				
				},
				yAxis: ${yAxisLabel},
				legend: {
					align: 'right',
					x: -40,
					verticalAlign: 'bottom',
					y: 120,
					floating: true,
					backgroundColor: '#FFFFFF',
					borderColor: '#CCC',
					borderWidth: 1,
					shadow: false
				},
				tooltip: {
					formatter: function() {
						return '' +
								this.x +' '+this.series.name+ ': ' + this.y + ' ';
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
                <h2>Report on User Action <a href="#">&darr;</a></h2>
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
	
	<form method="post" >
	<table class="datatable full">
                        <tbody>
                        <ul class="action-buttons clearfix fr">
                           <tr class="prop">
                                <td style="vertical-align:top;">
                                  From Date  <font color="red">*</font>
                                </td>
                                <td style="vertical-align:top;">
                                	<div><input type="date" name="fromDate" id="fromDate" value="${fromDate}"/></div>
                                </td>
                               <td>
                                  To Date <font color="red">*</font>
                                </td>
                                <td>
                                    <div><input type="date" id="toDate" name="toDate" value="${toDate}" /></div>
                                </td>
                            </tr><tr>
                               
                                <td>X-Axis: <font color="red">*</font> </td>
                                <td>
                               			<g:select style="width:200px" from="${formList }" name="userXAxis" optionKey="id" noSelection="['':'Please Select']"  value="${params.userXAxis }" />
                                </td>
                                <td >Y-Axis:  <font color="red">*</font></td>
                                <td>
                                	<g:select style="width:200px" from="${xAndYAxis}" name="userYAxis" optionKey="name" optionValue="label" value="${params.userYAxis}"/>
                                </td>
                            </tr>
                            
                        	<tr class="prop">
                                <td valign="top" class="name">Run Again </td>

                            	<td><g:actionSubmit class="button button-gray" action="userActionReport" controller="report" onclick="return twodateComp('fromDate','toDate');" name="Run Report" value="Run Report"/>
                            		<input type="button" class="button button-gray" style="width: 100px;"  value="Back" onclick="javascript:history.go(-1)" /> 
                            	 </td>
                            </tr>
                        </ul>
                  </tbody>
</table>
</form>
</div>

	<%
		def summaryTotal=new ArrayList()
		def totalEntries = 0
		def totalAttachmentSize=0
	
	%>
	<br/><br/><br/>
	<div id="container" class="grid_6"></div>

	<br/><br/><br/>
	<div id="grid" class="grid_6">
	<% if ( fullData != null ) { %>
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
