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

public class CategoryList extends AppCompatActivity {

    private FirebaseFirestore db=FirebaseFirestore.getInstance();
    String shop;
    String id;
    //String id=db.collection("shop").document().getId();
    private CollectionReference ref;
    public MenuAdapter adapter;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_category_list);

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
                Intent inta=new Intent(CategoryList.this, FoodList.class);
                inta.putExtra("Category",obj.getName());
                inta.putExtra("docId",id);
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
