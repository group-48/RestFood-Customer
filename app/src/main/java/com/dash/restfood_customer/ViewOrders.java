package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.TextView;
import android.widget.Toast;

import com.dash.restfood_customer.models.OrderFood;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

public class ViewOrders extends BaseActivity {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference ref=db.collection("users").document(user.getUid()).collection("cart");

    private int tot;
    public int price;
    public int qty;
    private TextView total;
    public ViewOrderAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_view_orders, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        total=(TextView)findViewById(R.id.tot);

        loadOrders();
        getTotal();

    }

    private void loadOrders() {

        Query query=ref.orderBy("name",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<OrderFood> options=new FirestoreRecyclerOptions.Builder<OrderFood>().setQuery(query,OrderFood.class).build();
        Log.d("Working","fine");
        adapter=new ViewOrderAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.order_list );
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(ViewOrders.this,1);
        recyclerView.setLayoutManager(gridLayoutManager);

        recyclerView.setAdapter(adapter);
    }

    private void getTotal() {
        tot=0;
        ref
                .get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {



                                OrderFood obj=document.toObject(OrderFood.class);
                                tot=tot+(obj.getPrice()*Integer.valueOf(obj.getQty()));
                                Toast.makeText(getApplicationContext(),String.valueOf(tot), Toast.LENGTH_LONG).show();



                            }
                            total.setText("Rs:"+String.valueOf(tot));
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
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
