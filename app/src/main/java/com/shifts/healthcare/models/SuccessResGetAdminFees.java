package com.shifts.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetAdminFees implements Serializable {

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
        @SerializedName("no_show_for_worker")
        @Expose
        public String noShowForWorker;
        @SerializedName("cancel_for_worker")
        @Expose
        public String cancelForWorker;
        @SerializedName("cancel_for_user")
        @Expose
        public String cancelForUser;
        @SerializedName("admin_fee")
        @Expose
        public String adminFee;
        @SerializedName("hst_tax")
        @Expose
        public String hstTax;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getNoShowForWorker() {
            return noShowForWorker;
        }

        public void setNoShowForWorker(String noShowForWorker) {
            this.noShowForWorker = noShowForWorker;
        }

        public String getCancelForWorker() {
            return cancelForWorker;
        }

        public void setCancelForWorker(String cancelForWorker) {
            this.cancelForWorker = cancelForWorker;
        }

        public String getCancelForUser() {
            return cancelForUser;
        }

        public void setCancelForUser(String cancelForUser) {
            this.cancelForUser = cancelForUser;
        }

        public String getAdminFee() {
            return adminFee;
        }

        public void setAdminFee(String adminFee) {
            this.adminFee = adminFee;
        }

        public String getHstTax() {
            return hstTax;
        }

        public void setHstTax(String hstTax) {
            this.hstTax = hstTax;
        }

    }


}

