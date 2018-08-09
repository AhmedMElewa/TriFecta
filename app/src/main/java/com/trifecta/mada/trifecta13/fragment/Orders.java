package com.trifecta.mada.trifecta13.fragment;

import android.app.ProgressDialog;
import android.content.Context;
import android.net.Uri;
import android.os.Bundle;
import android.support.v4.app.Fragment;
import android.support.v7.widget.GridLayoutManager;
import android.support.v7.widget.RecyclerView;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.TextView;

import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.GenericTypeIndicator;
import com.google.firebase.database.Query;
import com.google.firebase.database.ValueEventListener;
import com.trifecta.mada.trifecta13.R;
import com.trifecta.mada.trifecta13.other.CartAdapter;
import com.trifecta.mada.trifecta13.other.CartModel;
import com.trifecta.mada.trifecta13.other.DataAdapter;
import com.trifecta.mada.trifecta13.other.OrderAdapter;
import com.trifecta.mada.trifecta13.other.OrderModel;
import com.trifecta.mada.trifecta13.other.ProductModel;
import com.trifecta.mada.trifecta13.other.SellerOrderAdapter;
import com.trifecta.mada.trifecta13.other.StoreModel;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import static android.view.View.GONE;
import static android.view.View.VISIBLE;
import static com.facebook.FacebookSdk.getApplicationContext;


public class Orders extends Fragment {

    private RecyclerView recyclerView;
    private RecyclerView recyclerView2;
    private OrderAdapter adapter;
    private SellerOrderAdapter adapter2;
    private GridLayoutManager gridLayoutManager;
    private GridLayoutManager gridLayoutManager2;
    private FirebaseAuth mAuth;
    private ProgressDialog mProgressDialog;
//    private TextView noOrders;
    private TextView noMakeOrders;


