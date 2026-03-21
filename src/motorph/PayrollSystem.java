package motorph;

import java.io.*;
import java.util.Scanner;

public class PayrollSystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);

        // 1. Initial Login
        System.out.println("=====================================");
        System.out.println("      MOTORPH PAYROLL SYSTEM        ");
        System.out.println("=====================================");
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!password.equals("12345") || (!username.equals("employee") && !username.equals("payroll_staff"))) {
            System.out.println("Incorrect username and/or password.");
            return; // Terminate
        }

        if (username.equals("employee")) {
            handleEmployeeMode(sc);
        } else {
            handleStaffMode(sc);
        }
    }

    // --- MODE: EMPLOYEE ---
    public static void handleEmployeeMode(Scanner sc) {
        while (true) {
            System.out.println("\n1. Enter your employee number");
            System.out.println("2. Exit the program");
            System.out.print("Selection: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.print("Employee #: ");
                String empNum = sc.nextLine();
                String[] details = findEmployee(empNum);
                
                if (details == null) {
                    System.out.println("Employee number does not exist.");
                } else {
                    System.out.println("\n--- EMPLOYEE DETAILS ---");
                    System.out.println("Employee Number: " + details[0]);
                    System.out.println("Employee Name: " + details[2] + " " + details[1]);
                    System.out.println("Birthday: " + details[3]);
                }
            } else if (choice.equals("2")) {
                System.out.println("Terminating program...");
                break;
            }
        }
    }

    // --- MODE: PAYROLL STAFF ---
    public static void handleStaffMode(Scanner sc) {
        while (true) {
            System.out.println("\n1. Process Payroll");
            System.out.println("2. Exit the program");
            System.out.print("Selection: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                System.out.println("\n1. One employee");
                System.out.println("2. All employees");
                System.out.println("3. Exit the program");
                System.out.print("Selection: ");
                String subChoice = sc.nextLine();

                if (subChoice.equals("1")) {
                    System.out.print("Enter the employee number: ");
                    String empNum = sc.nextLine();
                    if (findEmployee(empNum) == null) {
                        System.out.println("Employee number does not exist.");
                    } else {
                        processPayrollForEmployee(empNum);
                    }
                } else if (subChoice.equals("2")) {
                    processAllEmployees();
                } else if (subChoice.equals("3")) {
                    break;
                }
            } else if (choice.equals("2")) {
                System.out.println("Terminating program...");
                break;
            }
        }
    }

    // --- HELPER: FIND EMPLOYEE DATA ---
    public static String[] findEmployee(String id) {
        try (BufferedReader br = new BufferedReader(new FileReader("Employee Details.csv"))) {
            String line;
            br.readLine(); // Skip header
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data[0].trim().equals(id)) return data;
            }
        } catch (IOException e) {
            System.out.println("Error reading Employee Details file.");
        }
        return null;
    }

    // --- LOGIC: PROCESS INDIVIDUAL PAYROLL ---
    public static void processPayrollForEmployee(String empNum) {
        String[] emp = findEmployee(empNum);
        if (emp == null) return;

        double hourlyRate = Double.parseDouble(emp[18].trim());

        System.out.println("\n=============================================");
        System.out.println("Employee #: " + emp[0]);
        System.out.println("Employee Name: " + emp[2] + " " + emp[1]);
        System.out.println("Birthday: " + emp[3]);
        System.out.println("=============================================");

        // Months June (6) to December (12)
        for (int month = 6; month <= 12; month++) {
            String mStr = (month < 10) ? "0" + month : "" + month;

            // Cutoff 1: 1st to 15th
            double hours1 = getHoursWorked(empNum, month, 1, 15);
            double gross1 = hours1 * hourlyRate;

            // Cutoff 2: 16th to end of month
            double hours2 = getHoursWorked(empNum, month, 16, 31);
            double gross2 = hours2 * hourlyRate;

            // Display Cutoff 1
            System.out.println("Cutoff Date: " + getMonthName(month) + " 1 to " + getMonthName(month) + " 15");
            System.out.println("Total Hours Worked: " + hours1);
            System.out.println("Gross Salary: " + gross1);
            System.out.println("Net Salary: " + gross1);
            System.out.println("---------------------------------------------");

            // Compute Deductions based on monthly total
            double totalMonthlyGross = gross1 + gross2;
            double philhealth = totalMonthlyGross * 0.03; // Placeholder 3%
            double love = 100.0; // Pag-IBIG
            double tax = totalMonthlyGross * 0.20; // Placeholder 20%
            double totalDeductions = philhealth + love + tax;

            // Display Cutoff 2
            System.out.println("Cutoff Date: " + getMonthName(month) + " 16 to " + getMonthName(month) + " End");
            System.out.println("Total Hours Worked: " + hours2);
            System.out.println("Gross Salary: " + gross2);
            System.out.println("Each Deduction:");
            System.out.println("  PhilHealth: " + philhealth);
            System.out.println("  LOVE: " + love);
            System.out.println("  Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeductions);
            System.out.println("Net Salary: " + (gross2 - totalDeductions));
            System.out.println("=============================================");
        }
    }

    // --- LOGIC: PROCESS ALL EMPLOYEES ---
    public static void processAllEmployees() {
        try (BufferedReader br = new BufferedReader(new FileReader("Employee Details.csv"))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                processPayrollForEmployee(data[0].trim());
            }
        } catch (IOException e) {
            System.out.println("Error processing payroll for all employees.");
        }
    }

    // --- LOGIC: CALCULATE ATTENDANCE HOURS ---
    public static double getHoursWorked(String id, int month, int startDay, int endDay) {
        double totalHours = 0;
        try (BufferedReader br = new BufferedReader(new FileReader("Attendance Record.csv"))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",");
                String csvId = data[0].trim();
                String date = data[3].trim(); // MM/DD/YYYY
                
                String[] dateParts = date.split("/");
                int csvMonth = Integer.parseInt(dateParts[0]);
                int csvDay = Integer.parseInt(dateParts[1]);

                if (csvId.equals(id) && csvMonth == month && csvDay >= startDay && csvDay <= endDay) {
                    String logIn = data[4].trim();
                    String logOut = data[5].trim();
                    totalHours += calculateDailyHours(logIn, logOut);
                }
            }
        } catch (IOException e) { }
        return totalHours;
    }

    public static double calculateDailyHours(String logIn, String logOut) {
        String[] inParts = logIn.split(":");
        String[] outParts = logOut.split(":");

        int hIn = Integer.parseInt(inParts[0]);
        int mIn = Integer.parseInt(inParts[1]);
        int hOut = Integer.parseInt(outParts[0]);
        int mOut = Integer.parseInt(outParts[1]);

        // Grace Period Logic: 8:05 treated as 8:00
        if (hIn == 8 && mIn <= 5) mIn = 0;
        // Constraint Logic: 8:00 AM to 5:00 PM
        if (hIn < 8) { hIn = 8; mIn = 0; }
        if (hOut >= 17) { hOut = 17; mOut = 0; }

        double start = hIn + (mIn / 60.0);
        double end = hOut + (mOut / 60.0);

        double duration = end - start;
        // Subtract 1 hour for lunch if working a full shift
        return (duration > 0) ? duration - 1.0 : 0;
    }

    public static String getMonthName(int m) {
        String[] months = {"", "", "", "", "", "", "June", "July", "August", "September", "October", "November", "December"};
        return months[m];
    }
}
