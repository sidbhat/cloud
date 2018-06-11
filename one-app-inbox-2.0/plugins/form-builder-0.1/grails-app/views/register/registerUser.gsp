
<head>
    <title>

Form Builder</title>
    <meta name='layout' content='main'/>
    <script>
      jQuery.noConflict();
    $(document).ready(function() {
        $('#username').focus();
    });
</script>

</head>

<body class="login">
<div class="login-box main-content">
    <header><h2>Sign Up</h2></header>
    <section>
    <g:form action='register' controller="user" name="register" autocomplete='off'>

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
        <g:submitButton name="submit" value="Sign Up" class="button button-green fr" type="submit">Sign Up</g:submitButton>
    </div>                    

    </g:form>
	</section>
</div>
</body>

   
   
  

