package com.chillbro.onealldigital.adapter;

import android.app.Activity;
import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;
import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import java.util.ArrayList;

import com.chillbro.onealldigital.R;
//import com.chillbro.onealldigital.activity.Review;
import com.chillbro.onealldigital.fragment.ProductDetailFragment;
import com.chillbro.onealldigital.helper.Constant;
import com.chillbro.onealldigital.model.Reviews;


public class ReviewAdapter extends RecyclerView.Adapter<ReviewAdapter.ReviewHolder>{

    Context context;
    ArrayList<Reviews> list1;
    Activity activity;
    int lyt_reviews;
    String sub_review;
    ProductDetailFragment productDetailFragment;


    public ReviewAdapter(Context context, Activity activity, ArrayList<Reviews> list, int lyt_reviews, String sub_review, ProductDetailFragment productDetailFragment) {
        this.context=context;
        this.list1=list;
        this.activity=activity;
        this.lyt_reviews=lyt_reviews;
        this.sub_review=sub_review;
        this.productDetailFragment=productDetailFragment;


    }

    @Override
    public void onBindViewHolder(@NonNull ReviewHolder holder, int position) {
        Reviews reviews = list1.get(position);
        holder.tvReviewerName.setText(reviews.getname());
        holder.tvReview.setText(reviews.getRe());
        holder.reviewRating.setRating(reviews.getRate());
    }

    @Override
    public int getItemCount() {
        int product;
        if (list1.size() > 4) {
            product = 4;
        } else {
            product = list1.size();
        }
        return product;
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.lyt_reviews, parent, false);
        return new ReviewHolder(view);
    }

    class ReviewHolder extends RecyclerView.ViewHolder {

        private final TextView tvReviewerName;
        private final TextView tvReview;
        private final RatingBar reviewRating;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);

            tvReviewerName = itemView.findViewById(R.id.tv_reviewer_name);
            tvReview = itemView.findViewById(R.id.tv_review);
            reviewRating = itemView.findViewById(R.id.review_rating);
        }
    }
}
