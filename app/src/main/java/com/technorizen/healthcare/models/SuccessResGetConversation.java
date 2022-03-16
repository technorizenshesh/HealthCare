package com.technorizen.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetConversation implements Serializable {
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
        @SerializedName("account_type")
        @Expose
        public String accountType;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("email")
        @Expose
        public String email;
        @SerializedName("phone")
        @Expose
        public String phone;
        @SerializedName("password")
        @Expose
        public String password;
        @SerializedName("company")
        @Expose
        public String company;
        @SerializedName("company_website")
        @Expose
        public String companyWebsite;
        @SerializedName("street_no")
        @Expose
        public String streetNo;
        @SerializedName("street_name")
        @Expose
        public String streetName;
        @SerializedName("apartment_no")
        @Expose
        public String apartmentNo;
        @SerializedName("worker_designation")
        @Expose
        public String workerDesignation;
        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("state")
        @Expose
        public String state;
        @SerializedName("couttry_code")
        @Expose
        public String couttryCode;
        @SerializedName("country")
        @Expose
        public String country;
        @SerializedName("zipcode")
        @Expose
        public String zipcode;
        @SerializedName("description")
        @Expose
        public String description;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("rate")
        @Expose
        public String rate;
        @SerializedName("sms_notification")
        @Expose
        public String smsNotification;
        @SerializedName("psus_notification")
        @Expose
        public String psusNotification;
        @SerializedName("email_notification")
        @Expose
        public String emailNotification;
        @SerializedName("distance")
        @Expose
        public String distance;
        @SerializedName("register_id")
        @Expose
        public String registerId;
        @SerializedName("admin_approval")
        @Expose
        public String adminApproval;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("worker_availability")
        @Expose
        public String workerAvailability;
        @SerializedName("email_code")
        @Expose
        public String emailCode;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("no_of_message")
        @Expose
        public Integer noOfMessage;
        @SerializedName("last_message")
        @Expose
        public String lastMessage;
        @SerializedName("last_image")
        @Expose
        public String lastImage;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("time")
        @Expose
        public Object time;
        @SerializedName("time_ago")
        @Expose
        public String timeAgo;
        @SerializedName("sender_id")
        @Expose
        public String senderId;
        @SerializedName("receiver_id")
        @Expose
        public String receiverId;
        @SerializedName("my_type")
        @Expose
        public String myType;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getFirstName() {
            return firstName;
        }

        public void setFirstName(String firstName) {
            this.firstName = firstName;
        }

        public String getLastName() {
            return lastName;
        }

        public void setLastName(String lastName) {
            this.lastName = lastName;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getEmail() {
            return email;
        }

        public void setEmail(String email) {
            this.email = email;
        }

        public String getPhone() {
            return phone;
        }

        public void setPhone(String phone) {
            this.phone = phone;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getCompanyWebsite() {
            return companyWebsite;
        }

        public void setCompanyWebsite(String companyWebsite) {
            this.companyWebsite = companyWebsite;
        }

        public String getStreetNo() {
            return streetNo;
        }

        public void setStreetNo(String streetNo) {
            this.streetNo = streetNo;
        }

        public String getStreetName() {
            return streetName;
        }

        public void setStreetName(String streetName) {
            this.streetName = streetName;
        }

        public String getApartmentNo() {
            return apartmentNo;
        }

        public void setApartmentNo(String apartmentNo) {
            this.apartmentNo = apartmentNo;
        }

        public String getWorkerDesignation() {
            return workerDesignation;
        }

        public void setWorkerDesignation(String workerDesignation) {
            this.workerDesignation = workerDesignation;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getLat() {
            return lat;
        }

        public void setLat(String lat) {
            this.lat = lat;
        }

        public String getLon() {
            return lon;
        }

        public void setLon(String lon) {
            this.lon = lon;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getState() {
            return state;
        }

        public void setState(String state) {
            this.state = state;
        }

        public String getCouttryCode() {
            return couttryCode;
        }

        public void setCouttryCode(String couttryCode) {
            this.couttryCode = couttryCode;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

        public String getZipcode() {
            return zipcode;
        }

        public void setZipcode(String zipcode) {
            this.zipcode = zipcode;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getRate() {
            return rate;
        }

        public void setRate(String rate) {
            this.rate = rate;
        }

        public String getSmsNotification() {
            return smsNotification;
        }

        public void setSmsNotification(String smsNotification) {
            this.smsNotification = smsNotification;
        }

        public String getPsusNotification() {
            return psusNotification;
        }

        public void setPsusNotification(String psusNotification) {
            this.psusNotification = psusNotification;
        }

        public String getEmailNotification() {
            return emailNotification;
        }

        public void setEmailNotification(String emailNotification) {
            this.emailNotification = emailNotification;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

        public String getRegisterId() {
            return registerId;
        }

        public void setRegisterId(String registerId) {
            this.registerId = registerId;
        }

        public String getAdminApproval() {
            return adminApproval;
        }

        public void setAdminApproval(String adminApproval) {
            this.adminApproval = adminApproval;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getWorkerAvailability() {
            return workerAvailability;
        }

        public void setWorkerAvailability(String workerAvailability) {
            this.workerAvailability = workerAvailability;
        }

        public String getEmailCode() {
            return emailCode;
        }

        public void setEmailCode(String emailCode) {
            this.emailCode = emailCode;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public Integer getNoOfMessage() {
            return noOfMessage;
        }

        public void setNoOfMessage(Integer noOfMessage) {
            this.noOfMessage = noOfMessage;
        }

        public String getLastMessage() {
            return lastMessage;
        }

        public void setLastMessage(String lastMessage) {
            this.lastMessage = lastMessage;
        }

        public String getLastImage() {
            return lastImage;
        }

        public void setLastImage(String lastImage) {
            this.lastImage = lastImage;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public Object getTime() {
            return time;
        }

        public void setTime(Object time) {
            this.time = time;
        }

        public String getTimeAgo() {
            return timeAgo;
        }

        public void setTimeAgo(String timeAgo) {
            this.timeAgo = timeAgo;
        }

        public String getSenderId() {
            return senderId;
        }

        public void setSenderId(String senderId) {
            this.senderId = senderId;
        }

        public String getReceiverId() {
            return receiverId;
        }

        public void setReceiverId(String receiverId) {
            this.receiverId = receiverId;
        }

        public String getMyType() {
            return myType;
        }

        public void setMyType(String myType) {
            this.myType = myType;
        }

    }

}

