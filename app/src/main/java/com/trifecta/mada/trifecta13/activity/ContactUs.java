package com.trifecta.mada.trifecta13.activity;

import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.support.v7.app.AlertDialog;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.support.v7.view.ContextThemeWrapper;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.MessageModel;
import com.trifecta.mada.trifecta13.other.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

public class ContactUs extends AppCompatActivity {


    private TextView ownerName;
    private EditText subject;
    private EditText message;
    private Button btnSend;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mRef;
    private Button btnSendface;

    private TextView popupText;
    private Button okbtn;
    private Button cancelbtn;
    private LinearLayout layoutOfPopup;
    public AlertDialog.Builder popDialog;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact_us);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact Us");

        ownerName=(TextView)findViewById(R.id.receiverName);
        subject=(EditText)findViewById(R.id.subject);
        message=(EditText)findViewById(R.id.message);
        btnSendface = (Button)findViewById(R.id.btnSendface);
        btnSend=(Button)findViewById(R.id.btnSend);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance();


        popupText = new TextView(getApplicationContext());
        okbtn = new Button(getApplicationContext());
        cancelbtn = new Button(getApplicationContext());
        layoutOfPopup = new LinearLayout(getApplicationContext());
        popDialog = new AlertDialog.Builder(new ContextThemeWrapper(this, R.style.myDialog));

        btnSendface.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String facebookId = "fb://page/1509003169132369";
                startActivity(new Intent(Intent.ACTION_VIEW, Uri.parse(facebookId)));
            }
        });

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("users").orderByChild("id").equalTo("nK6jgmDtCNP9sukfu6W44cCDKpJ3");
        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                try {
                    HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
                    List<User> posts = new ArrayList<>(results.values());
                    for (User post : posts) {
                        ownerName.setText("To: "+ post.getName());
                    }
                }catch (Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        btnSend.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!validateForm()) {
                    return;
                }

                MessageModel messageModel=new MessageModel();
                String uid = mAuth.getCurrentUser().getUid();
                messageModel.setSenderId(uid);
                messageModel.setReceiverName(ownerName.getText().toString());
                messageModel.setReceiverId("nK6jgmDtCNP9sukfu6W44cCDKpJ3");
                messageModel.setSubject(subject.getText().toString());
                messageModel.setMessage(message.getText().toString());
                DatabaseReference messages = mRef.getReference("contact");
                messageModel.setPushId(messages.push().getKey());
                messages.push().setValue(messageModel);


                okbtn.setText("Ok");
                cancelbtn.setText("Cancel");
                popupText.setText("Your Message has sent we will reply to you within 24 hours");
                popupText.setTextSize(16);
                popupText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                popupText.setPadding(40, 10, 40, 10);
                layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
                layoutOfPopup.addView(popupText);
                layoutOfPopup.setGravity(View.TEXT_ALIGNMENT_CENTER);
                popDialog.setPositiveButton("Ok",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {
                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                                startActivity(intent);
                                finish();
//                                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                                startActivity(intent);
                                finish();

                            }
                        });

//                popDialog.setIcon(android.R.drawable.);
                popDialog.setTitle("Thank You! ");
                popDialog.setView(layoutOfPopup);
                popDialog.create();
                popDialog.show();


            }
        });

    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        int id = item.getItemId();

        //noinspection SimplifiableIfStatement
        if (id == android.R.id.home) {
            // finish the activity
            onBackPressed();
            return true;
        }

        return super.onOptionsItemSelected(item);
    }

    private boolean validateForm() {
        boolean valid = true;

        String Title = subject.getText().toString();
        if (TextUtils
                .isEmpty(Title)) {
            subject.setError("Required.");
            valid = false;
        } else {
            subject.setError(null);
        }

        String msg = message.getText().toString();
        if (msg.isEmpty()) {
            message.setError("Required");
            valid = false;
        } else {
            message.setError(null);
        }
        return valid;
    }

}
