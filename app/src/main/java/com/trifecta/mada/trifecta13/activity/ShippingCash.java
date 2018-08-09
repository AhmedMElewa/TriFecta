package com.trifecta.mada.trifecta13.activity;

import android.app.Activity;
import android.app.ProgressDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.location.Address;
import android.location.Geocoder;
import android.os.AsyncTask;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.common.GooglePlayServicesNotAvailableException;
import com.google.android.gms.common.GooglePlayServicesRepairableException;
import com.google.android.gms.location.places.Place;
import com.google.android.gms.location.places.ui.PlacePicker;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.OrderModel;
import com.trifecta.mada.trifecta13.other.TrackGPS;
import com.trifecta.mada.trifecta13.other.User;

import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;
import java.util.Random;

import com.trifecta.mada.trifecta13.util.IabHelper;
import com.trifecta.mada.trifecta13.util.IabResult;
import com.trifecta.mada.trifecta13.util.Inventory;
import com.trifecta.mada.trifecta13.util.Purchase;

public class ShippingCash extends AppCompatActivity {
    String x="";
    private String prId;
    private String buyerId;
    private String sellerId;
    private String Commission;
    private String paymentMethod;
    private String orDate;
    private Boolean buyerConfirm;
    private Boolean sellerConfirm;
    private Boolean paid;
    private ProgressDialog mProgressDialog;

    private String prQuantity;
    private String prPrice;
    private String prPic;
    private String prName;
    private String prShipping;

    private EditText buyerName;
    private EditText buyerStreet;
    //    private EditText buyerHomeNum;
    private Spinner buyerCity;
    private EditText buyerPhone;
    private EditText buyerNote;

    private TextView popupText;
    private Button okbtn;
    private Button cancelbtn;
    private LinearLayout layoutOfPopup;
    public AlertDialog.Builder popDialog;


    //gps location
    private Button b_get;
    private TrackGPS gps;
    double longitude;
    double latitude;

    private Button btnOrder;
    private static final String TAG = "InAppBilling";
    IabHelper mHelper;
    private String ITEM_SKU ;


    int PLACE_PICKER_REQUEST = 1;
    final Context context = this;

    private FirebaseDatabase mRef = FirebaseDatabase.getInstance();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shipping_cash);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Shipping");

        buyerName = (EditText) findViewById(R.id.order_create_name);
        buyerStreet = (EditText) findViewById(R.id.order_create_street);
