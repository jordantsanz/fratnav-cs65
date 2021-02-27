package com.example.fratnav.callbacks;


import com.example.fratnav.models.House;
import com.example.fratnav.models.Review;

import java.util.ArrayList;

public interface getAllReviewsCallback {
    void onCallback(ArrayList<Review> reviews);
}
