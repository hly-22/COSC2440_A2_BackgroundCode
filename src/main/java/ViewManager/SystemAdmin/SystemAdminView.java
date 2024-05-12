package ViewManager.SystemAdmin;

import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.Provider.InsuranceManager;
import OperationManager.SystemAdmin.SystemAdminOperations;

import java.util.ArrayList;
import java.util.Scanner;

public class SystemAdminView {
    static SystemAdminOperations operations = new SystemAdminOperations();
    static Scanner scanner = new Scanner(System.in);

    public static void addAPolicyOwner() {
        operations.addPolicyOwner();
    }
    public static void addAPolicyHolder() {

        System.out.println();
        System.out.println("Enter a Policy Owner cID: ");
        String policyOwnerCID = scanner.nextLine();
        // find existing policy owner, print error message and return if not found
        // test policyOwner
        PolicyOwner policyOwner = new PolicyOwner(policyOwnerCID, "owner_tester", "090909090","Canada", "tester_owner@gmail.com", "popoipoiu");

        operations.addPolicyHolder(policyOwner);
    }
    public static void addADependent() {

        System.out.println();
        System.out.println("Enter a Policy Owner cID: ");
        String policyOwnerCID = scanner.nextLine();
        // find existing policy owner, print error message and return if not found
        // test policyOwner
        PolicyOwner policyOwner = new PolicyOwner(policyOwnerCID, "ownertester", "090909090","jijijiji", "testerowner@gmail.com", "popoipoiu");  // test

        System.out.println("Enter a Policy Holder cID: ");
        String policyHolderCID = scanner.nextLine();
        // find if policyHolder exist in policyOwner beneficiaries
        // test policyHolder
        PolicyHolder policyHolder = new PolicyHolder(policyHolderCID, "9090909090", new ArrayList<>());
//        if (policyOwner.getBeneficiaries() != null) {
//            for (String beneCID : policyOwner.getBeneficiaries()) {
//                if (policyHolderCID.equals(beneCID)) {
//                    policyHolder = new PolicyHolder(null, null, null);  // findBeneficiaryByCID
//                }
//            }
//        }

        operations.addDependent(policyOwner, policyHolder);
    }
    public static void addAnInsuranceManager() {
        operations.addInsuranceManager();
    }
    public static void addAnInsuranceSurveyor() {

        System.out.println("Enter a Insurance Manager pID: ");
        String insuranceManagerPID = scanner.nextLine();
        // find existing insuranceManager, print error message and return if not found
        // test insuranceManager
        InsuranceManager insuranceManager = new InsuranceManager(insuranceManagerPID, "InsuranceManager", "HUiHUI", "popopo");

        operations.addInsuranceSurveyor(insuranceManager);
    }
    public void displayAdminMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== System Admin ===");
            System.out.println("1. Add a Policy Owner");
            System.out.println("2. Add a Policy Holder");
            System.out.println("3. Add a Dependent");
            System.out.println("4. Add an Insurance Manager");
            System.out.println("5. Add an Insurance Surveyor");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addAPolicyOwner();
                    case 2 -> addAPolicyHolder();
                    case 3 -> addADependent();
                    case 4 -> addAnInsuranceManager();
                    case 5 -> addAnInsuranceSurveyor();
                    case 0 -> {
                        System.out.println("Exiting...");
                        return;
                    }
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
}
