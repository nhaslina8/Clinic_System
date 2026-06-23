/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.view;
import java.awt.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author mira
 */
public class AppointmentList extends JFrame {

    private JTable table;
    private DefaultTableModel tableModel;

    public AppointmentList() {
        setTitle("Sistem Klinik Pintar - Senarai Temujanji");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(700, 500);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(10, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));

        // Top Panel: Search Bar
        JPanel topPanel = new JPanel(new FlowLayout(FlowLayout.LEFT));
        JTextField searchField = new JTextField(20);
        JButton btnCari = new JButton("Cari");
        topPanel.add(new JLabel("Cari Pesakit:"));
        topPanel.add(searchField);
        topPanel.add(btnCari);

        // Center Panel: Table (Display functionality)
        String[] columns = {"ID", "Nama Pesakit", "Tarikh", "Masa", "Status"};
        tableModel = new DefaultTableModel(columns, 0);
        table = new JTable(tableModel);
        
        // Mock Data
        tableModel.addRow(new Object[]{"T01", "Ahmad Albab", "25-10-2023", "10:00 AM", "Menunggu"});
        tableModel.addRow(new Object[]{"T02", "Siti Nurhaliza", "25-10-2023", "11:30 AM", "Selesai"});

        JScrollPane scrollPane = new JScrollPane(table);

        // Bottom Panel: Action Buttons
        JPanel bottomPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT));
        JButton btnPadam = new JButton("Padam Temujanji");
        btnPadam.setBackground(Color.RED);
        btnPadam.setForeground(Color.WHITE);

        btnPadam.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Delete functionality
                tableModel.removeRow(selectedRow);
                JOptionPane.showMessageDialog(this, "Temujanji dipadam.");
                // TODO: Delete from your List Data Structure and update File I/O
            } else {
                JOptionPane.showMessageDialog(this, "Sila pilih baris untuk dipadam.");
            }
        });

        bottomPanel.add(btnPadam);

        mainPanel.add(topPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(bottomPanel, BorderLayout.SOUTH);

        add(mainPanel);
    }
}
