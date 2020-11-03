package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainSearchModel {


    @SerializedName("Trains")
    private List<TrainModel> trains;

    @SerializedName("Status")
    private String status;


    public List<TrainModel> getTrains() {
        return trains;
    }

    public void setTrains(List<TrainModel> trains) {
        this.trains = trains;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}
