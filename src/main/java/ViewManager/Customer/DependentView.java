package ViewManager.Customer;

import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import OperationManager.Customer.DependentOperations;
import OperationManager.Customer.PolicyHolderOperations;

import java.util.Scanner;

public class DependentView {
    private DependentOperations operations;
    static Scanner scanner = new Scanner(System.in);
    public DependentView(Dependent dependent) {
        this.operations = new DependentOperations(dependent);
    }
    public void retrieveClaimByFID() {

        System.out.println("Enter fID: ");
        String fID = scanner.nextLine().trim();

        System.out.println(operations.getClaimByID(fID));
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
