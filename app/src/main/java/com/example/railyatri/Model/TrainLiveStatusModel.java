package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TrainLiveStatusModel {

    @SerializedName("TrainRoute")
    private List<Route> route;

    @SerializedName("CurrentStation")
    private CurrentStation current_station;

    public List<Route> getRoute() {
        return route;
    }

    public void setRoute(List<Route> route) {
        this.route = route;
    }

    public CurrentStation getCurrent_station() {
        return current_station;
    }

    public void setCurrent_station(CurrentStation current_station) {
        this.current_station = current_station;
    }

    public class Route {

        @SerializedName("StationName")
        private String StationName;

        @SerializedName("StationCode")
        private String StationCode;

        @SerializedName("ScheduleArrival")
        private String ScheduleArrival;

        @SerializedName("ActualArrival")
        private String ActualArrival;

        @SerializedName("DelayInArrival")
        private String DelayInArrival;

        public String getStationName() {
            return StationName;
        }

        public void setStationName(String stationName) {
            StationName = stationName;
        }

        public String getStationCode() {
            return StationCode;
        }

        public void setStationCode(String stationCode) {
            StationCode = stationCode;
        }

        public String getScheduleArrival() {
            return ScheduleArrival;
        }

        public void setScheduleArrival(String scheduleArrival) {
            ScheduleArrival = scheduleArrival;
        }

        public String getActualArrival() {
            return ActualArrival;
        }

        public void setActualArrival(String actualArrival) {
            ActualArrival = actualArrival;
        }

        public String getDelayInArrival() {
            return DelayInArrival;
        }

        public void setDelayInArrival(String delayInArrival) {
            DelayInArrival = delayInArrival;
        }
    }

    public class CurrentStation {

        @SerializedName("StationName")
        private String StationName;

        @SerializedName("StationCode")
        private String StationCode;

        @SerializedName("ScheduleArrival")
        private String ScheduleArrival;

        @SerializedName("ActualArrival")
        private String ActualArrival;

        @SerializedName("DelayInArrival")
        private String DelayInArrival;

        @SerializedName("ScheduleDeparture")
        private String ScheduleDeparture;

        @SerializedName("ActualDeparture")
        private String ActualDeparture;

        @SerializedName("DelayInDeparture")
        private String DelayInDeparture;

        public String getStationName() {
            return StationName;
        }

        public void setStationName(String stationName) {
            StationName = stationName;
        }

        public String getStationCode() {
            return StationCode;
        }

        public void setStationCode(String stationCode) {
            StationCode = stationCode;
        }

        public String getScheduleArrival() {
            return ScheduleArrival;
        }

        public void setScheduleArrival(String scheduleArrival) {
            ScheduleArrival = scheduleArrival;
        }

        public String getActualArrival() {
            return ActualArrival;
        }

        public void setActualArrival(String actualArrival) {
            ActualArrival = actualArrival;
        }

        public String getDelayInArrival() {
            return DelayInArrival;
        }

        public void setDelayInArrival(String delayInArrival) {
            DelayInArrival = delayInArrival;
        }

        public String getScheduleDeparture() {
            return ScheduleDeparture;
        }

        public void setScheduleDeparture(String scheduleDeparture) {
            ScheduleDeparture = scheduleDeparture;
        }

        public String getActualDeparture() {
            return ActualDeparture;
        }

        public void setActualDeparture(String actualDeparture) {
            ActualDeparture = actualDeparture;
        }

        public String getDelayInDeparture() {
            return DelayInDeparture;
        }

        public void setDelayInDeparture(String delayInDeparture) {
            DelayInDeparture = delayInDeparture;
        }
    }
}
