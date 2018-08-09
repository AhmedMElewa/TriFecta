package com.trifecta.mada.trifecta13.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
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
import com.trifecta.mada.trifecta13.other.CartModel;
import com.trifecta.mada.trifecta13.other.ContactAdapter;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.MessageModel;
import com.trifecta.mada.trifecta13.other.ProductModel;
import com.trifecta.mada.trifecta13.other.StoreModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class MywishList extends Fragment {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
    private TextView noFavItems;


    public MywishList() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_mywish_list, container, false);

        gridLayoutManager = new GridLayoutManager(getContext(), 2);
        recyclerView = (RecyclerView) v.findViewById(R.id.my_wish_recycler_view);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
        recyclerView.setItemAnimator(new DefaultItemAnimator());
        noFavItems=(TextView)v.findViewById(R.id.noFavItems);

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference posts = database.getReference("products");
        showProgressDialog();

        Query query = database.getReference("wishlist").orderByChild("wishId").equalTo(uid);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                });

                try {

                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        noFavItems.setVisibility(VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }else {
                        List<ProductModel> data = new ArrayList<>(results.values());
                        adapter = new DataAdapter((ArrayList<ProductModel>)data, getApplicationContext());
                        recyclerView.setAdapter(adapter);
                    }



                }catch (Exception e){
                    return;
                };
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });





//        Query query = database.getReference("wishlist").orderByChild("uid").equalTo(uid);
//        query.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                try {
//
//                    HashMap<String, CartModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, CartModel>>() {
//                    });
//
//                    List<CartModel> data = new ArrayList<>(results.values());
//
//                    for (CartModel post : data) {
//
//                        Query query1 = database.getReference("products").orderByChild("prId").equalTo(post.getPushId());
//                        query1.addValueEventListener(new ValueEventListener() {
//                            @Override
//                            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                                HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
//                                });
//
//                                try {
//
//                                    List<ProductModel> data = new ArrayList<>(results.values());
//                                    adapter = new DataAdapter((ArrayList<ProductModel>)data, getApplicationContext());
//                                    recyclerView.setAdapter(adapter);
//
//                                }catch (Exception e){
//                                    return;
//                                };
//                            }
//
//                            @Override
//                            public void onCancelled(DatabaseError databaseError) {
//
//                            }
//                        });
//
//                    }
//
//                }catch (Exception e){}
//
//                hideProgressDialog();
//
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });



        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }

}

