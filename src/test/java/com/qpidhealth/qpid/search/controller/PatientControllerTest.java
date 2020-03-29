package com.qpidhealth.qpid.search.controller;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;
import static java.util.Collections.singletonList;

import com.qpidhealth.qpid.search.exception.ResourceNotFoundException;
import com.qpidhealth.qpid.search.model.Patient;
import com.qpidhealth.qpid.search.model.PatientSearch;
import com.qpidhealth.qpid.search.repository.PatientRepository;

import static org.mockito.Mockito.when;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PatientController.class)
public class PatientControllerTest {

    @InjectMocks
    PatientController patientController;

    @Mock
    PatientRepository patientRepository;

    @Test
    public void allPatientsTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");
        List<Patient> allPatients = singletonList(patient);

        when(patientRepository.findAll()).thenReturn(allPatients);

        // when
        ResponseEntity<List<Patient>> responseEntity = patientController.allPatients();

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().size()).isEqualTo(1);
        assertThat(responseEntity.getBody().get(0).getId())
                .isEqualTo(patient.getId());
        assertThat(responseEntity.getBody().get(0).getPatientname())
                .isEqualTo(patient.getPatientname());

    }

    @Test
    public void searchPatientsTest() throws Exception {
        // given
        PatientSearch ps1 = new PatientSearch("john smith", "title1", "document1");
        PatientSearch ps2 = new PatientSearch("tim cook", "title2", "document2");

        List<PatientSearch> ps = new ArrayList<>();
        ps.add(ps1);
        ps.add(ps2);

        when(patientRepository.findPatientDetailsBySearch("%%")).thenReturn(ps);

        // when
        List<PatientSearch> result = patientController.searchPatients("");

        // then
        assertThat(result.size()).isEqualTo(2);
        assertThat(result.get(0).getName())
                .isEqualTo(ps1.getName());
        assertThat(result.get(1).getName())
                .isEqualTo(ps2.getName());
    }

    @Test
    public void searchPatientsNoMatchTest() throws Exception {
        when(patientRepository.findPatientDetailsBySearch("%123%")).thenReturn(new ArrayList<>());

        // when
        List<PatientSearch> result = patientController.searchPatients("123");

        // then
        assertThat(result.size()).isEqualTo(0);
    }

    @Test
    public void getSinglePatientTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        // when
        ResponseEntity<Optional<Patient>> responseEntity = patientController.getSinglePatient(1L);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        Assert.assertTrue(responseEntity.getBody().isPresent());
        assertThat(responseEntity.getBody().get().getId())
                .isEqualTo(patient.getId());
        assertThat(responseEntity.getBody().get().getPatientname())
                .isEqualTo(patient.getPatientname());

    }

    @Test
    public void getSinglePatientUnknownTest() throws Exception {
        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        // when
        ResponseEntity<Optional<Patient>> responseEntity = patientController.getSinglePatient(2L);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void createPatientTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");

        when(patientRepository.save(any(Patient.class))).thenReturn(patient);

        Patient patientToAdd = new Patient();
        patientToAdd.setId(1);
        patientToAdd.setPatientname("John smith");
        // when
        ResponseEntity<Patient> responseEntity = patientController.createPatient(patientToAdd);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(201);
        assertThat(responseEntity.getBody().getPatientname()).isEqualTo("John smith");
    }

    @Test
    public void createPatientWithEmptyPatientTest() throws Exception {
        Patient patientToAdd = new Patient();
        ResponseEntity<Patient> responseEntity = patientController.createPatient(patientToAdd);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void createPatientWithNullPatientTest() throws Exception {
        ResponseEntity<Patient> responseEntity = patientController.createPatient(null);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(400);
    }

    @Test
    public void updatePatientTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient patientupdate = new Patient();
        patientupdate.setId(1);
        patientupdate.setPatientname("Tim kenny");
        when(patientRepository.save(any(Patient.class))).thenReturn(patientupdate);

        Patient result = patientController.updatePatient(1L, patientupdate);

        // then
        assertThat(result.getPatientname()).isEqualTo("Tim kenny");
    }

    @Test(expected = ResourceNotFoundException.class)
    public void updatePatientUnknownPatientIdExceptionTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));

        Patient patientupdate = new Patient();
        patientupdate.setId(1);
        patientupdate.setPatientname("Tim kenny");
        when(patientRepository.save(any(Patient.class))).thenReturn(patientupdate);

        patientController.updatePatient(2L, patientupdate);
    }

    @Test
    public void deletePatientTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        ResponseEntity<?> responseEntity = patientController.deletePatient(1L);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }

    @Test(expected = ResourceNotFoundException.class)
    public void deletePatientUnknownPatientIdExceptionTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        patientController.deletePatient(2L);
    }

}