    public Orders() {
        // Required empty public constructor
    }



    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

    }

    @Override
    public View onCreateView(LayoutInflater inflater, ViewGroup container,
                             Bundle savedInstanceState) {
        final View v=inflater.inflate(R.layout.fragment_orders, container, false);

        gridLayoutManager = new GridLayoutManager(getContext(), 1);
        recyclerView = (RecyclerView)v.findViewById(R.id.order_recycler);
        recyclerView.setHasFixedSize(true);
        recyclerView.setLayoutManager(gridLayoutManager);
//        noOrders=(TextView)v.findViewById(R.id.noOrders);
        noMakeOrders= (TextView)v.findViewById(R.id.noMakeOrders);

        gridLayoutManager2 = new GridLayoutManager(getContext(), 1);
        recyclerView2 = (RecyclerView)v.findViewById(R.id.my_order_recycler);
        recyclerView2.setHasFixedSize(true);
        recyclerView2.setLayoutManager(gridLayoutManager2);

        final FirebaseDatabase database = FirebaseDatabase.getInstance();

        mAuth = FirebaseAuth.getInstance();
        final String uid = mAuth.getCurrentUser().getUid();

        showProgressDialog();
        Query query1 = database.getReference("orders").orderByChild("sellerId").equalTo(uid);
        query1.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, OrderModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                });

                try {
                        List<OrderModel> data = new ArrayList<>(results.values());
                        adapter2 = new SellerOrderAdapter((ArrayList<OrderModel>)data, getContext());
                        recyclerView.setAdapter(adapter2);

                }catch (Exception e){
                    return;
                };
                hideProgressDialog();


            }

            @Override
            public void onCancelled(DatabaseError databaseError) {

            }
        });


        showProgressDialog();
        Query query2 = database.getReference("orders").orderByChild("buyerId").equalTo(uid);
        query2.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(DataSnapshot dataSnapshot) {

                HashMap<String, OrderModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                });
                try {

                    if ((int) dataSnapshot.getChildrenCount() == 0) {
                        noMakeOrders.setVisibility(VISIBLE);
                        recyclerView2.setVisibility(GONE);
                    }else {
                        List<OrderModel> data = new ArrayList<>(results.values());
                        adapter = new OrderAdapter((ArrayList<OrderModel>) data, getContext(), getActivity());
                        recyclerView2.setAdapter(adapter);
                    }

                }catch (Exception e){
                    return;
                };
                hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        //el commission
        showProgressDialog();
        Query query3 = database.getReference("orders").orderByChild("sellerId").equalTo(uid);
        query3.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                HashMap<String, OrderModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                });
                try {

                    List<OrderModel> data2 = new ArrayList<>(results.values());

                    for (final OrderModel post : data2) {
                        if (post.getPaid().toString() == "false"&&post.getSellerConfirm()==true) {

                            Query query4 = database.getReference("stores").orderByChild("storeId").equalTo(uid);
                            query4.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                @Override
                                public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {

                                    try {
                                        HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {
                                        });
                                        List<StoreModel> data2 = new ArrayList<>(results.values());

                                        for (StoreModel post1 : data2) {

                                            int newBallance=Integer.parseInt(post1.getBallance())-Integer.parseInt(post.getCommission());
                                            DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                                            String key = nodeDataSnapshot.getKey();
                                            String path = "/" + snapshot.getKey() + "/" + key;
                                            HashMap<String, Object> result = new HashMap<>();
                                            result.put("ballance", String.valueOf(newBallance));
                                            database.getReference(path).updateChildren(result);

                                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                                            Query query6 = database.getReference("orders").orderByChild("pushId").equalTo(post.getPushId());
                                            query6.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                                @Override
                                                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                                                    // do some stuff once


                                                    DataSnapshot nodeDataSnapshot2 = snapshot.getChildren().iterator().next();
                                                    String key2 = nodeDataSnapshot2.getKey();
                                                    String path2 = "/" + snapshot.getKey() + "/" + key2;
                                                    HashMap<String, Object> result2 = new HashMap<>();
                                                    result2.put("paid", true);
                                                    database.getReference(path2).updateChildren(result2);
                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });

                                            Query query5 = database.getReference("stores").orderByChild("storeId").equalTo("nK6jgmDtCNP9sukfu6W44cCDKpJ3");
                                            query5.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                                @Override
                                                public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {

                                                    try {
                                                        HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {
                                                        });
                                                        List<StoreModel> data2 = new ArrayList<>(results.values());

                                                        for (StoreModel post1 : data2) {
                                                            DataSnapshot nodeDataSnapshot3 = snapshot.getChildren().iterator().next();
                                                            String key3 = nodeDataSnapshot3.getKey();
                                                            String path3 = "/" + "stores" + "/" + key3;
                                                            HashMap<String, Object> result3 = new HashMap<>();
                                                            int adminBallance=Integer.parseInt(post1.getBallance())+Integer.parseInt(post.getCommission());
                                                            result3.put("ballance", String.valueOf(adminBallance));
                                                            database.getReference(path3).updateChildren(result3);
                                                        }
                                                        }catch (Exception e){}

                                                }
                                                @Override
                                                public void onCancelled(DatabaseError databaseError) {

                                                }
                                            });
                                        }

                                    } catch (Exception e) {
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });


                        }
                    }

                }catch (Exception e){
                    return;
                };
                hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });




        Query query10 = database.getReference("old_orders").orderByChild("sellerId").equalTo(uid);
        query10.addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(final DataSnapshot dataSnapshot) {

                HashMap<String, OrderModel> results = dataSnapshot.getValue(new GenericTypeIndicator<HashMap<String, OrderModel>>() {
                });
                try {
                    List<OrderModel> data2 = new ArrayList<>(results.values());
                    for (final OrderModel post : data2) {
                        if (post.getPaid().toString() == "true"&&post.getBuyerConfirm()==true&&post.getSellerConfirm()==false&&post.getPaymentMethod().equals("Credit")) {

                            Query query4 = database.getReference("stores").orderByChild("storeId").equalTo(uid);
                            query4.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                @Override
                                public void onDataChange(final com.google.firebase.database.DataSnapshot snapshot) {

                                    try {
                                        HashMap<String, StoreModel> results = snapshot.getValue(new GenericTypeIndicator<HashMap<String, StoreModel>>() {
                                        });
                                        List<StoreModel> data2 = new ArrayList<>(results.values());
                                        for (StoreModel post1 : data2) {
                                            int newCreditBalance=Integer.parseInt(post1.getBallance())+Integer.parseInt(post.getPrPrice());
                                            DataSnapshot nodeDataSnapshot = snapshot.getChildren().iterator().next();
                                            String key = nodeDataSnapshot.getKey();
                                            String path = "/" + snapshot.getKey() + "/" + key;
                                            HashMap<String, Object> result = new HashMap<>();
                                            result.put("ballance", String.valueOf(newCreditBalance));
                                            database.getReference(path).updateChildren(result);
                                        }
                                    } catch (Exception e) {
                                    }
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {
                                }
                            });

                            final FirebaseDatabase database = FirebaseDatabase.getInstance();
                            Query query6 = database.getReference("old_orders").orderByChild("pushId").equalTo(post.getPushId());
                            query6.addListenerForSingleValueEvent(new com.google.firebase.database.ValueEventListener() {
                                @Override
                                public void onDataChange(com.google.firebase.database.DataSnapshot snapshot) {
                                    // do some stuff once
                                    DataSnapshot nodeDataSnapshot2 = snapshot.getChildren().iterator().next();
                                    String key2 = nodeDataSnapshot2.getKey();
                                    String path2 = "/" + snapshot.getKey() + "/" + key2;
                                    HashMap<String, Object> result2 = new HashMap<>();
                                    result2.put("sellerConfirm", true);
                                    database.getReference(path2).updateChildren(result2);
                                }
                                @Override
                                public void onCancelled(DatabaseError databaseError) {

                                }
                            });
                        }
                    }

                }catch (Exception e){
                    return;
                };
                hideProgressDialog();
            }
            @Override
            public void onCancelled(DatabaseError databaseError) {
            }
        });

        hideProgressDialog();
        return v;
    }


    @Override
    public void onAttach(Context context) {
        super.onAttach(context);

    }

    @Override
    public void onDetach() {
        super.onDetach();
    }



    public void showProgressDialog() {
        if (mProgressDialog == null) {
            mProgressDialog = new ProgressDialog(getContext());
            mProgressDialog.setMessage(getString(R.string.loading));
            mProgressDialog.setIndeterminate(true);
        }

        mProgressDialog.show();
    }

    public void hideProgressDialog() {
        if (mProgressDialog != null && mProgressDialog.isShowing()) {
            mProgressDialog.dismiss();
        }
    }
}
