package Models.SystemAdmin;

import java.util.ArrayList;
import java.util.List;

public class SystemAdmin {
    private String id;
    private String password;
    private List<String> actionHistory;

    public SystemAdmin() {
        this.actionHistory = new ArrayList<>();
    }

    public SystemAdmin(String id, String password, List<String> actionHistory) {
        this.id = id;
        this.password = password;
        this.actionHistory = actionHistory;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
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
    public void addActionHistory(String action) {
        actionHistory.add(action);
    }

    @Override
    public String toString() {
        return "SystemAdmin{" +
                "id='" + id + '\'' +
                ", password='" + password + '\'' +
                ", actionHistory=" + actionHistory +
                '}';
    }
}
