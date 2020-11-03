package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class FoodCategoryModel {

    @SerializedName("data")
    List<FoodCategoryDataModel> data;

    public List<FoodCategoryDataModel> getData() {
        return data;
    }

    public void setData(List<FoodCategoryDataModel> data) {
        this.data = data;
    }

    public class FoodCategoryDataModel {

        @SerializedName("name")
        private String name;

        @SerializedName("id")
        private String id;

        @SerializedName("photo")
        private String photo;

        public String getName() {
            return name;
        }

        public void setName(String name) {
            this.name = name;
        }

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getPhoto() {
            return photo;
        }

        public void setPhoto(String photo) {
            this.photo = photo;
        }
    }
}
