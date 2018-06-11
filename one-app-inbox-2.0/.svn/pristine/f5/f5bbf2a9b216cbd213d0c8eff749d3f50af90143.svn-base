
<%@ page import="com.oneapp.cloud.core.RuleSet" %>

<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
  	<title><g:message code="ruleSet.create" default="Create Rule Set" /></title>
</head>

<body>
<section class="main-section grid_7">
<div class="main-content">
<header>
    <h2>
       <g:message code="ruleSet.edit" default="Create Rule Set" /></h2>
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
                                    <g:textField name="name" value="${fieldValue(bean: ruleSetInstance, field: 'name')}" />

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
	                                    <g:select name="_action" id="actionToPerform" from="['Subscribe', 'Fetch Email']" value="${ruleSetInstance._action}" valueMessagePrefix="ruleSet._action"  onchange="javascript:showeHideField()"/>
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
                                          onchange="${remoteFunction(action:'fields',update:'fields_dd',params:'\'id=\'+this.value')}"/>
           		
                                </td>
                            </tr>
                                             
      					 <tr class="prop noaction">
                                <td valign="top" class="name">
                                    <label for="resultInstance"><g:message code="ruleSet.resultInstance" default="Send to instance" /><span style="color:red;">* </span>:</label>
                                </td>
                                <td valign="top" class="value ${hasErrors(bean: ruleSetInstance, field: 'resultInstance', 'errors')}">
                              		<div id="fields_dd">
                              		  <%if(prop == null) {%>
                              			<g:select name="noName" id="noName" from="['']" />
									  <%}else{%>
									  	<g:select name="resultInstance" noSelection="['': 'Select One...']" from="${prop}" value="${ruleSetInstance.resultInstance}" />
									  <%}%>
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
     	 <g:actionSubmit class="button button-green" onclick="loadScreenBlock()" action="save" value="${message(code: 'default.button.create.label', default: 'Create')}" />
    </div>
</section>

</div>


  </section>
</div>
</g:form>
</div>
</section>
<script type="text/javascript">
	function showeHideField(){
		var action = $("#actionToPerform").val()
			if(action == "Fetch Email"){
				$(".noaction").hide();
			}else{
				$(".noaction").show();	
			}
		}
</script>
<sec:ifNotGranted roles="ROLE_ADMIN,ROLE_HR_MANAGER,ROLE_SUPER_ADMIN"> 
<sec:ifAnyGranted roles="ROLE_USER">
 <script>
 	$("#actionToPerform").val('Fetch Email')
 	showeHideField();
 </script>
</sec:ifAnyGranted>
</sec:ifNotGranted>  
</body>

</html>

