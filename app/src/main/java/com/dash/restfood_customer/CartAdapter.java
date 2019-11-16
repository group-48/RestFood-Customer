package com.dash.restfood_customer;

import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;


import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.dash.restfood_customer.models.CartItem;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

public class CartAdapter extends FirestoreRecyclerAdapter<CartItem,CartAdapter.CartHolder> {

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    public CartAdapter(@NonNull FirestoreRecyclerOptions<CartItem> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull final CartHolder cartHolder, int i, @NonNull final CartItem cartItem) {
        cartHolder.tv_name.setText(cartItem.getName());
        Log.d("Price",String.valueOf(cartItem.getPrice()));
        cartHolder.tv_price.setText(String.valueOf(cartItem.getPrice()*Integer.parseInt(cartItem.getQty())));
        cartHolder.btn_qty.setNumber(cartItem.getQty());

        Picasso.get().load(cartItem.getImage()).into(cartHolder.iv_image);

        cartHolder.btn_qty.setOnValueChangeListener(new ElegantNumberButton.OnValueChangeListener() {
            @Override
            public void onValueChange(ElegantNumberButton view, int oldValue, int newValue) {
                Log.d("num",cartHolder.btn_qty.getNumber());
                db.collection("users").document(FirebaseAuth.getInstance().getUid()).collection("cart").document(cartItem.getFoodId()).update("qty",cartHolder.btn_qty.getNumber());
            }
        });

        cartHolder.btn_remove.setOnClickListener(new Button.OnClickListener() {
            @Override
            public void onClick(View v) {
                Log.d("Removed","tru");
                db.collection("users").document(FirebaseAuth.getInstance().getUid())
                        .collection("cart")
                        .document(cartItem.getFoodId()).delete().addOnSuccessListener(new OnSuccessListener<Void>() {
                    @Override
                    public void onSuccess(Void aVoid) {

                    }
                });
            }
        });
    }

    @NonNull
    @Override
    public CartHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.cart_item,parent,false);
        return new CartHolder(v);
    }

    public class CartHolder extends RecyclerView.ViewHolder {
        TextView tv_name,tv_price;
        ElegantNumberButton btn_qty;
        ImageView iv_image;
        Button btn_remove;

        public CartHolder(@NonNull View itemView) {
            super(itemView);

            tv_name=itemView.findViewById(R.id.tv_product_name);
            tv_price=itemView.findViewById(R.id.tv_product_price);
            btn_qty=itemView.findViewById(R.id.btn_qty);
            iv_image=itemView.findViewById(R.id.iv_image);
            btn_remove=itemView.findViewById(R.id.btn_remove);
        }
    }
}
