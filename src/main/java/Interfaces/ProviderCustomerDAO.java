package Interfaces;

import Models.Customer.Customer;
import Models.Customer.Dependent;
import Models.Customer.PolicyHolder;
import Models.Customer.PolicyOwner;

import java.util.List;

public interface ProviderCustomerDAO {
    List<PolicyOwner> getAllPolicyOwners();
    List<PolicyHolder> getAllPolicyHolders();
    List<Dependent> getAllDependents();
    Customer getCustomerByID(String cID, String table_name);
}
