package com.ruchajoshi.popularmoviesadvance;

import android.os.Bundle;
import android.support.annotation.Nullable;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;

import com.ruchajoshi.popularmoviesadvance.model.Movie;
import com.squareup.picasso.Picasso;

import butterknife.BindView;
import butterknife.ButterKnife;

public class DetailActivity extends AppCompatActivity {

    /*@BindView(R.id.progress_loading)
     ProgressBar mProgressbar;*/
    @BindView(R.id.img_movie_cover)
     ImageView mMovieCover;
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



    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        setContentView(R.layout.activity_detail);

        ButterKnife.bind(this);

        if(getIntent().getExtras() != null){


            /*String poster = getIntent().getStringExtra("poster");
            String title = getIntent().getStringExtra("title");
            String rate = getIntent().getStringExtra("user_rate");
            String release = getIntent().getStringExtra("release_date");
            String overview = getIntent().getStringExtra("overview");*/


            Movie movieData= getIntent().getParcelableExtra("MovieData");

            mMovieTitle.setText(movieData.getMovieTitle());
            mMovieOverView.setText(movieData.getMovieOverview());
            mMovieUserRating.setText(movieData.getMovieUserRating() + "/10");
            mMovieReleseDate.setText(movieData.getMovieReleaseDate());

            Picasso.get()
                    .load(movieData.getMoviePoster())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mMovieCover);

            Picasso.get()
                    .load(movieData.getMoviePoster())
                    .placeholder(R.mipmap.ic_launcher)
                    .error(R.mipmap.ic_launcher)
                    .into(mMoviePoster);

        }else{
            Log.d("no data","poster");
        }

    }
}
