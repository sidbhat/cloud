// Resource declarations for Resources plugin
modules = {
    def yuiDir = "js/yui-cms"

    // Bubbling
    'bubbling-core' {
        dependsOn "yui-core"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/bubbling", file: "bubbling-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-core-dev' {
        dependsOn "yui-core-dev"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/bubbling", file: "bubbling.js"],
                disposition: "head"
    }

    'bubbling-core-debug' {
        dependsOn "yui-core-debug"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/bubbling", file: "bubbling-debug.js"],
                disposition: "head"
    }

    // Dispatcher
    'bubbling-dispatcher' {
        dependsOn "yui-connection"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/dispatcher", file: "dispatcher-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-dispatcher-dev' {
        dependsOn "yui-connection-dev"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/dispatcher", file: "dispatcher.js"],
                disposition: "head"
    }

    'bubbling-dispatcher-debug' {
        dependsOn "yui-connection-debug"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/dispatcher", file: "dispatcher-debug.js"],
                disposition: "head"
    }

    // Lighter
    'bubbling-lighter' {
        dependsOn "bubbling-core"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/lighter", file: "lighter-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-lighter-dev' {
        dependsOn "bubbling-core-dev"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/lighter", file: "lighter.js"],
                disposition: "head"
    }

    'bubbling-lighter-debug' {
        dependsOn "bubbling-core-debug"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/lighter", file: "lighter-debug.js"],
                disposition: "head"
    }

    // Loading
    'bubbling-loading' {
        dependsOn "bubbling-core"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/loading", file: "loading-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-loading-dev' {
        dependsOn "bubbling-core-dev"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/loading", file: "loading.js"],
                disposition: "head"
    }

    'bubbling-loading-debug' {
        dependsOn "bubbling-core-debug"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/loading", file: "loading-debug.js"],
                disposition: "head"
    }

    // Tool tips
    'bubbling-tooltips' {
        dependsOn "bubbling-core"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/tooltips", file: "tooltips-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-tooltips-dev' {
        dependsOn "bubbling-core-dev"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/tooltips", file: "tooltips.js"],
                disposition: "head"
    }

    'bubbling-tooltips-debug' {
        dependsOn "bubbling-core-debug"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/tooltips", file: "tooltips-debug.js"],
                disposition: "head"
    }

    // Wizard
    'bubbling-wizard' {
        dependsOn "bubbling-dispatcher"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/wizard", file: "wizard-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-wizard-dev' {
        dependsOn "bubbling-dispatcher-dev"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/wizard", file: "wizard.js"],
                disposition: "head"
    }

    'bubbling-wizard-debug' {
        dependsOn "bubbling-dispatcher-debug"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/wizard", file: "wizard-debug.js"],
                disposition: "head"
    }

    // Navigation
    'bubbling-navigation' {
        dependsOn "yui-history", "bubbling-core"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/navigation", file: "navigation-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-navigation-dev' {
        dependsOn "yui-history-dev", "bubbling-core-dev"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/navigation", file: "navigation.js"],
                disposition: "head"
    }

    'bubbling-navigation-debug' {
        dependsOn "yui-history-debug", "bubbling-core-debug"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/navigation", file: "navigation-debug.js"],
                disposition: "head"
    }

    // Accordion
    'bubbling-accordion-assets' {
        resource url: [plugin: "bubbling", dir: "${yuiDir}/accordion/assets", file: "accordion.css"]
    }

    'bubbling-accordion' {
        dependsOn "bubbling-core", "bubbling-accordion-assets"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/accordion", file: "accordion-min.js"],
                disposition: "head", exclude: ["minify"]
    }

    'bubbling-accordion-dev' {
        dependsOn "bubbling-core-dev", "bubbling-accordion-assets"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/accordion", file: "accordion.js"],
                disposition: "head"
    }

    'bubbling-accordion-debug' {
        dependsOn "bubbling-core-debug", "bubbling-accordion-assets"

        resource url: [plugin: "bubbling", dir: "${yuiDir}/accordion", file: "accordion-debug.js"],
                disposition: "head"
    }
}
