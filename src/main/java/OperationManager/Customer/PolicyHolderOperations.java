package OperationManager.Customer;

import Database.ClaimCRUD;
import Database.CustomerCRUD;
import Database.DatabaseConnection;
import Database.InsuranceCardCRUD;
import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Claim.Document;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.InsuranceCard.InsuranceCard;
import OperationManager.Utils.InputChecker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolicyHolderOperations implements UserInfoDAO, CustomerClaimDAO {

    public PolicyHolder policyHolder;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private CustomerCRUD customerCRUD = new CustomerCRUD(databaseConnection);
    private InsuranceCardCRUD insuranceCardCRUD = new InsuranceCardCRUD(databaseConnection);
    private ClaimCRUD claimCRUD = new ClaimCRUD(databaseConnection);
    private final Scanner scanner = new Scanner(System.in);
    public PolicyHolderOperations(PolicyHolder policyHolder) {
        this.policyHolder = policyHolder;
    }

    // methods relating to dependents
    public void addDependent() {

        System.out.println("Enter a valid cID (c-xxxxxxx): ");
        String cID = scanner.nextLine();
        if (!InputChecker.isValidCIDFormat(cID)) {
            System.out.println("Invalid customer ID format.");
            return;
        }
        // check if cID already exists

        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter phone number: ");
        String phone = scanner.nextLine();
        if (!InputChecker.isValidPhoneNumber(phone)) {
            System.out.println("Invalid phone number format.");
            return;
        }

        System.out.println("Enter address: ");
        String address = scanner.nextLine();

        System.out.println("Enter email: ");
        String email = scanner.nextLine();
        if (!InputChecker.isValidEmailAddress(email)) {
            System.out.println("Invalid email format.");
            return;
        }

        System.out.println("Enter password: ");
        String enteredPassword = scanner.nextLine();
        // convert password into hashed
        String password = enteredPassword;


        System.out.println("=== Create Insurance Card ===");
        System.out.println("Enter a 10-digit card number: ");
        String cardNumber = scanner.nextLine();

        if (!InputChecker.isValidCardFormat(cardNumber)) {
            System.out.println("Invalid card number format.");
            return;
        }
        // check for existing insurance card

        System.out.println("Enter the expiration date (yyyy-MM-dd):");
        LocalDate expirationDate;
        try {
            expirationDate = LocalDate.parse(scanner.nextLine());
            if (expirationDate.isBefore(LocalDate.now())) {
                System.out.println("Expiration date cannot be in the past.");
                return;
            }
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date input.");
            return;
        }

        Dependent dependent = new Dependent(cID, fullName, phone, address, email, password, policyHolder.getPolicyOwner(), policyHolder.getCID());
        InsuranceCard insuranceCard = new InsuranceCard(cardNumber, dependent.getCID(), dependent.getPolicyOwner(), expirationDate);
        dependent.setInsuranceCardNumber(insuranceCard.getCardNumber());
        policyHolder.addToDependentList(dependent);

        System.out.println(dependent);  // output test
        System.out.println(insuranceCard);  // output test
        System.out.println(policyHolder);   // output test

        policyHolder.addActionHistory(LocalDate.now() + ": add Dependent " + dependent.getCID());
        System.out.println(policyHolder.getActionHistory());
    }
    public void removeDependent(Dependent dependent) {
        return;
    }
    public Dependent getDependentInfo(String dependentCID) {
        // First, retrieve the list of dependents for the policyholder
        List<String> dependentList = policyHolder.getDependentList();

        // Check if the provided dependent CID is in the list of dependents
        if (!dependentList.contains(dependentCID)) {
            System.out.println("The provided CID does not belong to any of your dependents.");
            return null;
        }

        // Use the CRUD operation to fetch the dependent information
        return customerCRUD.readDependent(dependentCID);
    }
    public List<Dependent> getAllDependentsInfo() {
        // First, retrieve the list of dependents for the policyholder
        List<String> dependentList = policyHolder.getDependentList();

        // Create a list to store all dependent information
        List<Dependent> allDependentsInfo = new ArrayList<>();

        // Use the CRUD operation to fetch information for each dependent in the list
        for (String dependentCID : dependentList) {
            Dependent dependent = customerCRUD.readDependent(dependentCID);
            if (dependent != null) {
                allDependentsInfo.add(dependent);
            }
        }

        return allDependentsInfo;
    }


    // methods relating to claims
    @Override
    public boolean addClaim(String insuranceCardNumber) {

        InsuranceCard insuranceCard = insuranceCardCRUD.readInsuranceCard(insuranceCardNumber);
        if (insuranceCard == null) {
            return false;
        }

        System.out.println("Enter fID (f-xxxxxxx): ");
        String fID = scanner.nextLine();
        if (!InputChecker.isValidFIDFormat(fID)) {
            System.out.println("Invalid format.");
            return false;
        }

        System.out.println("Enter the exam date (yyyy-mm-dd): ");
        LocalDate examDate;
        try {
            examDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return false;
        }
        if (examDate.isAfter(LocalDate.now())) {
            System.out.println("The exam date cannot be in the future.");
            return false;
        }

        System.out.println("Enter the claim amount (USD xx.xx):");
        BigDecimal claimAmount;
        try {
            claimAmount = new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid claim amount format.");
            return false;
        }

        Claim claim = new Claim(fID, insuranceCard.getCardHolder(), insuranceCard, examDate, claimAmount, ClaimStatus.NEW);

        System.out.println("Enter receiver banking information in the following format:");
        System.out.println("bank, name, number");

        try {
            String input = scanner.nextLine();
            String[] parts = input.split(",");
            if (parts.length < 3) {
                System.out.println("Invalid input. Please make sure you provide all required information.");
                return false;
            }
            String bank = parts[0].trim();
            String name = parts[1].trim();
            String number = parts[2].trim();

            claim.setReceiverBankingInfo(bank, name, number);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid input. Please make sure you provide all required information.");
            return false;
        }

        System.out.println("Add document list: ");
        boolean addDocument = true;
        while (addDocument) {
            System.out.println("Enter document id (d-xxxxxxx): ");
            String documentID = scanner.nextLine().trim();

            if (documentID.equalsIgnoreCase("x")) {
                addDocument = false;
            }

            System.out.println("Enter document name: ");
            String fileName = scanner.nextLine().trim();

            claim.addDocument(new Document(documentID, fID, fileName));
            if (claimCRUD.createDocument(new Document(documentID, fID, fileName))) {
                System.out.println("Document added. Press 'x' to stop adding.");
            } else {
                System.out.println("Error adding document.");
            }
        }

        boolean createClaimSuccessfully = claimCRUD.createClaim(claim);

        if (createClaimSuccessfully) {
            customerCRUD.addToClaimList(insuranceCard.getCardHolder(), claim.getFID());
            customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(),LocalDate.now() + ": add Claim " + fID + " to Insurance Card " + insuranceCardNumber + " holder");
            return true;
        }
        return false;
    }
    public boolean addClaimForDependent(String dependentCID) {

        // Check if the dependent CID belongs to the policyholder's dependent list
        if (!claimCRUD.checkDependent(policyHolder.getCID(), dependentCID)) {
            System.out.println("Invalid dependent CID.");
            return false;
        }

        Dependent dependent = customerCRUD.readDependent(dependentCID);
        InsuranceCard insuranceCard = insuranceCardCRUD.readInsuranceCard(dependent.getInsuranceCardNumber());
        if (insuranceCard == null) {
            return false;
        }

        System.out.println("Enter fID (f-xxxxxxx): ");
        String fID = scanner.nextLine();
        if (!InputChecker.isValidFIDFormat(fID)) {
            System.out.println("Invalid format.");
            return false;
        }

        System.out.println("Enter the exam date (yyyy-mm-dd): ");
        LocalDate examDate;
        try {
            examDate = LocalDate.parse(scanner.nextLine());
        } catch (DateTimeParseException e) {
            System.out.println("Invalid date format.");
            return false;
        }
        if (examDate.isAfter(LocalDate.now())) {
            System.out.println("The exam date cannot be in the future.");
            return false;
        }

        System.out.println("Enter the claim amount (USD xx.xx):");
        BigDecimal claimAmount;
        try {
            claimAmount = new BigDecimal(scanner.nextLine());
        } catch (NumberFormatException e) {
            System.out.println("Invalid claim amount format.");
            return false;
        }

        Claim claim = new Claim(fID, insuranceCard.getCardHolder(), insuranceCard, examDate, claimAmount, ClaimStatus.NEW);

        System.out.println("Enter receiver banking information in the following format:");
        System.out.println("bank, name, number");

        try {
            String input = scanner.nextLine();
            String[] parts = input.split(",");
            if (parts.length < 3) {
                System.out.println("Invalid input. Please make sure you provide all required information.");
                return false;
            }
            String bank = parts[0].trim();
            String name = parts[1].trim();
            String number = parts[2].trim();

            claim.setReceiverBankingInfo(bank, name, number);

        } catch (ArrayIndexOutOfBoundsException e) {
            System.out.println("Invalid input. Please make sure you provide all required information.");
            return false;
        }

        System.out.println("Add document list: ");
        boolean addDocument = true;
        while (addDocument) {
            System.out.println("Enter document id (d-xxxxxxx): ");
            String documentID = scanner.nextLine().trim();

            if (documentID.equalsIgnoreCase("x")) {
                addDocument = false;
            }

            System.out.println("Enter document name: ");
            String fileName = scanner.nextLine().trim();

            claim.addDocument(new Document(documentID, fID, fileName));
            if (claimCRUD.createDocument(new Document(documentID, fID, fileName))) {
                System.out.println("Document added. Press 'x' to stop adding.");
            } else {
                System.out.println("Error adding document.");
            }
        }

        boolean createClaimSuccessfully = claimCRUD.createClaim(claim);

        if (createClaimSuccessfully) {
            customerCRUD.addToClaimList(insuranceCard.getCardHolder(), claim.getFID());
            customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(),LocalDate.now() + ": add Claim " + fID + " to Insurance Card " + dependent.getInsuranceCardNumber() + " holder");
            return true;
        }
        return false;
    }
    @Override
    public Claim getClaimByID(String fID) {
        Claim claim = claimCRUD.readClaim(fID);
        if (claim != null && claim.getInsuredPerson().equals(policyHolder.getCID())) {
            customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + ": retrieve Claim " + claim.getFID());
            return claim;
        }
        System.out.println("No claim found.");
        return null;
    }

    @Override
    public List<Claim> getAllClaims() {
        List<Claim> allClaims = new ArrayList<>();
        allClaims = claimCRUD.getClaimsByCustomerID(policyHolder.getCID());
        customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + ": retrieve all claims");
        return allClaims;
    }
    public boolean checkDependent(String policyHolderCID, String dependentCID) {
        return claimCRUD.checkDependent(policyHolderCID, dependentCID);
    }
    public boolean checkClaimOwner(String dependentCID, String claimFID) {
        return claimCRUD.checkClaimOwner(dependentCID, claimFID);
    }

    @Override
    public boolean updateClaim(String claimFID, List<Document> newDocuments, String newReceiverBankingInfo) {
        // Retrieve the claim from the database
        Claim claim = claimCRUD.readClaim(claimFID);

        if (claim == null) {
            System.out.println("Claim with FID " + claimFID + " not found.");
            return false;
        }

        // Append new documents to the existing list if provided
        if (newDocuments != null && !newDocuments.isEmpty()) {
            List<Document> currentDocuments = claim.getDocumentList();
            currentDocuments.addAll(newDocuments);
            claim.setDocumentList(currentDocuments);
        }

        // Update receiver banking info if provided
        if (newReceiverBankingInfo != null && !newReceiverBankingInfo.isEmpty()) {
            claim.setReceiverBankingInfo(newReceiverBankingInfo);
        }

        // Update the claim in the database
        return claimCRUD.updateClaim(claim);
    }

    public List<Claim> getDependentClaims() {
        PolicyHolder pH = customerCRUD.getPolicyHolder(policyHolder.getCID());
        if (pH == null) {
            System.out.println("PolicyHolder not found with cID: " + policyHolder.getCID());
            return new ArrayList<>();
        }

        List<String> dependentIDs = policyHolder.getDependentList();
        List<Claim> dependentClaims = new ArrayList<>();

        for (String dependentID : dependentIDs) {
            Dependent dependent = customerCRUD.readDependent(dependentID);
            if (dependent != null) {
                dependentClaims.addAll(dependent.getClaimList());
            }
        }

        return dependentClaims;
    }
    public List<Claim> getClaimsOfDependent(String dependentCID) {

        if (!policyHolder.getDependentList().contains(dependentCID)) {
            System.out.println("Dependent with cID: " + dependentCID + " is not associated with the PolicyHolder.");
            return new ArrayList<>();
        }

        Dependent dependent = customerCRUD.readDependent(dependentCID);
        if (dependent == null) {
            System.out.println("Dependent not found with cID: " + dependentCID);
            return new ArrayList<>();
        }

        return dependent.getClaimList();
    }



    @Override
    public boolean deleteClaim(String fID) {
        return false;   // not available to policyholders
    }

    // methods relating to user information
    @Override
    public void displayInfo() {
        PolicyHolder policyHolderInfo = customerCRUD.getPolicyHolder(policyHolder.getCID());
        System.out.println(policyHolderInfo);
        customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + "retrieve information");
    }

    @Override
    public boolean updatePhone(String newPhone) {
        return customerCRUD.updateCustomerContactInfo("policy_holder", policyHolder.getCID(), newPhone, "phone");
    }
    public void updateActionHistory(String action) {
        customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + action);
    }
    public void updateDependentActionHistory(String cID, String action) {
        customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + action + " for Dependent " + cID);
    }
    public boolean updateDependentPhone(String cID,String newPhone) {
        return customerCRUD.updateCustomerContactInfo("dependent", cID, newPhone, "phone");
    }

    @Override
    public boolean updateAddress(String newAddress) {
        return customerCRUD.updateCustomerContactInfo("policy_holder", policyHolder.getCID(), newAddress, "address");
    }
    public boolean updateDependentAddress(String cID, String newAddress) {
        return customerCRUD.updateCustomerContactInfo("dependent", cID, newAddress, "address");
    }

    @Override
    public boolean updateEmail(String newEmail) {
        return customerCRUD.updateCustomerContactInfo("policy_holder", policyHolder.getCID(), newEmail, "email");
    }
    public boolean updateDependentEmail(String cID, String newEmail) {
        return customerCRUD.updateCustomerContactInfo("dependent", cID, newEmail, "email");
    }

    @Override
    public boolean updatePassword() {
        // Ask for current password
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        // Retrieve the hashed password from the database based on the username
        String hashedPasswordFromDB = customerCRUD.getHashedPasswordFromDB(policyHolder.getCID());

        // Check if the current password matches the hashed password from the database
        if (!InputChecker.checkPassword(currentPassword, hashedPasswordFromDB)) {
            System.out.println("Invalid current password. Password not updated.");
            return false;
        }

        // Ask for new password
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        // Hash the new password before storing it in the database
        String hashedNewPassword = InputChecker.hashPassword(newPassword);
        customerCRUD.updatePasswordInDB(policyHolder.getCID(), hashedNewPassword);

        System.out.println("Password updated successfully.");
        customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + ": update password");
        return false;
    }
    public void updateDependentPassword() {
        // First, retrieve the list of dependents for the policyholder
        List<String> dependentList = policyHolder.getDependentList();

        // Ask for the dependent's CID
        System.out.println("Enter the dependent's CID: ");
        String dependentCID = scanner.nextLine().trim();

        // Check if the provided dependent CID is in the list of dependents
        if (!dependentList.contains(dependentCID)) {
            System.out.println("The provided CID does not belong to any of your dependents.");
            return;
        }

        // Ask for current password
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        // Retrieve the hashed password from the database based on the dependent's CID
        String hashedPasswordFromDB = customerCRUD.getHashedPasswordFromDB(dependentCID);

        // Check if the current password matches the hashed password from the database
        if (!InputChecker.checkPassword(currentPassword, hashedPasswordFromDB)) {
            System.out.println("Invalid current password. Password not updated.");
            return;
        }

        // Ask for new password
        System.out.print("Enter new password: ");
        String newPassword = scanner.nextLine();

        // Hash the new password before storing it in the database
        String hashedNewPassword = InputChecker.hashPassword(newPassword);
        customerCRUD.updatePasswordInDB(dependentCID, hashedNewPassword);

        System.out.println("Password updated successfully for the dependent.");
        customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + ": update password for Dependent " + dependentCID);
        customerCRUD.updateDependentActionHistory(dependentCID, LocalDate.now() + ": update password by Policy Holder");
    }

}
