package Database;

import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Claim.Document;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.InsuranceCard.InsuranceCard;
import Models.Provider.InsuranceManager;

import java.math.BigDecimal;
import java.sql.*;
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
    public boolean deletePolicyOwner(String policyOwnerCID) {
        String sql = "DELETE FROM policy_owner WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyOwnerCID);

            int rowsDeleted = pstmt.executeUpdate();
            if (rowsDeleted > 0) {
                System.out.println("Policy Owner " + policyOwnerCID + " deleted successfully.");

                // Also delete associated policy holders and dependents
                deleteBeneficiaries(policyOwnerCID);

                return true;
            } else {
                System.out.println("Policy Owner " + policyOwnerCID + " does not exist.");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting policy owner", e);
        }
    }
    private void deleteBeneficiaries(String policyOwnerCID) throws SQLException {
        deletePolicyHolders(policyOwnerCID);
        deleteDependents(policyOwnerCID);
    }
    private void deletePolicyHolders(String policyOwnerCID) throws SQLException {
        String sql = "DELETE FROM policy_holder WHERE policy_owner = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyOwnerCID);
            pstmt.executeUpdate();
        }
    }
    private void deleteDependents(String policyOwnerCID) throws SQLException {
        String sql = "DELETE FROM dependent WHERE policy_owner = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyOwnerCID);
            pstmt.executeUpdate();
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
    public List<String> getBeneficiaries(String policyOwnerCID) {
        List<String> beneficiaries = new ArrayList<>();
        String sql = "SELECT beneficiaries FROM policy_owner WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyOwnerCID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Array beneficiariesArray = rs.getArray("beneficiaries");
                if (beneficiariesArray != null) {
                    beneficiaries = Arrays.asList((String[]) beneficiariesArray.getArray());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving beneficiaries from database", e);
        }
        return beneficiaries;
    }
    public boolean removeFromBeneficiaries(String policyOwnerCID, String beneficiaryCID) {
        boolean removed = false;
        if (isPolicyHolder(beneficiaryCID)) {
            removed = deletePolicyHolder(beneficiaryCID);
            if (removed) {
                try {
                    deleteDependentsOfPolicyHolder(beneficiaryCID);
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error deleting dependents of policy holder", e);
                }
            }
        } else if (isDependent(beneficiaryCID)) {
            removed = deleteDependent(beneficiaryCID);
        }
        return removed;
    }
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
                        claimList.add(readClaim(fID));
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
    public boolean deletePolicyHolder(String policyHolderCID) {
        String sql = "DELETE FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderCID);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting policy holder", e);
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
    public List<String> getDependentList(String policyHolderCID) {
        List<String> dependentList = new ArrayList<>();
        String sql = "SELECT dependent_list FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderCID);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                Array dependentArray = rs.getArray("dependent_list");
                if (dependentArray != null) {
                    dependentList = Arrays.asList((String[]) dependentArray.getArray());
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving dependent list from database", e);
        }
        return dependentList;
    }
    public boolean removeFromDependentList(String policyHolderCID, String dependentCID) {
        // Check if the given policyholder CID exists
        if (!checkPolicyHolderExists(policyHolderCID)) {
            System.out.println("Policy Holder with CID " + policyHolderCID + " does not exist.");
            return false;
        }

        // Retrieve the current dependent list of the policyholder from the database
        List<String> dependentList = getDependentList(policyHolderCID);

        // If the dependent list is null or empty, there's nothing to remove
        if (dependentList == null || dependentList.isEmpty()) {
            System.out.println("No dependents found for Policy Holder with CID " + policyHolderCID);
            return false;
        }

        // Remove the dependent CID from the dependent list
        boolean removed = dependentList.remove(dependentCID);

        if (removed) {
            // Update the database with the modified dependent list
            try (Connection conn = databaseConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement(
                         "UPDATE policy_holder SET dependent_list = ? WHERE c_id = ?")) {
                Array dependentArray = conn.createArrayOf("varchar", dependentList.toArray());
                pstmt.setArray(1, dependentArray);
                pstmt.setString(2, policyHolderCID);
                pstmt.executeUpdate();
                System.out.println("Dependent with CID " + dependentCID + " removed from Policy Holder with CID "
                        + policyHolderCID + "'s dependent list.");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error removing dependent from dependent list", e);
            }
        } else {
            System.out.println("Dependent with CID " + dependentCID + " is not in the dependent list of Policy Holder "
                    + "with CID " + policyHolderCID);
            return false;
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
    private boolean isPolicyHolder(String cID) {
        String sql = "SELECT EXISTS (SELECT 1 FROM policy_holder WHERE c_id = ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking if CID belongs to a policy holder", e);
        }
        return false;
    }

    // CRUD for dependents
    public boolean createDependent(Dependent dependent) {

        // Check if the policyholder exists
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
    public boolean deleteDependent(String dependentCID) {
        String sql = "DELETE FROM dependent WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dependentCID);
            int rowsDeleted = pstmt.executeUpdate();
            return rowsDeleted > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting dependent", e);
        }
    }
    public void deleteDependentsOfPolicyHolder(String policyHolderCID) throws SQLException {
        String sql = "DELETE FROM dependent WHERE policy_holder = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderCID);
            pstmt.executeUpdate();
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
    private boolean checkDependentExists(String dependentId) {
        String sql = "SELECT EXISTS (" +
                "    SELECT 1 FROM dependent WHERE c_id = ?" +
                ")";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dependentId);
            ResultSet rs = pstmt.executeQuery();
            if (rs.next()) {
                return rs.getBoolean(1); // Returns true if the cID exists in the policy_holder table, false otherwise
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }

        return false; // Return false by default if an exception occurs
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
    public List<Claim> getClaimList(String cID) {
        List<Claim> claimList = new ArrayList<>();
        String sql = "SELECT claim_list FROM customer WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String[] claimIDs = (String[]) rs.getArray("claim_list").getArray();
                    for (String claimID : claimIDs) {
                        Claim claim = readClaim(claimID);
                        if (claim != null) {
                            claimList.add(claim);
                        }
                    }
                }
            }
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
        }
        return claimList;
    }
    public boolean removeFromClaimList(String cID, String fID) {
        String tableName;
        if (checkPolicyHolderExists(cID)) {
            tableName = "policy_holder";
        } else if (checkDependentExists(cID)) {
            tableName = "dependent";
        } else {
            System.out.println("Customer with cID " + cID + " does not exist.");
            return false;
        }

        String sql = "UPDATE " + tableName + " SET claim_list = array_remove(claim_list, ?) WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fID);
            pstmt.setString(2, cID);

            int rowsUpdated = pstmt.executeUpdate();
            if (rowsUpdated > 0) {
                System.out.println("Claim " + fID + " removed from claim list of customer " + cID + " successfully.");
                return true;
            } else {
                System.out.println("Claim " + fID + " is not in the claim list of customer " + cID + ".");
                return false;
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error removing claim from claim list", e);
        }
    }
    private boolean isDependent(String cID) {
        String sql = "SELECT EXISTS (SELECT 1 FROM dependent WHERE c_id = ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return rs.getBoolean(1);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error checking if CID belongs to a dependent", e);
        }
        return false;
    }
    public Claim readClaim(String fID) throws SQLException {
        String sql = "SELECT * FROM claim WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractClaimFromResultSet(rs);
                }
            }
        }
        return null; // Return null if no claim found with the given fID
    }
    private Claim extractClaimFromResultSet(ResultSet rs) throws SQLException {
        String fID = rs.getString("f_id");
        LocalDate claimDate = rs.getDate("claim_date").toLocalDate();
        String insuredPerson = rs.getString("insured_person");
        InsuranceCard cardNumber = fetchInsuranceCard(rs.getString("card_number")); // Fetch insurance card details
        LocalDate examDate = rs.getDate("exam_date").toLocalDate();
        BigDecimal claimAmount = rs.getBigDecimal("claim_amount");
        ClaimStatus status = ClaimStatus.valueOf(rs.getString("status"));
        String receiverBankingInfo = rs.getString("receiver_banking_info");
        String note = rs.getString("note");

        // Retrieve document list from database
        List<Document> documentList = getDocumentListByFID(fID);

        // Create and return the Claim object
        return new Claim(fID, claimDate, insuredPerson, cardNumber, examDate, documentList, claimAmount, status, receiverBankingInfo, note);
    }
    private InsuranceCard fetchInsuranceCard(String cardNumber) {
        String sql = "SELECT * FROM insurance_card WHERE card_number = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    // Create and return the InsuranceCard object
                    return new InsuranceCard(
                            rs.getString("card_number"),
                            rs.getString("card_holder"),
                            rs.getString("policy_owner"),
                            rs.getDate("expiration_date").toLocalDate()
                    );
                } else {
                    System.out.println("No Insurance Card found with card number: " + cardNumber);
                    return null;
                }
            }
        } catch (SQLException e) {
            try {
                throw new SQLException("Error reading insurance card: " + e.getMessage(), e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    private List<Document> getDocumentListByFID(String fID) throws SQLException {
        List<Document> documentList = new ArrayList<>();
        String sql = "SELECT * FROM document WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Document document = new Document(
                            rs.getString("document_id"),
                            rs.getString("f_id"),
                            rs.getString("file_name"),
                            rs.getString("convert_file_name"),
                            rs.getString("url")
                    );
                    documentList.add(document);
                }
            }
        }
        return documentList;
    }

}
