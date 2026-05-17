package com.clinic.model;

public class Doctor extends Person {
    private String specialization;
    private String licenseNumber;

    public Doctor(int id, String fullName, String email, String specialization, String licenseNumber) {
        super(id, fullName, email);
        this.specialization = specialization;
        this.licenseNumber = licenseNumber;
    }

    public String getSpecialization() {
        return specialization;
    }

    public void setSpecialization(String specialization) {
        this.specialization = specialization;
    }

    public String getLicenseNumber() {
        return licenseNumber;
    }

    public void setLicenseNumber(String licenseNumber) {
        this.licenseNumber = licenseNumber;
    }
}
