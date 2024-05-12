package OperationManager.Provider;

import Interfaces.ProviderClaimDAO;
import Interfaces.ProviderCustomerDAO;
import Models.Claim.Claim;
import Models.Customer.Dependent;
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;

import java.util.List;
import java.util.Scanner;

public class InsuranceManagerOperations implements ProviderClaimDAO, ProviderCustomerDAO {

    private final InsuranceManager insuranceManager = new InsuranceManager();
    private final Scanner scanner = new Scanner(System.in);

    // method to update password
    public void updateProviderPassword(String password) {

    }

    // methods relating to surveyors
    public boolean addSurveyor(InsuranceSurveyor insuranceSurveyor) {
        return false;
    }
    public boolean removeSurveyor(InsuranceSurveyor insuranceSurveyor) {
        return false;
    }
    public void getSurveyorInfo(InsuranceSurveyor insuranceSurveyor) {

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
        // not available to insurance managers
    }

    @Override
    public void proposeClaim(Claim claim) {
        // not available to insurance managers
    }

    @Override
    public boolean approveClaim(Claim claim) {
        return false;
    }

    @Override
    public boolean rejectClaim(Claim claim) {
        return false;
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
