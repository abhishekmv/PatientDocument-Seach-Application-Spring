package com.qpidhealth.qpid.search.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.FetchType;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import org.hibernate.annotations.OnDelete;
import org.hibernate.annotations.OnDeleteAction;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "patientdocument")
public class PatientDocument implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @NotNull
    @Column(name = "title")
    private String title;

    @NotNull
    @Column(name = "document", nullable = false)
    private String document;

    // creating one to many relationship. One patient can have many documents attached.
    @ManyToOne(fetch = FetchType.LAZY, optional = false)
    @JoinColumn(name = "patientid", nullable = false)
    @OnDelete(action = OnDeleteAction.CASCADE)
    @JsonIgnore
    private Patient patient;

    @Column(name = "patientid", updatable = false, insertable = false)
    private long patientid;

    public PatientDocument() {

    }

    @JsonCreator
    public PatientDocument(
            @JsonProperty("title") String title,
            @JsonProperty("document") String document,
            @JsonProperty("patient") Patient patient) {
        this.title = title;
        this.document = document;
        this.patient = patient;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public String getDocument() {
        return document;
    }

    public void setDocuments(String document) {
        this.document = document;
    }

    public long getPatientid() {
        return patientid;
    }

    public void setPatientid(long patientid) {
        this.patientid = patientid;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, title, document, patient, patientid);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("title", title)
                .add("document", document)
                .add("patient", patient)
                .add("patientid", patientid)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PatientSearch)) {
            return false;
        }
        PatientDocument other = (PatientDocument) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(title, other.title)
                && Objects.equals(document, other.document)
                && Objects.equals(patient, other.patient)
                && Objects.equals(patientid, other.patientid);
    }
}
