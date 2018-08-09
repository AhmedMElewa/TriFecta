package com.trifecta.mada.trifecta13.activity;

import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v4.view.MenuItemCompat;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.animation.Animation;
import android.view.animation.ScaleAnimation;
import android.widget.ArrayAdapter;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.RelativeLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.CartAdapter;
import com.trifecta.mada.trifecta13.other.CartModel;
import com.trifecta.mada.trifecta13.other.ReviewModel;
import com.trifecta.mada.trifecta13.other.ReviewsAdapter;
import com.trifecta.mada.trifecta13.other.StoreModel;
import com.trifecta.mada.trifecta13.other.WishModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;

public class ProductDetails extends AppCompatActivity  {

//    implements View.OnClickListener
    private String Id ,prId, name, price,pic,quantity,description,loaction,homeOne, prProcessing,category;
    private ImageView prPic;
    private TextView prName;
    private TextView prPrice;
    private Spinner prQuantity;
    private TextView btnBuy ;
    private ImageView btnVStore;
    private TextView deleteMyPr;
    private String uid;
    private long count=0;
    private TextView tv;
    private ImageView imgCart;

    private ImageView fav , unfav;

    private FirebaseDatabase mRef = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;

    private RecyclerView recyclerView;
    private ReviewsAdapter adapterRe;
    private GridLayoutManager gridLayoutManager;

    private TextView stName;
    private TextView prProcessTime;
    private TextView shipFees;
    private TextView prCategory;
    private TextView desc;
    private TextView delivery;
    private TextView returns;
    private TextView reviewsCheck;
    private boolean isTextViewClicked = false,isTextViewClicked2 = false,isTextViewClicked3 = false;


    private TextView popupText;
    private Button okbtn;
    private Button cancelbtn;
    private LinearLayout layoutOfPopup;
    public AlertDialog.Builder popDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_product_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Product Details");

        name = getIntent().getStringExtra("prName");
        price = getIntent().getStringExtra("prPrice");
        pic = getIntent().getStringExtra("prPic");
        quantity = getIntent().getStringExtra("prQuantity");
        Id = getIntent().getStringExtra("id");
        prId = getIntent().getStringExtra("prId");
        prProcessing = getIntent().getStringExtra("prProcessing");
        loaction = getIntent().getStringExtra("prLocation");
        homeOne = getIntent().getStringExtra("prMyOneItem");
        category = getIntent().getStringExtra("category");
        description = getIntent().getStringExtra("prDesc");
        mAuth = FirebaseAuth.getInstance();
        uid = mAuth.getCurrentUser().getUid();

        prPic = (ImageView) findViewById(R.id.prPic);
        prName = (TextView) findViewById(R.id.prName);
        prPrice = (TextView) findViewById(R.id.prPrice);
        prQuantity = (Spinner) findViewById(R.id.quantity);
        btnBuy = (TextView) findViewById(R.id.btnBuy);
        btnVStore=(ImageView) findViewById(R.id.btnVStore);
        stName = (TextView) findViewById(R.id.brand);
        prProcessTime = (TextView) findViewById(R.id.prProcessTime);
        shipFees = (TextView) findViewById(R.id.ship);
        prCategory = (TextView) findViewById(R.id.category);
        desc = (TextView) findViewById(R.id.disc1);
        delivery = (TextView) findViewById(R.id.delivery);
        returns = (TextView) findViewById(R.id.returns);
        reviewsCheck = (TextView) findViewById(R.id.reviewsCheck);
        deleteMyPr = (TextView)findViewById(R.id.deleteMyPr);

        popupText = new TextView(getApplicationContext());
        okbtn = new Button(getApplicationContext());
        cancelbtn = new Button(getApplicationContext());
        layoutOfPopup = new LinearLayout(getApplicationContext());
        popDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));

        prName.setText(name);
        prPrice.setText(price + " EGP");
        Glide.with(getApplicationContext()).load(pic)
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.back)
                .into(prPic);

        Integer[] items = new Integer[Integer.parseInt(quantity)];
        for (int i=0;i<Integer.parseInt(quantity);i++){
            items[i] = i+1;
        }
        ArrayAdapter<Integer> spinAdapter = new ArrayAdapter<Integer>(this,android.R.layout.simple_spinner_item, items);
        prQuantity.setAdapter(spinAdapter);

