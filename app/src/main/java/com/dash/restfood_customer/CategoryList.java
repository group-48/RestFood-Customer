package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.LayoutInflater;
import android.view.View;

import com.dash.restfood_customer.models.Category;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;

import java.util.Objects;

public class CategoryList extends BaseActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    String shop;
    String id;
    //String id=db.collection("shop").document().getId();
    private CollectionReference ref;
    public MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_category_list, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        if(getIntent()!=null)
            //Intent catInt=getIntent();
            shop=getIntent().getStringExtra("shop");
            id=getIntent().getStringExtra("id");
            ref=db.collection("shop").document(id).collection("Category");
        if(!shop.isEmpty() && shop!=null){
            loadMenu(shop);
        }



    }

    private void loadMenu(String shop) {


          Query query=ref.orderBy("Name",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Category> options=new FirestoreRecyclerOptions.Builder<Category>().setQuery(query,Category.class).build();

        adapter=new MenuAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.recyclerView);
        recyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(CategoryList.this,1 );
        recyclerView.setLayoutManager(gridLayoutManager);




        recyclerView.setAdapter(adapter);


        adapter.setOnItemClickListener(new MenuAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Category obj=documentSnapshot.toObject(Category.class);
                //Toast.makeText(getApplicationContext(),obj.getName(),Toast.LENGTH_LONG).show();
                Intent intent=new Intent(CategoryList.this, FoodList.class);
                intent.putExtra("Category",obj.getName());
                intent.putExtra("docId",id);
                intent.putExtra("Browse",getIntent().getStringExtra("Browse"));
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

    public void onBackPressed() {
        if(Objects.equals(getIntent().getStringExtra("Browse"),"True")){
            Intent intent=new Intent(CategoryList.this, ShopList.class);
            startActivity(intent);
            finish();
        }
        else{
            Intent intent=new Intent(CategoryList.this, MainActivity.class);
            startActivity(intent);
            finish();
        }



    }

}
