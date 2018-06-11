package org.grails.formbuilder.widget

import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class NameTypeField extends Widget {
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
	  String textField=""""""
	  String pre=(settings?.showPrefix)?'showthis':'hidethis'
	  String mid=(settings?.showMiddleName)?'showthis':'hidethis'
	  String value = formDesignerView ? settings."${language}".value : "\${domainInstance.${name}!''}"
 	  def mapValue=[:]
	  if(!formDesignerView)
		  try{ 
	  		 mapValue = grails.converters.JSON.parse((domainInstance."${name}").toString()) 
		  }catch(Exception e){}
	
	  if (!readOnly) {
		  if(!isEmailEmbedFrom || pre.equals('showthis')){
			  textField = """\
					  <div class="nameFieldControl">\
							<div class="nameFieldSmallWrapper nameFieldPrefix ${pre}">\
								${isEmailEmbedFrom?'<label>Prefix</label ><br/>':''}<input type="text" style="${isEmailEmbedFrom?'width:50%;margin-bottom:3px;':''}${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
								text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}" value="${!formDesignerView && mapValue?(mapValue?.pre):''}" name="${name}pre"  id="${name}pre"  class="nameFieldInputPrefix">${isEmailEmbedFrom?'':'<label>Prefix</label >'}\
							</div>"""
		  }
	  		 textField+= """\
						  <div class="nameFieldSmallWrapper nameFieldName nameFieldFn">\
								${isEmailEmbedFrom?'<label>First Name</label ><br/>':''}<input type="text" style="${isEmailEmbedFrom?'width:50%;margin-bottom:3px;':''}${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
								text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}"  value="${!formDesignerView && mapValue?(mapValue?.fn):''}" name="${name}fn"  id="${name}fn"class="nameFieldInput" >${isEmailEmbedFrom?'':'<label>First Name</label >'}\
							</div>"""
		 if(!isEmailEmbedFrom || mid.equals('showthis')){
			 textField+= """\
         					<div class="nameFieldSmallWrapper nameFieldName nameFieldMn ${mid}">\
								${isEmailEmbedFrom?'<label>Middle Name</label ><br/>':''}<input type="text" style="${isEmailEmbedFrom?'width:50%;margin-bottom:3px;':''}${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
								text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}" value="${!formDesignerView && mapValue?(mapValue?.mn):''}" name="${name}mn"  id="${name}mn"class="nameFieldInput" >${isEmailEmbedFrom?'':'<label>Middle Name</label >'}\
							</div>"""
		  }
		 	textField+= """\
				 			<div class="nameFieldSmallWrapper nameFieldName nameFieldLn">\
								${isEmailEmbedFrom?'<label>Last Name</label ><br/>':''}<input type="text" style="${isEmailEmbedFrom?'width:50%;margin-bottom:3px;':''}${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
								text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}" value="${!formDesignerView && mapValue?(mapValue?.ln):''}" name="${name}ln"  id="${name}ln" class="nameFieldInput" >${isEmailEmbedFrom?'':'<label>Last Name</label >'}\
							</div>\
			 			<p style="color: #777777; background-color: #transparent;" class="formHint">${settings?."${language}"?.description?.encodeAsHTML()}</p>\
		        	 </div>\
		     		"""
	  } else {
		  def nameDetails = ""
		  boolean	prex=(settings?.showPrefix)
		  boolean midx=(settings?.showMiddleName)
		  if(prex)
			  nameDetails += mapValue?."pre"?(mapValue."pre"):""
		  nameDetails += mapValue?."fn"?(" "+mapValue."fn"):""
		  if(midx)
			  nameDetails += mapValue?."mn"?(" "+mapValue."mn"):""
		  nameDetails += mapValue?."ln"?(" "+mapValue."ln"):""
		  textField = """\
		  <span class="textOutput" style="position:relative;left:50px;${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		  text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}">
		  ${nameDetails}
		  </span>"""
	  }
	  return """<div><div 
		  	class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}"><label for="${name}" style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}"><span
			style="text-decoration: ${textDecoration};line-height:16px;">${settings."${language}".label.encodeAsHTML()}</span><em>&nbsp;${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</em></label></div><div 
		  	class="${Widget.fullLength?'customLengthField':'fullLengthField'} ${settings.fieldSize?:'mClass'}">${textField}</div></div>"""
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