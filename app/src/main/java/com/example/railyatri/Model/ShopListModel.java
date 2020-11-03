package com.example.railyatri.Model;

import java.util.List;

public class ShopListModel {

    List<ShopListDataModel> data;

    public List<ShopListDataModel> getData() {
        return data;
    }

    public void setData(List<ShopListDataModel> data) {
        this.data = data;
    }

    public class ShopListDataModel {

        String id, name, station, address, contact, password, photo;

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

        public String getStation() {
            return station;
        }

        public void setStation(String station) {
            this.station = station;
        }

        public String getAddress() {
            return address;
        }

        public void setAddress(String address) {
            this.address = address;
        }

        public String getContact() {
            return contact;
        }

        public void setContact(String contact) {
            this.contact = contact;
        }

        public String getPassword() {
            return password;
        }

        public void setPassword(String password) {
            this.password = password;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }

}
