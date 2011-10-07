grails.project.class.dir = "target/classes"
grails.project.test.class.dir = "target/test-classes"
grails.project.test.reports.dir = "target/test-reports"

grails.server.port.http = 8083

//grails.project.war.file = "target/${appName}-${appVersion}.war"
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
		mavenCentral()

        mavenRepo "http://nexus.nmcdsp.org/content/repositories/releases"
//        mavenRepo "http://nexus.nmcdsp.org/content/repositories/snapshots"

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
		// runtime 'com.github.groovy-wslite:groovy-wslite:0.1'
        // runtime 'mysql:mysql-connector-java:5.1.13'
    }
}

//grails.plugin.location.'dbxpModuleBase'     = '../dbxpModuleBase'
grails.plugin.location.'dbxpModuleStorage'  = '../dbxpModuleStorage'
//grails.plugin.location.'matrixImporter'     = '../matrixImporter'
//grails.plugin.location.'uploadr'            = '../uploadr'