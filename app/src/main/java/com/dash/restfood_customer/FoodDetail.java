
package com.dash.restfood_customer;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.webkit.WebView;
import android.widget.Button;
import android.widget.ImageView;
import android.widget.TextView;
import android.widget.Toast;

import com.cepheuen.elegantnumberbutton.view.ElegantNumberButton;
import com.dash.restfood_customer.models.CartItem;
import com.dash.restfood_customer.models.Food;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnFailureListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.squareup.picasso.Picasso;


public class FoodDetail extends BaseActivity implements View.OnClickListener {

    TextView food_name, food_description, food_price;
    ImageView food_image, btn_Cart;
    ElegantNumberButton et_qty;
    Button btn_review;

    String food;
    String docId;
    String shopDoc;
    Food foodObj;

    FirebaseFirestore db = FirebaseFirestore.getInstance();


    @Override
    protected void onCreate(Bundle savedInstanceState) {

        super.onCreate(savedInstanceState);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_food_detail, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end


        //initialise view

        food_name = (TextView) findViewById(R.id.food_name);
        food_description = (TextView) findViewById(R.id.food_description);
        food_price=(TextView)findViewById(R.id.food_price);
        food_image = (ImageView) findViewById(R.id.food_image);
        btn_Cart = (ImageView) findViewById(R.id.btn_cart);
        et_qty= findViewById(R.id.eb_qty);
        et_qty.setNumber("1");
        btn_review=findViewById(R.id.btn_reviews);

        if (getIntent() != null)
        {
            food = getIntent().getStringExtra("Food");
            docId = getIntent().getStringExtra("docId");
            shopDoc=getIntent().getStringExtra("shopdoc");


            Log.d("Nameof:", food);

        }


        if (!food.isEmpty()) {
            getDetailFood(food);
        }

        btn_Cart.setOnClickListener(this);
        btn_review.setOnClickListener(this);

    }

    private void getDetailFood(final String food) {
        db.collection("shop")
                .document(shopDoc)
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

    @Override
    public void onClick(View v) {
        if(v==btn_Cart){
            Log.d("Clicked","yes");

            final CartItem cartItem=new CartItem(foodObj.getFoodName(),foodObj.getImage(),getIntent().getStringExtra("shopdoc"),getIntent().getStringExtra("docId"),et_qty.getNumber(),foodObj.getPrice());

            db.collection("users")
                    .document(FirebaseAuth.getInstance().getUid())
                    .collection("cart")
                    .document(cartItem.getFoodId()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    if(documentSnapshot.exists()){
                        Toast.makeText(FoodDetail.this,"Item Already in Cart",Toast.LENGTH_LONG).show();
                    }
                    else {
                        db.collection("users")
                                .document(FirebaseAuth.getInstance().getUid())
                                .collection("cart")
                                .document(cartItem.getFoodId())
                                .set(cartItem).addOnCompleteListener(new OnCompleteListener<Void>() {
                            @Override
                            public void onComplete(@NonNull Task<Void> task) {
                                Toast.makeText(FoodDetail.this,"Item Added to Cart",Toast.LENGTH_LONG).show();
                            }
                        });
                    }

                }
            });




        }
        else if(v==btn_review){
            Intent intent=new Intent(FoodDetail.this,ViewReviews.class);
            intent.putExtra("queryId",food);
            startActivity(intent);
        }
    }
}


