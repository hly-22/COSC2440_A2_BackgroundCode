package ViewManager.Provider;

import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Provider.InsuranceManagerOperations;
import OperationManager.Provider.InsuranceSurveyorOperations;

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
    public void displayInsuranceSurveyorMenu() {
        while (true) {
            System.out.println();
            System.out.println("=== Insurance Surveyor ===");
            System.out.println("1. Update Password");
            System.out.println("2. Add a Surveyor");

            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> updatePassword();
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
