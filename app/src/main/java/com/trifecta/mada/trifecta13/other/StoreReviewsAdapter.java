package com.trifecta.mada.trifecta13.other;

import android.content.Context;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.RatingBar;
import android.widget.TextView;

import com.firebase.client.Firebase;
import com.firebase.client.FirebaseError;
import com.trifecta.mada.trifecta13.R;

import java.util.ArrayList;

/**
 * Created by Mada on 3/30/2017.
 */

public class StoreReviewsAdapter extends RecyclerView.Adapter<StoreReviewsAdapter.RecyclerViewHolder> {

    private ArrayList<ReviewModel> results;
    Context context;
    private Firebase myFirebaseRef = new Firebase("");


    public StoreReviewsAdapter(ArrayList<ReviewModel> results, Context context){
        this.context=context;
        this.results = results;
    }



    @Override
    public StoreReviewsAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.review_row, viewGroup, false);
        return new StoreReviewsAdapter.RecyclerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final StoreReviewsAdapter.RecyclerViewHolder holder, final int position)  {


        myFirebaseRef.child(results.get(position).getBuyerName()).child("name").addValueEventListener(new com.firebase.client.ValueEventListener() {

            @Override
            public void onDataChange(com.firebase.client.DataSnapshot dataSnapshot) {

                String data = dataSnapshot.getValue(String.class);
                holder.Name.setText(data);

            }

            //onCancelled is called in case of any error
            @Override
            public void onCancelled(FirebaseError firebaseError) {
            }
        });

        holder.Message.setText(results.get(position).getMessage());
        holder.Rate.setRating(Float.parseFloat(results.get(position).getRating()));



    }


    @Override
    public int getItemCount() {
        if (results.size()>4){
            return 4;
        }else {
            return results.size();
        }
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {

        public TextView Name;
        public TextView Message;
        public RatingBar Rate;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            Name = (TextView)itemView.findViewById(R.id.review_name);
            Message = (TextView) itemView.findViewById(R.id.review_message);
            Rate=(RatingBar) itemView.findViewById(R.id.reviewRatingBar);

        }
    }


}

