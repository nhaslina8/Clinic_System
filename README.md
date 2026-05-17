# Clinic_System

Struktur minimum backend untuk **Sistem Pengurusan Klinik Pintar**:

- `database/clinic_schema.sql` — skrip MySQL untuk cipta jadual (`users`, `patients`, `doctors`, `appointments`, `bills`) dan masukkan dummy data.
- `src/com/clinic/model` — kelas OOP `Person`, `Patient`, `Doctor`.
- `src/com/clinic/controller/ClinicController.java` — fungsi backend berasaskan JDBC:
  - `addPatient(...)`
  - `getAppointmentList()`
  - `updateBilling(...)`
- `src/com/clinic/util/DatabaseConnection.java` — utiliti sambungan MySQL.

Pakej GUI boleh diletakkan di `com.clinic.view` dan memanggil fungsi dalam `com.clinic.controller`.
