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

        $('#password1').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'>password must be more than 4 char");
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
        $('#repassword1').html("<img src='${resource(dir:'images',file:'cross.png')}' width='20' height='20'>password mismatched");

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

        <g:form action='changePassword' controller="user" name='resetPasswordForm' method="post" autocomplete='off'>
          <g:hiddenField name='t' value='${token}'/>
          <div class="sign-in">
            <table>
              <tr class="prop">
                <td valign="top" class="name">
                  <label for="j_username"><g:message code='Username'/><em>*</em></label></td>
                <td valign="top" class="name"><g:textField name="j_username" value="${session.user?.username}"/></td>
              </tr>
              <tr class="prop">
                <td valign="top" class="name">
                  <s2ui:passwordFieldRow name='old_password' labelCode='resetPasswordCommand.password.label'
                          bean="${command}"
                          labelCodeDefault='Old Password' value="${command?.old_password}"/>
                </td>
                <td valign="top" class="name">
                  <s2ui:passwordFieldRow name='password' labelCode='resetPasswordCommand.password.label'
                          bean="${command}"
                          labelCodeDefault='New Password' value="${command?.password}" onblur="checkLength()" id="password"/><div id="password1" style="position:absolute;left:450px;top:139px"></div>
                </td><td valign="top" class="name">
                <s2ui:passwordFieldRow name='password2' labelCode='resetPasswordCommand.password2.label'
                        bean="${command}"
                        labelCodeDefault='New Password (again)' value="${command?.password2}" onblur="matchPassword()" id="repassword"/> <div id="repassword1" style="position:absolute;left:450px;top:200px"></div>
              </td><td valign="top" class="name">
              </td></tr>
            </table>
            <div class="action">
              <g:actionSubmit class="button button-green help" action="changePassword" controller="user"
                      value="${message(code: 'default.button.change.label', default: 'Change Password')}"/>
            </div>
          </div>
        </g:form>

      </div>

  </div>
</body>
</html>
   
   
   
  

