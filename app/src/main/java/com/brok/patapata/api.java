package com.brok.patapata;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;

public interface api {

    String BASE_URL = "https://api.myjson.com/bins/";

    @GET("11mf13")
    Call<List<sugg>> getSugg();
}
