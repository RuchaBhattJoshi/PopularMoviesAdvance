package com.ruchajoshi.popularmoviesadvance.database;

import android.app.Application;
import android.arch.lifecycle.AndroidViewModel;
import android.arch.lifecycle.LiveData;
import android.support.annotation.NonNull;

import com.ruchajoshi.popularmoviesadvance.model.Movie;

import java.util.List;

public class MovieViewModel extends AndroidViewModel {


    private MovieRepository movieRepository;
    private LiveData<List<Movie>> allMovies;


    public MovieViewModel(@NonNull Application application) {
        super(application);
        movieRepository = new MovieRepository(application);
        allMovies = movieRepository.getAllMovies();
    }


    public void insert(Movie movie)
    {
        movieRepository.insert(movie);
    }

    public void delete(Movie movie)
    {
        movieRepository.delete(movie);
    }

    public Movie getMovie(Integer integer)
    {
        return movieRepository.getMovie(integer);
    }

    public LiveData<List<Movie>> getAllMovies() {
        return allMovies;
    }

    public boolean isMovieFavorited(int id)
    {
        return this.getMovie(id) != null;
    }


}
