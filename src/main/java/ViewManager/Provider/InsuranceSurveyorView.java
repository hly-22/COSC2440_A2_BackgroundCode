package ViewManager.Provider;

import Models.Claim.Claim;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Provider.InsuranceSurveyorOperations;

import java.util.List;
import java.util.Scanner;

public class InsuranceSurveyorView {
    private InsuranceSurveyorOperations operations;
    static Scanner scanner = new Scanner(System.in);

    public InsuranceSurveyorView(InsuranceSurveyor insuranceSurveyor) {
        this.operations = new InsuranceSurveyorOperations(insuranceSurveyor);
    }
    public void updatePassword() {
        operations.updateProviderPassword();
    }
    public void displayInfo(){
        operations.displayInfo();
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
        List<Claim> newClaims = operations.getClaimsByStatus("NEW");

        // Display the list of processing claims
        displayClaims(newClaims);

        // Ask the surveyor to enter the claim fID to process
        System.out.println("Enter claim fID you wish to process (press \"exit\" to return): ");
        String claimFID = scanner.nextLine().trim();

        Claim claim = null;
        for (Claim c : newClaims) {
            if (c.getFID().equals(claimFID)) {
                claim = c;
                break;
            }
        }

        while (true) {
            System.out.println("Choose an option:");
            System.out.println("1. PROPOSE");
            System.out.println("2. REQUIRE MORE INFORMATION");
            System.out.println("0. Exit");

            try {
                int newStatusInput = Integer.parseInt(scanner.nextLine());
                switch (newStatusInput) {
                    case 1 -> {
                        if (operations.proposeClaim(claim)) {
                            System.out.println("Claim is proposed to Insurance Manager.");
                        }
                    }
                    case 2 -> {
                        if (operations.requireMoreInfo(claim)) {
                            System.out.println("Claim is rejected because of missing information.");
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

    public void displayInsuranceSurveyorMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Insurance Surveyor ===");
            System.out.println("1. Claim Menu");
            System.out.println("2. Customer Menu");
            System.out.println("3. Retrieve Personal Information");
            System.out.println("4. Update Password");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> displayClaimMenu();
                    case 3 -> displayInfo();
                    case 4 -> updatePassword();
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
