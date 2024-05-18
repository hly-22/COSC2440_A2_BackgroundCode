package ViewManager.Customer;

import Database.CustomerCRUD;
import Models.Claim.Claim;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import OperationManager.Customer.PolicyOwnerOperations;
import OperationManager.Utils.InputChecker;

import java.util.ArrayList;
import java.util.Scanner;

public class PolicyOwnerView {
    private PolicyOwnerOperations operations;
    private final Scanner scanner = new Scanner(System.in);
    public PolicyOwnerView(PolicyOwner policyOwner) {
        this.operations = new PolicyOwnerOperations(policyOwner);
    }

    public void addABeneficiary() {

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
    public void retrieveClaimByFID() {
        System.out.println("Enter the FID of the claim:");
        String fID = scanner.nextLine();
        if (!InputChecker.isValidFIDFormat(fID)) {
            System.out.println("Invalid FID format. FID should be in the format 'f-#######'.");
            return;
        }
        Claim claim = operations.getClaimByID(fID);
        if (claim != null) {
            System.out.println("Claim found:");
            System.out.println(claim);
        } else {
            System.out.println("Claim with ID " + fID + " not found.");
        }
    }
    public void updatePassword() {
        operations.updatePassword();
    }
    public void displayPolicyOwnerMenu() {

        while (true) {
            System.out.println();
            System.out.println("=== Policy Owner ===");
            System.out.println("1. Add a Beneficiary");
            System.out.println("2. Add Claim for Beneficiary");
            System.out.println("3. Retrieve Claim by fID");
            System.out.println("4. Retrieve All Claims");
            System.out.println("5. Retrieve All Claims of Beneficiary");
            System.out.println("9. Update Password");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addABeneficiary();
                    case 2 -> addAClaimForBeneficiary();
                    case 3 -> retrieveClaimByFID();
                    case 9 -> updatePassword();
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
