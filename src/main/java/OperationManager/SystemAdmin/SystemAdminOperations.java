package OperationManager.SystemAdmin;

import Database.*;
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.InsuranceCard.InsuranceCard;
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import Models.Provider.Provider;
import Models.SystemAdmin.SystemAdmin;
import OperationManager.Utils.InputChecker;

import java.math.BigDecimal;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Scanner;

public class SystemAdminOperations {
    private final SystemAdmin systemAdmin = new SystemAdmin();
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private ProviderCRUD providerCRUD = new ProviderCRUD(databaseConnection);
    private CustomerCRUD customerCRUD = new CustomerCRUD(databaseConnection);
    private InsuranceCardCRUD insuranceCardCRUD = new InsuranceCardCRUD(databaseConnection);
    private SystemAdminCRUD systemAdminCRUD = new SystemAdminCRUD(databaseConnection);
    private final Scanner scanner = new Scanner(System.in);

    // method to update password
    public void updateAdminPassword(String password) {

    }

    // method to sum successfully claimed amount with different filtering options
    public void sumClaimAmount() {

    }

    // CRUD for policy owners
    public Customer addCustomer(String role) {
        System.out.println("Enter a valid cID (c-xxxxxxx): ");
        String cID = scanner.nextLine();
        if (!InputChecker.isValidCIDFormat(cID)) {
            System.out.println("Invalid customer ID format.");
            return null;
        }

        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter phone number: ");
        String phone = scanner.nextLine();
        if (!InputChecker.isValidPhoneNumber(phone)) {
            System.out.println("Invalid phone number format.");
            return null;
        }

        System.out.println("Enter address: ");
        String address = scanner.nextLine();

        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        if (!InputChecker.isValidEmailAddress(email)) {
            System.out.println("Invalid email format.");
            return null;
        }

        System.out.println("Enter password: ");
        String enteredPassword = scanner.nextLine();
        // convert password into hashed
        String password = enteredPassword;

        if (role.equalsIgnoreCase("policyowner")) {
            return new PolicyOwner(cID, fullName, phone, address, email, password);
        } else if (role.equalsIgnoreCase("policyholder")) {
            return new PolicyHolder(cID, fullName, phone, address, email, password);
        } else if (role.equalsIgnoreCase("dependent")) {
            return new Dependent(cID, fullName, phone, address, email, password);
        }
        return null;
    }
    public InsuranceCard addInsuranceCard(PolicyOwner policyOwner) {

        System.out.println("=== Create Insurance Card ===");
        System.out.println("Enter a 10-digit card number: ");
        String cardNumber = scanner.nextLine();

        if (!InputChecker.isValidCardFormat(cardNumber)) {
            System.out.println("Invalid card number format.");
            return null;
        }
        // check for existing insurance card

        System.out.println("Enter the expiration date (yyyy-MM-dd):");
        LocalDate expirationDate;
        try {
            expirationDate = LocalDate.parse(scanner.nextLine());
            if (expirationDate.isBefore(LocalDate.now())) {
                System.out.println("Expiration date cannot be in the past.");
                return null;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date input.");
            return null;
        }

        return new InsuranceCard(cardNumber, policyOwner.getCID(), expirationDate);
    }
    public void addPolicyOwner() {

        PolicyOwner newPolicyOwner = (PolicyOwner) addCustomer("PolicyOwner");
        if (newPolicyOwner == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newPolicyOwner.setClaimList(null);

        System.out.println("Enter insurance fee (USD, use '.' to separate decimals): ");
        BigDecimal insuranceFee = scanner.nextBigDecimal();
        newPolicyOwner.setInsuranceFee(insuranceFee);

        boolean createdSuccessfully = customerCRUD.createPolicyOwner(newPolicyOwner);
        if (createdSuccessfully) {
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": add Policy Owner " + newPolicyOwner.getCID());
        }
    }
    public PolicyOwner getPolicyOwner(String cID) {
        try {
            PolicyOwner policyOwner = customerCRUD.readPolicyOwner(cID);
            if (policyOwner != null) {
                systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": retrieve Policy Owner " + cID);
            }
            return policyOwner;
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Policy Owner from database", e);
        }
    }
    public void updatePolicyOwner(String cID, String phone, String address, String email, String password) {
        try {
            // Check if the policy owner exists
            PolicyOwner policyOwner = customerCRUD.readPolicyOwner(cID);
            if (policyOwner == null) {
                System.out.println("Policy Owner with ID " + cID + " does not exist.");
                return;
            }

            // Update the policy owner fields
            customerCRUD.updatePolicyOwner(cID, phone, address, email, password);

            // Update admin action history
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": update Policy Owner " + cID);

            System.out.println("Policy Owner with ID " + cID + " updated successfully.");
        } catch (SQLException e) {
            throw new RuntimeException("Error updating Policy Owner", e);
        }
    }

    public void deletePolicyOwner(String cID) {
        try {
            PolicyOwner policyOwner = customerCRUD.readPolicyOwner(cID);
            if (policyOwner != null) {
                // Delete the policy owner
                customerCRUD.deletePolicyOwner(cID);
                // Update admin action history
                systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": delete Policy Owner " + cID);
            } else {
                System.out.println("Policy Owner with ID " + cID + " does not exist.");
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Policy Owner", e);
        }
    }


    // CRUD for policyholders
    public void addPolicyHolder(PolicyOwner policyOwner) {

        PolicyHolder newPolicyHolder = (PolicyHolder) addCustomer("PolicyHolder");
        if (newPolicyHolder == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newPolicyHolder.setPolicyOwner(policyOwner.getCID());

        InsuranceCard newInsuranceCard = addInsuranceCard(policyOwner);
        newInsuranceCard.setCardHolder(newPolicyHolder.getCID());

        newPolicyHolder.setInsuranceCardNumber(newInsuranceCard.getCardNumber());
        boolean createHolderSuccessfully = customerCRUD.createPolicyHolder(newPolicyHolder);

        if (createHolderSuccessfully) {
            boolean createInsuranceCardSuccessfully = insuranceCardCRUD.createInsuranceCard(newInsuranceCard);
            if (createInsuranceCardSuccessfully) {
                customerCRUD.addToBeneficiaries(policyOwner.getCID(), newPolicyHolder.getCID());
                systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": add Policy Holder " + newPolicyHolder.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to Policy Owner " + policyOwner.getCID());
                customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": add Policy Holder " + newPolicyHolder.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to beneficiaries by System Admin");
                System.out.println("Policy Holder and Insurance Card added successfully!");
            }
        }
    }
    public PolicyHolder getPolicyHolder(String cID) {
        try {
            PolicyHolder policyHolder = customerCRUD.getPolicyHolder(cID);
            if (policyHolder != null) {
                systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": retrieve Policy Holder " + cID);
            }
            return policyHolder;
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Policy Holder from database", e);
        }
    }

    public void updatePolicyHolder(String cID) {
        // getPolicyHolder(cID)
    }
    public void deletePolicyHolder(String cID) {
        // getPolicyHOlder(cID)
    }

    // CRUD for dependents
    public void addDependent(PolicyOwner policyOwner, PolicyHolder policyHolder) {

        Dependent newDependent = (Dependent) addCustomer("Dependent");
        if (newDependent == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newDependent.setPolicyOwner(policyOwner.getCID());
        newDependent.setPolicyHolder(policyHolder.getCID());
        policyHolder.addToDependentList(newDependent);
        policyOwner.addToBeneficiaries(newDependent);

        InsuranceCard newInsuranceCard = addInsuranceCard(policyOwner);
        newInsuranceCard.setCardHolder(newDependent.getCID());
        newDependent.setInsuranceCardNumber(newInsuranceCard.getCardNumber());

        boolean createDependentSuccessfully = customerCRUD.createDependent(newDependent);

        if (createDependentSuccessfully) {
            boolean createInsuranceCardSuccessfully = insuranceCardCRUD.createInsuranceCard(newInsuranceCard);
            if (createInsuranceCardSuccessfully) {
                customerCRUD.addToDependentList(policyHolder.getCID(), newDependent.getCID());
                customerCRUD.addToBeneficiaries(policyOwner.getCID(), newDependent.getCID());
                systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": add Dependent " + newDependent.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to Policy Holder " + policyHolder.getCID());
                customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": add Dependent " + newDependent.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to Policy Holder " + policyHolder.getCID() + " to beneficiaries by System Admin");
                customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + ": add Dependent " + newDependent.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to dependent list by System Admin");
            }
        }
    }
    public void getDependent(String cID) {

    }
    public void updateDependent(String cID) {

    }
    public void deleteDependent(String cID) {

    }

    // CRUD for insurance managers
    public Provider addProvider(String role) {

        System.out.println("Enter a valid pID (p-xxxxxxx): ");
        String pID = scanner.nextLine();
        if (!InputChecker.isValidPIDFormat(pID)) {
            System.out.println("Invalid provider ID format.");
            return null;
        }

        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter password: ");
        String enteredPassword = scanner.nextLine();
        // convert password into hashed
        String password = enteredPassword;

        if (role.equalsIgnoreCase("insurancemanager")) {
            return new InsuranceManager(pID, "InsuranceManager", fullName, password);
        } else if (role.equalsIgnoreCase("insurancesurveyor")) {
            return new InsuranceSurveyor(pID, fullName, password);
        }
        return null;
    }
    public void addInsuranceManager() {

        InsuranceManager newInsuranceManager = (InsuranceManager) addProvider("InsuranceManager");
        if (newInsuranceManager == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }

        boolean createdSuccessfully = providerCRUD.createInsuranceManager(newInsuranceManager);
        if (createdSuccessfully) {
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": add Insurance Manager " + newInsuranceManager.getPID());
        }
    }
    public InsuranceManager getInsuranceManager(String pID) {
        try {
            InsuranceManager insuranceManager = providerCRUD.readInsuranceManager(pID);
            if (insuranceManager != null) {
                systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": retrieve Insurance Manager " + pID);
            }
            return insuranceManager;
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Insurance Manager from database", e);
        }
    }
    public void updateInsuranceManager(String pID, String newPassword) throws SQLException {
        try {
            providerCRUD.updateInsuranceManager(pID, newPassword);
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": updated Insurance Manager " + pID);
        } catch (SQLException e) {
            if (e.getMessage().contains("no rows affected")) {
                throw new SQLException("No Insurance Manager found with pID: " + pID, e);
            }
            throw e;
        }
    }
    public void deleteInsuranceManager(String pID) {
        try {
            providerCRUD.deleteInsuranceManager(pID);
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": delete Insurance Manager with pID " + pID + " with all associated Insurance Surveyors");
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Insurance Manager from database", e);
        }
    }

    // CRUD for insurance surveyors
    public void addInsuranceSurveyor(InsuranceManager insuranceManager) {

        InsuranceSurveyor newInsuranceSurveyor = (InsuranceSurveyor) addProvider("InsuranceSurveyor");
        if (newInsuranceSurveyor == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newInsuranceSurveyor.setInsuranceManager(insuranceManager.getPID());

        boolean createdSuccessfully = providerCRUD.createInsuranceSurveyor(newInsuranceSurveyor);
        if (createdSuccessfully) {
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": add Insurance Surveyor " + newInsuranceSurveyor.getPID() + " to Insurance Manager " + insuranceManager.getPID());
            providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": add Insurance Surveyor " + newInsuranceSurveyor.getPID() + " to surveyor list by System Admin");
        }

    }
    public InsuranceSurveyor getInsuranceSurveyor(String pID) {
        try {
            InsuranceSurveyor insuranceSurveyor = providerCRUD.readInsuranceSurveyor(pID);
            if (insuranceSurveyor != null) {
                systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": retrieve Insurance Surveyor " + pID);
            }
            return insuranceSurveyor;
        } catch (SQLException e) {
            throw new RuntimeException("Error reading Insurance Surveyor from database", e);
        }
    }
    public void updateInsuranceSurveyor(String pID, String newPassword) throws SQLException {
        try {
            providerCRUD.updateInsuranceSurveyor(pID, newPassword);
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": updated Insurance Surveyor " + pID);
        } catch (SQLException e) {
            if (e.getMessage().contains("no rows affected")) {
                throw new SQLException("No Insurance Surveyor found with pID: " + pID, e);
            }
            throw e;
        }
    }
    public void deleteInsuranceSurveyor(String pID) {
        try {
            providerCRUD.deleteInsuranceSurveyor(pID);
            systemAdminCRUD.updateAdminActionHistory("admin", LocalDate.now() + ": delete Insurance Surveyor " + pID);
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Insurance Surveyor from database", e);
        }
    }
}
