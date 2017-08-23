package com.esung.biblotechandroid.Network.GsonConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sungdoo on 2017-08-23.
 */

public class SignUpValidity {

    @SerializedName("userNameValidity")
    @Expose
    private String userNameValidity;
    @SerializedName("phoneNumberValidity")
    @Expose
    private String phoneNumberValidity;
    @SerializedName("emailValidity")
    @Expose
    private String emailValidity;
    @SerializedName("passwordValidity")
    @Expose
    private String passwordValidity;
    @SerializedName("passwordConfirmValidity")
    @Expose
    private String passwordConfirmValidity;

    public String getUserNameValidity() {
        return userNameValidity;
    }

    public void setUserNameValidity(String userNameValidity) {
        this.userNameValidity = userNameValidity;
    }

    public String getPhoneNumberValidity() {
        return phoneNumberValidity;
    }

    public void setPhoneNumberValidity(String phoneNumberValidity) {
        this.phoneNumberValidity = phoneNumberValidity;
    }

    public String getEmailValidity() {
        return emailValidity;
    }

    public void setEmailValidity(String emailValidity) {
        this.emailValidity = emailValidity;
    }

    public String getPasswordValidity() {
        return passwordValidity;
    }

    public void setPasswordValidity(String passwordValidity) {
        this.passwordValidity = passwordValidity;
    }

    public String getPasswordConfirmValidity() {
        return passwordConfirmValidity;
    }

    public void setPasswordConfirmValidity(String passwordConfirmValidity) {
        this.passwordConfirmValidity = passwordConfirmValidity;
    }
}
