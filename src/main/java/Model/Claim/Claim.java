package Model.Claim;

import Model.InsuranceCard.InsuranceCard;

import java.math.BigDecimal;
import java.time.LocalDate;
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

    public Claim(String fID, LocalDate claimDate, String insuredPerson, InsuranceCard cardNumber, LocalDate examDate, List<Document> documentList, BigDecimal claimAmount, ClaimStatus status, String receiverBankingInfo) {
        this.fID = fID;
        this.claimDate = claimDate;
        this.insuredPerson = insuredPerson;
        this.cardNumber = cardNumber;
        this.examDate = examDate;
        this.documentList = documentList;
        this.claimAmount = claimAmount;
        this.status = status;
        this.receiverBankingInfo = receiverBankingInfo;
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

    public ClaimStatus getStatus() {
        return status;
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
}

enum ClaimStatus {
    NEW,
    PROCESSING,
    APPROVED,
    REJECTED
}

class Document {
    private String fileName;
    private String convertFileName;
    private LocalDate uploadDate;
    private boolean isPDF;

    public Document(String fileName, LocalDate uploadDate, boolean isPDF) {
        this.fileName = fileName;
        this.convertFileName = namingConversion(fileName);
        this.uploadDate = uploadDate;
        this.isPDF = isPDF;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public String getConvertFileName() {
        return convertFileName;
    }

    public void setConvertFileName(String convertFileName) {
        this.convertFileName = convertFileName;
    }

    public LocalDate getUploadDate() {
        return uploadDate;
    }

    public void setUploadDate(LocalDate uploadDate) {
        this.uploadDate = uploadDate;
    }

    public boolean isPDF() {
        return isPDF;
    }

    public void setPDF(boolean PDF) {
        isPDF = PDF;
    }

    public String namingConversion(String fileName) {
        // naming format: fID_fileName_uploadDate.pdf
        return fileName + "_" + ".pdf";
    }
}
