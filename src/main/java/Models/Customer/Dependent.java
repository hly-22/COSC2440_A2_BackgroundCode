package Models.Customer;

import Models.Claim.Claim;

import java.util.List;

public class Dependent extends Customer{
    private String policyOwner;
    private String policyHolder;
    private String insuranceCardNumber;

    public Dependent(String policyOwner, String policyHolder, String insuranceCardNumber) {
        this.policyOwner = policyOwner;
        this.policyHolder = policyHolder;
        this.insuranceCardNumber = insuranceCardNumber;
    }

    public Dependent(String cID, String role, String fullName, String phone, String address, String email, String password, List<String> actionHistory, List<Claim> claimList, String policyOwner, String policyHolder, String insuranceCardNumber) {
        super(cID, role, fullName, phone, address, email, password, actionHistory, claimList);
        this.policyOwner = policyOwner;
        this.policyHolder = policyHolder;
        this.insuranceCardNumber = insuranceCardNumber;
    }

    public Dependent(String cID, String fullName, String phone, String address, String email, String password) {
        super(cID, "Dependent", fullName, phone, address, email, password);
        this.policyOwner = null;
        this.policyHolder = null;
        this.insuranceCardNumber = null;
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

    @Override
    public String toString() {
        return "Dependent{" +
                super.toString() +
                ", policyOwner='" + policyOwner + '\'' +
                ", policyHolder='" + policyHolder + '\'' +
                ", insuranceCardNumber='" + insuranceCardNumber + '\'' +
                '}';
    }
}
