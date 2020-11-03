package com.example.railyatri.RestApi;

import org.apache.http.NameValuePair;

import java.util.ArrayList;
import java.util.List;

public class HttpDAO {

    String json = "";

    public String TrainList(String URL) {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String json = new HttpRequestHelper().getRequest(URL, param);
        return json;
    }

    public String LiveStatus(String URL) {
        List<NameValuePair> param = new ArrayList<NameValuePair>();
        String json = new HttpRequestHelper().getRequest(URL, param);
        return json;
    }
}
