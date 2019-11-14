
package com.dash.restfood_customer;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.os.PersistableBundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dash.restfood_customer.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.squareup.picasso.Picasso;

import ViewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity
{

    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    String category;
    String docId;
    private  FoodAdapter adapter;
    private CollectionReference ref;

    @Override
    protected void onCreate( Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);
        if(getIntent()!=null)
        //Intent catInt=getIntent();
        category=getIntent().getStringExtra("Category");
        docId=getIntent().getStringExtra("docId");
        ref=db.collection("shop").document(docId).collection("FoodList");

        if(!category.isEmpty() && category!=null){
            loadListFood(category);
        }

    }

    private void loadListFood(String category) {

        Query query=ref.whereEqualTo("category",category).orderBy("foodName",Query.Direction.ASCENDING);

        FirestoreRecyclerOptions<Food>options=new FirestoreRecyclerOptions.Builder<Food>().setQuery(query,Food.class).build();
        adapter=new FoodAdapter(options);

        RecyclerView recyclerView=findViewById(R.id.recycler_food);
        recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
       recyclerView.setLayoutManager(layoutManager);


        recyclerView.setAdapter(adapter);

        adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
            @Override
            public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                Food obj=documentSnapshot.toObject(Food.class);
                //Toast.makeText(getApplicationContext(),obj.getName(),Toast.LENGTH_LONG).show();
                Intent inta=new Intent(FoodList.this, FoodDetail.class);
                inta.putExtra("Food",obj.getFoodName());
                inta.putExtra("docId",documentSnapshot.getId());
                inta.putExtra("shopdoc",docId);
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



