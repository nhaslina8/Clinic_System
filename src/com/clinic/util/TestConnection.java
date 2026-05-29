/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.util;

import com.clinic.controller.ClinicController;
import com.clinic.model.Patient;
import java.time.LocalDate;
import java.sql.SQLException;

/**
 *
 * @author haslina
 */
public class TestConnection {
    public static void main(String[] args) {
        
        ClinicController controller = new ClinicController();

        // 1. Cipta data pesakit olok-olok (dummy patient)
        // Parameter: id, fullName, email, dateOfBirth, phone, address, medicalRecord
        Patient newPatient = new Patient(
            0, // Biarkan 0 kerana ID akan dijana secara automatik (Auto-Increment) oleh MySQL
            "Ahmad Albab",
            "ahmad.albab@example.com",
            LocalDate.of(1995, 8, 24), // Format: Tahun, Bulan, Hari
            "0198765432",
            "No 12, Jalan Merpati, Kuala Lumpur",
            "Tiada alahan ubat. Tekanan darah normal."
        );

        // 2. Cipta hash password olok-olok
        // Menggunakan format SHA-256 (64 aksara) supaya melepasi fungsi validatePasswordHash anda.
        String dummyPasswordHash = "e3b0c44298fc1c149afbf4c8996fb92427ae41e4649b934ca495991b7852b855";

        System.out.println("Memulakan ujian menyimpan data pesakit ke pangkalan data awan...");

        // 3. Cuba laksanakan fungsi addPatient dan tangkap sebarang ralat (Exception)
        try {
            controller.addPatient(newPatient, dummyPasswordHash);
            System.out.println("✅ BERJAYA! Data pesakit '" + newPatient.getFullName() + "' telah berjaya disimpan.");
            System.out.println("Sila semak jadual 'users' dan 'patients' dalam MySQL Workbench anda.");
            
        } catch (SQLException e) {
            System.out.println("❌ GAGAL: Berlaku ralat pangkalan data (SQL).");
            e.printStackTrace();
        } catch (IllegalArgumentException e) {
            System.out.println("❌ GAGAL: Ralat validasi data.");
            System.out.println("Mesej: " + e.getMessage());
        }
    }
}