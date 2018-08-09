package com.trifecta.mada.trifecta13.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.MessageDetails;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mada on 3/18/2017.
 */

public class MessageAdapter extends RecyclerView.Adapter<MessageAdapter.RecyclerViewHolder> {

    private ArrayList<MessageModel> results;
    Context context;
    Activity activity;


    public MessageAdapter(ArrayList<MessageModel> results, Context context, Activity activity){
        this.context=context;
        this.results = results;
        this.activity = activity;
    }

    public MessageAdapter(ArrayList<MessageModel> results, Context context) {
        this.context=context;
        this.results = results;
    }


    @Override
    public MessageAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_view, viewGroup, false);
        return new MessageAdapter.RecyclerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final MessageAdapter.RecyclerViewHolder holder, final int position)  {



        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query = database.getReference("users").orderByChild("id").equalTo(results.get(position).getSenderId());
        query.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once
                HashMap<String, User> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {});
                List<User> posts = new ArrayList<>(results.values());
                for (User post : posts) {
                    holder.senderName.setText(post.getName());
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.message.setText(results.get(position).getMessage());

//        holder.itemView.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                v.getContext().startActivity(new Intent(v.getContext(),MessageDetails.class)
//                        .putExtra("id",results.get(position).getPushId())
//                        .putExtra("Message",results.get(position).getMessage())
//                        .putExtra("senderId",results.get(position).getSenderId())
//                        .putExtra("receiver",results.get(position).getReceiverId()));
//            }
//        });

    }


    @Override
    public int getItemCount() {
        return results.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {

        public TextView senderName;
        public TextView message;



        public RecyclerViewHolder(View itemView) {
            super(itemView);

            senderName = (TextView)itemView.findViewById(R.id.sender_name);
            message = (TextView) itemView.findViewById(R.id.subject_message);

        }
    }


}
