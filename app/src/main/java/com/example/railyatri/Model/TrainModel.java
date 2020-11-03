package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainModel {

    @SerializedName("TrainName")
    private String name;

    @SerializedName("ArrivalTime")
    private String dest_arrival_time;

    @SerializedName("TrainNo")
    private String number;

    @SerializedName("DepartureTime")
    private String src_departure_time;

    @SerializedName("TrainType")
    private String TrainType;

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDest_arrival_time() {
        return dest_arrival_time;
    }

    public void setDest_arrival_time(String dest_arrival_time) {
        this.dest_arrival_time = dest_arrival_time;
    }

    public String getNumber() {
        return number;
    }

    public void setNumber(String number) {
        this.number = number;
    }

    public String getSrc_departure_time() {
        return src_departure_time;
    }

    public void setSrc_departure_time(String src_departure_time) {
        this.src_departure_time = src_departure_time;
    }

    public String getTrainType() {
        return TrainType;
    }

    public void setTrainType(String trainType) {
        TrainType = trainType;
    }
}
