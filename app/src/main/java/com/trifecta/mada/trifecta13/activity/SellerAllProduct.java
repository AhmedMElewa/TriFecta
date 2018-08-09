package com.trifecta.mada.trifecta13.activity;

import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.MenuItem;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.ProductModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class SellerAllProduct extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private GridLayoutManager gridLayoutManager;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_seller_all_product);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView = (RecyclerView)findViewById(R.id.my_products_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        String ownerId=getIntent().getStringExtra("OwnerId");

        Query query1 = database.getReference("products").orderByChild("uid").equalTo(ownerId);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                });

                try {

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
