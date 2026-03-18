package motorph;

import java.io.*;
import java.util.*;

public class PayrollSystem {
    static Map<String, String[]> employeeMap = new HashMap<>();
    static String loggedInID = "";

    public static void main(String[] args) {
        loadEmployeeData();
        
        Scanner sc = new Scanner(System.in);
        System.out.println("==============================================");
        System.out.println("        MOTORPH SELF-SERVICE PORTAL v5.0       ");
        System.out.println("==============================================\n");

        System.out.print("👤 Employee ID: ");
        String id = sc.nextLine();
        System.out.print("🔑 PIN: ");
        String pin = sc.nextLine();

        if (authenticate(id, pin)) {
            loggedInID = id;
            System.out.println("\n✅ Login Successful!");
            showDashboard();
        } else {
            System.out.println("\n❌ Access Denied: Incorrect ID or PIN.");
        }
    }

    public static void loadEmployeeData() {
        try (BufferedReader br = new BufferedReader(new FileReader("EmployeeDetails.csv"))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                // Splits by comma, but handles commas inside addresses/quotes
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data.length >= 20) {
                    employeeMap.put(data[0].trim(), data);
                }
            }
            System.out.println("✅ Loaded " + employeeMap.size() + " employees.");
        } catch (Exception e) {
            System.out.println("[!] ERROR: EmployeeDetails.csv not found in project root.");
        }
    }

    public static boolean authenticate(String id, String pin) {
        if (!employeeMap.containsKey(id)) return false;
        String[] emp = employeeMap.get(id);
        return emp[19].trim().equals(pin);
    }

    public static void showDashboard() {
        Scanner sc = new Scanner(System.in);
        String[] emp = employeeMap.get(loggedInID);
        
        while (true) {
            System.out.println("\nWELCOME, " + emp[2].toUpperCase() + " " + emp[1].toUpperCase());
            System.out.println("[1] View Profile  [2] Calculate Payslip  [3] Logout");
            System.out.print("Selection: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.println("\n--- 👤 PROFILE ---");
                System.out.println("ID: " + emp[0]);
                System.out.println("Name: " + emp[2] + " " + emp[1]);
                System.out.println("Position: " + emp[11]);
                System.out.println("Status: " + emp[10]);
                System.out.println("Basic Salary: P " + emp[13]);
            } else if (choice.equals("2")) {
                calculatePayslip(emp);
            } else if (choice.equals("3")) {
                break;
            }
        }
    }

    public static void calculatePayslip(String[] emp) {
        Scanner sc = new Scanner(System.in);
        System.out.print("\nEnter Month (e.g., 03): ");
        String month = sc.nextLine();
        
        double hourlyRate = Double.parseDouble(emp[18].trim());
        double totalHours = 0;
        double totalLateMins = 0;

        try (BufferedReader br = new BufferedReader(new FileReader("AttendanceRecords.csv"))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] att = line.split(",");
                if (att[0].trim().equals(loggedInID) && att[1].trim().startsWith(month)) {
                    String[] in = att[2].trim().split(":");
                    String[] out = att[3].trim().split(":");
                    double hIn = Double.parseDouble(in[0]);
                    double mIn = Double.parseDouble(in[1]);
                    double hOut = Double.parseDouble(out[0]);
                    double mOut = Double.parseDouble(out[1]);

                    if (hIn > 8 || (hIn == 8 && mIn > 0)) {
                        totalLateMins += ((hIn - 8) * 60) + mIn;
                    }

                    double dayHrs = (hOut + mOut/60) - (hIn + mIn/60);
                    totalHours += (dayHrs > 5) ? dayHrs - 1 : dayHrs;
                }
            }

            // Calculation Logic
            double lateDeduction = (hourlyRate / 60) * totalLateMins;
            double grossSalary = (hourlyRate * totalHours) - lateDeduction;
            
            // Deductions (Approximate PH rates)
            double sss = grossSalary * 0.045; 
            double philhealth = grossSalary * 0.02;
            double pagibig = 100.00; // Fixed flat rate
            double tax = (grossSalary > 20000) ? (grossSalary - 20000) * 0.20 : 0;
            
            double totalDeductions = sss + philhealth + pagibig + tax;
            double netPay = grossSalary - totalDeductions;

            System.out.println("\n--- 💵 PAYSLIP FOR MONTH " + month + " ---");
            System.out.println("Total Hours:     " + String.format("%.2f", totalHours));
            System.out.println("Late Minutes:    " + (int)totalLateMins);
            System.out.println("----------------------------------------------");
            System.out.println("GROSS SALARY:    P " + String.format("%.2f", grossSalary));
            System.out.println("SSS Deduction:  -P " + String.format("%.2f", sss));
            System.out.println("PhilHealth:     -P " + String.format("%.2f", philhealth));
            System.out.println("Pag-IBIG:       -P " + String.format("%.2f", pagibig));
            System.out.println("Withholding Tax:-P " + String.format("%.2f", tax));
            System.out.println("----------------------------------------------");
            System.out.println("NET PAY:         P " + String.format("%.2f", netPay));
            System.out.println("==============================================");

        } catch (Exception e) { 
            System.out.println("[!] Error: AttendanceRecords.csv not found."); 
        }
    }
}
