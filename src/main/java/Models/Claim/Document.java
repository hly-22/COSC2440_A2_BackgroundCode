package Models.Claim;

public class Document {
    private String documentID;
    private String fID;
    private String fileName;
    private String convertFileName;
    private String URL;

    public Document(String documentID, String fID, String fileName, String convertFileName, String URL) {
        this.documentID = documentID;
        this.fID = fID;
        this.fileName = fileName;
        this.convertFileName = setConvertFileName(fID, fileName);
        this.URL = URL;
    }

    public String getDocumentID() {
        return documentID;
    }

    public void setDocumentID(String documentID) {
        this.documentID = documentID;
    }

    public String getfID() {
        return fID;
    }

    public void setfID(String fID) {
        this.fID = fID;
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

    public String setConvertFileName(String fID, String fileName) {
        return fID + "_" + fileName + ".pdf";
    }

    public String getURL() {
        return URL;
    }

    public void setURL(String URL) {
        this.URL = URL;
    }

    @Override
    public String toString() {
        return "Document{" +
                "documentID='" + documentID + '\'' +
                ", fID='" + fID + '\'' +
                ", fileName='" + fileName + '\'' +
                ", convertFileName='" + convertFileName + '\'' +
                ", URL='" + URL + '\'' +
                '}';
    }
}
