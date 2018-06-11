<%@ page contentType="text/html;charset=ISO-8859-1" %>
<html>
<head>
<meta http-equiv="Content-Type" content="text/html; charset=ISO-8859-1"/>
<meta name="layout" content="main"/>
<title>Insert title here</title>
</head>
<body>
  <div class="body">
  		<h1>Logged-in User Details</h1>
  		<div class="dialog">
  			<table>
  				<tr><td>Email id: </td><td>${username }</td></tr>
  				<tr><td>First Name: </td><td>${firstName }</td></tr>
  				<tr><td>Last Name: </td><td>${lastName }</td></tr>
  			</table>
  		</div>
  </div>
</body>
</html>