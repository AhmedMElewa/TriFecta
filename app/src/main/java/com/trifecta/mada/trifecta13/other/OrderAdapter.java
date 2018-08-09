package com.trifecta.mada.trifecta13.other;

import android.app.Activity;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.graphics.Color;
import android.support.v7.app.ActionBar;
import android.support.v7.app.AlertDialog;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.RatingBar;
import android.widget.TextView;
import android.widget.Toast;

import com.bumptech.glide.Glide;
import com.bumptech.glide.load.engine.DiskCacheStrategy;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.activity.MainActivity;
import java.util.ArrayList;
import java.util.HashMap;


/**
 * Created by Mada on 2/23/2017.
 */

public class OrderAdapter extends RecyclerView.Adapter<OrderAdapter.RecyclerViewHolder> {

    private ArrayList<OrderModel> results;
    Context context;
    private FirebaseDatabase mRef = FirebaseDatabase.getInstance();
    private FirebaseAuth mAuth;
    Activity activity;

    public OrderAdapter(ArrayList<OrderModel> results, Context context, Activity activity) {
        this.context = context;
        this.results = results;
        this.activity = activity;
    }


    @Override
    public OrderAdapter.RecyclerViewHolder onCreateViewHolder(ViewGroup viewGroup, int i) {
        View view = LayoutInflater.from(viewGroup.getContext()).inflate(R.layout.order_item, viewGroup, false);
        return new OrderAdapter.RecyclerViewHolder(view);
    }


