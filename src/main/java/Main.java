import Database.DatabaseConnection;
import ViewManager.Customer.PolicyHolderView;
import ViewManager.Customer.PolicyOwnerView;
import ViewManager.SystemAdmin.SystemAdminView;

import java.sql.SQLException;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
//        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://aws-0-ap-southeast-1.pooler.supabase.com:5432/postgres", "postgres.kzweuuezlwlmafrfqxqx", "KPi3d~g3nBtJ9gy");
        databaseConnection.connect();
        System.out.println("Database Connected.");

        SystemAdminView adminView = new SystemAdminView();
        adminView.displayAdminMenu();

//        PolicyOwnerView policyOwnerView = new PolicyOwnerView();
//        policyOwnerView.displayPolicyOwnerMenu();

//        PolicyHolderView policyHolderView = new PolicyHolderView();
//        policyHolderView.displayPolicyHolderMenu();

        databaseConnection.closeConnection();
        System.out.println("Database Closed.");
    }
}
