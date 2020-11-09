package com.example.assignmentbox.pojo;

public class UploadPOJO {

    public String pdfName;
    public String pdfUrl;
    public String pdfComments;



    public UploadPOJO(String namePdf, String urlPdf, String commentsPdf){
        this.pdfName = namePdf;
        this.pdfUrl = urlPdf;
        this.pdfComments = commentsPdf;
    }

    public String getPdfName() {
        return pdfName;
    }

    public String getPdfUrl() {
        return pdfUrl;
    }

    public String getPdfComments() {
        return pdfComments;
    }

    public void setPdfName(String pdfName) {
        this.pdfName = pdfName;
    }

    public void setPdfUrl(String pdfUrl) {
        this.pdfUrl = pdfUrl;
    }

    public void setPdfComments(String pdfComments) {
        this.pdfComments = pdfComments;
    }

}
