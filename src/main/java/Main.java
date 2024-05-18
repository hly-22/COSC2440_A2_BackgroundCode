import Database.DatabaseConnection;
import UserManagement.LoginManager;

import java.sql.SQLException;

public class Main {

    public static void main(String[] args) throws SQLException {
        DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);
        databaseConnection.connect();
        System.out.println("Database Connected.");

        System.out.println("==== Welcome! ====");
        LoginManager loginManager = new LoginManager(databaseConnection);
        loginManager.runLogin();

//        SystemAdmin systemAdmin = new SystemAdmin("admin", "12345", null);
//        SystemAdminView adminView = new SystemAdminView(systemAdmin);
//        adminView.displayAdminMenu();

        databaseConnection.closeConnection();
        System.out.println("Database Closed.");
    }

}
