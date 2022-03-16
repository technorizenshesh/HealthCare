package com.technorizen.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetTransactionOverview implements Serializable {

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

    public class Paymentdetail {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("invoce_id")
        @Expose
        public String invoceId;
        @SerializedName("shift_id")
        @Expose
        public String shiftId;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("trans_id")
        @Expose
        public String transId;
        @SerializedName("amount")
        @Expose
        public String amount;
        @SerializedName("payment_type")
        @Expose
        public String paymentType;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInvoceId() {
            return invoceId;
        }

        public void setInvoceId(String invoceId) {
            this.invoceId = invoceId;
        }

        public String getShiftId() {
            return shiftId;
        }

        public void setShiftId(String shiftId) {
            this.shiftId = shiftId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getWorkerId() {
            return workerId;
        }

        public void setWorkerId(String workerId) {
            this.workerId = workerId;
        }

        public String getTransId() {
            return transId;
        }

        public void setTransId(String transId) {
            this.transId = transId;
        }

        public String getAmount() {
            return amount;
        }

        public void setAmount(String amount) {
            this.amount = amount;
        }

        public String getPaymentType() {
            return paymentType;
        }

        public void setPaymentType(String paymentType) {
            this.paymentType = paymentType;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }    public class Result {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("invoice_no")
        @Expose
        public String invoiceNo;
        @SerializedName("shifts_status_id")
        @Expose
        public String shiftsStatusId;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("shift_id")
        @Expose
        public String shiftId;
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("total_time")
        @Expose
        public String totalTime;
        @SerializedName("total_worked")
        @Expose
        public String totalWorked;
        @SerializedName("hourly_rate")
        @Expose
        public String hourlyRate;
        @SerializedName("unpaid_break")
        @Expose
        public String unpaidBreak;
        @SerializedName("total_amount")
        @Expose
        public String totalAmount;
        @SerializedName("payement_status")
        @Expose
        public String payementStatus;
        @SerializedName("pay_date")
        @Expose
        public String payDate;
        @SerializedName("transection_date")
        @Expose
        public String transectionDate;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("wallet_amount")
        @Expose
        public String walletAmount;
        @SerializedName("paymentdetail")
        @Expose
        public List<Paymentdetail> paymentdetail = null;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getInvoiceNo() {
            return invoiceNo;
        }

        public void setInvoiceNo(String invoiceNo) {
            this.invoiceNo = invoiceNo;
        }

        public String getShiftsStatusId() {
            return shiftsStatusId;
        }

        public void setShiftsStatusId(String shiftsStatusId) {
            this.shiftsStatusId = shiftsStatusId;
        }

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
        }

        public String getShiftId() {
            return shiftId;
        }

        public void setShiftId(String shiftId) {
            this.shiftId = shiftId;
        }

        public String getWorkerId() {
            return workerId;
        }

        public void setWorkerId(String workerId) {
            this.workerId = workerId;
        }

        public String getTotalTime() {
            return totalTime;
        }

        public void setTotalTime(String totalTime) {
            this.totalTime = totalTime;
        }

        public String getTotalWorked() {
            return totalWorked;
        }

        public void setTotalWorked(String totalWorked) {
            this.totalWorked = totalWorked;
        }

        public String getHourlyRate() {
            return hourlyRate;
        }

        public void setHourlyRate(String hourlyRate) {
            this.hourlyRate = hourlyRate;
        }

        public String getUnpaidBreak() {
            return unpaidBreak;
        }

        public void setUnpaidBreak(String unpaidBreak) {
            this.unpaidBreak = unpaidBreak;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getPayementStatus() {
            return payementStatus;
        }

        public void setPayementStatus(String payementStatus) {
            this.payementStatus = payementStatus;
        }

        public String getPayDate() {
            return payDate;
        }

        public void setPayDate(String payDate) {
            this.payDate = payDate;
        }

        public String getTransectionDate() {
            return transectionDate;
        }

        public void setTransectionDate(String transectionDate) {
            this.transectionDate = transectionDate;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getWalletAmount() {
            return walletAmount;
        }

        public void setWalletAmount(String walletAmount) {
            this.walletAmount = walletAmount;
        }

        public List<Paymentdetail> getPaymentdetail() {
            return paymentdetail;
        }

        public void setPaymentdetail(List<Paymentdetail> paymentdetail) {
            this.paymentdetail = paymentdetail;
        }

    }

}

