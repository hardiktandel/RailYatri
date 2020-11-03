package com.example.railyatri.Utils;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.SharedPreferences;
import android.content.SharedPreferences.Editor;

public class SharedPref {

    private SharedPreferences pref;
    private Editor editor;
    private Context _context;
    private int PRIVATE_MODE = 0;

    private final String PREF_NAME = "erail";

    private String FIRST_NAME = "firstName";
    private String LAST_NAME = "lastName";
    private String MOBILE = "mobile";
    private String ADDRESS = "address";
    private String ID = "id";
    private String TYPE = "type";
    private String ENROLL = "enroll";
    private String COLLAGE = "collage";
    private String COLLAGE_ID = "collage_id";
    private String PHOTO = "photo";

    @SuppressLint("CommitPrefEdits")
    public SharedPref(Context context) {
        this._context = context;
        pref = _context.getSharedPreferences(PREF_NAME, PRIVATE_MODE);
        editor = pref.edit();
    }

    public String getFirstName() {
        return pref.getString(FIRST_NAME, "");
    }

    public void setFirstName(String firstName) {
        editor.putString(FIRST_NAME, firstName);
        editor.commit();
    }

    public String getLastName() {
        return pref.getString(LAST_NAME, "");
    }

    public void setLastName(String lastName) {
        editor.putString(LAST_NAME, lastName);
        editor.commit();
    }

    public String getMobile() {
        return pref.getString(MOBILE, "");
    }

    public void setMobile(String mobile) {
        editor.putString(MOBILE, mobile);
        editor.commit();
    }

    public String getAddress() {
        return pref.getString(ADDRESS, "");
    }

    public void setAddress(String address) {
        editor.putString(ADDRESS, address);
        editor.commit();
    }

    public String getID() {
        return pref.getString(ID, "");
    }

    public void setID(String id) {
        editor.putString(ID, id);
        editor.commit();
    }

    public String getTYPE() {
        return pref.getString(TYPE, "");
    }

    public void setTYPE(String type) {
        editor.putString(TYPE, type);
        editor.commit();
    }

    public String getEnroll() {
        return pref.getString(ENROLL, "");
    }

    public void setEnroll(String enroll) {
        editor.putString(ENROLL, enroll);
        editor.commit();
    }

    public String getCollage() {
        return pref.getString(COLLAGE, "");
    }

    public void setCollage(String collage) {
        editor.putString(COLLAGE, collage);
        editor.commit();
    }

    public String getCollageId() {
        return pref.getString(COLLAGE_ID, "");
    }

    public void setCollageId(String collage_id) {
        editor.putString(COLLAGE_ID, collage_id);
        editor.commit();
    }


    public String getPhoto() {
        return pref.getString(PHOTO, "");
    }

    public void setPhoto(String photo) {
        editor.putString(PHOTO, photo);
        editor.commit();
    }

    public void LogOut() {
        editor.clear();
        editor.commit();
    }
}
