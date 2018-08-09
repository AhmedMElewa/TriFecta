package com.trifecta.mada.trifecta13.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.MenuItem;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.CartAdapter;
import com.trifecta.mada.trifecta13.other.CartModel;
import com.trifecta.mada.trifecta13.other.ReviewModel;
import com.trifecta.mada.trifecta13.other.ReviewsAdapter;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Reviews extends AppCompatActivity {

    private RecyclerView recyclerView;
    private ReviewsAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private FirebaseAuth mAuth;
    private TextView reviewsCheck ;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_reviews);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Reviews");

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView = (RecyclerView)findViewById(R.id.reviews_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
        reviewsCheck = (TextView)findViewById(R.id.reviewsCheck);


        FirebaseDatabase database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        Query query1 = database.getReference("reviews").orderByChild("uid").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, ReviewModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ReviewModel>>() {
                });

                try {
                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        reviewsCheck.setVisibility(VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }
                    List<ReviewModel> data = new ArrayList<>(results.values());
                    adapter = new ReviewsAdapter((ArrayList<ReviewModel>)data, getApplicationContext());
                    recyclerView.setAdapter(adapter);



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
