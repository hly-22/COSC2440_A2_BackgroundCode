package Models.Customer;

import Models.Claim.Claim;

import java.util.List;

public class Dependent {
    private String policyOwner;
    private String policyHolder;
    private String insuranceCardNumber;
    private List<Claim> claimList;

    public Dependent(String policyOwner, String policyHolder, String insuranceCardNumber, List<Claim> claimList) {
        this.policyOwner = policyOwner;
        this.policyHolder = policyHolder;
        this.insuranceCardNumber = insuranceCardNumber;
        this.claimList = claimList;
    }

    public String getPolicyOwner() {
        return policyOwner;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public String getPolicyHolder() {
        return policyHolder;
    }

    public void setPolicyHolder(String policyHolder) {
        this.policyHolder = policyHolder;
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
}
