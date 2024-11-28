package br.com.fatec.donationhaapi.dto;

public class FileUploadResponse {
    private String filename;
    
    public void setFilename(String filename) {
        this.filename = filename;
    }
    public void setDownloadUrl(String downloadUrl) {
        this.downloadUrl = downloadUrl;
    }
    public String getFilename() {
        return filename;
    }
    public String getDownloadUrl() {
        return downloadUrl;
    }
    private String downloadUrl;
}
