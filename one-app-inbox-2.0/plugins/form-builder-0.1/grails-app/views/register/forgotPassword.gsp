
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
    <g:form action='email' controller="user" name="forgotPasswordForm" autocomplete='off'>

        <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                            default="${flash.defaultMessage}"/></div>
        </g:if>

        <br/>
        <table>
            <tr class="prop">
            		<td valign="top" class="name"><label for="j_username"><g:message code='spring.security.ui.forgotPassword.username'/></label></td>
                <td valign="top" class="name"><g:textField name="j_username" size="25"/></td>
            </tr>
        </table>
        <br/>
  	<div class="action"> 
        <g:submitButton name="submit" value="Reset Password" class="button button-green fr" type="submit">Reset Password</g:submitButton>
    </div>                    

    </g:form>
	</section>
</div>
</body>
