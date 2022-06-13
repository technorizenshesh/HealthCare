package com.shifts.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetInvoices implements Serializable {

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
        @SerializedName("hours_purchased")
        @Expose
        public String hoursPurchased;
        @SerializedName("hourly_rate")
        @Expose
        public String hourlyRate;
        @SerializedName("unpaid_break")
        @Expose
        public String unpaidBreak;
        @SerializedName("transit_allowance")
        @Expose
        public String transitAllowance;
        @SerializedName("admin_fee_user")
        @Expose
        public String adminFeeUser;
        @SerializedName("hsttax_user")
        @Expose
        public String hsttaxUser;
        @SerializedName("total_amount")
        @Expose
        public String totalAmount;
        @SerializedName("user_no_show_cancel_charge")
        @Expose
        public String userNoShowCancelCharge;
        @SerializedName("worker_no_show_cancel_charge")
        @Expose
        public String workerNoShowCancelCharge;
        @SerializedName("worker_paid_amout")
        @Expose
        public String workerPaidAmout;
        @SerializedName("user_paid_amout")
        @Expose
        public String userPaidAmout;
        @SerializedName("payement_status")
        @Expose
        public String payementStatus;
        @SerializedName("pay_date")
        @Expose
        public String payDate;
        @SerializedName("transection_date")
        @Expose
        public String transectionDate;
        @SerializedName("schedule_pay_date")
        @Expose
        public String schedulePayDate;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("year")
        @Expose
        public String year;
        @SerializedName("invoicedate")
        @Expose
        public String invoicedate;
        @SerializedName("worker_total_earning")
        @Expose
        public String workerTotalEarning;
        @SerializedName("user_total_amount")
        @Expose
        public String userTotalAmount;
        @SerializedName("shift_date")
        @Expose
        public String shiftDate;
        @SerializedName("start_time")
        @Expose
        public String startTime;
        @SerializedName("end_time")
        @Expose
        public String endTime;
        @SerializedName("shiftsdetail")
        @Expose
        public List<Shiftsdetail> shiftsdetail = null;

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

        public String getHoursPurchased() {
            return hoursPurchased;
        }

        public void setHoursPurchased(String hoursPurchased) {
            this.hoursPurchased = hoursPurchased;
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

        public String getTransitAllowance() {
            return transitAllowance;
        }

        public void setTransitAllowance(String transitAllowance) {
            this.transitAllowance = transitAllowance;
        }

        public String getAdminFeeUser() {
            return adminFeeUser;
        }

        public void setAdminFeeUser(String adminFeeUser) {
            this.adminFeeUser = adminFeeUser;
        }

        public String getHsttaxUser() {
            return hsttaxUser;
        }

        public void setHsttaxUser(String hsttaxUser) {
            this.hsttaxUser = hsttaxUser;
        }

        public String getTotalAmount() {
            return totalAmount;
        }

        public void setTotalAmount(String totalAmount) {
            this.totalAmount = totalAmount;
        }

        public String getUserNoShowCancelCharge() {
            return userNoShowCancelCharge;
        }

        public void setUserNoShowCancelCharge(String userNoShowCancelCharge) {
            this.userNoShowCancelCharge = userNoShowCancelCharge;
        }

        public String getWorkerNoShowCancelCharge() {
            return workerNoShowCancelCharge;
        }

        public void setWorkerNoShowCancelCharge(String workerNoShowCancelCharge) {
            this.workerNoShowCancelCharge = workerNoShowCancelCharge;
        }

        public String getWorkerPaidAmout() {
            return workerPaidAmout;
        }

        public void setWorkerPaidAmout(String workerPaidAmout) {
            this.workerPaidAmout = workerPaidAmout;
        }

        public String getUserPaidAmout() {
            return userPaidAmout;
        }

        public void setUserPaidAmout(String userPaidAmout) {
            this.userPaidAmout = userPaidAmout;
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

        public String getSchedulePayDate() {
            return schedulePayDate;
        }

        public void setSchedulePayDate(String schedulePayDate) {
            this.schedulePayDate = schedulePayDate;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getYear() {
            return year;
        }

        public void setYear(String year) {
            this.year = year;
        }

        public String getInvoicedate() {
            return invoicedate;
        }

        public void setInvoicedate(String invoicedate) {
            this.invoicedate = invoicedate;
        }

        public String getWorkerTotalEarning() {
            return workerTotalEarning;
        }

        public void setWorkerTotalEarning(String workerTotalEarning) {
            this.workerTotalEarning = workerTotalEarning;
        }

        public String getUserTotalAmount() {
            return userTotalAmount;
        }

        public void setUserTotalAmount(String userTotalAmount) {
            this.userTotalAmount = userTotalAmount;
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

        public List<Shiftsdetail> getShiftsdetail() {
            return shiftsdetail;
        }

        public void setShiftsdetail(List<Shiftsdetail> shiftsdetail) {
            this.shiftsdetail = shiftsdetail;
        }

    }
    
    
    public class Shiftsdetail {

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

    }

}

