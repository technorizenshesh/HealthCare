package com.shifts.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetHiredWorker implements Serializable {

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
        @SerializedName("total_worked")
        @Expose
        public String totalWorked;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("first_name")
        @Expose
        public String firstName;
        @SerializedName("last_name")
        @Expose
        public String lastName;
        @SerializedName("designation")
        @Expose
        public String designation;
        @SerializedName("image")
        @Expose
        public String image;
        @SerializedName("avg_rating")
        @Expose
        public String avgRating;
        @SerializedName("user_approval")
        @Expose
        public String userApproval;

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

        public String getDesignation() {
            return designation;
        }

        public void setDesignation(String designation) {
            this.designation = designation;
        }

        public String getImage() {
            return image;
        }

        public void setImage(String image) {
            this.image = image;
        }

        public String getAvgRating() {
            return avgRating;
        }

        public void setAvgRating(String avgRating) {
            this.avgRating = avgRating;
        }

        public String getUserApproval() {
            return userApproval;
        }

        public void setUserApproval(String userApproval) {
            this.userApproval = userApproval;
        }

    }
    
}

