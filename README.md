# Clinic_System

Struktur minimum backend untuk **Sistem Pengurusan Klinik Pintar**:

- `database/clinic_schema.sql` — skrip MySQL untuk cipta jadual (`users`, `patients`, `doctors`, `appointments`, `bills`) dan masukkan dummy data.
- `src/com/clinic/model` — kelas OOP `Person`, `Patient`, `Doctor`, `Appointment`.
- `src/com/clinic/controller/ClinicController.java` — fungsi backend berasaskan JDBC:
  - `addPatient(...)`
  - `getAppointmentList()` (pulangkan `List<Appointment>`)
  - `updateBilling(...)`
- `src/com/clinic/util/DatabaseConnection.java` — utiliti sambungan MySQL.
  - Perlu tetapkan pembolehubah persekitaran: `CLINIC_DB_URL`, `CLINIC_DB_USER`, `CLINIC_DB_PASSWORD`.

Pakej GUI boleh diletakkan di `com.clinic.view` dan memanggil fungsi dalam `com.clinic.controller`.
