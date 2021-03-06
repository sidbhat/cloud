<html>
<head>
  		<title>${formName} Results</title>
        <meta name="layout" content="main" />
		<jqDT:resources jqueryUI="true"/>
      <script type="text/javascript">
                $(function() {
                      $('.grid_6 table').dataTable({
                          aaData: ${dataToRender.aaData as grails.converters.JSON},
                          aoColumns: ${dataToRender.aoColumns as grails.converters.JSON},
                          bJQueryUI: false,
                          searchAnyWord: true,
                          "sPaginationType": "full_numbers",
                          "aaSorting": [[ 0, "desc" ]],
                          "fnDrawCallback":function(){
                              //alert("coming here")
                              if($(".paginate_button").size()<5){
                                  $('.dataTables_paginate').hide();
                              } else {
                                  $('.dataTables_paginate').show();
                              }
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
    		table.datatable th, table.datatable td{
    			max-width:140px;
    		}
    	</style>

</head>
<body>
<section class="main-section grid_7">
    <div class="main-content">
        <header>
             <h2><g:message code="Form.history.list" default="History - ${formName} Form Id ${uniqueFormId}" /></h2>
            <g:if test="${flash.message}">
            <div class="message"><g:message code="${flash.message}" args="${flash.args}" default="${flash.defaultMessage}" /></div>
            </g:if>

        </header>
        <section class="container_6">
            <div class="grid_6">
                <table class="datatable tablesort selectable full">
                    
				</table>
							
           </div>
           <div class="grid_6 buttonDiv" style="width:120px;margin-top:10px;">
             	<div class="PostShare" style="width:120px;">
						
						<div class="PostShare" style="width:120px;">
							<ul style="width:120px;">
								<li ><a href="/form-builder/formViewer/list?formId=${params.formId}" class="ShareMainBtn" style="font-weight: bold;">
									<g:message code="default.button.back.label" default="Back"  /></a>
								</li>
							</ul>
						</div>
	             	  	
	                </div>
		</section>
    </div>
</section>

</body>
</html>
