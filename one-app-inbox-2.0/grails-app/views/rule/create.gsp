
<%@ page import="com.oneapp.cloud.core.Rule" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
  	<title><g:message code="rule.create" default="Create Rule" /></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2>
       <g:message code="rule.edit" default="Create Rule" /></h2>
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
<g:form method="post" >
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
                                    <label for="_order"><g:message code="rule._order" default="Order" /> <span style="color:red;">* </span> :</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: '_order', 'errors')}">
                          		<g:textField name="_order" value="${fieldValue(bean: ruleInstance, field: '_order')}" />
                                </td>
                            </tr>
                            
                         <% if(ruleInstance.ruleSet._action.equalsIgnoreCase("Fetch Email")){%>
							 <tr class="prop">
								 <td valign="top" class="name">
									 <label for="className"><g:message code="rule.className" default="Email Accounts" /><span style="color:red;">* </span>:</label>
								 </td>
								 <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'className', 'errors')}">
				 
									<g:select name="className" from="${emailAccountList}"  optionKey="id" noSelection="['': '--Select Account---']"
										   value="${ruleInstance.className}"/>
	
								 </td>
							 </tr>
										  
							<tr class="prop">
								 <td valign="top" class="name">
									 <label for="attributeName"><g:message code="rule.attributeName" default="Email Field" />:</label>
								 </td>
								 <%if(multipleRule){%>
									 <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'attributeName', 'errors')}">
											 <g:select id="attributeName" name="attributeName" from="['Subject','Sender','Day Prior']"
												value="${ruleInstance.attributeName}"/>
									  </td>
									 <%}else{%>
										 <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'attributeName', 'errors')}">
											 <g:select id="attributeName" name="attributeName" from="['Subject','Sender']"
												value="${ruleInstance.attributeName}"/>
									     </td>
									 <%}%>
								 
								
							 </tr>
							 <tr class="prop">
								 <td valign="top" class="name">
									 <label for="operator"><g:message code="rule.operator" default="Operator" />:</label>
								 </td>
								 <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'operator', 'errors')}">
									 <g:select name="operator" from="['=']"
									 value="${ruleInstance.operator}" valueMessagePrefix="rule.operator"/>
	
								 </td>
							 </tr>
						<%}else{%>
								<tr class="prop">
									<td valign="top" class="name">
										<label for="className"><g:message code="rule.className" default="Form Name" /><span style="color:red;">* </span>:</label>
									</td>
									<td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'className', 'errors')}">
					
									   <g:select name="className" from="${formInstance}"  optionKey="id" noSelection="['': '']"
											  value="${ruleInstance.className}"
											  onchange="${remoteFunction(action:'fields',update:'fields_dd',params:'\'id=\'+this.value')}"/>
			
									</td>
								</tr>
												 
							   <tr class="prop">
									<td valign="top" class="name">
										<label for="attributeName"><g:message code="rule.attributeName" default="Field Name" /><span style="color:red;">* </span>:</label>
									</td>
									<td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'attributeName', 'errors')}">
										   <div class="fields_dd" id="fields_dd">
										   	<g:select name="noName" id="noName" from="['']" />
										   </div>
									</td>
								</tr>
								<tr class="prop">
									<td valign="top" class="name">
										<label for="operator"><g:message code="rule.operator" default="Operator" />:</label>
									</td>
									<td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'operator', 'errors')}">
										<g:select name="operator" from="${ruleInstance.constraints.operator.inList}"
										value="${ruleInstance.operator}" valueMessagePrefix="rule.operator"/>
	
									</td>
								</tr>
						<%}%>
      					
                           
                       	
                 
      					 <tr class="prop">
                                <td valign="top" class="name">

                                    <a href="#" title="If Field Name is Date then value must be in ${message(code:'format.date',args:[],defaultMessage:'MM/dd/yyyy')} format or like @now+2 or @now-2"">

										<label for="value"><g:message code="rule.value" default="Value" /><span style="color:red;">* </span>:</label>
									</a>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'value', 'errors')}">
                                    <g:textField name="value" value="${fieldValue(bean: ruleInstance, field: 'value')}" />
                                </td>
                            </tr>
                           
                                 
      				                            
      			          <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="_condition"><g:message code="rule._condition" default="Condition" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: '_condition', 'errors')}">
                                    <g:select name="_condition" from="${ruleInstance.constraints._condition.inList}" value="${ruleInstance._condition}" valueMessagePrefix="rule._condition" noSelection="['': '']" />

                                </td>
                            </tr>                      
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="status"><g:message code="rule.status" default="Status" />:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'status', 'errors')}">
                                    <g:select name="status" from="${ruleInstance.constraints.status.inList}" value="${ruleInstance.status}" valueMessagePrefix="rule.status"  />

                                </td>
                            </tr>
                        
     
        </tbody>
    </table>
    <div class="action">
    	 <input type="hidden" name="ruleset.id" value="${ruleInstance?.ruleSet?.id}"/>
     	 <g:actionSubmit class="button button-green" action="save" value="${message(code: 'default.button.create.label', default: 'Create')}" style="width: 120px"/>
  		<input type="button" class="button button-gray" style="width: 140px" value="${message(code: 'default.button.ruleset.label', default: 'Rule Set')}"
                    onclick="javascript:openRuleSet();"/>
   </div>
</section>

</div>


  </section>
</div>
</g:form>
</div>
</section>
<script>
	function openRuleSet(){
			window.location = "${request.getContextPath()}/ruleSet/edit/${ruleInstance?.ruleSet?.id}"
		}
</script>
</body>
</html>

