/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */
package com.clinic.view;
import com.formdev.flatlaf.FlatClientProperties;
import java.awt.*;
import java.io.*;
import javax.swing.*;
import javax.swing.table.DefaultTableModel;
/**
 *
 * @author mira
 */
public class Patient_Registration extends JFrame {

    private JTextField namaField, icField, phoneField;
    private JTable table;
    private DefaultTableModel tableModel;
    private JButton btnSimpan;
    
    private final String FILE_NAME = "pesakit.txt";
    private int editingRow = -1; // -1 bermaksud mod "Tambah Baru", > -1 bermaksud mod "Kemaskini"

    public Patient_Registration () {
        // 1. Tetapan Asas Tetingkap
        setTitle("Sistem Klinik Pintar - Pengurusan Pesakit");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(850, 700);
        setLocationRelativeTo(null);

        JPanel mainPanel = new JPanel(new BorderLayout(15, 15));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(20, 20, 20, 20));
        mainPanel.setBackground(Color.WHITE);

        // ==========================================
        // 2. Bahagian Atas: Borang Pendaftaran (Lebih Mesra Pengguna)
        // ==========================================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        formPanel.setBorder(BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200)), "Borang Maklumat Pesakit"));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(10, 10, 10, 10);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        // Label & Field: Nama
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.3;
        formPanel.add(new JLabel("Nama Penuh:"), gbc);
        namaField = new JTextField();
        namaField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contoh: Ali bin Abu");
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.7;
        formPanel.add(namaField, gbc);

        // Label & Field: IC
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.3;
        formPanel.add(new JLabel("No. Kad Pengenalan:"), gbc);
        icField = new JTextField();
        icField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contoh: 901010015566 (Tanpa sempang)");
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.7;
        formPanel.add(icField, gbc);

