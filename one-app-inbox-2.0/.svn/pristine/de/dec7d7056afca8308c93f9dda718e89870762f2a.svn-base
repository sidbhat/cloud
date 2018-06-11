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
<html>
<head>
 
  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
  <title><g:message code="password.reset" default="Contact Detail"/></title>
   <meta name='layout' content='main'/>
   
   <script>
   
      jQuery.noConflict();
      
   </script>
   <style>

.intl-tel-input.pretty .flag-dropdown .country-list {
padding: 0;
margin: 0 0 0 -1px;
box-shadow: 1px 1px 4px rgba(0, 0, 0, 0.2);
background-color: white;
border: 1px solid #cccccc;
width: 266px !important;
max-height: 200px;
overflow-y: scroll;
}

.fr {
float: left!important;
}

</style>
  <link rel="stylesheet" href="${resource(dir: 'css', file: 'intlTelInput.css')}"/>
  
</head>

<body style="overflow:hidden;">

<!-- Main Section -->
	<section class="main-section grid_7" style="width:600px;margin-top:25px;margin-left:212px;">
	  <div class="main-content" style="min-height:400px;">
	    <header>
	      <h2>
	        <g:message code="contact.details" default="Contact Detail"/></h2>
		      <g:if test="${flash.message}">
		        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
		                default="${flash.defaultMessage}"/></div>
		      </g:if>
	
	    </header>
	    <section class="container_6 clearfix">
		      <div class="form grid_6" style="width:550px;">
		        <g:form action='updateDetails' controller="register" name='resetPasswordForm' method="post" autocomplete='off'>
		          <g:hiddenField name='t' value='${token}'/>
		          <div class="sign-in">
		            <table>
		            	<tr class="prop">
					        <td valign="top" class="name">
					            <label for="username"><g:message code="user.username" default="Phone"/>:</label>
					        </td>
					        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'username', 'errors')}">
					            <g:textField name="username" value="${username}" readonly="readonly" />
					        </td>
					    </tr>
					    <tr class="prop">
					        <td valign="top" class="name">
					            <label for="company"><g:message code="user.company" default="Company"/>:</label>
					        </td>
					        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'company', 'errors')}">
					            <g:textField name="company" value="${companyName}" readonly="readonly" />
					        </td>
					    </tr>
		               <tr class="prop">
					        <td valign="top" class="name">
					            <label for="mobilePhone"><g:message code="user.mobilePhone" default="Phone"/>:</label>
					        </td>
					        <td valign="top" class="value ${hasErrors(bean: userInstance, field: 'mobilePhone', 'errors')}">
					           &nbsp;&nbsp;&nbsp;<input type="tel" id="mobile-number" placeholder="E.g. +1 702 123 4567" name="mobilePhone" size="25" style="-webkit-border-radius: 3px;
-khtml-border-radius: 3px;
border-radius: 3px;
border: 1px solid #ccc;
padding: 4px 6px;
-moz-box-shadow: inset 0 1px 3px rgba(0,0,0,0.1);
-webkit-box-shadow: inset 0 1px 3px rgba(0,0,0,0.1);
box-shadow: inset 0 1px 3px rgba(0,0,0,0.1);
font: 13px/16px Arial,sans-serif!important;padding-left: 35px;width: 266px;">
					        </td>
					    </tr>  
		            </table>
		            <div class="action" style="width:310px;">
		              <input type="button" class="button button-green help" onclick="checkFieldValue();" style="width:140px;clear:left;" value="${message(code: 'default.button.change.label', default: 'Update')}"/>
		            </div>
		          </div>
		        </g:form>
		      </div>
		      
	      </section>
	      <script type="text/javascript">

		    function checkFieldValue(){
					document.resetPasswordForm.submit();
		        }
  </script>
  
	  </div>
  </section>
 <script type="text/javascript" src="${resource(dir: 'js', file: 'jquery-1.7.1.min.js')}"></script>
   <script src="${resource(dir: 'js', file: 'intlTelInput.js')}"></script>
   <script>
    $("#mobile-number").intlTelInput();
    </script>
</body>
 
</html>
   
   
   
  

