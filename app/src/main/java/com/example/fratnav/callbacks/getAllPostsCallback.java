package com.example.fratnav.callbacks;

import com.example.fratnav.Post;

import java.util.ArrayList;

public interface getAllPostsCallback {
    void onCallback(ArrayList<Post> posts);
}