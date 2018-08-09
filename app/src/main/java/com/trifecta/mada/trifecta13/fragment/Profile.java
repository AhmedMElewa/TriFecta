package com.trifecta.mada.trifecta13.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Bundle;
import android.provider.MediaStore;
import android.support.annotation.NonNull;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.storage.FirebaseStorage;
import com.google.firebase.storage.StorageReference;
import com.google.firebase.storage.UploadTask;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.Cart;
import com.trifecta.mada.trifecta13.activity.EditProfile;
import com.trifecta.mada.trifecta13.activity.Search;
import com.trifecta.mada.trifecta13.other.CircleTransform;
import com.trifecta.mada.trifecta13.other.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.app.Activity.RESULT_OK;


public class Profile extends Fragment {


    private FirebaseAuth mAuth;
    private FirebaseDatabase mRef = FirebaseDatabase.getInstance();
    private ImageView imgProfile;
    private TextView userName;
    private TextView city;
    private TextView mobile;
    private TextView email;
    private TextView address;
    int PICK_IMAGE_REQUEST = 111;
    Uri filePath;
    private ProgressDialog mProgressDialog;

    public Profile() {
        // Required empty public constructor
    }


    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setHasOptionsMenu(true);


    }




    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        View v=inflater.inflate(R.layout.fragment_profile, container, false);

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();
        imgProfile = (ImageView)v.findViewById(R.id.userProfilePhoto);
        userName=(TextView) v.findViewById(R.id.userProfileName);
        city=(TextView)v.findViewById(R.id.City);
        mobile=(TextView)v.findViewById(R.id.userPhone);
        email=(TextView)v.findViewById(R.id.userEmail);
        address=(TextView)v.findViewById(R.id.userAddress);
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        city.setVisibility(View.VISIBLE);

        showProgressDialog();
        Query query2 = database.getReference("users").orderByChild("id").equalTo(uid);
        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once

                try {
                    showProgressDialog();
                    HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
                    List<User> posts = new ArrayList<>(results.values());
                    for (User post : posts) {
                        Glide.with(getContext()).load(post.getProfilePic())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(getContext()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(imgProfile);
                        userName.setText(post.getName());
                        city.setText(post.getCountry());
                        mobile.setText(post.getPhoneNumber());
                        email.setText(post.getEmail());
                        address.setText(post.getAddress());
                        hideProgressDialog();
                    }
                }catch (Exception e){}
                hideProgressDialog();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        return v;
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        if (requestCode == PICK_IMAGE_REQUEST && resultCode == RESULT_OK && data != null && data.getData() != null) {
            filePath = data.getData();

            try {
                //getting image from gallery
                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getContentResolver(), filePath);

                imgProfile.setImageBitmap(bitmap);
            } catch (Exception e) {
                e.printStackTrace();
            }
        }
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

    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }


    @Override
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_edite, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            Intent intent = new Intent(getContext(), EditProfile.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

}

