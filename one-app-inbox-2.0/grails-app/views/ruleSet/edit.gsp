
<%@ page import="com.oneapp.cloud.core.RuleSet" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="ruleSet.edit" default="Edit RuleSet" /></title>
    <script>
		function checkFormForClient(valueSendTo){
			if(${isUsedForClient}){
				var alertSendToArray = ${grailsApplication.config.ruleSet.intimationSendTo.onChangeValues as grails.converters.JSON};
				if(jQuery.inArray( valueSendTo, alertSendToArray )>-1){
					alert("${message(code:'ruleSet.intimation.sendTo.onChange.intimation','default':'This rule is to be automatically created for newly created clients. Please make sure its value is available to the user.')}");
				}
			}
		}
	</script>
    
</head>
<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2>
       <g:message code="ruleSet.edit" default="Edit Rule Set" /></h2>
   <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>
            <g:hasErrors bean="${ruleSetInstance}">
            <div class="errors">
                <g:renderErrors bean="${ruleSetInstance}" as="list" />
            </div>
            </g:hasErrors>
</header>
<section class="container_6 clearfix">
<div class="form grid_6">
<g:form method="post" >
				<g:hiddenField name="id" value="${ruleSetInstance?.id}" />
				<g:hiddenField name="version" value="${ruleSetInstance?.version}" />
<div class="tabbed-pane">
<ul class="tabs">
    <li><a href="#">Details</a></li>
</ul>

<!-- tab "panes" -->
<div class="panes clearfix">

