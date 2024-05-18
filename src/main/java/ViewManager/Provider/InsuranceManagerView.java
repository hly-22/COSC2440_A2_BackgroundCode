package ViewManager.Provider;

import Models.Provider.InsuranceManager;
import OperationManager.Provider.InsuranceManagerOperations;

import java.util.Scanner;

public class InsuranceManagerView {
    private InsuranceManagerOperations operations;
    static Scanner scanner = new Scanner(System.in);

    public InsuranceManagerView(InsuranceManager insuranceManager) {
        this.operations = new InsuranceManagerOperations(insuranceManager);
    }
    public void displayInfo() {
        operations.displayInfo();
    }
    public void updatePassword() {
        operations.updateProviderPassword();
    }
    public void displaySurveyorMenu() {
        while (true) {
            System.out.println();
            System.out.println("1. Retrieve Surveyor by pID");
            System.out.println("2. Retrieve All Surveyors");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void displayClaimMenu() {
        while (true) {
            System.out.println();
            System.out.println("1. Retrieve Claim by fID");
            System.out.println("2. Retrieve All Claims of cID");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void displayCustomerMenu() {
        while (true) {
            System.out.println();
            System.out.println("1. Retrieve Customer by cID");
            System.out.println("2. Retrieve All Customers");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }

    public void displayInsuranceManagerMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Insurance Manager ===");
            System.out.println("1. Surveyor Menu");
            System.out.println("2. Claim Menu");
            System.out.println("3. Customer Menu");
            System.out.println("4. Retrieve Personal Information");
            System.out.println("5. Update Password");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displaySurveyorMenu();
                    case 2 -> displayClaimMenu();
                    case 3 -> displayCustomerMenu();
                    case 4 -> displayInfo();
                    case 5 -> updatePassword();
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
}
