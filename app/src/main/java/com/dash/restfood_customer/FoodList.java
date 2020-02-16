
package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;

import com.dash.restfood_customer.models.Food;
import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentChange;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.EventListener;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QueryDocumentSnapshot;
import com.google.firebase.firestore.QuerySnapshot;
import com.mancj.materialsearchbar.MaterialSearchBar;

import java.util.ArrayList;
import java.util.List;

public class FoodList extends BaseActivity {

    RecyclerView.LayoutManager layoutManager;
    FirebaseFirestore db = FirebaseFirestore.getInstance();

    String category;
    String docId;
    RecyclerView recyclerView;
    private FoodAdapter adapter;
    private CollectionReference ref;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);


        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_food_list, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        if (getIntent() != null)
            //Intent catInt=getIntent();
            category = getIntent().getStringExtra("Category");
        docId = getIntent().getStringExtra("docId");
        ref = db.collection("shop").document(docId).collection("FoodList");

        if (!category.isEmpty() && category != null) {
            loadListFood(category);
        }
    }



    private void loadListFood (String category){

            Query query = ref.whereEqualTo("category", category).whereEqualTo("isAvailable",true).orderBy("foodName", Query.Direction.ASCENDING);

            FirestoreRecyclerOptions<Food> options = new FirestoreRecyclerOptions.Builder<Food>().setQuery(query, Food.class).build();
            adapter = new FoodAdapter(options);

            RecyclerView recyclerView = findViewById(R.id.recycler_food);
            recyclerView.setHasFixedSize(true);
            layoutManager = new LinearLayoutManager(this);
            recyclerView.setLayoutManager(layoutManager);

            adapter.startListening();
            recyclerView.setAdapter(adapter);

            adapter.setOnItemClickListener(new FoodAdapter.OnItemClickListener() {
                @Override
                public void onItemClick(DocumentSnapshot documentSnapshot, int position) {
                    Food obj = documentSnapshot.toObject(Food.class);

                    //Toast.makeText(getApplicationContext(),obj.getName(),Toast.LENGTH_LONG).show();
                    Intent intent = new Intent(FoodList.this, FoodDetail.class);
                    intent.putExtra("Food", obj.getFoodName());
                    intent.putExtra("docId", documentSnapshot.getId());
                    intent.putExtra("shopdoc", docId);
                    intent.putExtra("Browse", getIntent().getStringExtra("Browse"));
                    startActivity(intent);
                }
            });



        }
  
        @Override
        protected void onStart () {
            super.onStart();
            adapter.startListening();

        }

        @Override
        protected void onStop () {
            super.onStop();
            adapter.stopListening();


        }
    }




