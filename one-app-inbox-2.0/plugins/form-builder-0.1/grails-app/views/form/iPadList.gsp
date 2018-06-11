<html>
<head>
  		<title>${formName} Results</title>
        <meta name="layout" content="tablet" />
		<jqDT:resources jqueryUI="true"/>
		<style>
			.datatable td img{cursor:pointer}
		</style>
      <script type="text/javascript">
                $(function() {
                      $('.grid_6 table').dataTable({
                          aaData: ${dataToRender.aaData as grails.converters.JSON},
                          aoColumns: ${dataToRender.aoColumns as grails.converters.JSON},
                          bJQueryUI: false,
                          searchAnyWord: true,
                          "bFilter": ${searchable},
                          "sPaginationType": "full_numbers",
                          "aaSorting": [[ 0, "desc" ]],
                          "fnDrawCallback":function(){
                        	  $('.datatable th:first-child').html('<img src="${grailsApplication.config.grails.serverURL}/images/edit.png">');
                        	  $('.datatable td:first-child').each(function(){
                            	  var entryId = ''
                            	  if($('img',this).size() == 0){
                            		  entryId = $(this).text();
                                  }else{
                                	  entryId = $('img',this).attr('entryid');
                                  }
                                  if($(this).html()!='No data available in table'){
                                	  $(this).html('<img src="${grailsApplication.config.grails.serverURL}/images/edit.png"/>');
                                	  $('img',this).attr('entryid',entryId);
                                  }
                              });
                              if($(".paginate_button").size()<5){
                                  $('.dataTables_paginate').hide();
                              } else {
                                  $('.dataTables_paginate').show();
                              }
                              $('td').each(function(){
                                  $(this).attr('title',$(this).text());
                              });
                              $('th').each(function(){
                                  $(this).attr('title',$(this).text());
                              });
                          }
                       });
                       $('.grid_6 tbody').click(function(event) {
                           var target = (event.target);
                           var id
                            
                            	id = $('img',target.parentNode).attr('entryid')
                            //}
                         //$('td:first img', event.target.parentNode.parentNode).attr('eventid');
                         if(id){
                             window.location = "${grailsApplication.config.grails.serverURL}/formViewer/edit/" + id + '?formId=${params.formId}';
                         }
                       });
                      
    
                   });   
        </script>
        <link rel="stylesheet" href="${resource(dir: 'css/streamlined', file: 'DasMain.css')}"/>
    	<style>
    		.dataTables_wrapper{
    			overflow-x:auto;
    		}
    		.paging_full_numbers{
    			width: 290px;
    		}
    		td.alignRight{
    			text-align:right;
    		}
    		th.sorting_asc,th.sorting_desc,th.sorting{
    		cursor: pointer;
    		}
    	</style>

</head>
<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="Form.list" default="List ${formName}" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    
				</table>
							
           </div><%--
            
            <export:formats formats="['pdf','excel']" controller="formViewer" action="exportResponseList" params="[formId:params.formId]" />
	      
	       --%><div class="grid_6 buttonDiv" style="width:120px;margin-top:10px;">
             	
						<div class="PostShare" style="width:120px;">
							<%--<ul style="width:120px;">
								<li ><a href="/form-builder/formViewer/create?formId=${params.formId}" class="ShareMainBtn" style="font-weight: bold;">
									<span class="add"></span><g:message code="default.button.create.label" default="Create"  /></a>
								</li>
							</ul>
							--%><ul style="width:140px;">
								<li ><a href="#" class="ShareMainBtn" style="font-weight: bold;">
									<span  class="add"></span><g:message code="default.button.create.label" default="Create"  /><span class="arrow-down"></span></a>
									<ul style="width:135px;">
										<li>
											<a href="/form-builder/formViewer/create?formId=${params.formId}" style="width:135px;padding:6px 3px;"><g:message code="default.button.create.label" default="Create"  /><span class="subForm" style="float:left;margin-top:-2px;margin-left:5px;padding-left:5px;"></span></a>
										</li>
										
									</ul>
								</li>
							</ul>
						</div>
	                </div>
	                <div>&nbsp;</div>
		            <div>&nbsp;</div>
		            <div>&nbsp;</div>
		            <div id="uploadFormDiv" style="display:none;border:1px solid #ddd;">
			                <g:form method="post" controller="ddc" action="excelUpload" enctype="multipart/form-data" style="margin:15px;">
			                	<g:hiddenField name="formId" value="${params.formId }"/>
			                	<div style="font-weight:bold;color:#666666;"> Upload Form Data</div><br/>
			                	<label for="uploadFormInstance" style="font-weight:bold;color:#666666;">Select File :</label>
			                	<input type="file" name="uploadFormInstance"/><br/>
			                	<input class="save button button-gray" type="submit" value="Upload" style="padding:0px 10px;"/>&nbsp;<input class="button button-red" type="button" onclick="hideUploadForm()" style="padding:0px 10px;" value="Cancel"/>
			                </g:form>
		                </div>
		            </div>
				</section>
				<script>
					function showUploadForm(){
						$("#uploadFormDiv").show('slow')
					}
	
					function hideUploadForm(){
						$("#uploadFormDiv").hide('slow')
					}
				</script>
    </div>
</section>

</body>
</html>
