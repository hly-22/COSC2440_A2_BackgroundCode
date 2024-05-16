package Database;

import Models.Claim.Claim;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.Provider.InsuranceManager;

import java.math.BigDecimal;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class CustomerCRUD {
    private final DatabaseConnection databaseConnection;

    public CustomerCRUD(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    // CRUD for policy owners
    public boolean createPolicyOwner(PolicyOwner policyOwner) {
        String sql = "INSERT INTO policy_owner (c_id, role, full_name, phone, address, email, password, action_history, beneficiaries, insurance_fee) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyOwner.getCID());
            pstmt.setString(2, policyOwner.getRole());
            pstmt.setString(3, policyOwner.getFullName());
            pstmt.setString(4, policyOwner.getPhone());
            pstmt.setString(5, policyOwner.getAddress());
            pstmt.setString(6, policyOwner.getEmail());
            pstmt.setString(7, policyOwner.getPassword());
            pstmt.setArray(8, conn.createArrayOf("varchar", policyOwner.getActionHistory().toArray()));
            pstmt.setArray(9, conn.createArrayOf("varchar", policyOwner.getBeneficiaries().toArray()));
            pstmt.setBigDecimal(10, policyOwner.getInsuranceFee());
            pstmt.executeUpdate();
            System.out.println("Policy Owner created successfully!");
            return true;
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            System.out.println(sqlState);
            if ("23505".equals(sqlState) || "P0001".equals(sqlState)) { // Unique violation or custom exception
                System.out.println("Duplicate cID detected: " + policyOwner.getCID());
            } else {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return false;
        }
    }
    public PolicyOwner readPolicyOwner(String cID) throws SQLException {
        String sql = "SELECT * FROM policy_owner WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String fullName = rs.getString("full_name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    String email = rs.getString("email");
                    String password = rs.getString("password");
                    String[] actionHistory = (String[]) rs.getArray("action_history").getArray();
                    String[] beneficiaries = (String[]) rs.getArray("beneficiaries").getArray();
                    BigDecimal insuranceFee = rs.getBigDecimal("insurance_fee");

                    return new PolicyOwner(cID, role, fullName, phone, address, email, password, Arrays.asList(actionHistory), null, Arrays.asList(beneficiaries), insuranceFee);
                } else {
                    System.out.println("No Policy Owner found with cID: " + cID);
                    return null;
                }
            }
        }
    }
    public void updatePolicyOwner(String pID, String phone, String address, String email, String password) throws SQLException {
        String sql = "UPDATE policy_owner SET phone = ?, address = ?, email = ?, password = ? WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, address);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, pID);
            pstmt.executeUpdate();
        }
    }
    public void deletePolicyOwner(String cID) throws SQLException {
        String sql = "DELETE FROM policy_owner WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            int affectedRows = pstmt.executeUpdate();
            if (affectedRows > 0) {
                System.out.println("Policy Owner deleted successfully!");
            } else {
                System.out.println("No Policy Owner found with cID: " + cID);
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error deleting Policy Owner", e);
        }
    }
    public void updatePolicyOwnerActionHistory(String policyOwnerId, String action) {
        String sql = "UPDATE policy_owner SET action_history = array_append(action_history, ?) WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, action);
            pstmt.setString(2, policyOwnerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void addToBeneficiaries(String policyOwnerId, String newBeneficiaryId) {
        String sql = "UPDATE policy_owner SET beneficiaries = array_append(beneficiaries, ?) WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newBeneficiaryId);
            pstmt.setString(2, policyOwnerId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }




//    public Claim readClaim(String fID) throws SQLException {
//        String sql = "SELECT * FROM claim WHERE f_id = ?";
//        try (Connection conn = databaseConnection.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, fID);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    LocalDate claimDate = rs.getObject("claim_date", LocalDate.class);
//                    String cID = rs.getString("c_id");
//                    String insuredPerson = rs.getString("insured_person");
//                    String cardNumber = rs.getString("card_number");
//                    LocalDate examDate = rs.getObject("exam_date", LocalDate.class);
//                    BigDecimal claimAmount = rs.getBigDecimal("claim_amount");
//                    String status = rs.getString("status");
//                    String receiverBankingInfo = rs.getString("receiver_banking_info");
//
//                    return new Claim(fID, claimDate, cID, insuredPerson, cardNumber, examDate, claimAmount, status, receiverBankingInfo);
//                } else {
//                    System.out.println("No Claim found with fID: " + fID);
//                    return null;
//                }
//            }
//        }
//    }

    private boolean checkPolicyOwnerExists(String policyOwnerCID) {
        String sql = "SELECT EXISTS (" +
                "    SELECT 1 FROM policy_owner WHERE c_id = ?" +
                ")";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyOwnerCID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1); // Returns true if the cID exists in the policy_owner table, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return false; // Return false by default if an exception occurs
    }

    // CRUD for policyholders
    public boolean createPolicyHolder(PolicyHolder policyHolder) {
        // Check if the policy owner exists
        String policyOwnerCID = policyHolder.getPolicyOwner();
        if (!checkPolicyOwnerExists(policyOwnerCID)) {
            System.out.println("Policy Owner with cID " + policyOwnerCID + " does not exist.");
            return false;
        }

        String sql = "INSERT INTO policy_holder (c_id, role, full_name, phone, address, email, password, action_history, claim_list, policy_owner, insurance_card_number, dependent_list) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolder.getCID());
            pstmt.setString(2, policyHolder.getRole());
            pstmt.setString(3, policyHolder.getFullName());
            pstmt.setString(4, policyHolder.getPhone());
            pstmt.setString(5, policyHolder.getAddress());
            pstmt.setString(6, policyHolder.getEmail());
            pstmt.setString(7, policyHolder.getPassword());
            // Handle action history
            List<String> actionHistory = policyHolder.getActionHistory();
            if (actionHistory == null || actionHistory.isEmpty()) {
                pstmt.setArray(8, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(8, conn.createArrayOf("varchar", actionHistory.toArray()));
            }
            // Handle claim list
            List<String> claimIDs = new ArrayList<>();
            List<Claim> claimList = policyHolder.getClaimList();
            if (claimList != null) {
                for (Claim claim : claimList) {
                    claimIDs.add(claim.getFID());
                }
            }
            if (claimIDs.isEmpty()) {
                pstmt.setArray(9, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(9, conn.createArrayOf("varchar", claimIDs.toArray(new String[0])));
            }
            pstmt.setString(10, policyHolder.getPolicyOwner());
            pstmt.setString(11, policyHolder.getInsuranceCardNumber());
            // Handle dependent list
            List<String> dependentList = policyHolder.getDependentList();
            if (dependentList == null || dependentList.isEmpty()) {
                pstmt.setArray(12, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(12, conn.createArrayOf("varchar", dependentList.toArray()));
            }

            pstmt.executeUpdate();

            System.out.println("Policy Holder created successfully!");
            return true;
        } catch (SQLException e) {
            String sqlState = e.getSQLState();
            if ("23505".equals(sqlState) || "P0001".equals(sqlState)) { // Unique violation or custom exception
                System.out.println("Duplicate cID detected: " + policyHolder.getCID());
            } else {
                e.printStackTrace();
                throw new RuntimeException(e);
            }
            return false;
        }
    }
    public PolicyHolder getPolicyHolder(String cID) throws SQLException {
        String sql = "SELECT * FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String role = rs.getString("role");
                    String fullName = rs.getString("full_name");
                    String phone = rs.getString("phone");
                    String address = rs.getString("address");
                    String email = rs.getString("email");
                    String password = rs.getString("password");

                    String[] actionHistoryArray = (String[]) rs.getArray("action_history").getArray();
                    List<String> actionHistory = Arrays.asList(actionHistoryArray);

                    String[] claimListArray = (String[]) rs.getArray("claim_list").getArray();
                    List<Claim> claimList = new ArrayList<>();
                    for (String fID : claimListArray) {
//                        claimList.add(getClaim(fID)); // Assumes a method getClaim(String fID) exists
                    }

                    String policyOwner = rs.getString("policy_owner");
                    String insuranceCardNumber = rs.getString("insurance_card_number");

                    String[] dependentListArray = (String[]) rs.getArray("dependent_list").getArray();
                    List<String> dependentList = Arrays.asList(dependentListArray);

                    return new PolicyHolder(cID, role, fullName, phone, address, email, password, actionHistory, claimList, policyOwner, insuranceCardNumber, dependentList);
                } else {
                    System.out.println("No Policy Holder found with cID: " + cID);
                    return null;
                }
            }
        }
    }

    public void deletePolicyHolder(String cID) throws SQLException {
        String sql = "DELETE FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            pstmt.executeUpdate();
        }
    }
    public void addToDependentList(String policyHolderId, String newDependentId) {
        String sql = "UPDATE policy_holder SET dependent_list = array_append(dependent_list, ?) WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newDependentId);
            pstmt.setString(2, policyHolderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public void updatePolicyHolderActionHistory(String policyHolderId, String action) {
        String sql = "UPDATE policy_holder SET action_history = array_append(action_history, ?) WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, action);
            pstmt.setString(2, policyHolderId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    private boolean checkPolicyHolderExists(String policyHolderCID) {
        String sql = "SELECT EXISTS (" +
                "    SELECT 1 FROM policy_holder WHERE c_id = ?" +
                ")";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderCID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1); // Returns true if the cID exists in the policy_holder table, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return false; // Return false by default if an exception occurs
    }

    // CRUD for dependents
    public boolean createDependent(Dependent dependent) {

        // Check if the policy holder exists
        String policyHolderCID = dependent.getPolicyHolder();
        if (!checkPolicyHolderExists(policyHolderCID)) {
            System.out.println("Policy Holder with cID " + policyHolderCID + " does not exist.");
            return false;
        }

        String sql = "INSERT INTO dependent (c_id, role, full_name, phone, address, email, password, action_history, claim_list, policy_owner, policy_holder, insurance_card_number) VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dependent.getCID());
            pstmt.setString(2, dependent.getRole());
            pstmt.setString(3, dependent.getFullName());
            pstmt.setString(4, dependent.getPhone());
            pstmt.setString(5, dependent.getAddress());
            pstmt.setString(6, dependent.getEmail());
            pstmt.setString(7, dependent.getPassword());
            // Handle action history
            List<String> actionHistory = dependent.getActionHistory();
            if (actionHistory == null || actionHistory.isEmpty()) {
                pstmt.setArray(8, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(8, conn.createArrayOf("varchar", actionHistory.toArray()));
            }
            // Handle claim list
            List<String> claimIDs = new ArrayList<>();
            List<Claim> claimList = dependent.getClaimList();
            if (claimList != null) {
                for (Claim claim : claimList) {
                    claimIDs.add(claim.getFID());
                }
            }
            if (claimIDs.isEmpty()) {
                pstmt.setArray(9, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(9, conn.createArrayOf("varchar", claimIDs.toArray(new String[0])));
            }
            pstmt.setString(10, dependent.getPolicyOwner());
            pstmt.setString(11, dependent.getPolicyHolder());
            pstmt.setString(12, dependent.getInsuranceCardNumber());

            int rowsInserted = pstmt.executeUpdate();
            return rowsInserted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public void updateDependentActionHistory(String dependentId, String action) {
        String sql = "UPDATE dependent SET action_history = array_append(action_history, ?) WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, action);
            pstmt.setString(2, dependentId);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }

    public void addToClaimList(String cID, String fID) {
        try (Connection conn = databaseConnection.connect()) {
            // Check in policy_holder table
            String policyHolderQuery = "SELECT c_id FROM policy_holder WHERE c_id = ?";
            try (PreparedStatement pstmtPolicyHolder = conn.prepareStatement(policyHolderQuery)) {
                pstmtPolicyHolder.setString(1, cID);
                try (ResultSet rsPolicyHolder = pstmtPolicyHolder.executeQuery()) {
                    if (rsPolicyHolder.next()) {
                        updateClaimList(conn, "policy_holder", cID, fID);
                        return;
                    }
                }
            }

            // Check in dependent table
            String dependentQuery = "SELECT c_id FROM dependent WHERE c_id = ?";
            try (PreparedStatement pstmtDependent = conn.prepareStatement(dependentQuery)) {
                pstmtDependent.setString(1, cID);
                try (ResultSet rsDependent = pstmtDependent.executeQuery()) {
                    if (rsDependent.next()) {
                        updateClaimList(conn, "dependent", cID, fID);
                        return;
                    }
                }
            }

            System.out.println("No entry found for cID: " + cID);
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException(e);
        }
    }

    private void updateClaimList(Connection conn, String tableName, String cID, String fID) throws SQLException {
        String updateQuery = "UPDATE " + tableName + " SET claim_list = array_append(claim_list, ?) WHERE c_id = ?";
        try (PreparedStatement pstmt = conn.prepareStatement(updateQuery)) {
            pstmt.setString(1, fID);
            pstmt.setString(2, cID);
            pstmt.executeUpdate();
            System.out.println("Added claim " + fID + " to " + tableName + " with cID: " + cID);

            if ("policy_holder".equals(tableName)) {
                updatePolicyHolderActionHistory(cID, LocalDate.now() + ": add Claim " + fID + " by Policy Owner");
            } else if ("dependent".equals(tableName)) {
                updateDependentActionHistory(cID, LocalDate.now() + ": add Claim " + fID + " by Policy Owner");
            }
        }
    }

}
