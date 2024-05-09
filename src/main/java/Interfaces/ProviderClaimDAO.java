package Interfaces;

import Models.Claim.Claim;

public interface ProviderClaimDAO {
    void getAllClaims();
    void getClaimByID(String fID);
    void filterClaim();
    void requireMoreInfo();
    void proposeClaim(Claim claim);
    boolean approveClaim(Claim claim);
    boolean rejectClaim(Claim claim);
}
