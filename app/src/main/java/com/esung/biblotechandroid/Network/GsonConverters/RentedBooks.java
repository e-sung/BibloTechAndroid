package com.esung.biblotechandroid.Network.GsonConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sungdoo on 2017-08-18.
 */

public class RentedBooks {

    @SerializedName("title")
    @Expose
    private String title;
    @SerializedName("due_date")
    @Expose
    private String dueDate;

    public String getTitle() {
        return title;
    }


    public String getDueDate() {
        return dueDate;
    }

}