    @Override
    public void onBindViewHolder(final OrderAdapter.RecyclerViewHolder holder, final int position) {

        Glide.with(holder.itemView.getContext())
                .load(results.get(position).getPrPic())
                .diskCacheStrategy(DiskCacheStrategy.ALL)
                .placeholder(R.drawable.back)
                .into(holder.crPic);

        holder.crName.setText(results.get(position).getPrName());
        holder.crPrice.setText(results.get(position).getPrPrice() + " EGP");
        holder.crQuantity.setText(results.get(position).getPrQuantity() + " piece");
        holder.paymentMethod.setText("Payment Method: "+"\n"+results.get(position).getPaymentMethod());
        holder.crShipping.setText("Shipping: " + results.get(position).getShipping() + " EGP");
        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                Query applesQuery = db_node.child("orders").orderByChild("pushId").equalTo(results.get(position).getPushId()+results.get(position).getBuyerId());

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

            }
        });

        final FirebaseDatabase database = FirebaseDatabase.getInstance();



            if (results.get(position).getSellerConfirm().toString() == "true" &&results.get(position).getBuyerConfirm().toString() == "true") {

                final OrderModel orderModel = new OrderModel();
                final DatabaseReference orders = mRef.getReference("old_orders");
                orderModel.setPushId(results.get(position).getPushId());
                orderModel.setBuyerId(results.get(position).getBuyerId());
                orderModel.setSellerId(results.get(position).getSellerId());
                orderModel.setPrName(results.get(position).getPrName());
                orderModel.setPrPic(results.get(position).getPrPic());
                orderModel.setPrPrice(results.get(position).getPrPrice());
                orderModel.setPrQuantity(results.get(position).getPrQuantity());
                orderModel.setOrDate(results.get(position).getOrDate());
                orderModel.setPaymentMethod(results.get(position).getPaymentMethod());
                orderModel.setBuyerConfirm(results.get(position).getBuyerConfirm());
                orderModel.setSellerConfirm(false);
                orderModel.setPaid(results.get(position).getPaid());
                orderModel.setBuyerName(results.get(position).getBuyerName());
                orderModel.setBuyerStreet(results.get(position).getBuyerStreet());
                orderModel.setBuyerHomeNum(results.get(position).getBuyerHomeNum());
                orderModel.setBuyerCity(results.get(position).getBuyerCity());
                orderModel.setBuyerPhone(results.get(position).getBuyerPhone());
                orderModel.setBuyerNote(results.get(position).getBuyerNote());
                orderModel.setCommission(results.get(position).getCommission());

                Query query2 = database.getReference("orders").orderByChild("pushId").equalTo(results.get(position).getPushId());
                query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                    @Override
                    public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {

                        try {
                            orders.child(results.get(position).getPushId()).setValue(orderModel);
                         }catch (Exception e){}

                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });

                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                Query applesQuery = db_node.child("orders").orderByChild("pushId").equalTo(results.get(position).getPushId());

                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                    @Override
                    public void onDataChange(DataSnapshot dataSnapshot) {

                        for (DataSnapshot appleSnapshot : dataSnapshot.getChildren()) {
                            appleSnapshot.getRef().removeValue();
                        }

                        notifyDataSetChanged();
                    }

                    @Override
                    public void onCancelled(DatabaseError databaseError) {

                    }
                });
            }
              else if (results.get(position).getBuyerConfirm().toString() == "true") {
                holder.done.setVisibility(View.GONE);
                holder.cancel.setVisibility(View.GONE);
            } else if (results.get(position).getSellerConfirm().toString()=="true"){

                  holder.done.setOnClickListener(new View.OnClickListener() {
                    @Override
                    public void onClick(final View v) {

                        holder.makeReview.setText("Submit");
                        holder.skip.setText("Skip");
                        holder.popupText.setText("If you did not got your product go back \n if you got your product, Hope you Rate this shop");
                        holder.popupText.setTextSize(16);
                        holder.popupText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        holder.popupText.setPadding(40, 10, 40, 10);
                        holder.popupEdit.setHint("Your review comment");
                        holder.popupEdit.setTextSize(16);
                        holder.popupEdit.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        holder.popupEdit.setPadding(40, 5, 10, 5);
                        holder.layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
                        holder.popupRate.setStepSize((float) 0.1);
                        holder.popupRate.setNumStars(5);
                        holder.popupRate.setPadding(5, 5, 0, 5);
                        holder.popupRate.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                                ActionBar.LayoutParams.WRAP_CONTENT));
                        ;
                        holder.layoutOfPopup.addView(holder.popupText);
                        holder.layoutOfPopup.addView(holder.popupEdit);
                        holder.layoutOfPopup.addView(holder.popupRate);
                        holder.layoutOfPopup.setBackgroundColor(Color.parseColor("#FBFBFB"));
                        holder.layoutOfPopup.setGravity(View.TEXT_ALIGNMENT_CENTER);
                        holder.popDialog.setPositiveButton("submit",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int which) {
                                        ((ViewGroup)holder.layoutOfPopup.getParent()).removeView(holder.layoutOfPopup);

                                        ReviewModel reviewModel = new ReviewModel();
                                        reviewModel.setRating(String.valueOf(holder.popupRate.getRating()));
                                        reviewModel.setMessage(holder.popupEdit.getText().toString());
                                        DatabaseReference reviews = mRef.getReference("reviews");
                                        reviewModel.setPushId(reviews.push().getKey());
                                        mAuth = FirebaseAuth.getInstance();
                                        String name = mAuth.getCurrentUser().getUid();
                                        reviewModel.setBuyerName(name);
                                        reviewModel.setUid(results.get(position).getSellerId());
                                        reviews.push().setValue(reviewModel);

                                        dialog.dismiss();

                                        Query query2 = database.getReference("orders").orderByChild("pushId").equalTo(results.get(position).getPushId());
                                        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                            @Override
                                            public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {
                                                // do some stuff once

                                                final HashMap<String, OrderModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                                                });
                                                DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                                                String key = nodeDataSnapshot.getKey();
                                                OrderModel orderModel = new OrderModel();
                                                orderModel.setBuyerConfirm(true);
                                                String path = "/" + snapshot.getKey() + "/" + key;
                                                HashMap<String, Object> result = new HashMap<>();
                                                result.put("buyerConfirm", true);
                                                database.getReference(path).updateChildren(result);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });

                                        DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                                        Query applesQuery = db_node.child("products").orderByChild("prId").equalTo(results.get(position).getPrId());

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
                                        context.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));
                                        Toast.makeText(context, "Thank you!", Toast.LENGTH_SHORT).show();

                                    }
                                }).setNegativeButton("Skip",
                                new DialogInterface.OnClickListener() {
                                    public void onClick(DialogInterface dialog, int id) {
                                        dialog.dismiss();

                                        Query query2 = database.getReference("orders").orderByChild("pushId").equalTo(results.get(position).getPushId());
                                        query2.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                            @Override
                                            public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {
                                                // do some stuff once

                                                final HashMap<String, OrderModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                                                });
                                                DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                                                String key = nodeDataSnapshot.getKey();
                                                OrderModel orderModel = new OrderModel();
                                                orderModel.setBuyerConfirm(true);
                                                String path = "/" + snapshot.getKey() + "/" + key;
                                                HashMap<String, Object> result = new HashMap<>();
                                                result.put("buyerConfirm", true);
                                                database.getReference(path).updateChildren(result);
                                            }

                                            @Override
                                            public void onCancelled(DatabaseError databaseError) {

                                            }
                                        });
                                        DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                                        Query applesQuery = db_node.child("products").orderByChild("prId").equalTo(results.get(position).getPrId());

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
                                        context.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));
                                        Toast.makeText(context, "Thank you!", Toast.LENGTH_SHORT).show();

                                    }
                                });

                        holder.popDialog.setIcon(android.R.drawable.btn_star_big_on);
                        holder.popDialog.setTitle("Vote!! ");
                        holder.popDialog.setView(holder.layoutOfPopup);
                        holder.popDialog.create();
                        holder.popDialog.show();


                    }
                });
            }else {
                holder.done.setVisibility(View.GONE);
            }



        holder.cancel.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {

                holder.makeReview.setText("Yes");
                holder.skip.setText("No");
                holder.popupText.setText("Are You Sure You Want to Cancel This Order");
                holder.popupText.setTextSize(16);
                holder.popupText.setGravity(View.TEXT_ALIGNMENT_CENTER);
                holder.popupText.setPadding(40, 10, 40, 10);
                holder.layoutOfPopup.setOrientation(LinearLayout.VERTICAL);
                holder.popupRate.setLayoutParams(new ActionBar.LayoutParams(ActionBar.LayoutParams.WRAP_CONTENT,
                        ActionBar.LayoutParams.WRAP_CONTENT));
                ;
                holder.layoutOfPopup.addView(holder.popupText);
                holder.layoutOfPopup.setBackgroundColor(Color.parseColor("#FBFBFB"));
                holder.layoutOfPopup.setGravity(View.TEXT_ALIGNMENT_CENTER);
                holder.popDialog.setPositiveButton("Yes",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int which) {

                                DatabaseReference db_node = FirebaseDatabase.getInstance().getReference().getRoot();
                                Query applesQuery = db_node.child("orders").orderByChild("pushId").equalTo(results.get(position).getPushId());

                                applesQuery.addListenerForSingleValueEvent(new ValueEventListener() {
                                    @Override
                                    public void onDataChange(DataSnapshot dataSnapshot) {

                                        for (DataSnapshot appleSnapshot: dataSnapshot.getChildren()) {
                                            appleSnapshot.getRef().removeValue();
                                            ((ViewGroup)holder.layoutOfPopup.getParent()).removeView(holder.layoutOfPopup);
                                        }

                                        notifyDataSetChanged();
                                    }

                                    @Override
                                    public void onCancelled(DatabaseError databaseError) {

                                    }
                                });
                                Toast.makeText(context, "Your order has canceled", Toast.LENGTH_SHORT).show();

                            }
                        }).setNegativeButton("No",
                        new DialogInterface.OnClickListener() {
                            public void onClick(DialogInterface dialog, int id) {
                                dialog.cancel();
                                ((ViewGroup)holder.layoutOfPopup.getParent()).removeView(holder.layoutOfPopup);
                                context.startActivity(new Intent(activity.getBaseContext(), MainActivity.class));

                            }
                        });

