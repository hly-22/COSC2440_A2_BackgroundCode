package ViewManager.Provider;

import OperationManager.Provider.InsuranceManagerOperations;

import java.util.Scanner;

public class InsuranceManagerView {
    static InsuranceManagerOperations operations = new InsuranceManagerOperations();
    static Scanner scanner = new Scanner(System.in);

    public static void addASurveyor() {
        operations.addSurveyor();
    }

    public void displayInsuranceManagerMenu () {

        while (true) {
            System.out.println();
            System.out.println("=== Insurance Manager ===");
            System.out.println("1. Add a Surveyor");
            System.out.println("0. Exit");

            try {
                int response = Integer.parseInt(scanner.nextLine());
                switch (response) {
                    case 1 -> addASurveyor();
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
