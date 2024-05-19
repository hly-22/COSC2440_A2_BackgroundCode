package ViewManager.Customer;

import Database.CustomerCRUD;
import Models.Claim.Claim;
import Models.Claim.Document;
import Models.Customer.Customer;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import OperationManager.Customer.PolicyOwnerOperations;
import OperationManager.Utils.InputChecker;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;
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
    public void updateBeneficiaryInfo() {
        // Ask for the beneficiary's CID
        System.out.print("Enter the beneficiary's cID: ");
        String beneficiaryCID = scanner.nextLine().trim();

        if (!(operations.policyOwner.getBeneficiaries().contains(beneficiaryCID))) {
            System.out.println("cID not found in beneficiaries.");
            return;
        }

        // Ask for the updated beneficiary information
        System.out.print("Enter updated phone number: ");
        String phone = scanner.nextLine().trim();

        System.out.print("Enter updated address: ");
        String address = scanner.nextLine().trim();

        System.out.print("Enter updated email: ");
        String email = scanner.nextLine().trim();

        // Call the updateBeneficiaryInfo method in PolicyOwnerOperations
        boolean success = operations.updateBeneficiaryInfo(beneficiaryCID, phone, address, email);
        if (success) {
            System.out.println("Beneficiary information updated successfully.");
        } else {
            System.out.println("Failed to update beneficiary information.");
        }
    }
    public void retrieveAllBeneficiaries() {
        // Get all beneficiaries associated with the policy owner
        List<Customer> beneficiaries = operations.getAllBeneficiaries();

        // Display the list of beneficiaries
        System.out.println("List of Beneficiaries:");
        for (Customer beneficiary : beneficiaries) {
            System.out.println(beneficiary);
        }
    }
    public void retrieveBeneficiaryByCID() {

        System.out.println("Enter beneficiary cID: ");
        String cID = scanner.nextLine().trim();

        Customer beneficiary = operations.getBeneficiaryByID(cID);
        if (beneficiary != null) {
            System.out.println("Beneficiary found: " + beneficiary);
        } else {
            System.out.println("No beneficiary found with cID: " + cID);
        }
    }
    public void retrievePolicyHolderAndDependents() {
        System.out.println("Enter cID of the policyholder: ");
        String cID = scanner.nextLine().trim();

        PolicyHolder policyHolder = operations.retrievePolicyholderAndDependents(cID);
        if (policyHolder != null) {
            System.out.println("Policyholder found: " + policyHolder);
            if (!policyHolder.getDependentList().isEmpty()) {
                System.out.println("Dependents: ");
                for (String dependentCID : policyHolder.getDependentList()) {
                    Customer dependent = operations.getCustomerByID(dependentCID);
                    if (dependent != null) {
                        System.out.println(dependent);
                    }
                }
            } else {
                System.out.println("No dependents found for this policyholder.");
            }
        } else {
            System.out.println("No policyholder found with the given cID.");
        }
    }
    public void deleteBeneficiary() {
        System.out.println("Enter the cID of the beneficiary to delete: ");
        String beneficiaryCID = scanner.nextLine().trim();

        boolean deleted = operations.deleteBeneficiary(operations.policyOwner.getCID(), beneficiaryCID);
        if (deleted) {
            System.out.println("Beneficiary with cID " + beneficiaryCID + " deleted successfully.");
        } else {
            System.out.println("Failed to delete beneficiary. Make sure the cID is correct and belongs to a beneficiary.");
        }
    }
    public void updatePassword() {
        operations.updatePassword();
    }
    public void displayBeneMenu() {
        while(true) {
            System.out.println();
            System.out.println("1. Add Beneficiary");
            System.out.println("2. Update Beneficiary Information");
            System.out.println("3. Retrieve Beneficiary by cID");
            System.out.println("4. Retrieve Policy Holder and Dependents");
            System.out.println("5. Retrieve All Beneficiaries");
            System.out.println("6. Delete Beneficiary");
            System.out.println("7. Update Beneficiary Password");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addABeneficiary();
                    case 2 -> updateBeneficiaryInfo();
                    case 3 -> retrieveBeneficiaryByCID();
                    case 4 -> retrievePolicyHolderAndDependents();
                    case 5 -> retrieveAllBeneficiaries();
                    case 6 -> deleteBeneficiary();
                    case 7 -> operations.updateBeneficiaryPassword();
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
    public void updateBeneficiaryClaim() {
        // Ask for the beneficiary's CID
        System.out.print("Enter the beneficiary's CID: ");
        String beneficiaryCID = scanner.nextLine().trim();

        // Ask for the FID of the claim to update
        System.out.print("Enter the FID of the claim to update: ");
        String claimFID = scanner.nextLine().trim();

        if (operations.getClaimByID(claimFID) == null) {
            System.out.println("Claim does not exist.");
            return;
        }

        if (!(operations.getClaimByID(claimFID).getCardNumber().getPolicyOwner().equals(operations.policyOwner.getCID()))) {
            System.out.println("Claim does not belong beneficiaries of this Policy Owner.");
            return;
        }

        // Ask for new documents
        List<Document> newDocuments = new ArrayList<>();
        System.out.println("Enter new documents (enter 'x' to finish): ");
        while (true) {
            System.out.println("Enter document id (d-xxxxxxx): ");
            String documentID = scanner.nextLine().trim();

            if (documentID.equalsIgnoreCase("x")) {
                break;
            }

            System.out.println("Enter document name: ");
            String fileName = scanner.nextLine().trim();

            Document document = new Document(documentID, claimFID, fileName);
            newDocuments.add(document);
        }

        // Ask for new receiver banking info
        System.out.print("Enter new receiver banking info (press enter to keep existing): ");

        System.out.println("Enter new receiver banking information in the following format:");
        System.out.println("bank, name, number (press enter to skip");

        String newReceiverBankingInfo = null;
        while (true) {
            String input = scanner.nextLine();
            if (input.isEmpty()) {
                break;
            }

            String [] parts = input.split(",");
            if (parts.length < 3) {
                System.out.println("Invalid input. Try again next time.");
                break;
            }
            String bank = parts[0].trim();
            String name = parts[1].trim();
            String number = parts[2].trim();

            newReceiverBankingInfo = bank + " - " + name + " - " + number;
        }

        // Update the claim
        boolean claimUpdated = operations.updateClaim(claimFID, newDocuments, newReceiverBankingInfo);

        // Display success or failure message
        if (claimUpdated) {
            System.out.println("Claim updated successfully.");
            operations.updateActionHistory(": update Claim");
        } else {
            System.out.println("Failed to update claim. Please try again.");
        }
    }
    public void getAllBeneficiaryClaims() {
        System.out.print("Enter the CID of the beneficiary: ");
        String beneficiaryCID = scanner.nextLine().trim();

        List<Claim> beneficiaryClaims = operations.getAllBeneficiaryClaims(beneficiaryCID);
        if (beneficiaryClaims.isEmpty()) {
            System.out.println("No claims found for the specified beneficiary.");
        } else {
            System.out.println("Claims for the beneficiary:");
            for (Claim claim : beneficiaryClaims) {
                System.out.println(claim);
            }
        }
    }
    public void deleteClaim() {

        System.out.println("Enter fID to delete: ");
        String fID = scanner.nextLine();

        if (fID == null) {
            System.out.println("Invalid input.");
            return;
        }
        operations.deleteClaim(fID);
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
                    case 2 -> updateBeneficiaryClaim();
                    case 3 -> retrieveClaimByFID();
                    case 4 -> getAllBeneficiaryClaims();
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
    public void updatePersonalPhone() {
        System.out.println("Enter new phone number: ");
        String newPhone = scanner.nextLine().trim();
        if (!InputChecker.isValidPhoneNumber(newPhone)) {
            System.out.println("Invalid format.");
            return;
        }

        boolean success = operations.updatePhone(newPhone);
        if (success) {
            System.out.println("Phone number updated successfully!");
            operations.updateActionHistory(": update phone");
        } else {
            System.out.println("Failed to update phone number.");
        }
    }
    public void updatePersonalAddress() {
        System.out.println("Enter new address: ");
        String newAddress = scanner.nextLine().trim();

        boolean success = operations.updateAddress(newAddress);
        if (success) {
            System.out.println("Address updated successfully!");
            operations.updateActionHistory(": update address");
        } else {
            System.out.println("Failed to update address.");
        }
    }
    public void updatePersonalEmail() {
        System.out.println("Enter new email: ");
        String newEmail = scanner.nextLine().trim();
        if (!InputChecker.isValidEmailAddress(newEmail)) {
            System.out.println("Invalid format.");
            return;
        }

        boolean success = operations.updateEmail(newEmail);
        if (success) {
            System.out.println("Email updated successfully!");
            operations.updateActionHistory(": update email");
        } else {
            System.out.println("Email to update address.");
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
                    case 1 -> operations.displayInfo();
                    case 2 -> updatePersonalPhone();
                    case 3 -> updatePersonalAddress();
                    case 4 -> updatePersonalEmail();
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
    public void calculateSumFee() {
        // Get the policy owner's CID from the logged-in user or user input
        String policyOwnerCID = operations.policyOwner.getCID();

        // Call the calculateSumInsuranceFee method in the operations class
        BigDecimal sumInsuranceFee = operations.calculateInsuranceFee(policyOwnerCID);

        // Display the calculated sum insurance fee
        System.out.println("Total insurance fee to be paid annually: USD " + sumInsuranceFee);
    }
    public void displayPolicyOwnerMenu() {

        while (true) {
            System.out.println();
            System.out.println("=== Policy Owner ===");
            System.out.println("1. CRUD for Beneficiaries");
            System.out.println("2. CRUD for Claims of Beneficiaries");
            System.out.println("3. Personal Information");
            System.out.println("4. Total Insurance Fee");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displayBeneMenu();
                    case 2 -> displayClaimMenu();
                    case 3 -> displayInfoMenu();
                    case 4 -> calculateSumFee();
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
