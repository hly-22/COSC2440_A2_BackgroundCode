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
        super();
        this.beneficiaries = new ArrayList<>();
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
        return beneficiaries.add(customer.getCID());
    }
    public boolean removeFromBeneficiaries(Customer customer) {
        // remove from beneficiaries, delete from the database; if it is a policyholder, delete also their dependents
        int indexToRemove = -1;
        for (int i = 0; i < beneficiaries.size(); i++) {
            if (beneficiaries.get(i).equals(customer.getCID())) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            beneficiaries.remove(indexToRemove);
            return true;
        } else {
            return false;
        }
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
