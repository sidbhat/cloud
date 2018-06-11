/* Copyright 2011 the original author or authors.
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *      http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.formbuilder.widget

import org.grails.formbuilder.Field
import org.grails.formbuilder.Form;
import org.grails.formbuilder.FormAdmin;
import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants
import org.codehaus.groovy.grails.web.pages.FastStringWriter
import org.codehaus.groovy.grails.commons.ConfigurationHolder

import grails.converters.JSON

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
abstract class Widget {
	
	boolean isRoleGranted = false;
	boolean isEmailEmbedFrom = false;
	def subFormDomainClass
	def domainInstanceForSubForm
	def attachments
	static def currentDomainClass
	static boolean fullLength;
	static int totalPageBreak;
	static int currentPageBreak;
	String getUniqueTemplateText(Object domainInstance = null,Form form=null){
		
	}
	String getRecaptchTemplateText(){
		
	}
	String getTemplateText(Field field, Integer index, Locale locale, Boolean readOnly = false, FormDesignerView formDesignerView = null, Object settings = null,FormAdmin formAdmin = null, Object domainInstance = null,Boolean setDefaultValues=false) {
		FastStringWriter out = new FastStringWriter()
		if(!readOnly || field.type!='PageBreak'){
			if (!settings) {
			   settings = JSON.parse(field.settings)
			}
			boolean allowDelete = true;
			boolean alertOnChangeSettings = false;
			String messageToShowOnDelete = "";
			String messageToShowOnChangeSettings = "";
			int deleteReasonCount = 1;
			if(formAdmin){
				if(formAdmin.statusField == field) {
					messageToShowOnDelete = "status field in Form Admin.<br/>"
					allowDelete = false;
					alertOnChangeSettings = true;
					messageToShowOnChangeSettings = ""
				}
				if(!allowDelete){
					messageToShowOnDelete = "Can not delete, the field is used as :<br/>"+messageToShowOnDelete
				}
			}
			out << """<div id="${field.name}ControlHolder" ${isEmailEmbedFrom && field.type!='PageBreak'?'style="border-bottom: 1px solid #EFEFEF;padding: 1em;"':''} class="${domainInstance?.errors?.name?.contains(field.name)?'errors':''} ${formDesignerView || field.type!='PageBreak'?('ctrlHolder'+(field.type=='Paypal'?' ppHolder':'')):''} """ 
			out << getFieldClasses(settings, locale)
			out << '" rel="'
			out << index
			out << '" style="'
			out << getFieldStyles(settings, locale)
			out << '">'
			String templateText 
			domainInstanceForSubForm = domainInstance
			if (readOnly) {
			  templateText = getWidgetReadOnlyTemplateText(field.name, settings, locale, formDesignerView, domainInstance)
			  templateText = templateText?:getWidgetTemplateText(field.name, settings, locale, formDesignerView, null)
			} else {  
				templateText = getWidgetTemplateText(field.name, settings, locale, formDesignerView, domainInstance)
			}
			out << templateText
			
			if (formDesignerView) {
				if (formDesignerView != FormDesignerView.SHOW) {
					out << '<a class="ui-corner-all ui-state-error closeButton" href="#" id="deleteButton'+index+'"><span class="ui-icon ui-icon-close">delete this widget</span></a>'
					if(field.type!='Paypal'){
						out << '<a class="ui-corner-all ui-state-highlight copyButton" href="#"><span class="ui-icon ui-icon-plus" title="Duplicate">duplicate this widget</span></a>'
					}
				}
				out << '<script id="fields'+index+'javascript">$(document).ready(function(){$(document.getElementById("fields['+index+'].settings")).val("'+field.settings.encodeAsJavaScript()+'");$("#fields'+index+'javascript").remove();'+(!allowDelete?('$("#deleteButton'+index+'").unbind("click").bind("click",function(){return false;});showToolTip(\''+messageToShowOnDelete+'\','+index+');'):"")+'});</script>'
				out << """\
					 <div class="fieldProperties">
						  <input type="hidden" name="fields[$index].id" id="fields[$index].id" value="${field.id}" />
						  <input type="hidden" name="fields[$index].name" id="fields[$index].name" value="${field.name}" />
						  <input type="hidden" name="fields[$index].type" id="fields[$index].type" value="${field.type}" />
						  <input type="hidden" name="fields[$index].settings" id="fields[$index].settings" />
						  <input type="hidden" name="fields[$index].sequence" id="fields[$index].sequence" value="${field.sequence}" />
						  <input type="hidden" name="fields[$index].status" id="fields[$index].status" />
					 </div>
				  """
			}else if(setDefaultValues){
				if(ConfigurationHolder.config.formViewer?.fields?.defaultValueFor?.contains(field.type)){
					def value = domainInstance?."${field.name}"?.encodeAsJavaScript()
					out << '<script id="fields'+index+'javascript">$(document).ready(function(){$(document.getElementById("'+field.name+'")).val("'+(value?:settings.en.value.encodeAsJavaScript())+'");$("#fields'+index+'javascript").remove();});</script>'
				}
			}
			if(formDesignerView || field.type!='PageBreak' || (field.type=='PageBreak' && readOnly)){
				out << '</div>'
			}
		}
		return out.toString()
	}
	
	String getConstraints(Field field, Object settings) {
		FastStringWriter out = new FastStringWriter()
		out << "${field.name} "
		if (settings.required && !settings.hideFromUser) {
			out << "nullable:true, blank:false, required: true"
		} else {
		  out << "nullable:true, required: false"
		}
		if(settings.hideFromUser) {
			out<< ", display: false"
		}
		String fieldConstraints = getFieldConstraints(settings)
		//println "field constraints ${fieldConstraints}"
		if (fieldConstraints != FormBuilderConstants.EMPTY_STRING) {
			out << ", ${fieldConstraints}"
		}
		return out.toString()
	}
	
	abstract String getWidgetTemplateText(String name, Object settings, 
	                                      Locale locale, FormDesignerView formDesignerView, Object domainInstance)
	abstract String getWidgetReadOnlyTemplateText(String name, Object settings,
		                                            Locale locale, FormDesignerView formDesignerView, Object domainInstance)
	String getFieldClasses(Object settings, Locale locale) { return FormBuilderConstants.EMPTY_STRING	}
	String getFieldStyles(Object settings, Locale locale) { return FormBuilderConstants.EMPTY_STRING	}
	String getFieldConstraints(Object settings) { return FormBuilderConstants.EMPTY_STRING	}
	Object getFieldValue(Object settings, Locale locale) { 
		String language = locale.language == 'en' ? 'en' : "${locale.language}_${locale.country}"
		return settings."${language}".value	
	}
	int getRandomIndex(def _max, def generated){
	 	int r
		while(true)
		{
			Random randomGenerator = new Random();
			 r=randomGenerator.nextInt(_max)
			if (!generated.contains(r))
			{
				break;
			}
		}
		return r;
	}
}
