package com.trifecta.mada.trifecta13.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.MessageDetails;


import java.util.ArrayList;

/**
 * Created by Mada on 3/18/2017.
 */

public class ContactAdapter extends RecyclerView.Adapter<ContactAdapter.RecyclerViewHolder> {

    private ArrayList<MessageModel> results;
    Context context;
    Activity activity;


    public ContactAdapter(ArrayList<MessageModel> results, Context context, Activity activity){
        this.context=context;
        this.results = results;
        this.activity = activity;
    }

    public ContactAdapter(ArrayList<MessageModel> results, Context context) {
        this.context=context;
        this.results = results;
    }


    @Override
    public ContactAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.message_view, viewGroup, false);
        return new ContactAdapter.RecyclerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final ContactAdapter.RecyclerViewHolder holder, final int position)  {

        holder.senderName.setText(results.get(position).getReceiverName());
        holder.subject.setText(results.get(position).getSubject());
        holder.message.setText(results.get(position).getMessage());

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(),MessageDetails.class)
                        .putExtra("id",results.get(position).getPushId())
                        .putExtra("Message",results.get(position).getMessage())
                        .putExtra("senderId",results.get(position).getSenderId())
                        .putExtra("receiver",results.get(position).getReceiverId()));
            }
        });

    }


    @Override
    public int getItemCount() {
        return results.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {

        public TextView senderName;
        public TextView subject;
        public TextView message;



        public RecyclerViewHolder(View itemView) {
            super(itemView);

            senderName = (TextView)itemView.findViewById(R.id.sender_name);
            subject = (TextView) itemView.findViewById(R.id.subject_message);
            message = (TextView)itemView.findViewById(R.id.message);

        }
    }


}
