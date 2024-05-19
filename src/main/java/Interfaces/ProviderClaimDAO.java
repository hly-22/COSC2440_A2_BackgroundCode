package Interfaces;

import Models.Claim.Claim;

import java.util.List;

public interface ProviderClaimDAO {
    List<Claim> getAllClaims();
    Claim getClaimByID(String fID);
    List<Claim> getClaimsByInsuranceCard(String insuranceCardNumber);
    List<Claim> getClaimsByStatus(String status);
    List<Claim> getClaimsByCID(String cID);
    boolean requireMoreInfo(Claim claim);
    boolean proposeClaim(Claim claim);
    boolean approveClaim(Claim claim);
    boolean rejectClaim(Claim claim);
}
