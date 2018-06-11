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

class DragAndDropTagLib {

    static namespace = "gui"
    def grailsUITagLibService
    def prepend = ''
    def lists

    /**
     * You must define a draggableListWorkArea to surround any draggableLists you want.  This takes only one optional
     * parameter:
     *
     * formReady: if true, writes out hidden inputs that contain dynamic list data for a form submission
     *
     */
    def draggableListWorkArea = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        formReady: false,
                ],
                attrs
        )
        // clear any old lists
        lists = [:]
        // process inner list tags
        out << body()
        
        def jsid = grailsUITagLibService.toJS(attrs.id)

        def hiddenInputData = [:]
        if (attrs.formReady) {
            lists.each {
                hiddenInputData."${it.key}" = "<input type='hidden' id='${it.key}_input' name='${it.key}_input' />\n"
            }
        }

        out << """
        ${hiddenInputData.values().join('');}
        <script>
            GRAILSUI.${jsid} = {
                init: function() {

                    ${
                        lists.collect { listItem ->
                            def result = "new YAHOO.util.DDTarget('${listItem.key}');\n"
                            listItem.value.each {
                                result += "var ${it}_dli = new GRAILSUI.DraggableListItem('${it}');\n"
                                result += "${it}_dli.on('endDragEvent', this.populateHiddenInput);\n"
                            }
                            result
                        }.join('')
                    }
                    this.populateHiddenInput();
                },

                populateHiddenInput: function() {
                    var parseListToHiddenInput = function(ul, input) {
                        var items = ul.getElementsByTagName("li");
                        var out = "";
                        for (i=0;i<items.length;i=i+1) {
                            out += items[i].id + " ";
                        }
                        YAHOO.util.Dom.get(input).value = out;
                    };

                    ${
                        lists.collect {listItem ->
                            "parseListToHiddenInput(YAHOO.util.Dom.get('${listItem.key}'), '${listItem.key}_input');\n"
                        }.join('')
                    }
                }
            };
            YAHOO.util.Event.onDOMReady(GRAILSUI.${jsid}.init, GRAILSUI.${jsid}, true);
        </script>
        """
    }

    /**
     * Used to create a list with items that can be dragged to all other draggableLists.
     *
     * id: (optional) list id
     * class: (optional) for styling
     */
    def draggableList = {attrs, body ->
        attrs = grailsUITagLibService.establishDefaultValues(
                [
                        id: grailsUITagLibService.getUniqueId(),
                        prepend: '',
                        'class':''
                ],
                attrs
        )
        // reset prepend
        prepend = attrs.remove('prepend')
        def liPattern = /(?s)<li\b[^>]*>(.*?)<\/li>/
        def rawBody = body()
        def liMatcher = rawBody =~ liPattern
        def parsedListContents = ''
        def listItem
        def count = 0
        def listItemIds = []
        for (match in liMatcher) {
            // check for an li id
            def liMatch = match[0]
            def textMatch = match[1]
            def itemId = "${prepend}${attrs.id}_${count++}"
            if(liMatch.contains('id=')) {
                // TODO: this could be a problem if someone types "id = '" with spaces
                def idPattern = /id=\"(.*)\"/
                def idMatcher = liMatch =~ idPattern
                def id = idMatcher[0][1]
                itemId = "${prepend}${id}_${count++}"
            }
            // TODO: check for an incoming class tag here in the future
            // remove the original li tags
            parsedListContents += "<li id='${itemId}' class='${attrs.'class'}'>$textMatch</li>\n"
            listItemIds << itemId
        }
        lists."${attrs.id}" = listItemIds
        out << """
        <div id="${attrs.id}_workarea" class="workarea">
            <ul id="${attrs.id}" class="draglist">
                ${parsedListContents}
            </ul>
        </div>
        """
    }

}
