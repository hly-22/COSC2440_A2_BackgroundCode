package Model.Customer;

import java.math.BigDecimal;
import java.util.List;

public class PolicyOwner extends Customer {
     private List<String> beneficiaries;
     private BigDecimal insuranceFeeRate;

    public PolicyOwner(List<String> beneficiaries, BigDecimal insuranceFeeRate) {
        this.beneficiaries = beneficiaries;
        this.insuranceFeeRate = insuranceFeeRate;
    }

    public PolicyOwner(String cID, String role, String fullName, String phone, String address, String email, String password, List<String> actionHistory, List<String> beneficiaries, BigDecimal insuranceFeeRate) {
        super(cID, role, fullName, phone, address, email, password, actionHistory);
        this.beneficiaries = beneficiaries;
        this.insuranceFeeRate = insuranceFeeRate;
    }

    public List<String> getBeneficiaries() {
        return beneficiaries;
    }

    public void setBeneficiaries(List<String> beneficiaries) {
        this.beneficiaries = beneficiaries;
    }

    public BigDecimal getInsuranceFeeRate() {
        return insuranceFeeRate;
    }

    public void setInsuranceFeeRate(BigDecimal insuranceFeeRate) {
        this.insuranceFeeRate = insuranceFeeRate;
    }

    public boolean addToBeneficiaries(Customer customer) {
        // body
        return false;
    }
    public boolean removeFromBeneficiaries(Customer customer) {
        // body
        return false;
    }
}
