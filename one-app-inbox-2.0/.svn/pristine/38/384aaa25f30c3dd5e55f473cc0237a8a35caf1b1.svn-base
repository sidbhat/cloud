<html>
	<head>
		<meta name="layout" content="apiTest"/>
		<title>Test APIs</title>
		<script>
			$(document).ready(function(){
				$("ul.tabs").tabs("div.panes > div",{initialIndex:0});
			});
		</script>
		<style>
			h1{margin:10px}
			body{height:auto;}
			.bodyDiv{margin:20px}
			.panes > div{padding:20px}
		</style>
	</head>
	<body>
		<div class="bodyDiv">
			<h1>Test APIs. Note: The response here will always be rendered in "pretty" format.</h1>
			<div class="tabbed-pane">
				<ul class="tabs">
					<li><a href="#">Login API</a></li>
					<li><a href="#">Register API</a></li>
					<li><a href="#">Users API</a></li>
					<li><a href="#">Forms API</a></li>
					<li><a href="#">Fields API</a></li>
					<li><a href="#">Entries API</a></li>
					<li><a href="#">EntrySave API</a></li>
					<li><a href="#">ActivityFeeds API</a></li>
					<li style="width: 160px;"><a href="#" style="width: 160px;">Create ActivityFeed API</a></li>
					<li style="width: 120px;"><a href="#" style="width: 120px;">Add Comment API</a></li>
				</ul>
				<div class="panes">
					<div>
						<form action="${createLink('controller':'apiV1','action':'login') }" method="post" autocomplete="off" target="loginResponse">
							<input type="text" name="username" placeholder="Enter Username" />
							<input type="password" name="password" placeholder="Enter Password" />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Go" />
						</form>
						<iframe id="loginResponse" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'registerUser') }" method="post" autocomplete="off" target="registerUser">
							<input type="hidden" name="test" value="true"/>
							<input type="text" name="consumerKey" placeholder="Enter Consumer-Key" />
							<input type="text" name="username" placeholder="Username" />
							<input type="password" name="password" placeholder="Password"  />
							<input type="text" name="mobile" placeholder="Enter mobile nubmber"  />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Go" />
						</form>
						<iframe id="registerUser" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'users') }" method="post" autocomplete="off" target="usersResponse">
							<input type="text" name="apiKey" placeholder="Enter API-Key" />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Go" />
						</form>
						<iframe id="usersResponse" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'forms') }" method="post" autocomplete="off" target="formsResponse">
							<input type="text" name="apiKey" placeholder="Enter API-Key" />
							<input type="text" name="formId" placeholder="Enter Form Id (optional)" />
							<input type="text" name="parentFormId" placeholder="Parent Form Id (Only for Sub Forms)" title="Parent Form Id (Only for Sub Forms)" />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Go" />
						</form>
						<iframe id="formsResponse" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'fields') }" method="post" autocomplete="off" target="fieldsResponse">
							<input type="text" name="apiKey" placeholder="Enter API-Key" />
							<input type="text" name="formId" placeholder="Enter Form Id" />
							<input type="text" name="parentFormId" placeholder="Parent Form Id (Only for Sub Forms)" title="Parent Form Id (Only for Sub Forms)" />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Go" />
						</form>
						<iframe id="fieldsResponse" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'entries') }" method="post" autocomplete="off" target="entriesResponse">
							<input type="text" name="apiKey" placeholder="Enter API-Key" />
							<input type="text" name="formId" placeholder="Enter Form Id" />
							<input type="text" name="entryId" placeholder="Enter Entry Id (optional)" />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Go" />
						</form>
						<iframe id="entriesResponse" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'fields') }" method="post" autocomplete="off" target="fieldForEntry">
							<input type="hidden" name="test" value="true"/>
							<input type="text" name="apiKey" placeholder="Enter API-Key" />
							<input type="text" name="formId" placeholder="Enter Form Id" />
							<input type="text" name="parentFormId" placeholder="Parent Form Id (Only for Sub Forms)" title="Parent Form Id (Only for Sub Forms)" />
							<input type="text" name="parentFormEntryId" placeholder="Parent Form Entry Id (Only for Sub Forms)" title="Parent Form Entry Id (Only for Sub Forms)" />
							<input type="text" name="parentFormFieldName" placeholder="Parent Form Field Name (Only for Sub Forms)" title="Parent Form Field Name (Only for Sub Forms)" />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Fetch test UI" />
						</form>
						<iframe id="fieldForEntry" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'getUserFeeds') }" method="post" autocomplete="off" target="feedsResponse">
							<input type="text" name="apiKey" placeholder="Enter API-Key" />
							Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Go" />
						</form>
						<iframe id="feedsResponse" width="100%" height="500px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'addFeed') }" method="post" name="postForm"  enctype="multipart/form-data" autocomplete="off" target="addFeed">
							 <input type="text" name="apiKey"style="width: 26%;"  placeholder="Enter API-Key" />
							 <input type="text" style="width: 26%;"  name="formId" placeholder="Enter Form Id(optional)" /> <input type="text" style="width: 26%;"  name="share" placeholder="Enter Enrty Id(optional)" /><br>
							 <input type="text" style="width: 26%;" name="usernames" placeholder="Enter usernames (separated by,)" /> <input type="text" style="width: 26%;" name="groups" placeholder="Enter group name (separated by,)" /><br>
							 <textarea style="height: 30px; width: 26%;resize: vertical;" name="postactivity" placeholder="Share something..." ></textarea> <input type="file" name="file" id="attachFile" style="display: inline; margin-left: 0px;width: 26%;" >
						    Response Type:<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Share" />
						</form>
						<iframe id="addFeed" width="100%" height="480px;"></iframe>
					</div>
					<div>
						<form action="${createLink('controller':'apiV1','action':'addComment') }" method="post" autocomplete="off" target="addComment">
							<input type="text" name="apiKey" placeholder="Enter API-Key" />
							<input type="text" name="id" placeholder="Enter activity feed Id" />
						     Response Type: 
							<g:select name="respType" from="${[[key:'xml',value:'XML'],[key:'json',value:'JSON']] }" optionKey="key" optionValue="value"/>
							<g:hiddenField name="pretty" value="true"/>
							<input type="submit" value="Add Comment" /><br>
							<textarea style="height: 20px; width: 46%;margin-right:30px;resize: vertical;" name="commentBody" placeholder="Write comment...." ></textarea>
						</form>
						<iframe id="addComment" width="100%" height="480px;"></iframe>
					</div>
				</div>
			</div>
		</div>
	</body>
</html>