package com.trifecta.mada.trifecta13.activity;

import android.app.ProgressDialog;
import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.widget.Button;
import android.widget.CheckBox;
import android.widget.EditText;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.Firebase;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.fragment.Store;
import com.trifecta.mada.trifecta13.other.StoreModel;

public class CreateStore extends AppCompatActivity {

    private static final String TAG = "TirFecta";
    //Add YOUR Firebase Reference URL instead of the following URL
    private FirebaseDatabase mRef = FirebaseDatabase.getInstance();
    private StoreModel storeModel;
    private EditText storeTitle;
    private Spinner country;
//    private CheckBox cash;
//    private CheckBox other;
    private ProgressDialog mProgressDialog;
    Button btnCreate;

    private EditText txtWelMessage;
    private EditText txtAbout;
    private EditText txtDeliveryPolicy;
    private EditText txtReAndEx;

    private Firebase myFirebaseRef;


    private FirebaseAuth mAuth;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_create_store);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Create Store");
    }

    @Override
    protected void onStart() {
        super.onStart();

        storeTitle = (EditText) findViewById(R.id.inputStName);
        country = (Spinner) findViewById(R.id.spinStCountry);
//        cash=(CheckBox)findViewById(R.id.checkboxCash) ;
//        other=(CheckBox)findViewById(R.id.checkboxOther) ;

        txtWelMessage = (EditText)findViewById(R.id.inputStWel);
        txtAbout = (EditText)findViewById(R.id.inputStAbout);
        txtDeliveryPolicy = (EditText)findViewById(R.id.inputStDPolicy);
        txtReAndEx = (EditText)findViewById(R.id.inputStReAndEx);

        btnCreate=(Button)findViewById(R.id.btnCreateStDone);



        btnCreate.setOnClickListener(new android.view.View.OnClickListener() {
            @Override
            public void onClick(android.view.View v) {
                if (!validateForm()) {
                    return;
                }
                setUpStore();

                mAuth = FirebaseAuth.getInstance();
                String uid = mAuth.getCurrentUser().getUid();
                myFirebaseRef = new Firebase("");
                myFirebaseRef.child("users/"+uid+"/store").setValue(true);

                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
//                finish();

                Toast.makeText(CreateStore.this, "Congratulation! you have created your store.",
                        Toast.LENGTH_SHORT).show();
            }
        });

    }

    @Override
    public void onStop() {
        super.onStop();
    }


    //This method sets up a new User by fetching the user entered details.
    protected void setUpStore() {



        storeModel = new StoreModel();
        storeModel.setStoreTitle(storeTitle.getText().toString());
//        storeModel.setLanguage(language.getSelectedItem().toString());
        storeModel.setCountry(country.getSelectedItem().toString());
        storeModel.setCash(true);
        storeModel.setCreditCard(false);
        storeModel.setPaypal(false);
//        storeModel.setCurrency(currency.getSelectedItem().toString());
        storeModel.setWelcomeM(txtWelMessage.getText().toString());
        storeModel.setAbout(txtAbout.getText().toString());
        storeModel.setDeliveryPolicy(txtDeliveryPolicy.getText().toString());
        storeModel.setReturnPolicy(txtReAndEx.getText().toString());
        storeModel.setStorePic("https://d30y9cdsu7xlg0.cloudfront.net/png/3279-200.png");
        storeModel.setBallance("0");
        storeModel.setWelcomeM(txtWelMessage.getText().toString());
        storeModel.setAbout(txtAbout.getText().toString());
        storeModel.setReturnPolicy(txtReAndEx.getText().toString());
        storeModel.setDeliveryPolicy(txtDeliveryPolicy.getText().toString());

        mAuth = FirebaseAuth.getInstance();
        String uid = mAuth.getCurrentUser().getUid();
        storeModel.setStoreId(uid);

        DatabaseReference store = mRef.getReference("stores");
        store.child(uid).setValue(storeModel);

    }

    private boolean validateForm() {
        boolean valid = true;

        String Title = storeTitle.getText().toString();
        if (TextUtils
                .isEmpty(Title)) {
            storeTitle.setError("Required.");
            valid = false;
        } else {
            storeTitle.setError(null);
        }

//

        return valid;
    }

//    String CardNum = cardNumber.getText().toString();
    //        if (CardNum.isEmpty() || CardNum.length() < 16 || CardNum.length() > 16) {
//            cardNumber.setError("enter 16 number correct");
//            valid = false;
//        } else {
//            cardNumber.setError(null);
//        }
//
//        String security = securityNum.getText().toString();
//        if (security.isEmpty() || security.length() < 3 ||  security.length()>3) {
//            securityNum.setError("enter your 3 security numbers");
//            valid = false;
//        } else {
//            securityNum.setError(null);
//        }
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