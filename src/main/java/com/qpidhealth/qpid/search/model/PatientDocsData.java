package com.qpidhealth.qpid.search.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;


/**
 * A patient document data object which has patient and their document details
 *
 */
public class PatientDocsData implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("DocumentId")
    private final long documentid;

    @JsonProperty("PatientId")
    private final long patientid;

    @JsonProperty("PatientName")
    private final String name;

    @JsonProperty("DocumentTitle")
    private final String title;

    @JsonProperty("DocumentDetials")
    private final String document;

    public PatientDocsData(long documentid, long patientid, String name, String title,
            String document) {
        this.documentid = documentid;
        this.patientid = patientid;
        this.name = name;
        this.title = title;
        this.document = document;
    }

    public long getId() {
        return documentid;
    }

    public long getPatientid() {
        return patientid;
    }

    public String getName() {
        return name;
    }

    public String getTitle() {
        return title;
    }

    public String getDocument() {
        return document;
    }

    @Override
    public int hashCode() {
        return Objects.hash(documentid, patientid, name, title, document);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("documentid", documentid)
                .add("patientid", patientid)
                .add("name", name)
                .add("title", title)
                .add("document", document)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof PatientDocsData)) {
            return false;
        }
        PatientDocsData other = (PatientDocsData) obj;
        return Objects.equals(documentid, other.documentid)
                && Objects.equals(patientid, other.patientid)
                && Objects.equals(title, other.title)
                && Objects.equals(title, other.title)
                && Objects.equals(document, other.document);
    }
}
