package com.qpidhealth.qpid.search.controller;

import java.util.Optional;

import javax.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
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

    @ApiOperation(value = "Patient document health API")
    @GetMapping("/documents/health")
    public ResponseEntity<String> healthCheck() {
        
        return ResponseEntity.ok("Patient Document API is healthy!!");
    }

    @ApiOperation(value = "Get all patients medical documents")
    @GetMapping("/documents/all")
    @ResponseBody
    public Page<PatientDocsData> allDocuments(Pageable pageable) {
        
        return patientDocumentRepository.findAllPatientDocuments(pageable);
    }

    @ApiOperation(value = "Get all the documents for a patient")
    @GetMapping("/patient/{patientId}/documents")
    public Page<PatientDocsData> getAllDocumentsByPatientId(@PathVariable(
            value = "patientId") Long patientId, Pageable pageable) {
        
        return patientDocumentRepository.findByPatientId(patientId, pageable);
    }
    
    @ApiOperation(value = "Get a single patient document")
    @GetMapping("/patient/{patientId}/document/{documentId}")
    public PatientDocsData getDocumentByPatientIdAndDocumentId(@PathVariable(value = "patientId") Long patientId,
                                                               @PathVariable(value = "documentId") Long documentId) {
        
        return patientDocumentRepository.findByPatientIdAndDocumentId(patientId, documentId);
    }

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

    @ApiOperation(value = "Update a medical document for a patient")
    @PutMapping("/patient/{patientId}/document/{documentId}")
    public PatientDocument updatePatientDocument(@PathVariable(value = "patientId") Long patientId,
                                                 @PathVariable(value = "documentId") Long documentId,
                                                 @Valid @RequestBody PatientDocument patientDocumentsRequest) {
       
        if (!patientRepository.existsById(patientId)) {
            throw new ResourceNotFoundException("Patient Id = " + patientId + " not found");
        }

        return patientDocumentRepository.findById(documentId).map(doc -> {
            doc.setTitle(patientDocumentsRequest.getTitle());
            doc.setDocuments(patientDocumentsRequest.getDocument());
            return patientDocumentRepository.save(doc);
        }).orElseThrow(() -> new ResourceNotFoundException(
                "Document Id " + documentId + "not found"));
    }

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
