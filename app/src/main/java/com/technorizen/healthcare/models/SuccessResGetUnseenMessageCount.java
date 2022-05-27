package com.technorizen.healthcare.models;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;
import java.util.List;

public class SuccessResGetUnseenMessageCount implements Serializable {

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
        @SerializedName("sender_id")
        @Expose
        public String senderId;
        @SerializedName("receiver_id")
        @Expose
        public String receiverId;
        @SerializedName("chat_message")
        @Expose
        public String chatMessage;
        @SerializedName("status")
        @Expose
        public String status;
        @SerializedName("receiver_status")
        @Expose
        public String receiverStatus;
        @SerializedName("date")
        @Expose
        public String date;
        @SerializedName("date_time")
        @Expose
        public String dateTime;
        @SerializedName("chat_type")
        @Expose
        public String chatType;
        @SerializedName("time_zone")
        @Expose
        public String timeZone;
        @SerializedName("total_unseen_message")
        @Expose
        public String totalUnseenMessage;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
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

        public String getChatMessage() {
            return chatMessage;
        }

        public void setChatMessage(String chatMessage) {
            this.chatMessage = chatMessage;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getReceiverStatus() {
            return receiverStatus;
        }

        public void setReceiverStatus(String receiverStatus) {
            this.receiverStatus = receiverStatus;
        }

        public String getDate() {
            return date;
        }

        public void setDate(String date) {
            this.date = date;
        }

        public String getDateTime() {
            return dateTime;
        }

        public void setDateTime(String dateTime) {
            this.dateTime = dateTime;
        }

        public String getChatType() {
            return chatType;
        }

        public void setChatType(String chatType) {
            this.chatType = chatType;
        }

        public String getTimeZone() {
            return timeZone;
        }

        public void setTimeZone(String timeZone) {
            this.timeZone = timeZone;
        }

        public String getTotalUnseenMessage() {
            return totalUnseenMessage;
        }

        public void setTotalUnseenMessage(String totalUnseenMessage) {
            this.totalUnseenMessage = totalUnseenMessage;
        }

    }

}

