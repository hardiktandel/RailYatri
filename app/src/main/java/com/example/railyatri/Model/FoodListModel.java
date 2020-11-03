package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodListModel {

    @SerializedName("data")
    public List<FoodModel> data;

    public List<FoodModel> getData() {
        return data;
    }

    public void setData(List<FoodModel> data) {
        this.data = data;
    }

    public class FoodModel {

        public boolean isExpanded = false;

        @SerializedName("id")
        public String id;

        @SerializedName("name")
        public String name;

        @SerializedName("description")
        public String description;

        @SerializedName("price")
        public String price;

        @SerializedName("price2")
        public String price2;

        @SerializedName("price3")
        public String price3;

        @SerializedName("photo")
        public String photo;

        @SerializedName("category_id")
        public String categoryId;

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

        public String getDescription() {
            return description;
        }

        public void setDescription(String description) {
            this.description = description;
        }

        public String getPrice() {
            return price;
        }

        public void setPrice(String price) {
            this.price = price;
        }

        public String getPrice2() {
            return price2;
        }

        public void setPrice2(String price2) {
            this.price2 = price2;
        }

        public String getPrice3() {
            return price3;
        }

        public void setPrice3(String price3) {
            this.price3 = price3;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }

        public String getCategoryId() {
            return categoryId;
        }

        public void setCategoryId(String categoryId) {
            this.categoryId = categoryId;
        }
    }
}
