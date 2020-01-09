package com.dash.restfood_customer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.dash.restfood_customer.models.Review;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class ReviewAdapter extends FirestoreRecyclerAdapter<Review, ReviewAdapter.ReviewHolder> {


    public ReviewAdapter(@NonNull FirestoreRecyclerOptions<Review> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ReviewHolder reviewHolder, int i, @NonNull Review review) {
        reviewHolder.tv_fName.setText(review.getFoodName().toLowerCase());
        reviewHolder.tv_comments.setText(review.getComments());
        reviewHolder.tv_name.setText("By: "+review.getUserName());
        reviewHolder.tv_rating.setText("Rating: "+review.getRating().toString());
        Log.d("Review","fine");
    }

    @NonNull
    @Override
    public ReviewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.review_item,parent,false);
        return new ReviewHolder(v);
    }

    class ReviewHolder extends RecyclerView.ViewHolder {
        TextView tv_fName,tv_name,tv_rating;
        TextView tv_comments;

        public ReviewHolder(@NonNull View itemView) {
            super(itemView);
            tv_fName =itemView.findViewById(R.id.tv_fName);
            tv_comments=itemView.findViewById(R.id.tv_comments);
            tv_name =itemView.findViewById(R.id.tv_name);
            tv_rating=itemView.findViewById(R.id.tv_rating);
        }
    }
}
