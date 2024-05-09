package Models.Customer;

import Models.Claim.Claim;

import java.util.List;

public class Dependent extends Customer{
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

    public Dependent(String cID, String role, String fullName, String phone, String address, String email, String password, List<String> actionHistory, String policyOwner, String policyHolder, String insuranceCardNumber, List<Claim> claimList) {
        super(cID, role, fullName, phone, address, email, password, actionHistory);
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

    @Override
    public String toString() {
        return "Dependent{" +
                super.toString() +
                "policyOwner='" + policyOwner + '\'' +
                ", policyHolder='" + policyHolder + '\'' +
                ", insuranceCardNumber='" + insuranceCardNumber + '\'' +
                ", claimList=" + claimList +
                '}';
    }
}
