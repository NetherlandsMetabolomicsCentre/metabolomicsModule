package org.dbxp.metabolomicsModule

import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion
import org.apache.commons.logging.LogFactory
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform

/**
 * The metabolomics module tag library delivers a rich set of tags to make it easier to re-use components
 * and elements in the interface.
 *
 * @version: $Rev$
 * @author: $Author$
 * @lastrevision: $Date$
*/

class MetabolomicsModuleTagLib {

    // abbreviation for Metabolomics Module
    static namespace = "mm"

    def assayService
    def uploadedFileService
    def measurementService
	def featurePropertyService

	private static final log = LogFactory.getLog(this);

    def uploadedFileList = { attrs ->

		if (!attrs.dialogProperties.redirectUrl) attrs.dialogProperties.redirectUrl = attrs.dialogProperties.mmBaseUrl

		// get file list from attributes
  		def uploadedFiles
		if (attrs.containsKey('files')) {
			uploadedFiles = attrs.get('files')
		} else {
			throw new Exception("required attribute 'files' is missing from the <uploadedFilesList files=\"...\"/> tag")
		}

		// output file uploadr
        out << '<h1>Uploaded files</h1>'
        out << uploadr.add(name: "uploadrArea", path: "/tmp", placeholder: "Drop file(s) here to upload", direction: 'up', maxVisible: 8, rating: true, maxSize: 52428800) {
            uploadedFiles.each { uploadedFile ->
				// add file to the uploadr
                uploadr.file(name: uploadedFile.fileName) {
					uploadr.deletable { ((uploadedFile.uploader.id == session.user.id || session.user.isAdministrator)) }
					if (uploadedFile.hasProperty('color') && uploadedFile.color) uploadr.color { "${uploadedFile.color}" }
					uploadr.rating { "${measurementService.determineUploadedFileRating(uploadedFile)}" }
					uploadr.fileSize { uploadedFile.fileSize }
                    uploadr.fileModified { uploadedFile.lastUpdated.time }
					uploadr.ratingText { measurementService.constructRatingText(uploadedFile) }
                    uploadr.fileId { uploadedFile.id }
                }
            }

            uploadr.onSuccess {
				out << g.render(template:'/js/uploadr/onSuccess', model:[assay: attrs.assay])
            }

            uploadr.onFailure {
                "console.log('failed uploading ' + file.fileName);"
            }

            uploadr.onAbort {
                "console.log('aborted uploading ' + file.fileName);"
            }

			uploadr.onView {
				out << g.render(template:'/js/uploadr/onView',
					model:[dialogProperties: attrs.dialogProperties]);
            }

            uploadr.onDownload {
				out << g.render(template:'/js/uploadr/onDownload', model:[])
            }

            uploadr.onDelete {
				out << g.render(template:'/js/uploadr/onDelete', model:[redirectUrl: attrs.dialogProperties.redirectUrl])
            }

			uploadr.onChangeColor {
				out << g.render(template:'/js/uploadr/onChangeColor', model:[])
			}
        }
    }

    def studyList = { attrs ->

		out << '''
<script>
$(document).ready(function() {
	$('#studyOverviewTable').dataTable(
	{
		bFilter: false,
		bInfo: false,
		bLengthChange: false,
		bPaginate: false
    });
});
</script>'''
		out << '<div style="float: right">'
		out << 	g.link(action:"createAssay", controller:"assay") { 'study or assay not here?' }
		out << '</div>'
		out << '<h1>Study overview</h1>'
		out << '<table id="studyOverviewTable"><thead><tr><th>Studies</th><th>Assays</th><th>Samples</th><th>Assigned</th><th>Platform</th></tr></thead><tbody>'

		def readableStudiesWithAssays = assayService.getAssaysReadableByUserAndGroupedByStudy(session.user)

		for (studyWithAssays in readableStudiesWithAssays) {

			out << "<tr><td>$studyWithAssays.key.name</td><td>"

			def assayNames = []
			def sampleCounts = []
			def assignedSampleCounts = []
			def measurementPlatformStrings = []

			studyWithAssays.value.each { MetabolomicsAssay assay ->

				def assayNameString

				if (attrs.highlightedAssay?.id == assay?.id)
					assayNameString = "<b>$assay.name</b>"
				else
					assayNameString = assay.name

				assayNames += g.link(action:"view", controller:"assay", id: assay.id) { assayNameString }

				sampleCounts += assay.samples?.size()

				// avoiding mongo bugs by obtaining the uploaded file instance like this
				Long uploadedFileId = UploadedFile.findByAssay(assay)?.id
				UploadedFile uploadedFile = UploadedFile.get(uploadedFileId)

				assignedSampleCounts += uploadedFile?.determineAmountOfSamplesWithData() ?: 0

				def mpv = assay.measurementPlatformVersion
				measurementPlatformStrings += mpv ? "${mpv.measurementPlatform?.name} (${mpv.versionNumber})" : ""
			}

			out << assayNames.join('<br />')

			out << '</td><td>'

			out << sampleCounts.join('<br />')

			out << '</td><td>'

			out << assignedSampleCounts.join('<br />')

			out << '</td><td>'

			out << measurementPlatformStrings.join('<br />')

			out << '</td></tr>'
		}

		out << '</tbody></table>'
    }

