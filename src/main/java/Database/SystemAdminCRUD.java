package Database;

import Models.SystemAdmin.SystemAdmin;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class SystemAdminCRUD {
    private final DatabaseConnection databaseConnection;
    private static final String SELECT_ADMIN_BY_ID = "SELECT * FROM system_admin WHERE id = ?";
    public SystemAdminCRUD(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    private String[] getAdminActionHistory(String ID) {
        String sql = "SELECT action_history FROM system_admin WHERE id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                java.sql.Array sqlArray = rs.getArray("action_history");
                if (sqlArray != null) {
                    return (String[]) sqlArray.getArray();
                } else {
                    return new String[]{};
                }
            } else {
                return new String[]{};
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateAdminActionHistory(String ID, String action) {
        // Retrieve current action history
        String[] currentHistory = getAdminActionHistory(ID);

        // Handle potential null currentHistory
        if (currentHistory == null) {
            currentHistory = new String[]{};
        }

        // Add new action to the history
        ArrayList<String> updatedHistory = new ArrayList<>(Arrays.asList(currentHistory));
        updatedHistory.add(action);

        // Update action history in the database
        String sql = "UPDATE system_admin SET action_history = ? WHERE id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setArray(1, conn.createArrayOf("varchar", updatedHistory.toArray(new String[0])));
            pstmt.setString(2, ID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public String getHashedPasswordFromDB(String ID) {
        String hashedPassword = null;
        String sql = "SELECT password FROM system_admin WHERE id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, ID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    hashedPassword = rs.getString("password");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving password from the database", e);
        }
        return hashedPassword;
    }
    public void updatePasswordInDB(String ID, String hashedPassword) {
        String sql = "UPDATE system_admin SET password = ? WHERE id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, ID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating password in the database", e);
        }
    }


    public SystemAdmin readAdmin(String adminID) {
        SystemAdmin admin = null;
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(SELECT_ADMIN_BY_ID)) {
            pstmt.setString(1, adminID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                String id = rs.getString("id");
                String password = rs.getString("password");
                List<String> actionHistory = getActionHistory(rs.getString("action_history"));
                admin = new SystemAdmin(id, password, actionHistory);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return admin;
    }

    private List<String> getActionHistory(String actionHistoryStr) {
        List<String> actionHistory = new ArrayList<>();
        if (actionHistoryStr != null && !actionHistoryStr.isEmpty()) {
            String[] actions = actionHistoryStr.split(",");
            for (String action : actions) {
                actionHistory.add(action);
            }
        }
        return actionHistory;
    }


}
