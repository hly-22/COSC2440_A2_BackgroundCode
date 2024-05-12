package OperationManager.Customer;

import Interfaces.CustomerClaimDAO;
import Interfaces.UserInfoDAO;
import Models.Claim.Claim;
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;

import java.util.List;
import java.util.Scanner;

public class PolicyOwnerOperations implements UserInfoDAO, CustomerClaimDAO {

    private final PolicyOwner policyOwner = new PolicyOwner();
    private final Scanner scanner = new Scanner(System.in);

    public void addActionHistory(String action) {
        List<String> actionHistory = policyOwner.getActionHistory();
        actionHistory.add(action);
        System.out.println(policyOwner);    // test output
    }

    // method to calculate yearly cost to pay for insurance providers
    public void calculateYearlyCost() {

    }

    // methods relating to beneficiaries
    public boolean addBeneficiary(PolicyHolder policyHolder) {
        return false;
    }
    public boolean addBeneficiary(PolicyHolder policyHolder, Dependent dependent) {
        return false;
    }
    public void getBeneficiary(Customer customer) {

    }
    public boolean updateBeneficiary(Customer customer) {
        return false;
    }
    public boolean removeBeneficiary(Customer customer) {
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
        return false;
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
