/*
 * Click nbfs://nbhost/SystemFileSystem/Templates/Licenses/license-default.txt to change this license
 * Click nbfs://nbhost/SystemFileSystem/Templates/Classes/Class.java to edit this template
 */

package com.clinic.view;

import com.clinic.controller.ClinicController;
import com.clinic.model.Appointment;
import com.clinic.model.Patient;
import com.formdev.flatlaf.FlatClientProperties;

import java.awt.*;
import java.sql.SQLException;
import java.sql.Timestamp;
import java.text.SimpleDateFormat;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import javax.swing.*;
import javax.swing.border.TitledBorder;
import javax.swing.event.DocumentEvent;
import javax.swing.event.DocumentListener;
import javax.swing.table.DefaultTableCellRenderer;
import javax.swing.table.DefaultTableModel;
import javax.swing.table.JTableHeader;
import javax.swing.table.TableRowSorter;

public class AppointmentList extends JFrame {

    private JComboBox<ComboItem> patientCombo, doctorCombo;
    private JSpinner dateSpinner, timeSpinner;
    private JTextField searchField;
    private JComboBox<String> statusCombo;
    private JTable table;
    private DefaultTableModel tableModel;
    private TableRowSorter<DefaultTableModel> rowSorter;
    private JButton btnSimpan, btnKosongkan, btnEdit, btnDelete;
    
    private ClinicController clinicController;
    private int editingRow = -1; 
    private int editingAppointmentId = -1;

