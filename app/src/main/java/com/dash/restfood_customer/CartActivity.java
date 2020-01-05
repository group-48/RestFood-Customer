package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dash.restfood_customer.models.CartItem;
import com.dash.restfood_customer.models.shop;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;

import org.w3c.dom.Text;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class CartActivity extends BaseActivity implements View.OnClickListener {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference ref=db.collection("users").document(user.getUid()).collection("cart");

    private Button btn_checkout;
    private TextView tv_total;

    public CartAdapter adapter;
    int[] total = new int[1];
    List<String> food_list = new ArrayList<String>();
    public String[] shopId = new String[1];

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_cart, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        btn_checkout=(Button)findViewById(R.id.btn_checkout);
        tv_total=(TextView)findViewById(R.id.tv_total);

        btn_checkout.setOnClickListener(this);
        loadCart();
    }

    private void loadCart() {
        Query query=ref.orderBy("name",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<CartItem> options=new FirestoreRecyclerOptions.Builder<CartItem>().setQuery(query,CartItem.class).build();
        Log.d("Working","fine");
        adapter=new CartAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.cart_list);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager=new GridLayoutManager(CartActivity.this,1);
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

    @Override
    public void onClick(View v) {
        if(v==btn_checkout){

            db.collection("users").document(user.getUid()).collection("cart")
                    .get()
                    .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {
                        @Override
                        public void onComplete(@NonNull Task<QuerySnapshot> task) {
                            if (task.isSuccessful()) {
                                for (QueryDocumentSnapshot document : task.getResult()) {
                                    Log.d("CartActvity", document.getId() + " => " + document.getData());
                                    Log.d("CartActvity", document.getId() + " => " + document.get("foodId"));
                                    food_list.add(document.get("foodId").toString());
                                    total[0] += Integer.parseInt(document.get("price").toString());
                                    shopId[0] =document.get("shopId").toString();
                                }
                                String[] foods = new String[ food_list.size() ];
                                food_list.toArray( foods);

                                Log.d("CartActvity", shopId[0]);
                                Map<String,Object> order=new HashMap<>();
                                order.put("Total",total[0]);
                                order.put("Food_List", Arrays.asList(foods));
                                order.put("Date","");
                                order.put("Time","");
                                order.put("User",user.getUid());
                                order.put("Shop",shopId[0]);
                                order.put("Status","Pending");
                                order.put("Done",false);
                                db.collection("orders").add(order)
                                .addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                                    @Override
                                    public void onSuccess(DocumentReference documentReference) {
                                        final String docId=documentReference.getId();

                                    }
                                });
                                //db.collection("users").document(user.getUid()).collection("Orders").document(docId).set(order);

                            } else {
                                Log.d("CartActivity", "Error getting documents: ", task.getException());
                            }
                        }
                    });
            startActivity(new Intent(this,TrackOrder.class));

        }
    }
}
