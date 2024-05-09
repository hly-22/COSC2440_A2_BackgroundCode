package OperationManager.Provider;

import Interfaces.ProviderClaimDAO;
import Interfaces.ProviderCustomerDAO;
import Models.Claim.Claim;

public class InsuranceSurveyorOperations implements ProviderClaimDAO, ProviderCustomerDAO {

    // method to update password
    public void updateProviderPassword(String password) {

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
