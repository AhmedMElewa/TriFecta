package com.trifecta.mada.trifecta13.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.CircleTransform;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.ProductModel;
import com.trifecta.mada.trifecta13.other.ReviewModel;
import com.trifecta.mada.trifecta13.other.ReviewsAdapter;
import com.trifecta.mada.trifecta13.other.StoreModel;
import com.trifecta.mada.trifecta13.other.StoreProductsAdapter;
import com.trifecta.mada.trifecta13.other.StoreReviewsAdapter;
import com.trifecta.mada.trifecta13.other.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class StorePreview extends AppCompatActivity {

    private FirebaseAuth mAuth;
    private TextView txtStoreName;
    private TextView txtStoreCountry;
    private TextView txtStoreLanguage;
    private TextView txtStoreSales;
    private ImageView imgStorePic;
    private ImageView imgProfile;
    private TextView userName;
    private String OwnerId;
    private RecyclerView recyclerView;
    private StoreProductsAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private RecyclerView recyclerView2;
    private ReviewsAdapter adapter2;
    private GridLayoutManager gridLayoutManager2;
    private TextView txtWelcome;
    private TextView txtAbout;
    private TextView txtShipping;
    private TextView txtReAndRx;
    private RatingBar ratingBar;
    private TextView btnContact;
    private TextView reviewsCheck;
    private TextView productsCheck;
    private boolean isTextViewClicked,isTextViewClicked1,isTextViewClicked2=false;
    private Button btnViewAllProducts;
    private FirebaseDatabase mRef = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_preview);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Preview");

        txtStoreName=(TextView) findViewById(R.id.storeProfileName);
        txtStoreCountry=(TextView)findViewById(R.id.City);
        txtStoreLanguage=(TextView)findViewById(R.id.storeLanguage);
        txtStoreSales=(TextView)findViewById(R.id.storeSales);
        imgStorePic=(ImageView)findViewById(R.id.storeProfilePhoto);
        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView = (RecyclerView)findViewById(R.id.StorePreview_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        imgProfile=(ImageView)findViewById(R.id.userProfilePhoto);
        userName=(TextView)findViewById(R.id.userProfileName);
        gridLayoutManager2 = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView2 = (RecyclerView)findViewById(R.id.reivews_recycler_view);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        txtWelcome=(TextView)findViewById(R.id.txtWelcome);
        txtAbout=(TextView)findViewById(R.id.txtAbout);
        txtShipping=(TextView)findViewById(R.id.txtShipping);
        txtReAndRx=(TextView)findViewById(R.id.txtReAndRx);
        ratingBar=(RatingBar)findViewById(R.id.profileRatingBar);
        btnContact=(TextView) findViewById(R.id.btnContact);
        reviewsCheck = (TextView)findViewById(R.id.reviewsCheck);
        productsCheck = (TextView)findViewById(R.id.productsCheck);
        btnViewAllProducts=(Button)findViewById(R.id.btnViewAllProducts);



        OwnerId=getIntent().getStringExtra("ownerId").toString();


        btnContact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Contact.class)
                        .putExtra("OwnerId",OwnerId);
                startActivity(intent);
            }
        });

        btnViewAllProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SellerAllProduct.class)
                        .putExtra("OwnerId",OwnerId);
                startActivity(intent);
            }
        });

        txtAbout.setMaxLines(1);
        txtShipping.setMaxLines(1);
        txtReAndRx.setMaxLines(1);

        txtAbout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewClicked) {
                    //This will shrink textview to 2 lines if it is expanded.
                    txtAbout.setMaxLines(1);
                    isTextViewClicked = false;
                } else {
                    //This will expand the textview if it is of 2 lines
                    txtAbout.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked = true;
                }
            }
        });

        txtShipping.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewClicked1) {
                    //This will shrink textview to 2 lines if it is expanded.
                    txtShipping.setMaxLines(1);
                    isTextViewClicked1 = false;
                } else {
                    //This will expand the textview if it is of 2 lines
                    txtShipping.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked1 = true;
                }
            }
        });

        txtReAndRx.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewClicked2) {
                    //This will shrink textview to 2 lines if it is expanded.
                    txtReAndRx.setMaxLines(1);
                    isTextViewClicked2 = false;
                } else {
                    //This will expand the textview if it is of 2 lines
                    txtReAndRx.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked2 = true;
                }
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Query query = database.getReference("stores").orderByChild("storeId").equalTo(OwnerId);
        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once

                try {
                    HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {});

                    List<StoreModel> posts = new ArrayList<>(results.values());
                    for (StoreModel post : posts) {
                        txtStoreName.setText(post.getStoreTitle());
                        txtStoreCountry.setText(post.getCountry());
                        txtStoreLanguage.setText(post.getLanguage());
                        Glide.with(getApplicationContext()).load(post.getStorePic())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(getApplicationContext()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgStorePic);
                        txtWelcome.setText(post.getWelcomeM());
                        txtAbout.setText("About\n"+post.getAbout());
                        txtShipping.setText("Shipping policy\n"+post.getDeliveryPolicy());
                        txtReAndRx.setText("Returns and exchange\n"+post.getReturnPolicy());
                    }

                }catch (Exception e){

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Query query5 = database.getReference("old_orders").orderByChild("sellerId").equalTo(OwnerId);
        query5.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                try {
                    txtStoreSales.setText((int) snapshot.getChildrenCount()+" sales");
                }catch (Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Query query6 = database.getReference("reviews").orderByChild("uid").equalTo(OwnerId);
        query6.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                try {
                    int totalNum= (int) snapshot.getChildrenCount();
                    Double totalRate=0.0;
//                    Object votes=snapshot.child("rating").getValue();
                    HashMap<String, ReviewModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, ReviewModel>>() {});
                    List<ReviewModel> posts = new ArrayList<>(results.values());
                    for (ReviewModel post : posts) {
                        totalRate +=Double.parseDouble(post.getRating());
                    }
                    ratingBar.setRating((float) (totalRate/totalNum));

                }catch (Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        Query query1 = database.getReference("products").orderByChild("uid").equalTo(OwnerId);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                });

                try {
                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        productsCheck.setVisibility(VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }
                    List<ProductModel> data = new ArrayList<>(results.values());
                    adapter = new StoreProductsAdapter((ArrayList<ProductModel>) data, getApplication());
                    recyclerView.setAdapter(adapter);

                }catch (Exception e){
                    return;
                };
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query2 = database.getReference("users").orderByChild("id").equalTo(OwnerId);
        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once
                try {

                    HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
                    List<User> posts = new ArrayList<>(results.values());
                    for (User post : posts) {
                        Glide.with(getApplicationContext()).load(post.getProfilePic())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(getApplicationContext()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgProfile);

                        userName.setText(post.getName());
                    }

                }catch (Exception e){}

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query3 = database.getReference("reviews").orderByChild("uid").equalTo(OwnerId);
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                HashMap<String, ReviewModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ReviewModel>>() {
                });
                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        reviewsCheck.setVisibility(VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }
                    List<ReviewModel> data = new ArrayList<>(results.values());
                    adapter2 = new ReviewsAdapter((ArrayList<ReviewModel>)data, getApplicationContext());
                    recyclerView2.setAdapter(adapter2);
                }catch (Exception e){
                    return;
                };
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {

        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}
