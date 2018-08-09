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
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.ProductDetails;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;


public class DataAdapter extends RecyclerView.Adapter<DataAdapter.RecyclerViewHolder> {

    private ArrayList<ProductModel> results;
    Context context;
    Activity activity;
    private FirebaseAuth mAuth;


    public DataAdapter(ArrayList<ProductModel> results, Context context, Activity activity){
        this.context=context;
        this.results = results;
        this.activity = activity;
    }

    public DataAdapter(ArrayList<ProductModel> results, Context context) {
        this.context=context;
        this.results = results;
    }


    @Override
    public RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.card_view_list, viewGroup, false);
        return new RecyclerViewHolder(view);
    }



    @Override
    public void onBindViewHolder(final RecyclerViewHolder holder, final int position)  {


        Glide.with(context).load(results.get(position).getPrPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.back)
                .into(holder.prPic);

//            Picasso.with(holder.itemView.getContext())
//                    .load(results.get(position).getPrPic())
//                    .placeholder(R.drawable.back)
//                    .into(holder.prPic);

        holder.prName.setText(results.get(position).getPrName());
        holder.prPrice.setText(results.get(position).getPrPrice()+" EGP");

        FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query1 = database.getReference("stores").orderByChild("storeId").equalTo(results.get(position).getUid());
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {
                HashMap<String, StoreModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {
                });
                try {
                    List<StoreModel> data = new ArrayList<>(results.values());
                    for (StoreModel storeModel:data){
                        holder.StoreName.setText(storeModel.getStoreTitle());
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

        holder.itemView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                v.getContext().startActivity(new Intent(v.getContext(),ProductDetails.class)
                        .putExtra("id",results.get(position).getUid().toString())
                        .putExtra("prId",results.get(position).getPrId().toString())
                        .putExtra("prName",results.get(position).getPrName().toString())
                        .putExtra("prPrice",results.get(position).getPrPrice().toString())
                        .putExtra("prPic",results.get(position).getPrPic().toString())
                        .putExtra("prQuantity",results.get(position).getPrQuantity().toString())
                        .putExtra("prDesc",results.get(position).getPrDescription().toString())
                        .putExtra("prLocation",results.get(position).getPrLocation().toString())
                        .putExtra("prProcessing",results.get(position).getPrProcessTime().toString())
                        .putExtra("prMyOneItem",results.get(position).getPrHomeCountryO().toString())
                        .putExtra("category",results.get(position).getPrCategory()));
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
        public TextView prPrice;
        public TextView StoreName;




        public RecyclerViewHolder(View itemView) {
            super(itemView);

            prName = (TextView)itemView.findViewById(R.id.prName);
            prPic = (ImageView)itemView.findViewById(R.id.prPic);
            prPrice=(TextView)itemView.findViewById(R.id.prPrice);
            StoreName = (TextView)itemView.findViewById(R.id.StoreName);


        }
    }


}
