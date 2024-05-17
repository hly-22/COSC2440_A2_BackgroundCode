package UserManagement;

import Database.DatabaseConnection;
import ViewManager.Customer.PolicyHolderView;
import ViewManager.Customer.PolicyOwnerView;
import ViewManager.Provider.InsuranceManagerView;
import ViewManager.SystemAdmin.SystemAdminView;

public class LoginManager {
    private final DatabaseConnection databaseConnection;
    private final Login login;

    public LoginManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        this.login = new Login(new UserAuthentication(databaseConnection), new SessionManager());
    }

    public void runLogin() {
        while (true) {
            Login login = new Login(new UserAuthentication(databaseConnection), new SessionManager());
            login.displayLogin();

            if (login.isAuthenticated()) {
                String userID = login.getSessionManager().getCurrentUserID();
                String userRole = login.getSessionManager().getCurrentUserRole();
                redirectToRoleMenu(userRole);
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        }
    }
    private void redirectToRoleMenu (String userRole){
        // Redirect to the respective role menu based on userRole
        switch (userRole) {
            case "system_admin":
                SystemAdminView systemAdminView = new SystemAdminView();
                systemAdminView.displayAdminMenu();
                break;
            case "policy_owner":
                PolicyOwnerView policyOwnerView = new PolicyOwnerView();
                policyOwnerView.displayPolicyOwnerMenu();
                break;
            case "policy_holder":
                PolicyHolderView policyHolderView = new PolicyHolderView();
                policyHolderView.displayPolicyHolderMenu();
                break;
            case "dependent":
                break;
            case "insurance_manager":
                InsuranceManagerView insuranceManagerView = new InsuranceManagerView();
                insuranceManagerView.displayInsuranceManagerMenu();
                break;
            case "insurance_surveyor":
                break;
            default:
                System.out.println("Role menu not available for " + userRole);
                break;
        }
    }

}

