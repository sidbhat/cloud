package org.grails.formbuilder.widget

import java.text.SimpleDateFormat;

import grails.converters.JSON;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.grails.formbuilder.Field;
import org.grails.formbuilder.Form;
import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class PageBreak extends Widget {
	
	String getFieldStyles(Object settings, Locale locale) {
		String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		String display = settings.hideFromUser && !isRoleGranted?'display:none; ':''
		String fontFamily = settings."${language}".styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${settings."${language}".styles.fontFamily}; "
		String labelColor = settings.styles.label.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: #${settings.styles.label.color}; "
		String labelBackgroundColor = settings.styles.label.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: #${settings.styles.label.backgroundColor}; "
		return "${fontFamily}${labelColor}${labelBackgroundColor}${display}"
	}
 
	String getWidgetTemplateText(String name, Object settings, Locale locale, FormDesignerView formDesignerView, Object domainInstance) {
		return widgetTemplateText(name, settings, locale, formDesignerView, false, domainInstance)
	}
								
	private String widgetTemplateText(String name, Object settings,Locale locale, FormDesignerView formDesignerView, boolean readOnly, Object domainInstance = null) {
		domainInstance = domainInstanceForSubForm
		String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		def styles = settings."${language}".styles
		String fontWeight = styles.fontStyles[0] == 1 ? 'bold' : 'normal'
		String fontStyle = styles.fontStyles[1] == 1 ? 'italic' : 'normal'
		String textDecoration = styles.fontStyles[2] == 1 ? 'underline' : 'none'
		String fontFamily = styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${styles.fontFamily}; "
		String fontSize = styles.fontSize == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-size: ${styles.fontSize}px; "  
		String valueColor = settings.styles.value.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: #${settings.styles.value.color}; "
		String valueBackgroundColor = settings.styles.value.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: #${settings.styles.value.backgroundColor}; "
		String descriptionColor = settings.styles.description.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: #${settings.styles.description.color}; "
		String descriptionBackgroundColor = settings.styles.description.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: #${settings.styles.description.backgroundColor}; "
		String textField
		Form subFormInstance
		def subFormFieldTypes = ConfigurationHolder.config.subFieldTypes
		SimpleDateFormat dateFormatter = new SimpleDateFormat(ConfigurationHolder.config.format.date)
		def serverURL = ConfigurationHolder.config.grails.serverURL
		
		String stringValueScript
		def counterMap = [counter:4];
		if(formDesignerView){
			stringValueScript = """<div class="buttons pageButtons" style="border:0;padding: 5px 1em;margin:0;"><input \
						type="button" class="button button-gray" value="Back" style="width:64px;" \
					/>&nbsp;<input \
						type="button" class="button button-gray" value="Next" style="width:64px;" 
					/> Page m of n</div>"""
		}else{
			stringValueScript = """<div class="buttons pageButtons" style="border:0;padding: 5px 1em;margin:0;"><div
					class="backButton" style="display:inline-block;"><input type="button" name="${name}" class="button button-gray" value="Back" style="width:64px;${currentPageBreak<=1?"display:none":""}" /></div><div \
					class="nextButton" style="display:inline-block;">&nbsp;<input \
						type="button" name="${name}" class="button button-gray" value="Next" style="width:64px;"\
					/> Page ${currentPageBreak++} of ${totalPageBreak}</div></div></div></div><div class="formControls" style="display:none;">"""
		}
	
	  if (!readOnly) {
		  textField = stringValueScript
	  } else {
		  textField = ""
	  }
	  return """${textField}"""
	}
								
	String getFieldConstraints(Object settings) {
		if (settings.restriction == 'no') {
			return FormBuilderConstants.EMPTY_STRING
		} else {
			return "${settings.restriction}:true"
		}
	}

	String getWidgetReadOnlyTemplateText(String name, Object settings, Locale locale, FormDesignerView formDesignerView, Object domainInstance) {														
		return widgetTemplateText(name, settings, locale, formDesignerView, true, domainInstance)
	}
}
