package com.trifecta.mada.trifecta13.activity;


import android.content.Context;
import android.content.Intent;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.LinearLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.KeyEvent;
import android.view.LayoutInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.inputmethod.EditorInfo;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.Category;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.ProductModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class Search extends AppCompatActivity {

    private RecyclerView recyclerView;

    private RecyclerView.Adapter adapter;
    private RecyclerView.LayoutManager layoutManager;
    private ArrayList<String> categoryList=new ArrayList();
    private EditText search_query;
    private ImageView searchicon;

    private RecyclerView recyclerView2;
    private DataAdapter adapter2;
    private GridLayoutManager gridLayoutManager;
    private String category;
    private String prName;
    private TextView noSearch;
    private TextView categortyTitle;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_search);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Search");

        ActionBar actionBar = getSupportActionBar();
//        actionBar.setDisplayHomeAsUpEnabled(true);
        actionBar.setDisplayShowCustomEnabled(true);
        actionBar.setDisplayShowTitleEnabled(false);
        actionBar.setIcon(R.drawable.searchicon);

        LayoutInflater inflator = (LayoutInflater) this .getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        View v = inflator.inflate(R.layout.search, null);
        search_query = (EditText)v.findViewById(R.id.search_query);
        searchicon = (ImageView)v.findViewById(R.id.searchicon);

        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 2);
        recyclerView2 = (RecyclerView)findViewById(R.id.searched_recycler_view);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView2.setItemAnimator(new DefaultItemAnimator());
        noSearch = (TextView)findViewById(R.id.noSearch);
        categortyTitle = (TextView)findViewById(R.id.categortyTitle);




        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference posts = database.getReference("products");



        search_query.setOnEditorActionListener(new TextView.OnEditorActionListener() {
            @Override
            public boolean onEditorAction(TextView v, int actionId, KeyEvent event) {
                if (actionId == EditorInfo.IME_ACTION_SEARCH) {
                    Intent intent = new Intent(v.getContext(), SearchResults.class);
                    intent.putExtra("prName",search_query.getText().toString());
                    v.getContext().startActivity(intent);
                    finish();
                    return true;
                }
                return false;
            }
        });




        actionBar.setCustomView(v);

        categoryList.add("Accessories");
        categoryList.add("Bags");
        categoryList.add("Jewelry");
        categoryList.add("Clothing & Shoes");
        categoryList.add("Home & Living");
        categoryList.add("Pet Supplies");
        categoryList.add("Party Supplies");
        categoryList.add("Electronic Accessories");
        categoryList.add("Toys");
        categoryList.add("Art & Collectibles");
        categoryList.add("Craft Supplies Tools");

        recyclerView= (RecyclerView) findViewById(R.id.search_recycler_view);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        adapter=new Category(categoryList,getApplicationContext());
        recyclerView.setAdapter(adapter);







        searchicon.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                prName = search_query.getText().toString();
                category = getIntent().getStringExtra("category");
                if (category!=null){
                    Query query1 = database.getReference("products").orderByChild("prCategory").equalTo(category);
                    query1.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                if ((int) dataSnapshot.getChildrenCount() == 0) {
                                    noSearch.setVisibility(VISIBLE);
                                    recyclerView2.setVisibility(GONE);
                                }else {
                                    HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                                    });
                                    List<ProductModel> data = new ArrayList<>(results.values());
                                    adapter2 = new DataAdapter((ArrayList<ProductModel>) data, getApplicationContext());
                                    recyclerView2.setAdapter(adapter2);
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

                    recyclerView.setVisibility(GONE);
                    categortyTitle.setVisibility(GONE);
                    recyclerView2.setVisibility(VISIBLE);
                    Query query2 = database.getReference("products").orderByChild("prName").startAt(prName)
                            .endAt(prName+"\uf8ff");
                    query2.addValueEventListener(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {

                            try {
                                if ((int) dataSnapshot.getChildrenCount() == 0) {
                                    noSearch.setVisibility(VISIBLE);
                                    recyclerView2.setVisibility(GONE);
                                }else {
                                    HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                                    });
                                    List<ProductModel> data = new ArrayList<>(results.values());
                                    adapter2 = new DataAdapter((ArrayList<ProductModel>) data, getApplicationContext());
                                    recyclerView2.setAdapter(adapter2);
                                    noSearch.setVisibility(GONE);
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
