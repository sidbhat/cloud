import org.codehaus.groovy.grails.plugins.web.taglib.JavascriptTagLib
import org.icepush.PushContext
import org.springframework.web.context.request.RequestContextHolder as RCH
import org.springframework.context.ApplicationContext
import org.codehaus.groovy.grails.web.context.ServletContextHolder as SCH

class IcepushGrailsPlugin {
  // the plugin version
  def version = "0.4"//beta1
  // the version or versions of Grails the plugin is designed for
  def grailsVersion = "1.2.0 > *"
  // the other plugins this plugin depends on
  def dependsOn = [:]
  // resources that are excluded from plugin packaging
  def pluginExcludes = [
          "grails-app/views/**","grails-app/conf/**",
          "web-app/**"
  ]

  

  def observe = ["services", "controllers", "icefaces2"]

  def loadAfter = ["controllers", "services", "icefaces2"]

  def author = "Stephane MALDINI"
  def authorEmail = "smaldini@doc4web.com"
  def title = "ICEpush integration"
  def description = '''\\
ICEpush is a notification framework, it doesn't carry data unlike Cometd. However you can register callbacks such as page/fragment refresh and script invokation. By combining 1 Tag and 1 Call (from javascript/ser/ver side), you add a new dimension in your webware.
You can push, join/leave channels from controllers/gsp and add support for services.

This plugin can be mixed with the coming ICEfaces 2 integration to allow collaborative application scaffolding and push capabilities for JSF2.
'''

  // URL to the plugin's documentation
  def documentation = "http://grails.org/plugin/icepush"

  def doWithWebDescriptor = { xml ->
    //if (!manager.hasGrailsPlugin("icefaces2")) {
    def servlets = xml.servlet[0]
    def mappings = xml.'servlet-mapping'[0]

    servlets + {
      servlet {
        'servlet-name'('icepush')
        'servlet-class'('org.icepush.servlet.ICEpushServlet')
        'load-on-startup'(1)
      }
    }
    mappings + {
      'servlet-mapping' {
        'servlet-name'('icepush')
        'url-pattern'('*.icepush')
      }
    }
    //}
  }

  def doWithSpring = {

  }

  def doWithDynamicMethods = { ctx ->
    JavascriptTagLib.LIBRARY_MAPPINGS.icepush = ["code.icepush"]

    if (manager.hasGrailsPlugin("controllers")) {
      for (bean in application.controllerClasses)
        addPushMethods(bean.metaClass, ctx)
    }
     if (application.config.icepush?.injectServices && manager.hasGrailsPlugin("services")) {
      for (bean in application.serviceClasses)
        addPushMethods(bean.metaClass, ctx)
    }

    //JavascriptTagLib.PROVIDER_MAPPINGS.icepush = ICEpushProvider.class
  }

  private addPushMethods(MetaClass mc, ApplicationContext ctx) {

    def pc = {->
      PushContext.getInstance(SCH.servletContext)
    }
    def pushid = {->
		pc().createPushId(RCH.currentRequestAttributes().currentRequest, RCH.currentRequestAttributes().response)
    }

    mc.getPushContext = {->
      pc()
    }

    mc.getPushId = {->
      pushid()
    }

    mc.addToPushGroup = {String name ->
      def pushId = pushid()
      pc().addGroupMember name, pushId
      pushId
    }
    mc.addToPushGroup = {String name, String _pushid ->
      pc().addGroupMember name, _pushid
    }
    mc.removeFromPushGroup = {String name, String _pushid ->
      pc().addGroupMember name, _pushid
    }

    mc.push = {
      pc().push "context-push"
    }

    mc.push = {String s ->
      pc().push s
    }

    mc.pushOthers = {String s ->
      def pushContext = pc()
      def cookieValue = pushContext.getBrowserIDFromCookie(RCH.currentRequestAttributes().currentRequest)+':'

      def idList = pushContext.pushGroupManager.groupMap?.getAt(s)?.pushIDs?.findAll{id->
        !id.startsWith(cookieValue)
      } as String[]

      if(idList)pushContext.pushGroupManager.outboundNotifier.notifyObservers(idList)
    }
  }

  def doWithApplicationContext = { applicationContext ->
  }

  def onChange = { event ->
    if (application.isArtefactOfType('Controller', event.source)
            || (application.config.icepush?.injectServices && application.isArtefactOfType('Service', event.source))) {
      event.manager?.getGrailsPlugin("icepush")?.doWithDynamicMethods(event.ctx)
    }
  }

  def onConfigChange = { event ->
  }
}
