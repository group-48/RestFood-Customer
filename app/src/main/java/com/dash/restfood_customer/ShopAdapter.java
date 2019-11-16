package com.dash.restfood_customer;

import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import com.dash.restfood_customer.models.shop;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.DocumentSnapshot;
import com.squareup.picasso.Picasso;

public class ShopAdapter extends FirestoreRecyclerAdapter <shop, ShopAdapter.ShopHolder>{
    private OnItemClickListener listener;

    public ShopAdapter(@NonNull FirestoreRecyclerOptions<shop> options) {
        super(options);
    }

    @Override
    protected void onBindViewHolder(@NonNull ShopHolder shopHolder, int i, @NonNull shop shop) {

        shopHolder.shopName.setText(shop.getShopName());
        shopHolder.shopAddress.setText(shop.getShopAddress());
        Picasso.get().load(shop.getShopimage()).into(shopHolder.shopimage);

    }

    @NonNull
    @Override
    public ShopHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
        View v= LayoutInflater.from(parent.getContext()).inflate(R.layout.shopitem,parent,false);
        return new ShopHolder(v);
    }

    class ShopHolder extends RecyclerView.ViewHolder{

        ImageView shopimage;
        TextView shopName;
        TextView shopAddress;

        public ShopHolder( View itemView) {
            super(itemView);

            shopName=itemView.findViewById(R.id.shopname);
            shopimage=itemView.findViewById(R.id.shopimage);
            shopAddress=itemView.findViewById(R.id.shopaddress);

            itemView.setOnClickListener(new View.OnClickListener() {
                @Override
                public void onClick(View view) {
                    int position=getAdapterPosition();
                    if (position != RecyclerView.NO_POSITION && listener != null) {
                        listener.onItemClick(getSnapshots().getSnapshot(position), position);
                    }
                }
            });
        }
    }
    public  interface OnItemClickListener{
        void  onItemClick(DocumentSnapshot documentSnapshot, int position);
    }

    public  void setOnItemClickListener(OnItemClickListener listener){
        this.listener=listener;
    }


}
