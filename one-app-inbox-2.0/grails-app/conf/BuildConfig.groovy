
grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"
grails.project.war.file = "target/${appName}.war"
grails.project.plugins.dir = "plugins"
grails.project.dependency.resolution = {
    // inherit Grails' default dependencies
    inherits("global") {
        // uncomment to disable ehcache
        // excludes 'ehcache'
    }
    log "warn" // log level of Ivy resolver, either 'error', 'warn', 'info', 'debug' or 'verbose'
    repositories {
        grailsPlugins()
        grailsHome()
        grailsCentral()

        // uncomment the below to enable remote dependency resolution
        // from public Maven repositories
        //mavenLocal()
        //mavenCentral()
        //mavenRepo "http://snapshots.repository.codehaus.org"
        //mavenRepo "http://repository.codehaus.org"
        //mavenRepo "http://download.java.net/maven/2/"
        //mavenRepo "http://repository.jboss.com/maven2/"
    }
    dependencies {
        // specify dependencies here under either 'build', 'compile', 'runtime', 'test' or 'provided' scopes eg.

        runtime 'mysql:mysql-connector-java:5.1.10'
        runtime 'xalan:xalan:2.7.1'
        runtime 'org.springframework:spring-test:3.0.3.RELEASE' 
        //  compile files('lib/iText-2.0.8.jar')
        //  compile files('lib/core-renderer-R8pre2.jar')
        // runtime files('lib/core-renderer-R8pre2.jar')
        runtime ('xerces:xercesImpl:2.8.1'){
			excludes "xml-apis"
		}
		
		runtime ('net.sourceforge.nekohtml:nekohtml:1.9.9'){
			excludes "xercesImpl"
		}
		
		inherits("global") {
			excludes 'xmlbeans', 'xbean'
		}

	
    }

//   plugins {
//   runtime ":audit-logging:0.5.4",
//":avatar:0.4.1",
//":commentable:0.7.5",
//":db-util:0.4",
//":dynamic-jasper:0.6",
//":excel-import:0.3",
//":falcone-util:1.0",
//":famfamfam:1.0.1",
//":fckeditor:0.9.5",
//":filterpane:2.0",
//":grails-ui:1.2",
//":hibernate:1.3.7",
//":i18n-templates:1.1.0.1",
//":jquery:1.4.2.7",
//":jquery-calendar:0.2.3",
//":jquery-ui:1.8.11",
//":mail:1.0-SNAPSHOT",
//":multi-select:0.2",
//":multi-tenant-core:1.0.3",
//":multi-tenant-spring-security:0.2.1",
//":oauth:0.10",
//":pretty-time:0.3",
//":quartz:0.4.2",
//":resources:1.0",
//":settings:1.4",
//":spring-security-core:1.1.2",
//":spring-security-ui:0.1.2",
//":taggable:0.6.4",
//":db-util:0.4",
//":rateable:0.7.0"
//    }
}
grails.tomcat.jvmArgs = [ '-Xmx512m', '-XX:MaxPermSize=256m' ]