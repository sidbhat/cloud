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

<%@page import="org.codehaus.groovy.grails.commons.GrailsApplication"%>
<%@ page import="com.oneapp.cloud.core.*" %>
<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title>${client?.name}&nbsp;<g:message code="usage.details" default="Account Settings"/></title>
</head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li style="width:125px;"><a href="javascript:;" onclick="sendDeleteRequest()" class="button button-red" style="width:125px;color:#fff !important;font-weight:normal;">Delete Account</a></li>
            </ul>

            <h2>
                ${client?.name}&nbsp;<g:message code="usage.details" default="Account Settings"/></h2>
            <g:if test="${flash.message}">
                <div class="message"> <g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>
        <section class="container_6 clearfix">
            <div class="form grid_6">
             <sec:ifAnyGranted roles="ROLE_ADMIN">
            	<fieldset style="padding-left: 0;">
					<legend style="margin-left: 10px">${client.plan?.description?:"Free Trial"}</legend>
					<table>
            			<tbody>
							<%  
							def dataPercentage = 0
								if(totalSpace > 0){
									dataPercentage = ((usedSpace/(1024*1024*totalSpace))*100)
								}
								def color="green"
								if(dataPercentage>=100){
									color="red"
									totalSpace=usedSpace
								   dataPercentage=100
								   }
							%>
	                        <tr class="prop">
	                        <td valign="top" class="name"  width="20%">
	                                <label for="type"><g:message code="clientPlan.Consumer.Key" default="Consumer-Key"/>:</label>
	                            </td><td width="60%" colspan="2">
	                            	<div style="font-weight: bold;padding-top: 6px;">${client?.consumerKey}</div>
	                            </td>
	                        </tr>
	                        <tr class="prop">
	                            <td valign="top" class="name"  width="20%">
	                                <label for="type"><g:message code="clientPlan.dataUsage" default="Data Usage"/>:</label>
	                            </td>
	                            <td valign="top"
	                                class="value ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}" width="40%">
	                                	<div class="progress progress-${color}"><span style="width: ${dataPercentage}%"><b>&nbsp;</b></span></div>
	                            </td>
	                            <td width="40%">
	                            	<div style="width:200px;"><% if(usedSpace > 0) {%><prettysize:display size="${usedSpace}" abbr="true" format="#.##"/><%}else{%>0 GB<%}%> of <prettysize:display size="${1024*1024*totalSpace}" abbr="true" format="#.##"/></div>
	                            </td>
	                        </tr>
	                         <%
							  color="green" 
							  if(usedForms>=totalForms){
								  color="red"
								 totalForms=usedForms
								 }
							 def formPercentage = ((usedForms/totalForms)*100)
								%>
	                        <tr class="prop" >
	                            <td valign="top" class="name" width="20%">
	                                <label for="type"><g:message code="clientPlan.formUsage" default="Forms"/>:</label>
	                            </td>
	                            <td valign="top"
	                                class="value ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}" width="40%">
	                                	<div class="progress progress-${color}"><span style="width: ${formPercentage}%"><b>&nbsp;</b></span></div>
	                            </td>
	                            <td width="40%">
	                            	<div style="width:200px;">${usedForms} of ${totalForms}</div>
	                            </td>
	                        </tr>
	                        <% 
								color="green" 
							  if(usedUserAcc>=totalUserAcc){
								  color="red"
								 totalUserAcc=usedUserAcc
								 }
							def userPercentage = ((usedUserAcc/totalUserAcc)*100)%>
	                        <tr class="prop" >
	                            <td valign="top" class="name" width="20%">
	                                <label for="type"><g:message code="clientPlan.userUsage" default="Users"/>:</label>
	                            </td>
	                            <td valign="top"
	                                class="value ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}" width="40%">
	                                	<div class="progress progress-${color}"><span style="width: ${userPercentage}%"><b>&nbsp;</b></span></div>
	                            </td>
	                            <td width="40%">
	                            	<div style="width:200px;">${usedUserAcc} of ${totalUserAcc}</div>
	                            </td>
	                        </tr>
	                       </tbody>
	                      </table>
					</fieldset>
					
	                <br>
	               </sec:ifAnyGranted>
	                <sec:ifAnyGranted roles="ROLE_TRIAL_USER">
	                	<sec:ifNotGranted roles="ROLE_SUPER_ADMIN, ROLE_ADMIN, ROLE_HR_MANAGER">
	                		<fieldset style="padding-left: 0;">
					<legend style="margin-left: 10px">Free Trial</legend>
					<table>
            			<tbody>
							<%  def dataPercentage = 0
								if(totalSpace > 0){
									dataPercentage = ((usedSpace/(1024*1024*totalSpace))*100)
								}
							
							%>
	                        <tr class="prop">
	                            <td valign="top" class="name"  width="20%">
	                                <label for="type"><g:message code="clientPlan.dataUsage" default="Data Usage"/>:</label>
	                            </td>
	                            <td valign="top"
	                                class="value ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}" width="40%">
	                                	<div class="progress progress-green"><span style="width: ${dataPercentage}%"><b>&nbsp;</b></span></div>
	                            </td>
	                            <td width="40%">
	                            	<div style="width:200px;"><% if(usedSpace > 0) {%><prettysize:display size="${usedSpace}" abbr="true" format="#.##"/><%}else{%>0 GB<%}%> of <prettysize:display size="${1024*1024*totalSpace}" abbr="true" format="#.##"/></div>
	                            </td>
	                        </tr>
	                         <%
							  color="green" 
							  if(usedForms>=totalForms){
								  color="red"
								 totalForms=usedForms
								 }
							 def formPercentage = ((usedForms/totalForms)*100)
								%>
	                        <tr class="prop" >
	                            <td valign="top" class="name" width="20%">
	                                <label for="type"><g:message code="clientPlan.formUsage" default="Forms"/>:</label>
	                            </td>
	                            <td valign="top"
	                                class="value ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}" width="40%">
	                                	<div class="progress progress-${color}"><span style="width: ${formPercentage}%"><b>&nbsp;</b></span></div>
	                            </td>
	                            <td width="40%">
	                            	<div style="width:200px;">${usedForms} of ${totalForms}</div>
	                            </td>
	                        </tr>
	                       </tbody>
	                      </table>
					</fieldset>
					
	                <br>
	                	</sec:ifNotGranted>
	                </sec:ifAnyGranted>
            	<g:form action="makePayment" method="post" name="paymentForm">
            		<g:hiddenField name="planType"  value=""/>
            		<fieldset>
						<legend style="margin-left: 10px">Plan Details</legend>
						<table>
							 <sec:ifAnyGranted roles="ROLE_TRIAL_USER">
         	 					<sec:ifNotGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN,ROLE_HR_MANAGER">
         	 						<tr class="prop">
			                            <td valign="top" class="name">
			                                <label for="name"><g:message code="upgradeclient.name" default="Client Name"/>: <em>*</em></label>
			                            </td>
			                            <td valign="top" class="value ${hasErrors(bean: clientInstance, field: 'name', 'errors')}">
			                                <g:textField name="clientName" value="${fieldValue(bean: clientInstance, field: 'name')}" style="border:1px solid #E6E6CB; box-shadow:0px 1px 10px 1px #E0FF00;"/>
			                            </td>
			                        </tr>
         	 					</sec:ifNotGranted>
         	 				</sec:ifAnyGranted>							
						</table>
						<% def planList = com.oneapp.cloud.core.Plan.list()%>
						<g:set var="planIdx" value="${-1}"/>
						<table class="datatable tablesort selectable full">
						<thead>
							<tr>
								<th width="25%"></th>
								<%planList.eachWithIndex{cPlan,idx->%>
									<th width="25%" <%if(client.plan?.id == cPlan?.id){planIdx=idx;println 'style="box-shadow:-2px -2px 6px 1px;border-right:1px solid #9C9191;"'}%>>
										<h3 >${cPlan.planName}</h3>
										<p class="price">$${cPlan.amount}/month</p>
									</th>
								<%}%>
							</tr>
						</thead>
						<tbody>
							<tr class="odd" style="text-align:center;">
								<td>Users</td>
								<%planList.eachWithIndex{cPlan,idx->%>
									<td  ${idx==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>${cPlan.maxUsers}</td>
								<%}%>
							</tr>
							<tr class="even" style="text-align:center;">
								<td>Forms</td>
								<%planList.eachWithIndex{cPlan,idx->%>
									<td ${idx==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>${cPlan.form}</td>
								<%}%>
							</tr>
							<tr class="odd" style="text-align:center;">
								<td>Data Storage</td>
								<%planList.eachWithIndex{cPlan,idx->%>
									<td ${idx==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><prettysize:display size="${(cPlan.maxStorage*1024*1024)}" abbr="true" format="#.##"/></td>
								<%}%>
							</tr>
							<tr class="even" style="text-align:center;">
								<td>Reporting</td>
								<td ${0==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>Inbox and form reporting</td>
								<td ${1==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}style="white-space:normal;">Inbox, form and advanced reporting</td>
								<td ${2==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>Full analytics and reporting</td>
							</tr>
							<tr class="odd" style="text-align:center;">
								<td>Email Integration</td>
								<%planList.eachWithIndex{cPlan,idx->%>
									<td ${idx==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>Connect upto ${cPlan.maxEmailAccount} accounts</td>
								<%}%>
							</tr>
							<tr class="even" style="text-align:center;">
								<td>Spreadsheet Integration</td>
								<td ${0==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
								<td ${1==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
								<td ${2==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
							</tr>
							<tr class="odd" style="text-align:center;">
								<td>Google Apps Integration</td>
								<td ${0==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
								<td ${1==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
								<td ${2==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
							</tr>
							<tr class="odd" style="text-align:center;">
								<td>PayPal Integration </td>
								<td ${0==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
								<td ${1==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
								<td ${2==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
							</tr>
							<tr class="odd" style="text-align:center;">
								<td>Toll Free Support</td>
								<td ${0==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>-</td>
								<td ${1==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>-</td>
								<td ${2==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}><img alt="" src="http://sidbhat1976.blogspot.com/wp-content/themes/Karma/functions/extended/pricing-tables//images/checkbox.png"></td>
							</tr>
						</tbody>
						<tfoot>
						<tr style="text-align:center;">
							<td></td>
							<%planList.eachWithIndex{cPlan,idx->
								if(client.plan){
									%>	
										<td ${idx==planIdx?'style="box-shadow:-2px 1px 6px 0;border-right:1px solid #9C9191;"':''}>
											<% if(client.plan.id != cPlan.id){%>
												<input type="button" class="button button-blue" style="width: 100px;margin:0 0 0 35px;" value="${message(code: 'request.for.payment', 'default': ' Change Plan')}" onclick="makePayment(true,'${cPlan.id}')"/>
											<%}else{%>
											<img src="${request.getContextPath()}/images/btn_unsubscribe_LG.gif" onclick="makeUnsubscribeRequest();" style="cursor: pointer;"></div><%}%>
										</td>
									<%	
									
								}else{
							%>
								<td>
									<input type="button" class="button button-blue" style="width: 100px;margin:0 0 0 35px;" value="${message(code: 'request.for.payment', 'default': ' Pay')}" onclick="makePayment(false,'${cPlan.id}')"/>
								</td>
							<%
								
								}
								%>
							<%}%>
						
						</tfoot>
						</table>
						<table>
							<tr>
								<td colspan="2">&nbsp;</td>
							</tr>
							<tr>
							   <td colspan="2" style="font-weight:bold;"><label for="termsAgree">Accept Terms <em>*</em></label></td>
							</tr>
							<tr >
							   <td><input type="checkbox" name="termsAgree" id="termsAgree" style="margin-top: 10px;margin-left:65px;"/></td>
							   <td><span style="position:relative;bottom:5px;">I Agree to the<a href="${grailsApplication.config.grails.serverURL}/documentation/terms_condition.gsp"  style="color:blue !important;text-decoration:underline !important;" class="no-text help" rel="#overlay"> Terms of Service</a></td>
							</tr>
						</table>
					</fieldset>
                </g:form>
                 <sec:ifAnyGranted roles="ROLE_ADMIN">
               	   <br>
               		<table width="100%">
		                <tbody>
		                	<tr class="prop" >
	                            <td valign="top" class="name" style="width:0.5%;padding-left:10px;">
	                                <label for="type"><g:message code="clientPlan.billingHistory" default="Billing History"/>:</label>
	                            </td>
	                            <td valign="top"
	                                class="name ${hasErrors(bean: dropDownInstance, field: 'type', 'errors')}" width="80%">
	                                <g:if test="${billingHistory.size() > 0}">
	                                	<g:each in="${billingHistory}" status="i" var="billingHistoryInstance">
		                                	<span><a href="${grailsApplication.config.grails.serverURL}/billingHistory/show/${billingHistoryInstance?.id}" style="text-decoration: underline !important;"><g:formatDate date="${billingHistoryInstance?.billDate}" /></a></span>
		                                </g:each>
	                                </g:if>
	                                <g:else>
	                                	<span><a href="javascript:;">No History available</a></span>
	                                </g:else>
	                            </td>
	                            
	                        </tr>
            			</tbody>
            		</table>
            	</sec:ifAnyGranted>
            		<br>
            		<table>
            			<tr>
	                        	<td colspan="2"><div style="margin-left:90px;font-weight:bold;">For any other enquiry please call us at <span style="color:#666666;font-style: italic;">1 (888)-368-4318</span> or write us at <a href="mailto:info@yourdomain.com" style="color:#666666;font-style: italic;">info@yourdomain.com</a></div></td>
	                        </tr>
            		</table>

            </div>
        </section>
    </div>
</section>
<script type="text/javascript">
function sendDeleteRequest(){
	var conf=confirm('Are you sure you want to send an account deletion request?')
	if(conf){
		loadScreenBlock()
		window.location="${grailsApplication.config.serverURL}/form-builder/activityFeed/accountDeleteRequest"
	}
}
function makeUnsubscribeRequest(){
	var conf=confirm('Are you sure you want to Unsubscribe this plan?')
	if(conf){
		window.location="${grailsApplication.config.serverURL}/form-builder/dropDown/unSubscribeRequest"
	}	
}
function makePayment(ch,planId){
	var conf
	if(ch){
		conf=confirm('Are you sure you want to change this plan?')
	}else{
		conf=true
	}
	if(conf){
		 var termsAgree = $("#termsAgree").is(':checked')
		 if(termsAgree){
			 $("#planType").val(planId)
			 $('[name="paymentForm"]').submit();
		 }else{
			 alert("Please accept the terms and condition")
		 }
	}
}


 
</script>
</body>
</html>
