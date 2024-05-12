package Models.Customer;

import Models.Claim.Claim;

import java.math.BigDecimal;
import java.util.ArrayList;
import java.util.List;

public class PolicyOwner extends Customer {
     private List<String> beneficiaries;
     private BigDecimal insuranceFee;

    public PolicyOwner(List<String> beneficiaries, BigDecimal insuranceFee) {
        this.beneficiaries = beneficiaries;
        this.insuranceFee = insuranceFee;
    }

    public PolicyOwner(String cID, String role, String fullName, String phone, String address, String email, String password, List<String> actionHistory, List<Claim> claimList, List<String> beneficiaries, BigDecimal insuranceFee) {
        super(cID, role, fullName, phone, address, email, password, actionHistory, claimList);
        this.beneficiaries = beneficiaries;
        this.insuranceFee = insuranceFee;
    }
    public PolicyOwner(String cID, String fullName, String phone, String address, String email, String password, BigDecimal insuranceFee) {
        super(cID, "PolicyOwner", fullName, phone, address, email, password);
        this.beneficiaries = new ArrayList<>();
        this.insuranceFee = insuranceFee;
    }

    public PolicyOwner(String cID, String fullName, String phone, String address, String email, String password) {
        super(cID, "PolicyOwner", fullName, phone, address, email, password);
        this.beneficiaries = new ArrayList<>();
        this.insuranceFee = null;
    }

    public PolicyOwner() {

    }

    public List<String> getBeneficiaries() {
        if (beneficiaries == null) {
            return null;
        }
        return beneficiaries;
    }

    public void setBeneficiaries(List<String> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public BigDecimal getInsuranceFee() {
        return insuranceFee;
    }

    public void setInsuranceFee(BigDecimal insuranceFee) {
        this.insuranceFee = insuranceFee;
    }

    public boolean addToBeneficiaries(Customer customer) {
        // body
        return false;
    }
    public boolean removeFromBeneficiaries(Customer customer) {
        // body
        return false;
    }

    @Override
    public String toString() {
        return "PolicyOwner{" +
                super.toString() +
                ", beneficiaries=" + beneficiaries +
                ", insuranceFee=" + insuranceFee +
                '}';
    }
}
