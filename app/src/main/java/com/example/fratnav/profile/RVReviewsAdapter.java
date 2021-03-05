package com.example.fratnav.profile;

import android.content.Context;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.R;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.Review;

import java.util.ArrayList;

public class RVReviewsAdapter extends RecyclerView.Adapter<RVReviewsAdapter.MyView>{
    private ArrayList<Review> reviews;


    public class MyView extends RecyclerView.ViewHolder{
        TextView reviewUser;
        TextView reviewText;
        String userDisplay;

        public MyView(View view) {
            super(view);

            reviewUser = (TextView) view.findViewById(R.id.postUser);
            reviewText = (TextView) view.findViewById(R.id.postText);
            // Populate the data into the template view using the data object
        }
    }

    public RVReviewsAdapter(Context context, ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.post,parent,false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Review review = getItem(position);
        String userDisplay = "@" + review.username;

        holder.reviewUser.setText(userDisplay);
        holder.reviewText.setText(review.description);

    }


    @Override
    public int getItemCount(){
        return reviews.size();
    }

    public Review getItem(int position) {
        return reviews.get(position);
    }
}
