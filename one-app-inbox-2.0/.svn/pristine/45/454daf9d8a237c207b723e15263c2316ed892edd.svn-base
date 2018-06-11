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

<!DOCTYPE html>
<html lang="en">
<head>
    

      <meta name="layout" content="main" />
    
    <script>
        $(document).ready(function() {
            $.tools.validator.fn("#username", function(input, value) {
                return value != 'Username' ? true : {
                    en: "Please enter a value for username"
                };
            });

            $.tools.validator.fn("#password", function(input, value) {
                return value != 'Password' ? true : {
                    en: "Please enter a value for password"
                };
            });

            $("#form").validator({
                position: 'top',
                offset: [25, 10],
                messageClass:'form-error',
                message: '<div><em/></div>' // em element is the arrow
            }).attr('novalidate', 'novalidate');
        });
    </script>

</head>

<body class="login">
<div class="login-box main-content">
    <header><h2>Form Builder Login</h2></header>
    <section>

        <g:if test='${flash.message}'>
            <div class='error'>Login Failed</div>
        </g:if>


        <div class="message info">Enter your username and password</div>

        <form id="form" action="${postUrl}" method="post" class="clearfix">
            <p>
                <input type="text" name='j_username' id='username' class="full" value="" name="username"
                       required="required" autocorrect="off" autocapitalize="off" placeholder="Username"/>
            </p>

            <p>
                <input type="password" name='j_password' id='password' class="full" value="" name="password"
                       required="required" placeholder="Password"/>
            </p>

            <p class="clearfix">
                <span class="fl">
                    <input type='checkbox' class='chk' name='${rememberMeParameter}' id='remember_me'
                           <g:if test='${hasCookie}'>checked='checked'</g:if>/>
                    <label class="choice" for="remember">Remember me</label>
                </span>
				<br/>
                <button class="button button-green fr" type="submit">Login</button>
            </p>
        </form>
        <ul><li><strong>HELP!</strong>&nbsp;<g:link controller="register"
                                                    action="forgotPassword">Forgot Password!</g:link> | <a href="${grailsApplication.config.grails.serverURL}/register/registerUser.gsp">New User ? Register</a></li></ul>
    <!-- parash -->
           <ul><li> <g:link controller="register" action="changePassword">Reset Your Password</g:link></li></ul>

      <!-- end-->


    </section>
</div>
<script type='text/javascript'>(function() {
    document.forms['loginForm'].elements['j_username'].focus();
})();
</script>
</body>
</html>




