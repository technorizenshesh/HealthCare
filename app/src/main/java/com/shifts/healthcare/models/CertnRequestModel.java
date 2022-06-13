package com.shifts.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class CertnRequestModel implements Serializable {


    @SerializedName("request_enhanced_criminal_record_check")
    @Expose
    public Boolean requestEnhancedCriminalRecordCheck;
    @SerializedName("request_enhanced_identity_verification")
    @Expose
    public Boolean requestEnhancedIdentityVerification;
    @SerializedName("email")
    @Expose
    public String email;
    @SerializedName("information")
    @Expose
    public Information information;

    public Boolean getRequestEnhancedCriminalRecordCheck() {
        return requestEnhancedCriminalRecordCheck;
    }

    public void setRequestEnhancedCriminalRecordCheck(Boolean requestEnhancedCriminalRecordCheck) {
        this.requestEnhancedCriminalRecordCheck = requestEnhancedCriminalRecordCheck;
    }

    public Boolean getRequestEnhancedIdentityVerification() {
        return requestEnhancedIdentityVerification;
    }

    public void setRequestEnhancedIdentityVerification(Boolean requestEnhancedIdentityVerification) {
        this.requestEnhancedIdentityVerification = requestEnhancedIdentityVerification;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public Information getInformation() {
        return information;
    }

    public void setInformation(Information information) {
        this.information = information;
    }


    public static class Address implements Serializable {

        @SerializedName("address")
        @Expose
        public String address;
        @SerializedName("city")
        @Expose
        public String city;
        @SerializedName("province_state")
        @Expose
        public String provinceState;
        @SerializedName("country")
        @Expose
        public String country;

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getCity() {
            return city;
        }

        public void setCity(String city) {
            this.city = city;
        }

        public String getProvinceState() {
            return provinceState;
        }

        public void setProvinceState(String provinceState) {
            this.provinceState = provinceState;
        }

        public String getCountry() {
            return country;
        }

        public void setCountry(String country) {
            this.country = country;
        }

    }


    public static class Conviction implements Serializable {

        @SerializedName("offense")
        @Expose
        public String offense;
        @SerializedName("date_of_sentence")
        @Expose
        public String dateOfSentence;
        @SerializedName("court_location")
        @Expose
        public String courtLocation;
        @SerializedName("description")
        @Expose
        public String description;

        public String getOffense() {
            return offense;
        }

        public void setOffense(String offense) {
            this.offense = offense;
        }

        public String getDateOfSentence() {
            return dateOfSentence;
        }

        public void setDateOfSentence(String dateOfSentence) {
            this.dateOfSentence = dateOfSentence;
        }

        public String getCourtLocation() {
            return courtLocation;
        }

        public void setCourtLocation(String courtLocation) {
            this.courtLocation = courtLocation;
        }

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

    }


    public static class Information implements Serializable {

        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("date_of_birth")
        @Expose
        public String dateOfBirth;
        @SerializedName("birth_city")
        @Expose
        public String birthCity;
        @SerializedName("birth_province_state")
        @Expose
        public String birthProvinceState;
        @SerializedName("birth_country")
        @Expose
        public String birthCountry;
        @SerializedName("gender")
        @Expose
        public String gender;
        @SerializedName("phone_number")
        @Expose
        public String phoneNumber;
        @SerializedName("addresses")
        @Expose
        public List<Address> addresses = null;
        @SerializedName("convictions")
        @Expose
        public List<Conviction> convictions = null;
        @SerializedName("rcmp_consent_given")
        @Expose
        public Boolean rcmpConsentGiven;

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

        public String getDateOfBirth() {
            return dateOfBirth;
        }

        public void setDateOfBirth(String dateOfBirth) {
            this.dateOfBirth = dateOfBirth;
        }

        public String getBirthCity() {
            return birthCity;
        }

        public void setBirthCity(String birthCity) {
            this.birthCity = birthCity;
        }

        public String getBirthProvinceState() {
            return birthProvinceState;
        }

        public void setBirthProvinceState(String birthProvinceState) {
            this.birthProvinceState = birthProvinceState;
        }

        public String getBirthCountry() {
            return birthCountry;
        }

        public void setBirthCountry(String birthCountry) {
            this.birthCountry = birthCountry;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPhoneNumber() {
            return phoneNumber;
        }

        public void setPhoneNumber(String phoneNumber) {
            this.phoneNumber = phoneNumber;
        }

        public List<Address> getAddresses() {
            return addresses;
        }

        public void setAddresses(List<Address> addresses) {
            this.addresses = addresses;
        }

        public List<Conviction> getConvictions() {
            return convictions;
        }

        public void setConvictions(List<Conviction> convictions) {
            this.convictions = convictions;
        }

        public Boolean getRcmpConsentGiven() {
            return rcmpConsentGiven;
        }

        public void setRcmpConsentGiven(Boolean rcmpConsentGiven) {
            this.rcmpConsentGiven = rcmpConsentGiven;
        }

    }


}

