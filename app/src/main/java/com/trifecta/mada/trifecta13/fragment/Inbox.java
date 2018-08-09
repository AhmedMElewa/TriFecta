package com.trifecta.mada.trifecta13.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
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
import com.trifecta.mada.trifecta13.other.ContactAdapter;
import com.trifecta.mada.trifecta13.other.MessageModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;


public class Inbox extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private ContactAdapter adapter;
    private GridLayoutManager gridLayoutManager;
    private GridLayoutManager gridLayoutManager2;
    private ProgressDialog mProgressDialog;
    private FirebaseAuth mAuth;
//    private TextView noInbox;

    public Inbox() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v = inflater.inflate(R.layout.fragment_inbox, container, false);

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView = (RecyclerView) v.findViewById(R.id.recycler_view_contact);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);

        gridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerView2 = (RecyclerView) v.findViewById(R.id.recycler_view_contact2);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(gridLayoutManager2);
        mAuth = FirebaseAuth.getInstance();
//        noInbox= (TextView)v.findViewById(R.id.noInbox);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        //DatabaseReference posts = database.getReference("products");
        showProgressDialog();
        final String uid = mAuth.getCurrentUser().getUid();
        Query query1 = database.getReference("contact").orderByChild("receiverId").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, MessageModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, MessageModel>>() {
                });

                try {

                    if ((int) dataSnapshot.getChildrenCount() == 0) {
//                        noInbox.setVisibility(VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }else {
                        List<MessageModel> data = new ArrayList<>(results.values());
                        adapter = new ContactAdapter((ArrayList<MessageModel>) data, getContext(), getActivity());
                        recyclerView.setAdapter(adapter);
                        recyclerView.setVisibility(VISIBLE);
                    }



                } catch (Exception e) {
                    return;
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

        Query query2 = database.getReference("contact").orderByChild("senderId").equalTo(uid);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, MessageModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, MessageModel>>() {
                });

                try {

                    List<MessageModel> data = new ArrayList<>(results.values());
                    adapter = new ContactAdapter((ArrayList<MessageModel>) data, getContext(), getActivity());
                    recyclerView2.setAdapter(adapter);
                    recyclerView2.setVisibility(VISIBLE);

                } catch (Exception e) {
                    return;
                }
                hideProgressDialog();
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

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
