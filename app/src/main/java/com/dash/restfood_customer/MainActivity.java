package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.recyclerview.widget.GridLayoutManager;
import androidx.recyclerview.widget.RecyclerView;

import android.content.Context;
import android.os.Bundle;
import android.view.View;
import android.view.ViewGroup;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.dash.restfood_customer.Interface.ItemClickListener;
import com.firebase.ui.database.FirebaseRecyclerAdapter;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.squareup.picasso.Picasso;

import java.util.ArrayList;
import java.util.List;
import java.util.Locale;

import ViewHolder.MenuViewHolder;

public class MainActivity extends AppCompatActivity {

   private RecyclerView mRecyclerView;
   private DatabaseReference mDatabase;

    //List<FoodData> myFoodList;
    FoodData mFoodData;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);
        mDatabase= FirebaseDatabase.getInstance().getReference().child("Category");
        mDatabase.keepSynced(true);

        mRecyclerView=(RecyclerView)findViewById(R.id.recyclerView);
        mRecyclerView.setHasFixedSize(true);
        GridLayoutManager gridLayoutManager = new GridLayoutManager(MainActivity.this,1 );
        mRecyclerView.setLayoutManager(gridLayoutManager);

        loadMenu();


    }

    private void loadMenu() {

        FirebaseRecyclerAdapter<FoodData, MenuViewHolder> firebaseRecyclerAdapter =new FirebaseRecyclerAdapter<FoodData, MenuViewHolder>
                (FoodData.class,R.layout.recycler_row_item,MenuViewHolder.class, mDatabase) {
            @Override
            protected void populateViewHolder(MenuViewHolder viewHolder, FoodData model, int position) {
                    viewHolder.txtMenuName.setText(model.getName());
                    Picasso.get().load(model.getImage()).into(viewHolder.imageView);

                  final  FoodData clickItem=model;
                    viewHolder.setItemClickListener(new ItemClickListener() {
                        @Override
                        public void onclick(View view, int position,boolean isLongClick) {

                            Toast.makeText(MainActivity.this, ""+clickItem.getName(), Toast.LENGTH_SHORT).show();

                        }
                    });
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);

    }

   /* @Override
    protected void onStart() {
        super.onStart();
        FirebaseRecyclerAdapter<FoodData, FoodDataViewHolder> firebaseRecyclerAdapter = new FirebaseRecyclerAdapter<FoodData, FoodDataViewHolder>
                (FoodData.class,R.layout.recycler_row_item,FoodDataViewHolder.class,mDatabase) {
            @Override
            protected void populateViewHolder(FoodDataViewHolder viewHolder, FoodData model, int position) {
                    viewHolder.setName(model.getName());
                    viewHolder.setImage(model.getImage());

                    final FoodData clickItem=model;
                    viewHolder.setI
            }
        };

        mRecyclerView.setAdapter(firebaseRecyclerAdapter);
    }

    public static class FoodDataViewHolder extends RecyclerView.ViewHolder {
        View mView;


        public FoodDataViewHolder( View itemView) {
            super(itemView);
            mView=itemView;


        }

        public void setName(String name){
            TextView tvTitle=(TextView)mView.findViewById(R.id.tvTitle);
            tvTitle.setText(name);

        }

        public void setImage(String image){

            ImageView ivImage=(ImageView)mView.findViewById(R.id.ivImage);
            Picasso.get().load(image).into(ivImage);

        }
    }

*/

}
