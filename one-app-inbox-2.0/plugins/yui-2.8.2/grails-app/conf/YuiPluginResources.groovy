// Resource declarations for Resources plugin
modules = {
    def yuiDir = "js/yui"

    // YUI Reset CSS
    'yui-reset' {
        resource url: [plugin: "yui", dir: "${yuiDir}/reset", file: "reset-min.css"]
    }

    'yui-reset-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/reset", file: "reset.css"]
    }

    // YUI Fonts CSS
    'yui-fonts' {
        resource url: [plugin: "yui", dir: "${yuiDir}/fonts", file: "fonts-min.css"]
    }

    'yui-fonts-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/fonts", file: "fonts.css"]
    }

    // YUI Grids CSS
    'yui-grids' {
        resource url: [plugin: "yui", dir: "${yuiDir}/grids", file: "grids-min.css"]
    }

    'yui-grids-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/grids", file: "grids.css"]
    }

    // YUI Base CSS
    'yui-base' {
        resource url: [plugin: "yui", dir: "${yuiDir}/base", file: "base-min.css"]
    }

    'yui-base-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/base", file: "base.css"]
    }

    // YUI Utilities
    'yui-utilities' {
        resource url: [plugin: "yui", dir: "${yuiDir}/utilities", file: "utilities.js"],
                disposition: "head", exclude: ["minify"]
    }

    // YUI Yahoo
    'yui-yahoo' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yahoo", file: "yahoo-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-yahoo-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yahoo", file: "yahoo.js"],
                disposition: "head"
    }

    'yui-yahoo-debug' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yahoo", file: "yahoo-debug.js"],
                disposition: "head"
    }

    // YUI YUI Loader
    'yui-yuiloader' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yuiloader", file: "yuiloader-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-yuiloader-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yuiloader", file: "yuiloader.js"],
                disposition: "head"
    }

    'yui-yuiloader-debug' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yuiloader", file: "yuiloader-debug.js"],
                disposition: "head"
    }

    // YUI DOM
    'yui-dom' {
        resource url: [plugin: "yui", dir: "${yuiDir}/dom", file: "dom-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-dom-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/dom", file: "dom.js"],
                disposition: "head"
    }

    'yui-dom-debug' {
        resource url: [plugin: "yui", dir: "${yuiDir}/dom", file: "dom-debug.js"],
                disposition: "head"
    }

    // YUI Event
    'yui-event' {
        resource url: [plugin: "yui", dir: "${yuiDir}/event", file: "event-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-event-dev' {
        resource url: [plugin: "yui", dir: "${yuiDir}/event", file: "event.js"],
                disposition: "head"
    }

    'yui-event-debug' {
        resource url: [plugin: "yui", dir: "${yuiDir}/event", file: "event-debug.js"],
                disposition: "head"
    }

    // YUI Core - combines Yahoo, DOM, and Event
    'yui-core' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yahoo-dom-event", file: "yahoo-dom-event.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-core-dev' {
        dependsOn "yui-yahoo-dev", "yui-dom-dev", "yui-event-dev"
    }

    'yui-core-debug' {
        dependsOn "yui-yahoo-debug", "yui-dom-debug", "yui-event-debug"
    }

    // YUI Date Math
    'yui-datemath' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/datemath", file: "datemath-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-datemath-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/datemath", file: "datemath.js"],
                disposition: "head"
    }

    'yui-datemath-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/datemath", file: "datemath-debug.js"],
                disposition: "head"
    }

    // YUI Get
    'yui-get' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/get", file: "get-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-get-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/get", file: "get.js"],
                disposition: "head"
    }

    'yui-get-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/get", file: "get-debug.js"],
                disposition: "head"
    }

    // YUI JSON
    'yui-json' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/json", file: "json-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-json-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/json", file: "json.js"],
                disposition: "head"
    }

    'yui-json-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/json", file: "json-debug.js"],
                disposition: "head"
    }

    // YUI Profiler
    'yui-profiler' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/profiler", file: "profiler-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-profiler-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/profiler", file: "profiler.js"],
                disposition: "head"
    }

    'yui-profiler-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/profiler", file: "profiler-debug.js"],
                disposition: "head"
    }

    // YUI Stylesheet
    'yui-stylesheet' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/stylesheet", file: "stylesheet-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-stylesheet-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/stylesheet", file: "stylesheet.js"],
                disposition: "head"
    }

    'yui-stylesheet-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/stylesheet", file: "stylesheet-debug.js"],
                disposition: "head"
    }

    // YUI Swf Detect
    'yui-swfdetect' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/swfdetect", file: "swfdetect-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-swfdetect-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/swfdetect", file: "swfdetect.js"],
                disposition: "head"
    }

    'yui-swfdetect-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/swfdetect", file: "swfdetect-debug.js"],
                disposition: "head"
    }

    // YUI Element
    'yui-element' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/element", file: "element-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-element-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/element", file: "element.js"],
                disposition: "head"
    }

    'yui-element-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/element", file: "element-debug.js"],
                disposition: "head"
    }

    // YUI Event Mouse Enter
    'yui-event-mouseenter' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-mouseenter", file: "event-mouseenter-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-event-mouseenter-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-mouseenter", file: "event-mouseenter.js"],
                disposition: "head"
    }

    'yui-event-mouseenter-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-mouseenter", file: "event-mouseenter-debug.js"],
                disposition: "head"
    }

    // YUI Event Simulate
    'yui-event-simulate' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-simulate", file: "event-simulate-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-event-simulate-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-simulate", file: "event-simulate.js"],
                disposition: "head"
    }

    'yui-event-simulate-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-simulate", file: "event-simulate-debug.js"],
                disposition: "head"
    }

    // YUI History
    'yui-history' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/history", file: "history-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-history-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/history", file: "history.js"],
                disposition: "head"
    }

    'yui-history-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/history", file: "history-debug.js"],
                disposition: "head"
    }

    // YUI Selector
    'yui-selector' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/selector", file: "selector-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-selector-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/selector", file: "selector.js"],
                disposition: "head"
    }

    'yui-selector-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/selector", file: "selector-debug.js"],
                disposition: "head"
    }

    // YUI Event Delegate
    'yui-event-delegate' {
        dependsOn "yui-selector"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-delegate", file: "event-delegate-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-event-delegate-dev' {
        dependsOn "yui-selector-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-delegate", file: "event-delegate.js"],
                disposition: "head"
    }

    'yui-event-delegate-debug' {
        dependsOn "yui-selector-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/event-delegate", file: "event-delegate-debug.js"],
                disposition: "head"
    }

    // YUI Cookie
    'yui-cookie' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/cookie", file: "cookie-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-cookie-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/cookie", file: "cookie.js"],
                disposition: "head"
    }

    'yui-cookie-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/cookie", file: "cookie-debug.js"],
                disposition: "head"
    }

    // YUI Drag & Drop
    'yui-dragdrop' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/dragdrop", file: "dragdrop-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-dragdrop-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/dragdrop", file: "dragdrop.js"],
                disposition: "head"
    }

    'yui-dragdrop-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/dragdrop", file: "dragdrop-debug.js"],
                disposition: "head"
    }

    // YUI Connection
    'yui-connection' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/connection", file: "connection-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-connection-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/connection", file: "connection.js"],
                disposition: "head"
    }

    'yui-connection-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/connection", file: "connection-debug.js"],
                disposition: "head"
    }

    // YUI Datasource
    'yui-datasource' {
        dependsOn "yui-connection"

        resource url: [plugin: "yui", dir: "${yuiDir}/datasource", file: "datasource-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-datasource-dev' {
        dependsOn "yui-connection-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/datasource", file: "datasource.js"],
                disposition: "head"
    }

    'yui-datasource-debug' {
        dependsOn "yui-connection-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/datasource", file: "datasource-debug.js"],
                disposition: "head"
    }

    // YUI Animation
    'yui-animation' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/animation", file: "animation-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-animation-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/animation", file: "animation.js"],
                disposition: "head"
    }

    'yui-animation-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/animation", file: "animation-debug.js"],
                disposition: "head"
    }

    // YUI Container
    'yui-container' {
        dependsOn "yui-dragdrop", "yui-animation", "yui-connection"

        resource url: [plugin: "yui", dir: "${yuiDir}/container", file: "container-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/container/assets/skins/sam", file: "container.css"]
    }

    'yui-container-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/container/assets", file: "container-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/container/assets/skins/sam", file: "container-skin.css"]
    }

    'yui-container-dev' {
        dependsOn "yui-dragdrop-dev", "yui-animation-dev", "yui-connection-dev", "yui-container-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/container", file: "container.js"],
                disposition: "head"
    }

    'yui-container-debug' {
        dependsOn "yui-dragdrop-debug", "yui-animation-debug", "yui-connection-debug", "yui-container-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/container", file: "container-debug.js"],
                disposition: "head"
    }

    // YUI Image Loader
    'yui-imageloader' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/imageloader", file: "imageloader-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-imageloader-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/imageloader", file: "imageloader.js"],
                disposition: "head"
    }

    'yui-imageloader-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/imageloader", file: "imageloader-debug.js"],
                disposition: "head"
    }

    // YUI Logger
    'yui-logger' {
        dependsOn "yui-dragdrop"

        resource url: [plugin: "yui", dir: "${yuiDir}/logger", file: "logger-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/logger/assets/skins/sam", file: "logger.css"]
    }

    'yui-logger-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/logger/assets", file: "logger-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/logger/assets/skins/sam", file: "logger-skin.css"]
    }

    'yui-logger-dev' {
        dependsOn "yui-dragdrop-dev", "yui-logger-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/logger", file: "logger.js"],
                disposition: "head"
    }

    'yui-logger-debug' {
        dependsOn "yui-dragdrop-debug", "yui-logger-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/logger", file: "logger-debug.js"],
                disposition: "head"
    }

    // YUI Menu
    'yui-menu' {
        dependsOn "yui-container"

        resource url: [plugin: "yui", dir: "${yuiDir}/menu", file: "menu-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/menu/assets/skins/sam", file: "menu.css"]
    }

    'yui-menu-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/menu/assets", file: "menu-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/menu/assets/skins/sam", file: "menu-skin.css"]
    }

    'yui-menu-dev' {
        dependsOn "yui-container-dev", "yui-menu-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/menu", file: "menu.js"],
                disposition: "head"
    }

    'yui-menu-debug' {
        dependsOn "yui-container-debug", "yui-menu-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/menu", file: "menu-debug.js"],
                disposition: "head"
    }

    // YUI Paginator
    'yui-paginator' {
        dependsOn "yui-element"

        resource url: [plugin: "yui", dir: "${yuiDir}/paginator", file: "paginator-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/paginator/assets/skins/sam", file: "paginator.css"]
    }

    'yui-paginator-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/paginator/assets", file: "paginator-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/paginator/assets/skins/sam", file: "paginator-skin.css"]
    }

    'yui-paginator-dev' {
        dependsOn "yui-element-dev", "yui-paginator-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/paginator", file: "paginator.js"],
                disposition: "head"
    }

    'yui-paginator-debug' {
        dependsOn "yui-element-debug", "yui-paginator-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/paginator", file: "paginator-debug.js"],
                disposition: "head"
    }

    // YUI Resize
    'yui-resize' {
        dependsOn "yui-dragdrop", "yui-event-mouseenter", "yui-event-delegate", "yui-element", "yui-animation"

        resource url: [plugin: "yui", dir: "${yuiDir}/resize", file: "resize-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/resize/assets/skins/sam", file: "resize.css"]
    }

    'yui-resize-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/resize/assets", file: "resize-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/resize/assets/skins/sam", file: "resize-skin.css"]
    }

    'yui-resize-dev' {
        dependsOn "yui-dragdrop-dev",
                "yui-event-mouseenter",
                "yui-event-delegate",
                "yui-element",
                "yui-animation-dev",
                "yui-resize-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/resize", file: "resize.js"],
                disposition: "head"
    }

    'yui-resize-debug' {
        dependsOn "yui-dragdrop-debug",
                "yui-event-mouseenter",
                "yui-event-delegate",
                "yui-element",
                "yui-animation-debug",
                "yui-resize-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/resize", file: "resize-debug.js"],
                disposition: "head"
    }

    // YUI Slider
    'yui-slider' {
        dependsOn "yui-dragdrop", "yui-animation"

        resource url: [plugin: "yui", dir: "${yuiDir}/slider", file: "slider-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/slider/assets/skins/sam", file: "slider.css"]
    }

    'yui-slider-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/slider/assets", file: "slider-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/slider/assets/skins/sam", file: "slider-skin.css"]
    }

    'yui-slider-dev' {
        dependsOn "yui-dragdrop-dev", "yui-animation-dev", "yui-slider-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/slider", file: "slider.js"],
                disposition: "head"
    }

    'yui-slider-debug' {
        dependsOn "yui-dragdrop-debug", "yui-animation-debug", "yui-slider-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/slider", file: "slider-debug.js"],
                disposition: "head"
    }

    // YUI Auto Complete
    'yui-autocomplete' {
        dependsOn "yui-datasource", "yui-animation"

        resource url: [plugin: "yui", dir: "${yuiDir}/autocomplete", file: "autocomplete-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/autocomplete/assets/skins/sam", file: "autocomplete.css"]
    }

    'yui-autocomplete-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/autocomplete/assets", file: "autocomplete-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/autocomplete/assets/skins/sam", file: "autocomplete-skin.css"]
    }

    'yui-autocomplete-dev' {
        dependsOn "yui-datasource-dev", "yui-animation-dev", "yui-autocomplete-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/autocomplete", file: "autocomplete.js"],
                disposition: "head"
    }

    'yui-autocomplete-debug' {
        dependsOn "yui-datasource-debug", "yui-animation-debug", "yui-autocomplete-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/autocomplete", file: "autocomplete-debug.js"],
                disposition: "head"
    }

    // YUI Button
    'yui-button' {
        dependsOn "yui-element", "yui-menu"

        resource url: [plugin: "yui", dir: "${yuiDir}/button", file: "button-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/button/assets/skins/sam", file: "button.css"]
    }

    'yui-button-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/button/assets", file: "button-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/button/assets/skins/sam", file: "button-skin.css"]
    }

    'yui-button-dev' {
        dependsOn "yui-element-dev", "yui-menu-dev", "yui-button-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/button", file: "button.js"],
                disposition: "head"
    }

    'yui-button-debug' {
        dependsOn "yui-element-debug", "yui-menu-debug", "yui-button-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/button", file: "button-debug.js"],
                disposition: "head"
    }

    // YUI Calendar
    'yui-calendar' {
        dependsOn "yui-core"

        resource url: [plugin: "yui", dir: "${yuiDir}/calendar", file: "calendar-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/calendar/assets/skins/sam", file: "calendar.css"]
    }

    'yui-calendar-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/calendar/assets", file: "calendar-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/calendar/assets/skins/sam", file: "calendar-skin.css"]
    }

    'yui-calendar-dev' {
        dependsOn "yui-core-dev", "yui-calendar-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/calendar", file: "calendar.js"],
                disposition: "head"
    }

    'yui-calendar-debug' {
        dependsOn "yui-core-debug", "yui-calendar-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/calendar", file: "calendar-debug.js"],
                disposition: "head"
    }

    // YUI Carousel
    'yui-carousel' {
        dependsOn "yui-element", "yui-animation"

        resource url: [plugin: "yui", dir: "${yuiDir}/carousel", file: "carousel-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/carousel/assets/skins/sam", file: "carousel.css"]
    }

    'yui-carousel-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/carousel/assets", file: "carousel-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/carousel/assets/skins/sam", file: "carousel-skin.css"]
    }

    'yui-carousel-dev' {
        dependsOn "yui-element-dev", "yui-animation-dev", "yui-carousel-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/carousel", file: "carousel.js"],
                disposition: "head"
    }

    'yui-carousel-debug' {
        dependsOn "yui-element-debug", "yui-animation-debug", "yui-carousel-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/carousel", file: "carousel-debug.js"],
                disposition: "head"
    }

    // YUI Charts
    'yui-charts' {
        dependsOn "yui-element", "yui-json", "yui-datasource"

        resource url: [plugin: "yui", dir: "${yuiDir}/charts", file: "charts-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-charts-dev' {
        dependsOn "yui-element-dev", "yui-json-dev", "yui-datasource-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/charts", file: "charts.js"],
                disposition: "head"
    }

    'yui-charts-debug' {
        dependsOn "yui-element-debug", "yui-json-debug", "yui-datasource-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/charts", file: "charts-debug.js"],
                disposition: "head"
    }

    // YUI Colorpicker
    'yui-colorpicker' {
        dependsOn "yui-element", "yui-slider"

        resource url: [plugin: "yui", dir: "${yuiDir}/colorpicker", file: "colorpicker-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/colorpicker/assets/skins/sam", file: "colorpicker.css"]
    }

    'yui-colorpicker-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/colorpicker/assets", file: "colorpicker-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/colorpicker/assets/skins/sam", file: "colorpicker-skin.css"]
    }

    'yui-colorpicker-dev' {
        dependsOn "yui-element-dev", "yui-slider-dev", "yui-colorpicker-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/colorpicker", file: "colorpicker.js"],
                disposition: "head"
    }

    'yui-colorpicker-debug' {
        dependsOn "yui-element-debug", "yui-slider-debug", "yui-colorpicker-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/colorpicker", file: "colorpicker-debug.js"],
                disposition: "head"
    }

    // YUI Data Table
    'yui-datatable' {
        dependsOn "yui-datasource", "yui-calendar", "yui-dragdrop", "yui-paginator"

        resource url: [plugin: "yui", dir: "${yuiDir}/datatable", file: "datatable-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/datatable/assets/skins/sam", file: "datatable.css"]
    }

    'yui-datatable-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/datatable/assets", file: "datatable-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/datatable/assets/skins/sam", file: "datatable-skin.css"]
    }

    'yui-datatable-dev' {
        dependsOn "yui-datasource-dev",
                "yui-calendar-dev",
                "yui-dragdrop-dev",
                "yui-paginator-dev",
                "yui-datatable-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/datatable", file: "datatable.js"],
                disposition: "head"
    }

    'yui-datatable-debug' {
        dependsOn "yui-datasource-debug",
                "yui-calendar-debug",
                "yui-dragdrop-debug",
                "yui-paginator-debug",
                "yui-datatable-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/datatable", file: "datatable-debug.js"],
                disposition: "head"
    }

    // YUI Editor
    'yui-editor' {
        dependsOn "yui-event-mouseenter", "yui-event-delegate", "yui-button"

        resource url: [plugin: "yui", dir: "${yuiDir}/editor", file: "editor-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/editor/assets/skins/sam", file: "editor.css"]
    }

    'yui-editor-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/editor/assets", file: "editor-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/editor/assets/skins/sam", file: "editor-skin.css"]
    }

    'yui-editor-dev' {
        dependsOn "yui-event-mouseenter-dev",
                "yui-event-delegate-dev",
                "yui-button-dev",
                "yui-editor-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/editor", file: "editor.js"],
                disposition: "head"
    }

    'yui-editor-debug' {
        dependsOn "yui-event-mouseenter-debug",
                "yui-event-delegate-debug",
                "yui-button-debug",
                "yui-editor-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/editor", file: "editor-debug.js"],
                disposition: "head"
    }

    // YUI Element Delegate
    'yui-element-delegate' {
        dependsOn "yui-event-mouseenter", "yui-event-delegate", "yui-element"

        resource url: [plugin: "yui", dir: "${yuiDir}/element-delegate", file: "element-delegate-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-element-delegate-dev' {
        dependsOn "yui-event-mouseenter-dev", "yui-event-delegate-dev", "yui-element-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/element-delegate", file: "element-delegate.js"],
                disposition: "head"
    }

    'yui-element-delegate-debug' {
        dependsOn "yui-event-mouseenter-debug", "yui-event-delegate-debug", "yui-element-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/element-delegate", file: "element-delegate-debug.js"],
                disposition: "head"
    }

    // YUI Image Cropper
    'yui-imagecropper' {
        dependsOn "yui-resize"

        resource url: [plugin: "yui", dir: "${yuiDir}/imagecropper", file: "imagecropper-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/imagecropper/assets/skins/sam", file: "imagecropper.css"]
    }

    'yui-imagecropper-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/imagecropper/assets", file: "imagecropper-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/imagecropper/assets/skins/sam", file: "imagecropper-skin.css"]
    }

    'yui-imagecropper-dev' {
        dependsOn "yui-resize-dev", "yui-imagecropper-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/imagecropper", file: "imagecropper.js"],
                disposition: "head"
    }

    'yui-imagecropper-debug' {
        dependsOn "yui-resize-debug", "yui-imagecropper-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/imagecropper", file: "imagecropper-debug.js"],
                disposition: "head"
    }

    // YUI Layout
    'yui-layout' {
        dependsOn "yui-resize"

        resource url: [plugin: "yui", dir: "${yuiDir}/layout", file: "layout-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/layout/assets/skins/sam", file: "layout.css"]
    }

    'yui-layout-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/layout/assets", file: "layout-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/layout/assets/skins/sam", file: "layout-skin.css"]
    }

    'yui-layout-dev' {
        dependsOn "yui-resize-dev", "yui-layout-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/layout", file: "layout.js"],
                disposition: "head"
    }

    'yui-layout-debug' {
        dependsOn "yui-resize-debug", "yui-layout-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/layout", file: "layout-debug.js"],
                disposition: "head"
    }

    // YUI Profiler Viewer
    'yui-profilerviewer' {
        dependsOn "yui-profiler", "yui-event-mouseenter", "yui-event-delegate", "yui-element"
        dependsOn "yui-resize"

        resource url: [plugin: "yui", dir: "${yuiDir}/profilerviewer", file: "profilerviewer-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/profilerviewer/assets/skins/sam", file: "profilerviewer.css"]
    }

    'yui-profilerviewer-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/profilerviewer/assets", file: "profilerviewer-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/profilerviewer/assets/skins/sam", file: "profilerviewer-skin.css"]
    }

    'yui-profilerviewer-dev' {
        dependsOn "yui-profiler-dev",
                "yui-event-mouseenter-dev",
                "yui-event-delegate-dev",
                "yui-element-dev",
                "yui-profilerviewer-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/profilerviewer", file: "profilerviewer.js"],
                disposition: "head"
    }

    'yui-profilerviewer-debug' {
        dependsOn "yui-profiler-debug",
                "yui-event-mouseenter-debug",
                "yui-event-delegate-debug",
                "yui-element-debug",
                "yui-profilerviewer-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/profilerviewer", file: "profilerviewer-debug.js"],
                disposition: "head"
    }

    // YUI Progress Bar
    'yui-progressbar' {
        dependsOn "yui-event-mouseenter", "yui-event-delegate", "yui-element", "yui-animation"

        resource url: [plugin: "yui", dir: "${yuiDir}/progressbar", file: "progressbar-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/progressbar/assets/skins/sam", file: "progressbar.css"]
    }

    'yui-progressbar-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/progressbar/assets", file: "progressbar-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/progressbar/assets/skins/sam", file: "progressbar-skin.css"]
    }

    'yui-progressbar-dev' {
        dependsOn "yui-event-mouseenter-dev",
                "yui-event-delegate-dev",
                "yui-element-dev",
                "yui-animation-dev",
                "yui-progressbar-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/progressbar", file: "progressbar.js"],
                disposition: "head"
    }

    'yui-progressbar-debug' {
        dependsOn "yui-event-mouseenter-debug",
                "yui-event-delegate-debug",
                "yui-element-debug",
                "yui-animation-debug",
                "yui-progressbar-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/progressbar", file: "progressbar-debug.js"],
                disposition: "head"
    }

    // YUI Simple Editor
    'yui-simpleeditor' {
        dependsOn "yui-event-mouseenter", "yui-event-delegate", "yui-button"

        resource url: [plugin: "yui", dir: "${yuiDir}/editor", file: "simpleeditor-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/editor/assets/skins/sam", file: "simpleeditor.css"]
    }

    'yui-simpleeditor-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/editor/assets", file: "simpleeditor-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/editor/assets/skins/sam", file: "simpleeditor-skin.css"]
    }

    'yui-simpleeditor-dev' {
        dependsOn "yui-event-mouseenter-dev",
                "yui-event-delegate-dev",
                "yui-button-dev",
                "yui-simpleeditor-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/editor", file: "simpleeditor.js"],
                disposition: "head"
    }

    'yui-simpleeditor-debug' {
        dependsOn "yui-event-mouseenter-debug",
                "yui-event-delegate-debug",
                "yui-button-debug",
                "yui-simpleeditor-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/editor", file: "simpleeditor-debug.js"],
                disposition: "head"
    }

    // YUI Swf
    'yui-swf' {
        dependsOn "yui-event-mouseenter", "yui-event-delegate", "yui-element"

        resource url: [plugin: "yui", dir: "${yuiDir}/swf", file: "swf-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-swf-dev' {
        dependsOn "yui-event-mouseenter-dev", "yui-event-delegate-dev", "yui-element-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/swf", file: "swf.js"],
                disposition: "head"
    }

    'yui-swf-debug' {
        dependsOn "yui-event-mouseenter-debug", "yui-event-delegate-debug", "yui-element-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/swf", file: "swf-debug.js"],
                disposition: "head"
    }

    // YUI Swf Store
    'yui-swfstore' {
        dependsOn "yui-cookie", "yui-swf"

        resource url: [plugin: "yui", dir: "${yuiDir}/swfstore", file: "swfstore-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-swfstore-dev' {
        dependsOn "yui-cookie-dev", "yui-swf-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/swfstore", file: "swfstore.js"],
                disposition: "head"
    }

    'yui-swfstore-debug' {
        dependsOn "yui-cookie-debug", "yui-swf-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/swfstore", file: "swfstore-debug.js"],
                disposition: "head"
    }

    // YUI Storage
    'yui-storage' {
        dependsOn "yui-swfstore"

        resource url: [plugin: "yui", dir: "${yuiDir}/storage", file: "storage-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-storage-dev' {
        dependsOn "yui-swfstore-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/storage", file: "storage.js"],
                disposition: "head"
    }

    'yui-storage-debug' {
        dependsOn "yui-swfstore-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/storage", file: "storage-debug.js"],
                disposition: "head"
    }

    // YUI Tab View
    'yui-tabview' {
        dependsOn "yui-element", "yui-menu"

        resource url: [plugin: "yui", dir: "${yuiDir}/tabview", file: "tabview-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/tabview/assets/skins/sam", file: "tabview.css"]
    }

    'yui-tabview-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/tabview/assets", file: "tabview-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/tabview/assets/skins/sam", file: "tabview-skin.css"]
    }

    'yui-tabview-dev' {
        dependsOn "yui-element-dev", "yui-menu-dev", "yui-tabview-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/tabview", file: "tabview.js"],
                disposition: "head"
    }

    'yui-tabview-debug' {
        dependsOn "yui-element-debug", "yui-menu-debug", "yui-tabview-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/tabview", file: "tabview-debug.js"],
                disposition: "head"
    }

    // YUI Tree View
    'yui-treeview' {
        dependsOn "yui-json", "yui-animation", "yui-calendar"

        resource url: [plugin: "yui", dir: "${yuiDir}/treeview", file: "treeview-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/treeview/assets/skins/sam", file: "treeview.css"]
    }

    'yui-treeview-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/treeview/assets", file: "treeview-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/treeview/assets/skins/sam", file: "treeview-skin.css"]
    }

    'yui-treeview-dev' {
        dependsOn "yui-json-dev", "yui-animation-dev", "yui-calendar-dev", "yui-treeview-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/treeview", file: "treeview.js"],
                disposition: "head"
    }

    'yui-treeview-debug' {
        dependsOn "yui-json-debug", "yui-animation-debug", "yui-calendar-debug", "yui-treeview-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/treeview", file: "treeview-debug.js"],
                disposition: "head"
    }

    // YUI Uploader
    'yui-uploader' {
        dependsOn "yui-event-mouseenter", "yui-event-delegate", "yui-element"

        resource url: [plugin: "yui", dir: "${yuiDir}/uploader", file: "uploader-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'yui-uploader-dev' {
        dependsOn "yui-event-mouseenter-dev",
                "yui-event-delegate-dev",
                "yui-element-dev"

        resource url: [plugin: "yui", dir: "${yuiDir}/uploader", file: "uploader.js"],
                disposition: "head"
    }

    'yui-uploader-debug' {
        dependsOn "yui-event-mouseenter-debug",
                "yui-event-delegate-debug",
                "yui-element-debug"

        resource url: [plugin: "yui", dir: "${yuiDir}/uploader", file: "uploader-debug.js"],
                disposition: "head"
    }

    // YUI Test
    'yui-yuitest' {
        dependsOn "yui-event-mouseenter", "yui-event-delegate", "yui-element"

        resource url: [plugin: "yui", dir: "${yuiDir}/yuitest", file: "yuitest-min.js"],
                disposition: "head", exclude: ["minify"]
        resource url: [plugin: "yui", dir: "${yuiDir}/yuitest/assets/skins/sam", file: "yuitest.css"]
    }

    'yui-yuitest-dev-assets' {
        resource url: [plugin: "yui", dir: "${yuiDir}/yuitest/assets", file: "yuitest-core.css"]
        resource url: [plugin: "yui", dir: "${yuiDir}/yuitest/assets/skins/sam", file: "yuitest-skin.css"]
    }

    'yui-yuitest-dev' {
        dependsOn "yui-event-mouseenter-dev",
                "yui-event-delegate-dev",
                "yui-element-dev",
                "yui-yuitest-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/yuitest", file: "yuitest.js"],
                disposition: "head"
    }

    'yui-yuitest-debug' {
        dependsOn "yui-event-mouseenter-debug",
                "yui-event-delegate-debug",
                "yui-element-debug",
                "yui-yuitest-dev-assets"

        resource url: [plugin: "yui", dir: "${yuiDir}/yuitest", file: "yuitest-debug.js"],
                disposition: "head"
    }
}
