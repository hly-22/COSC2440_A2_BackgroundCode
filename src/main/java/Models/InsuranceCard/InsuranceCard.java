package Models.InsuranceCard;

import java.time.LocalDate;

public class InsuranceCard {
    private String cardNumber;
    private String cardHolder;
    private String policyOwner;
    private LocalDate expirationDate;

    public InsuranceCard() {
    }

    public InsuranceCard(String cardNumber, String cardHolder, String policyOwner, LocalDate expirationDate) {
        this.cardNumber = cardNumber;
        this.cardHolder = cardHolder;
        this.policyOwner = policyOwner;
        this.expirationDate = expirationDate;
    }

    public InsuranceCard(String cardNumber, String policyOwner, LocalDate expirationDate) {
        this.cardNumber = cardNumber;
        this.cardHolder = null;
        this.policyOwner = policyOwner;
        this.expirationDate = expirationDate;
    }

    public void setPolicyOwner(String policyOwner) {
        this.policyOwner = policyOwner;
    }

    public String getCardNumber() {
        return cardNumber;
    }

    public String getCardHolder() {
        return cardHolder;
    }

    public String getPolicyOwner() {
        if (policyOwner == null) {
            return null;
        }
        return policyOwner;
    }

    public LocalDate getExpirationDate() {
        return expirationDate;
    }

    public void setCardNumber(String cardNumber) {
        this.cardNumber = cardNumber;
    }

    public void setCardHolder(String cardHolder) {
        this.cardHolder = cardHolder;
    }

    public void setExpirationDate(LocalDate expirationDate) {
        this.expirationDate = expirationDate;
    }

    @Override
    public String toString() {
        return "InsuranceCard{" +
                "cardNumber='" + cardNumber + '\'' +
                ", cardHolder='" + cardHolder + '\'' +
                ", policyOwner='" + policyOwner + '\'' +
                ", expirationDate=" + expirationDate +
                '}';
    }
}