<section>

    <table>
        <tbody>
         <ul class="action-buttons clearfix fr">
                         
                         
                                                                  
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="rule"><g:message code="ruleSet.rule" default="Rule" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: 'rule', 'errors')}">
                      &nbsp;
                      <%
                     	def list = ruleSetInstance?.rule?.toList()?.sort{ it._order}
						def formExist = true
						def emailRule = false
						if(list.size() > 0 && ruleSetInstance._action != "Fetch Email"){
							def formName = org.grails.formbuilder.Form.get(Long.parseLong(list[0]?.className))
							if(list[0]?.className != null && formName == null)
								formExist=false
						}else if(list.size() > 0 && ruleSetInstance._action == "Fetch Email"){
							emailRule = true
							formExist = false
						}
						if(formExist || emailRule){
                      %>
                          <g:link controller="rule" action="create" id="${ruleSetInstance.id}" style="float:right;margin-right:50px;margin-top:20px;"><input type="button" class="button1 small gray" style="width:65px;" value="${message(code: 'rule.new', default: 'New Rule')}"/></g:link>
               		<%}%>
						   &nbsp;
                
                <table class="datatable" width='380' style="position:relative;left:10px;bottom:15px;"> 
                    <thead >                
                      <th>Order</th>  
                      <th>Object</th>  
                      <th>Attribute</th> 
                      <th>Operator</th>
                      <th>Value</th>
                      <th>Condition</th>
                    </thead> 
                    <tbody>
                    
                    
                    <%
					if(formExist){
                    	list?.each { rule ->
						if (rule.className != null) { 
							
					%>
                    <tr>  
	                  	  <td>
						   		<g:link controller="rule" action="edit" id="${rule.id}">${rule._order}</g:link>
	                	  </td>     
	                      <td>
	                      		<% println "${org.grails.formbuilder.Form.get(Long.parseLong(rule?.className))}" %>
	                      </td>    
	                      <td>
						  		<% println "${org.grails.formbuilder.Field.get(Long.parseLong(rule?.attributeName))}" %>
	                      </td>         
	                      <td>
						  		${rule.operator}
	                      </td>
	                      <td>
						  		${rule.value}
	                      </td>
	                      <td>
						  		${rule._condition}
	                     </td>
                   </tr> 
                <%}}}else if(emailRule){
						list?.each { rule ->
						if (rule.className != null) { 
							
						%>
	                    <tr>  
		                  	  <td>
							   		<g:link controller="rule" action="edit" id="${rule.id}">${rule._order}</g:link>
		                	  </td>     
		                      <td>
		                      		<% println "${com.oneapp.cloud.core.EmailSettings.get(Long.parseLong(rule?.className))}" %>
		                      </td>    
		                      <td>
							  		<% println "${rule?.attributeName}" %>
		                      </td>         
		                      <td>
							  		${rule.operator}
		                      </td>
		                      <td>
							  		${rule.value}
		                      </td>
		                      <td>
							  		${rule._condition}
		                     </td>
	                   </tr> 
                <%}}}else{%>
					<tr>
						<td colspan="6" style="text-align:center;">
							Form for this rule has been deleted
						</td>
					</tr>	
				<%}%>        
                    
                
                </tbody>
                    </table> 
    			                </td>
                            </tr>
      			
      			<tr class="prop">
                                <td valign="top" class="name">
                                    <label for="_order"><g:message code="ruleSet._order" default="Order" /> <span style="color:red;">* </span> :</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: '_order', 'errors')}">
                                    <g:textField name="_order" value="${fieldValue(bean: ruleSetInstance, field: '_order')}" />

                                </td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="name"><g:message code="ruleSet.name" default="Name" /> <span style="color:red;">* </span> :</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: 'name', 'errors')}">
                                    <g:textField name="name" value="${ruleSetInstance?.name}" />

                                </td>
                            </tr>
                           <sec:ifNotGranted roles="ROLE_ADMIN,ROLE_HR_MANAGER,ROLE_SUPER_ADMIN"> 
                            	<sec:ifAnyGranted roles="ROLE_USER">
		                            <tr class="prop">
		                                <td valign="top" class="name">
		                                    <label for="_action"><g:message code="ruleSet._action" default="Action to perform" />:</label>
		                                </td>
		                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: '_action', 'errors')}">
		                                    <g:select name="_action" id="actionToPerform" from="['Fetch Email']" value="${ruleSetInstance._action}" valueMessagePrefix="ruleSet._action"  onchange="javascript:showeHideField()"/>
		                                </td>
		                            </tr>
                             </sec:ifAnyGranted>
                            </sec:ifNotGranted>  
                             <sec:ifAnyGranted roles="ROLE_HR_MANAGER"> 
	                            <tr class="prop">
	                                <td valign="top" class="name">
	                                    <label for="_action"><g:message code="ruleSet._action" default="Action to perform" />:</label>
	                                </td>
	                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: '_action', 'errors')}">
	                                    <g:select name="_action" id="actionToPerform" from='["${ruleSetInstance._action}"]' value="${ruleSetInstance._action}" valueMessagePrefix="ruleSet._action"  onchange="javascript:showeHideField()"/>
	                                </td>
	                            </tr>
                            </sec:ifAnyGranted>                   
      					 <tr class="prop noaction">
                                <td valign="top" class="name">
                                    <label for="resultClass"><g:message code="ruleSet.resultClass" default="Send to" /><span style="color:red;">* </span>:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: 'resultClass', 'errors')}">
                                  <g:select name="resultClass" from="${ruleSetInstance?.constraints?.resultClass?.inList}"
                       	   				  noSelection="['': '']"
                       	   				  value="${ruleSetInstance.resultClass}"
                                          onchange="${remoteFunction(action:'fields',update:'fields_dd',params:'\'id=\'+this.value')}checkFormForClient(this.value);"/>
           		
                                </td>
                            </tr>
                                             
      					 <tr class="prop noaction">
                                <td valign="top" class="name">
                                    <label for="resultInstance"><g:message code="ruleSet.resultInstance" default="Send to instance" /><span style="color:red;">* </span>:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: 'resultInstance', 'errors')}">
                                 <div id="fields_dd">
									<%--<% if ( ruleSetInstance?.resultInstance != null ){%>--%>
                                  		
                           			<%--<%}--%>	
                           			<g:if test="${prop == null}">
	                           			<g:select name="resultInstance" id="resultInstance"   from="${ruleSetInstance?.constraints?.resultInstance?.inList}" value="${ruleSetInstance.resultInstance}"
	                           			noSelection="['': 'Select One...']"/>
									</g:if>
									<g:else>
										<select>
										    <option value="">Select One...</option>
											<g:each in="${prop}" var="pr">
											   <option value="${pr }" ${ruleSetInstance.resultInstance.toString()==pr.toString()?'Selected=Selected':'N' }>${pr }</option>
											</g:each>
										</select>
								   </g:else>
								</div>

                                </td>
                            </tr>
                                             
      					          
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="status"><g:message code="ruleSet.status" default="Status" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${ruleSetInstance.constraints.status.inList}" value="${ruleSetInstance.status}" valueMessagePrefix="ruleSet.status"  />

                                </td>
                            </tr>
             
        </tbody>
    </table>
    <div class="action">
       <g:actionSubmit class="button button-green" onclick="loadScreenBlock()" action="update" style="width: 120px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
       <g:actionSubmit class="button button-red" onclick="loadScreenBlock()" action="delete" style="width: 120px" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
       <input type="button" class="button button-gray" style="width: 140px" value="${message(code: 'default.button.subscription.list.label', default: 'Subscription')}"
                    onclick="javascript:openSubscriptionList();"/>
   </div>
</section>

</div>


  </section>
</div>
</g:form>
</div>
</section>
 <script>
	<%
		if(ruleSetInstance._action.equalsIgnoreCase("Fetch Email")){
	 %>
	
	 	$("#actionToPerform").val('${ruleSetInstance._action}')
		showeHideField();
	
	 <%}%>
	 
	 function showeHideField(){
		var action = $("#actionToPerform").val()
			if(action == "Fetch Email"){
				$(".noaction").hide();
			}else{
				$(".noaction").show();	
			}
		}
		
	function openSubscriptionList(){
		loadScreenBlock()
		window.location = "${request.getContextPath()}/ruleSet/list"
	}
 </script>

</body>
</html>

