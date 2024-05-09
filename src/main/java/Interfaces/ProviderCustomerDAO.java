package Interfaces;

public interface ProviderCustomerDAO {
    void getAllCustomers();
    void getCustomerByID(String cID);
    void filterCustomer();
}
