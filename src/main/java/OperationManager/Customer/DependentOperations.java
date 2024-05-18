package OperationManager.Customer;

import Database.ClaimCRUD;
import Database.CustomerCRUD;
import Database.DatabaseConnection;
import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Claim.Document;
import Models.Customer.Dependent;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Scanner;

public class DependentOperations implements UserInfoDAO, CustomerClaimDAO {

    private Dependent dependent;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
    private CustomerCRUD customerCRUD = new CustomerCRUD(databaseConnection);
    private ClaimCRUD claimCRUD = new ClaimCRUD(databaseConnection);
    private final Scanner scanner = new Scanner(System.in);
    public DependentOperations(Dependent dependent) {
        this.dependent = dependent;
    }

    @Override
    public boolean addClaim(String insuranceCardNumber) {
        return false;   // not available to dependents
    }

    @Override
    public Claim getClaimByID(String fID) {
        Claim claim = claimCRUD.readClaim(fID);
        if (claim != null && claim.getInsuredPerson().equals(dependent.getCID())) {
            customerCRUD.updateDependentActionHistory(dependent.getCID(), LocalDate.now() + ": retrieve Claim " + claim.getFID());
            return claim;
        }
        System.out.println("No claim found.");
        return null;
    }

    @Override
    public List<Claim> getAllClaims() {
        List<Claim> allClaims = new ArrayList<>();
        allClaims = claimCRUD.getClaimsByCustomerID(dependent.getCID());
        customerCRUD.updateDependentActionHistory(dependent.getCID(), LocalDate.now() + ": retrieve all claims");
        return allClaims;
    }

    @Override
    public boolean updateClaim(String fID, List<Document> documentList, String receiverBankingInfo) {
        return false;    // not available to dependents
    }
    @Override
    public boolean deleteClaim(String fID) {
        return false;   // not available to dependents
    }

    @Override
    public void displayInfo() {
        Dependent dependentInfo = customerCRUD.readDependent(dependent.getCID());
        System.out.println(dependentInfo);
        customerCRUD.updateDependentActionHistory(dependent.getCID(), LocalDate.now() + "retrieve information");
    }

    @Override
    public boolean updatePhone(String phone) {
        return false;   // not available to dependents
    }

    @Override
    public boolean updateAddress(String address) {
        return false;   // not available to dependents
    }

    @Override
    public boolean updateEmail(String email) {
        return false;   // not available to dependents
    }

    @Override
    public boolean updatePassword() {
        return false;
        // not available to dependents
    }
}
