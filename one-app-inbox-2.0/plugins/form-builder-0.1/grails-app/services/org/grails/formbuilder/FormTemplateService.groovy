
package org.grails.formbuilder

import org.grails.formbuilder.widget.Widget
import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.plugins.springsecurity.SpringSecurityUtils;
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.springframework.web.servlet.support.RequestContextUtils as RCU
import grails.converters.JSON

/**
* Form Viewer Controller for Dynamic Domain Class.
*
* @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
*
* @since 0.1
*/
class FormTemplateService {
	def grailsApplication
	static transactional = false
	
	String getCreateViewTemplate(def request, def flash, Form form) {
		def view = getViewTemplateSource(FormBuilderConstants.FB_CREATE_VIEW, 
			request, flash, form, FormDesignerView.CREATE)
		return view
	}
	
	String getViewTemplateSource(String viewTemplate, def request, def flash, Form form, FormDesignerView formDesignerView) {
		def settings
		Locale locale = new Locale('en')
		//Locale locale = RCU.getLocale(request)
		if (form.settings) {
			settings = JSON.parse(form.settings)
		 }
		flash.language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		if(!flash.language)
		      flash.language='en'
		flash.panelClass = settings?.themeText?:""
		flash.description=(settings?.en?.description)?:""
		setBuilderPanelStyles(flash, settings, locale)
		setFormHeading(flash, settings,flash.language)
		return viewTemplate.replace('@FIELDS', 
			getWidgetsTemplateText(form, locale, formDesignerView))
	}
	
	private setBuilderPanelStyles(def flash, def settings, Locale locale) {
		//println "${settings}"
		//println settings?.formType
		if (settings) {
			flash.builderPanelStyles = "font-family: ${settings."${flash.language}".styles.fontFamily}; " + 
			                           "font-size: ${settings."${flash.language}".styles.fontSize?:13}px; " +
									               "color: #${settings.styles.form?.color?:settings.styles.color}; " +
												         "background-color: #${settings.styles.form?.backgroundColor?:settings.styles.backgroundColor};" + 
												         "form-type: ${settings.formType}"
		
		} else {
		  flash.builderPanelStyles = "font-family: Lucida Sans Unicode,Lucida Grande,sans-serif; font-size: 13px;"
		}
		flash.formCSS = settings?.en?.CSS?:'.formBodyStyle{\nfont-size:13px;\n}\ninput[type="submit"]{\ncolor:white;\n}'
		flash.formJS = settings?.en?.js?:'function beforeSubmit(){\n   //code for validation before save\n   return true;//or false accordingly\n}'
		 // end of setting the form type
	}
	
	private setFormHeading(def flash, def settings,def language) {
		if (settings) {
			String style = "font-weight: ${settings[language]?.styles.fontStyles[0] == 1 ? 'bold' : 'normal' };" +
			               "font-style: ${settings[language]?.styles.fontStyles[1] == 1 ? 'italic' : 'normal' };" +
						   "text-decoration: ${settings[language]?.styles.fontStyles[2] == 1 ? 'underline' : 'none' };" +
						   "color: #${settings.styles.heading?.color };"
			
			flash.formHeading = """<${settings[language]?.heading} class="heading" style="${style}">""" + 
				                  "${settings[language]?.name}</${settings[language]?.heading}>"
		  flash.headingStyle = "background-color: #${settings.styles.heading?.backgroundColor };"
		  flash.descriptionStyle = "color: #${settings.styles.description?.color };background-color: #${settings.styles.description?.backgroundColor };"
		  flash.formHeadingHorizontalAlign = " ${settings[language]?.classes[0]}"
		} else {
			flash.headingStyle = ""
			flash.descriptionStyle = ""
		  flash.formHeading = FormBuilderConstants.EMPTY_STRING
		  flash.formHeadingHorizontalAlign = FormBuilderConstants.EMPTY_STRING
		}
	}
	
	String getShowViewTemplate(def request, def flash, Form form) {
		return getViewTemplateSource(FormBuilderConstants.FB_SHOW_VIEW,
			request, flash, form, FormDesignerView.SHOW)
	}
	
	String getEditViewTemplate(def request, def flash, Form form) {
		return getViewTemplateSource(FormBuilderConstants.FB_EDIT_VIEW,
			request, flash, form, FormDesignerView.EDIT)
	}
	
	def getPersistableFieldsCount(List fields) {
		Integer persistableFieldsCount = 0
		fields?.each { field ->
	    if (field.settings.indexOf(FormBuilderConstants.PERSISTABLE) > -1) {
			  persistableFieldsCount++
			 }
		}
		return persistableFieldsCount
	}
	
	private String getWidgetTextAjax(name,settings,type,rel,request){
		Field field = new Field()
		field.name = name
		field.settings = settings
		field.type = type
		def i = rel.toInteger()
		FormDesignerView formDesignerView = FormDesignerView.CREATE
		def returnString = ""
		try{
			Locale locale = RCU.getLocale(request)
			boolean isRoleGranted = SpringSecurityUtils.ifAnyGranted(ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS)
			Widget widget
			widget = grailsApplication.classLoader.loadClass("${FormBuilderConstants.WIDGET_PACKAGE}.${field.type}").newInstance()
			widget.isRoleGranted = isRoleGranted
			if(field.type=="ImageUpload")
				widget.attachments = []
			returnString = widget.getTemplateText(field, i, locale, false, formDesignerView,null,null);
		}catch(Exception e){
			println "FormTemplateService-getWidgetTextAjax: "+e
		}
		return returnString
	}
	
	private String getWidgetsTemplateText(Form form, Locale locale, FormDesignerView formDesignerView) {
		Widget widget
		FormAdmin formAdmin = null;
		if(form?.id != null){
			formAdmin = FormAdmin.findByForm(form);
		}
		boolean isRoleGranted = SpringSecurityUtils.ifAnyGranted(ConfigurationHolder.config.ROLES_TO_SHOW_ALL_RECORDS)
		if (form.fieldsList?.size() > 0) {
			Widget.fullLength=JSON.parse(form.settings).labelDisplay!="0"?true:false;
			FastStringWriter out = new FastStringWriter()
			def fieldsList = form.fieldsList
			fieldsList.sort{it.sequence}.eachWithIndex { field, i ->
				widget = grailsApplication.classLoader.loadClass("${FormBuilderConstants.WIDGET_PACKAGE}.${field.type}").newInstance()
				widget.isRoleGranted = isRoleGranted
				if(field.type=="ImageUpload")
					widget.attachments = form && form.id?form.getAttachments(field.name):[]
				out << widget.getTemplateText(field, i, locale, false, formDesignerView,null,formAdmin)
			}
			return out.toString();
		} else {
			return FormBuilderConstants.EMPTY_STRING
		}
	}
}
