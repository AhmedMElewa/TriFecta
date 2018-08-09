package com.trifecta.mada.trifecta13.fragment;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.DefaultItemAnimator;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.support.v7.widget.SearchView;
import android.support.v7.widget.StaggeredGridLayoutManager;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.iid.FirebaseInstanceId;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.Cart;
import com.trifecta.mada.trifecta13.activity.WelcomeActivity;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.PrefManager;
import com.trifecta.mada.trifecta13.other.ProductModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import static android.content.Context.SEARCH_SERVICE;
import static com.trifecta.mada.trifecta13.R.id.name;


public class HomeTab extends Fragment {

    private RecyclerView recyclerView;
    private DataAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private ProgressDialog mProgressDialog;
    private Firebase myFirebaseRef;
    private FirebaseAuth mAuth;


    public HomeTab() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }



    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_home_tab, container, false);

        myFirebaseRef = new Firebase("");
        mAuth = FirebaseAuth.getInstance();

//        Firebase tokensRef = myFirebaseRef.child("tokens");
//        Map<String, String> tokensData = new HashMap<String, String>();
//        String uid = mAuth.getCurrentUser().getUid();
//        tokensData.put("uid", uid);
//        tokensData.put("token", FirebaseInstanceId.getInstance().getToken());
//
//        tokensRef = myFirebaseRef.child("tokens").child(uid);
//        tokensRef.setValue(tokensData);
//
//        Log.e("Token is ", FirebaseInstanceId.getInstance().getToken());

        PrefManager prefManager = new PrefManager(getContext());
        // make first time launch TRUE
//        prefManager.setFirstTimeLaunch(true);
        if (prefManager.isFirstTimeLaunch()){
            startActivity(new Intent(getContext(), WelcomeActivity.class));
        }else
        {
            gridLayoutManager = new GridLayoutManager(getContext(), 2);

            recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view);
            recyclerView.setHasFixedSize(true);
            recyclerView.setLayoutManager(new StaggeredGridLayoutManager(2, StaggeredGridLayoutManager.VERTICAL));
            recyclerView.setItemAnimator(new DefaultItemAnimator());

            FirebaseDatabase database = FirebaseDatabase.getInstance();
            //DatabaseReference posts = database.getReference("products");
            showProgressDialog();
            Query query1 = database.getReference("products").orderByChild("prState").equalTo(true);
            query1.addValueEventListener(new ValueEventListener() {
                @Override
                public void onDataChange(DataSnapshot dataSnapshot) {


                    HashMap<String, ProductModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {
                    });

                    try {

                        List<ProductModel> data = new ArrayList<>(results.values());
                        adapter = new DataAdapter((ArrayList<ProductModel>) data, getContext(), getActivity());
                        recyclerView.setAdapter(adapter);

                    } catch (Exception e) {
                        return;
                    }
                    hideProgressDialog();
//                data = new ArrayList<>(Arrays.asList(jsonResponse.getresult()));
                }

                @Override
                public void onCancelled(DatabaseError databaseError) {
                    Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
                }
            });
        }





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
