package OperationManager.SystemAdmin;

import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.InsuranceCard.InsuranceCard;
import Models.Provider.InsuranceManager;
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

    // add action history
    public void addActionHistory(String action) {
        List<String> actionHistory = systemAdmin.getActionHistory();
        actionHistory.add(action);
        System.out.println(systemAdmin);    // test output
    }

    // method to sum successfully claimed amount with different filtering options
    public void sumClaimAmount() {

    }

    // add customer
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


    // CRUD for policy owners
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

        addActionHistory(LocalDate.now() + ": add Policy Owner " + newPolicyOwner.getCID());
    }
    public void getPolicyOwner(String cID) {

    }
    public void updatePolicyOwner(String cID) {

    }
    public void deletePolicyOwner(String cID) {

    }

    // CRUD for policyholders
    public void addPolicyHolder() {
        System.out.println("Enter a Policy Owner cID: ");
        String policyOwnerCID = scanner.nextLine();
        // find existing policy owner, print error message and return if not found
        // test policyOwner
        PolicyOwner policyOwner = new PolicyOwner(policyOwnerCID, "owner_tester", "090909090","Canada", "tester_owner@gmail.com", "popoipoiu");

        PolicyHolder newPolicyHolder = (PolicyHolder) addCustomer("PolicyHolder");
        if (newPolicyHolder == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newPolicyHolder.setPolicyOwner(policyOwner.getCID());

        InsuranceCard newInsuranceCard = addInsuranceCard(policyOwner);
        newInsuranceCard.setCardHolder(newPolicyHolder.getCID());
        System.out.println(newInsuranceCard);

        newPolicyHolder.setInsuranceCardNumber(newInsuranceCard.getCardNumber());
        System.out.println(newPolicyHolder);

        addActionHistory(LocalDate.now() + ": add Policy Holder " + newPolicyHolder.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber());
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
    public void addDependent() {
        System.out.println("Enter a Policy Owner cID: ");
        String policyOwnerCID = scanner.nextLine();
        // find existing policy owner, print error message and return if not found
        // test policyOwner
        PolicyOwner policyOwner = new PolicyOwner(policyOwnerCID, "ownertester", "090909090","jijijiji", "testerowner@gmail.com", "popoipoiu");  // test

        System.out.println("Enter a Policy Holder cID: ");
        String policyHolderCID = scanner.nextLine();
        // find if policyHolder exist in policyOwner beneficiaries
        // test policyHolder
        PolicyHolder policyHolder = new PolicyHolder(policyHolderCID, "9090909090", new ArrayList<>());
//        if (policyOwner.getBeneficiaries() != null) {
//            for (String beneCID : policyOwner.getBeneficiaries()) {
//                if (policyHolderCID.equals(beneCID)) {
//                    policyHolder = new PolicyHolder(null, null, null);  // findBeneficiaryByCID
//                }
//            }
//        }

        Dependent newDependent = (Dependent) addCustomer("Dependent");
        if (newDependent == null) {
            System.out.println("Invalid input. Try again.");
            return;
        }
        newDependent.setPolicyOwner(policyOwner.getCID());
        newDependent.setPolicyHolder(policyHolder.getCID());
        policyHolder.addToDependentList(newDependent);
        System.out.println(policyHolder);   // output test

        InsuranceCard newInsuranceCard = addInsuranceCard(policyOwner);
        newInsuranceCard.setCardHolder(newDependent.getCID());
        System.out.println(newInsuranceCard);   // output test

        newDependent.setInsuranceCardNumber(newInsuranceCard.getCardNumber());
        System.out.println(newDependent);   // output test

        addActionHistory(LocalDate.now() + ": add Dependent " + newDependent.getCID() + " with Insurance Card " + newInsuranceCard.getCardNumber() + " to Policy Holder " + policyHolder.getCID());
    }
    public void getDependent(String cID) {

    }
    public void updateDependent(String cID) {

    }
    public void deleteDependent(String cID) {

    }

    // CRUD for insurance managers
    public void addInsuranceManager() {

    }
    public void getInsuranceManager(String pID) {

    }
    public void updateInsuranceManager(String pID) {

    }
    public void deleteInsuranceManager(String pID) {

    }

    // CRUD for insurance surveyors
    public void addInsuranceSurveyor(InsuranceManager insuranceManager) {

    }
    public void getInsuranceSurveyor(String pID) {

    }
    public void updateInsuranceSurveyor(String pID) {

    }
    public void deleteInsuranceSurveyor(String pID) {

    }
}
