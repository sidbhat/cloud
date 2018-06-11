<html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="main" />
        <title>%{----------------------------------------------------------------------------
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

<g:message code="activity.list" default="Latest Activity"/></title>
    </head>
<body>
<section class="main-section grid_7">
    <div class="main-content grid_4 alpha">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="${grailsApplication.config.grails.serverURL}/documentation/index.gsp" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>

            <h2>
                <g:message code="activity.list" default="Latest Activity"/></h2>
            <g:if test="${flash.message}">
                <div class="message"><g:message code="${flash.message}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
        </header>

        <section>
           
            <div>

                <ul class="listing list-view">

                    <g:each in="${tlist}" status="i" var="t">
                        <li class="calendar">
                        	 <a href="#">Due today!</a>
                            <span class="timestamp">${fieldValue(bean: t, field: "dueDate")}</span>

                            <p>${fieldValue(bean: t, field: "name")}</p>

                            <div class="entry-meta">
                                Posted by ${fieldValue(bean: t, field: "createdBy")}
                            </div>
                            <!--
                            <g:form method="post" action="taskComment">
                                <input type="hidden" name="task.id" value='${fieldValue(bean: t, field: "id")}'/>
                                	<textarea id="taskComment" class="markItUpTextarea" style="height: 30px; width: 100%;"></textarea>
                                <g:submitButton name="post" class="button button-green"
                                                value="${message(code: 'post', 'default': 'Post')}"/>
                            </g:form>
                            -->
                        </li>
                    </g:each>

                    <g:each in="${mylist}" status="i" var="p">
                        <li class="note">
                            <a href="#">Task!</a>
                            <span class="timestamp">${fieldValue(bean: t, field: "dueDate")}</span>

                            <p>${fieldValue(bean: p, field: "name")}</p>

                            <div class="entry-meta">
                                Posted by ${fieldValue(bean: t, field: "createdBy")}
                            </div>

                        </li>
                    </g:each>


                    <g:each in="${plist}" status="i" var="p">
                        <li class="tick">

                            <a href="#">New Project!</a>
                            <span class="timestamp">${fieldValue(bean: p, field: "dateCreated")}</span>

                            <p>${fieldValue(bean: p, field: "projectName")}</p>

                            <div class="entry-meta">
                                Posted by ${fieldValue(bean: t, field: "createdBy")}
                            </div>

                        </li>
                    </g:each>

                    <g:each in="${followedList}" status="i" var="p">

                      		<%
								String name
								if ( p?.className?.indexOf("Project") != -1 )
									name = "Project <b>${com.oneapp.cloud.core.Project.get(p.persistedObjectId).projectName}</b>"
								else if ( p?.className?.indexOf("User") != -1 )
									name = "User <b>${com.oneapp.cloud.core.User.get(p.persistedObjectId).username}</b>"
								
								String ename
								if ( p?.eventName?.indexOf("INSERT") != -1 )
									ename = "Created"
								else if ( p?.eventName?.indexOf("UPDATE") != -1 )
									ename = "Changed"
									
								
							%>
                            <%	if (ename.indexOf("Created") == -1  && name != null && ename != null) {%>
							  <li class="note">
                            <a href="#">Updates!</a>

					
                            <p>${name} ${ename}</p>

                            <span class="timestamp">${fieldValue(bean: p, field: "lastUpdated")}</span>
							<p>${fieldValue(bean: p, field: "propertyName")} changed to ${fieldValue(bean: p, field: "newValue")}</p>
							
						   
                            <div class="entry-meta">
                                Updated By ${fieldValue(bean: p, field: "actor")}
                            </div>
                            
                            	<% } %>
                         

                        </li>
                    </g:each>

                </ul>

            </div>

            <div class="paginateButtons">
                <g:paginate total="${activityInstanceTotal}"/>
            </div>
</body>
</section>
            </div>


         </section>

</html>
