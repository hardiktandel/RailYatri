package com.example.railyatri.Model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class TicketListModel {

    @SerializedName("data")
    private List<TicketModel> data;

    public List<TicketModel> getData() {
        return data;
    }

    public void setData(List<TicketModel> data) {
        this.data = data;
    }

    public class TicketModel {

        @SerializedName("id")
        private String id;

        @SerializedName("from_station")
        private String from;

        @SerializedName("to_station")
        private String to;

        @SerializedName("ticket_type")
        private String type;

        @SerializedName("qty")
        private String qty;

        @SerializedName("total")
        private String total;

        @SerializedName("token")
        private String token;

        @SerializedName("created")
        private String created;

        public String getId() {
            return id;
        }

        public void setId(String id) {
            this.id = id;
        }

        public String getFrom() {
            return from;
        }

        public void setFrom(String from) {
            this.from = from;
        }

        public String getTo() {
            return to;
        }

        public void setTo(String to) {
            this.to = to;
        }

        public String getType() {
            return type;
        }

        public void setType(String type) {
            this.type = type;
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

        public String getToken() {
            return token;
        }

        public void setToken(String token) {
            this.token = token;
        }

        public String getCreated() {
            return created;
        }

        public void setCreated(String created) {
            this.created = created;
        }
    }
}
