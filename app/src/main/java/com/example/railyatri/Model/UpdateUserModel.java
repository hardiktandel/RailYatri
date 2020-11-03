package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

public class UpdateUserModel {

    @SerializedName("result")
    public String result;

    @SerializedName("photo")
    public String photo;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }

    public String getPhoto() {
        return photo;
    }

    public void setPhoto(String photo) {
        this.photo = photo;
    }
}
