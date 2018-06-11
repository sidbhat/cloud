
<head>
    <title>

Form Builder</title>
    <meta name='layout' content='main'/>
    <script>
    $(document).ready(function() {
        $('#username').focus();
    });
</script>

</head>

<body class="login">
<div class="login-box main-content">
    <header><h2>Reset Password</h2></header>
    <section>
    <g:form action='email' controller="register" name="forgotPasswordForm" autocomplete='off'>
		<input type="hidden" value="reset" name="resetPassword">
        <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                            default="${flash.defaultMessage}"/></div>
        </g:if>

        <table>
            <tr class="prop">
            		<td valign="top" class="name"><label for="j_username"><g:message code='spring.security.ui.forgotPassword.username'/></label></td>
                <td valign="top" class="name"><g:textField name="j_username" size="25"/></td>
            </tr>
        </table>
        <br/>
  	<div class="action"> 
  	 <input type="button" class="button button-red fr"  value="Cancel"
                    onclick="javascript:cancelAction();"/>
        <g:submitButton name="submit" value="Submit" class="button button-green fr" style="margin-right:10px;" type="submit">Submit</g:submitButton>
       
    </div>                    

    </g:form>
    <br/>
    <script>
		function cancelAction(){
			window.location = "${request.getContextPath()}"
			}
</script>
	</section>
</div>
</body>
