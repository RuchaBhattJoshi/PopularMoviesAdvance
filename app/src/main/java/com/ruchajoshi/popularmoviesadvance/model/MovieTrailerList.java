package com.ruchajoshi.popularmoviesadvance.model;

import com.google.gson.annotations.SerializedName;

import java.util.List;

public class MovieTrailerList {


    @SerializedName("results")
    private List<Trailer> moviertrailer;

    public List<Trailer> getMovieTrailer() {
        return moviertrailer;
    }

    public void setMovieTrailer(List<Trailer> moviertrailer) {
        this.moviertrailer = moviertrailer;
    }
}
