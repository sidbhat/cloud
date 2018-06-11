<%@ page import="com.oneapp.cloud.core.*" %>
<% 
	if (!session.bgcolor){
		if ( session?.user && UserProfile.findByUser( session?.user) && UserProfile.findByUser( session?.user).background ) 
	   		session.bgcolor = UserProfile.findByUser( session?.user).background
	}

%>
body {
    background-color:${session.bgcolor} !important;
}




