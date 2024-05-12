package ViewManager.Customer;

import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import OperationManager.Customer.PolicyOwnerOperations;

import java.util.ArrayList;
import java.util.Scanner;

public class PolicyOwnerView {
    static PolicyOwnerOperations operations = new PolicyOwnerOperations();
    static Scanner scanner = new Scanner(System.in);

    public static void addABeneficiary() {

        while (true) {
            System.out.println();
            System.out.println("1. Add a Policy Holder");
            System.out.println("2. Add a Dependent");
            System.out.println("0. Go back");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1:
                        operations.addBeneficiary();
                        break;
                    case 2:
                        System.out.println("Enter a Policy Holder cID: ");
                        String policyHolderCID = scanner.nextLine();
                        // find existing policyholder in beneficiaries, print error message and return if not found
                        // test policyHolder
                        PolicyHolder policyHolder = new PolicyHolder(policyHolderCID);
                        operations.addBeneficiary(policyHolder);
                        break;
                    case 0:
                        System.out.println("Going back...");
                        return;
                    default:
                        System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }

    public void displayPolicyOwnerMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Policy Owner ===");
            System.out.println("1. Add a Beneficiary");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addABeneficiary();
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
