package org.grails.formbuilder.widget

import java.text.SimpleDateFormat;

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.grails.formbuilder.FormDesignerView
import org.grails.formbuilder.FormBuilderConstants

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class SingleLineDate extends Widget {
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
	  String value = formDesignerView ? settings."${language}".value : "domainInstance.${name}"
	  //String dateValue = java.text.SimpleDateFormat("MM.dd.yyyy").format(value)
	  String dateValueScript = ""
	  String dateValue = ""
	  String hoursDD = ""
	  String minutesDD = ""
	  String meridianDD = ""
	  String hours = ""
	  String minutes = ""
	  String meridian = ""
	  if(!formDesignerView)
	  	try{
			if(settings.timeFormat){
				try{
					minutes = domainInstance."${name}"?new SimpleDateFormat("mm").format(domainInstance."${name}"):'00'
					def minutesDDOptions = """"""
					def hoursDDOptions = """"""
					for(int i=0;i<60;i++){
						def m = "${i<10?('0'+i):i}"
						minutesDDOptions += """<option value="${m}" ${minutes==m?'selected=selected':''}>${m}</option>"""
					}
					if(settings.timeFormat == 'HH:mm'){
						hours = domainInstance."${name}"?new SimpleDateFormat("HH").format(domainInstance."${name}"):'00'
						for(int i=0;i<24;i++){
							def h = "${i<10?('0'+i):i}"
							hoursDDOptions += """<option value="${h}" ${hours==h?'selected=selected':''}>${h}</option>"""
						}
					}else if(settings.timeFormat == 'hh:mm a'){
						hours = domainInstance."${name}"?new SimpleDateFormat("hh").format(domainInstance."${name}"):'12'
						for(int i=1;i<13;i++){
							def h = "${i<10?('0'+i):i}"
							hoursDDOptions += """<option value="${h}" ${hours==h?'selected=selected':''}>${h}</option>"""
						}
						meridian = domainInstance."${name}"?new SimpleDateFormat("a").format(domainInstance."${name}"):'AM'
						
						meridianDD = """<select id="${name}Meridian" name="${name}Meridian" style="margin-top:3px;width:60px;margin-left:2px;"><option value="AM" ${meridian=='AM'?'selected=selected':''}>AM</option><option value="PM" ${meridian=='PM'?'selected=selected':''}>PM</option></select>"""
					}
					hoursDD = """<select id="${name}Hours" name="${name}Hours" style="margin-top:3px;width:60px;margin-left:2px;clear:left;">${hoursDDOptions}</select>"""
					minutesDD = """<select id="${name}Minutes" name="${name}Minutes" style="margin-top:3px;width:60px;margin-left:2px;">${minutesDDOptions}</select>"""
				}catch(Exception e){}
			}
			if(settings.showCalendar){
				dateValue = new SimpleDateFormat(ConfigurationHolder.config.format.date).format(domainInstance."${name}")
				dateValueScript = '<script>$("#'+name+'").val("'+dateValue?.encodeAsJavaScript()+'")</script>'
			}else{
				try {
				dateValue = new SimpleDateFormat(ConfigurationHolder.config.format.date).format(domainInstance."${name}")
				}catch(Exception ex){
				}
				dateValueScript = """<script>
										var dateValue = "${dateValue}"
										function setDateFields${name}(dateValues){
												try{
													var elements = dateValues.split("/");
													\$("#${name}dateMM").val(elements[0])
			                                        \$("#${name}dateDD").val(elements[1])
			                                        \$("#${name}dateYYYY").val(elements[2])
												}catch(err){
												}
											}
										setDateFields${name}(dateValue)
										function setFieldValue${name}(){
											try{
												var dateMM = \$("#${name}dateMM").val()
		                                        var dateDD = \$("#${name}dateDD").val()
		                                        var dateYY = \$("#${name}dateYYYY").val()
												var date = ""
												if(dateMM && dateDD && dateYY){
													date = (dateMM.length==1?"0":"")+dateMM+"/"+(dateDD.length==1?"0":"")+dateDD+"/"+dateYY
													\$("#${name}").val(date).trigger("change")
												}
											}catch(err){
											}
										}
									</script>"""
			}
	  	}catch(Exception e){
		}
	  if (!readOnly) {
		  def threeCalendarBoxes = ""
		  if(formDesignerView || !settings.showCalendar){
			  threeCalendarBoxes = """<div class="boxDateField" style='${settings.showCalendar?"display:none;":"display:inline-block;"}'>   \
			<input type="text" style="width:40px !important;" id="${name}dateMM" name="${name}dateMM" onchange="setFieldValue${name}()"/>/<input type="text" style="width:40px !important;" id="${name}dateDD" name="${name}dateDD" onchange="setFieldValue${name}()"/>/<input type="text" style="width:60px !important;" id="${name}dateYYYY" name="${name}dateYYYY" onchange="setFieldValue${name}()"/></div>"""
		  }
		  def timeFormatBox = ""
		  if(formDesignerView || settings.timeFormat){
			  timeFormatBox = """<div class="timeFormatDiv" style='${settings.timeFormat?"":"display:none;"}'\
				  >${hoursDD?:("<select class=\"hours\" style=\"margin-top:3px;width:60px;margin-left:2px;clear:left;\"><option>${settings.timeFormat=='HH:mm'?'HH':'hh'}</option></select>")}\
				 ${minutesDD?:('<select class="minutes" style="margin-top:3px;width:60px;margin-left:2px;"><option>mm</option></select>')}\
				${meridianDD?:(isEmailEmbedFrom?'':("<select class=\"meridian\" style=\"margin-top:3px;width:60px;margin-left:2px;${settings.timeFormat=='HH:mm'?'display:none;':''}\"><option>AM</option></select>"))}</div>"""
		  }
		  textField = """\
		  <div class="singleDateField ${settings.fieldSize?settings.fieldSize:"mClass"}"  style="${settings.showCalendar?"":"display:none;"}"><input type="hidden" id="${name}Value" disabled="disabled" itstype="SingleLineDate" value="${dateValue}" /><span><span><input ${((formDesignerView || settings.showCalendar)?('type="'+(isEmailEmbedFrom || formDesignerView?"text":"date")+'"'):'type="hidden"')} \
		    style="${isEmailEmbedFrom?'width:50%;':''}${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}" name="${name}" id="${name}" class="textInput" itsType="SingleLineDate" AUTOCOMPLETE="OFF" value="${dateValue}" onchange="setDateFields${name}(this.value)"/></span></span><img src="${ConfigurationHolder.config.grails.serverURL}/images/calendar_16.png" style="position:absolute;margin:5px;"></div>${threeCalendarBoxes}${timeFormatBox}"""+dateValueScript
	  } else {
		  textField = """\
		  <span class="textOutput" style="position:relative;left:50px;${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		  text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}">
		  ${dateValue+(dateValue && settings.timeFormat?((hours?(' '+hours):'')+(minutes?(':'+minutes):'')+(meridian?(' '+meridian):'')):'')}
		  </span>"""
	  }
	  return """<div><div 
		  		class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}"><label for="${name}" style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}"><span
		  		style="text-decoration: ${textDecoration};line-height:16px;">${settings."${language}".label?.encodeAsHTML()}</span><em>&nbsp;${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</em></label></div><div 
		  		class="${Widget.fullLength?'customLengthField':'fullLengthField'}" >${textField}<p
				class="formHint" style="${descriptionColor}${descriptionBackgroundColor}">${settings."${language}".description?.encodeAsHTML()}</p></div></div>"""
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
