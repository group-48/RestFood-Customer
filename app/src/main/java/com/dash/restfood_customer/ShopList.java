package com.dash.restfood_customer;

import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dash.restfood_customer.models.shop;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ShopList extends BaseActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference ref=db.collection("shop");
    public ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_shoplist, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        loadshoplist();
    }

    private void loadshoplist() {

        Query query=ref.orderBy("shopName",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<shop>options=new FirestoreRecyclerOptions.Builder<shop>().setQuery(query,shop.class).build();

        adapter=new ShopAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.recyclerShop);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(ShopList.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                shop obj=documentSnapshot.toObject(shop.class);
                Intent intent=new Intent(ShopList.this,ShopOption.class);
                intent.putExtra("shop",obj.getShop_id());
                intent.putExtra("id",documentSnapshot.getId());
                startActivity(intent);


            }
        });
    }

    @Override
    protected void onStart() {
        super.onStart();
        adapter.startListening();
    }

    @Override
    protected void onStop() {
        super.onStop();
        adapter.stopListening();
    }
}