//        prQuantity.setText(quantity);
        prProcessTime.setText(prProcessing + " Days");
        shipFees.setText(homeOne + " EGP");
        prCategory.setText(category);
        desc.setText("Item description\n\n" + description);
        desc.setMaxLines(1);
        delivery.setMaxLines(1);
        returns.setMaxLines(1);

        desc.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewClicked) {
                    //This will shrink textview to 2 lines if it is expanded.
                    desc.setMaxLines(1);
                    isTextViewClicked = false;
                } else {
                    //This will expand the textview if it is of 2 lines
                    desc.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked = true;
                }
            }
        });

        delivery.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewClicked2) {
                    //This will shrink textview to 2 lines if it is expanded.
                    delivery.setMaxLines(1);
                    isTextViewClicked2 = false;
                } else {
                    //This will expand the textview if it is of 2 lines
                    delivery.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked2 = true;
                }
            }
        });

        returns.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (isTextViewClicked3) {
                    //This will shrink textview to 2 lines if it is expanded.
                    returns.setMaxLines(1);
                    isTextViewClicked3 = false;
                } else {
                    //This will expand the textview if it is of 2 lines
                    returns.setMaxLines(Integer.MAX_VALUE);
                    isTextViewClicked3 = true;
                }
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("stores").orderByChild("storeId").equalTo(Id);
        query.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    HashMap<String, StoreModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {
                    });
                    List<StoreModel> data = new ArrayList<>(results.values());
                    for (StoreModel post : data) {
                        stName.setText(post.getStoreTitle());
                        delivery.setText("Delivery\n\n" + post.getDeliveryPolicy());
                        returns.setText("Returns\n\n" + post.getReturnPolicy());
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        Query query2 = database.getReference("carts").orderByChild("pushId").equalTo(prId);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try {
                    HashMap<String, CartModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, CartModel>>() {
                    });
                    List<CartModel> data = new ArrayList<>(results.values());
                    for (CartModel post : data) {
                        if(post.getUid().equals(uid)) {
                            btnBuy.setEnabled(false);
                        }
                    }
                } catch (Exception e) {
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        btnBuy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                CartModel cartModel = new CartModel();
                DatabaseReference cart = mRef.getReference("carts");
                cartModel.setCrId(Id);
                cartModel.setCrName(name);
                cartModel.setCrPic(pic);
                cartModel.setCrPrice(price);
                cartModel.setCrQuantity(String.valueOf(prQuantity.getSelectedItemPosition()));
                cartModel.setPushId(prId);
                cartModel.setUid(uid);
                cart.child(uid + prId).setValue(cartModel);
                Toast.makeText(ProductDetails.this, "product add to your cart", Toast.LENGTH_SHORT).show();
            }
        });


        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
        recyclerView = (RecyclerView) findViewById(R.id.lv_items);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);



        Query query1 = database.getReference("reviews").orderByChild("uid").equalTo(Id);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, ReviewModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, ReviewModel>>() {
                });

                try {
                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        reviewsCheck.setVisibility(VISIBLE);
                        recyclerView.setVisibility(GONE);
                    }

                    List<ReviewModel> data = new ArrayList<>(results.values());
                    adapterRe = new ReviewsAdapter((ArrayList<ReviewModel>) data, getApplicationContext());
                    recyclerView.setAdapter(adapterRe);

                } catch (Exception e) {
                    return;
                }
                ;

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        btnVStore.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), StorePreview.class).putExtra("ownerId",Id);
                startActivity(intent);

            }
        });


        //favourite and un favourite 3aks b3d

        fav = (ImageView) findViewById(R.id.unfav);
        unfav = (ImageView) findViewById(R.id.fav);

        fav.setVisibility(VISIBLE);
        unfav.setVisibility(GONE);
        fav.invalidate();
        unfav.invalidate();
        final ScaleAnimation animation = new ScaleAnimation(0f, 1f, 0f, 1f,
                Animation.RELATIVE_TO_SELF, 0.5f,
                Animation.RELATIVE_TO_SELF, 0.5f);
        animation.setDuration(165);

        fav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                fav.setVisibility(GONE);
                unfav.startAnimation(animation);
                unfav.setVisibility(VISIBLE);

                //sending data to db
                FirebaseDatabase database = FirebaseDatabase.getInstance();
                FirebaseDatabase mRef = FirebaseDatabase.getInstance();

                WishModel WishModel = new WishModel();
                DatabaseReference Wish = mRef.getReference("wishlist");
                WishModel.setUid(Id);
                WishModel.setWishId(uid);
                WishModel.setPrId(prId);
                WishModel.setPrName(name);
                WishModel.setPrPrice(price);
                WishModel.setPrPic(pic);
                WishModel.setPrQuantity(quantity);
                WishModel.setPrDescription(description);
                WishModel.setPrLocation(loaction);
                WishModel.setPrHomeCountryO(homeOne);
                WishModel.setPrProcessTime(prProcessing);
                Wish.child(uid + prId).setValue(WishModel);

            }
        });

        unfav.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                unfav.setVisibility(GONE);
                fav.startAnimation(animation);
                fav.setVisibility(VISIBLE);

                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                Query applesQuery = db_node.child("wishlist").orderByChild("prId").equalTo(prId);

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });


            }
        });

        DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
        Query applesQuery = db_node.child("wishlist").orderByChild("wishId").equalTo(uid);
        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                try {

                    DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                    Query applesQuery = db_node.child("wishlist").orderByChild("prId").equalTo(prId);
                    applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                        @Override
                        public void onDataChange(DataSnapshot dataSnapshot) {
                            try {
                                HashMap<String, WishModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, WishModel>>() {});
                                List<WishModel> posts = new ArrayList<>(results.values());
                                for (WishModel post : posts) {
                                    if (post.getPrId().toString() == prId) {
                                        unfav.setVisibility(GONE);
                                        fav.setVisibility(VISIBLE);
                                    } else {
                                        unfav.setVisibility(VISIBLE);
                                        fav.setVisibility(GONE);
                                    }
//                                    unfav.setVisibility(VISIBLE);
//                                    fav.setVisibility(GONE);
                                }
                            } catch (Exception e) {
                                unfav.setVisibility(GONE);
                                fav.setVisibility(VISIBLE);
                            }
                        }

                        @Override
                        public void onCancelled(DatabaseError databaseError) {

                        }
                    });


