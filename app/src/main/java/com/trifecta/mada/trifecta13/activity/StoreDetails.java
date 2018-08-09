package com.trifecta.mada.trifecta13.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.fragment.Store;
import com.trifecta.mada.trifecta13.other.CircleTransform;
import com.trifecta.mada.trifecta13.other.StoreModel;
import com.trifecta.mada.trifecta13.other.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class StoreDetails extends AppCompatActivity {


    private EditText txtWelMessage;
    private EditText txtAbout;
    private EditText txtDeliveryPolicy;
    private EditText txtReAndEx;
    private Button btnSave;
    private  FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_store_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Store Details");

        txtWelMessage = (EditText)findViewById(R.id.inputStWel);
        txtAbout = (EditText)findViewById(R.id.inputStAbout);
        txtDeliveryPolicy = (EditText)findViewById(R.id.inputStDPolicy);
        txtReAndEx = (EditText)findViewById(R.id.inputStReAndEx);
        btnSave=(Button)findViewById(R.id.btnSaveStoreDe);
        mAuth= FirebaseAuth.getInstance();


        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        String uid = mAuth.getCurrentUser().getUid();
        Query query2 = database.getReference("stores").orderByChild("storeId").equalTo(uid);
        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {

                try{
                    HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {});

                    List<StoreModel> posts = new ArrayList<>(results.values());

                    for (StoreModel post : posts) {

                        txtWelMessage.setText(post.getWelcomeM());
                        txtAbout.setText(post.getAbout());
                        txtDeliveryPolicy.setText(post.getDeliveryPolicy());
                        txtReAndEx.setText(post.getReturnPolicy());


                    }
                }catch (Exception e){}


            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnSave.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                final FirebaseDatabase database = FirebaseDatabase.getInstance();

                String uid = mAuth.getCurrentUser().getUid();

                Query query3 = database.getReference("stores").orderByChild("storeId").equalTo(uid);
                query3.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {

                        try {
                            DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey();
                            String path = "/" + snapshot.getKey() + "/" + key;
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("welcomeM", txtWelMessage.getText().toString());
                            result.put("about",txtAbout.getText().toString());
                            result.put("deliveryPolicy",txtDeliveryPolicy.getText().toString());
                            result.put("returnPolicy",txtReAndEx.getText().toString());
                            database.getReference(path).updateChildren(result);

                        }
                        catch (Exception e){}

                        Toast.makeText(getApplicationContext(), "updated successful", Toast.LENGTH_SHORT).show();
                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {
                    }
                });
                finish();
            }
        });


    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
        finish();
    }

    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getApplicationContext());
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
