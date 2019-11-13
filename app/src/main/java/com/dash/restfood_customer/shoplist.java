package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;

import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class shoplist extends AppCompatActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference ref=db.collection("shop");
    public ShopAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_shoplist);

        loadshoplist();
    }

    private void loadshoplist() {

        Query query=ref.orderBy("shopName",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<shop>options=new FirestoreRecyclerOptions.Builder<shop>().setQuery(query,shop.class).build();

        adapter=new ShopAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.recyclerShop);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(shoplist.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new ShopAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {

                shop obj=documentSnapshot.toObject(shop.class);
                Intent inta=new Intent(shoplist.this,CategoryList.class);
                inta.putExtra("shop",obj.getShop_id());
                inta.putExtra("id",documentSnapshot.getId());
                startActivity(inta);


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