//                    HashMap<String, CartModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, CartModel>>() {});
//                    List<CartModel> posts = new ArrayList<>(results.values());
//
//                    for (CartModel post : posts) {
//

//                    }

                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });



        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();

        if (Id.equals(uid)){
            deleteMyPr.setVisibility(VISIBLE);
        }

        deleteMyPr.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                okbtn.setText("Yes");
                cancelbtn.setText("Cancel");
                popupText.setText("Are You Sure You Want To Delete This Product");
                popupText.setTextSize(16);
                popupText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                popupText.setPadding(40, 10, 40, 10);
                popupText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                popupText.setTextColor(Color.parseColor("#000000"));
                layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
                layoutOfPopup.addView(popupText);
                layoutOfPopup.setBackgroundColor(Color.parseColor("#FFFFFF"));
                layoutOfPopup.setGravity(View.TEXT_ALIGNMENT_CENTER);
                popDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                                Query applesQuery = db_node.child("products").orderByChild("prId").equalTo(prId);

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {
                                    }
                                });
                                Toast.makeText(getApplicationContext(), "Your Product Deleted", Toast.LENGTH_SHORT).show();
                            }
                        }).setNegativeButton("Cancel",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.dismiss();
                            }});

//                popDialog.setIcon(android.R.drawable.);
                popDialog.setTitle("Delete! ");
                popDialog.setView(layoutOfPopup);
                popDialog.create();
                popDialog.show();

            }
        });

    }





    @Override
    public boolean onCreateOptionsMenu(Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.main, menu);
        final MenuItem item = menu.findItem(R.id.cart);
        MenuItemCompat.setActionView(item, R.layout.badge);
        RelativeLayout notifCount = (RelativeLayout)   MenuItemCompat.getActionView(item);

        tv = (TextView) notifCount.findViewById(R.id.txtCount);
        imgCart = (ImageView)notifCount.findViewById(R.id.imgCart);


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
                finish();
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
            Intent intent = new Intent(this, Cart.class);
            startActivity(intent);
            finish();
            return true;
        }
        if (id == R.id.search_ac) {
            Intent intent = new Intent(this, Search.class);
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


}
