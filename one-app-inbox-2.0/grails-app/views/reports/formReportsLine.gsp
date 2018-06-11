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
		if (date1 == '' ^ date2 == '') {
			if (date1 == '') {
				alert("Please Enter From Date..!");
				$('#' + cdate1).select();
			} else {
				alert("Please Enter To Date..!");
				$('#' + cdate2).select();
			}
			return false;
		}
		if ($('#formId').val()==null||$('#formId').val()==""){
			alert("Please select a Form ..!");
			return false;
		}
		if ($('#xAxis').val()==null||$('#xAxis').val()==""){
			alert("Please select value For X-Axis ..!");
			return false;
		}if ($('#yAxis').val()==null){
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
	</script>
	<script type="text/javascript" src="${resource(dir: 'js', file: 'formReport.js')}"></script>
<g:set var="entityName" value="${message(code: 'reports.label', default: 'Report')}"/>
    <title><g:message code="default.report.label" args="[entityName]"/></title>
    <% if ( fullData != null ) {%>
   <script type="text/javascript">

        var chart;
		var accessChart;
		var dataChartTitle = 'Report on form data for ${formName}'
		var dataDataLabel = ${dataLabel}
		var dataYAxisLabel = ${yAxisLabel}
		var dataData = ${data}
		var pieData = ${pieData as grails.converters.JSON}
		
		var viewChartTitle = 'Form Analytics for ${formName}'
		var yDataArrListViews = ${yDataArrListViews}
		var dataLabelViews = ${dataLabelViews}
		
		var accessData = ${accessArrList};
		var accessChartTitle = 'Access report for ${formName}'
		var accessDataLabel = ${accessDataLabel}
		var yAxisAccessLabels = ${yAxisAccessLabels}
		
        $(document).ready(function(){
			drawChart('bar');
			drawAccessChart();
			drawViewsChart()
			showDataReport();
		});

    </script>
	<%}%>
	<script type="text/javascript">
		function getXandYAxis(formDD){
			if($(formDD).val() !=''){
				jQuery.ajax({type:'POST', url:'/form-builder/report/getXandYAxis/'+$(formDD).val(),success:function(data,textStatus){setXandYAxis(data);},error:function(XMLHttpRequest,textStatus,errorThrown){alert("error.");}});
			}
		}
		function setXandYAxis(data){
			var $xAxis = $('#xAxis');
			$('option',$xAxis).remove();
			$xAxis.append('<option value="">Please Select</option>');
			
			var $yAxis = $('#yAxis');
			$('option',$yAxis).remove();
			
			var $sort = $('#sort');
			$('option',$sort).remove();
			$sort.append('<option value="">Please Select</option>');
			
			for(var i=0; i<data.xAxis.length; i++){
				var $opt = $('<option></option>');
				$opt.val(data.xAxis[i].name)
				$opt.text(data.xAxis[i].label)
				$opt.attr('title',data.xAxis[i].label)
				$xAxis.append($opt);
				if(data.xAxis[i].name!='createdBy'){
				 	$sort.append($opt.clone());
				}
			}
			for(var i=0; i<data.yAxis.length; i++){
				var $opt = $('<option></option>');
				$opt.val(data.yAxis[i].name)
				$opt.text(data.yAxis[i].label)
				$opt.attr('title',data.yAxis[i].label)
				$yAxis.append($opt);
				$sort.append($opt.clone());
			}
		}
	</script>
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
                <h2>Report on forms <a href="#">&darr;</a></h2>
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
       

        <section class="container_6 clearfix">
            
            
            
	<div class="grid_6">
	
	<g:form >
	<table class="datatable full">
                        <tbody>
                           <tr class="prop">
                                <td valign="top" class="name">
                                  From Date
                                </td>
                                <td>
	                                <div>
	                                	<input type="date" id="fromDate" name="fromDate" title="From Date" />
	                                </div>
                                </td>
                                <td>Form: <font color="red">*</font> </td>
                                <td>
                                	<g:select style="width:200px" from="${formList }" name="formId" optionKey="id" noSelection="['':'Please Select']" onchange="getXandYAxis(this);" value="${params.formId }" />
                                </td>
                            </tr><tr>
                                <td>
                                  To Date
                                </td>
                                <td>
                                	<div>
                                    	<input type="date" id="toDate" name="toDate" title="To Date" />
                                    	<script>
	                                		$(document).ready(function(){
		                                		$("#fromDate").val("${fromDate}")
		                                		$("#toDate").val("${toDate}")
		                                	});
	                                	</script>
                                    </div>
                                </td>
                                <td>X-Axis: <font color="red">*</font>  </td>
                                <td>
<%--                                	<g:select style="width:200px" from="${xAndYAxis?.xAxis }" name="xAxis" noSelection="['':'Please Select']" optionKey="name" optionValue="label" value="${params.xAxis }" />--%>
                                	<select id="xAxis" name="xAxis" style="width:200px">
                                		<option value="">Please Select</option>
                                		<g:each in="${xAndYAxis?.xAxis }" var="xAxis">
                                			<g:if test="${xAxis.isLikert && xAxis.rows }">
                                				<optgroup label="${xAxis.label.encodeAsHTML() }">
	                                				<g:each in="${xAxis.rows }" var="row" status="rowIdx">
	                                					<option value="${xAxis.name+'@_@'+rowIdx}" ${(xAxis.name+'@_@'+rowIdx) == params.xAxis?'selected="selected"':'' }>${row.encodeAsHTML() }</option>
	                                				</g:each>
                                				</optgroup>
                                			</g:if>
                                			<g:else>
                                				<option value="${xAxis.name}" ${xAxis.name == params.xAxis?'selected="selected"':'' }>${xAxis.label.encodeAsHTML() }</option>
                                			</g:else>
                                		</g:each>
                                	</select>
                                </td>
                            </tr>
                             <tr class="prop">
                                <td valign="top" class="name">Last Run </td>
                            	<td> <g:formatDate date="${new Date()}" type="datetime" style="MEDIUM"/></td>
                                <td rowspan=2>Y-Axis: <font color="red">*</font>  </td>
                                <td rowspan=2>
                                	<g:select style="width:200px" from="${xAndYAxis?.yAxis }" name="yAxis" optionKey="name" optionValue="label" value="${params.list('yAxis') }" multiple="true"/>
                                </td>
                            </tr>
                            <tr>
                            	<td>Sort/Order</td>
                            	<td>
                            		<select name="sort" id="sort" style="width:115px">
                            			<option value="">Please Select</option>
                            			<g:each in="${xAndYAxis?.xAxis }" var="xAxis">
                            				<g:if test="${xAxis.name!='createdBy'}">
                            				<option value="${xAxis.name}" ${xAxis.name==params.sort?'selected':'' } title="${xAxis.label }">${xAxis.label }</option>
 											</g:if>                           			
                            			</g:each>
                            			<g:each in="${xAndYAxis?.yAxis }" var="yAxis">
                            				<option value="${yAxis.name}" ${yAxis.name==params.sort?'selected':'' } title="${yAxis.label }">${yAxis.label }</option>
                            			</g:each>
                            		</select>
                            		<select name="order" id="order" style="width:100px">
                            			<option value="asc" ${'asc'==params.order?'selected':'' }>Ascending</option>
                            			<option value="desc" ${'desc'==params.order?'selected':'' }>Descending</option>
                            		</select>
                            	</td>
                            </tr>
                        	<tr class="prop">
                                <td valign="top" class="name" >Run Again </td>
                            	<td><g:actionSubmit class="button button-gray" action="formReport" onclick="return twodateComp('fromDate','toDate');" name="Run Report" value="Run Report"/></td>
                            	<td>Chart Type: </td><td>${fullData?'<select onchange="drawChart(this.value)"><option value="bar">Bar Chart</option>'+(params.list('yAxis') && params.list('yAxis').size()==1?'<option value="pie">Pie Chart</option>':'')+'<option value="line">Line Chart</option></select>':'&nbsp;' }</td>
                            </tr>
                            <tr>
                        		<td colspan="4">
                        			<div class="button button-red accessButtons" onclick="showDataReport();">
                        				Data<br>Report
                        			</div>
                        			<div class="button button-green accessButtons" onclick="showAccessReport();">
                        				Access<br>Report
                        			</div>
                        			<div class="button button-blue accessButtons" onclick="showViewsReport();">
                        				Views<br>${totalViews}
                        			</div>
                        			<div class="button button-blue accessButtons" onclick="showViewsReport();">
                        				Creates<br>${totalCreates}
                        			</div>
                        			<div class="button button-orange accessButtons">
                        				Conversion<br><%try{
											DecimalFormat df = new DecimalFormat("#.#");
											print(df.format(100/(totalViews/totalCreates)))
										}catch(Exception e){print("0")}
									%> %
                        			</div>
                        		</td>
                        	</tr>
                  </tbody>
</table>
</g:form>
</div>
		<br/><br/><br/>
		<div class="cap capContainer"></div>
		<div id="container" class="grid_6 chartDrawn"></div>
		<div id="containerAccess" class="grid_6 chartDrawn"></div>
		<div id="containerViews" class="grid_6 chartDrawn"></div>
	        
            
        </section>
    </div>

</section>

<!-- Main Section End -->

</div>
</section>
	 
	
	
</body>
</html>
