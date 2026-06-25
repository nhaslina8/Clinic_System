package com.clinic.controller;

import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import com.clinic.util.DatabaseConnection;
import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.sql.Statement;
import java.sql.Timestamp;
import java.util.ArrayList;
import java.util.List;
import java.util.Set;

public class ClinicController {

    private boolean emailExists(Connection connection, String email) throws SQLException {
        String sql = "SELECT 1 FROM users WHERE email = ?";
        try (PreparedStatement statement = connection.prepareStatement(sql)) {
            statement.setString(1, email);
            try (ResultSet resultSet = statement.executeQuery()) {
                return resultSet.next();
            }
        }
    }

    // 1. ADD PATIENT (Kemaskini dengan ic_number)
    public void addPatient(Patient patient, String passwordHash) throws SQLException {
        validatePasswordHash(passwordHash);

        String insertUserSql = "INSERT INTO users(full_name, email, password_hash, role) VALUES (?, ?, ?, 'PATIENT')";
        String insertPatientSql = "INSERT INTO patients(user_id, ic_number, date_of_birth, phone, address, medical_record) VALUES (?, ?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (emailExists(connection, patient.getEmail())) {
                throw new SQLException("Pengguna dengan e-mel '" + patient.getEmail() + "' telah wujud.");
            }
            connection.setAutoCommit(false);
            try (PreparedStatement userStatement = connection.prepareStatement(insertUserSql, Statement.RETURN_GENERATED_KEYS);
                 PreparedStatement patientStatement = connection.prepareStatement(insertPatientSql)) {

                userStatement.setString(1, patient.getFullName());
                userStatement.setString(2, patient.getEmail());
                userStatement.setString(3, passwordHash);
                userStatement.executeUpdate();

                try (ResultSet generatedKeys = userStatement.getGeneratedKeys()) {
                    if (!generatedKeys.next()) {
                        throw new SQLException("Gagal mendaftar pengguna baharu.");
                    }
                    int userId = generatedKeys.getInt(1);
                    java.sql.Date dateOfBirth = patient.getDateOfBirth() == null ? null : java.sql.Date.valueOf(patient.getDateOfBirth());

                    patientStatement.setInt(1, userId);
                    patientStatement.setString(2, patient.getIcNumber());
                    patientStatement.setDate(3, dateOfBirth);
                    patientStatement.setString(4, patient.getPhone());
                    patientStatement.setString(5, patient.getAddress());
                    patientStatement.setString(6, patient.getMedicalRecord());
                    patientStatement.executeUpdate();
                }
                connection.commit();
            } catch (SQLException exception) {
                connection.rollback();
                throw exception;
            } finally {
                connection.setAutoCommit(true);
            }
        }
    }

    public List<Appointment> getAppointmentList() throws SQLException {
        String sql = """
                SELECT a.appointment_id,
                       u_p.full_name AS patient_name,
                       u_d.full_name AS doctor_name,
                       a.appointment_time,
                       a.status
                FROM appointments a
                JOIN patients p ON a.patient_id = p.patient_id
                JOIN users u_p ON p.user_id = u_p.user_id
                JOIN doctors d ON a.doctor_id = d.doctor_id
                JOIN users u_d ON d.user_id = u_d.user_id
                ORDER BY a.appointment_time
                """;

        List<Appointment> appointments = new ArrayList<>();

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql);
             ResultSet resultSet = statement.executeQuery()) {

            while (resultSet.next()) {
                int appointmentId = resultSet.getInt("appointment_id");
                String patientName = resultSet.getString("patient_name");
                String doctorName = resultSet.getString("doctor_name");
                Timestamp appointmentTime = resultSet.getTimestamp("appointment_time");
                String status = resultSet.getString("status");

                appointments.add(new Appointment(
                        appointmentId,
                        patientName,
                        doctorName,
                        appointmentTime,
                        status));
            }
        }

        return appointments;
    }

    public boolean updateBilling(int billId, BigDecimal amount, String billingStatus) throws SQLException {
        validateAmount(amount);
        validateBillingStatus(billingStatus);

        String sql = """
                UPDATE bills
                SET amount = ?,
                    billing_status = ?,
                    paid_at = CASE WHEN ? = 'PAID' THEN CURRENT_TIMESTAMP ELSE NULL END
                WHERE bill_id = ?
                """;

        try (Connection connection = DatabaseConnection.getConnection();
             PreparedStatement statement = connection.prepareStatement(sql)) {

            statement.setBigDecimal(1, amount);
            statement.setString(2, billingStatus);
            statement.setString(3, billingStatus);
            statement.setInt(4, billId);

            return statement.executeUpdate() > 0;
        }
    }

    private void validatePasswordHash(String passwordHash) {
        if (passwordHash == null || passwordHash.isBlank()) {
            throw new IllegalArgumentException("passwordHash must be a non-empty hash value.");
        }

        boolean isBcrypt = passwordHash.matches("^\\$2[aby]\\$.{56}$");
        boolean isSha256Hex = passwordHash.matches("^[A-Fa-f0-9]{64}$");
        boolean isSha256Prefixed = passwordHash.matches("^sha256:[A-Fa-f0-9]{64}$");

        if (!isBcrypt && !isSha256Hex && !isSha256Prefixed) {
            throw new IllegalArgumentException("passwordHash must be a valid BCrypt or SHA-256 hash value.");
        }
    }

    private void validateBillingStatus(String billingStatus) {
        Set<String> validStatuses = Set.of("UNPAID", "PAID", "PARTIAL");
        if (!validStatuses.contains(billingStatus)) {
            throw new IllegalArgumentException("billingStatus must be one of: UNPAID, PAID, PARTIAL.");
        }
    }

    private void validateAmount(BigDecimal amount) {
        if (amount == null || amount.signum() <= 0) {
            throw new IllegalArgumentException("amount must be a positive value.");
        }
    }
    // 1. Ambil Semua Senarai Pesakit dari Database untuk dimasukkan ke JTable
   // 2. GET PATIENT LIST (Kemaskini dengan p.ic_number)
    public List<Patient> getPatientList() throws SQLException {
        String sql = "SELECT u.user_id, u.full_name, u.email, p.ic_number, p.date_of_birth, p.phone, p.address, p.medical_record " +
                     "FROM users u JOIN patients p ON u.user_id = p.user_id WHERE u.role = 'PATIENT'";
        List<Patient> list = new ArrayList<>();
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            while (rs.next()) {
                java.sql.Date dbDate = rs.getDate("date_of_birth");
                java.time.LocalDate dob = (dbDate != null) ? dbDate.toLocalDate() : null;
                list.add(new Patient(
                    rs.getInt("user_id"),
                    rs.getString("full_name"),
                    rs.getString("ic_number"),
                    rs.getString("email"),
                    dob,
                    rs.getString("phone"),
                    rs.getString("address"),
                    rs.getString("medical_record")
                ));
            }
        }
        return list;
    }

    // 3. UPDATE PATIENT (Kemaskini dengan ic_number)
    public boolean updatePatient(Patient patient) throws SQLException {
        String updateUsers = "UPDATE users SET full_name = ?, email = ? WHERE user_id = ?";
        String updatePatients = "UPDATE patients SET ic_number = ?, date_of_birth = ?, phone = ?, address = ?, medical_record = ? WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt1 = conn.prepareStatement(updateUsers);
                 PreparedStatement stmt2 = conn.prepareStatement(updatePatients)) {
                
                stmt1.setString(1, patient.getFullName());
                stmt1.setString(2, patient.getEmail());
                stmt1.setInt(3, patient.getId());
                stmt1.executeUpdate();

                java.sql.Date dob = (patient.getDateOfBirth() == null) ? null : java.sql.Date.valueOf(patient.getDateOfBirth());
                stmt2.setString(1, patient.getIcNumber());
                stmt2.setDate(2, dob);
                stmt2.setString(3, patient.getPhone());
                stmt2.setString(4, patient.getAddress());
                stmt2.setString(5, patient.getMedicalRecord());
                stmt2.setInt(6, patient.getId());
                stmt2.executeUpdate();

                conn.commit();
                return true;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }

    // 3. Fungsi Padam Rekod Pesakit (Delete KPI)
    public boolean deletePatient(int patientId) throws SQLException {
        String deletePatients = "DELETE FROM patients WHERE user_id = ?";
        String deleteUsers = "DELETE FROM users WHERE user_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection()) {
            conn.setAutoCommit(false);
            try (PreparedStatement stmt1 = conn.prepareStatement(deletePatients);
                 PreparedStatement stmt2 = conn.prepareStatement(deleteUsers)) {
                
                stmt1.setInt(1, patientId);
                stmt1.executeUpdate();

                stmt2.setInt(1, patientId);
                int rows = stmt2.executeUpdate();

                conn.commit();
                return rows > 0;
            } catch (SQLException e) {
                conn.rollback();
                throw e;
            } finally {
                conn.setAutoCommit(true);
            }
        }
    }
    
    // 4. Fungsi Padam Temujanji (Delete Appointment)
    public boolean deleteAppointment(int appointmentId) throws SQLException {
        String sql = "DELETE FROM appointments WHERE appointment_id = ?";
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            
            stmt.setInt(1, appointmentId);
            int rowsAffected = stmt.executeUpdate();
            
            return rowsAffected > 0;
        }
    }
    // Fungsi Tambah Temujanji Baru (Create)
    public void addAppointment(int patientId, int doctorId, Timestamp appointmentTime, String status) throws SQLException {
        String sql = "INSERT INTO appointments (patient_id, doctor_id, appointment_time, status) VALUES (?, ?, ?, ?)";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setInt(1, patientId);
            stmt.setInt(2, doctorId);
            stmt.setTimestamp(3, appointmentTime);
            stmt.setString(4, status);
            stmt.executeUpdate();
        }
    }

    // Fungsi Kemaskini Masa & Status Temujanji (Update)
    public boolean updateAppointmentDetails(int appointmentId, Timestamp appointmentTime, String status) throws SQLException {
        String sql = "UPDATE appointments SET appointment_time = ?, status = ? WHERE appointment_id = ?";
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql)) {
            stmt.setTimestamp(1, appointmentTime);
            stmt.setString(2, status);
            stmt.setInt(3, appointmentId);
            return stmt.executeUpdate() > 0;
        }
    }
    // Fungsi untuk mendapatkan senarai Doktor untuk Dropdown
    public List<String[]> getActiveDoctors() throws SQLException {
        // Query ini mencantumkan jadual doctors dan users untuk dapatkan nama dan ID
        String sql = "SELECT d.doctor_id, u.full_name FROM doctors d JOIN users u ON d.user_id = u.user_id WHERE u.role = 'DOCTOR'";
        List<String[]> list = new java.util.ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("doctor_id")), 
                    rs.getString("full_name")
                });
            }
        }
        return list;
    }
    // Fungsi untuk mendapatkan senarai Pesakit dengan PATIENT_ID yang betul untuk Dropdown
    public List<String[]> getActivePatients() throws SQLException {
        // Query ini wajib ambil p.patient_id, BUKAN u.user_id
        String sql = "SELECT p.patient_id, u.full_name FROM patients p JOIN users u ON p.user_id = u.user_id WHERE u.role = 'PATIENT'";
        List<String[]> list = new java.util.ArrayList<>();
        
        try (Connection conn = DatabaseConnection.getConnection();
             PreparedStatement stmt = conn.prepareStatement(sql);
             ResultSet rs = stmt.executeQuery()) {
            
            while (rs.next()) {
                list.add(new String[]{
                    String.valueOf(rs.getInt("patient_id")), 
                    rs.getString("full_name")
                });
            }
        }
        return list;
    }
}
