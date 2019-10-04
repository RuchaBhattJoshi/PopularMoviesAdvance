package com.ruchajoshi.popularmoviesadvance.service;


import com.ruchajoshi.popularmoviesadvance.model.Movie;
import com.ruchajoshi.popularmoviesadvance.model.MovieResults;

import java.util.List;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/popular")
    Call<MovieResults> getMostPopular(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResults> getTopRated(@Query("api_key") String apiKey);

}
