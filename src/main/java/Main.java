import Database.DatabaseConnection;
import UserManagement.Login;
import UserManagement.LoginManager;
import UserManagement.SessionManager;
import UserManagement.UserAuthentication;
import ViewManager.Customer.PolicyHolderView;
import ViewManager.SystemAdmin.SystemAdminView;

import java.sql.SQLException;
import java.util.function.Supplier;

public class Main {
//    public static void main(String[] args) throws SQLException {
//        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
//        databaseConnection.connect();
//        System.out.println("Database Connected.");
//
////        SystemAdminView adminView = new SystemAdminView();
////        adminView.displayAdminMenu();
//
////        PolicyOwnerView policyOwnerView = new PolicyOwnerView();
////        policyOwnerView.displayPolicyOwnerMenu();
//
////        PolicyHolderView policyHolderView = new PolicyHolderView();
////        policyHolderView.displayPolicyHolderMenu();
//
//        Login login = new Login(new UserAuthentication(databaseConnection), new SessionManager());
//        login.displayLogin();
//
//
//        databaseConnection.closeConnection();
//        System.out.println("Database Closed.");
//    }
    public static void main(String[] args) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
        databaseConnection.connect();
        System.out.println("Database Connected.");

        System.out.println("==== Welcome! ====");
        LoginManager loginManager = new LoginManager(databaseConnection);
        loginManager.runLogin();

        databaseConnection.closeConnection();
        System.out.println("Database Closed.");
    }

}
