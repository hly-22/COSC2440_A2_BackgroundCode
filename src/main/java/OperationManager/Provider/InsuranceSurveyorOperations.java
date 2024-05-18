package OperationManager.Provider;

import Database.ClaimCRUD;
import Database.CustomerCRUD;
import Database.DatabaseConnection;
import Database.ProviderCRUD;
import Interfaces.ProviderClaimDAO;
import Interfaces.ProviderCustomerDAO;
import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Utils.InputChecker;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class InsuranceSurveyorOperations implements ProviderClaimDAO, ProviderCustomerDAO {

    private InsuranceSurveyor insuranceSurveyor;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private ProviderCRUD providerCRUD = new ProviderCRUD(databaseConnection);
    private CustomerCRUD customerCRUD = new CustomerCRUD(databaseConnection);
    private ClaimCRUD claimCRUD = new ClaimCRUD(databaseConnection);
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
    public void displayInfo() {
        System.out.println(providerCRUD.readInsuranceSurveyor(insuranceSurveyor.getPID()));
        providerCRUD.updateManagerActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": get information");
    }
    // methods relating to claims
    @Override
    public List<Claim> getAllClaims() {
        providerCRUD.updateSurveyorActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": get all Claims");
        return claimCRUD.readAllClaims();
    }

    @Override
    public Claim getClaimByID(String fID) {
        providerCRUD.updateSurveyorActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": get Claim " + fID);
        return claimCRUD.readClaim(fID);
    }

    @Override
    public List<Claim> getClaimsByInsuranceCard(String insuranceCardNumber) {
        providerCRUD.updateSurveyorActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": get Claims of Insurance Card " + insuranceCardNumber);
        return claimCRUD.getClaimsByInsuranceCard(insuranceCardNumber);
    }

    @Override
    public List<Claim> getClaimsByStatus(String status) {
        providerCRUD.updateSurveyorActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": get Claims of Status " + status);
        return claimCRUD.getClaimsByStatus(status);
    }

    @Override
    public List<Claim> getClaimsByCID(String cID) {
        providerCRUD.updateSurveyorActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": get Claims of Insured Person " + cID);
        return claimCRUD.getClaimsByCustomerID(cID);
    }

    @Override
    public boolean requireMoreInfo(Claim claim) {
        System.out.println("Enter your note: ");
        String note = scanner.nextLine();

        if (claimCRUD.updateClaimStatus(claim.getFID(), ClaimStatus.REJECTED.name()) && claimCRUD.updateNote(claim.getFID(), note)) {
            providerCRUD.updateSurveyorActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": require more information for Claim " + claim.getFID());
            return true;
        }
        return false;
    }

    @Override
    public boolean proposeClaim(Claim claim) {
        if (claimCRUD.updateClaimStatus(claim.getFID(), ClaimStatus.PROCESSING.name())) {
            providerCRUD.updateSurveyorActionHistory(insuranceSurveyor.getPID(), LocalDate.now() + ": propose Claim " + claim.getFID());
            return true;
        }
        return false;
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
    public List<PolicyOwner> getAllPolicyOwners() {
        return customerCRUD.getAllPolicyOwners();
    }

    @Override
    public List<PolicyHolder> getAllPolicyHolders() {
        return customerCRUD.getAllPolicyHolders();
    }

    @Override
    public List<Dependent> getAllDependents() {
        return customerCRUD.getAllDependents();
    }

    @Override
    public Customer getCustomerByID(String cID, String table_name) {
        return customerCRUD.getCustomerByID(cID, table_name);
    }
}
