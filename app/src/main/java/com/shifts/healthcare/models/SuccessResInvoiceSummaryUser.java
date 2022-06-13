package com.shifts.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResInvoiceSummaryUser implements Serializable {

    @SerializedName("result")
    @Expose
    public List<Result> result = null;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public List<Result> getResult() {
        return result;
    }

    public void setResult(List<Result> result) {
        this.result = result;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("main_invoice_no")
        @Expose
        public String mainInvoiceNo;
        @SerializedName("invoice_no")
        @Expose
        public String invoiceNo;
        @SerializedName("invoice_id")
        @Expose
        public String invoiceId;
        @SerializedName("invoice_amount")
        @Expose
        public String invoiceAmount;
        @SerializedName("total_amount")
        @Expose
        public String totalAmount;
        @SerializedName("final_total")
        @Expose
        public String finalTotal;
        @SerializedName("no_show_cancel_charge")
        @Expose
        public String noShowCancelCharge;
        @SerializedName("pay_before")
        @Expose
        public String payBefore;
        @SerializedName("pay_status")
        @Expose
        public String payStatus;
        @SerializedName("from_date")
        @Expose
        public String fromDate;
        @SerializedName("to_date")
        @Expose
        public String toDate;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("invoice_nos")
        @Expose
        public List<String> invoiceNos = null;
        @SerializedName("invoice_ids")
        @Expose
        public List<String> invoiceIds = null;
        @SerializedName("invoiceamount")
        @Expose
        public List<String> invoiceamount = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getMainInvoiceNo() {
            return mainInvoiceNo;
        }

        public void setMainInvoiceNo(String mainInvoiceNo) {
            this.mainInvoiceNo = mainInvoiceNo;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public String getInvoiceId() {
            return invoiceId;
        }

        public void setInvoiceId(String invoiceId) {
            this.invoiceId = invoiceId;
        }

        public String getInvoiceAmount() {
            return invoiceAmount;
        }

        public void setInvoiceAmount(String invoiceAmount) {
            this.invoiceAmount = invoiceAmount;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getFinalTotal() {
            return finalTotal;
        }

        public void setFinalTotal(String finalTotal) {
            this.finalTotal = finalTotal;
        }

        public String getNoShowCancelCharge() {
            return noShowCancelCharge;
        }

        public void setNoShowCancelCharge(String noShowCancelCharge) {
            this.noShowCancelCharge = noShowCancelCharge;
        }

        public String getPayBefore() {
            return payBefore;
        }

        public void setPayBefore(String payBefore) {
            this.payBefore = payBefore;
        }

        public String getPayStatus() {
            return payStatus;
        }

        public void setPayStatus(String payStatus) {
            this.payStatus = payStatus;
        }

        public String getFromDate() {
            return fromDate;
        }

        public void setFromDate(String fromDate) {
            this.fromDate = fromDate;
        }

        public String getToDate() {
            return toDate;
        }

        public void setToDate(String toDate) {
            this.toDate = toDate;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public List<String> getInvoiceNos() {
            return invoiceNos;
        }

        public void setInvoiceNos(List<String> invoiceNos) {
            this.invoiceNos = invoiceNos;
        }

        public List<String> getInvoiceIds() {
            return invoiceIds;
        }

        public void setInvoiceIds(List<String> invoiceIds) {
            this.invoiceIds = invoiceIds;
        }

        public List<String> getInvoiceamount() {
            return invoiceamount;
        }

        public void setInvoiceamount(List<String> invoiceamount) {
            this.invoiceamount = invoiceamount;
        }

    }

}

