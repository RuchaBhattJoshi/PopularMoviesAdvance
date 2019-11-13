package com.ruchajoshi.popularmoviesadvance;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.View;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ruchajoshi.popularmoviesadvance.adapter.ReviewAdapter;
import com.ruchajoshi.popularmoviesadvance.adapter.TrailerAdapter;
import com.ruchajoshi.popularmoviesadvance.database.MovieViewModel;
import com.ruchajoshi.popularmoviesadvance.model.Movie;
import com.ruchajoshi.popularmoviesadvance.model.MovieReviewList;
import com.ruchajoshi.popularmoviesadvance.model.MovieTrailerList;
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


    private static final String TAG = "Detail Activity";
    private List<Movie> moviesList;
    private Observer<List<Movie>> observer;

    private TrailerAdapter trailerAdapter;
    private ReviewAdapter reviewAdapter;

    private Retrofit retrofit;
    private MovieService service;

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
    TextView mMovieReleaseDate;
    @BindView(R.id.tv_user_rating)
    TextView mMovieUserRating;
    @BindView(R.id.rbv_user_rating)
    RatingBar mMovieUserRatingBar;
    @BindView(R.id.tv_synopsis)
    TextView mMovieOverView;
    @BindView(R.id.rv_review)
    RecyclerView mMovieReviewRecycleView;
    @BindView(R.id.tv_reviews_not_available)
    TextView mMovieReviewNotAvailable;
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

        movieViewModel.getAllMovies().observe(this, observer);

        if (getIntent().getExtras() != null) {

            final Movie movieData = getIntent().getParcelableExtra("MovieData");

            movieId = movieData.getId();

            Log.d(TAG, "movieId:- " + movieId);


            if (movieViewModel.isMovieFavorited(movieId)) {
                mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_solid_24dp);
            } else {
                mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);
            }

            mMovieTitle.setText(movieData.getTitle());
            mMovieOverView.setText(movieData.getOverview());
            mMovieUserRating.setText(movieData.getVote_average() + "/10");
            mMovieUserRatingBar.setRating((float) movieData.getVote_average());
            mMovieReleaseDate.setText(movieData.getRelease_date());

            Picasso.get()
                    .load(this.getResources().getString(R.string.image_url)+ movieData.getPoster_path())
                    .placeholder(R.drawable.movie_icon)
                    .error(R.mipmap.ic_launcher)
                    .into(mMovieCover);

            Picasso.get()
                    .load(this.getResources().getString(R.string.image_url) + movieData.getPoster_path())
                    .placeholder(R.drawable.movie_icon)
                    .error(R.mipmap.ic_launcher)
                    .into(mMoviePoster);

            mMovieFavButton.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    if (movieViewModel.isMovieFavorited(movieId)) {
                        movieViewModel.delete(movieData);
                        Toast.makeText(DetailActivity.this, R.string.Favorite_Removed, Toast.LENGTH_LONG)
                                .show();
                        mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_border_black_24dp);

                    } else {
                        movieViewModel.insert(movieData);
                        mMovieFavButton.setBackgroundResource(R.drawable.ic_favorite_solid_24dp);
                        Toast.makeText(DetailActivity.this, R.string.Favorite_Removed, Toast.LENGTH_LONG)
                                .show();
                    }

                }
            });

            retrofit = new Retrofit.Builder()
                    .baseUrl("https://api.themoviedb.org/3/")
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();

            service = retrofit.create(MovieService.class);

            final Call<MovieReviewList> getMovieReview = service.getMovieReview(movieId, BuildConfig.API_KEY);

            getMovieReview.enqueue(new Callback<MovieReviewList>() {
                @Override
                public void onResponse(Call<MovieReviewList> call, Response<MovieReviewList> response) {

                    if (response.isSuccessful() && response.body() != null) {
                        if (response.body().getMovieReview() != null && !response.body().getMovieReview().isEmpty()) {
                            reviewAdapter = new ReviewAdapter(response.body().getMovieReview());
                            mMovieReviewRecycleView.setLayoutManager(new LinearLayoutManager(DetailActivity.this));
                            mMovieReviewRecycleView.setAdapter(reviewAdapter);
                        }
                        else{
                            mMovieReviewNotAvailable.setVisibility(View.VISIBLE);
                            mMovieReviewRecycleView.setVisibility(View.GONE);
                        }

                    }
                    else {

                    }

                }

                @Override
                public void onFailure(Call<MovieReviewList> call, Throwable t) {

                }

            });


            final Call<MovieTrailerList> getTrailers = service.getMovieTrailer(movieId, BuildConfig.API_KEY);
            getTrailers.enqueue(new Callback<MovieTrailerList>() {
                @Override
                public void onResponse(Call<MovieTrailerList> call, Response<MovieTrailerList> response) {
                    if (response.isSuccessful()) {
                        trailerAdapter = new TrailerAdapter(DetailActivity.this,response.body().getMovieTrailer());
                        mMovieTrailerRecycleView.setLayoutManager(new LinearLayoutManager(DetailActivity.this,LinearLayoutManager.HORIZONTAL,false));
                        mMovieTrailerRecycleView.setAdapter(trailerAdapter);
                    } else {
                    }
                }

                @Override
                public void onFailure(Call<MovieTrailerList> call, Throwable t) {

                }
            });


        } else {
            Log.d("no data", "poster");
        }

    }
}
