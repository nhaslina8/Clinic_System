/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.view;

/**
 *
 * @author haslina
 */

import com.formdev.flatlaf.FlatClientProperties;
import com.formdev.flatlaf.FlatLightLaf;
import java.awt.*;
import javax.swing.*;

public class LoginScreen extends JFrame {

    private JTextField emailField; // Ditukar dari usernameField ke emailField
    private JPasswordField passwordField;
    private JButton loginBtn;

    public LoginScreen() {
        initComponents();
        setupInteractivity();
    }

    private void initComponents() {
        // 1. Tetapan Asas Tetingkap
        setTitle("Sistem Klinik Pintar - Log Masuk");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(800, 500);
        setLocationRelativeTo(null); // Letak di tengah skrin

        // 2. Panel Latar Belakang Utama (Warna Biru)
        JPanel mainPanel = new JPanel(new GridBagLayout());
        mainPanel.setBackground(new Color(0, 102, 204)); // Biru Korporat

        // 3. Panel "Kad Putih" di tengah-tengah
        JPanel cardPanel = new JPanel();
        cardPanel.setLayout(new BoxLayout(cardPanel, BoxLayout.Y_AXIS));
        cardPanel.setBackground(Color.WHITE);
        cardPanel.setBorder(BorderFactory.createEmptyBorder(40, 50, 40, 50));
        
        // Ciri Khas FlatLaf: Bucu melengkung (Rounded Corners)
        cardPanel.putClientProperty(FlatClientProperties.STYLE, "arc: 20");

        // 4. Tajuk dan Subtajuk
        JLabel titleLabel = new JLabel("SISTEM KLINIK");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 28));
        titleLabel.setForeground(new Color(0, 102, 204));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JLabel subtitleLabel = new JLabel("Sila log masuk untuk teruskan");
        subtitleLabel.setFont(new Font("Segoe UI", Font.PLAIN, 14));
        subtitleLabel.setForeground(Color.GRAY);
        subtitleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 5. Kotak Isian (E-mel & Password)
        JLabel emailLabel = new JLabel("E-mel");
        emailLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        emailLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        emailField = new JTextField(20);
        emailField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        // Ciri Khas FlatLaf: Teks Placeholder
        emailField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan e-mel anda");

        JLabel passLabel = new JLabel("Password");
        passLabel.setFont(new Font("Segoe UI", Font.BOLD, 12));
        passLabel.setAlignmentX(Component.LEFT_ALIGNMENT);
        
        passwordField = new JPasswordField(20);
        passwordField.setMaximumSize(new Dimension(Integer.MAX_VALUE, 35));
        passwordField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Masukkan kata laluan");

        // 6. Butang Log Masuk
        loginBtn = new JButton("Log Masuk");
        loginBtn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        loginBtn.setBackground(new Color(0, 102, 204));
        loginBtn.setForeground(Color.WHITE);
        loginBtn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        loginBtn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 40));
        loginBtn.setAlignmentX(Component.CENTER_ALIGNMENT);

        // 7. Cantumkan semua komponen ke dalam Kad Putih
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

        // Cantumkan Kad Putih ke Panel Biru, dan ke Tetingkap Utama
        mainPanel.add(cardPanel);
        add(mainPanel);
    }

    private void setupInteractivity() {
        // Interaktif 1: Klik butang untuk log masuk
        loginBtn.addActionListener(e -> processLogin());

        // Interaktif 2: Tekan 'Enter' di kotak password terus log masuk
        passwordField.addActionListener(e -> processLogin());
    }

    private void processLogin() {
        String email = emailField.getText().trim();
        String password = new String(passwordField.getPassword()).trim();

        // Validasi: Semak jika pengguna biarkan kotak kosong
        if (email.isEmpty() || password.isEmpty()) {
            JOptionPane.showMessageDialog(this,
                "Sila masukkan E-mel dan Password!",
                "Amaran",
                JOptionPane.WARNING_MESSAGE);
            
            // Fokuskan semula kursor ke kotak yang kosong
            if (email.isEmpty()) emailField.requestFocus();
            else passwordField.requestFocus();
            return;
        }

        // Logik Log Masuk Olok-olok menggunakan data DB sebenar kau
        // Kita anggap password asal mereka ialah "password123"
        if (email.equals("sarah.lim@clinic.local") && password.equals("password123")) {
            JOptionPane.showMessageDialog(this,
                "Log Masuk Berjaya! Selamat datang, Dr. Sarah Lim.",
                "Berjaya",
                JOptionPane.INFORMATION_MESSAGE);

            // Buka DashboardScreen
            new DashboardScreen().setVisible(true);
            this.dispose();
            
        } else if (email.equals("ahmad.albab@example.com") && password.equals("password123")) {
            JOptionPane.showMessageDialog(this,
                "Log Masuk Berjaya! Selamat datang, Ahmad Albab.",
                "Berjaya",
                JOptionPane.INFORMATION_MESSAGE);

            // Buka DashboardScreen
            new DashboardScreen().setVisible(true);
            this.dispose();
            
        } else {
            JOptionPane.showMessageDialog(this,
                "E-mel atau Password salah!",
                "Ralat",
                JOptionPane.ERROR_MESSAGE);
            
            // Interaktif 3: Kosongkan kotak password dan fokus semula ke e-mel
            passwordField.setText("");
            emailField.requestFocus();
        }
    }

    public static void main(String args[]) {
        // Aktifkan tema FlatLaf dengan cara yang paling stabil
        try {
            UIManager.setLookAndFeel(new FlatLightLaf());
        } catch (Exception ex) {
            System.err.println("Gagal memuatkan FlatLaf.");
        }

        // Paparkan antaramuka
        EventQueue.invokeLater(() -> {
            new LoginScreen().setVisible(true);
        });
    }
}