package Database;

import Models.InsuranceCard.InsuranceCard;

import java.sql.*;
import java.time.LocalDate;

public class InsuranceCardCRUD {
    private final DatabaseConnection databaseConnection;

    public InsuranceCardCRUD(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean createInsuranceCard(InsuranceCard insuranceCard){
        String sql = "INSERT INTO insurance_card (card_number, card_holder, policy_owner, expiration_date) VALUES (?, ?, ?, ?)";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, insuranceCard.getCardNumber());
            pstmt.setString(2, insuranceCard.getCardHolder());
            pstmt.setString(3, insuranceCard.getPolicyOwner());
            pstmt.setDate(4, Date.valueOf(insuranceCard.getExpirationDate()));
            pstmt.executeUpdate();
            return true;
        } catch (SQLException e) {
            try {
                throw new SQLException("Error creating insurance card: " + e.getMessage(), e);
            } catch (SQLException ex) {
                throw new RuntimeException(ex);
            }
        }
    }
    public InsuranceCard readInsuranceCard(String cardNumber) throws SQLException {
        String sql = "SELECT * FROM insurance_card WHERE card_number = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            try (ResultSet rs = pstmt.executeQuery()) {
                if (rs.next()) {
                    String cardHolder = rs.getString("card_holder");
                    String policyOwner = rs.getString("policy_owner");
                    LocalDate expirationDate = rs.getDate("expiration_date").toLocalDate();
                    return new InsuranceCard(cardNumber, cardHolder, policyOwner, expirationDate);
                } else {
                    System.out.println("No Insurance Card found with card number: " + cardNumber);
                    return null;
                }
            }
        } catch (SQLException e) {
            throw new SQLException("Error reading insurance card: " + e.getMessage(), e);
        }
    }
    public void deleteInsuranceCard(String cardNumber) throws SQLException {
        String sql = "DELETE FROM insurance_card WHERE card_number = ?";
        try (Connection conn = databaseConnection.connect();
             PreparedStatement pstmt = conn.prepareStatement(sql)) {
            pstmt.setString(1, cardNumber);
            int rowsAffected = pstmt.executeUpdate();
            if (rowsAffected == 0) {
                System.out.println("No Insurance Card found with card number: " + cardNumber);
            }
        } catch (SQLException e) {
            throw new SQLException("Error deleting insurance card: " + e.getMessage(), e);
        }
    }
}
