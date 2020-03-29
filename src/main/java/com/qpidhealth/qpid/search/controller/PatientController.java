package com.qpidhealth.qpid.search.controller;

import java.net.URI;
import java.util.List;
import java.util.Optional;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.qpidhealth.qpid.search.exception.ResourceNotFoundException;
import com.qpidhealth.qpid.search.model.Patient;
import com.qpidhealth.qpid.search.model.PatientSearch;
import com.qpidhealth.qpid.search.repository.PatientRepository;

import io.swagger.annotations.Api;
import io.swagger.annotations.ApiOperation;

@RestController
@Api(value = "API Description")
@RequestMapping("/patients")
public class PatientController {

    @Autowired(required = false)
    PatientSearch patientsearch;

    @Autowired(required = false)
    PatientRepository patientRepository;

    /**
     * Method to check health of API
     * 
     * @return 
     *          Response status
     */
    @ApiOperation(value = "Patient health API")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return new ResponseEntity<>("Patient API is healthy!!", HttpStatus.OK);
    }

    /**
     * A method to do a word case insensitive search across all the patient data like patient name,
     * document title, document details
     * 
     * @param query
     *           A search word
     * @return 
     *          A list of patient search results. Return empty list if no match is found
     */
    @ApiOperation(value = "Word search patient details")
    @GetMapping("/search")
    @ResponseBody
    public List<PatientSearch> searchPatients(@RequestParam(defaultValue = "") String query) {

        String like = "%" + query + "%";
        List<PatientSearch> records = patientRepository.findPatientDetailsBySearch(like);
        return records;
    }

    /**
     * A method to get all the patient details
     * 
     * @return 
     *          A List of all the patients details
     */
    @ApiOperation(value = "Get all patients detail")
    @GetMapping("")
    @ResponseBody
    public ResponseEntity<List<Patient>> allPatients() {
        return new ResponseEntity<>(patientRepository.findAll(), HttpStatus.OK);
    }

    /**
     * A method get a single patient detail based on patient id
     * 
     * @param patientId
     *           A long value
     * @return 
     *         The patient detail with 200 Ok or would return 404 if a wrong/invalid patientId is
     *         provided
     */
    @ApiOperation(value = "Get a single patient detail")
    @GetMapping("/{patientId}")
    @ResponseBody
    public ResponseEntity<Optional<Patient>> getSinglePatient(@PathVariable Long patientId) {
        Optional<Patient> patient = patientRepository.findById(patientId);

        if (!patient.isPresent()) {
            return new ResponseEntity<Optional<Patient>>(HttpStatus.NOT_FOUND);
        }
        return new ResponseEntity<Optional<Patient>>(patient, HttpStatus.OK);
    }

    /**
     * A method to create a patient record
     * 
     * @param patient
     *           A patient object
     * @return 
     *          Response of 201 created and the created object details
     *          Response of 400 bad request is sent if patient object is null or empty
     */
    @ApiOperation(value = "Create a patient")
    @PostMapping("/")
    public ResponseEntity<Patient> createPatient(@Valid @RequestBody Patient patient) {
        if (patient == null || patient.getPatientname() == null) {
            return new ResponseEntity<Patient>(HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(patientRepository.save(patient), HttpStatus.CREATED);

    }

    /**
     * Update an existing patient detail
     * 
     * @param patientId
     *          A long value
     * @param patientRequest
     *          A patient object
     * @return
     *          Response of 200 OK and the updated object details
     *          Response of 400 bad request is sent if patient object is null or empty
     *          Response of 404 not found is sent if patient id does not exist
     */
    @ApiOperation(value = "Update a patient detail")
    @PutMapping("/{patientId}")
    public Patient updatePatient(@PathVariable Long patientId,
                                 @Valid @RequestBody Patient patientRequest) {

        return patientRepository.findById(patientId).map(patient -> {
            patient.setPatientname(patientRequest.getPatientname());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new ResourceNotFoundException(
                "patientId = " + patientId + " not found", "PatientId", patientId));
    }

    /**
     * Delete an existing patient detail
     * 
     * @param patientId
     *          A long value
     * @return
     *          Response of 200 OK once the patient is removed from patient table and
     *          its depending patient document table
     *          Response of 404 not found is sent if patient id does not exist
     */
    @ApiOperation(value = "Delete a patient record")
    @DeleteMapping("/{patientId}")
    public ResponseEntity<?> deletePatient(@PathVariable Long patientId) {
        return patientRepository.findById(patientId).map(post -> {
            patientRepository.delete(post);

            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException(
                "patientId = " + patientId + " not found", "patientId", patientId));
    }

}
