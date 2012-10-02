package org.dbxp.metabolomicsModule

import org.dbxp.dbxpModuleStorage.UploadedFile
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatform
import org.dbxp.metabolomicsModule.measurements.MeasurementPlatformVersion

class AssayController {

	/*
	 * embed required service(s)
	 */
	def assayService
	def gscfApiService
	def synchronizationService

	/*
	 * Assay by Token (for GSCF integration)
	 */
	def showByToken = {
		if (params.id){
			def assay = MetabolomicsAssay.findByAssayToken(params.id as String)
			try {
				redirect (action: "view", id: assay.id, params: params)
			} catch (e) {
				log.error e
			}
		}

		response.sendError(400, "No assayToken specified.")
	}

	/*
	 * Metabolomics Assay page
	 * - list basic assay info (name, members etc)
	 * - list files related to this assay
	 *
	 * params.id is required to load the assay
	 */
	def view = {

		// make sure to ignore the uploaded-file-was-moved-to-assay event here, is only relevant in home controller
		session.removeAttribute('uploadedFileWasMovedToAssay')

		if (!params.id) response.sendError(400, "No assay id specified.") // id of an assay must be present

		// load assay from id (for session.user)
		def assay = assayService.getAssayReadableByUserById(session.user, params.id as Long)

        if (!assay) {
            response.sendError(404, "No assay found with id $params.id")
            return
        }

		def assayFiles = UploadedFile.findAllByAssay(assay)

		[	assay: assay,
			assayFiles: assayFiles,
			selectedTab: params.selectedTab
		]
	}

	def updateAssayProperties = {

		def assay = MetabolomicsAssay.get(params.id)

		assay.measurementPlatformVersion = MeasurementPlatformVersion.get(params.platformVersionId)
		assay.comments = params.comments ?: ''
		assay.save(failOnError: true)

		redirect(action: 'view', id: params.id)
	}

	def createAssay = {
		//option to create an assay via the GSCF API
		if (params?.submit == 'create'){
			try {
				def resp = gscfApiService.createAssay(params)

				//force a sync!!!
				synchronizationService.fullSynchronization()


				redirect(action: 'showByToken', id: resp.token)
			} catch (e) {
				log.error(e) //failed to create an assay
				flash.message = "Was unable to create the assay!"
			}

		}

		[studies: gscfApiService.findAllStudies(), assayTemplates: gscfApiService.findAllAssayTemplates()]
	}
}
