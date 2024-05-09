package OperationManager.SystemAdmin;

import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.Provider.InsuranceManager;

public class SystemAdminOperations {

    // method to update password
    public void updateAdminPassword(String password) {

    }

    // method to sum successfully claimed amount with different filtering options
    public void sumClaimAmount() {

    }

    // CRUD for policy owners
    public void addPolicyOwner() {

    }
    public void getPolicyOwner(String cID) {

    }
    public void updatePolicyOwner(String cID) {

    }
    public void deletePolicyOwner(String cID) {

    }

    // CRUD for policyholders
    public void addPolicyHolder(PolicyOwner policyOwner) {

    }
    public void getPolicyHolder(String cID) {

    }
    public void updatePolicyHolder(String cID) {

    }
    public void deletePolicyHolder(String cID) {

    }

    // CRUD for dependents
    public void addDependent(PolicyOwner policyOwner, PolicyHolder policyHolder) {

    }
    public void getDependent(String cID) {

    }
    public void updateDependent(String cID) {

    }
    public void deleteDependent(String cID) {

    }

    // CRUD for insurance managers
    public void addInsuranceManager() {

    }
    public void getInsuranceManager(String pID) {

    }
    public void updateInsuranceManager(String pID) {

    }
    public void deleteInsuranceManager(String pID) {

    }

    // CRUD for insurance surveyors
    public void addInsuranceSurveyor(InsuranceManager insuranceManager) {

    }
    public void getInsuranceSurveyor(String pID) {

    }
    public void updateInsuranceSurveyor(String pID) {

    }
    public void deleteInsuranceSurveyor(String pID) {

    }
}
