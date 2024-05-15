package Database;

import Models.Claim.Claim;
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




    // CRUD for policyholders



//    public PolicyHolder readPolicyHolder(String cID) throws SQLException {
//        String sql = "SELECT * FROM policy_holder WHERE c_id = ?";
//        try (Connection conn = databaseConnection.connect();
//             PreparedStatement pstmt = conn.prepareStatement(sql)) {
//            pstmt.setString(1, cID);
//            try (ResultSet rs = pstmt.executeQuery()) {
//                if (rs.next()) {
//                    String role = rs.getString("role");
//                    String fullName = rs.getString("full_name");
//                    String phone = rs.getString("phone");
//                    String address = rs.getString("address");
//                    String email = rs.getString("email");
//                    String password = rs.getString("password");
//                    List<String> actionHistory = Arrays.asList((String[]) rs.getArray("action_history").getArray());
//                    String policyOwner = rs.getString("policy_owner");
//                    String insuranceCardNumber = rs.getString("insurance_card_number");
//                    List<String> dependentList = Arrays.asList((String[]) rs.getArray("dependentlist").getArray());
//                    return new PolicyHolder(cID, role, fullName, phone, address, email, password, actionHistory, policyOwner, insuranceCardNumber, dependentList);
//                } else {
//                    System.out.println("No Policy Holder found with cID: " + cID);
//                    return null;
//                }
//            }
//        }
//    }

    public void deletePolicyHolder(String cID) throws SQLException {
        String sql = "DELETE FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            pstmt.executeUpdate();
        }
    }


    // CRUD for dependents

}
