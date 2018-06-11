package org.grails.formbuilder.widget

import net.tanesha.recaptcha.ReCaptcha;
import net.tanesha.recaptcha.ReCaptchaFactory;
import net.tanesha.recaptcha.ReCaptchaImpl;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
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
class Recaptcha extends Widget {
	def serverURL = ConfigurationHolder.config.grails.serverURL
 String getFieldStyles(Object settings, Locale locale) {
	 return ""
  }
 
 String getWidgetTemplateText(String name, Object settings,  
	                            Locale locale, FormDesignerView formDesignerView, Object domainInstance) {
    return ""
   }
								
 public String getRecaptchTemplateText(def readOnly) {
	String textField
	if (!readOnly) {
		if(serverURL.startsWith("https"))
		{
			ReCaptcha c = ReCaptchaFactory.newSecureReCaptcha(ConfigurationHolder.config.recaptch.publicKey, ConfigurationHolder.config.recaptch.privateKey, false);
			((ReCaptchaImpl) c).setRecaptchaServer("https://www.google.com/recaptcha/api");
			textField = c.createRecaptchaHtml(null, null)
		}else if(serverURL.startsWith("http"))
		{
			ReCaptcha c = ReCaptchaFactory.newReCaptcha(ConfigurationHolder.config.recaptch.publicKey, ConfigurationHolder.config.recaptch.privateKey, false);
			textField = c.createRecaptchaHtml(null, null)
		}
	} else {
		textField = ""
	}
	return """<div class="ctrlHolder" rel="-1">${textField}</div>"""
  }
								
 String getFieldConstraints(Object settings) {
	return FormBuilderConstants.EMPTY_STRING
 }
 								
 String getWidgetReadOnlyTemplateText(String name, Object settings,
									                    Locale locale, FormDesignerView formDesignerView, Object domainInstance) {														
    return ""
  }
}
