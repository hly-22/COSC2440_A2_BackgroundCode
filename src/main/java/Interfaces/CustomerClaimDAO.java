package Interfaces;

import Models.Claim.Claim;
import Models.Claim.Document;
import Models.Customer.Customer;

import java.util.List;

public interface CustomerClaimDAO {
    boolean addClaim(String insuranceCardNumber);
    Claim getClaimByID(String fID);
    List<Claim> getAllClaims();
    boolean updateClaim(String fID, List<Document> documentList, String receiverBankingInfo);
    boolean deleteClaim(String fID);
}
