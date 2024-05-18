package ViewManager.Provider;

import Models.Provider.InsuranceManager;
import OperationManager.Provider.InsuranceManagerOperations;

import java.util.Scanner;

public class InsuranceManagerView {
    private InsuranceManagerOperations operations;
    static Scanner scanner = new Scanner(System.in);

    public InsuranceManagerView(InsuranceManager insuranceManager) {
        this.operations = new InsuranceManagerOperations(insuranceManager);
    }
    public void updatePassword() {
        operations.updateProviderPassword();
    }
    public void addASurveyor() {
        operations.addSurveyor();
    }

    public void displayInsuranceManagerMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Insurance Manager ===");
            System.out.println("1. Update Password");
            System.out.println("2. Add a Surveyor");

            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> updatePassword();
                    case 2 -> addASurveyor();
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
