package com.dash.restfood_customer;

import ViewHolder.CategoryViewHolder;
import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;

import com.firebase.ui.firestore.FirestoreRecyclerAdapter;
import com.firebase.ui.firestore.FirestoreRecyclerOptions;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.FirebaseFirestoreException;
import com.google.firebase.firestore.Query;

import com.squareup.picasso.Picasso;

public class FoodCategory extends AppCompatActivity {

    RecyclerView mRecyclerView;


    //FirebaseRecyclerAdapter<Category,CategoryViewHolder> firebaseRecyclerAdapter;

    FirestoreRecyclerAdapter <Category, CategoryViewHolder> firestoreRecyclerAdapter;
    FirebaseFirestore db=FirebaseFirestore.getInstance();
    private CollectionReference categoryRef=db.collection("Category");
    private Query query;


    //List<Category> myFoodList;
    Category mFoodData;

    @Override
    protected void onStart() {
        super.onStart();
        firestoreRecyclerAdapter.startListening();
    }

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_category);


        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(FoodCategory.this,1 );
        mRecyclerView.setLayoutManager(gridLayoutManager);

        query = categoryRef;

        loadMenu();


    }

    private void loadMenu() {

        /*firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Category, CategoryViewHolder>
                (Category.class,R.layout.food_item,CategoryViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(CategoryViewHolder viewHolder, Category model, int position) {
                    viewHolder.txtMenuName.setText(model.getName());
                    Picasso.get().load(model.getImage()).into(viewHolder.imageView);

                  final Category clickItem=model;

                 viewHolder.setItemClickListener(new ItemClickListener(){


                     @Override
                     public void onclick(View view, int position, boolean isLongClick) {
                         Intent foodList=new Intent(FoodCategory.this,FoodList.class);
                         foodList.putExtra("CategoryId",firebaseRecyclerAdapter.getRef(position).getKey());
                         startActivity(foodList);
                     }
                 });
            }
        };*/

        FirestoreRecyclerOptions<Category> options = new FirestoreRecyclerOptions.Builder<Category>()
                .setQuery(query, Category.class)
                .build();

        firestoreRecyclerAdapter = new FirestoreRecyclerAdapter<Category, CategoryViewHolder>(options) {
            @Override
            protected void onBindViewHolder(@NonNull CategoryViewHolder menuViewHolder, int i, @NonNull Category category) {
                menuViewHolder.txtMenuName.setText(category.getName());
                Picasso.get().load(category.getImage()).into(menuViewHolder.imageView);
                Log.d("Error",category.getName());
            }

            @NonNull
            @Override
            public CategoryViewHolder onCreateViewHolder(@NonNull ViewGroup parent, int viewType) {
                View view = LayoutInflater.from(parent.getContext()).inflate(R.layout.menu_item, parent, false);
                Log.d("Error","hiiiiiiiii");
                return new CategoryViewHolder(view);
            }

            @Override
            public void onError(FirebaseFirestoreException e) {
                Log.e("error", e.getMessage());
            }
        };





        mRecyclerView.setAdapter(firestoreRecyclerAdapter);


    }





}
