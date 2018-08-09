package com.trifecta.mada.trifecta13.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.net.wifi.WifiManager;
import android.os.Build;
import android.support.annotation.NonNull;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.widget.Toolbar;
import android.text.TextUtils;
import android.text.format.Formatter;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.TextView;
import android.widget.Toast;
import com.facebook.AccessToken;
import com.facebook.CallbackManager;
import com.facebook.FacebookCallback;
import com.facebook.FacebookException;
import com.facebook.FacebookSdk;
import com.facebook.login.LoginResult;
import com.facebook.login.widget.LoginButton;
import com.firebase.client.Firebase;
import com.google.android.gms.auth.api.signin.GoogleSignInAccount;
import com.google.android.gms.auth.api.signin.GoogleSignInOptions;
import com.google.android.gms.common.ConnectionResult;
import com.google.android.gms.common.api.GoogleApiClient;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.AuthCredential;
import com.google.firebase.auth.AuthResult;
import com.google.firebase.auth.FacebookAuthProvider;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.auth.GoogleAuthProvider;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.ProductModel;
import com.trifecta.mada.trifecta13.other.User;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;

import com.google.android.gms.auth.api.Auth;
import com.google.android.gms.auth.api.signin.GoogleSignInResult;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Login extends AppCompatActivity {

    private static final String TAG = "TriFecta";
    public User user;
    private EditText email;
    private EditText password;
    private FirebaseAuth mAuth;
    private FirebaseAuth.AuthStateListener mAuthListener;
    private ProgressDialog mProgressDialog;
    TextView button_sign_in;
    TextView button_sign_up;
    private static final int RC_SIGN_IN = 0 ;
    private com.google.android.gms.common.SignInButton signInButton;
    private GoogleApiClient mGoogleApiClient;
    //Add YOUR Firebase Reference URL instead of the following URL
    Firebase mRef=new Firebase("");

    //FaceBook callbackManager
    private CallbackManager callbackManager;
    //

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        Toolbar toolbar = (Toolbar) findViewById(R.id.toolbar);
        setSupportActionBar(toolbar);

        signInButton = (com.google.android.gms.common.SignInButton)findViewById(R.id.sign_in_button);

        GoogleSignInOptions gso = new GoogleSignInOptions.Builder(GoogleSignInOptions.DEFAULT_SIGN_IN)
                .requestIdToken(getString(R.string.default_web_client_id))
                .requestEmail()
                .build();

        mGoogleApiClient = new GoogleApiClient.Builder(this)
                .enableAutoManage(this , new GoogleApiClient.OnConnectionFailedListener() {
                    @Override
                    public void onConnectionFailed(@NonNull ConnectionResult connectionResult) {

                    }
                } /* OnConnectionFailedListener */)
                .addApi(Auth.GOOGLE_SIGN_IN_API, gso)
                .build();



        signInButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (Build.VERSION.SDK_INT >= Build.VERSION_CODES.GINGERBREAD) {
                    // Make the call to GoogleApiClient
                    signIn();
                }
            }
        });


        mAuth = FirebaseAuth.getInstance();

        FirebaseUser mUser = mAuth.getCurrentUser();
        if (mUser != null) {
            // User is signed in

//            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
            String uid = mAuth.getCurrentUser().getUid();
            Intent i = new Intent(this, MainActivity.class);
            i.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
            i.putExtra("user_id", uid);
            startActivity(i);
            finish();

//            String image=mAuth.getCurrentUser().getPhotoUrl().toString();
//            intent.putExtra("user_id", uid);
//            if(image!=null || image!=""){
//                intent.putExtra("profile_picture",image);
//            }
//            startActivity(intent);
//            finish();
            //Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
        }

        mAuthListener = new FirebaseAuth.AuthStateListener() {
            @Override
            public void onAuthStateChanged(@NonNull FirebaseAuth firebaseAuth) {
                FirebaseUser mUser = firebaseAuth.getCurrentUser();
                if (mUser != null) {
                    // User is signed in
                   // Log.d(TAG, "onAuthStateChanged:signed_in:" + mUser.getUid());
                } else {
                    // User is signed out
                   // Log.d(TAG, "onAuthStateChanged:signed_out");
                }

            }
        };



        //FaceBook
        FacebookSdk.sdkInitialize(getApplicationContext());
        callbackManager = CallbackManager.Factory.create();
        LoginButton loginButton = (LoginButton) findViewById(R.id.button_facebook_login);
        loginButton.setReadPermissions("email", "public_profile");
        loginButton.registerCallback(callbackManager, new FacebookCallback<LoginResult>() {
            @Override
            public void onSuccess(LoginResult loginResult) {
                //Log.d(TAG, "facebook:onSuccess:" + loginResult);
                signInWithFacebook(loginResult.getAccessToken());
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                String uid = mAuth.getCurrentUser().getUid();
//                intent.putExtra("user_id", uid);
//                startActivity(intent);
//                finish();
            }

            @Override
            public void onCancel() {
                //Log.d(TAG, "facebook:onCancel");
            }

            @Override
            public void onError(FacebookException error) {

                //Log.d(TAG, "facebook:onError", error);
            }
        });
        //

    }

    private void signIn() {
        Intent signInIntent = Auth.GoogleSignInApi.getSignInIntent(mGoogleApiClient);
        startActivityForResult(signInIntent, RC_SIGN_IN);
    }



    @Override
    protected void onStart() {
        super.onStart();
        email = (EditText) findViewById(R.id.edit_text_email_id);
        password = (EditText) findViewById(R.id.edit_text_password);
        button_sign_in=(TextView) findViewById(R.id.button_sign_in);
        button_sign_up =(TextView) findViewById(R.id.button_sign_up);

        button_sign_in.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                setUpUser();
                signIn(email.getText().toString(), password.getText().toString());
            }
        });

        button_sign_up.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getApplicationContext(), SignUp.class);
                startActivity(intent);
                finish();
            }
        });
        mAuth.addAuthStateListener(mAuthListener);
    }

    @Override
    public void onStop() {
        super.onStop();
        if (mAuthListener != null) {
            mAuth.removeAuthStateListener(mAuthListener);
        }
    }


    //FaceBook
    @Override
    protected void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        callbackManager.onActivityResult(requestCode, resultCode, data);
        // Result returned from launching the Intent from GoogleSignInApi.getSignInIntent(...);
        if (requestCode == RC_SIGN_IN) {
            GoogleSignInResult result = Auth.GoogleSignInApi.getSignInResultFromIntent(data);
            int statusCode = result.getStatus().getStatusCode();
            if (result.isSuccess()) {
                // Google Sign In was successful, authenticate with Firebase
                GoogleSignInAccount account = result.getSignInAccount();
                firebaseAuthWithGoogle(account);

            } else {
                // Google Sign In failed, update UI appropriately
                // ...
            }
        }
    }


    //
    private void firebaseAuthWithGoogle(GoogleSignInAccount token) {
        //Log.d(TAG, "signInWithGoogle:" + token);

        showProgressDialog();


        AuthCredential credential = GoogleAuthProvider.getCredential(token.getIdToken(),null);
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                        }else{
                            final String uid=task.getResult().getUser().getUid();
                            final String name=task.getResult().getUser().getDisplayName();
                            final String email=task.getResult().getUser().getEmail();
                            final String image=task.getResult().getUser().getPhotoUrl().toString();
                            Calendar c = Calendar.getInstance();
                            System.out.println("Current time => " + c.getTime());
                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            final String formattedDate = df.format(c.getTime());
                            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                            final String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());
                            FirebaseDatabase database = FirebaseDatabase.getInstance();

                            Query query = database.getReference("users");
                            query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                @Override
                                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {

                                    try {
                                        HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
                                        List<User> posts = new ArrayList<>(results.values());
                                        boolean flag=false;
                                        for (User user:posts){
                                            if (user.getId().equals(uid)){
                                                flag=true;
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                                startActivity(intent);
                                                finish();
//                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                                startActivity(intent);
//                                                finish();
                                                hideProgressDialog();
                                            }
                                        }
                                        if (flag==false){
                                            User user = new User(uid, name, "",  email,  "",  "",
                                                    "",  formattedDate,  ip,  formattedDate,  ip,
                                                    image,false,"","");
                                            mRef.child(uid).setValue(user);
                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                            startActivity(intent);
                                            finish();
//                                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                            startActivity(intent);
//                                            finish();
                                            hideProgressDialog();
                                        }
                                        }
                                        catch (Exception e){
                                        //Create a new User and Save it in Firebase database

                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }
                });
    }

    protected void setUpUser() {
        user = new User();
        user.setEmail(email.getText().toString());
        user.setPassword(password.getText().toString());
    }

    private void signIn(String email, String password) {
        //Log.d(TAG, "signIn:" + email);
        if (!validateForm()) {
            return;
        }

        showProgressDialog();

        mAuth.signInWithEmailAndPassword(email, password)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                        //Log.d(TAG, "signInWithEmail:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithEmail", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();

                        } else {
                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                            intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                            String uid = mAuth.getCurrentUser().getUid();
                            intent.putExtra("user_id", uid);
                            startActivity(intent);
                            finish();

//                            Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                            String uid = mAuth.getCurrentUser().getUid();
//                            intent.putExtra("user_id", uid);
//                            startActivity(intent);
//                            finish();
                        }

                        hideProgressDialog();
                    }
                });
        //
    }

    private boolean validateForm() {
        boolean valid = true;

        String userEmail = email.getText().toString();
        if (TextUtils
                .isEmpty(userEmail)) {
            email.setError("Required.");
            valid = false;
        } else {
            email.setError(null);
        }

        String userPassword = password.getText().toString();
        if (TextUtils.isEmpty(userPassword)) {
            password.setError("Required.");
            valid = false;
        } else {
            password.setError(null);
        }

        return valid;
    }


    private void signInWithFacebook(AccessToken token) {
        //Log.d(TAG, "signInWithFacebook:" + token);

        showProgressDialog();


        AuthCredential credential = FacebookAuthProvider.getCredential(token.getToken());
        mAuth.signInWithCredential(credential)
                .addOnCompleteListener(this, new OnCompleteListener<AuthResult>() {
                    @Override
                    public void onComplete(@NonNull Task<AuthResult> task) {
                       // Log.d(TAG, "signInWithCredential:onComplete:" + task.isSuccessful());

                        // If sign in fails, display a message to the user. If sign in succeeds
                        // the auth state listener will be notified and logic to handle the
                        // signed in user can be handled in the listener.
                        if (!task.isSuccessful()) {
                            Log.w(TAG, "signInWithCredential", task.getException());
                            Toast.makeText(Login.this, "Authentication failed.",
                                    Toast.LENGTH_SHORT).show();
                            hideProgressDialog();
                        }else{
                            final String uid=task.getResult().getUser().getUid();
                            final String name=task.getResult().getUser().getDisplayName();
                            final String email=task.getResult().getUser().getEmail();
                            final String image=task.getResult().getUser().getPhotoUrl().toString();

                            Calendar c = Calendar.getInstance();
                            System.out.println("Current time => " + c.getTime());

                            SimpleDateFormat df = new SimpleDateFormat("dd-MMM-yyyy");
                            final String formattedDate = df.format(c.getTime());

                            WifiManager wm = (WifiManager) getApplicationContext().getSystemService(WIFI_SERVICE);
                            final String ip = Formatter.formatIpAddress(wm.getConnectionInfo().getIpAddress());

                            FirebaseDatabase database = FirebaseDatabase.getInstance();
                            Query query2 = null;
                            query2 = database.getReference("users");
                                query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {

                                        try {

                                            HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {
                                            });

                                            List<User> data = new ArrayList<>(results.values());
                                            boolean flag=false;
                                            for (User user:data){
                                                if (user.getId().equals(uid)){
                                                    flag=true;
                                                    Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                    startActivity(intent);
                                                    finish();
                                                    hideProgressDialog();
                                                }
                                            }
                                            if (flag==false){
                                                User user = new User(uid, name, "",  email,  "",  "",
                                                        "",  formattedDate,  ip,  formattedDate,  ip,
                                                        image,false,"","");
                                                mRef.child(uid).setValue(user);
                                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                                startActivity(intent);
                                                finish();
                                                hideProgressDialog();
                                            }

                                        }catch (Exception e){
                                            //Create a new User and Save it in Firebase database

                                        }
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
//                            }else {
//                                User user = new User(uid, name, "",  email,  "",  "",
//                                        "",  formattedDate,  ip,  formattedDate,  ip,
//                                        image,false,"","");
//                                mRef.child(uid).setValue(user);
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
//                                finish();
//                                hideProgressDialog();
//                            }
                        }


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
            mProgressDialog = new ProgressDialog(this);
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