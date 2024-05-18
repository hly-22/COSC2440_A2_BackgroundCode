package OperationManager.Provider;

import Database.DatabaseConnection;
import Database.ProviderCRUD;
import Interfaces.ProviderClaimDAO;
import Interfaces.ProviderCustomerDAO;
import Models.Claim.Claim;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Utils.InputChecker;

import java.util.Scanner;

public class InsuranceSurveyorOperations implements ProviderClaimDAO, ProviderCustomerDAO {

    private InsuranceSurveyor insuranceSurveyor;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private ProviderCRUD providerCRUD = new ProviderCRUD(databaseConnection);
    private final Scanner scanner = new Scanner(System.in);
    public InsuranceSurveyorOperations(InsuranceSurveyor insuranceSurveyor) {
        this.insuranceSurveyor = insuranceSurveyor;
    }


    // method to update password
    public void updateProviderPassword() {
        // Ask for current password
        System.out.print("Enter current password: ");
        String currentPassword = scanner.nextLine();

        // Retrieve the hashed password from the database based on the username
        String hashedPasswordFromDB = providerCRUD.getHashedPasswordFromDB(insuranceSurveyor.getPID());

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
        providerCRUD.updatePasswordInDB(insuranceSurveyor.getPID(), hashedNewPassword);

        System.out.println("Password updated successfully.");
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

    }

    @Override
    public void proposeClaim(Claim claim) {

    }

    @Override
    public boolean approveClaim(Claim claim) {
        return false;   // not available to insurance surveyors
    }

    @Override
    public boolean rejectClaim(Claim claim) {
        return false;   // not available to insurance surveyors
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
