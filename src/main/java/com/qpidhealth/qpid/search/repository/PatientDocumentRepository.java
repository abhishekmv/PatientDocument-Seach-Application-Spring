package com.qpidhealth.qpid.search.repository;

import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpidhealth.qpid.search.model.PatientDocsData;
import com.qpidhealth.qpid.search.model.PatientDocument;

@Repository
public interface PatientDocumentRepository extends JpaRepository<PatientDocument, Long> {
    
    @Query("SELECT new com.qpidhealth.qpid.search.model.PatientDocsData(pd.id , p.id ,p.patientname, pd.title, pd.document) "
            + "FROM Patient as p INNER JOIN PatientDocument AS pd ON p.id = pd.patientid ")
    Page<PatientDocsData> findAllPatientDocuments(Pageable pageable);

    @Query("SELECT new com.qpidhealth.qpid.search.model.PatientDocsData(pd.id , p.id ,p.patientname, pd.title, pd.document) "
            + "FROM Patient as p INNER JOIN PatientDocument AS pd ON p.id = pd.patientid "
            + "WHERE p.id=?1")
    Page<PatientDocsData> findByPatientId(Long patientId, Pageable pageable);

    @Query("SELECT new com.qpidhealth.qpid.search.model.PatientDocsData(pd.id , p.id ,p.patientname, pd.title, pd.document) "
            + "FROM Patient as p INNER JOIN PatientDocument AS pd ON p.id = pd.patientid "
            + "WHERE p.id=?1 AND pd.id=?2")
    PatientDocsData findByPatientIdAndDocumentId(Long patientId, Long documentId);
    
    Optional<PatientDocument> deleteByIdAndPatientId(Long id, Long patientId);
}
