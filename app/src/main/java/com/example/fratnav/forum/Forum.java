package com.example.fratnav.forum;

import android.app.Activity;
import android.app.Dialog;
import android.app.ListActivity;
import android.content.Intent;
import android.graphics.Color;
import android.graphics.drawable.ColorDrawable;
import android.os.Bundle;
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

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
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
    public static PostsAdapter adapter;
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
        setContentView(R.layout.forum);
        tags = new HashMap<>();

        currentUser = FirebaseAuth.getInstance().getCurrentUser();

        // checks to make sure the user is currently logged in; otherwise, send to authentication
        if (currentUser == null) {
            startActivity(new Intent(this, Authentication.class));
            finish();
            return;
        }
        UserDatabaseHelper.getUserById(currentUser.getUid(), new getUserByIdCallback() {
            @Override
            public void onCallback(User user) {
                currentUserInfo = user;
                isHouse = user.house;
            }
        });

        coordinatorLayout = (CoordinatorLayout) findViewById(R.id.forumCoordinatorLayout);
        filter = (ImageView) findViewById(R.id.filter);



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

        recyclerViewforum = (RecyclerView) findViewById(R.id.rv_forumposts);
        LinearLayoutManager verticalLayoutManager =
                new LinearLayoutManager(Forum.this, LinearLayoutManager.VERTICAL, false);
        recyclerViewforum.setLayoutManager(verticalLayoutManager);

        postsAdapter = new RVPostsAdapter(getApplicationContext(), arrayOfPosts);
        recyclerViewforum.setAdapter(postsAdapter);

        postsAdapter.notifyDataSetChanged();



        // on click listener for posts
