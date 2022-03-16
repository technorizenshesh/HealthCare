package com.technorizen.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetPendingRecruitmentShift implements Serializable {

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

    public class PostshiftTime {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("shift_id")
        @Expose
        public String shiftId;
        @SerializedName("user_id")
        @Expose
        public String userId;
        @SerializedName("shift_date")
        @Expose
        public String shiftDate;
        @SerializedName("start_time")
        @Expose
        public String startTime;
        @SerializedName("end_time")
        @Expose
        public String endTime;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("clock_in")
        @Expose
        public String clockIn;
        @SerializedName("new_date")
        @Expose
        public String newDate;
        @SerializedName("new_month")
        @Expose
        public String newMonth;
        @SerializedName("new_date_single")
        @Expose
        public String newDateSingle;
        @SerializedName("total_hours")
        @Expose
        public Integer totalHours;
        @SerializedName("payamount")
        @Expose
        public Integer payamount;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getClockIn() {
            return clockIn;
        }

        public void setClockIn(String clockIn) {
            this.clockIn = clockIn;
        }

        public String getNewDate() {
            return newDate;
        }

        public void setNewDate(String newDate) {
            this.newDate = newDate;
        }

        public String getNewMonth() {
            return newMonth;
        }

        public void setNewMonth(String newMonth) {
            this.newMonth = newMonth;
        }

        public String getNewDateSingle() {
            return newDateSingle;
        }

        public void setNewDateSingle(String newDateSingle) {
            this.newDateSingle = newDateSingle;
        }

        public Integer getTotalHours() {
            return totalHours;
        }

        public void setTotalHours(Integer totalHours) {
            this.totalHours = totalHours;
        }

        public Integer getPayamount() {
            return payamount;
        }

        public void setPayamount(Integer payamount) {
            this.payamount = payamount;
        }

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
        @SerializedName("sid")
        @Expose
        public String sid;
        @SerializedName("shiftsdetail")
        @Expose
        public List<Shiftsdetail> shiftsdetail = null;
        @SerializedName("postshift_time")
        @Expose
        public List<PostshiftTime> postshiftTime = null;

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

        public String getSid() {
            return sid;
        }

        public void setSid(String sid) {
            this.sid = sid;
        }

        public List<Shiftsdetail> getShiftsdetail() {
            return shiftsdetail;
        }

        public void setShiftsdetail(List<Shiftsdetail> shiftsdetail) {
            this.shiftsdetail = shiftsdetail;
        }

        public List<PostshiftTime> getPostshiftTime() {
            return postshiftTime;
        }

        public void setPostshiftTime(List<PostshiftTime> postshiftTime) {
            this.postshiftTime = postshiftTime;
        }

    }

    public class Shiftsdetail {

        @SerializedName("id")
        @Expose
        public String id;
        @SerializedName("shift_no")
        @Expose
        public String shiftNo;
        @SerializedName("user_id")
        @Expose
        public String userId;
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
        @SerializedName("type")
        @Expose
        public String type;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("user_image")
        @Expose
        public String userImage;
        @SerializedName("user_name")
        @Expose
        public String userName;
        @SerializedName("account_type")
        @Expose
        public String accountType;
        @SerializedName("company")
        @Expose
        public String company;
        @SerializedName("worker_image")
        @Expose
        public String workerImage;
        @SerializedName("worker_name")
        @Expose
        public String workerName;
        @SerializedName("worker_designation")
        @Expose
        public String workerDesignation;
        @SerializedName("location_lat")
        @Expose
        public String locationLat;
        @SerializedName("location_lon")
        @Expose
        public String locationLon;
        @SerializedName("distance")
        @Expose
        public String distance;

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

        public String getUserId() {
            return userId;
        }

        public void setUserId(String userId) {
            this.userId = userId;
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

        public String getUserImage() {
            return userImage;
        }

        public void setUserImage(String userImage) {
            this.userImage = userImage;
        }

        public String getUserName() {
            return userName;
        }

        public void setUserName(String userName) {
            this.userName = userName;
        }

        public String getAccountType() {
            return accountType;
        }

        public void setAccountType(String accountType) {
            this.accountType = accountType;
        }

        public String getCompany() {
            return company;
        }

        public void setCompany(String company) {
            this.company = company;
        }

        public String getWorkerImage() {
            return workerImage;
        }

        public void setWorkerImage(String workerImage) {
            this.workerImage = workerImage;
        }

        public String getWorkerName() {
            return workerName;
        }

        public void setWorkerName(String workerName) {
            this.workerName = workerName;
        }

        public String getWorkerDesignation() {
            return workerDesignation;
        }

        public void setWorkerDesignation(String workerDesignation) {
            this.workerDesignation = workerDesignation;
        }

        public String getLocationLat() {
            return locationLat;
        }

        public void setLocationLat(String locationLat) {
            this.locationLat = locationLat;
        }

        public String getLocationLon() {
            return locationLon;
        }

        public void setLocationLon(String locationLon) {
            this.locationLon = locationLon;
        }

        public String getDistance() {
            return distance;
        }

        public void setDistance(String distance) {
            this.distance = distance;
        }

    }

}

