package Database;

import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Claim.Document;
import Models.InsuranceCard.InsuranceCard;

import java.math.BigDecimal;
import java.sql.*;
import java.time.LocalDate;
import java.util.ArrayList;
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
            pstmt.setArray(6, conn.createArrayOf("varchar", claim.getDocumentList().toArray()));
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
    private InsuranceCard fetchInsuranceCard(String cardNumber) throws SQLException {
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
                }
            }
        }
        return null; // Return null if no insurance card found with the given card number
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
