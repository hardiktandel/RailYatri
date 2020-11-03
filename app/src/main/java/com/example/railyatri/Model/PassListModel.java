package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class PassListModel {

    @SerializedName("data")
    private List<PassModel> data;

    public List<PassModel> getData() {
        return data;
    }

    public void setData(List<PassModel> data) {
        this.data = data;
    }

    public class PassModel {

        @SerializedName("id")
        private String id;

        @SerializedName("name")
        private String name;

        @SerializedName("photo")
        private String photo;

        @SerializedName("age")
        private String age;

        @SerializedName("gender")
        private String gender;

        @SerializedName("from_station")
        private String from;

        @SerializedName("to_station")
        private String to;

        @SerializedName("duration")
        private String duration;

        @SerializedName("pass_type")
        private String passType;

        @SerializedName("payment")
        private String payment;

        @SerializedName("status")
        private String status;

        @SerializedName("token")
        private String token;

        @SerializedName("created")
        private String created;

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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getDuration() {
            return duration;
        }

        public void setDuration(String duration) {
            this.duration = duration;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }

        public String getPassType() {
            return passType;
        }

        public void setPassType(String passType) {
            this.passType = passType;
        }

        public String getAge() {
            return age;
        }

        public void setAge(String age) {
            this.age = age;
        }

        public String getGender() {
            return gender;
        }

        public void setGender(String gender) {
            this.gender = gender;
        }

        public String getPayment() {
            return payment;
        }

        public void setPayment(String payment) {
            this.payment = payment;
        }

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }
    }
}
