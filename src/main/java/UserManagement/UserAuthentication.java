package UserManagement;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;

import Database.DatabaseConnection;
import org.mindrot.jbcrypt.BCrypt;

public class UserAuthentication {
    private DatabaseConnection databaseConnection;

    public UserAuthentication(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean authenticateUser(String userID, String password) {
        String role = getUserRole(userID);
        if (role == null) {
            return false;
        }
        String sql = getSqlQueryForRole(role);

        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String storedHash = rs.getString("password");
                    return BCrypt.checkpw(password, storedHash);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error authenticating user", e);
        }
        return false;
    }

    public String getUserRole(String userID) {
        String[] roles = {"policy_holder", "policy_owner", "dependent", "system_admin", "insurance_manager", "insurance_surveyor"};
        for (String role : roles) {
            if (checkUserExistsInRole(userID, role)) {
                return role;
            }
        }
        return null;
    }

    private boolean checkUserExistsInRole(String userID, String role) {
        String sql = getSqlQueryForRole(role).replace("SELECT password", "SELECT 1");
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, userID);
            try (ResultSet rs = pstmt.executeQuery()) {
                return rs.next();
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking user role", e);
        }
    }

    private String getSqlQueryForRole(String role) {
        switch (role) {
            case "policy_holder":
            case "policy_owner":
            case "dependent":
                return "SELECT password FROM " + role + " WHERE c_id = ?";
            case "system_admin":
                return "SELECT password FROM system_admin WHERE id = ?";
            case "insurance_manager":
            case "insurance_surveyor":
                return "SELECT password FROM " + role + " WHERE p_id = ?";
            default:
                return null;
        }
    }
    public static String hashPassword(String password) {
        return BCrypt.hashpw(password, BCrypt.gensalt());
    }

    public static boolean checkPassword(String inputPassword, String hashedPassword) {
        return BCrypt.checkpw(inputPassword, hashedPassword);
    }
}
