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
import android.widget.Button;
import android.widget.EditText;
import android.widget.RatingBar;
import android.widget.Switch;
import android.widget.TextView;

import com.dash.restfood_customer.models.Review;
import com.google.android.gms.tasks.OnCompleteListener;
import com.google.android.gms.tasks.OnSuccessListener;
import com.google.android.gms.tasks.Task;
import com.google.firebase.auth.FirebaseAuth;
import com.google.firebase.auth.FirebaseUser;
import com.google.firebase.firestore.DocumentReference;
import com.google.firebase.firestore.DocumentSnapshot;
import com.google.firebase.firestore.FirebaseFirestore;
import com.google.firebase.firestore.QuerySnapshot;

public class PlaceReview extends BaseActivity implements View.OnClickListener {
    private static final String TAG = "PlaceReview";
    private String OrderId,FoodId,ShopId,FoodName,Comments,Name;
    Boolean displayName;
    private Float Rating;
    RatingBar rb_review;
    Button btn_review;
    TextView tv_foodName;
    EditText et_comments;
    Switch sw_name;

    FirebaseFirestore db=FirebaseFirestore.getInstance();
    FirebaseUser user= FirebaseAuth.getInstance().getCurrentUser();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_place_review);

        //inflate begin
        LayoutInflater inflater = (LayoutInflater) this.getSystemService(Context.LAYOUT_INFLATER_SERVICE);
        //inflate your activity layout here!
        @SuppressLint("InflateParams")
        View contentView = inflater.inflate(R.layout.activity_place_review, null, false);
        drawerLayout.addView(contentView, 0);
        //inflate end

        rb_review=findViewById(R.id.rb_review);
        btn_review=findViewById(R.id.btn_review);
        tv_foodName=findViewById(R.id.tv_foodName);
        et_comments=findViewById(R.id.et_comments);
        sw_name=findViewById(R.id.sw_name);


        if(getIntent()!=null){
            OrderId=getIntent().getStringExtra("OrderId");
            FoodId=getIntent().getStringExtra("FoodId");
            FoodName=getIntent().getStringExtra("FoodName");
            ShopId=getIntent().getStringExtra("ShopId");
            if(getIntent().getStringExtra("Edit")!=null){
                Log.d(TAG,"Edit review ");
                loadReview();
            }
        }

        tv_foodName.setText("How was your \n"+FoodName+" ?");

        btn_review.setOnClickListener(this);

    }

    private void loadReview() {
        String reviewId=getIntent().getStringExtra("ReviewId");
        db.collection("reviews").document(reviewId).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
            @Override
            public void onSuccess(DocumentSnapshot documentSnapshot) {
                et_comments.setText(documentSnapshot.getString("comments"));
                rb_review.setRating(Float.valueOf(documentSnapshot.get("rating").toString()));
                if(documentSnapshot.get("userName")!=null){
                    sw_name.setChecked(true);
                }

            }
        });
    }

    private void placeReview() {
        Comments=et_comments.getText().toString();
        Rating=rb_review.getRating();
        displayName=sw_name.isChecked();

        Log.d(TAG,"Review: "+Comments+" Rating "+Rating+" display "+displayName);

        if(displayName==true){
            db.collection("users").document(user.getUid()).get().addOnSuccessListener(new OnSuccessListener<DocumentSnapshot>() {
                @Override
                public void onSuccess(DocumentSnapshot documentSnapshot) {
                    Name=documentSnapshot.get("fName").toString();
                }
            });
        }
        else{
            Name="Anonymous";
        }

        //db.collection("shop").document(ShopId).collection("FoodList").document(FoodId).set();
        Review review=new Review(FoodId,FoodName,ShopId,Comments,Name,user.getUid(),Rating,OrderId);
        if(getIntent().getStringExtra("Edit")!=null){
            db.collection("reviews").document(getIntent().getStringExtra("ReviewId")).set(review);
            db.collection("reviews").document(getIntent().getStringExtra("ReviewId")).update("reviewId",getIntent().getStringExtra("ReviewId"));
        }
        else{
            db.collection("reviews").add(review).addOnSuccessListener(new OnSuccessListener<DocumentReference>() {
                @Override
                public void onSuccess(DocumentReference documentReference) {
                    db.collection("reviews").document(documentReference.getId()).update("reviewId",documentReference.getId());
                    Log.d(TAG,"Done");
                }
            });
        }


    }

    @Override
    public void onClick(View v) {
        if(v==btn_review){
            placeReview();
            Intent intent=new Intent(PlaceReview.this,MainActivity.class);
            startActivity(intent);
        }
    }


}
