package com.trifecta.mada.trifecta13.activity;

import android.content.Intent;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.text.TextUtils;
import android.view.MenuItem;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
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
import com.trifecta.mada.trifecta13.other.StoreModel;
import com.trifecta.mada.trifecta13.other.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

public class Contact extends AppCompatActivity {

    private String OwnerId;
    private TextView ownerName;
    private EditText subject;
    private EditText message;
    private Button btnSend;
    private FirebaseAuth mAuth;
    private FirebaseDatabase mRef;



    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_contact);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        getSupportActionBar().setTitle("Contact");

        ownerName=(TextView)findViewById(R.id.receiverName);
        subject=(EditText)findViewById(R.id.subject);
        message=(EditText)findViewById(R.id.message);
        btnSend=(Button)findViewById(R.id.btnSend);
        mAuth = FirebaseAuth.getInstance();
        mRef = FirebaseDatabase.getInstance();

        OwnerId = getIntent().getStringExtra("OwnerId").toString();

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("users").orderByChild("id").equalTo(OwnerId);
        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                try {
                    HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
                    List<User> posts = new ArrayList<>(results.values());
                    for (User post : posts) {
                        ownerName.setText(post.getName());
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
                messageModel.setReceiverId(OwnerId);
                messageModel.setSubject(subject.getText().toString());
                messageModel.setMessage(message.getText().toString());
                DatabaseReference messages = mRef.getReference("contact");
                messageModel.setPushId(messages.push().getKey());
                messages.push().setValue(messageModel);

                Toast.makeText(Contact.this, "Message has sent", Toast.LENGTH_SHORT).show();
                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
                intent.setFlags(Intent.FLAG_ACTIVITY_NEW_TASK | Intent.FLAG_ACTIVITY_CLEAR_TASK);
                startActivity(intent);
                finish();
//                Intent intent = new Intent(getApplicationContext(), MainActivity.class);
//                startActivity(intent);
            }
        });
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
