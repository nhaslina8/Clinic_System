package com.clinic.model;

import java.time.LocalDate;

public class Patient extends Person {
    private LocalDate dateOfBirth;
    private String phone;
    private String address;
    private String medicalRecord;

    public Patient(int id, String fullName, String email, LocalDate dateOfBirth, String phone, String address, String medicalRecord) {
        super(id, fullName, email);
        this.dateOfBirth = dateOfBirth;
        this.phone = phone;
        this.address = address;
        this.medicalRecord = medicalRecord;
    }

    public LocalDate getDateOfBirth() {
        return dateOfBirth;
    }

    public void setDateOfBirth(LocalDate dateOfBirth) {
        this.dateOfBirth = dateOfBirth;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getMedicalRecord() {
        return medicalRecord;
    }

    public void setMedicalRecord(String medicalRecord) {
        this.medicalRecord = medicalRecord;
    }
}
