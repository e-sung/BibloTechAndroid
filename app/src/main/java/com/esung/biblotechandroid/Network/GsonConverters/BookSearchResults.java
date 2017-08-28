package com.esung.biblotechandroid.Network.GsonConverters;

/**
 * Created by sungdoo on 2017-08-11.
 */

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

public class BookSearchResults {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("author")
    @Expose
    private String author;
    @SerializedName("is_rented")
    @Expose
    private Integer isRented;
    @SerializedName("renter_email")
    @Expose
    private Object renterEmail;
    @SerializedName("rented_date")
    @Expose
    private Object rentedDate;
    @SerializedName("due_date")
    @Expose
    private Object dueDate;
    @SerializedName("publisher")
    @Expose
    private Object publisher;
    @SerializedName("id")
    @Expose
    private Integer id;

    public String getTitle() {
        return title;
    }

    public String getAuthor() {
        return author;
    }

    public Integer getIsRented() {
        return isRented;
    }

    public Object getRenterEmail() {
        return renterEmail;
    }

    public Object getRentedDate() {
        return rentedDate;
    }

    public Object getDueDate() {
        return dueDate;
    }

    public Object getPublisher() {
        return publisher;
    }

}