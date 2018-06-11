
package org.grails.formbuilder

import java.util.List;

/**
*
* @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
*
* @since 0.1
*/
class FormBuilderConstants {
	static final String EMPTY_STRING = ""
	static final String DEFAULT_STRING = "A"
	static final Date DEFAULT_DATE = new Date()
	static final Number DEFAULT_NUMBER = 0
	static final String DEFAULT_CREDIT_CARD_NO = "11111111111"
	static final String DEFAULT_EMAIL = "admin@yourdomain.com"
	static final String DEFAULT_URL = "http://www.google.com"
	static final String NEW_LINE = "\n"
	static final String WIDGET_PACKAGE = "org.grails.formbuilder.widget"
	static final String DESIGNER_LAYOUT = "formDesigner"
	static final String VIEWER_LAYOUT = "formViewer"
	static final String IPAD_LAYOUT = "tablet"
	static final String FORM_VIEWER_CONTROLLER = "formViewer"
	static final String PERSISTABLE = "_persistable"
	static final List DOMAIN_CLASS_SYSTEM_FIELDS = ["id", "version", "dateCreated", "lastUpdated"]
	static final String BUILDER_PANEL = """\
  <div id="builderPanel" style="\${flash.builderPanelStyles}" class="\${flash.panelClass}">
	<div class="formBodyStyle">
		<div style="padding:0 1em;">
			<div class="widthSetterSlider" style="height:0px;border:0px;"></div>
		</div>
		<div class="formHeading\${flash.formHeadingHorizontalAlign}" style="\${flash.headingStyle}"><div class="formHeadingStyle"><div class="playPreviewBox"><div title="Save and Preview" class="formPreviewButton"></div><div title="Settings" class="formSettingsButton"></div></div>\${flash.formHeading}</div><div id="formDescription" style="\${flash.descriptionStyle}"><div class="formDescriptionStyle">\${flash.description}</div></div></div>
		  <fieldset style="width:100%;">
			<div id="emptyBuilderPanel">Add fields from the menu on the left.</div>
		  @FIELDS
		</fieldset>
	  </div>
	</div>
	"""
	static final String BUILDER_PALETTE = """\
  <div id="builderPalette">
	 <div class="floatingPanelIdentifier"></div>
	 <div class="floatingPanel">
			<div id="paletteTabs">
				<ul>
					<li><a href="#addField">Add Field</a></li>
					<li><a href="#fieldSettings">Field Settings</a></li>
					<li><a href="#formSettings">Form Settings</a></li>
				</ul>
				<div id="addField">
				 	<fieldset class="language">
						<legend>Standard Fields</legend>
						<div id="standardFields"></div>
					</fieldset>
				 	<fieldset class="language">
						<legend>Fancy Fields</legend>
						<div id="fancyFields"></div>
					</fieldset>
				 	<fieldset class="language">
						<legend>Advanced Fields</legend>
						<div id="advanceFields"></div>
					</fieldset>
				</div>
				<div id="fieldSettings">
					<fieldset class="language">
					<legend></legend>
					</fieldset>
					<div class="general">
					</div>
				</div>
				<div id="formSettings">
					<fieldset class="language">
					<legend>
					<label for="language">Language: </label>
					<select id="language">
					  <option value="en">English</option>
					</select>
					</legend>
					</fieldset>
					<div class="general"></div>
				</div>
			</div>
	 </div>
  </div>
	  """
	static final String FB_CREATE_VIEW = """\
[#ftl/]
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="${DESIGNER_LAYOUT}" />
		[#assign  entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
		<title>[@g.message code="default.create.label" args=[entityName] /]</title>
		<script type="text/javascript">
		\$(function() {
			 \$('#container').formbuilder({formCounter: 1, language: '\${flash.language}'});
			 \$('div.buttons').children().button();
				});
		</script>
	</head>
	<body>
		
		<!--content tag="nav">
		   <div class="title"><h1>[@g.message code="default.create.label" args=[entityName] /]</h1></div>
		   <span class="menuButton">[@g.link class="list" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]</span>
		</content-->
				<div id="container">
				  <div id="header">
					<div id="dashBoardMessageId" style="width:100%;margin:auto;position: absolute;display: block;text-align: center;">
						<div class="message success closeable" style="padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 15px; position: relative;   margin-top: auto; margin-right: auto; margin-bottom: auto; margin-left: auto; width: auto; top: 50px; font-weight: bold; z-index: 1001; display: none;">
							<span class="message-close" style="display:block;"></span>
				             <p>
								[#if flash.message?exists]
				                 	\${flash.message}
					            [/#if]
								[@g.hasErrors bean=formInstance]
									[@g.renderErrors bean=formInstance _as="list" /]
								[/@g.hasErrors]
				             </p>
				         </div>
				       </div>
					 	[#if flash.message?exists]
					       <script>
					          showMessage();
					        </script>
					    [/#if]
						[@g.hasErrors bean=formInstance]
							<script>
					           showMessage();
					        </script>
						[/@g.hasErrors]
				  </div>
					${BUILDER_PALETTE}
				  [@g.form controller="form" name="builderForm" class="uniForm" enctype="multipart/form-data"]
					  [@g.hiddenField name="name" value="\${formInstance.name}" /]
					  [@g.hiddenField name="formCat" value="\${formInstance.formCat}" /]
						[@g.hiddenField name="settings" value="\${formInstance.settings}" /]
						[@g.hiddenField name="extraParameter" value="" /]
					${BUILDER_PANEL}
				  <div class="action">
						  [@g.actionSubmit class="button button-green formSaveButton" action="save" style="width:120px;" value="\${g.message({'code': 'default.button.create.label', 'default': 'Create'})}" /]
						  [@g.actionSubmit class="button button-blue" action="list" style="width:120px;" value="\${g.message({'code': 'default.button.list.label', 'default': 'List'})}" /]
					</div>
					<style id="FormStyles">\${flash.formCSS}</style>
					<script id="FormJS">\${flash.formJS}</script>
					[/@g.form]
				</div>
	
	</body>
</html>
"""
	static final String FB_SHOW_VIEW = """\
[#ftl/]
<html>
	<head>
		<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
		<meta name="layout" content="${DESIGNER_LAYOUT}" />
		 [#assign entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
		<title>[@g.message code="default.show.label" args=[entityName] /]</title>
		<script type="text/javascript">
		\$(function() {
			 \$('#container').formbuilder({tabDisabled: [0], tabSelected: 2, readOnly: true, language: '\${flash.language}'});
		   \$('div.buttons').children().button();
				});
		</script>
	</head>
	<body>
		<!--content tag="nav">
		   <div class="title"><h1>[@g.message code="default.show.label" args=[entityName] /]</h1></div>
		   <span class="menuButton">[@g.link class="list" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]</span>
		   <span class="menuButton">[@g.link class="create" action="create"][@g.message code="default.new.label" args=[entityName] /][/@g.link]</span>
		</content-->
				<div id="container">
				  <div id="header">
				[#if flash.message?exists]
				<div class="message">\${flash.message}</div>
				[/#if]
				  </div>
					${BUILDER_PALETTE}
				  [@g.form name="builderForm" class="uniForm"]
						[@g.hiddenField name="id" value="\${formInstance.id}" /]
						[@g.hiddenField name="name" value="\${formInstance.name}" /]
					  [@g.hiddenField name="settings" value="\${formInstance.settings}" /]
					${BUILDER_PANEL}
				  <div class="buttons">
				  [@g.actionSubmit class="edit" value="\${g.message({'code': 'default.button.edit.label', 'default': 'Edit'})}" /]
				  [@g.link class="save" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]	
				 </div>
					[/@g.form]
				</div>
	</body>
</html>
		"""
	static final String FB_EDIT_VIEW = """\
		[#ftl/]
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<meta name="layout" content="${DESIGNER_LAYOUT}" />
				[#assign  entityName=g.message({'code': 'form.label', 'default': 'Form'}) /]
				<title>[@g.message code="default.edit.label" args=[entityName] /]</title>
				<script type="text/javascript">
				\$(function() {
					 \$('#container').formbuilder({language: '\${flash.language}', disabledNameChange: true});
					 \$('div.buttons').children().button();
						});
				 function confirmMessage(message){
				            var confirmation = confirm(message)
				            if(confirmation == true){
				            	loadScreenBlock();
				                return true;
				            }else{
				                return false;
				            }
				        }
					</script>
			</head>
			<body>
				<!--content tag="nav">
				   <div class="title"><h1>[@g.message code="default.edit.label" args=[entityName] /]</h1></div>
				   <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
				   <span class="menuButton">[@g.link class="list" action="list"][@g.message code="default.list.label" args=[entityName] /][/@g.link]</span>
		       <span class="menuButton">[@g.link class="create" action="create"][@g.message code="default.new.label" args=[entityName] /][/@g.link]</span>
				</content-->
						<div id="container">
						  <div id="header">
                              <div id="dashBoardMessageId" style="width:100%;margin:auto;position: absolute;display: none;text-align: center;">
								<div class="message success closeable" style="padding-top: 5px; padding-right: 5px; padding-bottom: 5px; padding-left: 15px; position: relative;   margin-top: auto; margin-right: auto; margin-bottom: auto; margin-left: auto; width: auto; top: -15px; font-weight: bold; z-index: 1001; display: inline-block;">
									<span class="message-close" style="display:block;"></span>
						             <p>
										[#if flash.message?exists]
						                 	\${flash.message}
							            [/#if]
										[@g.hasErrors bean=formInstance]
											[@g.renderErrors bean=formInstance _as="list" /]
										[/@g.hasErrors]
						             </p>
						         </div>
						       </div>
							 	[#if flash.message?exists]
							       <script>
							          showMessage();
							        </script>
							    [/#if]
								[@g.hasErrors bean=formInstance]
									<script>
							           showMessage();
							        </script>
								[/@g.hasErrors]
						  </div>
						  ${BUILDER_PALETTE}
						  [@g.form method="post" name="builderForm" class="uniForm" enctype="multipart/form-data"]
							  [@g.hiddenField name="name" value="\${formInstance.name}" /]
						  		[@g.hiddenField name="formCat" value="\${formInstance.formCat}" /]
							  [@g.hiddenField name="settings" value="\${formInstance.settings}" /]
							  [@g.hiddenField name="id" value="\${formInstance.id}" /]
							  [@g.hiddenField name="version" value="\${formInstance.version}" /]
						  	  [@g.hiddenField name="extraParameter" value="" /]
						${BUILDER_PANEL}
						  <div class="action">
							  [@g.actionSubmit class="button button-green" action="update" style="width:120px;" value="\${g.message({'code': 'default.button.update.label', 'default': 'Update'})}" /]
							  [@g.actionSubmit class="button button-red" action="delete" style="width:120px;" value="\${g.message({'code': 'default.button.delete.label', 'default': 'Delete'})}" onclick="return confirmMessage('\${flash.deletedefaultmessage}');"/]
							  [@g.actionSubmit class="button button-blue" action="list" style="width:120px;" value="\${g.message({'code': 'default.button.list.label', 'default': 'List'})}" /]
							</div>
							<style id="FormStyles">\${flash.formCSS}</style>
							<script id="FormJS">\${flash.formJS}</script>
							[/@g.form]
						</div>
			</body>
		</html>
"""
	
