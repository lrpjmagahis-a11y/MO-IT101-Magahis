# MotorPH Automatic Payroll System v3.0
**Course Project: IT101 - Computer Programming 1**

A Java-based automatic payroll system that manages employee information, attendance records, and payroll calculations. This system demonstrates core programming skills, file handling, and payroll computation logic including SSS, PhilHealth, Pag-IBIG, and tax deductions.

---

## 📌 Project Contributor

### Princess Jade R. Magahis – Lead Developer & Data Architect
* **System Logic:** Implemented the primary payroll computation tasks, such as calculating net pay, gross pay, deductions, and employee worked hours. 
* **Data Management:** Processed and structured employee attendance information for 100+ records, including time-in and time-out data used to calculate work hours.
* **UI/UX:** Created the program menu and handled user input validation to ensure seamless interaction with the payroll processing engine.

---

## 🛠️ Technical Stack
* **Programming Language:** Java (JDK 11+)
* **Data Management:** Flat-file Database (CSV - Comma Separated Values)
* **IDE:** Apache NetBeans 
* **Version Control:** Git & GitHub
* **Algorithms:** Conditional logic for tax brackets and loop-based file parsing for scalability.

---

## 💻 Program Details
The **MotorPH Automatic Payroll System** is a Java application that automates payroll processing. The system reads employee information and attendance records from CSV files and calculates employee salaries based on their recorded working hours.

The program determines the total hours worked by each employee within a selected payroll cutoff period (1–15 or 16–31 of the month). Using the employee’s hourly rate, it calculates the gross salary and automatically applies government deductions such as **SSS, PhilHealth, Pag-IBIG, and withholding tax** to produce the final net salary.

---

## 🚀 Program Features
- **Employee Login:** Secure login system with username and 5-digit password.
- **Payroll Admin Access:** Dedicated login for processing and reviewing the entire workforce.
- **Scalability:** Handles 100+ employees (expandable up to 1000).
- **Automated CSV Reading:** Dynamically pulls data from `Employee_Data.csv` and `Attendance_Logs.csv`.
- **Deduction Engine:** Accurate computation for SSS, PhilHealth, Pag-IBIG, and Tax.
- **Cutoff Management:** Handles bi-monthly payroll periods (1–15, 16–31).

---

## 📂 Program Files
- `src/motorph/PayrollSystem.java` – Main program logic and entry point.
- `Employee_Data.csv` – Master list containing IDs, names, and hourly rates.
- `Attendance_Logs.csv` – Contains randomized attendance logs used for calculations.

---

## 📅 Project Documentation & Planning
The project development tasks and payroll logic mapping can be found in the official documentation link below:

[📊 View Project Plan & Data Sheets (Google Sheets)](https://docs.google.com/spreadsheets/d/13hAkgWlgDkVVQ-ZN1YtAlJpFTIJS0ifu/edit?usp=sharing&ouid=117006809110779909774&rtpof=true&sd=true)

---

## ▶️ How to Run the Program

### 1. Open the Project
Clone the repository and open the project using **NetBeans IDE**.

### 2. Verify Project Files
Ensure `Attendance_Logs.csv` and `Employee_Data.csv` are in the project root folder.

### 3. Run the Program
1. Open the project in **NetBeans**.
2. Run `PayrollSystem.java`.

### 4. Login Credentials
- **Employee Account:** Username: `employee` | Password: `12345`
- **Admin/Payroll Account:** Username: `admin` | Password: `12345`
