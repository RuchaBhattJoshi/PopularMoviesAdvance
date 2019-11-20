package com.ruchajoshi.popularmoviesadvance;

import android.arch.lifecycle.Observer;
import android.arch.lifecycle.ViewModelProviders;
import android.content.Context;
import android.content.Intent;
import android.net.ConnectivityManager;
import android.net.NetworkInfo;
import android.os.Bundle;
import android.os.Parcelable;
import android.support.annotation.Nullable;
import android.support.constraint.ConstraintLayout;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.DisplayMetrics;
import android.util.Log;
import android.view.Menu;
import android.view.MenuItem;
import android.view.View;
import android.widget.ProgressBar;
import android.widget.TextView;
import android.widget.Toast;

import com.ruchajoshi.popularmoviesadvance.adapter.MovieAdapter;
import com.ruchajoshi.popularmoviesadvance.database.MovieViewModel;
import com.ruchajoshi.popularmoviesadvance.model.Movie;
import com.ruchajoshi.popularmoviesadvance.model.MovieResults;
import com.ruchajoshi.popularmoviesadvance.service.MovieService;

import java.util.ArrayList;
import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class MovieActivity extends AppCompatActivity implements MovieAdapter.MovieAdapterOnClickHandler {

    private static final String SAVED_RECYCLER_VIEW_STATUS_ID ="RECYCLER_VIEW_STATUS_ID" ;
    private static final String SAVED_RECYCLER_VIEW_DATASET_ID ="RECYCLER_VIEW_DATASET_ID" ;


    @BindView(R.id.rv_movies)
    RecyclerView mDisplayMovieRecycleView;
    @BindView(R.id.layout_empty_view)
    ConstraintLayout mEmptyConstraintLayout;
    @BindView(R.id.progress_loading)
    ProgressBar mEmptyProgressbar;
    @BindView(R.id.textView_empty)
    TextView mEmptyTextview;

    GridLayoutManager layoutManager;
    private MovieAdapter mMovieAdapter;
    private Retrofit retrofit;
    private MovieService service;
    private static final String SORT_POPULAR = "popular";
    private static final String SORT_TOP_RATED = "top_rated";
    private static final String SORT_FAVORITE = "favorite";
    private static String currentSort = SORT_POPULAR;

    private MovieViewModel movieViewModel;
    private Observer<List<Movie>> favouriteMoviesObserver;
    private  List<Movie> movies = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_movie);

        ButterKnife.bind(this);
        mMovieAdapter = new MovieAdapter(getApplicationContext(), new ArrayList<Movie>(), MovieActivity.this);
        int mNoOfColumns = calculateNoOfColumns(getApplicationContext());
        movieViewModel = ViewModelProviders.of(this).get(MovieViewModel.class);

        layoutManager = new GridLayoutManager(this, mNoOfColumns);
        mDisplayMovieRecycleView.setLayoutManager(layoutManager);
        mDisplayMovieRecycleView.setHasFixedSize(true);
        mDisplayMovieRecycleView.setAdapter(mMovieAdapter);

        loadMovieData();

        if (savedInstanceState==null){
            loadMovieData(); // No saved data, get data from remote
        }else{
            restorePreviousState(savedInstanceState); // Restore data found in the Bundle
        }

    }

    @Override
    protected void onSaveInstanceState(Bundle outState) {

        Parcelable listState = mDisplayMovieRecycleView.getLayoutManager().onSaveInstanceState();

        // putting recyclerview position
        outState.putParcelable(SAVED_RECYCLER_VIEW_STATUS_ID, listState);

        //putting selected sorting
        //outState.putBoolean(MOST_POPULAR_OPTION_CHECKED, mostPopularOptionChecked);
        //outState.putBoolean(TOP_RATED_OPTION_CHECKED, topRatedOptionChecked);

        // putting recyclerview items
        //outState.putParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID,mMovieList);

        super.onSaveInstanceState(outState);

    }

    private void restorePreviousState(Bundle mSavedInstanceState) {
        // getting recyclerview position
        Parcelable mListState = mSavedInstanceState.getParcelable(SAVED_RECYCLER_VIEW_STATUS_ID);

        // getting recyclerview items
       // mMovieList = mSavedInstanceState.getParcelableArrayList(SAVED_RECYCLER_VIEW_DATASET_ID);

        // Restoring adapter items
       // mMovieAdapter.setMovies(mMovieList);

        // Restoring recycler view position
        mDisplayMovieRecycleView.getLayoutManager().onRestoreInstanceState(mListState);
    }

    public static int calculateNoOfColumns(Context context) {
        DisplayMetrics displayMetrics = context.getResources().getDisplayMetrics();
        float dpWidth = displayMetrics.widthPixels / displayMetrics.density;
        int noOfColumns = (int) (dpWidth / 180);
        return noOfColumns;
    }

    private void loadMovieData() {
        Log.i("currentSort", "-" + currentSort);

        if (isInternetAvailable()) {

            showResult();

            if (currentSort.equals(SORT_POPULAR)) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(MovieService.class);

                final Call<MovieResults> getMostPopluar = service.getMostPopular(BuildConfig.API_KEY);
                getMostPopluar.enqueue(new Callback<MovieResults>() {
                    @Override
                    public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                        if (response.isSuccessful()) {
                            movies.addAll(response.body().getMovies());
                            mMovieAdapter.setMovies(response.body().getMovies());
                        } else {
                            Log.i("response failed", "failed" + response.code());
                        }

                    }

                    @Override
                    public void onFailure(Call<MovieResults> call, Throwable t) {

                    }
                });

            }

            else if (currentSort.equals(SORT_TOP_RATED)) {
                retrofit = new Retrofit.Builder()
                        .baseUrl("https://api.themoviedb.org/3/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                service = retrofit.create(MovieService.class);

                final Call<MovieResults> gettoprated = service.getTopRated(BuildConfig.API_KEY);
                gettoprated.enqueue(new Callback<MovieResults>() {
                    @Override
                    public void onResponse(Call<MovieResults> call, Response<MovieResults> response) {
                        if (response.isSuccessful()) {
                            movies.addAll(response.body().getMovies());
                            mMovieAdapter.setMovies(response.body().getMovies());
                        } else {
                            Log.i("response failed", "failed" + response.code());
                        }

                    }

                    @Override
                    public void onFailure(Call<MovieResults> call, Throwable t) {

                    }
                });

            }

        } else {
            showErrorMessage();
        }
    }

    public boolean isInternetAvailable() {
        ConnectivityManager connectivityManager = (ConnectivityManager) getSystemService(Context.CONNECTIVITY_SERVICE);
        NetworkInfo activeNetwork = connectivityManager.getActiveNetworkInfo();
        return activeNetwork != null
                && activeNetwork.isConnectedOrConnecting();
    }

    private void showResult() {
        mEmptyConstraintLayout.setVisibility(View.INVISIBLE);
        mDisplayMovieRecycleView.setVisibility(View.VISIBLE);
    }

    private void showErrorMessage() {
        mDisplayMovieRecycleView.setVisibility(View.INVISIBLE);
        mEmptyConstraintLayout.setVisibility(View.VISIBLE);
    }


    @Override
    public void onClick(int adapterPosition) {
        Intent intentToDetailActivity = new Intent(this, DetailActivity.class);
        intentToDetailActivity.putExtra("MovieData", mMovieAdapter.getMoviePosition(adapterPosition));
        startActivity(intentToDetailActivity);
    }


    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        getMenuInflater().inflate(R.menu.movies_category_menu, menu);
        return true;
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        int menuItemSelected = item.getItemId();

        if (menuItemSelected == R.id.action_most_popular && !currentSort.equals(SORT_POPULAR)) {
            currentSort = SORT_POPULAR;
            loadMovieData();
            return true;
        }

        if (menuItemSelected == R.id.action_top_rated && !currentSort.equals(SORT_TOP_RATED)) {
            currentSort = SORT_TOP_RATED;
            loadMovieData();
            return true;
        }

        if (menuItemSelected == R.id.action_favorite && !currentSort.equals(SORT_FAVORITE)) {
            currentSort = SORT_FAVORITE;
            favouriteMoviesObserver = new Observer<List<Movie>>() {
                @Override
                public void onChanged(@Nullable List<Movie> movies) {
                    if (movies.size() != 0) {
                        mMovieAdapter.setMovies(movies);
                    } else {
                        mMovieAdapter.setMovies(movies);
                        Toast.makeText(MovieActivity.this, R.string.FavoritesNotFound, Toast.LENGTH_LONG).show();
                    }
                }
            };

            movieViewModel.getAllMovies().observe(this, favouriteMoviesObserver);

            return true;
        }

        return super.onOptionsItemSelected(item);
    }



//    @Override
//    protected void onPause() {
//        super.onPause();
//        index = layoutManager.findFirstCompletelyVisibleItemPosition();
//        View v = mDisplayMovieRecycleView.getChildAt(0);
//        top= (v==null) ? 0 : (v.getTop() - mDisplayMovieRecycleView.getPaddingTop());
//    }
//
//
//    @Override
//    protected void onResume() {
//        super.onResume();
//        if(index!=-1){
//            layoutManager.scrollToPositionWithOffset(index,top);
//        }
//    }



}
