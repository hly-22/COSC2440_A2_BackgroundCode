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
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Utils.InputChecker;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class InsuranceManagerOperations implements ProviderClaimDAO, ProviderCustomerDAO {

    private InsuranceManager insuranceManager;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private ProviderCRUD providerCRUD = new ProviderCRUD(databaseConnection);
    private CustomerCRUD customerCRUD = new CustomerCRUD(databaseConnection);
    private ClaimCRUD claimCRUD = new ClaimCRUD(databaseConnection);
    private final Scanner scanner = new Scanner(System.in);
    public InsuranceManagerOperations(InsuranceManager insuranceManager) {
        this.insuranceManager = insuranceManager;
    }

    // method to update password and display information
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
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": update password");

        System.out.println("Password updated successfully.");
    }
    public void displayInfo() {
        System.out.println(providerCRUD.readInsuranceManager(insuranceManager.getPID()));
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": get information");
    }

    // methods relating to surveyors
    public InsuranceSurveyor retrieveSurveyorInfo(String pID) {
        InsuranceSurveyor surveyor = providerCRUD.readInsuranceSurveyor(pID);
        if (surveyor != null && surveyor.getInsuranceManager().equals(insuranceManager.getPID())) {
            providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": retrieve Insurance Surveyor " + surveyor.getPID());
            return surveyor;
        } else {
            return null; // Surveyor not found or doesn't belong to the logged-in InsuranceManager
        }
    }
    public List<InsuranceSurveyor> retrieveAllSurveyors() {
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": get all Surveyors");
        return providerCRUD.getAllSurveyorsByManager(insuranceManager.getPID());
    }

    // methods relating to claims
    @Override
    public List<Claim> getAllClaims() {
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": get all Claims");
        return claimCRUD.readAllClaims();
    }

    @Override
    public Claim getClaimByID(String fID) {
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": get Claim " + fID);
        return claimCRUD.readClaim(fID);
    }

    @Override
    public List<Claim> getClaimsByInsuranceCard(String insuranceCardNumber) {
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": get Claims of Insurance Card " + insuranceCardNumber);
        return claimCRUD.getClaimsByInsuranceCard(insuranceCardNumber);
    }

    @Override
    public List<Claim> getClaimsByStatus(String status) {
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": get Claims of Status " + status);
        return claimCRUD.getClaimsByStatus(status);
    }

    @Override
    public List<Claim> getClaimsByCID(String cID) {
        providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": get Claims of Insured Person " + cID);
        return claimCRUD.getClaimsByCustomerID(cID);
    }

    @Override
    public boolean requireMoreInfo(Claim claim) {
        // not available to insurance managers
        return false;
    }

    @Override
    public boolean proposeClaim(Claim claim) {
        // not available to insurance managers
        return false;
    }

    @Override
    public boolean approveClaim(Claim claim) {
        if (claimCRUD.updateClaimStatus(claim.getFID(), ClaimStatus.APPROVED.name())) {
            providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": approve Claim " + claim.getFID());
            return true;
        }
        return false;
    }

    @Override
    public boolean rejectClaim(Claim claim) {
        if (claimCRUD.updateClaimStatus(claim.getFID(), ClaimStatus.REJECTED.name())) {
            providerCRUD.updateManagerActionHistory(insuranceManager.getPID(), LocalDate.now() + ": reject Claim " + claim.getFID());
            return true;
        }
        return false;
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