//        list.setOnItemClickListener(new AdapterView.OnItemClickListener() {
//            @Override
//            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
//
//                if (position != -1) {
//                    Log.d("position", String.valueOf(position));
//                    long viewId = view.getId();
//                    Log.d("click", String.valueOf(viewId));
//                    Post post = adapter.getItem(position);
//
//
//                    Intent intent = new Intent(Forum.this, PostActivity.class);
//                    intent.putExtra(POST_ID_KEY, post.id);
//                    intent.putExtra(USER_ID_KEY, currentUserInfo.username);
//
//
//                    startActivity(intent);
//                }}});

    }

    @Override
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
    public void savePost(View view) {
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

    public void openPostCreate(View view) {
        Intent intent = new Intent (Forum.this, CreatePost.class);
        startActivity(intent);
    }


    public static void refresh(){
        PostDatabaseHelper.getAllPosts(new getAllPostsCallback() {
            @Override
            public void onCallback(ArrayList<Post> posts) {
                arrayOfPosts = posts;
                postsAdapter.notifyDataSetChanged();
            }
        });

    }

//    public void onHeartClick(View v){
//        View parentRow = (View) v.getParent().getParent();
//        ListView lv = (ListView) parentRow.getParent();
//        int position = lv.getPositionForView(parentRow);
//        if (position == -1){
//            return;
//        }
//        Post post = adapter.getItem(position);
//        assert post != null;
//        Log.d("heartClick", post.id);
//
//        if (randomKey == null){
//            randomKey = UUID.randomUUID().toString();
//        }
//
//        boolean userDidLike = false;
//
//        ImageView heart = findViewById(R.id.heart);
//        heart.getTag();
//
//
//        if (post.usersLiked != null){
//            Log.d("userDidLike", post.usersLiked.toString());
//            for (String userId : post.usersLiked.values()){
//                Log.d("userDidLike", userId + ", " + currentUserInfo.userID);
//                if (userId.equals(currentUserInfo.userID)){
//                    userDidLike = true;
//                    break;
//                }
//            }
//        }
//
//
//        if (userDidLike){
//            PostDatabaseHelper.removeLikefromPost(currentUserInfo.userID, post.id, new likePostCallback() {
//                @Override
//                public void onCallback(int likes) {
//                    Forum.refresh();
//                    Log.d("like", "removeLike");
//                    post.usersLiked.remove(currentUserInfo.userID, currentUserInfo.userID);
//                    post.likes -= 1;
//                    Log.d("postprof", post.usersLiked.toString());
//
//
//                    /// need to change heart image drawable here
//                    heart.setBackgroundResource(R.drawable.like);
//
//                }
//            });
//
//
//        }
//        else{
//            PostDatabaseHelper.addLiketoPost(currentUserInfo.userID, post.id, new likePostCallback() {
//                @Override
//                public void onCallback(int likes) {
//                    Log.d("like", "addLike");
//                    Forum.refresh();
//                    if (post.usersLiked == null){
//                        post.usersLiked = new HashMap<>();
//                    }
//                    post.usersLiked.put(currentUserInfo.userID, currentUserInfo.userID);
//                    post.likes += 1;
//
//
//
//
//                }
//            });
//
//        }
//    }
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
                    heart.setBackgroundResource(R.drawable.filledlike);
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
                            post.likes -= 1;
                            Log.d("postprof", post.usersLiked.toString());


                            /// need to change heart image drawable here
                            //heart.setBackgroundResource(R.drawable.like);

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
        assert post != null;
        Log.d("heartClick", post.id);
        Intent intent = new Intent(getApplicationContext(), Profile.class);
        intent.putExtra(USER_ID_KEY, post.userID);
        startActivity(intent);

    }


    public void setOnClickListeners(ViewGroup container){
         axa = container.findViewById(R.id.axaTag);
        axa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("axa")){
                    tags.remove("axa");
                    axaOn = false;
                }
                else{
                    tags.put("axa", "");
                    axaOn = true;
                }

                axa.setChecked(axaOn);
            }
        });

         aka = container.findViewById(R.id.akaTag);
        aka.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("aka")){
                    tags.remove("aka");
                    akaOn = false;
                }
                else{
                    tags.put("aka", "");
                    akaOn = true;
                }

                aka.setChecked(akaOn);
            }
        });

         aphi = container.findViewById(R.id.aPhiTag);
        aphi.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("aphi")){
                    tags.remove("aphi");
                    aphiOn = false;
                }
                else{
                    tags.put("aphi", "");
                    aphiOn = true;
                }

                aphi.setChecked(aphiOn);
            }
        });


         alphas = container.findViewById(R.id.alphasTag);
        alphas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("alphas")){
                    tags.remove("alphas");
                    alphasOn = false;
                }
                else{
                    tags.put("alphas", "");
                    alphasOn = true;
                }

                alphas.setChecked(alphasOn);
            }
        });

         alphatheta = container.findViewById(R.id.alphaThetaTag);
        alphatheta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("alphatheta")){
                    tags.remove("alphatheta");
                    aphiOn = false;
                }
                else{
                    tags.put("alphatheta", "");
                    aphiOn = true;
                }

                alphatheta.setChecked(alphathetaOn);
            }
        });

         axid = container.findViewById(R.id.aXiDTag);
        axid.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("axid")){
                    tags.remove("axid");
                    axidOn = false;
                }
                else{
                    tags.put("axid", "");
                    axidOn = true;
                }

                axid.setChecked(axidOn);
            }
        });

         bg = container.findViewById(R.id.bGTag);
        bg.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("bg")){
                    tags.remove("bg");
                    bgOn = false;
                }
                else{
                    tags.put("bg", "");
                    bgOn = true;
                }

                bg.setChecked(bgOn);
            }
        });

         beta = container.findViewById(R.id.betaTag);
        beta.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("beta")){
                    tags.remove("beta");
                    bgOn = false;
                }
                else{
                    tags.put("beta", "");
                    betaOn = true;
                }

                beta.setChecked(betaOn);
            }
        });

         chidelt = container.findViewById(R.id.chiDeltTag);
        chidelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("chidelt")){
                    tags.remove("chidelt");
                    chideltOn = false;
                }
                else{
                    tags.put("chidelt", "");
                    chideltOn = true;
                }

                chidelt.setChecked(chideltOn);
            }
        });

         chigam = container.findViewById(R.id.chiGamTag);
        chigam.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("chigam")){
                    tags.remove("chigam");
                    bgOn = false;
                }
                else{
                    tags.put("chigam", "");
                    bgOn = true;
                }

                chigam.setChecked(chigamOn);
            }
        });

         deltas = container.findViewById(R.id.deltasTag);
        deltas.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("deltas")){
                    tags.remove("deltas");
                    deltasOn = false;
                }
                else{
                    tags.put("deltas", "");
                    deltasOn = true;
                }

                deltas.setChecked(deltasOn);
            }
        });

         ekt = container.findViewById(R.id.ektTag);
        ekt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("ekt")){
                    tags.remove("ekt");
                    ektOn = false;
                }
                else{
                    tags.put("ekt", "");
                    ektOn = true;
                }

                ekt.setChecked(ektOn);
            }
        });

         gdx = container.findViewById(R.id.gdxTag);
        gdx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("gdx")){
                    tags.remove("gdx");
                    gdxOn = false;
                }
                else{
                    tags.put("gdx", "");
                    gdxOn = true;
                }

                gdx.setChecked(gdxOn);
            }
        });

         hereot = container.findViewById(R.id.hereotTag);
        hereot.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("hereot")){
                    tags.remove("hereot");
                    hereotOn = false;
                }
                else{
                    tags.put("hereot", "");
                    hereotOn = true;
                }

                hereot.setChecked(hereotOn);
            }
        });

         kappa = container.findViewById(R.id.kappaTagSearch);
        kappa.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("kappa")){
                    tags.remove("kappa");
                    kappaOn = false;
                }
                else{
                    tags.put("kappa", "");
                    kappaOn = true;
                }

                kappa.setChecked(kappaOn);
            }
        });

         kd = container.findViewById(R.id.kdTag);
        kd.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("kd")){
                    tags.remove("kd");
                    kdOn = false;
                }
                else{
                    tags.put("kd", "");
                    kdOn = true;
                }

                kd.setChecked(kdOn);
            }
        });

         kde = container.findViewById(R.id.kdeTag);
        kde.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("kde")){
                    tags.remove("kde");
                    kdeOn = false;
                }
                else{
                    tags.put("kde", "");
                    kdeOn = true;
                }

                kde.setChecked(kdeOn);
            }
        });

         trikap = container.findViewById(R.id.triKapTag);
        trikap.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("trikap")){
                    tags.remove("trikap");
                    trikapOn = false;
                }
                else{
                    tags.put("trikap", "");
                    trikapOn = true;
                }

                trikap.setChecked(trikapOn);
            }
        });

         phidelt = container.findViewById(R.id.phiDeltTag);
        phidelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("phidelt")){
                    tags.remove("phidelt");
                    phideltOn = false;
                }
                else{
                    tags.put("phidelt", "");
                    phideltOn = true;
                }

                phidelt.setChecked(phideltOn);
            }
        });

         phitau = container.findViewById(R.id.phiTauTag);
        phitau.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("phitau")){
                    tags.remove("phitau");
                    phitauOn = false;
                }
                else{
                    tags.put("phitau", "");
                    phitauOn = true;
                }

                phitau.setChecked(phitauOn);
            }
        });
        psiu = container.findViewById(R.id.psiUTag);
        psiu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("psiu")){
                    tags.remove("psiu");
                    psiuOn = false;
                }
                else{
                    tags.put("psiu", "");
                    psiuOn = true;
                }

                psiu.setChecked(psiuOn);
            }
        });


        sigdelt = container.findViewById(R.id.sigDeltTag);
        sigdelt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("sigdelt")){
                    tags.remove("sigdelt");
                    sigdeltOn = false;
                }
                else{
                    tags.put("sigdelt", "");
                    sigdeltOn = true;
                }

                sigdelt.setChecked(sigdeltOn);
            }
        });

         signu = container.findViewById(R.id.sigNuTag);
        signu.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("signu")){
                    tags.remove("signu");
                    signuOn = false;
                }
                else{
                    tags.put("signu", "");
                    signuOn = true;
                }

                signu.setChecked(signuOn);
            }
        });

         tabard = container.findViewById(R.id.tabardTag);
        tabard.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("tabard")){
                    tags.remove("tabard");
                    tabardOn = false;
                }
                else{
                    tags.put("tabard", "");
                    tabardOn = true;
                }

                tabard.setChecked(tabardOn);
            }
        });

         tdx = container.findViewById(R.id.tdxTag);
        tdx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("tdx")){
                    tags.remove("tdx");
                    tdxOn = false;
                }
                else{
                    tags.put("tdx", "");
                    tdxOn = true;
                }

                tdx.setChecked(tdxOn);
            }
        });

        zete = container.findViewById(R.id.zeteTag);
        zete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (tags.containsKey("zete")){
                    tags.remove("zete");
                    zeteOn = false;
                }
                else{
                    tags.put("zete", "");
                    zeteOn = true;
                }

                zete.setChecked(zeteOn);
            }
        });

    }


    public void setCurrentColors(){


    }


}

