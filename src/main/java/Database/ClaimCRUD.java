package Database;

import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Claim.Document;
import Models.InsuranceCard.InsuranceCard;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

public class ClaimCRUD {
    private final DatabaseConnection databaseConnection;

    public ClaimCRUD(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean createClaim(Claim claim) {
        String sql = "INSERT INTO claim (f_id, claim_date, insured_person, card_number, exam_date, document_list, claim_amount, status, receiver_banking_info, note) " +
                "VALUES (?, ?, ?, ?, ?, ?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, claim.getFID());
            pstmt.setDate(2, Date.valueOf(claim.getClaimDate()));
            pstmt.setString(3, claim.getInsuredPerson());
            pstmt.setString(4, claim.getCardNumber().getCardNumber());
            pstmt.setDate(5, Date.valueOf(claim.getExamDate()));

            // Handle document list
            List<String> documentIDs = new ArrayList<>();
            List<Document> documentList = claim.getDocumentList();
            if (documentList != null) {
                for (Document document : documentList) {
                    documentIDs.add(document.getDocumentID());
                }
            }
            if (documentIDs.isEmpty()) {
                pstmt.setArray(6, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(6, conn.createArrayOf("varchar", documentIDs.toArray(new String[0])));
            }

            pstmt.setBigDecimal(7, claim.getClaimAmount());
            pstmt.setString(8, claim.getStatus());
            pstmt.setString(9, claim.getReceiverBankingInfo());
            pstmt.setString(10, claim.getNote());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
            return false;
        }
    }
    public boolean updateClaim(Claim claim) {
        String sql = "UPDATE claim SET document_list = ?, receiver_banking_info = ? WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            // Handle document list
            List<String> documentIDs = new ArrayList<>();
            List<Document> documentList = claim.getDocumentList();
            if (documentList != null) {
                for (Document document : documentList) {
                    documentIDs.add(document.getDocumentID());
                }
            }
            if (documentIDs.isEmpty()) {
                pstmt.setArray(1, conn.createArrayOf("varchar", new String[0]));
            } else {
                pstmt.setArray(1, conn.createArrayOf("varchar", documentIDs.toArray(new String[0])));
            }

            pstmt.setString(2, claim.getReceiverBankingInfo());
            pstmt.setString(3, claim.getFID());

            int rowsAffected = pstmt.executeUpdate();
            return rowsAffected > 0;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
            return false;
        }
    }
    public Claim readClaim(String fID) {
        String sql = "SELECT * FROM claim WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, fID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    return extractClaimFromResultSet(rs);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException(e);
        }
        return null; // Return null if no claim found with the given fID
    }
    public List<Claim> readAllClaims() {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT f_id FROM claim";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            while (rs.next()) {
                String fID = rs.getString("f_id");
                Claim claim = readClaim(fID);
                if (claim != null) {
                    claims.add(claim);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error getting all claims", e);
        }
        return claims;
    }
    public List<Claim> getClaimsByCustomerID(String cID) {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT * FROM claim WHERE insured_person = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cID);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Claim claim = extractClaimFromResultSet(rs);
                    claims.add(claim);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error fetching claims by customer ID", e);
        }
        return claims;
    }
    public List<Claim> getClaimsByInsuranceCard(String insuranceCardNumber) {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT * FROM claim WHERE card_number = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insuranceCardNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Claim claim = extractClaimFromResultSet(rs);
                    claims.add(claim);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving claims by insurance card number", e);
        }
        return claims;
    }
    public List<Claim> getClaimsByStatus(String status) {
        List<Claim> claims = new ArrayList<>();
        String sql = "SELECT * FROM claim WHERE status = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, status);
            try (ResultSet rs = pstmt.executeQuery()) {
                while (rs.next()) {
                    Claim claim = extractClaimFromResultSet(rs);
                    claims.add(claim);
                }
            }
        } catch (SQLException e) {
            throw new RuntimeException("Error retrieving claims by status", e);
        }
        return claims;
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
    public boolean deleteClaim(String fID) {
        // First, retrieve the claim to get its details, including the associated cID and documents
        Claim claim;
        claim = readClaim(fID);
        if (claim == null) {
            System.out.println("No claim found with fID: " + fID);
            return false;
        }

        String cID = claim.getInsuredPerson();

        // Remove the claim from the claim_list of the related customer (policyholder or dependent)
        try {
            removeFromClaimList(cID, fID);
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Delete associated documents
        String sqlDeleteDocuments = "DELETE FROM document WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteDocuments)) {
            pstmt.setString(1, fID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Delete the claim
        String sqlDeleteClaim = "DELETE FROM claim WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sqlDeleteClaim)) {
            pstmt.setString(1, fID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        // Update action history for the related customer
        String updateActionHistorySQL = "UPDATE customer SET action_history = array_append(action_history, ?) WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(updateActionHistorySQL)) {
            String action = LocalDate.now() + ": claim " + fID + " deleted.";
            pstmt.setString(1, action);
            pstmt.setString(2, cID);
            pstmt.executeUpdate();
        } catch (SQLException e) {
            e.printStackTrace();
            return false;
        }

        System.out.println("Claim with fID " + fID + " deleted successfully.");
        return true;
    }
    private void removeFromClaimList(String cID, String fID) throws SQLException {
        String getClaimListSQL = "SELECT claim_list FROM customer WHERE c_id = ?";
        String updateClaimListSQL = "UPDATE customer SET claim_list = ? WHERE c_id = ?";

        try (Connection conn = databaseConnection.connect();
             PreparedStatement getPstmt = conn.prepareStatement(getClaimListSQL);
             PreparedStatement updatePstmt = conn.prepareStatement(updateClaimListSQL)) {
            getPstmt.setString(1, cID);
            try (ResultSet rs = getPstmt.executeQuery()) {
                if (rs.next()) {
                    String[] claimList = (String[]) rs.getArray("claim_list").getArray();
                    List<String> updatedClaimList = new ArrayList<>(Arrays.asList(claimList));
                    updatedClaimList.remove(fID);

                    updatePstmt.setArray(1, conn.createArrayOf("varchar", updatedClaimList.toArray(new String[0])));
                    updatePstmt.setString(2, cID);
                    updatePstmt.executeUpdate();
                }
            }
        }
    }

    public boolean updateClaimStatus(String fID, String newStatus) {
        String sql = "UPDATE claim SET status = ? WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, newStatus);
            pstmt.setString(2, fID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating claim status", e);
        }
    }
    public boolean updateNote(String fID, String note) {
        String sql = "UPDATE claim SET note = ? WHERE f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, note);
            pstmt.setString(2, fID);
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace();
            throw new RuntimeException("Error updating claim note", e);
        }
    }

    public boolean createDocument(Document document) {
        String sql = "INSERT INTO document (document_id, f_id, file_name, convert_file_name, url) " +
                "VALUES (?, ?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, document.getDocumentID());
            pstmt.setString(2, document.getfID());
            pstmt.setString(3, document.getFileName());
            pstmt.setString(4, document.getConvertFileName());
            pstmt.setString(5, document.getURL());

            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            e.printStackTrace(); // Handle or log the exception as needed
            return false;
        }
    }
    public boolean checkDependent(String policyholderCID, String dependentCID) {
        String sql = "SELECT dependent_list FROM policy_holder WHERE c_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, policyholderCID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String[] dependentListArray = (String[]) rs.getArray("dependent_list").getArray();
                    List<String> dependentList = Arrays.asList(dependentListArray);
                    return dependentList.contains(dependentCID);
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }


    public boolean checkClaimOwner(String dependentCID, String claimFID) {
        String sql = "SELECT COUNT(*) AS count FROM claim WHERE insured_person = ? AND f_id = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, dependentCID);
            pstmt.setString(2, claimFID);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    int count = rs.getInt("count");
                    return count > 0;
                }
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return false;
    }
    public BigDecimal calculateSumOfClaimedAmounts() {
        String sql = "SELECT SUM(claim_amount) FROM claim WHERE status = 'APPROVED'";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql);
             ResultSet rs = pstmt.executeQuery()) {
            if (rs.next()) {
                return rs.getBigDecimal(1);
            }
        } catch (SQLException e) {
            e.printStackTrace();
        }
        return BigDecimal.ZERO; // Return zero if no sum is found
    }


}
