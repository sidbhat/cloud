package org.grails.formbuilder.widget

import org.grails.formbuilder.FormBuilderConstants
import org.grails.formbuilder.FormDesignerView
import org.apache.commons.lang.StringUtils

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class GroupButton extends Widget {
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
    String ddValue = ""
    String lab = name//settings."${language}".label
	
    def count = 0;
    if (!readOnly) {
      textField="";
	  boolean randomize=settings._randomize
	  if(!formDesignerView && randomize && value){
 		  def _tempList=[]
		   for(int _temp=0 ; _temp<value.size() ; _temp++){
				 int _index= getRandomIndex( value.size() , _tempList)
				 _tempList<<_index
				 def l=value[_index]
				 count=_index
				 textField = textField + """<div${isEmailEmbedFrom?' style="padding:5px 0;"':''}><input
					 type="radio" name="${name}" id="${name}${count}" value="${l.encodeAsHTML()}" ${(domainInstance?."${name}"==l?'checked="checked"':'')} />${isEmailEmbedFrom?'&nbsp;&nbsp;':'<span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">'}${l.encodeAsHTML()}  ${isEmailEmbedFrom?'':'&nbsp;&nbsp;</span>'}</br></div>"""//+(!formDesignerView?('<script>$("#'+name+count+'").val("'+l.encodeAsJavaScript()+'")'+(domainInstance."${name}"==l?'.attr("checked","checked")':'')+'</script>'):'')
			 }
		  }else{
		      value.each{l->
		        textField = textField + """<div${isEmailEmbedFrom?' style="padding:5px 0;"':''}><input  
					  type="radio" name="${name}" id="${name}${count}" value="${l.encodeAsHTML()}" ${(domainInstance?."${name}"==l?'checked="checked"':'')} />${isEmailEmbedFrom?'&nbsp;&nbsp;':'<span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">'}${l.encodeAsHTML()}  ${isEmailEmbedFrom?'':'&nbsp;&nbsp;</span>'}</br></div>"""//+(!formDesignerView?('<script>$("#'+name+count+'").val("'+l.encodeAsJavaScript()+'")'+(domainInstance."${name}"==l?'.attr("checked","checked")':'')+'</script>'):'')
				count++;
		      }
		  }
	  if(settings.otherOption && !formDesignerView){
		  def otherVal = (!value.contains(domainInstance?."${name}")?domainInstance?."${name}":'')
		  textField = textField + """<div${isEmailEmbedFrom?' style="padding:5px 0;"':''}><input 
			  type="radio" name="${name}" id="${name}other" style="display:none;"/><input type="text" id="${name}othertext" value="${otherVal?:''}" style="width:auto;"/><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">&nbsp;</span><br></div
			  ><script>
                \$("#${name}other").val("${otherVal?.encodeAsJavaScript()}")${otherVal!=''?'.attr("checked","checked")':''}
		  		\$("#${name}othertext").keyup(function(){
                      \$("#${name}other").val(\$(this).val()).trigger('click');
                });
              </script>"""
	  }else if(formDesignerView){
		  textField = textField + """<div style="${settings.otherOption?'':'display:none;'}"><input type="text" name="mycheckbox" id="${name}othertext" style="width:auto;"/><span style="display: block; margin: -17px 0px 0px 23px; line-height: 16px;">&nbsp;</span><br></div>"""
	  }
    } else {
	  ddValue = domainInstance."${name}"
      textField = """<span class="textOutput" style="position:relative;left:50px;${fontFamily}font-weight: ${fontWeight}; font-style: ${fontStyle}; \
		  text-decoration: ${textDecoration}; ${fontSize}${valueColor}${valueBackgroundColor}">
		  ${ddValue}
		  </span>"""
    }
    return """<div><div 
				class="${Widget.fullLength?'customLengthLabel':'fullLengthLabel'}"><label for="${name}" 
                style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}"><span 
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