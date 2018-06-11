<%@ page import="com.oneapp.cloud.core.FormTagLib" %>
<!DOCTYPE html>
<html lang="en">
<head>
    <title>Form Builder Inbox </title>
    <meta name="layout" content="tablet"/>
    <script type="text/javascript">
   // jQuery.noConflict();
   
	$(document).ready(function(){
		var hrefText = $('#sidebar_menu_home').find('a:first').attr('href');
		var formId = hrefText.substring(hrefText.indexOf('=')+1);	
		window.location = "${request.getContextPath()}/formViewer/create?formId="+formId;
});
	
	</script>
</head>
<body>
</body>
</html>

