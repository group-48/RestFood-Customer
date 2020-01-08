package com.dash.restfood_customer;

import android.content.Context;
import android.content.Intent;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dash.restfood_customer.models.OrderFood;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;
import com.google.firebase.firestore.local.IndexedQueryEngine;
import com.squareup.picasso.Picasso;

public class ViewOrderAdapter extends FirestoreRecyclerAdapter<OrderFood, ViewOrderAdapter.ViewOrderHolder> {

    private Context context;
        FirebaseFirestore db=FirebaseFirestore.getInstance();
        public ViewOrderAdapter(@NonNull FirestoreRecyclerOptions<OrderFood> options) {
            super(options);
        }

        @Override
        protected void onBindViewHolder(@NonNull final ViewOrderHolder viewOrderHolder, int i, @NonNull final OrderFood orderFood) {

            viewOrderHolder.tv_name.setText(orderFood.getName());
            viewOrderHolder.tv_price.setText(String.valueOf(orderFood.getPrice() * Integer.parseInt(orderFood.getQty())));
            //viewOrderHolder.btn_qty.setNumber(orderFood.getQty());
            Picasso.get().load(orderFood.getImage()).into(viewOrderHolder.iv_image);

            /*viewOrderHolder.btn_qty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {

                public  void  onValueChange(ElegantNumberButton view, int oldValue,int newValue){
                    Log.d("num",viewOrderHolder.btn_qty.getNumber());
                    db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("cart").document(orderFood.getFoodId()).update("qty",viewOrderHolder.btn_qty.getNumber());
                }


            });*/
            viewOrderHolder.btn_review.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View v) {

                    db.collection("reviews")
                            .whereEqualTo("foodId",orderFood.getFoodId())
                            .whereEqualTo("orderId",orderFood.getOrderId()).get().addOnSuccessListener(new OnSuccessListener<QuerySnapshot>() {
                        @Override
                        public void onSuccess(QuerySnapshot queryDocumentSnapshots) {
                            if(queryDocumentSnapshots.isEmpty()){
                                Log.d("select food for review","No Review");
                            }
                            else{
                                Log.d("select food for review","Review Exists");
                            }
                        }
                    });

                    Intent intent=new Intent(context,PlaceReview.class);
                    intent.putExtra("OrderId",orderFood.getOrderId());
                    intent.putExtra("FoodId",orderFood.getFoodId());
                    intent.putExtra("FoodName",orderFood.getName());
                    intent.putExtra("ShopId",orderFood.getShopId());

                    context.startActivity(intent);

                    Log.d("Place Review","Btn clicked");
                }
            });

        }

        @NonNull
        @Override
        public ViewOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
            context=parent.getContext();
            return new ViewOrderHolder(v);
        }

        class ViewOrderHolder extends RecyclerView.ViewHolder{

            TextView tv_name,tv_price;
            ImageView iv_image;
            Button btn_review;

            public ViewOrderHolder(@NonNull View itemView) {
                super(itemView);

                btn_review=itemView.findViewById(R.id.btn_review);
                tv_name=itemView.findViewById(R.id.tv_product_name);
                tv_price=itemView.findViewById(R.id.tv_product_price);
                //btn_qty=itemView.findViewById(R.id.btn_qty);
                iv_image=itemView.findViewById(R.id.iv_image);
            }
        }

    }



