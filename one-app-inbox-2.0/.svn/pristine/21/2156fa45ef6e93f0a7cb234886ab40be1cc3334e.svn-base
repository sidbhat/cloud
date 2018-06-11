
<%@ page import="com.oneapp.cloud.core.Rule" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="rule.edit" default="Edit Rule" /></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
   

    <h2>
       <g:message code="rule.edit" default="Edit Rule" /></h2>
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
				<g:hiddenField name="id" value="${ruleInstance?.id}" />
				<g:hiddenField name="version" value="${ruleInstance?.version}" />
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
								 <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'attributeName', 'errors')}">
										<g:select id="attributeName" name="attributeName" from='["${ruleInstance.attributeName}"]'
										   value="${ruleInstance.attributeName}"/>
								 </td>
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
                       	   		<g:select name="className" from="${[form] as List}"  
                       	   				   optionKey="id" 
                       	   				  noSelection="['': '']"
                                          value="${form?.id}"
                                          onchange="${remoteFunction(action:'fields',update:'fields_dd',params:'\'id=\'+this.value')}"/>
           						</td>
                            </tr>
                                             
      					 <tr class="prop">
                                <td valign="top" class="name">
                                    <label for="attributeName"><g:message code="rule.attributeName" default="Field Name" /><span style="color:red;">* </span>:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'attributeName', 'errors')}">
                                  	<div class="fields_dd" id="fields_dd">
                                  	<% if ( ruleInstance.attributeName != null ){%>
                                  		<g:select name="attriuteName" optionKey='id' from="${fields}" value="${ruleInstance.attributeName}"/>
                           			<%}%>
                           			</div>
                                </td>
                            </tr>
							<tr class="prop">
								<td valign="top" class="name">
									<label for="operator"><g:message code="rule.operator" default="Operator" />:</label>
								</td>
								<td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'operator', 'errors')}">
									<g:select name="operator" from="${ruleInstance.constraints.operator.inList}" value="${ruleInstance.operator}" valueMessagePrefix="rule.operator"  />
	
								</td>
							</tr>
						<%}%>
                         
                   
      					 <tr class="prop">
                                <td valign="top" class="name">

                                	<a href="#" title="If Attribute Name is Date then value must be in ${message(code:'format.date',args:[],defaultMessage:'MM/dd/yyyy')} format or like @now+2 or @now-2">
										<label for="value"><g:message code="rule.value" default="Value" /><span style="color:red;">* </span>:</label>
									</a>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleInstance, field: 'value', 'errors')}">
                                    <g:textField name="value" value="${ruleInstance?.value}" />

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
       <g:actionSubmit class="button button-green" action="update" style="width: 120px" value="${message(code: 'default.button.update.label', default: 'Update')}" />
       <g:actionSubmit class="button button-red" action="delete" style="width: 120px" value="${message(code: 'default.button.delete.label', default: 'Delete')}" onclick="return confirm('${message(code: 'default.button.delete.confirm.message', default: 'Are you sure?')}');" />
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

