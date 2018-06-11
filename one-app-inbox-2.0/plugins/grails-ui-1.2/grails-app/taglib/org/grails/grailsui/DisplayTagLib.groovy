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

class DisplayTagLib {

    static namespace = "gui"

    def grailsUITagLibService
    def displayTagLibService

    def expandablePanel = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        expanded: false,
                        closable: false,
                        vertical: false,
                        bounce: true
                ],
                attrs,
        )
        def bounceMarkup = attrs.bounce ? 'rel="bounceOut"' : ''
        def expanded = ''
        def vertical = attrs.vertical ? ' vertical' : ''
        if (grailsUITagLibService.makeJavascriptFriendly(attrs.expanded)) expanded = ' selected'
        def title = 'Expandable Panel Dummy title'
        if (attrs.title) title = attrs.title
        def closeAction = attrs.closable ? '<a href="#" class="accordionRemoveItem">&nbsp;</a>' : ''
        out << """
            <div id="${attrs.id}" class="yui-cms-accordion multiple fade fixIE${vertical}" ${bounceMarkup}>
                <div class="yui-cms-item yui-panel${expanded}">
                    <div class="hd ${attrs['class'] ? attrs['class'] : ''}">${title}</div>

                    <div class="bd">
                      <div class="fixed">
                        <p>
                          ${body()}
                        </p>
                      </div>
                    </div>

                    <div class="actions">
                      <a href="#" class="accordionToggleItem">&nbsp;</a>
                      ${closeAction}
                    </div>
                </div>
            </div>
        """
    }

    def accordion = {attrs, body  ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        multiple: false,
                        persistent: false,
                        fade: false,
                        bounce: false,
                        slow: false,
                        vertical: false
                ],
                attrs,
        )
        def bounce = attrs.remove('bounce');
        def bounceStr = bounce ? ' rel="bounceOut" ' : ''
        def classString = "class=\"yui-cms-accordion fixIE ${displayTagLibService.attrsToClassString(attrs)}\""
        out << """
    		<div id="${attrs.id}" ${classString} $bounceStr>
    			${body()}
    		</div>
        """
    }

    def accordionElement = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        selected: false
                ],
                attrs,
                ['title']
        )
        out << """
            <div class="yui-cms-item yui-panel ${attrs.selected ? 'selected' : ''}">
                <div class="hd">${attrs.title}</div>
                <div class="bd">
                    <div id="${attrs.id}" class="fixed">
                        ${body()}
                    </div>
                </div>
                <div class="actions">
                    <a href="#" class="accordionToggleItem">&nbsp;</a>
                </div>
            </div>
        """
    }

    def toolTip = {attrs, body ->

        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                ],
                attrs
        )

        def id = attrs.remove('id')
        def text = attrs.remove('text')

        // if  the tooltip text is set, we can assume that we want to use it, not a server call
        if (text) {
            // surround body with span that includes the tooltip trigger
            out << "<span id='$id' class='yui-tip' title='${text}'>${body()}</span>"
        } else {
            def dataUrl
            try {
                dataUrl = createLink(attrs)
            } catch (Exception e) {
                throw new GrailsUIException("There was not enough information in the gui:toolTip tag to create the link." +
                  "  Either a 'text' attribute, 'controller'/'action' atributes, or a 'url' attribute is required.")
            }
            // otherwise create the span with an URL to call to populate the toolTip
            out << "<span id='$id' url='$dataUrl' class='yui-tip'>${body()}</span>"
        }
    }
    
    def overlay = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                ],
                attrs
        )
        def id = attrs.remove('id')
        def classes = attrs.remove('class')
        out << """
        <div id="${id}" style="visibility:hidden" class="${classes}">
    		<div class="hd"></div>
    		<div class="bd">${body()}</div>
    		<div class="ft"></div>
    	</div>
        <script>
            GRAILSUI.${id} = new YAHOO.widget.Overlay('${id}', {${grailsUITagLibService.mapToConfig attrs}});
            GRAILSUI.${id}.render(document.body);
        </script>"""
    }
}
