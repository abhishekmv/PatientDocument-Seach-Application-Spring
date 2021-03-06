package com.qpidhealth.qpid.search.repository;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import com.qpidhealth.qpid.search.model.Patient;
import com.qpidhealth.qpid.search.model.PatientSearch;

/**
 * A repository for making database calls for the patient table
 */
@Repository
public interface PatientRepository extends JpaRepository<Patient, Long> {

    /**
     * A database call with inner join of patient and patient document table based on the patient id
     * and searching for the key word among patient name, title and document columns
     * 
     * @param query
     *          A String value
     * @return
     *         A list of patient details based on the search word match 
     */
    @Query("SELECT new com.qpidhealth.qpid.search.model.PatientSearch(p.patientname, pd.title, pd.document) "
            + "FROM Patient as p INNER JOIN PatientDocument AS pd ON p.id = pd.patientid "
            + "WHERE LOWER(p.patientname) LIKE LOWER(?1) OR LOWER(pd.title) LIKE LOWER(?1) OR LOWER(pd.document) LIKE LOWER(?1)")
    List<PatientSearch> findPatientDetailsBySearch(String query);
}
