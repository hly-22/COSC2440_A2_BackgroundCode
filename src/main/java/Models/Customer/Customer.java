/**
 * @author <Group 13>
 */

package Models.Customer;

import Models.Claim.Claim;

import java.util.ArrayList;
import java.util.List;

public abstract class Customer {
    private String cID;
    private String role;
    private String fullName;
    private String phone;
    private String address;
    private String email;
    private String password;
    private List<String> actionHistory;
    private List<Claim> claimList;

    public Customer() {}
    public Customer(String cID, String role, String fullName, String phone, String address, String email, String password, List<String> actionHistory, List<Claim> claimList) {
        this.cID = cID;
        this.role = role;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.actionHistory = actionHistory;
        this.claimList = claimList;
    }
    public Customer(String cID, String role, String fullName, String phone, String address, String email, String password) {
        this.cID = cID;
        this.role = role;
        this.fullName = fullName;
        this.phone = phone;
        this.address = address;
        this.email = email;
        this.password = password;
        this.actionHistory = new ArrayList<>();
        this.claimList = new ArrayList<>();
    }

    public String getCID() {
        return cID;
    }

    public void setCID(String cID) {
        this.cID = cID;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public List<String> getActionHistory() {
        return actionHistory;
    }

    public void setActionHistory(List<String> actionHistory) {
        this.actionHistory = actionHistory;
    }

    public List<Claim> getClaimList() {
        return claimList;
    }

    public void setClaimList(List<Claim> claimList) {
        this.claimList = claimList;
    }

    @Override
    public String toString() {
        return "cID='" + cID + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                ", phone='" + phone + '\'' +
                ", address='" + address + '\'' +
                ", email='" + email + '\'' +
                ", password='" + password + '\'' +
                ", actionHistory=" + actionHistory + '\'' +
                ", claimList=" + claimList + '\'';
    }
}
