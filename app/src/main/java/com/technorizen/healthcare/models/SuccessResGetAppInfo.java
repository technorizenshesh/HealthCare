package com.technorizen.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetAppInfo implements Serializable {

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
        @SerializedName("name")
        @Expose
        public String name;
        @SerializedName("logo")
        @Expose
        public String logo;
        @SerializedName("icon")
        @Expose
        public String icon;
        @SerializedName("play_store")
        @Expose
        public String playStore;
        @SerializedName("ios_store")
        @Expose
        public String iosStore;
        @SerializedName("facebook")
        @Expose
        public String facebook;
        @SerializedName("insta")
        @Expose
        public String insta;
        @SerializedName("youtube")
        @Expose
        public String youtube;
        @SerializedName("linkdin")
        @Expose
        public String linkdin;
        @SerializedName("google")
        @Expose
        public String google;
        @SerializedName("twitter")
        @Expose
        public String twitter;
        @SerializedName("mobile")
        @Expose
        public String mobile;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("email")
        @Expose
        public String email;
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

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getLogo() {
            return logo;
        }

        public void setLogo(String logo) {
            this.logo = logo;
        }

        public String getIcon() {
            return icon;
        }

        public void setIcon(String icon) {
            this.icon = icon;
        }

        public String getPlayStore() {
            return playStore;
        }

        public void setPlayStore(String playStore) {
            this.playStore = playStore;
        }

        public String getIosStore() {
            return iosStore;
        }

        public void setIosStore(String iosStore) {
            this.iosStore = iosStore;
        }

        public String getFacebook() {
            return facebook;
        }

        public void setFacebook(String facebook) {
            this.facebook = facebook;
        }

        public String getInsta() {
            return insta;
        }

        public void setInsta(String insta) {
            this.insta = insta;
        }

        public String getYoutube() {
            return youtube;
        }

        public void setYoutube(String youtube) {
            this.youtube = youtube;
        }

        public String getLinkdin() {
            return linkdin;
        }

        public void setLinkdin(String linkdin) {
            this.linkdin = linkdin;
        }

        public String getGoogle() {
            return google;
        }

        public void setGoogle(String google) {
            this.google = google;
        }

        public String getTwitter() {
            return twitter;
        }

        public void setTwitter(String twitter) {
            this.twitter = twitter;
        }

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
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

