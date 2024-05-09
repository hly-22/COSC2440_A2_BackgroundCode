package Model.Provider;

import java.util.List;

public class InsuranceManager extends Provider{
    private List<String> surveyorList;

    public InsuranceManager(List<String> surveyorList) {
        this.surveyorList = surveyorList;
    }

    public InsuranceManager(String pID, String role, String fullName, String password, List<String> actionHistory, List<String> surveyorList) {
        super(pID, role, fullName, password, actionHistory);
        this.surveyorList = surveyorList;
    }

    public List<String> getSurveyorList() {
        return surveyorList;
    }

    public void setSurveyorList(List<String> surveyorList) {
        this.surveyorList = surveyorList;
    }

    public boolean addToSurveyorList(InsuranceSurveyor insuranceSurveyor) {
        return false;
    }

    public boolean removeFromSurveyorList(InsuranceSurveyor insuranceSurveyor) {
        return false;
    }
}
