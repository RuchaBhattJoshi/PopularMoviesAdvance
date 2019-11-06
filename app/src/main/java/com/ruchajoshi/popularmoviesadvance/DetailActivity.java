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

import com.ruchajoshi.popularmoviesadvance.adapter.ReviewAdapter;
import com.ruchajoshi.popularmoviesadvance.adapter.TrailerAdapter;
import com.ruchajoshi.popularmoviesadvance.database.MovieViewModel;
import com.ruchajoshi.popularmoviesadvance.model.Movie;
import com.ruchajoshi.popularmoviesadvance.model.MovieResults;
import com.ruchajoshi.popularmoviesadvance.model.Review;
import com.ruchajoshi.popularmoviesadvance.model.Trailer;
import com.ruchajoshi.popularmoviesadvance.service.MovieService;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class DetailActivity extends AppCompatActivity {


    private List<Trailer> trailers;
    private List<Review> reviews;
    private List<Movie> moviesList;
    private Observer<List<Movie>> observer;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;
    private RecyclerView trailersList;
    private RecyclerView reviewsList;

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

//            retrofit = new Retrofit.Builder()
//                    .baseUrl("https://api.themoviedb.org/3/")
//                    .addConverterFactory(GsonConverterFactory.create())
//                    .build();
//
//            service = retrofit.create(MovieService.class);
//
//            final Call<MovieResults> gettoprated = service.getTopRated(BuildConfig.API_KEY);
//            gettoprated.enqueue(new Callback<MovieResults>(){
//                @Override
//                public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
//                    if(response.isSuccessful()){
//                        mMovieAdapter.setMovies(response.body().getMovies());
//                        Log.i("response sucess","sucess"+response.body().getMovies());
//                    }
//                    else{
//                        Log.i("response failed","failed"+response.code());
//                    }
//
//                }
//
//                @Override
//                public void onFailure(Call<MovieResults> call, Throwable t) {
//
//                }
//            });


        }
        else{
            Log.d("no data","poster");
        }

    }
}