//        buyerHomeNum=(EditText)findViewById(R.id.order_create_houseNumber);
        buyerCity = (Spinner) findViewById(R.id.order_create_city);
        buyerPhone = (EditText) findViewById(R.id.order_create_phone);
        buyerNote = (EditText) findViewById(R.id.order_create_note);
        btnOrder = (Button) findViewById(R.id.order_create_finish);

        popupText = new TextView(getApplicationContext());
        okbtn = new Button(getApplicationContext());
        cancelbtn = new Button(getApplicationContext());
        layoutOfPopup = new LinearLayout(getApplicationContext());
        popDialog = new AlertDialog.Builder(this);

        //get location
        b_get = (Button) findViewById(R.id.get);
        ITEM_SKU = getIntent().getStringExtra("prId");
        String base64EncodedPublicKey =
                "MIIBIjANBgkqhkiG9w0BAQEFAAOCAQ8AMIIBCgKCAQEAtqs7g+OHNqCqWXFOJ1149dtqkVLLI8FTdEfO19ghU8ESuD6DZkaZcNltCNoCCWn2EoZuuYQQ6yv2F16gBrP6OCLJ6TaP4ztKglOinb/L0Acv4va52qWJri0O9ok2T+eHdhy5rX5FtQM92MTkHAxwIgCjOPAjhOvk7Hij9swNSlPTMBMf0DIOc3+RGhEvgPVPJrIKkTa3QfEJQvGjfxJdkgAU5mLAMGQK7qcoqDcM7YuUopq+7lk4m1XJGrj3oMeODzH01zev1JmUZkXp0A8z+LSL7ZnUC2nlpOy2D5yAK6neP9U2SqTSZpiY6kP/ndI9HqNKN6GxsDD4RJuWqddxBwIDAQAB";

        mHelper = new IabHelper(this, base64EncodedPublicKey);
        mHelper.startSetup(new
                                   IabHelper.OnIabSetupFinishedListener() {
                                       public void onIabSetupFinished(IabResult result) {
                                           if (!result.isSuccess()) {
                                               //Log.d(TAG, "In-app Billing setup failed: " +
                                                //       result);
                                           } else {
                                               //Log.d(TAG, "In-app Billing is set up OK");
                                           }
                                       }
                                   });


        btnOrder.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }
                paymentMethod = getIntent().getStringExtra("paymentMethod");
                if (paymentMethod.equals("Credit")) {
                    Random Rand = new Random();
                    int Rndnum = Rand.nextInt(10000) + 1;
                    mHelper.launchPurchaseFlow(ShippingCash.this, ITEM_SKU, 10001,
                            mPurchaseFinishedListener, "token-" + Rndnum);
                } else {
                    okbtn.setText("Ok");
//                    cancelbtn.setText("Cancel");
                    popupText.setText("Your order has sent to the seller.He/She will contact with you soon");
                    popupText.setTextSize(16);
                    popupText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                    popupText.setPadding(40, 10, 40, 10);
                    popupText.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    popupText.setTextColor(Color.parseColor("#000000"));
                    layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
                    layoutOfPopup.addView(popupText);
                    layoutOfPopup.setBackgroundColor(Color.parseColor("#FFFFFF"));
                    layoutOfPopup.setGravity(View.TEXT_ALIGNMENT_CENTER);

                    popDialog.setPositiveButton("Ok",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int which) {
                                    setUpOrder();
                                    Intent intent = new Intent(getApplicationContext(), Cart.class);
                                    startActivity(intent);
                                    Toast.makeText(getApplicationContext(), "Your Order has Sent", Toast.LENGTH_SHORT).show();
                                    finish();
                                }
                            }).setNegativeButton("Cancel",
                            new DialogInterface.OnClickListener() {
                                public void onClick(DialogInterface dialog, int id) {
//                                dialog.dismiss();
                                    Intent intent = new Intent(getApplicationContext(), Cart.class);
                                    startActivity(intent);
                                    finish();
                                }
                            });
                    ;

//                popDialog.setIcon(android.R.drawable.);
                    popDialog.setTitle("Thank You! ");
                    popDialog.setView(layoutOfPopup);
                    popDialog.create();
                    popDialog.show();

                }


