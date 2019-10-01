package com.dash.restfood_customer;

import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.LinearLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.View;
import android.widget.Toast;

import com.dash.restfood_customer.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import ViewHolder.FoodViewHolder;

public class FoodList extends AppCompatActivity {

    RecyclerView recyclerView;
    RecyclerView.LayoutManager layoutManager;

    FirebaseDatabase database;
    DatabaseReference foodList;
    FirebaseRecyclerAdapter<Food, FoodViewHolder> firebaseRecyclerAdapter;

    String categoryId="";


    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_list);

        //firebase
        database=FirebaseDatabase.getInstance();
        foodList=database.getReference("Food");
        //foodList.keepSynced(true);

        recyclerView=(RecyclerView)findViewById(R.id.recycler_food);
         recyclerView.setHasFixedSize(true);
        layoutManager=new LinearLayoutManager(this);
        recyclerView.setLayoutManager(layoutManager);

        //Get the intent

        if (getIntent()!=null)
            categoryId=getIntent().getStringExtra("CategoryId");
        if (!categoryId.isEmpty() && categoryId!=null) {
            loadListFood(categoryId);
        }
}

    private void loadListFood(String categoryId) {
         firebaseRecyclerAdapter=new FirebaseRecyclerAdapter<Food, FoodViewHolder>(Food.class,
                 R.layout.menu_item,
                 FoodViewHolder.class,
                 foodList.orderByChild("MenuId").equalTo(categoryId)){
             @Override
             protected void populateViewHolder(FoodViewHolder viewHolder, Food model, int position) {

                 viewHolder.food_name.setText(model.getName());
                 Picasso.get().load(model.getImage()).into(viewHolder.food_image);

             final Food local=model;
             viewHolder.setItemClickListener(new ItemClickListener(){


                 @Override
                 public void onclick(View view, int position, boolean isLongClick) {
                     Intent foodDetail=new Intent(FoodList.this,FoodDetail.class);
                     foodDetail.putExtra("FoodId",firebaseRecyclerAdapter.getRef(position).getKey());
                     startActivity(foodDetail);
                 }
             });

             }
         };

         recyclerView.setAdapter(firebaseRecyclerAdapter);
         Log.d("TAG",""+firebaseRecyclerAdapter.getItemCount());
    }
    }
