package com.trifecta.mada.trifecta13.activity;

import android.content.Intent;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.RelativeLayout;
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

public class SearchResults extends AppCompatActivity {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private String category;
    private String prName;
    private TextView noSearch;
    private String uid;
    private long count=0;
    private TextView tv;
    private FirebaseAuth mAuth;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search_results);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search Results");

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView = (RecyclerView)findViewById(R.id.searched_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noSearch = (TextView)findViewById(R.id.noSearch);
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference posts = database.getReference("products");
        category = getIntent().getStringExtra("category");
        prName = getIntent().getStringExtra("prName");


        if (category!=null){
            Query query1 = database.getReference("products").orderByChild("prCategory").equalTo(category);
            query1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        if ((int) dataSnapshot.getChildrenCount() == 0) {
                            noSearch.setVisibility(VISIBLE);
                            recyclerView.setVisibility(GONE);
                        }else {
                            HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                            });
                            List<ProductModel> data = new ArrayList<>(results.values());
                            adapter = new DataAdapter((ArrayList<ProductModel>) data, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }
                    }catch (Exception e){
                        return;
                    };
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



        if (prName!=null){
            Query query2 = database.getReference("products").orderByChild("prName").startAt(prName)
                    .endAt(prName+"\uf8ff");
            query2.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {

                    try {
                        if ((int) dataSnapshot.getChildrenCount() == 0) {
                            noSearch.setVisibility(VISIBLE);
                            recyclerView.setVisibility(GONE);
                        }else {
                            HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                            });
                            List<ProductModel> data = new ArrayList<>(results.values());
                            adapter = new DataAdapter((ArrayList<ProductModel>) data, getApplicationContext());
                            recyclerView.setAdapter(adapter);
                        }

                    }catch (Exception e){
                        return;
                    };
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {

                }
            });
        }



    }

    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.badge);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        tv = (TextView) notifCount.findViewById(R.id.txtCount);
        ImageView imgCart = (ImageView)notifCount.findViewById(R.id.imgCart);


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query1 = database.getReference("carts").orderByChild("uid").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                count= dataSnapshot.getChildrenCount();
                tv.setText(String.valueOf( count));
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        imgCart.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cart.class);
                startActivity(intent);
            }
        });

        tv.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), Cart.class);
                startActivity(intent);
                finish();
            }
        });
        return true;
    }

    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();
        //noinspection SimplifiableIfStatement
        if (id == R.id.cart) {
//            Intent intent = new Intent(this, Cart.class);
//            startActivity(intent);
            Intent intent = new Intent(getApplicationContext(), Cart.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.search_ac) {
//            Intent intent = new Intent(this, Search.class);
//            startActivity(intent);
            Intent intent = new Intent(getApplicationContext(), Search.class);
            startActivity(intent);
            finish();
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