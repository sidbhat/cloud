import org.codehaus.groovy.grails.web.pages.FastStringWriter;
import org.grails.formbuilder.DomainClass
/**
 *
 * @author <a href='mailto:admin@yourdomain.com'>Nikkishi</a>
 *
 * @since 0.1
 */
class FormBuilderGrailsPlugin {
    // the plugin version
    def version = "0.1"
    // the version or versions of Grails the plugin is designed for
    def grailsVersion = "1.2.2 > *"
    // the other plugins this plugin depends on
    def dependsOn = [
		  	jquery:'1.4.2.7 > *',
		  	jqueryUi:'1.8.6 > *',
			jqueryJson:'2.2 > *',
			freemarkerTags:'0.5.8 > *',
			uniForm:'1.5 > *',
			jqueryDatatables:'1.7.5 > *',
			jqueryValidationUi:'1.2 > *',
			langSelector:'0.3 > *'
			]
	
    // resources that are excluded from plugin packaging
    def pluginExcludes = [
        "grails-app/views/error.gsp"
    ]

    def author = "Nikkishi LLC"
    def authorEmail = "admin@yourdomain.com"
    def title = "Create Online Forms"
    def description = '''\
 The Form Builder allows creation of online forms in web browser 
 without any programming knowledge.
 
'''

    // URL to the plugin's documentation
    def documentation = "http://www.google.com"

    def doWithWebDescriptor = { xml ->
        // TODO Implement additions to web.xml (optional), this event occurs before 
    }

    def doWithSpring = {
        // TODO Implement runtime spring config (optional)
    }

    def doWithDynamicMethods = { ctx ->
        // TODO Implement registering dynamic methods to classes (optional)
    }

	def doWithApplicationContext = { applicationContext ->
		
	}

    def onChange = { event ->
        // TODO Implement code that is executed when any artefact that this plugin is
        // watching is modified and reloaded. The event contains: event.source,
        // event.application, event.manager, event.ctx, and event.plugin.
    }

    def onConfigChange = { event ->
        // TODO Implement code that is executed when the project configuration changes.
        // The event is the same as for 'onChange'.
    }
}
