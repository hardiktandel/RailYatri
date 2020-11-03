package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

public class CommonModel {

    @SerializedName("result")
    public String result;

    public String getResult() {
        return result;
    }

    public void setResult(String result) {
        this.result = result;
    }
}
