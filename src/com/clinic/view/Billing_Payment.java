/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.view;
import java.awt.*;
import javax.swing.*;

/**
 *
 * @author mira
 */
public class Billing_Payment extends JFrame {

    private JTextField rawatanField, ubatField;
    private JLabel totalLabel;

    public Billing_Payment () {
        setTitle("Sistem Klinik Pintar - Urus Bil & Pembayaran");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(500, 450);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel();
        mainPanel.setLayout(new BoxLayout(mainPanel, BoxLayout.Y_AXIS));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 30, 20, 30));

        JLabel titleLabel = new JLabel("Pengiraan Bil Klinik");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setAlignmentX(Component.CENTER_ALIGNMENT);

        JPanel formPanel = new JPanel(new GridLayout(3, 2, 10, 15));
        formPanel.setMaximumSize(new Dimension(400, 150));
        
        formPanel.add(new JLabel("Kos Rawatan (RM):"));
        rawatanField = new JTextField("0.00");
        formPanel.add(rawatanField);

        formPanel.add(new JLabel("Kos Ubat-ubatan (RM):"));
        ubatField = new JTextField("0.00");
        formPanel.add(ubatField);

        formPanel.add(new JLabel("JUMLAH KESELURUHAN:"));
        totalLabel = new JLabel("RM 0.00");
        totalLabel.setFont(new Font("Segoe UI", Font.BOLD, 16));
        totalLabel.setForeground(Color.RED);
        formPanel.add(totalLabel);

        JButton btnKira = new JButton("Kira Jumlah");
        JButton btnBayar = new JButton("Sahkan Pembayaran");
        
        btnKira.setAlignmentX(Component.CENTER_ALIGNMENT);
        btnBayar.setAlignmentX(Component.CENTER_ALIGNMENT);

        // Calculation Accuracy Logic
        btnKira.addActionListener(e -> {
            try {
                double rawatan = Double.parseDouble(rawatanField.getText());
                double ubat = Double.parseDouble(ubatField.getText());
                double jumlah = rawatan + ubat;
                totalLabel.setText(String.format("RM %.2f", jumlah));
            } catch (NumberFormatException ex) {
                JOptionPane.showMessageDialog(this, "Sila masukkan nombor yang sah!", "Ralat Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        btnBayar.addActionListener(e -> {
            // TODO: Update Functionality - Update record in List/File to 'Paid'
            JOptionPane.showMessageDialog(this, "Pembayaran sebanyak " + totalLabel.getText() + " telah disahkan.");
            this.dispose();
        });

        mainPanel.add(titleLabel);
        mainPanel.add(Box.createVerticalStrut(30));
        mainPanel.add(formPanel);
        mainPanel.add(Box.createVerticalStrut(20));
        mainPanel.add(btnKira);
        mainPanel.add(Box.createVerticalStrut(10));
        mainPanel.add(btnBayar);

        add(mainPanel);
    }
}
