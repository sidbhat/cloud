/*
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 *     http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * See the License for the specific language governing permissions and
 * limitations under the License.
 */
package org.grails.grailsui

import org.codehaus.groovy.grails.plugins.grailsui.GrailsUIException
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

class ResourcesTagLibService implements ApplicationContextAware {

    static transactional = false

    private static resourceTypesToPlugins = [
            "yui": "yui",
            "yui-cms": "bubbling",
            "grailsui": "grails-ui" ]

    def grailsUITagLibService
    def grailsApplication
    def taglib

    def stylesheetTag(cssKey, pluginContextPath, config) {
        def uri = conjureURI(cssKey, 'css', null, pluginContextPath, config)
        "<link rel='stylesheet' type='text/css' href='$uri'/>\n"
    }

    def javascriptTag(jsKey, mode, pluginContextPath, config) {
        def uri = conjureURI(jsKey, 'js', mode, pluginContextPath, config)

        // if the file is custom grailsui stuff with only raw javascript
        if (hasOnlyRawJavascript(uri)) {
            // if the mode is already set to raw then we have a missing dependency
            if (mode == 'raw') {
                throw new GrailsUIException("Missing file for $jsKey: $uri")
            }
            // otherwise, try to reprocess as raw mode to get a good uri
            return javascriptTag(jsKey, 'raw', pluginContextPath, config)
        }
        "<script type=\"text/javascript\" src=\"$uri\" ></script>  \n"
    }

    def flashReference(flashKey, pluginContextPath, config) {
        def uri = conjureURI(flashKey, 'swf', null, pluginContextPath, config)
        "<script>YAHOO.widget.Chart.SWFURL = \"${uri}\";</script>"
    }
    
    private conjureURI(key, fileType, mode, pluginContextPath, config) {
        def trueKey = key.replaceAll('-','_')
        def resourceType = resourceType(trueKey, fileType, config)
        def safeRT = resourceType.replaceAll('-','_')
        def file = config.resources."$safeRT"."$fileType"."$trueKey"
        def version = config.resources."$safeRT".version ? '/' + config.resources."$safeRT".version : ''
        // find the correct file extention (-min.js, -debug.js, etc.) for javascript includes
        if (fileType == 'js') file += getFileExtension(mode, trueKey, config)

        def plugin = resourceType == "grailsui" ? "grails-ui" : resourceType
        def result = taglib.resource(
                plugin: resourceTypesToPlugins[resourceType],
                dir: "js/${resourceType}",
                file: file).toString()
        def dirPath = result[1..<(result.lastIndexOf('/'))]
        def appCtx = grailsApplication.mainContext
        // if the resource file doesn't exist, try to look for a beta version
        if (!appCtx.getResource(result).exists()) {
            // if this is a "-something" version of a script, look for the raw version
            if (file.contains('-')) {
                def rawFile = file.split('-')[0] + ".${fileType}"
                if (grailsApplication.mainContext.getResource(dirPath + File.separator + rawFile).exists()) {
                    result = taglib.resource(
                            plugin: resourceTypesToPlugins[resourceType],
                            dir: "js/${resourceType}",
                            file: rawFile)
                }
            } 
            // otherwise we'll check for beta and experimental versions
            else {
                def betaFile = (file - ".${fileType}") + "-beta.${fileType}"
                // if the beta file exists, use it
                if (appCtx.getResource(dirPath + File.separator + betaFile).exists()) {
                    result = taglib.resource(
                            plugin: resourceTypesToPlugins[resourceType],
                            dir: "js/${resourceType}",
                            file: betaFile)
                } 
                // also look for experimental
                else {
                    def expFile = (file - ".${fileType}") + "-experimental.${fileType}"
                    // if the experimental file exists, use it
                    if (appCtx.getResource(dirPath + File.separator + expFile).exists()) {
                        result = taglib.resource(
                            plugin: resourceTypesToPlugins[resourceType],
                            dir: "js/${resourceType}",
                            file: expFile)
                    }
                }
            }
            // otherwise, it might be a grailsui file that has not -min or -debug, and this is handled elsewhere
        }
        return result
    }
    
    private resourceType(key, type, config) {
        if (key in config.resources.yui."$type".keySet()) return 'yui'
        if (key in config.resources.yui_cms."$type".keySet()) return 'yui-cms'
        if (key in config.resources.grailsui."$type".keySet()) return 'grailsui'
        throw new GrailsUIException("Resource $key was not available within GrailsUI config.")
    }
    
    private dirPath (resourceType, version, pluginContextPath) {
        def path = "js"
        // only grails ui source files will use the plugin context path for URI
        if (resourceType == 'grailsui') {
            path = pluginContextPath + path
        }
        path
    }
    
    private getFileExtension(mode, key, config) {
        if (key in config.rollUps.keySet()) {
            mode = 'raw'
        }
        if (!mode) {
            mode = 'minimal'
        }
        config.mode."$mode"
    }

    // At this point, we are only checking to see if the javascript file is a part of GrailsUI's YUI extensions.  If it is, then
    // there will be no -min.js or -debug.js files for it (yet).  All these URIs should contain 'grailsui' because they are stored
    // within the /js/grailsui folder
    private hasOnlyRawJavascript = { uri ->
        uri.contains('grailsui') && ( uri.contains('-debug.js') || uri.contains('-min.js') )
    }

    // only getting the app context so we can grab the ApplicationTagLib, which will be used to createLinkTo() in the
    // tag helper functions
    void setApplicationContext(ApplicationContext applicationContext) {
        taglib = applicationContext.getBean('org.codehaus.groovy.grails.plugins.web.taglib.ApplicationTagLib')
    }
}