  static final String FV_CREATE_VIEW = """\
  [#ftl/]
	<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<meta name="layout" content="${VIEWER_LAYOUT}" />
			<title>[@g.message code="formViewer.create.label" args=[flash.formName] /]</title>
			<meta name="description" content="\${flash.encodedDescription}" />
  			<style>
  				.formBody{
  					width:\${flash.formWidth};
  				}
  			</style>
		</head>
		<body>
  		<script type="text/javascript">
				function saveUserDetails(){
  					try{
  						@widthSetterScript
  					}catch(e){}
  					try{
				       \$.ajax({
					  		type:"post",
					  		url: "\${g.createLink({'controller':'PF','action': 'saveFormViewUser'})}?formId=\${formInstance.id}",
							  success: function(){
								  //on successfully entry
							  },
						      failure : function(){
						    	  //on entry failure
							   }
						});
  					}catch(e){}
				}

				\$(document).ready(function(){saveUserDetails();});
			</script>
  			@styleBody
		<div class="container">
			<div class="body \${flash.panelClass}" style="\${flash.bodyStyles}">
  				<div class="formBodyStyle">
			  		<div class="formHeading\${flash.formHeadingHorizontalAlign}" style="\${flash.headingStyle}"><div class="headingContainer"><div class="formHeadingStyle">\${flash.formHeading}</div><p id="formDescription" style="\${flash.descriptionStyle}"><div class="formDescriptionStyle">\${flash.description}</div></p></div></div>
					[#if flash.message?exists]
					  <div class="message">\${flash.message}</div>
					[/#if]
	  				[#if domainInstance.errors?exists]
					  @FIELD_ERRORS
					[/#if]
	  				<div id="ruleErrors" class="errors" style="display:none;"></div>
				  	<form action="\${flash.formAction}" method="post" class="uniForm" enctype="multipart/form-data" onsubmit="return oneAppValidate();">
				        <div class="formControls">
				         @FIELDS
				        </div>
		  				[@g.hiddenField name="pfid" value="@pfid" /]
		  				[@g.hiddenField name="pfii" value="@pfii" /]
		  				[@g.hiddenField name="pffn" value="@pffn" /]
					    [@g.hiddenField name="formId" value="@aformId" /]
		  				[@g.hiddenField name="location" value="" /]		
  						[@g.hiddenField name="subFormid" value="null" /]
					    [@g.hiddenField name="subFormfn" value="null" /]
					<style id="FormStyles">\${flash.formCSS}</style>
					<script id="FormJS">\${flash.formJS}</script>
					</form>
  				</div>
			</div>
		</div>
		</body>
	</html>
	"""
  
