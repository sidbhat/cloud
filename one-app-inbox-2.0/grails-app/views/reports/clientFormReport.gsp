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
 
<g:set var="entityName" value="${message(code: 'reports.label', default: 'Report')}"/>
    <title><g:message code="default.report.label" args="[entityName]"/></title>
    <% if ( fullData != null ) { %>
   <script type="text/javascript">

        var chart;
		var seriesData = ${data};
		var totalForms = seriesData[0].data[0].y * 1;
        $(document).ready(function(){
			if(totalForms>20){
				$("#container").css("height",totalForms*15+'px');
			}
			chart = new Highcharts.Chart({
				chart: {
					renderTo: 'container',
					defaultSeriesType: 'bar'
				},
				title: {
					text: '${title}'
				},
				subtitle: {
					text: ''
				},
				xAxis: {"categories":${dataLabel}
				
				},
				yAxis: {
					title: {
						text: 'Count'
					}
				},
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
						var point = this.point,
							s = '';
						if (point.drilldown) {
							s += this.x +' has '+ this.y +'  forms<br/>'
							s += 'Click to view '+ point.category +' form';
						} else {
							s += this.x +' has '+ this.y +'  entries <br/>'
							s += 'Click to return to client view';
						}
						return s;
					}
				},
				plotOptions: {
					bar: {
						pointPadding: 0.2,
						borderWidth: 0,
						cursor: 'pointer',
						point: {
							events: {
								click: function() {
									var drilldown = this.drilldown;
									if (drilldown) { // drill down
										setChart(drilldown.name, drilldown.categories, drilldown.data, drilldown.color);
									} else { // restore
										setChart('Form Count', ${dataLabel}, seriesData[0].data);
									}
								}
							}
						},
						dataLabels: {
							enabled: true,
							color: this.color,
							style: {
								fontWeight: 'bold'
							},
							formatter: function() {
								return this.y ;
							}
						}
					}
				},
				series: seriesData
			});
			
			function setChart(name, categories, data, color) {
				chart.xAxis[0].setCategories(categories);
				chart.series[0].remove();
				chart.addSeries({
					name: name,
					data: data,
					color: color || 'white'
				});
			}
		});
	
		
		
		
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
                <h2>Report on forms of Client <a href="#">&darr;</a></h2>
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
                           <tr>
                               
                                <td>Client: <font color="red">*</font> </td>
                                <td>
                               			<g:select style="width:200px" from="${clientList }" name="client" optionKey="id" optionValue="name" value="${selectedClientList}" multiple="true" />
                               			<script>
	                                	$('#client').val(${selectedClientList as grails.converters.JSON})
	                                	</script>
                                </td>
                                
                            </tr>
                            
                        	<tr class="prop">
                                <td valign="top" class="name">Run Again </td>

                            	<td><g:actionSubmit class="button button-gray" action="clientFormReport" controller="report"  name="Run Report" value="Run Report"/>
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
	<div id="container" class="grid_6" style="height:300px;"></div>

	<br/><br/><br/>
	

            
            
        </section>
    </div>

</section>

<!-- Main Section End -->

</div>
</section>
	 
	
	
</body>
</html>
