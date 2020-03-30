package com.qpidhealth.qpid.search.repository;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpidhealth.qpid.search.model.PatientDocsData;
import com.qpidhealth.qpid.search.model.PatientDocument;

/**
 * A repository for making database calls for the patient document table
 */
@Repository
public interface PatientDocumentRepository extends JpaRepository<PatientDocument, Long> {
    
    /**
     *  A database call with inner join of patient and patient document table based on the patient id
     * and get a combination of patient and their document details
     * 
     * @return
     *          A list of combination of patient and document details for all the patients
     */
    @Query("SELECT new com.qpidhealth.qpid.search.model.PatientDocsData(pd.id , p.id ,p.patientname, pd.title, pd.document) "
            + "FROM Patient as p INNER JOIN PatientDocument AS pd ON p.id = pd.patientid ")
    List<PatientDocsData> findAllPatientDocuments();

    /**
     *  A database call with inner join of patient and patient document table based on the patient id
     * and get all the patient and their document results back for the matching patient id
     * 
     * @param patientId
     *          A long value
     * @return
     *         A list of patient and their document details is returned
     */
    @Query("SELECT new com.qpidhealth.qpid.search.model.PatientDocsData(pd.id , p.id ,p.patientname, pd.title, pd.document) "
            + "FROM Patient as p INNER JOIN PatientDocument AS pd ON p.id = pd.patientid "
            + "WHERE p.id=?1")
    List<PatientDocsData> findByPatientId(Long patientId);

    /**
     *  A database call with inner join of patient and patient document table based on the patient id
     * and get a single patient and their document result back for the matching patient id and document id
     * 
     * @param patientId
     *          A long value
     * @param documentId
     *          A long value
     * @return
     *          A patient document is returned for the matching patient and document id
     */
    @Query("SELECT new com.qpidhealth.qpid.search.model.PatientDocsData(pd.id , p.id ,p.patientname, pd.title, pd.document) "
            + "FROM Patient as p INNER JOIN PatientDocument AS pd ON p.id = pd.patientid "
            + "WHERE p.id=?1 AND pd.id=?2")
    PatientDocsData findByPatientIdAndDocumentId(Long patientId, Long documentId);
    
    /**
     * A document for a patient is provided based on the patient and document id
     * 
     * @param documentid
     *          A long value
     * @param patientId
     *          A long value
     * @return
     *          A patient's document is returned for a patient and document id combination
     */
    @Query("FROM PatientDocument AS pd WHERE pd.id=?1 AND pd.patientid=?2")
    Optional<PatientDocument> deleteByIdAndPatientId(Long documentid, Long patientId);
}
