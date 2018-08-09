package com.trifecta.mada.trifecta13.other;

import android.app.Activity;
import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.StorePreview;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mada on 3/17/2017.
 */

public class StoresAdapter extends RecyclerView.Adapter<StoresAdapter.RecyclerViewHolder> {

    private ArrayList<StoreModel> results;
    Context context;
    Activity activity;


    public StoresAdapter(ArrayList<StoreModel> results, Context context, Activity activity){
        this.context=context;
        this.results = results;
        this.activity = activity;
    }

    public StoresAdapter(ArrayList<StoreModel> results, Context context) {
        this.context=context;
        this.results = results;
    }


    @Override
    public StoresAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_list, viewGroup, false);
        return new StoresAdapter.RecyclerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final StoresAdapter.RecyclerViewHolder holder, final int position)  {

        Glide.with(holder.itemView.getContext())
                .load(results.get(position).getStorePic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.back)
                .into(holder.prPic);

        holder.prName.setText(results.get(position).getStoreTitle());


        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query1 = database.getReference("users").orderByChild("id").equalTo(results.get(position).getStoreId());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, User> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, User>>() {
                });
                try {
                    List<User> data = new ArrayList<>(results.values());
                    for (User user:data){
                        holder.ownserName.setText(user.getName());
                    }
                } catch (Exception e) {
                    return;
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
                Toast.makeText(context, "Check your Internet", Toast.LENGTH_SHORT).show();
            }
        });

        Query query2 = database.getReference("old_orders").orderByChild("sellerId").equalTo(results.get(position).getStoreId());
        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                try {
                    holder.sales.setText((int) snapshot.getChildrenCount()+" sales");
                }catch (Exception e){
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(),StorePreview.class)
                        .putExtra("ownerId",results.get(position).getStoreId().toString()));
            }
        });

    }


    @Override
    public int getItemCount() {
        return results.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder  {

        public TextView prName;
        public ImageView prPic;
        public TextView ownserName;
        public TextView sales;



        public RecyclerViewHolder(View itemView) {
            super(itemView);

            prName = (TextView)itemView.findViewById(R.id.prName);
            prPic = (ImageView)itemView.findViewById(R.id.prPic);
            sales=(TextView)itemView.findViewById(R.id.prPrice);
            ownserName=(TextView)itemView.findViewById(R.id.StoreName);

        }
    }


}
