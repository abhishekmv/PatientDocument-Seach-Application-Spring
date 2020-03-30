package com.qpidhealth.qpid.search.model;

import java.io.Serializable;
import java.util.Objects;

import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

/**
 * A patient search object class 
 */
public class PatientSearch implements Serializable {

    private static final long serialVersionUID = 1L;

    @JsonProperty("PatientName")
    private final String name;

    @JsonProperty("DocumentTitle")
    private final String title;

    @JsonProperty("DocumentDetails")
    private final String document;

    public PatientSearch(String name, String title, String document) {
        this.name = name;
        this.title = title;
        this.document = document;
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
        return Objects.hash(name, title, document);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
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
        if (!(obj instanceof PatientSearch)) {
            return false;
        }
        PatientSearch other = (PatientSearch) obj;
        return Objects.equals(name, other.name)
                && Objects.equals(title, other.title)
                && Objects.equals(document, other.document);
    }
}