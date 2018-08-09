package com.trifecta.mada.trifecta13.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.ProductModel;
import com.trifecta.mada.trifecta13.other.StoreModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class MyProducts extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private FirebaseAuth mAuth;
    private TextView productsCheck;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_my_products);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("My Product");


        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView = (RecyclerView)findViewById(R.id.my_products_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        productsCheck = (TextView)findViewById(R.id.productsCheck);



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference posts = database.getReference("products");

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();


        Query query1 = database.getReference("products").orderByChild("uid").equalTo(uid);
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
                    adapter = new DataAdapter((ArrayList<ProductModel>)data, getApplicationContext());
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

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.add_menu, menu);
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == R.id.add_product) {

            mAuth = FirebaseAuth.getInstance();
            String uid = mAuth.getCurrentUser().getUid();
            FirebaseDatabase database2 = FirebaseDatabase.getInstance();
            Query query1 = database2.getReference("stores").orderByChild("storeId").equalTo(uid);
            query1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                @Override
                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                    // do some stuff once
                    try {
                        HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {});
                        List<StoreModel> posts = new ArrayList<>(results.values());
                        for (StoreModel post : posts) {
                            if(Integer.parseInt(post.getBallance())<=-5){
                                Toast.makeText(getApplicationContext(), "Your have to pay your debit first", Toast.LENGTH_LONG).show();
                            }
                            else {
                                Intent intent = new Intent(getApplicationContext(), AddProduct.class);
                                startActivity(intent);
                                finish();
                            }
                        }
                    }catch (Exception e){

                    }
                }
                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
            return true;
        }

        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        Intent intent = new Intent(getApplicationContext(), MainActivity.class);
        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
        startActivity(intent);
        finish();
    }
}
