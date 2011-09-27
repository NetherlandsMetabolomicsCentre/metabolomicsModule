import org.mbte.gretty.httpserver.* 

@GrabResolver(name='gretty', root='http://groovypp.artifactoryonline.com/groovypp/libs-releases-local')
@Grab('org.mbte.groovypp:gretty:0.4.279')  

def addr = InetAddress.localHost.hostAddress
def hname = InetAddress.localHost.hostName
def cname =InetAddress.localHost.canonicalHostName

GrettyServer server = []
server.groovy =
[
    localAddress: new InetSocketAddress("localhost", 8080),
    defaultHandler:
    {
        response.redirect "/"
    },
    "/:name":
   {
        get
        {
		def namelist = request.getParameters()
		def servicePath = namelist.name		

		if (namelist.name.contains('?')){
			servicePath = namelist.name.split('?')[0]
		} 

		def strResponse = ''
		switch (servicePath){
			case 'gscf/rest/getUser' : strResponse = '{"username":"admin","id":1,"isAdministrator":true}'; break;
			case 'gscf/rest/getStudyVersions' : strResponse = '[{"studyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5","version":2},{"studyToken":"7bbba0fb-821e-4e16-903a-3887e1b54e59","version":2}]'; break;
			case 'gscf/rest/getStudies' : strResponse = '[{"studyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5","public":false,"title":"NuGO PPS human study","description":"Human study performed at RRI; centres involved: RRI, IFR, TUM, Maastricht U.","code":"PPSH","startDate":"2008-01-13T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":2},{"studyToken":"7bbba0fb-821e-4e16-903a-3887e1b54e59","public":false,"title":"NuGO PPS3 mouse study leptin module","description":"C57Bl/6 mice were fed a high fat (45 en%) or low fat (10 en%) diet after a four week run-in on low fat diet.","code":"PPS3_leptin_module","startDate":"2008-01-01T23:00:00Z","published":false,"Objectives":null,"Consortium":null,"Cohort name":null,"Lab id":null,"Institute":null,"Study protocol":null,"version":2}]'; break;
			case 'gscf/rest/getAuthorizationLevel' : strResponse = '{"isOwner":false,"canRead":true,"canWrite":true}'; break;
			case 'gscf/rest/getAssays' : strResponse = '[{"assayToken":"71ab1ac7-67d5-4066-90e5-8488da4b32de","name":"Lipidomics profile after","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":25,"name":"GC/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5"},{"assayToken":"3a09c02b-eb5b-42b3-9c12-b09c708a0498","name":"Lipidomics profile before","module":{"class":"org.dbnp.gdt.AssayModule","id":2,"name":"Metabolomics module","notify":false,"openInFrame":true,"url":"http://localhost:8083/metabolomicsModule"},"Description":null,"Spectrometry technique":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":25,"name":"GC/MS","parent":{"class":"TemplateField","id":88}},"parentStudyToken":"0082669f-2969-4ba8-9d38-3606bdd59cf5"}]'; break;
			case 'gscf/rest/getSamples' : strResponse = '[{"sampleToken":"e8ed9779-a69b-491d-b44d-337773c15f9a","material":"blood plasma","subject":"5","event":"Blood extraction","startTime":"4 days, 6 hours","name":"5_A","Remarks":null,"Text on vial":"T61.544925232460734","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"5","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":2,"name":"Female","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"82a29932-3eee-40b1-91b5-b6d664f520ec","material":"blood plasma","subject":"7","event":"Blood extraction","startTime":"4 days, 6 hours","name":"7_A","Remarks":null,"Text on vial":"T15.52474601744489","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"7","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":2,"name":"Female","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"901cf9b4-87ce-407f-ac8a-95e1886eb12c","material":"blood plasma","subject":"10","event":"Blood extraction","startTime":"4 days, 6 hours","name":"10_A","Remarks":null,"Text on vial":"T2.8251578498348917","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"10","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":1,"name":"Male","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"1f85bdd6-05be-41ab-8970-ac5b34689a20","material":"blood plasma","subject":"8","event":"Blood extraction","startTime":"4 days, 6 hours","name":"8_A","Remarks":null,"Text on vial":"T12.905305209188256","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"8","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":1,"name":"Male","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"634687c5-febf-4c62-95b5-83e14bc8826c","material":"blood plasma","subject":"2","event":"Blood extraction","startTime":"4 days, 6 hours","name":"2_A","Remarks":null,"Text on vial":"T53.22460478766919","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"2","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":1,"name":"Male","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"22095dd7-fef5-4d34-a6f9-9735711edfef","material":"blood plasma","subject":"3","event":"Blood extraction","startTime":"4 days, 6 hours","name":"3_A","Remarks":null,"Text on vial":"T91.14172776100582","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"3","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":2,"name":"Female","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"03290796-5ee5-4b3f-a489-8c88073e138d","material":"blood plasma","subject":"1","event":"Blood extraction","startTime":"4 days, 6 hours","name":"1_A","Remarks":null,"Text on vial":"T26.125392142030467","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"1","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":2,"name":"Female","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"6d3bdf4b-03f1-40c0-825d-0500ddb68988","material":"blood plasma","subject":"4","event":"Blood extraction","startTime":"4 days, 6 hours","name":"4_A","Remarks":null,"Text on vial":"T28.048525506283873","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"4","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":1,"name":"Male","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"7455da74-1f58-4423-8479-658c5fec1e51","material":"blood plasma","subject":"6","event":"Blood extraction","startTime":"4 days, 6 hours","name":"6_A","Remarks":null,"Text on vial":"T23.042680320999153","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"6","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":1,"name":"Male","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"65df9ef6-6f60-4ae9-a879-9b2ab0f65efa","material":"blood plasma","subject":"9","event":"Blood extraction","startTime":"4 days, 6 hours","name":"9_A","Remarks":null,"Text on vial":"T34.24583550344011","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"9","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":2,"name":"Female","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}},{"sampleToken":"40b82008-d12c-49dd-83ed-15674af3cbc3","material":"blood plasma","subject":"11","event":"Blood extraction","startTime":"4 days, 6 hours","name":"11_A","Remarks":null,"Text on vial":"T90.42055152978841","Sample measured volume":null,"eventObject":{"startTime":367200,"duration":0,"Sample Protocol":null,"Sample volume":4.5},"subjectObject":{"name":"11","species":{"class":"org.dbnp.gdt.Term","id":2,"accession":"9606","dateCreated":"2011-09-27T12:49:43Z","lastUpdated":"2011-09-27T12:49:43Z","name":"Homo sapiens","ontology":{"class":"Ontology","id":1}},"Gender":{"class":"org.dbnp.gdt.TemplateFieldListItem","id":1,"name":"Male","parent":{"class":"TemplateField","id":1}},"Age":null,"DOB":null,"Height":null,"Weight":null,"BMI":null,"Race":null,"Waist circumference":null,"Hip circumference":null,"Systolic blood pressure":null,"Diastolic blood pressure":null,"Heart rate":null,"Run-in-food":null}}]'; break;
			default : strResponse = '{"authenticated":true}'
		}
		
		response.html = strResponse
        
		} // end of get
    } // end of
] 

server.start()