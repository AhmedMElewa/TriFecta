package com.trifecta.mada.trifecta13.activity;

import android.graphics.Color;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ScrollView;
import android.widget.TextView;
import android.widget.Toast;

import com.firebase.client.ChildEventListener;
import com.firebase.client.DataSnapshot;
import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.firebase.client.ValueEventListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.MessageAdapter;
import com.trifecta.mada.trifecta13.other.MessageModel;
import com.trifecta.mada.trifecta13.other.User;

import java.util.ArrayList;
import java.util.Calendar;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class MessageDetails extends AppCompatActivity {

//    private RecyclerView recyclerView;
//    private MessageAdapter adapter;
//    private GridLayoutManager gridLayoutManager;
    private FirebaseAuth mAuth;
//
//    private TextView senderName;
//    private TextView message;
//    private FloatingActionButton btnSend;
//    private EditText newMessage;
    private String sender,msg,receiver,pushId;
//    private FirebaseDatabase mRef;

    LinearLayout layout;
    ImageView sendButton;
    EditText messageArea;
    ScrollView scrollView;
    Firebase reference1, reference2;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_message_details);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);

//        gridLayoutManager = new GridLayoutManager(getApplicationContext(), 1);
//        recyclerView = (RecyclerView)findViewById(R.id.recycler_view_message);
//        recyclerView.setHasFixedSize(true);
//        recyclerView.setLayoutManager(gridLayoutManager);
        mAuth = FirebaseAuth.getInstance();
//        senderName=(TextView)findViewById(R.id.senderName);
//        message=(TextView)findViewById(R.id.message);
        sender=getIntent().getStringExtra("senderId");
        msg=getIntent().getStringExtra("Message");
        pushId=getIntent().getStringExtra("id");
        receiver=getIntent().getStringExtra("receiver");
//        mRef = FirebaseDatabase.getInstance();
//
//
//        newMessage=(EditText)findViewById(R.id.input);
//        btnSend=(FloatingActionButton)findViewById(R.id.fab);
//
//        final FirebaseDatabase database = FirebaseDatabase.getInstance();
//        Query query2 = database.getReference("users").orderByChild("id").equalTo(sender);
//        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
//            @Override
//            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
//                // do some stuff once
//                HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
//                List<User> posts = new ArrayList<>(results.values());
//                for (User post : posts) {
//                    senderName.setText(post.getName());
//                    message.setText(msg);
//                }
//            }
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//
//            }
//        });
//
//        Query query1 = database.getReference("messages").orderByChild("pushId").equalTo(pushId);
//        query1.addValueEventListener(new ValueEventListener() {
//            @Override
//            public void onDataChange(DataSnapshot dataSnapshot) {
//
//                try {
//                HashMap<String, MessageModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, MessageModel>>() {
//                });
//                    List<MessageModel> data = new ArrayList<>(results.values());
//                    adapter = new MessageAdapter((ArrayList<MessageModel>) data, getApplicationContext());
//                    recyclerView.setAdapter(adapter);
//
//                } catch (Exception e) {
//                    return;
//                }
//            }
//
//            @Override
//            public void onCancelled(DatabaseError databaseError) {
//                Toast.makeText(getApplicationContext(), "Check your Internet", Toast.LENGTH_SHORT).show();
//            }
//        });
//
//        btnSend.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                MessageModel messageModel=new MessageModel();
//                String uid = mAuth.getCurrentUser().getUid();
//                messageModel.setSenderId(uid);
//                messageModel.setReceiverId(receiver);
//                messageModel.setMessage(newMessage.getText().toString());
//                DatabaseReference messages = mRef.getReference("messages");
//                messageModel.setPushId(pushId);
//                String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
//                messageModel.setDate(mydate);
//
//                messages.push().setValue(messageModel);
//                newMessage.setText("");
//            }
//        });


        layout = (LinearLayout)findViewById(R.id.layout1);
        sendButton = (ImageView)findViewById(R.id.sendButton);
        messageArea = (EditText)findViewById(R.id.messageArea);
        scrollView = (ScrollView)findViewById(R.id.scrollView);

        Firebase.setAndroidContext(this);
        reference1 = new Firebase("");
        reference2 = new Firebase("");

        sendButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String messageText = messageArea.getText().toString();

                if(!messageText.equals("")){
                    Map<String, String> map = new HashMap<String, String>();
                    map.put("message", messageText);
                    String uid = mAuth.getCurrentUser().getUid();
                    map.put("senderId", uid);
                    map.put("receiverId", receiver);
                    map.put("pushId", pushId);
                    String mydate = java.text.DateFormat.getDateTimeInstance().format(Calendar.getInstance().getTime());
                    map.put("date", mydate);
                    messageArea.setText("");

                    reference1.push().setValue(map);
//                    reference2.push().setValue(map);
                }
            }
        });

        reference1.addChildEventListener(new ChildEventListener() {
            @Override
            public void onChildAdded(DataSnapshot dataSnapshot, String s) {
                try{
                    Map map = dataSnapshot.getValue(Map.class);
                    final String message = map.get("message").toString();
                    String senderId = map.get("senderId").toString();
                    String receiverId = map.get("receiverId").toString();
                    String pushId2 = map.get("pushId").toString();



                            String uid = mAuth.getCurrentUser().getUid();

                            if(senderId.equals(uid)&&pushId2.equals(pushId)){
                                addMessageBox("You:-\n" + message, 1);
                            }
                            else if (pushId2.equals(pushId)){
                                final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                Query query2 = database.getReference("users").orderByChild("id").equalTo(receiverId);
                                query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                    @Override
                                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                                        // do some stuff once
                                        HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
                                        List<User> posts = new ArrayList<>(results.values());
                                        for (User post : posts) {
                                            addMessageBox(post.getName()+":-\n" + message, 1);
                                        }
                                    }
                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
//                                addMessageBox("Store Owner\n"+ message, 1);

                            }



                }catch (Exception e){};
            }

            @Override
            public void onChildChanged(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onChildRemoved(DataSnapshot dataSnapshot) {

            }

            @Override
            public void onChildMoved(DataSnapshot dataSnapshot, String s) {

            }

            @Override
            public void onCancelled(FirebaseError firebaseError) {

            }
        });

    }

    public void addMessageBox(String message, int type){
        TextView textView = new TextView(MessageDetails.this);
        textView.setText(message);
        textView.setTextColor(Color.WHITE);
        textView.setPadding(16,16,16,20);
        textView.setTextSize(16);
        LinearLayout.LayoutParams lp = new LinearLayout.LayoutParams(ViewGroup.LayoutParams.MATCH_PARENT, ViewGroup.LayoutParams.WRAP_CONTENT);
        lp.setMargins(0, 0, 0, 10);
        textView.setLayoutParams(lp);

        if(type == 1) {
            textView.setBackgroundResource(R.drawable.text_round);
        }
        else{
            textView.setBackgroundResource(R.drawable.border);
        }

        layout.addView(textView);
        scrollView.fullScroll(View.FOCUS_DOWN);
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
