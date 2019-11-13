package com.ruchajoshi.popularmoviesadvance.adapter;

import android.support.annotation.NonNull;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.LinearLayout;
import android.widget.TextView;

import com.ruchajoshi.popularmoviesadvance.R;
import com.ruchajoshi.popularmoviesadvance.model.Review;

import java.util.List;

import butterknife.BindView;
import butterknife.ButterKnife;

public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder> {

    private List<Review> reviews;
    public ReviewAdapter(List<Review> reviews) {
        this.reviews = reviews;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {

        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.item_review,parent,false);
        return new ReviewHolder(view);
    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, final int position) {
        final Review review = reviews.get(position);
        holder.bindReview(reviews.get(position));

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                boolean expanded = review.isExpanded();
                review.setExpanded(!expanded);
                notifyItemChanged(position);
            }
        });
    }

    @Override
    public int getItemCount() {
        return reviews == null ? 0 : reviews.size();
    }

     class ReviewHolder extends RecyclerView.ViewHolder{

        @BindView(R.id.tv_author)
        TextView review_author;
        @BindView(R.id.tv_content)
        TextView review_content;
        @BindView(R.id.review_detail)
        LinearLayout review_detail;

         ReviewHolder(View itemView) {
            super(itemView);
            ButterKnife.bind(this, itemView);
        }

         void bindReview(Review review) {
             boolean expanded = review.isExpanded();
             review_detail.setVisibility(expanded ? View.VISIBLE : View.GONE);
             review_author.setText(review.getAuthor());
             review_content.setText(review.getContent());
         }
    }
}