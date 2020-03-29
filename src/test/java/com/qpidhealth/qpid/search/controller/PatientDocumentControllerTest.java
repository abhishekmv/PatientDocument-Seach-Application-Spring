package com.qpidhealth.qpid.search.controller;

import static java.util.Collections.singletonList;
import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.rest.webmvc.ResourceNotFoundException;
import org.springframework.http.ResponseEntity;
import org.springframework.test.context.junit4.SpringRunner;

import com.qpidhealth.qpid.search.model.Patient;
import com.qpidhealth.qpid.search.model.PatientDocsData;
import com.qpidhealth.qpid.search.model.PatientDocument;
import com.qpidhealth.qpid.search.repository.PatientDocumentRepository;
import com.qpidhealth.qpid.search.repository.PatientRepository;

@RunWith(SpringRunner.class)
@SpringBootTest(classes = PatientDocumentController.class)
public class PatientDocumentControllerTest {

    @InjectMocks
    PatientDocumentController patientDocumentController;

    @Mock
    PatientRepository patientRepository;

    @Mock
    PatientDocumentRepository patientDocumentRepository;

    @Test
    public void allDocumentsTest() throws Exception {
        // given
        PatientDocsData pdd = new PatientDocsData(1L, 1L, "John smith", "title", "document");
        List<PatientDocsData> allPatientDocs = singletonList(pdd);

        when(patientDocumentRepository.findAllPatientDocuments()).thenReturn(allPatientDocs);

        // when
        ResponseEntity<List<PatientDocsData>> responseEntity = patientDocumentController
                .allDocuments();

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().size()).isEqualTo(1);
        assertThat(responseEntity.getBody().get(0).getId())
                .isEqualTo(pdd.getId());
        assertThat(responseEntity.getBody().get(0).getName())
                .isEqualTo(pdd.getName());

    }

    @Test
    public void getAllDocumentsByPatientIdTest() throws Exception {
        // given
        PatientDocsData pdd = new PatientDocsData(1L, 1L, "John smith", "title", "document");
        List<PatientDocsData> allPatientDocs = singletonList(pdd);

        when(patientRepository.existsById(1L)).thenReturn(true);
        when(patientDocumentRepository.findByPatientId(1L)).thenReturn(allPatientDocs);

        // when
        ResponseEntity<List<PatientDocsData>> responseEntity = patientDocumentController
                .getAllDocumentsByPatientId(1L);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().size()).isEqualTo(1);
        assertThat(responseEntity.getBody().get(0).getId())
                .isEqualTo(pdd.getId());
        assertThat(responseEntity.getBody().get(0).getName())
                .isEqualTo(pdd.getName());
    }

    @Test
    public void getAllDocumentsByPatientId404ErrorTest() throws Exception {
        when(patientRepository.existsById(1L)).thenReturn(false);

        // when
        ResponseEntity<List<PatientDocsData>> responseEntity = patientDocumentController
                .getAllDocumentsByPatientId(1L);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }

    @Test
    public void getDocumentByPatientIdAndDocumentIdTest() throws Exception {
        // given
        PatientDocsData pdd = new PatientDocsData(1L, 1L, "John smith", "title", "document");

        when(patientRepository.existsById(1L)).thenReturn(true);
        when(patientDocumentRepository.existsById(1L)).thenReturn(true);
        when(patientDocumentRepository.findByPatientIdAndDocumentId(1L, 1L)).thenReturn(pdd);

        // when
        ResponseEntity<PatientDocsData> responseEntity = patientDocumentController
                .getDocumentByPatientIdAndDocumentId(1L, 1L);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
        assertThat(responseEntity.getBody().getId())
                .isEqualTo(pdd.getId());
        assertThat(responseEntity.getBody().getName())
                .isEqualTo(pdd.getName());
    }

    @Test
    public void getDocumentByPatientIdAndDocumentId404ErrorTest() throws Exception {
        when(patientRepository.existsById(1L)).thenReturn(true);
        when(patientDocumentRepository.existsById(1L)).thenReturn(false);

        // when
        ResponseEntity<PatientDocsData> responseEntity = patientDocumentController
                .getDocumentByPatientIdAndDocumentId(1L, 1L);

        // then
        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(404);
    }
    
    @Test
    public void createPatientDocumentTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");
        
        PatientDocument pd = new PatientDocument();
        pd.setId(1L);
        pd.setTitle("title");
        pd.setPatient(patient);
        pd.setPatientid(1L);
        

        when(patientRepository.findById(1L)).thenReturn(Optional.of(patient));
        when(patientDocumentRepository.save(any(PatientDocument.class))).thenReturn(pd);

        Patient patientToAdd = new Patient();
        patientToAdd.setId(1);
        patientToAdd.setPatientname("John smith");
        // when
        PatientDocument result = patientDocumentController.createPatientDocument(1L, pd);

        // then
        assertThat(result.getPatient().getPatientname()).isEqualTo(patient.getPatientname());
        assertThat(result.getDocument()).isEqualTo(pd.getDocument());
        assertThat(result.getTitle()).isEqualTo(pd.getTitle());
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void createPatientDocumentUnknownPatientIdExceptionTest() throws Exception {
        // given
        PatientDocument pd = new PatientDocument();
        pd.setId(1L);
        pd.setTitle("title");
        pd.setPatient(new Patient());
        pd.setPatientid(1L);

        when(patientRepository.findById(1L)).thenReturn(Optional.empty());

        Patient patientToAdd = new Patient();
        patientToAdd.setId(1);
        patientToAdd.setPatientname("John smith");
        // when
        patientDocumentController.createPatientDocument(1L, pd);
    }
    
    @Test
    public void updatePatientDocumentTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");
        
        PatientDocument pd = new PatientDocument();
        pd.setId(1L);
        pd.setTitle("title");
        pd.setPatient(patient);
        pd.setPatientid(1L);

        when(patientRepository.existsById(1L)).thenReturn(true);
        when(patientDocumentRepository.findById(1L)).thenReturn(Optional.of(pd));

        Patient patientupdate = new Patient();
        patientupdate.setId(1);
        patientupdate.setPatientname("Tim kenny");
        
        PatientDocument pdUpdate = new PatientDocument();
        pdUpdate.setId(1L);
        pdUpdate.setTitle("title 2");
        pdUpdate.setPatient(patientupdate);
        pdUpdate.setPatientid(1L);
        
        when(patientDocumentRepository.save(any(PatientDocument.class))).thenReturn(pdUpdate);

        // when
        PatientDocument result = patientDocumentController.updatePatientDocument(1L,1L, pdUpdate);

        // then
        assertThat(result.getPatient().getPatientname()).isEqualTo(patientupdate.getPatientname());
        assertThat(result.getDocument()).isEqualTo(pdUpdate.getDocument());
        assertThat(result.getTitle()).isEqualTo(pdUpdate.getTitle());
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void updatePatientDocumentUknownPatientIdExceptionTest() throws Exception {
        // given
        when(patientRepository.existsById(1L)).thenReturn(false);

        Patient patientupdate = new Patient();
        patientupdate.setId(1);
        patientupdate.setPatientname("Tim kenny");
        
        PatientDocument pdUpdate = new PatientDocument();
        pdUpdate.setId(1L);
        pdUpdate.setTitle("title 2");
        pdUpdate.setPatient(patientupdate);
        pdUpdate.setPatientid(1L);
        
        // when
        patientDocumentController.updatePatientDocument(1L,1L, pdUpdate);
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void updatePatientDocumentUknownDocumentIdExceptionTest() throws Exception {
        // given
        when(patientRepository.existsById(1L)).thenReturn(true);
        when(patientDocumentRepository.findById(1L)).thenReturn(Optional.empty());

        Patient patientupdate = new Patient();
        patientupdate.setId(1);
        patientupdate.setPatientname("Tim kenny");
        
        PatientDocument pdUpdate = new PatientDocument();
        pdUpdate.setId(1L);
        pdUpdate.setTitle("title 2");
        pdUpdate.setPatient(patientupdate);
        pdUpdate.setPatientid(1L);
        
        // when
        patientDocumentController.updatePatientDocument(1L,1L, pdUpdate);
    }
    
    @Test
    public void deletePatientDocumentTest() throws Exception {
        // given
        Patient patient = new Patient();
        patient.setId(1);
        patient.setPatientname("John smith");
        
        PatientDocument pd = new PatientDocument();
        pd.setId(1L);
        pd.setTitle("title");
        pd.setPatient(patient);
        pd.setPatientid(1L);

        when(patientDocumentRepository.deleteByIdAndPatientId(1L,1L)).thenReturn(Optional.of(pd));
        ResponseEntity<?> responseEntity = patientDocumentController.deletePatientDocument(1L,1L);

        assertThat(responseEntity.getStatusCodeValue()).isEqualTo(200);
    }
    
    @Test(expected = ResourceNotFoundException.class)
    public void deletePatientDocumentUnknownPatientIdExceptionTest() throws Exception {
        when(patientDocumentRepository.deleteByIdAndPatientId(1L,1L)).thenReturn(Optional.empty());
        patientDocumentController.deletePatientDocument(1L,1L);
    }
    
}
