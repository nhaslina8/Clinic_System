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
import java.awt.*;
import javax.swing.*;

public class DashboardScreen extends JFrame {

    public DashboardScreen() {
        initComponents();
    }

    private void initComponents() {
        // 1. Tetapan Asas Tetingkap
        setTitle("Sistem Klinik Pintar - Dashboard Utama");
        setDefaultCloseOperation(JFrame.EXIT_ON_CLOSE);
        setSize(900, 600); // Saiz lebih besar untuk dashboard
        setLocationRelativeTo(null); // Letak di tengah skrin

        // 2. Panel Utama (Membahagikan skrin kepada Kiri dan Kanan)
        JPanel mainPanel = new JPanel(new BorderLayout());

        // 3. Bina Sidebar (Menu Tepi Berwarna Biru)
        JPanel sidebar = new JPanel();
        sidebar.setBackground(new Color(0, 102, 204)); // Biru Korporat
        sidebar.setPreferredSize(new Dimension(250, 0));
        sidebar.setLayout(new BoxLayout(sidebar, BoxLayout.Y_AXIS));
        sidebar.setBorder(BorderFactory.createEmptyBorder(30, 20, 30, 20));

        // Tajuk/Logo di Sidebar
        JLabel brandLabel = new JLabel("KLINIK PINTAR");
        brandLabel.setFont(new Font("Segoe UI", Font.BOLD, 22));
        brandLabel.setForeground(Color.WHITE);
        brandLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Cipta Butang-butang Menu
        JButton btnDaftar = createSidebarButton("Daftar Pesakit Baru");
        JButton btnTemujanji = createSidebarButton("Senarai Temujanji");
        JButton btnBil = createSidebarButton("Urus Bil & Pembayaran");
        JButton btnLogout = createSidebarButton("Log Keluar");
        btnLogout.setForeground(new Color(255, 200, 200)); // Warna sedikit kemerahan untuk amaran

        // Masukkan komponen ke dalam Sidebar
        sidebar.add(brandLabel);
        sidebar.add(Box.createVerticalStrut(50)); // Ruang kosong
        sidebar.add(btnDaftar);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnTemujanji);
        sidebar.add(Box.createVerticalStrut(15));
        sidebar.add(btnBil);
        sidebar.add(Box.createVerticalGlue()); // Tolak butang Log Keluar ke bawah sekali
        sidebar.add(btnLogout);

        // 4. Bina Ruangan Kandungan (Kawasan Putih)
        JPanel contentPanel = new JPanel(new GridBagLayout());
        contentPanel.setBackground(Color.WHITE);
        
        JLabel welcomeLabel = new JLabel("Selamat Datang ke Sistem Pengurusan Klinik");
        welcomeLabel.setFont(new Font("Segoe UI", Font.BOLD, 26));
        welcomeLabel.setForeground(Color.DARK_GRAY);
        
        JLabel subWelcomeLabel = new JLabel("Sila pilih menu di sebelah kiri untuk bermula.");
        subWelcomeLabel.setFont(new Font("Segoe UI", Font.PLAIN, 16));
        subWelcomeLabel.setForeground(Color.GRAY);

        // Susun teks 'Welcome' di tengah
        JPanel welcomeWrapper = new JPanel();
        welcomeWrapper.setLayout(new BoxLayout(welcomeWrapper, BoxLayout.Y_AXIS));
        welcomeWrapper.setBackground(Color.WHITE);
        welcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        subWelcomeLabel.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        welcomeWrapper.add(welcomeLabel);
        welcomeWrapper.add(Box.createVerticalStrut(10));
        welcomeWrapper.add(subWelcomeLabel);
        
        contentPanel.add(welcomeWrapper);

        // 5. Cantumkan Sidebar dan Kandungan ke Panel Utama
        mainPanel.add(sidebar, BorderLayout.WEST);
        mainPanel.add(contentPanel, BorderLayout.CENTER);
        
        add(mainPanel);

        // 6. Interaktiviti: Butang Log Keluar
        btnLogout.addActionListener(e -> {
            // Tanya kepastian sebelum log keluar
            int confirm = JOptionPane.showConfirmDialog(this, 
                    "Adakah anda pasti mahu log keluar?", 
                    "Log Keluar", 
                    JOptionPane.YES_NO_OPTION);
                    
            if (confirm == JOptionPane.YES_OPTION) {
                new LoginScreen().setVisible(true); // Buka login semula
                this.dispose(); // Tutup dashboard
            }
        });
    }

    // Fungsi khas untuk mencantikkan butang di Sidebar (supaya seragam)
    private JButton createSidebarButton(String text) {
        JButton btn = new JButton(text);
        btn.setFont(new Font("Segoe UI", Font.BOLD, 14));
        btn.setForeground(Color.WHITE);
        btn.setBackground(new Color(0, 85, 180)); // Biru sedikit gelap dari latar belakang
        btn.setCursor(new Cursor(Cursor.HAND_CURSOR));
        btn.setMaximumSize(new Dimension(Integer.MAX_VALUE, 45));
        btn.setAlignmentX(Component.CENTER_ALIGNMENT);
        
        // Ciri Khas FlatLaf: Buang garisan border dan jadikan bucu butang sedikit melengkung
        btn.putClientProperty(FlatClientProperties.STYLE, "arc: 10; borderWidth: 0; focusWidth: 0;");
        return btn;
    }
}
