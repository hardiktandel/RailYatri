package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoolieOrderListModel {

    @SerializedName("data")
    public List<CoolieOrderModel> data;

    @SerializedName("status")
    public String status;

    public List<CoolieOrderModel> getData() {
        return data;
    }

    public void setData(List<CoolieOrderModel> data) {
        this.data = data;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public class CoolieOrderModel {

        public boolean isExpanded = false;

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("contact")
        public String contact;

        @SerializedName("station")
        public String station;

        @SerializedName("place")
        public String place;

        @SerializedName("status")
        public String status;

        @SerializedName("first_name")
        public String firstName;

        @SerializedName("last_name")
        public String lastName;

        @SerializedName("mobile")
        public String mobile;

        @SerializedName("photo")
        public String photo;

        @SerializedName("created")
        public String created;

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

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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

        public String getMobile() {
            return mobile;
        }

        public void setMobile(String mobile) {
            this.mobile = mobile;
        }

        public String getPlace() {
            return place;
        }

        public void setPlace(String place) {
            this.place = place;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
