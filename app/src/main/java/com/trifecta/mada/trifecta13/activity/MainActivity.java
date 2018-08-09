package com.trifecta.mada.trifecta13.activity;

import android.content.Intent;
import android.os.AsyncTask;
import android.os.Bundle;
import android.os.Handler;
import android.support.design.widget.NavigationView;
import android.support.v4.app.Fragment;
import android.support.v4.app.FragmentTransaction;
import android.support.v4.view.GravityCompat;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.support.v7.widget.Toolbar;
import android.util.Log;
import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.facebook.login.LoginManager;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.iid.FirebaseInstanceId;
import com.trifecta.mada.trifecta13.R;
import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.trifecta.mada.trifecta13.fragment.Explore;
import com.trifecta.mada.trifecta13.fragment.Inbox;
import com.trifecta.mada.trifecta13.fragment.MywishList;
import com.trifecta.mada.trifecta13.fragment.Orders;
import com.trifecta.mada.trifecta13.fragment.Profile;
import com.trifecta.mada.trifecta13.fragment.Store;
import com.trifecta.mada.trifecta13.other.CircleTransform;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.OrderModel;
import com.trifecta.mada.trifecta13.other.PrefManager;
import com.trifecta.mada.trifecta13.other.ProductModel;
import com.trifecta.mada.trifecta13.other.StoreModel;
import com.trifecta.mada.trifecta13.other.User;

import org.json.JSONObject;

import java.io.OutputStreamWriter;
import java.net.HttpURLConnection;
import java.net.URL;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Locale;

public class MainActivity extends AppCompatActivity {

    private NavigationView navigationView;
    private DrawerLayout drawer;
    private View navHeader;
    private ImageView  imgProfile;
    private TextView txtName;
    private Toolbar toolbar;
    public static  int count=0;
    // urls to load navigation header background image
    // and profile image
    private static final String urlProfileImg = "https://www.techinasia.com/assets/images/profile/icon-defaultprofile.png";

    // index to identify current nav menu item
    public static int navItemIndex = 0;

    // tags used to attach the fragments
    private static final String TAG_HOME = "Trifecta";
    private static final String TAG_INBOX = "inbox";
    private static final String TAG_STORE = "store";
    private static final String TAG_ORDERS = "orders";
    private static final String TAG_PROFILE = "profile";
    private static final String TAG_WISHLIST = "my wish list";
    private static final String TAG_CONTACTUS = "contact us";
    public static String CURRENT_TAG = TAG_HOME;

    // toolbar titles respected to selected nav menu item
    private String[] activityTitles;

    // flag to load home fragment when user presses back key
    private boolean shouldLoadHomeFragOnBackPress = true;
    private Handler mHandler;

    private Firebase myFirebaseRef,myFirebaseRef2;
    private FirebaseAuth mAuth;
    // To hold Facebook profile picture
    private ImageView profilePicture;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        myFirebaseRef = new Firebase("");
        myFirebaseRef2 = new Firebase("");
        mAuth = FirebaseAuth.getInstance();

        Locale.setDefault(new Locale("en", "US"));
        mHandler = new Handler();

        drawer = (DrawerLayout) findViewById(R.id.drawer_layout);
        navigationView = (NavigationView) findViewById(R.id.nav_view);


       //-------


        // Navigation view header
        navHeader = navigationView.getHeaderView(0);
        txtName = (TextView) navHeader.findViewById(R.id.name);
        imgProfile = (ImageView) navHeader.findViewById(R.id.img_profile);

        // load toolbar titles from string resources
        activityTitles = getResources().getStringArray(R.array.nav_item_activity_titles);

        // load nav menu header data
        loadNavHeader();

        // initializing navigation menu
        setUpNavigationView();

        if (savedInstanceState == null) {
            navItemIndex = 0;
            CURRENT_TAG = TAG_HOME;
            loadHomeFragment();
        }


