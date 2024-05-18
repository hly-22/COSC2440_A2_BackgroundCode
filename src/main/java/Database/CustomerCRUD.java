package Database;

import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Claim.Document;
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.InsuranceCard.InsuranceCard;

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
    public PolicyOwner readPolicyOwner(String cID) {
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<PolicyOwner> getAllPolicyOwners() {
        List<PolicyOwner> policyOwners = new ArrayList<>();
        String sql = "SELECT c_id FROM policy_owner";
        try (Connection conn = databaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String cID = rs.getString("c_id");
                PolicyOwner policyOwner = readPolicyOwner(cID);
                if (policyOwner != null) {
                    policyOwners.add(policyOwner);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all policy owners", e);
        }
        return policyOwners;
    }
    public void updatePolicyOwner(String cID, String phone, String address, String email, String password) throws SQLException {
        String sql = "UPDATE policy_owner SET phone = ?, address = ?, email = ?, password = ? WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, address);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, cID);
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

                // Also delete associated policyholders and dependents
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
    private boolean removeFromBeneficiaries(String beneficiaryCID) {

        // Retrieve the list of beneficiaries from the database
        PolicyOwner policyOwner = readPolicyOwner(getPolicyOwnerCIDFromBeneficiary(beneficiaryCID));
        List<String> beneficiaries = new ArrayList<>(policyOwner.getBeneficiaries());

        // If the list is not null and contains the beneficiaryCID, remove it
        if (beneficiaries != null && beneficiaries.contains(beneficiaryCID)) {
            beneficiaries.remove(beneficiaryCID);

            // Update the list of beneficiaries in the database
            try (Connection conn = databaseConnection.connect();
                 PreparedStatement pstmt = conn.prepareStatement("UPDATE policy_owner SET beneficiaries = ? WHERE c_id = ?")) {
                Array beneficiariesArray = conn.createArrayOf("varchar", beneficiaries.toArray());
                pstmt.setArray(1, beneficiariesArray);
                pstmt.setString(2, policyOwner.getCID());
                pstmt.executeUpdate();
                System.out.println("Beneficiary with CID " + beneficiaryCID + " removed from the beneficiaries list.");
                return true;
            } catch (SQLException e) {
                e.printStackTrace();
                throw new RuntimeException("Error removing beneficiary from beneficiaries list", e);
            }
        } else {
            System.out.println("Beneficiary with CID " + beneficiaryCID + " is not in the beneficiaries list.");
            return false;
        }
    }
    public String getPolicyOwnerFromDependent(String cID) {
        String policyOwnerCID = null;
        String sql = "SELECT policy_owner FROM dependent WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    policyOwnerCID = rs.getString("policy_owner");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting policy owner from dependent", e);
        }
        return policyOwnerCID;
    }
    private String getPolicyOwnerFromPolicyHolder(String cID) {
        String policyOwnerCID = null;
        String sql = "SELECT policy_owner FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    policyOwnerCID = rs.getString("policy_owner");
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving policy owner CID from policy holder", e);
        }
        return policyOwnerCID;
    }
    private String getPolicyOwnerCIDFromBeneficiary(String beneficiaryCID) {
        // Look for the policy owner in the dependent table
        String policyOwnerCID = getPolicyOwnerFromDependent(beneficiaryCID);
        if (policyOwnerCID != null) {
            return policyOwnerCID;
        }

        // Look for the policy owner in the policy holder table
        policyOwnerCID = getPolicyOwnerFromPolicyHolder(beneficiaryCID);
        if (policyOwnerCID != null) {
            return policyOwnerCID;
        }

        // Return null if the beneficiary CID is not associated with any policy owner
        return null;
    }

    // CRUD for policyholders
    public boolean createPolicyHolder(PolicyHolder policyHolder) {
        // Check if the policy owner exists
        String policyOwnerCID = policyHolder.getPolicyOwner();
        if (!checkEntityExists("policy_owner", policyOwnerCID)) {
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
    public PolicyHolder getPolicyHolder(String cID){
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
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<PolicyHolder> getAllPolicyHolders() {
        List<PolicyHolder> policyHolders = new ArrayList<>();
        String sql = "SELECT c_id FROM policy_holder";
        try (Connection conn = databaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String cID = rs.getString("c_id");
                PolicyHolder policyHolder = getPolicyHolder(cID);
                if (policyHolder != null) {
                    policyHolders.add(policyHolder);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all policy holders", e);
        }
        return policyHolders;
    }
    public void updatePolicyHolder(String cID, String phone, String address, String email, String password) throws SQLException {
        String sql = "UPDATE policy_holder SET phone = ?, address = ?, email = ?, password = ? WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, address);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, cID);
            pstmt.executeUpdate();
        }
    }
    public boolean deletePolicyHolder(String cID) {
        // Check if the policy holder exists in the policy holder table
        if (!checkEntityExists("policy_holder", cID)) {
            System.out.println("Policy Holder with CID " + cID + " does not exist.");
            return false;
        }

        // Retrieve the policy holder's dependents and delete from the database
        List<String> dependentList = getDependentList(cID);
        boolean dependentsDeleted = deleteDependents(dependentList);

        // Remove the policy holder from the beneficiaries of policy owners
        boolean removedFromBeneficiaries = removeFromBeneficiaries(cID);

        // Remove the insurance card associated with the policy holder
        boolean removedInsuranceCard = deleteInsuranceCard(cID);

        // Delete the policy holder
        boolean policyHolderDeleted = deletePolicyHolderFromTable(cID);


        // Return true only if all removal operations and deletion from the table were successful
        return policyHolderDeleted && dependentsDeleted && removedFromBeneficiaries && removedInsuranceCard;
    }

    private boolean deletePolicyHolderFromTable(String cID) {
        // Delete the policy holder from the policy holder table
        String sql = "DELETE FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting policy holder from the policy holder table", e);
        }
    }

    private boolean deleteDependents(List<String> dependentList) {
        // Delete all dependents associated with the policyholder
        boolean allDeleted = true;
        for (String dependentCID : dependentList) {
            boolean deleted = deleteDependent(dependentCID);
            if (!deleted) {
                allDeleted = false;
                System.out.println("Failed to delete dependent with CID " + dependentCID);
            }
        }
        return allDeleted;
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
    private PolicyHolder getPolicyHolderByDependentCID(String cID) {
        // Retrieve the dependent using its CID
        Dependent dependent = readDependent(cID);

        // If the dependent exists and has an associated policy holder, return the policy holder
        if (dependent != null) {
            String policyHolderCID = dependent.getPolicyHolder();
            if (policyHolderCID != null) {
                // Ensure the policy holder CID is not the same as the dependent's CID
                if (!policyHolderCID.equals(cID)) {
                    return getPolicyHolder(policyHolderCID);
                }
            }
        }

        // Return null if the dependent or its associated policy holder is not found
        return null;
    }
    private boolean removeFromDependentList(String cID) {
        // Retrieve the associated policyholder
        PolicyHolder policyHolder = getPolicyHolderByDependentCID(cID);

        if (policyHolder != null) {
            // Retrieve the current dependent list of the policyholder
            List<String> dependentList = new ArrayList<>(policyHolder.getDependentList());

            // If the dependent list contains the dependent's cID, remove it
            if (dependentList.contains(cID)) {
                dependentList.remove(cID);
                // Update the list of dependents of the policy holder in the database
                try (Connection conn = databaseConnection.connect();
                     PreparedStatement pstmt = conn.prepareStatement(
                             "UPDATE policy_holder SET dependent_list = ? WHERE c_id = ?")) {
                    Array dependentArray = conn.createArrayOf("varchar", dependentList.toArray());
                    pstmt.setArray(1, dependentArray);
                    pstmt.setString(2, policyHolder.getCID());
                    pstmt.executeUpdate();
                    System.out.println("Dependent with CID " + cID + " removed from the dependent list of Policy Holder with CID "
                            + policyHolder.getCID());
                    return true;
                } catch (SQLException e) {
                    e.printStackTrace();
                    throw new RuntimeException("Error removing dependent from dependent list", e);
                }
            } else {
                System.out.println("Dependent with CID " + cID + " is not in the dependent list of Policy Holder with CID " + policyHolder.getCID());
            }
        } else {
            System.out.println("Policy Holder associated with dependent CID " + cID + " not found.");
        }
        return false;
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
    public boolean checkEntityExists(String tableName, String cID) {
        String sql = "SELECT EXISTS (SELECT 1 FROM " + tableName + " WHERE c_id = ?)";
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
            throw new RuntimeException("Error checking if CID exists in table: " + tableName, e);
        }
        return false;
    }


    // CRUD for dependents
    public boolean createDependent(Dependent dependent) {

        // Check if the policyholder exists
        String policyHolderCID = dependent.getPolicyHolder();
        if (!checkEntityExists("policy_holder", policyHolderCID)) {
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
    public boolean deleteDependent(String cID) {
        // Check if the dependent exists in the dependent table
        if (!checkEntityExists("dependent", cID)) {
            System.out.println("Dependent with CID " + cID + " does not exist.");
            return false;
        }

        // Proceed to remove the dependent from associated lists
        boolean removedFromDependentList = removeFromDependentList(cID);
        boolean removedFromBeneficiaries = removeFromBeneficiaries(cID);
        boolean removedInsuranceCard = deleteInsuranceCard(cID);

        // Proceed to delete the dependent from the dependent table
        boolean dependentDeleted = deleteDependentFromTable(cID);

        // Return true only if all removal operations and deletion from the table were successful
        return dependentDeleted && removedFromDependentList && removedFromBeneficiaries && removedInsuranceCard;
    }

    private boolean deleteDependentFromTable(String cID) {
        String sql = "DELETE FROM dependent WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error deleting dependent from table", e);
        }
    }
    public void updateDependent(String cID, String phone, String address, String email, String password) throws SQLException {
        String sql = "UPDATE dependent SET phone = ?, address = ?, email = ?, password = ? WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, phone);
            pstmt.setString(2, address);
            pstmt.setString(3, email);
            pstmt.setString(4, password);
            pstmt.setString(5, cID);
            pstmt.executeUpdate();
        }
    }
    public Dependent readDependent(String cID) {
        String sql = "SELECT * FROM dependent WHERE c_id = ?";
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
                    String policyHolder = rs.getString("policy_holder");
                    String insuranceCardNumber = rs.getString("insurance_card_number");

                    return new Dependent(cID, role, fullName, phone, address, email, password, actionHistory, claimList, policyOwner, policyHolder, insuranceCardNumber);
                } else {
                    System.out.println("No Dependent found with cID: " + cID);
                    return null;
                }
            } catch (SQLException e) {
                throw new RuntimeException(e);
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
    }
    public List<Dependent> getAllDependents() {
        List<Dependent> dependents = new ArrayList<>();
        String sql = "SELECT c_id FROM dependent";
        try (Connection conn = databaseConnection.connect();
             Statement stmt = conn.createStatement();
             ResultSet rs = stmt.executeQuery(sql)) {

            while (rs.next()) {
                String cID = rs.getString("c_id");
                Dependent dependent = readDependent(cID);
                if (dependent != null) {
                    dependents.add(dependent);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error retrieving all dependents", e);
        }
        return dependents;
    }
    public void deleteDependentsOfPolicyHolder(String policyHolderCID) throws SQLException {
        String sql = "DELETE FROM dependent WHERE policy_holder = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyHolderCID);
            pstmt.executeUpdate();
        }
    }
    public void deleteDependentsByPolicyHolder(String policyHolderCID) throws SQLException {
        // SQL query to delete dependents associated with the given policyholder
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
        if (checkEntityExists("policy_holder", cID)) {
            tableName = "policy_holder";
        } else if (checkEntityExists("dependent", cID)) {
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
    private boolean deleteInsuranceCard(String cID) {
        String sql = "DELETE FROM insurance_card WHERE card_holder = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }
    }
    public String getHashedPasswordFromDB(String cID) {
        String hashedPassword = null;
        String sql = "SELECT password FROM customer WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
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
    public void updatePasswordInDB(String cID, String hashedPassword) {
        String sql = "UPDATE customer SET password = ? WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, hashedPassword);
            pstmt.setString(2, cID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating password in the database", e);
        }
    }
    public Customer getCustomerByID(String cID, String tableName) {
        String sql = "SELECT * FROM " + tableName + " WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractCustomerFromResultSet(rs, tableName);
                } else {
                    System.out.println("No customer found with cID: " + cID);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving customer by cID", e);
        }
    }
    private Customer extractCustomerFromResultSet(ResultSet rs, String tableName) throws SQLException {
        String cID = rs.getString("c_id");
        String role = rs.getString("role");
        String fullName = rs.getString("full_name");
        String phone = rs.getString("phone");
        String address = rs.getString("address");
        String email = rs.getString("email");
        String password = rs.getString("password");
        String[] actionHistoryArray = (String[]) rs.getArray("action_history").getArray();
        List<String> actionHistory = Arrays.asList(actionHistoryArray);

        if (tableName.equals("policy_owner")) {
            String[] beneficiariesArray = (String[]) rs.getArray("beneficiaries").getArray();
            List<String> beneficiaries = Arrays.asList(beneficiariesArray);
            BigDecimal insuranceFee = rs.getBigDecimal("insurance_fee");
            return new PolicyOwner(cID, role, fullName, phone, address, email, password, actionHistory, null, beneficiaries, insuranceFee);

        } else if (tableName.equals("policy_holder")) {
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

        } else if (tableName.equals("dependent")) {
            String[] claimListArray = (String[]) rs.getArray("claim_list").getArray();
            List<Claim> claimList = new ArrayList<>();
            for (String fID : claimListArray) {
                claimList.add(readClaim(fID));
            }
            String policyOwner = rs.getString("policy_owner");
            String policyHolder = rs.getString("policy_holder");
            String insuranceCardNumber = rs.getString("insurance_card_number");
            return new Dependent(cID, role, fullName, phone, address, email, password, actionHistory, claimList, policyOwner, policyHolder, insuranceCardNumber);
        }

        return null;
    }

    public boolean updateCustomerContactInfo(String tableName, String customerCID, String newContactInfo, String column) {
        String sql = String.format("UPDATE %s SET %s = ? WHERE c_id = ?", tableName, column);
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newContactInfo);
            pstmt.setString(2, customerCID);
            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            throw new RuntimeException("Error updating contact info", e);
        }
    }
}