    public AppointmentList() {
        clinicController = new ClinicController();

        setTitle("Sistem Klinik Pintar - Pengurusan Temujanji");
        setDefaultCloseOperation(JFrame.DISPOSE_ON_CLOSE);
        setSize(1000, 750);
        setLocationRelativeTo(null);
        setMinimumSize(new Dimension(850, 600));

        JPanel mainPanel = new JPanel(new BorderLayout(15, 10));
        mainPanel.setBorder(BorderFactory.createEmptyBorder(10, 20, 15, 20));
        mainPanel.setBackground(new Color(245, 247, 250));

        JLabel titleLabel = new JLabel("Pendaftaran & Pengurusan Temujanji");
        titleLabel.setFont(new Font("Segoe UI", Font.BOLD, 20));
        titleLabel.setForeground(new Color(33, 37, 41));
        mainPanel.add(titleLabel, BorderLayout.NORTH);

        // ==========================================
        // BORANG PENDAFTARAN
        // ==========================================
        JPanel formPanel = new JPanel(new GridBagLayout());
        formPanel.setBackground(Color.WHITE);
        TitledBorder formBorder = BorderFactory.createTitledBorder(
                BorderFactory.createLineBorder(new Color(200, 200, 200), 1, true), "Borang Temujanji");
        formBorder.setTitleFont(new Font("Segoe UI", Font.BOLD, 13));
        formBorder.setTitleColor(new Color(70, 70, 70));
        formPanel.setBorder(BorderFactory.createCompoundBorder(formBorder, BorderFactory.createEmptyBorder(5, 15, 5, 15)));
        
        GridBagConstraints gbc = new GridBagConstraints();
        gbc.insets = new Insets(8, 15, 8, 15);
        gbc.fill = GridBagConstraints.HORIZONTAL;

        Font labelFont = new Font("Segoe UI", Font.PLAIN, 13);
        Font fieldFont = new Font("Segoe UI", Font.PLAIN, 13);

        // Baris 1: Nama Pesakit & Nama Doktor
        gbc.gridx = 0; gbc.gridy = 0; gbc.weightx = 0.15;
        JLabel lblPid = new JLabel("Nama Pesakit:"); lblPid.setFont(labelFont);
        formPanel.add(lblPid, gbc);
        patientCombo = new JComboBox<>(); patientCombo.setFont(fieldFont);
        gbc.gridx = 1; gbc.gridy = 0; gbc.weightx = 0.35;
        formPanel.add(patientCombo, gbc);

        gbc.gridx = 2; gbc.gridy = 0; gbc.weightx = 0.15;
        JLabel lblDid = new JLabel("Doktor Bertugas:"); lblDid.setFont(labelFont);
        formPanel.add(lblDid, gbc);
        doctorCombo = new JComboBox<>(); doctorCombo.setFont(fieldFont);
        gbc.gridx = 3; gbc.gridy = 0; gbc.weightx = 0.35;
        formPanel.add(doctorCombo, gbc);

        // Baris 2: Pemilih Tarikh & Masa
        gbc.gridx = 0; gbc.gridy = 1; gbc.weightx = 0.15;
        JLabel lblTime = new JLabel("Tarikh & Masa:"); lblTime.setFont(labelFont);
        formPanel.add(lblTime, gbc);

        JPanel dateTimePanel = new JPanel(new GridLayout(1, 2, 5, 0));
        dateTimePanel.setBackground(Color.WHITE);
        
        SpinnerDateModel dateModel = new SpinnerDateModel(new Date(), null, null, Calendar.DAY_OF_MONTH);
        dateSpinner = new JSpinner(dateModel);
        JSpinner.DateEditor dateEditor = new JSpinner.DateEditor(dateSpinner, "yyyy-MM-dd");
        dateSpinner.setEditor(dateEditor);
        dateSpinner.setFont(fieldFont);
        
        SpinnerDateModel timeModel = new SpinnerDateModel(new Date(), null, null, Calendar.MINUTE);
        timeSpinner = new JSpinner(timeModel);
        JSpinner.DateEditor timeEditor = new JSpinner.DateEditor(timeSpinner, "HH:mm:ss");
        timeSpinner.setEditor(timeEditor);
        timeSpinner.setFont(fieldFont);
        
        dateTimePanel.add(dateSpinner);
        dateTimePanel.add(timeSpinner);
        
        gbc.gridx = 1; gbc.gridy = 1; gbc.weightx = 0.35;
        formPanel.add(dateTimePanel, gbc);

        // Baris 2 Kanan: Status
        gbc.gridx = 2; gbc.gridy = 1; gbc.weightx = 0.15;
        JLabel lblStatus = new JLabel("Status:"); lblStatus.setFont(labelFont);
        formPanel.add(lblStatus, gbc);
        String[] statusOptions = {"PENDING", "CONFIRMED", "COMPLETED", "CANCELLED"};
        statusCombo = new JComboBox<>(statusOptions); statusCombo.setFont(fieldFont);
        gbc.gridx = 3; gbc.gridy = 1; gbc.weightx = 0.35;
        formPanel.add(statusCombo, gbc);

        // Butang Borang
        JPanel formActionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        formActionPanel.setBackground(Color.WHITE);
        
        btnKosongkan = new JButton("Batal / Kosongkan");
        btnKosongkan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnKosongkan.setCursor(new Cursor(Cursor.HAND_CURSOR));
        
        btnSimpan = new JButton("Simpan Temujanji");
        btnSimpan.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnSimpan.setBackground(new Color(40, 167, 69)); 
        btnSimpan.setForeground(Color.WHITE);
        btnSimpan.setCursor(new Cursor(Cursor.HAND_CURSOR));

        formActionPanel.add(btnKosongkan);
        formActionPanel.add(btnSimpan);

        gbc.gridx = 0; gbc.gridy = 2; gbc.gridwidth = 4; gbc.insets = new Insets(10, 0, 0, 0);
        formPanel.add(formActionPanel, gbc);

        JPanel topWrapper = new JPanel(new BorderLayout());
        topWrapper.setBackground(new Color(245, 247, 250));
        topWrapper.add(formPanel, BorderLayout.NORTH);

        // ==========================================
        // BAR CARIAN & JADUAL
        // ==========================================
        JPanel searchPanel = new JPanel(new FlowLayout(FlowLayout.LEFT, 10, 5));
        searchPanel.setBackground(new Color(245, 247, 250));
        JLabel lblSearch = new JLabel("Carian Temujanji:");
        lblSearch.setFont(new Font("Segoe UI", Font.BOLD, 13));
        searchField = new JTextField(30);
        searchField.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        applyPlaceholder(searchField, "Cari nama pesakit atau doktor...");
        searchPanel.add(lblSearch); searchPanel.add(searchField);

        JPanel tableContainer = new JPanel(new BorderLayout(5, 5));
        tableContainer.setBackground(new Color(245, 247, 250));
        tableContainer.add(searchPanel, BorderLayout.NORTH); 

        String[] columns = {"ID", "Nama Pesakit", "Nama Doktor", "Tarikh & Masa", "Status"};
        tableModel = new DefaultTableModel(columns, 0) {
            @Override public boolean isCellEditable(int row, int column) { return false; }
        };
        table = new JTable(tableModel);
        rowSorter = new TableRowSorter<>(tableModel);
        table.setRowSorter(rowSorter);
        
        table.setRowHeight(32); 
        table.setFont(new Font("Segoe UI", Font.PLAIN, 13));
        table.setSelectionMode(ListSelectionModel.SINGLE_SELECTION);
        table.setAutoResizeMode(JTable.AUTO_RESIZE_ALL_COLUMNS); 
        
        JTableHeader header = table.getTableHeader();
        header.setFont(new Font("Segoe UI", Font.BOLD, 13));
        header.setBackground(new Color(230, 235, 240));
        header.setPreferredSize(new Dimension(header.getWidth(), 38));
        
        ((DefaultTableCellRenderer) header.getDefaultRenderer()).setHorizontalAlignment(JLabel.CENTER);

        table.getColumnModel().removeColumn(table.getColumnModel().getColumn(0));
        
        // --- PENYELESAIAN ISU ALIGNMENT: Pusatkan (Center) semua data ---
        DefaultTableCellRenderer centerRenderer = new DefaultTableCellRenderer();
        centerRenderer.setHorizontalAlignment(JLabel.CENTER);
        
        table.getColumnModel().getColumn(0).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(0).setPreferredWidth(220); 
        
        table.getColumnModel().getColumn(1).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(1).setPreferredWidth(200); 
        
        table.getColumnModel().getColumn(2).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(2).setPreferredWidth(180); 
        
        table.getColumnModel().getColumn(3).setCellRenderer(centerRenderer); 
        table.getColumnModel().getColumn(3).setPreferredWidth(120);

        JScrollPane scrollPane = new JScrollPane(table);
        scrollPane.setBorder(BorderFactory.createLineBorder(new Color(220, 220, 220)));
        tableContainer.add(scrollPane, BorderLayout.CENTER);

        JPanel actionPanel = new JPanel(new FlowLayout(FlowLayout.RIGHT, 15, 0));
        actionPanel.setBackground(new Color(245, 247, 250));
        
        btnEdit = new JButton("✎ Kemaskini Pilihan");
        btnEdit.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnEdit.setBackground(new Color(0, 123, 255));
        btnEdit.setForeground(Color.WHITE);
        
        btnDelete = new JButton("🗑 Padam Rekod");
        btnDelete.setFont(new Font("Segoe UI", Font.BOLD, 12));
        btnDelete.setBackground(new Color(220, 53, 69)); 
        btnDelete.setForeground(Color.WHITE);
        
        actionPanel.add(btnEdit); actionPanel.add(btnDelete);
        tableContainer.add(actionPanel, BorderLayout.SOUTH);

        JSplitPane splitPane = new JSplitPane(JSplitPane.VERTICAL_SPLIT, topWrapper, tableContainer);
        splitPane.setResizeWeight(0.2); 
        splitPane.setOneTouchExpandable(true); 
        splitPane.setDividerSize(10); 
        splitPane.setBorder(null);
        splitPane.setDividerLocation(230); 
        mainPanel.add(splitPane, BorderLayout.CENTER);
        add(mainPanel);

        loadDropdownData();
        loadAppointmentsFromDatabase();

        // ==========================================
        // TINDAKAN BUTANG & INTERAKTIVITI
        // ==========================================
        searchField.getDocument().addDocumentListener(new DocumentListener() {
            @Override public void insertUpdate(DocumentEvent e) { search(); }
            @Override public void removeUpdate(DocumentEvent e) { search(); }
            @Override public void changedUpdate(DocumentEvent e) { search(); }
            private void search() {
                String text = searchField.getText();
                if (text.trim().length() == 0) rowSorter.setRowFilter(null); 
                else rowSorter.setRowFilter(RowFilter.regexFilter("(?i)" + text, 1, 2));
            }
        });

        btnKosongkan.addActionListener(e -> resetFormState());

        btnSimpan.addActionListener(e -> {
            try {
                Date selectedDate = (Date) dateSpinner.getValue();
                Date selectedTime = (Date) timeSpinner.getValue();
                
                Calendar calDate = Calendar.getInstance();
                calDate.setTime(selectedDate);
                Calendar calTime = Calendar.getInstance();
                calTime.setTime(selectedTime);
                
                calDate.set(Calendar.HOUR_OF_DAY, calTime.get(Calendar.HOUR_OF_DAY));
                calDate.set(Calendar.MINUTE, calTime.get(Calendar.MINUTE));
                calDate.set(Calendar.SECOND, calTime.get(Calendar.SECOND));
                
                Timestamp apptTime = new Timestamp(calDate.getTimeInMillis());
                String status = statusCombo.getSelectedItem().toString();

                if (editingRow == -1) {
                    if (patientCombo.getSelectedItem() == null || doctorCombo.getSelectedItem() == null) {
                        throw new Exception("Sila pastikan senarai Pesakit dan Doktor tidak kosong.");
                    }
                    
                    ComboItem selectedPatient = (ComboItem) patientCombo.getSelectedItem();
                    ComboItem selectedDoctor = (ComboItem) doctorCombo.getSelectedItem();
                    
                    int pId = Integer.parseInt(selectedPatient.getKey());
                    int dId = Integer.parseInt(selectedDoctor.getKey());
                    
                    clinicController.addAppointment(pId, dId, apptTime, status);
                    JOptionPane.showMessageDialog(this, "Temujanji baharu berjaya didaftarkan.", "Berjaya", JOptionPane.INFORMATION_MESSAGE);
                } else {
                    clinicController.updateAppointmentDetails(editingAppointmentId, apptTime, status);
                    JOptionPane.showMessageDialog(this, "Maklumat temujanji berjaya dikemaskini.", "Berjaya", JOptionPane.INFORMATION_MESSAGE);
                }
                
                loadAppointmentsFromDatabase();
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
                editingAppointmentId = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
                
                String patientName = tableModel.getValueAt(modelRow, 1).toString();
                String doctorName = tableModel.getValueAt(modelRow, 2).toString();
                
                for (int i = 0; i < patientCombo.getItemCount(); i++) {
                    if (patientCombo.getItemAt(i).getValue().equals(patientName)) {
                        patientCombo.setSelectedIndex(i); break;
                    }
                }
                for (int i = 0; i < doctorCombo.getItemCount(); i++) {
                    if (doctorCombo.getItemAt(i).getValue().equals(doctorName)) {
                        doctorCombo.setSelectedIndex(i); break;
                    }
                }
                
                try {
                    String timeStr = tableModel.getValueAt(modelRow, 3).toString();
                    SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss");
                    Date dbDate = sdf.parse(timeStr);
                    dateSpinner.setValue(dbDate);
                    timeSpinner.setValue(dbDate);
                } catch (Exception ex) {
                    System.out.println("Ralat format tarikh untuk edit.");
                }

                statusCombo.setSelectedItem(tableModel.getValueAt(modelRow, 4).toString());
                
                patientCombo.setEnabled(false);
                doctorCombo.setEnabled(false);
                
                btnSimpan.setText("✔ Sahkan Kemaskini");
                btnSimpan.setBackground(new Color(255, 152, 0)); 
            } else {
                JOptionPane.showMessageDialog(this, "Sila pilih satu rekod temujanji dari jadual di bawah.", "Tiada Pilihan", JOptionPane.WARNING_MESSAGE);
            }
        });

        btnDelete.addActionListener(e -> {
            int viewRow = table.getSelectedRow();
            if (viewRow >= 0) {
                int modelRow = table.convertRowIndexToModel(viewRow);
                int idToPadam = Integer.parseInt(tableModel.getValueAt(modelRow, 0).toString());
                String namaPesakit = tableModel.getValueAt(modelRow, 1).toString();
                
                int confirm = JOptionPane.showConfirmDialog(this, "Padam temujanji untuk pesakit: " + namaPesakit + "?", "Pengesahan", JOptionPane.YES_NO_OPTION, JOptionPane.WARNING_MESSAGE);
                
                if (confirm == JOptionPane.YES_OPTION) {
                    try {
                        clinicController.deleteAppointment(idToPadam);
                        JOptionPane.showMessageDialog(this, "Temujanji berjaya dipadam.", "Berjaya", JOptionPane.INFORMATION_MESSAGE);
                        loadAppointmentsFromDatabase();
                        resetFormState();
                    } catch (SQLException ex) {
                        JOptionPane.showMessageDialog(this, "Gagal memadam: " + ex.getMessage(), "Ralat DB", JOptionPane.ERROR_MESSAGE);
                    }
                }
            } else {
                JOptionPane.showMessageDialog(this, "Sila pilih satu rekod untuk dipadam.", "Tiada Pilihan", JOptionPane.WARNING_MESSAGE);
            }
        });
    }

