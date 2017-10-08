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

    @GET("books/titled/{title}")
    Call<List<BookSearchResults>> getBooksTitled(@Path("title") String bookTitle);

    @GET("books/written-by/{author}")
    Call<List<BookSearchResults>> getBooksWrittenBy(@Path("author") String author);

    @GET("books/published-by/{publisher}")
    Call<List<BookSearchResults>> getBooksPublishedBy(@Path("publisher") String publisher);

    @GET("books/rented-by/{user}")
    Call<List<RentedBooks>> getBooksRentedBy(@Path("user") String email);

    @GET("books/read-by/{user}")
    Call<ResponseBody> getBooksReadBy(@Path("user") String email);

    @GET("post-list/about/{book}")
    Call<List<PostInfo>> getPostListAboutBook(@Path("book") String title);

    @GET("post-list/written-by/{writer}")
    Call<List<PostInfo>> getPostListWrittenBy(@Path("writer") String writer);

    @GET("entry/of-book-review/{id}")
    Call<PostContent> getEntryOfBookReviewBy(@Path("id") int postId);

    @FormUrlEncoded
    @POST("entry/of-book-review/")
    Call<ResponseBody> postNewEntry(@Field("bookTitle") String bookTitle,
                                    @Field("postTitle") String postTitle,
                                    @Field("postContent") String postContent,
                                    @Field("userName") String userName);

    @FormUrlEncoded
    @PATCH("entry/of-book-review/")
    Call<ResponseBody> editEntry(@Header("Authorization") String authorizationToken,
                                 @Field("postTitle") String postTitle,
                                 @Field("postContent") String postContent,
                                 @Field("postId") int postId);

    @GET("rental/info-of/book/{id}")
    Call<RentalInfoResult> getRentalInfoOfBook(@Query("id") int bookId);

    @FormUrlEncoded
    @PATCH("rental/status-of/book/{id}/to/{situation}")
    Call<ResponseBody> sendRentOperationRequest(@Header("Authorization") String authorization,
                                       @Path("id") int bookId,
                                       @Path("situation") String targetStatus,
                                       @Field("userEmail") String userEmail);

    @FormUrlEncoded
    @POST("user/sign-in")
    Call<UserInfo> signInWith(@Field("userEmail") String userEmail, @Field("password") String password);

    @FormUrlEncoded
    @POST("user/sign-up")
    Call<SignUpValidity> signUpWith(@Field("userName") String userName, @Field("phoneNumber") String phoneNumber,
                                      @Field("email") String email, @Field("password") String password,
                                      @Field("passwordConfirm") String passwordConfirm
    );

    @GET("user/info-with/{email}")
    Call<UserInfo> getUserInfoWith(@Path("email") String email);
}
