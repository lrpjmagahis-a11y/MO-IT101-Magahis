package motorph;

import java.util.Scanner;

public class PayrollSystem {
    public static void main(String[] args) {
        Scanner input = new Scanner(System.in);
        boolean isRunning = true;

        while (isRunning) {
            System.out.println("\n======================================");
            System.out.println("    MOTORPH PAYROLL SYSTEM LOGIN");
            System.out.println("======================================");

            System.out.print("Enter Username (or 'exit' to quit): ");
            String username = input.nextLine();

            if (username.equalsIgnoreCase("exit")) {
                break;
            }

            System.out.print("Enter 5-digit Password: ");
            String password = input.nextLine();

            if (password.equals("12345")) {
                if (username.equals("admin")) {
                    showMenu("Admin", input);
                } else if (username.equals("employee")) {
                    showMenu("Employee", input);
                } else {
                    System.out.println("\n[!] User not found.");
                }
            } else {
                System.out.println("\n[!] Invalid Password. Access Denied.");
            }
        }
        System.out.println("System Shutdown. Goodbye!");
    }

    public static void showMenu(String role, Scanner input) {
        boolean backToLogin = false;

        while (!backToLogin) {
            System.out.println("\n--- " + role + " Dashboard ---");
            System.out.println("1. View Profile");
            System.out.println("2. Calculate Weekly Salary");
            System.out.println("3. View Attendance Logs");
            System.out.println("4. Logout");
            System.out.print("\nSelect an option: ");

            try {
                int choice = Integer.parseInt(input.nextLine());

                if (choice == 4) {
                    backToLogin = true;
                    System.out.println("Logging out...");
                } else {
                    processChoice(choice);
                }
            } catch (NumberFormatException e) {
                System.out.println("[!] Please enter a valid number (1-4).");
            }
        }
    }

    private static void processChoice(int choice) {
        // This is where you will eventually call your CSV Reader methods
        switch (choice) {
            case 1:
                System.out.println("Searching data/Employee_Data.csv for profile...");
                break;
            case 2:
                System.out.println("Calculating salary from HourlyRate and Attendance_Logs.csv...");
                break;
            case 3:
                System.out.println("Loading DTR from data/Attendance_Logs.csv...");
                break;
            default:
                System.out.println("Invalid option.");
        }
    }
}
