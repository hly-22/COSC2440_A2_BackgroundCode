package Interfaces;

public interface ProviderCustomerDAO {
    void getAllPolicyOwners();
    void getAllPolicyHolders();
    void getAllDependents();
    void getCustomerByID(String cID, String table_name);
}
