package com.esung.biblotechandroid.Network.GsonConverters;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Created by sungdoo on 2017-08-19.
 */

public class PostInfo {
    @SerializedName("id")
    @Expose
    private Integer id;

    @SerializedName("bookTitle")
    @Expose
    private String bookTitle;
    @SerializedName("postTitle")
    @Expose
    private String postTitle;

    @SerializedName("writtenTime")
    @Expose
    private String writtenTime;
    @SerializedName("writer")
    @Expose
    private String writer;

    public Integer getId() {
        return id;
    }

    public String getBookTitle() {
        return bookTitle;
    }

    public String getPostTitle() {
        return postTitle;
    }

    public String getWrittenTime() {
        return writtenTime;
    }

    public String getWriter() {
        return writer;
    }

}
