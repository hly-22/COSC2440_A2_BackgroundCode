package OperationManager.Provider;

import Database.DatabaseConnection;
import Database.ProviderCRUD;
import Interfaces.ProviderClaimDAO;
import Interfaces.ProviderCustomerDAO;
import Models.Claim.Claim;
import Models.Customer.Dependent;
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Utils.InputChecker;

import javax.xml.crypto.Data;
import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class InsuranceManagerOperations implements ProviderClaimDAO, ProviderCustomerDAO {

    private InsuranceManager insuranceManager;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private ProviderCRUD providerCRUD = new ProviderCRUD(databaseConnection);
    private final Scanner scanner = new Scanner(System.in);
    public InsuranceManagerOperations(InsuranceManager insuranceManager) {
        this.insuranceManager = insuranceManager;
    }

    // method to update password
    public void updateProviderPassword() {
        // Ask for current password
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        // Retrieve the hashed password from the database based on the username
        String hashedPasswordFromDB = providerCRUD.getHashedPasswordFromDB(insuranceManager.getPID());

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
        providerCRUD.updatePasswordInDB(insuranceManager.getPID(), hashedNewPassword);

        System.out.println("Password updated successfully.");
    }

    // methods relating to surveyors
    public void addSurveyor() {

        System.out.println("Enter a valid pID (p-xxxxxxx): ");
        String pID = scanner.nextLine();
        if (!InputChecker.isValidPIDFormat(pID)) {
            System.out.println("Invalid provider ID format.");
            return;
        }
        // check if pID already exists

        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter password: ");
        String enteredPassword = scanner.nextLine();
        // convert password into hashed
        String password = enteredPassword;

        InsuranceSurveyor insuranceSurveyor = new InsuranceSurveyor(pID, fullName, password, insuranceManager.getPID());
        System.out.println(insuranceSurveyor);

        insuranceManager.addActionHistory(LocalDate.now() + ": add Insurance Surveyor " + insuranceSurveyor.getPID() + " to surveyor list");
        System.out.println(insuranceManager.getActionHistory());
    }
    public boolean removeSurveyor(InsuranceSurveyor insuranceSurveyor) {
        return false;
    }
    public void getSurveyorInfo(InsuranceSurveyor insuranceSurveyor) {

    }

    // methods relating to claims
    @Override
    public void getAllClaims() {

    }

    @Override
    public void getClaimByID(String fID) {

    }

    @Override
    public void filterClaim() {

    }

    @Override
    public void requireMoreInfo() {
        // not available to insurance managers
    }

    @Override
    public void proposeClaim(Claim claim) {
        // not available to insurance managers
    }

    @Override
    public boolean approveClaim(Claim claim) {
        return false;
    }

    @Override
    public boolean rejectClaim(Claim claim) {
        return false;
    }

    // methods relating to customers
    @Override
    public void getAllCustomers() {

    }

    @Override
    public void getCustomerByID(String cID) {

    }

    @Override
    public void filterCustomer() {

    }
}
