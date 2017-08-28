package com.esung.biblotechandroid.Network.GsonConverters;

/**
 * Created by sungdoo on 2017-08-16.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class RentalInfoResult {

    @SerializedName("renter_email")
    @Expose
    private Object renterEmail;
    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("is_rented")
    @Expose
    private Integer isRented;
    @SerializedName("due_date")
    @Expose
    private Object dueDate;

    public Object getRenterEmail() {
        return renterEmail;
    }

    public void setRenterEmail(Object renterEmail) {
        this.renterEmail = renterEmail;
    }

    public String getTitle() {
        return title;
    }

    public void setTitle(String title) {
        this.title = title;
    }

    public Integer getIsRented() {
        return isRented;
    }

    public void setIsRented(Integer isRented) {
        this.isRented = isRented;
    }

    public Object getDueDate() {
        return dueDate;
    }

    public void setDueDate(Object dueDate) {
        this.dueDate = dueDate;
    }

}
