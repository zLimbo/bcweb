package com.zlimbo.bcweb.domain;

public class Invoice {


    private String id;
    private String hashValue;
    private String invoiceNo;
    private String buyerName;
    private String buyerTaxesNo;
    private String sellerName;
    private String sellerTaxesNo;
    private String invoiceDate;
    private String invoiceType;
    private String taxesPoint;
    private String taxes;
    private String price;
    private String pricePlusTaxes;
    private String invoiceNumber;
    private String statementSheet;
    private String statementWeight;
    private String timestamp;

    public Invoice() { }

    public Invoice(String id,
                   String hashValue,
                   String invoiceNo,
                   String buyerName,
                   String buyerTaxesNo,
                   String sellerName,
                   String sellerTaxesNo,
                   String invoiceDate,
                   String invoiceType,
                   String taxesPoint,
                   String taxes,
                   String price,
                   String pricePlusTaxes,
                   String invoiceNumber,
                   String statementSheet,
                   String statementWeight,
                   String timestamp) {
        this.id = id;
        this.hashValue = hashValue;
        this.invoiceNo = invoiceNo;
        this.buyerName = buyerName;
        this.buyerTaxesNo = buyerTaxesNo;
        this.sellerName = sellerName;
        this.sellerTaxesNo = sellerTaxesNo;
        this.invoiceDate = invoiceDate;
        this.invoiceType = invoiceType;
        this.taxesPoint = taxesPoint;
        this.taxes = taxes;
        this.price = price;
        this.pricePlusTaxes = pricePlusTaxes;
        this.invoiceNumber = invoiceNumber;
        this.statementSheet = statementSheet;
        this.statementWeight = statementWeight;
        this.timestamp = timestamp;
    }

    public String getHashValue() {
        return hashValue;
    }

    public void setHashValue(String hashValue) {
        this.hashValue = hashValue;
    }

    public String getInvoiceNo() {
        return invoiceNo;
    }

    public void setInvoiceNo(String invoiceNo) {
        this.invoiceNo = invoiceNo;
    }

    public String getBuyerName() {
        return buyerName;
    }

    public void setBuyerName(String buyerName) {
        this.buyerName = buyerName;
    }

    public String getBuyerTaxesNo() {
        return buyerTaxesNo;
    }

    public void setBuyerTaxesNo(String buyerTaxesNo) {
        this.buyerTaxesNo = buyerTaxesNo;
    }

    public String getSellerName() {
        return sellerName;
    }

    public void setSellerName(String sellerName) {
        this.sellerName = sellerName;
    }

    public String getSellerTaxesNo() {
        return sellerTaxesNo;
    }

    public void setSellerTaxesNo(String sellerTaxesNo) {
        this.sellerTaxesNo = sellerTaxesNo;
    }

    public String getInvoiceDate() {
        return invoiceDate;
    }

    public void setInvoiceDate(String invoiceDate) {
        this.invoiceDate = invoiceDate;
    }

    public String getInvoiceType() {
        return invoiceType;
    }

    public void setInvoiceType(String invoiceType) {
        this.invoiceType = invoiceType;
    }

    public String getTaxesPoint() {
        return taxesPoint;
    }

    public void setTaxesPoint(String taxesPoint) {
        this.taxesPoint = taxesPoint;
    }

    public String getTaxes() {
        return taxes;
    }

    public void setTaxes(String taxes) {
        this.taxes = taxes;
    }

    public String getPrice() {
        return price;
    }

    public void setPrice(String price) {
        this.price = price;
    }

    public String getPricePlusTaxes() {
        return pricePlusTaxes;
    }

    public void setPricePlusTaxes(String pricePlusTaxes) {
        this.pricePlusTaxes = pricePlusTaxes;
    }

    public String getInvoiceNumber() {
        return invoiceNumber;
    }

    public void setInvoiceNumber(String invoiceNumber) {
        this.invoiceNumber = invoiceNumber;
    }

    public String getStatementSheet() {
        return statementSheet;
    }

    public void setStatementSheet(String statementSheet) {
        this.statementSheet = statementSheet;
    }

    public String getStatementWeight() {
        return statementWeight;
    }

    public void setStatementWeight(String statementWeight) {
        this.statementWeight = statementWeight;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}