    private void resetFormState() {
        patientCombo.setEnabled(true);
        doctorCombo.setEnabled(true);
        if(patientCombo.getItemCount() > 0) patientCombo.setSelectedIndex(0);
        if(doctorCombo.getItemCount() > 0) doctorCombo.setSelectedIndex(0);
        
        dateSpinner.setValue(new Date());
        timeSpinner.setValue(new Date());
        
        statusCombo.setSelectedIndex(0);
        
        btnSimpan.setText("Simpan Temujanji");
        btnSimpan.setBackground(new Color(40, 167, 69));
        editingRow = -1;
        editingAppointmentId = -1;
        table.clearSelection();
    }

    private void loadDropdownData() {
        try {
            patientCombo.removeAllItems();
            List<String[]> patients = clinicController.getActivePatients();
            for (String[] p : patients) {
                patientCombo.addItem(new ComboItem(p[0], p[1]));
            }
            
            doctorCombo.removeAllItems();
            List<String[]> doctors = clinicController.getActiveDoctors();
            for (String[] doc : doctors) {
                doctorCombo.addItem(new ComboItem(doc[0], doc[1]));
            }
        } catch (SQLException ex) {
            System.out.println("Gagal memuatkan data pilihan: " + ex.getMessage());
        }
    }

    private void loadAppointmentsFromDatabase() {
        tableModel.setRowCount(0); 
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd HH:mm:ss"); 
        
        try {
            List<Appointment> appointments = clinicController.getAppointmentList();
            for (Appointment appt : appointments) {
                String timeStr = (appt.getAppointmentTime() != null) ? sdf.format(appt.getAppointmentTime()) : "";
                tableModel.addRow(new Object[]{
                    appt.getAppointmentId(),
                    appt.getPatientName(),
                    appt.getDoctorName(),
                    timeStr,
                    appt.getStatus()
                });
            }
        } catch (SQLException ex) {
            System.out.println("Gagal tarik data temujanji: " + ex.getMessage());
        }
    }

    private void applyPlaceholder(JTextField field, String text) {
        try { field.putClientProperty(FlatClientProperties.PLACEHOLDER_TEXT, text); } 
        catch (Exception e) { }
    }

    class ComboItem {
        private String key;
        private String value;

        public ComboItem(String key, String value) {
            this.key = key; this.value = value;
        }
        @Override public String toString() { return value; }
        public String getKey() { return key; }
        public String getValue() { return value; }
    }
}