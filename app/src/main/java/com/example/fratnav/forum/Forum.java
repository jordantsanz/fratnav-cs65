package com.example.fratnav.forum;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.MotionEvent;
import android.view.View;
import android.view.ViewGroup;
import android.view.inputmethod.InputMethodManager;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageButton;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.PopupWindow;
import android.widget.TextView;
import android.widget.Toast;
import android.widget.ToggleButton;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.coordinatorlayout.widget.CoordinatorLayout;
import androidx.recyclerview.widget.DividerItemDecoration;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import com.example.fratnav.callbacks.likePostCallback;
import com.example.fratnav.houses.HousePage;
import com.example.fratnav.onboarding.Authentication;
import com.example.fratnav.houses.HousesSearch;
import com.example.fratnav.MainActivity;
import com.example.fratnav.profile.Profile;
import com.example.fratnav.R;
import com.example.fratnav.callbacks.getAllPostsCallback;
import com.example.fratnav.callbacks.getUserByIdCallback;
import com.example.fratnav.databaseHelpers.PostDatabaseHelper;
import com.example.fratnav.databaseHelpers.UserDatabaseHelper;
import com.example.fratnav.models.Post;
import com.example.fratnav.models.User;
import com.example.fratnav.profile.RVPostsAdapter;
import com.example.fratnav.profile.RVReviewsAdapter;
import com.example.fratnav.tools.PostsAdapter;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.ListIterator;
import java.util.UUID;

public class Forum extends AppCompatActivity implements View.OnClickListener{
    BottomNavigationView bottomBar;
    private static FirebaseUser currentUser;
    private static User currentUserInfo;
    private static final String TAG = "RealtimeDB";
    private FirebaseDatabase database;
    private DatabaseReference dbRefPosts;
    private DatabaseReference dbRefUsers;
    private DatabaseReference dbRefHouses;
    private DatabaseReference dbRefReviews;
    private EditText userText;
    private TextView helloUser;
    DatabaseReference.CompletionListener completionListener;
    public static ArrayList<Post> arrayOfPosts;
    public String randomKey;

    Dialog filterDialog;

    public static final String POSTS_KEY = "postskey";

    public ToggleButton axa;
    public ToggleButton aka;
    public ToggleButton aphi;
    public ToggleButton alphas;
    public ToggleButton alphatheta;
    public ToggleButton axid;
    public ToggleButton bg;
    public ToggleButton beta;
    public ToggleButton chidelt;
    public ToggleButton chigam;
    public ToggleButton deltas;
    public ToggleButton ekt;
    public ToggleButton gdx;
    public ToggleButton hereot;
    public ToggleButton kappa;
    public ToggleButton kd;
    public ToggleButton kde;
    public ToggleButton trikap;
    public ToggleButton phidelt;
    public ToggleButton phitau;
    public ToggleButton psiu;
    public ToggleButton sigdelt;
    public ToggleButton signu;
    public ToggleButton tabard;
    public ToggleButton tdx;
    public ToggleButton zete;

    boolean isHouse;
    public ListView list;
    public HashMap<String, String> tags;

    public static final String POST_ID_KEY = "postid_key";
    public static final String USER_ID_KEY = "userid_key";
    ImageView filter;
    PopupWindow popupWindow;
    LayoutInflater layoutInflater;
    CoordinatorLayout coordinatorLayout;
    public static RVPostsAdapter postsAdapter;
    RecyclerView recyclerViewforum;


    /// booleans for houses
    boolean axaOn;
    boolean akaOn;
    boolean aphiOn;
    boolean alphasOn;
    boolean alphathetaOn;
    boolean axidOn;
    boolean bgOn;
    boolean betaOn;
    boolean chideltOn;
    boolean chigamOn;
    boolean deltasOn;
    boolean ektOn;
    boolean gdxOn;
    boolean hereotOn;
    boolean kappaOn;
    boolean kdOn;
    boolean kdeOn;
    boolean trikapOn;
    boolean phideltOn;
    boolean phitauOn;
    boolean psiuOn;
    boolean sigdeltOn;
    boolean signuOn;
    boolean tabardOn;
    boolean tdxOn;
    boolean zeteOn;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //connects XML
        setContentView(R.layout.forum);

