package OperationManager.Provider;

import Interfaces.ProviderClaimDAO;
import Interfaces.ProviderCustomerDAO;
import Models.Claim.Claim;
import Models.Customer.Dependent;
import Models.Provider.InsuranceManager;
import Models.Provider.InsuranceSurveyor;
import OperationManager.Utils.InputChecker;

import java.time.LocalDate;
import java.util.List;
import java.util.Scanner;

public class InsuranceManagerOperations implements ProviderClaimDAO, ProviderCustomerDAO {

    private final InsuranceManager insuranceManager = new InsuranceManager();
    private final Scanner scanner = new Scanner(System.in);

    // method to update password
    public void updateProviderPassword(String password) {

    }

    // methods relating to surveyors
    public void addSurveyor() {

        System.out.println("Enter a valid pID (p-xxxxxxx): ");
        String pID = scanner.nextLine();
        if (!InputChecker.isValidPIDFormat(pID)) {
            System.out.println("Invalid provider ID format.");
            return;
        }
        // check if pID already exists

        System.out.println("Enter full name: ");
        String fullName = scanner.nextLine();

        System.out.println("Enter password: ");
        String enteredPassword = scanner.nextLine();
        // convert password into hashed
        String password = enteredPassword;

        InsuranceSurveyor insuranceSurveyor = new InsuranceSurveyor(pID, fullName, password, insuranceManager.getPID());
        System.out.println(insuranceSurveyor);

        insuranceManager.addActionHistory(LocalDate.now() + ": add Insurance Surveyor " + insuranceSurveyor.getPID() + " to surveyor list");
        System.out.println(insuranceManager.getActionHistory());
    }
    public boolean removeSurveyor(InsuranceSurveyor insuranceSurveyor) {
        return false;
    }
    public void getSurveyorInfo(InsuranceSurveyor insuranceSurveyor) {

    }

    // methods relating to claims
    @Override
    public void getAllClaims() {

    }

    @Override
    public void getClaimByID(String fID) {

    }

    @Override
    public void filterClaim() {

    }

    @Override
    public void requireMoreInfo() {
        // not available to insurance managers
    }

    @Override
    public void proposeClaim(Claim claim) {
        // not available to insurance managers
    }

    @Override
    public boolean approveClaim(Claim claim) {
        return false;
    }

    @Override
    public boolean rejectClaim(Claim claim) {
        return false;
    }

    // methods relating to customers
    @Override
    public void getAllCustomers() {

    }

    @Override
    public void getCustomerByID(String cID) {

    }

    @Override
    public void filterCustomer() {

    }
}