	def assayList = { attrs, body ->

		attrs.assays.each { assay ->
			out << g.link(controller: 'assay', action: 'view', id: assay.id) { "${assay.name}" }
			out << "<br />"
		}

	}

	def assayPropertiesEditor = { attrs, body ->

		out << '<form action="../updateAssayProperties/' + attrs.assay.id + '">'
		out << assayPlatformChooser(assay: attrs.assay) {}
		out << assayCommentFieldEditor(assay: attrs.assay) {}
		out << '<input type="submit" value="Submit" />'
		out << '</form>'
	}

    /**
     * Dropdown list control to choose the platform used (DCL lipodomics, et cetera).
     *
     * @param assay
     */
    def assayPlatformChooser = { attrs, body ->
        out << "Platform:  <br />"
        out << '<select name="platformVersionId" size="8" style="width:100%;" ' + (attrs.disabled ? 'disabled>' : '>')

        measurementService.findAllMeasurementPlatforms().each { platform ->

			def measurementPlatformVersions = measurementService.findAllMeasurementPlatformVersions(measurementPlatform:platform)

			if (measurementPlatformVersions) {

				// if new studygroup create new label
				out << '<optgroup label="' + platform.name + '">'
				measurementPlatformVersions.each { platformVersion ->

					out << '<option value="' + platformVersion.id + '" ' + ((platformVersion.id == attrs.assay.measurementPlatformVersion?.id) ? 'selected' : '') + '>' + platformVersion.versionNumber + '</option>'
				}

				out << '</optgroup>'
			}
        }

        out << '</select>'
    }

	def assayCommentFieldEditor = { attrs, body ->

		out << "Comments: <br />"
		out << '<textarea name="comments" rows="8" style="width:100%;padding:0;">'
		out << attrs.assay.comments
		out << '</textarea>'
	}

	def assayFeatureTables = { attrs, body ->

		def assayFiles = UploadedFile.findAllByAssay(attrs.assay)
		assayFiles.each{ assayFile ->
			if (assayFile.matrix)
				out << mm.assayFeatureTable(assay: attrs.assay, assayFile: assayFile)
		}
	}

	def assayFeatureTable = {attrs, body ->

		out << "<h2>Platform features mapping for data file: ${attrs.assayFile.fileName}</h2>"

		out << '<div class="scrollingDiv"><table><thead><tr><th>Platform Feature</th><th>Feature in Data File</th>'

		def measurementPlatformVersionFeatures = attrs.assay.measurementPlatformVersion?.features
		Set propertyHeaders = measurementPlatformVersionFeatures.collect{
			it.props?.keySet()
		}.findAll{it}.sum() as Set

		def propertyMap = [:]
		measurementPlatformVersionFeatures.each { mpvf ->
			propertyMap[mpvf.feature.label] = mpvf.props
		}

		for (propertyLabel in propertyHeaders) {
			out << "<th>$propertyLabel</th>"
		}

		out << '</tr></thead><tbody>'

		def dataColumnHeaders = uploadedFileService.getDataColumnHeaders(attrs.assayFile)
		def measurementPlatformVersionFeatureLabels = measurementPlatformVersionFeatures*.feature?.label ?: []

		Set combinedLabels = dataColumnHeaders + measurementPlatformVersionFeatureLabels

		for (label in combinedLabels) {
			out << '<tr><td>'

			out << ((label in measurementPlatformVersionFeatureLabels) ? label : '<i>missing</i>')

			out << '</td><td>'

			out << ((label in dataColumnHeaders) ? label : '<i>missing</i>')
			out << '</td>'

			propertyHeaders.each{ propertyHeader ->
 				if (propertyMap[label]) {
					out << "<td>" + viewFeatureProperty(propertyHeader: propertyHeader, propertyValue: propertyMap[label][propertyHeader]) {} + "</td>"
				 } else {
					out << "<td></td>"
				 }
			}
			out << '</tr>'
		}

		out << '</tbody></table></div>'
	}

