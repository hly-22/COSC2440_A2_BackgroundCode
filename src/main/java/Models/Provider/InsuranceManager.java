package Models.Provider;

import java.util.ArrayList;
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

    public InsuranceManager(String pID, String role, String fullName, String password) {
        super(pID, role, fullName, password);
        this.surveyorList = new ArrayList<>();
    }

    public InsuranceManager() {

    }

    public List<String> getSurveyorList() {
        return surveyorList;
    }

    public void setSurveyorList(List<String> surveyorList) {
        this.surveyorList = surveyorList;
    }

    public boolean addToSurveyorList(InsuranceSurveyor insuranceSurveyor) {
        return surveyorList.add(insuranceSurveyor.getPID());
    }

    public boolean removeFromSurveyorList(InsuranceSurveyor insuranceSurveyor) {
        int indexToRemove = -1;
        for (int i = 0; i < surveyorList.size(); i++) {
            if (surveyorList.get(i).equals(insuranceSurveyor.getPID())) {
                indexToRemove = i;
                break;
            }
        }
        if (indexToRemove != -1) {
            surveyorList.remove(indexToRemove);
            return true;
        } else {
            return false;
        }
    }

    @Override
    public String toString() {
        return "InsuranceManager{" +
                super.toString() +
                ", surveyorList=" + surveyorList +
                '}';
    }
}
