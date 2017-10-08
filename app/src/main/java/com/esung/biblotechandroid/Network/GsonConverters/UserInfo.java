package com.esung.biblotechandroid.Network.GsonConverters;

import android.os.Parcel;
import android.os.Parcelable;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class UserInfo implements Parcelable {

    public static final Creator<UserInfo> CREATOR = new Creator<UserInfo>() {
        @Override
        public UserInfo createFromParcel(Parcel in) {
            return new UserInfo(in);
        }

        @Override
        public UserInfo[] newArray(int size) {
            return new UserInfo[size];
        }
    };
    @SerializedName("username")
    @Expose
    private String username;
    @SerializedName("email")
    @Expose
    private String email;
    @SerializedName("rentscore")
    @Expose
    private Integer rentscore;
    @SerializedName("rentableBooks")
    @Expose
    private Integer rentableBooks;
    @SerializedName("authToken")
    @Expose
    private String authToken;

    protected UserInfo(Parcel in) {
        username = in.readString();
        email = in.readString();
        rentscore = in.readInt();
        rentableBooks = in.readInt();
        authToken = in.readString();
    }

    @Override
    public void writeToParcel(Parcel dest, int flags) {
        dest.writeString(username);
        dest.writeString(email);
        dest.writeInt(rentscore);
        dest.writeInt(rentableBooks);
        dest.writeString(authToken);
    }

    @Override
    public int describeContents() {
        return 0;
    }

    public String getUsername() {
        return username;
    }


    public String getEmail() {
        return email;
    }

    public Integer getRentscore() {
        return rentscore;
    }

    public Integer getRentableBooks() {
        return rentableBooks;
    }

    public String getAuthToken() {
        return authToken;
    }

}