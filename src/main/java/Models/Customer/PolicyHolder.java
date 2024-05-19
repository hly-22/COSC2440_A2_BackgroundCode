package Models.Customer;

import Models.Claim.Claim;

import java.util.ArrayList;
import java.util.List;

public class PolicyHolder extends Customer {
    private String policyOwner;
    private String insuranceCardNumber;
    private List<String> dependentList;

    public PolicyHolder(String policyOwner, String insuranceCardNumber, List<String> dependentList) {
        this.policyOwner = policyOwner;
        this.insuranceCardNumber = insuranceCardNumber;
        this.dependentList = dependentList;
    }

    public PolicyHolder(String cID, String role, String fullName, String phone, String address, String email, String password, List<String> actionHistory, List<Claim> claimList, String policyOwner, String insuranceCardNumber, List<String> dependentList) {
        super(cID, role, fullName, phone, address, email, password, actionHistory, claimList);
        this.policyOwner = policyOwner;
        this.insuranceCardNumber = insuranceCardNumber;
        this.dependentList = dependentList;
    }

    public PolicyHolder(String cID, String fullName, String phone, String address, String email, String password) {
        super(cID, "PolicyHolder", fullName, phone, address, email, password);
        this.policyOwner = null;
        this.insuranceCardNumber = null;
        this.dependentList = new ArrayList<>();
    }
    public PolicyHolder(String cID, String fullName, String phone, String address, String email, String password, String policyOwner) {
        super(cID, "PolicyHolder", fullName, phone, address, email, password);
        this.policyOwner = policyOwner;
        this.insuranceCardNumber = null;
        this.dependentList = new ArrayList<>();
    }

    public PolicyHolder() {
        this.dependentList = new ArrayList<>();
    }
    public PolicyHolder(String cID) {
        super(cID, "PolicyHolder", "Haha", "0980980980", "Mountaint", "aweij@gmai.com", "198274hj");
        this.policyOwner = null;
        this.insuranceCardNumber = null;
        this.dependentList = new ArrayList<>();
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

    public List<String> getDependentList() {
        return dependentList;
    }

    public void setDependentList(List<String> dependentList) {
        this.dependentList = dependentList;
    }

    public boolean addToDependentList(Dependent dependent) {
        return dependentList.add(dependent.getCID());
    }

    public boolean removeFromDependentList(Dependent dependent) {
        int indexToRemove = -1;
        for (int i = 0; i < dependentList.size(); i++) {
            if (dependentList.get(i).equals(dependent.getCID())) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            dependentList.remove(indexToRemove);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "PolicyHolder{" +
                super.toString() +
                ", policyOwner='" + policyOwner + '\'' +
                ", insuranceCardNumber='" + insuranceCardNumber + '\'' +
                ", dependentList=" + dependentList + '\'' +
                '}';
    }
}
