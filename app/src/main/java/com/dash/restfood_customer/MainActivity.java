package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;

import com.dash.restfood_customer.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewHolder.MenuViewHolder;

public class MainActivity extends AppCompatActivity {

    RecyclerView mRecyclerView;
    FirebaseDatabase database;
    DatabaseReference mDatabase;
    FirebaseRecyclerAdapter <Category,MenuViewHolder> firebaseRecyclerAdapter;

    //List<Category> myFoodList;
    Category mFoodData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        database=FirebaseDatabase.getInstance();
        mDatabase= database.getReference("Category");
        mDatabase.keepSynced(true);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,1 );
        mRecyclerView.setLayoutManager(gridLayoutManager);

        loadMenu();


    }

    private void loadMenu() {

        firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<Category, MenuViewHolder>
                (Category.class,R.layout.recycler_row_item,MenuViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, Category model, int position) {
                    viewHolder.txtMenuName.setText(model.getName());
                    Picasso.get().load(model.getImage()).into(viewHolder.imageView);

                  final Category clickItem=model;

                 viewHolder.setItemClickListener(new ItemClickListener(){


                     @Override
                     public void onclick(View view, int position, boolean isLongClick) {
                         Intent foodList=new Intent(MainActivity.this,FoodList.class);
                         foodList.putExtra("CategoryId",firebaseRecyclerAdapter.getRef(position).getKey());
                         startActivity(foodList);
                     }
                 });
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);


    }



}