	static final String FV_EDIT_VIEW = """\
		[#ftl/]
		<html>
			<head>
				<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				<meta name="layout" content="${VIEWER_LAYOUT}" />
				<title>[@g.message code="formViewer.edit.label" args=[flash.formName] /]</title>
				<meta name="description" content="\${flash.encodedDescription}" />
	  			<style>
	  				.formBody{
	  					width:\${flash.formWidth};
	  				}
	  			</style>
			</head>
			<body>
				<script type="text/javascript">
					\$(document).ready(function(){
	  					try{
	  						@widthSetterScript
	  					}catch(e){}
	  				});
				</script>
				@styleBody
				<content tag="nav">
				   <div class="title"><h1>[@g.message code="default.edit.label" args=[flash.formName] /]</h1></div>
				   <span class="menuButton"><a class="list" href="\${g.createLink({'action': 'list'})}?formId=\${formInstance.id}">[@g.message code="default.list.label" args=[flash.formName] /]</a></span>
				   <span class="menuButton"><a class="create" href="\${g.createLink({'action': 'create'})}?formId=\${formInstance.id}">[@g.message code="default.create.label" args=[flash.formName] /]</a></span>
				</content>
				<div class="body \${flash.panelClass}" style="\${flash.bodyStyles}">
					<div class="formBodyStyle">
				  		<div class="formHeading\${flash.formHeadingHorizontalAlign}" style="\${flash.headingStyle}"><div class="headingContainer">\${flash.formHeading}<p id="formDescription" style="\${flash.descriptionStyle}">\${flash.description}</p></div></div>
						[#if flash.message?exists]
						  <div class="message">\${flash.message}</div>
						[/#if]
						[#if domainInstance.errors?exists]
							  @FIELD_ERRORS
						[/#if]
						<div id="ruleErrors" class="errors" style="display:none;"></div>
				  		[@g.form method="post" class="uniForm" enctype="multipart/form-data" onsubmit="return oneAppValidate();"]
						    <div class="formControls">
						     @FIELDS
						    </div>
			  				[@g.hiddenField name="pfid" value="@pfid" /]
			  				[@g.hiddenField name="pfii" value="@pfii" /]
			  				[@g.hiddenField name="pffn" value="@pffn" /]
						    [@g.hiddenField name="formId" value="@aformId" /]
						    [@g.hiddenField name="id" value="\${domainInstance.id}" /]
						    [@g.hiddenField name="version" value="\${domainInstance.version}" /]	
							[@g.hiddenField name="subFormid" value="null" /]
						    [@g.hiddenField name="subFormfn" value="null" /]			    				  
						<style id="FormStyles">\${flash.formCSS}</style>
						<script id="FormJS">\${flash.formJS}</script>
						[/@g.form]
					</div>
				</div>
			</body>
		</html>
		"""
	
