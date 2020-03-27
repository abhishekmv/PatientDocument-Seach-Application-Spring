package com.qpidhealth.qpid.search.controller;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
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

@RestController
//@Api(value = "API Description")
@RequestMapping("/patients")
public class PatientController {
	
	
	@Autowired(required = false)
	PatientSearch patientsearch;
	
	@Autowired(required = false)
	PatientRepository patientRepository;
	
	/***
     * Method to check health of API
     * @return Response status
     */
	//@ApiOperation(value = "It is the patient health API")
    @GetMapping("/health")
    public ResponseEntity<String> healthCheck() {
        return ResponseEntity.ok("Patient API is healthy!!");
    }
    
    @GetMapping("/all")
    @ResponseBody
    public Iterable<Patient> allPatients() {
    	return patientRepository.findAll();
    }
    
    @GetMapping("/search")
    @ResponseBody
    public List<PatientSearch> searchPatients(@RequestParam(defaultValue = "") String query) {
    	
       String like = "%"+query+"%";
       List<PatientSearch> records = patientRepository.findPatientDetailsBySearch(like);
       return records;
    }
    
    @PostMapping("/create")
    public Patient createPatient(@Valid @RequestBody Patient patient) {
        return patientRepository.save(patient);
        
    }
    
    @PutMapping("/update/{patientId}")
    public Patient updatePatient(@PathVariable Long patientId, @Valid @RequestBody Patient patientRequest) {
        return patientRepository.findById(patientId).map(patient -> {
        	patient.setPatientname(patientRequest.getPatientname());
            return patientRepository.save(patient);
        }).orElseThrow(() -> new ResourceNotFoundException("patientId = " + patientId + " not found", "PatientId", patientId));
    }


    @DeleteMapping("/delete/{patientId}")
    public ResponseEntity<?> deletePatient(@PathVariable Long patientId) {
        return patientRepository.findById(patientId).map(post -> {
        	patientRepository.delete(post);
            return ResponseEntity.ok().build();
        }).orElseThrow(() -> new ResourceNotFoundException("patientId = " + patientId + " not found", "patientId", patientId));
    }

}
