package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.widget.ImageView;
import android.widget.TextView;

import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.squareup.picasso.Picasso;

public class FoodDetail extends AppCompatActivity {

    TextView food_name,food_description,food_price;
    ImageView food_image,btnCart;

    String foodId="";

    FirebaseDatabase database;
    DatabaseReference foods;

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);

        //firebase
        database=FirebaseDatabase.getInstance();
        foods=database.getReference("Food");

        //initialise view

         food_name=(TextView)findViewById(R.id.food_name);
         food_description=(TextView)findViewById(R.id.food_description);
         food_price=(TextView)findViewById(R.id.food_price);
         food_image=(ImageView)findViewById(R.id.food_image);
         btnCart=(ImageView)findViewById(R.id.btn_cart);


         if (getIntent()!=null)
             foodId=getIntent().getStringExtra("FoodId");

         if (!foodId.isEmpty()){
             getDetailFood(foodId);
         }

    }

    private void getDetailFood(String foodId) {
        foods.child(foodId).addValueEventListener(new ValueEventListener() {
            @Override
            public void onDataChange(@NonNull DataSnapshot dataSnapshot) {
                Food food=dataSnapshot.getValue(Food.class);

                Picasso.get().load(food.getImage()).into(food_image);
                food_name.setText(food.getName());
                food_description.setText(food.getDescription());
                food_price.setText(food.getPrice());


            }

            @Override
            public void onCancelled(@NonNull DatabaseError databaseError) {

            }
        });

    }
}
