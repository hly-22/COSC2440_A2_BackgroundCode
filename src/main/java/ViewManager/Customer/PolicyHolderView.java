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

    public void addADependent() {
        operations.addDependent();
    }
    public void displayPolicyHolderMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Policy Holder ===");
            System.out.println("1. Add a Dependent");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addADependent();
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
