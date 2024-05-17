package UserManagement;

public class SessionManager {
    private String currentUserID;
    private String currentUserRole;

    public void createSession(String userID, String userRole) {
        this.currentUserID = userID;
        this.currentUserRole = userRole;
    }

    public void invalidateSession() {
        this.currentUserID = null;
        this.currentUserRole = null;
    }

    public boolean isAuthenticated() {
        return this.currentUserID != null && this.currentUserRole != null;
    }

    public String getCurrentUserID() {
        return this.currentUserID;
    }

    public String getCurrentUserRole() {
        return this.currentUserRole;
    }
}
