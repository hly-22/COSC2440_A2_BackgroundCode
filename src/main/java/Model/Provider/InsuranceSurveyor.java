package Model.Provider;

import java.util.List;

public class InsuranceSurveyor extends Provider {
    private String insuranceManager;

    public InsuranceSurveyor(String insuranceManager) {
        this.insuranceManager = insuranceManager;
    }

    public InsuranceSurveyor(String pID, String role, String fullName, String password, List<String> actionHistory, String insuranceManager) {
        super(pID, role, fullName, password, actionHistory);
        this.insuranceManager = insuranceManager;
    }
}
