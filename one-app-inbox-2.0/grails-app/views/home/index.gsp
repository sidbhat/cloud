<html>
<head>
    <title>Form Builder </title>
</head>
<body>
<div id="spinner" class="spinner" style="display:none;width:100%;height: 100%;position: fixed;z-index: 10000;background-color: #fff;">
  <img src="${grailsApplication.config.grails.serverURL}/images/loading.gif" alt="${message(code: 'spinner.alt', default: 'Loading...')}" style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>
</div>
	<script>
		document.getElementById("spinner").style.display = "block";
		window.location="<%=request.getContextPath()%>/dashboard/index"
	</script>
</body>
</html>


			