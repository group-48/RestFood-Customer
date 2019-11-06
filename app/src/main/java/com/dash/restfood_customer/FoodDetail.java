
package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.media.Image;
import android.os.Bundle;
import android.util.Log;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.database.DataSnapshot;
import com.google.firebase.database.DatabaseError;
import com.google.firebase.database.DatabaseReference;
import com.google.firebase.database.FirebaseDatabase;
import com.google.firebase.database.ValueEventListener;
import com.google.firebase.firestore.CollectionReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.Query;
import com.google.firebase.firestore.QuerySnapshot;
import com.squareup.picasso.Picasso;

import java.util.List;


public class FoodDetail extends AppCompatActivity {

    TextView food_name, food_description, food_price;
    ImageView food_image, btnCart;

    String food;
    String docId;
    Food foodObj;

    FirebaseFirestore db = FirebaseFirestore.getInstance();
    private CollectionReference ref = db.collection("shop").document("dILfWEqZh7fN5LBtiWMFMoeCShe2").collection("FoodList");

    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_food_detail);


        //initialise view

        food_name = (TextView) findViewById(R.id.food_name);
        food_description = (TextView) findViewById(R.id.food_description);
        food_price=(TextView)findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.food_image);
        btnCart = (ImageView) findViewById(R.id.btn_cart);


        if (getIntent() != null)
            food = getIntent().getStringExtra("Food");
        docId = getIntent().getStringExtra("docId");
        Log.d("Nameof:", food);

        if (!food.isEmpty()) {
            getDetailFood(food);
        }

    }

    private void getDetailFood(final String food) {
        db.collection("shop")
                .document("dILfWEqZh7fN5LBtiWMFMoeCShe2")
                .collection("FoodList")
                .document(docId)
                .get()
                .addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                    @Override
                    public void onSuccess(DocumentSnapshot documentSnapshot) {
                        foodObj = documentSnapshot.toObject(Food.class);

                        food_name.setText(foodObj.getFoodName());
                        food_price.setText("LKR."+String.valueOf(foodObj.getPrice()));
                        food_description.setText(foodObj.getDescription());
                        Picasso.get().load(foodObj.getImage()).into(food_image);


                    }

                });
    }
}


