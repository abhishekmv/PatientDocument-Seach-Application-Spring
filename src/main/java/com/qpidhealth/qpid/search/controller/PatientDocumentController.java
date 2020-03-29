package com.qpidhealth.qpid.search.controller;

import java.util.List;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qpidhealth.qpid.search.model.PatientDocsData;
import com.qpidhealth.qpid.search.model.PatientDocument;
import com.qpidhealth.qpid.search.repository.PatientDocumentRepository;
import com.qpidhealth.qpid.search.repository.PatientRepository;

import io.swagger.annotations.ApiOperation;

@RestController
public class PatientDocumentController {

    @Autowired(required = false)
    PatientDocsData patientDocsData;

    @Autowired(required = false)
    PatientRepository patientRepository;

    @Autowired(required = false)
    PatientDocumentRepository patientDocumentRepository;

    /**
     * Method to check health of API
     * 
     * @return 
     *          Response status
     */
    @ApiOperation(value = "Patient document health API")
    @GetMapping("/documents/health")
    public ResponseEntity<String> healthCheck() {

        return new ResponseEntity<>("Patient Document API is healthy!!", HttpStatus.OK);
    }

    /**
     * A method to get all the patient and all their documents
     * 
     * @return 
     *          A List of all the patients and documents
     */
    @ApiOperation(value = "Get all patients medical documents")
    @GetMapping("/documents/all")
    @ResponseBody
    public ResponseEntity<List<PatientDocsData>> allDocuments() {

        return new ResponseEntity<>(patientDocumentRepository.findAllPatientDocuments(),
                HttpStatus.OK);
    }

    /**
     * A method to return all the patient and their document details based on the patient Id
     * 
     * @param patientId
     *          A long value
     * @return
     *         Response of 200 OK and list of patient details will be sent
     *         Response of 404 not found is sent if patient id does not exist 
     */
    @ApiOperation(value = "Get all the documents for a patient")
    @GetMapping("/patient/{patientId}/documents")
    public ResponseEntity<List<PatientDocsData>> getAllDocumentsByPatientId(@PathVariable(
            value = "patientId") Long patientId) {

        // Note: If you are asking for a patient id that doesn't exist then it would be a 404 error.
        if (!patientRepository.existsById(patientId)) {
            return new ResponseEntity<List<PatientDocsData>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<>(patientDocumentRepository.findByPatientId(patientId),
                HttpStatus.OK);
    }

    /**
     * Get a patient detail based on patient id and document id
     * 
     * @param patientId
     *          A long value
     * @param documentId
     *          A long value
     * @return
     *         Response of 200 OK and empty result will be sent if the patient id and document id
     *         exist but wrong combination is provided
     *         Response of 200 OK and patient detail will be sent if the patient id and document id match
     *         Response of 404 not found is sent if patient id or document id does not exist 
     */
    @ApiOperation(value = "Get a single patient document")
    @GetMapping("/patient/{patientId}/document/{documentId}")
    public ResponseEntity<PatientDocsData> getDocumentByPatientIdAndDocumentId(@PathVariable(value = "patientId") Long patientId,
                                                                               @PathVariable(value = "documentId") Long documentId) {
        if (!patientRepository.existsById(patientId)
                || !patientDocumentRepository.existsById(documentId)) {
            return new ResponseEntity<PatientDocsData>(HttpStatus.NOT_FOUND);
        }

        return new ResponseEntity<PatientDocsData>(
                patientDocumentRepository.findByPatientIdAndDocumentId(patientId, documentId),
                HttpStatus.OK);
    }

    /**
     * A method to create/add a patient report/document for a patient
     * NOTE: one patient can have 'n' document attached to them. 1 to many relationship
     * 
     * @param patientId
     *          A long value
     * @param patientDocuments
     *          A patient document object
     * @return
     *          Response of 200OK and the created object details
     *          Response of 400 bad request is sent if patient document object is null or empty
     */
    @ApiOperation(value = "Create a medical document for a patient")
    @PostMapping("/patient/{patientId}/documents")
    public PatientDocument createPatientDocument(@PathVariable(value = "patientId") Long patientId,
                                                 @Valid @RequestBody PatientDocument patientDocuments) {

        return patientRepository.findById(patientId).map(patient -> {
            patientDocuments.setPatient(patient);
            return patientDocumentRepository.save(patientDocuments);
        }).orElseThrow(() -> new ResourceNotFoundException(
                "PatientId = " + patientId + " not found"));
    }

    /**
     * Update the patient document detail based on the patient id and document id
     * 
     * @param patientId
     *          A long value
     * @param documentId
     *          A long value
     * @param patientDocumentsRequest
     *          A patient document object
     * @return
     *          Response of 200 OK and the updated object details
     *          Response of 400 bad request is sent if patient document object is null or empty
     *          Response of 404 not found is sent if patient id or document id does not exist
     */
    @ApiOperation(value = "Update a medical document for a patient")
    @PutMapping("/patient/{patientId}/document/{documentId}")
    public PatientDocument updatePatientDocument(@PathVariable(value = "patientId") Long patientId,
                                                 @PathVariable( value = "documentId") Long documentId,
                                                 @Valid @RequestBody PatientDocument patientDocumentsRequest) {

        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient Id = " + patientId + " not found");
        }

        return patientDocumentRepository.findById(documentId).map(doc -> {
            doc.setTitle(patientDocumentsRequest.getTitle());
            doc.setDocuments(patientDocumentsRequest.getDocument());
            return patientDocumentRepository.save(doc);
        }).orElseThrow(() -> new ResourceNotFoundException(
                "Document Id " + documentId + " not found"));
    }

    /**
     * Delete a patient document based on the patient id and document id
     * 
     * @param patientId
     *          A long value
     * @param documentId
     *          A long value
     * @return
     *          Response of 200 OK once the patient document is removed from patient document table
     *          Response of 404 not found is sent if patient id or document id does not exist
     */
    @ApiOperation(value = "Delete a medical document for a patient")
    @DeleteMapping("/patient/{patientId}/document/{documentId}")
    public ResponseEntity<?> deletePatientDocument(@PathVariable(value = "patientId") Long patientId,
                                                   @PathVariable(value = "documentId") Long documentId) {

        return patientDocumentRepository.deleteByIdAndPatientId(documentId, patientId).map(doc -> {
            patientDocumentRepository.delete(doc);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("Patient document not found with id "
                + documentId + " and Patient Id " + patientId));
    }
}
