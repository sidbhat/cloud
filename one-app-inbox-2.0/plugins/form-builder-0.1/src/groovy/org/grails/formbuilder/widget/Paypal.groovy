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
class Paypal extends Widget {
	
	String getFieldStyles(Object settings, Locale locale) {
		String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		String display = settings.hideFromUser && !isRoleGranted?'display:none; ':''
		String fontFamily = settings."${language}".styles.fontFamily == 'default' ? FormBuilderConstants.EMPTY_STRING : "font-family: ${settings."${language}".styles.fontFamily}; "
		String labelColor = settings.styles.label.color == 'default' ? FormBuilderConstants.EMPTY_STRING : "color: ${((settings.styles.label.color+'').indexOf('rgb')>-1?'':'#')+settings.styles.label.color}; "
	 String labelBackgroundColor = settings.styles.label.backgroundColor == 'default' ? FormBuilderConstants.EMPTY_STRING : "background-color: ${((settings.styles.label.backgroundColor+'').indexOf('rgb')>-1?'':'#')+settings.styles.label.backgroundColor}; "
		return "${fontFamily}${labelColor}${labelBackgroundColor}${display}"
	}
 
	String getWidgetTemplateText(String name, Object settings, Locale locale, FormDesignerView formDesignerView, Object domainInstance) {
		return widgetTemplateText(name, settings, locale, formDesignerView, false, domainInstance)
	}
								
	private String widgetTemplateText(String name, Object settings,Locale locale, FormDesignerView formDesignerView, boolean readOnly, Object domainInstance = null) {
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
		Form subFormInstance
		def config = ConfigurationHolder.config
		def subFormFieldTypes = config.subFieldTypes
		SimpleDateFormat dateFormatter = new SimpleDateFormat(config.format.date)
		def serverURL = config.grails.serverURL
		
		String stringValueScript
		def counterMap = [counter:4];
		def headerRow = """<tr class="itmHeader"><td>Preview</td><td>Product Name &amp; Description</td><td>Price</td><td>Quantity</td><td>Amount</td></tr>"""
		if(formDesignerView){
			stringValueScript = """<div><div id="tableDiv">"""
			if(settings.itemForm){
				stringValueScript += """<table class="itmTable">
					<tbody>${headerRow}
						<tr class="itm">
							<td><img src="/form-builder/images/previewImage.png" style="width: 80px;"></td>
							<td><b>Item Name</b><br>Item description here.</td><td>&nbsp;</td><td>&nbsp;</td><td>&nbsp;</td>
						</tr>
					</tbody>
				</table>"""
			}
			stringValueScript += """</div><b>Total Payable Amount: <span class="currency">${config.formBuilder.currencies[settings.curr]}</span>0</b></div>\
        		<div style="border:0;padding:5px 0 0 0;margin:0;background:none;">\
        			<div class="pay_now_button"></div>\
        		</div>"""
		}else{
			def tableHTML = ""
			if(settings.itemForm){
				tableHTML = """<table class="itmTable">
							<tbody>${headerRow}@trsHTMLHere</tbody>
						</table>"""
			}else{
				if(settings.iaf){
					tableHTML = """<script id="#${name}scr">\$(document).ready(function(){\$("#${settings.iaf}").change(function(){updatePPA(this);}).trigger("change");});</script>"""
				}
			}
			stringValueScript = """<div>${tableHTML}@totalAndPaymentStatus</div>\
        		<div style="border:0;padding:5px 0 0 0;margin:0;background:none;">\
        			@payNowButton\
        		</div>"""
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
