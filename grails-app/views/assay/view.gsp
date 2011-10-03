<html>
<head>
	<meta name="layout" content="main"/>
</head>

<body>
<h1>${assay.name} <small>(${assay.study.token()})</small></h1>

<div id="uploadedFiles">
	<mm:uploadedFileList files="${assayFiles}" dialogProperties="${[title: 'Please choose the uploaded file data type', buttons: ['save', 'close'], assayId: id, controllerName: 'parseConfiguration', actionName: 'data']}"/>
</div>

<g:if test="${measurementPlatformVersions}">
	<h2>Measurement Platforms</h2>
	<g:each in="${measurementPlatformVersions}" var="measurementPlatformVersion">

		<h3>${measurementPlatformVersion.measurementPlatform.name} V${measurementPlatformVersion.versionNumber}</h3>
		<g:link controller="measurementPlatform" action="view"
				id="${measurementPlatformVersion.measurementPlatform.id}">- - - view - - -</g:link><br/>

		<g:if test="${measurementPlatformVersion.measurementPlatform.description}">
			<p>${measurementPlatformVersion.measurementPlatform.description}</p>
		</g:if>

		<g:if test="${measurementPlatformVersion.features}">
			identifiable features: ${measurementPlatformVersion.features.asList()*.label.join(', ')}
		</g:if>

		<g:if test="${measurementPlatformVersionUploadedFiles[measurementPlatformVersion.id]}">
			<h4>Related files</h4>
			<ul>
				<g:each in="${measurementPlatformVersionUploadedFiles[measurementPlatformVersion.id]}" var="assayFile">
					<li>${assayFile.fileName} (${assayFile.lastUpdated})</li>
				</g:each>
			</ul>
		</g:if>
	</g:each>
</g:if>
</body>
</html>  