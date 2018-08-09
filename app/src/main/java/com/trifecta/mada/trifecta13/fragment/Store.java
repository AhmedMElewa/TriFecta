package com.trifecta.mada.trifecta13.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.content.Intent;

import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.trifecta.mada.trifecta13.activity.EditProfile;
import com.trifecta.mada.trifecta13.activity.EditStore;
//import com.trifecta.mada.trifecta13.activity.MyProducts2;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.MyProducts2;
import com.trifecta.mada.trifecta13.activity.MyStore;
import com.trifecta.mada.trifecta13.activity.Reviews;
import com.trifecta.mada.trifecta13.activity.SellerTermsConditions;
import com.trifecta.mada.trifecta13.activity.StoreDetails;
import com.trifecta.mada.trifecta13.other.CircleTransform;
import com.trifecta.mada.trifecta13.other.ReviewModel;
import com.trifecta.mada.trifecta13.other.StoreModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class Store extends Fragment {


    private FirebaseAuth mAuth;
    private TextView storeName;
    private TextView storeCountry;
    private TextView storeLanguage;
    private Button btnMyProducts;
    private Button btnMyReviews;
    private TextView ballance;
    private TextView pay;
    private LinearLayout linPay;
//    private Button btnStDetails;
//    private TextView btnShare;
    private ImageView profilePic;
    private RatingBar ratingBar;
    private TextView btnTC;
//    private ShareDialog shareDialog;
    private FirebaseDatabase mRef = FirebaseDatabase.getInstance();

//    int PICK_IMAGE_REQUEST = 111;
//    Uri filePath;
//    Button save;

    private ProgressDialog mProgressDialog;

    public Store() {
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
        // Inflate the layout for this fragment

        View v=inflater.inflate(R.layout.fragment_store, container, false);

        storeName=(TextView)v.findViewById(R.id.userProfileName);
        storeCountry=(TextView)v.findViewById(R.id.City);
        storeLanguage=(TextView)v.findViewById(R.id.storeLanguage);
        btnMyProducts=(Button)v.findViewById(R.id.btnMyProducts);
        btnMyReviews=(Button)v.findViewById(R.id.btnReviews) ;
//        btnStDetails=(Button)v.findViewById(R.id.btnDetailsStore);
        ballance=(TextView)v.findViewById(R.id.txtBalance) ;
        profilePic=(ImageView)v.findViewById(R.id.userProfilePhoto);
//        save=(Button) v.findViewById(R.id.btnSaveStore);
        ratingBar=(RatingBar)v.findViewById(R.id.profileRatingBar) ;
//        btnShare= (TextView)v.findViewById(R.id.btnShareStore);
//        shareDialog = new ShareDialog(this);
        pay = (TextView)v.findViewById(R.id.pay);
        linPay = (LinearLayout)v.findViewById(R.id.linPay);
        btnTC = (TextView)v.findViewById(R.id.btnTC);

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();


        btnTC.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), SellerTermsConditions.class);
                startActivity(intent);
            }
        });

        showProgressDialog();
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
                        storeName.setText(post.getStoreTitle());
                        storeCountry.setText(post.getCountry());
                        storeLanguage.setText(post.getLanguage());
                        ballance.setText(post.getBallance());
                        Glide.with(getContext()).load(post.getStorePic())
                                .crossFade()
                                .thumbnail(0.5f)
                                .bitmapTransform(new CircleTransform(getContext()))
                                .diskCacheStrategy(DiskCacheStrategy.ALL)
                                .into(profilePic);
                    }

                }catch (Exception e){

                }
                hideProgressDialog();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query query2 = database2.getReference("stores").orderByChild("storeId").equalTo(uid);
        query1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once

                try {
                    HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {});

                    List<StoreModel> posts = new ArrayList<>(results.values());

                    for (StoreModel post : posts) {

                        if (Integer.parseInt(post.getBallance())<-100){
                            pay.setText("To UNLOCK your store you have to pay the commission!"+"\nSend us message with subject \"Pay\" Or call us"+"\nPhone: +201092993329");
                            linPay.setVisibility(View.VISIBLE);

                        }

                    }

                }catch (Exception e){

                }
                hideProgressDialog();

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query query = database2.getReference("reviews").orderByChild("uid").equalTo(uid);
        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                try {
                    int totalNum= (int) snapshot.getChildrenCount();
                    Double totalRate=0.0;
//                    Object votes=snapshot.child("rating").getValue();
                    HashMap<String, ReviewModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, ReviewModel>>() {});
                    List<ReviewModel> posts = new ArrayList<>(results.values());
                    for (ReviewModel post : posts) {
                        totalRate +=Double.parseDouble(post.getRating());
                    }
                    ratingBar.setRating((float) (totalRate/totalNum));

                }catch (Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });



        btnMyProducts.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), MyProducts2.class);
                startActivity(intent);
            }
        });

        btnMyReviews.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getContext(), Reviews.class);
                startActivity(intent);
            }
        });

