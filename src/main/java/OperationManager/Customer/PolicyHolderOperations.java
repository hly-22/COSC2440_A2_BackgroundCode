package OperationManager.Customer;

import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Customer.Customer;
import Models.Customer.Dependent;

public class PolicyHolderOperations implements UserInfoDAO, CustomerClaimDAO {

    // methods relating to dependents
    public boolean addDependent(Dependent dependent) {
        return false;
    }
    public boolean removeDependent(Dependent dependent) {
        return false;
    }

    // methods relating to claims
    @Override
    public boolean addClaim(Customer customer, Claim claim) {
        return false;
    }

    @Override
    public boolean getClaimByID(String fID) {
        return false;
    }

    @Override
    public boolean getAllClaimsByCustomer(String cID) {
        return false;
    }

    @Override
    public void getAllClaims() {

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
    public void displayInfo(Customer customer) {

    }

    @Override
    public boolean updatePhone(Customer customer, String phone) {
        return false;
    }

    @Override
    public boolean updateAddress(Customer customer, String address) {
        return false;
    }

    @Override
    public boolean updateEmail(Customer customer, String email) {
        return false;
    }

    @Override
    public boolean updatePassword(Customer customer, String password) {
        return false;
    }
}