package ViewManager.SystemAdmin;

import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import OperationManager.SystemAdmin.SystemAdminOperations;
import OperationManager.Utils.InputChecker;

import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Scanner;

public class SystemAdminView {
    static SystemAdminOperations operations = new SystemAdminOperations();
    static Scanner scanner = new Scanner(System.in);

    public static void addAPolicyOwner() {
        operations.addPolicyOwner();
    }
    public void retrieveAPolicyOwner() {

        System.out.println("Enter cID: ");
        String cID = scanner.nextLine();
        PolicyOwner policyOwner = operations.getPolicyOwner(cID);
        if (policyOwner != null) {
            System.out.println(policyOwner);
        }
    }
    public void deleteAPolicyOwner() {

        System.out.println("Enter the cID to delete:");
        String cID = scanner.nextLine();
        System.out.println("Are you sure you want to delete Policy Owner with ID " + cID + "? (yes/no)");
        String confirmation = scanner.nextLine();
        if (confirmation.equalsIgnoreCase("yes")) {
            operations.deletePolicyOwner(cID);
            System.out.println("Policy Owner with ID " + cID + " deleted successfully.");
        } else {
            System.out.println("Deletion canceled.");
        }
    }
    public static void addAPolicyHolder() {

        System.out.println();
        System.out.println("Enter a Policy Owner cID: ");
        String policyOwnerCID = scanner.nextLine();
        // find existing policy owner, print error message and return if not found
        // test policyOwner
        PolicyOwner policyOwner = new PolicyOwner(policyOwnerCID, "owner_tester", "090909090","Canada", "tester_owner@gmail.com", "popoipoiu");

        operations.addPolicyHolder(policyOwner);
    }
    public static void addADependent() {

        System.out.println();
        System.out.println("Enter a Policy Owner cID: ");
        String policyOwnerCID = scanner.nextLine();
        // find existing policy owner, print error message and return if not found
        // test policyOwner
        PolicyOwner policyOwner = new PolicyOwner(policyOwnerCID, "ownertester", "090909090","jijijiji", "testerowner@gmail.com", "popoipoiu");  // test

        System.out.println("Enter a Policy Holder cID: ");
        String policyHolderCID = scanner.nextLine();
        // find if policyHolder exist in policyOwner beneficiaries
        // test policyHolder
        PolicyHolder policyHolder = new PolicyHolder(policyHolderCID);


        operations.addDependent(policyOwner, policyHolder);
    }
    public static void addAnInsuranceManager() {
        operations.addInsuranceManager();
    }
    public static void retrieveAnInsuranceManager() {

        System.out.println("Enter a Insurance Manager pID that you want to retrieve: ");
        String insuranceManagerPID = scanner.nextLine();
        try {
            InsuranceManager insuranceManager = operations.getInsuranceManager(insuranceManagerPID);
            if (!(insuranceManager == null)) {
                System.out.println(insuranceManager);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    public static void deleteAnInsuranceManager() {
        System.out.println("Enter the pID of the Insurance Manager to delete:");
        String pID = scanner.nextLine();

        // First confirmation
        System.out.println("Are you sure you want to delete Insurance Manager with pID " + pID + "? (yes/no)");
        String firstConfirmation = scanner.nextLine();

        if (!firstConfirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        // Second confirmation
        System.out.println("This action will also delete all associated Insurance Surveyors. Are you absolutely sure? (yes/no)");
        String secondConfirmation = scanner.nextLine();

        if (!secondConfirmation.equalsIgnoreCase("yes")) {
            System.out.println("Deletion cancelled.");
            return;
        }

        try {
            operations.deleteInsuranceManager(pID);
            System.out.println("Insurance Manager and all associated Insurance Surveyors deleted successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public static void addAnInsuranceSurveyor() {

        System.out.println("Enter a Insurance Manager pID: ");
        String insuranceManagerPID = scanner.nextLine();
        // find existing insuranceManager, print error message and return if not found
        // test insuranceManager
        InsuranceManager insuranceManager = new InsuranceManager(insuranceManagerPID, "InsuranceManager", "HUiHUI", "popopo");

        operations.addInsuranceSurveyor(insuranceManager);
    }
    public static void retrieveAnInsuranceSurveyor() {

        System.out.println("Enter a Insurance Surveyor pID that you want to retrieve: ");
        String insuranceSurveyorPID = scanner.nextLine();
        try {
            InsuranceSurveyor insuranceSurveyor = operations.getInsuranceSurveyor(insuranceSurveyorPID);
            if (!(insuranceSurveyor == null)) {
                System.out.println(insuranceSurveyor);
            }
        } catch (RuntimeException e) {
            e.printStackTrace();
        }
    }
    public static void deleteAnInsuranceSurveyor() {

        System.out.println("Enter the pID of the Insurance Surveyor to delete:");
        String pID = scanner.nextLine();
        try {
            operations.deleteInsuranceSurveyor(pID);
            System.out.println("Insurance Surveyor deleted successfully!");
        } catch (RuntimeException e) {
            System.out.println("Error: " + e.getMessage());
        }
    }
    public void updatePolicyOwner() {

        System.out.println("Enter Policy Owner ID to update:");
        String cID = scanner.nextLine();
        if (!InputChecker.isValidCIDFormat(cID)) {
            System.out.println("Invalid customer ID format.");
            return;
        }

        System.out.println("Enter new phone number:");
        String phone = scanner.nextLine();
        if (!InputChecker.isValidPhoneNumber(phone)) {
            System.out.println("Invalid phone number format.");
            return;
        }

        System.out.println("Enter new address:");
        String address = scanner.nextLine();
        System.out.println("Enter new email:");
        String email = scanner.nextLine();
        if (!InputChecker.isValidEmailAddress(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        System.out.println("Enter new password:");
        String password = scanner.nextLine();

        operations.updatePolicyOwner(cID, phone, address, email, password);
        System.out.println("Policy Owner updated successfully!");

    }
    public void updateInsuranceManager() {

        System.out.println("Enter Insurance Manager ID to update:");
        String pID = scanner.nextLine();
        if (!InputChecker.isValidPIDFormat(pID)) {
            System.out.println("Invalid provider ID format.");
            return;
        }
        System.out.println("Enter new password:");
        String newPassword = scanner.nextLine();

        try {
            operations.updateInsuranceManager(pID, newPassword);
            System.out.println("Insurance Manager updated successfully!");
        } catch (SQLException e) {
            if (e.getMessage().contains("No Insurance Manager found with pID")) {
                System.out.println(e.getMessage());
            } else {
                System.out.println("Error updating Insurance Manager: " + e.getMessage());
            }
        }
    }
    public void updateInsuranceSurveyor() {

        System.out.println("Enter Insurance Surveyor ID to update:");
        String pID = scanner.nextLine();
        if (!InputChecker.isValidPIDFormat(pID)) {
            System.out.println("Invalid provider ID format.");
            return;
        }
        System.out.println("Enter new password:");
        String newPassword = scanner.nextLine();

        try {
            operations.updateInsuranceSurveyor(pID, newPassword);
            System.out.println("Insurance Surveyor updated successfully!");
        } catch (SQLException e) {
            if (e.getMessage().contains("No Insurance Surveyor found with pID")) {
                System.out.println(e.getMessage());
            } else {
                System.out.println("Error updating Insurance Surveyor: " + e.getMessage());
            }
        }
    }
    public void retrieveAPolicyHolder() {

        System.out.println("Enter cID: ");
        String cID = scanner.nextLine();
        PolicyHolder policyHolder = operations.getPolicyHolder(cID);
        if (policyHolder != null) {
            System.out.println(policyHolder);
        }
    }
    public void retrieveADependent() {
        System.out.println("Enter cID: ");
        String cID = scanner.nextLine();
        Dependent dependent = operations.getDependent(cID);
        if (dependent != null) {
            System.out.println(dependent);
        }
    }
    public void updatePolicyHolder() {
        System.out.println("Enter Policy Holder ID to update:");
        String cID = scanner.nextLine();
        if (!InputChecker.isValidCIDFormat(cID)) {
            System.out.println("Invalid customer ID format.");
            return;
        }

        System.out.println("Enter new phone number:");
        String phone = scanner.nextLine();
        if (!InputChecker.isValidPhoneNumber(phone)) {
            System.out.println("Invalid phone number format.");
            return;
        }

        System.out.println("Enter new address:");
        String address = scanner.nextLine();
        System.out.println("Enter new email:");
        String email = scanner.nextLine();
        if (!InputChecker.isValidEmailAddress(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        System.out.println("Enter new password:");
        String password = scanner.nextLine();

        operations.updatePolicyHolder(cID, phone, address, email, password);
        System.out.println("Policy Holder updated successfully!");
    }
    public void updateDependent() {
        System.out.println("Enter Dependent ID to update:");
        String cID = scanner.nextLine();
        if (!InputChecker.isValidCIDFormat(cID)) {
            System.out.println("Invalid customer ID format.");
            return;
        }

        System.out.println("Enter new phone number:");
        String phone = scanner.nextLine();
        if (!InputChecker.isValidPhoneNumber(phone)) {
            System.out.println("Invalid phone number format.");
            return;
        }

        System.out.println("Enter new address:");
        String address = scanner.nextLine();
        System.out.println("Enter new email:");
        String email = scanner.nextLine();
        if (!InputChecker.isValidEmailAddress(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        System.out.println("Enter new password:");
        String password = scanner.nextLine();

        operations.updateDependent(cID, phone, address, email, password);
        System.out.println("Dependent updated successfully!");
    }
    public void deleteAPolicyHolder() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the cID of the Policy Holder to delete:");
        String cID = scanner.nextLine().trim();

        // Confirm deletion with the user
        System.out.println("Are you sure you want to delete the Policy Holder with cID " + cID + "? (yes/no)");
        String confirmation = scanner.nextLine().trim().toLowerCase();

        if (confirmation.equalsIgnoreCase("yes")) {
            operations.deletePolicyHolder(cID);
        } else {
            System.out.println("Deletion cancelled.");
        }
    }
    public void deleteADependent() {
        Scanner scanner = new Scanner(System.in);
        System.out.println("Enter the cID of the Dependent to delete:");
        String cID = scanner.nextLine().trim();

        operations.deleteDependent(cID);
    }
    public void displayAdminMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== System Admin ===");
            System.out.println("1. Add a Policy Owner");
            System.out.println("2. Add a Policy Holder");
            System.out.println("3. Add a Dependent");
            System.out.println("4. Add an Insurance Manager");
            System.out.println("5. Add an Insurance Surveyor");
            System.out.println("6. Retrieve a Policy Owner");
            System.out.println("7. Retrieve an Insurance Manager");
            System.out.println("8. Retrieve an Insurance Surveyor");
            System.out.println("9. Delete an Insurance Manager");
            System.out.println("10. Delete an Insurance Surveyor");
            System.out.println("11. Delete a Policy Owner");
            System.out.println("12. Update a Policy Owner");
            System.out.println("13. Update an Insurance Manager");
            System.out.println("14. Update an Insurance Surveyor");
            System.out.println("15. Retrieve a Policy Holder");
            System.out.println("16. Retrieve a Dependent");
            System.out.println("17. Update a Policy Holder");
            System.out.println("18. Update a Dependent");
            System.out.println("19. Delete a Policy Holder");
            System.out.println("20. Delete a Dependent");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addAPolicyOwner();
                    case 2 -> addAPolicyHolder();
                    case 3 -> addADependent();
                    case 4 -> addAnInsuranceManager();
                    case 5 -> addAnInsuranceSurveyor();
                    case 6 -> retrieveAPolicyOwner();
                    case 7 -> retrieveAnInsuranceManager();
                    case 8 -> retrieveAnInsuranceSurveyor();
                    case 9 -> deleteAnInsuranceManager();
                    case 10 -> deleteAnInsuranceSurveyor();
                    case 11 -> deleteAPolicyOwner();
                    case 12 -> updatePolicyOwner();
                    case 13 -> updateInsuranceManager();
                    case 14 -> updateInsuranceSurveyor();
                    case 15 -> retrieveAPolicyHolder();
                    case 16 -> retrieveADependent();
                    case 17 -> updatePolicyHolder();
                    case 18 -> updateDependent();
                    case 19 -> deleteAPolicyHolder();
                    case 20 -> deleteADependent();
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
