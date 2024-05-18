package ViewManager.Customer;

import Models.Claim.Claim;
import Models.Claim.Document;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.InsuranceCard.InsuranceCard;
import OperationManager.Customer.PolicyHolderOperations;
import OperationManager.Utils.InputChecker;
import UserManagement.SessionManager;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolicyHolderView {
    private PolicyHolderOperations operations;
    static Scanner scanner = new Scanner(System.in);
    public PolicyHolderView(PolicyHolder policyHolder) {
        this.operations = new PolicyHolderOperations(policyHolder);
    }

    public void updatePersonalPassword() {
        operations.updatePassword();
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
    public void displayPersonalMenu() {

        while (true) {
            System.out.println();
            System.out.println("1. Retrieve Information");
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
                    case 5 -> updatePersonalPassword();
                    case 0 -> { return;}
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }

    public void retrieveDependentInfo() {
        // Ask for the dependent's CID
        System.out.print("Enter the CID of the dependent: ");
        String dependentCID = scanner.nextLine().trim();

        // Retrieve the dependent's information using the policyholder's CID and dependent's CID
        Dependent dependentInfo = operations.getDependentInfo(dependentCID);

        // Display the dependent's information if found
        if (dependentInfo != null) {
            System.out.println(dependentInfo);
            operations.updateDependentActionHistory(dependentCID, ": retrieve information");
        }
    }
    public void retrieveAllDependentsInfo() {
        // Retrieve information for all dependents using the policyholder's CID
        List<Dependent> allDependentsInfo = operations.getAllDependentsInfo();

        // Display information for all dependents
        if (!allDependentsInfo.isEmpty()) {
            System.out.println("Dependents:");
            for (Dependent dependent : allDependentsInfo) {
                System.out.println(dependent);
            }
            operations.updateActionHistory(": retrieve all Dependents");
        } else {
            System.out.println("You have no dependents.");
        }
    }
    public void updateDependentPhone() {
        // First, retrieve the list of dependents for the policyholder
        List<String> dependentList = operations.policyHolder.getDependentList();

        // Ask for the dependent's CID
        System.out.println("Enter the dependent's CID: ");
        String dependentCID = scanner.nextLine().trim();

        // Check if the provided dependent CID is in the list of dependents
        if (!dependentList.contains(dependentCID)) {
            System.out.println("The provided CID does not belong to any of your dependents.");
            return;
        }

        // Proceed with updating the dependent's phone number
        System.out.println("Enter new phone number for the dependent: ");
        String newPhone = scanner.nextLine().trim();

        // Update the phone number for the dependent
        boolean success = operations.updateDependentPhone(dependentCID, newPhone);
        if (success) {
            System.out.println("Phone number updated successfully for the dependent!");
            operations.updateDependentActionHistory(dependentCID, ": update phone");
        } else {
            System.out.println("Failed to update phone number for the dependent.");
        }
    }
    public void updateDependentAddress() {
        // First, retrieve the list of dependents for the policyholder
        List<String> dependentList = operations.policyHolder.getDependentList();

        // Ask for the dependent's CID
        System.out.println("Enter the dependent's CID: ");
        String dependentCID = scanner.nextLine().trim();

        // Check if the provided dependent CID is in the list of dependents
        if (!dependentList.contains(dependentCID)) {
            System.out.println("The provided CID does not belong to any of your dependents.");
            return;
        }

        // Proceed with updating the dependent's phone number
        System.out.println("Enter new address for the dependent: ");
        String newAddress = scanner.nextLine().trim();

        // Update the phone number for the dependent
        boolean success = operations.updateDependentAddress(dependentCID, newAddress);
        if (success) {
            System.out.println("Address updated successfully for the dependent!");
            operations.updateDependentActionHistory(dependentCID, ": update address");
        } else {
            System.out.println("Failed to update address for the dependent.");
        }
    }
    public void updateDependentEmail() {
        // First, retrieve the list of dependents for the policyholder
        List<String> dependentList = operations.policyHolder.getDependentList();

        // Ask for the dependent's CID
        System.out.println("Enter the dependent's CID: ");
        String dependentCID = scanner.nextLine().trim();

        // Check if the provided dependent CID is in the list of dependents
        if (!dependentList.contains(dependentCID)) {
            System.out.println("The provided CID does not belong to any of your dependents.");
            return;
        }

        // Proceed with updating the dependent's phone number
        System.out.println("Enter new email for the dependent: ");
        String newEmail = scanner.nextLine().trim();

        // Update the phone number for the dependent
        boolean success = operations.updateDependentEmail(dependentCID, newEmail);
        if (success) {
            System.out.println("Email updated successfully for the dependent!");
            operations.updateDependentActionHistory(dependentCID, ": update email");
        } else {
            System.out.println("Failed to update email for the dependent.");
        }
    }
    public void displayDependentMenu() {

        while (true) {
            System.out.println();
            System.out.println("1. Retrieve Dependent by cID");
            System.out.println("2. Retrieve All Dependents");
            System.out.println("3. Update Phone");
            System.out.println("4. Update Address");
            System.out.println("5. Update Email");
            System.out.println("6. Update Password");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> retrieveDependentInfo();
                    case 2 -> retrieveAllDependentsInfo();
                    case 3 -> updateDependentPhone();
                    case 4 -> updateDependentAddress();
                    case 5 -> updateDependentEmail();
                    case 6 -> operations.updateDependentPassword();
                    case 0 -> { return;}
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
    public void addClaim() {
        operations.addClaim(operations.policyHolder.getInsuranceCardNumber());
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
    public void updateClaim() {
        // Ask for claim FID
        System.out.print("Enter fID of the claim to update: ");
        String claimfID = scanner.nextLine().trim();

        if (operations.getClaimByID(claimfID) == null) {
            System.out.println("Claim does not exist.");
            return;
        }

        if (!(operations.getClaimByID(claimfID).getInsuredPerson().equals(operations.policyHolder.getCID()))) {
            System.out.println("Claim does not belong to this Policy Holder.");
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

            Document document = new Document(documentID, claimfID, fileName);
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
        boolean claimUpdated = operations.updateClaim(claimfID, newDocuments, newReceiverBankingInfo);

        // Display success or failure message
        if (claimUpdated) {
            System.out.println("Claim updated successfully.");
            operations.updateActionHistory(": update Claim");
        } else {
            System.out.println("Failed to update claim. Please try again.");
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
                    case 1 -> addClaim();
                    case 2 -> retrieveClaimByFID();
                    case 3 -> retrieveAllClaims();
                    case 4 -> updateClaim();
                    case 0 -> { return;}
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }

    public void addClaimForDependent() {

        System.out.println("Enter dependent cID: ");
        String dependentCID = scanner.nextLine();
        if (!InputChecker.isValidCIDFormat(dependentCID)) {
            System.out.println("Invalid format.");
            return;
        }
        operations.addClaimForDependent(dependentCID);
    }
    public void retrieveClaimByFIDForDependent() {
        System.out.println("Enter dependent cID: ");
        String dependentCID = scanner.nextLine();

        System.out.print("Enter fID of the claim to update: ");
        String claimfID = scanner.nextLine().trim();

        // Check if the dependent CID belongs to the policyholder's dependent list
        if (!operations.checkDependent(operations.policyHolder.getCID(), dependentCID)) {
            System.out.println("Invalid dependent CID.");
            return;
        }

        // Check if the claim FID belongs to the specified dependent
        if (!operations.checkClaimOwner(dependentCID, claimfID)) {
            System.out.println("Invalid claim FID for the specified dependent.");
            return;
        }

        Claim claim = null;
        List<Claim> claimList = operations.getClaimsOfDependent(dependentCID);
        for (Claim c: claimList) {
            if (c.getFID().equals(claimfID)) {
                claim = c;
            }
        }

        if (claim != null) {
            System.out.println("Claim found:");
            System.out.println(claim);
        } else {
            System.out.println("Claim with ID " + claimfID + " not found.");
        }
    }
    public void updateClaimForDependent() {

        System.out.println("Enter dependent cID: ");
        String dependentCID = scanner.nextLine();

        System.out.print("Enter fID of the claim to update: ");
        String claimfID = scanner.nextLine().trim();

        // Check if the dependent CID belongs to the policyholder's dependent list
        if (!operations.checkDependent(operations.policyHolder.getCID(), dependentCID)) {
            System.out.println("Invalid dependent CID.");
            return;
        }

        // Check if the claim FID belongs to the specified dependent
        if (!operations.checkClaimOwner(dependentCID, claimfID)) {
            System.out.println("Invalid claim FID for the specified dependent.");
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

            Document document = new Document(documentID, claimfID, fileName);
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
        boolean claimUpdated = operations.updateClaim(claimfID, newDocuments, newReceiverBankingInfo);

        // Display success or failure message
        if (claimUpdated) {
            System.out.println("Claim updated successfully.");
            operations.updateDependentActionHistory(dependentCID,": update Claim" + claimfID);
        } else {
            System.out.println("Failed to update claim. Please try again.");
        }
    }
    public void getDependentClaims() {
        for (Claim claim : operations.getDependentClaims()) {
            System.out.println(claim);
        }
    }
    public void getClaimsOfDependent() {
        System.out.println("Enter the cID of the dependent to view their claims: ");
        String dependentCID = scanner.nextLine().trim();

        List<Claim> dependentClaims = operations.getClaimsOfDependent(dependentCID);
        if (dependentClaims == null) {
            System.out.println("Dependent has no claims.");
        }
        for (Claim claim: dependentClaims) {
            System.out.println(claim);
        }
    }
    public void displayDependentClaimMenu() {

        while (true) {
            System.out.println();
            System.out.println("1. Add Claim");
            System.out.println("2. Retrieve Claim by fID of Dependent");
            System.out.println("3. Retrieve All Claims of Dependent");
            System.out.println("4. Retrieve All Claims");
            System.out.println("5. Update Claim");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addClaimForDependent();
                    case 2 -> retrieveClaimByFIDForDependent();
                    case 3 -> getClaimsOfDependent();
                    case 4 -> getDependentClaims();
                    case 5 -> updateClaimForDependent();
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
            System.out.println("3. CRUD for Claims");
            System.out.println("4. CRUD for Dependent Claims");
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
