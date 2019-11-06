package com.ruchajoshi.popularmoviesadvance.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieReviewList {

    @SerializedName("results")
    private List<Review> moviereview;

    public List<Review> getMovieReview() {
        return moviereview;
    }

    public void setMovieReview(List<Review> moviereview) {
        this.moviereview = moviereview;
    }

}
