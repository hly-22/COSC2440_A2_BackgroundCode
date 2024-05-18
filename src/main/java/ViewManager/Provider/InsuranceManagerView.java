package ViewManager.Provider;

import Models.Claim.Claim;
import Models.Claim.ClaimStatus;
import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Provider.InsuranceManagerOperations;

import java.util.List;
import java.util.Scanner;

public class InsuranceManagerView {
    private InsuranceManagerOperations operations;
    static Scanner scanner = new Scanner(System.in);

    public InsuranceManagerView(InsuranceManager insuranceManager) {
        this.operations = new InsuranceManagerOperations(insuranceManager);
    }
    public void displayInfo() {
        operations.displayInfo();
    }
    public void updatePassword() {
        operations.updateProviderPassword();
    }
    public void retrieveSurveyorInfo() {
        System.out.println("Enter the pID of the surveyor: ");
        String pID = scanner.nextLine().trim();

        InsuranceSurveyor insuranceSurveyor = operations.retrieveSurveyorInfo(pID);

        if (insuranceSurveyor != null) {
            System.out.println("Surveyor found:");
            System.out.println(insuranceSurveyor);
        } else {
            System.out.println("Surveyor not found or doesn't belong to the logged-in Insurance Manager.");
        }
    }
    public void retrieveClaimByFID() {
        System.out.println("Enter fID (claim ID): ");
        String fID = scanner.nextLine().trim();

        Claim claim = operations.getClaimByID(fID);
        if (claim != null) {
            System.out.println("Claim found:");
            System.out.println(claim);
        } else {
            System.out.println("Claim with ID " + fID + " not found.");
        }
    }
    public void retrieveClaimsByStatus() {

        while (true) {
            System.out.println();
            System.out.println("Choose status.");
            System.out.println("1. NEW");
            System.out.println("2. PROCESSING");
            System.out.println("3. APPROVED");
            System.out.println("4. REJECTED");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displayClaims(operations.getClaimsByStatus("NEW"));
                    case 2 -> displayClaims(operations.getClaimsByStatus("PROCESSING"));
                    case 3 -> displayClaims(operations.getClaimsByStatus("APPROVED"));
                    case 4 -> displayClaims(operations.getClaimsByStatus("REJECTED"));
                    case 0 -> {return;}
                    default -> System.out.println("Invalid option. Please try again");
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }
        }
    }
    public void processClaimStatus() {
        // Get the list of processing claims
        List<Claim> processingClaims = operations.getClaimsByStatus("PROCESSING");

        // Display the list of processing claims
        displayClaims(processingClaims);

        // Ask the manager to enter the claim fID to process
        System.out.println("Enter claim fID you wish to process (press \"exit\" to return): ");
        String claimFID = scanner.nextLine().trim();

        Claim claim = null;
        for (Claim c : processingClaims) {
            if (c.getFID().equals(claimFID)) {
                claim = c;
                break;
            }
        }

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. APPROVE");
            System.out.println("2. REJECT");
            System.out.println("0. Exit");

            try {
                int newStatusInput = Integer.parseInt(scanner.nextLine());
                switch (newStatusInput) {
                    case 1 -> {
                        if (operations.approveClaim(claim)) {
                            System.out.println("Claim is approved.");
                        }
                    }
                    case 2 -> {
                        if (operations.rejectClaim(claim)) {
                            System.out.println("Claim is rejected.");
                        }
                    }
                    case 0 -> {
                        System.out.println("Exiting claim status update.");
                        return;
                    }
                    default -> {
                        System.out.println("Invalid option. Please enter 1, 2, or 3.");
                    }
                }
            } catch (NumberFormatException e) {
                System.out.println("Invalid option. Please try again");
            }

        }
    }
    private void displayClaims(List<Claim> claims) {
        if (claims.isEmpty()) {
            System.out.println("No claims found.");
        } else {
            System.out.println("Claims:");
            for (Claim claim : claims) {
                System.out.println(claim);
            }
        }
    }
    public void displaySurveyors() {
        List<InsuranceSurveyor> surveyors = operations.retrieveAllSurveyors();
        if (surveyors.isEmpty()) {
            System.out.println("No surveyors found for the current insurance manager.");
        } else {
            System.out.println("Surveyor List:");
            for (InsuranceSurveyor surveyor : surveyors) {
                System.out.println(surveyor);
            }
        }
    }
    private void displayAllPolicyOwners() {
        List<PolicyOwner> policyOwners = operations.getAllPolicyOwners();
        for (PolicyOwner policyOwner : policyOwners) {
            System.out.println(policyOwner);
        }
    }

    private void displayAllPolicyHolders() {
        List<PolicyHolder> policyHolders = operations.getAllPolicyHolders();
        for (PolicyHolder policyHolder : policyHolders) {
            System.out.println(policyHolder);
        }
    }

    private void displayAllDependents() {
        List<Dependent> dependents = operations.getAllDependents();
        for (Dependent dependent : dependents) {
            System.out.println(dependent);
        }
    }

    private void displayCustomerByID(String tableName) {
        System.out.println("Enter cID: ");
        String cID = scanner.nextLine();
        Customer customer = operations.getCustomerByID(cID, tableName);
        if (customer != null) {
            System.out.println(customer);
        } else {
            System.out.println("Customer not found.");
        }
    }
    public void displaySurveyorMenu() {
        while (true) {
            System.out.println();
            System.out.println("1. Retrieve Surveyor by pID");
            System.out.println("2. Retrieve All Surveyors");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> retrieveSurveyorInfo();
                    case 2 -> displaySurveyors();
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
    public void displayClaimMenu() {
        while (true) {
            System.out.println();
            System.out.println("1. Retrieve Claim by fID");
            System.out.println("2. Retrieve Claims by cID");
            System.out.println("3. Retrieve Claims by Insurance Card");
            System.out.println("4. Retrieve Claims by Status");
            System.out.println("5. Retrieve All Claims");
            System.out.println("6. Process Claim Status");
            System.out.println("0. Return");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> retrieveClaimByFID();
                    case 2 -> {
                        System.out.println("Enter cID: ");
                        String cID = scanner.nextLine().trim();
                        displayClaims(operations.getClaimsByCID(cID));
                    }
                    case 3 -> {
                        System.out.println("Enter insurance card number: ");
                        String cardNumber = scanner.nextLine().trim();
                        displayClaims(operations.getClaimsByInsuranceCard(cardNumber));
                    }
                    case 4 -> {retrieveClaimsByStatus();}
                    case 5 -> {displayClaims(operations.getAllClaims());}
                    case 6 -> processClaimStatus();
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
    public void displayCustomerMenu() {
        while (true) {
            System.out.println();
            System.out.println("1. Retrieve All Policy Owners");
            System.out.println("2. Retrieve All Policy Holders");
            System.out.println("3. Retrieve All Dependents");
            System.out.println("4. Retrieve Policy Owner by cID");
            System.out.println("5. Retrieve Policy Holder by cID");
            System.out.println("6. Retrieve Dependent by cID");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displayAllPolicyOwners();
                    case 2 -> displayAllPolicyHolders();
                    case 3 -> displayAllDependents();
                    case 4 -> displayCustomerByID("policy_owner");
                    case 5 -> displayCustomerByID("policy_holder");
                    case 6 -> displayCustomerByID("dependent");
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

    public void displayInsuranceManagerMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Insurance Manager ===");
            System.out.println("1. Surveyor Menu");
            System.out.println("2. Claim Menu");
            System.out.println("3. Customer Menu");
            System.out.println("4. Retrieve Personal Information");
            System.out.println("5. Update Password");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displaySurveyorMenu();
                    case 2 -> displayClaimMenu();
                    case 3 -> displayCustomerMenu();
                    case 4 -> displayInfo();
                    case 5 -> updatePassword();
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
