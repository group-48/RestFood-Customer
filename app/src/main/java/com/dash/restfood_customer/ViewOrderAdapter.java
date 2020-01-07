package com.dash.restfood_customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dash.restfood_customer.models.OrderFood;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

public class ViewOrderAdapter extends FirestoreRecyclerAdapter<OrderFood, ViewOrderAdapter.ViewOrderHolder> {

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
        }

        @NonNull
        @Override
        public ViewOrderHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
            View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.order_item,parent,false);
            return new ViewOrderHolder(v);
        }

        class ViewOrderHolder extends RecyclerView.ViewHolder{

            TextView tv_name,tv_price;
            ElegantNumberButton btn_qty;
            ImageView iv_image;

            public ViewOrderHolder(@NonNull View itemView) {
                super(itemView);

                tv_name=itemView.findViewById(R.id.tv_product_name);
                tv_price=itemView.findViewById(R.id.tv_product_price);
                //btn_qty=itemView.findViewById(R.id.btn_qty);
                iv_image=itemView.findViewById(R.id.iv_image);
            }
        }

    }



