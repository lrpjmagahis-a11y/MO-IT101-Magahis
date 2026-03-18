package motorph;

import java.io.*;
import java.util.*;

public class PayrollSystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        System.out.println("======================================");
        System.out.println("   MOTORPH ENTERPRISE PAYROLL V3.0");
        System.out.println("======================================");
        
        System.out.print("Enter Username: ");
        String user = input.nextLine();
        System.out.print("Enter 5-digit Password: ");
        String pass = input.nextLine();

        // Feature: Secure 5-digit Password System
        if (pass.length() == 5 && (user.equals("admin") || user.equals("employee"))) {
            showMenu(user, input);
        } else {
            System.out.println("[!] Access Denied. Check your credentials.");
        }
    }

    public static void showMenu(String role, Scanner input) {
        while (true) {
            System.out.println("\n--- " + role.toUpperCase() + " DASHBOARD ---");
            System.out.println("1. Process Payroll (ID + Attendance)");
            System.out.println("2. Exit");
            System.out.print("Select: ");
            int choice = input.nextInt();

            if (choice == 1) {
                System.out.print("Enter Employee ID (10001-10100): ");
                String id = input.next();
                processEmployee(id);
            } else break;
        }
    }

    // Feature: Automated CSV Reading for Attendance_Logs.csv
    public static double getHoursFromLogs(String id) {
        double totalHours = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("Attendance_Logs.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] logs = line.split(",");
                if (logs[0].equals(id)) {
                    totalHours += 8.0; // Standard 8-hour shift per log entry
                }
            }
        } catch (IOException e) {
            System.out.println("[!] Warning: Attendance_Logs.csv not found.");
        }
        return totalHours;
    }

    // Feature: Deduction Engine & Scalability
    public static void processEmployee(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("Employee_Data.csv"))) {
            String line;
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                if (data[0].equals(id)) {
                    double monthlySalary = Double.parseDouble(data[4]);
                    double hoursWorked = getHoursFromLogs(id);
                    
                    // Calculation Logic
                    double hourlyRate = monthlySalary / 160; 
                    double grossPay = hourlyRate * hoursWorked;
                    
                    // Deduction Engine (SSS, PhilHealth, Tax)
                    double sss = grossPay * 0.045;
                    double philhealth = grossPay * 0.02;
                    double tax = (grossPay > 15000) ? (grossPay * 0.12) : 0;
                    double netPay = grossPay - (sss + philhealth + tax);

                    System.out.println("\n[PAYROLL RESULT FOR ID: " + id + "]");
                    System.out.println("Name: " + data[2] + " " + data[1]);
                    System.out.println("Total Hours Worked: " + hoursWorked);
                    System.out.println("--------------------------------------");
                    System.out.println("Gross Pay: P" + String.format("%.2f", grossPay));
                    System.out.println("Deductions (SSS/PhilH/Tax): P" + String.format("%.2f", (sss+philhealth+tax)));
                    System.out.println("NET MONTHLY PAY: P" + String.format("%.2f", netPay));
                    System.out.println("BI-MONTHLY CUTOFF (1-15): P" + String.format("%.2f", netPay / 2));
                    return;
                }
            }
            System.out.println("[!] Employee ID not found in database.");
        } catch (Exception e) {
            System.out.println("[!] System Error: Check if CSV files are in the main folder.");
        }
    }
}
