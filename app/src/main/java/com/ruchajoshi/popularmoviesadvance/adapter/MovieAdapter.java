package com.ruchajoshi.popularmoviesadvance.adapter;

import android.content.Context;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;


import com.ruchajoshi.popularmoviesadvance.R;
import com.ruchajoshi.popularmoviesadvance.model.Movie;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class MovieAdapter extends RecyclerView.Adapter<MovieAdapter.MovieAdapterViewHolder> {

    private List<Movie> movies;
    private final MovieAdapterOnClickHandler mClickHandler;
    Context context;

    public MovieAdapter(Context context, List<Movie> movie, MovieAdapterOnClickHandler clickHandler) {
        this.context= context;
        this.movies=movie;
        mClickHandler = clickHandler;
    }

    public interface MovieAdapterOnClickHandler {
        void onClick(int adapterPosition);
    }

    @NonNull
    @Override
    public MovieAdapterViewHolder onCreateViewHolder(@NonNull ViewGroup viewGroup, int i) {
        Context context = viewGroup.getContext();
        LayoutInflater inflater = LayoutInflater.from(context);
        boolean shouldAttachToParentImmediately = false;
        View view = inflater.inflate(R.layout.item_movie, viewGroup, shouldAttachToParentImmediately);
        return new MovieAdapterViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull MovieAdapterViewHolder movieAdapterViewHolder, int position) {
        String movieToBind = "https://image.tmdb.org/t/p/w185"+movies.get(position).getPoster_path();
        Log.i("poster","-"+movieToBind);

        Picasso.get()
                .load(movieToBind)
                .placeholder(R.drawable.movie_icon)
                .error(R.mipmap.ic_launcher)
                .into(movieAdapterViewHolder.mMoviePoster);
    }

    @Override
    public int getItemCount() {
        if (movies.isEmpty()) {
            return 0;
        }
        return movies.size();
    }


    public Movie getMoviePosition(int position){
       return movies.get(position);
    }

    public void setMovies(List<Movie> movies){
        this.movies=movies;
        notifyDataSetChanged();
    }

    public class MovieAdapterViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener {

        @BindView(R.id.iv_movie_poster)
        ImageView mMoviePoster;

        public MovieAdapterViewHolder(@NonNull View itemView) {
            super(itemView);
            ButterKnife.bind(this,itemView);
            itemView.setOnClickListener(this);
        }

        @Override
        public void onClick(View view) {
            int adapterPosition = getAdapterPosition();
            mClickHandler.onClick(adapterPosition);
        }
    }


}
