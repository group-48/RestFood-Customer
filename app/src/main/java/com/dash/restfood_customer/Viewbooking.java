package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;

import com.dash.restfood_customer.models.Reserve;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

public class Viewbooking extends BaseActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference ref=db.collection("reserve");
    public ReserveAdapter adapter;


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_viewbooking);

        loadreservation();
    }

    private void loadreservation() {

        Query query=ref.orderBy("date",Query.Direction.ASCENDING);
        FirestoreRecyclerOptions<Reserve>options=new FirestoreRecyclerOptions.Builder<Reserve>().setQuery(query,Reserve.class).build();

        adapter=new ReserveAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.recyclerbooking);
        recyclerView.setHasFixedSize(true);

        GridLayoutManager gridLayoutManager=new GridLayoutManager(Viewbooking.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);
        recyclerView.setAdapter(adapter);
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
