
<%@ page import="com.oneapp.cloud.core.BillingHistory" %>
<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title><g:message code="billingHistory.show" default="Invoice Details" /></title>
    </head>
    <body>
         <section class="main-section grid_7">
			<div class="main-content">
				<header>
				    <h2><g:message code="billingHistory.show" default="Invoice Details" /></h2>
			   			<g:if test="${flash.message}">
			            	<div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
			            </g:if>
			            <g:hasErrors bean="${ruleInstance}">
				            <div class="errors">
				                <g:renderErrors bean="${ruleInstance}" as="list" />
				            </div>
			            </g:hasErrors>
				</header>
		        <section class="container_6 clearfix">
					<div class="form grid_6">
			            <g:form>
			             <section>
			                <g:hiddenField name="id" value="${billingHistoryInstance?.id}" />
			                    <table>
			                        <tbody>
			                        	<ul class="action-buttons clearfix fr">
			                           
			                            <tr class="prop">
			                                <td valign="top" class="name" style="padding-left:200px;"><g:message code="billingHistory.client" default="Client" />:</td>
			                                
			                                <td valign="top" class="name" style="width:40%;"><g:link controller="client" action="show" id="${billingHistoryInstance?.client?.id}">${billingHistoryInstance?.client?.encodeAsHTML()}</g:link></td>
			                                
			                            </tr>
			                            
			                            <tr class="prop">
			                                <td valign="top" class="name" style="padding-left:200px;"><g:message code="billingHistory.description" default="Description" />:</td>
			                                
			                                <td valign="top" class="name" style="width:40%;">${fieldValue(bean: billingHistoryInstance, field: "description")}</td>
			                                
			                            </tr>
			                            
			                            <tr class="prop">
			                                <td valign="top" class="name" style="padding-left:200px;"><g:message code="billingHistory.amount" default="Amount" />:</td>
			                                
			                                <td valign="top" class="name" style="width:40%;"><g:formatNumber number="${billingHistoryInstance?.amount}" format="#.##"/></td>
			                                
			                            </tr>
			                            
			                            <tr class="prop">
			                                <td valign="top" class="name" style="padding-left:200px;"><g:message code="billingHistory.currency" default="Currency" />:</td>
			                                
			                                <td valign="top" class="name" style="width:40%;">${fieldValue(bean: billingHistoryInstance, field: "currency")}</td>
			                                
			                            </tr>
			                            
			                            <tr class="prop">
			                                <td valign="top" class="name" style="padding-left:200px;"><g:message code="billingHistory.billDate" default="Bill Date" />:</td>
			                                
			                                <td valign="top" class="name" style="width:40%;"><g:formatDate date="${billingHistoryInstance?.billDate}" /></td>
			                                
			                            </tr>
			                            
			                            <tr class="prop">
			                                <td valign="top" class="name" style="padding-left:200px;"><g:message code="billingHistory.transactionId" default="Transaction Id" />:</td>
			                                
			                                <td valign="top" class="name" style="width:40%;">${fieldValue(bean: billingHistoryInstance, field: "transactionId")}</td>
			                                
			                            </tr>
			                            
			                            
			                           </ul>
			                        </tbody>
			                    </table>
			                   </section>
			                   <div>&nbsp;</div>
			                <div class="action" style="margin-left:245px;">
			                    <input type="button" class="button button-gray" style="width: 140px" value="${message(code: 'default.button.back', default: 'Back')}"
			                    onclick="javascript:goToAccountSetting();"/>
			                </div>
			               
			            </g:form>
			       </div>
		      </section>
			</div>
		</section>
		<script>
			 function goToAccountSetting(){
				loadScreenBlock()
				window.location="${request.getContextPath()}/dropDown/clientUsage"
			}
		</script>
    </body>
</html>
