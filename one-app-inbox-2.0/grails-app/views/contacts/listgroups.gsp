<!DOCTYPE html>
<html>
<head>
<title>DashBoard</title>
<meta http-equiv="Content-type" content="text/html; charset=utf-8">
<meta name="layout" content="main">
<link rel="stylesheet" media="screen" href="${resource(dir:'css',file:'DasMain.css')}" />

<script type="text/javascript">
		function showlayer(san){document.getElementById(san).style.display="block";}
		function hidelayer(san){document.getElementById(san).style.display="none";}
	<script type="text/javascript">   
 $(document).ready(function(){
 $("a#CreateGroup").click(function(){  
 $("#lightbox, #CreateGroupOver").fadeIn(400);  
 })  
 $("input#CreateGroupClose").click(function(){  
 $("#lightbox, #CreateGroupOver").fadeOut(400);  
 }) 
 $("a#LeaveGroup").click(function(){  
 $("#lightbox, #LGroup").fadeIn(400);  
 })  
 $("input#LeaveGroupClose").click(function(){  
 $("#lightbox, #LGroup").fadeOut(400);  
 })  
 })
 </script> 	
	</script>
</head>
<body>
	<section>
		        
                    <div class="main-content grid_4 alpha">  
                                     
                        <header>
                            <ul class="action-buttons clearfix fr">
                               <li><a href="#" class="button button-gray" id="CreateGroup"><b>Create Group</b></a></li>
                            </ul>
   		  				  <div class="view-switcher"> <h2>Group</h2> </div>

                      </header>
                        
                        <section class="GroupMain">
                        	<h5>My Groups (${groupList.size()})</h5>
                            
                            <ul id="contacts" class="listing list-view clearfix">
                            
                            <g:if test="${groupList!=null && groupList.size()==0}">
											<h2>No Groups for you</h2>
							</g:if>
							<g:else>
							<g:each in="${groupList}" var="groupDetails">
                            
                                <li class="GroupCtnt clearfix" style="padding:5px 10px">
                                    <div class="avatar"><img src="${resource(dir:'images',file:'user_32.png')}" /></div>
                                   <g:remoteLink controller="contacts"
														action="groupContactDetails" id="${groupDetails.id}"
														update="sideexpansion" class="more">&raquo;</g:remoteLink>
                                    <div class="GroupCtntInr">

                                    	<p><a href="#">${groupDetails.groupName}</a></p>
                                        <p class="LastUpdated">${groupDetails.groupDescription} </p>
                                        <p><span><b>Created On: <prettytime:display
																	date="${groupDetails.createdOn}"/></b> <g:link controller="contacts" action="deletegroup" id="${groupDetails.id}">Delete Group</g:link></span></p>
                                    </div>
                                </li>

                               	</g:each>
							</g:else> 
                               
                                
                            </ul>
                            
                            <ul class="pagination clearfix">
                                <li><a href="#" class="button-gray">&laquo;</a></li>
                                <li><a href="#" class="current button-gray">1</a></li>
                                <li><a href="#" class="button-gray">2</a></li>
                                <li><a href="#" class="button-gray">3</a></li>

                                <li><a href="#" class="button-gray">&raquo;</a></li>
                            </ul>
                            
                        </section>
                        
                    </div>

                  </div>

                </section>

            </div>
            
            <div id="push"></div>
            
        </section>
        
    </div>
    
    
    
    <div class="Ovarlay" id="CreateGroupOver">

      <div class="CreateGroupMain">
    	<div class="CreateGroupMainInr">
        	<h2><span>Create Group</span></h2>
        	<g:form controller="contacts" action="creategroup" method="post">
            <div>
            	<span>Group name :</span>
                <input name="groupName" type="text" class="TextField"></input>
            </div>

            <div>
            	<span>Description :</span>
                <textarea name="groupDescription" class="TextArea"></textarea>
            </div>

           
            <div class="CreateGroupBtn">
            	<input type="button" class="button button-gray" value="Cancel" id="CreateGroupClose" />

            	<input type="submit" class="button button-blue" value="Create" />
            </div>
            
           </g:form>
        </div>
      </div> <!--CreateGroupMain div end here-->
    </div> <!--Getting Ovarlay div end here-->
    
    <div class="Ovarlay" id="LGroup">
    	<div class="DeleteGroupMain">
        	<div class="DeleteGroupMainInr">

            	<h2>Leave Test Group</h2>
                <p>Are you sure you want to give up your administrator privileges and leave this group? The administrator position will be offered to other people who are currently in the group. This will also prevent members from re-adding you.</p>
            	<p>If you ever want to rejoin, visit the group and click Ask to Join Group.</p>
                <div>
                    <input type="button" class="button button-gray" value="Cancel" id="LeaveGroupClose" />
                    <input type="button" class="button button-blue" value="Leave Group" id="LeaveGroupClose" />
                </div>

            </div>
        </div> <!--Getting DeleteGroupMain div end here-->
    </div> <!--Getting overlay div end here-->
    
   
</body>
</html>