package com.shifts.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class SuccessResEditShift implements Serializable {


    @SerializedName("result")
    @Expose
    public Result result;
    @SerializedName("message")
    @Expose
    public String message;
    @SerializedName("status")
    @Expose
    public String status;

    public Result getResult() {
        return result;
    }

    public void setResult(Result result) {
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
        @SerializedName("shift_no")
        @Expose
        public String shiftNo;
        @SerializedName("shift_no_new")
        @Expose
        public String shiftNoNew;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("job_position")
        @Expose
        public String jobPosition;
        @SerializedName("no_vacancies")
        @Expose
        public String noVacancies;
        @SerializedName("remaining_vacancies")
        @Expose
        public String remainingVacancies;
        @SerializedName("hourly_rate")
        @Expose
        public String hourlyRate;
        @SerializedName("duty_of_worker")
        @Expose
        public String dutyOfWorker;
        @SerializedName("covid_status")
        @Expose
        public String covidStatus;
        @SerializedName("unpaid_break")
        @Expose
        public String unpaidBreak;
        @SerializedName("transit_allowance")
        @Expose
        public String transitAllowance;
        @SerializedName("shift_location")
        @Expose
        public String shiftLocation;
        @SerializedName("lat")
        @Expose
        public String lat;
        @SerializedName("lon")
        @Expose
        public String lon;
        @SerializedName("day_type")
        @Expose
        public String dayType;
        @SerializedName("time_type")
        @Expose
        public String timeType;
        @SerializedName("shift_notes")
        @Expose
        public String shiftNotes;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("status_new")
        @Expose
        public String statusNew;
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("date_time")
        @Expose
        public String dateTime;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getShiftNo() {
            return shiftNo;
        }

        public void setShiftNo(String shiftNo) {
            this.shiftNo = shiftNo;
        }

        public String getShiftNoNew() {
            return shiftNoNew;
        }

        public void setShiftNoNew(String shiftNoNew) {
            this.shiftNoNew = shiftNoNew;
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

        public String getJobPosition() {
            return jobPosition;
        }

        public void setJobPosition(String jobPosition) {
            this.jobPosition = jobPosition;
        }

        public String getNoVacancies() {
            return noVacancies;
        }

        public void setNoVacancies(String noVacancies) {
            this.noVacancies = noVacancies;
        }

        public String getRemainingVacancies() {
            return remainingVacancies;
        }

        public void setRemainingVacancies(String remainingVacancies) {
            this.remainingVacancies = remainingVacancies;
        }

        public String getHourlyRate() {
            return hourlyRate;
        }

        public void setHourlyRate(String hourlyRate) {
            this.hourlyRate = hourlyRate;
        }

        public String getDutyOfWorker() {
            return dutyOfWorker;
        }

        public void setDutyOfWorker(String dutyOfWorker) {
            this.dutyOfWorker = dutyOfWorker;
        }

        public String getCovidStatus() {
            return covidStatus;
        }

        public void setCovidStatus(String covidStatus) {
            this.covidStatus = covidStatus;
        }

        public String getUnpaidBreak() {
            return unpaidBreak;
        }

        public void setUnpaidBreak(String unpaidBreak) {
            this.unpaidBreak = unpaidBreak;
        }

        public String getTransitAllowance() {
            return transitAllowance;
        }

        public void setTransitAllowance(String transitAllowance) {
            this.transitAllowance = transitAllowance;
        }

        public String getShiftLocation() {
            return shiftLocation;
        }

        public void setShiftLocation(String shiftLocation) {
            this.shiftLocation = shiftLocation;
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

        public String getDayType() {
            return dayType;
        }

        public void setDayType(String dayType) {
            this.dayType = dayType;
        }

        public String getTimeType() {
            return timeType;
        }

        public void setTimeType(String timeType) {
            this.timeType = timeType;
        }

        public String getShiftNotes() {
            return shiftNotes;
        }

        public void setShiftNotes(String shiftNotes) {
            this.shiftNotes = shiftNotes;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getStatusNew() {
            return statusNew;
        }

        public void setStatusNew(String statusNew) {
            this.statusNew = statusNew;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

    }

}

