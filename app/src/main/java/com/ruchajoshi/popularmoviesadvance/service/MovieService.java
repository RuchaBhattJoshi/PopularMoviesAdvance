package com.ruchajoshi.popularmoviesadvance.service;


import com.ruchajoshi.popularmoviesadvance.model.MovieResults;
import com.ruchajoshi.popularmoviesadvance.model.MovieReviewList;
import com.ruchajoshi.popularmoviesadvance.model.MovieTrailerList;

import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Path;
import retrofit2.http.Query;

public interface MovieService {

    @GET("movie/popular")
    Call<MovieResults> getMostPopular(@Query("api_key") String apiKey);

    @GET("movie/top_rated")
    Call<MovieResults> getTopRated(@Query("api_key") String apiKey);

    @GET("movie/{id}/reviews")
    Call<MovieReviewList> getMovieReview(@Path("id") int movieId, @Query("api_key") String apiKey);

    @GET("movie/{id}/videos")
    Call<MovieTrailerList> getMovieTrailer(@Path("id") int movieId, @Query("api_key") String apiKey);

}