//                holder.popDialog.setIcon(android.R.drawable.btn_star_big_on);
                holder.popDialog.setTitle("Cancel!! ");
                holder.popDialog.setView(holder.layoutOfPopup);
                holder.popDialog.create();
                holder.popDialog.show();



            }
        });




    }



//    public void popupInit() {
//
//        makeOrder.setOnClickListener(new View.OnClickListener() {
//            @Override
//            public void onClick(View v) {
//                Intent intent = new Intent(getApplicationContext(), AboutUs.class);
//                startActivity(intent);
//            }
//        });
//
//
//    }



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
        public TextView cancel;
        public TextView done;
        public TextView paymentMethod;

        public TextView popupText;
        public EditText popupEdit;
        public RatingBar popupRate;
        public Button makeReview;
        public Button skip;
        public LinearLayout layoutOfPopup;
        public AlertDialog.Builder popDialog;

        public RecyclerViewHolder(View itemView) {
            super(itemView);

            crName = (TextView) itemView.findViewById(R.id.order_product_name);
            crPic = (ImageView) itemView.findViewById(R.id.order_product_image);
            crPrice = (TextView) itemView.findViewById(R.id.order_product_price);
            crQuantity=(TextView)itemView.findViewById(R.id.order_product_quantity);
            crShipping=(TextView)itemView.findViewById(R.id.order_product_shipping);
            done = (TextView)itemView.findViewById(R.id.order_product_done);
            cancel = (TextView)itemView.findViewById(R.id.order_product_cancel);
            paymentMethod = (TextView)itemView.findViewById(R.id.order_product_paymentMethod);

            popupText = new TextView(context);
            popupEdit = new EditText(context);
            popupRate = new RatingBar(context);
            makeReview = new Button(context);
            skip = new Button(context);
            layoutOfPopup = new LinearLayout(context);
            popDialog = new AlertDialog.Builder(activity);

        }
    }
}

