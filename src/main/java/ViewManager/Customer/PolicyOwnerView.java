package ViewManager.Customer;

import Database.CustomerCRUD;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import OperationManager.Customer.PolicyOwnerOperations;
import OperationManager.Utils.InputChecker;

import java.util.ArrayList;
import java.util.Scanner;

public class PolicyOwnerView {
    static PolicyOwnerOperations operations = new PolicyOwnerOperations(new PolicyOwner("c-0000000", "Cathy", "0907838929", "Vietnamese", "policy_owner1@gmail.com", "testing"));
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
    public void addAClaimForBeneficiary() {

        System.out.println("Enter insurance card number: ");
        String insuranceCardNumber = scanner.nextLine();
        if (!InputChecker.isValidCardFormat(insuranceCardNumber)) {
            System.out.println("Invalid format.");
            return;
        }
        operations.addClaim(insuranceCardNumber);
    }

    public void displayPolicyOwnerMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Policy Owner ===");
            System.out.println("1. Add a Beneficiary");
            System.out.println("2. Add Claim for Beneficiary");
            System.out.println("3. Retrieve Claim by fID");
            System.out.println("4. Retrieve All Claims");
            System.out.println("5. Retrieve All Claims of Beneficiary");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addABeneficiary();
                    case 2 -> addAClaimForBeneficiary();
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
