package OperationManager.Customer;

import Database.ClaimCRUD;
import Database.CustomerCRUD;
import Database.DatabaseConnection;
import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Customer.Dependent;

import java.sql.SQLException;
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
        Claim claim;
        try {
            claim = claimCRUD.readClaim(fID);
            if (claim.getFID().equals(fID)) {
                return claim;
            }
            return null;
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public boolean getAllClaimsByCustomer(String cID) {
        return false;   // not available to dependents
    }

    @Override
    public void getAllClaims() {

    }

    @Override
    public boolean updateClaim(String fID, Claim claim) {
        return false;   // not available to dependents
    }

    @Override
    public boolean deleteClaim(String fID) {
        return false;   // not available to dependents
    }

    @Override
    public void displayInfo() {
        System.out.println(customerCRUD.readDependent(dependent.getCID()));
    }

    @Override
    public boolean updatePhone() {
        return false;   // not available to dependents
    }

    @Override
    public boolean updateAddress() {
        return false;   // not available to dependents
    }

    @Override
    public boolean updateEmail() {
        return false;   // not available to dependents
    }

    @Override
    public boolean updatePassword() {
        return false;   // not available to dependents
    }
}
