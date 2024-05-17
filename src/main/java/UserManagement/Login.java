package UserManagement;

import Database.DatabaseConnection;
import ViewManager.Customer.PolicyHolderView;
import ViewManager.Customer.PolicyOwnerView;
import ViewManager.Provider.InsuranceManagerView;
import ViewManager.SystemAdmin.SystemAdminView;

import java.sql.SQLException;
import java.util.Scanner;

public class Login {
    private UserAuthentication userAuth;
    private SessionManager sessionManager;
    private Scanner scanner;
    private DatabaseConnection databaseConnection = new DatabaseConnection("jdbc:postgresql://localhost:5432/postgres", "lyminhhanh", null);

    public Login(UserAuthentication userAuth, SessionManager sessionManager) {
        this.userAuth = userAuth;
        this.sessionManager = sessionManager;
        this.scanner = new Scanner(System.in);
    }

    public void displayLogin() {
        System.out.println();
        System.out.println("=== Login Page ===");
        boolean loggedIn = false;
        while (!loggedIn) {
            System.out.println("[Enter '0' for user ID and password to exit program]");
            System.out.print("Enter user ID: ");
            String userID = scanner.nextLine();
            System.out.print("Enter password: ");
            String password = scanner.nextLine();

            if (userID.equals("0") && password.equals("0")) {
                System.out.println("Exiting...");
                try {
                    databaseConnection.closeConnection();
                    System.out.println("Database Closed.");
                } catch (SQLException e) {
                    throw new RuntimeException(e);
                }
                System.exit(0);
            }

            if (userAuth.authenticateUser(userID, password)) {
                String userRole = userAuth.getUserRole(userID);
                sessionManager.createSession(userID, userRole);
                System.out.println("Login successful. User role: " + userRole);
                loggedIn = true;
            } else {
                System.out.println("Invalid user ID or password.");
            }
        }
    }

    public void logout() {
        sessionManager.invalidateSession();
        System.out.println("Logout successful.");
    }

    public boolean isAuthenticated() {
        return sessionManager.isAuthenticated();
    }

    public SessionManager getSessionManager() {
        return sessionManager;
    }

    public String getUserID() {
        return sessionManager.getCurrentUserID(); // Assuming you have a method to get current user ID in SessionManager
    }
    public String getUserRole() {
        return sessionManager.getCurrentUserRole(); // Assuming you have a method to get user role by ID in SessionManager
    }
}
