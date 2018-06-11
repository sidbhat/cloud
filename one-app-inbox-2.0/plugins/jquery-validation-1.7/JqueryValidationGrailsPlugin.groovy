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

/**
*
* @author <a href='mailto:limcheekin@vobject.com'>Lim Chee Kin</a>
*
* @since 1.7
*/
class JqueryValidationGrailsPlugin {
    // the plugin version
    def version = "1.7"

    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2 > *"
    
    // the other plugins this plugin depends on
    def dependsOn = [jquery:'1.4.2.7']
    
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
            "grails-app/views/error.gsp"
    ]

    def author = "Lim Chee Kin"
    def authorEmail = "limcheekin@vobject.com"
    def title = "JQuery Validation Plugin"
    def description = '''\\
Simply supplies jQuery Validation resources, depends on jQuery plugin. Use this plugin to avoid resource duplication and conflicts.
'''

    // URL to the plugin's documentation
    def documentation = "http://grails.org/plugin/jquery-validation"
}
