package com.trifecta.mada.trifecta13.other;

import android.content.Context;
import android.content.Intent;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.Contact;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

/**
 * Created by Mada on 2/25/2017.
 */

public class SellerOrderAdapter  extends RecyclerView.Adapter<SellerOrderAdapter.RecyclerViewHolder> {

    private ArrayList<OrderModel> results;
    Context context;

    public SellerOrderAdapter(ArrayList<OrderModel> results, Context context) {
        this.context = context;
        this.results = results;
    }


    @Override
    public SellerOrderAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.seller_order_item, viewGroup, false);
        return new SellerOrderAdapter.RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final SellerOrderAdapter.RecyclerViewHolder holder, final int position) {

        Glide.with(holder.itemView.getContext())
                .load(results.get(position).getPrPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.back)
                .into(holder.crPic);

        holder.crName.setText(results.get(position).getPrName());
        holder.crPrice.setText(results.get(position).getPrPrice()+" EGP");
        holder.crQuantity.setText(results.get(position).getPrQuantity()+" piece");
        holder.buyerName.setText(results.get(position).getBuyerName());
        holder.buyerCity.setText(results.get(position).getBuyerCity());
        holder.paymentMethod.setText("Payment Method: "+"\n"+results.get(position).getPaymentMethod());
//        holder.buyerHomeNum.setText(results.get(position).getBuyerHomeNum());
        holder.buyerStreet.setText(results.get(position).getBuyerStreet());
        holder.buyerPhone.setText(results.get(position).getBuyerPhone());
        holder.accept.setVisibility(View.GONE);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();
        Query query1 = database.getReference("products").orderByChild("prId").equalTo(results.get(position).getPrId());
        query1.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                // do some stuff once

                HashMap<String, ProductModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, ProductModel>>() {});

                List<ProductModel> posts = new ArrayList<>(results.values());

                for (ProductModel post : posts) {
                    holder.crShipping.setText("Shipping: "+post.getPrHomeCountryO()+" EGP");

                }
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.done.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Query query2 = database.getReference("orders").orderByChild("pushId").equalTo(results.get(position).getPushId());
                query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {
                        // do some stuff once

                        final HashMap<String, OrderModel> results0 = snapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {});
                        List<OrderModel> posts = new ArrayList<>(results0.values());
                        for (OrderModel orderModel:posts){
                            if(orderModel.getPrId().equals((results.get(position).getPrId()))) {
                                DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                                String key = nodeDataSnapshot.getKey();
//                                OrderModel orderModel = new OrderModel();
                                orderModel.setBuyerConfirm(true);
                                String path = "/" + snapshot.getKey() + "/" + key;
                                HashMap<String, Object> result = new HashMap<>();
                                result.put("sellerConfirm", true);
                                result.put("notifyOrder", true);
                                database.getReference(path).updateChildren(result);
                                Toast.makeText(context, "Thank you for using our app this order will delete after your customer rate you ask him or her for rating", Toast.LENGTH_LONG).show();
                            }

                        }

                    }
                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
        });


        Query query2 = database.getReference("orders").orderByChild("pushId").equalTo(results.get(position).getPushId());
        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
            @Override
            public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {

                try {

                    HashMap<String, OrderModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {});
                    List<OrderModel> posts = new ArrayList<>(results.values());

                    for (OrderModel post : posts) {
                        if(post.getSellerConfirm()==true){
                            holder.accept.setVisibility(View.VISIBLE);
                            holder.done.setVisibility(View.GONE);
                        }else {
                            holder.accept.setVisibility(View.GONE);
                        }

                    }

                }catch (Exception e){}

            }
            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });

        holder.contact.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(v.getContext(), Contact.class)
                        .putExtra("OwnerId",results.get(position).getBuyerId().toString());
                v.getContext().startActivity(intent);
            }
        });


//        sellerConfirm

    }


    @Override
    public int getItemCount() {
        return results.size();
    }


    public class RecyclerViewHolder extends RecyclerView.ViewHolder {

        public TextView crName;
        public ImageView crPic;
        public TextView crPrice;
        public TextView crQuantity;
        public TextView crShipping;
        public TextView done;
        public TextView buyerName;
        public TextView buyerCity;
//        public TextView buyerHomeNum;
        public TextView buyerStreet;
        public TextView buyerPhone;
        public LinearLayout accept;
        public TextView contact;
        public TextView paymentMethod;



        public RecyclerViewHolder(View itemView) {
            super(itemView);

            crName = (TextView) itemView.findViewById(R.id.order_product_name);
            crPic = (ImageView) itemView.findViewById(R.id.order_product_image);
            crPrice = (TextView) itemView.findViewById(R.id.order_product_price);
            crQuantity=(TextView)itemView.findViewById(R.id.order_product_quantity);
            crShipping = (TextView) itemView.findViewById(R.id.order_product_shipping);
            done = (TextView)itemView.findViewById(R.id.order_product_done);
            buyerName = (TextView)itemView.findViewById(R.id.buyerName);
            buyerCity = (TextView)itemView.findViewById(R.id.buyerCity);
//            buyerHomeNum = (TextView)itemView.findViewById(R.id.buyerHomeNum);
            buyerStreet = (TextView)itemView.findViewById(R.id.buyerStreet);
            buyerPhone = (TextView)itemView.findViewById(R.id.buyerPhone);
            accept = (LinearLayout)itemView.findViewById(R.id.accept);
            contact = (TextView)itemView.findViewById(R.id.contact);
            paymentMethod = (TextView)itemView.findViewById(R.id.order_product_paymentMethod);

        }
    }
}
