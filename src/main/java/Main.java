import Database.DatabaseConnection;
import ViewManager.Customer.PolicyHolderView;
import ViewManager.Customer.PolicyOwnerView;
import ViewManager.SystemAdmin.SystemAdminView;

import java.sql.SQLException;
import java.sql.Connection;

public class Main {
    public static void main(String[] args) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
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
