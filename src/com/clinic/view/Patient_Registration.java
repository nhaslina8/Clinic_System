/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.view;

import com.clinic.controller.ClinicController;
import com.clinic.model.Patient;
import com.formdev.flatlaf.FlatClientProperties; 

import java.awt.*;
import java.nio.charset.StandardCharsets;
import java.security.MessageDigest;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class Patient_Registration extends JFrame {

    private JTextField namaField, emailField, phoneField, dobField, searchField;
    private JPasswordField passwordField;
    private JTextArea addressArea, medicalRecordArea;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JButton btnSimpan, btnKosongkan, btnEdit, btnDelete;
    
    private ClinicController clinicController;
    private int editingRow = -1; 
    private int editingPatientId = -1;

    public Patient_Registration() {
        clinicController = new ClinicController();

        // 1. Tetapan Asas Tetingkap
        setTitle("Sistem Klinik Pintar - Pengurusan Pesakit");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(850, 600));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 10)); // Kurangkan gap menegak
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20)); // Kurangkan padding atas
        mainPanel.setBackground(new Color(245, 247, 250));

        // Title Label
        JLabel titleLabel = new JLabel("Pendaftaran & Pengurusan Pesakit");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20)); // Saiz font tajuk dikurangkan sikit
        titleLabel.setForeground(new Color(33, 37, 41));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ==========================================
        // 2. Bahagian Borang (Versi Compact)
        // ==========================================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        TitledBorder formBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), "Maklumat Pesakit");
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        formBorder.setTitleColor(new Color(70, 70, 70));
        // Padding dalam borang diminimumkan
        formPanel.setBorder(BorderFactory.createCompoundBorder(formBorder, BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(4, 15, 4, 15); // INSETS DIKURANGKAN SECARA DRASTIK (ruang atas bawah input)
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13); // Font input sedikit padat

        // Baris 1
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15;
        JLabel lblNama = new JLabel("Nama Penuh:"); lblNama.setFont(labelFont);
        formPanel.add(lblNama, gbc);
        namaField = new JTextField(); namaField.setFont(fieldFont);
        applyPlaceholder(namaField, "Cth: Ahmad bin Abu");
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.35;
        formPanel.add(namaField, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.15;
        JLabel lblPhone = new JLabel("No. Telefon:"); lblPhone.setFont(labelFont);
        formPanel.add(lblPhone, gbc);
        phoneField = new JTextField(); phoneField.setFont(fieldFont);
        applyPlaceholder(phoneField, "Cth: 0123456789");
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.35;
        formPanel.add(phoneField, gbc);

        // Baris 2
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.15;
        JLabel lblEmail = new JLabel("E-mel:"); lblEmail.setFont(labelFont);
        formPanel.add(lblEmail, gbc);
        emailField = new JTextField(); emailField.setFont(fieldFont);
        applyPlaceholder(emailField, "Cth: ahmad@email.com");
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.35;
        formPanel.add(emailField, gbc);

        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.15;
        JLabel lblDob = new JLabel("Tarikh Lahir:"); lblDob.setFont(labelFont);
        formPanel.add(lblDob, gbc);
        dobField = new JTextField(); dobField.setFont(fieldFont);
        applyPlaceholder(dobField, "YYYY-MM-DD");
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.35;
        formPanel.add(dobField, gbc);

        // Baris 3
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.15;
        JLabel lblPass = new JLabel("Kata Laluan:"); lblPass.setFont(labelFont);
        formPanel.add(lblPass, gbc);
        passwordField = new JPasswordField(); passwordField.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.35;
        formPanel.add(passwordField, gbc);

        // Baris 4 - JTextArea padat
        gbc.gridx = 0; gbc.gridy = 3; gbc.weightx = 0.15; gbc.anchor = GridBagConstraints.NORTHWEST;
        JLabel lblAddr = new JLabel("Alamat:"); lblAddr.setFont(labelFont);
        formPanel.add(lblAddr, gbc);
        addressArea = new JTextArea(2, 20); addressArea.setFont(fieldFont);
        addressArea.setLineWrap(true); addressArea.setWrapStyleWord(true);
        JScrollPane scrollAddress = new JScrollPane(addressArea);
        gbc.gridx = 1; gbc.gridy = 3; gbc.gridwidth = 3; gbc.weightx = 0.85; 
        formPanel.add(scrollAddress, gbc);

        // Baris 5 - JTextArea padat
        gbc.gridx = 0; gbc.gridy = 4; gbc.gridwidth = 1; gbc.weightx = 0.15;
        JLabel lblMed = new JLabel("Rekod Perubatan:"); lblMed.setFont(labelFont);
        formPanel.add(lblMed, gbc);
        medicalRecordArea = new JTextArea(2, 20); medicalRecordArea.setFont(fieldFont);
        medicalRecordArea.setLineWrap(true); medicalRecordArea.setWrapStyleWord(true);
        JScrollPane scrollMed = new JScrollPane(medicalRecordArea);
        gbc.gridx = 1; gbc.gridy = 4; gbc.gridwidth = 3; gbc.weightx = 0.85; 
        formPanel.add(scrollMed, gbc);

        // Butang Form Action
        JPanel formActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        formActionPanel.setBackground(Color.WHITE);
        
        btnKosongkan = new JButton("Batal / Kosongkan");
        btnKosongkan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKosongkan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSimpan = new JButton("Simpan Rekod Baru");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpan.setBackground(new Color(40, 167, 69)); 
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formActionPanel.add(btnKosongkan);
        formActionPanel.add(btnSimpan);

        gbc.gridx = 0; gbc.gridy = 5; gbc.gridwidth = 4; gbc.insets = new Insets(10, 0, 0, 0); // Gap atas butang diminimumkan
        formPanel.add(formActionPanel, gbc);

        // Wrap Form supaya duduk diam di atas
        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(new Color(245, 247, 250));
        topWrapper.add(formPanel, BorderLayout.NORTH);

        // ==========================================
        // 3. Bahagian Carian (Search Bar)
        // ==========================================
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5)); // Gap dioptimumkan
        searchPanel.setBackground(new Color(245, 247, 250));
        
        JLabel lblSearch = new JLabel("🔍 Carian Nama Pesakit:");
        lblSearch.setFont(new Font("Segoe UI Emoji", Font.BOLD, 13));
        
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        applyPlaceholder(searchField, "Taipkan nama untuk mula carian pantas...");
        
        searchPanel.add(lblSearch);
        searchPanel.add(searchField);

        // ==========================================
        // 4. Bahagian Jadual & Butang Tindakan
        // ==========================================
        JPanel tableContainer = new JPanel(new BorderLayout(5, 5));
        tableContainer.setBackground(new Color(245, 247, 250));
        tableContainer.add(searchPanel, BorderLayout.NORTH); 

        String[] columns = {"ID", "Nama Penuh", "E-mel", "No. Telefon", "Tarikh Lahir", "Alamat", "Rekod Perubatan"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        
        table.setRowHeight(28); // Tinggi row dikurangkan sikit untuk nampak lebih banyak rekod
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setIntercellSpacing(new Dimension(10, 5));
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(230, 235, 240));
        header.setPreferredSize(new Dimension(header.getWidth(), 35));

        // Menyembunyikan Kolom ID
        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
        table.getColumnModel().getColumn(0).setPreferredWidth(150);
        table.getColumnModel().getColumn(1).setPreferredWidth(150);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(new Color(245, 247, 250));
        
        btnEdit = new JButton("✎ Kemaskini Pilihan");
        btnEdit.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnEdit.setBackground(new Color(0, 123, 255));
        btnEdit.setForeground(Color.WHITE);
        
        btnDelete = new JButton("🗑 Padam Rekod");
        btnDelete.setFont(new Font("Segoe UI Emoji", Font.BOLD, 12));
        btnDelete.setBackground(new Color(220, 53, 69)); 
        btnDelete.setForeground(Color.WHITE);
        
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);
        tableContainer.add(actionPanel, BorderLayout.SOUTH);

        // ==========================================
        // PENYELESAIAN ISU RUANG: SPLIT PANE ADJUSTMENT
        // ==========================================
        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topWrapper, tableContainer);
        splitPane.setResizeWeight(0.2); // Form ambil ruang minimum yang boleh
        splitPane.setOneTouchExpandable(true); 
        splitPane.setDividerSize(10); 
        splitPane.setBorder(null);
        splitPane.setBackground(new Color(245, 247, 250));
        
        // TETAPAN KUNCI: Paksa form supaya kecil (300px sahaja berbanding 370px sebelum ni)
        splitPane.setDividerLocation(300); 

        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);

        loadDataFromDatabase();

        // ==========================================
        // 5. Interaktiviti (Tindakan Butang & Carian)
        // ==========================================
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { search(); }
            @Override public void removeUpdate(DocumentEvent e) { search(); }
            @Override public void changedUpdate(DocumentEvent e) { search(); }
            
            private void search() {
                String text = searchField.getText();
                if (text.trim().length() == 0) {
                    rowSorter.setRowFilter(null); 
                } else {
                    rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1));
                }
            }
        });

        btnKosongkan.addActionListener(e -> resetFormState());

        btnSimpan.addActionListener(e -> {
            try {
                String nama = namaField.getText().trim();
                String email = emailField.getText().trim();
                String password = new String(passwordField.getPassword()).trim();
                String dobStr = dobField.getText().trim();
                String phone = phoneField.getText().trim();
                String address = addressArea.getText().trim();
                String medicalRecord = medicalRecordArea.getText().trim();

                if(nama.isEmpty() || email.isEmpty() || dobStr.isEmpty() || phone.isEmpty()) {
                    throw new Exception("Sila isi semua ruangan wajib (Nama, E-mel, DOB, No Telefon)!");
                }

                LocalDate dob;
                try {
                    dob = LocalDate.parse(dobStr, DateTimeFormatter.ofPattern("yyyy-MM-dd"));
                } catch (DateTimeParseException dtpe) {
                    throw new Exception("Format Tarikh Lahir salah! Guna format: YYYY-MM-DD.");
                }

                if (editingRow == -1) {
                    if (password.isEmpty()) throw new Exception("Kata laluan wajib diisi untuk pesakit baru!");
                    String passwordHash = generateSHA256Hash(password);
                    Patient newPatient = new Patient(0, nama, email, dob, phone, address, medicalRecord);
                    
                    clinicController.addPatient(newPatient, passwordHash);
                    JOptionPane.showMessageDialog(this, "Pesakit baharu berjaya didaftarkan.", "Berjaya", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    Patient updatedPatient = new Patient(editingPatientId, nama, email, dob, phone, address, medicalRecord);
                    clinicController.updatePatient(updatedPatient);
                    JOptionPane.showMessageDialog(this, "Rekod pesakit berjaya dikemaskini.", "Berjaya", JOptionPane.INFORMATION_MESSAGE);
                }
                
                loadDataFromDatabase();
                resetFormState();
                
            } catch (SQLException ex) {
                JOptionPane.showMessageDialog(this, "Ralat Pangkalan Data: " + ex.getMessage(), "Ralat DB", JOptionPane.ERROR_MESSAGE);
            } catch (Exception ex) {
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Ralat Input", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnEdit.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                editingRow = modelRow;
                editingPatientId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
                
                namaField.setText(tableModel.getValueAt(modelRow, 1).toString());
                emailField.setText(tableModel.getValueAt(modelRow, 2).toString());
                phoneField.setText(tableModel.getValueAt(modelRow, 3).toString());
                dobField.setText(tableModel.getValueAt(modelRow, 4).toString());
                addressArea.setText(tableModel.getValueAt(modelRow, 5).toString());
                medicalRecordArea.setText(tableModel.getValueAt(modelRow, 6).toString());
                
                passwordField.setText(""); 
                btnSimpan.setText("✔ Sahkan Kemaskini");
                btnSimpan.setBackground(new Color(255, 152, 0)); 
                namaField.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Sila pilih satu rekod dari jadual di bawah.", "Tiada Pilihan", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                int idToPadam = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
                String namaPesakit = tableModel.getValueAt(modelRow, 1).toString();
                
                int confirm = JOptionPane.showConfirmDialog(this, "Adakah anda pasti mahu memadam rekod untuk pesakit: " + namaPesakit + "?", "Pengesahan Padam", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        clinicController.deletePatient(idToPadam);
                        JOptionPane.showMessageDialog(this, "Rekod berjaya dipadam.", "Berjaya", JOptionPane.INFORMATION_MESSAGE);
                        loadDataFromDatabase();
                        resetFormState();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal memadam: " + ex.getMessage(), "Ralat DB", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sila pilih satu rekod dari jadual untuk dipadam.", "Tiada Pilihan", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void resetFormState() {
        namaField.setText(""); emailField.setText(""); passwordField.setText("");
        dobField.setText(""); phoneField.setText(""); addressArea.setText("");
        medicalRecordArea.setText(""); 
        
        btnSimpan.setText("Simpan Rekod Baru");
        btnSimpan.setBackground(new Color(40, 167, 69));
        editingRow = -1;
        editingPatientId = -1;
        table.clearSelection();
        namaField.requestFocus();
    }

    private void loadDataFromDatabase() {
        tableModel.setRowCount(0); 
        try {
            List<Patient> patients = clinicController.getPatientList();
            for (Patient p : patients) {
                tableModel.addRow(new Object[]{
                    p.getId(), p.getFullName(), p.getEmail(), p.getPhone(),
                    p.getDateOfBirth() != null ? p.getDateOfBirth().toString() : "",
                    p.getAddress(), p.getMedicalRecord()
                });
            }
        } catch (Exception ex) {
            System.out.println("Gagal muat data: " + ex.getMessage());
        }
    }

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
            throw new RuntimeException(ex);
        }
    }

    private void applyPlaceholder(JTextField field, String text) {
        try {
            field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, text);
        } catch (Exception e) {
            // Abaikan jika FlatLaf tidak digunakan
        }
    }
}