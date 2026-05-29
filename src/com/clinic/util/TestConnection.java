/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.util;
import java.sql.Connection;
import java.sql.SQLException;

/**
 *
 * @author haslina
 */
public class TestConnection {
     public static void main(String[] args) throws SQLException {
        System.out.println("Memulakan ujian sambungan pangkalan data...");
        
        // Memanggil fungsi sambungan daripada kelas DatabaseConnection anda
        Connection conn = DatabaseConnection.getConnection();
        
        if (conn != null) {
            System.out.println("TAHNIAH! NetBeans berjaya disambungkan ke MySQL Workbench.");
        } else {
            System.out.println("RALAT! Sambungan gagal. Sila periksa semula langkah atau status MySQL anda.");
        }
    }
}
