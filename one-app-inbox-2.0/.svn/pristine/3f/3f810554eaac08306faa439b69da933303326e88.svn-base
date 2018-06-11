/* Copyright 2010 the original author or authors.
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
package org.grails.uniform

/**
 *
 * @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
 *
 * @since 1.5
 */
class UniFormTagLib {
    static namespace = "uf"
    
    def resources = { attrs, body ->
		    String type = attrs.remove("type")
		    if (!type) {
		      String style = attrs.remove("style")?:"default"
		      boolean validation = attrs.validation ? Boolean.valueOf(attrs.remove("validation")) : grailsApplication.config.uniForm.get("validation", true)		    
		      renderJavaScript g.resource(plugin:"uniForm", dir:"js", file:"uni-form.jquery.js")
		      if (validation) {
              renderJavaScript g.resource(plugin:"uniForm", dir:"js", file:"uni-form-validation.jquery.js")
                    }
		      renderCSS g.resource(plugin:"uniForm", dir:"css", file:"uni-form.css")
		      renderCSS g.resource(plugin:"uniForm", dir:"css", file:"${style}.uni-form.css")
		      renderInternetExplorerFixedStyle()
		    } else if (type.equals("js")) {
		      boolean validation = attrs.validation ? Boolean.valueOf(attrs.remove("validation")) : grailsApplication.config.uniForm.get("validation", true)		    		    
		      renderJavaScript g.resource(plugin:"uniForm", dir:"js", file:"uni-form.jquery.js")
		      if (validation) {
              renderJavaScript g.resource(plugin:"uniForm", dir:"js", file:"uni-form-validation.jquery.js")
                    }
			  } else if (type.equals("css")) {
		      String style = attrs.remove("style")?:"default"
		      renderCSS g.resource(plugin:"uniForm", dir:"css", file:"uni-form.css")
		      renderCSS g.resource(plugin:"uniForm", dir:"css", file:"${style}.uni-form.css")
		      renderInternetExplorerFixedStyle()
			    }
    }
	
	private renderInternetExplorerFixedStyle() {
    out << '''<!--[if lte ie 7]>
      <style type="text/css" media="screen">
        /* Move these to your IE6/7 specific stylesheet if possible */
        .uniForm, .uniForm fieldset, .uniForm .ctrlHolder, .uniForm .formHint, .uniForm .buttonHolder, .uniForm .ctrlHolder ul{ zoom:1; }
      </style>
    <![endif]-->''' 
	}
	
	private renderJavaScript(def url) {
		out << '<script type="text/javascript" src="' + url + '"></script>\n'
	}
	
	private renderCSS(def url) {
    out << '<link rel="stylesheet" type="text/css" media="screen" href="' + url + '" />\n'
	}	
}
