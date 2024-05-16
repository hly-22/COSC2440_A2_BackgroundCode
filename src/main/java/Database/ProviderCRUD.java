package Database;

import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;

import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ProviderCRUD {
    private final DatabaseConnection databaseConnection;

    public ProviderCRUD(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public List<String> getActionHistory(String cID) {
        List<String> actionHistory = new ArrayList<>();
        String sql = "SELECT action_history FROM customer WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Array actionHistoryArray = rs.getArray("action_history");
                if (actionHistoryArray != null) {
                    actionHistory = Arrays.asList((String[]) actionHistoryArray.getArray());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving action history from database", e);
        }
        return actionHistory;
    }
    // CRUD for insurance managers
    public boolean createInsuranceManager(InsuranceManager insuranceManager) {
        String sql = "INSERT INTO insurance_manager (p_id, role, full_name, password, action_history, surveyor_list) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insuranceManager.getPID());
            pstmt.setString(2, insuranceManager.getRole());
            pstmt.setString(3, insuranceManager.getFullName());
            pstmt.setString(4, insuranceManager.getPassword());
            // Handle action history
            List<String> actionHistory = insuranceManager.getActionHistory();
            if (actionHistory == null || actionHistory.isEmpty()) {
                pstmt.setArray(5, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(5, conn.createArrayOf("varchar", actionHistory.toArray()));
            }

            // Handle surveyor list
            List<String> surveyorList = insuranceManager.getSurveyorList();
            if (surveyorList == null || surveyorList.isEmpty()) {
                pstmt.setArray(6, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(6, conn.createArrayOf("varchar", surveyorList.toArray()));
            }

            pstmt.executeUpdate();
            System.out.println("Insurance Manager created successfully!");
            return true;
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if ("23505".equals(sqlState) || "P0001".equals(sqlState)) { // Unique violation or custom exception
                System.out.println("Duplicate pID detected: " + insuranceManager.getPID());
            } else {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return false;
        }
    }
    public InsuranceManager readInsuranceManager(String pID) throws SQLException {
        String sql = "SELECT * FROM insurance_manager WHERE p_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String fullName = rs.getString("full_name");
                    String password = rs.getString("password");
                    String[] actionHistory = (String[]) rs.getArray("action_history").getArray();
                    String[] surveyorList = (String[]) rs.getArray("surveyor_list").getArray();
                    return new InsuranceManager(pID, role, fullName, password, Arrays.asList(actionHistory), Arrays.asList(surveyorList));
                } else {
                    System.out.println("No Insurance Manager found with pID: " + pID);
                    return null;
                }
            }
        }
    }
    public void updateInsuranceManager(String pID, String newPassword) throws SQLException {
        String sql = "UPDATE insurance_manager SET password = ? WHERE p_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, pID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating insurance manager failed, no rows affected.");
            }
        }
    }
    public void deleteInsuranceManager(String pID) throws SQLException {
        String deleteSurveyorsSQL = "DELETE FROM insurance_surveyor WHERE insurance_manager = ?";
        String deleteManagerSQL = "DELETE FROM insurance_manager WHERE p_id = ?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement deleteSurveyorsStmt = conn.prepareStatement(deleteSurveyorsSQL);
             PreparedStatement deleteManagerStmt = conn.prepareStatement(deleteManagerSQL)) {

            // Start transaction
            conn.setAutoCommit(false);

            // Delete associated InsuranceSurveyors
            deleteSurveyorsStmt.setString(1, pID);
            deleteSurveyorsStmt.executeUpdate();

            // Delete InsuranceManager
            deleteManagerStmt.setString(1, pID);
            int affectedRows = deleteManagerStmt.executeUpdate();

            if (affectedRows == 0) {
                conn.rollback();
                throw new SQLException("Deleting insurance manager failed, no rows affected.");
            }

            // Commit transaction
            conn.commit();
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Insurance Manager and their Insurance Surveyors", e);
        }
    }
    private String[] getManagerActionHistory(String pID) {
        String sql = "SELECT action_history FROM insurance_manager WHERE p_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return (String[]) rs.getArray("action_history").getArray();
            } else {
                return new String[]{};
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateManagerActionHistory(String pID, String action) {
        // Retrieve current action history
        String[] currentHistory = getManagerActionHistory(pID);

        // Add new action to the history
        ArrayList<String> updatedHistory = new ArrayList<>(Arrays.asList(currentHistory));
        updatedHistory.add(action);

        // Update action history in the database
        String sql = "UPDATE insurance_manager SET action_history = ? WHERE p_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setArray(1, conn.createArrayOf("varchar", updatedHistory.toArray()));
            pstmt.setString(2, pID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }


    // CRUD for insurance surveyors
    public boolean createInsuranceSurveyor(InsuranceSurveyor insuranceSurveyor) {
        // Check if the insurance manager exists
        String insuranceManagerPID = insuranceSurveyor.getInsuranceManager();
        if (!checkInsuranceManagerExists(insuranceManagerPID)) {
            System.out.println("Insurance Manager with pID " + insuranceManagerPID + " does not exist.");
            return false;
        }

        String sql = "INSERT INTO insurance_surveyor (p_id, role, full_name, password, action_history, insurance_manager) VALUES (?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insuranceSurveyor.getPID());
            pstmt.setString(2, insuranceSurveyor.getRole());
            pstmt.setString(3, insuranceSurveyor.getFullName());
            pstmt.setString(4, insuranceSurveyor.getPassword());
            // Handle action history
            List<String> actionHistory = insuranceSurveyor.getActionHistory();
            if (actionHistory == null || actionHistory.isEmpty()) {
                pstmt.setArray(5, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(5, conn.createArrayOf("varchar", actionHistory.toArray()));
            }

            pstmt.setString(6, insuranceManagerPID);

            pstmt.executeUpdate();
            addSurveyorToList(insuranceManagerPID, insuranceSurveyor.getPID());

            System.out.println("Insurance Surveyor created successfully!");
            return true;
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if ("23505".equals(sqlState) || "P0001".equals(sqlState)) { // Unique violation or custom exception
                System.out.println("Duplicate pID detected: " + insuranceSurveyor.getPID());
            } else {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return false;
        }
    }
    public InsuranceSurveyor readInsuranceSurveyor(String pID) throws SQLException {
        String sql = "SELECT * FROM insurance_surveyor WHERE p_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String fullName = rs.getString("full_name");
                    String password = rs.getString("password");
                    String[] actionHistory = (String[]) rs.getArray("action_history").getArray();
                    String insuranceManager = rs.getString("insurance_manager");
                    return new InsuranceSurveyor(pID, role, fullName, password, Arrays.asList(actionHistory), insuranceManager);
                } else {
                    System.out.println("No Insurance Surveyor found with pID: " + pID);
                    return null;
                }
            }
        }
    }
    public void updateInsuranceSurveyor(String pID, String newPassword) throws SQLException {
        String sql = "UPDATE insurance_surveyor SET password = ? WHERE p_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newPassword);
            pstmt.setString(2, pID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows == 0) {
                throw new SQLException("Updating insurance surveyor failed, no rows affected.");
            }
        }
    }
    public void deleteInsuranceSurveyor(String pID) throws SQLException {
        String sql = "DELETE FROM insurance_surveyor WHERE p_id = ?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pID);
            int affectedRows = pstmt.executeUpdate();

            if (affectedRows == 0) {
                throw new SQLException("Deleting insurance surveyor failed, no rows affected.");
            }

            // Remove pID from the surveyor_list of the corresponding InsuranceManager
            removeSurveyorFromList(pID);
        }
    }
    private boolean checkInsuranceManagerExists(String insuranceManagerPID) {
        String sql = "SELECT EXISTS (" +
                "    SELECT 1 FROM insurance_manager WHERE p_id = ?" +
                ")";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insuranceManagerPID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1); // Returns true if the pID exists in the insurance_manager table, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return false; // Return false by default if an exception occurs
    }
    public void addSurveyorToList(String managerID, String surveyorID) {
        String sql = "UPDATE insurance_manager SET surveyor_list = array_append(surveyor_list, ?) WHERE p_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, surveyorID);
            pstmt.setString(2, managerID);
            pstmt.executeUpdate();
            System.out.println("Surveyor added to list successfully!");
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<String> getSurveyorList(String insuranceManagerPID) {
        List<String> surveyorList = new ArrayList<>();
        String sql = "SELECT surveyor_list FROM insurance_manager WHERE p_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insuranceManagerPID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Array surveyorArray = rs.getArray("surveyor_list");
                if (surveyorArray != null) {
                    surveyorList = Arrays.asList((String[]) surveyorArray.getArray());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving surveyor list from database", e);
        }
        return surveyorList;
    }
    private void removeSurveyorFromList(String surveyorID) throws SQLException {
        String selectSQL = "SELECT p_id, surveyor_list FROM insurance_manager WHERE ? = ANY(surveyor_list)";
        String updateSQL = "UPDATE insurance_manager SET surveyor_list = ? WHERE p_id = ?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement selectPstmt = conn.prepareStatement(selectSQL);
             PreparedStatement updatePstmt = conn.prepareStatement(updateSQL)) {
            selectPstmt.setString(1, surveyorID);
            ResultSet rs = selectPstmt.executeQuery();

            if (rs.next()) {
                String managerID = rs.getString("p_id");
                Array surveyorListArray = rs.getArray("surveyor_list");
                String[] surveyorList = (String[]) surveyorListArray.getArray();

                List<String> updatedSurveyorList = new ArrayList<>(Arrays.asList(surveyorList));
                updatedSurveyorList.remove(surveyorID);

                updatePstmt.setArray(1, conn.createArrayOf("varchar", updatedSurveyorList.toArray()));
                updatePstmt.setString(2, managerID);
                updatePstmt.executeUpdate();

                updateManagerActionHistory(managerID, LocalDate.now() + ": delete Insurance Surveyor " + surveyorID + " by System Admin");
            }
        }
    }
    private String[] getSurveyorActionHistory(String pID) {
        String sql = "SELECT action_history FROM insurance_surveyor WHERE p_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, pID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return (String[]) rs.getArray("action_history").getArray();
            } else {
                return new String[]{};
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updateSurveyorActionHistory(String pID, String action) {
        // Retrieve current action history
        String[] currentHistory = getSurveyorActionHistory(pID);

        // Add new action to the history
        ArrayList<String> updatedHistory = new ArrayList<>(Arrays.asList(currentHistory));
        updatedHistory.add(action);

        // Update action history in the database
        String sql = "UPDATE insurance_surveyor SET action_history = ? WHERE p_id = ?";
        try (Connection conn = databaseConnection.getConnection();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setArray(1, conn.createArrayOf("varchar", updatedHistory.toArray()));
            pstmt.setString(2, pID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
}
