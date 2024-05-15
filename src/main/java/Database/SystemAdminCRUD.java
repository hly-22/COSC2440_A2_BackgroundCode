package Database;

import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.Arrays;

public class SystemAdminCRUD {

    private final DatabaseConnection databaseConnection;

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


}
