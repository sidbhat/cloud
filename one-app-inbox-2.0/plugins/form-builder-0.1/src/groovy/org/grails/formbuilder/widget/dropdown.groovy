package org.grails.formbuilder.widget

import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class dropdown extends Widget {
  String getFieldStyles(Object settings, Locale locale) {
	String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
    String display = settings.hideFromUser && !isRoleGranted?'display:none; ':''
    String fontFamily = settings."${language}".styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${settings."${language}".styles.fontFamily}; "
    String labelColor = settings.styles.label.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: ${((settings.styles.label.color+'').indexOf('rgb')>-1?'':'#')+settings.styles.label.color}; "
	 String labelBackgroundColor = settings.styles.label.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: ${((settings.styles.label.backgroundColor+'').indexOf('rgb')>-1?'':'#')+settings.styles.label.backgroundColor}; "
    return "${fontFamily}${labelColor}${labelBackgroundColor}${display}"
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
    def value = settings."${language}".value
	String ddValue = ""
	String ddValueScript = ""
	if(!formDesignerView)
		try{
		  ddValue = domainInstance."${name}"
		  ddValueScript = '<script>$("#'+name+' option").each(function(){$(this).val($(this).text());});'
		  ddValueScript += '$("#'+name+'").prepend("<option>Please select one</option>");'
		  ddValueScript += '$("#'+name+'").val("'+(ddValue?ddValue.encodeAsJavaScript():'')+'");</script>'
		}catch(Exception e){
	  }

    if (!readOnly) {
      String opt = ""
      value.each{l->
        opt+="<option>"+l.encodeAsHTML()+"</option>"
      }
      textField = """ <select name="${name}" id="${name}" class="textInput"
		    style="${isEmailEmbedFrom?'width:50%;':''}${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		    text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}">${opt}</select>"""+ddValueScript

    } else {
      textField = """\
		  <span class="textOutput" style="position:relative;left:50px;${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		  text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}">
		  ${ddValue}
		  </span>"""
    }
    return """<div><div 
		  	class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}"><label for="${name}" style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}"><span
			style="text-decoration: ${textDecoration};line-height:16px;">${settings."${language}".label.encodeAsHTML()}</span><em>&nbsp;${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</em></label></div><div 
		  	class="${Widget.fullLength?'customLengthField':'fullLengthField'} ${settings.fieldSize?:'mClass'}">${textField}<p
			  class="formHint" style="${descriptionColor}${descriptionBackgroundColor}">${settings."${language}".description.encodeAsHTML()}</p></div></div>"""
  }

  String getFieldConstraints(Object settings) {
    if (settings.restriction == 'no') {
      return FormBuilderConstants.EMPTY_STRING
    } else {
      return "${settings.restriction}:true"//
    }
  }

  String getWidgetReadOnlyTemplateText(String name, Object settings,
                                       Locale locale, FormDesignerView formDesignerView, Object domainInstance) {
    return widgetTemplateText(name, settings, locale, formDesignerView, true, domainInstance)
  }
}
