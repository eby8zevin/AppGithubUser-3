package com.ahmadabuhasan.appgithubuser.api;

import com.ahmadabuhasan.appgithubuser.model.Follow;
import com.ahmadabuhasan.appgithubuser.model.Search;
import com.ahmadabuhasan.appgithubuser.model.UserDetail;

import java.util.ArrayList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface ApiService {

    @GET("users/{username}")
    Call<UserDetail> detailUser(
            @Path("username") String username);

    @GET("/search/users")
    Call<Search> searchUser(
            @Query("q") String username);

    @GET("/users/{username}/followers")
    Call<ArrayList<Follow>> followersUser(
            @Path("username") String username);

    @GET("users/{username}/following")
    Call<ArrayList<Follow>> followingUser(
            @Path("username") String username);
}