//                Toast.makeText(ShippingCash.this, "Your order has sent to the seller.He/She will contact with you!",
//                        Toast.LENGTH_SHORT).show();
            }
        });


        FirebaseAuth mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();

        Query query1 = database.getReference("users").orderByChild("id").equalTo(uid);
        query1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once
                try {
                    HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {
                    });
                    List<User> posts = new ArrayList<>(results.values());
                    for (User post : posts) {
                        buyerName.setText(post.getName());
                        buyerStreet.setText(post.getAddress());
                        buyerPhone.setText(post.getPhoneNumber());
                    }
                } catch (Exception e) {
                }
            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        b_get.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                goToLocation();
            }
        });

    }

    public void goToLocation() {

        PlacePicker.IntentBuilder builder = new
                PlacePicker.IntentBuilder();

        try {
            startActivityForResult(builder.build(this), PLACE_PICKER_REQUEST);
        } catch (GooglePlayServicesRepairableException e) {
            e.printStackTrace();
        } catch (GooglePlayServicesNotAvailableException e) {
            e.printStackTrace();
        }

    }

    @Override
    protected void onActivityResult(int requestCode, int resultCode,
                                    Intent data) {
        if (!mHelper.handleActivityResult(requestCode,
                resultCode, data)) {
            super.onActivityResult(requestCode, resultCode, data);
        }

        if (requestCode == PLACE_PICKER_REQUEST) {
            if (resultCode == RESULT_OK) {
                Place place = PlacePicker.getPlace(data, this);
                String toastMsg = String.format("%s"+"\n", place.getAddress());
                //Toast.makeText(this, toastMsg, Toast.LENGTH_LONG).show();
                buyerStreet.setText(toastMsg);


            }
        }


    }

    IabHelper.OnIabPurchaseFinishedListener mPurchaseFinishedListener
            = new IabHelper.OnIabPurchaseFinishedListener() {
        public void onIabPurchaseFinished(IabResult result,
                                          Purchase purchase) {
            if (result.isFailure()) {
                // Handle error
                return;
            } else if (purchase.getSku().equals(ITEM_SKU)) {
                consumeItem();
            }

        }
    };

    public void consumeItem() {
        mHelper.queryInventoryAsync(mReceivedInventoryListener);
    }

    IabHelper.QueryInventoryFinishedListener mReceivedInventoryListener
            = new IabHelper.QueryInventoryFinishedListener() {
        public void onQueryInventoryFinished(IabResult result,
                                             Inventory inventory) {

            if (result.isFailure()) {
                // Handle failure
            } else {
                mHelper.consumeAsync(inventory.getPurchase(ITEM_SKU),
                        mConsumeFinishedListener);
            }
        }
    };

    IabHelper.OnConsumeFinishedListener mConsumeFinishedListener =
            new IabHelper.OnConsumeFinishedListener() {
                public void onConsumeFinished(Purchase purchase,
                                              IabResult result) {

                    if (result.isSuccess()) {

                        okbtn.setText("Ok");
//                        cancelbtn.setText("Cancel");
                        popupText.setText("Your order has sent to the seller.He/She will contact with you soon");
                        popupText.setTextSize(16);
                        popupText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        popupText.setPadding(40, 10, 40, 10);
                        layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
                        layoutOfPopup.addView(popupText);
//                layoutOfPopup.setBackgroundColor(Color.parseColor("#FFFFFF"));
                        layoutOfPopup.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        popDialog.setPositiveButton("Ok",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        setUpOrder();
                                        Intent intent = new Intent(getApplicationContext(), Cart.class);
                                        startActivity(intent);
                                        Toast.makeText(getApplicationContext(), "Your Order has Sent", Toast.LENGTH_SHORT).show();
                                        finish();
                                    }
                                });

//                popDialog.setIcon(android.R.drawable.);
                        popDialog.setTitle("Thank You! ");
                        popDialog.setView(layoutOfPopup);
                        popDialog.create();
                        popDialog.show();

//                        Toast.makeText(ShippingCash.this, "Worked",
//                                Toast.LENGTH_SHORT).show();

                    } else {
                        // handle error
                    }
                }
            };

    @Override
    public void onDestroy() {
        super.onDestroy();
        if (mHelper != null) mHelper.dispose();
        mHelper = null;
    }

    private class RetrieveAdress extends AsyncTask<String, String, String> {
        @Override
        protected String doInBackground(String... params) {
            String strAdd = "";
            try {
                Geocoder geocoder;
                List<Address> addresses;
                geocoder = new Geocoder(ShippingCash.this, Locale.getDefault());

                addresses = geocoder.getFromLocation(latitude, longitude, 1); // Here 1 represent max location result to returned, by documents it recommended 1 to 5

//                String address = addresses.get(0).getAddressLine(0); // If any additional address line present than only, check with max available address lines by getMaxAddressLineIndex()
//                String addressline = addresses.get(0).getAddressLine(0);

                x+=addresses.get(0).getAddressLine(0)+",";
                x+=addresses.get(0).getAddressLine(1)+",";
                x+=addresses.get(0).getAddressLine(2)+",";
                x+=addresses.get(0).getAddressLine(3)+",";
                x+=addresses.get(0).getAddressLine(4)+".";
//                String city = addresses.get(0).getLocality();
//                String state = addresses.get(0).getAdminArea();
//                String country = addresses.get(0).getCountryName();
//                String postalCode = addresses.get(0).getPostalCode();
//                String knownName = addresses.get(0).getFeatureName();
            } catch (Exception e) {
                e.printStackTrace();
                Log.w("My Current ", "Canont get Address!");
            }
            return x;
        }

        @Override
        protected void onPostExecute(String aVoid) {
            super.onPostExecute(aVoid);
            buyerStreet.setText(x);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }


//    private String getCompleteAddressString(double LATITUDE, double LONGITUDE) {
//
//    }

    protected void setUpOrder() {

        prId = getIntent().getStringExtra("prId");
        buyerId = getIntent().getStringExtra("buyer");
        sellerId = getIntent().getStringExtra("seller");
        prQuantity = getIntent().getStringExtra("prQuantity");
        prPrice = getIntent().getStringExtra("prPrice");
        prPic = getIntent().getStringExtra("prPic");
        prName = getIntent().getStringExtra("prName");
        prShipping = getIntent().getStringExtra("prShipping");
        Calendar c = Calendar.getInstance();
        System.out.println("Current time => " + c.getTime());
        SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
        orDate = df.format(c.getTime());
        paymentMethod=getIntent().getStringExtra("paymentMethod");
        buyerConfirm=false;
        sellerConfirm=false;
        paid=false;





        OrderModel orderModel= new OrderModel();
        DatabaseReference orders = mRef.getReference("orders");
        orderModel.setPushId(prId+buyerId);
        orderModel.setPrId(prId);
        orderModel.setBuyerId(buyerId);
        orderModel.setSellerId(sellerId);
        orderModel.setPrName(prName);
        orderModel.setShipping(prShipping);
        orderModel.setPrPic(prPic);
        orderModel.setPrPrice(prPrice);
        orderModel.setPrQuantity(prQuantity);
        orderModel.setOrDate(orDate);
        orderModel.setPaymentMethod(paymentMethod);
        orderModel.setBuyerConfirm(buyerConfirm);
        orderModel.setPaid(paid);
        orderModel.setSellerConfirm(sellerConfirm);
        orderModel.setBuyerName(buyerName.getText().toString());
        orderModel.setBuyerStreet(buyerStreet.getText().toString());
        orderModel.setNotifyOrder(false);
//        orderModel.setBuyerHomeNum(buyerHomeNum.getText().toString());
        orderModel.setBuyerCity(buyerCity.getSelectedItem().toString());
        orderModel.setBuyerPhone(buyerPhone.getText().toString());
        orderModel.setBuyerNote(buyerNote.getText().toString());
        if (Integer.parseInt(prPrice)*Integer.parseInt(prQuantity)<60){
            Commission= String.valueOf(3);
            orderModel.setCommission(Commission);
        }else {
            Commission= String.valueOf(((Integer.parseInt(prPrice)*Integer.parseInt(prQuantity))*10)/100);
            orderModel.setCommission(Commission);
        }
        orders.push().setValue(orderModel);


        DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
        Query applesQuery = db_node.child("carts").orderByChild("pushId").equalTo(prId);

        applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                    appleSnapshot.getRef().removeValue();
                }

            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

    }



    private boolean validateForm() {
        boolean valid = true;

        String name = buyerName.getText().toString();
        if (TextUtils
                .isEmpty(name)) {
            buyerName.setError("Required.");
            valid = false;
        } else {
            buyerName.setError(null);
        }

        String street = buyerStreet.getText().toString();
        if (TextUtils
                .isEmpty(street)) {
            buyerStreet.setError("Required.");
            valid = false;
        } else {
            buyerStreet.setError(null);
        }

//        String HomeNum = buyerHomeNum.getText().toString();
//        if (TextUtils
//                .isEmpty(HomeNum)) {
//            buyerHomeNum.setError("Required.");
//            valid = false;
//        } else {
//            buyerHomeNum.setError(null);
//        }

        String City = buyerCity.getSelectedItem().toString();
        if (TextUtils
                .isEmpty(City)) {
            Toast.makeText(ShippingCash.this, "Select your City",
                    Toast.LENGTH_SHORT).show();
            valid = false;
        }


//        if (TextUtils
//                .isEmpty(Phone)) {
//            buyerPhone.setError("Required.");
//            valid = false;
//        } else {
//            buyerPhone.setError(null);
//        }
        String Phone = buyerPhone.getText().toString();
        if (Phone.isEmpty() || Phone.length() < 11 || Phone.length() > 11) {
            buyerPhone.setError("enter 11 number correct");
            valid = false;
        } else {
            buyerPhone.setError(null);
        }

        return valid;
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
