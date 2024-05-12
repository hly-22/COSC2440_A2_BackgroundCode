import OperationManager.SystemAdmin.SystemAdminOperations;

public class Main {
    public static void main(String[] args) {
        SystemAdminOperations operations = new SystemAdminOperations();
//        operations.addPolicyOwner();
//        operations.addPolicyHolder();
//        operations.addDependent();

        operations.addInsuranceManager();
        operations.addInsuranceSurveyor();
    }
}
