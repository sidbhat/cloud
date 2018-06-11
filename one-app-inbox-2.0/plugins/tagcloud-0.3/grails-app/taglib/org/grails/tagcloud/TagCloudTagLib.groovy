/*
 * Copyright 2010 Marek Podsiadły
 *
 *   Licensed under the Apache License, Version 2.0 (the "License");
 *   you may not use this file except in compliance with the License.
 *   You may obtain a copy of the License at
 *
 *       http://www.apache.org/licenses/LICENSE-2.0
 *
 *   Unless required by applicable law or agreed to in writing, software
 *   distributed under the License is distributed on an "AS IS" BASIS,
 *   WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 *   See the License for the specific language governing permissions and
 *   limitations under the License.
 */

package org.grails.tagcloud

import org.codehaus.groovy.grails.plugins.web.taglib.JavascriptTagLib
import org.springframework.context.ApplicationContextAware
import org.springframework.context.ApplicationContext

/**
 * This tag liblary provides tag for simple creating Tag Cloud
 *
 * @author Marek Podsiadły <marekpodsiadly@gmail.com>
 */
class TagCloudTagLib implements ApplicationContextAware {

    ApplicationContext applicationContext
    
    static namespace = "tc"

    def tagCloud = { attrs, body ->
        if (!request[JavascriptTagLib.INCLUDED_LIBRARIES]) {
            request[JavascriptTagLib.INCLUDED_LIBRARIES] = []
        }
        if (!request[JavascriptTagLib.INCLUDED_LIBRARIES].contains("jquery"))
            out << '<script type="text/javascript" src="' +
                g.resource(dir: 'js', file: JavascriptTagLib.LIBRARY_MAPPINGS.jquery[0] + '.js') + '"></script>'

        out << '<script type="text/javascript" src="' +
            g.resource(dir: pluginContextPath, file:'js/jquery.tagcloud.js') + '"></script>'

        def color
        if (attrs.color) {
            def c = attrs.color
            color = [start: c.start ?: '#cde', end: c.end ?: '#f52']
        }
        else color = [start: '#cde', end: '#f52']

        def size
        if (attrs.size) {
            def s = attrs.size
            size = [start: s.start ?: 14, end: s.end ?: 18, unit: s.unit ?: 'pt']
        }
        else size = [start: 14, end: 18, unit: 'pt']

        def tags =[:]
        if (!attrs.bean) tags = attrs.tags ?: [:]
        else {
            if (applicationContext.getBean('pluginManager')?.hasGrailsPlugin("taggable"))
                tags = TagCloudUtil.tags(attrs.bean)
        }

        if (attrs.sort?.toBoolean()) tags = tags.sort{it.key}

        out << g.render(
            template: '/shared/cloud',
            model: [
                color: color,
                size: size,
                tags: tags,
                controller: attrs.controller ?: controllerName,
                action: attrs.action ?: '',
                id: attrs.id ?: null,
                paramName: attrs.paramName ?: null
            ],
            plugin: 'tagcloud'
        )
    }
}
