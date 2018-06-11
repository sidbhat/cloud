<html>
<head>
    <meta http-equiv="Content-Type" content="text/html; charset=UTF-8"/>
    <meta name="layout" content="main"/>
    <title><g:message code="upload.data" default="Upload Data"/></title>
</head>

<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
            <ul class="action-buttons clearfix fr">
                <li><a href="documentation/index.html" class="button button-gray no-text help" rel="#overlay">Help<span
                        class="help"></span></a></li>
            </ul>
               <h2>
                <g:message code="upload" default="Upload"/></h2>

        </header>
      <section class="container_6 clearfix">
            <div class="grid_6">
      <g:if test="${flash.uploadMessage}">
                <div class="message"><g:message code="${flash.uploadMessage}" args="${flash.args}"
                                                default="${flash.defaultMessage}"/></div>
            </g:if>
 
      <g:form controller="upload" method="POST" action="save" enctype="multipart/form-data">
                      <input type="file" name="file"><br/>
                       <input type="submit" class="button button-green" value="${message(code: 'default.button.upload.label', default: 'Upload')}">
      </g:form>
     
                 
  </div>
</section>
            
 </div>
</section>

</body>
</html>