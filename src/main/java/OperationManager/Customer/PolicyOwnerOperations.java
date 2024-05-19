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
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.InsuranceCard.InsuranceCard;
import OperationManager.Utils.InputChecker;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class PolicyOwnerOperations implements UserInfoDAO, CustomerClaimDAO {

    public PolicyOwner policyOwner;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private CustomerCRUD customerCRUD = new CustomerCRUD(databaseConnection);
    private InsuranceCardCRUD insuranceCardCRUD = new InsuranceCardCRUD(databaseConnection);
    private ClaimCRUD claimCRUD = new ClaimCRUD(databaseConnection);
    private final Scanner scanner = new Scanner(System.in);

    public PolicyOwnerOperations(PolicyOwner policyOwner) {
        this.policyOwner = policyOwner;
    }

    // method to calculate yearly cost to pay for insurance providers
    public void calculateYearlyCost() {

    }

    // methods relating to beneficiaries
    public Customer addCustomer(String role) {
        System.out.println("Enter a valid cID (c-xxxxxxx): ");
        String cID = scanner.nextLine();
        if (!InputChecker.isValidCIDFormat(cID)) {
            System.out.println("Invalid customer ID format.");
            return null;
        }
        // check if cID already exists

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

        if (role.equalsIgnoreCase("policyholder")) {
            return new PolicyHolder(cID, fullName, phone, address, email, password, policyOwner.getCID());
        } else if (role.equalsIgnoreCase("dependent")) {
            return new Dependent(cID, fullName, phone, address, email, password, policyOwner.getCID());
        }
        return null;
    }
    public InsuranceCard addInsuranceCard() {

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
    public void addBeneficiary() {

        PolicyHolder policyHolder = (PolicyHolder) addCustomer("PolicyHolder");
        if (policyHolder == null) {
            return;
        }
        InsuranceCard insuranceCard = addInsuranceCard();
        if (insuranceCard == null) {
            return;
        }

        policyHolder.setInsuranceCardNumber(insuranceCard.getCardNumber());
        insuranceCard.setCardHolder(policyHolder.getCID());
        policyOwner.addToBeneficiaries(policyHolder);

        boolean createHolderSuccessfully = customerCRUD.createPolicyHolder(policyHolder);
        boolean createInsuranceCardSuccessfully = insuranceCardCRUD.createInsuranceCard(insuranceCard);

        if (createHolderSuccessfully && createInsuranceCardSuccessfully) {
            customerCRUD.addToBeneficiaries(policyOwner.getCID(), policyOwner.getCID());
            customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": add Policy Holder " + policyHolder.getCID() + " with Insurance Card " + insuranceCard.getCardNumber() + " to beneficiaries");
            System.out.println("Policy Holder and Insurance Card added successfully!");
        }

    }
    public void addBeneficiary(PolicyHolder policyHolder) {

        Dependent dependent = (Dependent) addCustomer("Dependent");
        if (dependent == null) {
            return;
        }
        InsuranceCard insuranceCard = addInsuranceCard();
        if (insuranceCard == null) {
            return;
        }

        dependent.setPolicyHolder(policyHolder.getCID());
        dependent.setInsuranceCardNumber(insuranceCard.getCardNumber());
        insuranceCard.setCardHolder(dependent.getCID());

        boolean createDependentSuccessfully = customerCRUD.createDependent(dependent);

        if (createDependentSuccessfully) {
            boolean createInsuranceCardSuccessfully = insuranceCardCRUD.createInsuranceCard(insuranceCard);
            if (createInsuranceCardSuccessfully) {
                customerCRUD.addToBeneficiaries(policyOwner.getCID(), dependent.getCID());
                customerCRUD.addToDependentList(policyHolder.getCID(), dependent.getCID());
                customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": add Dependent " + dependent.getCID() + " with Insurance Card " + insuranceCard.getCardNumber() + " to Policy Holder " + policyHolder.getCID() + " to beneficiaries");
                customerCRUD.updatePolicyHolderActionHistory(policyHolder.getCID(), LocalDate.now() + ": add Dependent " + dependent.getCID() + " with Insurance Card " + insuranceCard.getCardNumber() + " to dependent list by Policy Owner " + policyOwner.getCID());
            }
        }

    }
    public void getBeneficiary(Customer customer) {
        // displayInfo
    }
    public boolean updateBeneficiary(Customer customer) {
        return false;
    }
    public boolean removeBeneficiary(Customer customer) {
        return false;
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
            customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": add Claim " + fID + " to Insurance Card " + insuranceCardNumber + " holder");
            return true;
        }
        return false;
    }

    @Override
    public Claim getClaimByID(String fID) {
        Claim claim = claimCRUD.readClaim(fID);
        if (claim != null && claim.getCardNumber().getPolicyOwner().equals(policyOwner.getCID())) {
            customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": retrieve Claim " + claim.getFID());
            return claim;
        }
        System.out.println("No claim found.");
        return null;
    }
    public List<Claim> getAllBeneficiaryClaims(String beneficiaryCID) {
        // Check if the beneficiaryCID is in the list of beneficiaries
        if (policyOwner.getBeneficiaries().contains(beneficiaryCID)) {
            // If beneficiaryCID is in the list, retrieve their claims
            return claimCRUD.getClaimsByCustomerID(beneficiaryCID);
        } else {
            // If beneficiaryCID is not in the list, return an empty list
            System.out.println("Beneficiary not found in the list of beneficiaries.");
            return new ArrayList<>();
        }
    }

    @Override
    public List<Claim> getAllClaims() {
        List<Claim> allClaims = new ArrayList<>();
        for (String beneCID: policyOwner.getBeneficiaries()) {
            allClaims.addAll(claimCRUD.getClaimsByCustomerID(beneCID));
        }
        customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": retrieve all claims");
        return allClaims;
    }
    public List<Customer> getAllBeneficiaries() {
        // Get all beneficiaries associated with the policy owner from the database
        List<Customer> beneficiaries = new ArrayList<>();
        List<PolicyHolder> policyHolders = customerCRUD.getPolicyHoldersByOwner(policyOwner.getCID());
        List<Dependent> dependents = customerCRUD.getDependentsByOwner(policyOwner.getCID());

        beneficiaries.addAll(policyHolders);
        beneficiaries.addAll(dependents);

        return beneficiaries;
    }
    public Customer getBeneficiaryByID(String cID) {
        return customerCRUD.getBeneficiaryByID(cID);
    }
    public PolicyHolder retrievePolicyholderAndDependents(String cID) {
        Customer beneficiary = customerCRUD.getBeneficiaryByID(cID);
        if (beneficiary instanceof PolicyHolder) {
            return (PolicyHolder) beneficiary;
        }
        return null;
    }
    public Customer getCustomerByID(String cID) {
        return customerCRUD.getCustomerByID(cID, "dependent");
    }
    public boolean deleteBeneficiary(String policyOwnerCID, String beneficiaryCID) {
        PolicyOwner policyOwner = (PolicyOwner) customerCRUD.getCustomerByID(policyOwnerCID,"policy_owner");
        if (policyOwner != null) {
            List<String> beneficiaries = policyOwner.getBeneficiaries();
            if (beneficiaries.contains(beneficiaryCID)) {
                beneficiaries.remove(beneficiaryCID);
                policyOwner.setBeneficiaries(beneficiaries);
                customerCRUD.updateBeneficiaries(beneficiaries, policyOwnerCID, beneficiaryCID);
                return true;
            }
        }
        return false;
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
    public boolean updateBeneficiaryClaim(String beneficiaryCID, String claimFID, List<Document> newDocuments, String newReceiverBankingInfo) {
        // Check if the beneficiaryCID is in the list of beneficiaries
        if (policyOwner.getBeneficiaries().contains(beneficiaryCID)) {
            // If beneficiaryCID is in the list, retrieve their claims
            List<Claim> beneficiaryClaims = claimCRUD.getClaimsByCustomerID(beneficiaryCID);
            // Check if the claimFID is in the list of claims for the beneficiary
            for (Claim claim : beneficiaryClaims) {
                if (claim.getFID().equals(claimFID)) {
                    return updateClaim(claimFID, newDocuments, newReceiverBankingInfo);
                }
            }
            // If claimFID is not found for the beneficiary, print a message
            System.out.println("Claim with FID " + claimFID + " not found for beneficiary with CID " + beneficiaryCID);
            return false;
        } else {
            // If beneficiaryCID is not in the list, print a message
            System.out.println("Beneficiary with CID " + beneficiaryCID + " not found in the list of beneficiaries.");
            return false;
        }
    }
    @Override
    public boolean deleteClaim(String fID) {
        Claim claim = claimCRUD.readClaim(fID);
        if (claim != null && claim.getCardNumber().getPolicyOwner().equals(policyOwner.getCID())) {
            customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": delete Claim " + fID);
            return claimCRUD.deleteClaim(fID);
        }
        System.out.println("Claim does not exist.");
        return false;
    }

    // methods relating to user information
    @Override
    public void displayInfo() {
        PolicyOwner policyOwnerInfo = customerCRUD.readPolicyOwner(policyOwner.getCID());
        System.out.println(policyOwnerInfo);
        customerCRUD.updatePolicyHolderActionHistory(policyOwner.getCID(), LocalDate.now() + "retrieve information");
    }
    @Override
    public boolean updatePhone(String newPhone) {
        return customerCRUD.updateCustomerContactInfo("policy_owner", policyOwner.getCID(), newPhone, "phone");
    }
    public boolean updateBeneficiaryPhone(String cID, String newPhone) {
        return customerCRUD.updateCustomerContactInfo("customer", cID, newPhone, "phone");
    }

    @Override
    public boolean updateAddress(String newAddress) {
        return customerCRUD.updateCustomerContactInfo("policy_owner", policyOwner.getCID(), newAddress, "address");
    }
    public boolean updateBeneficiaryAddress(String cID, String newAddress) {
        return customerCRUD.updateCustomerContactInfo("policy_owner", cID, newAddress, "address");
    }

    @Override
    public boolean updateEmail(String newEmail) {
        return customerCRUD.updateCustomerContactInfo("policy_owner", policyOwner.getCID(), newEmail, "email");
    }
    public boolean updateBeneficiaryEmail(String cID, String newEmail) {
        return customerCRUD.updateCustomerContactInfo("policy_owner", cID, newEmail, "email");
    }
    public boolean updateBeneficiaryInfo(String beneficiaryCID, String phone, String address, String email) {
        if (updateBeneficiaryPhone(beneficiaryCID, phone) && updateBeneficiaryAddress(beneficiaryCID, address) && updateBeneficiaryEmail(beneficiaryCID, email)) {
            return true;
        }
        return false;
    }
    @Override
    public boolean updatePassword() {
        // Ask for current password
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        // Retrieve the hashed password from the database based on the username
        String hashedPasswordFromDB = customerCRUD.getHashedPasswordFromDB(policyOwner.getCID());

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
        customerCRUD.updatePasswordInDB(policyOwner.getCID(), hashedNewPassword);

        System.out.println("Password updated successfully.");
        customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": update password");
        return false;
    }
    public void updateBeneficiaryPassword() {
        // First, retrieve the list of beneficiaries for the policy owner
        List<String> beneficiaries = policyOwner.getBeneficiaries();

        // Ask for the dependent's CID
        System.out.println("Enter the beneficiary's CID: ");
        String beneficiaryCID = scanner.nextLine().trim();

        // Check if the provided dependent CID is in the list of dependents
        if (!beneficiaries.contains(beneficiaryCID)) {
            System.out.println("The provided CID does not belong to any of your beneficiaries.");
            return;
        }

        // Ask for current password
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        // Retrieve the hashed password from the database based on the dependent's CID
        String hashedPasswordFromDB = customerCRUD.getHashedPasswordFromDB(beneficiaryCID);

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
        customerCRUD.updatePasswordInDB(beneficiaryCID, hashedNewPassword);

        System.out.println("Password updated successfully for the beneficiary.");
        customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": update password for Beneficiary " + beneficiaryCID);
    }
    public void updateActionHistory(String action) {
        customerCRUD.updatePolicyHolderActionHistory(policyOwner.getCID(), LocalDate.now() + action);
    }
}
