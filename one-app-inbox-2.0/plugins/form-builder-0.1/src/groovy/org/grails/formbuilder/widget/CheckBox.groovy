package org.grails.formbuilder.widget

import org.grails.formbuilder.FormBuilderConstants
import org.grails.formbuilder.FormDesignerView

class CheckBox extends Widget {
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
	  def value = settings."${language}".value
    
    String lab = name//settings."${language}".label
   /* String tempName = ""
    def ragex = """. , "/' ? ! ; : # % & ( ) * + - / < > = @ [ ]  ^ _ { } | ~ """
    for (int i = 0; i < lab.length(); i++) {
      String s = lab.charAt(i)
      if (ragex.contains(s)) {
        tempName += ""
      } else {
        tempName += s
      }
    }
   lab=getToCamel(tempName)*/
//println("VALUE::::"+value);
	def fieldValueList
	if(!formDesignerView){
		if(domainInstance."${name}"){
			try{
				boolean isList = false;
				try{
					println "checkbox list size: "+domainInstance."${name}".length
					isList = true
				}catch(Exception e){}
				if(isList){
					fieldValueList = domainInstance."${name}" as List
				}else{
					fieldValueList = grails.converters.JSON.parse(domainInstance."${name}".decodeHTML())
				}
			}catch(Exception e){}
		}else{
		 fieldValueList = new ArrayList<String>()
		}
	}
	
    def counter = 0;
	  if (!readOnly) {
		textField="";
		boolean randomize=settings._randomize
		if(!formDesignerView && randomize && value){
			def _tempList=[]
			for(int _temp=0 ; _temp<value.size() ; _temp++){
				  int _index= getRandomIndex( value.size() , _tempList)
				  _tempList<<_index
				  def l=value[_index]
				  counter=_index
				  textField = textField +"""<div${isEmailEmbedFrom?' style="padding:5px 0;"':''}><input 
					type="hidden" name="_${name}"><input 
					type="checkbox" name="${name}" id="${name}${counter}" value="${l.encodeAsHTML()}" ${(fieldValueList?.contains(l)?'checked="checked"':'')} 
					style="${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
					text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}"
						/>${isEmailEmbedFrom?'&nbsp;&nbsp;':'<span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">'}${l.encodeAsHTML()} ${isEmailEmbedFrom?'':'&nbsp;&nbsp;</span>'}</br></div>"""//+(!formDesignerView?('<script>$("#'+name+counter+'").val("'+l.encodeAsJavaScript()+'")'+(fieldValueList?.contains(l)?'.attr("checked","checked")':'')+'</script>'):'')
				}
		   }else{
	        value.each{l->
			textField = textField + """<div${isEmailEmbedFrom?' style="padding:5px 0;"':''}><input 
					type="hidden" name="_${name}"><input 
					type="checkbox" name="${name}" id="${name}${counter}" value="${l.encodeAsHTML()}" ${(fieldValueList?.contains(l)?'checked="checked"':'')} 
					style="${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
					text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}"
						/>${isEmailEmbedFrom?'&nbsp;&nbsp;':'<span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">'}${l.encodeAsHTML()} ${isEmailEmbedFrom?'':'&nbsp;&nbsp;</span>'}</br></div>"""//+(!formDesignerView?('<script>$("#'+name+counter+'").val("'+l.encodeAsJavaScript()+'")'+(fieldValueList?.contains(l)?'.attr("checked","checked")':'')+'</script>'):'')
				counter++;
	        }
		   }
			if(settings.otherOption){
				def otherVal = fieldValueList.find{!value.contains(it)}
				textField = textField + """<div${isEmailEmbedFrom?' style="padding:5px 0;"':''}><input type="text" name="${name}" id="${name}othertext" value="${otherVal?:''}" style="width:auto;"   /><span style=" margin: 0px 0px 0px 23px; line-height: 16px;"> Other if any </span></div>"""
			}else if(formDesignerView){
				textField = textField + """<div style="display:none;"><input type="text" name="mycheckbox" placeholder="Other" style="width:auto;"/><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">&nbsp;</span></div>"""
			}
			
	  } else {
	      def fieldValueString = ""
	  	  fieldValueList.each{ x ->
				fieldValueString =  fieldValueString + x.encodeAsHTML() + "<br/>"
			}
		  textField = """<span class="textOutput" style="position:relative;left:50px;${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		  text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}">
		  ${fieldValueString}</span>"""
	  }
	  return """<div><div 
		  	class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}"><label 
			for="${name}" style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}"><span 
			style="text-decoration: ${textDecoration};line-height:16px;">${settings."${language}".label.encodeAsHTML()}</span><em>&nbsp;${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</em></label><p style="line-height:5px;margin:0">&nbsp;</p></div><div 
			class="${Widget.fullLength?'customLengthField':'fullLengthField'} ${settings.fieldLayout?:'oneCol'}">${textField}</div><p 
			class="formHint" style="${descriptionColor}${descriptionBackgroundColor}">${settings."${language}".description.encodeAsHTML()}</p></div>"""
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
