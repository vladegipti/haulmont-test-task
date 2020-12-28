package com.haulmont.testtask.model;

import java.util.Date;

public class Recipe {
    private long id;
    private String description;
    private Patient patient;
    private Doctor doctor;
    private Date dateOfIssue;
    private int validFor;
    private String priority;

    public Recipe() {
    }

    public Recipe(String description, Patient patient, Doctor doctor, Date dateOfIssue, int validFor, String priority) {
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.dateOfIssue = dateOfIssue;
        this.validFor = validFor;
        this.priority = priority;
    }

    public Recipe(long id, String description, Patient patient, Doctor doctor, Date dateOfIssue, int validFor, String priority) {
        this.id = id;
        this.description = description;
        this.patient = patient;
        this.doctor = doctor;
        this.dateOfIssue = dateOfIssue;
        this.validFor = validFor;
        this.priority = priority;
    }

    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public Patient getPatient() {
        return patient;
    }

    public void setPatient(Patient patient) {
        this.patient = patient;
    }

    public Doctor getDoctor() {
        return doctor;
    }

    public void setDoctor(Doctor doctor) {
        this.doctor = doctor;
    }

    public Date getDateOfIssue() {
        return dateOfIssue;
    }

    public void setDateOfIssue(Date dateOfIssue) {
        this.dateOfIssue = dateOfIssue;
    }

    public int getValidFor() {
        return validFor;
    }

    public void setValidFor(int validFor) {
        this.validFor = validFor;
    }

    public String getPriority() {
        return priority;
    }

    public void setPriority(String priority) {
        this.priority = priority;
    }
}