	def sampleTable = { attrs, body ->

		out << "<h2>Sample overview</h2>"

		def assayFiles = UploadedFile.findAllByAssay(attrs.assay)
		def amountOfSamplesWithData = assayFiles.sum{ it.determineAmountOfSamplesWithData() } ?: 0

		out << "Number of GSCF samples: ${attrs.assay.samples?.size() ?: 0}<br/>"
		out << "Number of samples with data: ${amountOfSamplesWithData}"

		if (!assayFiles) return

		out << '''<div class="scrollingDiv"><table><thead><tr><th>File</th><th>Matched samples</th><th>Samples with data</th></tr></thead><tbody>'''

		assayFiles.each{ assayFile ->
			out << "<tr>"
			out << "<td>${assayFile.fileName}<br />Features per sample: ${uploadedFileService.getFeatureNames(assayFile).size()}</td>"
			out << "<td>${assayFile.assaySamplesWithData.join(', <br/>')}</td>" //show samples, only when matched in the GSCF
			out << "<td>${assayFile.samplesWithData.join(', <br/>')}</td>" //show samples, also when not linked to assay in GSCF
			out << "</tr>"
		}

		out << '</tbody></table></div>'
	}

	def viewFeatureProperty = { attrs, body ->

		if (!attrs.propertyHeader || !attrs.propertyValue){
			out << ''
		} else {
			out << featurePropertyService.view(attrs.propertyHeader as String, attrs.propertyValue as String)
		}
	}

	def platformVersionTable = {attrs, body ->

		out << '<div class="scrollingDiv"><table><thead><tr><th>Platform Feature</th>'

		def measurementPlatformVersionFeatures = attrs.measurementPlatformVersion?.features
		def propertyLabels = measurementPlatformVersionFeatures ? measurementPlatformVersionFeatures[0].props.keySet() : []

		def propertyValueMap = [:]
		measurementPlatformVersionFeatures.each { mpvf ->
			propertyValueMap[mpvf.id] = mpvf.props.values()
		}

		for (propertyLabel in propertyLabels) {
			out << "<th>$propertyLabel</th>"
		}

		out << '</tr></thead><tbody>'

		measurementPlatformVersionFeatures.each { mpvf ->
			out << "<tr><td>"
			out << g.link(controller: 'measurementPlatformVersionFeature', action: 'view', id: mpvf.id) { mpvf.feature.label }
			out << "</td>"

			propertyValueMap[mpvf.id].each { propertyValue ->

				def stringValue = propertyValue.toString()

				// uncomment this line to remove html tags (and comment the following)
//				stringValue = stringValue.replaceAll('<','&lt;').replaceAll('>','&gt;');

				// replace all '<', '>' with unicode character so they are displayed but have no meaning in html
				stringValue = stringValue.replaceAll("<(.|\n)*?>", '');

				out << "<td>${stringValue}</td>"
			}
			out << '</tr>'
		}

		out << '</tbody></table></div>'
	}

	def notification = {attrs, body ->

		out << '''<script>$(document).ready(
function(){$(".notification").delay(4000).slideUp("slow", function(){ $(this).remove() } );})
</script>'''

		out << '<div class="notification">'

		out << body()

		out << '</div>'
	}

	def measurementPlatformOverview = {attrs, body ->

		out << '<table><thead><tr><th>Measurement Platforms</th><th>Versions</th><th>Associated Assays</th></tr></thead><tbody>'

		attrs.measurementPlatforms.each { MeasurementPlatform measurementPlatform ->

			out << '<tr><td>'

			out << g.link(controller: "measurementPlatform", action: "view", id: "${measurementPlatform.id}") {
				"${measurementPlatform.name}"
			}

			def versions = measurementPlatform.versions

			out << """</td><td>${versions.collect { version ->
				g.link(controller: 'measurementPlatformVersion', action: 'view', id: version.id) { version.versionNumber }
			}.join('<br/>')}</td><td>"""

			out << versions.collect { MeasurementPlatformVersion version ->
				MetabolomicsAssay.findAllByMeasurementPlatformVersion(version).collect{ assay ->
					g.link(controller: 'assay', action: 'view', id: assay.id){ assay }
				}.join(', ')
			}.join('<br />')

			out << '</td></tr>'
		}
		out << '</tbody></table>'
	}

	def dataTypeOption = {attrs, body ->

		out << """<div class="dataTypeOption" onclick='parseConfigurationDialog.dialog("close");parseConfigurationDialog=openParseConfigurationDialog(eval(${attrs.dialogProperties}))'>"""

		out <<  body()

		out << '</div>'
	}
}
