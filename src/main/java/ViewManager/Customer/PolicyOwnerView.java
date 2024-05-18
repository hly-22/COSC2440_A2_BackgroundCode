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
    public void displayBeneMenu() {
        while(true) {
            System.out.println();
            System.out.println("1. Add Beneficiary");
            System.out.println("2. Update Beneficiary");
            System.out.println("3. Retrieve Beneficiary by cID");
            System.out.println("4. Retrieve Policy Holder and Dependents");
            System.out.println("5. Retrieve All Beneficiaries");
            System.out.println("6. Delete Beneficiary");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addABeneficiary();
                    case 2 -> updateABeneficiary();
                    case 3 -> retrieveBeneficiaryByCID();
                    case 4 -> retrievePolicyHolderAndDependents();
                    case 5 -> retrieveAllBeneficiaries();
                    case 6 -> deleteBeneficiary();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void retrieveAllClaims() {
        operations.getAllClaims();
    }
    public void displayClaimMenu() {
        while(true) {
            System.out.println();
            System.out.println("1. Add Claim for Beneficiary");
            System.out.println("2. Update Claim for Beneficiary");
            System.out.println("3. Retrieve Claim by fID");
            System.out.println("4. Retrieve All Claims of Beneficiary");
            System.out.println("5. Retrieve All Claims");
            System.out.println("6. Delete Claim");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addAClaimForBeneficiary();
                    case 2 -> updateClaim();
                    case 3 -> retrieveClaimByFID();
                    case 4 -> retrieveAllClaimsOfBeneficiary();
                    case 5 -> retrieveAllClaims();
                    case 6 -> deleteClaim();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void displayInfoMenu() {
        while(true) {
            System.out.println();
            System.out.println("1. Retrieve Personal Information");
            System.out.println("2. Update Phone");
            System.out.println("3. Update Address");
            System.out.println("4. Update Email");
            System.out.println("5. Update Password");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displayInfo();
                    case 2 -> updatePhone();
                    case 3 -> updateAddress();
                    case 4 -> updateEmail();
                    case 5 -> updatePassword();
                    case 0 -> {
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void displayPolicyOwnerMenu() {

        while (true) {
            System.out.println();
            System.out.println("=== Policy Owner ===");
            System.out.println("1. CRUD for Beneficiaries");
            System.out.println("2. CRUD for Claims of Beneficiaries");
            System.out.println("3. Personal Information");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displayBeneMenu();
                    case 2 -> displayClaimMenu();
                    case 3 -> displayInfoMenu();
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
