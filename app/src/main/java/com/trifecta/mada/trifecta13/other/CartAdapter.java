package com.trifecta.mada.trifecta13.other;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ArrayAdapter;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.firebase.client.Firebase;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.ShippingCash;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static com.facebook.FacebookSdk.getApplicationContext;

/**
 * Created by Mada on 2/23/2017.
 */

public class CartAdapter extends RecyclerView.Adapter<CartAdapter.RecyclerViewHolder> {

    private ArrayList<CartModel> results;
    Context context;
    boolean flag=false;
    private Firebase myFirebaseRef = new Firebase("");

    public CartAdapter(ArrayList<CartModel> results, Context context) {
        this.context = context;
        this.results = results;
    }


    @Override
    public CartAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.cart_item, viewGroup, false);
        return new CartAdapter.RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final CartAdapter.RecyclerViewHolder holder, final int position) {


        Glide.with(holder.itemView.getContext())
                .load(results.get(position).getCrPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.back)
                .into(holder.crPic);


        holder.crName.setText(results.get(position).getCrName());
        holder.crPrice.setText(results.get(position).getCrPrice()+" EGP");

//        holder.crQuantity.setText(results.get(position).getCrQuantity()+" Piece");


        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query1 = database.getReference("products").orderByChild("prId").equalTo(results.get(position).getPushId());
        query1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once

                HashMap<String, ProductModel> result = snapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {});

                List<ProductModel> posts = new ArrayList<>(result.values());

                for (ProductModel post : posts) {
                    holder.crShipping.setText(post.getPrHomeCountryO()+" EGP");

                    Integer[] items = new Integer[Integer.parseInt(post.getPrQuantity())];
                    for (int i=0;i<Integer.parseInt(post.getPrQuantity());i++){
                        items[i] = i+1;
                    }
                    ArrayAdapter<Integer> spinAdapter = new ArrayAdapter<Integer>(getApplicationContext(),R.layout.spinner_item, items);
                    holder.crQuantity.setAdapter(spinAdapter);
                    holder.crQuantity.setSelection(Integer.parseInt(results.get(position).getCrQuantity()));
                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        Query query2 = database.getReference("stores").orderByChild("storeId").equalTo(results.get(position).getCrId());
        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once

                HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {});

                List<StoreModel> posts = new ArrayList<>(results.values());

                for (StoreModel post : posts) {
                    holder.stName.setText(post.getStoreTitle());
                    Glide.with(holder.itemView.getContext())
                            .load(post.getStorePic())
                            .diskCacheStrategy(DiskCacheStrategy.ALL)
                            .placeholder(R.drawable.back)
                            .into((holder.stPic));

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.delete.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                Query applesQuery = db_node.child("carts").orderByChild("pushId").equalTo(results.get(position).getPushId());

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                            notifyDataSetChanged();

                        }


                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

//        final String flag= holder.payment.getSelectedItem().toString();

        holder.buy.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {

                Query query2 = database.getReference("orders").orderByChild("pushId");
                query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                        // do some stuff once

                        try{
                            HashMap<String, OrderModel> results0 = snapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {});

                            List<OrderModel> posts = new ArrayList<>(results0.values());
                            for (OrderModel orderModel:posts){
                                if (orderModel.getPushId().equals(results.get(position).getPushId()+results.get(position).getUid()))
                                {
                                    Toast.makeText(getApplicationContext(), "Your order sent before please contact to the seller", Toast.LENGTH_LONG).show();
                                    flag=true;
                                }
                            }
                            if(flag==false){
                                Intent intent = new Intent(context, ShippingCash.class);
                                v.getContext().startActivity(intent.putExtra("prName",results.get(position).getCrName())
                                        .putExtra("prPic",results.get(position).getCrPic())
                                        .putExtra("prPrice",results.get(position).getCrPrice())
                                        .putExtra("prQuantity",holder.crQuantity.getSelectedItem().toString())
                                        .putExtra("buyer",results.get(position).getUid())
                                        .putExtra("seller",results.get(position).getCrId())
                                        .putExtra("prId",results.get(position).getPushId())
                                        .putExtra("paymentMethod",holder.payment.getSelectedItem().toString()));
                            }

                        }catch (Exception e){
                            if(flag==false){
                                Intent intent = new Intent(context, ShippingCash.class);
                                v.getContext().startActivity(intent.putExtra("prName",results.get(position).getCrName())
                                        .putExtra("prPic",results.get(position).getCrPic())
                                        .putExtra("prPrice",results.get(position).getCrPrice())
                                        .putExtra("prQuantity",holder.crQuantity.getSelectedItem().toString())
                                        .putExtra("buyer",results.get(position).getUid())
                                        .putExtra("seller",results.get(position).getCrId())
                                        .putExtra("prId",results.get(position).getPushId())
                                        .putExtra("prShipping",holder.crShipping.toString())
                                        .putExtra("paymentMethod",holder.payment.getSelectedItem().toString()));
                            }
                        }



                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });

    }


    @Override
    public int getItemCount() {
        return results.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView crName;
        public ImageView crPic;
        public TextView crPrice;
        public Spinner crQuantity;
        public TextView crShipping;
        public TextView delete;
        public TextView buy;
        public Spinner payment;
        public ImageView stPic;
        public TextView stName;


        public RecyclerViewHolder(View itemView) {
            super(itemView);

            crName = (TextView) itemView.findViewById(R.id.cart_product_name);
            crPic = (ImageView) itemView.findViewById(R.id.cart_product_image);
            crPrice = (TextView) itemView.findViewById(R.id.cart_product_price);
            crQuantity=(Spinner)itemView.findViewById(R.id.cart_product_quantity);
            delete = (TextView)itemView.findViewById(R.id.cart_product_delete);
            buy = (TextView)itemView.findViewById(R.id.cart_product_buy);
            payment = (Spinner)itemView.findViewById(R.id.order_payment_method);
            crShipping=(TextView)itemView.findViewById(R.id.cart_product_shipping);
            stPic = (ImageView) itemView.findViewById(R.id.stPic);
            stName = (TextView)itemView.findViewById(R.id.stName);

        }
    }
}
