package ViewManager.Customer;

import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.InsuranceCard.InsuranceCard;
import OperationManager.Customer.PolicyHolderOperations;
import OperationManager.Utils.InputChecker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.Scanner;

public class PolicyHolderView {
    private PolicyHolderOperations operations;
    static Scanner scanner = new Scanner(System.in);
    public PolicyHolderView(PolicyHolder policyHolder) {
        this.operations = new PolicyHolderOperations(policyHolder);
    }

//    public void addADependent() {
//        operations.addDependent();
//    }
    public void updatePersonalPassword() {
        operations.updatePassword();
    }
    public void displayPersonalMenu() {

        while (true) {
            System.out.println();
            System.out.println("1. Update Phone");
            System.out.println("2. Update Address");
            System.out.println("3. Update Email");
            System.out.println("4. Update Password");
            System.out.println("5. Retrieve Information");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 4 -> updatePersonalPassword();
                    case 0 -> { return;}
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void displayDependentMenu() {

        while (true) {
            System.out.println();
            System.out.println("1. Update Phone");
            System.out.println("2. Update Address");
            System.out.println("3. Update Email");
            System.out.println("4. Update Password");
            System.out.println("5. Retrieve Dependent by cID");
            System.out.println("6. Retrieve All Dependents");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 0 -> { return;}
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
            System.out.println("1. Add Claim");
            System.out.println("2. Retrieve Claim by fID");
            System.out.println("3. Retrieve All Claims");
            System.out.println("4. Update Claim");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {

                    case 0 -> { return;}
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void displayDependentClaimMenu() {

        while (true) {
            System.out.println();
            System.out.println("1. Add Claim");
            System.out.println("2. Retrieve Claim by fID");
            System.out.println("3. Retrieve All Claims");
            System.out.println("4. Retrieve All Claims of Dependent");
            System.out.println("4. Update Claim");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {

                    case 0 -> { return;}
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void displayPolicyHolderMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Policy Holder ===");
            System.out.println("1. Personal Information");
            System.out.println("2. Dependent Information");
            System.out.println("3. Personal Claims");
            System.out.println("4. Dependent Claims");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displayPersonalMenu();
                    case 2 -> displayDependentMenu();
                    case 3 -> displayClaimMenu();
                    case 4 -> displayDependentClaimMenu();
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
