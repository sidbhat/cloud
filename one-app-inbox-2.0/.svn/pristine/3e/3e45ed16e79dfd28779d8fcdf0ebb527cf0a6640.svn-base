// Resource declarations for Resources plugin
modules = {
    'grailsui-styles' {
        resource url: [plugin: "grails-ui", dir: "css/grailsui", file: "grails-ui.css"]
    }

    'grailsui-core' {
        dependsOn "yui-core"

        resource url: [plugin: "grails-ui", dir: "js/grailsui", file: "grailsui.js"], disposition: "head"
    }

    'grailsui-tabview' {
        dependsOn "yui-fonts", "yui-tabview", "grailsui-core"
    }

    'grailsui-dialog' {
        dependsOn "yui-fonts", "yui-button", "grailsui-core"
    }

    'grailsui-autocomplete' {
        dependsOn "yui-fonts", "grailsui-styles", "yui-autocomplete"
    }

    /*
    'grailsui-richEditor {
        styleSheetKeys = ["fonts", "sam"]
        javascriptKeys = ["yahoo-dom-event", "element", "container", "editor"]
    }
    'grailsui-expandablePanel {
        styleSheetKeys = ["fonts", "sam", "container", "accordion"]
        javascriptKeys = ["utilities", "bubbling", "accordion"]
    }
    'grailsui-accordion {
        styleSheetKeys = ['fonts', 'container','accordion', 'grails-ui']
        javascriptKeys = ['utilities', 'bubbling', 'accordion']
    }
    'grailsui-dataTable {
        styleSheetKeys = ["fonts", "datatable", "paginator", "container", "expandable-datatable", "grails-ui"]
        javascriptKeys = ["utilities", "datasource", "datatable", "paginator", "expandable-datatable", "connection", "container", "menu", "loadingDialog"]
    }
    'grailsui-dialog {
        styleSheetKeys = ["fonts", "container", "button"]
        javascriptKeys = ["yahoo-dom-event", "element", "connection", "dragdrop", "element", "button", "container"]
    }
    'grailsui-draggableList {
        styleSheetKeys = ["fonts", "sam"]
        javascriptKeys = ["yahoo-dom-event", "animation", "dragdrop", "draggableList"]
    }
    'grailsui-toolTip {
        styleSheetKeys = ["fonts", "container"]
        javascriptKeys = ["utilities", "connection", "container", "bubbling", "tooltips"]
    }
    'grailsui-menu {
        styleSheetKeys = ["fonts", "menu"]
        javascriptKeys = ["yahoo-dom-event", "container", "menu"]
    }
    'grailsui-chart {
        javascriptKeys = ["yahoo-dom-event", "element", "datasource", "json", "connection", "charts", "swf"]
        flashKeys = ["charts"]
    }
    'grailsui-overlay {
        styleSheetKeys = ["fonts", "container"]
        javascriptKeys = ["yahoo-dom-event", "container"]
    }
    */
}
