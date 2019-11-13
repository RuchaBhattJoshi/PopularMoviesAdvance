package com.ruchajoshi.popularmoviesadvance.adapter;

import android.content.ActivityNotFoundException;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.ruchajoshi.popularmoviesadvance.R;
import com.ruchajoshi.popularmoviesadvance.model.Trailer;
import com.squareup.picasso.Picasso;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class TrailerAdapter extends RecyclerView.Adapter<TrailerAdapter.ViewHolder> {

    private List<Trailer> trailers;
    private Context context;


    public TrailerAdapter(Context context, List<Trailer> trailers) {
        this.context = context;
        this.trailers = trailers;
    }

    @NonNull
    @Override
    public ViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_video,parent,false);
        return new ViewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull final ViewHolder holder, final int position) {
        holder.mMovieTrailerName.setText(trailers.get(position).getName());
        if (trailers.get(position).getSite().equalsIgnoreCase("youtube")) {
            Uri uri = Uri.parse(context.getResources().getString(R.string.YOUTUBE_BASE_IMAGE_URL) + trailers.get(position).getKey() + context.getResources().getString(R.string.YOUTUBE_IMAGE_EXTENSION));
            Picasso.Builder builder = new Picasso.Builder(context);
            builder.build()
                    .load(uri)
                    .placeholder((R.drawable.gradient_background))
                    .error(R.drawable.ic_launcher_background)
                    .into(holder.mMovieTrailerPoster);
        }
    }

    @Override
    public int getItemCount() {
        return trailers.size();
    }

    public class ViewHolder extends RecyclerView.ViewHolder implements View.OnClickListener, View.OnLongClickListener {

        @BindView(R.id.play_button)
        ImageView mMoviePlayButton;
        @BindView(R.id.thumbnail)
        ImageView mMovieTrailerPoster;
        @BindView(R.id.tv_trailer_name)
        TextView mMovieTrailerName;


        public ViewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);

            mMoviePlayButton.setOnClickListener(this);
            itemView.setOnClickListener(this);
            itemView.setOnLongClickListener(this);

        }

        @Override
        public void onClick(View view) {
            Trailer video = trailers.get(getAdapterPosition());
            if (video != null) {
                Intent intent = new Intent(Intent.ACTION_VIEW);
                intent.setData(Uri.parse(context.getResources().getString(R.string.YOUTUBE_BASE_VIDEO_URL) + video.getKey()));
                if (intent.resolveActivity(context.getPackageManager()) != null) {
                    context.startActivity(intent);
                } else {
                    Toast.makeText(context, "Error playing the video", Toast.LENGTH_SHORT).show();
                }
            }
        }

        @Override
        public boolean onLongClick(View view) {
            Trailer video = trailers.get(getAdapterPosition());
            if (video != null) {
                Intent sharingIntent = new Intent(android.content.Intent.ACTION_SEND);
                Uri uri = Uri.parse(context.getResources().getString(R.string.YOUTUBE_BASE_VIDEO_URL) + video.getKey());
                sharingIntent.setType("text/plain");
                sharingIntent.putExtra(Intent.EXTRA_STREAM, uri);
                context.startActivity(Intent.createChooser(sharingIntent, "Share Video url using"));
            }
            return true;
        }
    }
}
