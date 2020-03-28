package com.qpidhealth.qpid.search.model;

import java.io.Serializable;
import java.util.Objects;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.Table;
import javax.validation.constraints.NotNull;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;
import com.google.common.base.MoreObjects;

@Entity
@Table(name = "patient")
public class Patient implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "patientname")
    @NotNull
    private String patientname;

    public Patient() {
    }

    @JsonCreator
    public Patient(@JsonProperty("patientname") String patientname) {
        this.patientname = patientname;
    }

    public Long getId() {
        return id;
    }

    public String getPatientname() {
        return patientname;
    }

    public void setId(long id) {
        this.id = id;
    }

    public void setPatientname(String patientname) {
        this.patientname = patientname;
    }

    @Override
    public int hashCode() {
        return Objects.hash(id, patientname);
    }

    @Override
    public String toString() {
        return MoreObjects.toStringHelper(this)
                .add("id", id)
                .add("patientname", patientname)
                .toString();
    }

    @Override
    public boolean equals(Object obj) {
        if (obj == this) {
            return true;
        }
        if (!(obj instanceof Patient)) {
            return false;
        }
        Patient other = (Patient) obj;
        return Objects.equals(id, other.id)
                && Objects.equals(patientname, other.patientname);
    }
}