        //instantiates the tags hashmap

        Log.d("create", "onCreate: jsjsjsjs");
        setContentView(R.layout.forum);
        recyclerViewforum = (RecyclerView) findViewById(R.id.rv_forumposts);
        recyclerViewforum.removeAllViews();

        if (arrayOfPosts != null) {
            Log.d("create", "onCrdsfsdfseate: ");
            for (int i = arrayOfPosts.size() - 1; i > -1; i -= 1) {
                arrayOfPosts.remove(i);
            }
        }


        tags = new HashMap<>();

        //gets the current user
        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }

        //sets the current user information and checks if the account is a house
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                currentUserInfo = user;
                isHouse = user.house;
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.forumCoordinatorLayout);
        filter = (ImageView) findViewById(R.id.filter);
        //filter pop up wiht options to filter by tags
        filter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                layoutInflater = (LayoutInflater) getApplicationContext().getSystemService(LAYOUT_INFLATER_SERVICE);
                ViewGroup container = (ViewGroup) layoutInflater.inflate(R.layout.tag_filter_popup, null);
                popupWindow = new PopupWindow(container, 520, 240, true);
                Log.d("popupWindo", popupWindow.toString());
                popupWindow.showAtLocation(coordinatorLayout, Gravity.NO_GRAVITY, 250, 240);
                container.setOnTouchListener(new View.OnTouchListener() {
                    @Override
                    public boolean onTouch(View v, MotionEvent event) {
                        popupWindow.dismiss();
                        return true;
                    }
                });

                setOnClickListeners(container);
                setCurrentColors();
            }
        });

        //sets the nav bar
        bottomBar = (BottomNavigationView) findViewById(R.id.bottomBar);
        bottomBar.setSelectedItemId(R.id.forum);
        bottomBar.setOnNavigationItemSelectedListener(new BottomNavigationView.OnNavigationItemSelectedListener() {
            @Override
            public boolean onNavigationItemSelected(@NonNull MenuItem item) {
                Log.d("sad", "made it item clicked " + item.getTitle());
                if (item.getItemId()==R.id.houses) {
                    Log.d("swtich", "houses");
                    startActivity(new Intent(Forum.this, HousesSearch.class));
                    finish();
                    return true;
                } else if (item.getItemId()==R.id.profile) {
                    Log.d("swtich", "profile");
                    Intent intent = new Intent(Forum.this, Profile.class);
                    intent.putExtra(MainActivity.USER_HOUSE_BOOL, isHouse);
                    startActivity(intent);
                    return true;
                } else if (item.getItemId()==R.id.home) {
                    Log.d("swtich", "home");
                    startActivity(new Intent(Forum.this, MainActivity.class));
                    finish();
                    return true;
                }
                return false;
            }
        });


        // checks to see if data has been entered
        completionListener =
                new DatabaseReference.CompletionListener() {
                    @Override
                    public void onComplete(DatabaseError databaseError,
                                           DatabaseReference databaseReference) {

                        if (databaseError != null) {
                            notifyUser(databaseError.getMessage());
                        }
                    }
                };

        // gets all the post and adds the post to an array liust to be updated in the adapter
        PostDatabaseHelper.getAllPosts(new getAllPostsCallback() {
            @Override
            public void onCallback(ArrayList<Post> posts) {
                for (int i = posts.size() - 1; i > -1; i--){
                    Post post = posts.get(i);
                    arrayOfPosts.add(post);
                }
                postsAdapter.notifyDataSetChanged();

            }
        });

        // Construct the data source
        if (arrayOfPosts == null) {
            arrayOfPosts = new ArrayList<Post>();
        }
        //connecting the recycler view to the adapter and the xml
        recyclerViewforum = (RecyclerView) findViewById(R.id.rv_forumposts);


        if (savedInstanceState != null){
            ArrayList<Post> posts = savedInstanceState.getParcelableArrayList(POSTS_KEY);
            for (int i = arrayOfPosts.size() - 1; i > -1; i -= 1){
                arrayOfPosts.remove(i);
            }

            for (int i = 0; i < posts.size(); i+= 1){
                arrayOfPosts.add(posts.get(i));
            }
            if (postsAdapter != null) {
                postsAdapter.notifyDataSetChanged();
            }
        }



        LinearLayoutManager verticalLayoutManager =
                new LinearLayoutManager(Forum.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewforum.setLayoutManager(verticalLayoutManager);

        postsAdapter = new RVPostsAdapter(getApplicationContext(), arrayOfPosts);
        recyclerViewforum.setAdapter(postsAdapter);

        postsAdapter.notifyDataSetChanged();

    }

    @Override
    public void onSaveInstanceState(@NonNull Bundle outState) {
        super.onSaveInstanceState(outState);
        outState.putParcelableArrayList(POSTS_KEY, arrayOfPosts);

    }

    @Override
    //on click method of the post to open the PostActivity class
    public void onClick(View v) {
        Log.d("hereee", "lol");
        int position = recyclerViewforum.getChildAdapterPosition(v);
        if (position!=-1) {
            Log.d("position", String.valueOf(position));
            long viewId = v.getId();
            Log.d("click", String.valueOf(viewId));
            Post post = postsAdapter.getItem(position);


            Intent intent = new Intent(Forum.this, PostActivity.class);
            intent.putExtra(POST_ID_KEY, post.id);
            intent.putExtra(USER_ID_KEY, currentUserInfo.username);


            startActivity(intent);
        }
    }

    // if error
    private void notifyUser(String message) {
        Toast.makeText(Forum.this, message,
                Toast.LENGTH_SHORT).show();
    }



    // saves post currently
    public void savePosts(View view) {
        Post post = new Post(currentUserInfo.username, currentUser.getUid(), userText.getText().toString(),
                new ArrayList<>(), new HashMap<>(), 0);

        DatabaseReference pushRef = dbRefPosts.push();

        post.id = pushRef.getKey();

        pushRef.setValue(post);

        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);

        arrayOfPosts.add(0, post);

        postsAdapter.notifyDataSetChanged();

        UserDatabaseHelper.addPostToUser(post, currentUser, currentUserInfo);

        Toast.makeText(this, "Post successfully created.", Toast.LENGTH_SHORT).show();
    }

    public void hideKeyboard(View view) {
        InputMethodManager inputMethodManager =(InputMethodManager)getSystemService(Activity.INPUT_METHOD_SERVICE);
        inputMethodManager.hideSoftInputFromWindow(view.getWindowToken(), 0);
    }

    //opens the activity to create a post
    public void openPostCreate(View view) {
        Intent intent = new Intent (Forum.this, CreatePost.class);
        startActivity(intent);
    }

    //on refresh
    public static void refresh(){
        PostDatabaseHelper.getAllPosts(new getAllPostsCallback() {
            @Override
            public void onCallback(ArrayList<Post> posts) {
                arrayOfPosts = posts;
                postsAdapter.notifyDataSetChanged();
            }
        });

    }

    // for likes
    public void onHeartClick(View v){
        View parentRow = (View) v.getParent().getParent();
        Log.d("parentRow", parentRow.toString());
        RecyclerView rv = (RecyclerView) parentRow.getParent();
        Log.d("recycle", rv.toString());
        int position = rv.getChildLayoutPosition(parentRow);
        Post post = postsAdapter.getItem(position);
        assert post != null;
        Log.d("heartClick", post.id);

        if (randomKey == null){
            randomKey = UUID.randomUUID().toString();
        }

        boolean userDidLike = false;

        ImageView heart = findViewById(R.id.heart);
        heart.getTag();


        if (post.usersLiked != null){
            Log.d("userDidLike", post.usersLiked.toString());
            for (String userId : post.usersLiked.values()){
                Log.d("userDidLike", userId + ", " + currentUserInfo.userID);
                if (userId.equals(currentUserInfo.userID)){
                    userDidLike = true;

                    break;
                }
            }
        }

                if (userDidLike){
                    PostDatabaseHelper.removeLikefromPost(currentUserInfo.userID, post.id, new likePostCallback() {
                        @Override
                        public void onCallback(int likes) {
                            Forum.refresh();
                            Log.d("like", "removeLike");
                            post.usersLiked.remove(currentUserInfo.userID, currentUserInfo.userID);
                            Log.d("postprof", post.usersLiked.toString());
                            post.likes -= 1;

                        }
                    });


                }
                else{
                    PostDatabaseHelper.addLiketoPost(currentUserInfo.userID, post.id, new likePostCallback() {
                        @Override
                        public void onCallback(int likes) {
                            Log.d("like", "addLike");
                            Forum.refresh();
                            if (post.usersLiked == null){
                                post.usersLiked = new HashMap<>();
                            }
                            post.usersLiked.put(currentUserInfo.userID, currentUserInfo.userID);
                            post.likes += 1;
                        }
                    });

                }
            }


    // clicks for username pops up the user profile page
    public void onUsernameClick(View v){
        View parentRow = (View) v.getParent();
        Log.d("listview", parentRow.getParent().toString());
        RecyclerView rv = (RecyclerView) parentRow.getParent();
        Log.d("listview", rv.toString());
        int position = rv.getChildLayoutPosition(parentRow);
        Log.d("listview", String.valueOf(position));
        if (position == -1){
            return;
        }

        Post post = postsAdapter.getItem(position);
        assert post != null;
        Log.d("listview", post.username);
        Log.d("heartClick", post.id);
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.putExtra(USER_ID_KEY, post.userID);
        startActivity(intent);

    }

    //creates listeners for each tag in the filter
    public void setOnClickListeners(ViewGroup container){
        axa = container.findViewById(R.id.axaTagSearch);
        axa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Alpha Chi")){
                    tags.remove("Alpha Chi");
                    axaOn = false;
                }
                else{
                    tags.put("Alpha Chi", "");
                    axaOn = true;
                }

                axa.setChecked(axaOn);
                filter();
            }
        });

         aka = container.findViewById(R.id.akaTagSearch);
        aka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("AKA")){
                    tags.remove("AKA");
                    akaOn = false;
                }
                else{
                    tags.put("AKA", "");
                    akaOn = true;
                }

                aka.setChecked(akaOn);
                filter();
            }
        });

         aphi = container.findViewById(R.id.aPhiTagSearch);
        aphi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("APhi")){
                    tags.remove("APhi");
                    aphiOn = false;
                }
                else{
                    tags.put("APhi", "");
                    aphiOn = true;
                }

                aphi.setChecked(aphiOn);
                filter();
            }
        });


         alphas = container.findViewById(R.id.alphasTagSearch);
        alphas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("ΑΦΑ")){
                    tags.remove("ΑΦΑ");
                    alphasOn = false;
                }
                else{
                    tags.put("ΑΦΑ", "");
                    alphasOn = true;
                }

                alphas.setChecked(alphasOn);
                filter();
            }
        });

         alphatheta = container.findViewById(R.id.alphaThetaTagSearch);
        alphatheta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Alpha Theta")){
                    tags.remove("Alpha Theta");
                    alphathetaOn = false;
                }
                else{
                    tags.put("Alpha Theta", "");
                    alphathetaOn = true;
                }

                alphatheta.setChecked(alphathetaOn);
                filter();
            }
        });

         axid = container.findViewById(R.id.aXiDTagSearch);
        axid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("AXiD")){
                    tags.remove("AXiD");
                    axidOn = false;
                }
                else{
                    tags.put("AXiD", "");
                    axidOn = true;
                }

                axid.setChecked(axidOn);
                filter();
            }
        });

         bg = container.findViewById(R.id.bGTagSearch);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("BG")){
                    tags.remove("BG");
                    bgOn = false;
                }
                else{
                    tags.put("BG", "");
                    bgOn = true;
                }

                bg.setChecked(bgOn);
                filter();
            }
        });

         beta = container.findViewById(R.id.betaTagSearch);
        beta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Beta")){
                    tags.remove("Beta");
                    betaOn = false;
                }
                else{
                    tags.put("Beta", "");
                    betaOn = true;
                }

                beta.setChecked(betaOn);
                filter();
            }
        });

         chidelt = container.findViewById(R.id.chiDeltTagSearch);
        chidelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Chi Delt")){
                    tags.remove("Chi Delt");
                    chideltOn = false;
                }
                else{
                    tags.put("Chi Delt", "");
                    chideltOn = true;
                }

                chidelt.setChecked(chideltOn);
                filter();
            }
        });

         chigam = container.findViewById(R.id.chiGamTagSearch);
        chigam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Chi Gam")){
                    tags.remove("Chi Gam");
                    chigamOn = false;
                }
                else{
                    tags.put("Chi Gam", "");
                    chigamOn = true;
                }

                chigam.setChecked(chigamOn);
                filter();
            }
        });

         deltas = container.findViewById(R.id.deltasTagSearch);
        deltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("ΔΣΘ")){
                    tags.remove("ΔΣΘ");
                    deltasOn = false;
                }
                else{
                    tags.put("ΔΣΘ", "");
                    deltasOn = true;
                }

                deltas.setChecked(deltasOn);
                filter();
            }
        });

         ekt = container.findViewById(R.id.ektTagSearch);
        ekt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("EKT")){
                    tags.remove("EKT");
                    ektOn = false;
                }
                else{
                    tags.put("EKT", "");
                    ektOn = true;
                }

                ekt.setChecked(ektOn);
                filter();
            }
        });

         gdx = container.findViewById(R.id.gdxTagSearch);
        gdx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("GDX")){
                    tags.remove("GDX");
                    gdxOn = false;
                }
                else{
                    tags.put("GDX", "");
                    gdxOn = true;
                }

                gdx.setChecked(gdxOn);
                filter();
            }
        });

         hereot = container.findViewById(R.id.hereotTagSearch);
        hereot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Hereot")){
                    tags.remove("Hereot");
                    hereotOn = false;
                }
                else{
                    tags.put("Hereot", "");
                    hereotOn = true;
                }

                hereot.setChecked(hereotOn);
                filter();
            }
        });

         kappa = container.findViewById(R.id.kappaTagSearch);
        kappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Kappa")){
                    tags.remove("Kappa");
                    kappaOn = false;
                }
                else{
                    tags.put("Kappa", "");
                    kappaOn = true;
                }

                kappa.setChecked(kappaOn);
                filter();
            }
        });

         kd = container.findViewById(R.id.kdTagSearch);
        kd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("KD")){
                    tags.remove("KD");
                    kdOn = false;
                }
                else{
                    tags.put("KD", "");
                    kdOn = true;
                }

                kd.setChecked(kdOn);
                filter();
            }
        });

         kde = container.findViewById(R.id.kdeTagSearch);
        kde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("KDE")){
                    tags.remove("KDE");
                    kdeOn = false;
                }
                else{
                    tags.put("KDE", "");
                    kdeOn = true;
                }

                kde.setChecked(kdeOn);
                filter();
            }
        });

         trikap = container.findViewById(R.id.triKapTagSearch);
        trikap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Tri-Kap")){
                    tags.remove("Tri-Kap");
                    trikapOn = false;
                }
                else{
                    tags.put("Tri-Kap", "");
                    trikapOn = true;
                }

                trikap.setChecked(trikapOn);
                filter();
            }
        });

         phidelt = container.findViewById(R.id.phiDeltTagSearch);
        phidelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Phi Delt")){
                    tags.remove("Phi Delt");
                    phideltOn = false;
                }
                else{
                    tags.put("Phi Delt", "");
                    phideltOn = true;
                }

                phidelt.setChecked(phideltOn);
                filter();
            }
        });

         phitau = container.findViewById(R.id.phiTauTagSearch);
        phitau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Phi Tau")){
                    tags.remove("Phi Tau");
                    phitauOn = false;
                }
                else{
                    tags.put("Phi Tau", "");
                    phitauOn = true;
                }

                phitau.setChecked(phitauOn);
                filter();
            }
        });
        psiu = container.findViewById(R.id.psiUTagSearch);
        psiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Psi U")){
                    tags.remove("Psi U");
                    psiuOn = false;
                }
                else{
                    tags.put("Psi U", "");
                    psiuOn = true;
                }

                psiu.setChecked(psiuOn);
                filter();
            }
        });


        sigdelt = container.findViewById(R.id.sigDeltTagSearch);
        sigdelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Sigma Delt")){
                    tags.remove("Sigma Delt");
                    sigdeltOn = false;
                }
                else{
                    tags.put("Sigma Delt", "");
                    sigdeltOn = true;
                }

                sigdelt.setChecked(sigdeltOn);
                filter();
            }
        });

         signu = container.findViewById(R.id.sigNuTagSearch);
        signu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Sig Nu")){
                    tags.remove("Sig Nu");
                    signuOn = false;
                }
                else{
                    tags.put("Sig Nu", "");
                    signuOn = true;
                }

                signu.setChecked(signuOn);
                filter();
            }
        });

         tabard = container.findViewById(R.id.tabardTagSearch);
        tabard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Tabard")){
                    tags.remove("Tabard");
                    tabardOn = false;
                }
                else{
                    tags.put("Tabard", "");
                    tabardOn = true;
                }

                tabard.setChecked(tabardOn);
                filter();
            }
        });

         tdx = container.findViewById(R.id.tdxTagSearch);
        tdx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("TDX")){
                    tags.remove("TDX");
                    tdxOn = false;
                }
                else{
                    tags.put("TDX", "");
                    tdxOn = true;
                }

                tdx.setChecked(tdxOn);
                filter();
            }
        });

        zete = container.findViewById(R.id.zeteTagSearch);
        zete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("Zete")){
                    tags.remove("Zete");
                    zeteOn = false;
                }
                else{
                    tags.put("Zete", "");
                    zeteOn = true;
                }

                zete.setChecked(zeteOn);
                filter();
            }
        });

    }

    //sets the colors for each tag
    public void setCurrentColors(){
        HashMap<ToggleButton, Boolean> map = new HashMap<>();
        map.put(axa, axaOn);
        map.put(aka, akaOn);
        map.put(beta, betaOn);
        map.put(bg, bgOn);
        map.put(chigam, chigamOn);
        map.put(hereot, hereotOn);
        map.put(gdx, gdxOn);
        map.put(trikap, trikapOn);
        map.put(phidelt, phideltOn);
        map.put(psiu, psiuOn);
        map.put(signu, signuOn);
        map.put(tdx, tdxOn);
        map.put(zete, zeteOn);
        map.put(aphi, aphiOn);
        map.put(axid, axidOn);
        map.put(chidelt, chideltOn);
        map.put(ekt, ektOn);
        map.put(kd, kdOn);
        map.put(kde, kdeOn);
        map.put(kappa, kappaOn);
        map.put(sigdelt, sigdeltOn);
        map.put(alphatheta, alphathetaOn);
        map.put(phitau, phitauOn);
        map.put(tabard, tabardOn);
        map.put(alphas, alphasOn);
        map.put(aka, akaOn);
        map.put(deltas, deltasOn);

        for (ToggleButton button : map.keySet()){
            button.setChecked(map.get(button));
        }


    }

    //filters the post by tags
    public void filter(){
        PostDatabaseHelper.getAllPosts(new getAllPostsCallback() {
            @Override
            public void onCallback(ArrayList<Post> posts) {
                //array list for the filtered post
                ArrayList<Post> filteredPosts = new ArrayList<>();
                for(ListIterator<Post> it = posts.listIterator(); it.hasNext();){
                  //goes through the post and checks if the tag was clicked base off of the listeners
                Post post = it.next();
                    if (tags.size() != 0 & post.attributes != null){
                        boolean itsIn = false;
                        for (String att : post.attributes){
                            Log.d("att", att);
                            if (tags.containsKey(att)){
                                itsIn = true;
                                break;
                            }
                        }

                        if (itsIn){
                            filteredPosts.add(post);
                        }
                    }
                    else if (tags.size() == 0){
                        filteredPosts.add(post);
                    }
                }

                Log.d("filteredPosts", filteredPosts.toString());
                Log.d("filteredPosts", tags.toString());
                for (int i = arrayOfPosts.size() - 1; i > -1; i -= 1){
                    arrayOfPosts.remove(i);
                }
                //adds the post to the arraylist
                for (int i =filteredPosts.size() - 1; i > -1; i -= 1){
                    arrayOfPosts.add(filteredPosts.get(i));
                }


                assert filteredPosts.size() == arrayOfPosts.size();
                //sends the information over
                postsAdapter.notifyDataSetChanged();
            }
        });

    }


}

