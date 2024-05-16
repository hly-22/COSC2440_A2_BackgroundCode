package Database;

import Models.Claim.Claim;

import java.sql.Connection;
import java.sql.Date;
import java.sql.PreparedStatement;
import java.sql.SQLException;

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

}
