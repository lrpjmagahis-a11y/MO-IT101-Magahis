package motorph;

import java.io.*;
import java.util.Scanner;

public class PayrollSystem {

    public static void main(String[] args) {
        Scanner sc = new Scanner(System.in);
        System.out.println("=====================================");
        System.out.println("      MOTORPH PAYROLL SYSTEM        ");
        System.out.println("=====================================");
        
        System.out.print("Username: ");
        String username = sc.nextLine();
        System.out.print("Password: ");
        String password = sc.nextLine();

        if (!password.equals("12345") || (!username.equals("employee") && !username.equals("payroll_staff"))) {
            System.out.println("Incorrect username and/or password.");
            return; 
        }

        if (username.equals("employee")) {
            handleEmployeeMode(sc);
        } else if (username.equals("payroll_staff")) {
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
                    System.out.println("\na. Employee Number: " + details[0]);
                    System.out.println("b. Employee Name: " + details[2] + " " + details[1]);
                    System.out.println("c. Birthday: " + details[3]);
                    employeeSubMenu(sc, details);
                }
            } else if (choice.equals("2")) break;
        }
    }

    public static void employeeSubMenu(Scanner sc, String[] details) {
        while (true) {
            System.out.println("\n--- EMPLOYEE OPTIONS ---");
            System.out.println("[1] View Full Profile");
            System.out.println("[2] Generate My Payroll");
            System.out.println("[3] Back");
            System.out.print("Selection: ");
            String choice = sc.nextLine();

            if (choice.equals("1")) {
                displayFullProfile(details);
            } else if (choice.equals("2")) {
                System.out.println("\n[1] Full Year Report (June-Dec)");
                System.out.println("[2] Specific Month/Cutoff");
                System.out.print("Selection: ");
                String payChoice = sc.nextLine();
                if (payChoice.equals("1")) processFullPayroll(details[0]);
                else if (payChoice.equals("2")) processSpecificPayroll(sc, details[0]);
            } else if (choice.equals("3")) break;
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
                String sub = sc.nextLine();

                if (sub.equals("1")) {
                    System.out.print("Enter the employee number: ");
                    String id = sc.nextLine();
                    if (findEmployee(id) != null) {
                        processFullPayroll(id);
                    } else {
                        System.out.println("Employee number does not exist.");
                    }
                } else if (sub.equals("2")) {
                    processAllEmployees();
                } else if (sub.equals("3")) {
                    break;
                }
            } else if (choice.equals("2")) {
                break;
            }
        }
    }

    // --- PAYROLL COMPUTATION ---
    public static void processFullPayroll(String empNum) {
        String[] emp = findEmployee(empNum);
        double rate = Double.parseDouble(emp[18].trim().replace(",", ""));

        System.out.println("\nEmployee #:\t" + emp[0]);
        System.out.println("Employee Name:\t" + emp[2] + " " + emp[1]);
        System.out.println("Birthday:\t" + emp[3]);

        for (int m = 6; m <= 12; m++) {
            double h1 = getMonthlyHours(empNum, m, 1, 15);
            double h2 = getMonthlyHours(empNum, m, 16, 31);
            double g1 = h1 * rate;
            double g2 = h2 * rate;

            // First Cutoff
            System.out.println("\nCutoff Date: " + getMonthName(m) + " 1 to " + getMonthName(m) + " 15");
            System.out.println("Total Hours Worked: " + h1);
            System.out.println("Gross Salary: " + g1);
            System.out.println("Net Salary: " + g1);

            // Second Cutoff (Deductions based on Monthly Total)
            double monthlyGross = g1 + g2;
            double phil = monthlyGross * 0.03;
            double love = 100.0;
            double tax = (monthlyGross - (phil + love)) * 0.15;
            double totalDeduct = phil + love + tax;

            System.out.println("\nCutoff Date: " + getMonthName(m) + " 16 to " + getMonthName(m) + " 30/31");
            System.out.println("Total Hours Worked: " + h2);
            System.out.println("Gross Salary: " + g2);
            System.out.println("Each Deduction:");
            System.out.println("PhilHealth: " + phil);
            System.out.println("LOVE: " + love);
            System.out.println("Tax: " + tax);
            System.out.println("Total Deductions: " + totalDeduct);
            System.out.println("Net Salary: " + (g2 - totalDeduct));
            System.out.println("---------------------------------------------");
        }
    }

    public static void processSpecificPayroll(Scanner sc, String empNum) {
        System.out.print("Enter Month (6-12): ");
        int m = Integer.parseInt(sc.nextLine());
        System.out.print("Enter Cutoff [1] 1-15 [2] 16-31: ");
        int cutoff = Integer.parseInt(sc.nextLine());

        String[] emp = findEmployee(empNum);
        double rate = Double.parseDouble(emp[18].trim().replace(",", ""));
        double h1 = getMonthlyHours(empNum, m, 1, 15);
        double h2 = getMonthlyHours(empNum, m, 16, 31);

        if (cutoff == 1) {
            System.out.println("\nCutoff Date: " + getMonthName(m) + " 1 to 15");
            System.out.println("Total Hours Worked: " + h1);
            System.out.println("Gross Salary: " + (h1 * rate));
            System.out.println("Net Salary: " + (h1 * rate));
        } else {
            double totalGross = (h1 + h2) * rate;
            double phil = totalGross * 0.03, love = 100.0, tax = (totalGross - (phil + love)) * 0.15;
            System.out.println("\nCutoff Date: " + getMonthName(m) + " 16 to End");
            System.out.println("Total Hours Worked: " + h2);
            System.out.println("Gross Salary: " + (h2 * rate));
            System.out.println("Net Salary: " + ((h2 * rate) - (phil + love + tax)));
        }
    }

    public static void processAllEmployees() {
        File f = getFile("Employee Details.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                processFullPayroll(data[0].trim());
            }
        } catch (Exception e) { }
    }

    // --- CORE LOGIC & UTILS ---
    public static String[] findEmployee(String id) {
        File f = getFile("Employee Details.csv");
        if (f == null) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data[0].trim().equals(id)) return data;
            }
        } catch (Exception e) { }
        return null;
    }

    public static double getMonthlyHours(String id, int m, int start, int end) {
        double total = 0;
        File f = getFile("Attendance Record.csv");
        if (f == null) return 0;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; br.readLine();
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                String[] dp = d[3].split("/");
                int csvM = Integer.parseInt(dp[0]), csvD = Integer.parseInt(dp[1]);
                if (d[0].equals(id) && csvM == m && csvD >= start && csvD <= end) {
                    total += calcHours(d[4], d[5]);
                }
            }
        } catch (Exception e) { }
        return total;
    }

    public static double calcHours(String in, String out) {
        String[] t1 = in.split(":"), t2 = out.split(":");
        int hIn = Integer.parseInt(t1[0]), mIn = Integer.parseInt(t1[1]);
        int hOut = Integer.parseInt(t2[0]), mOut = Integer.parseInt(t2[1]);
        if (hIn == 8 && mIn <= 5) mIn = 0;
        if (hIn < 8) { hIn = 8; mIn = 0; }
        if (hOut >= 17) { hOut = 17; mOut = 0; }
        double dur = (hOut + mOut/60.0) - (hIn + mIn/60.0);
        return (dur > 5) ? dur - 1 : (dur > 0) ? dur : 0;
    }

    public static File getFile(String name) {
        File f = new File(name);
        if (f.exists()) return f;
        f = new File("..", name);
        return f.exists() ? f : null;
    }

    public static String getMonthName(int m) {
        String[] names = {"","","","","","","June","July","August","September","October","November","December"};
        return names[m];
    }

    private static void displayFullProfile(String[] details) {
        String[] labels = {"Employee #", "Last Name", "First Name", "Birthday", "Address", "Phone Number", "SSS #", "Philhealth #", "TIN #", "Pag-ibig #", "Status", "Position", "Immediate Supervisor", "Basic Salary", "Rice Subsidy", "Phone Allowance", "Clothing Allowance", "Gross Semi-monthly Rate", "Hourly Rate"};
        System.out.println("\n--- FULL PROFILE ---");
        for (int i = 0; i < labels.length; i++) System.out.println(labels[i] + ": " + details[i]);
    }
}
