package OperationManager.Customer;

import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Customer.Customer;
import Models.Customer.Dependent;

import java.util.List;
import java.util.Scanner;

public class DependentOperations implements UserInfoDAO, CustomerClaimDAO {

    private final Dependent dependent = new Dependent();
    private final Scanner scanner = new Scanner(System.in);

    @Override
    public boolean addClaim(Customer customer, Claim claim) {
        return false;   // not available to dependents
    }

    @Override
    public boolean getClaimByID(String fID) {
        return false;
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
