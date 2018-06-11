<!DOCTYPE html>
<html>
<head>
<title>Create Post</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="mobile">
</head>
<body>
	<div data-role="header" data-position="fixed">
			<a data-icon="arrow-l" class="ui-btn-left" href="${createLink(uri: '/dashboard/index')}"><g:message code="default.label" default="Back"/></a>
			<h1>Create Post</h1>
	</div>
	<div class="content-primary">
		<nav>
			<div>
				<g:form name="postForm" url="[ controller: 'activityFeed', action: 'ajaxcreate']" >
					<div id="messageTable">
						<table width="100%">
							<tr>					
								<td id="tableContentTextArea">													
									<textarea class="markItUpTextarea postAreaMargin" id="postactivity" name="postactivity"
												style="height: 60px; width: 96%;margin-top:10px;background:#fff;" placeholder="Share something..." ></textarea>
								</td>
							</tr>
							<tr>
								<td>
									<a href="javascript:;" onclick="showShareList()" id="showOptionLink" data-role="button" data-inline="true" style="float:right;font-size:11px;">Share Option</a>
								</td>
							</tr>
						</table>
					</div>
					<input type="hidden" name="source" value="">
					<input type="hidden" name="visibility" value="">
					<input type="hidden" name="share" id="shareFormId" value="">
					<input type="hidden" name="shareType" id="shareType" value="">
					<input type="hidden" name="sharedWith" id="sharedWith" value="">
				</g:form>
				<div data-role="content" id="shareList" style="display:none">
					<ul data-role="listview" data-inset="true" data-filter="true" >
						<li data-role="list-divider" >
							Company
						</li>
						<li>
							<a href="javascript:;" onclick="submitform2()"><g:message code="share.company" default="Company" /></a>
						</li>
						<li data-role="list-divider">
							<g:message code="share.user" default="User"/>
						</li>
						<g:each in="${userList}" var="user">
							<li>
								<a href="javascript:;" onclick="submitform3(${user.id})">
									${user.username.substring(0,user.username.indexOf("@"))}
								</a>
							</li>
						</g:each>
						<li data-role="list-divider">
							<g:message code="share.group" default="Group"/>
						</li>
						<g:each in="${groupList}" var="groupDetails">
							<li>
								<a href="javascript:;" onclick="submitform1(${groupDetails.id})">${groupDetails.groupName}</a>
							</li>
						</g:each>
						<li data-role="list-divider">
							<g:message code="share.role" default="Role"/>
						</li>
						<g:each in="${roleList}" var="roleDetails">
							<g:if test="${roleDetails.authority != 'ROLE_SUPER_ADMIN'}">
								<li>
									<a href="javascript:;" onclick="submitform4(${roleDetails.id})">${roleDetails.description}</a>
								</li>
							</g:if>
						</g:each>
						<li data-role="list-divider">
							<g:message code="share.department" default="Department"/>
						</li>
						<g:each in="${deptList}" var="deptDetails">
							<li>
								<a href="javascript:;" onclick="submitform5(${deptDetails.id})">${deptDetails.name}</a>
							</li>
						</g:each>
					</ul>
				</div>
			</div>
		</nav>
		<script>
			function showShareList(){
				$("#showOptionLink").hide();
				$("#shareList").show();
			}
		</script>
	</div>
</body>					
