package ViewManager.Customer;

import Models.Claim.Claim;
import Models.Customer.Dependent;
import OperationManager.Customer.DependentOperations;

import java.util.Scanner;

public class DependentView {
    private final DependentOperations operations;
    static Scanner scanner = new Scanner(System.in);
    public DependentView(Dependent dependent) {
        this.operations = new DependentOperations(dependent);
    }
    public void retrieveClaimByFID() {
        System.out.println("Enter fID: ");
        String fID = scanner.nextLine().trim();

        Claim claim = operations.getClaimByID(fID);
        if (claim != null) {
            System.out.println(claim);
        } else {
            System.out.println("No claim found for FID: " + fID);
        }
    }
    public void retrieveAllClaims() {
        operations.getAllClaims();
    }
    public void retrieveInfo() {
        operations.displayInfo();
    }

    public void displayDependentMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Dependent ===");
            System.out.println("1. Retrieve Claim by fID");
            System.out.println("2. Retrieve All Claims");
            System.out.println("3. Retrieve Personal Information");
            System.out.println("0. Logout");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> retrieveClaimByFID();
                    case 2 -> retrieveAllClaims();
                    case 3 -> retrieveInfo();
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
