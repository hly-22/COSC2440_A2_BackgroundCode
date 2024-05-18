package OperationManager.Customer;

import Database.ClaimCRUD;
import Database.DatabaseConnection;
import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Customer.Customer;
import Models.Customer.Dependent;

import java.sql.SQLException;
import java.util.List;
import java.util.Scanner;

public class DependentOperations implements UserInfoDAO, CustomerClaimDAO {

    private Dependent dependent = new Dependent();
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
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
        try {
            return claimCRUD.readClaim(fID);
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
    public void displayInfo(Customer customer) {

    }

    @Override
    public boolean updatePhone(Customer customer, String phone) {
        return false;   // not available to dependents
    }

    @Override
    public boolean updateAddress(Customer customer, String address) {
        return false;   // not available to dependents
    }

    @Override
    public boolean updateEmail(Customer customer, String email) {
        return false;   // not available to dependents
    }

    @Override
    public boolean updatePassword(Customer customer, String password) {
        return false;   // not available to dependents
    }
}
