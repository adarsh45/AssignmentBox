package com.example.assignmentbox.pojo;

public class UploadPOJO {

    public String pdfName;
    public String pdfUrl;



    public UploadPOJO(String namePdf, String urlPdf){
        this.pdfName = namePdf;
        this.pdfUrl = urlPdf;

    }

    public String getPdfName() {
        return pdfName;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

}
