package com.shifts.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResWorkerAcceptedShift implements Serializable {

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
        @SerializedName("shift_no")
        @Expose
        public String shiftNo;
        @SerializedName("vacancies_no")
        @Expose
        public String vacanciesNo;
        @SerializedName("shift_sub_no")
        @Expose
        public String shiftSubNo;
        @SerializedName("shift_id")
        @Expose
        public String shiftId;
        @SerializedName("worker_id")
        @Expose
        public String workerId;
        @SerializedName("time_id")
        @Expose
        public String timeId;
        @SerializedName("shift_date")
        @Expose
        public String shiftDate;
        @SerializedName("start_time")
        @Expose
        public String startTime;
        @SerializedName("end_time")
        @Expose
        public String endTime;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("on_time")
        @Expose
        public String onTime;
        @SerializedName("total_worked")
        @Expose
        public String totalWorked;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("shift_date_new")
        @Expose
        public String shiftDateNew;
        @SerializedName("start_time_new")
        @Expose
        public String startTimeNew;
        @SerializedName("end_time_new")
        @Expose
        public String endTimeNew;

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

        public String getShiftNo() {
            return shiftNo;
        }

        public void setShiftNo(String shiftNo) {
            this.shiftNo = shiftNo;
        }

        public String getVacanciesNo() {
            return vacanciesNo;
        }

        public void setVacanciesNo(String vacanciesNo) {
            this.vacanciesNo = vacanciesNo;
        }

        public String getShiftSubNo() {
            return shiftSubNo;
        }

        public void setShiftSubNo(String shiftSubNo) {
            this.shiftSubNo = shiftSubNo;
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

        public String getTimeId() {
            return timeId;
        }

        public void setTimeId(String timeId) {
            this.timeId = timeId;
        }

        public String getShiftDate() {
            return shiftDate;
        }

        public void setShiftDate(String shiftDate) {
            this.shiftDate = shiftDate;
        }

        public String getStartTime() {
            return startTime;
        }

        public void setStartTime(String startTime) {
            this.startTime = startTime;
        }

        public String getEndTime() {
            return endTime;
        }

        public void setEndTime(String endTime) {
            this.endTime = endTime;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getOnTime() {
            return onTime;
        }

        public void setOnTime(String onTime) {
            this.onTime = onTime;
        }

        public String getTotalWorked() {
            return totalWorked;
        }

        public void setTotalWorked(String totalWorked) {
            this.totalWorked = totalWorked;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getShiftDateNew() {
            return shiftDateNew;
        }

        public void setShiftDateNew(String shiftDateNew) {
            this.shiftDateNew = shiftDateNew;
        }

        public String getStartTimeNew() {
            return startTimeNew;
        }

        public void setStartTimeNew(String startTimeNew) {
            this.startTimeNew = startTimeNew;
        }

        public String getEndTimeNew() {
            return endTimeNew;
        }

        public void setEndTimeNew(String endTimeNew) {
            this.endTimeNew = endTimeNew;
        }

    }

}

