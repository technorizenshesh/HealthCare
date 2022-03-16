package com.technorizen.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResAddGetWorkerAvail implements Serializable {


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
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("worker_availability")
        @Expose
        public String workerAvailability;
        @SerializedName("monday_from")
        @Expose
        public String mondayFrom;
        @SerializedName("monday_to")
        @Expose
        public String mondayTo;
        @SerializedName("monday")
        @Expose
        public String monday;
        @SerializedName("tuesday_from")
        @Expose
        public String tuesdayFrom;
        @SerializedName("tuesday_to")
        @Expose
        public String tuesdayTo;
        @SerializedName("tuesday")
        @Expose
        public String tuesday;
        @SerializedName("wednesday_from")
        @Expose
        public String wednesdayFrom;
        @SerializedName("wednesday_to")
        @Expose
        public String wednesdayTo;
        @SerializedName("wednesday")
        @Expose
        public String wednesday;
        @SerializedName("thursday_from")
        @Expose
        public String thursdayFrom;
        @SerializedName("thursday_to")
        @Expose
        public String thursdayTo;
        @SerializedName("thursday")
        @Expose
        public String thursday;
        @SerializedName("friday_from")
        @Expose
        public String fridayFrom;
        @SerializedName("friday_to")
        @Expose
        public String fridayTo;
        @SerializedName("friday")
        @Expose
        public String friday;
        @SerializedName("saturday_form")
        @Expose
        public String saturdayForm;
        @SerializedName("saturday_to")
        @Expose
        public String saturdayTo;
        @SerializedName("saturday")
        @Expose
        public String saturday;
        @SerializedName("sunday_form")
        @Expose
        public String sundayForm;
        @SerializedName("sunday_to")
        @Expose
        public String sundayTo;
        @SerializedName("sunday")
        @Expose
        public String sunday;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("status")
        @Expose
        public String status;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getWorkerId() {
            return workerId;
        }

        public void setWorkerId(String workerId) {
            this.workerId = workerId;
        }

        public String getWorkerAvailability() {
            return workerAvailability;
        }

        public void setWorkerAvailability(String workerAvailability) {
            this.workerAvailability = workerAvailability;
        }

        public String getMondayFrom() {
            return mondayFrom;
        }

        public void setMondayFrom(String mondayFrom) {
            this.mondayFrom = mondayFrom;
        }

        public String getMondayTo() {
            return mondayTo;
        }

        public void setMondayTo(String mondayTo) {
            this.mondayTo = mondayTo;
        }

        public String getMonday() {
            return monday;
        }

        public void setMonday(String monday) {
            this.monday = monday;
        }

        public String getTuesdayFrom() {
            return tuesdayFrom;
        }

        public void setTuesdayFrom(String tuesdayFrom) {
            this.tuesdayFrom = tuesdayFrom;
        }

        public String getTuesdayTo() {
            return tuesdayTo;
        }

        public void setTuesdayTo(String tuesdayTo) {
            this.tuesdayTo = tuesdayTo;
        }

        public String getTuesday() {
            return tuesday;
        }

        public void setTuesday(String tuesday) {
            this.tuesday = tuesday;
        }

        public String getWednesdayFrom() {
            return wednesdayFrom;
        }

        public void setWednesdayFrom(String wednesdayFrom) {
            this.wednesdayFrom = wednesdayFrom;
        }

        public String getWednesdayTo() {
            return wednesdayTo;
        }

        public void setWednesdayTo(String wednesdayTo) {
            this.wednesdayTo = wednesdayTo;
        }

        public String getWednesday() {
            return wednesday;
        }

        public void setWednesday(String wednesday) {
            this.wednesday = wednesday;
        }

        public String getThursdayFrom() {
            return thursdayFrom;
        }

        public void setThursdayFrom(String thursdayFrom) {
            this.thursdayFrom = thursdayFrom;
        }

        public String getThursdayTo() {
            return thursdayTo;
        }

        public void setThursdayTo(String thursdayTo) {
            this.thursdayTo = thursdayTo;
        }

        public String getThursday() {
            return thursday;
        }

        public void setThursday(String thursday) {
            this.thursday = thursday;
        }

        public String getFridayFrom() {
            return fridayFrom;
        }

        public void setFridayFrom(String fridayFrom) {
            this.fridayFrom = fridayFrom;
        }

        public String getFridayTo() {
            return fridayTo;
        }

        public void setFridayTo(String fridayTo) {
            this.fridayTo = fridayTo;
        }

        public String getFriday() {
            return friday;
        }

        public void setFriday(String friday) {
            this.friday = friday;
        }

        public String getSaturdayForm() {
            return saturdayForm;
        }

        public void setSaturdayForm(String saturdayForm) {
            this.saturdayForm = saturdayForm;
        }

        public String getSaturdayTo() {
            return saturdayTo;
        }

        public void setSaturdayTo(String saturdayTo) {
            this.saturdayTo = saturdayTo;
        }

        public String getSaturday() {
            return saturday;
        }

        public void setSaturday(String saturday) {
            this.saturday = saturday;
        }

        public String getSundayForm() {
            return sundayForm;
        }

        public void setSundayForm(String sundayForm) {
            this.sundayForm = sundayForm;
        }

        public String getSundayTo() {
            return sundayTo;
        }

        public void setSundayTo(String sundayTo) {
            this.sundayTo = sundayTo;
        }

        public String getSunday() {
            return sunday;
        }

        public void setSunday(String sunday) {
            this.sunday = sunday;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

    }
    
}

