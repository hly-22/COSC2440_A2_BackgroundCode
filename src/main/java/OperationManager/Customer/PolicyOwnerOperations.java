package OperationManager.Customer;

import Database.ClaimCRUD;
import Database.CustomerCRUD;
import Database.DatabaseConnection;
import Database.InsuranceCardCRUD;
import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.InsuranceCard.InsuranceCard;
import OperationManager.Utils.InputChecker;

import java.math.BigDecimal;
import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PolicyOwnerOperations implements UserInfoDAO, CustomerClaimDAO {

    private PolicyOwner policyOwner;
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

        // add document

        boolean createClaimSuccessfully = claimCRUD.createClaim(claim);

        if (createClaimSuccessfully) {
            customerCRUD.addToClaimList(insuranceCard.getCardHolder(), claim.getFID());
            customerCRUD.updatePolicyOwnerActionHistory(policyOwner.getCID(), LocalDate.now() + ": add Claim " + fID + " to Insurance Card " + insuranceCardNumber + " holder");
        }
        return false;
    }

    @Override
    public Claim getClaimByID(String fID) {
        try {
            return claimCRUD.readClaim(fID);
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getAllClaimsByCustomer(String cID) {
        return false;
    }

    @Override
    public List<Claim> getAllClaims() {
        return null;
    }

    @Override
    public boolean updateClaim(String fID, Claim claim) {
        return false;
    }

    @Override
    public boolean deleteClaim(String fID) {
        return false;
    }

    // methods relating to user information
    @Override
    public void displayInfo() {

    }

    @Override
    public boolean updatePhone() {
        return false;
    }

    @Override
    public boolean updateAddress() {
        return false;
    }

    @Override
    public boolean updateEmail() {
        return false;
    }

    @Override
    public boolean updatePassword() {
        return false;
    }
}
