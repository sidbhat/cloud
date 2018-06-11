package org.grails.formbuilder.widget

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.grails.formbuilder.FormBuilderConstants
import org.grails.formbuilder.FormDesignerView

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class ScaleRating extends Widget {
 String getFieldStyles(Object settings, Locale locale) {
	 String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
	 String display = settings.hideFromUser && !isRoleGranted?'display:none; ':''
	 String fontFamily = settings."${language}".styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${settings."${language}".styles.fontFamily}; "
	 String labelColor = settings.styles.label.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: ${((settings.styles.label.color+'').indexOf('rgb')>-1?'':'#')+settings.styles.label.color}; "
	 String labelBackgroundColor = settings.styles.label.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: ${((settings.styles.label.backgroundColor+'').indexOf('rgb')>-1?'':'#')+settings.styles.label.backgroundColor}; "
	 return "${fontFamily}${labelColor}${labelBackgroundColor}${display}"
  }

  public String getToCamel(String string) {
    String result = "";
    for (int i = 0; i < string.length(); i++) {

      if (i == 0) {
        result += string[i].toLowerCase()
      } else {
        result += string[i]
      }
    }
    return result
  }
 String getWidgetTemplateText(String name, Object settings,  
	                            Locale locale, FormDesignerView formDesignerView, Object domainInstance) {
    return widgetTemplateText(name, settings, locale, formDesignerView, false, domainInstance)
   }
								
 private String widgetTemplateText(String name, Object settings, 
	                            Locale locale, FormDesignerView formDesignerView, boolean readOnly, Object domainInstance = null) {
	  String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
	  def styles = settings."${language}".styles
	  String fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal'
      String fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal'
      String textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none'
	  String fontFamily = styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${styles.fontFamily}; "
	  String fontSize = styles.fontSize == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-size: ${styles.fontSize}px; "  
	  String valueColor = settings.styles.value.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: ${((settings.styles.value.color+'').indexOf('rgb')>-1?'':'#')+settings.styles.value.color}; "
	  String valueBackgroundColor = settings.styles.value.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: ${((settings.styles.value.backgroundColor+'').indexOf('rgb')>-1?'':'#')+settings.styles.value.backgroundColor}; "
	  String descriptionColor = settings.styles.description.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: ${((settings.styles.description.color+'').indexOf('rgb')>-1?'':'#')+settings.styles.description.color}; "
	  String descriptionBackgroundColor = settings.styles.description.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: ${((settings.styles.description.backgroundColor+'').indexOf('rgb')>-1?'':'#')+settings.styles.description.backgroundColor}; "
    String textField
	  String value = settings."${language}".value
    String lab = name//settings."${language}".label
   /* String tempName = ""
    def ragex = """. , "/' ? ! ; : # % & ( ) * + - / < > = @ [ ]  ^ _ { } | ~ """
    for (int i = 0; i < lab.length(); i++) {
      String s = lab.charAt(i)
      if (ragex.contains(s)) {
        tempName += ""
      } else {
        tempName += s
      }
    }
   lab=getToCamel(tempName)*/
	
	  if (!readOnly) {
		  textField ="""<div> \
					<div>\
						<div>\
							<div class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}">\
								<label style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}"><span
								style="text-decoration: ${textDecoration};line-height:16px;">${settings."${language}".label?.encodeAsHTML()}</span><em>&nbsp;${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</em>\
								</label>\
							</div>\
        					<div class="${Widget.fullLength?'customLengthField':'fullLengthField'}">\
					        	<table class="scaleRateTable" cellspacing="0" style="width:100%;border-collapse:collapse;background-color:rgba(238, 238, 238, 0.4);color:inherit;${settings.moodRate?"display:none;":""}margin:0;">\
						        	<thead>\
						        		<tr>\
						        			<th style="border: 1px solid #CCC;text-align:center;padding:5px;width:32%;">\
						        				Very Satisfied\
						        			</th>\
        									<th style="border: 1px solid #CCC;text-align:center;padding:5px;">\
						        				Satisfied\
						        			</th>\
											<th style="border: 1px solid #CCC;text-align:center;padding:5px;">\
						        				Neutral\
						        			</th>\
        									<th style="border: 1px solid #CCC;text-align:center;padding:5px;">\
						        				Dissatisfied\
						        			</th>\
						        		</tr>\
						        	</thead>\
						        	<tr>\
						        		<td style="border: 1px solid #CCC;text-align:center;font-weight:bold;">\
						        			<input type="radio" name="${lab}" value="VerySatisfied" ${domainInstance?."${name}"=="VerySatisfied"?("checked=checked"):""}/><br>1\
						        		</td>\
						        		<td style="border: 1px solid #CCC;text-align:center;font-weight:bold;">\
						        			<input type="radio" name="${lab}" value="Satisfied" ${domainInstance?."${name}"=="Satisfied"?("checked=checked"):""}/><br>2\
						        		</td>\
						        		<td style="border: 1px solid #CCC;text-align:center;font-weight:bold;">\
						        			<input type="radio" name="${lab}" value="Neutral" ${domainInstance?."${name}"=="Neutral"?("checked=checked"):""}/><br>3\
						        		</td>\
						        		<td style="border: 1px solid #CCC;text-align:center;font-weight:bold;">\
						        			<input type="radio" name="${lab}" value="Dissatisfied" ${domainInstance?."${name}"=="Dissatisfied"?("checked=checked"):""}/><br>4\
						        		</td>\
						        	</tr>\
						        </table>\
		  						<div class="slider" id="${lab}" style="${settings.moodRate?"":"display:none;"}"></div>\
		  						<script>\$(".slider[id='${lab}']").slider({value:setMood("${domainInstance?."${name}"}"), min: 25, max: 100,create: function(event,ui){\$('a',\$(this)).attr('style', 'background-image: url(${ConfigurationHolder.config.grails.serverURL}/plugins/form-builder-0.1/images/face-'+Math.round(setMood("${domainInstance?."${name}"}")/25).toFixed(0)+'.png) !important;').css('left',setMood("${domainInstance?."${name}"}")+'%');},slide: function( event, ui ) {moodToScale(Math.round(ui.value/25).toFixed(0),'${lab}');\$('a',\$(this)).attr('style', 'background-image: url(${ConfigurationHolder.config.grails.serverURL}/plugins/form-builder-0.1/images/face-'+Math.round(ui.value/25).toFixed(0)+'.png) !important;');}});</script>\
        					</div>\
						</div>\
					</div> \
		      </div>"""
	  } else {
		  textField = """\
		  <div>\
                  <label><em></em><span>${settings."${language}".label?.encodeAsHTML()}</span><span style="color:red;">${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</span></label>
				  <span class="textOutput" style="position:relative;left:50px;">\
                      ${domainInstance?."${name}"} \
		  		  </span>
		      </div>"""
	  }
	  return """<div>
	      ${textField}
	    <p class="formHint" style="${descriptionColor}${descriptionBackgroundColor}">${settings."${language}".description?.encodeAsHTML()}</p></div>"""
  }
								
 String getFieldConstraints(Object settings) {
	 if (settings.restriction == 'no') {
		 return FormBuilderConstants.EMPTY_STRING
	 } else {
	   return "${settings.restriction}:true"
	 }
 }
 								
 String getWidgetReadOnlyTemplateText(String name, Object settings,
									                    Locale locale, FormDesignerView formDesignerView, Object domainInstance) {														
    return widgetTemplateText(name, settings, locale, formDesignerView, true, domainInstance)
  }
}
