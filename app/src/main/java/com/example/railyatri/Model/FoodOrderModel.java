package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodOrderModel {

    @SerializedName("data")
    public List<FoodOrder> data;

    public List<FoodOrder> getData() {
        return data;
    }

    public void setData(List<FoodOrder> data) {
        this.data = data;
    }

    public class FoodOrder {

        public boolean isExpanded = false;

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("contact")
        public String contact;

        @SerializedName("food_name")
        public String foodName;

        @SerializedName("size")
        public String size;

        @SerializedName("qty")
        public String qty;

        @SerializedName("total")
        public String total;

        @SerializedName("category_name")
        public String categoryName;

        @SerializedName("status")
        public String status;

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

        public String getFoodName() {
            return foodName;
        }

        public void setFoodName(String foodName) {
            this.foodName = foodName;
        }

        public String getSize() {
            return size;
        }

        public void setSize(String size) {
            this.size = size;
        }

        public String getQty() {
            return qty;
        }

        public void setQty(String qty) {
            this.qty = qty;
        }

        public String getTotal() {
            return total;
        }

        public void setTotal(String total) {
            this.total = total;
        }

        public String getCategoryName() {
            return categoryName;
        }

        public void setCategoryName(String categoryName) {
            this.categoryName = categoryName;
        }

        public String getStatus() {
            return status;
        }

        public void setStatus(String status) {
            this.status = status;
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
