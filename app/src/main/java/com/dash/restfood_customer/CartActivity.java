package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.widget.Button;
import android.widget.TextView;

import com.dash.restfood_customer.models.CartItem;
import com.dash.restfood_customer.models.shop;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import org.w3c.dom.Text;

public class CartActivity extends BaseActivity {

    RecyclerView.LayoutManager layoutManager;
    RecyclerView recyclerView;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();
    private CollectionReference ref=db.collection("users").document(user.getUid()).collection("cart");

    private Button btn_checkout;
    private TextView tv_total;

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
}
