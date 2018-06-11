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
import org.codehaus.groovy.grails.plugins.PluginManagerHolder
 
tags {
    autoComplete {
        styleSheetKeys = ["fonts", "container", "autocomplete", "grails-ui"]
        javascriptKeys = ["yahoo-dom-event", "datasource", "connection", "autocomplete"]
    }
    datePicker {
        styleSheetKeys = ["fonts", "container", "calendar", "grails-ui", "datepicker-with-time"]
        javascriptKeys = ["yahoo-dom-event", "utilities", "container", "calendar","datepicker-with-time"]
    }
    tabView {
        styleSheetKeys = ["fonts", "tabview"]
        javascriptKeys = ["yahoo-dom-event", "connection", "element", "tabview"]
    }
    richEditor {
        styleSheetKeys = ["fonts", "sam"]
        javascriptKeys = ["yahoo-dom-event", "element", "container", "editor"]
    }
    expandablePanel {
        styleSheetKeys = ["fonts", "sam", "container", "accordion"]
        javascriptKeys = ["utilities", "bubbling", "accordion"]
    }
    accordion {
        styleSheetKeys = ['fonts', 'container','accordion', 'grails-ui']
        javascriptKeys = ['utilities', 'bubbling', 'accordion']
    }
    dataTable {
        styleSheetKeys = ["fonts", "datatable", "paginator", "container", "expandable-datatable", "grails-ui"]
        javascriptKeys = ["utilities", "datasource", "datatable", "paginator", "expandable-datatable", "connection", "container", "menu", "loadingDialog"]
    }
    dialog {
        styleSheetKeys = ["fonts", "container", "button"]
        javascriptKeys = ["yahoo-dom-event", "element", "connection", "dragdrop", "element", "button", "container"]
    }
    draggableList {
        styleSheetKeys = ["fonts", "sam"]
        javascriptKeys = ["yahoo-dom-event", "animation", "dragdrop", "draggableList"]
    }
    toolTip {
        styleSheetKeys = ["fonts", "container"]
        javascriptKeys = ["utilities", "connection", "container", "bubbling", "tooltips"]
    }
    menu {
        styleSheetKeys = ["fonts", "menu"]
        javascriptKeys = ["yahoo-dom-event", "container", "menu"]
    }
    chart {
        javascriptKeys = ["yahoo-dom-event", "element", "datasource", "json", "connection", "charts", "swf"]
        flashKeys = ["charts"]
    }
    overlay {
        styleSheetKeys = ["fonts", "container"]
        javascriptKeys = ["yahoo-dom-event", "container"]
    }
}

resources {
    
    yui {
        // finding out what version of YUI we have installed
        def yuiVersion = PluginManagerHolder.pluginManager.getGrailsPlugin('yui').version
        // for plugin version 2.6.*, the YUI lib version is 2.6.0
        // for any other plugin version, we'll use the plugin version, minus any digits after x.x.x
        //  ex: 2.7.0.1 ==> 2.7.0
        def yuiLibVersion = yuiVersion.startsWith('2.6') ? '2.6.0' : yuiVersion[0..4]
        version = yuiLibVersion
        css {
            sam = "assets/skins/sam/skin.css"
            reset_fonts_grids = "reset-fonts-grids/reset-fonts-grids.css"
            base = "base/base-min.css"
            autocomplete = "autocomplete/assets/skins/sam/autocomplete.css"
            container = "container/assets/skins/sam/container.css"
            fonts = "fonts/fonts.css"
            menu = "menu/assets/skins/sam/menu.css"
            button = "button/assets/skins/sam/button.css"
            calendar = "calendar/assets/skins/sam/calendar.css"
            colorpicker = "colorpicker/assets/skins/sam/colorpicker.css"
            datatable = "datatable/assets/skins/sam/datatable.css"
            paginator = "paginator/assets/skins/sam/paginator.css"
            editor = "editor/assets/skins/sam/editor.css"
            resize = "resize/assets/skins/sam/resize.css"
            imagecropper = "imagecropper/assets/skins/sam/imagecropper.css"
            layout = "layout/assets/skins/sam/layout.css"
            logger = "logger/assets/skins/sam/logger.css"
            profilerviewer = "profilerviewer/assets/skins/sam/profilerviewer.css"
            tabview = "tabview/assets/skins/sam/tabview.css"
            treeview = "treeview/assets/skins/sam/treeview.css"
            yuitest = "yuitest/assets/skins/sam/yuitest.css"
            editor = "editor/assets/skins/sam/simpleeditor.css"
        }
        js {
            yahoo_dom_event = "yahoo-dom-event/yahoo-dom-event"
            yahoo = "yahoo/yahoo"
            dom = "dom/dom"
            event = "event/event"
            utilities = "utilities/utilities"
            element = "element/element"
            animation = "animation/animation"
            dragdrop = "dragdrop/dragdrop"
            get = "get/get"
            yuiloader = "yuiloader/yuiloader"
            autocomplete = "autocomplete/autocomplete"
            container = "container/container"
            menu = "menu/menu"
            button = "button/button"
            calendar = "calendar/calendar"
            connection = "connection/connection"
            datasource = "datasource/datasource"
            json = "json/json"
            charts = "charts/charts"
            slider = "slider/slider"
            colorpicker = "colorpicker/colorpicker"
            cookie = "cookie/cookie"
            datatable = "datatable/datatable"
            paginator = "paginator/paginator"
            editor = "editor/editor"
            history = "history/history"
            resize = "resize/resize"
            imagecropper = "imagecropper/imagecropper"
            imageloader = "imageloader/imageloader"
            selector = "selector/selector-beta"
            layout = "layout/layout"
            logger = "logger/logger"
            profiler = "profiler/profiler"
            profilerviewer = "profilerviewer/profilerviewer"
            tabview = "tabview/tabview"
            treeview = "treeview/treeview"
            uploader = "uploader/uploader"
            yuitest = "yuitest/yuitest"
            swf = "swf/swf"
        }
        swf {
            charts = "charts/assets/charts.swf"
        }
    }

    yui_cms {
        version = "2-1"
        css {
            accordion = "accordion/assets/accordion.css"
        }
        js {
            bubbling = "bubbling/bubbling"
            accordion = "accordion/accordion"
            dispatcher = "dispatcher/dispatcher"
        }
    }
    
    grailsui {
        css {
            grails_ui = "../../css/grailsui/grails-ui.css"
            expandable_datatable = "../../css/grailsui/datatable.css"
            datepicker_with_time = "../../css/grailsui/datepicker.css"
        }
        js {
            grailsui = "grailsui"
            expandable_datatable = "DataTable"
            datepicker_with_time="SimpleDateFormat"
            draggableList = "DraggableListItem"
            loadingDialog = "LoadingDialog"
            tooltips = "ToolTips" // this overrides the bubbling Tooltips.js to prevent A link tips
        }
    }
}

mode {
    minimal = "-min.js"
    debug = "-debug.js"
    //TODO:determine what should be output for raw mode
    raw = ".js"
}

rollUps {
        yahoo_dom_event = ["yahoo","dom","event"]
        utilities = ["yahoo","dom","event", "animation", "dragdrop", "element", "get", "yuiloader"]
}