        // Label & Field: Phone
        gbc.gridx = 0; gbc.gridy = 2; gbc.weightx = 0.3;
        formPanel.add(new JLabel("No. Telefon:"), gbc);
        phoneField = new JTextField();
        phoneField.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, "Contoh: 0123456789");
        gbc.gridx = 1; gbc.gridy = 2; gbc.weightx = 0.7;
        formPanel.add(phoneField, gbc);

        // Butang Simpan / Kemaskini
        btnSimpan = new JButton("Simpan Rekod Baru");
        btnSimpan.setBackground(new Color(0, 153, 51));
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        gbc.gridx = 1; gbc.gridy = 3; gbc.anchor = GridBagConstraints.EAST; gbc.fill = GridBagConstraints.NONE;
        formPanel.add(btnSimpan, gbc);

        // ==========================================
        // 3. Bahagian Tengah: Jadual Paparan Data
        // ==========================================
        String[] columns = {"Nama Penuh", "No. Kad Pengenalan", "No. Telefon"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override
            public boolean isCellEditable(int row, int column) {
                return false; // Jadual tidak boleh diedit secara terus (double click), mesti guna butang
            }
        };
        table = new JTable(tableModel);
        table.setRowHeight(25);
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createTitledBorder("Senarai Rekod Pesakit"));

        // Muat turun data sedia ada
        loadDataFromFile();

        // ==========================================
        // 4. Bahagian Bawah: Butang Tindakan (Edit & Delete)
        // ==========================================
        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(Color.WHITE);
        
        JButton btnEdit = new JButton("Kemaskini Pilihan");
        btnEdit.setBackground(new Color(0, 102, 204));
        btnEdit.setForeground(Color.WHITE);
        
        JButton btnDelete = new JButton("Padam Rekod");
        btnDelete.setBackground(new Color(204, 0, 0));
        btnDelete.setForeground(Color.WHITE);
        
        actionPanel.add(btnEdit);
        actionPanel.add(btnDelete);

        // Cantumkan semua panel
        mainPanel.add(formPanel, BorderLayout.NORTH);
        mainPanel.add(scrollPane, BorderLayout.CENTER);
        mainPanel.add(actionPanel, BorderLayout.SOUTH);
        add(mainPanel);

        // ==========================================
        // 5. Interaktiviti (Tindakan Butang & Validasi)
        // ==========================================

        // TINDAKAN: Butang Simpan / Kemaskini
        btnSimpan.addActionListener(e -> {
            try {
                String nama = namaField.getText().trim();
                String ic = icField.getText().trim();
                String phone = phoneField.getText().trim();

                // a) Validasi Kosong
                if(nama.isEmpty() || ic.isEmpty() || phone.isEmpty()) {
                    throw new Exception("Sila isi semua ruangan (Nama, IC, No Telefon)!");
                }
                
                // b) Validasi Nama (Hanya huruf dan ruang kosong)
                if(!nama.matches("^[a-zA-Z\\s/]+$")) {
                    throw new Exception("Format Nama salah! Nama hanya boleh mengandungi huruf.");
                }
                
                // c) Validasi Kad Pengenalan (Hanya nombor, mesti 12 digit)
                if(!ic.matches("^\\d{12}$")) {
                    throw new Exception("Format IC salah! Sila masukkan 12 digit nombor tanpa sempang (-).");
                }
                
                // d) Validasi No Telefon (Hanya nombor, 10 hingga 11 digit)
                if(!phone.matches("^\\d{10,11}$")) {
                    throw new Exception("Format No. Telefon salah! Pastikan hanya nombor (10-11 digit).");
                }

                if (editingRow == -1) {
                    // MOD: TAMBAH BARU
                    tableModel.addRow(new Object[]{nama, ic, phone});
                    JOptionPane.showMessageDialog(this, "Pesakit baharu berjaya ditambah.");
                } else {
                    // MOD: KEMASKINI
                    tableModel.setValueAt(nama, editingRow, 0);
                    tableModel.setValueAt(ic, editingRow, 1);
                    tableModel.setValueAt(phone, editingRow, 2);
                    JOptionPane.showMessageDialog(this, "Rekod berjaya dikemaskini.");
                    
                    // Reset balik keadaan butang selepas kemaskini
                    btnSimpan.setText("Simpan Rekod Baru");
                    editingRow = -1;
                    table.clearSelection();
                }
                
                // Tulis semula ke fail text untuk memastikan data terkini disimpan
                rewriteFile();
                
                // Kosongkan borang
                clearForm();
                
            } catch (Exception ex) {
                // Menangkap exception dari validasi dan memaparkan kotak amaran
                JOptionPane.showMessageDialog(this, ex.getMessage(), "Ralat Input", JOptionPane.ERROR_MESSAGE);
            }
        });

        // TINDAKAN: Butang Edit
        btnEdit.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                // Tarik data dari jadual dan masukkan ke borang
                namaField.setText(tableModel.getValueAt(selectedRow, 0).toString());
                icField.setText(tableModel.getValueAt(selectedRow, 1).toString());
                phoneField.setText(tableModel.getValueAt(selectedRow, 2).toString());
                
                // Tukar mod kepada kemaskini
                editingRow = selectedRow;
                btnSimpan.setText("Kemaskini Rekod");
                namaField.requestFocus();
            } else {
                JOptionPane.showMessageDialog(this, "Sila pilih satu baris dari jadual untuk dikemaskini.", "Tiada Pilihan", JOptionPane.WARNING_MESSAGE);
            }
        });

        // TINDAKAN: Butang Padam
        btnDelete.addActionListener(e -> {
            int selectedRow = table.getSelectedRow();
            if (selectedRow >= 0) {
                int confirm = JOptionPane.showConfirmDialog(this, "Adakah anda pasti mahu memadam rekod ini?", "Pengesahan Padam", JOptionPane.YES_NO_OPTION);
                if (confirm == JOptionPane.YES_OPTION) {
                    tableModel.removeRow(selectedRow); // Buang dari paparan jadual
                    
                    try {
                        rewriteFile(); // Tulis semula ke fail text tanpa rekod yang dibuang
                        JOptionPane.showMessageDialog(this, "Rekod berjaya dipadam.");
                        clearForm();
                        btnSimpan.setText("Simpan Rekod Baru");
                        editingRow = -1;
                    } catch (IOException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal memadam dari fail: " + ex.getMessage(), "Ralat Fail", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sila pilih satu baris dari jadual untuk dipadam.", "Tiada Pilihan", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    // Fungsi tambahan untuk membersihkan borang input
    private void clearForm() {
        namaField.setText("");
        icField.setText("");
        phoneField.setText("");
        namaField.requestFocus();
    }

    // Membaca data ketika screen dibuka
    private void loadDataFromFile() {
        File file = new File(FILE_NAME);
        if (!file.exists()) return;

        try (BufferedReader br = new BufferedReader(new FileReader(file))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data.length == 3) {
                    tableModel.addRow(new Object[]{data[0], data[1], data[2]});
                }
            }
        } catch (IOException ex) {
            JOptionPane.showMessageDialog(this, "Gagal membaca fail pesakit.", "Ralat Fail", JOptionPane.ERROR_MESSAGE);
        }
    }

    // Menulis semula SELURUH isi jadual ke dalam text file (Sesuai untuk Update & Delete)
    private void rewriteFile() throws IOException {
        try (FileWriter writer = new FileWriter(FILE_NAME, false)) { // 'false' bermaksud ia akan overwrite fail lama
            for (int i = 0; i < tableModel.getRowCount(); i++) {
                String n = tableModel.getValueAt(i, 0).toString();
                String k = tableModel.getValueAt(i, 1).toString();
                String p = tableModel.getValueAt(i, 2).toString();
                writer.write(n + "," + k + "," + p + "\n");
            }
        }
    }
}
