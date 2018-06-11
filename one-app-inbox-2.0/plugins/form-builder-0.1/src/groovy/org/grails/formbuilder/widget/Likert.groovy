package org.grails.formbuilder.widget

import org.codehaus.groovy.grails.commons.ConfigurationHolder;
import org.codehaus.groovy.grails.web.pages.FastStringWriter;
import org.grails.formbuilder.FormBuilderConstants
import org.grails.formbuilder.FormDesignerView

/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class Likert extends Widget {
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
	  String value = settings."${language}".value
    String lab = name//settings."${language}".label
		  boolean randomize=settings._randomize
  		  def switchRowCol = settings.en.switchRowCol?true:false
		  def columns = settings.en.columns
		  def rows = settings.en.rows
		  def columnsToPrint = switchRowCol?rows:columns
		  def rowsToPrint = switchRowCol?columns:rows
		  FastStringWriter columnsHTML = new FastStringWriter()
		  columnsHTML << """<td>&nbsp;</td>"""
		  FastStringWriter rowsHTML = new FastStringWriter()
		  def _tempcolumnsToPrintList=[]
		  if(columnsToPrint && columnsToPrint?.size()>0){
			  if(!formDesignerView && randomize ){
				  for(int _temp=0 ; _temp<columnsToPrint.size() ; _temp++){
						int _index= getRandomIndex( columnsToPrint.size() , _tempcolumnsToPrintList)
						_tempcolumnsToPrintList<<_index
						def c=columnsToPrint[_index]
						 columnsHTML << """<td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}>${c.encodeAsHTML()}</td>"""
				  }
				 }else{
				  columnsToPrint?.eachWithIndex{c,i->
					  columnsHTML << """<td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}>${c.encodeAsHTML()}</td>"""
				  }
				 }
		  }else{
			  columnsHTML << """<td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}>&nbsp;</td>"""
		  }
		  def _temprowsToPrintList=[]
		  if(rowsToPrint && rowsToPrint?.size()>0){
			  if(!formDesignerView && randomize ){
				  for(int _temp=0 ; _temp<rowsToPrint.size() ; _temp++){
					  int _index= getRandomIndex( columnsToPrint.size() , _temprowsToPrintList)
					  _temprowsToPrintList<<_index
					  def iIdx = switchRowCol?0:_index
					  def jIdx = switchRowCol?_index:0
					  def r=rowsToPrint[_index]
					  rowsHTML << """<tr><td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);width:45%;"':''}>${r.encodeAsHTML()}</td>"""
					  def rowValue = ""
					   try{
						  rowValue = domainInstance["${name}"][iIdx]
					  }catch(Exception e){}
					  if(_tempcolumnsToPrintList?.size()>0){
						  _tempcolumnsToPrintList.eachWithIndex{j,c->
							  iIdx = switchRowCol?j:_index
							  jIdx = switchRowCol?_index:j
							  try{
								  rowValue = switchRowCol?domainInstance["${name}"][iIdx]:rowValue
							  }catch(Exception e){}
							  rowsHTML << """<td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}><input name="${name}__${iIdx}_" type="radio" value="${jIdx}" ${"${rowValue}" == "${jIdx}"?'checked="checked"':FormBuilderConstants.EMPTY_STRING}></td>"""
						  }
					  }else{
							rowsHTML << """<td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}><input name="${name}__${iIdx}_" type="radio" value="${jIdx}" ${"${rowValue}" == "${jIdx}"?'checked="checked"':FormBuilderConstants.EMPTY_STRING}></td>"""
					  }
					  rowsHTML << """</tr>"""
					  }
				  }else{
				  rowsToPrint?.eachWithIndex{r,i->
					  def iIdx = switchRowCol?0:i
					  def jIdx = switchRowCol?i:0
					  rowsHTML << """<tr><td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);width:45%;"':''}>${r.encodeAsHTML()}</td>"""
					  def rowValue = ""
					  try{
						  rowValue = domainInstance["${name}"][iIdx]
					  }catch(Exception e){}
					  if(columnsToPrint?.size()>0){
						  columnsToPrint.eachWithIndex{c,j->
							  iIdx = switchRowCol?j:i
							  jIdx = switchRowCol?i:j
							  try{
								  rowValue = switchRowCol?domainInstance["${name}"][iIdx]:rowValue
							  }catch(Exception e){}
							  rowsHTML << """<td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}><input name="${name}__${iIdx}_" type="radio" value="${jIdx}" ${"${rowValue}" == "${jIdx}"?'checked="checked"':FormBuilderConstants.EMPTY_STRING}></td>"""
						  }
					  }else{
							rowsHTML << """<td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}><input name="${name}__${iIdx}_" type="radio" value="${jIdx}" ${"${rowValue}" == "${jIdx}"?'checked="checked"':FormBuilderConstants.EMPTY_STRING}></td>"""
					  }
					  rowsHTML << """</tr>"""
				  }
				  
				  }
			 
		  }else{
			  def rowValue = ""
			  try{
				  rowValue = domainInstance["${name}"][0]
			  }catch(Exception e){}
			  rowsHTML = """<tr><td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}></td><td ${isEmailEmbedFrom?'style="padding:5px;border:1px solid #CCC;background-color:rgba(238, 238, 238, 0.4);text-align:center;"':''}><input name="${name}__0_" type="radio" value="0" ${"${rowValue}" == "0"?'checked="checked"':FormBuilderConstants.EMPTY_STRING}></td></tr>"""
		  }
		  textField ="""<div>\
							<label style="font-weight: ${fontWeight}; font-style: ${fontStyle}; ${fontSize}"><span
								style="text-decoration: ${textDecoration};line-height:16px;">${settings."${language}".label?.encodeAsHTML()}</span><em>${settings.required ? '*' : FormBuilderConstants.EMPTY_STRING}</em>\
							</label>\
						</div>\
    					<div>\
				        	<table class="lTable" cellspacing="0" style="border-collapse:collapse;width:100%;color:inherit;">\
					        	<thead>\
					        		<tr>\
					        			${columnsHTML}\
					        		</tr>\
					        	</thead>\
								<tbody>\
		  							${rowsHTML}\
					        	</tbody>\
					        </table>\
		  				</div>"""
	 
	  return """<div>
	      ${textField}
	    <p class="formHint" style="${descriptionColor}${descriptionBackgroundColor}">${settings."${language}".description?.encodeAsHTML()}</p></div>"""
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
