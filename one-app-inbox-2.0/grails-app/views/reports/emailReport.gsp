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
                        text: 'Email Count'
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
                	name:'Total Emails',
                	data:${data}
                	}]
            });


        });

       

    </script>
	<style type="text/css">
		.message a:link, .message a:visited, .message a:hover {
			color: orangered;
			text-decoration: underline;
			} 
	</style>
</head>

<body>
	
	
	<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <g:if test="${flash.message}">
<%--                <div class="message"><g:message code="${flash.message}" args="${flash.args}"--%>
<%--                                                default="${flash.defaultMessage}"/></div>--%>
			<div class="message">${flash.message}</div>
            </g:if>
  		
  			<div class="view-switcher">
                <h2>Email Analyzer<a href="#">&darr;</a></h2>
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
			<g:form >
				<table class="datatable full">
	                        <tbody>
	                        <ul class="action-buttons clearfix fr">
	                           <tr class="prop">
	                                <td style="vertical-align:top;width:150px;max-width:300px;">
	                                  Days Prior(Upto 180 days)
	                                </td>
	                                <td style="vertical-align:top;">
	                                  <g:textField name="daysPrior" value="${daysPrior}" placeholder="7"/>
	                                </td>
	                            </tr>
	                            <tr class="prop">
	                                <td style="vertical-align:top;">
	                                  Email Account
	                                </td>
	                                <td style="vertical-align:top;">
	                                 	<g:select name="id" from="${emailSettingList}" optionKey="id" value="${selectedAccount}"  />
	                                 	
	                                </td>
	                            </tr>
	                        	<tr class="prop">
	                                <td valign="top" class="name">Run Again </td>
	                            	<td>
	                            		<g:actionSubmit class="button button-gray" action="search" controller="report" onclick="showSpinner()" name="Run Report" value="Run Report"/>
	                            		<input type="button" class="button button-gray" style="width: 140px;"  value="${message(code: 'report.forms', default: 'Check Email Settings')}" onclick="navigateToEmailSetting()" /> 
	                            	</td>
	                            	
	                            </tr>
	                        </ul>
	                  </tbody>
				</table>
			</g:form>
			<% if ( fullData != null ) { %>
	<% if ( params.print == null )  {%>
		<div id="container" style="width: 780px; height: 500px; margin: 0 auto; "></div>
	<%}%>
	<%
		def totalEmails=0
		def totalAttachmentSize=0
	
	%>
	<% if ( params.print == null )  {%>
	<div id="grid" style="width: 800px; margin: 0 auto">
	<%}else{%>
	<div id="grid" style="width: 770px;">
	<%}%>
	<div id="grid" class="grid_6">
	<table class="datatable selectable full">
			<thead>
			<tr>
				<th style="width:225px;max-width:250px;">Sender</th>
				<th>Total Emails</th>
				<th style="width:170px;max-width:200px;">Most Recent</th>
				<th>Total Attmt. Size</th>
				<th>Largest Attmt. Size</th>
				
			</tr>
			</thead>
                    <tbody>
                    
                    <% 
                    	int i = 0
                    	fullData.each {
                    	com.oneapp.cloud.core.Sender o = (com.oneapp.cloud.core.Sender)it.value
						def fromAddress = o?.email
						def containsName = fromAddress.indexOf("<")
						def fromEmailId = null
						if(containsName == -1){
							fromEmailId = fromAddress
						}else{
							fromEmailId = fromAddress.substring(fromAddress.indexOf("<")+1,fromAddress.indexOf(">"))
						}
					%>
                        <tr class="${(i++ % 2) == 0 ? 'odd' : 'even'}">
                        
                            <td>
								<g:link controller="ruleSet" action="emailRule" params="['sender':fromEmailId,'emailAccount':selectedAccount]" >${fieldValue(bean: o, field: "email")}</g:link>
							</td>
                        
                            <td>${fieldValue(bean: o, field: "emailCount")}</td>
                            <% totalEmails += o.emailCount %>
                        	<td><g:formatDate date="${o.mostRecent}" type="datetime" style="MEDIUM"/></td>
                        	<td><prettysize:display size="${o.totalSize}" abbr="true" format="###"/></td>
                        	<% totalAttachmentSize += o.totalSize %>
                        	<td><prettysize:display size="${o.largestSize}" abbr="true" format="###"/></td>
                        </tr>
                    <%}%>
                    
                    <td><b>Total Emails</b> </td><td>${totalEmails}</td><td><b>Total Attachmet Size</b></td><td><prettysize:display size="${totalAttachmentSize}" abbr="true" format="###"/></td><td></td>
                    </tbody>
                </table>
	
			<table>
	          <tr style='background-color: #FFF8DC' > <td><b> Report Summary : In past ${search.daysPrior} day(s) you have received ${totalEmails} mails from ${i} people. </b></td></tr>
   			</table>
   			
   			<p align="center">
   			<%if (params.print != null ){%>
   			<br/>
				sidbhat1976.blogspot.com
			<%}%>
			</p>
   </div>
	<%}%>
		</div>
	  </section>
    </div>

</section>

<!-- Main Section End -->

	 
	<script>
	 function navigateToEmailSetting(){
     	window.location = "${request.getContextPath()}/emailSettings/index"
     }
	</script>
	
</body>
</html>
