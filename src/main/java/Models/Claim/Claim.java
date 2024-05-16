package Models.Claim;

import Models.InsuranceCard.InsuranceCard;

import java.math.BigDecimal;
import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.Objects;

public class Claim {
    private String fID;
    private LocalDate claimDate;
    private String insuredPerson;
    private InsuranceCard cardNumber;
    private LocalDate examDate;
    private List<Document> documentList;
    private BigDecimal claimAmount;
    private ClaimStatus status;
    private String receiverBankingInfo;
    private String note;

    public Claim(String fID, LocalDate claimDate, String insuredPerson, InsuranceCard cardNumber, LocalDate examDate, List<Document> documentList, BigDecimal claimAmount, ClaimStatus status, String receiverBankingInfo, String note) {
        this.fID = fID;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.documentList = documentList;
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBankingInfo = receiverBankingInfo;
        this.note = note;
    }
    public Claim(String fID, String insuredPerson, InsuranceCard cardNumber, LocalDate examDate, BigDecimal claimAmount, ClaimStatus status) {
        this.fID = fID;
        this.claimDate = LocalDate.now();
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.documentList = new ArrayList<>();
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBankingInfo = null;
        this.note = null;
    }

    public String getFID() {
        return fID;
    }

    public void setFID(String fID) {
        this.fID = fID;
    }

    public LocalDate getClaimDate() {
        return claimDate;
    }

    public void setClaimDate(LocalDate claimDate) {
        this.claimDate = claimDate;
    }

    public String getInsuredPerson() {
        return insuredPerson;
    }

    public void setInsuredPerson(String insuredPerson) {
        this.insuredPerson = insuredPerson;
    }

    public InsuranceCard getCardNumber() {
        return cardNumber;
    }

    public void setCardNumber(InsuranceCard cardNumber) {
        this.cardNumber = cardNumber;
    }

    public LocalDate getExamDate() {
        return examDate;
    }

    public void setExamDate(LocalDate examDate) {
        this.examDate = examDate;
    }

    public List<Document> getDocumentList() {
        return documentList;
    }

    public void setDocumentList(List<Document> documentList) {
        this.documentList = documentList;
    }

    public BigDecimal getClaimAmount() {
        return claimAmount;
    }

    public void setClaimAmount(BigDecimal claimAmount) {
        this.claimAmount = claimAmount;
    }

    public String getStatus() {
        return String.valueOf(status);
    }

    public void setStatus(ClaimStatus status) {
        this.status = status;
    }

    public String getReceiverBankingInfo() {
        return receiverBankingInfo;
    }

    public void setReceiverBankingInfo(String bank, String name, String number) {
        this.receiverBankingInfo = bank + " - " + name + " - " + number;;
    }

    public boolean addDocument(Document document) {
        return false;
    }
    public boolean removeDocument(Document document) {
        return false;
    }
    public boolean getDocument(Document document) {
        return false;
    }

    public void setReceiverBankingInfo(String receiverBankingInfo) {
        this.receiverBankingInfo = receiverBankingInfo;
    }

    public String getNote() {
        return note;
    }

    public void setNote(String note) {
        this.note = note;
    }

    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        Claim other = (Claim) o;
        return Objects.equals(this.getFID(), other.getFID());
    }
    @Override
    public int hashCode() {
        return Objects.hash(getFID());
    }

    @Override
    public String toString() {
        return "Claim{" +
                "fID='" + fID + '\'' +
                ", claimDate=" + claimDate +
                ", insuredPerson='" + insuredPerson + '\'' +
                ", cardNumber=" + cardNumber +
                ", examDate=" + examDate +
                ", documentList=" + documentList +
                ", claimAmount=" + claimAmount +
                ", status=" + status +
                ", receiverBankingInfo='" + receiverBankingInfo + '\'' +
                ", note='" + note + '\'' +
                '}';
    }
}

