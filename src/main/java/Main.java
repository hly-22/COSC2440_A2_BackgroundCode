import ViewManager.Customer.PolicyHolderView;
import ViewManager.Customer.PolicyOwnerView;
import ViewManager.SystemAdmin.SystemAdminView;

public class Main {
    public static void main(String[] args) {
//        SystemAdminView adminView = new SystemAdminView();
//        adminView.displayAdminMenu();

        PolicyOwnerView policyOwnerView = new PolicyOwnerView();
        policyOwnerView.displayPolicyOwnerMenu();

//        PolicyHolderView policyHolderView = new PolicyHolderView();
//        policyHolderView.displayPolicyHolderMenu();
    }
}
