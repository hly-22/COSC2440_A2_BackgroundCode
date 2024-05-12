package Models.SystemAdmin;

import java.util.ArrayList;
import java.util.List;

public class SystemAdmin {
    private String username;
    private String password;
    private List<String> actionHistory;

    public SystemAdmin() {
        this.actionHistory = new ArrayList<>();
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
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
        return "SystemAdmin{" +
                "username='" + username + '\'' +
                ", password='" + password + '\'' +
                ", actionHistory=" + actionHistory +
                '}';
    }
}
