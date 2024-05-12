package OperationManager.SystemAdmin;

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
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class SystemAdminOperations {
    private final SystemAdmin systemAdmin = new SystemAdmin();
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

        System.out.println(newPolicyOwner); // output test

        systemAdmin.addActionHistory(LocalDate.now() + ": add Policy Owner " + newPolicyOwner.getCID());
        System.out.println(systemAdmin.getActionHistory());
    }
    public void getPolicyOwner(String cID) {
        // connect database to execute
    }
    public void updatePolicyOwner(String cID) {
        // find policyOwner by cID: getPolicyOwner(String cID)
        // test policyOwner
        PolicyOwner policyOwner = new PolicyOwner("c-0000000", "owner_tester", "090909090","Canada", "tester_owner@gmail.com", "popoipoiu");



    }
    public void deletePolicyOwner(String cID) {

    }

    // CRUD for policyholders
    public void addPolicyHolder(PolicyOwner policyOwner) {

        PolicyHolder newPolicyHolder = (PolicyHolder) addCustomer("PolicyHolder");
        if (newPolicyHolder == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newPolicyHolder.setPolicyOwner(policyOwner.getCID());
        policyOwner.addToBeneficiaries(newPolicyHolder);
        System.out.println(policyOwner);

        InsuranceCard newInsuranceCard = addInsuranceCard(policyOwner);
        newInsuranceCard.setCardHolder(newPolicyHolder.getCID());
        System.out.println(newInsuranceCard);

        newPolicyHolder.setInsuranceCardNumber(newInsuranceCard.getCardNumber());
        System.out.println(newPolicyHolder);

        systemAdmin.addActionHistory(LocalDate.now() + ": add Policy Holder " + newPolicyHolder.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to Policy Owner " + policyOwner.getCID());
        System.out.println(systemAdmin.getActionHistory());
        policyOwner.addActionHistory(LocalDate.now() + ": add Policy Holder " + newPolicyHolder.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to beneficiaries by System Admin");
    }
    public void getPolicyHolder(String cID) {

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
        System.out.println(policyHolder);   // output test
        System.out.println(policyOwner);    // output test

        InsuranceCard newInsuranceCard = addInsuranceCard(policyOwner);
        newInsuranceCard.setCardHolder(newDependent.getCID());
        System.out.println(newInsuranceCard);   // output test

        newDependent.setInsuranceCardNumber(newInsuranceCard.getCardNumber());
        System.out.println(newDependent);   // output test

        systemAdmin.addActionHistory(LocalDate.now() + ": add Dependent " + newDependent.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to Policy Holder " + policyHolder.getCID());
        System.out.println(systemAdmin.getActionHistory());
        policyOwner.addActionHistory(LocalDate.now() + ": add Dependent " + newDependent.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to Policy Holder " + policyHolder.getCID() + " to beneficiaries by System Admin");
        System.out.println(policyOwner.getActionHistory());
        policyHolder.addActionHistory(LocalDate.now() + ": add Dependent " + newDependent.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to dependent list by System Admin");

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
        // check if pID already exists

        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter password: ");
        String enteredPassword = scanner.nextLine();
        // convert password into hashed
        String password = enteredPassword;

        if (role.equalsIgnoreCase("insurancemanager")) {
            return new InsuranceManager(pID, "InsuranceManager", fullName, password);
        } else if (role.equalsIgnoreCase("insurancesurveyor")) {
            return new InsuranceSurveyor(pID, "InsuranceSurveyor", fullName, password);
        }
        return null;
    }
    public void addInsuranceManager() {

        InsuranceManager newInsuranceManager = (InsuranceManager) addProvider("InsuranceManager");
        if (newInsuranceManager == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }

        System.out.println(newInsuranceManager);    // output test

        systemAdmin.addActionHistory(LocalDate.now() + ": add Insurance Manager " + newInsuranceManager.getPID());
        System.out.println(systemAdmin.getActionHistory());
    }
    public void getInsuranceManager(String pID) {

    }
    public void updateInsuranceManager(String pID) {

    }
    public void deleteInsuranceManager(String pID) {

    }

    // CRUD for insurance surveyors
    public void addInsuranceSurveyor(InsuranceManager insuranceManager) {

        InsuranceSurveyor newInsuranceSurveyor = (InsuranceSurveyor) addProvider("InsuranceSurveyor");
        if (newInsuranceSurveyor == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newInsuranceSurveyor.setInsuranceManager(insuranceManager.getPID());
        insuranceManager.addToSurveyorList(newInsuranceSurveyor);

        System.out.println(newInsuranceSurveyor);    // output test
        System.out.println(insuranceManager);   //output test

        systemAdmin.addActionHistory(LocalDate.now() + ": add Insurance Surveyor " + newInsuranceSurveyor.getPID() + " to Insurance Manager " + insuranceManager.getPID());
        System.out.println(systemAdmin.getActionHistory());
        insuranceManager.addActionHistory(LocalDate.now() + ": add Insurance Surveyor " + newInsuranceSurveyor.getPID() + " to surveyor list by System Admin");
    }
    public void getInsuranceSurveyor(String pID) {

    }
    public void updateInsuranceSurveyor(String pID) {

    }
    public void deleteInsuranceSurveyor(String pID) {

    }
}
