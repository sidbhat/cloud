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
        $(document).ready(function() {
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
                    min: 0,
                    title: {
                        text: 'Company Feed By Type'
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
                        return '' +
                                this.x + ': ' + this.y + ' ';
                    }
                },
                plotOptions: {
                    column: {
                        pointPadding: 0.2,
                        borderWidth: 0
                    }
                },
                series: [{
                	name:'Total Feeds',
                	data:${data}
                	}]
            });


        });

    </script>
	<%}%>
	
</head>

<body>
	
	
	<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
 		<div class="view-switcher">
                <h2>Feeds by Type <a href="#">&darr;</a></h2>
                <ul>
        		     <li><g:link controller="report" action="sender"><g:message
                            code="report.sender" default: 'Feeds by Sender'/></g:link></li>
                     <li><g:link controller="report" action="config"><g:message
                            code="report.config" default: 'Feeds by Type'/></g:link></li>
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
                                <td valign="top" class="name">
                                  From Date
                                </td>
                                <td>
                                  <input type="date" name="fromDate" value="${fromDate}"/>
                                </td>
                                </tr><tr>
                                <td>
                                  To Date
                                </td>
                                <td>
                                    <input type="date" name="toDate" value="${toDate}" />
                                </td>
                            </tr>
                             <tr class="prop">
                                <td valign="top" class="name">Last Run </td>
                            <td> <g:formatDate date="${new Date()}" type="datetime" style="MEDIUM"/></td>
                            </tr>
                        	<tr class="prop">
                                <td valign="top" class="name">Run Again </td>
                            <td><g:actionSubmit class="button button-gray" action="config" controller="report" onclick="showSpinner()" name="Run Report" value="Run Report"/> </td>
                            </tr>
                        </ul>
                  </tbody>
</table>
</form>
</div>

	<%
		def totalFeeds=0
		def totalAttachmentSize=0
	
	%>
	<br/><br/><br/>
	<div id="container" class="grid_6"></div>

	<br/><br/><br/>
	<div id="grid" class="grid_6">
	<table class="datatable selectable full">
			<thead>
			<tr>
				<th>Feed Type</th>
				<th>Sender</th>
				<th>Date</th>
				
			</tr>
			</thead>
                    <tbody>
                    
                    <% 
                    	int i = 0
                    	
                    	fullData?.sort{it.config}?.reverse().each {
                    	 
					%>
                        <tr class="${(i++ % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>${it.config}</td>
                            <td>${it.createdBy}</td>
                           <td><g:formatDate date="${it.dateCreated}" type="datetime" style="MEDIUM"/></td>
                        	<% totalFeeds ++ %>
                        </tr>
                    <%}%>
                    
                    <td><b>Total Feeds</b> </td><td>${totalFeeds}</td><td><prettysize:display size="${totalAttachmentSize}" abbr="true" format="###"/></td>
                    </tbody>
                </table>
	
			<table>
	 		<% if ( fromDate != null ) {%>
	 		<tr style='background-color: #FFF8DC' > <td><b> Report Summary : Between  ${fromDate} and ${toDate} there have been ${totalFeeds} feeds shared. </b></td></tr>
   			<%}%>
   			</table>
   			
   	 </div>

            
            
        </section>
    </div>

</section>

<!-- Main Section End -->

</div>
</section>
	 
	
	
</body>
</html>
