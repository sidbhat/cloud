package org.grails.formbuilder.widget

import java.text.DateFormat;
import java.text.SimpleDateFormat;

import org.grails.formbuilder.Form;
import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants
import org.grails.formbuilder.UniqueFormEntry;

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class UniqueId extends Widget {
 String getFieldStyles(Object settings, Locale locale) {
	 return ""
  }
 
 String getWidgetTemplateText(String name, Object settings,  
	                            Locale locale, FormDesignerView formDesignerView, Object domainInstance) {
    return ""
   }
								
 public String getUniqueTemplateText(Object domainInstance = null,Form form=null,def readOnly) {
	String fontWeight = 'bold'
	String textField
	String value = UniqueFormEntry.findByFormIdAndInstanceId(form.id,domainInstance.id)?.uniqueId
	def lastUpdated = domainInstance.last_updated
	def onFormatter = new SimpleDateFormat("MM/dd/yyyy")
	def atFormatter = new SimpleDateFormat("hh:mm: a zzz")
	if (!readOnly) {
		textField = """<input type="text" style="${isEmailEmbedFrom?'width:50%;':''}font-weight: ${fontWeight};background:#DDD;" name="uniqueID" id="uniqueID" class="textInput" disabled="disabled" value="${value?:''}"
			/><div style="width:100%;position:relative;float:left;">${lastUpdated?('Updated on: '+onFormatter.format(lastUpdated)+' at '+atFormatter.format(lastUpdated)):''}</div>"""
	} else {
		textField = """\
		  <span class="textOutput" style="position:relative;left:50px;font-weight: ${fontWeight};">
		  ${value?:''}<br>${lastUpdated?('Updated on: '+onFormatter.format(lastUpdated)+' at '+atFormatter.format(lastUpdated)):''}
		  </span>"""
	}
	return """<div class="ctrlHolder" rel="-1"><div 
		  	class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}"><label style="font-weight: ${fontWeight};"><span
			style="line-height:16px;">Unique ID</span></label></div><div 
		  	class="${Widget.fullLength?'customLengthField':'fullLengthField'} mClass"><p 
			 class="formHint"></p>${textField}</div></div>"""
  }
								
 String getFieldConstraints(Object settings) {
	return FormBuilderConstants.EMPTY_STRING
 }
 								
 String getWidgetReadOnlyTemplateText(String name, Object settings,
									                    Locale locale, FormDesignerView formDesignerView, Object domainInstance) {														
    return ""
  }
}