        imgProfile.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                navItemIndex = 5;
                CURRENT_TAG = TAG_PROFILE;
                loadHomeFragment();
            }
        });


        final String uid = mAuth.getCurrentUser().getUid();
        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query1 = database.getReference("orders").orderByChild("sellerId").equalTo(uid);
        query1.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, OrderModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                    });
                    List<OrderModel> posts = new ArrayList<>(results.values());
                    for (OrderModel orderModel:posts){
                        if(!orderModel.getSellerConfirm()){
                            new RetrieveFeedTask().execute(dataSnapshot.getChildrenCount());
                        }
                    }

                } catch (Exception e) {

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });


        Query query2 = database.getReference("orders").orderByChild("notifyOrder").equalTo(true);
        query2.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, OrderModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                    });
                    List<OrderModel> posts = new ArrayList<>(results.values());
                    for (OrderModel orderModel:posts){
                        if (orderModel.getBuyerId().equals(uid)&&orderModel.getSellerConfirm()==true&&orderModel.isNotifyOrder()==true){
                            com.google.firebase.database.DataSnapshot nodeDataSnapshot = dataSnapshot.getChildren().iterator().next();
                            String key = nodeDataSnapshot.getKey();
                            orderModel.setNotifyOrder(false);
                            String path = "/" + dataSnapshot.getKey() + "/" + key;
                            HashMap<String, Object> result = new HashMap<>();
                            result.put("notifyOrder",false);
                            database.getReference(path).updateChildren(result);
                            count++;
                            new RetrieveFeedTask().execute(Long.valueOf(0));
                        }
                    }
                } catch (Exception e) {

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    protected void onStart() {
        super.onStart();
        final String uid = mAuth.getCurrentUser().getUid();
        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query3 = database.getReference("stores").orderByChild("storeId").equalTo(uid);
        query3.addValueEventListener(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot dataSnapshot) {
                try {
                    HashMap<String, StoreModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {
                    });
                    List<StoreModel> posts = new ArrayList<>(results.values());
                    for (StoreModel storeModel:posts){
                        if(Integer.parseInt(storeModel.getBallance())<-100){
                            myFirebaseRef2.child("stores/"+uid+"/storeState").setValue(false);

                        }else {
                            myFirebaseRef2.child("stores/"+uid+"/storeState").setValue(true);
                        }
                    }

                } catch (Exception e) {
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

    }


    @Override
    public void onStop() {
        super.onStop();

    }



    /***
     * Load navigation menu header information
     * like background image, profile image
     * name, website, notifications action view (dot)
     */
    private void loadNavHeader() {

        final String uid = mAuth.getCurrentUser().getUid();
        myFirebaseRef.child(uid).child("name").addValueEventListener(new ValueEventListener() {

            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                try{
                    String data = dataSnapshot.getValue(String.class);
                    txtName.setText(data);
                }catch (Exception e){}


            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query2 = database.getReference("users").orderByChild("id").equalTo(uid);
        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once

                HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});

                List<User> posts = new ArrayList<>(results.values());

                for (User post : posts) {
                    Glide.with(getApplication()).load(post.getProfilePic())
                            .crossFade()
                            .thumbnail(0.5f)
                            .bitmapTransform(new CircleTransform(getApplicationContext()))
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .into(imgProfile);


                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });
        // Loading profile image
        Glide.with(this).load(urlProfileImg)
                .crossFade()
                .thumbnail(0.5f)
                .bitmapTransform(new CircleTransform(this))
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .into(imgProfile);

        // showing dot next to notifications label
        navigationView.getMenu().getItem(3).setActionView(R.layout.menu_dot);
    }

    /***
     * Returns respected fragment that user
     * selected from navigation menu
     */
    private void loadHomeFragment() {
        // selecting appropriate nav menu item
        selectNavMenu();

        // set toolbar title
        setToolbarTitle();

        // if user select the current navigation menu again, don't do anything
        // just close the navigation drawer
        if (getSupportFragmentManager().findFragmentByTag(CURRENT_TAG) != null) {
            drawer.closeDrawers();

            // show or hide the fab button
            return;
        }

        // Sometimes, when fragment has huge data, screen seems hanging
        // when switching between navigation menus
        // So using runnable, the fragment is loaded with cross fade effect
        // This effect can be seen in GMail app
        Runnable mPendingRunnable = new Runnable() {
            @Override
            public void run() {
                // update the main content by replacing fragments
                Fragment fragment = getHomeFragment();
                FragmentTransaction fragmentTransaction = getSupportFragmentManager().beginTransaction();
                fragmentTransaction.setCustomAnimations(android.R.anim.fade_in,
                        android.R.anim.fade_out);
                fragmentTransaction.replace(R.id.frame, fragment, CURRENT_TAG);
                fragmentTransaction.commitAllowingStateLoss();
            }
        };

        // If mPendingRunnable is not null, then add to the message queue
        if (mPendingRunnable != null) {
            mHandler.post(mPendingRunnable);
        }

        //Closing drawer on item click
        drawer.closeDrawers();

        // refresh toolbar menu
        invalidateOptionsMenu();
    }

    private Fragment getHomeFragment() {
        switch (navItemIndex) {
            case 0:
                // home
                Explore explore = new Explore();
                return explore;
            case 1:
                // inbox
                Inbox inbox = new Inbox();
                return inbox;
            case 2:
                // store
                Store store = new Store();
                return store;

            case 3:
                // orders
                Orders orders = new Orders();
                return orders;
            case 4:
                // mywishList
                MywishList mywishList = new MywishList();
                return mywishList;
            case 5:
                // profile
                com.trifecta.mada.trifecta13.fragment.Profile profile = new Profile();
                return profile;



            default:
                return new Explore();
        }
    }

    private void setToolbarTitle() {
        getSupportActionBar().setTitle(activityTitles[navItemIndex]);
    }

    private void selectNavMenu() {
        navigationView.getMenu().getItem(navItemIndex).setChecked(true);
    }

    private void setUpNavigationView() {
        //Profile Navigation View Item Selected Listener to handle the item click of the navigation menu
        navigationView.setNavigationItemSelectedListener(new NavigationView.OnNavigationItemSelectedListener() {

            // This method will trigger on item Click of navigation menu
            @Override
            public boolean onNavigationItemSelected(MenuItem menuItem) {

                //Check to see which item was being clicked and perform appropriate action
                switch (menuItem.getItemId()) {
                    //Replacing the main content with ContentFragment Which is our Inbox View;
                    case R.id.nav_home:
                        navItemIndex = 0;
                        CURRENT_TAG = TAG_HOME;
                        break;
                    case R.id.nav_inbox:
                        navItemIndex = 1;
                        CURRENT_TAG = TAG_INBOX;
                        break;
                    case R.id.nav_store:
                        navItemIndex = 2;
                        CURRENT_TAG = TAG_STORE;
                        String uid = mAuth.getCurrentUser().getUid();
                        FirebaseDatabase database = FirebaseDatabase.getInstance();
                        Query query1 = database.getReference("users").orderByChild("id").equalTo(uid);
                        query1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                            @Override
                            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                                // do some stuff once
                                HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});

                                List<User> posts = new ArrayList<>(results.values());

                                for (User post : posts) {
                                    if (post.getStore() != true) {
                                        Intent intent = new Intent(getApplicationContext(), MyStore.class);
                                        startActivity(intent);


                                    } else {
                                        navItemIndex = 2;
                                        CURRENT_TAG = TAG_STORE;
                                    }

                                }

                            }
                            @Override
                            public void onCancelled(DatabaseError databaseError) {

                            }
                        });


                        break;
                    case R.id.nav_orders:
                        navItemIndex = 3;
                        CURRENT_TAG = TAG_ORDERS;
                        break;
                    case R.id.nav_wishlist:
                        navItemIndex = 4;
                        CURRENT_TAG = TAG_WISHLIST;
                        break;
                    case R.id.nav_profile:
                        navItemIndex = 5;
                        CURRENT_TAG = TAG_PROFILE;
                        break;
                    case R.id.nav_about_us:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, AboutUs.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_contact:
                        // launch new intent instead of loading fragment
                        startActivity(new Intent(MainActivity.this, ContactUs.class));
                        drawer.closeDrawers();
                        return true;

                    case R.id.nav_logout:
                        mAuth.signOut();
                        Intent intent = new Intent(getApplicationContext(), Login.class);
                        intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                        startActivity(intent);
                        finish();
                        LoginManager.getInstance().logOut();
                        drawer.closeDrawers();
                        return true;

                    default:
                        navItemIndex = 0;
                }

                //Checking if the item is in checked state or not, if not make it in checked state
                if (menuItem.isChecked()) {
                    menuItem.setChecked(false);
                } else {
                    menuItem.setChecked(true);
                }
                menuItem.setChecked(true);

                loadHomeFragment();

                return true;
            }
        });


        ActionBarDrawerToggle actionBarDrawerToggle = new ActionBarDrawerToggle(this, drawer, toolbar, R.string.openDrawer, R.string.closeDrawer) {

            @Override
            public void onDrawerClosed(View drawerView) {
                // Code here will be triggered once the drawer closes as we dont want anything to happen so we leave this blank
                super.onDrawerClosed(drawerView);
            }

            @Override
            public void onDrawerOpened(View drawerView) {
                // Code here will be triggered once the drawer open as we dont want anything to happen so we leave this blank
                super.onDrawerOpened(drawerView);
            }
        };

        //Profile the actionbarToggle to drawer layout
        drawer.setDrawerListener(actionBarDrawerToggle);

        //calling sync state is necessary or else your hamburger icon wont show up
        actionBarDrawerToggle.syncState();
    }

    @Override
    public void onBackPressed() {
        if (drawer.isDrawerOpen(GravityCompat.START)) {
            drawer.closeDrawers();
            return;
        }

        // This code loads home fragment when back key is pressed
        // when user is in other fragment than home
        if (shouldLoadHomeFragOnBackPress) {
            // checking if user is on other navigation menu
            // rather than home
            if (navItemIndex != 0) {
                navItemIndex = 0;
                CURRENT_TAG = TAG_HOME;
                loadHomeFragment();
                return;
            }
            if(navItemIndex == 0){
                finish();
            }
        }

        super.onBackPressed();
    }


    // Method to send Notifications from server to client end.
    //AIzaSyBhbAKYhSLNPutu2ofIA2v5u7qK6_ksX-U
    public final static String AUTH_KEY_FCM = "AIzaSyD3MDAd7QN9WGaM1x4NQe8bA9OpIlyhIYo";
    public final static String API_URL_FCM = "https://fcm.googleapis.com/fcm/send";
    // userDeviceIdKey is the device id you will query from your database
    public static void pushFCMNotification(String userDeviceIdKey,long s)
    {
        try {
            String authKey = AUTH_KEY_FCM;   // You FCM AUTH key
            String FMCurl = API_URL_FCM;

            URL url = new URL(FMCurl);
            HttpURLConnection conn = (HttpURLConnection) url.openConnection();

            conn.setUseCaches(false);
            conn.setDoInput(true);
            conn.setDoOutput(true);

            conn.setRequestMethod("POST");
            conn.setRequestProperty("Authorization","key="+authKey);
            conn.setRequestProperty("Content-Type","application/json");

            JSONObject json = new JSONObject();
            json.put("to",userDeviceIdKey.trim());
            JSONObject info = new JSONObject();
            info.put("title", "TIRFECTA");   // Notification title
            if (s!=0){
                info.put("body", "You have "+s+" new orders"); // Notification body
            }else {
                info.put("body", "The Seller Accepted Your Order"); // Notification body

            }
            json.put("notification", info);

            OutputStreamWriter wr = new OutputStreamWriter(conn.getOutputStream());
            wr.write(json.toString());
            wr.flush();
            int responseCode = conn.getResponseCode();
            String respMsg = conn.getResponseMessage();
            String test = "";
        }
        catch (Exception ex)
        {
//            Log.d("errors", "pushFCMNotification: " + ex.getMessage());
//            Log.e("errors", ex.getMessage());
        }

    }


   private class RetrieveFeedTask extends AsyncTask<Long, Void, Void> {



       @Override
        protected Void doInBackground(Long... params) {


           String token = FirebaseInstanceId.getInstance().getToken().toString();
           if (params[0]!=0){
               pushFCMNotification(token,params[0]);
           }else {
               pushFCMNotification(token,0);
           }
            return null;

        }

        @Override
        protected void onPostExecute(Void aVoid) {
            super.onPostExecute(aVoid);
        }

        @Override
        protected void onPreExecute() {
            super.onPreExecute();
        }
    }









}
