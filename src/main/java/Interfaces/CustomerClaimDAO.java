package Interfaces;

import Models.Claim.Claim;
import Models.Customer.Customer;

import java.util.List;

public interface CustomerClaimDAO {
    boolean addClaim(String insuranceCardNumber);
    Claim getClaimByID(String fID);
    boolean getAllClaimsByCustomer(String cID);
    List<Claim> getAllClaims();
    boolean updateClaim(String fID, Claim claim);
    boolean deleteClaim(String fID);
}
