

package com.oneapp.cloud.core

import groovy.text.Template
import org.codehaus.groovy.grails.commons.ApplicationHolder
import org.xhtmlrenderer.pdf.ITextRenderer

class ExportController {
 
 
 	def index = { redirect(action: "pdf", params: params) }
    def groovyPagesTemplateEngine
    def exportService
      
    def export = {
        
		if( params?.domain && params.domain == "Project" ){
			if(params?.format && params.format != "html"){
				response.contentType = ConfigurationHolder.config.grails.mime.types[params.format]
				response.setHeader("Content-disposition", "attachment; filename=books.${params.extension}")
				exportService.export(params.format, response.outputStream,Project.list(params), [:], [:])
			}
		}
	
    }
 
    def pdf = {

//        if (!params.id) {
//            flash.message = "params.id.not.found"
//            flash.args = [params.id]
//            flash.defaultMessage = "Object not found with id ${params.id}"
//            return
//        }

        File tmp = File.createTempFile("template", ".txt")
        FileOutputStream fos = new FileOutputStream(tmp)
        //params.properties = Invoice.get(params.id)

        //def path = ApplicationHolder.getApplication().getParentContext().getServletContext().getRealPath("");
        //if ( params.template == null )
        //  params.template = "grails-app/views/calendar/month.gsp"
        
        //def reportFile = new File(path,params.template)

        // expand from groovy template to filled in html file
         //Template template = groovyPagesTemplateEngine.createTemplate(reportFile)

        //fos << template.make(params)
		//def textToPDF = "<html><body>Hello</body></html>"
		def textToPDF = "http://demo.oneappcloud.com:8009/form-builder/PF/a?formId=168".toURL().text
		def temp1 = textToPDF.substring(0,textToPDF.indexOf('var masterFormFieldMap = {}'))
		def temp2 = textToPDF.substring(textToPDF.indexOf('</script><div class="buttons pageButtons"'),textToPDF.length())
		textToPDF = temp1+temp2
		
		textToPDF = textToPDF.trim()
		while(textToPDF.indexOf('font-')>-1){
			textToPDF = textToPDF.replace('font-','font:')
		}
		textToPDF = textToPDF.replace('<!DOCTYPE HTML PUBLIC "-//W3C//DTD HTML 4.01//EN" "http://www.w3.org/TR/html4/strict.dtd">','')
		textToPDF = textToPDF.replace('<meta http-equiv="Content-Type" content="text/html; charset=UTF-8" />','')
		textToPDF = textToPDF.replace('<meta name="layout" content="formViewer" />','')
		textToPDF = textToPDF.replace('<meta name="description" content="" />','')
		textToPDF = textToPDF.replace("ga.src = ('https:' == document.location.protocol ? 'https://ssl' : 'http://www') + '.google-analytics.com/ga.js';",'')
		textToPDF = textToPDF.replace("var location = geoip_country_name();",'')
		textToPDF = textToPDF.replace('$("#location").val(location)','')
		textToPDF = textToPDF.replace('url: "/form-builder/PF/saveFormViewUser?formId=168&location="+location,','')
		//textToPDF = textToPDF.replace('','')
		textToPDF = textToPDF.replace('"textInput" />','"textInput" ></input>')
		textToPDF = textToPDF.replace('id="pfid" />','id="pfid" ></input>')
		textToPDF = textToPDF.replace('id="pfii" />','id="pfii" ></input>')
		textToPDF = textToPDF.replace('id="pffn" />','id="pffn" ></input>')
		textToPDF = textToPDF.replace('id="formId" />','id="formId" ></input>')
		textToPDF = textToPDF.replace('id="location" />','id="location" ></input>')
		textToPDF = textToPDF.replace("><select",'></input><select')
		textToPDF = textToPDF.replace('</br>','<br></br>')
		//
		textToPDF = textToPDF.replace('168" style="width:80px;">','168" style="width:80px;"></img>')
		//textToPDF = textToPDF.replace('<img src="http://demo.oneappcloud.com:8009/form-builder/preview/firstImg/9_168" style="width:80px;">','<img src="http://demo.oneappcloud.com:8009/form-builder/preview/firstImg/9_168" style="width:80px;"></img>')
		textToPDF = textToPDF.replace('<img src="http://demo.oneappcloud.com:8009/form-builder/images/loading.gif" alt="Loading..." style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"/>','<img src="http://demo.oneappcloud.com:8009/form-builder/images/loading.gif" alt="Loading..." style="position: absolute;margin: auto;left:0;right: 0;top: 0;bottom: 0;z-index:10001;"></img>')
		while(textToPDF.indexOf('&nbsp;')>-1){
			textToPDF = textToPDF.replace('&nbsp;',' ')
		}
		while(textToPDF.indexOf('"/form-builder')>-1){
			textToPDF = textToPDF.replace('"/form-builder','"http://demo.oneappcloud.com:8009/form-builder')
		}
		fos << textToPDF
        fos.close()

        ITextRenderer renderer = new ITextRenderer()
        renderer.document = tmp.toURI().toURL().toString()
        renderer.layout()
        ByteArrayOutputStream baos = new ByteArrayOutputStream();
        tmp.delete()

        response.setContentType("application/pdf")
        response.setHeader("Content-disposition", "attachment; filename=${'_'+168}.pdf")
        renderer.createPDF baos
        //response.setContentLength(baos.size())
        // println baos.size()
        def os = response.getOutputStream()
        os.write(baos.toByteArray())
        os.flush()
        os.close()

    }
}