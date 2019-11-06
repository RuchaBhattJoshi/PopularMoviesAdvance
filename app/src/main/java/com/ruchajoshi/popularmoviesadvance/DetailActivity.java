package com.ruchajoshi.popularmoviesadvance;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.graphics.Color;
import android.os.AsyncTask;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruchajoshi.popularmoviesadvance.adapter.MovieAdapter;
import com.ruchajoshi.popularmoviesadvance.adapter.ReviewAdapter;
import com.ruchajoshi.popularmoviesadvance.adapter.TrailerAdapter;
import com.ruchajoshi.popularmoviesadvance.database.MovieViewModel;
import com.ruchajoshi.popularmoviesadvance.model.Movie;
import com.ruchajoshi.popularmoviesadvance.model.MovieResults;
import com.ruchajoshi.popularmoviesadvance.model.MovieReviewList;
import com.ruchajoshi.popularmoviesadvance.model.MovieTrailerList;
import com.ruchajoshi.popularmoviesadvance.model.Review;
import com.ruchajoshi.popularmoviesadvance.model.Trailer;
import com.ruchajoshi.popularmoviesadvance.service.MovieService;
import com.squareup.picasso.Picasso;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import okhttp3.Request;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {



    private List<Movie> moviesList;
    private Observer<List<Movie>> observer;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private Retrofit retrofit;
    private  MovieService service;

    private MovieViewModel movieViewModel;

    @BindView(R.id.img_movie_cover)
     ImageView mMovieCover;
    @BindView(R.id.iv_favButton)
    ImageView mMovieFavButton;
    @BindView(R.id.img_movie_poster)
     ImageView mMoviePoster;
    @BindView(R.id.tv_title)
     TextView mMovieTitle;
    @BindView(R.id.tv_release_date)
     TextView mMovieReleseDate;
    @BindView(R.id.tv_user_rating)
     TextView mMovieUserRating;
    @BindView(R.id.tv_synopsis)
     TextView mMovieOverView;
    @BindView(R.id.rv_review)
    RecyclerView mMovieReviewRecycleView;
    @BindView(R.id.rv_trailers)
    RecyclerView mMovieTrailerRecycleView;

    private int movieId;

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);
        ButterKnife.bind(this);

       movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        movieViewModel.getAllMovies().observe(this, new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                moviesList = movies;
            }
        });

        observer = new Observer<List<Movie>>() {
            @Override
            public void onChanged(@Nullable List<Movie> movies) {
                moviesList = movies;
            }
        };

        movieViewModel.getAllMovies().observe(this,observer);

        if(getIntent().getExtras() != null){

            final Movie movieData= getIntent().getParcelableExtra("MovieData");

            movieId = movieData.getId();

            if(movieViewModel.isMovieFavorited(movieId))
            {
                mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_solid_24dp);
            }
            else {
                mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
            }

            mMovieTitle.setText(movieData.getTitle());
            mMovieOverView.setText(movieData.getOverview());
            mMovieUserRating.setText(movieData.getVote_count() + "/10");
            mMovieReleseDate.setText(movieData.getRelease_date());

            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w185"+movieData.getPoster_path())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mMovieCover);

            Picasso.get()
                    .load("https://image.tmdb.org/t/p/w185"+movieData.getPoster_path())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mMoviePoster);

            mMovieFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if(movieViewModel.isMovieFavorited(movieId))
                    {
                        movieViewModel.delete(movieData);
                        Toast.makeText(DetailActivity.this,"Removed from favorites",Toast.LENGTH_LONG)
                                .show();
                        mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);

                    }
                    else {
                        movieViewModel.insert(movieData);
                        mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_solid_24dp);
                        Toast.makeText(DetailActivity.this,"Inserted into favorites",Toast.LENGTH_LONG)
                                .show();
                    }

                }
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(MovieService.class);

            final Call<MovieReviewList> getMovieReview = service.getMovieReview(movieId,BuildConfig.API_KEY);
            getMovieReview.enqueue(new Callback<MovieReviewList>(){
                @Override
                public void onResponse(Call<MovieReviewList> call, Response<MovieReviewList> response) {
                    if(response.isSuccessful()){
                        reviewAdapter = new ReviewAdapter(response.body().getMovieReview());
                        mMovieReviewRecycleView.setAdapter(reviewAdapter);
                        Log.i("review sucess","sucess"+response.body().getMovieReview());
                    }
                    else{
                        Log.i("review failed","failed"+response.code());
                    }

                }

                @Override
                public void onFailure(Call<MovieReviewList> call, Throwable t) {

                }

            });


            final Call<MovieTrailerList> getTrailers= service.getMovieTrailer(movieId,BuildConfig.API_KEY);
            getTrailers.enqueue(new Callback<MovieTrailerList>(){


                @Override
                public void onResponse(Call<MovieTrailerList> call, Response<MovieTrailerList> response) {
                    if(response.isSuccessful()){
                        trailerAdapter = new TrailerAdapter(response.body().getMovieTrailer());
                        mMovieTrailerRecycleView.setAdapter(trailerAdapter);
                        Log.i("video sucess","sucess"+response.body().getMovieTrailer());
                    }
                    else{
                        Log.i("video failed","failed"+response.code());
                    }
                }

                @Override
                public void onFailure(Call<MovieTrailerList> call, Throwable t) {

                }
            } );


        }
        else{
            Log.d("no data","poster");
        }

    }
}
