package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.app.AlertDialog;
import android.content.Context;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;
import android.widget.Toast;

import com.dash.restfood_customer.models.CartItem;
import com.dash.restfood_customer.models.OrderFood;
import com.dash.restfood_customer.models.shop;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FieldValue;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
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

    private static final String TAG = "CartActivity";
    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference ref=db.collection("users").document(user.getUid()).collection("cart");

    private Button btn_checkout;
    private TextView tv_total;
    private int tot;

    public CartAdapter adapter;


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
        getTotal();
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

    private void getTotal() {
        tot=0;
       /* ref.get()
                .addOnCompleteListener(new OnCompleteListener<QuerySnapshot>() {

                    @Override
                    public void onComplete(@NonNull Task<QuerySnapshot> task) {
                        if (task.isSuccessful()) {
                            for (QueryDocumentSnapshot document : task.getResult()) {
                                /*
                                price=Integer.valueOf((Integer) document.getData().get("price"));
                                Log.d(TAG, document.getId() + " => " + document.getData());
                                 qty=Integer.valueOf(String.valueOf(document.getData().get("qty")));


                                OrderFood obj=document.toObject(OrderFood.class);
                                tot=tot+(obj.getPrice()*Integer.valueOf(obj.getQty()));
                                Toast.makeText(getApplicationContext(),String.valueOf(tot), Toast.LENGTH_LONG).show();


                                //tv_total.setText(String.valueOf(document.getData().get("price")));
                            }
                            tv_total.setText("Total is:"+String.valueOf(tot));
                        } else {
                            //Log.d(TAG, "Error getting documents: ", task.getException());
                        }
                    }
                });*/

        ref.addSnapshotListener(new EventListener<QuerySnapshot>() {
            @Override
            public void onEvent(@Nullable QuerySnapshot value, @Nullable FirebaseFirestoreException e) {
                if (e != null) {
                    Log.w(TAG, "Listen failed.", e);
                    tv_total.setText("Empty");
                    return;
                }

                List<String> cities = new ArrayList<>();
                tot=0;
                for (QueryDocumentSnapshot doc : value) {

                    tot=tot+(Integer.valueOf(doc.get("price").toString())*Integer.valueOf(doc.get("qty").toString()));
                    Toast.makeText(getApplicationContext(),String.valueOf(tot), Toast.LENGTH_LONG).show();
                    tv_total.setText("Total is:"+String.valueOf(tot));
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

    @Override
    public void onClick(View v) {
        if(v==btn_checkout && tot!=0){
            startActivity(new Intent(this,ConfirmOrder.class)
                .putExtra("Total",String.valueOf(tot)));


        }
        else{
            android.app.AlertDialog.Builder builder = new android.app.AlertDialog.Builder(this);
            builder
                    .setCancelable(false)
                    .setNegativeButton("Ok", new DialogInterface.OnClickListener() {
                        public void onClick(DialogInterface dialog, int id) {
                            dialog.cancel();
                        }
                    });
            //Creating dialog box
            AlertDialog alert = builder.create();
            //Setting the title manually
            alert.setTitle("No items in cart to checkout");
            alert.show();
        }
    }
}