  static final String FV_LIST_VIEW = """\
	[#ftl/]  
  <html>
    <head>
        <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
        <meta name="layout" content="${VIEWER_LAYOUT}" />
        <title>[@g.message code="default.list.label" args=[flash.formName] /]</title>
        [@jqDT.resources jqueryUi="true" /]
      <script type="text/javascript">
                \$(function() {
                      \$('.list table').dataTable({
                          sScrollY: '50%',
                          bProcessing: true,
                          bServerSide: true,
                          sAjaxSource: './listData?formId=\${formInstance.id}',
                          bJQueryUI: true,
                          "sPaginationType": "full_numbers",
                          "bScrollCollapse": true            
                       });
                       \$('.list tbody').click(function(event) {
                         var id = \$('td:first', event.target.parentNode).text();
                         window.location = "./show/" + id + '?formId=\${formInstance.id}';
                       });
                   });       
        </script>
    </head>
    <body>
			<content tag="nav">
			   <div class="title"><h1>[@g.message code="default.list.label" args=[flash.formName] /]</h1></div>
			   <span class="menuButton"><a class="create" href="\${g.createLink({'action': 'create'})}?formId=\${formInstance.id}">[@g.message code="default.create.label" args=[flash.formName] /]</a></span>
			</content>
      <div class="body">
			[#if flash.message?exists]
			  <div class="message">\${flash.message}</div>
			[/#if]
        <div class="list">
           <table>
						  <thead>
							   <tr>
								  @FIELDS_HEADER
							   </tr>
							</thead>
							<tbody></tbody>
							<tfoot>
							   <tr>
								  @FIELDS_HEADER
							   </tr>
							</tfoot>
           </table>
        </div>
    </div>
    </body>
</html>  
  """
  static final String FV_SHOW_VIEW = """\
	  [#ftl/]
	  <html>
		  <head>
			  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			  <meta name="layout" content="${VIEWER_LAYOUT}" />
			  <title>[@g.message code="default.show.label" args=[flash.formName] /]</title>
			  <script type="text/javascript">
			  \$(function() {
				 \$('div.buttons').children().button();
					});
			  </script>
  			  <style>
  					.formBody {
					    margin: 5px auto;
  						width:640px;
					}
  			  </style>
		  </head>
		  <body>
			  <content tag="nav">
				 <div class="title"><h1>[@g.message code="default.show.label" args=[flash.formName] /]</h1></div>
				 <span class="menuButton"><a class="home" href="\${g.createLink({'uri': '/'})}">\${g.message({'code':'default.home.label'})}</a></span>
				 <!--span class="menuButton"><a class="list" href="\${g.createLink({'action': 'list'})}?formId=\${formInstance.id}">[@g.message code="default.list.label" args=[flash.formName] /]</a></span>
				 <span class="menuButton"><a class="create" href="\${g.createLink({'action': 'create'})}?formId=\${formInstance.id}">[@g.message code="default.create.label" args=[flash.formName] /]</a></span-->
			  </content>
	      <div class="body" style="\${flash.bodyStyles}">
	        <div class="formHeading\${flash.formHeadingHorizontalAlign}">\${flash.formHeading}</div>
				[#if flash.message?exists]
				  <div class="message">\${flash.message}</div>
				[/#if]
				[@g.form class="uniForm"]
			        <div class="formControls">
			         @FIELDS
			        </div>
					
				[/@g.form]
				</div>
		  </body>
	  </html>
	 """
  
