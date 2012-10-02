// locations to search for config files that get merged into the main config
// config files can either be Java properties files or ConfigSlurper scripts
//
grails.config.locations = [
	// the default configuration properties
	"classpath:default.properties",

	// the external configuration to override the default
	// configuration (e.g. ~/.gscf/ci.properties)
	"file:${userHome}/.${appName}/${grails.util.GrailsUtil.environment}.properties"
]

grails.project.groupId = appName // change this to alter the default package name and Maven publishing destination
grails.mime.file.extensions = true // enables the parsing of file extensions from URLs into the request format
grails.mime.use.accept.header = false
grails.mime.types = [ html: ['text/html','application/xhtml+xml'],
                      xml: ['text/xml', 'application/xml'],
                      text: 'text/plain',
                      js: 'text/javascript',
                      rss: 'application/rss+xml',
                      atom: 'application/atom+xml',
                      css: 'text/css',
                      csv: 'text/csv',
                      all: '*/*',
                      json: ['application/json','text/json'],
                      form: 'application/x-www-form-urlencoded',
                      multipartForm: 'multipart/form-data'
                    ]

// URL Mapping Cache Max Size, defaults to 5000
//grails.urlmapping.cache.maxsize = 1000

// The default codec used to encode data with ${}
grails.views.default.codec = "none" // none, html, base64
grails.views.gsp.encoding = "UTF-8"
grails.converters.encoding = "UTF-8"
// enable Sitemesh preprocessing of GSP pages
grails.views.gsp.sitemesh.preprocess = true
// scaffolding templates configuration
grails.scaffolding.templates.domainSuffix = 'Instance'

// Set to false to use the new Grails 1.2 JSONBuilder in the render method
grails.json.legacy.builder = false
// enabled native2ascii conversion of i18n properties files
grails.enable.native2ascii = true
// whether to install the java.util.logging bridge for sl4j. Disable for AppEngine!
grails.logging.jul.usebridge = true
// packages to include in Spring bean scanning
grails.spring.bean.packages = []

// request parameters to mask when logging exceptions
grails.exceptionresolver.params.exclude = ['password']

// log4j configuration
log4j = {
    // Example of changing the log pattern for the default console
    // appender:
    //
    //appenders {
    //    console name:'stdout', layout:pattern(conversionPattern: '%c{2} %m%n')
    //}
	appenders {
		rollingFile name: "stacktrace", maxFileSize: 1024, file: "${userHome}/${appName}-${grails.util.GrailsUtil.environment}.log"
	}

	info   'org.dbxp'

    error  'org.codehaus.groovy.grails.web.servlet',  //  controllers
           'org.codehaus.groovy.grails.web.pages', //  GSP
           'org.codehaus.groovy.grails.web.sitemesh', //  layouts
           'org.codehaus.groovy.grails.web.mapping.filter', // URL mapping
           'org.codehaus.groovy.grails.web.mapping', // URL mapping
           'org.codehaus.groovy.grails.commons', // core / classloading
           'org.codehaus.groovy.grails.plugins', // plugins
           'org.codehaus.groovy.grails.orm.hibernate', // hibernate integration
           'org.springframework',
           'org.hibernate',
           'net.sf.ehcache.hibernate'

    warn   'org.mortbay.log'
}

// jQuery plugin
grails.views.javascript.library="jquery"

// define in which environments the development bar should be available
development.bar=['development','ci','nmcdsptest']

// set default properties here in case default.properties in not placed in classpath and no external config
grails.serverURL='http://localhost:8083/metabolomicsModule'
gscf.baseURL='http://localhost:8080/gscf'
module.consumerId='http://localhost:8083/metabolomicsModule' //replace @runtime by resource(dir: '/', absolute: true)
module.name='Metabolomics Module'
module.synchronization.classes.assay = 'org.dbxp.metabolomicsModule.MetabolomicsAssay'
module.synchronization.perform = true

// ****** trackR Config ******
trackr.path = "/tmp/"
trackr.prefix = "nmcdsp.${grails.util.GrailsUtil.environment}."

// ****** GSCF-API ******
api.url = "http://localhost:8080/"
api.endpoint = "gscf/api"
api.username = "api"
api.password = "apI123!"
api.apikey = "11111111-2222-3333-4444-555555555555"
api.module = "Metabolomics module"
