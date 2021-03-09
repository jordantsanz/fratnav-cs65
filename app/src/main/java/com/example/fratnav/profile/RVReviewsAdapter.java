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

import org.w3c.dom.Text;

import java.util.ArrayList;

public class RVReviewsAdapter extends RecyclerView.Adapter<RVReviewsAdapter.MyView>{
    private ArrayList<Review> reviews;


    public class MyView extends RecyclerView.ViewHolder{
        TextView postUser;
        TextView postHouse;
        TextView safetyView;
        TextView incView;
        TextView baseView;
        TextView overView;
        TextView comments;
        String userDisplay;

        public MyView(View view) {
            super(view);

            postUser = (TextView) view.findViewById(R.id.postUser);
            postHouse = (TextView) view.findViewById(R.id.postHouse);
            safetyView = (TextView) view.findViewById(R.id.safety_review);
            incView = (TextView) view.findViewById(R.id.inclusive_review);
            baseView = (TextView) view.findViewById(R.id.basement_review);
            overView = (TextView) view.findViewById(R.id.overall_review);
            comments = (TextView) view.findViewById(R.id.review_comment);
            // Populate the data into the template view using the data object
        }
    }

    public RVReviewsAdapter(Context context, ArrayList<Review> reviews) {
        this.reviews = reviews;
    }

    @Override
    public MyView onCreateViewHolder(ViewGroup parent, int viewType) {
        View itemView = LayoutInflater.from(parent.getContext()).inflate(R.layout.review_post,parent,false);
        return new MyView(itemView);
    }

    @Override
    public void onBindViewHolder(@NonNull MyView holder, int position) {
        Review review = getItem(position);
        String userDisplay = "@" + review.username;

        holder.postUser.setText(String.valueOf(review.username));
        holder.postHouse.setText(String.valueOf(review.houseId));
        holder.safetyView.setText(String.valueOf(review.safetyRating));
        holder.incView.setText(String.valueOf(review.inclusivityRating));
        holder.baseView.setText(String.valueOf(review.basementRating));
        holder.overView.setText(String.valueOf(review.overallRating));
        holder.comments.setText(String.valueOf(review.description));

    }


    @Override
    public int getItemCount(){
        return reviews.size();
    }

    public Review getItem(int position) {
        return reviews.get(position);
    }
}
