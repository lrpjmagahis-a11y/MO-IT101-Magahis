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
                }
            } else if (choice.equals("2")) break;
        }
    }

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
                    String empNum = sc.nextLine();
                    if (findEmployee(empNum) == null) {
                        System.out.println("Employee number does not exist.");
                    } else {
                        processPayroll(empNum);
                    }
                } else if (sub.equals("2")) {
                    processAllEmployees();
                } else if (sub.equals("3")) break;
            } else if (choice.equals("2")) break;
        }
    }

    public static String[] findEmployee(String id) {
        File f = getFile("Employee Details.csv");
        if (f == null) return null;
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line;
            br.readLine(); 
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                if (data[0].trim().equals(id)) return data;
            }
        } catch (Exception e) { System.out.println("Error reading Employee Details."); }
        return null;
    }

    public static void processAllEmployees() {
        File f = getFile("Employee Details.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; br.readLine();
            while ((line = br.readLine()) != null) {
                String[] data = line.split(",(?=(?:[^\"]*\"[^\"]*\")*[^\"]*$)");
                processPayroll(data[0].trim());
            }
        } catch (Exception e) { System.out.println("Error processing all employees."); }
    }

    public static void processPayroll(String empNum) {
        String[] emp = findEmployee(empNum);
        double rate = Double.parseDouble(emp[18].trim());

        System.out.println("\nEmployee #: " + emp[0]);
        System.out.println("Employee Name: " + emp[2] + " " + emp[1]);
        System.out.println("Birthday: " + emp[3]);

        for (int m = 6; m <= 12; m++) {
            double h1 = getMonthlyHours(empNum, m, 1, 15);
            double h2 = getMonthlyHours(empNum, m, 16, 31);
            double g1 = h1 * rate;
            double g2 = h2 * rate;

            System.out.println("Cutoff Date: " + getMonthName(m) + " 1 to " + getMonthName(m) + " 15");
            System.out.println("Total Hours Worked: " + h1);
            System.out.println("Gross Salary: " + g1);
            System.out.println("Net Salary: " + g1);

            double totalGross = g1 + g2;
            double phil = totalGross * 0.03;
            double love = 100.0;
            double tax = (totalGross - (phil + love)) * 0.15;
            double totalDeduct = phil + love + tax;

            System.out.println("Cutoff Date: " + getMonthName(m) + " 16 to " + getMonthName(m) + " 30/31");
            System.out.println("Total Hours Worked: " + h2);
            System.out.println("Gross Salary: " + g2);
            System.out.println("Each Deduction:");
            System.out.println("PhilHealth: " + phil + "\nLOVE: " + love + "\nTax: " + tax);
            System.out.println("Total Deductions: " + totalDeduct);
            System.out.println("Net Salary: " + (g2 - totalDeduct));
            System.out.println("---------------------------------------------");
        }
    }

    public static double getMonthlyHours(String id, int m, int start, int end) {
        double total = 0;
        File f = getFile("Attendance Record.csv");
        try (BufferedReader br = new BufferedReader(new FileReader(f))) {
            String line; br.readLine();
            while ((line = br.readLine()) != null) {
                String[] d = line.split(",");
                String[] dateParts = d[3].split("/");
                int csvM = Integer.parseInt(dateParts[0]);
                int csvD = Integer.parseInt(dateParts[1]);
                if (d[0].equals(id) && csvM == m && csvD >= start && csvD <= end) {
                    total += calcHours(d[4], d[5]);
                }
            }
        } catch (Exception e) { }
        return total;
    }

    public static double calcHours(String in, String out) {
        String[] t1 = in.split(":");
        String[] t2 = out.split(":");
        int hIn = Integer.parseInt(t1[0]), mIn = Integer.parseInt(t1[1]);
        int hOut = Integer.parseInt(t2[0]), mOut = Integer.parseInt(t2[1]);

        // Rule 4c & 4d: 8:05 grace period
        if (hIn == 8 && mIn <= 5) mIn = 0;
        // Rule 4a: Max 8:00 AM to 5:00 PM
        if (hIn < 8) { hIn = 8; mIn = 0; }
        if (hOut >= 17) { hOut = 17; mOut = 0; }

        double duration = (hOut + mOut/60.0) - (hIn + mIn/60.0);
        return (duration > 5) ? duration - 1 : duration; // Less lunch
    }

    public static File getFile(String name) {
        File f = new File(name);
        if (f.exists()) return f;
        f = new File("..", name); // Check parent directory for NetBeans
        if (f.exists()) return f;
        return null;
    }

    public static String getMonthName(int m) {
        String[] names = {"","","","","","","June","July","August","September","October","November","December"};
        return names[m];
    }
}
