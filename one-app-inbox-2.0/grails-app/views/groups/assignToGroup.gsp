
<%@ page import="com.oneapp.cloud.core.*;" contentType="text/html;charset=UTF-8" %>
<html>
<head><title>Assign User(s) to Group</title>
  <meta name="layout" content="main" />
    <script type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery-1.5.1.js')}"></script>
    <script type="text/javascript" src="${resource(dir: 'js/jquery', file: 'jquery-ui-1.8.custom.min.js')}"></script>
   <script language="Javascript">
					function SelectMoveRows(SS1,SS2)
					{
					    var SelID='';
					    var SelText='';
					    // Move rows from SS1 to SS2 from bottom to top
					    for (i=SS1.options.length - 1; i>=0; i--)
					    {
					        if (SS1.options[i].selected == true)
					        {
					            SelID=SS1.options[i].value;
					            SelText=SS1.options[i].text;
					            var newRow = new Option(SelText,SelID);
					            SS2.options[SS2.length]=newRow;
					            SS1.options[i]=null;
					        }
					    }
					    SelectSort(SS2);
					}
					function SelectSort(SelList)
					{
					    var ID='';
					    var Text='';
					    for (x=0; x < SelList.length - 1; x++)
					    {
					        for (y=x + 1; y < SelList.length; y++)
					        {
					            if (SelList[x].text > SelList[y].text)
					            {
					                // Swap rows
					                ID=SelList[x].value;
					                Text=SelList[x].text;
					                SelList[x].value=SelList[y].value;
					                SelList[x].text=SelList[y].text;
					                SelList[y].value=ID;
					                SelList[y].text=Text;
					            }
					        }
					    }
					}

					function showSelectedFields(){
						  var valueArr = new Array()
			               $("#groupUsers option").each(function(){
			                       valueArr[valueArr.length] = $(this).val();
			               })
			               $("#groupUsers").val(valueArr);
						}

					
					</script>
  
</head>
<body>

	<section class="main-section grid_7">
	    <div class="main-content">
	        <header>
	            <h2>Group Assignment</h2>
	            <g:if test="${flash.message}">
	                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
	                                                default="${flash.defaultMessage}"/></div>
	            </g:if>
	            <g:hasErrors bean="${groupsInstance}">
	                <div class="errors">
	                    <g:renderErrors bean="${groupsInstance}" as="list"/>
	                </div>
	            </g:hasErrors>
	            <div id="message"></div>
	
	        </header>
			<section class="container_6 clearfix">
				<div class="form grid_6">
					<div id="wrapper">
					   
						<g:form name="groupAssignmentForm" method="post" action="assignToGroup">
							<g:hiddenField name="groupId" value="${groupList.id}"/>
							<table border="0" cellpadding="3" cellspacing="0" width="40%">
							    <tr>
							        <td width="100%">
							        	<label for="availableUsers">Users</label>
							            <g:select name="availableUsers" size="9"  from="${userList}" multiple="true" optionKey="id"
							                          optionValue="username" style="width:300px;" value=""/>
							        </td>
							        <td align="center" style="vertical-align: middle;" width="20%">
							            <input type="Button" value="Add >>" style="width:100px" class="button button-blue" onClick="SelectMoveRows(document.groupAssignmentForm.availableUsers,document.groupAssignmentForm.groupUsers)"><br>
							            <br>
							            <input type="Button" value="<< Remove" style="width:100px" class="button button-blue" onClick="SelectMoveRows(document.groupAssignmentForm.groupUsers,document.groupAssignmentForm.availableUsers)">
							        </td>
							        <td width="40%">
							        	<label for="groupUsers">${groupList.groupName} Group</label>
							            <g:select name="groupUsers" id="groupUsers" size="9"  from="${groupList?.user}" multiple="true" optionKey="id"
							                          optionValue="username" style="width:300px;"/>
							        </td>
							    </tr>
							</table>
						 	<div class="action" style="margin-left:260px;">
						 			<span style="float:left;">
						 				<input type="button" class="button button-gray" style="width: 100px" value="${message(code: 'back', 'default': 'Back')}"
                    						onclick="javascript:showPreviousScreen();"/>
				                     </span>
				                     <span>
                    					<g:submitButton name="Create" class="button button-gray" style="width: 100px"
                    							value="Assign" onclick="showSelectedFields()"><span class="add"></span></g:submitButton>
                    				</span>
						</g:form>
					   <script>
				                    	function showPreviousScreen(){
					                    		window.location = "${request.getContextPath()}/groups/edit/${groupList.id}"
					                    	}
				                    </script>
					 
					  </div>
				</div>
			</section>
	   </div>
	</section>

</body>
</html>