	 static final String FV_SHOW_VIEW_ONLY_MESSAGE = """\
		 <html>
			 <head>
				 <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
				 <meta name="layout" content="${VIEWER_LAYOUT}" />
				 <title>Thank you</title>
			 </head>
			 <body>
				<div class="body" style="\${flash.bodyStyles}">
	 			   <div class="formViewerMessageOuterDiv">
	 					<div class="message">@FLASH_MESSAGE. (@FOOTER_TEXT)</div>
	 				</div>
	 			</div>
			 </body>
		 </html>
		"""

static final String FVIPAD_CREATE_VIEW = """\
	[#ftl/]
	  <html>
		  <head>
			  <meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			  <meta name="layout" content="${IPAD_LAYOUT}" />
			  <title>[@g.message code="formViewer.create.label" args=[flash.formName] /]</title>
			  <meta name="description" content="\${flash.encodedDescription}" />
				<style>
					.formBody{
						width:\${flash.formWidth};
					}
				</style>
		  </head>
		  <body>
			<script type="text/javascript">
				  function saveUserDetails(){
						try{
							@widthSetterScript
						}catch(e){}
						try{
						 \$.ajax({
								type:"post",
								url: "\${g.createLink({'controller':'PF','action': 'saveFormViewUser'})}?formId=\${formInstance.id}",
								success: function(){
									//on successfully entry
								},
								failure : function(){
									//on entry failure
								 }
						  });
						}catch(e){}
				  }
  
				  \$(document).ready(function(){saveUserDetails();});
			  </script>
				@styleBody
		  <div class="container">
			  <div class="body \${flash.panelClass}" style="\${flash.bodyStyles}">
					<div class="formBodyStyle">
	<div class="formHeading\${flash.formHeadingHorizontalAlign}" style="\${flash.headingStyle}"><div class="headingContainer"><div class="formHeadingStyle">\${flash.formHeading}</div><p id="formDescription" style="\${flash.descriptionStyle}"><div class="formDescriptionStyle">\${flash.description}</div></p></div></div>
					  [#if flash.message?exists]
						<div class="message">\${flash.message}</div>
					  [/#if]
						[#if domainInstance.errors?exists]
						@FIELD_ERRORS
					  [/#if]
						<div id="ruleErrors" class="errors" style="display:none;"></div>
						<form action="\${flash.formAction}" method="post" class="uniForm" enctype="multipart/form-data" onsubmit="return oneAppValidate();">
						  <div class="formControls">
						   @FIELDS
						  </div>
							[@g.hiddenField name="pfid" value="@pfid" /]
							[@g.hiddenField name="pfii" value="@pfii" /]
							[@g.hiddenField name="pffn" value="@pffn" /]
					   	    [@g.hiddenField name="formId" value="@aformId" /]
							[@g.hiddenField name="location" value="" /]
							[@g.hiddenField name="subFormid" value="null" /]
						    [@g.hiddenField name="subFormfn" value="null" /]
					  <style id="FormStyles">\${flash.formCSS}</style>
					  <script id="FormJS">\${flash.formJS}</script>
					  </form>
					</div>
			  </div>
		  </div>
		  </body>
	  </html>
	  """
static final String FVIPAD_EDIT_VIEW = """\
	[#ftl/]
	<html>
		<head>
			<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />
			<meta name="layout" content="${IPAD_LAYOUT}" />
			<title>[@g.message code="formViewer.edit.label" args=[flash.formName] /]</title>
			<meta name="description" content="\${flash.encodedDescription}" />
			  <style>
				  .formBody{
					  width:\${flash.formWidth};
				  }
			  </style>
		</head>
		<body>
			<script type="text/javascript">
				\$(document).ready(function(){
					  try{
						  @widthSetterScript
					  }catch(e){}
				  });
			</script>
			@styleBody
			<content tag="nav">
			   <div class="title"><h1>[@g.message code="default.edit.label" args=[flash.formName] /]</h1></div>
			   <span class="menuButton"><a class="list" href="\${g.createLink({'action': 'list'})}?formId=\${formInstance.id}">[@g.message code="default.list.label" args=[flash.formName] /]</a></span>
			   <span class="menuButton"><a class="create" href="\${g.createLink({'action': 'create'})}?formId=\${formInstance.id}">[@g.message code="default.create.label" args=[flash.formName] /]</a></span>
			</content>
			<div class="body \${flash.panelClass}" style="\${flash.bodyStyles}">
				<div class="formBodyStyle">
					  <div class="formHeading\${flash.formHeadingHorizontalAlign}" style="\${flash.headingStyle}"><div class="headingContainer">\${flash.formHeading}<p id="formDescription" style="\${flash.descriptionStyle}">\${flash.description}</p></div></div>
					[#if flash.message?exists]
					  <div class="message">\${flash.message}</div>
					[/#if]
					[#if domainInstance.errors?exists]
						  @FIELD_ERRORS
					[/#if]
					<div id="ruleErrors" class="errors" style="display:none;"></div>
					  [@g.form method="post" class="uniForm" enctype="multipart/form-data" onsubmit="return oneAppValidate();"]
						<div class="formControls">
						 @FIELDS
						</div>
						  [@g.hiddenField name="pfid" value="@pfid" /]
						  [@g.hiddenField name="pfii" value="@pfii" /]
						  [@g.hiddenField name="pffn" value="@pffn" /]
						  [@g.hiddenField name="formId" value="@aformId" /]
						  [@g.hiddenField name="id" value="\${domainInstance.id}" /]
						  [@g.hiddenField name="version" value="\${domainInstance.version}" /]
						  [@g.hiddenField name="subFormid" value="null" /]
						  [@g.hiddenField name="subFormfn" value="null" /]
					<style id="FormStyles">\${flash.formCSS}</style>
					<script id="FormJS">\${flash.formJS}</script>
					[/@g.form]
				</div>
			</div>
		</body>
	</html>
	"""
	  }	