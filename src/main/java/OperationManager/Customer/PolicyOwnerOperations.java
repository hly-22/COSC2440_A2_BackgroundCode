package OperationManager.Customer;

import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.InsuranceCard.InsuranceCard;
import OperationManager.Utils.InputChecker;

import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PolicyOwnerOperations implements UserInfoDAO, CustomerClaimDAO {

    private final PolicyOwner policyOwner = new PolicyOwner();
    private final Scanner scanner = new Scanner(System.in);


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

        System.out.println(policyHolder);   // output test
        System.out.println(insuranceCard);  // output test

        policyOwner.addActionHistory(LocalDate.now() + ": add Policy Holder " + policyHolder.getCID() + " with Insurance Card " + insuranceCard.getCardNumber() + " to beneficiaries");
        System.out.println(policyOwner.getActionHistory());
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
        policyHolder.addToDependentList(dependent);
        policyOwner.addToBeneficiaries(dependent);

        System.out.println(dependent);  // output test
        System.out.println(insuranceCard);  // output test
        System.out.println(policyHolder);   // output test

        policyOwner.addActionHistory(LocalDate.now() + ": add Dependent " + dependent.getCID() + " with Insurance Card " + insuranceCard.getCardNumber() + " to Policy Holder " + policyHolder.getCID() + " to beneficiaries");
        System.out.println(policyOwner.getActionHistory());
        policyHolder.addActionHistory(LocalDate.now() + ": add Dependent " + dependent.getCID() + " with Insurance Card " + insuranceCard.getCardNumber() + " to dependent list by Policy Owner " + policyOwner.getCID());
        System.out.println(policyHolder.getActionHistory());
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
    public boolean addClaim(Customer customer, Claim claim) {
        return false;
    }

    @Override
    public boolean getClaimByID(String fID) {
        return false;
    }

    @Override
    public boolean getAllClaimsByCustomer(String cID) {
        return false;
    }

    @Override
    public void getAllClaims() {

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
    public void displayInfo(Customer customer) {

    }

    @Override
    public boolean updatePhone(Customer customer, String phone) {
        return false;
    }

    @Override
    public boolean updateAddress(Customer customer, String address) {
        return false;
    }

    @Override
    public boolean updateEmail(Customer customer, String email) {
        return false;
    }

    @Override
    public boolean updatePassword(Customer customer, String password) {
        return false;
    }
}
