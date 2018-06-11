<div id="spinner" class="spinner" style="width:100%;height: 100%;position: fixed;z-index: 10000;background-color: #fff;">
   <DIV style="Z-INDEX: 10000; POSITION: fixed; TEXT-ALIGN: center; BACKGROUND-COLOR: rgb(255,255,255); WIDTH: 100%; HEIGHT: 100%; VERTICAL-ALIGN: middle" id=spinner class=spinner>
<TABLE width="100%" height="100%">
<TBODY>
<TR>
<TD style="TEXT-ALIGN: center" vAlign=middle align=center>
</TD></TR></TBODY></TABLE></DIV>
</div>
				<sec:ifLoggedIn>
					<g:if test="${request['isMobile']}">
							<g:set var="url" value="${request.getContextPath()}/home/index"/>
					</g:if>
					     <g:elseif test="${request['isTablet']}">
							<g:set var="url" value="${request.getContextPath()}/home/index"/>
					     </g:elseif>
					<g:else>
						<g:if test="${session['user']?.lastLogIn == null }">
							<g:set var="url" value="${request.getContextPath()}/home/welcome"/>
						</g:if>
						<g:elseif test="${session['user']?.mobilePhone == null && session['user']?.claimedId}">
							<g:set var="url" value="${request.getContextPath()}/register/userContactDetail"/>
						</g:elseif>
						<g:else>
							<sec:ifAnyGranted roles="ROLE_TRIAL_USER">
								<sec:ifNotGranted roles="ROLE_HR_MANAGER">
									<g:set var="url" value="${request.getContextPath()}/form/list"/>
								</sec:ifNotGranted>
								<sec:ifAnyGranted roles="ROLE_ADMIN">
									<g:set var="url" value="${request.getContextPath()}/form/list"/>
								</sec:ifAnyGranted>
						    </sec:ifAnyGranted>
						    <sec:ifAnyGranted roles="ROLE_HR_MANAGER">
								<sec:ifNotGranted roles="ROLE_SUPER_ADMIN,ROLE_ADMIN">
									<g:set var="url" value="${request.getContextPath()}/dashboard/index"/>
								</sec:ifNotGranted>
						    </sec:ifAnyGranted>
						    <sec:ifNotGranted roles="ROLE_TRIAL_USER">
								<g:set var="url" value="${request.getContextPath()}/dashboard/index"/>
							</sec:ifNotGranted>
						</g:else>
					</g:else>
					<script>
						window.location="${url}"
					</script>
				</sec:ifLoggedIn>
				<sec:ifNotLoggedIn>
					<script>
						window.location="<%=request.getContextPath()%>/login/auth"
					</script>
				</sec:ifNotLoggedIn>

