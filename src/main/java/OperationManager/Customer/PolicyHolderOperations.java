package OperationManager.Customer;

import Database.ClaimCRUD;
import Database.DatabaseConnection;
import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.InsuranceCard.InsuranceCard;
import OperationManager.Utils.InputChecker;

import java.sql.SQLException;
import java.time.LocalDate;
import java.time.format.DateTimeParseException;
import java.util.List;
import java.util.Scanner;

public class PolicyHolderOperations implements UserInfoDAO, CustomerClaimDAO {

    private PolicyHolder policyHolder = new PolicyHolder();
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
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

    // methods relating to claims
    @Override
    public boolean addClaim(String insuranceCardNumber) {
        return false;
    }

    @Override
    public Claim getClaimByID(String fID) {
        return claimCRUD.readClaim(fID);
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
        return false;   // not available to policyholders
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
