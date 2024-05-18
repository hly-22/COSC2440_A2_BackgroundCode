package UserManagement;

import Database.CustomerCRUD;
import Database.DatabaseConnection;
import Database.ProviderCRUD;
import Database.SystemAdminCRUD;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import Models.SystemAdmin.SystemAdmin;
import ViewManager.Customer.DependentView;
import ViewManager.Customer.PolicyHolderView;
import ViewManager.Customer.PolicyOwnerView;
import ViewManager.Provider.InsuranceManagerView;
import ViewManager.Provider.InsuranceSurveyorView;
import ViewManager.SystemAdmin.SystemAdminView;

public class LoginManager {
    private final DatabaseConnection databaseConnection;
    private final SystemAdminCRUD systemAdminCRUD;
    private final CustomerCRUD customerCRUD;
    private final ProviderCRUD providerCRUD;
    public LoginManager(DatabaseConnection databaseConnection) {
        this.databaseConnection = databaseConnection;
        this.systemAdminCRUD = new SystemAdminCRUD(databaseConnection);
        this.customerCRUD = new CustomerCRUD(databaseConnection);
        this.providerCRUD = new ProviderCRUD(databaseConnection);
    }

    public void runLogin() {
        while (true) {
            Login login = new Login(new UserAuthentication(databaseConnection), new SessionManager());
            login.displayLogin();

            if (login.isAuthenticated()) {
                String userID = login.getSessionManager().getCurrentUserID();
                String userRole = login.getSessionManager().getCurrentUserRole();
                redirectToRoleMenu(userID, userRole);
            } else {
                System.out.println("Authentication failed. Please try again.");
            }
        }
    }
    private void redirectToRoleMenu (String userID, String userRole){
        // Redirect to the respective role menu based on userRole
        switch (userRole) {
            case "system_admin" -> {
                SystemAdmin systemAdmin = systemAdminCRUD.readAdmin(userID);
                SystemAdminView systemAdminView = new SystemAdminView(systemAdmin);
                systemAdminView.displayAdminMenu();
            }
            case "policy_owner" -> {
                PolicyOwner policyOwner = customerCRUD.readPolicyOwner(userID);
                PolicyOwnerView policyOwnerView = new PolicyOwnerView(policyOwner);
                policyOwnerView.displayPolicyOwnerMenu();
            }
            case "policy_holder" -> {
                PolicyHolder policyHolder = customerCRUD.getPolicyHolder(userID);
                PolicyHolderView policyHolderView = new PolicyHolderView(policyHolder);
                policyHolderView.displayPolicyHolderMenu();
            }
            case "dependent" -> {
                Dependent dependent = customerCRUD.readDependent(userID);
                DependentView dependentView = new DependentView(dependent);
                dependentView.displayDependentMenu();
            }
            case "insurance_manager" -> {
                InsuranceManager insuranceManager = providerCRUD.readInsuranceManager(userID);
                InsuranceManagerView insuranceManagerView = new InsuranceManagerView(insuranceManager);
                insuranceManagerView.displayInsuranceManagerMenu();
            }
            case "insurance_surveyor" -> {
                InsuranceSurveyor insuranceSurveyor = providerCRUD.readInsuranceSurveyor(userID);
                InsuranceSurveyorView insuranceSurveyorView = new InsuranceSurveyorView(insuranceSurveyor);
                insuranceSurveyorView.displayInsuranceSurveyorMenu();
            }
            default -> System.out.println("Role menu not available for " + userRole);
        }
    }

}

