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
  <meta name="layout" content="main"/>
  <title><g:message code="password.reset" default="Password Change"/></title>
  <script type="text/javascript">
    function checkLength() {
      var password = $("input#password").val()
      if (password.length >= 5) {
        document.resetPasswordForm.password.style.background = "white";
        $('#password1').html("<img src='${resource(dir:'images',file:'check.png')}' width='20' height='20'>");
      } else {

        $('#password1').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'><span style='vertical-align:top;margin-left:5px;'>Password must be more than 4 char</span>");
        document.resetPasswordForm.password.focus();
        document.resetPasswordForm.password.style.background = "pink";

      }
    }
    function matchPassword() {
      var password = $("input#password").val()
      var repassword = $("input#repassword").val()
      if (password == repassword) {
        $('#repassword1').html("<img src='${resource(dir:'images',file:'check.png')}' width='20' height='20'>");
      } else {
        $('#repassword1').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'><span style='vertical-align:top;margin-left:5px;'>Password mismatched</span>");

      }
    }

    function checkFieldValue(){
    	 var userName = $("input#username").val()
         var oldPassword = $("input#old_password").val()
    	 var password = $("input#password").val()
         var repassword = $("input#repassword").val()
         if(!userName){
        	 $('#resetUserName').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'><span style='vertical-align:top;margin-left:5px;'>Username is required</span>");
             document.resetPasswordForm.username.focus();
             document.resetPasswordForm.username.style.background = "pink";
             return;
          }
         else if(!oldPassword){
        	 $('#oldpassword').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'><span style='vertical-align:top;margin-left:5px;'>Old password cannot be empty</span>");
             document.resetPasswordForm.old_password.focus();
             document.resetPasswordForm.old_password.style.background = "pink";
             return;
          }
         else if(!password){
        	 $('#password1').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'><span style='vertical-align:top;margin-left:5px;'>Password must be more than 4 char</span>");
             document.resetPasswordForm.password.focus();
             document.resetPasswordForm.password.style.background = "pink";
             return;
          }else if(!repassword)
           {
        	  $('#repassword1').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'><span style='vertical-align:top;margin-left:5px;'>Password mismatched</span>");
              document.resetPasswordForm.repassword.focus();
              document.resetPasswordForm.repassword.style.background = "pink";
              return;
           }else{
				document.resetPasswordForm.submit();
              }
        }
  </script>
</head>

<body>
<!-- Main Section -->
	<section class="main-section grid_7">
	  <div class="main-content">
	    <header>
	      <ul class="action-buttons clearfix fr">
	        <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
	                class="help"></span></a></li>
	      </ul>
	
	      <h2>
	        <g:message code="password.reset" default="Password Change"/></h2>
	      <g:if test="${flash.message}">
	        <div class="message"><g:message code="${flash.message}" args="${flash.args}"
	                default="${flash.defaultMessage}"/></div>
	      </g:if>
	
	    </header>
	    <section class="container_6 clearfix">
		      <div class="form grid_6">
		        <g:form action='changeUserPassword' controller="register" name='resetPasswordForm' method="post" autocomplete='off'>
		          <g:hiddenField name='t' value='${token}'/>
		          <div class="sign-in">
		            <table>
		              <tr class="prop">
		                <td valign="top" class="name">
		                  <label for="j_username"><g:message code='Username'/><em>*</em></label></td>
		                <td valign="top" class="value"><g:textField name="j_username" id="username" value="${userNameInstance}"/><div id="resetUserName" style="position:absolute;left:450px;top:10px"></td>
		              </tr>
		              <tr class="prop">
		                <td valign="top" class="name">
		                  <s2ui:passwordFieldRow name='old_password' labelCode='resetPasswordCommand.password.label'
		                          bean="${command}"
		                          labelCodeDefault='Old Password' value="${command?.old_password}"/><div id="oldpassword" style="position:absolute;left:450px;top:65px">
		                </td>
		                <td valign="top" class="name">
		                  <s2ui:passwordFieldRow name='password' labelCode='resetPasswordCommand.password.label'
		                          bean="${command}"
		                          labelCodeDefault='New Password' value="${command?.password}" onblur="checkLength()" id="password"/><div id="password1" style="position:absolute;left:450px;top:115px"></div>
		                </td>
		                <td valign="top" class="name">
		                <s2ui:passwordFieldRow name='password2' labelCode='resetPasswordCommand.password2.label'
		                        bean="${command}"
		                        labelCodeDefault='Confirm Password' value="${command?.password2}" onblur="matchPassword()" id="repassword"/> <div id="repassword1" style="position:absolute;left:450px;top:160px"></div>
		              </td><td valign="top" class="name">
		              </td></tr>
		            </table>
		            <div class="action" style="width:310px;">
			           <input type="button" class="button button-red fr" style="width:140px;"  value="Cancel"
	                    onclick="javascript:cancelAction();"/>
		              <input type="button" class="button button-green help" onclick="checkFieldValue()" style="width:140px;clear:left;" value="${message(code: 'default.button.change.label', default: 'Change Password')}"/>
		              
		            </div>
		          </div>
		        </g:form>
		      </div>
		      <script>
					function cancelAction(){
						window.location = "${request.getContextPath()}"
						}
			  </script>
	      </section>
	  </div>
  </section>
</body>
</html>
   
   
   
  