//        btnStDetails.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), StoreDetails.class);
//                startActivity(intent);
//            }
//        });

//        btnShare.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                FirebaseDatabase database2 = FirebaseDatabase.getInstance();
//                Query query1 = database2.getReference("stores").orderByChild("storeId").equalTo(uid);
//                query1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//                    @Override
//                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
//                        // do some stuff once
//                        try {
//                            HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {});
//                            List<StoreModel> posts = new ArrayList<>(results.values());
//                            for (StoreModel post : posts) {
//                                storeName.setText(post.getStoreTitle());
//                                Glide.with(getContext()).load(post.getStorePic())
//                                        .crossFade()
//                                        .thumbnail(0.5f)
//                                        .bitmapTransform(new CircleTransform(getContext()))
//                                        .diskCacheStrategy(DiskCacheStrategy.ALL)
//                                        .into(profilePic);
//                                ShareLinkContent content = new ShareLinkContent.Builder()
//                                        .setContentTitle(post.getStoreTitle())
//                                        .setImageUrl(Uri.parse(post.getStorePic()))
////                        .setContentUrl(Uri.parse("https://developers.facebook.com"))
//                                        .build();
//
//                                ShareDialog.show(Store.this,content);
//                            }
//
//                        }catch (Exception e){
//                        }
//                    }
//                    @Override
//                    public void onCancelled(DatabaseError databaseError) {
//                    }
//                });
//
//
//            }
//        });

//        btnMyProducts.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getContext(), MyProducts.class);
//                startActivity(intent);
//            }
//        });
//


//        profilePic.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent();
//                intent.setType("image/*");
//                intent.setAction(Intent.ACTION_PICK);
//                startActivityForResult(Intent.createChooser(intent, "Select Image"), PICK_IMAGE_REQUEST);
//            }
//        });
//
//        save.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//
//                if (filePath != null) {
//                    FirebaseStorage storage = FirebaseStorage.getInstance();
//                    final StorageReference storageRef = storage.getReferenceFromUrl("gs://trifecta12-693fa.appspot.com");
//                    DatabaseReference users = mRef.getReference("users");
//                    StorageReference childRef = storageRef.child("storeImage/"+users.push().getKey());
//                    UploadTask uploadTask = childRef.putFile(filePath);
//                    uploadTask.addOnSuccessListener(new OnSuccessListener<UploadTask.TaskSnapshot>() {
//                        @Override
//                        public void onSuccess(UploadTask.TaskSnapshot taskSnapshot) {
//
//                            final Uri downloadUrl = taskSnapshot.getDownloadUrl();
//                            Toast.makeText(getContext(), "Upload successful", Toast.LENGTH_SHORT).show();
//
//                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
//
//                            Query query2 = database.getReference("stores").orderByChild("storeId").equalTo(uid);
//                            query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//                                @Override
//                                public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {
//
//                                    DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
//                                    String key = nodeDataSnapshot.getKey();
//                                    String path = "/" + snapshot.getKey() + "/" + key;
//                                    HashMap<String, Object> result = new HashMap<>();
//                                    result.put("storePic", String.valueOf(downloadUrl));
//                                    result.put("storeTitle",storeName.getText().toString());
//                                    database.getReference(path).updateChildren(result);
//
//                                }
//                                @Override
//                                public void onCancelled(DatabaseError databaseError) {
//                                }
//                            });
//
//                        }
//                    }).addOnFailureListener(new OnFailureListener() {
//                        @Override
//                        public void onFailure(@NonNull Exception e) {
//                            Toast.makeText(getContext(), "Upload Failed -> " + e, Toast.LENGTH_SHORT).show();
//                        }
//                    });
//                } else {
//                    Toast.makeText(getContext(), "Select an image", Toast.LENGTH_SHORT).show();
//                }
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


//    @Override
//    public void onActivityResult(int requestCode, int resultCode, Intent data) {
//        super.onActivityResult(requestCode, resultCode, data);
//        if (requestCode == PICK_IMAGE_REQUEST && resultCode == Activity.RESULT_OK && data != null && data.getData() != null) {
//            filePath = data.getData();
//
//            try {
//                //getting image from gallery
//                Bitmap bitmap = MediaStore.Images.Media.getBitmap(getActivity().getApplicationContext().getContentResolver(), filePath);
//
//                profilePic.setImageBitmap(bitmap);
//            } catch (Exception e) {
//                e.printStackTrace();
//            }
//        }
//    }
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
    public void onCreateOptionsMenu(Menu menu, MenuInflater inflater) {
        inflater.inflate(R.menu.profile_edite, menu);
        super.onCreateOptionsMenu(menu,inflater);

    }

    public boolean onOptionsItemSelected(MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.edit_profile) {
            Intent intent = new Intent(getContext(), EditStore.class);
            startActivity(intent);
            return true;
        }

        return super.onOptionsItemSelected(item);
    }
}