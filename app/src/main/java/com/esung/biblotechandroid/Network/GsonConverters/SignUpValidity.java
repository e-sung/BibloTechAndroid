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

    public String getPhoneNumberValidity() {
        return phoneNumberValidity;
    }


    public String getEmailValidity() {
        return emailValidity;
    }

    public String getPasswordValidity() {
        return passwordValidity;
    }

    public String getPasswordConfirmValidity() {
        return passwordConfirmValidity;
    }

}
