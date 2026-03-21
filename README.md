# MotorPH Automatic Payroll System v3.0
**Course Project: IT101 - Computer Programming 1**

A procedural Java-based payroll system designed to manage employee information and attendance records for MotorPH. This system demonstrates file handling, procedural logic, and complex payroll computations strictly following corporate business rules.

---

## 📌 Project Contributor

### Princess Jade R. Magahis – Lead Developer & Data Architect
* **System Logic:** Developed procedural algorithms for net pay, gross pay, and statutory deductions.
* **Attendance Engine:** Implemented strict 8:00 AM–5:00 PM work-hour logic with a 5-minute grace period.
* **Data Architecture:** Structured flat-file processing for employee records and 7 months of attendance data.

---

## 🛠️ Technical Stack
* **Language:** Java (Procedural approach, No OOP)
* **Data Storage:** Flat-file Database (CSV)
* **IDE:** Apache NetBeans 
* **Version Control:** Git & GitHub

---

## 💻 Business Rules & Logic
The system follows specific calculation guidelines for accuracy:
- **Work Hours:** Only hours between **08:00 and 17:00** are compensable.
- **Grace Period:** Logins between 08:01 and 08:05 are treated as **08:00** (no late deduction).
- **Lunch Break:** An automatic **1-hour deduction** is applied to daily totals.
- **Statutory Deductions:** PhilHealth, Pag-IBIG (LOVE), and Withholding Tax are calculated based on the combined monthly gross salary and deducted during the **second cutoff** of each month.

---

## 🚀 Program Features
- **Dual-Mode Access:** - **Employee Mode:** View personal profile and basic details.
  - **Payroll Staff Mode:** Process detailed payroll for one or all employees.
- **Timeframe:** Automated processing for the **June to December** period.
- **Data Integrity:** Direct CSV parsing using regex to handle complex fields containing commas.
- **Cutoff Management:** Automated bi-monthly splitting (1–15 and 16–end of month).

---

## 📂 Program Files
- `src/motorph/PayrollSystem.java` – The single-file source code containing all logic.
- `Employee Details.csv` – Master list containing IDs, hourly rates, and personal info.
- `Attendance Record.csv` – Raw logs used for hours-worked computations.

---

## ▶️ How to Run the Program

### 1. Setup Environment
Clone the repository and ensure you have **JDK 11** or higher installed.

### 2. File Placement
**CRITICAL:** Ensure `Employee Details.csv` and `Attendance Record.csv` are located in the **Project Root** folder (the same folder containing the `src` folder).

### 3. Execution
1. Open the project in **NetBeans**.
2. Clean and Build the project.
3. Run `PayrollSystem.java`.

### 4. Login Credentials
| User Role | Username | Password |
| :--- | :--- | :--- |
| **Employee** | `employee` | `12345` |
| **Payroll Staff** | `payroll_staff` | `12345` |

---

## 📅 Project Documentation
[📊 View Project Plan & Data Sheets (Google Sheets)](https://docs.google.com/spreadsheets/d/13hAkgWlgDkVVQ-ZN1YtAlJpFTIJS0ifu/edit?usp=sharing&ouid=117006809110779909774&rtpof=true&sd=true)
