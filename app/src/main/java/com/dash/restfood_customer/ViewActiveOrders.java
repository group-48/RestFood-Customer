package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dash.restfood_customer.models.Order;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class ViewActiveOrders extends BaseActivity {

    RecyclerView.LayoutManager layoutManager;
    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference orders=db.collection("orders");

    private OrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_view_active_orders, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        setUpRecyclerView();
    }

    private void setUpRecyclerView() {
        Query query=orders.whereEqualTo("User", FirebaseAuth.getInstance().getUid());
        Query query1=query.whereEqualTo("Done",false);

        FirestoreRecyclerOptions<Order> options=new FirestoreRecyclerOptions.Builder<Order>()
                .setQuery(query1,Order.class).build();
        adapter=new OrderAdapter(options);


        RecyclerView recyclerView=findViewById(R.id.active_orders_recycler);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                //Food obj=documentSnapshot.toObject(Food.class);
                //Toast.makeText(getApplicationContext(),obj.getName(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(ViewActiveOrders.this, TrackOrder.class);
                intent.putExtra("OrderId",documentSnapshot.get("OrderId").toString());
                //inta.putExtra("docId",documentSnapshot.getId());
                //inta.putExtra("shopdoc",docId);
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
