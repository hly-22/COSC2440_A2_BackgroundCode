package Models.Customer;

import Models.Claim.Claim;

import java.util.List;

public class PolicyHolder extends Customer {
    private String policyOwner;
    private String insuranceCardNumber;
    private List<Claim> claimList;
    private List<String> dependentList;

    public PolicyHolder(String policyOwner, String insuranceCardNumber, List<Claim> claimList, List<String> dependentList) {
        this.policyOwner = policyOwner;
        this.insuranceCardNumber = insuranceCardNumber;
        this.claimList = claimList;
        this.dependentList = dependentList;
    }

    public PolicyHolder(String cID, String role, String fullName, String phone, String address, String email, String password, List<String> actionHistory, String policyOwner, String insuranceCardNumber, List<Claim> claimList, List<String> dependentList) {
        super(cID, role, fullName, phone, address, email, password, actionHistory);
        this.policyOwner = policyOwner;
        this.insuranceCardNumber = insuranceCardNumber;
        this.claimList = claimList;
        this.dependentList = dependentList;
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public String getInsuranceCardNumber() {
        return insuranceCardNumber;
    }

    public void setInsuranceCardNumber(String insuranceCardNumber) {
        this.insuranceCardNumber = insuranceCardNumber;
    }

    public List<Claim> getClaimList() {
        return claimList;
    }

    public void setClaimList(List<Claim> claimList) {
        this.claimList = claimList;
    }

    public List<String> getDependentList() {
        return dependentList;
    }

    public void setDependentList(List<String> dependentList) {
        this.dependentList = dependentList;
    }

    public boolean addToDependentList(Dependent dependent) {
        return false;
    }

    public boolean removeFromDependentList(Dependent dependent) {
        return false;
    }
}
