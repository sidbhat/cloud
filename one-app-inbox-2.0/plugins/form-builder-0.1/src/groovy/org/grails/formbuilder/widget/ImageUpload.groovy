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
class ImageUpload extends Widget {
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
	  String value
	  if(settings.en.uploadImage){
	  	if(attachments && attachments[0]){
	  	 value = "${ConfigurationHolder.config.grails.serverURL}/preview/formImagePath/${attachments[0]?.id}"
	  	}
	  }else
	  	value = settings."${language}".value
    String lab = settings."${language}".label
    def width = settings."${language}".width
	def height = settings."${language}".height
    String tempName = ""
    def ragex = """. , "/' ? ! ; : # % & ( ) * + - / < > = @ [ ]  ^ _ { } | ~ """
    for (int i = 0; i < lab.length(); i++) {
      String s = lab.charAt(i)
      if (ragex.contains(s)) {
        tempName += ""
      } else {
        tempName += s
      }
    }
   lab=getToCamel(tempName)
	  if (!readOnly) {
		  def x = ""
		  if(formDesignerView){
			  x = """<input type="button" class="button button-green" style="${settings.en.uploadImage?'':'display:none'};" value="Select Image"><input type="file" name="${name}" style="display:none;"/>"""
		  }
		  def clickableURL = ""
		  if(settings.en.clickable){
			  clickableURL = settings.en.clickableURL
			  if(clickableURL?.indexOf("http://")!=0 && clickableURL?.indexOf("https://")!=0){
				  clickableURL = "http://"+clickableURL
			  }
		  }
		  textField = """\
		  <img style="display: none;" class="imgDisplay" userSpecifiedWidth="${width}" userSpecifiedHeight="${height}" id="${name}" src="${value}"
		  />${settings.en.clickable?"<script>\$(document).ready(function(){\$('#${name}').click(function(){OpenNewWindow('${clickableURL}')}).css('cursor','pointer')})</script>":''}<p></p>${x}
		 		  """
	  } else {
     		  textField = """\
		 <img style="display: none;" class="imgDisplay" userSpecifiedWidth="${width}" userSpecifiedHeight="${height}" id="image" src="${value}"/>
         <p></p>
		 """
	  }
	  return """<div><div  class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}"><label for="${name}" style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}">\
		  		<span style="text-decoration: ${textDecoration};line-height:16px;">${settings."${language}".label.encodeAsHTML()}</span></label></div><div  class="${Widget.fullLength?'customLengthField':'fullLengthField'}">\
           ${textField}\
	    <p class="formHint" style="${descriptionColor}${descriptionBackgroundColor}">${settings."${language}".description.encodeAsHTML()}</p></div></div>"""
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
