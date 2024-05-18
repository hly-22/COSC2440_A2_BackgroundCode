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
    public void displayDependentMenu() {}

}
