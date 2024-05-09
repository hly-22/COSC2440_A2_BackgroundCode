package Interfaces;

import Models.Claim.Claim;
import Models.Customer.Customer;

public interface CustomerClaimDAO {
    boolean addClaim(Customer customer, Claim claim);
    boolean getClaimByID(String fID);
    boolean getAllClaimsByCustomer(String cID);
    void getAllClaims();
    boolean updateClaim(String fID, Claim claim);
    boolean deleteClaim(String fID);
}
