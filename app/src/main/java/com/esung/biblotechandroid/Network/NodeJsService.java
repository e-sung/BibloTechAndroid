package com.esung.biblotechandroid.Network;

import com.esung.biblotechandroid.Network.GsonConverters.BookSearchResults;
import com.esung.biblotechandroid.Network.GsonConverters.PostContent;
import com.esung.biblotechandroid.Network.GsonConverters.PostInfo;
import com.esung.biblotechandroid.Network.GsonConverters.RentalInfoResult;
import com.esung.biblotechandroid.Network.GsonConverters.RentedBooks;
import com.esung.biblotechandroid.Network.GsonConverters.SignUpValidity;
import com.esung.biblotechandroid.Network.GsonConverters.UserInfo;

import java.util.List;

import okhttp3.ResponseBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Header;
import retrofit2.http.PATCH;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;

/**
 * Created by sungdoo on 2017-08-18.
 */
public interface NodeJsService {
    @GET("test-connection")
    Call<ResponseBody> testConnection();

    @FormUrlEncoded
    @POST("login")
    Call<UserInfo> submitSignIn(@Field("userEmail") String userEmail, @Field("password") String password);

    @FormUrlEncoded
    @POST("signup")
    Call<SignUpValidity> signUpSubmit(@Field("userName") String userName, @Field("phoneNumber") String phoneNumber,
                                      @Field("email") String email, @Field("password") String password,
                                      @Field("passwordConfirm") String passwordConfirm
    );

    @GET("user-info")
    Call<UserInfo> fetchUserInfo(@Query("email") String email);

    @GET("search-result/{criteria}")
    Call<List<BookSearchResults>> fetchSearchResult(@Path("criteria") String criteria,
                                                    @Query("value") String query);

    @GET("post-info/by-book-title")
    Call<List<PostInfo>> fetchPostInfoByBookTitle(@Query("bookTitle") String title);

    @GET("post-info/by-writer")
    Call<List<PostInfo>> fetchPostInfoByUserName(@Query("writer") String writer);

    @GET("post-content")
    Call<PostContent> fetchPostContent(@Query("postId") int postId);

    @GET("books/rented")
    Call<List<RentedBooks>> fetchRentedBy(@Query("email") String email);

    @GET("books/read")
    Call<ResponseBody> fetchReadBy(@Query("email") String email);


    @FormUrlEncoded
    @POST("new-post-entry")
    Call<ResponseBody> submitPost(@Field("bookTitle") String bookTitle,
                                  @Field("postTitle") String postTitle,
                                  @Field("postContent") String postContent,
                                  @Field("userName") String userName);

    @FormUrlEncoded
    @PATCH("written-post")
    Call<ResponseBody> editPost(@Header("Authorization") String authorizationToken,
                                @Field("postTitle") String postTitle,
                                @Field("postContent") String postContent,
                                @Field("postId") int postId);

    @GET("rental-info")
    Call<RentalInfoResult> fetchRentalInfo(@Query("id") int bookId);

    @FormUrlEncoded
    @POST("rent")
    Call<ResponseBody> sendRentRequest(@Header("Authorization") String authorization,
                                       @Field("id") int bookId,
                                       @Field("userEmail") String userEmail);

    @FormUrlEncoded
    @POST("return")
    Call<ResponseBody> sendReturnRequest(@Field("id") int bookId,
                                         @Field("userName") String userName);

}
