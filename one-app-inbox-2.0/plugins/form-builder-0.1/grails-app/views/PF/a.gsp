<g:if test="${flash.message }">
	<g:message args="[]" default="${flash.defaultMessage }" code="${flash.message }"/>
</g:if>
<g:form action="a">
<table>
	<tr>
		<td colspan="2">This form is password protected. Please Enter the password.</td>
	</tr>
	<tr>
		<td>Password: </td><td><input type="password" name="jPass" placeholder="Enter Password"></td>
	</tr>
	<tr>
		<td>
			<input type="hidden" name="pfii" value="${params.pfii }">
			<input type="hidden" name="pfid" value="${params.pfid }">
			<input type="hidden" name="pffn" value="${params.pffn }">
			<input type="hidden" name="formId" value="${params.formId }">
			<input type="hidden" name="goToAction" value="${params.goToAction }">
			<input type="submit">
		</td>
	</tr>
</table>
</g:form>