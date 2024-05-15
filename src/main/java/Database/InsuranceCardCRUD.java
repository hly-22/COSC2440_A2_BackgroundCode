package Database;

import Models.InsuranceCard.InsuranceCard;

public class InsuranceCardCRUD {
    private final DatabaseConnection databaseConnection;

    public InsuranceCardCRUD(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
    }

    public boolean createInsuranceCard(InsuranceCard insuranceCard) {
        return false;
    }
}
