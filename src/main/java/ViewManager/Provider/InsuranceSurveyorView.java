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
    public void displayInsuranceSurveyorMenu() {}
}
