package Models.Provider;

import java.util.ArrayList;
import java.util.List;

public abstract class Provider {
    private String pID;
    private String role;
    private String fullName;
    private String password;
    private List<String> actionHistory;
    public Provider() {}

    public Provider(String pID, String role, String fullName, String password, List<String> actionHistory) {
        this.pID = pID;
        this.role = role;
        this.fullName = fullName;
        this.password = password;
        this.actionHistory = actionHistory;
    }

    public Provider(String pID, String role, String fullName, String password) {
        this.pID = pID;
        this.role = role;
        this.fullName = fullName;
        this.password = password;
        this.actionHistory = new ArrayList<>();
    }

    public String getPID() {
        return pID;
    }

    public void setPID(String pID) {
        this.pID = pID;
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

    @Override
    public String toString() {
        return "pID='" + pID + '\'' +
                ", role='" + role + '\'' +
                ", fullName='" + fullName + '\'' +
                ", password='" + password + '\'' +
                ", actionHistory=" + actionHistory;
    }
}
