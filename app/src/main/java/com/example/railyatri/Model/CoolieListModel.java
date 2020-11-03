package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class CoolieListModel {

    @SerializedName("data")
    public List<CoolieModel> data;

    public List<CoolieModel> getData() {
        return data;
    }

    public void setData(List<CoolieModel> data) {
        this.data = data;
    }

    public class CoolieModel {

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("contact")
        public String contact;

        @SerializedName("station")
        public String station;

        @SerializedName("photo")
        private String photo;

        @SerializedName("status")
        public String status;


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

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
        }
    }
}
