package com.clinic.model;

import java.sql.Timestamp;

public class Appointment {
    private final int appointmentId;
    private final String patientName;
    private final String doctorName;
    private final Timestamp appointmentTime;
    private final String status;

    public Appointment(int appointmentId, String patientName, String doctorName, Timestamp appointmentTime, String status) {
        this.appointmentId = appointmentId;
        this.patientName = patientName;
        this.doctorName = doctorName;
        this.appointmentTime = appointmentTime;
        this.status = status;
    }

    public int getAppointmentId() {
        return appointmentId;
    }

    public String getPatientName() {
        return patientName;
    }

    public String getDoctorName() {
        return doctorName;
    }

    public Timestamp getAppointmentTime() {
        return appointmentTime;
    }

    public String getStatus() {
        return status;
    }
}
