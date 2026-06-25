/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.view;

/**
 *
 * @author haslina
 */

/**
 *
 * @author mira
 */

import com.clinic.controller.ClinicController;
import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import javax.swing.*;

public class LoginScreen extends JFrame {

    private JTextField emailField; 
    private JPasswordField passwordField;
    private JButton loginBtn;
    private ClinicController clinicController; // Panggil backend

    public LoginScreen() {
        clinicController = new ClinicController();
        initComponents();
        setupInteractivity();
    }

    private void initComponents() {
        setTitle("Sistem Klinik Pintar - Log Masuk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null); 

        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0, 102, 204)); 

        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");

        JLabel titleLabel = new JLabel("SISTEM KLINIK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sila log masuk untuk teruskan");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel emailLabel = new JLabel("E-mel");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan e-mel anda");

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan kata laluan");

        loginBtn = new JButton("Log Masuk");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(0, 102, 204));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        cardPanel.add(titleLabel);
        cardPanel.add(Box.createVerticalStrut(10));
        cardPanel.add(subtitleLabel);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(emailLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(emailField);
        cardPanel.add(Box.createVerticalStrut(15));
        cardPanel.add(passLabel);
        cardPanel.add(Box.createVerticalStrut(5));
        cardPanel.add(passwordField);
        cardPanel.add(Box.createVerticalStrut(30));
        cardPanel.add(loginBtn);

        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private void setupInteractivity() {
        loginBtn.addActionListener(e -> processLogin());
        passwordField.addActionListener(e -> processLogin());
    }

    private void processLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Sila masukkan E-mel dan Password!",
                "Amaran",
                JOptionPane.WARNING_MESSAGE);
            
            if (email.isEmpty()) emailField.requestFocus();
            else passwordField.requestFocus();
            return;
        }

        try {
            // Tukar password biasa kepada SHA-256 Hash
            String hashedPassword = generateSHA256Hash(password);
            
            // Periksa dengan Pangkalan Data Sebenar
            boolean isValidUser = clinicController.authenticateUser(email, hashedPassword);

            if (isValidUser) {
                JOptionPane.showMessageDialog(this,
                    "Log Masuk Berjaya!",
                    "Berjaya",
                    JOptionPane.INFORMATION_MESSAGE);

                new DashboardScreen().setVisible(true);
                this.dispose();
                
            } else {
                JOptionPane.showMessageDialog(this,
                    "E-mel atau Password salah!",
                    "Ralat Log Masuk",
                    JOptionPane.ERROR_MESSAGE);
                
                passwordField.setText("");
                emailField.requestFocus();
            }
        } catch (SQLException ex) {
            JOptionPane.showMessageDialog(this,
                "Gagal menyambung ke pangkalan data: " + ex.getMessage(),
                "Ralat Sistem",
                JOptionPane.ERROR_MESSAGE);
        }
    }

    // Fungsi wajib untuk menyulitkan kata laluan supaya sepadan dengan rekod DB
    private String generateSHA256Hash(String base) {
        try {
            MessageDigest digest = MessageDigest.getInstance("SHA-256");
            byte[] hash = digest.digest(base.getBytes(StandardCharsets.UTF_8));
            StringBuilder hexString = new StringBuilder(2 * hash.length);
            for (byte b : hash) {
                String hex = Integer.toHexString(0xff & b);
                if (hex.length() == 1) hexString.append('0');
                hexString.append(hex);
            }
            return hexString.toString();
        } catch (Exception ex) {
            throw new RuntimeException("Ralat penyulitan kata laluan.", ex);
        }
    }

    public static void main(String args[]) {
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal memuatkan FlatLaf.");
        }

        EventQueue.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}