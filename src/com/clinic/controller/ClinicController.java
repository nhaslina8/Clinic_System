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

    public void addPatient(Patient patient, String passwordHash) throws SQLException {
        validatePasswordHash(passwordHash);

        String insertUserSql = "INSERT INTO users(full_name, email, password_hash, role) VALUES (?, ?, ?, 'PATIENT')";
        String insertPatientSql = "INSERT INTO patients(user_id, date_of_birth, phone, address, medical_record) VALUES (?, ?, ?, ?, ?)";

        try (Connection connection = DatabaseConnection.getConnection()) {
            if (emailExists(connection, patient.getEmail())) {
                throw new SQLException("User with email '" + patient.getEmail() + "' already exists.");
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
                        throw new SQLException("Failed to insert user record: no generated key returned.");
                    }

                    int userId = generatedKeys.getInt(1);
                    java.sql.Date dateOfBirth = patient.getDateOfBirth() == null
                            ? null
                            : java.sql.Date.valueOf(patient.getDateOfBirth());

                    patientStatement.setInt(1, userId);
                    patientStatement.setDate(2, dateOfBirth);
                    patientStatement.setString(3, patient.getPhone());
                    patientStatement.setString(4, patient.getAddress());
                    patientStatement.setString(5, patient.getMedicalRecord());
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
}
